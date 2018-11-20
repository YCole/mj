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
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings.System;
import android.util.Log;
import android.widget.Toast;
import com.bumptech.glide.Glide;

public class GalleryAppImpl extends Application implements GalleryApp,ComponentCallbacks2 {
    private static GalleryAppImpl mInstance;
    private static final String GALLERY_TAG_PRESETED = "gallery_tags_preset";
    public static boolean isMarshmallow;

    public static GalleryApp getApplication() {
        return mInstance;
    }

    private static final String DOWNLOAD_FOLDER = "download";
    private static final long DOWNLOAD_CAPACITY = 64 * 1024 * 1024; // 64M

    private Object mLock = new Object();
    private DataManager mDataManager;
    private ThreadPool mThreadPool;
    private StitchingProgressManager mStitchingProgressManager;
    private int PRELOAD_PHOTO_COUNT = 24;



    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        // mPresetBucket = new PresetBucket(getApplicationContext());
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        GalleryConfig.init(this);

        startInitIntentService();
        //startClusterService();
        mStitchingProgressManager = null;// LightCycleHelper.createStitchingManagerInstance(this);
        if (mStitchingProgressManager != null) {
//            mStitchingProgressManager.addChangeListener(getDataManager());
        }
        // If tags not preset, start a thread to preset tags.
        int settingValue = System.getInt(getContentResolver(),
                GALLERY_TAG_PRESETED, 0);
        if (settingValue == 0) {
            Thread thread = new Thread() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    super.run();
                    Log.w("gallery", "check" + checkSelfPermission(getApplicationContext(), "android.permission.INTERACT_ACROSS_USERS"));
            		if (checkSelfPermission(getApplicationContext(), "android.permission.INTERACT_ACROSS_USERS")) {
            			System.putInt(getContentResolver(), GALLERY_TAG_PRESETED, 1);
            		}
                }
            };
            thread.start();
        }
        sendScanOBBBroadcast();

        isMarshmallow=isMarshmallow();
    }
    /* modify by liuxiaoshuan 20171227 for 12140 start */
    private void sendScanOBBBroadcast() {
        String path = "file://"/*+EncryptionManager.PRIVICE*/;
        Uri mUri = Uri.parse(path);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,mUri);
        sendBroadcast(intent);
    }
    /* modify by liuxiaoshuan 20171227 for 12140 end */
    private void startInitIntentService() {
       Intent mIntent = new Intent( );
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            getAndroidContext().startForegroundService(mIntent);
        } else {
            getAndroidContext().startService(mIntent);
        }
    }



	@SuppressLint("NewApi") public static boolean checkSelfPermission(Context context, String sPermission) {
    	if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
    		return true;
    	}
		if (context.checkSelfPermission(sPermission) == PackageManager.PERMISSION_GRANTED) {
			return true;
		}
		return false;
    }

    @Override
    public Context getAndroidContext() {
        return this;
    }

    @Override
    public synchronized DataManager getDataManager() {
        if (mDataManager == null) {
            mDataManager = new DataManager(this);
            mDataManager.initializeSourceMap();
        }
        return mDataManager;
    }



    @Override
    public synchronized ThreadPool getThreadPool() {
        if (mThreadPool == null) {
            mThreadPool = new ThreadPool();
        }
        return mThreadPool;
    }



    private void initializeAsyncTask() {
        // AsyncTask class needs to be loaded in UI thread.
        // So we load it here to comply the rule.
        try {
            Class.forName(AsyncTask.class.getName());
        } catch (ClassNotFoundException e) {
        }
    }

    private PropertyProvider mProvider;

    @Override
    public PropertyProvider getPropertyProvider() {
        // TODO Auto-generated method stub
        if (mProvider == null)
            return new PropertyProvider(this);
        return mProvider;
    }

    @Override
    public void closePropertyProvider() {
        // TODO Auto-generated method stub
        if (mProvider != null) {
            mProvider.close();
            mProvider = null;
        }
    }


    public boolean isMarshmallow(){
    	return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
    @Override
    public void onLowMemory() {
        Glide.get(this).clearMemory();
        super.onLowMemory();
    }
    @Override
    public void onTrimMemory(int arg0) {
        Glide.get(this).trimMemory(arg0);
        super.onTrimMemory(arg0);
    }

}
