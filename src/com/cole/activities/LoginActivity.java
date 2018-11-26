package cole.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gome.beautymirror.data.DataService;

import com.gome.beautymirror.R;
import com.yanzhenjie.permission.AndPermission;

import org.json.JSONException;
import org.json.JSONObject;

import cole.utils.AccountValidatorUtil;
import cole.utils.SaveUtils;

import static android.content.ContentValues.TAG;


public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout llBackground;
    private ImageView ivLogo;
    private ImageView checkPassword;
    private TextView phoneOrUsers;
    private TextView showphoneOrUsersPass;
    private TextView wifiAgreement;
    private Button getCode;
    private Button mLogin;
    private RadioButton schoolUsers;
    private RadioButton visitor;
    private EditText inputName;
    private EditText inputPassword;
    private String phoneNum, phoneCode;
    private CheckBox checkAgreements;
    //    private CountDownButtonHelper helper;
    private static final float ZOOM_MAX = 1.3f;
    private static final float ZOOM_MIN = 1.0f;
    Context mContext;
    private ImageView ivBack;
    String TAG = "cole_LoginActivity";
    private float mWidth, mHeight;
    private TextView mLoging;
    private TextView mLoginRegister;
    private TextView mFindPassword;
    private String mUserPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = LoginActivity.this;
    }

    @Override
    protected void loadXml() {
        setContentView(R.layout.activity_login);
    }

    protected void initView() {
        llBackground = (RelativeLayout) findViewById(R.id.activity_login);
//        ivLogo = (ImageView) findViewById(R.id.iv_school_logo);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        checkPassword = (ImageView)findViewById(R.id.check_password);


        mLoginRegister = (TextView) findViewById(R.id.tv_login_register);
        mFindPassword = (TextView) findViewById(R.id.tv_login_findPassword);


        mLoging = (TextView) findViewById(R.id.tv_logining);
//        showphoneOrUsersPass = (TextView) findViewById(R.id.tv_show_users_prd);


        mLogin = (Button) findViewById(R.id.btn_login);
        inputName = (EditText) findViewById(R.id.et_login_input_phone);
        inputPassword = (EditText) findViewById(R.id.et_login_input_password);


//        ivLogo.getBackground().setAlpha(100);

    }

    protected void initData() {

        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0.5f);
        alphaAnimation.setDuration(1000);
        ivBack.startAnimation(alphaAnimation);
        playHeartbeatAnimation();


        mLogin.setOnClickListener(this);

        checkPassword.setOnClickListener(this);

        inputName.setOnClickListener(this);
        inputPassword.setOnClickListener(this);


        mWidth = mLogin.getMeasuredWidth();
        mHeight = mLogin.getMeasuredHeight();


        phoneNum = SaveUtils.readUser(this, "username");

        mUserPassword = SaveUtils.readUser(this, "password");
        if (phoneNum.equals(null) | mUserPassword.equals("")) {
            isClickLogin(false);
        } else {
            inputName.setText(phoneNum);
            inputPassword.setText(mUserPassword);
            isClickLogin(true);
        }

        inputName.setInputType(InputType.TYPE_CLASS_PHONE);


        if (inputName.getText().equals(null) || inputPassword.getText().equals(null)) {
            isClickLogin(false);

        } else {
            isClickLogin(true);
        }

    }

    /**
     * 登录按钮的缩放动画
     *
     * @param view
     * @param w
     * @param h
     */
    private void inputAnimator(final View view, float w, float h) {

        AnimatorSet set = new AnimatorSet();

        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mLogin,
                "scaleX", 1f, 0f);
        set.setDuration(100);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
