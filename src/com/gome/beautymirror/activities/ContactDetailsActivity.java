package com.gome.beautymirror.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;

import com.gome.beautymirror.ui.RoundImageView;
import com.gome.beautymirror.LinphoneManager;
import com.gome.beautymirror.LinphoneUtils;
import com.gome.beautymirror.R;
import com.gome.beautymirror.contacts.LinphoneContact;
import com.gome.beautymirror.contacts.LinphoneNumberOrAddress;

import org.linphone.core.Call;
import org.linphone.core.ChatMessage;
import org.linphone.core.ChatRoom;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.ProxyConfig;
import org.linphone.core.RegistrationState;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.gome.beautymirror.data.DataService;
import com.gome.beautymirror.data.DataUtil;

import gome.beautymirror.ui.blurdialog.BlurDialog;

public class ContactDetailsActivity  extends Activity  implements View.OnClickListener {

    TextView contactName,nickNname ,mTvAccount,mTvDevice;//备注  昵称  账户名  设备（contactName+"的设备"）
    TextView    mAddRemarkName;
    Button deleteContact;
    ImageView callDevice, callAccount, mBtBack;
    RoundImageView mIcon;
    LinearLayout mRecentCall;
    private LinphoneContact contact;
    private CoreListenerStub mListener;
    private LinearLayout mAddRemarkCell;
    private String displayednumberOrAddress;
    LinearLayout mSearchRoot, mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Window window =getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        );
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.contact);
        contact = (LinphoneContact) getIntent().getExtras().getSerializable("Contact");
        initView();

    }

    private void initView(){
        contactName = findViewById(R.id.contact_name);//备注 (无备注时显示昵称，nickNname 不显示)
        nickNname = findViewById(R.id.nick_name);//昵称
        mTvAccount = findViewById(R.id.tv_account);//账户名
        mTvDevice=findViewById(R.id.tv_device); //设备
        mAddRemarkName  = findViewById(R.id.add_remark_name);
        callDevice = findViewById(R.id.vedio_call_device);
        callAccount = findViewById(R.id.vedio_call_account);
        deleteContact = findViewById(R.id.deleteContact);
        mAddRemarkCell=findViewById(R.id.add_remark_cell);
        mRecentCall = findViewById(R.id.recent_call);
        mIcon = findViewById(R.id.contact_picture);
        mBtBack = findViewById(R.id.bt_back);
        mSearchRoot = findViewById(R.id.search_root);
        mSearchRoot.setVisibility(View.GONE);

        mActionBar = findViewById(R.id.action_bar);
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        mActionBar.setPadding(0, getResources().getDimensionPixelSize(resourceId),0,0);

        callDevice.setOnClickListener(this);
        callAccount.setOnClickListener(this);
        deleteContact.setOnClickListener(this);
        mAddRemarkCell.setOnClickListener(this);
        mRecentCall.setOnClickListener(this);
        mBtBack.setOnClickListener(this);

        refreshView();
        for (LinphoneNumberOrAddress noa : contact.getNumbersOrAddresses()) {
            String value = noa.getValue();
            displayednumberOrAddress = LinphoneUtils.getDisplayableUsernameFromAddress(value);
            mTvAccount.setText(displayednumberOrAddress);
            callAccount.setTag(value);
        }


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

    private void refreshView(){
        byte[] icon = contact.getIcon();
        if (icon != null) {
            mIcon.setImageBitmap(DataUtil.getImage(icon));
        }
        if(contact.getRemarkName()==null || contact.getRemarkName().equals("")){
            contactName.setText(contact.getFullName());
            mAddRemarkName.setText(getString(R.string.add_remark_name));
            mTvDevice.setText(contact.getFullName()+getString(R.string.contact_detail_device));
            nickNname.setVisibility(View.GONE);
        }else{
            contactName.setText(contact.getRemarkName());
            mAddRemarkName.setText(contact.getRemarkName());
            mTvDevice.setText(contact.getRemarkName()+getString(R.string.contact_detail_device));
            nickNname.setVisibility(View.VISIBLE);
            nickNname.setText(getString(R.string.contact_detail_nickname)+contact.getFullName());
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.vedio_call_account){
            if (BeautyMirrorActivity.isInstanciated()) {
                LinphoneUtils.setInitiateVideoCall(true);
                String tag = (String) v.getTag();
                BeautyMirrorActivity.instance().setAddresGoToDialerAndCall(tag, contact.getFullName(), contact.getPhotoUri());
            }
        }else if(id == R.id.vedio_call_device){

        }else if(id == R.id.recent_call){
            Intent intent = new Intent(this, com.gome.beautymirror.activities.RecentCallActivity.class);
            Bundle extras = new Bundle();
            extras.putString("Account", displayednumberOrAddress);
            intent.putExtras(extras);
            startActivity(intent);
        }else if (id == R.id.deleteContact) {
            showDeleteDialog();
        } else if (id == R.id.add_remark_cell) {
            showRemakeNameDialog();
        }else if(id == R.id.bt_back){
            finish();
        }
    }

    private void showDeleteDialog(){
        String name;
        if(contact.getRemarkName()==null || contact.getRemarkName().equals("")){
            name = contact.getFullName();
        }else {
            name = contact.getRemarkName();
        }

        final View view= LayoutInflater.from(this).inflate(R.layout.dialog_delete_contact, null);
        final TextView cancel =view.findViewById(R.id.cancel);
        final TextView sure =view.findViewById(R.id.sure);
        final TextView message =view.findViewById(R.id.message);
        message.setText(String.format(getResources().getString(R.string.delete_friend_message),name));
        new BlurDialog(this){
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(view);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
                sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DataService.instance().deleteFriend(null, displayednumberOrAddress, new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                if (DataService.checkResult(msg)) {
                                    finish();
                                } else {
                                    Toast.makeText(ContactDetailsActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, 0);
                        dismiss();
                    }
                });
            }
        }.show();

    }


    private void showRemakeNameDialog() {
        final View view= LayoutInflater.from(this).inflate(R.layout.dialog_edittext_layout, null);
        final TextView cancel =view.findViewById(R.id.choosepage_cancel);
        final TextView sure =view.findViewById(R.id.choosepage_sure);
        final EditText edittext =view.findViewById(R.id.choosepage_edittext);
        edittext.setText(contact.getRemarkName());
         new BlurDialog(this){
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(view);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
                sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String remakename=edittext.getText().toString();
                        DataService.instance().updateFriend(mTvAccount.getText().toString().trim(),remakename);
                        contact.setRemarkName(remakename);
                        refreshView();
                        dismiss();
                    }
                });
            }
        }.show();
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