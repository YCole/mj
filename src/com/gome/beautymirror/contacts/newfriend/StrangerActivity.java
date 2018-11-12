package com.gome.beautymirror.contacts;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import com.gome.beautymirror.R;
import com.gome.beautymirror.data.DataService;
import com.gome.beautymirror.data.DataThread;

public class StrangerActivity extends Activity {
    TextView mAccount,mNickName;
    Button mAddFriend;
    ImageView mContactPicture;
    String account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stranger_detail);
        String name=getIntent().getStringExtra("NAME");
        account =getIntent().getStringExtra("ACCOUNT");
        String bitmap=getIntent().getStringExtra("ICON");

        mAccount=findViewById(R.id.account);
        mNickName=findViewById(R.id.nick_name);
        mAddFriend=findViewById(R.id.add_friend);
        mContactPicture=findViewById(R.id.contact_picture);


        mContactPicture.setImageBitmap(com.gome.beautymirror.data.DataUtil.getImage(bitmap));
        mAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });
        mNickName.setText(getString(R.string.contact_detail_nickname)+name);
        mAccount.setText(account);
    }


    private void showConfirmationDialog() {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        View view= LayoutInflater.from(this).inflate(R.layout.dialog_edittext_layout, null);
        TextView cancel =view.findViewById(R.id.choosepage_cancel);
        TextView sure =view.findViewById(R.id.choosepage_sure);
        final EditText edittext =view.findViewById(R.id.choosepage_edittext);
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
                DataService.instance().requestFriend(null, account, edittext.getText().toString(), new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        try {
                            JSONObject obj = (JSONObject) msg.obj;
                            if (DataThread.RESULT_OK.equals(obj.getString(DataThread.RESULT_CODE))) {
                                Toast.makeText(StrangerActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            } else if ("444".equals(obj.getString(DataThread.RESULT_CODE))) {
                                Toast.makeText(StrangerActivity.this, obj.getString(DataThread.RESULT_MSG), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(StrangerActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("StrangerActivity", "showConfirmationDialog: JSONException is ", e);
                        }
                    }
                }, 0);
                dialog.dismiss();
            }
        });
    }





}



