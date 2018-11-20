package cole.crop;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;

import com.gome.beautymirror.R;

import android.content.Context;
import android.drm.DrmManagerClient;
import android.drm.DrmStore;
import android.util.Log;

public class JBDrmImpl
{
	static DrmInputStream stream;
    private static DrmManagerClient mDrmClient;
    public static final String DRM_MIME_TYPE = "application/vnd.oma.drm.message";

    public static final int RIGHTS_PLAY = DrmStore.Action.PLAY;
    public static final int RIGHTS_DISPLAY = DrmStore.Action.DISPLAY;
    public static final int RIGHTS_EXECUTE = DrmStore.Action.EXECUTE;
    public static final int RIGHTS_PRINT = DrmStore.Action.DEFAULT;

    public static boolean verifyRights(String filePath, int action)
    {
        return true;
    }

    public static Hashtable<Integer, String> getDrmLicenseInfo(Context context,
            String path)
    {
        Hashtable<Integer, String> info = new Hashtable<Integer, String>();
        DrmManagerClient drmClient = new DrmManagerClient(context);
        if (drmClient == null
                || !drmClient.canHandle(path, DrmAdapter.DRM_MIME_TYPE))
        {
            // Avoid null pointer exception
            return info;
        }

        // Now just for FL type.
        if (true)
        {
            String unlimit = "没有限制";
            info.put(MediaDetails.INDEX_DRM_FL_PLAY, unlimit);
            info.put(MediaDetails.INDEX_DRM_FL_DISPLAY, unlimit);
            info.put(MediaDetails.INDEX_DRM_FL_EXECUTE, unlimit);
            info.put(MediaDetails.INDEX_DRM_FL_PRINT, unlimit);
        }
        // Release
        Method releaseMethod = null;
        try
        {
            releaseMethod = DrmManagerClient.class.getMethod("release", null);
            if (releaseMethod != null)
            {
                releaseMethod.setAccessible(true);

                releaseMethod.invoke(drmClient, null);

            }
        }
        catch (NoSuchMethodException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return info;
    }

    public static InputStream getDrmInputStream(MediaItem item)
    {
        if (item == null)
            return null;
        String filePath = item.getFilePath();
        stream = DrmInputStream.open(filePath);
        return stream;
    }

    public static InputStream getDrmInputStream(String filePath)
    {
    	try{
    		stream = DrmInputStream.open(filePath);
    		return stream;
    	}catch(UnsatisfiedLinkError el){
    		Log.i("JBDrmImpl", "getDrmInputStream UnsatisfiedLinkError + " + el.toString());
    		return null;
    	}catch(Exception e){
    		Log.i("JBDrmImpl", "getDrmInputStream Exception + " + e.toString());
    		return null;
    	}
    }

    public static String getSDRightsUri(MediaItem item)
    {
        return null;
    }

    public static int getActionDisplay()
    {
        return DrmStore.Action.DISPLAY;
    }

    public static boolean isDrmCanShareFile(MediaItem item)
    {
        return false;
    }

    public static void startDrmUsage(MediaItem item)
    {

    }

    public static void stopDrmUsage(MediaItem item)
    {

    }

    public static boolean isProtected(String filePath)
    {
        if (mDrmClient != null && mDrmClient.canHandle(filePath, DrmAdapter.DRM_MIME_TYPE))
        {
            return true;
        }
        return false;
    }

    public static boolean isFLFile(MediaItem item)
    {
        if (mDrmClient != null
                && mDrmClient.canHandle(item.getFilePath(), DRM_MIME_TYPE))
            return true;

        return false;

    }

    public static void startDrmService(Context context)
    {
    	if (mDrmClient != null)
    		return;
        mDrmClient = new DrmManagerClient(context);
    }

    public static void stopDrmService()
    {

    }

    public static void resume(Context context)
    {
        if (mDrmClient == null)
            mDrmClient = new DrmManagerClient(context);
    }
    
    public static  boolean checkRightStatus(String filePath)
    {
    	int result = DrmStore.RightsStatus.RIGHTS_INVALID;
    	
    	if (mDrmClient != null) {
        	result = mDrmClient.checkRightsStatus(filePath, DrmStore.Action.DEFAULT);
        	if (result == 0) {
        		return true;    		
    		}else {
    			return false;
    		}
		}    	
    	return false;
    }
}
