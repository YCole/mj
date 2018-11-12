package cole.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import cole.utils.ToastUtils;


public abstract class BaseActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
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
