package com.gome.beautymirror.call;

/*
CallActivity.java
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
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import org.linphone.core.AddressFamily;
import org.linphone.core.Call;
import org.linphone.core.Call.State;
import org.linphone.core.CallListenerStub;
import org.linphone.core.CallParams;
import org.linphone.core.CallStats;
import org.linphone.core.ChatMessage;
import org.linphone.core.ChatRoom;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.MediaEncryption;
import org.linphone.core.PayloadType;
import org.linphone.core.Player;
import org.linphone.core.StreamType;
import org.linphone.mediastream.Log;
import org.linphone.mediastream.video.capture.hwconf.AndroidCameraConfiguration;
import com.gome.beautymirror.receivers.BluetoothManager;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CallActivity extends BeautyMirrorGenericActivity implements OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private final static int SECONDS_BEFORE_HIDING_CONTROLS = 5000;
    private final static int SECONDS_BEFORE_DENYING_CALL_UPDATE = 30000;
    private static final int PERMISSIONS_REQUEST_CAMERA = 202;
    private static final int PERMISSIONS_ENABLED_CAMERA = 203;
    private static final int PERMISSIONS_ENABLED_MIC = 204;

    private static CallActivity instance;

    private Handler mControlsHandler = new Handler();
    private Runnable mControls;
    private ImageView switchCamera;
    private TextView mVedioAudioTitle,mSwitchCameraTitle;
    private RelativeLayout topLayout;
    private ImageView hangUp, video, micro, speaker, options, addCall, transfer, conference, conferenceStatus;
    private ImageView mContactPicture;
    private TextView mContactName;
    private LinearLayout mContactInfo;
    private ImageView audioRoute, routeSpeaker, routeEarpiece, routeBluetooth, menu, chat;
    private ProgressBar videoProgress;
    private CallAudioFragment audioCallFragment;
    private CallVideoFragment videoCallFragment;
    private boolean isSpeakerEnabled = false, isMicMuted = false, isTransferAllowed, isVideoAsk;
    private LinearLayout mControlsLayout;
    private int cameraNumber;
    private CountDownTimer timer;
    private boolean isVideoCallPaused = false;
    private Dialog dialog = null;
    private static long TimeRemind = 0;
    private HeadsetReceiver headsetReceiver;

    private LinearLayout conferenceList;
    private LayoutInflater inflater;
    private ViewGroup container;
    private boolean isConferenceRunning = false;
    private CoreListenerStub mListener;
    private HashMap<String, String> mEncoderTexts;
    private HashMap<String, String> mDecoderTexts;

    private boolean oldIsSpeakerEnabled = false;

    public static CallActivity instance() {
        return instance;
    }

    public static boolean isInstanciated() {
        return instance != null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;

        if (getResources().getBoolean(R.bool.orientation_portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        setContentView(R.layout.call);
        //Earset Connectivity Broadcast Processing
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
        headsetReceiver = new HeadsetReceiver();
        registerReceiver(headsetReceiver, intentFilter);

        isTransferAllowed = getApplicationContext().getResources().getBoolean(R.bool.allow_transfers);

        cameraNumber = AndroidCameraConfiguration.retrieveCameras().length;

        mEncoderTexts = new HashMap<>();
        mDecoderTexts = new HashMap<>();

        mListener = new CoreListenerStub() {
            @Override
            public void onMessageReceived(Core lc, ChatRoom cr, ChatMessage message) {
            }

            @Override
            public void onCallStateChanged(Core lc, final Call call, Call.State state, String message) {
                if (LinphoneManager.getLc().getCallsNb() == 0) {
                    LinphoneService.instance().removeSasNotification();
                    finish();
                    return;
                }

                if (state == State.IncomingReceived) {
                    startIncomingCallActivity();
                    return;
                } else if (state == State.Paused || state == State.PausedByRemote || state == State.Pausing) {
                    if (LinphoneManager.getLc().getCurrentCall() != null) {
                        enabledVideoButton(false);
                    }
                    if (isVideoEnabled(call)) {
                        showAudioView();
                    }
                } else if (state == State.Resuming) {
                    if (LinphonePreferences.instance().isVideoEnabled()) {
                        if (call.getCurrentParams().videoEnabled()) {
                            showVideoView();
                        }
                    }
                    if (LinphoneManager.getLc().getCurrentCall() != null) {
                        enabledVideoButton(true);
                    }
                } else if (state == State.StreamsRunning) {
                    switchVideo(isVideoEnabled(call));
                    enableAndRefreshInCallActions();
                } else if (state == State.UpdatedByRemote) {
                    // If the correspondent proposes video while audio call
                    boolean videoEnabled = LinphonePreferences.instance().isVideoEnabled();
                    if (!videoEnabled) {
                        acceptCallUpdate(false);
                    }

                    boolean remoteVideo = call.getRemoteParams().videoEnabled();
                    boolean localVideo = call.getCurrentParams().videoEnabled();
                    boolean autoAcceptCameraPolicy = LinphonePreferences.instance().shouldAutomaticallyAcceptVideoRequests();
                    if (remoteVideo && !localVideo && !autoAcceptCameraPolicy && !LinphoneManager.getLc().isInConference()) {
                        showAcceptCallUpdateDialog();
                        createTimerForDialog(SECONDS_BEFORE_DENYING_CALL_UPDATE);
                    }
                }

                refreshIncallUi();
                transfer.setEnabled(LinphoneManager.getLc().getCurrentCall() != null);
            }

            @Override
            public void onCallEncryptionChanged(Core lc, final Call call, boolean encrypted, String authenticationToken) {
            }

        };

        if (findViewById(R.id.fragmentContainer) != null) {
            initUI();

            if (LinphoneManager.getLc().getCallsNb() > 0) {
                Call call = LinphoneManager.getLc().getCalls()[0];

                if (LinphoneUtils.isCallEstablished(call)) {
                    enableAndRefreshInCallActions();
                }
            }
            if (savedInstanceState != null) {
                // Fragment already created, no need to create it again (else it will generate a memory leak with duplicated fragments)
                isSpeakerEnabled = savedInstanceState.getBoolean("Speaker");
                isMicMuted = savedInstanceState.getBoolean("Mic");
                isVideoCallPaused = savedInstanceState.getBoolean("VideoCallPaused");
                if (savedInstanceState.getBoolean("AskingVideo")) {
                    showAcceptCallUpdateDialog();
                    TimeRemind = savedInstanceState.getLong("TimeRemind");
                    createTimerForDialog(TimeRemind);
                }
                refreshInCallActions();
                return;
            } else {
                isSpeakerEnabled = LinphoneManager.getInstance().isSpeakerEnabled();
                isMicMuted = !LinphoneManager.getLc().micEnabled();
            }

            Fragment callFragment;
            if (isVideoEnabled(LinphoneManager.getLc().getCurrentCall())) {
                callFragment = new CallVideoFragment();
                videoCallFragment = (CallVideoFragment) callFragment;
                displayVideoCall(false);
                LinphoneManager.getInstance().routeAudioToSpeaker();
                isSpeakerEnabled = true;
            } else {
                callFragment = new CallAudioFragment();
                audioCallFragment = (CallAudioFragment) callFragment;
            }

            if (BluetoothManager.getInstance().isBluetoothHeadsetAvailable()) {
                BluetoothManager.getInstance().routeAudioToBluetooth();
            }

            callFragment.setArguments(getIntent().getExtras());
            getFragmentManager().beginTransaction().add(R.id.fragmentContainer, callFragment).commitAllowingStateLoss();
        }
    }

    public void createTimerForDialog(long time) {
        timer = new CountDownTimer(time, 1000) {
            public void onTick(long millisUntilFinished) {
                TimeRemind = millisUntilFinished;
            }

            public void onFinish() {
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
                acceptCallUpdate(false);
            }
        }.start();
    }

    private boolean isVideoEnabled(Call call) {
        if (call != null) {
            return call.getCurrentParams().videoEnabled();
        }
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("Speaker", LinphoneManager.getInstance().isSpeakerEnabled());
        outState.putBoolean("Mic", !LinphoneManager.getLc().micEnabled());
        outState.putBoolean("VideoCallPaused", isVideoCallPaused);
        outState.putBoolean("AskingVideo", isVideoAsk);
        outState.putLong("TimeRemind", TimeRemind);
        if (dialog != null) dialog.dismiss();
        super.onSaveInstanceState(outState);
    }

    private boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }

    private void initUI() {
        inflater = LayoutInflater.from(this);
        container = findViewById(R.id.topLayout);
        topLayout = findViewById(R.id.topLayout);
        conferenceList = findViewById(R.id.conference_list);

        mContactPicture = findViewById(R.id.contact_picture);
        mContactName = findViewById(R.id.contact_name);
        mContactInfo = findViewById(R.id.contact_info);

        //TopBar
        video = findViewById(R.id.video);
        video.setOnClickListener(this);
        enabledVideoButton(false);

        videoProgress = findViewById(R.id.video_in_progress);
        videoProgress.setVisibility(View.GONE);

        micro = findViewById(R.id.micro);
        micro.setOnClickListener(this);

        speaker = findViewById(R.id.speaker);
        speaker.setOnClickListener(this);

        options = findViewById(R.id.options);
        options.setOnClickListener(this);
        options.setEnabled(false);

        //BottonBar
        hangUp = findViewById(R.id.hang_up);
        hangUp.setOnClickListener(this);

        //Options
        addCall = findViewById(R.id.add_call);
        addCall.setOnClickListener(this);
        addCall.setEnabled(false);

        transfer = findViewById(R.id.transfer);
        transfer.setOnClickListener(this);
        transfer.setEnabled(false);

        conference = findViewById(R.id.conference);
        conference.setEnabled(false);
        conference.setOnClickListener(this);

        try {
            audioRoute = findViewById(R.id.audio_route);
            audioRoute.setOnClickListener(this);
            routeSpeaker = findViewById(R.id.route_speaker);
            routeSpeaker.setOnClickListener(this);
            routeEarpiece = findViewById(R.id.route_earpiece);
            routeEarpiece.setOnClickListener(this);
            routeBluetooth = findViewById(R.id.route_bluetooth);
            routeBluetooth.setOnClickListener(this);
        } catch (NullPointerException npe) {
            Log.e("Bluetooth: Audio routes menu disabled on tablets for now (1)");
        }

        switchCamera = findViewById(R.id.switchCamera);
        switchCamera.setOnClickListener(this);

        mSwitchCameraTitle = findViewById(R.id.switchCameraTitle);
        mVedioAudioTitle = findViewById(R.id.videoAudioTitle);

        mControlsLayout = findViewById(R.id.menu);

        if (!isTransferAllowed) {
            addCall.setBackgroundResource(R.drawable.options_add_call);
        }

        if (BluetoothManager.getInstance().isBluetoothHeadsetAvailable()) {
            try {
                audioRoute.setVisibility(View.VISIBLE);
                speaker.setVisibility(View.GONE);
            } catch (NullPointerException npe) {
                Log.e("Bluetooth: Audio routes menu disabled on tablets for now (2)");
            }
        } else {
            try {
                audioRoute.setVisibility(View.GONE);
                speaker.setVisibility(View.VISIBLE);
            } catch (NullPointerException npe) {
                Log.e("Bluetooth: Audio routes menu disabled on tablets for now (3)");
            }
        }
        LinphoneManager.getInstance().changeStatusToOnThePhone();
    }

    public void checkAndRequestPermission(String permission, int result) {
        int permissionGranted = getPackageManager().checkPermission(permission, getPackageName());
        Log.i("[Permission] " + permission + " is " + (permissionGranted == PackageManager.PERMISSION_GRANTED ? "granted" : "denied"));

        if (permissionGranted != PackageManager.PERMISSION_GRANTED) {
            if (LinphonePreferences.instance().firstTimeAskingForPermission(permission) || ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                Log.i("[Permission] Asking for " + permission);
                ActivityCompat.requestPermissions(this, new String[]{permission}, result);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, final int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            Log.i("[Permission] " + permissions[i] + " is " + (grantResults[i] == PackageManager.PERMISSION_GRANTED ? "granted" : "denied"));
        }

        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAMERA:
                LinphoneUtils.dispatchOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        acceptCallUpdate(grantResults[0] == PackageManager.PERMISSION_GRANTED);
                    }
                });
                break;
            case PERMISSIONS_ENABLED_CAMERA:
                LinphoneUtils.dispatchOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        disableVideo(grantResults[0] != PackageManager.PERMISSION_GRANTED);
                    }
                });
                break;
            case PERMISSIONS_ENABLED_MIC:
                LinphoneUtils.dispatchOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            toggleMicro();
                        }
                    }
                });
                break;
        }
    }

    private void refreshIncallUi() {
        refreshInCallActions();
        refreshCallList(getResources());
        enableAndRefreshInCallActions();
    }

    public void setSpeakerEnabled(boolean enabled) {
        isSpeakerEnabled = enabled;
    }

    public void refreshInCallActions() {
        if (!LinphonePreferences.instance().isVideoEnabled() || isConferenceRunning) {
            enabledVideoButton(false);
        } else {
            if (video.isEnabled()) {
                if (isVideoEnabled(LinphoneManager.getLc().getCurrentCall())) {
                    video.setImageResource(R.drawable.camera_selected);
                    videoProgress.setVisibility(View.INVISIBLE);
                } else {
                    video.setImageResource(R.drawable.camera_button);
                }
            } else {
                video.setImageResource(R.drawable.camera_button);
            }
        }
        if (getPackageManager().checkPermission(Manifest.permission.CAMERA, getPackageName()) != PackageManager.PERMISSION_GRANTED) {
            video.setImageResource(R.drawable.camera_button);
        }

        if (isSpeakerEnabled) {
            speaker.setImageResource(R.drawable.speaker_selected);
        } else {
            speaker.setImageResource(R.drawable.speaker_default);
        }

        if (getPackageManager().checkPermission(Manifest.permission.RECORD_AUDIO, getPackageName()) != PackageManager.PERMISSION_GRANTED) {
            isMicMuted = true;
        }
        if (isMicMuted) {
            micro.setImageResource(R.drawable.micro_selected);
        } else {
            micro.setImageResource(R.drawable.micro_default);
        }

        try {
            routeSpeaker.setImageResource(R.drawable.route_speaker);
            if (BluetoothManager.getInstance().isUsingBluetoothAudioRoute()) {
                isSpeakerEnabled = false; // We need this if isSpeakerEnabled wasn't set correctly
                routeEarpiece.setImageResource(R.drawable.route_earpiece);
                routeBluetooth.setImageResource(R.drawable.route_bluetooth_selected);
                return;
            } else {
                routeEarpiece.setImageResource(R.drawable.route_earpiece_selected);
                routeBluetooth.setImageResource(R.drawable.route_bluetooth);
            }

            if (isSpeakerEnabled) {
                routeSpeaker.setImageResource(R.drawable.route_speaker_selected);
                routeEarpiece.setImageResource(R.drawable.route_earpiece);
                routeBluetooth.setImageResource(R.drawable.route_bluetooth);
            }
        } catch (NullPointerException npe) {
            Log.e("Bluetooth: Audio routes menu disabled on tablets for now (4)");
        }
    }

    private void enableAndRefreshInCallActions() {
        int confsize = 0;

        if (LinphoneManager.getLc().isInConference()) {
            confsize = LinphoneManager.getLc().getConferenceSize() - (LinphoneManager.getLc().getConference() != null ? 1 : 0);
        }

        //Enabled transfer button
        if (isTransferAllowed && !LinphoneManager.getLc().soundResourcesLocked())
            enabledTransferButton(true);

        //Enable conference button
        if (LinphoneManager.getLc().getCallsNb() > 1 && LinphoneManager.getLc().getCallsNb() > confsize && !LinphoneManager.getLc().soundResourcesLocked()) {
            enabledConferenceButton(true);
        } else {
            enabledConferenceButton(false);
        }

        addCall.setEnabled(LinphoneManager.getLc().getCallsNb() < LinphoneManager.getLc().getMaxCalls() && !LinphoneManager.getLc().soundResourcesLocked());
        options.setEnabled(!getResources().getBoolean(R.bool.disable_options_in_call) && (addCall.isEnabled() || transfer.isEnabled()));

        if (LinphoneManager.getLc().getCurrentCall() != null && LinphonePreferences.instance().isVideoEnabled() && !LinphoneManager.getLc().getCurrentCall().mediaInProgress()) {
            enabledVideoButton(true);
        } else {
            enabledVideoButton(false);
        }
        micro.setEnabled(true);
        if (!isTablet()) {
            speaker.setEnabled(true);
        }
        transfer.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (isVideoEnabled(LinphoneManager.getLc().getCurrentCall())) {
            //displayVideoCallControlsIfHidden();
        }

        if (id == R.id.video) {
            int camera = getPackageManager().checkPermission(Manifest.permission.CAMERA, getPackageName());
            Log.i("[Permission] Camera permission is " + (camera == PackageManager.PERMISSION_GRANTED ? "granted" : "denied"));

            if (camera == PackageManager.PERMISSION_GRANTED) {
                disableVideo(isVideoEnabled(LinphoneManager.getLc().getCurrentCall()));
            } else {
                checkAndRequestPermission(Manifest.permission.CAMERA, PERMISSIONS_ENABLED_CAMERA);

            }
        } else if (id == R.id.micro) {
            int recordAudio = getPackageManager().checkPermission(Manifest.permission.RECORD_AUDIO, getPackageName());
            Log.i("[Permission] Record audio permission is " + (recordAudio == PackageManager.PERMISSION_GRANTED ? "granted" : "denied"));

            if (recordAudio == PackageManager.PERMISSION_GRANTED) {
                toggleMicro();
            } else {
                checkAndRequestPermission(Manifest.permission.RECORD_AUDIO, PERMISSIONS_ENABLED_MIC);
            }
        } else if (id == R.id.speaker) {
            toggleSpeaker();
        } else if (id == R.id.add_call) {
            goBackToDialer();
        } else if (id == R.id.hang_up) {
            hangUp();
        } else if (id == R.id.conference) {
            enterConference();
            hideOrDisplayCallOptions();
        } else if (id == R.id.switchCamera) {
            if (videoCallFragment != null) {
                videoCallFragment.switchCamera();
            }
        } else if (id == R.id.transfer) {
            goBackToDialerAndDisplayTransferButton();
        } else if (id == R.id.options) {
            hideOrDisplayCallOptions();
        } else if (id == R.id.audio_route) {
            hideOrDisplayAudioRoutes();
        } else if (id == R.id.route_bluetooth) {
            if (BluetoothManager.getInstance().routeAudioToBluetooth()) {
                isSpeakerEnabled = false;
                routeBluetooth.setImageResource(R.drawable.route_bluetooth_selected);
                routeSpeaker.setImageResource(R.drawable.route_speaker);
                routeEarpiece.setImageResource(R.drawable.route_earpiece);
            }
            hideOrDisplayAudioRoutes();
        } else if (id == R.id.route_earpiece) {
            LinphoneManager.getInstance().routeAudioToReceiver();
            isSpeakerEnabled = false;
            routeBluetooth.setImageResource(R.drawable.route_bluetooth);
            routeSpeaker.setImageResource(R.drawable.route_speaker);
            routeEarpiece.setImageResource(R.drawable.route_earpiece_selected);
            hideOrDisplayAudioRoutes();
        } else if (id == R.id.route_speaker) {
            LinphoneManager.getInstance().routeAudioToSpeaker();
            isSpeakerEnabled = true;
            routeBluetooth.setImageResource(R.drawable.route_bluetooth);
            routeSpeaker.setImageResource(R.drawable.route_speaker_selected);
            routeEarpiece.setImageResource(R.drawable.route_earpiece);
            hideOrDisplayAudioRoutes();
        } else if (id == R.id.call_pause) {
            Call call = (Call) v.getTag();
        } else if (id == R.id.conference_pause) {
            pauseOrResumeConference();
        }
    }

    private void enabledVideoButton(boolean enabled) {
        if (enabled) {
            video.setEnabled(true);
        } else {
            video.setEnabled(false);
        }
    }

    private void enabledTransferButton(boolean enabled) {
        if (enabled) {
            transfer.setEnabled(true);
        } else {
            transfer.setEnabled(false);
        }
    }

    private void enabledConferenceButton(boolean enabled) {
        if (enabled) {
            conference.setEnabled(true);
        } else {
            conference.setEnabled(false);
        }
    }

    private void disableVideo(final boolean videoDisabled) {
        final Call call = LinphoneManager.getLc().getCurrentCall();
        if (call == null) {
            return;
        }

        if (videoDisabled) {
            CallParams params = LinphoneManager.getLc().createCallParams(call);
            params.enableVideo(false);
            LinphoneManager.getLc().updateCall(call, params);
        } else {
            videoProgress.setVisibility(View.VISIBLE);
            if (call.getRemoteParams() != null && !call.getRemoteParams().lowBandwidthEnabled()) {
                LinphoneManager.getInstance().addVideo();
            } else {
                displayCustomToast(getString(R.string.error_low_bandwidth), Toast.LENGTH_LONG);
            }
        }
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

    private void switchVideo(final boolean displayVideo) {
        final Call call = LinphoneManager.getLc().getCurrentCall();
        if (call == null) {
            return;
        }

        //Check if the call is not terminated
        if (call.getState() == State.End || call.getState() == State.Released) return;

        if (!displayVideo) {
            showAudioView();
        } else {
            if (!call.getRemoteParams().lowBandwidthEnabled()) {
                LinphoneManager.getInstance().addVideo();
                if (videoCallFragment == null || !videoCallFragment.isVisible())
                    showVideoView();
            } else {
                displayCustomToast(getString(R.string.error_low_bandwidth), Toast.LENGTH_LONG);
            }
        }
    }

    private void showAudioView() {
        if (LinphoneManager.getLc().getCurrentCall() != null) {
            if (!isSpeakerEnabled) {
                LinphoneManager.getInstance().enableProximitySensing(true);
            }
        }

        replaceFragmentVideoByAudio();
        displayAudioCall();
        removeCallbacks();
    }

    private void showVideoView() {
        if (!BluetoothManager.getInstance().isBluetoothHeadsetAvailable()) {
            Log.w("Bluetooth not available, using speaker");
            LinphoneManager.getInstance().routeAudioToSpeaker();
            isSpeakerEnabled = true;
        }
        refreshInCallActions();

        LinphoneManager.getInstance().enableProximitySensing(false);

        replaceFragmentAudioByVideo();
        hideStatusBar();
    }

    private void displayAudioCall() {
        mControlsLayout.setVisibility(View.VISIBLE);
        switchCamera.setVisibility(View.GONE);
        mSwitchCameraTitle.setVisibility(View.GONE);
        mVedioAudioTitle.setText(getString(R.string.switch_video));
        mContactInfo.setVisibility(View.VISIBLE);
    }

    private void replaceFragmentVideoByAudio() {
        Toast.makeText(this,R.string.is_audio,Toast.LENGTH_LONG).show();
        audioCallFragment = new CallAudioFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, audioCallFragment);
        try {
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
        }
    }

    private void replaceFragmentAudioByVideo() {
        Toast.makeText(this,R.string.is_video,Toast.LENGTH_LONG).show();
//      Hiding controls to let displayVideoCallControlsIfHidden add them plus the callback
        videoCallFragment = new CallVideoFragment();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, videoCallFragment);
        try {
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
        }
        mContactInfo.setVisibility(View.GONE);
    }

    private void toggleMicro() {
        Core lc = LinphoneManager.getLc();
        isMicMuted = !isMicMuted;
        lc.enableMic(!isMicMuted);
        if (isMicMuted) {
            micro.setImageResource(R.drawable.micro_selected);
        } else {
            micro.setImageResource(R.drawable.micro_default);
        }
    }

    protected void toggleSpeaker() {
        isSpeakerEnabled = !isSpeakerEnabled;
        if (LinphoneManager.getLc().getCurrentCall() != null) {
            if (isVideoEnabled(LinphoneManager.getLc().getCurrentCall()))
                LinphoneManager.getInstance().enableProximitySensing(false);
            else
                LinphoneManager.getInstance().enableProximitySensing(!isSpeakerEnabled);
        }
        if (isSpeakerEnabled) {
            LinphoneManager.getInstance().routeAudioToSpeaker();
            speaker.setImageResource(R.drawable.speaker_selected);
            LinphoneManager.getInstance().enableSpeaker(isSpeakerEnabled);
        } else {
            Log.d("Toggle speaker off, routing back to earpiece");
            LinphoneManager.getInstance().routeAudioToReceiver();
            speaker.setImageResource(R.drawable.speaker_default);
        }
    }

    private void hangUp() {
        Core lc = LinphoneManager.getLc();
        Call currentCall = lc.getCurrentCall();

        if (currentCall != null) {
            lc.terminateCall(currentCall);
        } else if (lc.isInConference()) {
            lc.terminateConference();
        } else {
            lc.terminateAllCalls();
        }
    }

    public void displayVideoCall(boolean display) {
        if (display) {
            mControlsLayout.setVisibility(View.VISIBLE);
            if (cameraNumber > 1) {
                switchCamera.setVisibility(View.VISIBLE);
                mSwitchCameraTitle.setVisibility(View.VISIBLE);
                mVedioAudioTitle.setText(getString(R.string.switch_audio));
            }
        } else {
            hideStatusBar();
            mControlsLayout.setVisibility(View.GONE);
            switchCamera.setVisibility(View.GONE);
            mSwitchCameraTitle.setVisibility(View.GONE);
        }
    }


    public void displayVideoCallControlsIfHidden() {
        if (mControlsLayout != null) {
            if (mControlsLayout.getVisibility() != View.VISIBLE) {
                displayVideoCall(true);
            }else {
                displayVideoCall(false);
            }
            resetControlsHidingCallBack();
        }
    }

    public void resetControlsHidingCallBack() {
        if (mControlsHandler != null && mControls != null) {
            mControlsHandler.removeCallbacks(mControls);
        }
        mControls = null;

        if (isVideoEnabled(LinphoneManager.getLc().getCurrentCall()) && mControlsHandler != null) {
            mControlsHandler.postDelayed(mControls = new Runnable() {
                public void run() {
                    video.setEnabled(true);
                    transfer.setVisibility(View.INVISIBLE);
                    addCall.setVisibility(View.INVISIBLE);
                    conference.setVisibility(View.INVISIBLE);
                    displayVideoCall(false);
                    options.setImageResource(R.drawable.options_default);
                }
            }, SECONDS_BEFORE_HIDING_CONTROLS);
        }
    }

    public void removeCallbacks() {
        if (mControlsHandler != null && mControls != null) {
            mControlsHandler.removeCallbacks(mControls);
        }
        mControls = null;
    }

    private void hideOrDisplayAudioRoutes() {
        if (routeSpeaker.getVisibility() == View.VISIBLE) {
            routeSpeaker.setVisibility(View.INVISIBLE);
            routeBluetooth.setVisibility(View.INVISIBLE);
            routeEarpiece.setVisibility(View.INVISIBLE);
        } else {
            routeSpeaker.setVisibility(View.VISIBLE);
            routeBluetooth.setVisibility(View.VISIBLE);
            routeEarpiece.setVisibility(View.VISIBLE);
        }
    }

    private void hideOrDisplayCallOptions() {
        //Hide options
        if (addCall.getVisibility() == View.VISIBLE) {
            options.setImageResource(R.drawable.options_default);
            if (isTransferAllowed) {
                transfer.setVisibility(View.INVISIBLE);
            }
            addCall.setVisibility(View.INVISIBLE);
            conference.setVisibility(View.INVISIBLE);
        } else { //Display options
            if (isTransferAllowed) {
                transfer.setVisibility(View.VISIBLE);
            }
            addCall.setVisibility(View.VISIBLE);
            conference.setVisibility(View.VISIBLE);
            options.setImageResource(R.drawable.options_selected);
            transfer.setEnabled(LinphoneManager.getLc().getCurrentCall() != null);
        }
    }

    public void goBackToDialer() {
        Intent intent = new Intent();
        intent.putExtra("Transfer", false);
        setResult(Activity.RESULT_FIRST_USER, intent);
        finish();
    }

    private void goBackToDialerAndDisplayTransferButton() {
        Intent intent = new Intent();
        intent.putExtra("Transfer", true);
        setResult(Activity.RESULT_FIRST_USER, intent);
        finish();
    }

    public void acceptCallUpdate(boolean accept) {
        if (timer != null) {
            timer.cancel();
        }

        Call call = LinphoneManager.getLc().getCurrentCall();
        if (call == null) {
            return;
        }

        CallParams params = LinphoneManager.getLc().createCallParams(call);
        if (accept) {
            params.enableVideo(true);
            LinphoneManager.getLc().enableVideoCapture(true);
            LinphoneManager.getLc().enableVideoDisplay(true);
        }

        LinphoneManager.getLc().acceptCallUpdate(call, params);
    }

    public void startIncomingCallActivity() {
        startActivity(new Intent(this, CallIncomingActivity.class));
    }

    public void hideStatusBar() {
        if (isTablet()) {
            return;
        }
        findViewById(R.id.fragmentContainer).setPadding(0, 0, 0, 0);
    }

    private void showAcceptCallUpdateDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Drawable d = new ColorDrawable(ContextCompat.getColor(this, R.color.colorC));
        d.setAlpha(200);
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(d);

        TextView customText = dialog.findViewById(R.id.customText);
        customText.setText(getResources().getString(R.string.add_video_dialog));
        Button delete = dialog.findViewById(R.id.delete_button);
        delete.setText(R.string.accept);
        Button cancel = dialog.findViewById(R.id.cancel);
        cancel.setText(R.string.decline);
        isVideoAsk = true;

        delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int camera = getPackageManager().checkPermission(Manifest.permission.CAMERA, getPackageName());
                Log.i("[Permission] Camera permission is " + (camera == PackageManager.PERMISSION_GRANTED ? "granted" : "denied"));

                if (camera == PackageManager.PERMISSION_GRANTED) {
                    CallActivity.instance().acceptCallUpdate(true);
                } else {
                    checkAndRequestPermission(Manifest.permission.CAMERA, PERMISSIONS_REQUEST_CAMERA);
                }
                isVideoAsk = false;
                dialog.dismiss();
                dialog = null;
            }
        });

        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CallActivity.isInstanciated()) {
                    CallActivity.instance().acceptCallUpdate(false);
                }
                isVideoAsk = false;
                dialog.dismiss();
                dialog = null;
            }
        });
        dialog.show();
    }

    @Override
    protected void onResume() {

        instance = this;
        super.onResume();

        Core lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
        if (lc != null) {
            lc.addListener(mListener);
        }
        isSpeakerEnabled = LinphoneManager.getInstance().isSpeakerEnabled();

        refreshIncallUi();
        handleViewIntent();

        if (!isVideoEnabled(LinphoneManager.getLc().getCurrentCall())) {
            if (!isSpeakerEnabled) {
                LinphoneManager.getInstance().enableProximitySensing(true);
                removeCallbacks();
            }
        }
    }

    private void handleViewIntent() {
        Intent intent = getIntent();
        if (intent != null && intent.getAction() == "android.intent.action.VIEW") {
            Call call = LinphoneManager.getLc().getCurrentCall();
            if (call != null && isVideoEnabled(call)) {
                Player player = call.getPlayer();
                String path = intent.getData().getPath();
                Log.i("Openning " + path);
                /*int openRes = */
                player.open(path);
                /*if(openRes == -1) {
                    String message = "Could not open " + path;
                    Log.e(message);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    return;
                }*/
                Log.i("Start playing");
                /*if(*/
                player.start()/* == -1) {*/;
                    /*player.close();
                    String message = "Could not start playing " + path;
                    Log.e(message);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }*/
            }
        }
    }

    @Override
    protected void onPause() {
        Core lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
        if (lc != null) {
            lc.removeListener(mListener);
        }

        super.onPause();

        if (mControlsHandler != null && mControls != null) {
            mControlsHandler.removeCallbacks(mControls);
        }
        mControls = null;
    }

    @Override
    protected void onDestroy() {
        LinphoneManager.getInstance().changeStatusToOnline();
        LinphoneManager.getInstance().enableProximitySensing(false);

        unregisterReceiver(headsetReceiver);

        if (mControlsHandler != null && mControls != null) {
            mControlsHandler.removeCallbacks(mControls);
        }
        mControls = null;
        mControlsHandler = null;

        unbindDrawables(findViewById(R.id.topLayout));
        instance = null;
        super.onDestroy();
        System.gc();
    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ImageView) {
            view.setOnClickListener(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (LinphoneUtils.onKeyVolumeAdjust(keyCode)) return true;
        if (LinphoneUtils.onKeyBackGoHome(this, keyCode, event)) return true;
        return super.onKeyDown(keyCode, event);
    }

    @Override // Never invoke actually
    public void onBackPressed() {
        if (dialog != null) {
            acceptCallUpdate(false);
            dialog.dismiss();
            dialog = null;
        }
        return;
    }

    public void bindAudioFragment(CallAudioFragment fragment) {
        audioCallFragment = fragment;
    }

    public void bindVideoFragment(CallVideoFragment fragment) {
        videoCallFragment = fragment;
    }


    //CALL INFORMATION
    private void displayCurrentCall(Call call) {
        setBgContactPicture(topLayout, call);
        setContactInformation(mContactPicture,mContactName,call);
        registerCallDurationTimer(null, call);
    }

    private void setBgContactPicture( RelativeLayout layout, Call call) {
        LinphoneContact lContact = getContactFromAccount(call);
        if (lContact == null) {
           // layout.setBackgroundDrawable(new BitmapDrawable(ContactsManager.getInstance().getDefaultAvatarBitmap()));
        } else {
            LinphoneUtils.setBgPictureFromUri(layout.getContext(), layout, lContact.getPhotoUri(), lContact.getThumbnailUri());
        }

    }

    public LinphoneContact getContactFromAccount(Call call){
        String lAddress = call.getRemoteAddress().asStringUriOnly();
        Cursor cursor ;
        if(call.getDir() == Call.Dir.Incoming){
            lAddress = lAddress.substring(4,lAddress.length()-5);
        }else {
            lAddress = lAddress.substring(4);
        }
        cursor = com.gome.beautymirror.data.DataService.instance().getFriendForSip(lAddress);
        String account="";
        while (cursor != null && cursor.moveToNext()) {
            account = cursor.getString(com.gome.beautymirror.data.provider.DatabaseUtil.Friend.COLUMN_ACCOUNT);
        }
        return ContactsManager.getInstance().getContactFromAccount(account);
    }

    public void setContactInformation(ImageView contactPicture, TextView contactName, Call call) {
        LinphoneContact lContact = getContactFromAccount(call);
        if (lContact == null) {
            contactPicture.setImageBitmap(ContactsManager.getInstance().getDefaultAvatarBitmap());
        } else {
            LinphoneUtils.setImagePictureFromUri(contactPicture.getContext(), contactPicture, lContact.getPhotoUri(), lContact.getThumbnailUri());
            contactName.setText(lContact.getRemarkName() == null ? lContact.getFullName():lContact.getRemarkName());
        }

    }

    private boolean displayCallStatusIconAndReturnCallPaused(LinearLayout callView, Call call) {
        boolean isCallPaused, isInConference;
        ImageView onCallStateChanged = callView.findViewById(R.id.call_pause);
        onCallStateChanged.setTag(call);
        onCallStateChanged.setOnClickListener(this);

        if (call.getState() == State.Paused || call.getState() == State.PausedByRemote || call.getState() == State.Pausing) {
            onCallStateChanged.setImageResource(R.drawable.pause);
            isCallPaused = true;
            isInConference = false;
        } else if (call.getState() == State.OutgoingInit || call.getState() == State.OutgoingProgress || call.getState() == State.OutgoingRinging) {
            isCallPaused = false;
            isInConference = false;
        } else {
            isInConference = isConferenceRunning && call.getConference() != null;
            isCallPaused = false;
        }

        return isCallPaused || isInConference;
    }

    private void registerCallDurationTimer(View v, Call call) {
        int callDuration = call.getDuration();
        if (callDuration == 0 && call.getState() != State.StreamsRunning) {
            return;
        }

        Chronometer timer;
        if (v == null) {
            timer = findViewById(R.id.current_call_timer);
        } else {
            timer = v.findViewById(R.id.call_timer);
        }

        if (timer == null) {
            throw new IllegalArgumentException("no callee_duration view found");
        }

        timer.setBase(SystemClock.elapsedRealtime() - 1000 * callDuration);
        timer.start();
    }

    public void refreshCallList(Resources resources) {
        isConferenceRunning = LinphoneManager.getLc().isInConference();
        List<Call> pausedCalls = LinphoneUtils.getCallsInState(LinphoneManager.getLc(), Arrays.asList(State.PausedByRemote));

        //Active call
        if (LinphoneManager.getLc().getCurrentCall() != null) {
            displayCurrentCall(LinphoneManager.getLc().getCurrentCall());
            if (isVideoEnabled(LinphoneManager.getLc().getCurrentCall()) && !isConferenceRunning && pausedCalls.size() == 0) {
                displayVideoCall(false);
            } else {
                displayAudioCall();
            }
        } else {
            showAudioView();
        }
        //Conference
        if (isConferenceRunning) {
            displayConference(true);
        } else {
            displayConference(false);
        }

    }

    //Conference
    private void exitConference(final Call call) {
        Core lc = LinphoneManager.getLc();

        if (lc.isInConference()) {
            lc.removeFromConference(call);
            if (lc.getConferenceSize() <= 1) {
                lc.leaveConference();
            }
        }
        refreshIncallUi();
    }

    private void enterConference() {
        LinphoneManager.getLc().addAllToConference();
    }

    public void pauseOrResumeConference() {
        Core lc = LinphoneManager.getLc();
        conferenceStatus = findViewById(R.id.conference_pause);
        if (conferenceStatus != null) {
            if (lc.isInConference()) {
                conferenceStatus.setImageResource(R.drawable.pause_big_over_selected);
                lc.leaveConference();
            } else {
                conferenceStatus.setImageResource(R.drawable.pause_big_default);
                lc.enterConference();
            }
        }
        refreshCallList(getResources());
    }

    private void displayConferenceParticipant(int index, final Call call) {
        LinearLayout confView = (LinearLayout) inflater.inflate(R.layout.conf_call_control_row, container, false);
        conferenceList.setId(index + 1);
        TextView contact = confView.findViewById(R.id.contactNameOrNumber);

        LinphoneContact lContact = ContactsManager.getInstance().findContactFromAddress(call.getRemoteAddress());
        if (lContact == null) {
            contact.setText(call.getRemoteAddress().getUsername());
        } else {
            contact.setText(lContact.getFullName());
        }

        registerCallDurationTimer(confView, call);

        ImageView quitConference = confView.findViewById(R.id.quitConference);
        quitConference.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                exitConference(call);
            }
        });
        conferenceList.addView(confView);

    }

    private void displayConferenceHeader() {
        conferenceList.setVisibility(View.VISIBLE);
        RelativeLayout headerConference = (RelativeLayout) inflater.inflate(R.layout.conference_header, container, false);
        conferenceStatus = headerConference.findViewById(R.id.conference_pause);
        conferenceStatus.setOnClickListener(this);
        conferenceList.addView(headerConference);

    }

    private void displayConference(boolean isInConf) {
        if (isInConf) {
            mControlsLayout.setVisibility(View.VISIBLE);
            conferenceList.removeAllViews();

            //Conference Header
            displayConferenceHeader();

            //Conference participant
            int index = 1;
            for (Call call : LinphoneManager.getLc().getCalls()) {
                if (call.getConference() != null) {
                    displayConferenceParticipant(index, call);
                    index++;
                }
            }
            conferenceList.setVisibility(View.VISIBLE);
        } else {
            conferenceList.setVisibility(View.GONE);
        }
    }

    ////Earset Connectivity Broadcast innerClass
    public class HeadsetReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!BluetoothManager.getInstance().isBluetoothHeadsetAvailable()) {
                if (intent.hasExtra("state")) {
                    switch (intent.getIntExtra("state", 0)) {
                        case 0:
                            if (oldIsSpeakerEnabled) {
                                LinphoneManager.getInstance().routeAudioToSpeaker();
                                isSpeakerEnabled = true;
                                speaker.setEnabled(true);
                            }
                            break;
                        case 1:
                            LinphoneManager.getInstance().routeAudioToReceiver();
                            oldIsSpeakerEnabled = isSpeakerEnabled;
                            isSpeakerEnabled = false;
                            speaker.setEnabled(false);
                            break;
                    }
                    refreshInCallActions();
                }
            }
        }
    }
}
