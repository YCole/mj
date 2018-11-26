package com.gome.beautymirror.data;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
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

import com.gome.beautymirror.activities.BeautyMirrorActivity;
import com.gome.beautymirror.contacts.ContactsManager;
import com.gome.beautymirror.data.CallLog;
import com.gome.beautymirror.data.DataUtil;
import com.gome.beautymirror.data.DataThread;
import com.gome.beautymirror.data.Information;
import com.gome.beautymirror.data.provider.DatabaseProvider;
import com.gome.beautymirror.data.provider.DatabaseUtil;
import com.gome.beautymirror.LinphoneManager;
import com.gome.beautymirror.LinphonePreferences;
import com.gome.beautymirror.LinphonePreferences.AccountBuilder;
import com.gome.beautymirror.R;

import cole.utils.SaveUtils;

import org.linphone.core.TransportType;
import org.linphone.core.CoreException;

public class DataService extends Service {

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
    private static final int MESSAGE_ACCOUNT = 3;
    private static final int MESSAGE_PEOPLE = 4;
    private static final int MESSAGE_BROADCAST_FRIEND = 5;
    private static final int MESSAGE_BROADCAST_PEOPLE = 6;
    private static final int MESSAGE_BROADCAST_PROPOSER = 7;
    private static final int MESSAGE_BROADCAST_INFORMATION = 8;

    private static DataService instance;

    private DataServiceBinder mBinder = new DataServiceBinder();
    private DataServiceHandler mHandler = new DataServiceHandler();
    private TelephonyManager mTelephonyManager = null;
    private NotificationManager mNotificationManager = null;
    private ContentResolver mContentResolver = null;

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

                case MESSAGE_SIP:
                    result = loginSip(null);
                    break;

                case MESSAGE_ACCOUNT:
                    result = checkDialog();
                    break;

                case MESSAGE_PEOPLE:
                    String account = (String) msg.obj;
                    int count = updatePeople(account, matchFriend(account));
                    if (count > 0) {
                        sendMsgDelayed(MESSAGE_BROADCAST_PEOPLE, 0);
                    }
                    result = true;
                    break;

                case MESSAGE_BROADCAST_FRIEND:
                    sendBroadcast(new Intent(DataUtil.BROADCAST_FRIEND));
                    result = true;
                    break;

                case MESSAGE_BROADCAST_PEOPLE:
                    sendBroadcast(new Intent(DataUtil.BROADCAST_PEOPLE));
                    result = true;
                    break;

                case MESSAGE_BROADCAST_PROPOSER:
                    sendBroadcast(new Intent(DataUtil.BROADCAST_PROPOSER));
                    result = true;
                    break;

                case MESSAGE_BROADCAST_INFORMATION:
                    sendBroadcast(new Intent(DataUtil.BROADCAST_INFORMATION));
                    result = true;
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

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        registerReceiver(mBroadcastReceiver, filter);

        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
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

        unregisterReceiver(mBroadcastReceiver);

