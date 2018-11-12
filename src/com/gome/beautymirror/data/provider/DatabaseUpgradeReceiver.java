package com.gome.beautymirror.data.provider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseUpgradeReceiver extends BroadcastReceiver {

    private static final String TAG = DatabaseUpgradeReceiver.class.getSimpleName();

    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "SipphoneUpgradeReceiver");

        DatabaseOpenHelper helper = DatabaseOpenHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.close();
    }
}
