/*

 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cole.crop;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;


import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.WeakHashMap;

// DataManager manages all media sets and media items in the system.
//
// Each MediaSet and MediaItem has a unique 64 bits id. The most significant
// 32 bits represents its parent, and the least significant 32 bits represents
// the self id. For MediaSet the self id is is globally unique, but for
// MediaItem it's unique only relative to its parent.
//
// To make sure the id is the same when the MediaSet is re-created, a child key
// is provided to obtainSetId() to make sure the same self id will be used as
// when the parent and key are the same. A sequence of child keys is called a
// path. And it's used to identify a specific media set even if the process is
// killed and re-created, so child keys should be stable identifiers.

public class DataManager /*implements StitchingChangeListener*/ {
    //
    public static final int INCLUDE_ALL = 1;// INCLUDE_LOCAL_ALL
    public static final int INCLUDE_IMAGE = 2;// INCLUDE_LOCAL_IMAGE
    public static final int INCLUDE_VIDEO = 3;// INCLUDE_LOCAL_VIDEO

    public static final int INCLUDE_HCTCLOUD_ALL = 4;
    public static final int INCLUDE_HCTCLOUD_IMAGE = 5;
    public static final int INCLUDE_HCTCLOUD_VIDEO = 6;

    public static final int INCLUDE_LOCAL_ALL_AND_NEWALBUM = 7;
    public static final int INCLUDE_HCTCLOUD_ALL_AND_NEWALBUM = 8;

    public static final int SOURCE_TYPE_LOCAL = 1;
    public static final int SOURCE_TYPE_HCTCLOUD = 2;

    public static final int SHOW_ALL = 1;
    public static final int SHOW_DISPLAYED = 2;
    public static final int SHOW_HIDEN = 3;

    // Any one who would like to access data should require this lock
    // to prevent concurrency issue.
    public static final Object LOCK = new Object();

    public static DataManager from(Context context) {
        GalleryApp app = (GalleryApp) context.getApplicationContext();
        return app.getDataManager();
    }

    private static final String TAG = "DataManager";

    // This is the path for the media set seen by the user at top level.

    private static final String TOP_LOCAL_SET_PATH = "/local/all";
    private static final String TOP_LOCAL_IMAGE_SET_PATH = "/local/image";
    private static final String TOP_LOCAL_VIDEO_SET_PATH = "/local/video";

    private static final String TOP_CLOUD_SET_PATH = "/hctcloud/all";
    private static final String TOP_CLOUD_IMAGE_SET_PATH = "/hctcloud/image";
    private static final String TOP_CLOUD_VIDEO_SET_PATH = "/hctcloud/video";

    private static final String TOP_LOCAL_AND_NEWALBUM_PATH = "/combo/{/local/all,/newalbum/all}";
    private static final String TOP_HCTCLOUD_AND_NEWALBUM_PATH = "/combo/{/hctcloud/all,/newalbum/all}";

    private static final String TOP_LOCAL_DISPLAYED_SET_PATH = "/local/displayed/all";
    private static final String TOP_LOCAL_DISPLAYED_IMAGE_SET_PATH = "/local/displayed/image";
    private static final String TOP_LOCAL_DISPLAYED_VIDEO_SET_PATH = "/local/displayed/video";

    private static final String TOP_LOCAL_HIDEN_SET_PATH = "/local/hiden/all";
    private static final String TOP_LOCAL_HIDEN_IMAGE_SET_PATH = "/local/hiden/image";
    private static final String TOP_LOCAL_HIDEN_VIDEO_SET_PATH = "/local/hiden/video";

    public static final Comparator<MediaItem> sDateTakenComparator = new DateTakenComparator();

    private static class DateTakenComparator implements Comparator<MediaItem> {
        @Override
        public int compare(MediaItem item1, MediaItem item2) {
            return -Utils.compare(item1.getDateInMs(), item2.getDateInMs());
        }
    }

    private final Handler mDefaultMainHandler;

    private GalleryApp mApplication;
    private int mActiveCount = 0;


    private HashMap<String, MediaSource> mSourceMap = new LinkedHashMap<String, MediaSource>();

