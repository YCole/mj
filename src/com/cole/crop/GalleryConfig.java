
package cole.crop;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.MediaColumns;
import android.provider.MediaStore.Files.FileColumns;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Video.VideoColumns;
import android.util.Log;


import java.util.ArrayList;

public class GalleryConfig {
//    private static final String CONFIG_PATH = "config/config.prop";
    private static GalleryConfig mInstance;
//    private Properties mProp;
    private static final String TAG = "GalleryConfig";

//    public static final String HCT_newproj="HCT_newproj";
    
    private static boolean mIsEnableBurstGroup = false;
    private static boolean mIsFileTypeColumeExist = false;
    private static boolean mIsThumbMagicExist = false;
    private boolean mIsEnableShowSetCallScreenWallpaper;
    private boolean mIsEnableDRM = false;
    private static boolean mIsCheckComplate = false;
    private boolean mIsVolumeKeyForTurnPage = false;
    public boolean mIsDualCameraDevice = false;

    private GalleryConfig(Context context) {
    	String model = Build.MODEL;
    	String productName = Build.PRODUCT;//SystemProperties.get("ro.product.name");
		Log.v("GalleryConfig", "model:" + model);
		Log.v("HCT_Gallery_HctConfig", "productName=" + productName);
		
//		if (productName.equals(HCT_P894A02)) {
//			mIsDualCameraDevice = true;
//		}
		
//		if(model.equals(HCT_newproj)){
//			mIsVolumeKeyForTurnPage = true;
//		}
		
		mIsEnableDRM = isAbroadBranch(context);
		Log.w("GalleryConfig", "mIsEnableHCTDRM-->" + mIsEnableDRM);
    }

    public static void init(Context context) {
    	if(mInstance == null){
    		mInstance = new GalleryConfig(context);
    	}
    	initHctConfig(context);
    	initProjection();
    }    

    public static boolean isAbroadBranch(Context cx) {
        String val = SystemPropertiesProxy.get(cx, "ro.gios.custom");
        return  val.equalsIgnoreCase("abroad") || val.equalsIgnoreCase("na");
    }
    public static GalleryConfig getInstance() {
        return mInstance;
    }


    /**
     * Check whether the column "group_id" exist.
     * @param context
     * @return
     */
    public static void checkGroupIdExist(Context context){
    	if (context != null) {
    		Cursor cursor = null;
    		try {
        		Uri mBaseUri = Files.getContentUri("external");
                Uri uri = mBaseUri.buildUpon().appendQueryParameter("limit", "0").build();
    			cursor = context.getContentResolver().query(uri, null, null, null, null);
    			mIsEnableBurstGroup = cursor != null && cursor.getColumnIndex("group_id") != -1 ;
    			Log.d("GalleryConfig", "check group_id exist, result="+mIsEnableBurstGroup);
			} catch (Exception e) {
				Log.e("GalleryConfig", "check group_id error ", e);
			} finally{
				if (cursor != null) {
					cursor.close();
				}
			}
		}    	
    }
    
    
    public static void checkImageTypeExist(Context context){
    	if (context != null) {
    		Cursor cursor = null;
    		try {
        		Uri mBaseUri = Files.getContentUri("external");
                Uri uri = mBaseUri.buildUpon().appendQueryParameter("limit", "0").build();
    			cursor = context.getContentResolver().query(uri, null, null, null, null);
                    mIsFileTypeColumeExist= cursor != null && cursor.getColumnIndex("file_type") != -1 ;
                Log.d("GalleryConfig", "check file_type exist, result="+mIsFileTypeColumeExist);
			} catch (Exception e) {
				Log.e("GalleryConfig", "check  file_type error ", e);
			} finally{
				if (cursor != null) {
					cursor.close();
				}
			}
		}    	
    }
    
    public static void checkThumbMagicExist(Context context){
    	if (context != null) {
    		Cursor cursor = null;
    		try {
        		Uri mBaseUri = Files.getContentUri("external");
                Uri uri = mBaseUri.buildUpon().appendQueryParameter("limit", "0").build();
    			cursor = context.getContentResolver().query(uri, null, null, null, null);
    			mIsThumbMagicExist = cursor != null && cursor.getColumnIndex("mini_thumb_magic") != -1 ;
    			Log.d("GalleryConfig", "check mini_thumb_magic exist, result=" + mIsThumbMagicExist);
			} catch (Exception e) {
				Log.e("GalleryConfig", "check  mini_thumb_magic error ", e);
			} finally{
				if (cursor != null) {
					cursor.close();
				}
			}
		}    	
    }
    
