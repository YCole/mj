
package cole.crop;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Log;
import android.view.WindowManager;

import com.zte.theme.ThemeManager;
import com.zte.theme.ThemeManagerCallback;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public class LauncherProviderHelper {
    private static final String TAG = "LauncherProviderHelper";

    public static final String AUTHORITY = "content://com.android.gallery3d.fileprovider/cached/";

    public static final String CUR_DESK_WALLPAPER_FILE = AUTHORITY + "/curWallpaper0.jpg";
    public static final String DESK_GALLERY_ICON_FILE = AUTHORITY + "/curWallpaper0_gallery.jpg";

    public static final String CUR_LOCK_WALLPAPER_FILE = AUTHORITY + "/curWallpaper1.jpg";
    public static final String LOCK_GALLERY_ICON_FILE = AUTHORITY + "/curWallpaper1_gallery.jpg";

    private static final String ACTION_LOCK_WALLPAPER_CHANGED = "com.android.gallery3d.lock_wallpaper_changed";
    public static final String ACTION_GALLERY_SET_WALLPAPER = "com.android.gallery3d.GALLERY_SET_WALLPAPER";

    private static LauncherProviderHelper mInstance;
    protected Context mContext;
    protected ContentResolver mContentResolver;

    private ThemeManager mThemeManager = null;

    public static synchronized LauncherProviderHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new LauncherProviderHelper(context);
        } 
        
        return mInstance;
    }

    private LauncherProviderHelper(Context context) {
        mContext = context;
        mContentResolver = context.getContentResolver();
        
        if(mThemeManager == null){
        	Log.w(TAG, "send connected message 1");
        	mThemeManager = mThemeManager.getInstance(mContext, new ThemeManagerCallback(){
				@Override
				public void onServiceConnected() {
					// TODO Auto-generated method stub
					Log.w(TAG, "receive connected message");				
				}
			});
        }
    }

    public boolean setGalleryWallpaper(final Bitmap bitmap, int type) throws IOException {
        Log.d(TAG, "setGalleryWallpaper begin..." + type);

        if (bitmap == null) {
            Log.e(TAG, "setGalleryWallpaper bitmap is null.");
            return false;
        }

        if (type != 0 && type != 1) {
            Log.e(TAG, "setGalleryWallpaper type is Illegal.");
            return false;
        }

        //add by yujun for SetWallpaper
        final WallpaperManager wm = WallpaperManager.getInstance(mContext);

        
        String wallpaperUri = type == 0 ? CUR_DESK_WALLPAPER_FILE : CUR_LOCK_WALLPAPER_FILE;
        //String GalleryIconUri = type == 0 ? DESK_GALLERY_ICON_FILE : LOCK_GALLERY_ICON_FILE;

        Bitmap bitmap2 = clipWallpaper(mContext, bitmap);        
        boolean isSuccess = saveBitmapToInputStream(bitmap2);
        /*
        boolean isSuccess = saveBitmapToProvider(bitmap, wallpaperUri);
        if (!isSuccess) {
            Log.i(TAG, "save Gallery bitmap failed.");
            return false;
        }
        //int width = mContext.getResources().getDimensionPixelSize(180);
        int width = mContext.getResources().getDimensionPixelSize(R.dimen.launcher_setwallpaper_width);
        Bitmap icon = getBitmap(bitmap, width);
        if (icon != null) {
            saveBitmapToProvider(icon, GalleryIconUri);
        }

        Log.d(TAG, "setGalleryLockScreenWallpaper end.");

        if (type == 1) {
            mContext.sendBroadcast(new Intent(ACTION_LOCK_WALLPAPER_CHANGED));
        }

        Intent intent = new Intent(ACTION_GALLERY_SET_WALLPAPER);
        intent.putExtra("type", type);
        mContext.sendBroadcast(intent);*/
        return isSuccess;
    }

    private static Bitmap getBitmap(Bitmap bitmap, int width) {
        bitmap = scaleBitMap(bitmap, width);
        bitmap = regularBitMap(bitmap);
        return bitmap;
    }

    private static Bitmap scaleBitMap(Bitmap bitmap, int width) {
        if (bitmap == null) {
            return null;
        }
        int bitmapWidth = bitmap.getWidth();
        if (bitmapWidth == width) {
            return bitmap;
        }
        float scale = width / (float) bitmapWidth;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        if (bitmap != resizeBmp) {
            bitmap.recycle();
        }
        return resizeBmp;
    }

    private static Bitmap regularBitMap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        if (bitmapWidth == bitmapHeight) {
            return bitmap;
        }
        int x = bitmapHeight > bitmapWidth ? 0 : ((bitmapWidth - bitmapHeight) / 2);
        int y = bitmapHeight > bitmapWidth ? ((bitmapHeight - bitmapWidth) / 2) : 0;
        int height = bitmapHeight > bitmapWidth ? bitmapWidth : bitmapHeight;
        Bitmap newbitmap = Bitmap.createBitmap(bitmap, x, y, height, height);
        if (bitmap != newbitmap) {
            bitmap.recycle();
        }
        return newbitmap;
    }

    private static Bitmap clipWallpaper(Context context, Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        Log.d(TAG, "clipWallpaper() the bitmap isRecycled:" + bitmap.isRecycled());

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point outSize = new Point();
        wm.getDefaultDisplay().getSize(outSize);
        int screenWidth = outSize.x;
        int screenHeight = outSize.y;
        float screenRate = (float) screenHeight / (float) screenWidth;

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        float bitmapRate = (float) bitmapHeight / (float) bitmapWidth;

        if (Math.abs(screenRate - bitmapRate) < 0.01) {
            Log.d(TAG, "clipWallpaper() the bitmap need not clip.");
            return bitmap;
        }

        Bitmap newBitmap = null;
        if (screenRate > bitmapRate) {
            int standardWidth = (int) (bitmapHeight / screenRate);
            int x = (bitmapWidth - standardWidth) / 2;
            newBitmap = Bitmap.createBitmap(bitmap, x, 0, standardWidth, bitmapHeight);
        } else {
            int standardHeigh = (int) (bitmapWidth * screenRate);
            int y = (bitmapHeight - standardHeigh) / 2;
            newBitmap = Bitmap.createBitmap(bitmap, 0, y, bitmapWidth, standardHeigh);
        }

        if (newBitmap != null) {
            if (bitmap != newBitmap) {
                bitmap.recycle();
            }
        }
        return newBitmap;
    }

    private boolean saveBitmapToProvider(Bitmap bitmap, String url) {
        try {
            if (bitmap == null) {
                Log.e(TAG, "saveBitmapToProvider bitmap is null.");
                return false;
            }
            OutputStream outStream = mContentResolver.openOutputStream(Uri.parse(url), "w");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            //bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outStream);
            outStream.close();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "exception while writing bitmap: " + url, e);
            return false;
        }
    }

    public Map<String, Bitmap> getAppPackageIcon(Context context, String[] componentNames) {
        final Uri CONTENT_URI = Uri.parse("content://com.hct.lqsoft.launcher/hct_share_icon");
        final String COMPONENT_NAME = "component_name";
        final String ICON_BYTE = "icon_byte";
        Map<String, Bitmap> result = new HashMap<String, Bitmap>();
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(
                    CONTENT_URI,
                    new String[] {
                            COMPONENT_NAME, ICON_BYTE
                    }, null, componentNames, null);
            if (cursor == null) {
                Log.e(TAG, "getAppPackageIcon: cursor is null.");
                return result;
            }
            while (cursor.moveToNext()) {
                String classname = cursor.getString(0);
                byte[] bytes = cursor.getBlob(1);
                if (bytes != null && bytes.length > 0) {
                    Bitmap icon = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    result.put(classname, icon);
                }
                else {
                    Log.w(TAG, "getAppPackageIcon: " + classname + " not found.");
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "getAppPackageIcon:" + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }
    
    boolean saveBitmapToInputStream(Bitmap bp){
    	if(bp == null)
    		return false; 
    	
    	try {
    		InputStream inputStream = Bitmap2InputStream(bp, 100);
            	
        	if(inputStream != null){
        		callThemeManager(inputStream, "gallery_set_lock_wallpaper");	
        	}
		} catch (Exception e) {
			Log.w(TAG, "saveBitmapToInputStream ,error2, ",e);
			return false;
		}
    	
    	return true;    	
    }
    
    public InputStream Bitmap2InputStream(Bitmap bm, int quality) {  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        bm.compress(Bitmap.CompressFormat.PNG, quality, baos);  
        InputStream is = new ByteArrayInputStream(baos.toByteArray());  
        return is;  
    }  

	void callThemeManager(final InputStream in, final String id){
		if(mThemeManager == null){
			Log.w(TAG, "send connected message 2");
			mThemeManager = mThemeManager.getInstance(mContext, new ThemeManagerCallback(){
				@Override
				public void onServiceConnected() {
					// TODO Auto-generated method stub
					Log.w(TAG, "receive connected message 2");
					mThemeManager.applyLockScreenWallpaper(id, in, 1);
				}
			});
		} else {
			Log.w(TAG, "mThemeManager is not null");
			mThemeManager.applyLockScreenWallpaper(id, in, 1);
		}
	}

	@SuppressLint("NewApi")
	public void setLockWallPaper(Context context, final InputStream data, Rect visibleCropHint, boolean allowBackup,
			int whichWallpaper) {
		// TODO Auto-generated method stub
        Log.d(TAG, "setLockWallPaper begin");
		try {
			WallpaperManager mWallManager = WallpaperManager.getInstance(context);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Log.d(TAG, "setLockWallPaper end");
	}
}
