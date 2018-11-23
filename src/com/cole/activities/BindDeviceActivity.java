package cole.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.gome.beautymirror.data.DataService;
import com.gome.beautymirror.data.DataThread;

public class BindDeviceActivity extends BaseActivity {


    private Button mScanCode;
    private TextView mTextCenter;
    private MyActionBar actionBar;
    private TextView mDevicesId;
    private Button mBindDeviceId;
    private String mDeviceId;
    private String mDeviceKey;

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
        if (DataService.isReady()) {
            DataService.instance().bindDevice(null, mDeviceId, mDeviceKey, null, new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (DataService.checkResult(msg)) {
                        shortToast("bindDevice: success");
                        finish();
                    } else {
                        shortToast("bindDevice: error");
                    }
                }
            }, 0);
        } else {
            shortToast("DataService not ready");
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
                try{
                    mDeviceId = Integer.parseInt(scanResult) + "";
                    Log.d("BindDeviceActivity", "mDeviceId = " + mDeviceId);
                    if (DataService.isReady()) {
                        DataService.instance().requestKey(mDeviceId, new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                try {
                                    JSONObject obj = (JSONObject) msg.obj;
                                    if (DataThread.RESULT_OK.equals(obj.getString(DataThread.RESULT_CODE))) {
                                        mDeviceKey = obj.getString("deviceKey");
                                        mDevicesId.setText("You can bind the Device:" + mDeviceId);
                                    } else {
                                        shortToast("requestKey: error");
                                    }
                                } catch (JSONException e) {
                                    shortToast("requestKey: JSONException");
                                }
                            }
                        }, 0);
                    } else {
                        shortToast("DataService not ready");
                    }
                } catch(NumberFormatException e) {
                    shortToast("id error");
                }
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
