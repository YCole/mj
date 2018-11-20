package cole.crop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import java.io.ByteArrayOutputStream;


@SuppressLint("AppCompatCustomView")
public class ClipZoomImageView extends ImageView
        implements OnScaleGestureListener, OnTouchListener, ViewTreeObserver.OnGlobalLayoutListener {
    public static float SCALE_MAX = 4.0f;
    private static float SCALE_MID = 2.0f;

    private float initScale = 1.0f;
    private boolean once = true;


    private final float[] matrixValues = new float[9];


    private ScaleGestureDetector mScaleGestureDetector = null;
    private final Matrix mScaleMatrix = new Matrix();


    private GestureDetector mGestureDetector;
    private boolean isAutoScale;

    private int mTouchSlop;

    private float mLastX;
    private float mLastY;

    private boolean isCanDrag;
    private int lastPointerCount;

    private int mHorizontalPadding;


    private boolean isCheckLeftAndRight;

    private boolean isCheckTopAndBottom;
    int width;
    int height;

    public ClipZoomImageView(Context context) {
        this(context, null);
    }

    public ClipZoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        int temp = GalleryUtils.getDaoHangHeight(context);
        height = wm.getDefaultDisplay().getHeight() + temp;
        setScaleType(ScaleType.MATRIX);
        mGestureDetector = new GestureDetector(context, new SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (isAutoScale == true)
                    return true;

                float x = e.getX();
                float y = e.getY();
                Log.d("xiemin", "scale" + getScale());
                if (getScale() < SCALE_MID) {
                    ClipZoomImageView.this.postDelayed(new AutoScaleRunnable(SCALE_MID, x, y), 16);
                    isAutoScale = true;
                } else {
                    ClipZoomImageView.this.postDelayed(new AutoScaleRunnable(initScale, x, y), 16);
                    isAutoScale = true;
                }

                return true;
            }
        });
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        this.setOnTouchListener(this);
    }


    private class AutoScaleRunnable implements Runnable {
        static final float BIGGER = 1.07f;
        static final float SMALLER = 0.93f;
        private float mTargetScale;
        private float tmpScale;


        private float x;
        private float y;


        public AutoScaleRunnable(float targetScale, float x, float y) {
            this.mTargetScale = targetScale;
            this.x = x;
            this.y = y;
            if (getScale() < mTargetScale) {
                tmpScale = BIGGER;
            } else {
                tmpScale = SMALLER;
            }

        }

        @Override
        public void run() {

            mScaleMatrix.postScale(tmpScale, tmpScale, x, y);
            checkBorder();
            setImageMatrix(mScaleMatrix);

            final float currentScale = getScale();
            Log.d("ClipZoomImageView", "currentScale" + currentScale);

            if (((tmpScale > 1f) && (currentScale < mTargetScale))
                    || ((tmpScale < 1f) && (mTargetScale < currentScale))) {
                ClipZoomImageView.this.postDelayed(this, 16);
            } else

            {
                final float deltaScale = mTargetScale / currentScale;
                mScaleMatrix.postScale(deltaScale, deltaScale, x, y);
                checkBorder();
                setImageMatrix(mScaleMatrix);
                isAutoScale = false;
            }

        }
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scale = getScale();
        float scaleFactor = detector.getScaleFactor();

        if (getDrawable() == null)
            return true;


        if ((scale < SCALE_MAX && scaleFactor > 1.0f) || (scale > initScale && scaleFactor < 1.0f)) {
            if (scaleFactor * scale < initScale) {
                scaleFactor = initScale / scale;
            }
            if (scaleFactor * scale > SCALE_MAX) {
                scaleFactor = SCALE_MAX / scale;
            }

            mScaleMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            checkBorder();
            setImageMatrix(mScaleMatrix);
        }
        return true;
    }


    private RectF getMatrixRectF() {
        Matrix matrix = mScaleMatrix;
        RectF rect = new RectF();
        Drawable d = getDrawable();
        if (null != d) {
            rect.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(rect);
        }
        return rect;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event))
            return true;
        mScaleGestureDetector.onTouchEvent(event);

        float x = 0, y = 0;

        final int pointerCount = event.getPointerCount();

        for (int i = 0; i < pointerCount; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }
        x = x / pointerCount;
        y = y / pointerCount;


        if (pointerCount != lastPointerCount) {
            isCanDrag = false;
            mLastX = x;
            mLastY = y;
        }

        lastPointerCount = pointerCount;
        switch (event.getAction()) {
        case MotionEvent.ACTION_MOVE:
            float dx = x - mLastX;
            float dy = y - mLastY;

            if (!isCanDrag) {
                isCanDrag = isCanDrag(dx, dy);
            }
            if (isCanDrag) {
                if (getDrawable() != null) {

                    RectF rectF = getMatrixRectF();

                    isCheckLeftAndRight = true;
                    isCheckTopAndBottom = true;
                    if (rectF.width() < getWidth()) {
                        dx = 0;

                        isCheckLeftAndRight = false;
                    }


                    if (rectF.height() < getHeight()) {
                        dy = 0;

                        isCheckTopAndBottom = false;
                    }
                    mScaleMatrix.postTranslate(dx, dy);
                    checkBorder();
                    setImageMatrix(mScaleMatrix);
                }
            }
            mLastX = x;
            mLastY = y;
            break;

        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
            lastPointerCount = 0;
            break;
        }

        return true;
    }


    public final float getScale() {
        mScaleMatrix.getValues(matrixValues);
        return matrixValues[Matrix.MSCALE_X];
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }



    @Override
    public void onGlobalLayout() {
        if (once) {
            Drawable d = getDrawable();
            if (d == null)
                return;

            // getHVerticalPadding() = (getHeight() - (getWidth() - 2 *
            // mHorizontalPadding)) / 2;

            //int width = 1080;
            //int height = 1920;

            int drawableW = d.getIntrinsicWidth();
            int drawableH = d.getIntrinsicHeight();
            float scale = 1.0f;

            int frameSize = width - mHorizontalPadding * 2;


            Log.d("ClipZoomImageView", "drawableW--" + drawableW + "drawableH" + drawableH);
            if (drawableW > width && drawableH < height) {

                if (drawableW > drawableH) {
                    scale = Math.max(drawableW * 1.0f / width, height * 1.0f / drawableH);
                } else {
                    scale = Math.max(drawableW * 1.0f / width, drawableH * 1.0f / height);
                }
            }

            if (drawableW == width && drawableH < height) {
                if (drawableW > drawableH) {
                    scale = Math.max(drawableW * 1.0f / width, height * 1.0f / drawableH);
                } else {
                    scale = Math.max(width * 1.0f / drawableW, drawableH * 1.0f / height);
                }
            }

            if (drawableW < width && drawableH > height) {
                if (drawableH > drawableW) {
                    scale = Math.max(height * 1.0f / drawableH, width * 1.0f / drawableW);
                } else {
                    scale = Math.max(drawableH * 1.0f / height, width * 1.0f / drawableW);
                }
            }

            if (drawableW < width && drawableH == height) {
                if (drawableH > drawableW) {
                    scale = Math.max(height * 1.0f / drawableH, width * 1.0f / drawableW);
                } else {
                    scale = Math.max(drawableH * 1.0f / height, width * 1.0f / drawableW);
                }
            }

            if (drawableW > width && drawableH > height) {

                scale = Math.max(width * 1.0f / drawableW, height * 1.0f / drawableH);
            }

            if (drawableW < width && drawableH < height) {

                scale = Math.max(width * 1.0f / drawableW, height * 1.0f / drawableH);
            }
            Log.d("xiemin", "initScale" + scale);
            initScale = scale;
            SCALE_MID = initScale / 4;
            SCALE_MAX = initScale * 4;
            mScaleMatrix.postTranslate((width - drawableW) / 2, (height - drawableH) / 2);
            mScaleMatrix.postScale(scale, scale, width / 2, height / 2);


            setImageMatrix(mScaleMatrix);
            once = false;
        }
    }


    public Bitmap clip() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 500) {
            baos.reset();
            options -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }

        Canvas canvas = new Canvas(bitmap);
        draw(canvas);
        return Bitmap.createBitmap(bitmap, mHorizontalPadding, 0, getWidth(), getHeight());
    }


    private void checkBorder() {

        RectF rectF = getMatrixRectF();

        float deltaX = 0.0f;
        float deltaY = 0.0f;

        int bmpWidth = getWidth();

        int bmpWHeight = getHeight();

        if (isCheckLeftAndRight) {

            if (rectF.left > 0) {

                deltaX = -rectF.left;
            }

            if (rectF.right < bmpWidth) {

                deltaX = bmpWidth - rectF.right;
            }
        }

        if (isCheckTopAndBottom) {

            if (rectF.top > 0) {

                deltaY = -rectF.top;
            }

            if (rectF.bottom < bmpWHeight) {

                deltaY = bmpWHeight - rectF.bottom;
            }
        }

        mScaleMatrix.postTranslate(deltaX, deltaY);
    }


    private boolean isCanDrag(float dx, float dy) {
        return Math.sqrt((dx * dx) + (dy * dy)) >= mTouchSlop;
    }

    public void setHorizontalPadding(int mHorizontalPadding) {
        this.mHorizontalPadding = mHorizontalPadding;
    }

    private int getHVerticalPadding() {
        return (getHeight() - (getWidth() - 2 * mHorizontalPadding)) / 2;
    }
}
