package com.gome.beautymirror.contacts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.beautymirror.R;


import java.util.ArrayList;
import com.gome.beautymirror.activities.BaseStatusBarActivity;
import gome.beautymirror.contacts.newfriend.RequestAdapter;
import gome.beautymirror.contacts.newfriend.RequestInfo;
import com.gome.beautymirror.activities.BeautyMirrorActivity;

public class NewFriendActivity extends BaseStatusBarActivity implements View.OnClickListener {
    ImageView mBtBack;
    TextView mTvTitleName;
    RelativeLayout mRlImportCcontact;
    ArrayList<RequestInfo> mData = new ArrayList();
    ListView mLvRequest;
    LinearLayout actionBar ,mFriendRequest,mNoRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_new_friend);
        mBtBack = findViewById(R.id.bt_back);
        mRlImportCcontact = findViewById(R.id.rl_import_contact);
        mTvTitleName=findViewById(R.id.tv_title_name);
        mTvTitleName.setText(getString(R.string.contact_new_friend));

        actionBar = findViewById(R.id.action_bar);
        actionBar.setPadding(0,BeautyMirrorActivity.getStatusBarHeight(this),0,0);

        mData =getIntent().getParcelableArrayListExtra("FRIEND_REQUEST");
        mLvRequest = findViewById(R.id.lv_friend_request);
        mLvRequest.setAdapter(new RequestAdapter(this, mData));

        mFriendRequest = findViewById(R.id.friend_request);
        mNoRequest = findViewById(R.id.noRequest);

        if(mData.isEmpty()){
            mFriendRequest.setVisibility(View.GONE);
            mNoRequest.setVisibility(View.VISIBLE);
        }else{
            mFriendRequest.setVisibility(View.VISIBLE);
            mNoRequest.setVisibility(View.GONE);
        }

        mBtBack.setOnClickListener(this);
        mRlImportCcontact.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_back) {
            finish();
        } else if (v.getId() == R.id.rl_import_contact) {
            startActivity(new Intent(this, ContactListActivity.class));
        }
    }

}
