package com.gome.beautymirror.call;

/*
CallOutgoingActivity.java
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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout;

import com.gome.beautymirror.LinphoneManager;
import com.gome.beautymirror.LinphonePreferences;
import com.gome.beautymirror.LinphoneUtils;
import com.gome.beautymirror.R;
import com.gome.beautymirror.activities.BeautyMirrorActivity;
import com.gome.beautymirror.activities.BeautyMirrorGenericActivity;
import com.gome.beautymirror.contacts.ContactsManager;
import com.gome.beautymirror.contacts.LinphoneContact;
import org.linphone.core.Address;
import org.linphone.core.Call;
import org.linphone.core.Call.State;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.Reason;
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
import com.gome.beautymirror.data.DataUtil;
import com.gome.beautymirror.data.DataService;
import com.gome.beautymirror.data.provider.DatabaseUtil;

public class CallOutgoingActivity extends BeautyMirrorGenericActivity implements OnClickListener ,SurfaceHolder.Callback{
    private static CallOutgoingActivity instance;

    private TextView name, number;
    private ImageView micro, speaker,contactPicture;
    private Call mCall;
    private CoreListenerStub mListener;
    private boolean isMicMuted, isSpeakerEnabled;
    private RelativeLayout topLayout,mRlContactDetail;
    private FrameLayout hangUp;
    //videoCaptureSurface
    private Camera mCamera;
    private SurfaceHolder mHolder;
    private SurfaceView mView;
    private static final String TAG = "CallOutgoingActivity";

    public static CallOutgoingActivity instance() {
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
        setContentView(R.layout.call_outgoing);

        mRlContactDetail = findViewById(R.id.contact_detail);
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        mRlContactDetail.setPadding(0, getResources().getDimensionPixelSize(resourceId),0,0);
        name = findViewById(R.id.contact_name);
        number = findViewById(R.id.contact_number);
        topLayout= findViewById(R.id.topLayout);
        contactPicture = findViewById(R.id.contact_picture);

        isMicMuted = false;
        isSpeakerEnabled = false;

        micro = findViewById(R.id.micro);
        micro.setOnClickListener(this);
        speaker = findViewById(R.id.speaker);
        speaker.setOnClickListener(this);

        // set this flag so this activity will stay in front of the keyguard
        int flags = WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;
        getWindow().addFlags(flags);

        hangUp = findViewById(R.id.outgoing_hang_up);
        hangUp.setOnClickListener(this);
        //videoCaptureSurface
        mView = (SurfaceView) findViewById(R.id.videoCaptureSurface);
        mHolder = mView.getHolder();
        mHolder.addCallback(this);
        if(LinphonePreferences.instance().shouldInitiateVideoCall()){
            mView.setVisibility(View.VISIBLE);
        }

        mListener = new CoreListenerStub() {
            @Override
            public void onCallStateChanged(Core lc, Call call, Call.State state, String message) {
                if (call == mCall && State.Connected == state) {
                    if (!BeautyMirrorActivity.isInstanciated()) {
                        return;
                    }
                    if(null!= mCamera) {
                        mCamera.setPreviewCallback(null);
                        mCamera.stopPreview();
                        mCamera.release();
                        mCamera = null;
                    }
                    BeautyMirrorActivity.instance().startIncallActivity(mCall);
                    finish();
                    return;
                } else if (state == State.Error) {
                    // Convert Core message for internalization
                    if (call.getErrorInfo().getReason() == Reason.Declined) {
                        displayCustomToast(getString(R.string.error_call_declined), Toast.LENGTH_SHORT);
                        decline();
                    } else if (call.getErrorInfo().getReason() == Reason.NotFound) {
                        displayCustomToast(getString(R.string.error_user_not_found), Toast.LENGTH_SHORT);
                        decline();
                    } else if (call.getErrorInfo().getReason() == Reason.NotAcceptable) {
                        displayCustomToast(getString(R.string.error_incompatible_media), Toast.LENGTH_SHORT);
                        decline();
                    } else if (call.getErrorInfo().getReason() == Reason.Busy) {
                        displayCustomToast(getString(R.string.error_user_busy), Toast.LENGTH_SHORT);
                        decline();
                    } else if (message != null) {
                        displayCustomToast(getString(R.string.error_unknown) + " - " + message, Toast.LENGTH_SHORT);
                        decline();
                    }
                } else if (state == State.End) {
                    // Convert Core message for internalization
                    if (call.getErrorInfo().getReason() == Reason.Declined) {
                        displayCustomToast(getString(R.string.error_call_declined), Toast.LENGTH_SHORT);
                        decline();
                    }
                }

                if (LinphoneManager.getLc().getCallsNb() == 0) {
                    finish();
                    return;
                }
            }
        };
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

        mCall = null;

        // Only one call ringing at a time is allowed
        if (LinphoneManager.getLcIfManagerNotDestroyedOrNull() != null) {
            List<Call> calls = LinphoneUtils.getCalls(LinphoneManager.getLc());
            for (Call call : calls) {
                State cstate = call.getState();
                if (State.OutgoingInit == cstate || State.OutgoingProgress == cstate
                        || State.OutgoingRinging == cstate || State.OutgoingEarlyMedia == cstate) {
                    mCall = call;
                    break;
                }
                if (State.StreamsRunning == cstate) {
                    if (!BeautyMirrorActivity.isInstanciated()) {
                        return;
                    }
                    BeautyMirrorActivity.instance().startIncallActivity(mCall);
                    finish();
                    return;
                }
            }
        }
        if (mCall == null) {
            Log.e("Couldn't find outgoing call");
            finish();
            return;
        }

        Address address = mCall.getRemoteAddress();
        LinphoneContact contact = ContactsManager.getInstance().findContactFromAddress(address);
        byte[] icon = null;
        if (contact != null) {
            LinphoneUtils.setBgPictureFromUri(this, topLayout, contact.getPhotoUri(), contact.getThumbnailUri());
            icon = contact.getIcon();
            if (icon != null) {
                contactPicture.setImageBitmap(DataUtil.getImage(icon));
            }
            name.setText(contact.getFullName());
        } else {
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
                    icon = cursor.getBlob(DatabaseUtil.Friend.COLUMN_ICON);
                    if (icon != null) {
                        contactPicture.setImageBitmap(DataUtil.getImage(icon));
                    }
                    LinphoneManager.getLc().setCallLogsFriend(account, device);
                } else {
                    if (cursor != null) cursor.close();
                    cursor = DataService.instance().getAccountForSip(sip);
                    if (cursor != null && cursor.moveToFirst()) {
                        account = cursor.getString(DatabaseUtil.Account.COLUMN_ACCOUNT);
                        if (sip.equals(cursor.getString(DatabaseUtil.Account.COLUMN_DEVICE_SIP))) {
                            device = cursor.getString(DatabaseUtil.Account.COLUMN_ID);
                        }
                        icon = cursor.getBlob(DatabaseUtil.Account.COLUMN_ICON);
                        if (icon != null) {
                            contactPicture.setImageBitmap(DataUtil.getImage(icon));
                        }
                        name.setText(account + " " + device);
                        LinphoneManager.getLc().setCallLogsFriend(account, device);
                    } else {
                        name.setText(account + " " + device);
                        LinphoneManager.getLc().setCallLogsFriend(sip, device);
                    }
                }
                if (cursor != null) cursor.close();
            } else{
                name.setText(LinphoneUtils.getAddressDisplayName(address));
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
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.micro) {
            isMicMuted = !isMicMuted;
            if (isMicMuted) {
                micro.setImageResource(R.drawable.micro_selected);
            } else {
                micro.setImageResource(R.drawable.micro_default);
            }
            LinphoneManager.getLc().enableMic(!isMicMuted);
        }
        if (id == R.id.speaker) {
            isSpeakerEnabled = !isSpeakerEnabled;
            if (isSpeakerEnabled) {
                speaker.setImageResource(R.drawable.speaker_selected);
            } else {
                speaker.setImageResource(R.drawable.speaker_default);
            }
            LinphoneManager.getInstance().enableSpeaker(isSpeakerEnabled);
        }
        if (id == R.id.outgoing_hang_up) {
            Toast.makeText(this,R.string.cancel_call,Toast.LENGTH_LONG).show();
            decline();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (LinphoneManager.isInstanciated() && (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME)) {
            LinphoneManager.getLc().terminateCall(mCall);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void displayCustomToast(final String message, final int duration) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast, (ViewGroup) findViewById(R.id.toastRoot));

        TextView toastText = layout.findViewById(R.id.toastMessage);
        toastText.setText(message);

        final Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }

    private void decline() {
        LinphoneManager.getLc().terminateCall(mCall);
        finish();
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
