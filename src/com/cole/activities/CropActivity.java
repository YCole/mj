/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cole.activities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
/*import gome.app.GomeProgressDialog;

import com.android.gallery3d.R;
import com.android.gallery3d.common.ApiHelper;
import com.android.gallery3d.common.Utils;
import com.android.gallery3d.crop.ClipImageLayout;
import com.android.gallery3d.crop.CommonPopupWindow;
import com.android.gallery3d.crop.CropImageView;
import com.android.gallery3d.crop.CropLoader;
import com.android.gallery3d.crop.CropMath;
import com.android.gallery3d.crop.CropView;
import com.android.gallery3d.util.BitmapUtils;
import com.android.gallery3d.util.Log;
import com.android.gallery3d.util.StorageManagerHelper;
import com.android.gallery3d.util.LauncherProviderHelper;*/

import android.Manifest;
import android.content.pm.PackageManager;

import com.gome.beautymirror.R;

import cole.crop.BitmapUtils;
import cole.crop.ClipImageLayout;
import cole.crop.CommonPopupWindow;
import cole.crop.CropExtras;
import cole.crop.CropImageView;
import cole.crop.CropLoader;
import cole.crop.CropMath;
import cole.crop.CropView;
import cole.crop.GomeProgressDialog;
import cole.crop.LauncherProviderHelper;
import cole.crop.StorageManagerHelper;
import cole.crop.Utils;

/**
 * Activity for cropping an image.
 */
public class CropActivity extends Activity implements CropImageView.OnBitmapSaveCompleteListener {

    // Add by zhihongye10116550, add for two type wallpaper.
    private static final int FIX_SCREEN_WALLPAPER = 1;
    private static final int SLIDE_SCREEN_WALLPAPER = 2;
    private int mWallPaperType = FIX_SCREEN_WALLPAPER;

    private static final String LOGTAG = "CropActivity";
    public static final String CROP_ACTION = "com.android.camera.action.CROP";
    public static final String CROP_ACTION_HCT = "hct.com.android.camera.action.CROP";
    private CropExtras mCropExtras = null;
    private LoadBitmapTask mLoadBitmapTask = null;

    private int mOutputX = 0;
    private int mOutputY = 0;
    private Bitmap mOriginalBitmap = null;
    private RectF mOriginalBounds = null;
    private int mOriginalRotation = 0;
    private Uri mSourceUri = null;
    private Uri mOutUri = null;
    private CropView mCropView = null;
    private View mSaveButton = null;
    private boolean finalIOGuard = false;

    private static final int SELECT_PICTURE = 1000; // request code for picker

    private static final int DEFAULT_COMPRESS_QUALITY = 90;
    /**
     * The maximum bitmap size we allow to be returned through the intent.
     * Intents have a maximum of 1MB in total size. However, the Bitmap seems to
     * have some overhead to hit so that we go way below the limit here to make
     * sure the intent stays below 1MB.We should consider just returning a byte
     * array instead of a Bitmap instance to avoid overhead.
     */
    public static final int MAX_BMAP_IN_INTENT = 500000;// 750000;

    // Flags
    private static final int DO_SET_WALLPAPER = 1;
    private static final int DO_RETURN_DATA = 1 << 1;
    private static final int DO_EXTRA_OUTPUT = 1 << 2;
    private static final int DO_SET_LOCKSCREEN = 1 << 3;
    private static final int DO_SET_CALLSCREEN = 1 << 4;
    private static final int DO_RETURN_LARGE_DATA = 1 << 5;

    private static final int FLAG_CHECK = DO_SET_WALLPAPER | DO_RETURN_DATA | DO_EXTRA_OUTPUT | DO_SET_LOCKSCREEN
            | DO_SET_CALLSCREEN | DO_RETURN_LARGE_DATA;

    private static final String ACTION_LOCK_WALLPAPER_CHANGED = "com.lqsoft.lock_wallpaper_changed";
    private static final String ACTION_GALLERY_ICON_CHANGED = "com.lqsoft.gallery_icon_changed";

