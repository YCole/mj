package cole.crop;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

public class ClipImageBorderView extends View {
    private int mHorizontalPadding;
    private int mVerticalPadding;

    private int mWidth;

    private int mBorderColor = Color.parseColor("#00000000");

    private int mBorderWidth = 1;

    private Paint mPaint;
    private static int width;
    private static int height;

    public ClipImageBorderView(Context context) {
        this(context, null);
    }

    public ClipImageBorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipImageBorderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        int temp = GalleryUtils.getDaoHangHeight(context);
        height = wm.getDefaultDisplay().getHeight() + temp;
        mBorderWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mBorderWidth,
                getResources().getDisplayMetrics());
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mWidth = getWidth();

        mVerticalPadding = (getHeight() - mWidth) / 2;
        mPaint.setColor(Color.parseColor("#00000000"));
        mPaint.setStyle(Style.FILL);

        canvas.drawRect(0, 0, 0, height, mPaint);

        canvas.drawRect(width, 0, width, height, mPaint);

        canvas.drawRect(0, 0, width, 0, mPaint);

        canvas.drawRect(0, height, width, height, mPaint);

        mPaint.setColor(mBorderColor);
        mPaint.setStrokeWidth(0);
        mPaint.setStyle(Style.STROKE);
        canvas.drawRect(0, 0, width - 0, height, mPaint);

    }

    public void setHorizontalPadding(int mHorizontalPadding) {
        this.mHorizontalPadding = mHorizontalPadding;
    }
}
