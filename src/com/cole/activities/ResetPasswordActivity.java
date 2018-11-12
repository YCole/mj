package cole.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gome.beautymirror.R;

import cole.utils.AccountValidatorUtil;
import cole.view.ClearEditText;
import cole.view.MyActionBar;

import com.gome.beautymirror.data.DataService;
import com.google.zxing.util.CountDownButtonHelper;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;


public class ResetPasswordActivity extends BaseActivity {


    private MyActionBar actionBar;
    private ClearEditText mInputNewPassword;
    private Button mToFinishReset;
    private String password;
    private TextView mphone;
    private Button getCode;
    private EditText setCode;
    private String phone;
    private String code;
    private CountDownButtonHelper helper;

    @Override
    protected void loadXml() {
        setContentView(R.layout.activity_reset_password);
    }

    @Override
    protected void initView() {
        mToFinishReset = (Button) findViewById(R.id.bt_reset_password_finish);
        mInputNewPassword = (ClearEditText) findViewById(R.id.et_reset_input_newpassword);
        mphone = (TextView) findViewById(R.id.tv_reset_password_phone);
        actionBar = (MyActionBar) findViewById(R.id.actionbar_resetpassword);
        getCode = (Button) findViewById(R.id.bt_reset_getCodeNumber);
        setCode = (EditText) findViewById(R.id.et_reset_codeNumber);
    }

    @Override
    protected void initData() {
        isClickLogin(false, 1);
        actionBar.setTranslucent(100);
        actionBar.setVisibility(View.VISIBLE);
        actionBar.setStatusBarHeight(this, true);
        actionBar.setData(getResources().getString(R.string.tv_login_findPassword), R.id.tv_actionbar_back, this.getString(R.string.back), 0, null, new MyActionBar.ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                longToast("返回了");
                finish();
            }

            @Override
            public void onRightClick() {
//                longToast("11111111111111111");
            }
        })
        ;
        Intent in = getIntent();
        if (in != null) {
            Bundle bundle = in.getExtras();
            phone = bundle.getString("phone");
            mphone.setText("正在找回：" + phone);
        }


    }

    @Override
    protected void setListener() {
        mInputNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                password = s.toString().trim();
                if (!TextUtils.isEmpty(password) && AccountValidatorUtil.isPassword(password)) {
                    isClickLogin(true, 1);
                } else {
                    isClickLogin(false, 1);
                }
            }
        });
        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                helper = new CountDownButtonHelper(getCode,
                        "重发", 60, 1);
                helper.setOnFinishListener(new CountDownButtonHelper.OnFinishListener() {
                    @Override
                    public void finish() {
                        isClickLogin(true, 2);
                    }
                });
                if (helper.start() == true) {
                    isClickLogin(false, 2);
                    DataService.instance().requestReset(phone, mHandler, 0);
                }

            }
        });
        setCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                code = editable.toString().trim();
            }
        });
        mToFinishReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (code == null || code.equals(null) || password == null || password.equals(null)) {
                    shortToast("请输入验证密码");
                } else {

                    DataService.instance().resetAccount(phone, code, password, mHandler, 1);
                }

            }
        });
    }

    /**
     * 按钮的状态
     *
     * @param buttontype  2 获取重置的验证码
     * @param isClickable true 能点击 false不能点击
     */
    private void isClickLogin(boolean isClickable, int buttontype) {

        if (buttontype == 1) {
            if (isClickable == true) {
                mToFinishReset.setClickable(isClickable);
                mToFinishReset.setTextColor(Color.WHITE);
                mToFinishReset.setBackgroundResource(R.drawable.botton_orange_rectangle_background
                );

            } else {

                mToFinishReset.setClickable(isClickable);
                mToFinishReset.setTextColor(Color.GRAY);
                mToFinishReset.setBackgroundResource(R.drawable.botton_white_rectangle_background
                );
            }
        } else {

            if (isClickable == true) {
                getCode.setClickable(isClickable);
                getCode.setTextColor(Color.WHITE);
                getCode.setBackgroundResource(R.drawable.botton_orange_rectangle_background
                );

            } else {
                getCode.setClickable(isClickable);
                getCode.setTextColor(Color.GRAY);
                getCode.setBackgroundResource(R.drawable.botton_white_rectangle_background
                );
            }

        }


    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "handleMessage: msg is " + msg);
            try {
                JSONObject object = ((JSONObject) msg.obj);
                if (msg.what == 0) {

                    if (object.get("resCode").equals("000")) {
                        longToast("验证码发送成功");
                    } else if (object.get("resCode").equals("111")) {
                        longToast("验证码发送失败");
                    } else if (object.get("resCode").equals("222")) {
                        longToast("验证失败");
                    }
                } else if (msg.what == 1) {

                    if (object.get("resCode").equals("000")) {
                        longToast("重置成功,请登陆");

                        startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                    } else if (object.get("resCode").equals("111")) {
                        longToast("找回失败");
                    } else if (object.get("resCode").equals("222")) {
                        longToast("网络异常");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

}
