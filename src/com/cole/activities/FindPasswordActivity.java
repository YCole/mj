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

import com.gome.beautymirror.R;

import cole.utils.AccountValidatorUtil;
import cole.view.ClearEditText;
import cole.view.MyActionBar;

import com.gome.beautymirror.data.DataService;

import org.json.JSONException;
import org.json.JSONObject;


public class FindPasswordActivity extends BaseActivity {


    private Button mNextToGetCode;
    private ClearEditText mInputFindUser;
    private String phoneNum;
    private MyActionBar actionBar;

    @Override
    protected void loadXml() {
        setContentView(R.layout.activity_find_password);
    }

    @Override
    protected void initView() {

        mNextToGetCode = (Button) findViewById(R.id.btn_find_next_getcode);
        mInputFindUser = (ClearEditText) findViewById(R.id.et_find_input_phone);
        actionBar = (MyActionBar) findViewById(R.id.actionbar_findpassword);

    }

    @Override
    protected void initData() {

        isClickLogin(false);
        actionBar.setTranslucent(100);
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


    }

    @Override
    protected void setListener() {
        mInputFindUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                phoneNum = s.toString().trim();
                if (!TextUtils.isEmpty(phoneNum) && AccountValidatorUtil.isMobileExact(phoneNum)) {
                    isClickLogin(true);
                } else {
                    isClickLogin(false);
                }
            }
        });

        mNextToGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("COLE", "onclick  " + v.toString());
//                longToast("验证通过，去重置密码");
                DataService.instance().queryAccount(phoneNum, new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        android.util.Log.d("cole", "handleMessage: msg is " + msg);
                        JSONObject object = (JSONObject) msg.obj;
                        try {
                            if (object.get("resCode").equals("000")) {
                                longToast("验证通过，去重置密码");
                                Intent in = new Intent(FindPasswordActivity.this, ResetPasswordActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("phone", phoneNum);
                                in.putExtras(bundle);
                                startActivity(in);
                            } else if (object.get("resCode").equals("111")) {
                                longToast("请确认手机号码是否注册");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, 0);

            }
        });

    }


    /**
     * 按钮的状态
     *
     * @param isClickable true 能点击 false不能点击
     */
    private void isClickLogin(boolean isClickable) {


        if (isClickable == true) {
            Log.d("COLE", isClickable + "*********");
            mNextToGetCode.setClickable(isClickable);
            mNextToGetCode.setTextColor(Color.WHITE);
            mNextToGetCode.setBackgroundResource(R.drawable.botton_orange_rectangle_background
            );

        } else {


            Log.d("COLE", isClickable + "555555555");
            mNextToGetCode.setClickable(isClickable);
            mNextToGetCode.setTextColor(Color.GRAY);
            mNextToGetCode.setBackgroundResource(R.drawable.botton_white_rectangle_background
            );
        }

    }

}
