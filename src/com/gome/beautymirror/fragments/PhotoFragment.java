package gome.beautymirror.fragments;

/*
HistoryListFragment.java
Copyright (C) 2017  Belledonne Communications, Grenoble, France

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

import android.content.Intent;

import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.gome.beautymirror.advertisement.BannerLayout;
import com.gome.beautymirror.advertisement.PicassoImageLoader;

import com.gome.beautymirror.R;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import com.gome.beautymirror.gallery.FileUtil;
public class PhotoFragment extends Fragment implements OnClickListener {
    private LinearLayout mActionBar, mPhotoLayout, mNullPhotoLayout, mPhotoMainLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.photo, container, false);

        mActionBar = view.findViewById(R.id.action_bar);
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        mActionBar.setPadding(0, getResources().getDimensionPixelSize(resourceId), 0, 0);
        mPhotoLayout = view.findViewById(R.id.photo_layout);
        mNullPhotoLayout = view.findViewById(R.id.photo_null_layout);
        mPhotoMainLayout = view.findViewById(R.id.photo_main_layout);


        //加载广告页
        BannerLayout bannerLayout2 = (BannerLayout) view.findViewById(R.id.banner2);
        final List<String> urls2 = new ArrayList<>();
        urls2.add("http://img3.imgtn.bdimg.com/it/u=2674591031,2960331950&fm=23&gp=0.jpg");
        urls2.add("http://img5.imgtn.bdimg.com/it/u=3639664762,1380171059&fm=23&gp=0.jpg");
        urls2.add("http://img0.imgtn.bdimg.com/it/u=1095909580,3513610062&fm=23&gp=0.jpg");
        urls2.add("http://img4.imgtn.bdimg.com/it/u=1030604573,1579640549&fm=23&gp=0.jpg");
        bannerLayout2.setImageLoader(new PicassoImageLoader());
        bannerLayout2.setViewUrls(urls2);
        bannerLayout2.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //Toast.makeText(MainActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });

        mPhotoMainLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), com.gome.beautymirror.gallery.GalleryActivity.class));
            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        String[] titles = FileUtil.getImageNames(FileUtil.FileName);
        if(titles == null ){
            mPhotoMainLayout.setVisibility(View.GONE);
            mNullPhotoLayout.setVisibility(View.VISIBLE);
        }else{
            String[] imagePaths = new String[titles.length];
            for (int i = 0; i < titles.length; i++) {
                imagePaths[titles.length - 1 - i] = FileUtil.FileName + titles[i];
            }
            if (imagePaths.length == 0) {
                mPhotoMainLayout.setVisibility(View.GONE);
                mNullPhotoLayout.setVisibility(View.VISIBLE);
            } else {
                mPhotoMainLayout.setVisibility(View.VISIBLE);
                mNullPhotoLayout.setVisibility(View.GONE);
                mPhotoLayout.removeAllViews();
                addPhotoView(imagePaths);
            }
        }
    }


    private void addPhotoView(String[] imagePaths) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        for (int i = 0; i < (imagePaths.length > 3 ? 3 : imagePaths.length); i++) {
            View view = inflater.inflate(R.layout.gallery_round_item, null);
            ImageView mImageView = view.findViewById(R.id.MyImageView);
            mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            ImageView mImageView2=view.findViewById(R.id.vedio_play);
            if(com.gome.beautymirror.gallery.MediaFile.isVideoFileType(imagePaths[i])){
                mImageView2.setVisibility(View.VISIBLE);
            }else{
                mImageView2.setVisibility(View.GONE);
            }
            Glide.with(getActivity())
                    .load(imagePaths[i])
                    .into(mImageView);
            view.setLayoutParams(lp);
            mPhotoLayout.addView(view);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}