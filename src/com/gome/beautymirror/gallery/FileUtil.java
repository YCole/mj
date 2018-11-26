package com.gome.beautymirror.gallery;


import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

public class FileUtil {
    public static String FileName=Environment.getExternalStorageDirectory()+"/DCIM/Camera/";

    public static String[] getImageNames(String folderPath) {
        File mfile = new File(folderPath);
        String[] files01 = mfile.list();
        if(files01==null){
            return null;
        }

        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < files01.length; i++) {
            File file = new File(folderPath + "/" + files01[i]);
            if (!file.isDirectory()) {
                if (MediaFile.isImageFileType(file.getName())||  MediaFile.isVideoFileType(file.getName())) {
                    list.add(file.getName());
                }
            }
        }
        String[] files02 = (String[])list.toArray(new String[list.size()]);
        return files02;
    }

}
