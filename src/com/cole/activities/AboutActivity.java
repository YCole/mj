package cole.activities;

import android.content.Context;
import android.graphics.Color;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gome.beautymirror.data.provider.DatabaseUtil;
import com.gome.beautymirror.data.DataService;
import com.gome.beautymirror.R;

import cole.view.MyOneLineView;

public class AboutActivity  extends BaseActivity  {

    Context mContext;
    private LinearLayout mSearchRoot, mActionBar;
    private TextView mTvTitleName;
    private MyOneLineView mAccount;
    private String mStrAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = AboutActivity.this;
    }

    @Override
    protected void loadXml() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        );
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.about);
    }

    @Override
    protected void initView() {
        mSearchRoot = findViewById(R.id.search_root);
        mSearchRoot.setVisibility(View.GONE);
        mActionBar = findViewById(R.id.action_bar);
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        mActionBar.setPadding(0, getResources().getDimensionPixelSize(resourceId),0,0);
        mTvTitleName=findViewById(R.id.tv_title_name);
        mTvTitleName.setText("关于美镜");

        mAccount = (MyOneLineView) findViewById(R.id.account);
        mAccount.initMine(0, getString(R.string.beautymirror_account), "", true,false);
        mAccount.showLeftIcon(false);

    }

    @Override
    protected void initData() {
        Cursor cursor = DataService.instance().getAccountsAndDevices(null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            mStrAccount = cursor.getString(DatabaseUtil.Account.COLUMN_ACCOUNT);
        }
        mAccount.setRightText(mStrAccount);
    }

    @Override
    protected void setListener() {
    }
}