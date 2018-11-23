package com.gome.beautymirror.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gome.beautymirror.LinphoneManager;
import com.gome.beautymirror.R;
import com.gome.beautymirror.data.provider.DatabaseUtil;
import com.gome.beautymirror.ui.RoundImageView;

import org.linphone.core.Call;
import org.linphone.core.ChatMessage;
import org.linphone.core.ChatRoom;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.ProxyConfig;
import org.linphone.core.RegistrationState;
import com.gome.beautymirror.data.DataService;


public class MyDeviceDetailsActivity extends BaseStatusBarActivity  implements View.OnClickListener {

    TextView mTvDeviceName, mTvDevice;
    ImageView callDevice, callAccount, mBtBack;
    RoundImageView mIcon;
    LinearLayout mRecentCall;
    private CoreListenerStub mListener;
    LinearLayout mSearchRoot, mActionBar;
    private String mStrDeviceName, mStrAccount,mStrDeviceSIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_device_details);

        initView();
        initData();

    }

    private void initView(){
        mTvDeviceName=findViewById(R.id.device_name);
        mTvDevice=findViewById(R.id.tv_device); //设备
        callDevice = findViewById(R.id.vedio_call_device);
        mRecentCall = findViewById(R.id.recent_call);
        mIcon = findViewById(R.id.contact_picture);
        mBtBack = findViewById(R.id.bt_back);
        mSearchRoot = findViewById(R.id.search_root);
        mSearchRoot.setVisibility(View.GONE);

        mActionBar = findViewById(R.id.action_bar);
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        mActionBar.setPadding(0, getResources().getDimensionPixelSize(resourceId),0,0);

        callDevice.setOnClickListener(this);
        mRecentCall.setOnClickListener(this);
        mBtBack.setOnClickListener(this);

        refreshView();

        mListener = new CoreListenerStub() {
            @Override
            public void onMessageReceived(Core lc, ChatRoom cr, ChatMessage message) {

            }

            @Override
            public void onRegistrationStateChanged(Core lc, ProxyConfig proxy, RegistrationState
                    state, String smessage) {

            }

            @Override
            public void onCallStateChanged(Core lc, Call call, Call.State state, String message) {
                if (state == Call.State.OutgoingInit || state == Call.State.OutgoingProgress) {
                    startActivity(new Intent(com.gome.beautymirror.activities.BeautyMirrorActivity.instance(), com.gome.beautymirror.call.CallOutgoingActivity.class));
                }
            }
        };

    }

    private void initData(){
        Cursor cursor =DataService.instance().getAccountsAndDevices(null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            mStrDeviceName = cursor.getString(com.gome.beautymirror.data.provider.DatabaseUtil.Account.COLUMN_DEVICE_NAME);
            mStrAccount = cursor.getString(com.gome.beautymirror.data.provider.DatabaseUtil.Account.COLUMN_ACCOUNT);
            mStrDeviceSIP = cursor.getString(DatabaseUtil.Account.COLUMN_DEVICE_SIP);
            refreshView();
        }
    }

    private void refreshView(){
        mTvDeviceName.setText(mStrDeviceName!=null && !"".equals(mStrDeviceName) ? mStrDeviceName : getString(R.string.my_device));
        mTvDevice.setText(mStrDeviceName!=null && !"".equals(mStrDeviceName) ? mStrDeviceName : getString(R.string.my_device));
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.vedio_call_device){
            BeautyMirrorActivity.instance().setAddresGoToDialerAndCall(mStrDeviceSIP,mStrDeviceName, null);
        }else if(id == R.id.recent_call){
            Intent intent = new Intent(this, com.gome.beautymirror.activities.RecentCallActivity.class);
            Bundle extras = new Bundle();
            extras.putString("Account", mStrAccount);
            intent.putExtras(extras);
            startActivity(intent);
        }else if(id == R.id.bt_back){
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Core lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
        if (lc != null) {
            lc.addListener(mListener);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        Core lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
        if (lc != null) {
            lc.removeListener(mListener);
        }
    }
}