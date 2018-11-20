
package cole.crop;

import android.graphics.BitmapRegionDecoder;

public class FullImageDecoder {
    // /private static int newCount = 0;
    // private static int releaseCount = 0;
    private boolean mIsRegionDecode;
    private ScreenNail mScreenNail;
    private BitmapRegionDecoder mRegionDecoder;

    public boolean isUseRegionDecoder() {
        return mIsRegionDecode;
    }

    public FullImageDecoder(ScreenNail s) {
        mIsRegionDecode = false;
        mScreenNail = s;
        // newCount++;
        // Log.e("zhy", "Allocat count:" + newCount);
    }

    public FullImageDecoder(BitmapRegionDecoder decoder) {
        mRegionDecoder = decoder;
        mIsRegionDecode = true;
    }

    public ScreenNail getScreenNail() {
        return mScreenNail;
    }

    public BitmapRegionDecoder getRegionDecoder() {
        return mRegionDecoder;
    }

    public void recycle() {
        if (mScreenNail != null) {
            // releaseCount++;
            // Log.e("zhy", "Release count:" + releaseCount);
            mScreenNail.recycle();
        }

        if (mRegionDecoder != null)
            mRegionDecoder.recycle();
    }

    public void clear() {
        if (mScreenNail != null) {
            // releaseCount++;
            // Log.e("zhy", "Release count:" + releaseCount);
            mScreenNail.recycle();
        }

    }

    public int getWidth() {
        if (mIsRegionDecode)
            return mRegionDecoder.getWidth();
        else
            return mScreenNail.getWidth();
    }

    public int getHeight() {
        if (mIsRegionDecode)
            return mRegionDecoder.getHeight();
        else
            return mScreenNail.getHeight();
    }
}
