package com.gome.beautymirror.contacts;

/*
ContactsListFragment.java
Copyright (C) 2017  Belledonne Communications, Grenoble, France

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/


import android.support.v4.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.promeg.pinyinhelper.Pinyin;
import com.gome.beautymirror.LinphoneManager;
import com.gome.beautymirror.LinphoneUtils;
import com.gome.beautymirror.R;
import com.gome.beautymirror.activities.BeautyMirrorActivity;
import com.gome.beautymirror.fragments.FragmentsAvailable;
import com.gome.beautymirror.ui.SelectableHelper;
import com.gome.beautymirror.contacts.ContactListActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.gome.beautymirror.data.DataService;
import com.gome.beautymirror.data.provider.DatabaseUtil;

import cole.activities.MyDeviceActivity;
import gome.beautymirror.contacts.newfriend.RequestInfo;
import gome.beautymirror.contacts.newfriend.SearchContactsAdapter;

import com.gome.beautymirror.data.DataUtil;

public class ContactsListFragment extends Fragment implements OnItemClickListener,
        ContactsUpdatedListener,
        ContactsListAdapter.ViewHolder.ClickListener,
        SearchContactsAdapter.ViewHolder.ClickListener,
        SelectableHelper.DeleteListener {
    private RecyclerView contactsList;
    private TextView noContact,goto_contacts;
    private ImageView newContact;
    private LinearLayout actionBar,noContactLayout,noSearchResultLayout;
    private ImageView edit;
    private int lastKnownPosition;
    private boolean editOnClick = false, editConsumed = false;
    private ImageView clearSearchField;
    private EditText searchField;
    private ProgressBar contactsFetchInProgress;
    private LinearLayoutManager layoutManager;
    private Context mContext;
    private SelectableHelper mSelectionHelper;
    private ContactsListAdapter mContactAdapter;
    private SearchContactsAdapter mSearchAdapter;
    private RelativeLayout mRlNewFriend,mRlMyDevice;
    private TextView mRequestNum;
    ArrayList<RequestInfo> mData = new ArrayList();

    List<LinphoneContact> listContact;
    List<LinphoneContact> mSearchContact;
    private FrameLayout listLayout;
    private LinearLayout mLlNewFriend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contacts_list, container, false);
        mContext = getActivity().getApplicationContext();
        mSelectionHelper = new SelectableHelper(view, this);

        noContact = view.findViewById(R.id.noContact);
        noContactLayout =  view.findViewById(R.id.no_Contact);
        contactsList = view.findViewById(R.id.contactsList);
        listLayout = view.findViewById(R.id.list_layout);
        mLlNewFriend = view.findViewById(R.id.ll_new_friend);
        goto_contacts = view.findViewById(R.id.goto_contacts);
        noSearchResultLayout = view.findViewById(R.id.no_search_result);
        edit = view.findViewById(R.id.edit);
        contactsFetchInProgress = view.findViewById(R.id.contactsFetchInProgress);
        newContact = view.findViewById(R.id.newContact);
        newContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editConsumed = true;
                startActivity(new Intent(getActivity(), ContactListActivity.class));
            }
        });

        actionBar = view.findViewById(R.id.action_bar);
        actionBar.setPadding(0,BeautyMirrorActivity.getStatusBarHeight(getContext()),0,0);
        goto_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ContactListActivity.class));
            }
        });

        newContact.setEnabled(LinphoneManager.getLc().getCallsNb() == 0);
        contactsFetchInProgress.setVisibility(View.VISIBLE);

        mRequestNum=view.findViewById(R.id.tv_new_request_num);
        mRlNewFriend= view.findViewById(R.id.rl_new_friend);
        mRlNewFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoNewFriendActivity();
            }
        });
        mRlMyDevice = view.findViewById(R.id.rl_my_device);
        mRlMyDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMyDeviceDetailActivity();
            }
        });

        clearSearchField = view.findViewById(R.id.clearSearchField);
        clearSearchField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchField.setText("");
                clearSearchField.setVisibility(View.GONE);
            }
        });

        final TextView tvStickyHeaderView = (TextView) view.findViewById(R.id.tv_contact_header);
        tvStickyHeaderView.setVisibility(View.GONE);

        searchField = view.findViewById(R.id.searchField);
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchKey = s.toString().trim();
                tvStickyHeaderView.setVisibility(View.GONE);
                if(searchContacts(searchKey).size()==0){
                    listLayout.setVisibility(View.GONE);
                    noSearchResultLayout.setVisibility(View.VISIBLE);
                }else{
                    listLayout.setVisibility(View.VISIBLE);
                }
                if (TextUtils.isEmpty(searchKey)) {
                    clearSearchField.setVisibility(View.GONE);
                    noContactLayout.setVisibility(View.VISIBLE);
                    noSearchResultLayout.setVisibility(View.GONE);
                } else {
                    clearSearchField.setVisibility(View.VISIBLE);
                    noContactLayout.setVisibility(View.GONE);

                }
            }
        });

        layoutManager = new LinearLayoutManager(mContext);
        contactsList.setLayoutManager(layoutManager);

        /*DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(contactsList.getContext(),
                layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(getActivity().getResources().getDrawable(R.drawable.divider));
        contactsList.addItemDecoration(dividerItemDecoration);*/

        contactsFetchInProgress = view.findViewById(R.id.contactsFetchInProgress);
        contactsFetchInProgress.setVisibility(View.VISIBLE);

        contactsList.setOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    if (transViewStatus == ContactsListAdapter.HAS_STICKY_VIEW) {
                        tvStickyHeaderView.setVisibility(View.VISIBLE);
                        if (transInfoView.getTop() > 0) {
                            tvStickyHeaderView.setTranslationY(dealtY);
                        } else {
                            tvStickyHeaderView.setTranslationY(0);
                        }
                    } else if (transViewStatus == ContactsListAdapter.NONE_STICKY_VIEW) {
                        tvStickyHeaderView.setVisibility(View.VISIBLE);
                        tvStickyHeaderView.setTranslationY(0);
                    }else if(transViewStatus ==ContactsListAdapter.NEW_FRIEND_STICKY_VIEW){
                        tvStickyHeaderView.setVisibility(View.GONE);
                    }else{
                        tvStickyHeaderView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        return view;
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            handRequestMessage();
            return true;
        }
    });

    private void handRequestMessage() {
        if (mData.size() > 0) {
            int num=0;
            String RequestNumFormat = getResources().getString(R.string.contact_new_request_num);
            for(RequestInfo info:mData){
                if(info.getHandleFlag()==0){
                    num++;
                }
            }
            if(num>0){
                mRequestNum.setText(String.format(RequestNumFormat, num));
            }else{
                mRequestNum.setText(getString(R.string.contact_add_phone_contact_friend));
            }
        }
    }

    /**
     * 异步返回请求列表
     */
    public  ArrayList<RequestInfo> getFriendRequestList() {
        ArrayList<RequestInfo> data=new ArrayList<RequestInfo>();
        android.util.Log.d("zlm","getProposers");
        Cursor cursor = DataService.instance().getProposers(null, null, null, DatabaseUtil.Proposer.REQUEST_TIME + " desc");
        android.util.Log.d("zlm","cursor.getCount()="+cursor.getCount());
        if (cursor != null && cursor.getCount() > 0) {
            byte[] defaultBytes = DataUtil.getImage(ContactsManager.getInstance().getDefaultAvatarBitmap());
            if (cursor.moveToFirst()) {
                do {
                    android.util.Log.d("zlm","new RequestInfo");
                    byte[] bytes = cursor.getBlob(DatabaseUtil.Proposer.COLUMN_ICON);
                    if (bytes == null) {
                        bytes = defaultBytes;
                    }
                    RequestInfo info = new RequestInfo(
                            bytes,
                            cursor.getString(DatabaseUtil.Proposer.COLUMN_ACCOUNT),
                            cursor.getString(DatabaseUtil.Proposer.COLUMN_NAME),
                            cursor.getString(DatabaseUtil.Proposer.COLUMN_MESSAGE),
                            cursor.getInt(DatabaseUtil.Proposer.COLUMN_STATUS));
                    data.add(info);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        //compare(contactsInfos);
        return data;
    }

    public void displayFirstContact() {
        if (contactsList != null && contactsList.getAdapter() != null && contactsList.getAdapter().getItemCount() > 0) {
            ContactsListAdapter mAdapt = (ContactsListAdapter) contactsList.getAdapter();
            BeautyMirrorActivity.instance().displayContact((LinphoneContact) mAdapt.getItem(0), false);
        } else {
            BeautyMirrorActivity.instance().displayEmptyFragment();
        }
    }

    private List<LinphoneContact> searchContacts(String search) {
        boolean isEditionEnabled = false;
        List<LinphoneContact> listContact;
        List<LinphoneContact> mSearchList = new ArrayList<>();

        listContact = ContactsManager.getInstance().getSIPContacts();
        if (search == null || search.length() == 0) {
            changeContactsAdapter();
            return listContact;
        }
        mContactAdapter.setmIsSearchMode(true);
        String displayednumberOrAddress="";
        for (LinphoneContact info : listContact) {
            for (LinphoneNumberOrAddress noa : info.getNumbersOrAddresses()) {
                String value = noa.getValue();
                displayednumberOrAddress= LinphoneUtils.getDisplayableUsernameFromAddress(value);
            }
            if (ContactsUtils.searchContact(search, info.getFullName(),displayednumberOrAddress)) {
                mSearchList.add(info);
            }
        }
        mSearchAdapter= new SearchContactsAdapter(mContext, mSearchList, this, search);

        if (isEditionEnabled) {
            mSelectionHelper.enterEditionMode();
        }
        contactsList.setAdapter(mSearchAdapter);
        return mSearchList;
    }


    private void changeContactsAdapter() {
        contactsList.setVisibility(View.VISIBLE);
        boolean isEditionEnabled = false;
        if (searchField.getText().toString() == null || searchField.getText().toString().length() == 0) {
            listContact = ContactsManager.getInstance().getSIPContacts();

            if (mContactAdapter != null && mContactAdapter.isEditionEnabled()) {
                isEditionEnabled = true;
            }

            mContactAdapter = new ContactsListAdapter(mContext, listContact, this, mSelectionHelper,false);
            mContactAdapter.addHeadView(View.inflate(getActivity().getApplicationContext(),R.layout.contacts_list_head,null));

            mSelectionHelper.setAdapter(mContactAdapter);

            if (isEditionEnabled) {
                mSelectionHelper.enterEditionMode();
            }
            contactsList.setAdapter(mContactAdapter);
            edit.setEnabled(true);

            mContactAdapter.notifyDataSetChanged();

            if (mContactAdapter.getItemCount() > 0) {
                contactsFetchInProgress.setVisibility(View.GONE);
            }

        } else {
            mSearchContact = searchContacts(searchField.getText().toString());

            mSearchAdapter = new SearchContactsAdapter(mContext, mSearchContact, this,searchField.getText().toString());

            contactsList.setAdapter(mSearchAdapter);
            edit.setEnabled(true);

            mSearchAdapter.notifyDataSetChanged();

        }


    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

        LinphoneContact contact = (LinphoneContact) adapter.getItemAtPosition(position);
        if (editOnClick) {
            editConsumed = true;
        } else {
            lastKnownPosition = layoutManager.findFirstVisibleItemPosition();
        }
    }


    @Override
    public void onSearchItemClicked(int position,boolean isCall) {
        LinphoneContact contact = (LinphoneContact) mContactAdapter.getItem(position);
        if (isCall) {
            for (LinphoneNumberOrAddress noa : contact.getNumbersOrAddresses()) {
                String value = noa.getValue();
                BeautyMirrorActivity.instance().setAddresGoToDialerAndCall(value, contact.getFullName(), contact.getPhotoUri());
            }
        } else {
            if (position < mContactAdapter.getItemCount()) {
                BeautyMirrorActivity.instance().displayContact(contact, false);
            }
        }
    }

    @Override
    public void onItemClicked(int position,boolean isCall, boolean isNewFriend, boolean isMyDevice) {
        if(position == 0 ){
            if(isNewFriend){
               gotoNewFriendActivity();
            }else if(isMyDevice){
                gotoMyDeviceDetailActivity();
            }
            return;
        }
        position--;
        LinphoneContact contact = (LinphoneContact) mContactAdapter.getItem(position);

        if (mContactAdapter.isEditionEnabled()) {
            mContactAdapter.toggleSelection(position);

        } else if (editOnClick) {
            editConsumed = true;

        } else {
            if (isCall) {
                for (LinphoneNumberOrAddress noa : contact.getNumbersOrAddresses()) {
                    String value = noa.getValue();
                    BeautyMirrorActivity.instance().setAddresGoToDialerAndCall(value, contact.getFullName(), contact.getPhotoUri());
                }
            } else {
                if (position < mContactAdapter.getItemCount() - 1) {    //最后一项不可点击
                    lastKnownPosition = layoutManager.findFirstVisibleItemPosition();
                    BeautyMirrorActivity.instance().displayContact(contact, false);
                }
            }
        }
    }

    @Override
    public boolean onItemLongClicked(int position) {
        if (!mContactAdapter.isEditionEnabled()) {
            mSelectionHelper.enterEditionMode();
        }
        mContactAdapter.toggleSelection(position);
        return true;
    }

    public void refreshData(){
        if (!isAdded()) {
            return;
        }
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                mData.clear();
                mData = getFriendRequestList();
                mHandler.sendEmptyMessage(0);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();

        ContactsManager.addContactsListener(this);

        if (editConsumed) {
            editOnClick = false;
        }

        if (searchField != null && searchField.getText().toString().length() > 0) {
            if (contactsFetchInProgress != null) contactsFetchInProgress.setVisibility(View.GONE);
        }

        if (BeautyMirrorActivity.isInstanciated()) {
            BeautyMirrorActivity.instance().selectMenu(FragmentsAvailable.CONTACTS_LIST);
            BeautyMirrorActivity.instance().hideTabBar(false);
        }
        invalidate();
        refresh();
    }

    @Override
    public void onPause() {
        ContactsManager.removeContactsListener(this);
        super.onPause();
    }

    @Override
    public void onContactsUpdated() {
        if (!BeautyMirrorActivity.isInstanciated()
                || (BeautyMirrorActivity.instance().getCurrentFragment() != FragmentsAvailable.HISTORY_LIST
                && BeautyMirrorActivity.instance().getCurrentFragment() != FragmentsAvailable.CONTACTS_LIST
                && BeautyMirrorActivity.instance().getCurrentFragment() != FragmentsAvailable.MINE))
            return;
        if (mContactAdapter != null) {
            mContactAdapter.updateDataSet(ContactsManager.getInstance().getSIPContacts());
            mContactAdapter.notifyDataSetChanged();
            refresh();
        }
    }

    private void refresh(){
        noContactLayout.setVisibility(View.GONE);
        listContact = ContactsManager.getInstance().getSIPContacts();
        if(listContact.size() == 0){
            mLlNewFriend.setVisibility(View.VISIBLE);
            listLayout.setVisibility(View.GONE);
            noContactLayout.setVisibility(View.VISIBLE);
            contactsFetchInProgress.setVisibility(View.GONE);
        }else{
            listLayout.setVisibility(View.VISIBLE);
            mLlNewFriend.setVisibility(View.GONE);
        }
    }

    public void invalidate() {
        if (searchField != null && searchField.getText().toString().length() > 0) {
            searchContacts(searchField.getText().toString());
        } else {
            changeContactsAdapter();
        }
        contactsList.scrollToPosition(lastKnownPosition);
    }

    @Override
    public void onDeleteSelection(Object[] objectsToDelete) {
        ArrayList<String> ids = new ArrayList<String>();
        int size = mContactAdapter.getSelectedItemCount();
        for (int i = size - 2; i >= 0; i--) {
            LinphoneContact contact = (LinphoneContact) objectsToDelete[i];
            if (contact.isAndroidContact()) {
                contact.deleteFriend();
                ids.add(contact.getAndroidId());
            } else {
                contact.delete();
            }
        }
        ContactsManager.getInstance().deleteMultipleContactsAtOnce(ids);
    }

    private void gotoNewFriendActivity(){
        Intent mIntent= new Intent(getActivity(), com.gome.beautymirror.contacts.NewFriendActivity.class);
        mIntent.putParcelableArrayListExtra("FRIEND_REQUEST",mData);
        startActivity(mIntent);
    }

    private void gotoMyDeviceDetailActivity(){
        Intent mIntent= new Intent(getActivity(),  com.gome.beautymirror.activities.MyDeviceDetailsActivity.class);
        startActivity(mIntent);
    }
}