    private int mSourceType = SOURCE_TYPE_LOCAL;

    private boolean mIsGetContent = false;

    public DataManager(GalleryApp application) {
        mApplication = application;
        mDefaultMainHandler = new Handler(application.getMainLooper());
    }

    public synchronized void switchSourceMap(int sourceType) {

        if (mSourceType == sourceType)
            return;

        // for (MediaSource source : mSourceMap.values())
        // {
        // source.pause();
        // }
        mSourceType = sourceType;
        // mSourceMap.clear();
        // initializeSourceMap();

    }

    public synchronized void initializeSourceMap() {
        if (!mSourceMap.isEmpty())
            return;

        // the order matters, the UriSource must come last


        if (mActiveCount > 0) {
            for (MediaSource source : mSourceMap.values()) {
                source.resume();
            }
        }

    }

    public String getTopSetPath(int typeBits) {
        return getTopSetPath(typeBits, DataManager.SHOW_DISPLAYED);
    }

    public void setGetContent(boolean isGetContent) {

        mIsGetContent = isGetContent;
    }

    public String getTopSetPath(int typeBits, int displayOption) {
        if (mIsGetContent) {
            mIsGetContent = false;
            return getLocalTopSetPath(typeBits, displayOption);
        }
        if (typeBits == INCLUDE_ALL) {
            if (displayOption == DataManager.SHOW_ALL) {
                return TOP_LOCAL_SET_PATH;
            } else if (displayOption == DataManager.SHOW_DISPLAYED) {
                return TOP_LOCAL_DISPLAYED_SET_PATH;
            } else if (displayOption == DataManager.SHOW_HIDEN) {
                return TOP_LOCAL_HIDEN_SET_PATH;
            }
        } else if (typeBits == INCLUDE_IMAGE) {
            if (displayOption == DataManager.SHOW_ALL) {
                return TOP_LOCAL_IMAGE_SET_PATH;
            } else if (displayOption == DataManager.SHOW_DISPLAYED) {
                return TOP_LOCAL_DISPLAYED_IMAGE_SET_PATH;
            } else if (displayOption == DataManager.SHOW_HIDEN) {
                return TOP_LOCAL_HIDEN_IMAGE_SET_PATH;
            }
        } else if (typeBits == INCLUDE_VIDEO) {
            if (displayOption == DataManager.SHOW_ALL) {
                return TOP_LOCAL_VIDEO_SET_PATH;
            } else if (displayOption == DataManager.SHOW_DISPLAYED) {
                return TOP_LOCAL_DISPLAYED_VIDEO_SET_PATH;
            } else if (displayOption == DataManager.SHOW_HIDEN) {
                return TOP_LOCAL_HIDEN_VIDEO_SET_PATH;
            }
        } else if (typeBits == INCLUDE_HCTCLOUD_ALL) {
            return TOP_CLOUD_SET_PATH;
        } else if (typeBits == INCLUDE_LOCAL_ALL_AND_NEWALBUM) {
            return TOP_LOCAL_AND_NEWALBUM_PATH;
        } else if (typeBits == INCLUDE_HCTCLOUD_ALL_AND_NEWALBUM) {
            return TOP_HCTCLOUD_AND_NEWALBUM_PATH;
        }
        throw new IllegalArgumentException();

    }

    private String getLoaclAndNewalbumSetPath(int typeBits) {
        // TODO Auto-generated method stub
        return TOP_LOCAL_AND_NEWALBUM_PATH;
    }

    private String getHctcloudAndNewalbumSetPath(int typeBits) {
        // TODO Auto-generated method stub
        return TOP_HCTCLOUD_AND_NEWALBUM_PATH;
    }

    private String getHctCloudTopSetPath(int typeBits) {
        // TODO Auto-generated method stub
        return TOP_CLOUD_SET_PATH;
    }

