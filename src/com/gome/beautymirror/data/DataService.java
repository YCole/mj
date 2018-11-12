package com.gome.beautymirror.data;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gome.beautymirror.data.CallLog;
import com.gome.beautymirror.data.DataUtil;
import com.gome.beautymirror.data.DataThread;
import com.gome.beautymirror.data.Notification;
import com.gome.beautymirror.data.provider.DatabaseProvider;
import com.gome.beautymirror.data.provider.DatabaseUtil;
import com.gome.beautymirror.LinphoneManager;
import com.gome.beautymirror.LinphonePreferences;
import com.gome.beautymirror.LinphonePreferences.AccountBuilder;

import cole.utils.SaveUtils;

import org.linphone.core.TransportType;
import org.linphone.core.CoreException;

public class DataService extends Service implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = "DataService";

    private static final String EMPTY = "null";

    private static final int FRIEND_REQUEST = 0;
    private static final int FRIEND_NORMAL = 1;

    private static final int DEVICE_BIND = 0;
    private static final int DEVICE_UNBIND = 1;

    private static final int REQUEST_OK = 1;
    private static final int REQUEST_CANCEL = 0;

    private static final int LOOP_SYNC_TIME = 60 * 1000;
    private static final int MESSAGE_SYNC = 1;
    private static final int LOOP_SIP_TIME = 10 * 1000;
    private static final int MESSAGE_SIP = 2;

    private static DataService instance;

    private DataServiceBinder mBinder = new DataServiceBinder();
    private ContentResolver mContentResolver = null;
    private DataServiceHandler mHandler = new DataServiceHandler();

    public static boolean isReady() {
        return instance != null;
    }

    public static DataService instance() {
        if (isReady()) return instance;

        throw new RuntimeException("DataService not instantiated yet");
    }

    public class DataServiceBinder extends Binder {
        public DataService getService() {
            return instance;
        }
    }

    private class DataServiceHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            boolean result = false;
            Log.d(TAG, "[DataServiceHandler]handleMessage: start");
            switch (msg.what) {
                case MESSAGE_SYNC:
                    result = sync();
                    break;

                default:
                    Log.d(TAG, "[handleMessage] default is " + msg);
                    break;
            }
        }
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "[onCreate]");
        super.onCreate();

        mContentResolver = getContentResolver();
        initialise(null);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "[onBind]");

        instance = this;

        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "[onStartCommand]");

        instance = this;

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "[onDestroy]");

        mHandler.removeMessages(MESSAGE_SYNC);
        instance = null;

        super.onDestroy();
    }

    public boolean initialise(Activity activity) {
        boolean result = false;
        Log.d(TAG, "initialise: start");

        syncContacts(activity);

        result = loginSip(null);

        mHandler.removeMessages(MESSAGE_SYNC);
        result = mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_SYNC), 0);

        return result;
    }

    private boolean sync() {
        boolean result = false;
        Log.d(TAG, "sync: start");
        checkAccount();
        checkFriend();
        checkPeople(null, null);
        if (!mHandler.hasMessages(MESSAGE_SYNC)) {
            result = mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_SYNC), LOOP_SYNC_TIME);
        }
        return result;
    }

    private String matchAccount(String account) {
        if (TextUtils.isEmpty(account)) {
            Cursor cursor = getAccounts(null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                account = cursor.getString(DatabaseUtil.Account.COLUMN_ACCOUNT);
            } else {
                Log.d(TAG, "matchAccount: account is null");
            }
            if (cursor != null) cursor.close();
        }
        return account;
    }

    /* Account @{ */
    public void requestCode(String number, Handler handler, int what) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("number", number);
        params.put("smsType", "1");
        new DataThread(DataUtil.ACCOUNT_URL + "getSmsCode", params,
                handler == null ? null : handler.obtainMessage(what)).start();
    }

    public void registerAccount(String number, String code, String password, Handler handler, int what) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("number", number);
        params.put("smsType", "1");
        params.put("password", password);
        params.put("smsCode", code);
        new DataThread(DataUtil.ACCOUNT_URL + "registerMagic", params,
                handler == null ? null : handler.obtainMessage(what)).start();
    }

    public void loginAccount(final String account, final String password, String id, final Handler handler, final int what) {
        final String info = DataUtil.getDevice(this);
        Handler h = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    JSONObject obj = (JSONObject) msg.obj;
                    try {
                        if (obj.getString(DataThread.RESULT_CODE).equals(DataThread.RESULT_OK)) {
                            Uri uri = saveAccount(account, password, null, null, obj.getString("sip"), 0L, info, null);
                            if (uri == null) {
                                obj = DataThread.setJSONObject(DataThread.RESULT_ERROR, "account is error");
                            } else {
                                SaveUtils.writeUser(DataService.this, "account", account);
                            }
                        } else {
                            Log.d(TAG, "loginAccount: obj is " + obj);
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "loginAccount: JSONException is ", e);
                    }
                    if (handler != null) {
                        handler.obtainMessage(what, obj).sendToTarget();
                    } else {
                        Log.d(TAG, "loginAccount: handler is null");
                    }
                }
        };
        final Map<String, String> params = new HashMap<String, String>();
        params.put("number", account);
        params.put("password", password);
        params.put("loginInfo", info);
        if (!TextUtils.isEmpty(id)) {
            params.put("deviceId", id);
        }
        new DataThread(DataUtil.ACCOUNT_URL + "loginMagic", params,
                h.obtainMessage(0)).start();
    }

    private Uri saveAccount(String account, String password, String name, Bitmap icon, String sip, Long time, String info, String id) {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.Account._ID, 0);
        values.put(DatabaseUtil.Account.ACCOUNT, account);
        values.put(DatabaseUtil.Account.PASSWORD, password);
        values.put(DatabaseUtil.Account.NAME, name);
        values.put(DatabaseUtil.Account.ICON, DataUtil.getImage(icon));
        values.put(DatabaseUtil.Account.SIP, "sip:" + sip);
        values.put(DatabaseUtil.Account.TIME, time);
        values.put(DatabaseUtil.Account.INFO, info);
        values.put(DatabaseUtil.Account.ID, id);
        return mContentResolver.insert(DatabaseProvider.ACCOUNT_URI, values);
    }

    private Cursor getAccounts(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mContentResolver.query(DatabaseProvider.ACCOUNT_URI, projection, selection, selectionArgs, sortOrder);
    }

    public void queryAccount(String account, Handler handler, int what) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("number", account);
        new DataThread(DataUtil.ACCOUNT_URL + "findAccount", params,
                handler == null ? null : handler.obtainMessage(what)).start();
    }

    @SuppressLint("SimpleDateFormat")
    private void checkAccount() {
        Cursor cursor = getAccounts(null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            final String account = cursor.getString(DatabaseUtil.Account.COLUMN_ACCOUNT);
            final Long time = cursor.getLong(DatabaseUtil.Account.COLUMN_TIME);
            final String info = cursor.getString(DatabaseUtil.Account.COLUMN_INFO);
            final String id = cursor.getString(DatabaseUtil.Account.COLUMN_ID);
            Handler h = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    try {
                        JSONObject obj = (JSONObject) msg.obj;
                        Log.d(TAG, "xiongwei1 checkAccount: obj is " + obj);
                        if (DataThread.RESULT_OK.equals(obj.getString(DataThread.RESULT_CODE))) {
                            String deviceNo = obj.optString("deviceNo");
                            if (!TextUtils.isEmpty(deviceNo)) {
                                if (!deviceNo.equals(id)) {
                                    int count = updateAccountAndDevice(account, deviceNo);
                                    Uri uri = saveDevice(deviceNo, null, obj.getString("deviceSip"));
                                }
                                checkDevice();
                            } else {
                                if (!TextUtils.isEmpty(id)) {
                                    int count = updateAccountAndDevice(account, null);
                                    count = deleteDevice(id);
                                }
                            }
                            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(obj.getString("updateTime"));
                            if (!info.equals(obj.getString("loginInfo"))) {
                                sendBroadcast(new Intent(DataUtil.BROADCAST_INFO));
                            } else if (date.getTime() != time) {
                                syncAccount(account);
                            } else {
                                Log.d(TAG, "checkAccount: not changed");
                            }
                        } else {
                            Log.d(TAG, "checkAccount: obj is " + obj);
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "checkAccount: JSONException is ", e);
                    } catch (ParseException e) {
                        Log.e(TAG, "checkAccount: ParseException is ", e);
                    }
                }
            };
            final Map<String, String> params = new HashMap<String, String>();
            params.put("number", account);
            new DataThread(DataUtil.ACCOUNT_URL + "findAccount", params,
                    h.obtainMessage(0)).start();
        } else {
            Log.d(TAG, "checkAccount: account is null");
        }
        if (cursor != null) cursor.close();
    }

    @SuppressLint("SimpleDateFormat")
    private void syncAccount(final String account) {
        Handler h = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    try {
                        JSONObject obj = (JSONObject) msg.obj;
                        if (obj.getString(DataThread.RESULT_CODE).equals(DataThread.RESULT_OK)) {
                            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(obj.getString("updateTime"));
                            int count = updateAccount(account, obj.optString("name"), DataUtil.getImage(obj.optString("icon")), date.getTime());
                        } else {
                            Log.d(TAG, "syncAccount: obj is " + obj);
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "syncAccount: JSONException is ", e);
                    } catch (ParseException e) {
                        Log.e(TAG, "syncAccount: ParseException is ", e);
                    }
                }
        };
        final Map<String, String> params = new HashMap<String, String>();
        params.put("number", account);
        new DataThread(DataUtil.ACCOUNT_URL + "findAccountDetail", params,
                h.obtainMessage(0)).start();
    }

    @SuppressLint("SimpleDateFormat")
    public void updateAccount(final String account, final String name, final Bitmap icon, final Handler handler, final int what) {
        Cursor cursor = getAccounts(null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Handler h = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        JSONObject obj = (JSONObject) msg.obj;
                        try {
                            if (obj.getString(DataThread.RESULT_CODE).equals(DataThread.RESULT_OK)) {
                                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(obj.getString("updateTime"));
                                int count = updateAccount(account, name, icon, date.getTime());
                            } else {
                                Log.d(TAG, "updateAccount: obj is " + obj);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "updateAccount: JSONException is ", e);
                        } catch (ParseException e) {
                            Log.e(TAG, "updateAccount: ParseException is ", e);
                        }
                        if (handler != null) {
                            handler.obtainMessage(what, obj).sendToTarget();
                        } else {
                            Log.d(TAG, "updateAccount: handler is null");
                        }
                    }
            };
            final Map<String, String> params = new HashMap<String, String>();
            params.put("number", account);
            params.put("name", name);
            params.put("password", cursor.getString(DatabaseUtil.Account.COLUMN_PASSWORD));
            new DataThread(DataUtil.ACCOUNT_URL + "updateAccount", params, account, icon,
                    h.obtainMessage(0)).start();
        } else {
            Log.d(TAG, "updateAccount: account is null");
        }
        if (cursor != null) cursor.close();
    }

    private int updateAccount(String account, String name, Bitmap icon, Long time) {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.Account.NAME, name);
        values.put(DatabaseUtil.Account.ICON, DataUtil.getImage(icon));
        values.put(DatabaseUtil.Account.TIME, time);
        return mContentResolver.update(DatabaseProvider.ACCOUNT_URI, values, DatabaseUtil.Account.ACCOUNT + " = ?", new String[]{account});
    }

    private int updateAccountAndDevice(String account, String id) {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.Account.ID, id);
        return mContentResolver.update(DatabaseProvider.ACCOUNT_URI, values, DatabaseUtil.Account.ACCOUNT + " = ?", new String[]{account});
    }

    public boolean isAccount(String account) {
        boolean result = false;
        Cursor cursor = getAccounts(null, DatabaseUtil.Account.ACCOUNT + " = ?", new String[]{account}, null);
        if (cursor != null && cursor.getCount() > 0) {
            result = true;
        }
        if (cursor != null) cursor.close();
        return result;
    }

    public int logoutAccount(String account) {
        SaveUtils.writeUser(this, "account", "");
        return mContentResolver.delete(Uri.withAppendedPath(DatabaseProvider.ACCOUNT_URI, account), null, null);
    }

    public void requestReset(String account, Handler handler, int what) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("number", account);
        params.put("smsType", "2");
        new DataThread(DataUtil.ACCOUNT_URL + "getSmsCode", params,
                handler == null ? null : handler.obtainMessage(what)).start();
    }

    public void resetAccount(String account, String code, String password, Handler handler, int what) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("number", account);
        params.put("smsType", "2");
        params.put("password", password);
        params.put("smsCode", code);
        new DataThread(DataUtil.ACCOUNT_URL + "findPassword", params,
                handler == null ? null : handler.obtainMessage(what)).start();
    }

    public void deleteAccount(String account, Handler handler, int what) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("number", account);
        new DataThread(DataUtil.ACCOUNT_URL + "delAccount", params,
                handler == null ? null : handler.obtainMessage(what)).start();
    }

    public void requestKey(String id, Handler handler, int what) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("deviceId", id);
        new DataThread(DataUtil.DEVICE_URL + "reqDeviceKey", params,
                handler == null ? null : handler.obtainMessage(what)).start();
    }

    public void bindDevice(String account, final String id, String time, final String name, final Handler handler, final int what) {
        final String a = matchAccount(account);
        Handler h = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    JSONObject obj = (JSONObject) msg.obj;
                    try {
                        if (DataThread.RESULT_OK.equals(obj.getString(DataThread.RESULT_CODE))) {
                            int count = updateAccountAndDevice(a, id);
                            Uri uri = saveDevice(id, name, obj.optString("sipNo"));
                        } else {
                            Log.d(TAG, "bindDevice: obj is " + obj);
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "bindDevice: JSONException is ", e);
                    }
                    if (handler != null) {
                        handler.obtainMessage(what, obj).sendToTarget();
                    } else {
                        Log.d(TAG, "bindDevice: handler is null");
                    }
                }
        };
        final Map<String, String> params = new HashMap<String, String>();
        params.put("number", a);
        params.put("deviceId", id);
        params.put("createTime", time);
        if (!TextUtils.isEmpty(name)) {
            params.put("deviceName", name);
        }
        new DataThread(DataUtil.DEVICE_URL + "bindMagic", params,
                h.obtainMessage(0)).start();
    }

    private Uri saveDevice(String id, String name, String sip) {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.Device.ID, id);
        values.put(DatabaseUtil.Device.TYPE, DatabaseUtil.Device.TYPE_MIRROR);
        values.put(DatabaseUtil.Device.PERMISSION, DatabaseUtil.Device.PERMISSION_PUBLIC);
        values.put(DatabaseUtil.Device.DEVICE_NAME, name);
        values.put(DatabaseUtil.Device.DEVICE_SIP, "sip:" + sip);
        values.put(DatabaseUtil.Device.DEVICE_TIME, 0L);
        return mContentResolver.insert(DatabaseProvider.DEVICES_URI, values);
    }

    private void checkDevice() {
        Cursor cursor = getAccounts(null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            final String id = cursor.getString(DatabaseUtil.Account.COLUMN_ID);
            if (!TextUtils.isEmpty(id)) {
                Handler h = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            try {
                                JSONObject obj = (JSONObject) msg.obj;
                                Log.d(TAG, "xiongwei1 checkDevice: obj is " + obj);
                                if (DataThread.RESULT_OK.equals(obj.getString(DataThread.RESULT_CODE))) {
                                    int count = updateDevice(id, obj.optString("deviceName"));
                                    if (obj.getInt("deviceStatus") == DEVICE_UNBIND) {
                                        Log.d(TAG, "checkDevice: ready to unbind");
                                    }
                                } else {
                                    Log.d(TAG, "checkDevice: obj is " + obj);
                                }
                            } catch (JSONException e) {
                                Log.e(TAG, "checkDevice: JSONException is ", e);
                            }
                        }
                };
                final Map<String, String> params = new HashMap<String, String>();
                params.put("number", cursor.getString(DatabaseUtil.Account.COLUMN_ACCOUNT));
                params.put("deviceId", id);
                new DataThread(DataUtil.DEVICE_URL + "confirmReqRemove", params,
                        h.obtainMessage(0)).start();
            } else {
                Log.d(TAG, "checkDevice: device is null");
            }
        } else {
            Log.d(TAG, "checkDevice: account is null");
        }
        if (cursor != null) cursor.close();
    }

    public void updateDevice(String account, String id, final String name, final Handler handler, final int what) {
        Cursor cursor = getAccounts(null, null, null, null);
        if (account != null || (cursor != null && cursor.moveToFirst())) {
            final String a = (account != null ? account : cursor.getString(DatabaseUtil.Account.COLUMN_ACCOUNT));
            final String d = (id != null ? id : cursor.getString(DatabaseUtil.Account.COLUMN_ID));
            if (!TextUtils.isEmpty(d)) {
                Handler h = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            JSONObject obj = (JSONObject) msg.obj;
                            try {
                                if (DataThread.RESULT_OK.equals(obj.getString(DataThread.RESULT_CODE))) {
                                    int count = updateDevice(d, name);
                                } else {
                                    Log.d(TAG, "updateDevice: obj is " + obj);
                                }
                            } catch (JSONException e) {
                                Log.e(TAG, "updateDevice: JSONException is ", e);
                            }
                            if (handler != null) {
                                handler.obtainMessage(what, obj).sendToTarget();
                            } else {
                                Log.d(TAG, "updateDevice: handler is null");
                            }
                        }
                };
                final Map<String, String> params = new HashMap<String, String>();
                params.put("number", a);
                params.put("deviceId", d);
                params.put("deviceName", name);
                new DataThread(DataUtil.DEVICE_URL + "updateMagic", params,
                        h.obtainMessage(0)).start();
            } else {
                Log.d(TAG, "updateDevice: device is null");
            }
        } else {
            Log.d(TAG, "updateDevice: account is null");
        }
        if (cursor != null) cursor.close();
    }

    private int updateDevice(String id, String name) {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.Device.PERMISSION, DatabaseUtil.Device.PERMISSION_PUBLIC);
        values.put(DatabaseUtil.Device.DEVICE_NAME, name);
        values.put(DatabaseUtil.Device.DEVICE_TIME, 0L);
        return mContentResolver.update(DatabaseProvider.DEVICES_URI, values, DatabaseUtil.Device.ID + " = ?", new String[]{id});
    }

    public void requestDevice(String account, String id, Handler handler, int what) {
        Cursor cursor = getAccounts(null, null, null, null);
        if (account != null || (cursor != null && cursor.moveToFirst())) {
            account = (account != null ? account : cursor.getString(DatabaseUtil.Account.COLUMN_ACCOUNT));
            id = (id != null ? id : cursor.getString(DatabaseUtil.Account.COLUMN_ID));
            if (!TextUtils.isEmpty(id)) {
                final Map<String, String> params = new HashMap<String, String>();
                params.put("number", account);
                params.put("deviceId", id);
                new DataThread(DataUtil.DEVICE_URL + "reqRemoveBindMagic", params,
                        handler == null ? null : handler.obtainMessage(what)).start();
            } else {
                Log.d(TAG, "requestDevice: device is null");
            }
        } else {
            Log.d(TAG, "requestDevice: account is null");
        }
        if (cursor != null) cursor.close();
    }

    public void unbindDevice(String account, String id, int request, final Handler handler, final int what) {
        Cursor cursor = getAccounts(null, null, null, null);
        if (account != null || (cursor != null && cursor.moveToFirst())) {
            final String a = (account != null ? account : cursor.getString(DatabaseUtil.Account.COLUMN_ACCOUNT));
            final String d = (id != null ? id : cursor.getString(DatabaseUtil.Account.COLUMN_ID));
            if (!TextUtils.isEmpty(d)) {
                Handler h = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            JSONObject obj = (JSONObject) msg.obj;
                            try {
                                if (DataThread.RESULT_OK.equals(obj.getString(DataThread.RESULT_CODE))) {
                                    int count = updateAccountAndDevice(a, null);
                                    count = deleteDevice(d);
                                } else {
                                    Log.d(TAG, "unbindDevice: obj is " + obj);
                                }
                            } catch (JSONException e) {
                                Log.e(TAG, "unbindDevice: JSONException is ", e);
                            }
                            if (handler != null) {
                                handler.obtainMessage(what, obj).sendToTarget();
                            } else {
                                Log.d(TAG, "unbindDevice: handler is null");
                            }
                        }
                };
                final Map<String, String> params = new HashMap<String, String>();
                params.put("number", a);
                params.put("deviceId", d);
                params.put("request", request + "");
                new DataThread(DataUtil.DEVICE_URL + "removeBind", params,
                        h.obtainMessage(0)).start();
            } else {
                Log.d(TAG, "unbindDevice: device is null");
            }
        } else {
            Log.d(TAG, "unbindDevice: account is null");
        }
        if (cursor != null) cursor.close();
    }

    private Cursor getDevice(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mContentResolver.query(DatabaseProvider.DEVICES_URI, projection, selection, selectionArgs, sortOrder);
    }

    private int deleteDevice(String id) {
        return mContentResolver.delete(DatabaseProvider.DEVICES_URI, DatabaseUtil.Device.ID + " = ?", new String[]{id});
    }

    public Cursor getAccountsAndDevices(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mContentResolver.query(DatabaseProvider.ACCOUNT_VIEW_URI, projection, selection, selectionArgs, sortOrder);
    }

    public Cursor getAccountForSip(String sip) {
        return getAccountsAndDevices(null,
                DatabaseUtil.Account.SIP + " = ? or " + DatabaseUtil.Account.DEVICE_SIP + " = ?",
                new String[]{"sip:" + sip, "sip:" + sip},
                null);
    }
    /* } */

    /* Friend @{ */
    private class Friend {
        private Long mTime;
        private String mId;

        public Friend(Long time, String id) {
            mTime = time;
            mId = id;
        }

        public Long getTime() {
            return mTime;
        }

        public String getId() {
            return mId;
        }
    }

    private void checkFriend() {
        Cursor cursor = getAccounts(null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Handler h = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        try {
                            JSONObject obj = (JSONObject) msg.obj;
                            Log.d(TAG, "xiongwei1 checkFriend: obj is " + obj);
                            if (obj.getString(DataThread.RESULT_CODE).equals(DataThread.RESULT_OK)) {
                                Map<String, Friend> friends = new HashMap<String, Friend>();
                                Cursor cursor = getFriends(null, null, null, null);
                                while(cursor.moveToNext()) {
                                    friends.put(cursor.getString(DatabaseUtil.Friend.COLUMN_ACCOUNT),
                                            new Friend(cursor.getLong(DatabaseUtil.Friend.COLUMN_TIME), cursor.getString(DatabaseUtil.Friend.COLUMN_ID)));
                                }
                                if (cursor != null) cursor.close();

                                JSONArray array = new JSONArray(obj.getString("friendList"));
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject data = array.getJSONObject(i);
                                    String account = data.getString("number");
                                    Long time = data.getLong("friendTime");
                                    Log.d(TAG, "checkFriend: array[" + i + "] is " + data);
                                    if (data.getInt("friendStatus") == FRIEND_REQUEST) {
                                        Long requestTime = data.getLong("requestTime");
                                        String message = data.getString("friendMessage");
                                        cursor = getProposers(null,
                                                DatabaseUtil.Proposer.ACCOUNT + " = ?",
                                                new String[] {account},
                                                null);
                                        if (cursor != null && cursor.moveToFirst()) {
                                            if (requestTime != cursor.getLong(DatabaseUtil.Proposer.COLUMN_REQUEST_TIME)) {
                                                int count = updateProposer(account, requestTime, message, DatabaseUtil.Proposer.STATUS_NEW);
                                            }
                                            if (time != cursor.getLong(DatabaseUtil.Proposer.COLUMN_TIME)) {
                                                syncProposer(account, time);
                                            }
                                        } else {
                                            Uri uri = saveProposer(account,
                                                    null,
                                                    null,
                                                    0L,
                                                    requestTime,
                                                    message,
                                                    DatabaseUtil.Proposer.STATUS_NEW);
                                            syncProposer(account, time);
                                        }
                                        if (cursor != null) cursor.close();
                                    } else {
                                        String id = data.optString("deviceNo");
                                        if (friends.size() > 0 && friends.get(account) != null) {
                                            Friend friend = friends.get(account);
                                            if (!TextUtils.isEmpty(id)) {
                                                if (!id.equals(friend.getId())) {
                                                    int count = updateFriendAndDevice(account, id);
                                                    Uri uri = saveFriendDevice(id, null, obj.optString("deviceSip"));
                                                }
                                            } else {
                                                if (!TextUtils.isEmpty(id)) {
                                                    int count = updateFriendAndDevice(account, null);
                                                    count = deleteFriendDevices(friend.getId());
                                                }
                                            }
                                            if (time != friend.getTime()) {
                                                syncFriend(account);
                                            }
                                            friends.remove(account);
                                        } else {
                                            if (!TextUtils.isEmpty(id)) {
                                                Uri uri = saveFriend(account,
                                                        null,
                                                        null,
                                                        0L,
                                                        id,
                                                        null,
                                                        data.getString("accountSip"));
                                                uri = saveFriendDevice(id, null, obj.optString("deviceSip"));
                                            } else {
                                                Uri uri = saveFriend(account,
                                                        null,
                                                        null,
                                                        0L,
                                                        null,
                                                        null,
                                                        data.getString("accountSip"));
                                            }
                                            updateProposer(account, DatabaseUtil.Proposer.STATUS_FRIEND);
                                            logNotification(account, null, DatabaseUtil.Notification.REQUEST_OTHER);
                                            syncFriend(account);
                                        }
                                    }
                                }
                                for (String account : friends.keySet()) {
                                    int count = deleteFriend(account);
                                }
                            } else {
                                Log.d(TAG, "checkFriend: obj is " + obj);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "checkFriend: JSONException is ", e);
                        }
                    }
            };
            final Map<String, String> params = new HashMap<String, String>();
            params.put("number", cursor.getString(DatabaseUtil.Account.COLUMN_ACCOUNT));
            new DataThread(DataUtil.ACCOUNT_URL + "findFriendAccountList", params,
                    h.obtainMessage(0)).start();
        } else {
            Log.d(TAG, "checkFriend: account is null");
        }
        if (cursor != null) cursor.close();
    }

    @SuppressLint("SimpleDateFormat")
    private void syncFriend(final String account) {
        Handler h = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    try {
                        JSONObject obj = (JSONObject) msg.obj;
                        if (DataThread.RESULT_OK.equals(obj.getString(DataThread.RESULT_CODE))) {
                            String name = obj.optString("name");
                            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(obj.getString("updateTime"));
                            int count = updateFriend(account, (TextUtils.isEmpty(name) || EMPTY.equals(name)) ? null : name, DataUtil.getImage(obj.optString("icon")), date.getTime());
                        } else {
                            Log.d(TAG, "syncFriend: obj is " + obj);
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "syncFriend: JSONException is ", e);
                    } catch (ParseException e) {
                        Log.e(TAG, "syncFriend: ParseException is ", e);
                    }
                }
        };
        final Map<String, String> params = new HashMap<String, String>();
        params.put("number", account);
        new DataThread(DataUtil.ACCOUNT_URL + "findFriendAccountDetail", params,
                h.obtainMessage(0)).start();
    }

    private Uri saveFriend(String account, String name, Bitmap icon, Long time, String id, String comment, String sip) {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.Friend.ACCOUNT, account);
        values.put(DatabaseUtil.Friend.NAME, name);
        values.put(DatabaseUtil.Friend.ICON, DataUtil.getImage(icon));
        values.put(DatabaseUtil.Friend.TIME, time);
        values.put(DatabaseUtil.Friend.ID, id);
        values.put(DatabaseUtil.Friend.COMMENT, comment);
        values.put(DatabaseUtil.Friend.SIP, "sip:" + sip);
        return mContentResolver.insert(DatabaseProvider.FRIENDS_URI, values);
    }

    private int updateFriendAndDevice(String account, String id) {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.Friend.ID, id);
        return mContentResolver.update(DatabaseProvider.FRIENDS_URI, values, DatabaseUtil.Friend.ACCOUNT + " = ?", new String[]{account});
    }

    private int updateFriend(String account, String name, Bitmap icon, Long time) {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.Friend.NAME, name);
        values.put(DatabaseUtil.Friend.ICON, DataUtil.getImage(icon));
        values.put(DatabaseUtil.Friend.TIME, time);
        return mContentResolver.update(DatabaseProvider.FRIENDS_URI, values, DatabaseUtil.Friend.ACCOUNT + " = ?", new String[]{account});
    }

    public int updateFriend(String account, String comment) {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.Friend.COMMENT, comment);
        return mContentResolver.update(DatabaseProvider.FRIENDS_URI, values, DatabaseUtil.Friend.ACCOUNT + " = ?", new String[]{account});
    }

    private Cursor getFriends(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mContentResolver.query(DatabaseProvider.FRIENDS_URI, projection, selection, selectionArgs, sortOrder);
    }

    public void deleteFriend(String account, final String friendAccount, final Handler handler, final int what) {
        account = matchAccount(account);
        Handler h = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    JSONObject obj = (JSONObject) msg.obj;
                    try {
                        if (DataThread.RESULT_OK.equals(obj.getString(DataThread.RESULT_CODE))) {
                            int count = deleteFriend(friendAccount);
                            count = deleteProposers(friendAccount);
                            count = deleteCalllog(friendAccount);
                            count = deleteNotification(friendAccount);
                        } else {
                            Log.d(TAG, "deleteFriend: obj is " + obj);
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "deleteFriend: JSONException is ", e);
                    }
                    if (handler != null) {
                        handler.obtainMessage(what, obj).sendToTarget();
                    } else {
                        Log.d(TAG, "loginAccount: handler is null");
                    }
                }
        };
        final Map<String, String> params = new HashMap<String, String>();
        params.put("number", account);
        params.put("friendNumber", friendAccount);
        new DataThread(DataUtil.FRIEND_URL + "delFriendRelation", params,
                h.obtainMessage(0)).start();
    }

    private int deleteFriend(String account) {
        return mContentResolver.delete(DatabaseProvider.FRIENDS_URI, DatabaseUtil.Friend.ACCOUNT + " = ?", new String[]{account});
    }

    private Uri saveFriendDevice(String id, String name, String sip) {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.FriendDevice.ID, id);
        values.put(DatabaseUtil.FriendDevice.TYPE, DatabaseUtil.Device.TYPE_MIRROR);
        values.put(DatabaseUtil.FriendDevice.DEVICE_NAME, name);
        values.put(DatabaseUtil.FriendDevice.DEVICE_SIP, "sip:" + sip);
        values.put(DatabaseUtil.FriendDevice.DEVICE_TIME, 0L);
        return mContentResolver.insert(DatabaseProvider.FRIENDDEVICES_URI, values);
    }

    private Cursor getFriendDevices(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mContentResolver.query(DatabaseProvider.FRIENDDEVICES_URI, projection, selection, selectionArgs, sortOrder);
    }

    private int deleteFriendDevices(String id) {
        return mContentResolver.delete(DatabaseProvider.FRIENDDEVICES_URI, DatabaseUtil.FriendDevice.ID + " = ?", new String[]{id});
    }

    public Cursor getFriendsAndDevices(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mContentResolver.query(DatabaseProvider.FRIENDS_VIEW_URI, projection, selection, selectionArgs, sortOrder);
    }

    public Cursor getFriendForSip(String sip) {
        return getFriendsAndDevices(null,
                DatabaseUtil.Friend.SIP + " = ? or " + DatabaseUtil.Friend.DEVICE_SIP + " = ?",
                new String[]{"sip:" + sip, "sip:" + sip},
                null);
    }
    /* } */

    public void queryPeople(String number, Handler handler, int what) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("contactNumber", number);
        new DataThread(DataUtil.FRIEND_URL + "findFriendAccount", params,
                handler == null ? null : handler.obtainMessage(what)).start();
    }

    private void checkPeople(Cursor cursor, String exception) {
        if (cursor == null) {
            cursor = getPeoples(null, null, null, null);
        } else {
            Log.d(TAG, "checkPeople: cursor is " + cursor);
        }
        exception = matchAccount(exception);
        if (cursor != null && cursor.moveToNext()) {
            syncPeople(cursor, exception);
        } else {
            Log.d(TAG, "checkPeople: people is null");
            if (cursor != null) cursor.close();
        }
    }

    private void syncPeople(final Cursor cursor, final String exception) {
        final int contactId = cursor.getInt(DatabaseUtil.People.COLUMN_CONTACT_ID);
        String number = cursor.getString(DatabaseUtil.People.COLUMN_NUMBER);
        if (!TextUtils.isEmpty(number)) {
            String[] numberArray = number.split(",");
            number = numberArray[0];
        }
        if (!TextUtils.isEmpty(number) && !number.equals(exception)) {
            Handler h = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        try {
                            JSONObject obj = (JSONObject) msg.obj;
                            Log.d(TAG, "xiongwei1 syncPeople: obj is " + obj);
                            if (DataThread.RESULT_OK.equals(obj.getString(DataThread.RESULT_CODE))) {
                                String name = obj.getString("contact_name");
                                int count = updatePeople(contactId, obj.getString("contact_account"),
                                        (TextUtils.isEmpty(name) || EMPTY.equals(name)) ? null : name,
                                        DataUtil.getImage(obj.optString("contact_icon")), 0L);
                            } else if ("111".equals(obj.getString(DataThread.RESULT_CODE))) {
                                int count = updatePeople(contactId, null, null, null, 0L);
                            } else {
                                Log.d(TAG, "syncPeople: obj is " + obj);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "syncPeople: JSONException is ", e);
                        }
                        checkPeople(cursor, exception);
                    }
            };
            final Map<String, String> params = new HashMap<String, String>();
            params.put("contactNumber", number);
            new DataThread(DataUtil.FRIEND_URL + "findFriendAccount", params,
                    h == null ? null : h.obtainMessage(0)).start();
        } else {
            if (number.equals(exception)) {
                int count = updatePeople(contactId, null, null, null, 0L);
            }
            checkPeople(cursor, exception);
        }
    }

    private int updatePeople(int id, String account, String name, Bitmap icon, Long time) {
        int status = DatabaseUtil.People.STATUS_DEFAULT;
        Cursor cursor = getFriends(null, DatabaseUtil.Friend.ACCOUNT + " = " + account, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            status = DatabaseUtil.People.STATUS_FRIEND;
        } else {
            status = DatabaseUtil.People.STATUS_UNKNOW;
        }
        if (cursor != null) cursor.close();

        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.People.ACCOUNT, account);
        values.put(DatabaseUtil.People.NAME, name);
        values.put(DatabaseUtil.People.ICON, DataUtil.getImage(icon));
        values.put(DatabaseUtil.People.TIME, time);
        values.put(DatabaseUtil.People.STATUS, status);
        return mContentResolver.update(DatabaseProvider.PEOPLES_URI, values, DatabaseUtil.People.CONTACT_ID + " = " + id, null);
    }

    public Cursor getPeoples(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mContentResolver.query(DatabaseProvider.PEOPLES_URI, projection, selection, selectionArgs, sortOrder);
    }

    public void syncContacts(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if(activity == null) {
                Log.d(TAG, "syncContacts: activity is null.");
            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.WRITE_CONTACTS},
                        PERMISSIONS_REQUEST_WRITE_CONTACTS);
            }
            return;
        }

        Cursor cursorContatcs = mContentResolver.query(ContactsContract.RawContacts.CONTENT_URI,
                new String[] { ContactsContract.RawContacts.CONTACT_ID,
                        ContactsContract.RawContacts.VERSION,
                        ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY},
                null, null, ContactsContract.RawContacts.CONTACT_ID + " asc");

        Cursor cursorPeoples = getPeoples(
                new String[] { DatabaseUtil.People.CONTACT_ID,
                        DatabaseUtil.People.VERSION},
                null, null, DatabaseUtil.People.CONTACT_ID + " asc");

        if (cursorContatcs == null || !cursorContatcs.moveToFirst()) {
            if (cursorPeoples != null && cursorPeoples.moveToFirst()) {
                mContentResolver.delete(DatabaseProvider.PEOPLES_URI, null, null);
            } else {
                Log.d(TAG, "syncContacts: contacts is null.");
            }
        } else if (cursorPeoples == null || !cursorPeoples.moveToFirst()) {
            if (cursorContatcs != null && cursorContatcs.moveToFirst()) {
                ContentValues values = new ContentValues();
                do {
                    Long id = cursorContatcs.getLong(0);
                    values.put(DatabaseUtil.People.CONTACT_ID, id);
                    values.put(DatabaseUtil.People.VERSION, cursorContatcs.getLong(1));
                    values.put(DatabaseUtil.People.CONTACT_NAME, cursorContatcs.getString(2));
                    values.put(DatabaseUtil.People.NUMBER, getContactNumbers(id));
                    mContentResolver.insert(DatabaseProvider.PEOPLES_URI, values);
                    values.clear();
                } while (cursorContatcs.moveToNext());
            } else {
                Log.d(TAG, "syncContacts: peoples is null.");
            }
        } else {
            ContentValues values = new ContentValues();
            while(!cursorContatcs.isAfterLast() || !cursorPeoples.isAfterLast()) {
                if (cursorContatcs.isAfterLast()) {
                    Long id = cursorPeoples.getLong(0);
                    mContentResolver.delete(
                            Uri.withAppendedPath(DatabaseProvider.PEOPLES_URI, "/" + Uri.encode(Long.toString(id))),
                            null, null);
                    cursorPeoples.moveToNext();
                } else if (cursorPeoples.isAfterLast()) {
                    Long id = cursorContatcs.getLong(0);
                    Long version = cursorContatcs.getLong(1);
                    values.put(DatabaseUtil.People.CONTACT_ID, id);
                    values.put(DatabaseUtil.People.VERSION, version);
                    values.put(DatabaseUtil.People.CONTACT_NAME, cursorContatcs.getString(2));
                    values.put(DatabaseUtil.People.NUMBER, getContactNumbers(id));
                    mContentResolver.insert(DatabaseProvider.PEOPLES_URI, values);
                    values.clear();
                    cursorContatcs.moveToNext();
                } else {
                    Long contactId = cursorContatcs.getLong(0);
                    Long contactVersion = cursorContatcs.getLong(1);
                    Long peopleId = cursorPeoples.getLong(0);
                    Long peopleVersion = cursorPeoples.getLong(1);
                    Log.d(TAG, "syncContacts: contactId" + contactId + "(" + contactVersion + "), peopleId" + peopleId + "(" + peopleVersion + ")");
                    if (contactId < peopleId) {
                        Log.d(TAG, "syncContacts: peoples is error.");
                        cursorContatcs.moveToNext();
                    } else if (contactId > peopleId) {
                        mContentResolver.delete(
                                Uri.withAppendedPath(DatabaseProvider.PEOPLES_URI, "/" + Uri.encode(Long.toString(peopleId))),
                                null, null);
                        cursorPeoples.moveToNext();
                    } else if (contactId == peopleId && contactVersion == peopleVersion) {
                        cursorContatcs.moveToNext();
                        cursorPeoples.moveToNext();
                    } else {
                        values.put(DatabaseUtil.People.CONTACT_ID, contactId);
                        values.put(DatabaseUtil.People.VERSION, contactVersion);
                        values.put(DatabaseUtil.People.CONTACT_NAME, cursorContatcs.getString(2));
                        values.put(DatabaseUtil.People.NUMBER, getContactNumbers(contactId));
                        mContentResolver.update(
                                Uri.withAppendedPath(DatabaseProvider.PEOPLES_URI, "/" + Uri.encode(Long.toString(peopleId))),
                                values, null, null);
                        values.clear();
                        cursorContatcs.moveToNext();
                        cursorPeoples.moveToNext();
                    }
                }
            }
        }

        if (cursorContatcs != null) cursorContatcs.close();
        if (cursorPeoples != null) cursorPeoples.close();
    }

    private String getContactNumbers(Long id) {
        StringBuilder sb = new StringBuilder();

        Cursor cursor = mContentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
        if (cursor != null) {
            while(cursor.moveToNext()) {
                if (!TextUtils.isEmpty(sb.toString())) {
                    sb.append(",");
                }
                String number = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    number = PhoneNumberUtils.normalizeNumber(cursor.getString(0));
                } else {
                    number = cursor.getString(0);
                }
                Log.d(TAG, "getContactNumbers: number = " + number);
                sb.append(number);
            }
            cursor.close();
        } else {
            Log.d(TAG, "getContactNumbers: number is null.");
        }

        return sb.toString();
    }

    public void requestFriend(String account, String number, String message, final Handler handler, final int what) {
        account = matchAccount(account);
        Handler h = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    JSONObject obj = (JSONObject) msg.obj;
                    try {
                        if ("444".equals(obj.getString(DataThread.RESULT_CODE))) {
                            checkFriend();
                        } else {
                            Log.d(TAG, "requestFriend: obj is " + obj);
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "requestFriend: JSONException is ", e);
                    }
                    if (handler != null) {
                        handler.obtainMessage(what, obj).sendToTarget();
                    } else {
                        Log.d(TAG, "requestFriend: handler is null");
                    }
                }
        };
        final Map<String, String> params = new HashMap<String, String>();
        params.put("number", account);
        params.put("contactNumber", number);
        params.put("status", 0 + "");
        params.put("contactMessage", message);
        new DataThread(DataUtil.FRIEND_URL + "addFriendRequest", params,
                h.obtainMessage(0)).start();
    }

    private void syncProposer(final String account, final Long time) {
        Handler h = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    try {
                        JSONObject obj = (JSONObject) msg.obj;
                        if (DataThread.RESULT_OK.equals(obj.getString(DataThread.RESULT_CODE))) {
                            String name = obj.optString("friendName");
                            int count = updateProposer(account, (TextUtils.isEmpty(name) || EMPTY.equals(name)) ? null : name, DataUtil.getImage(obj.optString("friendIcon")), time);
                        } else {
                            Log.d(TAG, "syncProposer: obj is " + obj);
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "syncProposer: JSONException is ", e);
                    }
                }
        };
        final Map<String, String> params = new HashMap<String, String>();
        params.put("number", account);
        new DataThread(DataUtil.FRIEND_URL + "requestFriendDetail", params,
                h.obtainMessage(0)).start();
    }

    private Uri saveProposer(String account, String name, Bitmap icon, Long time, Long request_time, String message, int status) {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.Proposer.ACCOUNT, account);
        values.put(DatabaseUtil.Proposer.NAME, name);
        values.put(DatabaseUtil.Proposer.ICON, DataUtil.getImage(icon));
        values.put(DatabaseUtil.Proposer.TIME, time);
        values.put(DatabaseUtil.Proposer.REQUEST_TIME, request_time);
        values.put(DatabaseUtil.Proposer.MESSAGE, message);
        values.put(DatabaseUtil.Proposer.STATUS, status);
        return mContentResolver.insert(DatabaseProvider.PROPOSERS_URI, values);
    }

    private int updateProposer(String account, Long request_time, String message, int status) {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.Proposer.REQUEST_TIME, request_time);
        values.put(DatabaseUtil.Proposer.MESSAGE, message);
        values.put(DatabaseUtil.Proposer.STATUS, status);
        return mContentResolver.update(DatabaseProvider.PROPOSERS_URI, values, DatabaseUtil.Proposer.ACCOUNT + " = ?", new String[]{account});
    }

    private int updateProposer(String account, int status) {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.Proposer.STATUS, status);
        return mContentResolver.update(DatabaseProvider.PROPOSERS_URI, values, DatabaseUtil.Proposer.ACCOUNT + " = ?", new String[]{account});
    }

    private int updateProposer(String account, String name, Bitmap icon, Long time) {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.Proposer.ACCOUNT, account);
        values.put(DatabaseUtil.Proposer.NAME, name);
        values.put(DatabaseUtil.Proposer.ICON, DataUtil.getImage(icon));
        values.put(DatabaseUtil.Proposer.TIME, time);
        return mContentResolver.update(DatabaseProvider.PROPOSERS_URI, values, DatabaseUtil.Proposer.ACCOUNT + " = ?", new String[]{account});
    }

    public Cursor getProposers(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mContentResolver.query(DatabaseProvider.PROPOSERS_URI, projection, selection, selectionArgs, sortOrder);
    }

    private int deleteProposers(String account) {
        return mContentResolver.delete(DatabaseProvider.PROPOSERS_URI, DatabaseUtil.Proposer.ACCOUNT + " = ?", new String[]{account});
    }

    public void confirmProposer(String account, final String friendAccount, final int request, final Handler handler, final int what) {
        account = matchAccount(account);
        Handler h = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    JSONObject obj = (JSONObject) msg.obj;
                    try {
                        if (DataThread.RESULT_OK.equals(obj.getString(DataThread.RESULT_CODE))) {
                            int count = updateProposer(friendAccount,
                                    request == REQUEST_CANCEL ? DatabaseUtil.Proposer.STATUS_IGNORE : DatabaseUtil.Proposer.STATUS_FRIEND);
                            if (request == REQUEST_OK) {
                                logNotification(friendAccount, null, DatabaseUtil.Notification.REQUEST_SELF);
                                checkFriend();
                            } else {
                                Log.d(TAG, "confirmProposer: cancel for " + friendAccount);
                            }
                        } else {
                            Log.d(TAG, "confirmProposer: obj is " + obj);
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "confirmProposer: JSONException is ", e);
                    }
                    if (handler != null) {
                        handler.obtainMessage(what, obj).sendToTarget();
                    } else {
                        Log.d(TAG, "confirmProposer: handler is null");
                    }
                }
        };
        final Map<String, String> params = new HashMap<String, String>();
        params.put("number", account);
        params.put("friendNumber", friendAccount);
        params.put("request", request + "");
        new DataThread(DataUtil.FRIEND_URL + "confirmFriendRelation", params,
                h.obtainMessage(0)).start();
    }

    public Cursor getCalllogs(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mContentResolver.query(DatabaseProvider.CALLLOGS_VIEW_URI, projection, selection, selectionArgs, sortOrder);
    }

    public int readCalllog() {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.Calllog.READ, DatabaseUtil.Calllog.READ_OLD);
        return mContentResolver.update(DatabaseProvider.CALLLOGS_URI, values, DatabaseUtil.Calllog.READ + " = ?", new String[]{DatabaseUtil.Calllog.READ_NEW + ""});
    }

    private int deleteCalllog(String account) {
        return mContentResolver.delete(DatabaseProvider.CALLLOGS_URI, DatabaseUtil.Calllog.ACCOUNT + " = ?", new String[]{account});
    }

    private Uri logNotification(String account, String id, int request) {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.Notification.TIME, System.currentTimeMillis()/1000);
        values.put(DatabaseUtil.Notification.ACCOUNT, account);
        values.put(DatabaseUtil.Notification.ID, id);
        values.put(DatabaseUtil.Notification.REQUEST, request);
        values.put(DatabaseUtil.Notification.READ, DatabaseUtil.Notification.READ_NEW);
        return mContentResolver.insert(DatabaseProvider.NOTIFICATIONS_URI, values);
    }

    private Cursor getNotifications(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mContentResolver.query(DatabaseProvider.NOTIFICATIONS_VIEW_URI, projection, selection, selectionArgs, sortOrder);
    }

    public int readNotification() {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.Notification.READ, DatabaseUtil.Notification.READ_OLD);
        return mContentResolver.update(DatabaseProvider.NOTIFICATIONS_URI, values, DatabaseUtil.Notification.READ + " = ?", new String[]{DatabaseUtil.Notification.READ_NEW + ""});
    }

    public int deleteNotification(String account) {
        return mContentResolver.delete(DatabaseProvider.NOTIFICATIONS_URI, DatabaseUtil.Notification.ACCOUNT + " = ?", new String[]{account});
    }

    public ArrayList<Object> getCalllogsAndNotifications() {
        Cursor cursorCalllogs = getCalllogs(null, null, null, DatabaseUtil.Calllog.TIME + " desc");
        Cursor cursorNotifications = getNotifications(null, null, null, DatabaseUtil.Notification.TIME + " desc");

        ArrayList<Object> arrayList = new ArrayList<Object>();

        if (cursorCalllogs == null || !cursorCalllogs.moveToFirst()) {
            if (cursorNotifications != null && cursorNotifications.moveToFirst()) {
                do {
                    arrayList.add(new Notification(cursorNotifications.getLong(DatabaseUtil.Notification.COLUMN_TIME),
                            cursorNotifications.getString(DatabaseUtil.Notification.COLUMN_ACCOUNT),
                            cursorNotifications.getString(DatabaseUtil.Notification.COLUMN_ID),
                            cursorNotifications.getInt(DatabaseUtil.Notification.COLUMN_REQUEST),
                            cursorNotifications.getInt(DatabaseUtil.Notification.COLUMN_READ),
                            cursorNotifications.getString(DatabaseUtil.Notification.COLUMN_ACCOUNT_NAME),
                            DataUtil.getImage(cursorNotifications.getBlob(DatabaseUtil.Notification.COLUMN_ACCOUNT_ICON)),
                            cursorNotifications.getString(DatabaseUtil.Notification.COLUMN_ACCOUNT_COMMENT)));
                } while (cursorNotifications.moveToNext());
            } else {
                Log.d(TAG, "getCalllogsAndNotifications: calllogs is null.");
            }
        } else if (cursorNotifications == null || !cursorNotifications.moveToFirst()) {
            if (cursorCalllogs != null && cursorCalllogs.moveToFirst()) {
                do {
                    arrayList.add(new CallLog(cursorCalllogs.getLong(DatabaseUtil.Calllog.COLUMN_TIME),
                            cursorCalllogs.getString(DatabaseUtil.Calllog.COLUMN_ACCOUNT),
                            cursorCalllogs.getString(DatabaseUtil.Calllog.COLUMN_ID),
                            cursorCalllogs.getLong(DatabaseUtil.Calllog.COLUMN_END_TIME),
                            cursorCalllogs.getInt(DatabaseUtil.Calllog.COLUMN_DURATION),
                            cursorCalllogs.getInt(DatabaseUtil.Calllog.COLUMN_STATUS),
                            cursorCalllogs.getInt(DatabaseUtil.Calllog.COLUMN_READ),
                            cursorCalllogs.getString(DatabaseUtil.Calllog.COLUMN_ACCOUNT_NAME),
                            DataUtil.getImage(cursorCalllogs.getBlob(DatabaseUtil.Calllog.COLUMN_ACCOUNT_ICON)),
                            cursorCalllogs.getString(DatabaseUtil.Calllog.COLUMN_ACCOUNT_COMMENT)));
                } while (cursorCalllogs.moveToNext());
            } else {
                Log.d(TAG, "getCalllogsAndNotifications: notifications is null.");
            }
        } else {
            while (!cursorCalllogs.isAfterLast() || !cursorNotifications.isAfterLast()) {
                if (cursorCalllogs.isAfterLast()) {
                    arrayList.add(new Notification(cursorNotifications.getLong(DatabaseUtil.Notification.COLUMN_TIME),
                            cursorNotifications.getString(DatabaseUtil.Notification.COLUMN_ACCOUNT),
                            cursorNotifications.getString(DatabaseUtil.Notification.COLUMN_ID),
                            cursorNotifications.getInt(DatabaseUtil.Notification.COLUMN_REQUEST),
                            cursorNotifications.getInt(DatabaseUtil.Notification.COLUMN_READ),
                            cursorNotifications.getString(DatabaseUtil.Notification.COLUMN_ACCOUNT_NAME),
                            DataUtil.getImage(cursorNotifications.getBlob(DatabaseUtil.Notification.COLUMN_ACCOUNT_ICON)),
                            cursorNotifications.getString(DatabaseUtil.Notification.COLUMN_ACCOUNT_COMMENT)));
                    cursorNotifications.moveToNext();
                } else if (cursorNotifications.isAfterLast()) {
                    arrayList.add(new CallLog(cursorCalllogs.getLong(DatabaseUtil.Calllog.COLUMN_TIME),
                            cursorCalllogs.getString(DatabaseUtil.Calllog.COLUMN_ACCOUNT),
                            cursorCalllogs.getString(DatabaseUtil.Calllog.COLUMN_ID),
                            cursorCalllogs.getLong(DatabaseUtil.Calllog.COLUMN_END_TIME),
                            cursorCalllogs.getInt(DatabaseUtil.Calllog.COLUMN_DURATION),
                            cursorCalllogs.getInt(DatabaseUtil.Calllog.COLUMN_STATUS),
                            cursorCalllogs.getInt(DatabaseUtil.Calllog.COLUMN_READ),
                            cursorCalllogs.getString(DatabaseUtil.Calllog.COLUMN_ACCOUNT_NAME),
                            DataUtil.getImage(cursorCalllogs.getBlob(DatabaseUtil.Calllog.COLUMN_ACCOUNT_ICON)),
                            cursorCalllogs.getString(DatabaseUtil.Calllog.COLUMN_ACCOUNT_COMMENT)));
                    cursorCalllogs.moveToNext();
                } else {
                    Long timeCalllog = cursorCalllogs.getLong(DatabaseUtil.Calllog.COLUMN_TIME);
                    Long timeNotification = cursorNotifications.getLong(DatabaseUtil.Notification.COLUMN_TIME);
                    if (timeCalllog > timeNotification) {
                        arrayList.add(new CallLog(timeCalllog,
                                cursorCalllogs.getString(DatabaseUtil.Calllog.COLUMN_ACCOUNT),
                                cursorCalllogs.getString(DatabaseUtil.Calllog.COLUMN_ID),
                                cursorCalllogs.getLong(DatabaseUtil.Calllog.COLUMN_END_TIME),
                                cursorCalllogs.getInt(DatabaseUtil.Calllog.COLUMN_DURATION),
                                cursorCalllogs.getInt(DatabaseUtil.Calllog.COLUMN_STATUS),
                                cursorCalllogs.getInt(DatabaseUtil.Calllog.COLUMN_READ),
                                cursorCalllogs.getString(DatabaseUtil.Calllog.COLUMN_ACCOUNT_NAME),
                                DataUtil.getImage(cursorCalllogs.getBlob(DatabaseUtil.Calllog.COLUMN_ACCOUNT_ICON)),
                                cursorCalllogs.getString(DatabaseUtil.Calllog.COLUMN_ACCOUNT_COMMENT)));
                        cursorCalllogs.moveToNext();
                    } else if (timeCalllog < timeNotification) {
                        arrayList.add(new Notification(timeNotification,
                                cursorNotifications.getString(DatabaseUtil.Notification.COLUMN_ACCOUNT),
                                cursorNotifications.getString(DatabaseUtil.Notification.COLUMN_ID),
                                cursorNotifications.getInt(DatabaseUtil.Notification.COLUMN_REQUEST),
                                cursorNotifications.getInt(DatabaseUtil.Notification.COLUMN_READ),
                                cursorNotifications.getString(DatabaseUtil.Notification.COLUMN_ACCOUNT_NAME),
                                DataUtil.getImage(cursorNotifications.getBlob(DatabaseUtil.Notification.COLUMN_ACCOUNT_ICON)),
                                cursorNotifications.getString(DatabaseUtil.Notification.COLUMN_ACCOUNT_COMMENT)));
                        cursorNotifications.moveToNext();
                    } else {
                        arrayList.add(new CallLog(timeCalllog,
                                cursorCalllogs.getString(DatabaseUtil.Calllog.COLUMN_ACCOUNT),
                                cursorCalllogs.getString(DatabaseUtil.Calllog.COLUMN_ID),
                                cursorCalllogs.getLong(DatabaseUtil.Calllog.COLUMN_END_TIME),
                                cursorCalllogs.getInt(DatabaseUtil.Calllog.COLUMN_DURATION),
                                cursorCalllogs.getInt(DatabaseUtil.Calllog.COLUMN_STATUS),
                                cursorCalllogs.getInt(DatabaseUtil.Calllog.COLUMN_READ),
                                cursorCalllogs.getString(DatabaseUtil.Calllog.COLUMN_ACCOUNT_NAME),
                                DataUtil.getImage(cursorCalllogs.getBlob(DatabaseUtil.Calllog.COLUMN_ACCOUNT_ICON)),
                                cursorCalllogs.getString(DatabaseUtil.Calllog.COLUMN_ACCOUNT_COMMENT)));
                        cursorCalllogs.moveToNext();
                        arrayList.add(new Notification(timeNotification,
                                cursorNotifications.getString(DatabaseUtil.Notification.COLUMN_ACCOUNT),
                                cursorNotifications.getString(DatabaseUtil.Notification.COLUMN_ID),
                                cursorNotifications.getInt(DatabaseUtil.Notification.COLUMN_REQUEST),
                                cursorNotifications.getInt(DatabaseUtil.Notification.COLUMN_READ),
                                cursorNotifications.getString(DatabaseUtil.Notification.COLUMN_ACCOUNT_NAME),
                                DataUtil.getImage(cursorNotifications.getBlob(DatabaseUtil.Notification.COLUMN_ACCOUNT_ICON)),
                                cursorNotifications.getString(DatabaseUtil.Notification.COLUMN_ACCOUNT_NAME)));
                        cursorNotifications.moveToNext();
                    }
                }
            }
        }

        if (cursorCalllogs != null) cursorCalllogs.close();
        if (cursorNotifications != null) cursorNotifications.close();

        return arrayList;
    }

    public Uri saveFile(String name, int status) {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.File.NAME, name);
        values.put(DatabaseUtil.File.STATUS, status);
        return mContentResolver.insert(DatabaseProvider.FILES_URI, values);
    }

    public Cursor getFiles(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mContentResolver.query(DatabaseProvider.FILES_URI, projection, selection, selectionArgs, sortOrder);
    }

    private boolean loginSip(String account) {
        boolean result = false;
        String sip = "";

        Cursor cursor = getAccounts(null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            if (DataUtil.IS_APP) {
                sip = cursor.getString(DatabaseUtil.Account.COLUMN_SIP);
            } else {
                String id = cursor.getString(DatabaseUtil.Account.COLUMN_ID);
                if (!TextUtils.isEmpty(id)) {
                    Cursor c = getDevice(null, DatabaseUtil.Device.ID + " = ?", new String[]{id}, null);
                    if (c != null && c.moveToFirst()) {
                        sip = c.getString(DatabaseUtil.Device.COLUMN_SIP);
                    }
                    if (c != null) c.close();
                } else {
                    Log.d(TAG, "loginSip: device is null");
                }
            }
        } else {
            Log.d(TAG, "loginSip: account is null");
        }

        if (!TextUtils.isEmpty(sip) && LinphonePreferences.instance().getAccountCount() < 1) {
            try {
                AccountBuilder builder = new AccountBuilder(LinphoneManager.getLc())
                        .setUsername("303")
                        .setDomain(DataUtil.SIP_DOMAIN)
                        .setPassword("303")
                        .setTransport(TransportType.Udp);
                builder.saveNewAccount();
                result = true;
            } catch (CoreException e) {
                Log.e(TAG, "loginSip: CoreException is ", e);
            } catch (Exception e) {
                Log.e(TAG, "loginSip: Exception is ", e);
                result = mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_SIP), LOOP_SIP_TIME);
            }
        }

        if (cursor != null) cursor.close();
        return result;
    }

    private void logoutSip() {
        if (LinphonePreferences.instance().getAccountCount() > 0) {
            LinphonePreferences.instance().deleteAccount(0);
        }
    }

    private static final int PERMISSIONS_REQUEST_WRITE_CONTACTS = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_WRITE_CONTACTS){
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "[onRequestPermissionsResult]Contacts permission success.", Toast.LENGTH_SHORT).show();
                    syncContacts(null);
                } else {
                    Toast.makeText(this, "[onRequestPermissionsResult]Contacts permission fail.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public static boolean checkResult(Message msg) {
        boolean result = false;
        try {
            JSONObject obj = (JSONObject) msg.obj;
            if (DataThread.RESULT_OK.equals(obj.getString(DataThread.RESULT_CODE))) {
                result = true;
            } else {
                Log.d(TAG, "checkResult: obj is " + obj);
            }
        } catch (JSONException e) {
            Log.e(TAG, "checkResult: JSONException is ", e);
        }
        return result;
    }
    /* ===end=== */
}
