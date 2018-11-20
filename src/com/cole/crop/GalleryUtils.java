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

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.ConditionVariable;
import android.os.Environment;
import android.os.StatFs;
import android.os.UserHandle;
import android.os.storage.StorageManager;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;


import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class GalleryUtils {
//    private static final String TAG = "GalleryUtils";
//    private static final String MAPS_PACKAGE_NAME = "com.google.android.apps.maps";
//    private static final String MAPS_CLASS_NAME = "com.google.android.maps.MapsActivity";
//    private static final String CAMERA_LAUNCHER_NAME = "com.android.camera.CameraLauncher";
//
//    public static final String MIME_TYPE_IMAGE = "image/*";
//    public static final String MIME_TYPE_VIDEO = "video/*";
//    public static final String MIME_TYPE_PANORAMA360 = "application/vnd.google.panorama360+jpg";
//    public static final String MIME_TYPE_ALL = "*/*";
//
//    private static final String DIR_TYPE_IMAGE = "vnd.android.cursor.dir/image";
//    private static final String DIR_TYPE_VIDEO = "vnd.android.cursor.dir/video";
//
//    private static final String PREFIX_PHOTO_EDITOR_UPDATE = "editor-update-";
//    private static final String PREFIX_HAS_PHOTO_EDITOR = "has-editor-";
//
//    private static final String KEY_ENCRYPT_UPDATE = "encrypt-update";
//    private static final String KEY_HAS_ENCRYPT = "has-encrypt";
//
//    private static final String KEY_CAMERA_UPDATE = "camera-update";
//    private static final String KEY_HAS_CAMERA = "has-camera";
//
//    private static float sPixelDensity = -1f;
//    private static boolean sCameraAvailableInitialized = false;
//    private static boolean sCameraAvailable;
//
//    /* Modify By Zhangjie,20171123,PRODUCTION-7327,begin*/
//    public static final int FLAG_FROM_WHERE_NONE = -1;
//    public static final int FLAG_FROM_WHERE_SEARCH = 0;
//    public static final int FLAG_FROM_WHERE_FEATURE = 1;
//    public static final int FLAG_FROM_WHERE_MAPVIEW = 2;
//    /* Modify By Zhangjie,20171123,PRODUCTION-7327,end*/
//
//    @SuppressLint("NewApi")
//    public static void initialize(Context context) {
//        DisplayMetrics metrics = new DisplayMetrics();
//        WindowManager wm = (WindowManager) context
//                .getSystemService(Context.WINDOW_SERVICE);
//        wm.getDefaultDisplay().getMetrics(metrics);
//        sPixelDensity = metrics.density;
//        Resources r = context.getResources();
//        TiledScreenNail.setPlaceholderColor(context.getColor(R.color.bitmap_screennail_placeholder));
//        initializeThumbnailSizes(metrics, r);
//    }
//
//    private static void initializeThumbnailSizes(DisplayMetrics metrics,
//            Resources r) {
//        int maxPixels = Math.max(metrics.heightPixels, metrics.widthPixels);
//
//        // For screen-nails, we never need to completely fill the screen
//        MediaItem.setThumbnailSizes(maxPixels / 2, Math.min(200, maxPixels / 5));
//        TiledScreenNail.setMaxSide(maxPixels / 2);
//    }
//
//    public static float[] intColorToFloatARGBArray(int from) {
//        return new float[] {
//                Color.alpha(from) / 255f, Color.red(from) / 255f,
//                Color.green(from) / 255f, Color.blue(from) / 255f
//        };
//    }
//
//    public static float dpToPixel(float dp) {
//        return sPixelDensity * dp;
//    }
//
//    public static int dpToPixel(int dp) {
//        return Math.round(dpToPixel((float) dp));
//    }
//
//    public static int meterToPixel(float meter) {
//        // 1 meter = 39.37 inches, 1 inch = 160 dp.
//        return Math.round(dpToPixel(meter * 39.37f * 160));
//    }
//
//    public static byte[] getBytes(String in) {
//        byte[] result = new byte[in.length() * 2];
//        int output = 0;
//        for (char ch : in.toCharArray()) {
//            result[output++] = (byte) (ch & 0xFF);
//            result[output++] = (byte) (ch >> 8);
//        }
//        return result;
//    }
//
//    // Below are used the detect using database in the render thread. It only
//    // works most of the time, but that's ok because it's for debugging only.
//
//    private static volatile Thread sCurrentThread;
//    private static volatile boolean sWarned;
//
//    public static void setRenderThread() {
//        sCurrentThread = Thread.currentThread();
//    }
//
//    public static void assertNotInRenderThread() {
//        if (!sWarned) {
//            if (Thread.currentThread() == sCurrentThread) {
//                sWarned = true;
//                Log.w(TAG, new Throwable("Should not do this in render thread"));
//            }
//        }
//    }
//
//    private static final double RAD_PER_DEG = Math.PI / 180.0;
//    private static final double EARTH_RADIUS_METERS = 6367000.0;
//
//    public static double fastDistanceMeters(double latRad1, double lngRad1,
//            double latRad2, double lngRad2) {
//        if ((Math.abs(latRad1 - latRad2) > RAD_PER_DEG)
//                || (Math.abs(lngRad1 - lngRad2) > RAD_PER_DEG)) {
//            return accurateDistanceMeters(latRad1, lngRad1, latRad2, lngRad2);
//        }
//        // Approximate sin(x) = x.
//        double sineLat = (latRad1 - latRad2);
//
//        // Approximate sin(x) = x.
//        double sineLng = (lngRad1 - lngRad2);
//
//        // Approximate cos(lat1) * cos(lat2) using
//        // cos((lat1 + lat2)/2) ^ 2
//        double cosTerms = Math.cos((latRad1 + latRad2) / 2.0);
//        cosTerms = cosTerms * cosTerms;
//        double trigTerm = sineLat * sineLat + cosTerms * sineLng * sineLng;
//        trigTerm = Math.sqrt(trigTerm);
//
//        // Approximate arcsin(x) = x
//        return EARTH_RADIUS_METERS * trigTerm;
//    }
//
//    public static double accurateDistanceMeters(double lat1, double lng1,
//            double lat2, double lng2) {
//        double dlat = Math.sin(0.5 * (lat2 - lat1));
//        double dlng = Math.sin(0.5 * (lng2 - lng1));
//        double x = dlat * dlat + dlng * dlng * Math.cos(lat1) * Math.cos(lat2);
//        return (2 * Math.atan2(Math.sqrt(x), Math.sqrt(Math.max(0.0, 1.0 - x))))
//                * EARTH_RADIUS_METERS;
//    }
//
//    public static final double toMile(double meter) {
//        return meter / 1609;
//    }
//
//    // For debugging, it will block the caller for timeout millis.
//    public static void fakeBusy(JobContext jc, int timeout) {
//        final ConditionVariable cv = new ConditionVariable();
//        jc.setCancelListener(new CancelListener() {
//            @Override
//            public void onCancel() {
//                cv.open();
//            }
//        });
//        cv.block(timeout);
//        jc.setCancelListener(null);
//    }
//
//    public static boolean isEditorAvailable(Context context, String mimeType) {
//        int version = PackagesMonitor.getPackagesVersion(context);
//
//        String updateKey = PREFIX_PHOTO_EDITOR_UPDATE + mimeType;
//        String hasKey = PREFIX_HAS_PHOTO_EDITOR + mimeType;
//
//        SharedPreferences prefs = PreferenceManager
//                .getDefaultSharedPreferences(context);
//        if (prefs.getInt(updateKey, 0) != version) {
//            PackageManager packageManager = context.getPackageManager();
//            List<ResolveInfo> infos = packageManager.queryIntentActivities(
//                    new Intent(Intent.ACTION_EDIT).setType(mimeType), 0);
//            prefs.edit().putInt(updateKey, version)
//                    .putBoolean(hasKey, !infos.isEmpty()).commit();
//        }
//
//        return prefs.getBoolean(hasKey, true);
//    }
//
//    public static boolean isEncryptAvailable(Context context) {
//        int version = PackagesMonitor.getPackagesVersion(context);
//        SharedPreferences prefs = PreferenceManager
//                .getDefaultSharedPreferences(context);
//        if (prefs.getInt(KEY_ENCRYPT_UPDATE, 0) != version) {
//            PackageManager packageManager = context.getPackageManager();
//            boolean install = false;
//            try {
//            	packageManager.getPackageInfo("com.hct.heartyservice", 0);
//            	install = true;
//			} catch (Exception e) {
//				//ignore
//			}
//            /*List<ResolveInfo> infos = packageManager.queryIntentActivities(
//                    new Intent("com.hct.heartyservice.intent.action.ADD_PRIVACY_IMAGE"), 0);*/
//            prefs.edit().putInt(KEY_ENCRYPT_UPDATE, version)
//                    .putBoolean(KEY_HAS_ENCRYPT, install).commit();
//        }
//
//        return prefs.getBoolean(KEY_HAS_ENCRYPT, true);
//    }
//
//    public static boolean isAnyCameraAvailable(Context context) {
//        int version = PackagesMonitor.getPackagesVersion(context);
//        SharedPreferences prefs = PreferenceManager
//                .getDefaultSharedPreferences(context);
//        if (prefs.getInt(KEY_CAMERA_UPDATE, 0) != version) {
//            PackageManager packageManager = context.getPackageManager();
//            List<ResolveInfo> infos = packageManager.queryIntentActivities(
//                    new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA), 0);
//            prefs.edit().putInt(KEY_CAMERA_UPDATE, version)
//                    .putBoolean(KEY_HAS_CAMERA, !infos.isEmpty()).commit();
//        }
//        return prefs.getBoolean(KEY_HAS_CAMERA, true);
//    }
//
//    public static boolean isCameraAvailable(Context context) {
//
//        if (sCameraAvailableInitialized)
//            return sCameraAvailable;
//        PackageManager pm = context.getPackageManager();
//        ComponentName name = new ComponentName(context, CAMERA_LAUNCHER_NAME);
//        int state = pm.getComponentEnabledSetting(name);
//        sCameraAvailableInitialized = true;
//        sCameraAvailable = (state == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT)
//                || (state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED);
//        return sCameraAvailable;
//    }
//
//    public static void startGalleryActivity(Context context) {
//        Intent intent = new Intent(context, Gallery.class)
//                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                        | Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//    }
//
//    public static boolean isValidLocation(double latitude, double longitude) {
//        // TODO: change || to && after we fix the default location issue
//        return (latitude != MediaItem.INVALID_LATLNG || longitude != MediaItem.INVALID_LATLNG);
//    }
//
//    public static String formatLatitudeLongitude(String format,
//            double latitude, double longitude) {
//        // We need to specify the locale otherwise it may go wrong in some
//        // language
//        // (e.g. Locale.FRENCH)
//        return String.format(Locale.ENGLISH, format, latitude, longitude);
//    }
//
//    public static void showOnMap(Context context, double latitude,
//            double longitude) {
//        try {
//            // We don't use "geo:latitude,longitude" because it only centers
//            // the MapView to the specified location, but we need a marker
//            // for further operations (routing to/from).
//            // The q=(lat, lng) syntax is suggested by geo-team.
//            String uri = formatLatitudeLongitude(
//                    "http://maps.google.com/maps?f=q&q=(%f,%f)", latitude,
//                    longitude);
//            ComponentName compName = new ComponentName(MAPS_PACKAGE_NAME,
//                    MAPS_CLASS_NAME);
//            Intent mapsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri))
//                    .setComponent(compName);
//            context.startActivity(mapsIntent);
//        } catch (ActivityNotFoundException e) {
//            // Use the "geo intent" if no GMM is installed
//            Log.e(TAG, "GMM activity not found!", e);
//            String url = formatLatitudeLongitude("geo:%f,%f", latitude,
//                    longitude);
//            Intent mapsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//            context.startActivity(mapsIntent);
//        }
//    }
//
//    public static void setViewPointMatrix(float matrix[], float x, float y,
//            float z) {
//        // The matrix is
//        // -z, 0, x, 0
//        // 0, -z, y, 0
//        // 0, 0, 1, 0
//        // 0, 0, 1, -z
//        Arrays.fill(matrix, 0, 16, 0);
//        matrix[0] = matrix[5] = matrix[15] = -z;
//        matrix[8] = x;
//        matrix[9] = y;
//        matrix[10] = matrix[11] = 1;
//    }
//
//    public static int getBucketId(String path) {
//        return path.toLowerCase().hashCode();
//    }
//
//    // Return the local path that matches the given bucketId. If no match is
//    // found, return null
//    public static String searchDirForPath(File dir, int bucketId) {
//        File[] files = dir.listFiles();
//        if (files != null) {
//            for (File file : files) {
//                if (file.isDirectory()) {
//                    String path = file.getAbsolutePath();
//                    if (GalleryUtils.getBucketId(path) == bucketId) {
//                        return path;
//                    } else {
//                        path = searchDirForPath(file, bucketId);
//                        if (path != null)
//                            return path;
//                    }
//                }
//            }
//        }
//        return null;
//    }
//
//    // Returns a (localized) string for the given duration (in seconds).
//    public static String formatDuration(final Context context, int duration) {
//        int h = duration / 3600;
//        int m = (duration - h * 3600) / 60;
//        int s = duration - (h * 3600 + m * 60);
//        String durationValue;
//        if (h == 0) {
//            durationValue = String.format(
//                    context.getString(R.string.details_ms), m, s);
//        } else {
//            durationValue = String.format(
//                    context.getString(R.string.details_hms), h, m, s);
//        }
//        return durationValue;
//    }
//
//    @TargetApi(ApiHelper.VERSION_CODES.HONEYCOMB)
//    public static int determineTypeBits(Context context, Intent intent) {
//        int typeBits = 0;
//        String type = intent.resolveType(context);
//
//        if (MIME_TYPE_ALL.equals(type)) {
//            typeBits = DataManager.INCLUDE_ALL;
//        } else if (MIME_TYPE_IMAGE.equals(type) || DIR_TYPE_IMAGE.equals(type)) {
//            typeBits = DataManager.INCLUDE_IMAGE;
//        } else if (MIME_TYPE_VIDEO.equals(type) || DIR_TYPE_VIDEO.equals(type)) {
//            typeBits = DataManager.INCLUDE_VIDEO;
//        } else {
//            typeBits = DataManager.INCLUDE_ALL;
//        }
//
//        /*
//         * if (ApiHelper.HAS_INTENT_EXTRA_LOCAL_ONLY) { if
//         * (intent.getBooleanExtra(Intent.EXTRA_LOCAL_ONLY, false)) { typeBits
//         * |= DataManager.INCLUDE_LOCAL_ONLY; } }
//         */
//
//        return typeBits;
//    }
//
//    public static int getSelectionModePrompt(int typeBits) {
//        if ((typeBits & DataManager.INCLUDE_VIDEO) != 0) {
//            return (typeBits & DataManager.INCLUDE_IMAGE) == 0 ? R.string.select_video
//                    : R.string.select_item;
//        }
//        return R.string.select_image;
//    }
//
//    public static boolean hasSpaceForSize(long size) {
//        String state = Environment.getExternalStorageState();
//        if (!Environment.MEDIA_MOUNTED.equals(state)) {
//            return false;
//        }
//
//        String path = Environment.getExternalStorageDirectory().getPath();
//        try {
//            StatFs stat = new StatFs(path);
//            return stat.getAvailableBlocks() * (long) stat.getBlockSize() > size;
//        } catch (Exception e) {
//            Log.i(TAG, "Fail to access external storage", e);
//        }
//        return false;
//    }
//
//    public static boolean isPanorama(MediaItem item) {
//        if (item == null)
//            return false;
//        int w = item.getWidth();
//        int h = item.getHeight();
//        return (h > 0 && w / h >= 2);
//    }
//
//    public static String getFileName(String filePath) {
//        if (TextUtils.isEmpty(filePath))
//            return filePath;
//        int lastPosition = filePath.lastIndexOf("/");
//        if (lastPosition < 0)
//            return filePath;
//        String fileName = filePath.substring(lastPosition + 1);
//        int endPos = fileName.lastIndexOf(".");
//        if (endPos < 0)
//            return fileName;
//        return fileName.substring(0, endPos);
//    }
//
//    public static String getFileNameWithExtension(String filePath) {
//        if (TextUtils.isEmpty(filePath))
//            return filePath;
//        int lastPosition = filePath.lastIndexOf("/");
//        if (lastPosition < 0)
//            return filePath;
//        String fileName = filePath.substring(lastPosition + 1);
//        return fileName;
//    }
//
//    public static String getNewFilePath(String filePath, String newFileName) {
//        if (TextUtils.isEmpty(filePath))
//            return filePath;
//        int lastPosition = filePath.lastIndexOf("/");
//        if (lastPosition < 0)
//            return null;
//        String directoryName = filePath.substring(0, lastPosition + 1);
//        String fileName = filePath.substring(lastPosition + 1);
//        int endPos = fileName.lastIndexOf(".");
//        if (endPos < 0)
//            return directoryName + newFileName;
//        String extendsion = fileName.substring(endPos);
//        return directoryName + newFileName + extendsion;
//    }
//
//    public static long getAvailableBlocks(AbstractGalleryActivity mActivity,
//            String path) {
//        long size = 0l;
//        StatFs stat = null;
//        try {
//            stat = new StatFs(path);
//        } catch (Exception e) {
//            Log.w(TAG, "StatFs error, path=" + path, e);
//            if (!TextUtils.isEmpty(path) && mActivity != null) {
//                StorageManager manager = (StorageManager) mActivity
//                        .getSystemService(Context.STORAGE_SERVICE);
//                String internDestPath = StorageManagerHelper
//                        .getInternalStoragePath(manager);
//                if (path.startsWith(internDestPath + "/")) {
//                    stat = new StatFs(internDestPath);
//                } else {
//                    String externalDestPath = StorageManagerHelper
//                            .getExternalStoragePath(manager);
//                    if (path.startsWith(externalDestPath + "/")) {
//                        stat = new StatFs(externalDestPath);
//                    }
//                }
//            }
//        }
//
//        if (stat != null) {
//            // put a bit of margin (in case creating the file grows the system
//            // by a few blocks)
//            long availableBlocks = (long) stat.getAvailableBlocks() - 4;
//            size = stat.getBlockSize() * availableBlocks;
//            size = size - 1024 * 1024 * 3; // remain :3M
//        }
//        return size;
//    }
//
//    /**
//     * judge is self created album
//     * @param path
//     * @return
//     */
//    public static boolean isSelfCreateAlbum(String path) {
//        boolean result = false;
//        String parentDirPath = BucketNames.STORAGE + BucketNames.SELF_CREATE_ALBUM_PARENT_PATH;
//        File file = new File(path);
//        if (file.exists() && file.isDirectory() && file.getParent().equals(parentDirPath)
//                && !file.getName().equals(BucketNames.SCREENSHOTS_FOLDER_NAME)) {
//            result = true;
//        }
//        return result;
//    }
//
//    /**
//     * To determine whether there is any self created album:check there is any folder in Pictures other than Screenshots
//     * @param album path
//     * @return
//     */
//    public static boolean isHasSelfCreateAlbum() {
//        boolean result = false;
//        String parentDirPath = BucketNames.STORAGE + Environment.DIRECTORY_PICTURES;
//        File parentDir = new File(parentDirPath);
//        if (parentDir.listFiles() != null) {
//            File[] files = parentDir.listFiles();
//            for (File file : files) {
//                if (file.isDirectory() && !file.getAbsolutePath().equals(BucketNames.STORAGE + BucketNames.SCREENSHOTS)) {
//                    result = true;
//                }
//            }
//            return result;
//        }
//        return result;
//    }
//
//    /**
//     * judge is system album according to bucketId
//     * @param bucketId
//     * @return true->system
//     */
//    public static boolean isSystemAlbum(int bucketId) {
//    	boolean result = false;
//    	if (bucketId == GalleryUtils.getBucketId(BucketNames.STORAGE + BucketNames.SCREENSHOTS)
//                || bucketId == GalleryUtils.getBucketId(BucketNames.STORAGE + BucketNames.BLUETOOTH)
//                || bucketId == GalleryUtils.getBucketId(BucketNames.STORAGE + BucketNames.COLLECT)
//                || bucketId == GalleryUtils.getBucketId(BucketNames.STORAGE + BucketNames.CAMERA)
//                || bucketId == GalleryUtils.getBucketId(BucketNames.STORAGE + BucketNames.ENCRYPT)
//                || bucketId == GalleryUtils.getBucketId(BucketNames.STORAGE + BucketNames.GROUP_PHOTO)
//                || bucketId == GalleryUtils.getBucketId(BucketNames.STORAGE + BucketNames.SELFIE)) {
//    		result = true;
//    	}
//    	return result;
//    }
//
//    public static String getRealPathFromURI(Context context, Uri contentURI) {
//        String result;
//        Cursor cursor = context.getContentResolver().query(contentURI,
//                new String[] { MediaStore.Images.ImageColumns.DATA },
//                null, null, null);
//        if (cursor == null)
//            result = contentURI.getPath();
//        else {
//            cursor.moveToFirst();
//            int index = cursor
//                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//            result = cursor.getString(index);
//            cursor.close();
//        }
//        return result;
//    }
//
//    /**
//     * @param context
//     * @return
//     */
    public static int getDaoHangHeight(Context context) {
        int result = 0;
        int resourceId = 0;
        int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0) {
            resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
        } else
            return 0;
    }
