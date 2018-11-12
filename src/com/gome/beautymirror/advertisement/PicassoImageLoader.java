package com.gome.beautymirror.advertisement;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gome.beautymirror.R;


public class PicassoImageLoader implements BannerLayout.ImageLoader {
    @Override
    public void displayImage(Context context, String path, ImageView imageView) {
        Glide.with(context)
                .load(path)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageView);
    }
}