//        set.play(animator2);
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                mLoging.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mLoging.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub
                mLogin.setVisibility(View.VISIBLE);
            }
        });
    }

    protected void setListener() {

        inputName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                phoneNum = s.toString().trim();
                if (!TextUtils.isEmpty(mUserPassword) && !TextUtils.isEmpty(phoneNum) &&

                        AccountValidatorUtil.isMobileExact(s) && AccountValidatorUtil.isPassword(mUserPassword)) {

                    isClickLogin(true);

                } else {

                    isClickLogin(false);
                }
            }
        });
        inputPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

                mUserPassword = s.toString().trim();

                if (!TextUtils.isEmpty(mUserPassword) && !TextUtils.isEmpty(phoneNum) &&

                        AccountValidatorUtil.isMobileExact(phoneNum) && AccountValidatorUtil.isPassword(mUserPassword)) {

                    isClickLogin(true);

                } else {

                    isClickLogin(false);
                }
            }
        });

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.btn_login:
               /* if (mLogin.isClickable()) {
//                    startActivity(new Intent(LoginActivity.this, BindDeviceActivity.class));
                } else {
                    longToast("手机号和密码不能为空");
                }*/

                if (TextUtils.isEmpty(phoneNum) || TextUtils.isEmpty(mUserPassword)) {

                    longToast("手机号和密码不能为空");

                } else {



                    userNameAndPasswordLogin();
                }
                break;

            case R.id.tv_login_register:

                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

                break;
            case R.id.tv_login_findPassword:

                startActivity(new Intent(LoginActivity.this, FindPasswordActivity.class));
                break;
            case R.id.check_password:
                if (inputPassword.getInputType() == (InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD)){
                    checkPassword.setImageResource(R.drawable.icon_check);
                    inputPassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else{
                    checkPassword.setImageResource(R.drawable.icon_conceal);
                    inputPassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;


        }


    }


    /**
     * 背景图片的缩放效果
     */
    public void playHeartbeatAnimation() {
        /**
         * 放大动画
         */
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(new ScaleAnimation(ZOOM_MIN, ZOOM_MAX, ZOOM_MIN, ZOOM_MAX, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f));
        animationSet.addAnimation(new AlphaAnimation(1.0f, 0.3f));

        animationSet.setDuration(3000);
        animationSet.setInterpolator(new AccelerateInterpolator());
        animationSet.setFillAfter(true);
        // 实现心跳的View
        ivBack.startAnimation(animationSet);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                /**
                 * 缩小动画
                 */
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(new ScaleAnimation(ZOOM_MAX, ZOOM_MIN, ZOOM_MAX, ZOOM_MIN, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f));
                animationSet.addAnimation(new AlphaAnimation(0.3f, 0.6f));
                animationSet.setDuration(3000);
                animationSet.setInterpolator(new DecelerateInterpolator());
                animationSet.setFillAfter(false);
                // 实现心跳的View
                ivBack.startAnimation(animationSet);
                ivBack.setAlpha(0.5f);
            }
        });

    }


    private void userNameAndPasswordLogin() {

        DataService.instance().loginAccount(this, phoneNum, mUserPassword, null,
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        android.util.Log.d("cole", "handleMessage: msg is " + msg);
                        JSONObject object = (JSONObject) msg.obj;
                        try {
                            if (object.get("resCode").equals("000")) {
                                inputAnimator(mLogin, mWidth, mHeight);
                                longToast("登录成功");
                                if (TextUtils.isEmpty(DataService.instance().getDevice(null))) {
                                    startActivity(new Intent(LoginActivity.this, BindDeviceActivity.class));
                                }
                                finish();
                                RigisterAndLoginMainActivity.quit();
                            } else if (object.get("resCode").equals("111")) {
                                longToast("登录失败");
                            } else if (object.get("resCode").equals("333")) {
                                longToast("账号密码错误");
                            } else {
                                longToast(object.get("resCode") + "错误");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, 0);

    }


//    UsersInfo info = new UsersInfo();
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    /**
     * 登陆按钮的状态
     *
     * @param isClickable true 能点击 false不能点击
     */
    private void isClickLogin(boolean isClickable) {

        if (isClickable == true) {

            mLogin.setEnabled(isClickable);
            mLogin.setBackgroundResource(R.drawable.botton_orange_rectangle_background
            );

        } else {

            mLogin.setEnabled(isClickable);
            mLogin.setBackgroundResource(R.drawable.botton_white_rectangle_background
            );
            mLogin.setTextColor(Color.GRAY);

        }

    }
}
