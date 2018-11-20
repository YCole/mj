package cole.crop;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Process;
import android.os.UserHandle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;



/**
 * Created by Administrator on 2016/9/22.
 */

public class UserManagerUtil {


    public static final String PRIVACY_USER_ID = "privacy_user_id";

    public static final String CLONE_USER_ID = "clone_user_id";

    public static final int USER_NORMAL_SPACE_ID = 0;

    public static final int USER_PRIVACY_USER_ID = 10;

    public static final int USER_CLONE_USER_ID = 11;
    


    private static int init(Context mContext,String name){
        int id = 0;
        if(PRIVACY_USER_ID.equals(name)){
            id = 10;
        } else if(CLONE_USER_ID.equals(name)){
            id = 11;
        }
        try {
            id = getUserId(mContext,name);
            Log.e("zzzz","UserManagerUtil -- init id = "+id);
        } catch (Exception e){
            e.printStackTrace();
            Log.e("zzzz","UserManagerUtil -- init error = "+e.getMessage());
        }
        Log.e("zzzz","UserManagerUtil -- init 22 id = "+id);
        return id;
    }


    public static final int PER_USER_RANGE = 100000;
    public static final boolean MU_ENABLED = true;

    private static final int getUserId(int uid) {
        if (MU_ENABLED) {
            return uid / PER_USER_RANGE;
        } else {
            return 0;
        }
    }

    public static final int myUserId() {
        return getUserId(Process.myUid());
    }
    
    public static Context getAppContext(){
    	return (GalleryAppImpl)GalleryAppImpl.getApplication();
    }

    public static boolean isPrvateMode(){
        if(myUserId() == UserManagerUtil.USER_NORMAL_SPACE_ID){
            return false;
        } else if(myUserId() == init(getAppContext(),PRIVACY_USER_ID)){
            return true;
        } else if(myUserId() == init(getAppContext(),CLONE_USER_ID)){
            return false;
        }
        return false;
    }


    public static boolean isNormalMode(){
        if(myUserId() == UserManagerUtil.USER_NORMAL_SPACE_ID){
            return true;
        } else if(myUserId() == init(getAppContext(),PRIVACY_USER_ID)){
            return false;
        } else if(myUserId() == init(getAppContext(),CLONE_USER_ID)){
            return false;
        }
        return true;
    }

    public static boolean isCloneMode(){
        if(myUserId() == UserManagerUtil.USER_NORMAL_SPACE_ID){
            return false;
        } else if(myUserId() == init(getAppContext(),PRIVACY_USER_ID)){
            return false;
        } else if(myUserId() == init(getAppContext(),CLONE_USER_ID)){
            return true;
        }
        return true;
    }

    



    public static int getUserId(Context mcontext, String name){
        int id = 0;
        try {
         /* id = Integer.parseInt((String)PrivateUtil.invoke2(Settings.System.class,"getStringForUser",new Class[]{ContentResolver.class,String.class,int.class},
                    new Object[]{mcontext.getContentResolver(),name, 0}));*/
            Log.e("zzzz","UserManagerUtil -- getUserId 2 - id = "+ id);
        } catch (Exception e) {
            Log.e("zzzz","UserManagerUtil -- getUserId - "+e.getMessage());
            e.printStackTrace();
        }
        Log.e("zzzz","UserManagerUtil -- getUserId - 3 id = "+ id);
        return id;
    }

    public static UserHandle getAllUserHandle(){
        UserHandle userHandle = null;
        Log.e("zzzz","UserManagerUtil -- getAllUserHandle 1");
        try {
            UserHandle myUserHandle = Process.myUserHandle();
            Log.e("zzzz","UserManagerUtil -- getAllUserHandle 2");
            Field field = UserHandle.class.getDeclaredField("ALL");
            Log.e("zzzz","UserManagerUtil -- getAllUserHandle 3");
            field.setAccessible(true);
            userHandle = (UserHandle) field.get(myUserHandle);
            Log.e("zzzz","UserManagerUtil -- getAllUserHandle 4");
        } catch (Exception e2) {
            Log.e("zzzz","UserManagerUtil -- getAllUserHandle 5 error = "+ e2.getMessage());
            e2.printStackTrace();
        }
        Log.e("zzzz","UserManagerUtil -- getAllUserHandle 6 userHandle = "+ userHandle);
        return userHandle;
    }

    public static UserHandle getUserHandle(int userId){
        UserHandle userHandle = null;
        Log.e("zzzz","UserManagerUtil -- getUserHandle 1");
        try {
            Log.e("zzzz","UserManagerUtil -- getUserHandle 2");
            Constructor c0 = UserHandle.class.getDeclaredConstructor(new Class[]{int.class});
            Log.e("zzzz","UserManagerUtil -- getUserHandle 3");
            c0.setAccessible(true);
            userHandle = (UserHandle)c0.newInstance(new Object[]{userId});
            Log.e("zzzz","UserManagerUtil -- getUserHandle 4");
        } catch (Exception e2) {
            Log.e("zzzz","UserManagerUtil -- getUserHandle 5 error = "+ e2.getMessage());
            e2.printStackTrace();
        }
        Log.e("zzzz","UserManagerUtil -- getUserHandle 6 userHandle = "+ userHandle);
        return userHandle;
    }

    

}
