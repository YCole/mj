package com.gome.beautymirror.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.BroadcastReceiver;
import android.content.Context;
import com.gome.beautymirror.data.CallLog;
import com.gome.beautymirror.data.provider.DatabaseUtil.Calllog;

import com.gome.beautymirror.R;

import java.util.ArrayList;

import com.gome.beautymirror.call.RecentCallAdapter;

public class RecentCallActivity  extends BaseStatusBarActivity implements View.OnClickListener {

    private ArrayList<CallLog> mLogs;
    private String account;
    private RecentCallAdapter mRecentCallAdapter;
    private RecyclerView recentList;
    private LinearLayout noCallRecent;
    private LinearLayoutManager mLayoutManager;
    private ImageView mBtBack;
    private TextView mTvTitleName;
    LinearLayout mSearchRoot, mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recent_call);
        account = getIntent().getExtras().getString("Account");
        recentList = findViewById(R.id.recent_list);
        noCallRecent = findViewById(R.id.no_call_recent);
        mLayoutManager = new LinearLayoutManager(this);
        recentList.setLayoutManager(mLayoutManager);
        mBtBack = findViewById(R.id.bt_back);
        mTvTitleName=findViewById(R.id.tv_title_name);
        mTvTitleName.setText(getResources().getString(R.string.recent_call));
        mBtBack.setOnClickListener(this);

        mSearchRoot = findViewById(R.id.search_root);
        mSearchRoot.setVisibility(View.GONE);

        mActionBar = findViewById(R.id.action_bar);
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        mActionBar.setPadding(0, getResources().getDimensionPixelSize(resourceId),0,0);

    }

    @Override
    public void onResume() {
        super.onResume();
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(mReceiver, filter);
        mLogs = getRecentCalllogs(account);

        if (!hideHistoryListAndDisplayMessageIfEmpty()) {
            mRecentCallAdapter = new RecentCallAdapter(this, mLogs);
            recentList.setAdapter(mRecentCallAdapter);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
       unregisterReceiver(mReceiver);
    }

    private boolean hideHistoryListAndDisplayMessageIfEmpty() {
        if (mLogs.isEmpty()) {
            noCallRecent.setVisibility(View.VISIBLE);
            recentList.setVisibility(View.GONE);
            return true;
        } else {
            noCallRecent.setVisibility(View.GONE);
            recentList.setVisibility(View.VISIBLE);
            return false;
        }
    }

    private ArrayList<CallLog> getRecentCalllogs(String account){

        Cursor cursorCalllogs = com.gome.beautymirror.data.DataService.instance().getCalllogs(null,
                Calllog.ACCOUNT + " = ?",
                new String[]{account},
                Calllog.TIME
                        + " desc");

        ArrayList<CallLog> arrayList = new ArrayList<CallLog>();

        if (cursorCalllogs != null && cursorCalllogs.moveToFirst()) {
            do {
                arrayList.add(new CallLog(cursorCalllogs.getLong(Calllog.COLUMN_TIME),
                        cursorCalllogs.getString(Calllog.COLUMN_ACCOUNT),
                        cursorCalllogs.getString(Calllog.COLUMN_ID),
                        cursorCalllogs.getLong(Calllog.COLUMN_END_TIME),
                        cursorCalllogs.getInt(Calllog.COLUMN_DURATION),
                        cursorCalllogs.getInt(Calllog.COLUMN_STATUS),
                        cursorCalllogs.getInt(Calllog.COLUMN_READ),
                        cursorCalllogs.getString(Calllog.COLUMN_ACCOUNT_NAME),
                        com.gome.beautymirror.data.DataUtil.getImage(cursorCalllogs.getBlob(Calllog.COLUMN_ACCOUNT_ICON)),
                        cursorCalllogs.getString(Calllog.COLUMN_ACCOUNT_COMMENT)));
            } while (cursorCalllogs.moveToNext());
        }
        return arrayList;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_back) {
            finish();
        }
    }

    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            if(mRecentCallAdapter!=null){
                mRecentCallAdapter.notifyDataSetChanged();
            }
        };
    };

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_TIME_TICK.equals(action)) {
                mHandler.sendEmptyMessage(0);
            }
        }
    };

}
