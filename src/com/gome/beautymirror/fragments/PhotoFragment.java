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

import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.gome.beautymirror.advertisement.BannerLayout;
import com.gome.beautymirror.advertisement.PicassoImageLoader;

import com.gome.beautymirror.R;

import java.util.ArrayList;
import java.util.List;


public class PhotoFragment extends Fragment implements OnClickListener{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.photo, container, false);

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
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
    }

}