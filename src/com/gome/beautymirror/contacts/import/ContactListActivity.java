package com.gome.beautymirror.contacts;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.provider.Settings;

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

import gome.beautymirror.ui.MyToast;
import gome.beautymirror.ui.blurdialog.BlurDialog;

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
    TextView mTvTitleName , mCancleSearch , mSearchText;
    ImageView mBtBack;
    RelativeLayout mRLSearchPhonenumber ;
    LinearLayout mRlMainSearchView;
    LinearLayout mMainContent, mActionBar, mSearchRoot, mNoContact, mSearchBar, mNoUser,mNoPermission,mLlSearch;
    FrameLayout mListLayout;
    TextView mGotoSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transportStatus(this);
        setContentView(R.layout.activity_contact_list);
        initView();
        initData();
        refreshView();
        initClick();
        initStickyHeader();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
        refreshView();
    }

    private void refreshView(){
        if(isHasContactPermission()){
            mNoPermission.setVisibility(View.GONE);
        }else {
            mNoPermission.setVisibility(View.VISIBLE);
            mListLayout.setVisibility(View.GONE);
            mNoContact.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
        }
    }


    private boolean isHasContactPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }else {
            return true;
        }
    }

    private void initStickyHeader(){
        final TextView tvStickyHeaderView = findViewById(R.id.tv_contact_header);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                View stickyInfoView = recyclerView.findChildViewUnder(tvStickyHeaderView.getMeasuredWidth() / 2, 5);
                if (stickyInfoView != null && stickyInfoView.getContentDescription() != null) {
                    tvStickyHeaderView.setText(String.valueOf(stickyInfoView.getContentDescription()));
                }
                View transInfoView = recyclerView.findChildViewUnder(tvStickyHeaderView.getMeasuredWidth() / 2, tvStickyHeaderView.getMeasuredHeight() + 1);
                if (transInfoView != null && transInfoView.getTag() != null) {
                    int transViewStatus = (int) transInfoView.getTag();
                    int dealtY = transInfoView.getTop() - tvStickyHeaderView.getMeasuredHeight();
                    if (transViewStatus == ContactAdapter.HAS_STICKY_VIEW) {
                        if (transInfoView.getTop() > 0) {
                            tvStickyHeaderView.setTranslationY(dealtY);
                        } else {
                            tvStickyHeaderView.setTranslationY(0);
                        }
                    } else if (transViewStatus == ContactAdapter.NONE_STICKY_VIEW) {
                        tvStickyHeaderView.setTranslationY(0);
                    }
                }
            }
        });

    }

    private void transportStatus(Activity context){
        context.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Window window = context.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        );
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        context.getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    private void initView() {
        mSearchBox = (EditText) findViewById(R.id.searchField);
        mClearBtn = (ImageView) findViewById(R.id.clearSearchField);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mTvTitleName=findViewById(R.id.tv_title_name);
        mBtBack = findViewById(R.id.bt_back);
        mRLSearchPhonenumber =findViewById(R.id.rl_search_phonenumber);
        mRlMainSearchView =findViewById(R.id.main_search_view);
        mCancleSearch =findViewById(R.id.bt_cancle_search);
        mMainContent=findViewById(R.id.main_content);
        mSearchNumBox=findViewById(R.id.et_search_number);
        mSearchClearNum=findViewById(R.id.search_clear_num);;

        mProgressBar = (ProgressBar) findViewById(R.id.pb);
        mTvTitleName.setText(getString(R.string.contact_phone_contact));

        mSearchRoot = findViewById(R.id.search_root);
        mSearchRoot.setVisibility(View.VISIBLE);

        mActionBar = findViewById(R.id.action_bar);
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        mActionBar.setPadding(0, getResources().getDimensionPixelSize(resourceId),0,0);
        mSearchBar = findViewById(R.id.search_bar);
        mSearchBar.setPadding(0, getResources().getDimensionPixelSize(resourceId),0,0);

        mListLayout =  findViewById(R.id.list_layout);
        mNoContact =  findViewById(R.id.no_register_user);
        mNoPermission = findViewById(R.id.no_permission);

        mNoUser =  findViewById(R.id.no_User);

        mGotoSettings = findViewById(R.id.goto_settings);
        mSearchText = findViewById(R.id.search_text);
        mLlSearch = findViewById(R.id.ll_search);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_back) {
            finish();
        } else if (v.getId() == R.id.clearSearchField) {
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
        }else if(v.getId() == R.id.goto_settings){
         /*   Intent localIntent = new Intent();
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= 9) {
                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                localIntent.setData(Uri.fromParts("package", getPackageName(), null));
            } else if (Build.VERSION.SDK_INT <= 8) {
                localIntent.setAction(Intent.ACTION_VIEW);
                localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
            }*/
            Intent localIntent =  new Intent(Settings.ACTION_SETTINGS);
            startActivity(localIntent);
        }else if(v.getId() == R.id.ll_search){
            searchForPhoneNumber();
            mLlSearch.setVisibility(View.GONE);
        }
    }

    private void searchForPhoneNumber(){
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
                            if (account == null) {
                                return;
                            }
                            LinphoneContact contact = ContactsManager.getInstance().getContactFromAccount(account);
                            if (contact != null) {
                                Bundle extras = new Bundle();
                                extras.putSerializable("Contact", contact);
                                Intent intent = new Intent(ContactListActivity.this, ContactDetailsActivity.class);
                                intent.putExtras(extras);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(ContactListActivity.this, StrangerActivity.class);
                                intent.putExtra("NAME", name);
                                intent.putExtra("ACCOUNT", account);
                                intent.putExtra("ICON", bitmap);
                                startActivity(intent);
                            }
                        } else if ("111".equals(obj.getString(DataThread.RESULT_CODE))) {
                            mNoUser.setVisibility(View.VISIBLE);

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

    private void initClick() {
        mClearBtn.setOnClickListener(this);
        mBtBack.setOnClickListener(this);
        mRLSearchPhonenumber.setOnClickListener(this);
        mCancleSearch .setOnClickListener(this);
        mSearchClearNum.setOnClickListener(this);
        mGotoSettings.setOnClickListener(this);
        mLlSearch.setOnClickListener(this);

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
                mNoUser.setVisibility(View.GONE);
                if (TextUtils.isEmpty(searchKey)) {
                    mSearchClearNum.setVisibility(View.GONE);
                    mLlSearch.setVisibility(View.GONE);
                } else {
                    mSearchClearNum.setVisibility(View.VISIBLE);
                    mLlSearch.setVisibility(View.VISIBLE);
                    mSearchText.setText(searchKey);
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
                showConfirmationDialog(info.getAccount());
            }
        });

    }

    private void showConfirmationDialog(final String account) {
        final View view= LayoutInflater.from(this).inflate(R.layout.dialog_add_friend, null);
        final TextView cancel =view.findViewById(R.id.choosepage_cancel);
        final TextView sure =view.findViewById(R.id.choosepage_sure);
        final EditText edittext =view.findViewById(R.id.choosepage_edittext);
        new BlurDialog(this,true){
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(view);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
                sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DataService.instance().requestFriend(null, account, edittext.getText().toString(), new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                try {
                                    JSONObject obj = (JSONObject) msg.obj;
                                    if (DataThread.RESULT_OK.equals(obj.getString(DataThread.RESULT_CODE))) {
                                       MyToast.showToast(ContactListActivity.this,getResources().getString(R.string.send_add_friend_request) , Toast.LENGTH_SHORT);
                                    } else if ("444".equals(obj.getString(DataThread.RESULT_CODE))) {
                                        MyToast.showToast(ContactListActivity.this,obj.getString(DataThread.RESULT_MSG) , Toast.LENGTH_SHORT);
                                    } else {
                                        MyToast.showToast(ContactListActivity.this,"Fail" , Toast.LENGTH_SHORT);
                                    }
                                } catch (JSONException e) {
                                    Log.e("ContactListActivity", "showConfirmationDialog: JSONException is ", e);
                                }
                            }
                        }, 0);
                        dismiss();
                    }
                });
            }
        }.show();
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
            if(mContactList.isEmpty()){
                mListLayout.setVisibility(View.GONE);
                mNoContact.setVisibility(View.VISIBLE);
            }else {
                mListLayout.setVisibility( isHasContactPermission() ? View.VISIBLE :  View.GONE);
                mNoContact.setVisibility(View.GONE);
            }

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
        refreshData();
    }

    private void refreshData() {
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
