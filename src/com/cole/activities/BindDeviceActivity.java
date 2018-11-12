package cole.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gome.beautymirror.R;
import com.google.zxing.activity.CaptureActivity;
import com.google.zxing.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import cole.common.P;
import cole.utils.SaveUtils;
import cole.view.MyActionBar;
import com.gome.beautymirror.activities.BeautyMirrorLauncherActivity;



public class BindDeviceActivity extends BaseActivity {


    private Button mScanCode;
    private TextView mTextCenter;
    private MyActionBar actionBar;
    private TextView mDevicesId;
    private Button mBindDeviceId;
    private String mDeviceId;

    @Override
    protected void loadXml() {
        setContentView(R.layout.activity_bind_device);
    }

    @Override
    protected void initView() {

        mScanCode = (Button) findViewById(R.id.bt_scancode_device);
        mBindDeviceId = (Button) findViewById(R.id.bt_scanbind_device);
        mTextCenter = (TextView) findViewById(R.id.tv_sacn_centertips);
        mDevicesId = (TextView) findViewById(R.id.tv_sacn_deviceid);
        actionBar = (MyActionBar) findViewById(R.id.actionbar_scancode_device);

    }

    @Override
    protected void initData() {


//        actionBar.setTranslucent(50);
        actionBar.setStatusBarHeight(this, true);
        actionBar.setData(getResources().getString(R.string.add_device), R.id.tv_actionbar_back, this.getString(R.string.back), R.id.tv_actionbar_enter, this.getString(R.string.jump_over), new MyActionBar.ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                longToast("返回了");
                finish();
            }

            @Override
            public void onRightClick() {
//                longToast("11111111111111111");
                longToast("跳过");
                Intent intent = new Intent(BindDeviceActivity.this,  BeautyMirrorLauncherActivity.class);
                startActivity(intent);
                finish();

            }
        })
        ;


    }

    @Override
    protected void setListener() {
        mScanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQrCode();
            }
        });
        mBindDeviceId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userGotoBindDevice();
            }
        });


    }

    /**
     * 绑定设备请求
     */
    private void userGotoBindDevice() {

        String phoneNum = SaveUtils.readUser(this, "username");
        String password = SaveUtils.readUser(this, "password");
        if (TextUtils.isEmpty(phoneNum) | TextUtils.isEmpty(password) | TextUtils.isEmpty(mDeviceId)) {
            shortToast("绑定设备用户密码为空");
        } else {

            String strUrl = P.PROPOTOL + "://" + P.IP + ":" + P.PORT + P.CKECK_USE +
                    "&vname=" + phoneNum + "&passwd=" + password + "&deviceid" + mDeviceId;
            Log.i("info", strUrl + "*******USENAME");
          /*  HttpUtils.get(strUrl, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                    // TODO Auto-generated method stub
                    try {
                        JSONObject obj = new JSONObject(new String(arg2));
                        if (obj.getString("result").equals("0")) {
                            shortToast("绑定成功");
                            Intent intent = new Intent(BindDeviceActivity.this, LoginActivity.class);
                            startActivity(intent);
                            SaveUtils.writeUser(BindDeviceActivity.this, "username", mDevicesId.getText().toString().trim());

                            //多用户管理时候需要数据库了，储存用户信息，管理区别账号资料
                            //userinfo sqlite3 数据库管理



                        } else {
                            shortToast("绑定失败");
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                    // TODO Auto-generated method stub
                    shortToast("检测网络");
                }
            });*/
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (requestCode == Constant.REQ_QR_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
            if (!scanResult.equals(null)) {
                //将扫描出的信息显示出来
                mDevicesId.setVisibility(View.VISIBLE);
                mDevicesId.setText(scanResult);

                mDeviceId = scanResult;
            } else {
                shortToast("扫码失败");
                mDevicesId.setVisibility(View.INVISIBLE);
                mDevicesId.setText("");
            }

        }
    }


    // 开始扫码
    private void startQrCode() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, Constant.REQ_PERM_CAMERA);
            return;
        }
        // 二维码扫码
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, Constant.REQ_QR_CODE);
    }

}
