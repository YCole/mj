package com.gome.beautymirror.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataReceiver extends BroadcastReceiver {

    private static final String TAG = DataReceiver.class.getSimpleName();

    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "[onReceive]");

        context.startService(new Intent(context, DataService.class));
    }
}