//
//    // add By zhangjie,20171013,PRODUCTION-2205,begin
////    public static boolean isNavigationBarShow(Activity activity) {
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
////            Display display = activity.getWindowManager().getDefaultDisplay();
////            Point size = new Point();
////            Point realSize = new Point();
////            display.getSize(size);
////            display.getRealSize(realSize);
////            return realSize.y != size.y;
////        } else {
////            boolean menu = ViewConfiguration.get(activity)
////                    .hasPermanentMenuKey();
////            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
////            if (menu || back) {
////                return false;
////            } else {
////                return true;
////            }
////        }
////    }
//
//    public static boolean isNavigationBarShow(Activity activity) {
//        return Settings.System.getIntForUser(activity.getContentResolver(), SHOW_NAVIGATIONBAR_SWITCH, 1, UserHandle.USER_CURRENT) == 1 ? true : false;
//    }
//
//    public static int getNavigationBarHeight(Activity activity) {
//        if (!isNavigationBarShow(activity)) {
//            return 0;
//        }
//        Resources resources = activity.getResources();
//        int resourceId = resources.getIdentifier("navigation_bar_height",
//                "dimen", "android");
//        int height = resources.getDimensionPixelSize(resourceId);
//        return height;
//    }
//
//    public static int getSceenHeight(Activity activity) {
//        return activity.getWindowManager().getDefaultDisplay().getHeight()
//                + getNavigationBarHeight(activity);
//    }
//    // add By zhangjie,20171013,PRODUCTION-2205,end
}
