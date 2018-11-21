package com.gome.beautymirror.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.widget.ImageView;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import com.gome.beautymirror.contacts.ContactsManager;

import android.util.AttributeSet;

import com.gome.beautymirror.R;

public class RoundImageView extends ImageView {
    private int mBorderThickness = 0;
    private Context mContext;
    private int defaultColor = 0xFFFFFFF;
    // 如果只有其中一个有值，则只画一个圆形边框
    private int mBorderOutsideColor = 0;
    private int mBorderInsideColor =0;
    // 控件默认长、宽
    private int defaultWidth = 0;
    private int defaultHeight = 0;
    private int defaultRadius = 13;

    private boolean mIsCircle = true;
    //构造方法，参数上下文
    public RoundImageView(Context context,boolean isCricle) {
        super(context);
        mContext = context;
        mIsCircle = isCricle;
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setCustomAttributes(attrs);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        setCustomAttributes(attrs);
    }

    private void setCustomAttributes(AttributeSet attrs) {
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.MCircleImageView);
        mBorderThickness = a.getDimensionPixelSize(R.styleable.MCircleImageView_border_width, 2);
        mBorderOutsideColor = a.getColor(R.styleable.MCircleImageView_border_outside_color, defaultColor);
        mBorderInsideColor = a.getColor(R.styleable.MCircleImageView_border_inside_color, defaultColor);
        defaultRadius = a.getDimensionPixelOffset(R.styleable.MCircleImageView_radius, defaultRadius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mIsCircle){
            Drawable drawable = getDrawable();
            if (drawable == null) {
                return;
            }
            if (getWidth() == 0 || getHeight() == 0) {
                return;
            }
            this.measure(0, 0);
            if (drawable.getClass() == NinePatchDrawable.class)
                return;
            Bitmap b = ((BitmapDrawable) drawable).getBitmap();
            Bitmap bitmap;
            if(b!=null){
                bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
            }else{
                bitmap = ContactsManager.getInstance().getDefaultAvatarBitmap();
            }

            if (defaultWidth == 0) {
                defaultWidth = getWidth();
            }
            if (defaultHeight == 0) {
                defaultHeight = getHeight();
            }
            int radius = 0;

            if (mBorderInsideColor != defaultColor && mBorderOutsideColor != defaultColor) {// 定义画两个边框，分别为外圆边框和内圆边框
                radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2 - 2 * mBorderThickness;
                // 画内圆
                drawCircleBorder(canvas, radius + mBorderThickness / 2, mBorderInsideColor);
                // 画外圆
                drawCircleBorder(canvas, radius + mBorderThickness + mBorderThickness / 2, mBorderOutsideColor);
            } else if (mBorderInsideColor != defaultColor && mBorderOutsideColor == defaultColor) {// 定义画一个边框
                radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2 - mBorderThickness;
                drawCircleBorder(canvas, radius + mBorderThickness / 2, mBorderInsideColor);
            } else if (mBorderInsideColor == defaultColor && mBorderOutsideColor != defaultColor) {// 定义画一个边框
                radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2 - mBorderThickness;
                drawCircleBorder(canvas, radius + mBorderThickness / 2, mBorderOutsideColor);
            } else {// 没有边框
                radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2;
            }
            Bitmap roundBitmap = getCroppedRoundBitmap(bitmap, radius);
            canvas.drawBitmap(roundBitmap, defaultWidth / 2 - radius, defaultHeight / 2 - radius, null);
        }else {
            defaultRadius = (int)dpToPx(mContext,13);
            int minWidth = defaultRadius*2;
            int minHeight = defaultRadius*2;
            int radianWidth = (int)dpToPx(mContext,338);
            int radianHeight = (int)dpToPx(mContext,334);
            if (radianWidth >= minWidth && 334 > minHeight) {
                Path path = new Path();
                //四个角：右上，右下，左下，左上
                path.moveTo(defaultRadius, 0);
                path.lineTo(radianWidth - defaultRadius, 0);
                path.quadTo(radianWidth, 0, radianWidth, defaultRadius);

                path.lineTo(radianWidth, radianHeight - defaultRadius);
                path.quadTo(radianWidth, radianHeight, radianWidth - defaultRadius, radianHeight);

                path.lineTo(defaultRadius, radianHeight);
                path.quadTo(0, radianHeight, 0, radianHeight - defaultRadius);

                path.lineTo(0, defaultRadius);
                path.quadTo(0, 0, defaultRadius, 0);

                canvas.clipPath(path);
            }
            super.onDraw(canvas);

        }
    }

    private static float dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return dp * density;
    }

    /**
     * 获取裁剪后的圆形图片
     *
     * @param
     */
    public Bitmap getCroppedRoundBitmap(Bitmap bmp, int radius) {
        Bitmap scaledSrcBmp;
        int diameter = radius * 2;
        // 为了防止宽高不相等，造成圆形图片变形，因此截取长方形中处于中间位置最大的正方形图片
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        int squareWidth = 0, squareHeight = 0;
        int x = 0, y = 0;
        Bitmap squareBitmap;
        if (bmpHeight > bmpWidth) {// 高大于宽
            squareWidth = squareHeight = bmpWidth;
            x = 0;
            y = (bmpHeight - bmpWidth) / 2;
            // 截取正方形图片
            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth, squareHeight);
        } else if (bmpHeight < bmpWidth) {// 宽大于高
            squareWidth = squareHeight = bmpHeight;
            x = (bmpWidth - bmpHeight) / 2;
            y = 0;
            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth, squareHeight);
        } else {
            squareBitmap = bmp;
        }
        if (squareBitmap.getWidth() != diameter || squareBitmap.getHeight() != diameter) {
            scaledSrcBmp = Bitmap.createScaledBitmap(squareBitmap, diameter, diameter, true);
        } else {
            scaledSrcBmp = squareBitmap;
        }
        Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(),
                scaledSrcBmp.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(), scaledSrcBmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(scaledSrcBmp.getWidth() / 2,
                scaledSrcBmp.getHeight() / 2,
                scaledSrcBmp.getWidth() / 2,
                paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);
        bmp = null;
        squareBitmap = null;
        scaledSrcBmp = null;
        return output;
    }

    /**
     * 边缘画圆
     */
    private void drawCircleBorder(Canvas canvas, int radius, int color) {
        Paint paint = new Paint();
        /* 去锯齿 */
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColor(color);
        /* 设置paint的　style　为STROKE：空心 */
        paint.setStyle(Paint.Style.STROKE);
        /* 设置paint的外框宽度 */
        paint.setStrokeWidth(mBorderThickness);
        canvas.drawCircle(defaultWidth / 2, defaultHeight / 2, radius, paint);
    }
}
