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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.beautymirror.LinphoneUtils;
import com.gome.beautymirror.R;
import com.gome.beautymirror.activities.BeautyMirrorActivity;
import com.gome.beautymirror.contacts.ContactsManager;
import com.gome.beautymirror.ui.SelectableAdapter;
import com.gome.beautymirror.ui.SelectableHelper;
import com.gome.beautymirror.ui.SlidingButtonView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;

import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.util.List;
import java.util.Map;
import android.graphics.Typeface;
import android.widget.Toast;

import com.gome.beautymirror.data.CallLog;
import com.gome.beautymirror.data.Notification;
import com.gome.beautymirror.data.provider.DatabaseUtil;
import com.gome.beautymirror.advertisement.BannerLayout;
import com.gome.beautymirror.advertisement.PicassoImageLoader;
public class CallHistoryAdapter extends SelectableAdapter<CallHistoryAdapter.ViewHolder> implements SlidingButtonView.IonSlidingButtonListener {
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {
        public TextView contact;
        public CheckBox select;
        public ImageView callDirection;
        public ImageView contactPicture;
        public ImageView vedioCall;
        public LinearLayout CallContact;
        public TextView callDate;
        private CallHistoryAdapter.ViewHolder.ClickListener mListener;
        private SlidingButtonView slidingButtonView;
        private ViewGroup layout_content;
        private TextView btn_Delete;
        private TextView count,content;
        private RelativeLayout historyClickLayout;

        public ViewHolder(View view, CallHistoryAdapter.ViewHolder.ClickListener listener) {
            super(view);
            slidingButtonView = (SlidingButtonView) view;
            layout_content = (ViewGroup) itemView.findViewById(R.id.layout_content);
            btn_Delete = (TextView) itemView.findViewById(R.id.history_delete);
            contact = view.findViewById(R.id.sip_uri);
            select = view.findViewById(R.id.delete);
            callDirection = view.findViewById(R.id.icon);
            contactPicture = view.findViewById(R.id.contact_picture);
            callDate = view.findViewById(R.id.call_date);
            count = view.findViewById(R.id.count);
            content = view.findViewById(R.id.content);
            vedioCall = view.findViewById(R.id.vedio_call);
            historyClickLayout = view.findViewById(R.id.history_click);
            mListener = listener;
            layout_content.setOnClickListener(this);
            vedioCall.setOnClickListener(this);
            historyClickLayout.setOnClickListener(this);
            view.setOnLongClickListener(this);
            btn_Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onDeleteBtnClick(getAdapterPosition());
                }
            });
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

    private boolean allOpen = false;
    public void setAllOpen(boolean allOpen) {
        this.allOpen = allOpen;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_cell_item, parent, false);
        return new ViewHolder(v, clickListener);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,  int position) {
        holder.slidingButtonView.setSlidingButtonListener(this);
        Map.Entry element = (Map.Entry) mList_key.get(position);
        if(element.getKey() instanceof CallLog){
            final CallLog log = (CallLog)element.getKey();
            if(log.mStatus == DatabaseUtil.Calllog.STATUS_MISSED){
                holder.callDirection.setImageResource(R.drawable.call_status_missed);
                holder.content.setText(mContext.getString(R.string.missed_calls_notif_title));
            }else if(log.mStatus == DatabaseUtil.Calllog.STATUS_INCOMING){
                holder.callDirection.setImageResource(R.drawable.call_status_incoming);
                holder.content.setText(mContext.getString(R.string.incoming_calls_notif_title));
            }else if(log.mStatus == DatabaseUtil.Calllog.STATUS_OUTGOING){
                holder.callDirection.setImageResource(R.drawable.call_status_outgoing);
                holder.content.setText(mContext.getString(R.string.outgoing_calls_notif_title));
            }
            holder.contactPicture.setImageBitmap(ContactsManager.getInstance().getDefaultAvatarBitmap());
            holder.callDate.setText(updateRelativeTime(log));
            int strValue = (int)element.getValue();
            holder.count.setText("("+strValue+")");
            if(log.mComment!=null){
                holder.contact.setText(log.mComment);
            }else{
                holder.contact.setText(log.mAccount);
            }

        }else if(element.getKey() instanceof Notification){//mList_key.get(position)
            final Notification mNotification = (Notification)element.getKey();
            holder.callDirection.setVisibility(View.GONE);
            holder.contact.setText(mNotification.mName+mContext.getString(R.string.nofiticafion_title));
            holder.contact.setTextSize(14);
            holder.contact.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            holder.content.setText(mContext.getString(R.string.nofiticafion_message));
        }

        holder.contact.setSelected(true); // For automated horizontal scrolling of long texts
        Calendar logTime = Calendar.getInstance();
        holder.select.setVisibility(isEditionEnabled() ? View.VISIBLE : View.GONE);
        holder.select.setChecked(isSelected(position));

//        LinphoneContact c = ContactsManager.getInstance().findContactFromAddress(address);
//        if (c != null) {
//            LinphoneUtils.setThumbnailPictureFromUri(BeautyMirrorActivity.instance(), holder.contactPicture, c.getThumbnailUri());
//        } else {
//            holder.contactPicture.setImageBitmap(ContactsManager.getInstance().getDefaultAvatarBitmap());
//        }

        holder.layout_content.getLayoutParams().width =getScreenWidth(mContext) ;//+ viewHolder.rl_left.getLayoutParams().width;

        if (allOpen) {
            holder.slidingButtonView.openMenu();
            holder.slidingButtonView.setCanTouch(false);
        } else {
            holder.slidingButtonView.closeMenu();
            holder.slidingButtonView.setCanTouch(true);
        }
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

    private String updateRelativeTime(CallLog log) {
        Long now = System.currentTimeMillis()/1000;
        Long duration = Math.abs(now - log.mTime);
        int count;
        String result;
        if (duration < 60) {
            result = mContext.getString(R.string.now_string_shortest);
        } else if (duration < 60*10) {
            count = (int)(duration / 60);
            result = String.format(mContext.getResources().getString(R.string.duration_minutes_shortest),count);

        } else{
            Long longDate = Long.parseLong(String.valueOf(log.mTime));
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

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
}