    public String getLocalTopSetPath(int typeBits, int showOptions) {
        if (typeBits == INCLUDE_ALL) {
            if (showOptions == DataManager.SHOW_ALL) {
                return TOP_LOCAL_SET_PATH;
            } else if (showOptions == DataManager.SHOW_DISPLAYED) {
                return TOP_LOCAL_DISPLAYED_SET_PATH;
            } else if (showOptions == DataManager.SHOW_HIDEN) {
                return TOP_LOCAL_HIDEN_SET_PATH;
            }
        } else if (typeBits == INCLUDE_IMAGE) {
            if (showOptions == DataManager.SHOW_ALL) {
                return TOP_LOCAL_IMAGE_SET_PATH;
            } else if (showOptions == DataManager.SHOW_DISPLAYED) {
                return TOP_LOCAL_DISPLAYED_IMAGE_SET_PATH;
            } else if (showOptions == DataManager.SHOW_HIDEN) {
                return TOP_LOCAL_HIDEN_IMAGE_SET_PATH;
            }
        } else if (typeBits == INCLUDE_VIDEO) {
            if (showOptions == DataManager.SHOW_ALL) {
                return TOP_LOCAL_VIDEO_SET_PATH;
            } else if (showOptions == DataManager.SHOW_DISPLAYED) {
                return TOP_LOCAL_DISPLAYED_VIDEO_SET_PATH;
            } else if (showOptions == DataManager.SHOW_HIDEN) {
                return TOP_LOCAL_HIDEN_VIDEO_SET_PATH;
            }
        }

        throw new IllegalArgumentException();

    }

    // open for debug
    void addSource(MediaSource source) {
        if (source == null)
            return;
        mSourceMap.put(source.getPrefix(), source);
    }

    // A common usage of this method is:
    // synchronized (DataManager.LOCK) {
    // MediaObject object = peekMediaObject(path);
    // if (object == null) {
    // object = createMediaObject(...);
    // }
    // }
    public MediaObject peekMediaObject(Path path) {
        return path.getObject();
    }

    public MediaObject getMediaObject(Path path) {
        synchronized (LOCK) {
            if (null == path || path.equals("")) {
                return null;
            }
            MediaObject obj = path.getObject();
            if (obj != null)
                return obj;

            MediaSource source = mSourceMap.get(path.getPrefix());
            if (source == null) {
                Log.w(TAG, "cannot find media source for path: " + path);
                return null;
            }

            try {
                MediaObject object = source.createMediaObject(path);
                if (object == null) {
                    Log.w(TAG, "cannot create media object: " + path);
                }
                return object;
            } catch (Throwable t) {
                Log.w(TAG, "exception in creating media object: " + path, t);
                return null;
            }
        }
    }

    public MediaObject getMediaObject(String s) {
        return getMediaObject(Path.fromString(s));
    }

    public MediaSet getMediaSet(Path path) {
        return (MediaSet) getMediaObject(path);
    }

    public MediaSet getMediaSet(String s) {
        return (MediaSet) getMediaObject(s);
    }

    public MediaSet[] getMediaSetsFromString(String segment) {
        String[] seq = Path.splitSequence(segment);
        int n = seq.length;
        MediaSet[] sets = new MediaSet[n];
        for (int i = 0; i < n; i++) {
            sets[i] = getMediaSet(seq[i]);
        }
        return sets;
    }

    // Maps a list of Paths to MediaItems, and invoke consumer.consume()
    // for each MediaItem (may not be in the same order as the input list).
    // An index number is also passed to consumer.consume() to identify
    // the original position in the input list of the corresponding Path (plus
    // startIndex).
    public void mapMediaItems(ArrayList<Path> list, MediaSet.ItemConsumer consumer,
                              int startIndex) {
        HashMap<String, ArrayList<MediaSource.PathId>> map = new HashMap<String, ArrayList<MediaSource.PathId>>();
        HashMap<String, ArrayList<Path>> pathMap = new HashMap<String, ArrayList<Path>>();
        // Group the path by the prefix.
        int n = list.size();
        for (int i = 0, j = 0; i < n; i++) {
            Path path = list.get(i);
            String prefix = path.getPrefix();
            ArrayList<MediaSource.PathId> group = map.get(prefix);
            ArrayList<Path> groupPath = pathMap.get(prefix);
            if (group == null) {
                group = new ArrayList<MediaSource.PathId>();
                map.put(prefix, group);
                groupPath = new ArrayList<Path>();
                pathMap.put(prefix, groupPath);
            }
            if (!groupPath.contains(path)) {
                groupPath.add(path);
                group.add(new MediaSource.PathId(path, j + startIndex));
                j++;
            }

        }

        // For each group, ask the corresponding media source to map it.
        for (Entry<String, ArrayList<MediaSource.PathId>> entry : map.entrySet()) {
            String prefix = entry.getKey();
            MediaSource source = mSourceMap.get(prefix);
            source.mapMediaItems(entry.getValue(),  null);
        }
    }



