package com.gome.beautymirror.gallery;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gome.beautymirror.R;
import com.bumptech.glide.Glide;

class PictureAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Picture> pictures;
    private Context mContext;

    public PictureAdapter(String[] titles, String[] images, Context context) {
        super();
        mContext = context;
        pictures = new ArrayList<Picture>();
        inflater = LayoutInflater.from(context);
        for (int i = 0; i < images.length; i++) {
            Picture picture = new Picture(titles[i], images[i] , MediaFile.isVideoFileType(images[i]));
            pictures.add(picture);
        }
    }

    @Override
    public int getCount() {
        if (null != pictures) {
            return pictures.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return pictures.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gallery_item, null);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.vedioPlay=(ImageView) convertView.findViewById(R.id.vedio_play);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(pictures.get(position).isVedio()){
            Glide.with(mContext).load(Uri.fromFile(new File( pictures.get(
                    position).getImageId()))).into(viewHolder.image);
            viewHolder.vedioPlay.setVisibility(View.VISIBLE);
        } else{
            Glide.with(mContext)
                    .load(pictures.get(
                            position).getImageId())
                    .into(viewHolder.image);
            viewHolder.vedioPlay.setVisibility(View.GONE);
        }

        return convertView;
    }

}

class ViewHolder {
    public ImageView image;
    public ImageView vedioPlay;

}
