package com.gome.beautymirror.contacts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.gome.beautymirror.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.gome.beautymirror.activities.ContactDetailsActivity;

import com.gome.beautymirror.contacts.StrangerActivity;
import com.gome.beautymirror.data.DataService;
import com.gome.beautymirror.data.DataThread;

/**
 * 联系人选择
 */

public class ContactListActivity extends Activity implements View.OnClickListener{

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private ArrayList<ContactInfo> mContactList;
    private EditText mSearchBox ,mSearchNumBox;
    private ImageView mClearBtn, mSearchClearNum;
    private ArrayList<com.gome.beautymirror.contacts.ContactInfo> mSearchList;
    private ContactAdapter mContactAdapter;
    TextView mTvTitleName;
    ImageButton mBtBack;
    RelativeLayout mRLSearchPhonenumber ,mRlMainSearchView;
    Button mCancleSearch ,mBtSearch;
    LinearLayout mMainContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        initView();
        initData();
        initClick();
    }

    private void initView() {
        mSearchBox = (EditText) findViewById(R.id.et_search);
        mClearBtn = (ImageView) findViewById(R.id.iv_search_clear);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mTvTitleName=findViewById(R.id.tv_title_name);
        mBtBack = findViewById(R.id.bt_back);
        mRLSearchPhonenumber =findViewById(R.id.rl_search_phonenumber);
        mRlMainSearchView =findViewById(R.id.main_search_view);
        mCancleSearch =findViewById(R.id.bt_cancle_search);
        mMainContent=findViewById(R.id.main_content);
        mSearchNumBox=findViewById(R.id.et_search_number);
        mSearchClearNum=findViewById(R.id.search_clear_num);
        mBtSearch=findViewById(R.id.bt_search);

        mProgressBar = (ProgressBar) findViewById(R.id.pb);
        mTvTitleName.setText(getString(R.string.contact_phone_contact));

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_back) {
            finish();
        } else if (v.getId() == R.id.iv_search_clear) {
            mSearchBox.setText("");
            mClearBtn.setVisibility(View.GONE);
        }else if (v.getId() == R.id.rl_search_phonenumber) {
            mMainContent.setVisibility(View.GONE);
            mRlMainSearchView.setVisibility(View.VISIBLE);
        }else if (v.getId() == R.id.bt_cancle_search) {
            mMainContent.setVisibility(View.VISIBLE);
            mRlMainSearchView.setVisibility(View.GONE);
        }else if (v.getId() == R.id.search_clear_num) {
            mSearchNumBox.setText("");
            mSearchClearNum.setVisibility(View.GONE);
        }else if (v.getId() == R.id.bt_search) {
            // 查找
            String searchText = mSearchNumBox.getText().toString();
            if(DataService.instance().isAccount(searchText)) {
                Toast.makeText(ContactListActivity.this, "Account is fail", Toast.LENGTH_SHORT).show();
            } else {
                DataService.instance().queryPeople(searchText, new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        try {
                            JSONObject obj = (JSONObject) msg.obj;
                            if (DataThread.RESULT_OK.equals(obj.getString(DataThread.RESULT_CODE))) {
                                String name = obj.optString("contact_name");
                                String account = obj.optString("contact_account");
                                String bitmap = obj.optString("contact_icon");
                                if(account==null){
                                    return;
                                }
                                LinphoneContact contact = ContactsManager.getInstance().getContactFromAccount(account);
                                if(contact!=null){
                                    Bundle extras = new Bundle();
                                    extras.putSerializable("Contact", contact);
                                    Intent intent =new Intent(ContactListActivity.this, ContactDetailsActivity.class);
                                    intent.putExtras(extras);
                                    startActivity(intent);
                                }else{
                                    Intent intent =new Intent(ContactListActivity.this, StrangerActivity.class);
                                    intent.putExtra("NAME",name);
                                    intent.putExtra("ACCOUNT",account);
                                    intent.putExtra("ICON",bitmap);
                                    startActivity(intent);
                                }
                            } else if ("111".equals(obj.getString(DataThread.RESULT_CODE))) {
                                Toast.makeText(ContactListActivity.this, "Contact is fail", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ContactListActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("ContactListActivity", "onClick: JSONException is ", e);
                        }
                    }
                }, 0);
            }
        }
    }

    private void initClick() {
        mClearBtn.setOnClickListener(this);
        mBtBack.setOnClickListener(this);
        mRLSearchPhonenumber.setOnClickListener(this);
        mCancleSearch .setOnClickListener(this);
        mSearchClearNum.setOnClickListener(this);
        mBtSearch.setOnClickListener(this);

        mSearchNumBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchKey = s.toString().trim();
                if (TextUtils.isEmpty(searchKey)) {
                    mSearchClearNum.setVisibility(View.GONE);
                } else {
                    mSearchClearNum.setVisibility(View.VISIBLE);
                }
            }
        });


        mSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchKey = s.toString().trim();
                if (mContactList.size() == 0) return;

                mSearchList.clear();
                searchContacts(searchKey);
                if (TextUtils.isEmpty(searchKey)) {
                    mClearBtn.setVisibility(View.GONE);
                    mSearchList.clear();
                    mContactAdapter.setContactList(mContactList);
                } else {
                    mClearBtn.setVisibility(View.VISIBLE);
                    mContactAdapter.setContactList(mSearchList);
                }
            }
        });
        //联系人未获取到之前搜索不可用
        mSearchBox.setEnabled(false);

        mContactAdapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ContactInfo info, int position) {


            }
        });

    }

    /**
     * 搜索联系人
     *
     * @param searchKey 搜索key
     */
    private void searchContacts(String searchKey) {
        for (ContactInfo info : mContactList) {
            if (ContactsUtils.searchContact(searchKey, info.getName(),info.getPhoneNumber())) {
                mSearchList.add(info);
            }
        }
    }

    /**
     * 滑动到索引字母出
     */
    private void scrollToLetter(String letter) {
        for (int i = 0; i < mContactList.size(); i++) {
            if (TextUtils.equals(letter, mContactList.get(i).getLetter())) {
                ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                break;
            }
        }
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            mProgressBar.setVisibility(View.GONE);

            if (mContactList.size() > 0) {
                mSearchBox.setEnabled(true);
                mContactAdapter.setContactList(mContactList);
            }
            return true;
        }
    });

    private void initData() {
        mSearchList = new ArrayList<>();
        mContactList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mContactAdapter = new ContactAdapter(this, mContactList);
        mRecyclerView.setAdapter(mContactAdapter);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                mContactList = ContactsUtils.getContactList(ContactListActivity.this);
                handler.sendEmptyMessage(0);
            }
        });
    }
}
