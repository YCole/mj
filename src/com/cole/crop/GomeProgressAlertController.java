package cole.crop;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gome.beautymirror.R;

import java.text.NumberFormat;

/**
 * Created by zhenjie on 2017/9/14.
 */

public class GomeProgressAlertController {
    private static final String TAG = "GomeProgressDialog";

    private static final int MESSAGE_PROGRESS_UPDATE = 0;

    public static final int STYLE_SPINNER = 0;
    public static final int STYLE_HORIZONTAL = 1;

    private CharSequence mTitle;
    private CharSequence mMessage;
    private int mProgressStyle = STYLE_SPINNER;

    private TextView mTitleView;
    private TextView mMessageView;
    private ProgressBar mProgressBar;
    private TextView mPregressPercent;
    private NumberFormat mProgressPercentFormat;

    private TextView mPregressNum;
    private String mProgressNumberFormat;

    private int mProgressVal;
    private int mSecondaryProgressVal;
    private int mMax;
    private boolean mIndeterminate;

    private Handler mViewUpdateHandler;

    private Context mContext;
    private DialogInterface mDialogInterface;
    private Window mWindow;

    private int mProgressAlertLayout;
    private int mSpinnerProgressHeight;

    private GomeProgressAlertController(Context context, DialogInterface di, Window window) {
        mContext = context;
        mDialogInterface = di;
        mWindow = window;

        mProgressAlertLayout = R.layout.gome_progress_dialog_layout;
        window.requestFeature(Window.FEATURE_NO_TITLE);
        mSpinnerProgressHeight = mContext.getResources().getDimensionPixelOffset(R.dimen.progress_spinner_height_gome);
    }

    /**
     * 創建一個GomeProgressAlertController对象
     * @param context Context
     * @param di
     * @param window
     * @return
     */
    public static final GomeProgressAlertController create(Context context, DialogInterface di, Window window) {
        return new GomeProgressAlertController(context, di, window);
    }

    /**
     * 创建Dialog的布局内容
     */
    public void installContent() {
        int contentView = selectContentView();
        mWindow.setContentView(contentView);
        setupView();
    }

    /**
     * 选择GomeProgressDialog的根布局文件，目前只有一种，以后可能根据不同情况进行扩展
     * @return
     */
    private int selectContentView() {
        return mProgressAlertLayout;
    }

    /**
     * 设置布局内容
     */
    private void setupView() {
        final ViewGroup parentPanel = (ViewGroup) mWindow.findViewById(R.id.parentPanel);
        final ViewGroup titlePanel = (ViewGroup) mWindow.findViewById(R.id.topPanel);
        final ViewGroup contentPanel = (ViewGroup) mWindow.findViewById(R.id.contentPanel);

        setupTitle(titlePanel);
        setupContent(contentPanel);

    }

