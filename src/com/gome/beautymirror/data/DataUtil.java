package com.gome.beautymirror.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

import cole.common.P;

public class DataUtil {
    public static final String TAG = DataUtil.class.getSimpleName();

    static final String GET_CODE_SET_PASSWORD_TO_REGISTER = P.PROPOTOL + "://" + P.IP   + P.PORT +P.CKECK_SMS_Register;
    static final String GET_CODE = P.PROPOTOL + "://" + P.IP   + P.PORT +P.GET_SMS;

    public static final boolean IS_APP = true;

    private static final String URL = "http://222.190.139.10:7070/magicRest/";
    public static final String ACCOUNT_URL = URL + "accountRest/";
    public static final String DEVICE_URL = URL + "deviceRest/";
    public static final String FRIEND_URL = URL + "friendRelationRest/";

    public static final String SIP_DOMAIN = "222.190.139.10";

    public static final String BROADCAST_INFO = "BROADCAST_INFO";

    public static String getDevice(Context context) {
        String device = "test";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();
            Log.d(TAG, "getDevice: imei = " + imei);
            if (!TextUtils.isEmpty(imei)) {
                device = imei;
            }
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(device.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return device;
        }
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
