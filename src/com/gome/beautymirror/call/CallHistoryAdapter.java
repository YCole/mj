package com.gome.beautymirror.call;

/*
 CallHistoryAdapter.java
 Copyright (C) 2018  Belledonne Communications, Grenoble, France

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

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gome.beautymirror.LinphoneUtils;
import com.gome.beautymirror.R;
import com.gome.beautymirror.ui.SelectableAdapter;
import com.gome.beautymirror.ui.SelectableHelper;
import com.gome.beautymirror.ui.SlidingButtonView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Map;

import com.gome.beautymirror.data.CallLog;
import com.gome.beautymirror.data.Information;
import com.gome.beautymirror.data.provider.DatabaseUtil;
import com.gome.beautymirror.ui.RoundImageView;

public class CallHistoryAdapter extends SelectableAdapter<CallHistoryAdapter.ViewHolder> implements SlidingButtonView.IonSlidingButtonListener {
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {
        public TextView mCallName,mInformationConfirmed,mInformationConfirm;
        public ImageView callDirection;
        public RoundImageView contactPicture;
        public ImageView vedioCall;
        public TextView callDate, informationData;
        private CallHistoryAdapter.ViewHolder.ClickListener mListener;
        private TextView content,mInformationTitle;
        private LinearLayout mLlCallLog,mLlInformation,historyClickLayout;

        public ViewHolder(View view, CallHistoryAdapter.ViewHolder.ClickListener listener) {
            super(view);
            mCallName = view.findViewById(R.id.call_name);
            mInformationConfirmed = view.findViewById(R.id.information_confirmed);
            mInformationConfirm = view.findViewById(R.id.information_confirm);
            callDirection = view.findViewById(R.id.icon);
            contactPicture = view.findViewById(R.id.contact_picture);
            callDate = view.findViewById(R.id.call_date);
            informationData = view.findViewById(R.id.information_date);
            content = view.findViewById(R.id.content);
            mInformationTitle = view.findViewById(R.id.information_title);
            vedioCall = view.findViewById(R.id.vedio_call);
            historyClickLayout = view.findViewById(R.id.history_click);
            mLlCallLog = view.findViewById(R.id.ll_call_log);
            mLlInformation = view.findViewById(R.id.ll_information);
            mListener = listener;
            vedioCall.setOnClickListener(this);
            historyClickLayout.setOnClickListener(this);
            view.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClicked(getAdapterPosition(),(view.getId()==R.id.vedio_call));
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (mListener != null) {
                return mListener.onItemLongClicked(getAdapterPosition());
            }
            return false;
        }

        public interface ClickListener {
            void onItemClicked(int position,boolean isVedioCall);
            boolean onItemLongClicked(int position);
            void onDeleteBtnClick(int position);
        }
    }

    private ArrayList<Object> mList_key;
    private Context mContext;
    private CallHistoryAdapter.ViewHolder.ClickListener clickListener;
    private SlidingButtonView mMenu = null;

    public CallHistoryAdapter(Context aContext,ArrayList<Object> list_key, CallHistoryAdapter.ViewHolder.ClickListener listener, SelectableHelper helper) {
        super(helper);
        this.mContext = aContext;
        this.clickListener = listener;
        this.mList_key = list_key;
    }

    public int getCount() {
        return mList_key.size();
    }

    public Object getItem(int position) {
        return mList_key.get(position);
    }

    public void updateDataSet(ArrayList<Object> list_key) {
        mList_key = list_key;
        notifyDataSetChanged();
    }

    private boolean allOpen = false;
    public void setAllOpen(boolean allOpen) {
        this.allOpen = allOpen;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_cell, parent, false);
        return new ViewHolder(v, clickListener);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,  int position) {
        Map.Entry element = (Map.Entry) mList_key.get(position);
        if(element.getKey() instanceof CallLog){
            holder.mLlInformation.setVisibility(View.GONE);
            final CallLog log = (CallLog)element.getKey();
            if(log.mStatus == DatabaseUtil.Calllog.STATUS_MISSED){
                holder.callDirection.setImageResource(R.drawable.bg_ciricle_call_miss);
                holder.content.setText(mContext.getString(R.string.missed_calls_notif_title));
            }else if(log.mStatus == DatabaseUtil.Calllog.STATUS_INCOMING){
                holder.callDirection.setImageResource(R.drawable.bg_ciricle_call_in);
                holder.content.setText(mContext.getString(R.string.incoming_calls_notif_title));
            }else if(log.mStatus == DatabaseUtil.Calllog.STATUS_OUTGOING){
                holder.callDirection.setImageResource(R.drawable.bg_ciricle_call_out);
                holder.content.setText(mContext.getString(R.string.outgoing_calls_notif_title));
            }
            if(log.mIcon != null){
                holder.contactPicture.setImageBitmap(log.mIcon);
            }
            holder.callDate.setText(updateRelativeTime(log.mTime));
            int strValue = (int)element.getValue();

            if(log.mComment!=null && !log.mComment.equals("")){
                holder.mCallName.setText(log.mComment);
            }else if(log.mName != null && !log.mName.equals("")){
                holder.mCallName.setText(log.mName);
            }else if(log.mAccount != null && !log.mAccount.equals("")){
                holder.mCallName.setText(log.mAccount);
            }
            if(strValue > 1){
                holder.mCallName.setText(holder.mCallName.getText()+"（ "+strValue+" ）");
            }

        }else if(element.getKey() instanceof Information){
            holder.mLlCallLog.setVisibility(View.GONE);
            final Information mInformation = (Information)element.getKey();
            if(mInformation.mIcon != null){
                holder.contactPicture.setImageBitmap(mInformation.mIcon);
            }
            if (mInformation.mRequest == DatabaseUtil.Information.REQUEST_CONFIRMED) {
                holder.mInformationConfirmed.setText(mInformation.mName);
                holder.mInformationTitle.setText(mContext.getString(R.string.information_title_confirmed));
                holder.mInformationConfirm.setVisibility(View.GONE);
            } else {
                holder.mInformationConfirmed.setVisibility(View.GONE);
                holder.mInformationTitle.setText(mContext.getString(R.string.information_title_confirm));
                holder.mInformationConfirm.setText(mInformation.mName);
            }
            holder.informationData.setText(updateRelativeTime(mInformation.mTime));
        }

        holder.mCallName.setSelected(true); // For automated horizontal scrolling of long texts

    }

    @Override
    public int getItemCount() {
        return mList_key.size();
    }

    @SuppressLint("SimpleDateFormat")
    private String timestampToHumanDate(Calendar cal) {
        SimpleDateFormat dateFormat;
        if (isToday(cal)) {
            return mContext.getString(R.string.today);
        } else if (isYesterday(cal)) {
            return mContext.getString(R.string.yesterday);
        } else {
            dateFormat = new SimpleDateFormat(mContext.getResources().getString(R.string.history_date_format));
        }

        return dateFormat.format(cal.getTime());
    }

    private boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            return false;
        }

        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    private boolean isToday(Calendar cal) {
        return isSameDay(cal, Calendar.getInstance());
    }

    private boolean isYesterday(Calendar cal) {
        Calendar yesterday = Calendar.getInstance();
        yesterday.roll(Calendar.DAY_OF_MONTH, -1);
        return isSameDay(cal, yesterday);
    }

    private String updateRelativeTime(Long time) {
        Long now = System.currentTimeMillis()/1000;
        Long duration = Math.abs(now - time);
        int count;
        String result;
        if (duration < 60) {
            result = mContext.getString(R.string.now_string_shortest);
        } else if (duration < 60*10) {
            count = (int)(duration / 60);
            result = String.format(mContext.getResources().getString(R.string.duration_minutes_shortest),count);

        } else{
            Long longDate = Long.parseLong(String.valueOf(time));
            result = LinphoneUtils.timestampToHumanDate(mContext, longDate, mContext.getString(R.string.history_detail_date_format));
        }
        return result;
    }

    @Override
    public void onDownOrMove(SlidingButtonView slidingButtonView) {
        if (menuIsOpen()) {
            if (mMenu != slidingButtonView) {
                closeMenu();
            }
        }
    }

    public Boolean menuIsOpen() {
        if (mMenu != null) {
            return true;
        }
        return false;
    }

    public void closeMenu() {
        mMenu.closeMenu();
        mMenu = null;

    }

    @Override
    public void onMenuIsOpen(View view) {
        mMenu = (SlidingButtonView) view;
    }

}