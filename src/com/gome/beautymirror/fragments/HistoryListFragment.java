package com.gome.beautymirror.fragments;

/*
HistoryListFragment.java
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
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.gome.beautymirror.LinphoneManager;
import com.gome.beautymirror.LinphoneUtils;
import com.gome.beautymirror.R;
import com.gome.beautymirror.activities.BeautyMirrorActivity;
import com.gome.beautymirror.call.CallHistoryAdapter;
import com.gome.beautymirror.contacts.ContactsManager;
import com.gome.beautymirror.contacts.ContactsUpdatedListener;
import android.content.Intent;
import com.gome.beautymirror.ui.SelectableHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.LinkedHashMap ;
import android.os.Handler;

import com.gome.beautymirror.advertisement.BannerLayout;
import com.gome.beautymirror.advertisement.PicassoImageLoader;

import com.gome.beautymirror.data.DataService;
import com.gome.beautymirror.data.CallLog;
import com.gome.beautymirror.data.Notification;
import com.gome.beautymirror.activities.ContactDetailsActivity;
import com.gome.beautymirror.contacts.LinphoneContact;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;

public class HistoryListFragment extends Fragment implements OnClickListener, OnItemClickListener, CallHistoryAdapter.ViewHolder.ClickListener, ContactsUpdatedListener, SelectableHelper.DeleteListener {
    private RecyclerView historyList;
    private TextView noCallHistory;
    private ImageView edit;
     private ArrayList<Object> mLogs;
    private LinkedHashMap <Object,Integer> showLogs;
    private ArrayList<Object> list_key;
    private CallHistoryAdapter mHistoryAdapter;
    private LinearLayoutManager mLayoutManager;
    private Context mContext;
    private SelectableHelper mSelectionHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history, container, false);
        mContext = getActivity().getApplicationContext();
        mSelectionHelper = new SelectableHelper(view, this);

        noCallHistory = view.findViewById(R.id.no_call_history);

        historyList = view.findViewById(R.id.history_list);

        mLayoutManager = new LinearLayoutManager(mContext);
        historyList.setLayoutManager(mLayoutManager);

        edit = view.findViewById(R.id.edit);



        return view;
    }

    private ArrayList<Object> deleteSameLogs(){
        showLogs = new LinkedHashMap <Object, Integer>();
        list_key = new ArrayList<Object>();
        String address  = "";
        String nextAddress="";

        for(int i = 0; i < mLogs.size(); i++){
            int count = 0;
            for(int j = i+1 ; j<mLogs.size();j++){
                CallLog log = null;
                if (mLogs.get(i) instanceof CallLog) {
                    log = (CallLog)mLogs.get(i);
                    address = log.mAccount;
                }else{
                    showLogs.put(mLogs.get(i),0);
                    continue;
                }
                if(mLogs.get(j) instanceof CallLog){
                    CallLog nextLog =(CallLog) mLogs.get(j);
                    nextAddress= nextLog.mAccount;
                }else{
                    nextAddress= "";
                }

                if(address!=null && address.equals(nextAddress)){
                    count++;
                    mLogs.remove(j);
                    j--;
                }
                showLogs.put(log,count+1);
            }
            if(i == (mLogs.size()-1)){
                if(!showLogs.containsKey( mLogs.get(i))){
                    showLogs.put( mLogs.get(i),1);
                }
            }
        }

        for(Iterator iter = showLogs.entrySet().iterator();iter.hasNext();){
            list_key.add(iter.next());
        }

        return mLogs;
    }

    public void displayFirstLog() {
//        if (mLogs != null && mLogs.size() > 0) {
//            CallLog log = mLogs.get(0);
//            String addr;
//            if (log.getDir() == Call.Dir.Incoming) {
//                addr = log.getFromAddress().asString();
//            } else {
//                addr = log.getToAddress().asString();
//            }
//        } else {
//            BeautyMirrorActivity.instance().displayEmptyFragment();
//        }
    }

    private boolean hideHistoryListAndDisplayMessageIfEmpty() {
        if (mLogs.isEmpty()) {
            noCallHistory.setVisibility(View.VISIBLE);
            historyList.setVisibility(View.GONE);
            edit.setEnabled(false);
            return true;
        } else {
            noCallHistory.setVisibility(View.GONE);
            historyList.setVisibility(View.VISIBLE);
            edit.setEnabled(true);
            return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ContactsManager.addContactsListener(this);

        if (BeautyMirrorActivity.isInstanciated()) {
            BeautyMirrorActivity.instance().selectMenu(com.gome.beautymirror.fragments.FragmentsAvailable.HISTORY_LIST);
            BeautyMirrorActivity.instance().hideTabBar(false);
            BeautyMirrorActivity.instance().displayMissedCalls(0);
        }

        mLogs = getCalllogsAndNotifications();
        mLogs = deleteSameLogs();

        mHistoryAdapter = new CallHistoryAdapter(getActivity().getApplicationContext(), list_key, this, mSelectionHelper);//list_key
        historyList.setAdapter(mHistoryAdapter);
        mSelectionHelper.setAdapter(mHistoryAdapter);
        mSelectionHelper.setDialogMessage(R.string.chat_room_delete_dialog);
        noCallHistory.setVisibility((mLogs.isEmpty()) ? View.VISIBLE : View.GONE);

        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        mContext.registerReceiver(mReceiver, filter);
    }



    @Override
    public void onPause() {
        ContactsManager.removeContactsListener(this);
        super.onPause();
        mContext.unregisterReceiver(mReceiver);
    }

    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            if(mHistoryAdapter!=null){
                mHistoryAdapter.notifyDataSetChanged();
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



    @Override
    public void onContactsUpdated() {
        BeautyMirrorActivity.instance().refreshBagdeNumber(getMissedCallLogsAndNotifications());
        if (!BeautyMirrorActivity.isInstanciated() || BeautyMirrorActivity.instance().getCurrentFragment() != com.gome.beautymirror.fragments.FragmentsAvailable.HISTORY_LIST)
            return;
        CallHistoryAdapter adapter = (CallHistoryAdapter) historyList.getAdapter();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (!hideHistoryListAndDisplayMessageIfEmpty()) {
//          historyList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
            mHistoryAdapter = new CallHistoryAdapter(mContext, list_key, this, mSelectionHelper);
            historyList.setAdapter(mHistoryAdapter);
            mSelectionHelper.setAdapter(mHistoryAdapter);
            mSelectionHelper.setDialogMessage(R.string.chat_room_delete_dialog);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

    }

    @Override
    public void onDeleteSelection(Object[] objectsToDelete) {
    /*     int size = mHistoryAdapter.getSelectedItemCount();
        for (int i = 0; i < size; i++) {
            CallLog log = (CallLog) objectsToDelete[i];
            LinphoneManager.getLc().removeCallLog(log);
            onResume();
        }*/
    }

    @Override
    public void onItemClicked(int position , boolean isVedioCall) {
        if (mHistoryAdapter.isEditionEnabled()) {
            mHistoryAdapter.toggleSelection(position);
        } else {
            if (BeautyMirrorActivity.isInstanciated()) {
                String address="";
                if(isVedioCall){
                    if(mLogs.get(position) instanceof CallLog){
                        address = ((CallLog) mLogs.get(position)).mAccount;
                    }else {
                        address = ((Notification) mLogs.get(position)).mAccount;
                    }
                    LinphoneUtils.setInitiateVideoCall(true);
                    BeautyMirrorActivity.instance().setAddresGoToDialerAndCall(address, null, null);
                }else{
                    if(mLogs.get(position) instanceof CallLog){
                        address = ((CallLog) mLogs.get(position)).mAccount;
                        LinphoneUtils.setInitiateVideoCall(true);
                        BeautyMirrorActivity.instance().setAddresGoToDialerAndCall(address, null, null);
                    }else {
                        address = ((Notification) mLogs.get(position)).mAccount;
                        LinphoneContact contact = ContactsManager.getInstance().getContactFromAccount(address);
                        Bundle extras = new Bundle();
                        extras.putSerializable("Contact", contact);
                        Intent intent =new Intent(mContext, ContactDetailsActivity.class);
                        intent.putExtras(extras);
                        mContext.startActivity(intent);
                    }
                }
            }
        }
    }

    @Override
    public boolean onItemLongClicked(int position) {
        if (!mHistoryAdapter.isEditionEnabled()) {
            mSelectionHelper.enterEditionMode();
        }
        mHistoryAdapter.toggleSelection(position);
        return true;
    }

    @Override
    public void onDeleteBtnClick(int position) {
        if (BeautyMirrorActivity.isInstanciated()) {

        }
    }

    private ArrayList<Object> getCalllogsAndNotifications(){
        ArrayList<Object> arrayList = null;
        if(DataService.isReady()){
            arrayList = DataService.instance().getCalllogsAndNotifications();
        }
        return arrayList;
    }

    public int getMissedCallLogsAndNotifications(){
        int count =0;
        ArrayList<Object> mMissedLogs = getCalllogsAndNotifications();
        if(mMissedLogs!=null && mMissedLogs.size()>0){
            for(int i = 0; i < mMissedLogs.size(); i++){
                if (mMissedLogs.get(i) instanceof CallLog) {
                    if(((CallLog) mMissedLogs.get(i)).mRead == 0 ){
                        count++;
                    }
                }else{
                    if(((Notification) mMissedLogs.get(i)).mRead == 0 ){
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public void setReadedCallLogsAndNotifications(){
        if(mLogs!=null && mLogs.size()>0){
            for(int i = 0; i < mLogs.size(); i++){
                if (mLogs.get(i) instanceof CallLog) {
                    ((CallLog) mLogs.get(i)).mRead =1;
                    DataService.instance().readCalllog();
                }else{
                    ((Notification) mLogs.get(i)).mRead =1;
                    DataService.instance().readNotification();
                }
            }
        }
    }
}