package com.gome.beautymirror.gallery;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import android.widget.AdapterView.OnItemClickListener;
import android.os.StrictMode;
import com.gome.beautymirror.R;

import java.io.File;

public class GalleryActivity extends Activity {
    private GridView gridView;
    String[] imagePaths;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_activity);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();


        String[] titles = FileUtil.getImageNames(FileUtil.FileName);
         imagePaths = new String[titles.length];
        for (int i = 0; i < titles.length; i++) {
            imagePaths[titles.length-1-i] = FileUtil.FileName + titles[i];
        }
        gridView = (GridView) findViewById(R.id.gridview);
        PictureAdapter adapter = new PictureAdapter(titles, imagePaths, this);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                File file = new File(imagePaths[position]);
                if(MediaFile.isVideoFileType(imagePaths[position])){
                    intent.setDataAndType(Uri.fromFile(file), "video/*");
                }else{
                    intent.setDataAndType(Uri.fromFile(file), "image/*");
                }
                startActivity(intent);
            }
        });
    }
}
