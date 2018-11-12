package cole.activities;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceActivity;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.gome.beautymirror.R;
import com.google.zxing.util.CountDownButtonHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cole.common.P;
import cole.net.HttpUtils;
import cole.utils.AccountValidatorUtil;
import cole.utils.SaveUtils;
import cole.view.ClearEditText;
import cole.view.MyActionBar;
import cz.msebera.android.httpclient.Header;
import com.gome.beautymirror.data.DataService;

import static android.content.ContentValues.TAG;


public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private ClearEditText getPhone;
    private EditText mCodeNumber;
    private Button mGetCodeNumber;
    private EditText mSetPassword;
    private Button mGotoRegister;
    private CheckBox mcheckBoxAgree;
    private String phoneNum;
    private CountDownButtonHelper helper;
    private String password;
    private String mCode;
    private MyActionBar actionBar;


    @Override
    protected void loadXml() {
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void initView() {
        getPhone = ((ClearEditText) findViewById(R.id.et_register_getphone));
        mCodeNumber = ((EditText) findViewById(R.id.et_register_codeNumber));
        mGetCodeNumber = ((Button) findViewById(R.id.bt_register_getCodeNumber));
        mSetPassword = ((EditText) findViewById(R.id.et_register_setPassword));

        mGotoRegister = ((Button) findViewById(R.id.bt_register_goToRegister));
        mcheckBoxAgree = ((CheckBox) findViewById(R.id.cb_register_readAndAgree));
        actionBar = ((MyActionBar) findViewById(R.id.actionbar_register));

//        actionBar.setData("注册",R.id.tv_actionbar_back,);

    }

    @Override
    protected void initData() {
        mGotoRegister.setOnClickListener(this);
        mGetCodeNumber.setOnClickListener(this);

        mSetPassword.setOnClickListener(this);
        getPhone.setOnClickListener(this);


        isClickLogin(false, 1);
        isClickLogin(false, -1);

        mCodeNumber.setInputType(InputType.TYPE_CLASS_PHONE);
        getPhone.setInputType(InputType.TYPE_CLASS_PHONE);


        actionBar.setTranslucent(100);

        actionBar.setStatusBarHeight(this, false);

        actionBar.setData(getResources().getString(R.string.register), R.id.tv_actionbar_back, this.getString(R.string.back), 0, null, new MyActionBar.ActionBarClickListener() {
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

    }

    @Override
    protected void setListener() {

        mCodeNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                mCode = s.toString().trim();
                Log.d("cole", "6位正则 = " + mCode + "   password= " + password);
                if (!TextUtils.isEmpty(phoneNum) && !TextUtils.isEmpty(mCode) && !TextUtils.isEmpty(password) &&

                        AccountValidatorUtil.isCodenumber(mCode) && AccountValidatorUtil.isPassword(password)) {
                    Log.d("cole", "6位正则 in = " + mCode);
                    isClickLogin(true, 1);
                } else {
//                    shortToast("请输入正确的手机号码或验证码");
                    isClickLogin(false, 1);
                }

            }
        });
        getPhone.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void afterTextChanged(Editable s) {

                phoneNum = s.toString().trim();
                if (phoneNum.length() == 11 && AccountValidatorUtil.isMobileExact(phoneNum)
                        ) {
                    isClickLogin(true, -1);

                } else {
                    isClickLogin(false, -1);
                }
            }
        });


        mcheckBoxAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if ((!TextUtils.isEmpty(phoneNum) && (!TextUtils.isEmpty(mCode) && !TextUtils.isEmpty(password)) &&

                            phoneNum.length() < 11 | (mCode.length() < 6) | password.length() < 8)) {
                        shortToast("请输入正确的手机号码或验证码");
                        isClickLogin(false, 1);
                    } else {
                        isClickLogin(true, 1);
                    }

                } else {
                    isClickLogin(false, 1);
                }
            }
        });


        mSetPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                password = s.toString().trim();

                if (!TextUtils.isEmpty(password) && mcheckBoxAgree.isChecked() &&
                        !TextUtils.isEmpty(phoneNum) && !TextUtils.isEmpty(mCode)) {
                    if (phoneNum.length() < 11 || (mCode.length() < 6)) {
                        shortToast("请输入正确的手机号码或验证码");
                        isClickLogin(false, 1);
                    } else if ( AccountValidatorUtil.isPassword(password)) {
                        isClickLogin(true, 1);
                    } else if (password.length() < 8) {
                        shortToast("密码设置不符合规范请重新设置");
                        isClickLogin(false, 1);
                    } else {
                        isClickLogin(false, 1);
                    }
                } else {
                    shortToast("请输核对输入信息");
                }


            }
        });


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bt_register_getCodeNumber:


                helper = new CountDownButtonHelper(mGetCodeNumber,
                        "重发", 60, 1);
                helper.setOnFinishListener(new CountDownButtonHelper.OnFinishListener() {
                    @Override
                    public void finish() {
                        isClickLogin(true, -1);

                    }
                });
                if (helper.start() == true) {

                    isClickLogin(false, -1);
                    getPhoneCode();
                }

                break;


            case R.id.bt_register_goToRegister:


                if (mcheckBoxAgree.isChecked() && mGotoRegister.isClickable()) {

                    userNameAndPasswordGotoRegister();
                    longToast("注册成功,请登录");
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    SaveUtils.writeUser(RegisterActivity.this, "username", phoneNum);
                    SaveUtils.writeUser(RegisterActivity.this, "password", password);


                } else {
                    longToast("阅读并同意");
                }


                break;
        }
    }


    @SuppressLint("HandlerLeak")
    private void getPhoneCode() {

        String urlString = P.PROPOTOL + "://" + P.IP + P.PORT + P.GET_SMS + "&number=" + phoneNum + "&smsType=1";
        Log.i("info", urlString);
//        DataUtil.getInstance(this).requestCode("18055835997",null);
        DataService.instance().requestCode(phoneNum, mHandler, 0);
    }


    /**
     * 验证码注册账户
     */
    private void userNameAndPasswordGotoRegister() {

        Log.i("info",    "*******USENAME");
        DataService.instance().registerAccount(phoneNum, mCode, password, mHandler, 1);
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
                        longToast("已经注册请登录");
                    }
                } else if (msg.what == 1) {

                    if (object.get("resCode").equals("000")) {
                        longToast("注册成功");
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    } else if (object.get("resCode").equals("111")) {
                        longToast("注册失败");
                    } else if (object.get("resCode").equals("222")) {
                        longToast("已经注册请登录");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };



    /**
     * 按钮的状态
     *
     * @param isClickable isClickable  true 能点击 false不能点击
     * @param buttontype  //1 去注册
     */
    private void isClickLogin(boolean isClickable, int buttontype) {


        if (buttontype == 1) {

            if (isClickable == true) {

                mGotoRegister.setEnabled(isClickable);
                mGotoRegister.setBackgroundResource(R.drawable.botton_orange_rectangle_background
                );

            } else {

                mGotoRegister.setEnabled(isClickable);
                mGotoRegister.setBackgroundResource(R.drawable.botton_white_rectangle_background
                );
                mGotoRegister.setTextColor(Color.GRAY);

            }
        } else {

            if (isClickable == true) {

                mGetCodeNumber.setEnabled(isClickable);
                mGetCodeNumber.setBackgroundResource(R.drawable.botton_orange_rectangle_background
                );

            } else {

                mGetCodeNumber.setEnabled(isClickable);
                mGetCodeNumber.setBackgroundResource(R.drawable.botton_white_rectangle_background
                );
                mGetCodeNumber.setTextColor(Color.GRAY);

            }

        }


    }

}