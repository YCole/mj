package cole.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.beautymirror.R;


public class MyActionBar extends LinearLayout {
    private RelativeLayout layRoot;
    private View layLeft;
    private View layRight;
    private TextView tvTitle;
    private TextView tvLeft;
    private TextView tvRight;
    private MyActionBar actionBar;
    private LinearLayout totoalText;
    private View mline;

    public MyActionBar(Context context) {
        super(context);
    }

    public MyActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
//        setStatusBarHeight(context,true);
    }

    private void init() {
        setOrientation(HORIZONTAL);
        View contentView = inflate(getContext(), R.layout.actionbar_title, this);
        layRoot =  (RelativeLayout)contentView.findViewById(R.id.lay_transroot);
        totoalText = (LinearLayout) contentView.findViewById(R.id.action_bar_title_text);
        tvTitle = (TextView) contentView.findViewById(R.id.tv_actionbar_title);
        tvLeft = (TextView) contentView.findViewById(R.id.tv_actionbar_back);
        mline =  contentView.findViewById(R.id.action_bar_line);
        tvRight = (TextView) contentView.findViewById(R.id.tv_actionbar_enter);

    }


    /**
     * 设置状态栏高度     *     * @param statusBarHeight
     */
    public void setStatusBarHeight(Context context,boolean noStatusBar) {
        ViewGroup.LayoutParams params = layRoot.getLayoutParams();
        if (true == noStatusBar) {
            params.height = getStatusBarHeight(context)  + getActionBarHeight(context) ;

            Log.d("info",params.height+"高度值是多少" +
                    "  /getStatusBarHeight(context) ="+ getStatusBarHeight(context)  + "  " +
                    "getActionBarHeight(context)"+getActionBarHeight(context));
            totoalText.setPadding(0,getStatusBarHeight(context),0,0);

        }else {
            params.height =getActionBarHeight(context) ;
        }

        
        layRoot.setLayoutParams(params);
    }


    /**
     * 设置是否需要渐变     *     * @param translucent
     */
    public void setNeedTranslucent(boolean translucent) {
        if (translucent) {
            layRoot.setBackgroundDrawable(null);
        }
    }

    /**
     * 设置标题     *     * @param strTitle
     */
    public void setTitle(String strTitle) {
        if (!TextUtils.isEmpty(strTitle)) {
            totoalText.setGravity(Gravity.CENTER);
            tvRight.setVisibility(View.GONE);
            tvLeft.setVisibility(View.GONE);
            tvTitle.setTextColor(Color.RED);
            mline.setVisibility(View.GONE);
            tvTitle.setText(strTitle);

        } else {
            
        }
    }

    /**
     * 设置透明度     *     * @param transAlpha 0-255 之间
     */
    public void setTranslucent(int transAlpha) {
        layRoot.setBackgroundColor(ColorUtils.setAlphaComponent(getResources().getColor(R.color.primary), transAlpha));
        tvTitle.setAlpha(transAlpha);
        tvLeft.setAlpha(transAlpha);
        tvRight.setAlpha(transAlpha);
    }

    /**
     * 设置数据     *     * @param strTitle     * @param resIdLeft     * @param strLeft     * @param resIdRight     * @param strRight     * @param listener
     */
    public void setData(String strTitle, int resIdLeft, String strLeft, int resIdRight, String strRight, final ActionBarClickListener listener) {
        if (!TextUtils.isEmpty(strTitle)) {
            tvTitle.setText(strTitle);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(strLeft)) {
            tvLeft.setText(strLeft);
            tvLeft.setVisibility(View.VISIBLE);
        } else {
            tvLeft.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(strRight)) {
            tvRight.setText(strRight);
            tvRight.setVisibility(View.VISIBLE);
        } else {
            tvRight.setVisibility(View.INVISIBLE);
            tvRight.setFocusable(false);
        }


        if (listener != null) {
            layLeft = findViewById(R.id.tv_actionbar_back);
            layRight = findViewById(R.id.tv_actionbar_enter);
            layLeft.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onLeftClick();
                }
            });
            layRight.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onRightClick();
                }
            });
        }
    }


    public interface ActionBarClickListener {

        void onLeftClick();

        void onRightClick();

    }


    /**
     * 获取actionBar
     *
     * @return
     */
    protected MyActionBar getMyActionBar() {
        return actionBar;
    }






//    Android 沉浸式状态栏与隐藏导航栏

    /**
     * 设置状态栏的颜色
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public   void statusBarTintColor(Activity activity, int color) {
        // 代表 5.0 及以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(color);
            return;
        }

        // versionCode > 4.4  and versionCode < 5.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup androidContainer = (ViewGroup) activity.findViewById(android.R.id.content);
            // 留出高度 setFitsSystemWindows  true代表会调整布局，会把状态栏的高度留出来
            View contentView = androidContainer.getChildAt(0);
            if (contentView != null) {
                contentView.setFitsSystemWindows(true);
            }
            // 在原来的位置上添加一个状态栏
            View statusBarView = createStatusBarView(activity);
            androidContainer.addView(statusBarView, 0);
            statusBarView.setBackgroundColor(color);
        }
    }

    /**
     * 创建一个需要填充statusBarView
     */
    public View createStatusBarView(Activity activity) {
        View statusBarView = new View(activity);
        ViewGroup.LayoutParams statusBarParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(statusBarParams);
        return statusBarView;
    }

    /**
     * 获取状态栏的高度
     */
    private    int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }

        Log.d("info","获取状态栏的高度resourceId="+resourceId);
        return result;
    }
    /**
     * 获取actionBar的高度
     */
    private    int getActionBarHeight(Context context) {
        int actionBarHeight1 = 0;
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
//            //方法一
//            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
//            Log.d("info","tv.data="+tv.data+",actionBarHeight="+actionBarHeight);

            //方法二
            int[] attribute = new int[] { android.R.attr.actionBarSize };
            TypedArray array = context.obtainStyledAttributes(tv.resourceId, attribute);
              actionBarHeight1 = array.getDimensionPixelSize(0 /* index */, -1 /* default size */);
            array.recycle();
//
//            //方法三
//            TypedArray actionbarSizeTypedArray = context.obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
//            float actionBarHeight2 = actionbarSizeTypedArray.getDimension(0, 0);
//            actionbarSizeTypedArray.recycle();
        }
        return  actionBarHeight1;
    }






}
