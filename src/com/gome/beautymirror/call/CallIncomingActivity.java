package com.gome.beautymirror.call;

/*
CallIncomingActivity.java
Copyright (C) 2017  Belledonne Communications, Grenoble, France

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.beautymirror.LinphoneManager;
import com.gome.beautymirror.LinphonePreferences;
import com.gome.beautymirror.LinphoneService;
import com.gome.beautymirror.LinphoneUtils;
import com.gome.beautymirror.R;
import com.gome.beautymirror.activities.BeautyMirrorActivity;
import com.gome.beautymirror.activities.BeautyMirrorGenericActivity;
import com.gome.beautymirror.contacts.ContactsManager;
import com.gome.beautymirror.contacts.LinphoneContact;
import org.linphone.core.Address;
import org.linphone.core.Call;
import org.linphone.core.Call.State;
import org.linphone.core.CallParams;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.mediastream.Log;

import java.util.ArrayList;
import java.util.List;

import android.hardware.Camera;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.io.IOException;

import android.database.Cursor;
import com.gome.beautymirror.data.DataService;
import com.gome.beautymirror.data.provider.DatabaseUtil;

public class CallIncomingActivity extends BeautyMirrorGenericActivity implements SurfaceHolder.Callback{
    private static CallIncomingActivity instance;

    private TextView name, number;
    private ImageView accept, decline, arrow, contactPicture,toAudio;
    private Call mCall;
    private CoreListenerStub mListener;
    private LinearLayout acceptUnlock;
    private LinearLayout declineUnlock;
    private RelativeLayout topLayout;
    private boolean alreadyAcceptedOrDeniedCall, begin;
    private float answerX, oldMove;
    private float declineX;

    //videoCaptureSurface
    private Camera mCamera;
    private SurfaceHolder mHolder;
    private SurfaceView mView;
    private static final String TAG = "CallIncomingActivity";

    public static CallIncomingActivity instance() {
        return instance;
    }

    public static boolean isInstanciated() {
        return instance != null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getBoolean(R.bool.orientation_portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.call_incoming);

        name = findViewById(R.id.contact_name);
        number = findViewById(R.id.contact_number);
        contactPicture = findViewById(R.id.contact_picture);

        //videoCaptureSurface
        mView = (SurfaceView) findViewById(R.id.videoCaptureSurface);
        mHolder = mView.getHolder();
        mHolder.addCallback(this);

        // set this flag so this activity will stay in front of the keyguard
        int flags = WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;
        getWindow().addFlags(flags);

        topLayout= findViewById(R.id.topLayout);
        accept = findViewById(R.id.accept);
        decline = findViewById(R.id.decline);
        toAudio =  findViewById(R.id.to_audio);
        lookupCurrentCall();
        if (LinphonePreferences.instance() != null && mCall != null && mCall.getRemoteParams() != null &&
                LinphonePreferences.instance().shouldAutomaticallyAcceptVideoRequests() &&
                mCall.getRemoteParams().videoEnabled()) {
            accept.setImageResource(R.drawable.call_video_start);
            mView.setVisibility(View.VISIBLE);
        }

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer(true);
            }
        });


        toAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer(false);
            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decline();
            }
        });

        mListener = new CoreListenerStub() {
            @Override
            public void onCallStateChanged(Core lc, Call call, State state, String message) {
                if (call == mCall && State.End == state) {
                    finish();
                }
                if (state == State.StreamsRunning) {
                    Log.e("CallIncommingActivity - onCreate -  State.StreamsRunning - speaker = " + LinphoneManager.getInstance().isSpeakerEnabled());
                    // The following should not be needed except some devices need it (e.g. Galaxy S).
                    LinphoneManager.getInstance().enableSpeaker(LinphoneManager.getInstance().isSpeakerEnabled());
                }
            }
        };

       // super.onCreate(savedInstanceState);
        instance = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        instance = this;
        Core lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
        if (lc != null) {
            lc.addListener(mListener);
        }

        alreadyAcceptedOrDeniedCall = false;
        mCall = null;

        // Only one call ringing at a time is allowed
        lookupCurrentCall();
        if (mCall == null) {
            //The incoming call no longer exists.
            Log.d("Couldn't find incoming call");
            finish();
            return;
        }
        Address address = mCall.getRemoteAddress();
        LinphoneContact contact = ContactsManager.getInstance().findContactFromAddress(address);
        if (contact != null) {
            LinphoneUtils.setBgPictureFromUri(this, topLayout, contact.getPhotoUri(), contact.getThumbnailUri());
            LinphoneUtils.setImagePictureFromUri(this, contactPicture, contact.getPhotoUri(), contact.getThumbnailUri());
            name.setText(contact.getFullName());
        } else {
            name.setText(LinphoneUtils.getAddressDisplayName(address));
            String sip = LinphoneUtils.getAddressDisplayName(address);
            if (sip != null && !"".equals(sip)) {
                Cursor cursor = DataService.instance().getFriendForSip(sip);
                String account = "";
                String device = "";
                if (cursor != null && cursor.moveToFirst()) {
                    account = cursor.getString(DatabaseUtil.Friend.COLUMN_ACCOUNT);
                    if (sip.equals(cursor.getString(DatabaseUtil.Friend.COLUMN_DEVICE_SIP))) {
                        device = cursor.getString(DatabaseUtil.Friend.COLUMN_ID);
                    }
                    name.setText(account + " " + device);
                    LinphoneManager.getLc().setCallLogsFriend(account, device);
                } else {
                    if (cursor != null) cursor.close();
                    cursor = DataService.instance().getAccountForSip(sip);
                    if (cursor != null && cursor.moveToFirst()) {
                        account = cursor.getString(DatabaseUtil.Account.COLUMN_ACCOUNT);
                        if (sip.equals(cursor.getString(DatabaseUtil.Account.COLUMN_DEVICE_SIP))) {
                            device = cursor.getString(DatabaseUtil.Account.COLUMN_ID);
                        }
                        name.setText(account + " " + device);
                        LinphoneManager.getLc().setCallLogsFriend(account, device);
                    }
                }
                if (cursor != null) cursor.close();
            }
        }
        number.setText(address.asStringUriOnly());
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkAndRequestCallPermissions();
    }

    @Override
    protected void onPause() {
        Core lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
        if (lc != null) {
            lc.removeListener(mListener);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (LinphoneManager.isInstanciated() && (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME)) {
            LinphoneManager.getLc().terminateCall(mCall);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void lookupCurrentCall() {
        if (LinphoneManager.getLcIfManagerNotDestroyedOrNull() != null) {
            List<Call> calls = LinphoneUtils.getCalls(LinphoneManager.getLc());
            for (Call call : calls) {
                if (State.IncomingReceived == call.getState()) {
                    mCall = call;
                    break;
                }
            }
        }
    }

    private void decline() {
        if (alreadyAcceptedOrDeniedCall) {
            return;
        }
        alreadyAcceptedOrDeniedCall = true;

        LinphoneManager.getLc().terminateCall(mCall);
        finish();
    }

    private void answer(boolean isVedio) {
        if(null!= mCamera) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }

        if (alreadyAcceptedOrDeniedCall) {
            return;
        }
        alreadyAcceptedOrDeniedCall = true;

        CallParams params = LinphoneManager.getLc().createCallParams(mCall);
        params.enableVideo(isVedio);

        boolean isLowBandwidthConnection = !LinphoneUtils.isHighBandwidthConnection(LinphoneService.instance().getApplicationContext());

        if (params != null) {
            params.enableLowBandwidth(isLowBandwidthConnection);
        } else {
            Log.e("Could not create call params for call");
        }

        if (params == null || !LinphoneManager.getInstance().acceptCallWithParams(mCall, params)) {
            // the above method takes care of Samsung Galaxy S
            Toast.makeText(this, R.string.couldnt_accept_call, Toast.LENGTH_LONG).show();
        } else {
            if (!BeautyMirrorActivity.isInstanciated()) {
                return;
            }
            LinphoneManager.getInstance().routeAudioToReceiver();
            BeautyMirrorActivity.instance().startIncallActivity(mCall);
        }
    }

    private void checkAndRequestCallPermissions() {
        ArrayList<String> permissionsList = new ArrayList<String>();

        int recordAudio = getPackageManager().checkPermission(Manifest.permission.RECORD_AUDIO, getPackageName());
        Log.i("[Permission] Record audio permission is " + (recordAudio == PackageManager.PERMISSION_GRANTED ? "granted" : "denied"));
        int camera = getPackageManager().checkPermission(Manifest.permission.CAMERA, getPackageName());
        Log.i("[Permission] Camera permission is " + (camera == PackageManager.PERMISSION_GRANTED ? "granted" : "denied"));

        if (recordAudio != PackageManager.PERMISSION_GRANTED) {
            if (LinphonePreferences.instance().firstTimeAskingForPermission(Manifest.permission.RECORD_AUDIO) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                Log.i("[Permission] Asking for record audio");
                permissionsList.add(Manifest.permission.RECORD_AUDIO);
            }
        }
        if (LinphonePreferences.instance().shouldInitiateVideoCall() || LinphonePreferences.instance().shouldAutomaticallyAcceptVideoRequests()) {
            if (camera != PackageManager.PERMISSION_GRANTED) {
                if (LinphonePreferences.instance().firstTimeAskingForPermission(Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    Log.i("[Permission] Asking for camera");
                    permissionsList.add(Manifest.permission.CAMERA);
                }
            }
        }

        if (permissionsList.size() > 0) {
            String[] permissions = new String[permissionsList.size()];
            permissions = permissionsList.toArray(permissions);
            ActivityCompat.requestPermissions(this, permissions, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            Log.i("[Permission] " + permissions[i] + " is " + (grantResults[i] == PackageManager.PERMISSION_GRANTED ? "granted" : "denied"));
        }
    }


    //videoCaptureSurface
    @Override
    public void surfaceCreated(SurfaceHolder holder){
        mCamera = getCameraInstance();
        if (null != mCamera) {
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
                Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
        refreshCamera();
        int rotation = getDisplayOrientation();
        if (null != mCamera) {
            mCamera.setDisplayOrientation(rotation);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        mHolder.removeCallback(this);
        if(null!= mCamera){
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(1);
        } catch(Exception e){
            Log.d("TAG", "camera is not available");
        }
        return c;
    }

    private int getDisplayOrientation(){
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int rotation = display.getRotation();
        int degrees = 0;
        switch (rotation){
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        android.hardware.Camera.CameraInfo camInfo =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, camInfo);
        int result = (camInfo.orientation - degrees + 360) % 360;
        return result;
    }

    private void refreshCamera(){
        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }
        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch(Exception e){
            // ignore: tried to stop a non-existent preview
        }
        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {

        }
    }

}