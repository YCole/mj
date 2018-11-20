
package cole.crop;

import android.os.Environment;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class StorageManagerUtils {

    private static boolean INITED = false;
    private static Method methodGetVolumePaths;
    private static Method methodGetVolumeList;
    private static Method methodGetVolumeState;

    private static void init() {
        if (INITED)
            return;

        try {
            Class storageManagerClass = Class
                    .forName("android.os.storage.StorageManager");

            methodGetVolumePaths = storageManagerClass
                    .getDeclaredMethod("getVolumePaths");
            methodGetVolumeList = storageManagerClass
                    .getDeclaredMethod("getVolumeList");
            methodGetVolumeState = storageManagerClass.getDeclaredMethod(
                    "getVolumeState", String.class);
        } catch (ClassNotFoundException e) {
            Log.e("zhy", "StorageManager RefError:" + e);
        } catch (NoSuchMethodException e) {
            Log.e("zhy", "StorageManager RefError:" + e);
        }

        INITED = true;
    }

    public static String getVolumeState(Object storageManager, String path) {
        init();
        if (methodGetVolumeState == null) {
            Log.e("zhy",
                    "StorageManager RefError can not find getVolumeState method.");
            return Environment.MEDIA_UNMOUNTED;
        }
        try {
            String state = (String) methodGetVolumeState.invoke(storageManager,
                    path);
            if (state == null) {
                Log.e("zhy",
                        "StorageManager RefError getVolumeState invoke error.");
                return Environment.MEDIA_UNMOUNTED;
            }
            return state;
        } catch (IllegalArgumentException e) {
            Log.e("zhy", "StorageManager RefError:" + e);
        } catch (IllegalAccessException e) {
            Log.e("zhy", "StorageManager RefError:" + e);
        } catch (InvocationTargetException e) {
            Log.e("zhy", "StorageManager RefError:" + e);
        }
        return Environment.MEDIA_UNMOUNTED;
    }

    public static String[] getVolumePaths(Object storageManager) {
        init();
        if (methodGetVolumePaths == null) {
            Log.e("zhy",
                    "StorageManager RefError can not find getVolumePaths method.");
            return null;
        }
        try {
            String[] paths = (String[]) methodGetVolumePaths
                    .invoke(storageManager);
            return paths;
        } catch (IllegalArgumentException e) {
            Log.e("zhy", "StorageManager RefError:" + e);
        } catch (IllegalAccessException e) {
            Log.e("zhy", "StorageManager RefError:" + e);
        } catch (InvocationTargetException e) {
            Log.e("zhy", "StorageManager RefError:" + e);
        }
        return null;
    }

    public static Object[] getVolumeList(Object storageManager) {
        init();
        if (methodGetVolumeList == null) {
            Log.e("zhy",
                    "StorageManager RefError can not find getVolumeList method.");
            return null;
        }
        try {
            Object[] volumes = (Object[]) methodGetVolumeList
                    .invoke(storageManager);
            return volumes;
        } catch (IllegalArgumentException e) {
            Log.e("zhy", "StorageManager RefError:" + e);
        } catch (IllegalAccessException e) {
            Log.e("zhy", "StorageManager RefError:" + e);
        } catch (InvocationTargetException e) {
            Log.e("zhy", "StorageManager RefError:" + e);
        }
        return null;
    }
}
