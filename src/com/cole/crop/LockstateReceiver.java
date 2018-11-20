package cole.crop;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Files.FileColumns;
import android.provider.MediaStore.Images.ImageColumns;


import org.linphone.mediastream.Log;

import java.util.ArrayList;

public class LockstateReceiver extends BroadcastReceiver {

    public String TAG = "LockstateReceiver";
    public static boolean isScreenLock = false;
    public static int cameraTotalCount = 0;
    public static int threeDcameraTotalCount = 0;
    public static int lockPhotoCount = 1;
    public static ArrayList<String> lockNewImageArray = new ArrayList<String>();

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        String action = intent.getAction();
        Log.i(TAG, "action = " + action);
        Bundle bundle = intent.getExtras();
        if (bundle.containsKey("state")) {
            String state = bundle.getString("state");
            Log.i(TAG, "state = " + state);

            isScreenLock = state.equals("open") ? true : false;

            if (state.equals("open")) {
                cameraTotalCount = getCurrentCameraCount();
                threeDcameraTotalCount = getCurrent3DCameraCount();
            } else if (state.equals("close")) {
                clearImageInfo();
                cameraTotalCount = 0;
                threeDcameraTotalCount = 0;
            }
        }
    }

    public static int getCurrentCameraCount() {

        GalleryApp mApp = GalleryAppImpl.getApplication();
        Context mContext = mApp.getAndroidContext();
//		String mWhereClause = "(" + FileColumns.MEDIA_TYPE + "="
//                + FileColumns.MEDIA_TYPE_IMAGE + " or "
//                + FileColumns.MEDIA_TYPE + "="
//                + FileColumns.MEDIA_TYPE_VIDEO + ") AND "
//                + ImageColumns.BUCKET_ID + " = ?";

        String mWhereClause = "(" + FileColumns.MEDIA_TYPE + "="
                + FileColumns.MEDIA_TYPE_IMAGE + " or ("
                + FileColumns.MEDIA_TYPE + "="
                + FileColumns.MEDIA_TYPE_VIDEO
                //+ " AND image_type!='livephoto')) AND "
                + ")) AND "
                + ImageColumns.BUCKET_ID + " = ?";
//		String mWhereClausePlus = " AND (group_id is null or group_id = 0 OR _id IN (SELECT _id FROM files WHERE group_id!=0 AND bucket_id=? GROUP BY group_id))";
//    	String order = ImageColumns._ID + " DESC";
//    	String[] PROJECTIONS = {
//                MediaStore.Images.Media.DATA,
//                MediaStore.Images.Media._ID
//        };
        String[] PROJECTIONS = {
                "count(*)"
        };
        int currentTotal = 0;
        Cursor cursor = null;



        try {
            cursor = mContext.getContentResolver().query(MediaStore.Files.getContentUri("external"), PROJECTIONS,
                    mWhereClause, new String[]{String.valueOf("cole")}, null);

//    		cursor = mContext.getContentResolver().query(MediaStore.Files.getContentUri("external"), PROJECTIONS,
//    				mWhereClause + mWhereClausePlus, new String[] {String.valueOf(bucketId),String.valueOf(bucketId)}, null);

            if (cursor == null) {
                Log.i("LockstateReceiver", "cursor = null return 0");
                return 0;
            }
            cursor.moveToNext();
            currentTotal = cursor.getInt(0);
            Log.i("LockstateReceiver", "currentTotal = " + currentTotal);

        } catch (Exception e) {
            Log.i("LockstateReceiver", "exception error");

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return currentTotal;

//		int bucketID = PresetBucket.getPrimaryCameraBucketId();
//		Path path = Path.fromString(String.format("/local/displayed/all/%d", bucketID));
//		LocalAlbum mTimeLocalAlbum = (LocalAlbum)mApp.getDataManager().getMediaObject(path);		
//		int count = mTimeLocalAlbum.getMediaItemCount();
//		return count;
    }

    public static int getCurrent3DCameraCount() {
        GalleryApp mApp = GalleryAppImpl.getApplication();
        Context mContext = mApp.getAndroidContext();

        String mWhereClause = "(" + FileColumns.MEDIA_TYPE + "="
                + FileColumns.MEDIA_TYPE_IMAGE + " or ("
                + FileColumns.MEDIA_TYPE + "="
                + FileColumns.MEDIA_TYPE_VIDEO
                //+ " AND image_type!='livephoto')) AND "
                + " )) AND "
                + ImageColumns.BUCKET_ID + " = ?";

        String[] PROJECTIONS = {
                "count(*)"
        };
        int currentTotal = 0;
        Cursor cursor = null;




        return currentTotal;
    }

//	public static int getCameraTotalCount(){
//		return cameraTotalCount;
//	}

    public static void setCameraTotalCount(int count) {
        cameraTotalCount = count;
    }

    public static void setLockPhotoCount(int count) {
        lockPhotoCount = count;
    }

    public static int getLockPhotoCount() {
        return lockPhotoCount;
    }

    public static void addNewImageInfo(String path) {
        if (!LockstateReceiver.lockNewImageArray.contains(path)) {
            Log.i("LockstateReceiver", "addNewImageInfo path = " + path);
            LockstateReceiver.lockNewImageArray.add(0, path);
        }
    }

    public static void deleteImageInfo(String path) {
        Log.i("LockstateReceiver", "delete path = " + path);
        if (LockstateReceiver.lockNewImageArray.contains(path)) {
            LockstateReceiver.lockNewImageArray.remove(path);
            cameraTotalCount--;
        }
    }

    public static void deleteImageInfo(int index) {
        Log.i("LockstateReceiver", "delete index = " + index);
        int size = LockstateReceiver.lockNewImageArray.size();
        if (index < size) {
            LockstateReceiver.lockNewImageArray.remove(index);
            cameraTotalCount--;
        }
    }

    public static void clearImageInfo() {
        LockstateReceiver.lockNewImageArray.clear();
    }


}
