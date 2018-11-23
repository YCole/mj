package cole.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.gome.beautymirror.data.DataService;
import com.gome.beautymirror.data.provider.DatabaseUtil;
import com.gome.beautymirror.ui.RoundImageView;
import com.gome.beautymirror.R;

import cole.view.MyOneLineView;
import gome.beautymirror.ui.blurdialog.BlurDialog;
import gome.beautymirror.ui.MyToast;

public class MyDeviceActivity extends BaseActivity implements View.OnClickListener {

    Context mContext;
    private LinearLayout mSearchRoot, mActionBar;
    private TextView mTvTitleName, mTvMyDeviceName, mTvBoundDevice;
    private ImageView mBtBack;
    private RoundImageView mBindDeviceIcon;
    private boolean isBound;
    private MyOneLineView mResetDeviceName;
    private LinearLayout mLlBindDevice;
    private Button mBtnBind, mBtnUnBind;
    private Switch mSwitchSyncGallery;
    private String mStrDeviceName, mStrAccount,mStrDeviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = MyDeviceActivity.this;
    }

    @Override
    protected void loadXml() {
        setContentView(R.layout.bind_device);
    }

    @Override
    protected void initView() {
        mSearchRoot = findViewById(R.id.search_root);
        mSearchRoot.setVisibility(View.GONE);
        mActionBar = findViewById(R.id.action_bar);
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        mActionBar.setPadding(0, getResources().getDimensionPixelSize(resourceId),0,0);
        mBtBack = findViewById(R.id.bt_back);
        mTvTitleName=findViewById(R.id.tv_title_name);
        mTvTitleName.setText(getString(R.string.my_device));
        mBindDeviceIcon = findViewById(R.id.bind_device_icon);
        mTvMyDeviceName = findViewById(R.id.my_device_name);
        mTvBoundDevice = findViewById(R.id.bound_device);
        mResetDeviceName = findViewById(R.id.reset_device_name);
        mResetDeviceName.initMine(0, getString(R.string.device_name_title), "", true,true);
        mResetDeviceName.showLeftIcon(false);
        mLlBindDevice = findViewById(R.id.ll_bind_device);
        mBtnBind = findViewById(R.id.btn_bind_device);
        mBtnUnBind = findViewById(R.id.btn_unbind_device);
        mSwitchSyncGallery = findViewById(R.id.sync_gallery);
        refresh();
    }

    @Override
    protected void initData() {
        Cursor cursor = DataService.instance().getAccountsAndDevices(null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            mStrDeviceName = cursor.getString(DatabaseUtil.Account.COLUMN_DEVICE_NAME);
            mStrAccount = cursor.getString(DatabaseUtil.Account.COLUMN_ACCOUNT);
            mStrDeviceId = cursor.getString(DatabaseUtil.Account.COLUMN_ID);
            if(mStrDeviceId != null && !"".equals(mStrDeviceId)){
                isBound = true;
            }
            refresh();
        }
        if (cursor != null) cursor.close();
    }

    @Override
    protected void setListener() {
        mBtBack.setOnClickListener(this);
        mResetDeviceName.setOnClickListener(this);
        mBtnBind.setOnClickListener(this);
        mBtnUnBind.setOnClickListener(this);
        mSwitchSyncGallery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_back:
                finish();
                break;
            case R.id.reset_device_name:
                showDeviceNameDialog();
                break;
            case R.id.btn_bind_device:
                startActivityForResult(new Intent(this, BindDeviceActivity.class), 1);
                break;
            case R.id.btn_unbind_device:
                showUnbindDialog();
                break;
        }
    }

    private void refresh(){
        if(isBound){
            mBindDeviceIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.image_mymirror_connect));
            mTvMyDeviceName.setText(getString(R.string.bound_device));
            mLlBindDevice.setVisibility(View.VISIBLE);
            mBtnBind.setVisibility(View.GONE);
            mResetDeviceName.setRightText(mStrDeviceName!=null && !"".equals(mStrDeviceName) ? mStrDeviceName : getString(R.string.my_device));
            mTvMyDeviceName.setText(mStrDeviceName!=null && !"".equals(mStrDeviceName) ? mStrDeviceName : getString(R.string.my_device));
        }else {
            mBindDeviceIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.image_mymirror_unconnect));
            mTvMyDeviceName.setText(getString(R.string.no_bind_device));
            mTvBoundDevice.setVisibility(View.GONE);
            mLlBindDevice.setVisibility(View.GONE);
            mBtnBind.setVisibility(View.VISIBLE);
        }
    }

    private void showDeviceNameDialog() {
        final View view= LayoutInflater.from(this).inflate(R.layout.dialog_edittext_layout, null);
        TextView title =view.findViewById(R.id.dialog_title);
        title.setText(R.string.edit_device_name);
        final TextView cancel =view.findViewById(R.id.choosepage_cancel);
        final TextView sure =view.findViewById(R.id.choosepage_sure);
        final EditText edittext =view.findViewById(R.id.choosepage_edittext);
        edittext.setText(mStrDeviceName);
        new BlurDialog(this,false){
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
                        DataService.instance().updateDevice(null, null, edittext.getText().toString(), new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                if (DataService.checkResult(msg)) {
                                    String name = edittext.getText().toString();
                                    mResetDeviceName.setRightText(name!=null && !"".equals(name) ? name : getString(R.string.my_device));
                                    mTvMyDeviceName.setText(name!=null && !"".equals(name) ? name : getString(R.string.my_device));
                                    dismiss();
                                } else {
                                    MyToast.showToast(MyDeviceActivity.this, "update fail.", MyToast.LENGTH_SHORT);
                                }
                            }
                        }, 0);
                    }
                });
            }
        }.show();
    }

    private void showUnbindDialog(){
        final View view= LayoutInflater.from(this).inflate(R.layout.dialog_unbind_device, null);
        final TextView cancel =view.findViewById(R.id.cancel);
        final TextView sure =view.findViewById(R.id.sure);

        new BlurDialog(this,false){
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(view);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        unBindService(false);
                        dismiss();
                    }
                });
                sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        unBindService(true);
                        dismiss();
                    }
                });
            }
        }.show();

    }

    private void unBindService(boolean unbind) {
        DataService.instance().unbindDevice(null, null, unbind ? 1 : 0, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (DataService.checkResult(msg)) {
                    isBound = false;
                    refresh();
                } else {
                    MyToast.showToast(MyDeviceActivity.this, "unbind fail.", MyToast.LENGTH_SHORT);
                }
            }
        }, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            initData();
        }
    }
}