        super.onDestroy();
    }

    private boolean sendMsgDelayed(int msg, int delayed) {
        Log.d(TAG, "sendMsgDelayed: send " + msg + " after " + delayed);
        mHandler.removeMessages(msg);
        return mHandler.sendEmptyMessageDelayed(msg, delayed);
    }

    public boolean initialise(Activity activity) {
        boolean result = false;
        Log.d(TAG, "initialise: start");

        result = syncContacts(activity);

        result = loginSip(null);

        result = sync();

        return result;
    }

    private boolean sync() {
        boolean result = false;
        Log.d(TAG, "sync: start");
        checkAccount();
        checkPeople(null, null);
        if (!TextUtils.isEmpty(getAccount(null))) {
            result = sendMsgDelayed(MESSAGE_SYNC, LOOP_SYNC_TIME);
        }
        return result;
    }

    private String getAccount(String account) {
        if (TextUtils.isEmpty(account)) {
            Cursor cursor = getAccounts(null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                account = cursor.getString(DatabaseUtil.Account.COLUMN_ACCOUNT);
            } else {
                Log.d(TAG, "getAccount: account is null");
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

    @SuppressLint("SimpleDateFormat")
    public boolean loginAccount(Activity activity, final String account, final String password, String id, final Handler handler, final int what) {
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password) || (!DataUtil.IS_APP && TextUtils.isEmpty(id))) {
            Log.d(TAG, "loginAccount: return");
            return false;
        } else if (checkAndRequestPermission(activity, Manifest.permission.READ_PHONE_STATE, PERMISSIONS_REQUEST_READ_PHONE_STATE)) {
            return false;
        }

        final String info = System.currentTimeMillis() + "";//DataUtil.getMD5(mTelephonyManager.getDeviceId());
        Handler h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    if (obj.getString(DataThread.RESULT_CODE).equals(DataThread.RESULT_OK)) {
                        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(obj.getString("updateTime"));
                        String deviceNo = obj.optString("deviceNo");
                        Uri uri = saveAccount(account, password,
                                 obj.optString("name"), DataUtil.getImage(obj.optString("icon")),
                                 obj.getString("sip"), date.getTime(),
                                 info, deviceNo);
                        if (uri == null) {
                            obj = DataThread.setJSONObject(DataThread.RESULT_ERROR, "account is error");
                        } else {
                            SaveUtils.writeUser(DataService.this, "account", account);
                            if (!TextUtils.isEmpty(deviceNo)) {
                                uri = saveDevice(deviceNo, null, obj.getString("deviceSip"));
                                if (uri != null) checkDevice(account);
                            }
                        }
                        boolean result = initialise(null);
                        result = sendMsgDelayed(MESSAGE_ACCOUNT, 1000);
                    } else {
                        Log.d(TAG, "loginAccount: obj is " + obj);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "loginAccount: JSONException is ", e);
                    obj = DataThread.setJSONObject(DataThread.RESULT_ERROR, e.toString());
                } catch (ParseException e) {
                    Log.e(TAG, "loginAccount: ParseException is ", e);
                    obj = DataThread.setJSONObject(DataThread.RESULT_ERROR, e.toString());
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
        return true;
    }

    private Uri saveAccount(String account, String password, String name, Bitmap icon, String sip, Long time, String info, String id) {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.Account._ID, 0);
        values.put(DatabaseUtil.Account.ACCOUNT, account);
        values.put(DatabaseUtil.Account.PASSWORD, password);
        values.put(DatabaseUtil.Account.NAME, name);
        values.put(DatabaseUtil.Account.ICON, DataUtil.getImage(icon));
        values.put(DatabaseUtil.Account.SIP, sip);
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
                                } else {
                                    int count = updateDevice(deviceNo, obj.getString("deviceSip"));
                                }
                                checkDevice(account);
                            } else {
                                if (!TextUtils.isEmpty(id)) {
                                    int count = updateAccountAndDevice(account, null);
                                    count = deleteDevice(id);
                                }
                            }
                            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(obj.getString("updateTime"));
                            if (!info.equals(obj.getString("loginInfo"))) {
                                int count = logoutAccount(account);
                                if (count > 0) {
                                    boolean result = showDialog("Warn", "Account is abnormal.");
                                } else {
                                    Log.d(TAG, "checkAccount: logoutAccount fail");
                                }
                            } else if (date.getTime() != time) {
                                syncAccount(account);
                                checkFriend();
                            } else {
                                Log.d(TAG, "checkAccount: not changed");
                                checkFriend();
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
    public void updateAccount(String account, final String name, final Bitmap icon, final Handler handler, final int what) {
        Cursor cursor = getAccounts(null, TextUtils.isEmpty(account) ? null : DatabaseUtil.Account.ACCOUNT + " = " + account, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            final String a = cursor.getString(DatabaseUtil.Account.COLUMN_ACCOUNT);
            Handler h = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    JSONObject obj = (JSONObject) msg.obj;
                    try {
                        if (DataThread.RESULT_OK.equals(obj.getString(DataThread.RESULT_CODE))) {
                            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(obj.getString("updateTime"));
                            int count = updateAccount(a, name, icon, date.getTime());
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
            params.put("number", a);
            params.put("password", cursor.getString(DatabaseUtil.Account.COLUMN_PASSWORD));
            params.put("name", name);
            new DataThread(DataUtil.ACCOUNT_URL + "updateAccount", params, a, icon,
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

    public boolean matchAccount(String account) {
        boolean result = false;
        Cursor cursor = getAccounts(null, DatabaseUtil.Account.ACCOUNT + " = ?", new String[]{account}, null);
        if (cursor != null && cursor.getCount() > 0) {
            result = true;
        }
        if (cursor != null) cursor.close();
        return result;
    }

    public int logoutAccount(String account) {
        account = getAccount(account);
        if (!TextUtils.isEmpty(account)) {
            logoutSip();
            SaveUtils.writeUser(this, "account", "");
            int count = mContentResolver.delete(Uri.withAppendedPath(DatabaseProvider.ACCOUNT_URI, account), null, null);
            if (count > 0) {
                deleteDevices(null, null);
                deleteFriends(null, null);
                deleteFriendDevices(null, null);
                deletePeoples(null, null);
                deleteProposers(null, null);
                deleteCalllogs(null, null);
                deleteInformations(null, null);
                deleteFiles(null, null);
                mNotificationManager.cancelAll();
            }
            return count;
        } else {
            Log.d(TAG, "logoutAccount: account is null");
            return 0;
        }
    }

    public boolean updatePassword(String account, String password, final String newPassword, final Handler handler, final int what) {
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(newPassword)) {
            return false;
        }
        Cursor cursor = getAccounts(null, TextUtils.isEmpty(account) ? null : DatabaseUtil.Account.ACCOUNT + " = " + account, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            final String a = cursor.getString(DatabaseUtil.Account.COLUMN_ACCOUNT);
            String p = cursor.getString(DatabaseUtil.Account.COLUMN_PASSWORD);
            if (p != null && p.equals(password)) {
                Handler h = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        JSONObject obj = (JSONObject) msg.obj;
                        try {
                            if (DataThread.RESULT_OK.equals(obj.getString(DataThread.RESULT_CODE))) {
                                int count = updatePassword(a, newPassword);
                            } else {
                                Log.d(TAG, "updatePassword: obj is " + obj);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "updatePassword: JSONException is ", e);
                        }
                        if (handler != null) {
                            handler.obtainMessage(what, obj).sendToTarget();
                        } else {
                            Log.d(TAG, "updatePassword: handler is null");
                        }
                    }
                };
                final Map<String, String> params = new HashMap<String, String>();
                params.put("number", a);
                params.put("password", password);
                params.put("newPassword", newPassword);
                new DataThread(DataUtil.ACCOUNT_URL + "updatePassword", params,
                        h.obtainMessage(0)).start();
            } else {
                Log.d(TAG, "updatePassword: password is error");
            }
        } else {
            Log.d(TAG, "updatePassword: account is null");
        }
        if (cursor != null) cursor.close();
        return true;
    }

    private int updatePassword(String account, String password) {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.Account.PASSWORD, password);
        return mContentResolver.update(DatabaseProvider.ACCOUNT_URI, values, DatabaseUtil.Account.ACCOUNT + " = ?", new String[]{account});
    }

    public void requestReset(String account, Handler handler, int what) {
        account = getAccount(account);
        final Map<String, String> params = new HashMap<String, String>();
        params.put("number", account);
        params.put("smsType", "2");
        new DataThread(DataUtil.ACCOUNT_URL + "getSmsCode", params,
                handler == null ? null : handler.obtainMessage(what)).start();
    }

    public void resetAccount(String account, String code, String password, Handler handler, int what) {
        account = getAccount(account);
        final Map<String, String> params = new HashMap<String, String>();
        params.put("number", account);
        params.put("smsType", "2");
        params.put("password", password);
        params.put("smsCode", code);
        new DataThread(DataUtil.ACCOUNT_URL + "findPassword", params,
                handler == null ? null : handler.obtainMessage(what)).start();
    }

    public void deleteAccount(String account, Handler handler, int what) {
        account = getAccount(account);
        int count = logoutAccount(account);
        final Map<String, String> params = new HashMap<String, String>();
        params.put("number", account);
        new DataThread(DataUtil.ACCOUNT_URL + "delAccount", params,
                handler == null ? null : handler.obtainMessage(what)).start();
    }

    public String getDevice(String id) {
        if (TextUtils.isEmpty(id)) {
            Cursor cursor = getAccounts(null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                id = cursor.getString(DatabaseUtil.Account.COLUMN_ID);
            } else {
                Log.d(TAG, "getDevice: id is null");
            }
            if (cursor != null) cursor.close();
        }
        return id;
    }

    private String mId;
    private String mKey;

    public boolean requestKey(String id, final Handler handler, final int what) {
        if (!TextUtils.isEmpty(getDevice(null))) {
            showToast("Device is exist", Toast.LENGTH_SHORT);
            return false;
        } else if (TextUtils.isEmpty(id)) {
            Log.d(TAG, "requestKey: return");
            return false;
        }
        mId = id;
        Handler h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    if (DataThread.RESULT_OK.equals(obj.getString(DataThread.RESULT_CODE))) {
                        mKey = obj.getString("deviceKey");
                    } else {
                        Log.d(TAG, "requestKey: obj is " + obj);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "requestKey: JSONException is ", e);
                }
                if (handler != null) {
                    handler.obtainMessage(what, obj).sendToTarget();
                } else {
                    Log.d(TAG, "requestKey: handler is null");
                }
            }
        };
        final Map<String, String> params = new HashMap<String, String>();
        params.put("deviceId", id);
        new DataThread(DataUtil.DEVICE_URL + "reqDeviceKey", params,
                h.obtainMessage(0)).start();
        return true;
    }

    public boolean bindDevice(String account, String id, String key, final String name, final Handler handler, final int what) {
        final String a = getAccount(account);
        if (TextUtils.isEmpty(id)) id = mId;
        mId = null;
        final String d = id;
        if (TextUtils.isEmpty(key)) key = mKey;
        mKey = null;
        if (!TextUtils.isEmpty(getDevice(null))) {
            showToast("Device is exist", Toast.LENGTH_SHORT);
            return false;
        } else if (TextUtils.isEmpty(a) || TextUtils.isEmpty(d) || TextUtils.isEmpty(key)) {
            Log.d(TAG, "bindDevice: return");
            return false;
        }
        Handler h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    if (DataThread.RESULT_OK.equals(obj.getString(DataThread.RESULT_CODE))) {
                        int count = updateAccountAndDevice(a, d);
                        Uri uri = saveDevice(d, name, obj.optString("sipNo"));
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
        params.put("deviceId", d);
        params.put("deviceKey", key);
        if (!TextUtils.isEmpty(name)) {
            params.put("deviceName", name);
        }
        new DataThread(DataUtil.DEVICE_URL + "bindMagic", params,
                h.obtainMessage(0)).start();
        return true;
    }

    private Uri saveDevice(String id, String name, String sip) {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.Device.ID, id);
        values.put(DatabaseUtil.Device.TYPE, DatabaseUtil.Device.TYPE_MIRROR);
        values.put(DatabaseUtil.Device.PERMISSION, DatabaseUtil.Device.PERMISSION_PUBLIC);
        values.put(DatabaseUtil.Device.DEVICE_NAME, name);
        values.put(DatabaseUtil.Device.DEVICE_SIP, sip);
        values.put(DatabaseUtil.Device.DEVICE_TIME, 0L);
        return mContentResolver.insert(DatabaseProvider.DEVICES_URI, values);
    }

    private void checkDevice(String account) {
        Cursor cursor = getAccounts(null, TextUtils.isEmpty(account) ? null : DatabaseUtil.Account.ACCOUNT + " = " + account, null, null);
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
                                String name = obj.optString("deviceName");
                                int count = updateDevice(id, (TextUtils.isEmpty(name) || EMPTY.equals(name)) ? null : name, 0L);
                                if (obj.getInt("deviceStatus") == DEVICE_UNBIND) {
                                    showNotification(NOTIF_DEVICE_UNBIND, "Notification", "Ready to unbind.", null, new Intent(DataService.this, BeautyMirrorActivity.class));
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

    @SuppressLint("SimpleDateFormat")
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
                                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(obj.getString("updateTime"));
                                int count = updateDevice(d, name, date.getTime());
                            } else {
                                Log.d(TAG, "updateDevice: obj is " + obj);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "updateDevice: JSONException is ", e);
                        } catch (ParseException e) {
                            Log.e(TAG, "updateDevice: ParseException is ", e);
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

    private int updateDevice(String id, String name, Long time) {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.Device.PERMISSION, DatabaseUtil.Device.PERMISSION_PUBLIC);
        values.put(DatabaseUtil.Device.DEVICE_NAME, name);
        values.put(DatabaseUtil.Device.DEVICE_TIME, time);
        return mContentResolver.update(DatabaseProvider.DEVICES_URI, values, DatabaseUtil.Device.ID + " = ?", new String[]{id});
    }

    private int updateDevice(String id, String sip) {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.Device.TYPE, DatabaseUtil.Device.TYPE_MIRROR);
        values.put(DatabaseUtil.Device.DEVICE_SIP, sip);
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

    private Cursor getDevices(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mContentResolver.query(DatabaseProvider.DEVICES_URI, projection, selection, selectionArgs, sortOrder);
    }

    private int deleteDevices(String selection, String[] selectionArgs) {
        return mContentResolver.delete(DatabaseProvider.DEVICES_URI, selection, selectionArgs);
    }

    private int deleteDevice(String id) {
        return deleteDevices(DatabaseUtil.Device.ID + " = ?", new String[]{id});
    }

    public Cursor getAccountsAndDevices(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mContentResolver.query(DatabaseProvider.ACCOUNT_VIEW_URI, projection, selection, selectionArgs, sortOrder);
    }

    public Cursor getAccountForSip(String sip) {
        return getAccountsAndDevices(null,
                DatabaseUtil.Account.SIP + " = ? or " + DatabaseUtil.Account.DEVICE_SIP + " = ?",
                new String[]{sip, sip},
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
                                        int count = 0;
                                        if (requestTime != cursor.getLong(DatabaseUtil.Proposer.COLUMN_REQUEST_TIME)) {
                                            count = updateProposer(account, requestTime, message, DatabaseUtil.Proposer.STATUS_NEW);
                                            if (count > 0) showNotification(NOTIF_FRIEND_REQUEST, "Notification",
                                                    "New request for " + account,
                                                    ContactsManager.getInstance().getDefaultAvatarBitmap(),
                                                    new Intent(DataService.this, BeautyMirrorActivity.class));
                                        }
                                        if (count > 0 || time != cursor.getLong(DatabaseUtil.Proposer.COLUMN_TIME)) {
                                            syncProposer(account, time, count > 0);
                                        }
                                    } else {
                                        Uri uri = saveProposer(account,
                                                null,
                                                null,
                                                0L,
                                                requestTime,
                                                message,
                                                DatabaseUtil.Proposer.STATUS_NEW);
                                        if (uri != null) {
                                            showNotification(NOTIF_FRIEND_REQUEST, "Notification",
                                                    "New request for " + account,
                                                    ContactsManager.getInstance().getDefaultAvatarBitmap(),
                                                    new Intent(DataService.this, BeautyMirrorActivity.class));
                                            syncProposer(account, time, true);
                                        }
                                    }
                                    if (cursor != null) cursor.close();
                                    sendMsgDelayed(MESSAGE_BROADCAST_PROPOSER, 0);
                                } else {
                                    String id = data.optString("deviceNo");
                                    if (friends.size() > 0 && friends.get(account) != null) {
                                        Friend friend = friends.get(account);
                                        if (!TextUtils.isEmpty(id)) {
                                            if (!id.equals(friend.getId())) {
                                                int count = updateFriendAndDevice(account, id);
                                                Uri uri = saveFriendDevice(id, null, data.optString("deviceSip"));
                                            } else {
                                                int count = updateFriendDevice(id, null, data.optString("deviceSip"));
                                            }
                                        } else {
                                            if (!TextUtils.isEmpty(id)) {
                                                int count = updateFriendAndDevice(account, null);
                                                count = deleteFriendDevice(friend.getId());
                                            }
                                        }
                                        if (time != friend.getTime()) {
                                            syncFriend(account, false);
                                        }
                                        friends.remove(account);
                                    } else {
                                        Uri uri = saveFriend(account,
                                                null,
                                                null,
                                                0L,
                                                TextUtils.isEmpty(id) ? null : id,
                                                data.optString("friendComment"),
                                                data.getString("accountSip"));
                                        if (!TextUtils.isEmpty(id)) uri = saveFriendDevice(id, null, data.optString("deviceSip"));
                                        int count = updateProposer(account, DatabaseUtil.Proposer.STATUS_FRIEND);
                                        if (count > 0) sendMsgDelayed(MESSAGE_BROADCAST_PROPOSER, 0);
                                        count = updatePeople(account, DatabaseUtil.People.STATUS_FRIEND);
                                        count = updateInformation(account, DatabaseUtil.Information.REQUEST_CONFIRMED);
                                        if (count > 0) {
                                            sendMsgDelayed(MESSAGE_BROADCAST_INFORMATION, 0);
                                        }
                                        if (uri != null) {
                                            if (count > 0) showNotification(NOTIF_FRIEND_CONFIRMED, "Notification",
                                                    "New confirmed for " + account,
                                                    ContactsManager.getInstance().getDefaultAvatarBitmap(),
                                                    new Intent(DataService.this, BeautyMirrorActivity.class));
                                            syncFriend(account, count > 0);
                                        }
                                    }
                                    sendMsgDelayed(MESSAGE_BROADCAST_FRIEND, 0);
                                }
                            }
                            for (String account : friends.keySet()) {
                                int count = deleteFriend(account);
                                if (count > 0) {
                                    sendMsgDelayed(MESSAGE_BROADCAST_FRIEND, 0);
                                    showNotification(NOTIF_FRIEND_DELETED, "Notification",
                                            "Deleted for " + account,
                                            ContactsManager.getInstance().getDefaultAvatarBitmap(),
                                            new Intent(DataService.this, BeautyMirrorActivity.class));
                                }
                                count = updatePeople(account, DatabaseUtil.People.STATUS_UNKNOW);
                                count = deleteProposer(account);
                                if (count > 0) sendMsgDelayed(MESSAGE_BROADCAST_PROPOSER, 0);
                                count = deleteCalllog(account);
                                count = deleteInformation(account);
                                if (count > 0) sendMsgDelayed(MESSAGE_BROADCAST_INFORMATION, 0);
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
    private void syncFriend(final String account, final boolean show) {
        Handler h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                try {
                    JSONObject obj = (JSONObject) msg.obj;
                    if (DataThread.RESULT_OK.equals(obj.getString(DataThread.RESULT_CODE))) {
                        String name = obj.optString("name");
                        Bitmap icon = DataUtil.getImage(obj.optString("icon"));
                        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(obj.getString("updateTime"));
                        int count = updateFriend(account, (TextUtils.isEmpty(name) || EMPTY.equals(name)) ? null : name,
                                icon, date.getTime());
                        if (count > 0) sendMsgDelayed(MESSAGE_BROADCAST_FRIEND, 0);
                        count = updateProposer(account, (TextUtils.isEmpty(name) || EMPTY.equals(name)) ? null : name,
                                icon, date.getTime());
                        if (count > 0) sendMsgDelayed(MESSAGE_BROADCAST_PROPOSER, 0);
                        count = updatePeople(account, (TextUtils.isEmpty(name) || EMPTY.equals(name)) ? null : name,
                                icon, date.getTime());
                        if (show) showNotification(NOTIF_FRIEND_CONFIRMED, "Notification",
                                "New confirmed for " + (TextUtils.isEmpty(name) || EMPTY.equals(name) ? account : name),
                                icon != null ? icon : ContactsManager.getInstance().getDefaultAvatarBitmap(),
                                new Intent(DataService.this, BeautyMirrorActivity.class));
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
        values.put(DatabaseUtil.Friend.SIP, sip);
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

    public boolean updateFriend(String account, final String friendAccount, final String comment, final Handler handler, final int what) {
        account = getAccount(account);
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(friendAccount)) {
            return false;
        }
        Handler h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    if (DataThread.RESULT_OK.equals(obj.getString(DataThread.RESULT_CODE))) {
                        int count = updateFriend(friendAccount, comment);
                    } else {
                        Log.d(TAG, "updateFriend: obj is " + obj);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "updateFriend: JSONException is ", e);
                }
                if (handler != null) {
                    handler.obtainMessage(what, obj).sendToTarget();
                } else {
                    Log.d(TAG, "updateFriend: handler is null");
                }
            }
        };
        final Map<String, String> params = new HashMap<String, String>();
        params.put("number", account);
        params.put("friendNumber", friendAccount);
        params.put("friendComment", comment);
        new DataThread(DataUtil.FRIEND_URL + "updateFriend", params,
                h.obtainMessage(0)).start();
        return true;
    }

    private int updateFriend(String account, String comment) {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.Friend.COMMENT, comment);
        return mContentResolver.update(DatabaseProvider.FRIENDS_URI, values, DatabaseUtil.Friend.ACCOUNT + " = ?", new String[]{account});
    }

    private Cursor getFriends(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mContentResolver.query(DatabaseProvider.FRIENDS_URI, projection, selection, selectionArgs, sortOrder);
    }

    public void deleteFriend(String account, final String friendAccount, final Handler handler, final int what) {
        account = getAccount(account);
        Handler h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    if (DataThread.RESULT_OK.equals(obj.getString(DataThread.RESULT_CODE))) {
                        int count = deleteFriend(friendAccount);
                        if (count > 0) sendMsgDelayed(MESSAGE_BROADCAST_FRIEND, 0);
                        count = updatePeople(friendAccount, DatabaseUtil.People.STATUS_UNKNOW);
                        count = deleteProposer(friendAccount);
                        if (count > 0) sendMsgDelayed(MESSAGE_BROADCAST_PROPOSER, 0);
                        count = deleteCalllog(friendAccount);
                        count = deleteInformation(friendAccount);
                        if (count > 0) sendMsgDelayed(MESSAGE_BROADCAST_INFORMATION, 0);
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

    private int deleteFriends(String selection, String[] selectionArgs) {
        return mContentResolver.delete(DatabaseProvider.FRIENDS_URI, selection, selectionArgs);
    }

    private int deleteFriend(String account) {
        return deleteFriends(DatabaseUtil.Friend.ACCOUNT + " = ?", new String[]{account});
    }

    private Uri saveFriendDevice(String id, String name, String sip) {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.FriendDevice.ID, id);
        values.put(DatabaseUtil.FriendDevice.TYPE, DatabaseUtil.Device.TYPE_MIRROR);
        values.put(DatabaseUtil.FriendDevice.DEVICE_NAME, name);
        values.put(DatabaseUtil.FriendDevice.DEVICE_SIP, sip);
        values.put(DatabaseUtil.FriendDevice.DEVICE_TIME, 0L);
        return mContentResolver.insert(DatabaseProvider.FRIENDDEVICES_URI, values);
    }

    private int updateFriendDevice(String id, String name, String sip) {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.FriendDevice.TYPE, DatabaseUtil.Device.TYPE_MIRROR);
        values.put(DatabaseUtil.FriendDevice.DEVICE_NAME, name);
        values.put(DatabaseUtil.FriendDevice.DEVICE_SIP, sip);
        values.put(DatabaseUtil.FriendDevice.DEVICE_TIME, 0L);
        return mContentResolver.update(DatabaseProvider.FRIENDDEVICES_URI, values, DatabaseUtil.FriendDevice.ID + " = ?", new String[]{id});
    }

    private Cursor getFriendDevices(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mContentResolver.query(DatabaseProvider.FRIENDDEVICES_URI, projection, selection, selectionArgs, sortOrder);
    }

    private int deleteFriendDevices(String selection, String[] selectionArgs) {
        return mContentResolver.delete(DatabaseProvider.FRIENDDEVICES_URI, selection, selectionArgs);
    }

    private int deleteFriendDevice(String id) {
        return deleteFriendDevices(DatabaseUtil.FriendDevice.ID + " = ?", new String[]{id});
    }

    public Cursor getFriendsAndDevices(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mContentResolver.query(DatabaseProvider.FRIENDS_VIEW_URI, projection, selection, selectionArgs, sortOrder);
    }

    public Cursor getFriendForSip(String sip) {
        Cursor cursor = getFriendsAndDevices(null,
                DatabaseUtil.Friend.SIP + " = ? or " + DatabaseUtil.Friend.DEVICE_SIP + " = ?",
                new String[]{sip, sip},
                null);
        if (cursor == null || cursor.getCount() < 1) {
            checkFriend();
        }
        return cursor;
    }
    /* } */

    public void queryPeople(String number, final Handler handler, final int what) {
        Handler h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    if (DataThread.RESULT_OK.equals(obj.getString(DataThread.RESULT_CODE))) {
                        String name = obj.getString("contact_name");
                        int count = updatePeople(obj.getString("contact_account"),
                                (TextUtils.isEmpty(name) || EMPTY.equals(name)) ? null : name,
                                DataUtil.getImage(obj.optString("contact_icon")), 0L);
                        count = updateFriend(obj.getString("contact_account"),
                                (TextUtils.isEmpty(name) || EMPTY.equals(name)) ? null : name,
                                DataUtil.getImage(obj.optString("contact_icon")), 0L);
                        if (count > 0) sendMsgDelayed(MESSAGE_BROADCAST_FRIEND, 0);
                        count = updateProposer(obj.getString("contact_account"),
                                (TextUtils.isEmpty(name) || EMPTY.equals(name)) ? null : name,
                                DataUtil.getImage(obj.optString("contact_icon")), 0L);
                        if (count > 0) sendMsgDelayed(MESSAGE_BROADCAST_PROPOSER, 0);
                    } else {
                        Log.d(TAG, "queryPeople: obj is " + obj);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "queryPeople: JSONException is ", e);
                }
                if (handler != null) {
                    handler.obtainMessage(what, obj).sendToTarget();
                } else {
                    Log.d(TAG, "queryPeople: handler is null");
                }
            }
        };
        final Map<String, String> params = new HashMap<String, String>();
        params.put("contactNumber", number);
        new DataThread(DataUtil.FRIEND_URL + "findFriendAccount", params,
                h.obtainMessage(0)).start();
    }

    private void checkPeople(Cursor cursor, String exception) {
        if (cursor == null) {
            cursor = getPeoples(null, null, null, null);
        } else {
            Log.d(TAG, "checkPeople: cursor is " + cursor);
        }
        exception = getAccount(exception);
        if (cursor != null && cursor.moveToNext()) {
            syncPeople(cursor, exception);
        } else {
            Log.d(TAG, "checkPeople: people is null");
            if (cursor != null) cursor.close();
        }
    }

    private int matchFriend(String account) {
        int status = DatabaseUtil.People.STATUS_DEFAULT;
        Cursor cursor = getFriends(null, DatabaseUtil.Friend.ACCOUNT + " = " + account, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            status = DatabaseUtil.People.STATUS_FRIEND;
        } else {
            status = DatabaseUtil.People.STATUS_UNKNOW;
        }
        if (cursor != null) cursor.close();
        return status;
    }

    private void syncPeople(final Cursor cursor, final String exception) {
        final int contactId = cursor.getInt(DatabaseUtil.People.COLUMN_CONTACT_ID);
        String number = cursor.getString(DatabaseUtil.People.COLUMN_NUMBER);
        if (!TextUtils.isEmpty(number)) {
            String[] numberArray = number.split(",");
            number = numberArray[0];
        }
        if (!TextUtils.isEmpty(number) && !number.equals(exception)
                && cursor.getInt(DatabaseUtil.People.COLUMN_STATUS) != DatabaseUtil.People.STATUS_FRIEND) {
            Handler h = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    try {
                        JSONObject obj = (JSONObject) msg.obj;
                        if (DataThread.RESULT_OK.equals(obj.getString(DataThread.RESULT_CODE))) {
                            String name = obj.getString("contact_name");
                            int count = updatePeople(contactId, obj.getString("contact_account"),
                                    (TextUtils.isEmpty(name) || EMPTY.equals(name)) ? null : name,
                                    DataUtil.getImage(obj.optString("contact_icon")), 0L);
                            count = updateProposer(obj.getString("contact_account"),
                                    (TextUtils.isEmpty(name) || EMPTY.equals(name)) ? null : name,
                                    DataUtil.getImage(obj.optString("contact_icon")), 0L);
                            if (count > 0) sendMsgDelayed(MESSAGE_BROADCAST_PROPOSER, 0);
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
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.People.ACCOUNT, account);
        values.put(DatabaseUtil.People.NAME, name);
        values.put(DatabaseUtil.People.ICON, DataUtil.getImage(icon));
        values.put(DatabaseUtil.People.TIME, time);
        values.put(DatabaseUtil.People.STATUS, matchFriend(account));
        return mContentResolver.update(DatabaseProvider.PEOPLES_URI, values, DatabaseUtil.People.CONTACT_ID + " = " + id, null);
    }

    private int updatePeople(String account, String name, Bitmap icon, Long time) {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.People.NAME, name);
        values.put(DatabaseUtil.People.ICON, DataUtil.getImage(icon));
        values.put(DatabaseUtil.People.TIME, time);
        return mContentResolver.update(DatabaseProvider.PEOPLES_URI, values, DatabaseUtil.People.ACCOUNT + " = " + account, null);
    }

    private int updatePeople(String account, int status) {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.People.STATUS, status);
        return mContentResolver.update(DatabaseProvider.PEOPLES_URI, values, DatabaseUtil.People.ACCOUNT + " = " + account, null);
    }

    public Cursor getPeoples(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mContentResolver.query(DatabaseProvider.PEOPLES_URI, projection, selection, selectionArgs, sortOrder);
    }

    private int deletePeoples(String selection, String[] selectionArgs) {
        return mContentResolver.delete(DatabaseProvider.PEOPLES_URI, selection, selectionArgs);
    }

    public boolean syncContacts(Activity activity) {
        if (checkAndRequestPermission(activity, Manifest.permission.READ_CONTACTS , PERMISSIONS_REQUEST_READ_CONTACTS)) {
            return false;
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

        return false;
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

    public boolean requestFriend(String account, final String number, String message, final Handler handler, final int what) {
        account = getAccount(account);
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(number) || account.equals(number)) {
            Log.d(TAG, "requestFriend: return");
            return false;
        }
        Handler h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    if (DataThread.RESULT_OK.equals(obj.getString(DataThread.RESULT_CODE))) {
                        int count = updatePeople(number, DatabaseUtil.People.STATUS_REQUEST);
                        if (count > 0) {
                            sendMsgDelayed(MESSAGE_BROADCAST_PEOPLE, 0);
                            mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_PEOPLE, number), 60 * 1000);
                        }
                        Uri uri = logInformation(number, DatabaseUtil.Information.REQUEST_FRIEND);
                        if (uri != null) sendMsgDelayed(MESSAGE_BROADCAST_INFORMATION, 0);
                    } else if ("444".equals(obj.getString(DataThread.RESULT_CODE))) {
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
        return true;
    }

    private void syncProposer(final String account, final Long time, final boolean show) {
        Handler h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                try {
                    JSONObject obj = (JSONObject) msg.obj;
                    if (DataThread.RESULT_OK.equals(obj.getString(DataThread.RESULT_CODE))) {
                        String name = obj.optString("friendName");
                        Bitmap icon = DataUtil.getImage(obj.optString("friendIcon"));
                        int count = updateProposer(account, (TextUtils.isEmpty(name) || EMPTY.equals(name)) ? null : name, icon, time);
                        if (count > 0) sendMsgDelayed(MESSAGE_BROADCAST_PROPOSER, 0);
                        if (show) showNotification(NOTIF_FRIEND_REQUEST, "Notification",
                                "New request for " + (TextUtils.isEmpty(name) || EMPTY.equals(name) ? account : name),
                                icon != null ? icon : ContactsManager.getInstance().getDefaultAvatarBitmap(),
                                new Intent(DataService.this, BeautyMirrorActivity.class));
                    } else {
                        Log.d(TAG, "syncProposer: obj is " + obj);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "syncProposer: JSONException is ", e);
                }
            }
        };
        final Map<String, String> params = new HashMap<String, String>();
        params.put("friendNumber", account);
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
        values.put(DatabaseUtil.Proposer.READ, DatabaseUtil.Proposer.READ_NEW);
        return mContentResolver.insert(DatabaseProvider.PROPOSERS_URI, values);
    }

    public Cursor getProposers(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mContentResolver.query(DatabaseProvider.PROPOSERS_URI, projection, selection, selectionArgs, sortOrder);
    }

    private int updateProposer(String account, Long request_time, String message, int status) {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.Proposer.REQUEST_TIME, request_time);
        values.put(DatabaseUtil.Proposer.MESSAGE, message);
        values.put(DatabaseUtil.Proposer.STATUS, status);
        values.put(DatabaseUtil.Proposer.READ, DatabaseUtil.Proposer.READ_NEW);
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

    public int readProposer() {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.Proposer.READ, DatabaseUtil.Proposer.READ_OLD);
        return mContentResolver.update(DatabaseProvider.PROPOSERS_URI, values, DatabaseUtil.Proposer.READ + " = ?", new String[]{DatabaseUtil.Proposer.READ_NEW + ""});
    }

    private int deleteProposers(String selection, String[] selectionArgs) {
        return mContentResolver.delete(DatabaseProvider.PROPOSERS_URI, selection, selectionArgs);
    }

    private int deleteProposer(String account) {
        return deleteProposers(DatabaseUtil.Proposer.ACCOUNT + " = ?", new String[]{account});
    }

    public void confirmProposer(String account, final String friendAccount, final int request, final Handler handler, final int what) {
        account = getAccount(account);
        Handler h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    if (DataThread.RESULT_OK.equals(obj.getString(DataThread.RESULT_CODE))) {
                        int count = updateProposer(friendAccount,
                                request == REQUEST_CANCEL ? DatabaseUtil.Proposer.STATUS_IGNORE : DatabaseUtil.Proposer.STATUS_FRIEND);
                        if (request == REQUEST_OK) {
                            Uri uri = logInformation(friendAccount, DatabaseUtil.Information.REQUEST_CONFIRM);
                            if (uri != null) {
                                sendMsgDelayed(MESSAGE_BROADCAST_INFORMATION, 0);
                            }
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

    private int deleteCalllogs(String selection, String[] selectionArgs) {
        return mContentResolver.delete(DatabaseProvider.CALLLOGS_URI, selection, selectionArgs);
    }

    private int deleteCalllog(String account) {
        return deleteCalllogs(DatabaseUtil.Calllog.ACCOUNT + " = ?", new String[]{account});
    }

    private Uri logInformation(String account, int request) {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.Information.TIME, System.currentTimeMillis()/1000);
        values.put(DatabaseUtil.Information.ACCOUNT, account);
        values.put(DatabaseUtil.Information.ID, "");
        values.put(DatabaseUtil.Information.REQUEST, request);
        values.put(DatabaseUtil.Information.READ, DatabaseUtil.Information.READ_NEW);
        return mContentResolver.insert(DatabaseProvider.INFORMATIONS_URI, values);
    }

    private Cursor getInformations(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mContentResolver.query(DatabaseProvider.INFORMATIONS_VIEW_URI, projection, selection, selectionArgs, sortOrder);
    }

    private int updateInformation(String account, int request) {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.Information.REQUEST, request);
        values.put(DatabaseUtil.Information.READ, DatabaseUtil.Information.READ_NEW);
        return mContentResolver.update(DatabaseProvider.INFORMATIONS_URI, values, DatabaseUtil.Information.ACCOUNT + " = ? and " + DatabaseUtil.Information.REQUEST + " = " + DatabaseUtil.Information.REQUEST_FRIEND, new String[]{account});
    }

    public int readInformation() {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.Information.READ, DatabaseUtil.Information.READ_OLD);
        return mContentResolver.update(DatabaseProvider.INFORMATIONS_URI, values, DatabaseUtil.Information.READ + " = ?", new String[]{DatabaseUtil.Information.READ_NEW + ""});
    }

    private int deleteInformations(String selection, String[] selectionArgs) {
        return mContentResolver.delete(DatabaseProvider.INFORMATIONS_URI, selection, selectionArgs);
    }

    private int deleteInformation(String account) {
        return deleteInformations(DatabaseUtil.Information.ACCOUNT + " = ?", new String[]{account});
    }

    public ArrayList<Object> getCalllogsAndInformations() {
        Cursor cursorCalllogs = getCalllogs(null, null, null, DatabaseUtil.Calllog.TIME + " desc");
        Cursor cursorInformations = getInformations(null,
                DatabaseUtil.Information.REQUEST + " != " + DatabaseUtil.Information.REQUEST_FRIEND,
                null,
                DatabaseUtil.Information.TIME + " desc");

        ArrayList<Object> arrayList = new ArrayList<Object>();

        if (cursorCalllogs == null || !cursorCalllogs.moveToFirst()) {
            if (cursorInformations != null && cursorInformations.moveToFirst()) {
                do {
                    arrayList.add(new Information(cursorInformations.getLong(DatabaseUtil.Information.COLUMN_TIME),
                            cursorInformations.getString(DatabaseUtil.Information.COLUMN_ACCOUNT),
                            cursorInformations.getString(DatabaseUtil.Information.COLUMN_ID),
                            cursorInformations.getInt(DatabaseUtil.Information.COLUMN_REQUEST),
                            cursorInformations.getInt(DatabaseUtil.Information.COLUMN_READ),
                            cursorInformations.getString(DatabaseUtil.Information.COLUMN_ACCOUNT_NAME),
                            DataUtil.getImage(cursorInformations.getBlob(DatabaseUtil.Information.COLUMN_ACCOUNT_ICON)),
                            cursorInformations.getString(DatabaseUtil.Information.COLUMN_ACCOUNT_COMMENT)));
                } while (cursorInformations.moveToNext());
            } else {
                Log.d(TAG, "getCalllogsAndNotifications: calllogs is null.");
            }
        } else if (cursorInformations == null || !cursorInformations.moveToFirst()) {
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
            while (!cursorCalllogs.isAfterLast() || !cursorInformations.isAfterLast()) {
                if (cursorCalllogs.isAfterLast()) {
                    arrayList.add(new Information(cursorInformations.getLong(DatabaseUtil.Information.COLUMN_TIME),
                            cursorInformations.getString(DatabaseUtil.Information.COLUMN_ACCOUNT),
                            cursorInformations.getString(DatabaseUtil.Information.COLUMN_ID),
                            cursorInformations.getInt(DatabaseUtil.Information.COLUMN_REQUEST),
                            cursorInformations.getInt(DatabaseUtil.Information.COLUMN_READ),
                            cursorInformations.getString(DatabaseUtil.Information.COLUMN_ACCOUNT_NAME),
                            DataUtil.getImage(cursorInformations.getBlob(DatabaseUtil.Information.COLUMN_ACCOUNT_ICON)),
                            cursorInformations.getString(DatabaseUtil.Information.COLUMN_ACCOUNT_COMMENT)));
                    cursorInformations.moveToNext();
                } else if (cursorInformations.isAfterLast()) {
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
                    Long timeNotification = cursorInformations.getLong(DatabaseUtil.Information.COLUMN_TIME);
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
                        arrayList.add(new Information(timeNotification,
                                cursorInformations.getString(DatabaseUtil.Information.COLUMN_ACCOUNT),
                                cursorInformations.getString(DatabaseUtil.Information.COLUMN_ID),
                                cursorInformations.getInt(DatabaseUtil.Information.COLUMN_REQUEST),
                                cursorInformations.getInt(DatabaseUtil.Information.COLUMN_READ),
                                cursorInformations.getString(DatabaseUtil.Information.COLUMN_ACCOUNT_NAME),
                                DataUtil.getImage(cursorInformations.getBlob(DatabaseUtil.Information.COLUMN_ACCOUNT_ICON)),
                                cursorInformations.getString(DatabaseUtil.Information.COLUMN_ACCOUNT_COMMENT)));
                        cursorInformations.moveToNext();
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
                        arrayList.add(new Information(timeNotification,
                                cursorInformations.getString(DatabaseUtil.Information.COLUMN_ACCOUNT),
                                cursorInformations.getString(DatabaseUtil.Information.COLUMN_ID),
                                cursorInformations.getInt(DatabaseUtil.Information.COLUMN_REQUEST),
                                cursorInformations.getInt(DatabaseUtil.Information.COLUMN_READ),
                                cursorInformations.getString(DatabaseUtil.Information.COLUMN_ACCOUNT_NAME),
                                DataUtil.getImage(cursorInformations.getBlob(DatabaseUtil.Information.COLUMN_ACCOUNT_ICON)),
                                cursorInformations.getString(DatabaseUtil.Information.COLUMN_ACCOUNT_NAME)));
                        cursorInformations.moveToNext();
                    }
                }
            }
        }

        if (cursorCalllogs != null) cursorCalllogs.close();
        if (cursorInformations != null) cursorInformations.close();

        return arrayList;
    }

    public int getUnreadCount() {
        int count = 0;
        Cursor cursorCalllogs = getCalllogs(null, DatabaseUtil.Calllog.READ + " = " + DatabaseUtil.Calllog.READ_NEW, null, null);
        Cursor cursorInformations = getInformations(null,
                DatabaseUtil.Information.REQUEST + " != " + DatabaseUtil.Information.REQUEST_FRIEND + " and " + DatabaseUtil.Information.READ + " = " + DatabaseUtil.Information.READ_NEW,
                null, null);
        if (cursorCalllogs != null) {
            count = count + cursorCalllogs.getCount();
            cursorCalllogs.close();
        }
        if (cursorInformations != null) {
            count = count + cursorInformations.getCount();
            cursorInformations.close();
        }
        return count;
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

    private int deleteFiles(String selection, String[] selectionArgs) {
        return mContentResolver.delete(DatabaseProvider.FILES_URI, selection, selectionArgs);
    }

    private boolean loginSip(String account) {
        boolean result = false;
        String sip = "";

        Cursor cursor = getAccounts(null, TextUtils.isEmpty(account) ? null : DatabaseUtil.Account.ACCOUNT + " = " + account, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            if (DataUtil.IS_APP) {
                sip = cursor.getString(DatabaseUtil.Account.COLUMN_SIP);
            } else {
                String id = cursor.getString(DatabaseUtil.Account.COLUMN_ID);
                if (!TextUtils.isEmpty(id)) {
                    Cursor c = getDevices(null, DatabaseUtil.Device.ID + " = ?", new String[]{id}, null);
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

        if (!TextUtils.isEmpty(sip)
                && LinphonePreferences.instance().getAccountCount() < 1) {
            try {
                AccountBuilder builder = new AccountBuilder(LinphoneManager.getLc())
                        .setUsername(sip)
                        .setDomain(DataUtil.SIP_DOMAIN)
                        .setPassword(sip)
                        .setTransport(TransportType.Udp);
                builder.saveNewAccount();
                result = true;
            } catch (CoreException e) {
                Log.e(TAG, "loginSip: CoreException is ", e);
            } catch (Exception e) {
                Log.e(TAG, "loginSip: Exception is ", e);
                result = sendMsgDelayed(MESSAGE_SIP, LOOP_SIP_TIME);
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

    public boolean checkSipRelation(String account, String friendAccount, String id, final Handler handler, final int what) {
        account = getAccount(account);
        if (TextUtils.isEmpty(account) || (TextUtils.isEmpty(friendAccount) && TextUtils.isEmpty(id))) {
            Log.d(TAG, "checkSipRelation: return false");
            return false;
        } else {
            Log.d(TAG, "checkSipRelation: " + account + " to "
                    + (TextUtils.isEmpty(friendAccount) ? "id:" + id : "account:" + friendAccount));
        }
        Handler h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    if (!DataThread.RESULT_OK.equals(obj.getString(DataThread.RESULT_CODE))) {
                        checkFriend();
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "checkSipRelation: JSONException is ", e);
                }
                if (handler != null) {
                    handler.obtainMessage(what, obj).sendToTarget();
                } else {
                    Log.d(TAG, "checkSipRelation: handler is null");
                }
            }
        };
        final Map<String, String> params = new HashMap<String, String>();
        params.put("number", account);
        if (!TextUtils.isEmpty(friendAccount)) {
            params.put("friendNumber", friendAccount);
        }
        if (!TextUtils.isEmpty(id)) {
            params.put("deviceId", id);
        }
        new DataThread(DataUtil.FRIEND_URL + "isFriend", params,
                h.obtainMessage(0)).start();
        return true;
    }

    public static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;
    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 2;

    private boolean checkAndRequestPermission(Activity activity, String permission, int request) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(getApplication(), permission) != PackageManager.PERMISSION_GRANTED) {
            if (activity == null) {
                Log.d(TAG, "checkAndRequestPermission: activity is null.");
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, request);
            }
            return true;
        } else {
            return false;
        }
    }

    private void showToast(String text, int length) {
        Toast.makeText(this, text, length).show();
    }

    private static final int NOTIF_DEVICE_UNBIND = 1;
    private static final int NOTIF_FRIEND_REQUEST = 2;
    private static final int NOTIF_FRIEND_CONFIRMED = 3;
    private static final int NOTIF_FRIEND_DELETED = 4;

    private void showNotification(int id, String title, String text, Bitmap icon, Intent intent) {
        Notification.Builder builder = new Notification.Builder(this);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentTitle(title)
                .setContentText(text)
                .setContentIntent(contentIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("New message")
                .setLargeIcon(icon != null ? icon : BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(title, text, NotificationManager.IMPORTANCE_DEFAULT);
            builder.setChannelId(title);
            mNotificationManager.createNotificationChannel(channel);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setFullScreenIntent(contentIntent, true);
        }
        Notification notify = builder.build();
        mNotificationManager.notify(id, notify);
    }

    private boolean checkDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            return false;
        } else {
            return true;
        }
    }

    private boolean showDialog(String title, String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            showToast(text, Toast.LENGTH_LONG);
            sendBroadcast(new Intent(DataUtil.BROADCAST_ACCOUNT));
            return false;
        }

        final AlertDialog dialog = new AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher)
                .setTitle(title)
                .setMessage(text)
                .setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(DataService.this, BeautyMirrorActivity.class));
                            dialog.dismiss();
                        }
                    })
                .setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sendBroadcast(new Intent(DataUtil.BROADCAST_ACCOUNT));
                            dialog.dismiss();
                        }
                    }).create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
        dialog.show();
        return true;
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                sync();
            } else {
                Log.d(TAG, "[mBroadcastReceiver]onReceive: action is " + action);
            }
        }
    };

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
