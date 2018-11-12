package com.gome.beautymirror.contacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.beautymirror.R;


import java.util.ArrayList;

import gome.beautymirror.contacts.newfriend.RequestAdapter;
import gome.beautymirror.contacts.newfriend.RequestInfo;

public class NewFriendActivity extends Activity implements View.OnClickListener {
    ImageButton mBtBack;
    TextView mTvTitleName;
    RelativeLayout mRlImportCcontact;
    ArrayList<RequestInfo> mData = new ArrayList();
    ListView mLvRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_new_friend);

        mBtBack = findViewById(R.id.bt_back);
        mRlImportCcontact = findViewById(R.id.rl_import_contact);
        mTvTitleName=findViewById(R.id.tv_title_name);
        mTvTitleName.setText(getString(R.string.contact_new_friend));

        mData =getIntent().getParcelableArrayListExtra("FRIEND_REQUEST");
        mLvRequest = findViewById(R.id.lv_friend_request);
        mLvRequest.setAdapter(new RequestAdapter(this, mData));

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
