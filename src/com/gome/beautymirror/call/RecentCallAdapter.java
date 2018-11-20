package com.gome.beautymirror.call;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gome.beautymirror.LinphoneUtils;
import com.gome.beautymirror.R;
import com.gome.beautymirror.data.CallLog;
import com.gome.beautymirror.data.provider.DatabaseUtil;
import java.util.ArrayList;

public class RecentCallAdapter extends RecyclerView.Adapter<RecentCallAdapter.ViewHolder>  {
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView callDuration, callDate, callAccount;
        public ImageView callDirection;

        private ViewHolder(View view) {
            super(view);
            callDuration = view.findViewById(R.id.call_duration);
            callDate = view.findViewById(R.id.call_date);
            callAccount = view.findViewById(R.id.call_account);
            callDirection = view.findViewById(R.id.icon);
        }
    }

    private ArrayList<CallLog> mLogs;
    private Context mContext;
    private final int MINUTE_IN_MILLIS = 60;

    public RecentCallAdapter(Context context,ArrayList<CallLog> logs ) {
        mContext = context;
        mLogs = logs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_call_cell, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        CallLog log = (CallLog) getItem(position);
        holder.callDate.setText(updateRelativeTime(log));
        if("".equals(log.mId)){
            holder.callAccount.setText(log.mAccount);
        } else{
            if(log.mComment!=null && !log.mComment.equals("")){
                holder.callAccount.setText(log.mComment+mContext.getResources().getString(R.string.devices));
            }else if(log.mName!=null && !log.mName.equals("")){
                holder.callAccount.setText(log.mName+mContext.getResources().getString(R.string.devices));
            }else {
                holder.callAccount.setText(log.mAccount+mContext.getResources().getString(R.string.devices));
            }
        }

       if(log.mStatus == DatabaseUtil.Calllog.STATUS_MISSED){
            holder.callDirection.setImageResource(R.drawable.ic_call_miss);
           holder.callDate.setTextColor(Color.parseColor("#FFFF7786"));
           holder.callDuration.setText(mContext.getResources().getString(R.string.missed_call)+getDurationText((int)(log.mEndTime-log.mTime)));
        }else{
           holder.callDate.setTextColor(Color.parseColor("#E6000000"));
           holder.callDuration.setText(getDurationText(log.mDuration));
           if(log.mStatus == DatabaseUtil.Calllog.STATUS_INCOMING){
               holder.callDirection.setImageResource(R.drawable.ic_call_in);
           }else if(log.mStatus == DatabaseUtil.Calllog.STATUS_OUTGOING){
               holder.callDirection.setImageResource(R.drawable.ic_call_out);
           }
       }
    }

    private String updateRelativeTime(CallLog log) {
        Long now = System.currentTimeMillis()/1000;
        Long duration = Math.abs(now - log.mTime);
        int count;
        String result;
        if (duration < MINUTE_IN_MILLIS) {
            result = mContext.getString(R.string.now_string_shortest);
        } else if (duration < MINUTE_IN_MILLIS*10) {
            count = (int)(duration / MINUTE_IN_MILLIS);
            result = String.format(mContext.getResources().getString(R.string.duration_minutes_shortest),count);

        } else{
            Long longDate = Long.parseLong(String.valueOf(log.mTime));
            result = LinphoneUtils.timestampToHumanDate(mContext, longDate, mContext.getString(R.string.history_detail_date_format));
        }
        return result;
    }

    private String getDurationText(int duration) {
        String result = "";
        if(duration < MINUTE_IN_MILLIS){
            result =String.format(mContext.getResources().getString(R.string.duration_seconds),duration);
        }else {
            int min = duration/MINUTE_IN_MILLIS;
            int min1 = duration%MINUTE_IN_MILLIS;
            result =String.format(mContext.getResources().getString(R.string.duration_minutes_seconds),min,min1);
        }

        return result;
    }

    @Override
    public int getItemCount() {
        return mLogs.size();
    }

    public Object getItem(int position) {
        if (position >= getItemCount() )return null;
        return mLogs.get(position);
    }

}
