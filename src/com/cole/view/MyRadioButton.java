package cole.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;

import com.gome.beautymirror.R;


public class MyRadioButton extends AppCompatButton {
    //图片大小
    int mDrawableSize = 50;// xml文件中设置的大小

    public MyRadioButton(Context context) {
        this(context, null);
    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        Drawable drawableLeft = null, drawableTop = null, drawableRight = null, drawableBottom = null;
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.MyRadioButton);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            Log.i("MyRadioButton", "attr:" + attr);
            switch (attr) {
                case R.styleable.MyRadioButton_drawableSize:
                    mDrawableSize = a.getDimensionPixelSize(R.styleable.MyRadioButton_drawableSize, 40);
                    Log.i("MyRadioButton", "mDrawableSize:" + mDrawableSize);
                    break;
                case R.styleable.MyRadioButton_drawableTop:
                    drawableTop = a.getDrawable(attr);
                    break;
                case R.styleable.MyRadioButton_drawableBottom:
                    drawableRight = a.getDrawable(attr);
                    break;
                case R.styleable.MyRadioButton_drawableRight:
                    drawableBottom = a.getDrawable(attr);
                    break;
                case R.styleable.MyRadioButton_drawableLeft:
                    drawableLeft = a.getDrawable(attr);
                    break;
                default:
                    break;
            }
        }


        //释放资源
        a.recycle();

        setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
    }

    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        if (left != null) {
            left.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        if (right != null) {
            right.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        if (top != null) {
            top.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        if (bottom != null) {
            bottom.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        setCompoundDrawables(left, top, right, bottom);


    }

}
