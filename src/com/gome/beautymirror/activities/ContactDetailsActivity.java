package com.gome.beautymirror.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class ContactDetailsActivity  extends Activity  implements View.OnClickListener {

    TextView contactName,nickNname ,mTvAccount,mTvDevice;//备注  昵称  账户名  设备（contactName+"的设备"）
    TextView    mAddRemarkName,deleteContact;
    ImageView callDevice, callAccount;
    LinearLayout mRecentCall;
    private LinphoneContact contact;
    private CoreListenerStub mListener;
    private LinearLayout mAddRemarkCell;
    private String displayednumberOrAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


        callDevice.setOnClickListener(this);
        callAccount.setOnClickListener(this);
        deleteContact.setOnClickListener(this);
        mAddRemarkCell.setOnClickListener(this);
        mRecentCall.setOnClickListener(this);

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
        }
    }

    private void showDeleteDialog(){
        String name;
        if(contact.getRemarkName()==null || contact.getRemarkName().equals("")){
            name = contact.getFullName();
        }else {
            name = contact.getRemarkName();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.delete_friend_title));
        builder.setMessage(String.format(getResources().getString(R.string.delete_friend_message),name));
        builder.setNegativeButton(getResources().getString(R.string.cancel),new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {

              }
        });
        builder.setPositiveButton(getResources().getString(R.string.ensure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
            }
        });
        builder.show();
    }

    private void showRemakeNameDialog() {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        View view= LayoutInflater.from(this).inflate(R.layout.dialog_edittext_layout, null);
        TextView cancel =view.findViewById(R.id.choosepage_cancel);
        TextView sure =view.findViewById(R.id.choosepage_sure);
        final EditText edittext =view.findViewById(R.id.choosepage_edittext);
        edittext.setText(contact.getRemarkName());
        final Dialog dialog= builder.create();
        dialog.show();
        dialog.getWindow().setContentView(view);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String remakename=edittext.getText().toString();
                DataService.instance().updateFriend(mTvAccount.getText().toString().trim(),remakename);
                contact.setRemarkName(remakename);
                refreshView();
                dialog.dismiss();
            }
        });
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