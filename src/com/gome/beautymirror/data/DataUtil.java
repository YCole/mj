package com.gome.beautymirror.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

public class DataUtil {
    public static final String TAG = DataUtil.class.getSimpleName();

    public static final boolean IS_APP = true;

    private static final String URL = "http://222.190.139.10:7070/magicRest/";
    public static final String ACCOUNT_URL = URL + "accountRest/";
    public static final String DEVICE_URL = URL + "deviceRest/";
    public static final String FRIEND_URL = URL + "friendRelationRest/";

    public static final String SIP_DOMAIN = "222.190.139.10";

    public static final String BROADCAST_ACCOUNT = "BROADCAST_ACCOUNT";
    public static final String BROADCAST_FRIEND = "BROADCAST_FRIEND";
    public static final String BROADCAST_PEOPLE = "BROADCAST_PEOPLE";
    public static final String BROADCAST_PROPOSER = "BROADCAST_PROPOSER";
    public static final String BROADCAST_NOTIFICATION = "BROADCAST_NOTIFICATION";

    public static String getMD5(String str) {
        try {
            if (TextUtils.isEmpty(str)) {
                str = "test";
            } else {
                Log.d(TAG, "getMD5: str is " + str);
            }
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            str = new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            throw new RuntimeException("getMD5: " + e.getMessage(), e);
        }
        return str;
    }

    public static byte[] getImage(Bitmap bitmap) {
        if(bitmap == null) {
            return null;
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        return os.toByteArray();
    }

    public static Bitmap getImage(byte[] b) {
        if(b == null || b.length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(b, 0, b.length, null);
    }

    public static Bitmap getImage(String data) {
        if (data == null) {
            return null;
        }
        Bitmap bitmap = null;
        try {
            byte[] bytes = Base64.decode(data, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