    private String temPathForMoji = "";
    private CommonPopupWindow window;
    private TextView setLockWallpaper, setWallpaper, setAllWallpaper;
    private View activityPopup;
    private TextView doneTv, infoTv;
    private boolean isLockWallpaper = false;
    private boolean isAllWallpaper = false;
    private boolean isGalleryWallpaper = false;
    private CropImageView mCropImageView;
    private Bitmap bitmap = null;
    private ImageView cropClose;
    private boolean isWallpaper = false;
    private boolean isGalleryWidget = false;
    private boolean isGomeAccount = false;
    private boolean isContacts = false;
    private ClipImageLayout mClipImageLayout;
    private String filePathString;
    GomeProgressDialog dialog;

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getActionBar().setDisplayShowHomeEnabled(false);
        // getActionBar().setDisplayHomeAsUpEnabled(true);
        // getActionBar().setDisplayShowTitleEnabled(true);
        changeHomeUpIndicatorIcon();
        Intent intent = getIntent();
        setResult(RESULT_CANCELED, new Intent());
        mCropExtras = getExtrasFromIntent(intent);
        if (mCropExtras != null && mCropExtras.getShowWhenLocked()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setNavigationBarColor(Color.BLACK);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        setContentView(R.layout.crop_activity);
        mCropView = (CropView) findViewById(R.id.cropView);
        mCropImageView = (CropImageView) findViewById(R.id.cropImageView);
        mClipImageLayout = (ClipImageLayout) this.findViewById(R.id.clipImageLayout);
        initPopupWindow();
//        getActionBar().hide();
        activityPopup = findViewById(R.id.mainView);
        doneTv = (TextView) this.findViewById(R.id.done);
        infoTv = (TextView) this.findViewById(R.id.crop_info);
        cropClose = (ImageView) this.findViewById(R.id.crop_close);
        if (intent.getData() != null) {
            mSourceUri = intent.getData();
            mOutUri = intent.getExtras().getParcelable(MediaStore.EXTRA_OUTPUT);
            if (mSourceUri.toString().contains("com.android.dialer.files")) {
                mClipImageLayout.setVisibility(View.GONE);
                setCropContactsView();
                mCropImageView.setOnBitmapSaveCompleteListener(this);
                infoTv.setText(R.string.setting_contact_photo);
                isContacts = true;
                isWallpaper = false;
            } else if (intent.getStringExtra("crop") != null && (intent.getStringExtra("crop").equals("crop"))) {
                mClipImageLayout.setVisibility(View.GONE);
                setCropContactsView();
                mCropImageView.setOnBitmapSaveCompleteListener(this);
                infoTv.setText(R.string.setting_contact_photo);
                isGomeAccount = true;
                isWallpaper = false;
            } else if (intent.getStringExtra("crop") != null && intent.getStringExtra("crop").toString().equals("true")) {
                mClipImageLayout.setVisibility(View.GONE);
                setCropContactsView();
                mCropImageView.setOnBitmapSaveCompleteListener(this);
                infoTv.setText(R.string.setting_contact_photo);
                isGomeAccount = true;
                isWallpaper = false;
            } else {
                mCropImageView.setVisibility(View.GONE);
                isWallpaper = true;
            }
            startLoadBitmap(mSourceUri);
        } else {
            pickImage();
        }
        // setIndicatorColor(0Xffd0235c);
        // setIndicatorColor(0xccd0235c);
        // setIndicatorColor(getResources().getColor(R.color.new_actionbar_bg_color));//fff4f4f4

        doneTv.setOnClickListener(new OnClickListener() {
            PopupWindow win = window.getPopupWindow();

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (isWallpaper) {
                    //win.setAnimationStyle(R.style.animTranslate);
                    window.showAtLocation(activityPopup, Gravity.BOTTOM, 0, 0);
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 0.3f;
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    getWindow().setAttributes(lp);
                } else {
                    startFinishOutput();
                }
            }
        });

