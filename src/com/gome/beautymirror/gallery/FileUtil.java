package com.gome.beautymirror.gallery;


import android.os.Environment;


import java.io.File;

public class FileUtil {
    public static String FileName=Environment.getExternalStorageDirectory()+"/DCIM/Camera/";

    public static String[] getImageNames(String folderPath) {
        File mfile = new File(folderPath);
        String[] files01 = mfile.list();
        if(files01==null){
            return null;
        }
        int imageFileNums = 0;
        for (int i = 0; i < files01.length; i++) {
            File file = new File(folderPath + "/" + files01[i]);
            if (!file.isDirectory()) {
                if (MediaFile.isImageFileType(file.getName())||  MediaFile.isVideoFileType(file.getName())) {
                    imageFileNums++;
                }
            }
        }

        String[] files02 = new String[imageFileNums];
        for (int i = 0; i < files01.length; i++) {
            File file = new File(folderPath + "/" + files01[i]);
            if (!file.isDirectory()) {
                if (MediaFile.isImageFileType(file.getName()) || MediaFile.isVideoFileType(file.getName()) ) {
                    files02[i] = file.getName();
                }
            }
        }
        return files02;
    }

}
