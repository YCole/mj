package cole.activities;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.gome.beautymirror.R;

public class RigisterAndLoginMainActivity extends BaseActivity {


    private Button mainLogin;
    private Button mainRegister;

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
}