        cropClose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }

    private Uri getDstUriForMoji(Uri srcUri) {
        Uri dstUri = srcUri;
        String srcPath = mSourceUri.getEncodedPath();

        temPathForMoji = (String) srcPath.subSequence(0, srcPath.length() - 4) + "-cropped.jpg";
        Log.i(LOGTAG, "temPathForMoji = " + temPathForMoji);
        dstUri = Uri.parse("file:///" + temPathForMoji);
        return dstUri;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        // inflater.inflate(R.menu.crop, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
           /* case R.id.action_done: {
                // startFinishOutput();
                return true;
            }*/
            case android.R.id.home:
                done();
                return true;

        }

        return false;
    }

    private void enableSave(boolean enable) {
        if (mSaveButton != null) {
            mSaveButton.setEnabled(enable);
        }
    }

    @Override
    protected void onDestroy() {
        if (mLoadBitmapTask != null) {
            mLoadBitmapTask.cancel(false);
        }
        if (mOriginalBitmap != null && !mOriginalBitmap.isRecycled()) {
            mOriginalBitmap.recycle();
            mOriginalBitmap = null;
        }
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mCropView.configChanged();
    }

    /**
     * Opens a selector in Gallery to chose an image for use when none was given
     * in the CROP intent.
     */
    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_image)), SELECT_PICTURE);
    }

    /**
     * Callback for pickImage().
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == SELECT_PICTURE) {
            mSourceUri = data.getData();
            startLoadBitmap(mSourceUri);
        }
    }

    /**
     * Gets screen size metric.
     */
    private int getScreenImageSize() {
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        return (int) Math.max(outMetrics.heightPixels, outMetrics.widthPixels);
    }

    /**
     * Method that loads a bitmap in an async task.
     */
    private void startLoadBitmap(Uri uri) {
        if (uri != null) {
            enableSave(false);
            // final View loading = findViewById(R.id.loading);
            // loading.setVisibility(View.VISIBLE);
            dialog = new GomeProgressDialog(this);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage(this.getResources().getString(R.string.loading_image));
            dialog.show();
            mLoadBitmapTask = new LoadBitmapTask();
            mLoadBitmapTask.execute(uri);
        } else {
            cannotLoadImage();
            done();
        }
    }

    private void switchWallpaperType(int type) {
        if (!mCropExtras.getSetAsWallpaper())
            return;
        if (mWallPaperType == type)
            return;
        reInitCropMatrix(type);
    }

    private void reInitCropMatrix(int type) {
        if (mCropExtras == null)
            return;

        int aspectX, aspectY;
        int outputX, outputY;
        float spotX, spotY;
        if (type == FIX_SCREEN_WALLPAPER) {
            int width = getWallpaperDesiredMinimumWidth();
            int height = getWallpaperDesiredMinimumHeight();
            Point size = getDefaultDisplaySize(new Point());
            spotX = (float) size.x / width;
            spotY = (float) size.y / height;
            aspectX = width;
            aspectY = height;
            outputX = width;
            outputY = height;
        } else if (type == SLIDE_SCREEN_WALLPAPER) {
            int height = getWallpaperDesiredMinimumHeight();
            Point size = getDefaultDisplaySize(new Point());

            int width = size.y * 2;
            if (size.x > size.y) {
                width = size.y * 2;
            }
            spotX = (float) size.x / width;
            spotY = (float) size.y / height;
            aspectX = width;
            aspectY = height;
            outputX = width;
            outputY = height;
        } else {
            return;
        }

        if (mCropExtras != null) {
            mOutputX = outputX;
            mOutputY = outputY;
            if (mOutputX > 0 && mOutputY > 0) {
                mCropView.applyAspect(mOutputX, mOutputY);

            }
            if (spotX > 0 && spotY > 0) {
                mCropView.setWallpaperSpotlight(spotX, spotY);
            }
            if (aspectX > 0 && aspectY > 0) {
                mCropView.applyAspect(aspectX, aspectY);
            }
        }

    }

    /**
     * Method called on UI thread with loaded bitmap.
     */
    private void doneLoadBitmap(Bitmap bitmap, RectF bounds, int orientation) {
        // final View loading = findViewById(R.id.loading);
        // loading.setVisibility(View.GONE);
        dialog.dismiss();
        mOriginalBitmap = bitmap;
        mOriginalBounds = bounds;
        mOriginalRotation = orientation;
        Log.w(LOGTAG, "bitmap:" + bitmap);
        if (bitmap != null && bitmap.getWidth() != 0 && bitmap.getHeight() != 0) {
            RectF imgBounds = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
            mCropView.initialize(bitmap, imgBounds, imgBounds, orientation);
            if (mCropExtras != null) {
                int aspectX = mCropExtras.getAspectX();
                int aspectY = mCropExtras.getAspectY();
                mOutputX = mCropExtras.getOutputX();
                mOutputY = mCropExtras.getOutputY();
                if (mOutputX > 0 && mOutputY > 0) {
                    mCropView.applyAspect(mOutputX, mOutputY);

                }
                float spotX = mCropExtras.getSpotlightX();
                float spotY = mCropExtras.getSpotlightY();
                if (spotX > 0 && spotY > 0) {
                    mCropView.setWallpaperSpotlight(spotX, spotY);
                }
                if (aspectX > 0 && aspectY > 0) {
                    mCropView.applyAspect(aspectX, aspectY);
                }
            }
            enableSave(true);
        } else {
            Log.w(LOGTAG, "could not load image for cropping");
            cannotLoadImage();
            setResult(RESULT_CANCELED, new Intent());
            done();
        }
    }

    /**
     * Display toast for image loading failure.
     */
    private void cannotLoadImage() {
        CharSequence text = getString(R.string.cannot_load_image);
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * AsyncTask for loading a bitmap into memory.
     *
     * @see #startLoadBitmap(Uri)
     */
    private class LoadBitmapTask extends AsyncTask<Uri, Void, Bitmap> {
        int mBitmapSize;
        Context mContext;
        Rect mOriginalBounds;
        int mOrientation;

        public LoadBitmapTask() {
            mBitmapSize = getScreenImageSize();
            mContext = getApplicationContext();
            mOriginalBounds = new Rect();
            mOrientation = 0;
        }

        @Override
        protected Bitmap doInBackground(Uri... params) {
            Uri uri = params[0];
            Bitmap bmap = CropLoader.getConstrainedBitmap(uri, mContext, mBitmapSize, mOriginalBounds);
            Log.w(LOGTAG, "bmap: " + bmap);
            mOrientation = CropLoader.getMetadataRotation(uri, mContext);
            return bmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            doneLoadBitmap(result, new RectF(mOriginalBounds), mOrientation);
        }
    }

    private void startFinishOutput() {
        if (finalIOGuard) {
            return;
        } else {
            finalIOGuard = true;
        }
        enableSave(false);
        Uri destinationUri = null;
        int flags = 0;
        if (mOriginalBitmap != null && mCropExtras != null) {
            if (mCropExtras.getExtraOutput() != null) {
                destinationUri = mCropExtras.getExtraOutput();
                if (destinationUri != null) {
                    flags |= DO_EXTRA_OUTPUT;
                }
            }
            if (mCropExtras.getSetAsWallpaper()) {
                flags |= DO_SET_WALLPAPER;
            }
            if (mCropExtras.getSetAsLockscreen()) {
                flags |= DO_SET_LOCKSCREEN;
            }
            if (mCropExtras.getSetAsCallscreen()) {
                flags |= DO_SET_CALLSCREEN;
            }
            if (mCropExtras.getReturnData()) {
                flags |= DO_RETURN_DATA;
            }
            if (mCropExtras.getReturnLargeData()) {
                flags |= DO_RETURN_LARGE_DATA;
            }
        }

        if (flags == 0) {
            destinationUri = CropLoader.makeAndInsertUri(this, mSourceUri);
            if (destinationUri != null) {
                flags |= DO_EXTRA_OUTPUT;
            }
        }
        if ((flags & FLAG_CHECK) != 0 && mOriginalBitmap != null) {
            RectF photo = new RectF(0, 0, mOriginalBitmap.getWidth(), mOriginalBitmap.getHeight());
            RectF crop = getBitmapCrop(photo);
            startBitmapIO(flags, mOriginalBitmap, mSourceUri, destinationUri, crop, photo, mOriginalBounds,
                    (mCropExtras == null) ? null : mCropExtras.getOutputFormat(), mOriginalRotation);
            return;
        }
        setResult(RESULT_CANCELED, new Intent());
        done();
        return;
    }

    private void startBitmapIO(int flags, Bitmap currentBitmap, Uri sourceUri, Uri destUri, RectF cropBounds,
                               RectF photoBounds, RectF currentBitmapBounds, String format, int rotation) {
        if (cropBounds == null || photoBounds == null || currentBitmap == null || currentBitmap.getWidth() == 0
                || currentBitmap.getHeight() == 0 || cropBounds.width() == 0 || cropBounds.height() == 0
                || photoBounds.width() == 0 || photoBounds.height() == 0) {
            return; // fail fast
        }
        if ((flags & FLAG_CHECK) == 0) {
            return; // no output options
        }


        if (isWallpaper) {
            flags = 1;
        }

        // add toast for set as contacts photos
        Uri outputUri = mCropExtras.getExtraOutput();
        if (outputUri != null && outputUri.toString().startsWith("content://com.android.contacts.files")) {
            Toast.makeText(this, R.string.setting_contact_photo, Toast.LENGTH_SHORT).show();
        }

        final View loading = findViewById(R.id.loading);
        //loading.setVisibility(View.VISIBLE);
        BitmapIOTask ioTask = new BitmapIOTask(sourceUri, destUri, format, flags, cropBounds, photoBounds,
                currentBitmapBounds, rotation, mOutputX, mOutputY);
        ioTask.execute(currentBitmap);
    }

    private void doneBitmapIO(boolean success, Intent intent) {
        final View loading = findViewById(R.id.loading);
        //loading.setVisibility(View.GONE);
        if (success) {
            if (isGalleryWallpaper || isAllWallpaper) {
                intent.putExtra("isFromGallery", true);
            }
            if (isContacts) {
                Toast.makeText(CropActivity.this, getString(R.string.operation_successed), Toast.LENGTH_SHORT).show();
            }
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED, intent);
            Toast.makeText(CropActivity.this, getString(R.string.operation_failed), Toast.LENGTH_SHORT).show();
        }
        done();
    }

    private class BitmapIOTask extends AsyncTask<Bitmap, Void, Boolean> {

        private final WallpaperManager mWPManager;
        InputStream mInStream = null;
        OutputStream mOutStream = null;
        String mOutputFormat = null;
        Uri mOutUri = null;
        Uri mInUri = null;
        int mFlags = 0;
        RectF mCrop = null;
        RectF mPhoto = null;
        RectF mOrig = null;
        Intent mResultIntent = null;
        int mRotation = 0;
        GomeProgressDialog dialog;

        // Helper to setup input stream
        private void regenerateInputStream() {
            if (mInUri == null) {
                Log.w(LOGTAG, "cannot read original file, no input URI given");
            } else {
                Utils.closeSilently(mInStream);
                try {
                    // add by yujun for drm
                    InputStream is = null;
                    is = getContentResolver().openInputStream(mInUri);
                    BitmapFactory.Options optionsTmp = new BitmapFactory.Options();
                    Bitmap bp = BitmapUtils.getBitmapFormUri(CropActivity.this, mInUri);
                    //BitmapFactory.decodeStream(is, null, optionsTmp);

                    Log.w(LOGTAG, "bmap: " + bp);
                    Log.w(LOGTAG, "uri: " + mInUri);

                    mInStream = getContentResolver().openInputStream(mInUri);
                } catch (FileNotFoundException e) {
                    Log.w(LOGTAG, "cannot read file: " + mInUri.toString(), e);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    Log.w(LOGTAG, "cannot read file: " + mInUri.toString(), e);
                }
            }
        }

        public BitmapIOTask(Uri sourceUri, Uri destUri, String outputFormat, int flags, RectF cropBounds,
                            RectF photoBounds, RectF originalBitmapBounds, int rotation, int outputX, int outputY) {
            mOutputFormat = outputFormat;
            mOutStream = null;
            mOutUri = destUri;
            mInUri = sourceUri;
            mFlags = flags;
            mCrop = cropBounds;
            mPhoto = photoBounds;
            mOrig = originalBitmapBounds;
            mWPManager = WallpaperManager.getInstance(getApplicationContext());
            mResultIntent = new Intent();
            mRotation = (rotation < 0) ? -rotation : rotation;
            mRotation %= 360;
            mRotation = 90 * (int) (mRotation / 90); // now mRotation is a
            // multiple of 90
            mOutputX = outputX;
            mOutputY = outputY;

            Log.i(LOGTAG, "BitmapIOTask mOutUri = " + mOutUri);

            // if(mInUri.toString().startsWith("file:///") &&
            // mInUri.toString().equals(mOutUri.toString())){
            if (mInUri.toString().startsWith("file:///") && mOutUri != null
                    && mInUri.toString().equals(mOutUri.toString())) {
                mOutUri = getDstUriForMoji(mOutUri);
                Log.i(LOGTAG, "BitmapIOTask mOutUri22 = " + mOutUri);
            }

            if ((flags & DO_EXTRA_OUTPUT) != 0) {
                if (mOutUri == null) {
                    Log.w(LOGTAG, "cannot write file, no output URI given");
                } else {
                    try {
                        mOutStream = getContentResolver().openOutputStream(mOutUri);
                    } catch (FileNotFoundException e) {
                        Log.w(LOGTAG, "cannot write file: " + mOutUri.toString(), e);
                    }
                }
            }

            if ((flags & (DO_EXTRA_OUTPUT | DO_SET_WALLPAPER | DO_SET_LOCKSCREEN | DO_SET_CALLSCREEN
                    | DO_RETURN_LARGE_DATA)) != 0) {
                regenerateInputStream();
            }
        }

        @Override
        protected Boolean doInBackground(Bitmap... params) {
            boolean failure = false;
            Bitmap img = params[0];
            Log.d(LOGTAG, "doInBackground enter");

            Log.w(LOGTAG, "mCrop:" + mCrop);
            Log.w(LOGTAG, "mPhoto:" + mPhoto);
            Log.w(LOGTAG, "mOrig:" + mOrig);

            // Set extra for crop bounds
            if (mCrop != null && mPhoto != null && mOrig != null) {
                RectF trueCrop = CropMath.getScaledCropBounds(mCrop, mPhoto, mOrig);
                Matrix m = new Matrix();
                m.setRotate(mRotation);
                m.mapRect(trueCrop);
                if (trueCrop != null) {
                    Rect rounded = new Rect();
                    trueCrop.roundOut(rounded);
                    mResultIntent.putExtra(CropExtras.KEY_CROPPED_RECT, rounded);
                }
            }

            // Find the small cropped bitmap that is returned in the intent
            if ((mFlags & DO_RETURN_DATA) != 0) {
                assert (img != null);
                Bitmap ret = null;
                if (mCropImageView != null) {
                    if (isWallpaper || isGalleryWidget) {
                        ret = getCroppedImage(img, mCrop, mPhoto);
                    } else {
                        ret = mCropImageView.getCropBitmap(200, 200, false);
                    }
                    if (ret != null) {
                        ret = getDownsampledBitmap(ret, MAX_BMAP_IN_INTENT);
                    }
                    if (ret == null) {
                        Log.w(LOGTAG, "could not downsample bitmap to return in data");
                        failure = true;
                    } else {
                        if (mRotation > 0) {
                            Matrix m = new Matrix();
                            m.setRotate(mRotation);
                            Bitmap tmp = Bitmap.createBitmap(ret, 0, 0, ret.getWidth(), ret.getHeight(), m, true);
                            if (tmp != null) {
                                ret = tmp;
                            }
                        }
                        mResultIntent.putExtra(CropExtras.KEY_DATA, ret);
                    }
                }
            }

            // Do the large cropped bitmap and/or set the wallpaper
            if ((mFlags & (DO_EXTRA_OUTPUT | DO_SET_WALLPAPER | DO_SET_LOCKSCREEN | DO_SET_CALLSCREEN
                    | DO_RETURN_LARGE_DATA)) != 0) {
                Log.d(LOGTAG, "doInBackground 2");
                // Find crop bounds (scaled to original image size)
                RectF trueCrop = CropMath.getScaledCropBounds(mCrop, mPhoto, mOrig);
                if (trueCrop == null) {
                    Log.w(LOGTAG, "cannot find crop for full size image");
                    failure = true;
                    return false;
                }
                Rect roundedTrueCrop = new Rect();
                trueCrop.roundOut(roundedTrueCrop);

                if (roundedTrueCrop.width() <= 0 || roundedTrueCrop.height() <= 0) {
                    Log.w(LOGTAG, "crop has bad values for full size image");
                    failure = true;
                    return false;
                }

                // Attempt to open a region decoder
                BitmapRegionDecoder decoder = null;
                try {
                    if (mInUri.toString().startsWith("file:///")) {
                        decoder = BitmapRegionDecoder.newInstance(mInUri.getPath(), true);
                        File file = new File(temPathForMoji);
                        if (file.exists()) {
                            Log.w(LOGTAG, "delete moji temp file");
                            try {
                                //file.delete();
                            } catch (Exception e) {
                                Log.w(LOGTAG, "delete moji temp file failed");
                            }
                        }

                    } else {
                        decoder = BitmapRegionDecoder.newInstance(mInStream, true);
                    }

                } catch (IOException e) {
                    Log.w(LOGTAG, "cannot open region decoder for file: " + mInUri.toString(), e);
                }

                Bitmap crop = null;
                if (decoder != null) {
                    // Do region decoding to get crop bitmap
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inMutable = true;
                    if (isContacts || isGomeAccount) {
                        crop = mCropImageView.getCropBitmap(200, 200, false);
                    } else {
                        crop = decoder.decodeRegion(roundedTrueCrop, options);
                    }
                    decoder.recycle();
                }

                if (crop == null) {
                    // BitmapRegionDecoder has failed, try to crop in-memory
                    regenerateInputStream();
                    Bitmap fullSize = null;
                    if (mInStream != null) {
                        fullSize = BitmapFactory.decodeStream(mInStream);
                    }
                    if (fullSize != null) {
                        crop = Bitmap.createBitmap(fullSize, roundedTrueCrop.left, roundedTrueCrop.top,
                                roundedTrueCrop.width(), roundedTrueCrop.height());
                    }
                }

                if (crop == null) {
                    Log.w(LOGTAG, "cannot decode file: " + mInUri.toString());
                    failure = true;
                    return false;
                }
                if (mOutputX > 0 && mOutputY > 0) {
                    Matrix m = new Matrix();
                    RectF cropRect = new RectF(0, 0, crop.getWidth(), crop.getHeight());
                    if (mRotation > 0) {
                        m.setRotate(mRotation);
                        m.mapRect(cropRect);
                    }
                    RectF returnRect = new RectF(0, 0, mOutputX, mOutputY);
                    m.setRectToRect(cropRect, returnRect, Matrix.ScaleToFit.FILL);
                    m.preRotate(mRotation);
                    Bitmap tmp = Bitmap.createBitmap((int) returnRect.width(), (int) returnRect.height(),
                            Bitmap.Config.ARGB_8888);
                    if (tmp != null) {
                        Canvas c = new Canvas(tmp);
                        c.drawBitmap(crop, m, new Paint());
                        crop = tmp;
                    }
                } else if (mRotation > 0) {
                    Matrix m = new Matrix();
                    m.setRotate(mRotation);
                    Bitmap tmp = Bitmap.createBitmap(crop, 0, 0, crop.getWidth(), crop.getHeight(), m, true);
                    if (tmp != null) {
                        crop = tmp;
                    }
                }
                // Get output compression format
                CompressFormat cf = convertExtensionToCompressFormat(getFileExtension(mOutputFormat));

                // If we only need to output to a URI, compress straight to file
                if (mFlags == DO_EXTRA_OUTPUT) {
                    try {
                        Log.w(LOGTAG, "OutStream=" + mOutStream);
                        if (mOutStream == null || !crop.compress(cf, DEFAULT_COMPRESS_QUALITY, mOutStream)) {
                            Log.w(LOGTAG, "#1,failed to compress bitmap to file: " + mOutUri.toString());
                            failure = true;
                        } else {
                            mResultIntent.setData(mOutUri);
                        }
                    } catch (Exception e) {
                        Log.w(LOGTAG, "#2, failed to compress bitmap to file: " + mOutUri.toString(), e);
                        failure = true;
                    }
                } else {
                    // Compress to byte array
                    ByteArrayOutputStream tmpOut = new ByteArrayOutputStream(2048);
                    // modify for high quality wallpaper, png compressformat
                    // 20150722
                    if (crop.compress(CompressFormat.PNG/* cf */, 100/* DEFAULT_COMPRESS_QUALITY */, tmpOut)) {
                        // If we need to output to a Uri, write compressed
                        // bitmap out
                        if ((mFlags & DO_EXTRA_OUTPUT) != 0) {
                            if (mOutStream == null) {
                                Log.w(LOGTAG, "failed to compress bitmap to file: " + mOutUri.toString());
                                failure = true;
                            } else {
                                try {
                                    mOutStream.write(tmpOut.toByteArray());
                                    mResultIntent.setData(mOutUri);
                                    tmpOut.close();
                                } catch (IOException e) {
                                    Log.w(LOGTAG, "failed to compress bitmap to file: " + mOutUri.toString(), e);
                                    failure = true;
                                }
                            }
                        }

                    } else {
                        Log.w(LOGTAG, "cannot compress bitmap");
                        failure = true;
                    }

                    if ((mFlags & DO_RETURN_LARGE_DATA) != 0) {
                        Log.d(LOGTAG, "doInBackground DO_RETURN_LARGE_DATA");
                        StorageManager manager = (StorageManager) getApplicationContext()
                                .getSystemService(Context.STORAGE_SERVICE);
                        String cachePath = StorageManagerHelper.getInternalStoragePath(manager)
                                + "/android/data/com.android.gallery3d/";
                        String filenameTemp1 = "cropcache.tmp";
                        String filePath = cachePath + filenameTemp1;
                        File cacheFile = new File(cachePath);
                        if (!cacheFile.isDirectory()) {
                            cacheFile.mkdirs();
                        }
                        File dir = new File(filePath);
                        String filenameTemp = StorageManagerHelper.getInternalStoragePath(manager)
                                + "/android/data/com.android.gallery3d/cropcache.tmp";// add
                        if (!dir.exists()) {
                            try {
                                dir.createNewFile();
                            } catch (Exception e) {
                                Log.w(LOGTAG, "DO_RETURN_LARGE_DATA , createNewFile error, ", e);
                            }
                        }

                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(dir);
                        } catch (FileNotFoundException e1) {
                            // TODO Auto-generated catch block
                            Log.w(LOGTAG, "DO_RETURN_LARGE_DATA , FileOutputStream error, ", e1);
                        }

                        try {
                            if (fos != null) {
                                Log.d(LOGTAG, "doInBackground DO_RETURN_LARGE_DATA, fos not null");
                                crop.compress(cf, DEFAULT_COMPRESS_QUALITY, fos);
                                Uri data = Uri.fromFile(new File(filenameTemp));
                                Log.d(LOGTAG, "doInBackground DO_RETURN_LARGE_DATA, fos not null, data uri=" + data);
                                mResultIntent.setData(data);
                                fos.close();
                            }
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            Log.w(LOGTAG, "DO_RETURN_LARGE_DATA ,error1, ", e);
                        } catch (Exception e) {
                            Log.w(LOGTAG, "DO_RETURN_LARGE_DATA ,error2, ", e);
                        }
                    }

                    // If we need to set to the wallpaper, set it
                    if ((mFlags & DO_SET_WALLPAPER) != 0 && mWPManager != null) {
                        if (mWPManager == null) {
                            Log.w(LOGTAG, "no wallpaper manager");
                            failure = true;
                        } else {

                        }
                    }
                }
                if ((mFlags & DO_SET_LOCKSCREEN) != 0 && mWPManager != null) {
                    if (mWPManager == null) {
                        Log.w(LOGTAG, "no wallpaper manager");
                        failure = true;
                    } else {


                    }

                }
                if ((mFlags & DO_SET_CALLSCREEN) != 0 && mWPManager != null) {
                    if (mWPManager == null) {
                        Log.w(LOGTAG, "no wallpaper manager");
                        failure = true;
                    } else {
                        if (!setCallScreenWallPaper(CropActivity.this, mWPManager, crop)) {
                            failure = true;
                        }
                    }

                }
            }
            return !failure; // True if any of the operations failed
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Utils.closeSilently(mOutStream);
            Utils.closeSilently(mInStream);
            doneBitmapIO(result.booleanValue(), mResultIntent);
        }

        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
            super.onCancelled();
            dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new GomeProgressDialog(CropActivity.this);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage(CropActivity.this.getString(R.string.setting_head_as_gallery));
            dialog.show();
        }

    }

    private void done() {
        finish();
    }

    protected static Bitmap getCroppedImage(Bitmap image, RectF cropBounds, RectF photoBounds) {
        RectF imageBounds = new RectF(0, 0, image.getWidth(), image.getHeight());
        RectF crop = CropMath.getScaledCropBounds(cropBounds, photoBounds, imageBounds);
        if (crop == null) {
            return null;
        }
        Rect intCrop = new Rect();
        crop.roundOut(intCrop);
        return Bitmap.createBitmap(image, intCrop.left, intCrop.top, intCrop.width(), intCrop.height());
    }

    protected static Bitmap getDownsampledBitmap(Bitmap image, int max_size) {
        if (image == null || image.getWidth() == 0 || image.getHeight() == 0 || max_size < 16) {
            throw new IllegalArgumentException("Bad argument to getDownsampledBitmap()");
        }
        int shifts = 0;
        int size = CropMath.getBitmapSize(image);
        while (size > max_size) {
            shifts++;
            size /= 4;
        }
        Bitmap ret = Bitmap.createScaledBitmap(image, image.getWidth() >> shifts, image.getHeight() >> shifts, true);
        if (ret == null) {
            return null;
        }
        // Handle edge case for rounding.
        if (CropMath.getBitmapSize(ret) > max_size) {
            return Bitmap.createScaledBitmap(ret, ret.getWidth() >> 1, ret.getHeight() >> 1, true);
        }
        return ret;
    }

    /**
     * Gets the crop extras from the intent, or null if none exist.
     */
    protected static CropExtras getExtrasFromIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            return new CropExtras(extras.getInt(CropExtras.KEY_OUTPUT_X, 0), extras.getInt(CropExtras.KEY_OUTPUT_Y, 0),
                    extras.getBoolean(CropExtras.KEY_SCALE, true)
                            && extras.getBoolean(CropExtras.KEY_SCALE_UP_IF_NEEDED, false),
                    extras.getInt(CropExtras.KEY_ASPECT_X, 0), extras.getInt(CropExtras.KEY_ASPECT_Y, 0),
                    extras.getBoolean(CropExtras.KEY_SET_AS_WALLPAPER, false),
                    extras.getBoolean(CropExtras.KEY_RETURN_DATA, false),
                    (Uri) extras.getParcelable(MediaStore.EXTRA_OUTPUT), extras.getString(CropExtras.KEY_OUTPUT_FORMAT),
                    extras.getBoolean(CropExtras.KEY_SHOW_WHEN_LOCKED, false),
                    extras.getFloat(CropExtras.KEY_SPOTLIGHT_X), extras.getFloat(CropExtras.KEY_SPOTLIGHT_Y),
                    extras.getBoolean(CropExtras.KEY_SET_AS_LOCKSCREEN_WALLPAPER, false),
                    extras.getBoolean(CropExtras.KEY_SET_AS_CALLSCREEN_WALLPAPER, false),
                    extras.getBoolean(CropExtras.KEY_RETURN_LARGE_DATA, false));
        }
        return null;
    }

    protected static CompressFormat convertExtensionToCompressFormat(String extension) {
        return extension.equals("png") ? CompressFormat.PNG : CompressFormat.JPEG;
    }

    protected static String getFileExtension(String requestFormat) {
        String outputFormat = (requestFormat == null) ? "jpg" : requestFormat;
        outputFormat = outputFormat.toLowerCase();
        return (outputFormat.equals("png") || outputFormat.equals("gif")) ? "png" // We
                // don't
                // support
                // gif
                // compression.
                : "jpg";
    }

    private RectF getBitmapCrop(RectF imageBounds) {
        RectF crop = mCropView.getCrop();
        RectF photo = mCropView.getPhoto();
        if (crop == null || photo == null) {
            Log.w(LOGTAG, "could not get crop");
            return null;
        }
        RectF scaledCrop = CropMath.getScaledCropBounds(crop, photo, imageBounds);
        return scaledCrop;
    }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private Point getDefaultDisplaySize(Point size) {
        Display d = getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT >=  19) {
            d.getSize(size);
        } else {
            size.set(d.getWidth(), d.getHeight());
        }
        return size;
    }

    public final static boolean setCallScreenWallPaper(Context context, WallpaperManager wm, Bitmap bitmap) {
        boolean setted = false;

        Method methodSetcallScreenWallpaper = null;
        try {
            methodSetcallScreenWallpaper = WallpaperManager.class.getMethod("setAppBitmap", Bitmap.class);
            Settings.System.putInt(context.getContentResolver(), "call_wallpaper_settings", 2);
            setted = true;
        } catch (NoSuchMethodException e) {
        } catch (NoSuchMethodError e) {
        }
        if (methodSetcallScreenWallpaper != null) {
            try {
                methodSetcallScreenWallpaper.invoke(wm, bitmap);
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return setted;
    }

    public final static boolean setLockScreenWallPaper(WallpaperManager wm, Bitmap bitmap) {
        boolean setted = false;

        Method methodSetLockScreenWallpaper = null;
        try {
            methodSetLockScreenWallpaper = WallpaperManager.class.getMethod("setLockscreenBitmap", Bitmap.class);
            setted = true;
        } catch (NoSuchMethodException e) {
        } catch (NoSuchMethodError e) {
        }
        if (methodSetLockScreenWallpaper != null) {
            try {
                methodSetLockScreenWallpaper.invoke(wm, bitmap);
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return setted;
    }

    public void changeHomeUpIndicatorIcon() {
    }

    private Bitmap createConfigCenterItemAreaBitmap(Bitmap bitmap, int width, int height) {
        if (null == bitmap || width <= 0 || height <= 0) {
            return null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if (widthOrg > width && heightOrg > height) {
            int longerEdge = (int) (width * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : width;
            int scaledHeight = widthOrg > heightOrg ? height : longerEdge;
            Bitmap scaledBitmap;

            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } catch (Exception e) {
                return bitmap;
            }

            int xTopLeft = (scaledWidth - width) / 2;
            int yTopLeft = (scaledHeight - height) / 2;

            try {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, width, height);
                scaledBitmap.recycle();
            } catch (Exception e) {
                return scaledBitmap;
            }
        } else if (widthOrg > width && heightOrg < height) {

            int xTopLeft = (widthOrg - width) / 2;

            try {
                result = Bitmap.createBitmap(bitmap, xTopLeft, 0, width, heightOrg);
                bitmap.recycle();
            } catch (Exception e) {
                return bitmap;
            }
        } else if (widthOrg < width && heightOrg > height) {

            int yTopLeft = (heightOrg - height) / 2;

            try {
                result = Bitmap.createBitmap(bitmap, 0, yTopLeft, widthOrg, height);
                bitmap.recycle();
            } catch (Exception e) {
                return bitmap;
            }
        }
        return result;
    }

    @Override
    public void onBitmapSaveSuccess(File file) {
        Log.d(LOGTAG, "onBitmapSaveSuccess");
        if (file != null) {
            Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
            Uri data = Uri.fromFile(file);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            Bitmap crop;
            Rect roundedTrueCrop = new Rect();
            BitmapRegionDecoder decoder;
            try {
                decoder = BitmapRegionDecoder.newInstance(data.getPath(), true);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inMutable = true;
                crop = decoder.decodeRegion(roundedTrueCrop, options);
                decoder.recycle();
                intent.setDataAndType(data, "image/*");
                intent.putExtra(CropExtras.KEY_DATA, bitmap);
                intent.putExtra(CropExtras.KEY_CROPPED_RECT, roundedTrueCrop);
                intent.putExtra("mimeType", "image/*");
                setResult(RESULT_OK, intent);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
        }
    }

    @Override
    public void onBitmapSaveError(File file) {
        Log.d(LOGTAG, "onBitmapSaveError");
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
    }

    private void initPopupWindow() {
        // get the height of screen
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        // create popup window
        window = new CommonPopupWindow(this, R.layout.crop_pop_line, 1000,
                ViewGroup.LayoutParams.WRAP_CONTENT) {
            @Override
            protected void initView() {
                View view = getContentView();
                setLockWallpaper = (TextView) view.findViewById(R.id.set_as_lockwallpaper);
                setWallpaper = (TextView) view.findViewById(R.id.set_as_wallpaper);
                setAllWallpaper = (TextView) view.findViewById(R.id.set_as_lockandwallpaper);
            }

            @Override
            protected void initEvent() {
                setWallpaper.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        Log.d("xiemin", "setWallpaper");
                        isGalleryWallpaper = true;
                        isAllWallpaper = false;
                        isLockWallpaper = false;
                        startFinishOutput();
                        window.getPopupWindow().dismiss();
                    }
                });

                setLockWallpaper.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        Log.d("xiemin", "setLockWallpaper");
                        isLockWallpaper = true;
                        isGalleryWallpaper = false;
                        isAllWallpaper = false;
                        startFinishOutput();

                        window.getPopupWindow().dismiss();
                    }
                });

                setAllWallpaper.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        isAllWallpaper = true;
                        isLockWallpaper = false;
                        isGalleryWallpaper = false;
                        startFinishOutput();
                        window.getPopupWindow().dismiss();
                    }
                });

            }

            @Override
            protected void initWindow() {
                super.initWindow();
                PopupWindow instance = getPopupWindow();
                instance.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 1.0f;
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        getWindow().setAttributes(lp);
                    }
                });
            }
        };
    }

    public String getDataColumn(Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private int readBitmapDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    private Bitmap createBitmap(String path) {
        if (path == null) {
            return null;
        }

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;

        opts.inSampleSize = width > 1080 ? (int) (width / 1080) : 1;

        opts.inJustDecodeBounds = false;
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        opts.inDither = false;
        opts.inPurgeable = true;
        FileInputStream is = null;
        Bitmap bitmap = null;
        try {
            is = new FileInputStream(path);
            bitmap = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bitmap;
    }

    private Bitmap rotateBitmap(int angle, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        return resizedBitmap;
    }



    public void setCropContactsView() {
        mCropImageView.setVisibility(View.VISIBLE);
        try {
            bitmap = BitmapUtils.getBitmapFormUri(this, mSourceUri);
            Log.d(LOGTAG, "mSourceUri" + mSourceUri + "bitmap---" + bitmap);
            if (bitmap != null) {
                mCropImageView.setImageBitmap(bitmap);
            } else {
                finish();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


}
