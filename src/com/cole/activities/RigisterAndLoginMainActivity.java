package cole.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.gome.beautymirror.R;
import com.gome.beautymirror.activities.BeautyMirrorActivity;

public class RigisterAndLoginMainActivity extends BaseActivity {
    private static RigisterAndLoginMainActivity instance;

    private Button mainLogin;
    private Button mainRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }

    @Override
    protected void loadXml() {
        setContentView(
                R.layout.activity_main_register_login
        );
    }

    @Override
    protected void initView() {
        mainLogin = ((Button) findViewById(R.id.botton_main_login));
        mainRegister = ((Button) findViewById(R.id.botton_main_register));
    }

    @Override
    protected void initData() {


    }

    @Override
    protected void setListener() {

        mainLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RigisterAndLoginMainActivity.this, LoginActivity.class));
            }
        });
        mainRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RigisterAndLoginMainActivity.this, RegisterActivity.class));
            }
        });

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public static void quit() {
        if (instance != null) {
            instance.finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            BeautyMirrorActivity.instance().quit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
