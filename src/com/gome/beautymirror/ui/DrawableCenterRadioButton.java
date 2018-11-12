package gome.beautymirror.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RadioButton;

/**
 * Drawable居中的TextView
 */
public class DrawableCenterRadioButton extends RadioButton {
    /** 字体高度 */
    private float mFontHeight;
    /** 位图集合 */
    private Drawable[] mDrawables;
    /** 字体偏移数据 */
    private float mOffSize;

    /**
     * 构造函数
     *
     * @param context 上下文
     */
    public DrawableCenterRadioButton(Context context) {
        super(context);
        init();
    }

    /**
     * 构造函数
     *
     * @param context 上下文
     * @param attrs 属性
     */
    public DrawableCenterRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 构造函数
     *
     * @param context 上下文
     * @param attrs 属性
     * @param defStyle 样式
     */
    public DrawableCenterRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mDrawables = getCompoundDrawables();
//        if (mDrawables[0] != null || mDrawables[2] != null) {
//            // 左、右
//            setGravity(Gravity.CENTER_VERTICAL | (mDrawables[0] != null ? Gravity.LEFT : Gravity.RIGHT));
//
//        } else
            if (mDrawables[1] != null || mDrawables[3] != null) {
            // 上、下
            setGravity(Gravity.CENTER_HORIZONTAL | (mDrawables[1] != null ? Gravity.TOP : Gravity.BOTTOM));
            Paint.FontMetrics fm = getPaint().getFontMetrics();
            mFontHeight = (float) Math.ceil(fm.descent - fm.ascent);
        }
        super.onLayout(changed, left, top, right, bottom);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        int drawablePadding = getCompoundDrawablePadding();
        if (mDrawables[1] != null) {
            // 上
            int drawableHeight = mDrawables[1].getIntrinsicHeight()/3;
            float bodyHeight = mFontHeight + drawableHeight + drawablePadding;
            if (TextUtils.isEmpty(getText())) {
                bodyHeight = drawableHeight;

            }
            mOffSize = (getHeight() - bodyHeight) / 2;

            canvas.translate(0, 12.5f);
        }
        super.onDraw(canvas);
    }

    public float getOffSize() {
        return mOffSize;
    }

    /**
     * 初始化
     */
    void init() {

    }
}
