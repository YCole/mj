package cole.crop;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;


import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import java.text.NumberFormat;

/**
 * Created by zhenjie on 2017/9/14.
 */

public class GomeProgressDialog extends Dialog implements DialogInterface {
    private static final String TAG = "GomeProgressDialog";

    private GomeProgressAlertController mAlert;

    /**
     * 创建一个圆形的ProgressBar,这个是默认样式
     */
    public static final int STYLE_SPINNER = 0;

    /**
     * 创建一个水平进度的ProgressBar
     */
    public static final int STYLE_HORIZONTAL = 1;

    public GomeProgressDialog(Context context, int themeResId) {
        super(context);
        mAlert = GomeProgressAlertController.create(getContext(), this, getWindow());
        mAlert.initFormates();
    }

    public GomeProgressDialog(Context context) {
        this(context, 0);
    }

    public GomeProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        this(context);
        setCancelable(cancelable);
        setOnCancelListener(cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //創建GomeProgressDialog的布局内容
        mAlert.installContent();
    }


    /**
     * 设置GomePrgressDialog的Title信息
     *
     * @param title
     */
    public void setTitle(String title) {
        super.setTitle(title);
        if (mAlert != null) {
            mAlert.setTitle(title);
        }
    }

    /**
     * 设置GomePrgressDialog的Title信息
     *
     * @param titleId
     */
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        if (mAlert != null) {
            mAlert.setTitle(getContext().getString(titleId));
        }
    }

    /**
     * 设置GomePrgressDialog的Message信息
     *
     * @param message
     */
    public void setMessage(String message) {
        if (mAlert != null) {
            mAlert.setMessage(message);
        }
    }

    /**
     * 设置GomeProgressDialog的进度
     *
     * @param value
     */
    public void setProgress(int value) {
        if (mAlert != null) {
            mAlert.setProgress(value);
        }
    }

    /**
     * 设置GomeProgressDialog的secondary进度
     */
    public void setSecondaryProgress(int secondValue) {
        if (mAlert != null) {
            mAlert.setSencondaryProgress(secondValue);
        }
    }

    /**
     * 获取Progress的值
     *
     * @return
     */
    public int getProgress() {
        if (mAlert != null) {
            return mAlert.getProgress();
        }
        return 0;
    }

    /**
     * 获取Secondary progress的值
     *
     * @return
     */
    public int getSecondaryProgress() {
        if (mAlert != null) {
            return mAlert.getSecondaryProgress();
        }
        return 0;
    }

    /**
     * 设置GomeProgressDialog的样式.
     *
     * @param style STYLE_SPINNER 圆形进度条 STYLE_HORIZONTAL 水平进度条
     */
    public void setProgressStyle(int style) {
        if (mAlert != null) {
            mAlert.setProgressStyle(style);
        }
    }

    /**
     * 设置ProgressBar的Max值
     *
     * @param max
     */
    public void setMax(int max) {
        if (mAlert != null) {
            mAlert.setMax(max);
        }
    }

    /**
     * 获取ProgressBar的max值
     *
     * @return
     */
    public int getMax() {
        if (mAlert != null) {
            return mAlert.getMax();
        }
        return 0;
    }

    /**
     * 设置ProgressBar的indeterminate模式
     *
     * @param indeterminate
     */
    public void setIndeterminate(boolean indeterminate) {
        if (mAlert != null) {
            mAlert.setIndeterminate(indeterminate);
        }
    }

    /**
     * 判断ProgressBar是否处于indeterminate状态
     *
     * @return
     */
    public boolean isIndeterminate() {
        if (mAlert != null) {
            return mAlert.isIndeterminate();
        }
        return false;
    }

    /**
     * incrementProgressBy
     *
     * @param diff
     */
    public void incrementProgressBy(int diff) {
        if (mAlert != null) {
            mAlert.incrementProgressBy(diff);
        }
    }

    /**
     * incrementSecondaryProgressBy
     *
     * @param diff
     */
    public void incrementSecondaryProgressBy(int diff) {
        if (mAlert != null) {
            mAlert.incrementSecondaryProgressBy(diff);
        }
    }

    public void setProgressNumberFormat(String format) {
        if (mAlert != null) {
            mAlert.setProgressNumberFormat(format);
        }
    }

    public void setProgressPercentFormat(NumberFormat format) {
        if (mAlert != null) {
            mAlert.setProgressPercentFormat(format);
        }
    }

    @Override
    protected void onStart() {
        Window dialogWindow = getWindow();
        dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogWindow.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.dimAmount = 0.2f;
        lp.x = 0;
        lp.y = 25;
        dialogWindow.setAttributes(lp);
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public static class Builder {

        private GomeProgressAlertController.AlertParams P;

        /**
         * 创建一个GomeProgressDialog的Builder
         *
         * @param context
         */
        public Builder(Context context) {
            P = new GomeProgressAlertController.AlertParams(context);
        }

        /**
         * 返回Builder的Context
         *
         * @return
         */
        public Context getContext() {
            return P.mContext;
        }

        /**
         * 设置GomePrgressDialog的title信息
         *
         * @param titleId
         * @return
         */
        public Builder setTitle(int titleId) {
            P.mTitle = P.mContext.getText(titleId).toString();
            return this;
        }

        /**
         * 设置GomePrgressDialog的title信息
         *
         * @param title
         * @return
         */
        public Builder setTitle(CharSequence title) {
            P.mTitle = title.toString();
            return this;
        }

        /**
         * 设置GomeProgressDialog的Message信息
         *
         * @param messageId
         * @return
         */
        public Builder setMessage(int messageId) {
            P.mMessage = P.mContext.getText(messageId).toString();
            return this;
        }

        /**
         * 设置GomeProgressDialog的Message信息
         *
         * @param message
         * @return
         */
        public Builder setMessage(CharSequence message) {
            P.mMessage = message.toString();
            return this;
        }

        /**
         * 设置GomeProgressDialog的style样式
         *
         * @param style GomeProgressDialog.STYLE_SPINNER 圆环样式
         *              GomeProgressDialog.STYLE_HORIZONTAL 水平进度条
         * @return
         */
        public Builder setProgressStyle(int style) {
            P.mProgressBarStyle = style;
            return this;
        }

        /**
         * 设置GomeProgressDialog的取消监听事件
         *
         * @param onCancelListener
         * @return
         */
        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            P.mOnCancelListener = onCancelListener;
            return this;
        }

        /**
         * 设置GomeProgressDialog的Dismiss监听事件
         *
         * @param onDismissListener
         * @return
         */
        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            P.mOnDismissListener = onDismissListener;
            return this;
        }

        /**
         * 設置ProgressBar的max值
         *
         * @param max
         * @return
         */
        public Builder setMax(int max) {
            P.mMax = max;
            return this;
        }

        /**
         * 设置ProgressBar的indeterninate状态
         *
         * @param indeterminate
         * @return
         */
        public Builder setIndeterminate(boolean indeterminate) {
            P.mIndeterminate = indeterminate;
            return this;
        }

        /**
         * 设置Dialog是否可以取消
         *
         * @param cancelable
         * @return
         */
        public Builder setCancelable(boolean cancelable) {
            P.mCancelable = cancelable;
            return this;
        }

        /**
         * create方法用于创建一个GomeProgressDialog, 创建的时候使用Builder中设置的参数。
         * 可以直接使用show方法来替代此方法
         *
         * @return
         */
        public GomeProgressDialog create() {
            // Context has already been wrapped with the appropriate theme.
            final GomeProgressDialog dialog = new GomeProgressDialog(P.mContext, true, null);
            P.apply(dialog.mAlert);
            dialog.setCancelable(P.mCancelable);
            if (P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(P.mOnCancelListener);
            dialog.setOnDismissListener(P.mOnDismissListener);
            return dialog;
        }

        /**
         * create方法用于创建一个GomeProgressDialog, 创建的时候使用Builder中设置的参数。
         * 创建完成后显示此Dialog
         *
         * @return
         */
        public GomeProgressDialog show() {
            final GomeProgressDialog dialog = create();
            dialog.show();
            return dialog;
        }


    }

}
