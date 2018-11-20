package cole.crop;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import android.app.Activity;
import android.content.Context;
import android.drm.DrmStore;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapRegionDecoder;
import android.graphics.ColorSpace;
import android.media.browse.MediaBrowser;
import android.support.v4.media.MediaBrowserCompat;

import org.linphone.mediastream.Log;

public class DrmAdapter {
    private static final String TAG = "DecodeUtils";
    public static final String DRM_MIME_TYPE = "application/vnd.oma.drm.message";

    public static void verifyRights(final Activity activity,
                                    final MediaItem item, final ColorSpace.Model model) {
        if (!GalleryConfig.getInstance().isEnableDRM()) {
            return;
        }
        if (!isValid(item)) {
            return;
        }
        if (!isProtected(item)) {
            return;
        }

    }

    public static boolean verifyPlayRights(String filePath) {
        if (!GalleryConfig.getInstance().isEnableDRM()) {
            return false;
        }
        return JBDrmImpl.verifyRights(filePath, DrmStore.Action.PLAY);
    }

    public static boolean verifyDisPlayRights(String filePath) {
        if (!GalleryConfig.getInstance().isEnableDRM()) {
            return false;
        }
        return JBDrmImpl.verifyRights(filePath, DrmStore.Action.DISPLAY);
    }

    public static void startDrmService(Context context) {
        if (!GalleryConfig.getInstance().isEnableDRM()) {
            return;
        }
        JBDrmImpl.startDrmService(context);
    }

    public static void stopDrmService() {
        if (!GalleryConfig.getInstance().isEnableDRM()) {
            return;
        }
        JBDrmImpl.stopDrmService();
    }

    public static void startDrmUsage(MediaItem item) {
        if (!GalleryConfig.getInstance().isEnableDRM()) {
            return;
        }
        if (!isValid(item)) {
            return;
        }

        if (!isProtected(item)) {
            return;
        }

        JBDrmImpl.startDrmUsage(item);
    }

    public static void stopDrmUsage(MediaItem item) {
        if (!GalleryConfig.getInstance().isEnableDRM()) {
            return;
        }
        if (!isValid(item)) {
            return;
        }

        if (!isProtected(item)) {
            return;
        }

        JBDrmImpl.stopDrmUsage(item);

    }

    public static boolean isProtected(String filePath) {
        if (!GalleryConfig.getInstance().isEnableDRM()) {
            Log.w(TAG, "DRM check is off");
            return false;
        }
        if (filePath == null) {
            return false;
        }

        return JBDrmImpl.isProtected(filePath);
    }

    public static boolean isProtected(MediaItem item) {
        if (!GalleryConfig.getInstance().isEnableDRM()) {
            return false;
        }

        if (!isValid(item)) {
            return false;
        }

        return isProtected(item.getFilePath());
    }

    public static Hashtable<Integer, String> getDrmLicenseInfo(Context context,
                                                               MediaItem item) {
        return JBDrmImpl.getDrmLicenseInfo(context, item.getFilePath());
    }

    public static boolean hasRights(MediaItem item) {
        if (!GalleryConfig.getInstance().isEnableDRM()) {
            return false;
        }
        if (!isValid(item)) {
            return false;
        }

        if (!isProtected(item)) {
            return false;
        }

        return JBDrmImpl.verifyRights(item.getFilePath(),
                JBDrmImpl.getActionDisplay());

    }


    public static boolean isFLFile(MediaItem item) {
        if (!GalleryConfig.getInstance().isEnableDRM()) {
            return false;
        }

        return JBDrmImpl.isFLFile(item);
    }

    public static boolean isValid(MediaItem item) {
        if (item != null
                && item.getFilePath() != null
                ) {
            return true;
        }

        return false;
    }

    public static InputStream getDrmInputStream(String filePath) {

        return JBDrmImpl.getDrmInputStream(filePath);
    }


    public static Bitmap requestDrmDecode(ThreadPool.JobContext jc, String filePath,
                                          Options options, int targetSize, int type) {
        InputStream stream = null;
        stream = getDrmInputStream(filePath);
        if (stream != null) {
            options = DecodeUtils.requestOptionFromStream(stream, options,
                    targetSize, type);
            try {
                stream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


        stream = getDrmInputStream(filePath);
        Bitmap bitmap = null;
        if (null != stream) {
            bitmap = DecodeUtils.requestDecode(jc, stream, options,
                    targetSize, type);

            try {
                stream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return bitmap;
    }


    /**
     * add by yujun for drm file
     */
    public static Bitmap requestDrmDecode(String filePath,
                                          Options options, int targetSize, int type) {
        InputStream stream = null;
        stream = getDrmInputStream(filePath);
        if (stream != null) {
            options = DecodeUtils.requestOptionFromStream(stream, options,
                    targetSize, type);
            try {
                stream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Log.w(TAG, "stream: " + stream);

        stream = getDrmInputStream(filePath);
        Bitmap bitmap = null;
        if (null != stream) {
            bitmap = DecodeUtils.requestDecode(stream, options,
                    targetSize, type);

            try {
                stream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return bitmap;
    }


    public static BitmapRegionDecoder requestCreateDrmBitmapRegionDecoder(
            ThreadPool.JobContext jc, String filePath, boolean shareable) {
        InputStream stream = getDrmInputStream(filePath);
        if (null == stream) {
            return null;
        }
        BitmapRegionDecoder decoder = DecodeUtils
                .createBitmapRegionDecoder(jc, stream, false);
        try {
            if (stream != null)
                stream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return decoder;
    }


    public static boolean isDrmCanShare(MediaItem item) {
        return false;
    }


    public static void resume(Context context) {
        JBDrmImpl.resume(context);
    }

    public static boolean isMultiFrame(Context context, MediaItem item) {
        InputStream in = getDrmInputStream(item.getFilePath());
        // For invalid drm file.
        if (in == null)
            return false;
        ByteArrayInputStream stream = null;
        try {
            byte[] drmData = new byte[in.available()];
            in.read(drmData);
            stream = new ByteArrayInputStream(drmData);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        boolean playable = item.getFilePath().endsWith(".gif");

        try {
            if (stream != null)
                stream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return playable;
    }

    public static boolean checkRightStatus(String filePath) {
        return JBDrmImpl.checkRightStatus(filePath);
    }

}
