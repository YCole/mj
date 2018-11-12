package com.gome.beautymirror.data;

import android.graphics.Bitmap;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class DataThread extends Thread {
    private static final String TAG = "DataThread";

    private static final String BOUNDARY = UUID.randomUUID().toString();
    private static final String PREFIX = "--";
    private static final String LINE_END = "\r\n";
    private static final String CONTENT_TYPE = "multipart/form-data";
    private static final int TIMEOUT = 2000;
    private static final int BYTE_SIZE = 1024;

    public static final String RESULT_CODE = "resCode";
    public static final String RESULT_OK = "000";
    public static final String RESULT_ERROR = "999";
    public static final String RESULT_MSG = "resMsg";

    private volatile boolean mDone = false;

    private String mUrl;
    private Map<String, String> mParams;
    private String mAccount;
    private Bitmap mBitmap;
    private Message mMessage;

    public DataThread(String url, Map<String, String> params, Message msg) {
        super(TAG);
        mUrl = url;
        mParams = params;
        mMessage = msg;
    }

    public DataThread(String url, Map<String, String> params, String account, Bitmap bitmap, Message msg) {
        super(TAG);
        mUrl = url;
        mParams = params;
        mAccount = account;
        mBitmap = bitmap;
        mMessage = msg;
    }

    public void stopProcessing() {
        mDone = true;
    }

    @Override
    public void run() {
        Log.d(TAG, "run: mUrl is " + mUrl);
        JSONObject obj = null;
        try {
            URL url = new URL(mUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setReadTimeout(TIMEOUT);
            connection.setConnectTimeout(TIMEOUT);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            connection.connect();

            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
            StringBuffer sb = null;
            String params = "";

            if (mParams != null && mParams.size() > 0) {
                Iterator<String> it = mParams.keySet().iterator();
                while (it.hasNext()) {
                    sb = null;
                    sb = new StringBuffer();
                    String key = it.next();
                    String value = mParams.get(key);
                    sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(LINE_END).append(LINE_END);
                    sb.append(value).append(LINE_END);
                    params = sb.toString();
                    Log.i(TAG, "run: " + key + " = " + params + "##");
                    dos.write(params.getBytes());
                }
            } else {
                Log.d(TAG, "run: mParams is null");
            }

            if (mBitmap != null) {
                sb = null;
                params = null;
                sb = new StringBuffer();
                sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                sb.append("Content-Disposition: form-data; name=\"" + "file" + "\"; filename=\"" + mAccount + "\"" + LINE_END);
                sb.append("Content-Type: image/png" + LINE_END);
                sb.append(LINE_END);
                params = sb.toString();
                sb = null;
                Log.i(TAG, "run: "+ mAccount + " = " + params + "##");
                dos.write(params.getBytes());

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                InputStream is = new ByteArrayInputStream(baos.toByteArray());
                byte[] bytes = new byte[BYTE_SIZE];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                baos.close();
                dos.write(LINE_END.getBytes());
            } else {
                Log.d(TAG, "run: mBitmap is null");
            }

            dos.write((PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes());
            dos.flush();
            dos.close();
            int code = connection.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                InputStream is = connection.getInputStream();
                byte bytes[] = new byte[BYTE_SIZE];
                int len = 0;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((len = is.read(bytes)) != -1) {
                    baos.write(bytes, 0, len);
                }
                is.close();
                baos.close();
                obj = getJSONObject(baos.toString());
            } else {
                Log.d(TAG, "run: code is " + code);
                obj = setJSONObject(RESULT_ERROR, code + "");
            }
            connection.disconnect();
        } catch (Exception e) {
            Log.e(TAG, "run: Exception is ", e);
            obj = setJSONObject(RESULT_ERROR, e.toString());
        }
        sendMsg(obj);
    }

    private void sendMsg(JSONObject obj) {
        if (mMessage != null && !mDone) {
            Log.d(TAG, "sendMsg: obj is " + obj);
            mMessage.obj = obj;
            mMessage.sendToTarget();
        } else {
            Log.d(TAG, "sendMsg: mMessage is " + mMessage + ", mDone is" + mDone);
        }
        mMessage = null;
    }

    private JSONObject getJSONObject(String msg) {
        JSONObject obj = null;
        try {
            if (!TextUtils.isEmpty(msg)) {
                obj = new JSONObject(msg);
            }
        } catch (JSONException e) {
            Log.e(TAG, "getJSONObject: JSONException is ", e);
        }
        Log.d(TAG, "getJSONObject: obj is " + obj);
        return obj;
    }

    public static JSONObject setJSONObject(String result, String msg) {
        JSONObject obj = null;
        try {
            obj = new JSONObject();
            obj.put(RESULT_CODE, result);
            obj.put(RESULT_MSG, msg);
        } catch (JSONException e) {
            Log.e(TAG, "setJSONObject: JSONException is ", e);
        }
        Log.d(TAG, "setJSONObject: obj is " + obj);
        return obj;
    }
}
