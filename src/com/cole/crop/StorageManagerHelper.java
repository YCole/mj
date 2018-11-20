
package cole.crop;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;


import java.util.ArrayList;


public class StorageManagerHelper {
    public static String getPrimaryStoragePath(Context context) {
    	
    	int location = Settings.System.getInt(context.getContentResolver(), "save_location",0);
    	String dataPath = "";
    	Log.d("location", ""+location);
    	StorageManager manager=(StorageManager)context.getSystemService(Context.STORAGE_SERVICE);    	
    	if(location == 1){
    		dataPath = getExternalStoragePath(manager);
    		String state= StorageManagerUtils.getVolumeState(manager, dataPath);
/*    		boolean bIsMounted = Environment.getExternalStorageState().equals(  
    		Environment.MEDIA_MOUNTED);*/
    		boolean bIsMounted = state.equalsIgnoreCase(Environment.MEDIA_MOUNTED) ? true : false; 
    		Log.d("StorageManagerHelper", "bIsMounted"+bIsMounted);
    		if(!bIsMounted || dataPath==null || dataPath.isEmpty() == true){
        		dataPath =  getInternalStoragePath(manager);
        	}
    		Log.d("StorageManagerHelper:", dataPath);
    		return dataPath;
    	}else{
    		dataPath =  getInternalStoragePath(manager);
    		Log.d("StorageManagerHelper:", dataPath);
    		return dataPath;
    	}
    	

    }

    public static String getInternalStoragePath(
            StorageManager manager) {
        Object[] volumes = StorageManagerUtils.getVolumeList(manager);
        if (volumes == null)
            return null;
        for (Object volume : volumes) {
            if (!StorageVolumeUtils.isRemovable(volume)) {
                return StorageVolumeUtils.getPath(volume);
            }
        }
        return null;
    }

    public static String getExternalStoragePath(
            StorageManager manager) {
        Object[] volumes = StorageManagerUtils.getVolumeList(manager);
        if (volumes == null)
            return null;
        for (Object volume : volumes) {
            if (StorageVolumeUtils.isRemovable(volume)) {
                return StorageVolumeUtils.getPath(volume);
            }
        }
        return null;
    }

    private static boolean isExternalStorageMounted(
            StorageManager manager) {
        Object[] volumes = StorageManagerUtils.getVolumeList(manager);
        if (volumes == null)
            return false;
        for (Object volume : volumes) {
            if (StorageVolumeUtils.isRemovable(volume)) {
                String path = StorageVolumeUtils.getPath(volume);
                String state = StorageManagerUtils
                        .getVolumeState(manager, path);
                if (Environment.MEDIA_MOUNTED.equals(state)
                        || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
                    return true;
                break;
            }
        }
        return false;
    }
    
    public static boolean isExternalAndInternalStorageNotMounted(
            StorageManager manager) {
        Object[] volumes = StorageManagerUtils.getVolumeList(manager);
        if (volumes == null)
            return false;
        boolean result = true;
		for (Object volume : volumes) {
			String path = StorageVolumeUtils.getPath(volume);
			String state = StorageManagerUtils.getVolumeState(manager, path);
			if (Environment.MEDIA_MOUNTED.equals(state)
					|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
				result = false;
		}
        return result;
    }

    public static String getStoragePathById(Context context, int storageId) {
    	StorageManager manager = (StorageManager) context
                .getSystemService(Context.STORAGE_SERVICE);
    	
    	if( StorageVolumeUtils.INVALID_INTERNAL_SD == storageId){
    		return getInternalStoragePath( manager );
    	}
    	else if( StorageVolumeUtils.INVALID_EXTERNAL_SD == storageId){
    		return getExternalStoragePath( manager );
    	}
    	
    	Object[] volumes = StorageManagerUtils.getVolumeList(manager);
        for (Object volume : volumes) {
            int volumeId = getFatVolumeId(volume);
            if (volumeId == storageId) {
                return StorageVolumeUtils.getPath(volume);
            }
        }
        return null;
    }

    /*
    public static int getFatVolumeId(Object volume) {
        if (Build.VERSION.SDK_INT >= 19)
            return StorageVolumeUtils.getFatVolumeId(volume);
        else {
            String path = StorageVolumeUtils.getPath(volume);
            return StorageVolumeUtils.getFatVolumeId(path);
        }
    }
    */
    
    public static int getFatVolumeId(Object volume) {
    	int retVal = StorageVolumeUtils.INVALID_FAT_VOLUME_ID;
    	
    	if (Build.VERSION.SDK_INT >= 19)
    		retVal = StorageVolumeUtils.getFatVolumeId(volume);
        else {
            String path = StorageVolumeUtils.getPath(volume);
            retVal = StorageVolumeUtils.getFatVolumeId(path);
        }
    	
        if( StorageVolumeUtils.INVALID_FAT_VOLUME_ID != retVal){
        	return retVal;
        }
        
        if(StorageVolumeUtils.isRemovable(volume)){
        	if(StorageVolumeUtils.allowMassStorage(volume)){
        		retVal = StorageVolumeUtils.INVALID_EXTERNAL_SD;
        	}
        }
        else{
        	retVal = StorageVolumeUtils.INVALID_INTERNAL_SD;
        }
        
        return retVal;
    }
    

    public static FilePathInfo getFilePathInfo(StorageManager manager,
            String filePath) {
        if (TextUtils.isEmpty(filePath))
            return null;
        Object[] volumes = StorageManagerUtils.getVolumeList(manager);
        for (Object volume : volumes) {
            String volumePath = StorageVolumeUtils.getPath(volume);
            if (filePath.startsWith(volumePath + "/")) {
                FilePathInfo info = new FilePathInfo();
                info.volumeId = getFatVolumeId(volume);
                info.volumePath = volumePath;
                info.relativePath = filePath.replace(volumePath, "");
                return info;
            } else if (filePath.equals(volumePath)) {
                FilePathInfo info = new FilePathInfo();
                info.volumeId = getFatVolumeId(volume);
                info.volumePath = volumePath;
                info.relativePath = "";
                return info;
            }
        }
        return null;
    }

    public static String getDescriptionPath(Context context, String filePath) {
        StorageManager manager = (StorageManager) context
                .getSystemService(Context.STORAGE_SERVICE);
        if (TextUtils.isEmpty(filePath))
            return null;
        Object[] volumes = StorageManagerUtils.getVolumeList(manager);
        for (Object volume : volumes) {
            String volumePath = StorageVolumeUtils.getPath(volume);
            if (filePath.startsWith(volumePath + "/")) {

                String vPath = volumePath;
                String dPath = StorageVolumeUtils.getDescription(volume,
                        context);
                return filePath.replace(volumePath, dPath);
            }
        }
        return null;
    }

    public static String getRelativePath(String filePath, String volumePath) {
        if (filePath == null || volumePath == null
                || !filePath.startsWith(volumePath))
            return null;

        return filePath.replace(volumePath, "");
    }

    public static Integer[] getMountedVolumeIds(StorageManager manager) {
        Object[] volumes = StorageManagerUtils.getVolumeList(manager);
        if (volumes == null)
            return null;

        ArrayList<Integer> ids = new ArrayList<Integer>();
        for (Object volume : volumes) {
            String path = StorageVolumeUtils.getPath(volume);
            String state = StorageManagerUtils.getVolumeState(manager, path);
            if (Environment.MEDIA_MOUNTED.equals(state)
                    || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                int volumeId = getFatVolumeId(volume);
                ids.add(volumeId);
            }
        }
        int size = ids.size();
        if (size < 1)
            return null;
        return ids.toArray(new Integer[size]);
    }
}
