package gome.beautymirror.ui;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.beautymirror.R;

public class MyToast extends Toast {
    public final static int LENGTH_SHORT = Toast.LENGTH_SHORT;
    public final static int LENGTH_LONG = Toast.LENGTH_LONG;

    /**
     * Toast单例
     */
    private static MyToast toast;

    /**
     * 构造
     * @param context
     */
    public MyToast(Context context) {
        super(context);
    }

    /**
     * 隐藏当前Toast
     */
    public static void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }

    public void cancel() {
        try {
            super.cancel();
        } catch (Exception e) {

        }
    }

    @Override
    public void show() {
        try {
            super.show();
        } catch (Exception e) {

        }
    }

    /**
     * 初始化Toast
     *
     * @param context 上下文
     * @param text    显示的文本
     */
    private static void initToast(Context context, CharSequence text) {
        try {
            cancelToast();
            toast = new MyToast(context);

            View toastView =LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
            LinearLayout relativeLayout = (LinearLayout)toastView.findViewById(R.id.toast_linear);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int)dpToPx(context, 40));
            layoutParams.setMargins(10,0,10,0);
            relativeLayout.setLayoutParams(layoutParams);
            TextView toast_text = (TextView) toastView.findViewById(R.id.toast_text);
            toast_text.setText(text);
            toast.setView(toastView);
            toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_VERTICAL, 0, (int)dpToPx(context,20));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static float dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return dp * density;
    }

    public static void showToast(Context context, CharSequence text, int time) {
        initToast(context, text);
        if (time == LENGTH_LONG) {
            toast.setDuration(Toast.LENGTH_LONG);
        } else {
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }
}
