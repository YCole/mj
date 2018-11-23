package cole.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import cole.utils.ToastUtils;


public abstract class BaseActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window =getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        );
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        loadXml();
        initView();
        setListener();
        initData();
    }



    /**
     * 该方法用于加载布局
     * setContent等
     */
    protected abstract void loadXml(

    );

    /**
     * 初始化控件
     * findById()等
     */
    protected abstract void initView();

    /**
     * 设置数据
     */
    protected abstract void initData();
    /**
     * 设置监听器
     * 所有的事件监听都写在该方法之中
     */
    protected abstract void setListener();

    /**
     * 短时间的Toast
     * @param str
     */
    protected void shortToast(String str) {
        ToastUtils.showShortToastSafe(str);
    }

    /**
     * 长时间的Toast
     * @param str
     */
    protected void longToast(String str) {
        ToastUtils.showLongToastSafe(str);
    }





}
