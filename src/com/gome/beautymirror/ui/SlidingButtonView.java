package com.gome.beautymirror.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.gome.beautymirror.R;

public class SlidingButtonView extends HorizontalScrollView {
    private TextView mTextView_Delete;
    private int leftWidth;
    private int mScrollWidth;
    private IonSlidingButtonListener mIonSlidingButtonListener;
    private Boolean isOpen = false;

    public Boolean getOpen() {
        return isOpen;
    }

    public void setOpen(Boolean open) {
        isOpen = open;
    }

    private Boolean once = false;

    public SlidingButtonView(Context context) {
        this(context, null);
    }

    public SlidingButtonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!once) {
            mTextView_Delete = (TextView) findViewById(R.id.history_delete);
            once = true;

        }
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            mScrollWidth = mTextView_Delete.getWidth();
            this.scrollTo(0, 0);

        }
    }

    private boolean canTouch = true;

    public boolean isCanTouch() {
        return canTouch;
    }

    public int getLeftWidth() {
        return leftWidth;
    }

    public void setCanTouch(boolean canTouch) {
        this.canTouch = canTouch;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!canTouch) {
            return true;
        }
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mIonSlidingButtonListener.onDownOrMove(this);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                changeScrollx();
                return true;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public void changeScrollx() {
        if (getScrollX()-leftWidth >= (mScrollWidth / 2)) {
            this.smoothScrollTo(mScrollWidth + leftWidth, 0);
            isOpen = true;
            mIonSlidingButtonListener.onMenuIsOpen(this);
        } else {
            this.smoothScrollTo(leftWidth, 0);
            isOpen = false;
        }
    }

    Handler scrollHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==2) {
                SlidingButtonView.this.smoothScrollTo(leftWidth,0);
            } else if (msg.what==1) {
                SlidingButtonView.this.smoothScrollTo(0,0);
            }
        }
    };

    public void openMenu() {
        Message msg = new Message();
        msg.what= 1;
        scrollHandler.sendMessage(msg);

        isOpen = true;
        mIonSlidingButtonListener.onMenuIsOpen(this);
    }

    public void closeMenu() {
        if (!isOpen) {
            return;
        }
        Message msg = new Message();
        msg.what= 2;
        scrollHandler.sendMessage(msg);

        isOpen = false;
    }

    public void setSlidingButtonListener(IonSlidingButtonListener listener) {
        mIonSlidingButtonListener = listener;
    }

    public interface IonSlidingButtonListener {
        void onMenuIsOpen(View view);

        void onDownOrMove(SlidingButtonView slidingButtonView);
    }

}