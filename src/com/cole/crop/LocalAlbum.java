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

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Files.FileColumns;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Video;
import android.provider.MediaStore.Video.VideoColumns;
import android.text.TextUtils;
import android.widget.Gallery;

import com.gome.beautymirror.R;

// LocalAlbumSet lists all media items in one bucket on local storage.
// The media items need to be all images or all videos, but not both.
 public class LocalAlbum /*extends MediaSet*/ {
//	public static final int TYPE_IMAGE = 1;
//	public static final int TYPE_VIDEO = 2;
//	public static final int TYPE_ALL = 3;
//	private static final String TAG = "LocalAlbum";
//	private static final String[] COUNT_PROJECTION = { "count(*)" };
//
//	private static final int INVALID_COUNT = -1;
//	private final String mWhereClause;
//	private/* final */String mOrderClause;
//	private String mWhereClausePlus;
//	private final Uri mBaseUri;
//	private final String[] mProjection;
//
//	private final GalleryApp mApplication;
//	private final ContentResolver mResolver;
//	private final int mBucketId;
//	private String mName;
//	// private final Path mItemPath;
//	private int mCachedCount = INVALID_COUNT;
//	private final int mType;
//	private final boolean mExpand;
//	private boolean mIsIncSmallFile = false;
//	private String sdDataPath = null;
//	private String mOrderByName = " title COLLATE LOCALIZED ";
//	private String mOrderByDate = ImageColumns.DATE_MODIFIED + " DESC, " + ImageColumns.DATE_TAKEN + " DESC,"
//			+ ImageColumns._ID + " DESC";
//
//	// private static LoadLocationTask mLoadLocationTask;
//
////	Settings settings = null;
////	private final String mWhereClauseByPhotoFilter;
//
//	public LocalAlbum(Path path, GalleryApp application, int bucketId, int type, String name, boolean expand) {
//		super(path, nextVersionNumber());
//		mApplication = application;
//		mResolver = application.getContentResolver();
//		mBucketId = bucketId;
//		mExpand = expand;
//		mName = name;
//		mType = type;
//		// mLoadLocationTask = LoadLocationTask.getLoadLocationTask();
//
//		mBaseUri = Files.getContentUri("external");
//
//		if (Gallery.sAlbumPageOrderbyType == Constants.ORDER_BY_DATE) {
//			mOrderClause = mOrderByDate;
//		} else {
//			mOrderClause = mOrderByName;// hxh
//		}
//
//		mProjection = LocalMediaItem.PROJECTION;
//		String temWhere = null;
//
//		StorageManager manager=(StorageManager)application.getAndroidContext().getSystemService(Context.STORAGE_SERVICE);
//		sdDataPath= StorageManagerHelper.getExternalStoragePath(manager);
//
//        if (mType == TYPE_IMAGE) {
//            temWhere = FileColumns.MEDIA_TYPE + "="
//                    + FileColumns.MEDIA_TYPE_IMAGE + " AND (("
//                    + ImageColumns.BUCKET_ID + " = ? or ("
//                    + ImageColumns.BUCKET_ID + " = ? )))";
//
//            mNotifier = new ChangeNotifier(this,
//                    Images.Media.EXTERNAL_CONTENT_URI, application);
//
//        } else if (mType == TYPE_VIDEO) {
//            temWhere = "(" + FileColumns.MEDIA_TYPE + "="
//                    + FileColumns.MEDIA_TYPE_VIDEO
//                    // +
//                    // " AND (image_type is null or image_type!='livephoto')) AND "
//                    + " ) AND ((" + VideoColumns.BUCKET_ID + " = ? or ("
//                    + VideoColumns.BUCKET_ID + " = ? )))";
//
//            mNotifier = new ChangeNotifier(this,
//                    Video.Media.EXTERNAL_CONTENT_URI, application);
//        } else if (mType == TYPE_ALL) {
//            temWhere = "(" + FileColumns.MEDIA_TYPE + "="
//                    + FileColumns.MEDIA_TYPE_IMAGE + " or ("
//                    + FileColumns.MEDIA_TYPE
//                    + "="
//                    + FileColumns.MEDIA_TYPE_VIDEO
//                    // + " AND (image_type is null or image_type!='livephoto')))
//                    // AND "
//                    + " )) AND ((" + ImageColumns.BUCKET_ID + " = ? ) or ("
//                    + ImageColumns.BUCKET_ID + " = ? ))";
//
//            mNotifier = new ChangeNotifier(this, new Uri[] {
//                    Images.Media.EXTERNAL_CONTENT_URI,
//                    Video.Media.EXTERNAL_CONTENT_URI }, application);
//
//        } else {
//			throw (new RuntimeException("Unkown local type:" + mType));
//		}
//		if (EncryptionManager.isShowNormalAndPrivacy(EncryptionManager.getAppContext())) {
//			temWhere = temWhere + EncryptionManager.getShowNormalAndPrivacyWherePlus(true);
//		}
//		if (UserManagerUtil.isPrvateMode()) {
//			temWhere = temWhere + EncryptionManager.getPrivacyWherePlus(true);
//		}
//		mWhereClause = temWhere;
//        if (!mExpand && GalleryConfig.isEnableBurstGroup()) {
//            mWhereClausePlus = " AND (group_id is null or group_id = 0 OR _id IN (SELECT _id FROM files WHERE group_id!=0 AND (bucket_id = ? or bucket_id = ?)GROUP BY group_id))";
//        } else {
//            mWhereClausePlus = null;
//        }
//		// mWhereClauseByPhotoFilter = " and (width > 320 and height > 320 and
//		// mime_type is not \"image/gif\") ";
////		mWhereClauseByPhotoFilter = " and ( _size > 20480 )";
//	}
//
//	public LocalAlbum(Path path, GalleryApp application, int bucketId, int type, boolean expand) {
//		this(path, application, bucketId, type, BucketHelper.getBucketName(application.getContentResolver(), bucketId),
//				expand);
//	}
//
//	public LocalAlbum(Path path, GalleryApp application, int bucketId, int type, boolean expand, boolean isIncGif) {
//		this(path, application, bucketId, type, BucketHelper.getBucketName(application.getContentResolver(), bucketId),
//				expand);
//		mIsIncSmallFile = isIncGif;
//	}
//
//	@Override
//	public boolean isCameraRoll() {
//		return mBucketId == PresetBucket.getPrimaryCameraBucketId();
//	}
//
//	@Override
//	public Uri getContentUri() {
//		if (mType == TYPE_IMAGE) {
//			return Images.Media.EXTERNAL_CONTENT_URI.buildUpon()
//					.appendQueryParameter(LocalSource.KEY_BUCKET_ID, String.valueOf(mBucketId)).build();
//		} else if (mType == TYPE_VIDEO) {
//			return Video.Media.EXTERNAL_CONTENT_URI.buildUpon()
//					.appendQueryParameter(LocalSource.KEY_BUCKET_ID, String.valueOf(mBucketId)).build();
//		} else if (mType == TYPE_ALL) {
//			return Files.getContentUri("external").buildUpon()
//					.appendQueryParameter(LocalSource.KEY_BUCKET_ID, String.valueOf(mBucketId)).build();
//		} else {
//			Log.e("zhy", "Invalid local type:" + mType);
//			return null;
//		}
//	}
//
//	public String getQueryClause() {
//		String tmpClause = null;
//
//		if (mWhereClausePlus != null && mType != TYPE_VIDEO && isCameraRoll())
//			tmpClause = mWhereClause + mWhereClausePlus;
//		else
//			tmpClause = mWhereClause;
//
//		if (EncryptionManager.isShowNormalAndPrivacy(EncryptionManager.getAppContext())) {
//			if (tmpClause == null) {
//				tmpClause = tmpClause + EncryptionManager.getShowNormalAndPrivacyWherePlus(true);
//			} else if (!tmpClause.contains("userId")) {
//				tmpClause = tmpClause + EncryptionManager.getShowNormalAndPrivacyWherePlus(true);
//			}
//		}
//		if (UserManagerUtil.isPrvateMode()) {
//			if (tmpClause == null) {
//				tmpClause = tmpClause + EncryptionManager.getPrivacyWherePlus(true);
//			} else if (!tmpClause.contains("userId")) {
//				tmpClause = tmpClause + EncryptionManager.getPrivacyWherePlus(true);
//			}
//		}
//		return tmpClause;
//	}
//
//    public String[] getSelectionArgs() {
//        if (mWhereClausePlus != null && mType != TYPE_VIDEO && isCameraRoll()) {
//            if (mBucketId == PresetBucket.getPrimaryCameraBucketId()) {
//                return new String[] {
//                        String.valueOf(mBucketId),
//                        String.valueOf(GalleryUtils.getBucketId(sdDataPath
//                                + "/" + BucketNames.CAMERA)),
//                        String.valueOf(mBucketId),
//                        String.valueOf(GalleryUtils.getBucketId(sdDataPath
//                                + "/" + BucketNames.CAMERA)) };
//            } else {
//                return new String[] { String.valueOf(mBucketId),
//                        String.valueOf(mBucketId), String.valueOf(mBucketId) };
//            }
//
//        } else {
//            if (mBucketId == PresetBucket.getPrimaryCameraBucketId()) {
//                return new String[] {
//                        String.valueOf(mBucketId),
//                        String.valueOf(GalleryUtils.getBucketId(sdDataPath
//                                + "/" + BucketNames.CAMERA)) };
//            } else {
//                return new String[] { String.valueOf(mBucketId),
//                        String.valueOf(mBucketId) };
//            }
//        }
//
//    }
//
//	// private ArrayList<MediaItem> mMediaData = new ArrayList<MediaItem>();
//
//	/*
//	 * @Override public ArrayList<MediaItem> getMediaItem(int start, int count)
//	 * { return getMediaItem(); ArrayList<MediaItem> list = new
//	 * ArrayList<MediaItem>();
//	 *
//	 * int max; int end = start + count; int size = mMediaData.size();
//	 *
//	 * if(size == 0){ return list; }
//	 *
//	 * if(end > size){ max = size; } else { max = end; }
//	 *
//	 *
//	 *
//	 * for(int i = start; i<max; i++){ list.add(mMediaData.get(i)); } //return
//	 * mMediaData;//list; }
//	 */
//
//	@Override
//	public ArrayList<MediaItem> getMediaItemByGroupId(int start, int count, long groupId) {
//		DataManager dataManager = mApplication.getDataManager();
//		Uri uri = mBaseUri.buildUpon().appendQueryParameter("limit", start + "," + count).build();
//
//		ArrayList<MediaItem> list = new ArrayList<MediaItem>();
//		mOrderClause = mOrderByDate;
//
//		String groupBy = " AND  group_id=? ";
////		settings = ((GalleryApp) mApplication.getAndroidContext()).getSettings();
////		if (!mIsIncSmallFile && settings.isPhotoFilter())
////			groupBy = " AND (_size > 20480) " + groupBy;
//
//		String where = mWhereClause + groupBy;
//
//		if (EncryptionManager.isShowNormalAndPrivacy(EncryptionManager.getAppContext())) {
//			where = EncryptionManager.getShowNormalAndPrivacyWherePlus(false) + where;
//		}
//		if (UserManagerUtil.isPrvateMode()) {
//			where = EncryptionManager.getPrivacyWherePlus(false) + where;
//		}
//		Cursor cursor = mResolver.query(uri, mProjection, where,
//				new String[] { String.valueOf(mBucketId), String.valueOf(groupId), }, mOrderClause);
//
//		if (cursor == null) {
//			Log.w(TAG, "query fail: " + uri);
//			return list;
//		}
//
//		try {
//			while (cursor.moveToNext()) {
//
//				MediaItem item = loadOrUpdateItem(cursor, dataManager, mApplication);
//				if (item == null) {
//					Log.e("zhy", "Failed to load media item." + cursor.getString(LocalMediaItem.INDEX_DATA));
//					continue;
//				}
//				list.add(item);
//			}
//		} finally {
//			cursor.close();
//		}
//		return list;
//	}
//
//	@Override
//	public ArrayList<MediaItem> getMediaItem(int start, int count) {
//		Log.d(TAG, "classx.start=" + start + " count=" + count);
//		// mMediaData.clear();
//		mCallDataVersion = mDataVersion;
//
//		DataManager dataManager = mApplication.getDataManager();
//		Uri uri = mBaseUri.buildUpon().appendQueryParameter("limit", start + "," + count).build();
//
//		ArrayList<MediaItem> list = new ArrayList<MediaItem>();
//		// GalleryUtils.assertNotInRenderThread();
//		if (Gallery.sAlbumPageOrderbyType == Constants.ORDER_BY_DATE) {
//			mOrderClause = mOrderByDate;
//		} else {
//			mOrderClause = mOrderByName;
//		}
//
//		Cursor cursor = mResolver.query(uri, mProjection, getQueryClause(), getSelectionArgs(), mOrderClause);
//
//		if (cursor == null) {
//			Log.w(TAG, "query fail: " + mBaseUri);
//			return null;
//		}
//		Log.w(TAG, "cursor count-->" + cursor.getCount() + " mProjection-->" + mProjection);
//		try {
//			while (cursor.moveToNext()) {
//
//				MediaItem item = loadOrUpdateItem(cursor, dataManager, mApplication);
//				if (item == null) {
//					Log.e("zhy", "Failed to load media item." + cursor.getString(LocalMediaItem.INDEX_DATA));
//					continue;
//				}
//
//				if (LockstateReceiver.isScreenLock) {
//					String path = item.getFilePath();
//					Log.i(TAG, "LockstateReceiver path = " + path);
//					Log.i(TAG, "LockstateReceiver getLockPhotoCount = " + LockstateReceiver.getLockPhotoCount()
//							+ " list.size = " + list.size());
//					if (LockstateReceiver.getLockPhotoCount() == list.size()) {
//						break;
//					}
//				}
//                    list.add(item);
//			}
//		} finally {
//			cursor.close();
//		}
//		Log.i(TAG, "LockstateReceiver list.size = " + list.size());
//		return list;
//	}
//
//	private static MediaItem loadOrUpdateItem(Cursor cursor, DataManager dataManager, GalleryApp app) {
//		int type = cursor.getInt(LocalMediaItem.INDEX_MEDIATYPE);
//		int id = cursor.getInt(LocalMediaItem.INDEX_ID);
//		Path path;
//		boolean isImage;
//		if (type == FileColumns.MEDIA_TYPE_IMAGE) {
//			path = LocalImage.ITEM_PATH.getChild(id);
//			isImage = true;
//		} else if (type == FileColumns.MEDIA_TYPE_VIDEO) {
//			path = LocalVideo.ITEM_PATH.getChild(id);
//			isImage = false;
//		} else {
//			Log.e("zhy", "Unkown media type when load or update itme:" + type);
//			return null;
//		}
//		synchronized (DataManager.LOCK) {
//			LocalMediaItem item = (LocalMediaItem) dataManager.peekMediaObject(path);
//			if (item == null) {
//				if (isImage) {
//					item = new LocalImage(path, app, cursor);
//				} else {
//					item = new LocalVideo(path, app, cursor);
//				}
//
//				// double[] latLong = new double[2];
//				// item.getLatLong(latLong);
//				// mLoadLocationTask.addTask(latLong[0], latLong[1]);
//			} else {
//				item.updateContent(cursor);
//			}
//			return item;
//		}
//	}
//
//	// The pids array are sorted by the (path) id.
//	public static MediaItem[] getMediaItemById(GalleryApp application, ArrayList<Integer> ids) {
//		// get the lower and upper bound of (path) id
//		MediaItem[] result = new MediaItem[ids.size()];
//		if (ids.isEmpty())
//			return result;
//		int idLow = ids.get(0);
//		int idHigh = ids.get(ids.size() - 1);
//
//		// prepare the query parameters
//		Uri baseUri;
//		String[] projection;
//		Path itemPath;
//
//		baseUri = Files.getContentUri("external");
//		projection = LocalMediaItem.PROJECTION;
//		String where = "(" + FileColumns.MEDIA_TYPE + "=" + FileColumns.MEDIA_TYPE_IMAGE + " or "
//				+ FileColumns.MEDIA_TYPE + "=" + FileColumns.MEDIA_TYPE_VIDEO + ") AND _id BETWEEN ? AND ?";
//
//		ContentResolver resolver = application.getContentResolver();
//		DataManager dataManager = application.getDataManager();
//
//		if (EncryptionManager.isShowNormalAndPrivacy(EncryptionManager.getAppContext())) {
//			where = EncryptionManager.getShowNormalAndPrivacyWherePlus(false) + where;
//		}
//		if (UserManagerUtil.isPrvateMode()) {
//			where = EncryptionManager.getPrivacyWherePlus(false) + where;
//		}
//		Cursor cursor = resolver.query(baseUri, projection, where,
//				new String[] { String.valueOf(idLow), String.valueOf(idHigh) }, "_id");
//
//		if (cursor == null) {
//			Log.w(TAG, "query fail" + baseUri);
//			return result;
//		}
//		try {
//			int n = ids.size();
//			int i = 0;
//
//			while (i < n && cursor.moveToNext()) {
//				int id = cursor.getInt(0); // _id must be in the first column
//
//				// Match id with the one on the ids list.
//				if (ids.get(i) > id) {
//					continue;
//				}
//
//				while (ids.get(i) < id) {
//					if (++i >= n) {
//						return result;
//					}
//				}
//
//				MediaItem item = loadOrUpdateItem(cursor, dataManager, application);
//				result[i] = item;
//				++i;
//			}
//			return result;
//		} finally {
//			cursor.close();
//		}
//	}
//
//	public static MediaItem getMediaItemById(GalleryApp application, int imageID) {
//		Uri baseUri;
//		String[] projection;
//
//		MediaItem item = null;
//
//		baseUri = Files.getContentUri("external");
//		projection = LocalMediaItem.PROJECTION;
//		String where = "(" + FileColumns.MEDIA_TYPE + "=" + FileColumns.MEDIA_TYPE_IMAGE + " or "
//				+ FileColumns.MEDIA_TYPE + "=" + FileColumns.MEDIA_TYPE_VIDEO + ") AND _id = ?";
//
//		ContentResolver resolver = application.getContentResolver();
//		DataManager dataManager = application.getDataManager();
//		if (EncryptionManager.isShowNormalAndPrivacy(EncryptionManager.getAppContext())) {
//			where = EncryptionManager.getShowNormalAndPrivacyWherePlus(false) + where;
//		}
//		if (UserManagerUtil.isPrvateMode()) {
//			where = EncryptionManager.getPrivacyWherePlus(false) + where;
//		}
//
//		Cursor cursor = resolver.query(baseUri, projection, where, new String[] { String.valueOf(imageID) }, "_id");
//
//		if (cursor == null) {
//			Log.w(TAG, "query fail" + baseUri);
//			return item;
//		}
//
//		try {
//			while (cursor.moveToNext()) {
//				item = loadOrUpdateItem(cursor, dataManager, application);
//				break;
//			}
//		} finally {
//			cursor.close();
//		}
//
//		return item;
//	}
//
//	public static LinkedHashMap<String, MediaItem> getAllFirstImageItems(GalleryApp application) {
//
//		LinkedHashMap<String, MediaItem> firstImageItems = new LinkedHashMap<String, MediaItem>();
//
//		Uri baseUri;
//        /*modify for bug 10048 fix NullPointerException liuxiaoshuan 20171205 start*/
//        if(null == LocalMediaItem.PROJECTION){
//            Log.d(TAG, " LocalMediaItem.PROJECTION = null , getAllFirstImageItems return empty");
//            return firstImageItems;
//        }
//        /*modify for bug 10048 fix NullPointerException liuxiaoshuan 20171205 end*/
//		String[] projection = new String[LocalMediaItem.PROJECTION.length];
//
//		for (int i = 0; i < LocalMediaItem.PROJECTION.length; i++) {
//
//			if (i == 5) {
//				projection[5] = "MAX(datetaken)";
//			} else {
//				projection[i] = LocalMediaItem.PROJECTION[i];
//			}
//		}
//
//		MediaItem item = null;
//		baseUri = Files.getContentUri("external");
//
//		String where = "(" + FileColumns.MEDIA_TYPE + "=" + FileColumns.MEDIA_TYPE_IMAGE + " or ("
//				+ FileColumns.MEDIA_TYPE + "=" + FileColumns.MEDIA_TYPE_VIDEO
//				// + " AND (image_type is null or image_type!='livephoto') )))
//				// GROUP BY (bucket_id";
//				+ " ))) GROUP BY (bucket_id";
//		Settings settings = ((GalleryApp) application.getAndroidContext()).getSettings();
//
////		if (settings.isPhotoFilter()) {
////			where = " ( _size > 20480) and " + where;
////		}
//
//		String mOrderByDate = ImageColumns.DATE_MODIFIED + " DESC, " + ImageColumns.DATE_TAKEN + " DESC,"
//	            + ImageColumns._ID + " DESC";
//
//		ContentResolver resolver = application.getContentResolver();
//		DataManager dataManager = application.getDataManager();
//		if (EncryptionManager.isShowNormalAndPrivacy(EncryptionManager.getAppContext())) {
//			where = EncryptionManager.getShowNormalAndPrivacyWherePlus(false) + where;
//		}
//		if (UserManagerUtil.isPrvateMode()) {
//			where = EncryptionManager.getPrivacyWherePlus(false) + where;
//		}
//
//		Cursor cursor = resolver.query(baseUri, projection, where, null, mOrderByDate);
//
//		if (cursor == null) {
//			Log.w(TAG, "query fail" + baseUri);
//			return firstImageItems;
//		}
//
//		try {
//			while (cursor.moveToNext()) {
//				item = loadOrUpdateItem(cursor, dataManager, application);
//				firstImageItems.put(String.valueOf(item.getBucketId()), item);
//			}
//		} finally {
//			cursor.close();
//		}
//
//		return firstImageItems;
//	}
//
//	public static MediaItem getFirstImageItem(GalleryApp application, int bucket_id) {
//		Uri baseUri;
//		String[] projection;
//
//		MediaItem item = null;
//
//		baseUri = Files.getContentUri("external");
//		projection = LocalMediaItem.PROJECTION;
//
//		String where = "(" + FileColumns.MEDIA_TYPE + "=" + FileColumns.MEDIA_TYPE_IMAGE + " or "
//				+ FileColumns.MEDIA_TYPE + "=" + FileColumns.MEDIA_TYPE_VIDEO
//				// + ") AND (image_type is null or image_type!='livephoto') AND
//				// bucket_id = ?";
//				+ ") AND ((is_encrypt = 0 or (is_encrypt = 2))) "+" AND bucket_id = ?";
//		Settings settings = ((GalleryApp) application.getAndroidContext()).getSettings();
//
////		if (settings.isPhotoFilter()) {
////			where = " ( _size > 20480) and " + where;
////		}
//
//		String mOrderByDate = ImageColumns.DATE_MODIFIED+ " DESC, " + ImageColumns.DATE_TAKEN  + " DESC,"
//                + ImageColumns._ID + " DESC";
//
//		ContentResolver resolver = application.getContentResolver();
//		DataManager dataManager = application.getDataManager();
//		if (EncryptionManager.isShowNormalAndPrivacy(EncryptionManager.getAppContext())) {
//			where = EncryptionManager.getShowNormalAndPrivacyWherePlus(false) + where;
//		}
//		if (UserManagerUtil.isPrvateMode()) {
//			where = EncryptionManager.getPrivacyWherePlus(false) + where;
//		}
//		Cursor cursor = resolver.query(baseUri, projection, where, new String[] { String.valueOf(bucket_id) },
//				mOrderByDate);
//
//		if (cursor == null) {
//			Log.w(TAG, "query fail" + baseUri);
//			return item;
//		}
//
//		try {
//			while (cursor.moveToNext()) {
//				item = loadOrUpdateItem(cursor, dataManager, application);
//				break;
//			}
//		} finally {
//			cursor.close();
//		}
//
//		return item;
//	}
//
//	public static Cursor getItemCursor(ContentResolver resolver, Uri uri, String[] projection, int id) {
//		String where = "_id=?";
//		if (EncryptionManager.isShowNormalAndPrivacy(EncryptionManager.getAppContext())) {
//			where += EncryptionManager.SELECT_PLUS;
//		}
//		if (UserManagerUtil.isPrvateMode()) {
//			where = EncryptionManager.getPrivacyWherePlus(false) + where;
//		}
//		return resolver.query(uri, projection, where, new String[] { String.valueOf(id) }, null);
//	}
//
//	private static String getParent(String filePath) {
//		int lastSlash = filePath.lastIndexOf('/');
//		if (lastSlash > 0) {
//			return filePath.substring(0, lastSlash);
//		}
//
//		return filePath;
//	}
//
//	public void include() {
//		String albumPath = getBucketFilePath();
//		if (albumPath == null) {
//			Log.e("zhy", "Could not find album path.");
//			return;
//		}
//		try {
//			PropertyProvider provider = mApplication.getPropertyProvider();
//			provider.displayAlbum(albumPath);
//			ContentValues values = new ContentValues();
//			values.put(ImageColumns.DESCRIPTION, "Include by Gallery @" + System.currentTimeMillis());
//			String where = ImageColumns.BUCKET_ID + "=" + mBucketId;
//			mApplication.getContentResolver().update(Images.Media.EXTERNAL_CONTENT_URI, values, where, null);
//			mApplication.getContentResolver().update(Video.Media.EXTERNAL_CONTENT_URI, values, where, null);
//		} catch (Exception e) {
//			Log.e("zhy", "Exception :" + e);
//		} finally {
//		}
//	}
//
//	public void exclude() {
//		Cursor cursor = null;
//		String albumPath = null;
//		try {
//			Uri uri = Files.getContentUri("external").buildUpon().appendQueryParameter("limit", 0 + "," + 1).build();
//			;
//			String[] projection = new String[] { FileColumns._ID, FileColumns.DATA };
//			String where = "bucket_id=" + mBucketId + " AND (" + FileColumns.MEDIA_TYPE + "="
//					+ FileColumns.MEDIA_TYPE_IMAGE + " OR " + FileColumns.MEDIA_TYPE + "="
//					+ FileColumns.MEDIA_TYPE_VIDEO + ")";
//			if (EncryptionManager.isShowNormalAndPrivacy(EncryptionManager.getAppContext())) {
//				where = where + EncryptionManager.getShowNormalAndPrivacyWherePlus(true);
//			}
//			if (UserManagerUtil.isPrvateMode()) {
//				where = where + EncryptionManager.getPrivacyWherePlus(true);
//			}
//			cursor = mResolver.query(uri, projection, where, null, null);
//			if (cursor == null) {
//				Log.w(TAG, "query fail");
//				return;
//			}
//			if (cursor.getCount() < 1) {
//				Log.e("zhy", "Cound not find album with bucketId:" + mBucketId);
//				return;
//			}
//			cursor.moveToFirst();
//			String filePath = cursor.getString(1);
//			albumPath = getParent(filePath);
//
//		} finally {
//			if (cursor != null)
//				cursor.close();
//		}
//
//		if (albumPath == null) {
//			Log.e("zhy", "Could not find album path.");
//			return;
//		}
//		try {
//			PropertyProvider provider = mApplication.getPropertyProvider();
//			provider.hideAlbum(albumPath);
//			ContentValues values = new ContentValues();
//			values.put(ImageColumns.DESCRIPTION, "Exclude by Gallery @" + System.currentTimeMillis());
//			String where = ImageColumns.BUCKET_ID + "=" + mBucketId;
//			mApplication.getContentResolver().update(Images.Media.EXTERNAL_CONTENT_URI, values, where, null);
//			mApplication.getContentResolver().update(Video.Media.EXTERNAL_CONTENT_URI, values, where, null);
//		} catch (Exception e) {
//			Log.e("zhy", "Exception :" + e);
//		} finally {
//			cursor.close();
//		}
//	}
//
//	@Override
//	public int getMediaItemCount() {
//		Cursor cursor = null;
//
//		if (LockstateReceiver.isScreenLock) {
//			String where = mWhereClause;
//			if (EncryptionManager.isShowNormalAndPrivacy(EncryptionManager.getAppContext())) {
//				where = where + EncryptionManager.getShowNormalAndPrivacyWherePlus(true);
//			}
//			if (UserManagerUtil.isPrvateMode()) {
//				where = where + EncryptionManager.getPrivacyWherePlus(true);
//			}
//			cursor = mResolver.query(mBaseUri, COUNT_PROJECTION, where, new String[] { String.valueOf(mBucketId) },
//					null);
//		} else {
//			cursor = mResolver.query(mBaseUri, COUNT_PROJECTION, getQueryClause(), getSelectionArgs(), null);
//		}
//
//		if (cursor == null) {
//			Log.w(TAG, "classx.query fail");
//			return 0;
//		}
//		try {
//			Utils.assertTrue(cursor.moveToNext());
//			mCachedCount = cursor.getInt(0);
//
//		} finally {
//			cursor.close();
//		}
//
//		if (LockstateReceiver.isScreenLock) {
//			if (mBucketId == PresetBucket.getPrimaryCameraBucketId()) {
//				Log.i(TAG, "mCachedCount = " + mCachedCount + " cameraTotalCount" + LockstateReceiver.cameraTotalCount
//						+ "  mBucketId:" + mBucketId);
//				if (mCachedCount >= LockstateReceiver.lockPhotoCount) {
//					int count = LockstateReceiver.lockPhotoCount;
//					LockstateReceiver.setLockPhotoCount(count);
//					return count;
//				}
//			} else {
//				Log.i(TAG, "mCachedCount = " + mCachedCount + " 3DcameraTotalCount"
//						+ LockstateReceiver.threeDcameraTotalCount + "  mBucketId:" + mBucketId);
//				if (mCachedCount >= LockstateReceiver.lockPhotoCount) {
//					int count = LockstateReceiver.lockPhotoCount;
//					LockstateReceiver.setLockPhotoCount(count);
//					return count;
//				}
//			}
//
//        }
//        Log.w(TAG, "mCachedCount-->" + mCachedCount);
//        if (mCachedCount < 0) {
//            mCachedCount = 0;
//        }
//		return mCachedCount;
//	}
//
//	@Override
//	public String getName() {
//		return getLocalizedName(mApplication.getResources(), mBucketId, mName);
//	}
//
//	public static String PhoneRootDirectory = "/storage/emulated/0";
//	public static String SdCardRootDirectory = "/storage";
//
//	public String getName(String filePath) {
//		String directroy = getFileDirectroy(filePath);
//
//		if (directroy != null) {
//			if (mName == null || mName.length() == 0) {
//				mName = getDirectroyName(directroy);
//			}
//
//			if (PhoneRootDirectory.equalsIgnoreCase(directroy)) {
//				return mApplication.getResources().getString(R.string.phone_root_directroy);
//			} else {
//				directroy = getFileDirectroy(directroy);
//
//				if (directroy != null) {
//					if (SdCardRootDirectory.equalsIgnoreCase(directroy)) {
//						return mApplication.getResources().getString(R.string.sdcard_root_directroy);
//					}
//				}
//			}
//			return getLocalizedName(mApplication.getResources(), mBucketId, mName);
//		} else {
//			Log.d(TAG, "directroy is null");
//			Log.d(TAG, "filePath:" + filePath);
//			return "";
//		}
//
//	}
//
//	private String getFileDirectroy(String path) {
//		String directroy = null;
//		if (path != null) {
//			int index = path.lastIndexOf(File.separator);
//
//			if (index > 0) {
//				directroy = path.substring(0, index);
//			}
//		}
//
//		return directroy;
//	}
//
//	private String getDirectroyName(String path) {
//		String directroy = null;
//		int index = path.lastIndexOf(File.separator);
//
//		if (index >= 0) {
//			directroy = path.substring(index + 1);
//		}
//
//		return directroy;
//	}
//
//	@Override
//	public long reload() {
//		Log.d(TAG, "refreshxxs.call localalbum add version");
//		mDataVersion = nextVersionNumber();
//		mCachedCount = INVALID_COUNT;
//
//		return mDataVersion;
//	}
//
//	@Override
//	public boolean getVersion() {
//		return mDataVersion != mCallDataVersion;
//	}
//
//	@Override
//	public int getSupportedOperations() {
//		return SUPPORT_DELETE | SUPPORT_SHARE | SUPPORT_INFO | SUPPORT_EXPORT | SUPPORT_EXCLUDE | SUPPORT_ORDER_BY
//				| EncryptionManager.getEncryptionOperation(isNeedShowLockIcon());
//	}
//
//	@Override
//	public void delete() {
//		GalleryUtils.assertNotInRenderThread();
//		// first , delete Tag record form local database
//		Cursor cursor = null;
//		try {
//			cursor = mResolver.query(mBaseUri, new String[] { FileColumns.DATA }, mWhereClause,
//					new String[] { String.valueOf(mBucketId) }, null);
//			PropertyProvider provider = mApplication.getPropertyProvider();
//			StorageManager sm = (StorageManager) mApplication.getAndroidContext()
//					.getSystemService(Context.STORAGE_SERVICE);
//			while (cursor != null && cursor.moveToNext()) {
//				String filePath = cursor.getString(cursor.getColumnIndex(FileColumns.DATA));
//				Log.d(TAG, "delete album, file path =" + filePath);
//				if (!TextUtils.isEmpty(filePath)) {
//					FilePathInfo info = StorageManagerHelper.getFilePathInfo(sm, filePath);
//					if (info != null) {
//						provider.deleteTagItems(info.volumeId, info.relativePath);
//					}
//				}
//			}
//		} catch (Exception e) {
//			Log.e(TAG, "delete tag when delete local album,catch exception,ignore", e);
//		} finally {
//			if (cursor != null) {
//				cursor.close();
//			}
//		}
//
//		mResolver.delete(mBaseUri, mWhereClause, new String[] { String.valueOf(mBucketId) });
//	}
//
//	/** add by yujun for bug 617003105936 */
//	@Override
//	public void delete(JobContext jc) {
//		GalleryUtils.assertNotInRenderThread();
//		// first , delete Tag record form local database
//		Cursor cursor = null;
//		try {
//			cursor = mResolver.query(mBaseUri, new String[] { FileColumns.DATA }, mWhereClause,
//					new String[] { String.valueOf(mBucketId) }, null);
//			PropertyProvider provider = mApplication.getPropertyProvider();
//			StorageManager sm = (StorageManager) mApplication.getAndroidContext()
//					.getSystemService(Context.STORAGE_SERVICE);
//			while (cursor != null && cursor.moveToNext() && !jc.isCancelled()) {
//				String filePath = cursor.getString(cursor.getColumnIndex(FileColumns.DATA));
//				Log.d(TAG, "delete album, file path =" + filePath);
//				if (!TextUtils.isEmpty(filePath)) {
//					FilePathInfo info = StorageManagerHelper.getFilePathInfo(sm, filePath);
//					if (info != null) {
//						provider.deleteTagItems(info.volumeId, info.relativePath);
//					}
//
//					/** delete file */
//					String where = " _data=? ";
//
//					mResolver.delete(mBaseUri, where, new String[] { filePath.toLowerCase() });
//				}
//			}
//		} catch (Exception e) {
//			Log.e(TAG, "delete tag when delete local album,catch exception,ignore", e);
//		} finally {
//			if (cursor != null) {
//				cursor.close();
//			}
//		}
//	}
//
//	@Override
//	public boolean isLeafAlbum() {
//		return true;
//	}
//
//    public String getLocalizedName(Resources res, int bucketId, String name) {
//        StorageManager manager = (StorageManager) mApplication
//                .getAndroidContext().getSystemService(Context.STORAGE_SERVICE);
//        sdDataPath = StorageManagerHelper.getExternalStoragePath(manager);
//        int screenShotucketId = GalleryUtils.getBucketId(sdDataPath + "/"
//                + BucketNames.SCREENSHOTS);
//        int bluetoothShotucketId = GalleryUtils.getBucketId(sdDataPath + "/"
//                + BucketNames.BLUETOOTH);
//        if (bucketId == PresetBucket.getPrimaryCameraBucketId()) {
//            return res.getString(R.string.folder_camera);
//        } else if (bucketId == PresetBucket.getPrimaryDownloadBucketId()) {
//            return res.getString(R.string.folder_download);
//        } else if (bucketId == PresetBucket.getPrimaryImportBucketId()) {
//            return res.getString(R.string.folder_imported);
//        } else if (bucketId == screenShotucketId) {
//            StringBuilder builder = new StringBuilder(
//                    res.getString(R.string.folder_screenshot));
//            return builder.append("(SD)").toString();
//        } else if (bucketId == PresetBucket.getPrimarySnapshotBucketId()) {
//            return res.getString(R.string.folder_screenshot);
//        } else if (bucketId == PresetBucket.getPrimaryEditOnlineBucketId()) {
//            return res.getString(R.string.folder_edited_online_photos);
//        } else if (bucketId == GalleryUtils.getBucketId(BucketNames.STORAGE
//                + BucketNames.BLUETOOTH)) {
//            return res.getString(R.string.bluetooth_album);
//        } else if (bucketId == bluetoothShotucketId) {
//            StringBuilder builder = new StringBuilder(
//                    res.getString(R.string.bluetooth_album));
//            return builder.append("(SD)").toString();
//        } else {
//            return name;
//        }
//    }
//
//	// Relative path is the absolute path minus external storage path
//	public static String getRelativePath(int bucketId) {
//		String relativePath = "/";
//		if (bucketId == PresetBucket.getPrimaryCameraBucketId()) {
//			relativePath += BucketNames.CAMERA;
//		} else if (bucketId == PresetBucket.getPrimaryDownloadBucketId()) {
//			relativePath += BucketNames.DOWNLOAD;
//		} else if (bucketId == PresetBucket.getPrimaryImportBucketId()) {
//			relativePath += BucketNames.IMPORTED;
//		} else if (bucketId == PresetBucket.getPrimarySnapshotBucketId()) {
//			relativePath += BucketNames.SCREENSHOTS;
//		} else if (bucketId == PresetBucket.getPrimaryEditOnlineBucketId()) {
//			relativePath += BucketNames.EDITED_ONLINE_PHOTOS;
//		} else {
//			// If the first few cases didn't hit the matching path, do a
//			// thorough search in the local directories.
//			File extStorage = Environment.getExternalStorageDirectory();
//			String path = GalleryUtils.searchDirForPath(extStorage, bucketId);
//			if (path == null) {
//				Log.w(TAG, "Relative path for bucket id: " + bucketId + " is not found.");
//				relativePath = null;
//			} else {
//				relativePath = path.substring(extStorage.getAbsolutePath().length());
//			}
//		}
//		return relativePath;
//	}
//
//	private String formatTitle(String today, String yesterday, Calendar now, String title) {
//		if (TextUtils.isEmpty(title)) {
//			return mApplication.getResources().getString(R.string.unknown);
//		}
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		SimpleDateFormat withoutYearFormatter = new SimpleDateFormat("MM-dd");
//		try {
//			Date date = sdf.parse(title);
//			Calendar datetaken = Calendar.getInstance();
//			datetaken.setTime(date);
//			int dYear = datetaken.get(Calendar.YEAR);
//			int dM = datetaken.get(Calendar.MONTH);
//			int dD = datetaken.get(Calendar.DAY_OF_MONTH);
//
//			int nYear = now.get(Calendar.YEAR);
//			int nM = now.get(Calendar.MONTH);
//			int nD = now.get(Calendar.DAY_OF_MONTH);
//
//			if (dYear == nYear) {
//				if (dM == nM) {
//					if (dD == nD) {
//						// Got today string.
//						return today;
//					} else if (dD == nD - 1) {
//						// Got yesterday string
//						return yesterday;
//					}
//				}
//				return withoutYearFormatter.format(datetaken.getTime());
//			} else {
//				return title;
//			}
//
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return title;
//
//	}
//
//	/*
//	 * @Override public ArrayList<MediaItemGroup> getMediaItemGroups() { if
//	 * (mExpand) return null;
//	 *
//	 * String today = mApplication.getResources().getString(R.string.today);
//	 * String yesterday = mApplication.getResources().getString(
//	 * R.string.yesterday); Calendar now = Calendar.getInstance();
//	 *
//	 * int colsLand =
//	 * mApplication.getResources().getInteger(R.integer.album_cols_land); int
//	 * colsPort =
//	 * mApplication.getResources().getInteger(R.integer.album_cols_port);
//	 *
//	 * int startItem = 0; int startPortLine = 0; int startLandLine = 0;
//	 *
//	 * LinkedHashMap<String, ArrayList<Path>> map = new LinkedHashMap<String,
//	 * ArrayList<Path>>();
//	 *
//	 * for (int i = 0; i < mMediaData.size(); i++) { MediaItem item =
//	 * mMediaData.get(i);
//	 *
//	 * Path path = item.getPath(); long dateInLong = item.getDateInMs();
//	 *
//	 * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); Date date =
//	 * new Date(dateInLong); String title1 = sdf.format(date); String title =
//	 * formatTitle(today, yesterday, now, title1); Log.w(TAG, "title-->" +
//	 * title);
//	 *
//	 * ArrayList<Path> list = map.get(title); if (list == null) { list = new
//	 * ArrayList<Path>(); map.put(title, list); }
//	 *
//	 * list.add(path); }
//	 *
//	 * ArrayList<MediaItemGroup> groups = new ArrayList<MediaItemGroup>();
//	 *
//	 * for (Map.Entry<String, ArrayList<Path>> entry : map.entrySet()){ String
//	 * title = entry.getKey(); ArrayList<Path> paths = entry.getValue();
//	 *
//	 * MediaItemGroup group = new MediaItemGroup();
//	 *
//	 * group.title = title; group.itemCount = paths.size(); group.startItem =
//	 * startItem; group.startPortLine = startPortLine; group.startLandLine =
//	 * startLandLine; groups.add(group);
//	 *
//	 * startItem += group.itemCount; startPortLine += Math.ceil(1.0f *
//	 * group.itemCount / colsPort); startLandLine += Math.ceil(1.0f *
//	 * group.itemCount / colsLand); }
//	 *
//	 * return groups; }
//	 */
//
//	@Override
//	public ArrayList<MediaItemGroup> getMediaItemGroups() {
//		Log.d(TAG, "classx.getMediaItemGroups.bucket_id=" + mBucketId);
//		if (mExpand)
//			return null;
//
//		String today = mApplication.getResources().getString(R.string.today);
//		String yesterday = mApplication.getResources().getString(R.string.yesterday);
//		Calendar now = Calendar.getInstance();
//
//		ArrayList<MediaItemGroup> list = new ArrayList<MediaItemGroup>();
//		// GalleryUtils.assertNotInRenderThread();
//		String[] projection;
//		if (Gallery.sAlbumPageOrderbyType == Constants.ORDER_BY_DATE) {
//			projection = new String[] { "DATE(datetaken/1000,'unixepoch','localtime')", "COUNT(*)" };
//		} else {
//			projection = new String[] { "substr(title,1,1) as titleName", "COUNT(*)" };
//		}
//		String groupBy;
//		if (GalleryConfig.isEnableBurstGroup() && isCameraRoll()) {
//			if (Gallery.sAlbumPageOrderbyType == Constants.ORDER_BY_DATE) {
//				groupBy = "bucket_id=? AND (group_id is null or group_id = 0 or _id IN (SELECT _id FROM files WHERE group_id!=0 GROUP BY group_id)))  GROUP BY (DATE(datetaken/1000,'unixepoch','localtime')";
//			} else {
//				groupBy = "bucket_id=? AND (group_id is null or group_id = 0 or _id IN (SELECT _id FROM files WHERE group_id!=0 GROUP BY group_id)))  GROUP BY (titleName";
//			}
//		} else {
//			if (Gallery.sAlbumPageOrderbyType == Constants.ORDER_BY_DATE) {
//				groupBy = "bucket_id=?)  GROUP BY (DATE(datetaken/1000,'unixepoch','localtime')";
//			} else {
//				groupBy = "bucket_id=?)  GROUP BY (titleName";
//			}
//		}
//
////		settings = ((GalleryApp) mApplication.getAndroidContext()).getSettings();
////		if (!mIsIncSmallFile && settings.isPhotoFilter()) {
////			groupBy = " ( _size > 20480)  and " + groupBy;
////		}
//
//		if (mType == TYPE_IMAGE) {
//			groupBy = FileColumns.MEDIA_TYPE + "=" + FileColumns.MEDIA_TYPE_IMAGE + " AND " + groupBy;
//		} else if (mType == TYPE_VIDEO) {
//			groupBy = "(" + FileColumns.MEDIA_TYPE + "=" + FileColumns.MEDIA_TYPE_VIDEO
//			// + " AND (image_type is null or image_type!='livephoto'))" + " AND
//			// " + groupBy;
//					+ " )" + " AND " + groupBy;
//		} else if (mType == TYPE_ALL) {
//			groupBy = "((" + FileColumns.MEDIA_TYPE + "=" + FileColumns.MEDIA_TYPE_VIDEO
//			// + " AND (image_type is null or image_type!='livephoto'))" + " OR
//			// "
//					+ " )" + " OR " + FileColumns.MEDIA_TYPE + "=" + FileColumns.MEDIA_TYPE_IMAGE + ") AND " + groupBy;
//		}
//
//		// if(EncryptionManager.isShowNormalAndPrivacy(EncryptionManager.getAppContext())){
//		// groupBy = EncryptionManager.getShowNormalAndPrivacyWherePlus(false) +
//		// groupBy;
//		// }
//		// if(UserManagerUtil.isPrvateMode()){
//		// groupBy = EncryptionManager.getPrivacyWherePlus(false) + groupBy;
//		// }
//		Cursor cursor = null;
//		if (Gallery.sAlbumPageOrderbyType == Constants.ORDER_BY_DATE) {
//			mOrderClause = mOrderByDate;
//			cursor = mResolver.query(mBaseUri, projection, groupBy, new String[] { String.valueOf(mBucketId) },
//					mOrderClause);
//		} else {
//			mOrderClause = mOrderByName;
//			cursor = mResolver.query(mBaseUri, projection, groupBy, new String[] { String.valueOf(mBucketId) },
//					mOrderClause);
//		}
//
//		if (cursor == null) {
//			Log.w(TAG, "classx.query fail: " + mBaseUri);
//			return list;
//		}
//
//		try {
//			int colsLand = mApplication.getResources().getInteger(R.integer.album_cols_land);
//			int colsPort = mApplication.getResources().getInteger(R.integer.album_cols_port);
//			int startPortLine = 0;
//			int startLandLine = 0;
//			int startItem = 0;
//			while (cursor.moveToNext()) {
//				MediaItemGroup group = new MediaItemGroup();
//				Log.w("TAG", "classx.title2.1-->" + cursor.getString(0));
//				Log.w("TAG", "classx.title2-->" + cursor.getInt(1));
//
//				if (Gallery.sAlbumPageOrderbyType == Constants.ORDER_BY_DATE) {
//					group.title = formatTitle(today, yesterday, now, cursor.getString(0));
//				} else {
//					group.title = cursor.getString(0);
//				}
//				group.itemCount = cursor.getInt(1);
//				group.startItem = startItem;
//				group.startPortLine = startPortLine;
//				group.startLandLine = startLandLine;
//				group.bucketId = this.mBucketId;
//				list.add(group);
//
//				startItem += group.itemCount;
//				startPortLine += Math.ceil(1.0f * group.itemCount / colsPort);
//				startLandLine += Math.ceil(1.0f * group.itemCount / colsLand);
//
//			}
//		} finally {
//			cursor.close();
//		}
//		if (list.size() > 0) {
//			Log.d(TAG, "classx.group.list.size=" + list.size());
//			return list;
//		} else
//			return null;
//	}
//
//	public String getBucketFilePath() {
//		Cursor cursor = null;
//		String albumPath = null;
//		try {
//			Uri uri = Files.getContentUri("external").buildUpon().appendQueryParameter("limit", 0 + "," + 1).build();
//			;
//			String[] projection = new String[] { FileColumns._ID, FileColumns.DATA };
//			String where = "bucket_id=" + mBucketId + " AND (" + FileColumns.MEDIA_TYPE + "="
//					+ FileColumns.MEDIA_TYPE_IMAGE + " OR " + FileColumns.MEDIA_TYPE + "="
//					+ FileColumns.MEDIA_TYPE_VIDEO + ")";
//			if (EncryptionManager.isShowNormalAndPrivacy(EncryptionManager.getAppContext())) {
//				where = where + EncryptionManager.getShowNormalAndPrivacyWherePlus(true);
//			}
//			if (UserManagerUtil.isPrvateMode()) {
//				where = where + EncryptionManager.getPrivacyWherePlus(true);
//			}
//			cursor = mResolver.query(uri, projection, where, null, null);
//			if (cursor == null) {
//				Log.w(TAG, "query fail");
//				return null;
//			}
//			if (cursor.getCount() < 1) {
//				Log.e("zhy", "Cound not find album with bucketId:" + mBucketId);
//				return null;
//			}
//			cursor.moveToFirst();
//			String filePath = cursor.getString(1);
//			albumPath = getParent(filePath);
//
//		} finally {
//			if (cursor != null)
//				cursor.close();
//		}
//		return albumPath;
//	}
//
//	@Override
//	public int getMediaType() {
//		return mType;
//	}
//
//	public int getBucketId() {
//		return mBucketId;
//	}
//
//	@Override
//	public boolean hasImage() {
//		// TODO Auto-generated method stub
//		if (mType == TYPE_VIDEO)
//			return false;
//		if (mType == TYPE_IMAGE)
//			return getMediaItemCount() > 0;
//		Cursor cursor = null;
//		try {
//			String where;
//			if (GalleryConfig.isEnableBurstGroup()) {
//				where = FileColumns.MEDIA_TYPE + "=" + FileColumns.MEDIA_TYPE_IMAGE + " AND " + ImageColumns.BUCKET_ID
//						+ " = ? AND "
//						+ "(group_id = 0 or _id IN (SELECT _id FROM files WHERE group_id!=0 GROUP BY group_id))";
//			} else {
//				where = FileColumns.MEDIA_TYPE + "=" + FileColumns.MEDIA_TYPE_IMAGE + " AND " + ImageColumns.BUCKET_ID
//						+ " = ?";
//			}
//
//			if (EncryptionManager.isShowNormalAndPrivacy(EncryptionManager.getAppContext())) {
//				where = EncryptionManager.getShowNormalAndPrivacyWherePlus(false) + where;
//			}
//			if (UserManagerUtil.isPrvateMode()) {
//				where = EncryptionManager.getPrivacyWherePlus(false) + where;
//			}
//			Uri uri = mBaseUri.buildUpon().appendQueryParameter("limit", 0 + "," + 10).build();
//			cursor = mResolver.query(uri, mProjection, where, new String[] { String.valueOf(mBucketId) }, mOrderClause);
//			return cursor == null ? false : cursor.getCount() > 0;
//
//		} finally {
//			if (cursor != null)
//				cursor.close();
//		}
//
//	}
//
//	@Override
//	public ArrayList<MediaItemGroup> getMediaItemGroupsByGroupId(long groupId) {
//		if (mExpand)
//			return null;
//
//		String today = mApplication.getResources().getString(R.string.today);
//		String yesterday = mApplication.getResources().getString(R.string.yesterday);
//		Calendar now = Calendar.getInstance();
//
//		ArrayList<MediaItemGroup> list = new ArrayList<MediaItemGroup>();
//		GalleryUtils.assertNotInRenderThread();
//		String[] projection;
//
//		projection = new String[] { "DATE(datetaken/1000,'unixepoch','localtime')", "COUNT(*)" };
//
//		String groupBy;
//
//		// groupBy = "bucket_id=? AND ( _id IN (SELECT _id FROM files WHERE
//		// group_id=? GROUP BY group_id))) GROUP BY
//		// (DATE(datetaken/1000,'unixepoch','localtime')";
//		groupBy = "bucket_id=? AND  group_id=?)  GROUP BY (DATE(datetaken/1000,'unixepoch','localtime')";
//
////		settings = ((GalleryApp) mApplication.getAndroidContext()).getSettings();
////		if (!mIsIncSmallFile && settings.isPhotoFilter())
////			groupBy = " ( _size > 20480 ) and " + groupBy;
//
//		groupBy = FileColumns.MEDIA_TYPE + "=" + FileColumns.MEDIA_TYPE_IMAGE + " AND " + groupBy;
//		if (EncryptionManager.isShowNormalAndPrivacy(EncryptionManager.getAppContext())) {
//			groupBy = EncryptionManager.getShowNormalAndPrivacyWherePlus(false) + groupBy;
//		}
//		if (UserManagerUtil.isPrvateMode()) {
//			groupBy = EncryptionManager.getPrivacyWherePlus(false) + groupBy;
//		}
//		Cursor cursor = null;
//
//		mOrderClause = mOrderByDate;
//		cursor = mResolver.query(mBaseUri, projection, groupBy,
//				new String[] { String.valueOf(mBucketId), String.valueOf(groupId) }, mOrderClause);
//
//		if (cursor == null) {
//			Log.w(TAG, "query fail: " + mBaseUri);
//			return list;
//		}
//
//		try {
//			int colsLand = mApplication.getResources().getInteger(R.integer.album_cols_land);
//			int colsPort = mApplication.getResources().getInteger(R.integer.album_cols_port);
//			int startPortLine = 0;
//			int startLandLine = 0;
//			int startItem = 0;
//			while (cursor.moveToNext()) {
//				MediaItemGroup group = new MediaItemGroup();
//				if (Gallery.sAlbumPageOrderbyType == Constants.ORDER_BY_DATE) {
//					group.title = formatTitle(today, yesterday, now, cursor.getString(0));
//				} else {
//					group.title = cursor.getString(0);
//				}
//				group.itemCount = cursor.getInt(1);
//				group.startItem = startItem;
//				group.startPortLine = startPortLine;
//				group.startLandLine = startLandLine;
//				list.add(group);
//
//				startItem += group.itemCount;
//				startPortLine += Math.ceil(1.0f * group.itemCount / colsPort);
//				startLandLine += Math.ceil(1.0f * group.itemCount / colsLand);
//
//			}
//		} finally {
//			cursor.close();
//		}
//		if (list.size() > 0)
//			return list;
//		else
//			return null;
//	}
//    /*modify for bug 7621 liuxiaoshuan 20171124 start*/
//    public void setName(String name){
//        if(name == null){
//            return;
//        }
//        mName = name;
//    }
//    /*modify for bug 7621 liuxiaoshuan 20171124 end*/
//
}
