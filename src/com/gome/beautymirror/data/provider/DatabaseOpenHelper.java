package com.gome.beautymirror.data.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = "SipphoneDatabaseHelper";

    private static DatabaseOpenHelper sSingleton = null;

    public static synchronized DatabaseOpenHelper getInstance(Context context) {
        if (sSingleton == null) {
            sSingleton = new DatabaseOpenHelper(context);
        }
        return sSingleton;
    }

    public DatabaseOpenHelper(Context context) {
        super(context, DatabaseUtil.DB_NAME, null, DatabaseUtil.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate");

        try {
            db.execSQL(DatabaseUtil.Account.CREATE);
            db.execSQL(DatabaseUtil.Device.CREATE);
            db.execSQL(DatabaseUtil.Account.CREATE_VIEW);

            db.execSQL(DatabaseUtil.Friend.CREATE);
            db.execSQL(DatabaseUtil.FriendDevice.CREATE);
            db.execSQL(DatabaseUtil.Friend.CREATE_VIEW);

            db.execSQL(DatabaseUtil.People.CREATE);

            db.execSQL(DatabaseUtil.Proposer.CREATE);

            db.execSQL(DatabaseUtil.Calllog.CREATE);
            db.execSQL(DatabaseUtil.Calllog.CREATE_TRIGGER);
            db.execSQL(DatabaseUtil.Calllog.CREATE_VIEW);

            db.execSQL(DatabaseUtil.Information.CREATE);
            db.execSQL(DatabaseUtil.Information.CREATE_TRIGGER);
            db.execSQL(DatabaseUtil.Information.CREATE_VIEW);

            db.execSQL(DatabaseUtil.File.CREATE);
        } catch(SQLiteException e) {
            Log.e(TAG, "onCreate: " + e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade from " + oldVersion + " to " + newVersion);

        if (isUpgradeRequired(oldVersion, newVersion, 2)) {
            upgradeToVersion2(db);
            oldVersion = 2;
        }

        if (oldVersion != newVersion) {
            throw new IllegalStateException(
                    "error upgrading the database to version " + newVersion);
        }
    }

    private static boolean isUpgradeRequired(int oldVersion, int newVersion, int version) {
        return oldVersion < version && newVersion >= version;
    }

    private void upgradeToVersion2(SQLiteDatabase db) {
        Log.d(TAG, "upgradeToVersion2 is unfinished");
    }
}