    // The following methods forward the request to the proper object.
    public int getSupportedOperations(Path path) {
        return getMediaObject(path).getSupportedOperations();
    }

    public void getPanoramaSupport(Path path, MediaObject.PanoramaSupportCallback callback) {
        getMediaObject(path).getPanoramaSupport(callback);
    }

    public void delete(Path path) {
        getMediaObject(path).delete();
    }

    /**
     * add by yujun for bug 617003105936
     */
    public void delete(Path path, ThreadPool.JobContext jc) {
        if (LockstateReceiver.isScreenLock) {
            int count = LockstateReceiver.getLockPhotoCount();
            count--;
            LockstateReceiver.setLockPhotoCount(count);
        }
        MediaObject obj = getMediaObject(path);

    }


    public Intent getEncryptIntent(Path path) {
        MediaObject object = getMediaObject(path);
        return object.getEncryptIntent();
    }

    public void rename(Path path, String name) {
        MediaObject object = getMediaObject(path);
        object.rename(name);
    }

    public void rotate(Path path, int degrees) {
        getMediaObject(path).rotate(degrees);
    }

    public void unRefreshRotate(Path path, int degrees) {
        getMediaObject(path).unRefreshRotate(degrees);
    }

    public void addTags(Path path, Vector<String> tags) {
        getMediaObject(path).addTags(tags);
    }

    public void copyOrMoveTo(Path path, String destCopyOrMovePath,
                             boolean isMove) {
        MediaObject obj = getMediaObject(path);
        if (obj instanceof MediaItem) {
            ((MediaItem) obj).copyOrMoveTo(destCopyOrMovePath, isMove);
        }

    }

    public void copyOrMoveTo(Path path, String destCopyOrMovePath,
                             boolean isMove, boolean isRename) {
        MediaObject obj = getMediaObject(path);
        if (obj instanceof MediaItem) {
            ((MediaItem) obj).copyOrMoveTo(destCopyOrMovePath, isMove, isRename);
        }

    }


    public Uri getContentUri(Path path) {
        return getMediaObject(path).getContentUri();
    }

    public int getMediaType(Path path) {
        return getMediaObject(path).getMediaType();
    }

    public Path findPathByUri(Uri uri, String type) {
        if (uri == null)
            return null;
        for (MediaSource source : mSourceMap.values()) {
            Path path = source.findPathByUri(uri, type);
            if (path != null)
                return path;
        }
        return null;
    }

    public Path getDefaultSetOf(Path item) {
        MediaSource source = mSourceMap.get(item.getPrefix());
        return source == null ? null : source.getDefaultSetOf(item);
    }

    // Returns number of bytes used by cached pictures currently downloaded.
    public long getTotalUsedCacheSize() {
        long sum = 0;
        for (MediaSource source : mSourceMap.values()) {
            sum += source.getTotalUsedCacheSize();
        }
        return sum;
    }

    // Returns number of bytes used by cached pictures if all pending
    // downloads and removals are completed.
    public long getTotalTargetCacheSize() {
        long sum = 0;
        for (MediaSource source : mSourceMap.values()) {
            sum += source.getTotalTargetCacheSize();
        }
        return sum;
    }



    public void resume() {
        if (++mActiveCount == 1) {
            for (MediaSource source : mSourceMap.values()) {
                source.resume();
            }
        }
    }

    public void pause() {
        if (--mActiveCount == 0) {
            for (MediaSource source : mSourceMap.values()) {
                source.pause();
            }
        }
    }






}