    /**
     * 设置ProgressBar内容区域
     * @param contentPanel
     */
    private void setupContent(ViewGroup contentPanel){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if(mProgressStyle == STYLE_HORIZONTAL){
            mWindow.setBackgroundDrawableResource(R.drawable.gome_bg_dialog);
            mViewUpdateHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if(mProgressBar != null){
                        int progress = mProgressBar.getProgress();
                        int max = mProgressBar.getMax();
                        if(mProgressNumberFormat != null){
                            String format = mProgressNumberFormat;
                            mPregressNum.setText(String.format(format, progress, max));
                        }else{
                            mPregressNum.setText("");
                        }
                        if(mProgressPercentFormat != null){
                            double percent = (double) progress / (double) max;
                            SpannableString temString = new SpannableString(mProgressPercentFormat.format(percent));
                            temString.setSpan(new StyleSpan(Typeface.BOLD), 0, temString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            mPregressPercent.setText(temString);
                        }else{
                            mPregressPercent.setText("");
                        }
                    }
                }
            };
            View view = inflater.inflate(R.layout.gome_progress_horizontal_layout, null);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
            if(mProgressBar != null){
                mProgressBar.setMax(mMax);
                mProgressBar.setIndeterminate(mIndeterminate);
            }
            mPregressPercent = (TextView) view.findViewById(R.id.progress_percent);
            mPregressNum = (TextView) view.findViewById(R.id.progress_number);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            contentPanel.addView(view, params);
        }else{
            mWindow.setBackgroundDrawableResource(R.drawable.gome_bg_dialog_dark);
            View view = inflater.inflate(R.layout.gome_progress_spinner_layout, null);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
            mMessageView = (TextView) view.findViewById(R.id.message);
            mMessageView.setText(mMessage);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.height = mSpinnerProgressHeight;
            contentPanel.addView(view, params);
        }
    }

    /**
     * 设置ProgressBar的标题区域
     * @param titlePanel
     */
    private void setupTitle(ViewGroup titlePanel){
        mTitleView = (TextView) mWindow.findViewById(R.id.alertTitle);
        boolean hasTitle = !TextUtils.isEmpty(mTitle);
        if(hasTitle && mTitleView != null){
            titlePanel.setVisibility(View.VISIBLE);
            mTitleView.setText(mTitle);
        }else{
            titlePanel.setVisibility(View.GONE);
        }
    }

    public void initFormates(){
        mProgressNumberFormat = "%1d/%2d";
        mProgressPercentFormat = NumberFormat.getPercentInstance();
        mProgressPercentFormat.setMaximumFractionDigits(0);
    }

    /**
     * 设置ProgressBarDialog的标题
     * @param title
     */
    public void setTitle(String title){
        mTitle = title;
        if(mTitleView != null){
            mTitleView.setText(mTitle);
        }
    }

    /**
     * 设置ProgressBarDialog的Message
     * @param message
     */
    public void setMessage(String message){
        mMessage = message;
        if(mMessageView != null){
            mMessageView.setText(mMessage);
        }
    }

    /**
     * 设置ProgressBarDialog的样式
     * @param progressStyle
     */
    public void setProgressStyle(int progressStyle){
        mProgressStyle = progressStyle;
    }

    /**
     * 设置水平进度条的progress值
     * @param value
     */
    public void setProgress(int value){
        mProgressVal = value;
        if(mProgressBar != null){
            mProgressBar.setProgress(value);
            onProgressChanged();
        }
    }

    /**
     * 设置Sencondary的进度值
     * @param value
     */
    public void setSencondaryProgress(int value){
        mSecondaryProgressVal = value;
        if(mProgressBar != null){
            mProgressBar.setSecondaryProgress(value);
            onProgressChanged();
        }
    }

    /**
     * 设置Progress的Max值
     * @param max
     */
    public void setMax(int max){
        mMax = max;
        if(mProgressBar != null){
            mProgressBar.setMax(max);
        }
    }

    /**
     * 获取Progress的值
     * @return
     */
    public int getProgress(){
        if(mProgressBar != null){
            return mProgressBar.getProgress();
        }
        return mProgressVal;
    }

    /**
     * 获取SecondaryProgress的值
     * @return
     */
    public int getSecondaryProgress(){
        if(mProgressBar != null){
            return mProgressBar.getSecondaryProgress();
        }
        return mSecondaryProgressVal;
    }

    /**
     * 获取ProgressBar的Max值
     * @return
     */
    public int getMax(){
        if(mProgressBar != null){
            return mProgressBar.getMax();
        }
        return mMax;
    }

    /**
     * 设置ProgressBar的indeterminate
     * @param indeterminate
     */
    public void setIndeterminate(boolean indeterminate){
        mIndeterminate = indeterminate;
        if(mProgressBar != null){
            mProgressBar.setIndeterminate(indeterminate);
        }
    }

    /**
     * 判断ProgressBar是否处于indeterminate状态
     * @return
     */
    public boolean isIndeterminate(){
        if(mProgressBar != null){
            return mProgressBar.isIndeterminate();
        }
        return mIndeterminate;
    }

    /**
     * 按照diff的值增加
     * @param diff
     */
    public void incrementProgressBy(int diff){
        if(mProgressBar != null){
            mProgressBar.incrementProgressBy(diff);
            onProgressChanged();
        }
    }

    /**
     * 按照diff的值增加
     * @param diff
     */
    public void incrementSecondaryProgressBy(int diff){
        if(mProgressBar != null){
            mProgressBar.incrementSecondaryProgressBy(diff);
            onProgressChanged();
        }
    }

    public void setProgressNumberFormat(String format) {
        mProgressNumberFormat = format;
        onProgressChanged();
    }

    public void setProgressPercentFormat(NumberFormat format) {
        mProgressPercentFormat = format;
        onProgressChanged();
    }

    private void onProgressChanged(){
        if(mProgressStyle == STYLE_HORIZONTAL){
            if(mViewUpdateHandler != null && !mViewUpdateHandler.hasMessages(MESSAGE_PROGRESS_UPDATE)){
                mViewUpdateHandler.sendEmptyMessage(MESSAGE_PROGRESS_UPDATE);
            }
        }
    }

    public static class AlertParams {
        public final Context mContext;
        public final LayoutInflater mInflater;

        public String mTitle;
        public String mMessage;
        public boolean mCancelable;
        public int mProgressBarStyle = STYLE_SPINNER;
        public int mMax;
        public boolean mIndeterminate;

        public DialogInterface.OnCancelListener mOnCancelListener;
        public DialogInterface.OnDismissListener mOnDismissListener;

        public AlertParams(Context context) {
            mContext = context;
            mCancelable = true;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void apply(GomeProgressAlertController dialog) {
            if(mTitle != null){
                dialog.setTitle(mTitle);
            }
            if(mMessage != null){
                dialog.setMessage(mMessage);
            }
            if(mMax != 0){
                dialog.setMax(mMax);
            }
            dialog.setIndeterminate(mIndeterminate);
            dialog.setProgressStyle(mProgressBarStyle);
        }

    }
}
