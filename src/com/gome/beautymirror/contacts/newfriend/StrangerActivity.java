package com.gome.beautymirror.contacts;
import android.app.Activity;;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import com.gome.beautymirror.R;
import com.gome.beautymirror.data.DataService;
import com.gome.beautymirror.data.DataThread;

import gome.beautymirror.ui.MyToast;
import gome.beautymirror.ui.blurdialog.BlurDialog;

public class StrangerActivity extends Activity {
    TextView mAccount;
    Button mAddFriend;
    ImageView mContactPicture, mBtBack;
    String account;
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
        setContentView(R.layout.stranger_detail);
        String name=getIntent().getStringExtra("NAME");
        account =getIntent().getStringExtra("ACCOUNT");
        String bitmap=getIntent().getStringExtra("ICON");

        mAccount=findViewById(R.id.account);
        mAddFriend=findViewById(R.id.add_friend);
        mContactPicture=findViewById(R.id.contact_picture);

        mSearchRoot = findViewById(R.id.search_root);
        mSearchRoot.setVisibility(View.GONE);

        mActionBar = findViewById(R.id.action_bar);
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        mActionBar.setPadding(0, getResources().getDimensionPixelSize(resourceId),0,0);

        mBtBack = findViewById(R.id.bt_back);
        mBtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(bitmap!=null && !bitmap.equals("") ){
            mContactPicture.setImageBitmap(com.gome.beautymirror.data.DataUtil.getImage(bitmap));
        }
        mAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });
        mAccount.setText(account);
    }

    private void showConfirmationDialog() {
        final View view= LayoutInflater.from(this).inflate(R.layout.dialog_add_friend, null);
        final TextView cancel =view.findViewById(R.id.choosepage_cancel);
        final TextView sure =view.findViewById(R.id.choosepage_sure);
        final EditText edittext =view.findViewById(R.id.choosepage_edittext);
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
                        DataService.instance().requestFriend(null, account, edittext.getText().toString(), new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                try {
                                    JSONObject obj = (JSONObject) msg.obj;
                                    if (DataThread.RESULT_OK.equals(obj.getString(DataThread.RESULT_CODE))) {
                                        MyToast.showToast(StrangerActivity.this,getResources().getString(R.string.send_add_friend_request) , Toast.LENGTH_SHORT);
                                    } else if ("444".equals(obj.getString(DataThread.RESULT_CODE))) {
                                        MyToast.showToast(StrangerActivity.this,obj.getString(DataThread.RESULT_MSG) , Toast.LENGTH_SHORT);
                                    } else {
                                        MyToast.showToast(StrangerActivity.this,"Fail" , Toast.LENGTH_SHORT);
                                    }
                                } catch (JSONException e) {
                                    Log.e("StrangerActivity", "showConfirmationDialog: JSONException is ", e);
                                }
                            }
                        }, 0);
                        dismiss();
                    }
                });
            }
        }.show();
    }

}