    public static void initHctConfig(Context context){
    	if (context != null) {
    		Cursor cursor = null;
    		try {
        		Uri mBaseUri = Files.getContentUri("external");
                Uri uri = mBaseUri.buildUpon().appendQueryParameter("limit", "0").build();
    			cursor = context.getContentResolver().query(uri, null, null, null, null);
    			
    			mIsThumbMagicExist = cursor != null && cursor.getColumnIndex("mini_thumb_magic") != -1 ;
                mIsFileTypeColumeExist= cursor != null && cursor.getColumnIndex("file_type") != -1 ;
    			mIsEnableBurstGroup = cursor != null && cursor.getColumnIndex("group_id") != -1 ;
    			
    			mIsCheckComplate = true;
    			Log.d("GalleryConfig", "initHctConfig , mIsThumbMagicExist=" + mIsThumbMagicExist);
                Log.d("GalleryConfig", "initHctConfig , mIsFileTypeColumeExist=" + mIsFileTypeColumeExist);
    			Log.d("GalleryConfig", "initHctConfig , mIsEnableBurstGroup=" + mIsEnableBurstGroup);
			} catch (Exception e) {
				mIsCheckComplate = false;
				Log.e("GalleryConfig", "initHctConfig error ", e);
			} finally{
				if (cursor != null) {
					cursor.close();
				}
			}
		}    	
    }
    
    public static boolean isThumbMagicExist(){
    	return getInstance().mIsThumbMagicExist;
    }

    public static boolean isEnableBurstGroup() {
        return  true;
//    	return true;
    }
    
    public static boolean isImageTypeColumeExist() {
        return getInstance().mIsFileTypeColumeExist;
    }
    
    public static boolean isEnableShowSetCallWallpaper() {
        return getInstance().mIsEnableShowSetCallScreenWallpaper;
    }
    
    public static boolean isEnableDRM(){
        if(getInstance()!=null){
            return getInstance().mIsEnableDRM;
        }else{
            return false;
        }
    }
    
    private static ArrayList<String> mProjectionList = null;
    static void initDefaultProjectionList(){
    	mProjectionList.add(FileColumns._ID);
    	mProjectionList.add(FileColumns.TITLE);// 1
    	mProjectionList.add(FileColumns.MIME_TYPE);// 2
    	mProjectionList.add(ImageColumns.LATITUDE); // 3
    	mProjectionList.add(ImageColumns.LONGITUDE); // 4
    	mProjectionList.add(ImageColumns.DATE_TAKEN); // 5
    	mProjectionList.add(FileColumns.DATE_ADDED); // 6
    	mProjectionList.add(FileColumns.DATE_MODIFIED);// 7
    	mProjectionList.add(FileColumns.DATA);// 8
    	mProjectionList.add(ImageColumns.BUCKET_ID); // 9
    	mProjectionList.add(FileColumns.SIZE); // 10
    	mProjectionList.add(ImageColumns.ORIENTATION); // 11
    	mProjectionList.add(VideoColumns.DURATION); // 12
    	mProjectionList.add(VideoColumns.RESOLUTION);// 13
    	mProjectionList.add(MediaColumns.WIDTH); // 14
    	mProjectionList.add(MediaColumns.HEIGHT);// 15
    	mProjectionList.add(FileColumns.MEDIA_TYPE); // 16
    	mProjectionList.add(VideoColumns.DESCRIPTION);// 17
        mProjectionList.add(ImageColumns.DISPLAY_NAME);//18
        mProjectionList.add("group_id");//19
        mProjectionList.add("file_type");//20
        mProjectionList.add("mini_thumb_magic");//21
        /*mProjectionList.add("is_encrypt");//22
        mProjectionList.add("is_favorite");//23
*/    }
    
    public static String[] PROJECTION ;
    public static void initProjection(){

    	if(mProjectionList == null ){
    		Log.w(TAG, "initProjection ");
    		mProjectionList = new ArrayList<String>();
    		initDefaultProjectionList();
    	}
	    	
        if (mIsCheckComplate == true && mProjectionList != null) {
            Log.w(TAG, "mIsCheckComplate-->" + mIsCheckComplate);
            if (!GalleryConfig.isEnableBurstGroup() && mProjectionList.contains("group_id")) {
                mProjectionList.remove("group_id");
            }

            if (!GalleryConfig.isImageTypeColumeExist() && mProjectionList.contains("file_type")) {
                mProjectionList.remove("file_type");
            }

            if (!GalleryConfig.isThumbMagicExist() && mProjectionList.contains("mini_thumb_magic")) {
                mProjectionList.remove("mini_thumb_magic");
            }
        }
    	
        //print((String[])mProjectionList.toArray(new String[0]));
    	PROJECTION = (String[])mProjectionList.toArray(new String[0]);
    }
    
    public static void print(String[] list){
    	Log.w(TAG, "FINAL_PROJECTION list length--> " + list.length);

    	for(int i = 0; i < list.length; i++){
    		Log.w(TAG, "FINAL_PROJECTION value-->" + list[i].toString());
    	}
    }
    
    public static String[] getProjection(){
        //print(PROJECTION);
    	return PROJECTION;
    }
    
    public static boolean isEnableVolumnKeyForTurnPage() {
        return getInstance().mIsVolumeKeyForTurnPage;
    }
    
    public static boolean isDualCameraDevice(){
    	return getInstance().mIsDualCameraDevice;
    }
    
}
