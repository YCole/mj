package gome.beautymirror;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;

import com.gome.beautymirror.R;


public class BlurUtils{

    public static final String TAG = "BlurView";
    private static final float BITMAP_SCALE = 0.125f;

    public BlurUtils() {

    }

    private static Bitmap takeScreenShot(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        // 获取屏幕长和高
        int width = activity.getResources().getDisplayMetrics().widthPixels;
        int height = activity.getResources().getDisplayMetrics().heightPixels;

        Bitmap bmp = Bitmap.createBitmap(b1, 0, 0, width, height);
        view.destroyDrawingCache();
        return bmp;
    }

    public static Bitmap getBlurBackgroundDrawer(Activity activity) {
        Bitmap bmp = takeScreenShot(activity);
        return blurBitmap(activity,bmp,5f);
    }

    public static Bitmap blurBitmap(Context context, Bitmap image, float blurRadius) {
        if (null == image) {
            return null;
        }
        boolean isRgb8888=false;
        if (image.getConfig() != Bitmap.Config.ARGB_8888) {
            image = RGB565toARGB888(image);
            isRgb8888 = true;
        }
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
        if(isRgb8888){
            image.recycle();
            isRgb8888 = false;
        }
        RenderScript rs = null;
        try {
            rs = RenderScript.create(context);
        } catch (Exception e) {
            e.printStackTrace();

            inputBitmap.recycle();
            outputBitmap.recycle();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(Color.parseColor("#D9454545"));
            return bitmap;
        }
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

        blurScript.setRadius(blurRadius);
        blurScript.setInput(tmpIn);
        blurScript.forEach(tmpOut);

        tmpOut.copyTo(outputBitmap);

        blurScript.destroy();
        tmpIn.destroy();
        tmpOut.destroy();
        rs.destroy();
        inputBitmap.recycle();

        return outputBitmap;
    }

    private static Bitmap RGB565toARGB888(Bitmap img) {
        int numPixels = img.getWidth()* img.getHeight();
        int[] pixels = new int[numPixels];

        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());
        Bitmap result = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);
        result.setPixels(pixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());

        return result;
    }
}
