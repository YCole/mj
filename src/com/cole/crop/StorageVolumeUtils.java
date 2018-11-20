
package cole.crop;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class StorageVolumeUtils {
    public static final int INVALID_FAT_VOLUME_ID = -1;
    public static final int INVALID_INTERNAL_SD = -2;
    public static final int INVALID_EXTERNAL_SD = -3;
    
    private static boolean INITED = false;
    private static Method methodGetPath;
    private static Method methodIsRemovable;
    private static Method methodGetDescription;
    private static Method methodGetFatVolumeId;
    private static Method methodGetFileUtilsId;
    private static Method methodAllowMassStorage;
    private static Class FileUtils;

    private static void init() {
        if (INITED)
            return;

        try {
            Class storageVolumeClass = Class
                    .forName("android.os.storage.StorageVolume");

            methodGetPath = storageVolumeClass.getDeclaredMethod("getPath");
            methodIsRemovable = storageVolumeClass
                    .getDeclaredMethod("isRemovable");
            methodGetDescription = storageVolumeClass.getDeclaredMethod(
                    "getDescription", Context.class);
            if (Build.VERSION.SDK_INT >= 19) {
                methodGetFatVolumeId = storageVolumeClass
                        .getDeclaredMethod("getFatVolumeId");
            } else {
                FileUtils = Class.forName("android.os.FileUtils");
                methodGetFileUtilsId = FileUtils.getDeclaredMethod(
                        "getFatVolumeId", String.class);
            }
            
            methodAllowMassStorage  = storageVolumeClass
                    .getDeclaredMethod("allowMassStorage");

        } catch (ClassNotFoundException e) {
            Log.e("zhy", "StorageVolume init:" + e);
        } catch (NoSuchMethodException e) {
            Log.e("zhy", "StorageVolume init:" + e);
        } catch (Exception e) {
            Log.e("zhy", "StorageVolume init:" + e);
        }

        INITED = true;
    }

    public static String getPath(Object volume) {
        init();
        if (methodGetPath == null) {
            Log.e("zhy", "StorageVolume RefError can not find method getPath");
            return null;
        }
        try {
            return (String) methodGetPath.invoke(volume);
        } catch (IllegalArgumentException e) {
            Log.e("zhy", "StorageVolume getPath:" + e);
        } catch (IllegalAccessException e) {
            Log.e("zhy", "StorageVolume getPath:" + e);
        } catch (InvocationTargetException e) {
            Log.e("zhy", "StorageVolume getPath:" + e);
        }
        return null;
    }

    public static boolean allowMassStorage(Object volume) {
        init();
        if (methodAllowMassStorage == null) {
            Log.e("zhy",
                    "StorageVolume RefError can not find method allowMassStorage");
            return false;
        }
        try {
            Boolean retVal = (Boolean) methodAllowMassStorage.invoke(volume);
            if (retVal == null)
                return false;
            return retVal;
        } catch (IllegalArgumentException e) {
            Log.e("zhy", "StorageVolume allowMassStorage:" + e);
        } catch (IllegalAccessException e) {
            Log.e("zhy", "StorageVolume allowMassStorage:" + e);
        } catch (InvocationTargetException e) {
            Log.e("zhy", "StorageVolume allowMassStorage:" + e);
        }
        return false;
    }
    
    public static boolean isRemovable(Object volume) {
        init();
        if (methodIsRemovable == null) {
            Log.e("zhy",
                    "StorageVolume RefError can not find method isRemovable");
            return false;
        }
        try {
            Boolean retVal = (Boolean) methodIsRemovable.invoke(volume);
            if (retVal == null)
                return false;
            return retVal;
        } catch (IllegalArgumentException e) {
            Log.e("zhy", "StorageVolume isRemovable:" + e);
        } catch (IllegalAccessException e) {
            Log.e("zhy", "StorageVolume isRemovable:" + e);
        } catch (InvocationTargetException e) {
            Log.e("zhy", "StorageVolume isRemovable:" + e);
        }
        return false;
    }

    public static String getDescription(Object volume, Context context) {
        init();
        if (methodGetDescription == null) {
            Log.e("zhy", "StorageVolume RefError can not find method getPath");
            return null;
        }
        try {
            return (String) methodGetDescription.invoke(volume, context);
        } catch (IllegalArgumentException e) {
            Log.e("zhy", "StorageVolume getDescription:" + e);
        } catch (IllegalAccessException e) {
            Log.e("zhy", "StorageVolume getDescription:" + e);
        } catch (InvocationTargetException e) {
            Log.e("zhy", "StorageVolume getDescription:" + e);
        }
        return null;
    }

    public static int getFatVolumeId(Object volume) {
        init();
        if (methodGetFatVolumeId == null) {
            Log.e("zhy",
                    "StorageVolume RefError can not find method getFatVoumeId");
            return INVALID_FAT_VOLUME_ID;
        }
        try {
            Integer retVal = (Integer) methodGetFatVolumeId.invoke(volume);
            if (retVal == null)
                return INVALID_FAT_VOLUME_ID;

            return retVal;
        } catch (IllegalArgumentException e) {
            Log.e("zhy", "StorageVolume getFatVolumeId:" + e);
        } catch (IllegalAccessException e) {
            Log.e("zhy", "StorageVolume getFatVolumeId:" + e);
        } catch (InvocationTargetException e) {
            Log.e("zhy", "StorageVolume getFatVolumeId:" + e);
        }
        return INVALID_FAT_VOLUME_ID;
    }

    public static int getFatVolumeId(String path) {
        init();
        if (methodGetFileUtilsId == null) {
            Log.e("zhy",
                    "StorageVolume RefError can not find FileUtils method getFatVoumeId");
            return INVALID_FAT_VOLUME_ID;
        }
        try {
            Integer retVal = (Integer) methodGetFileUtilsId.invoke(FileUtils,
                    path);
            if (retVal == null)
                return INVALID_FAT_VOLUME_ID;

            return retVal;
        } catch (IllegalArgumentException e) {
            Log.e("zhy", "StorageVolume getFatVolumeIdS:" + e);
        } catch (IllegalAccessException e) {
            Log.e("zhy", "StorageVolume getFatVolumeIdS:" + e);
        } catch (InvocationTargetException e) {
            Log.e("zhy", "StorageVolume getFatVolumeIdS:" + e);
        }
        return INVALID_FAT_VOLUME_ID;
    }
}
