package com.gome.beautymirror.contacts;

/*
 ContactsListAdapter.java
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
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.gome.beautymirror.LinphoneUtils;
import com.gome.beautymirror.R;
import com.gome.beautymirror.activities.BeautyMirrorActivity;
import org.linphone.mediastream.Log;
import com.gome.beautymirror.ui.SelectableAdapter;
import com.gome.beautymirror.ui.SelectableHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import android.text.TextUtils;

public class ContactsListAdapter extends SelectableAdapter<ContactsListAdapter.ViewHolder> implements SectionIndexer {
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public CheckBox delete;
        public ImageView linphoneFriend;
        public TextView name;
        public TextView sipUri;
        public LinearLayout separator;
        public ImageView contactPicture;
        public TextView organization;
        //public ImageView friendStatus;
        private ClickListener mListener;
        public RelativeLayout contactCall;
        public TextView headerTextView;
        public TextView contactCount;
        public LinearLayout contactCell;

        private ViewHolder(View view, ClickListener listener) {
            super(view);

            delete = view.findViewById(R.id.delete);
            linphoneFriend = view.findViewById(R.id.friendLinphone);
            name = view.findViewById(R.id.name);
            sipUri = view.findViewById(R.id.sip_uri);
            separator = view.findViewById(R.id.separator);
            contactPicture = view.findViewById(R.id.contact_picture);
            organization = view.findViewById(R.id.contactOrganization);
            //friendStatus = view.findViewById(R.id.friendStatus);
            contactCall =  view.findViewById(R.id.contact_call);
            headerTextView = view.findViewById(R.id.tv_contact_header);
            contactCount = view.findViewById(R.id.contact_count);
            contactCell = view.findViewById(R.id.contact_cell);
            mListener = listener;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            contactCall.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClicked(getAdapterPosition(),view.getId()==R.id.contact_call);
            }
        }

        public boolean onLongClick(View v) {
            if (mListener != null) {
                return mListener.onItemLongClicked(getAdapterPosition());
            }
            return false;
        }

        public interface ClickListener {
            void onItemClicked(int position,boolean isDetail);

            boolean onItemLongClicked(int position);
        }
    }

    private List<LinphoneContact> mContacts;
    private String[] mSections;
    private ArrayList<String> mSectionsList;
    private Map<String, Integer> mMap = new LinkedHashMap<>();
    private ViewHolder.ClickListener mClickListener;
    private Context mContext;
    private boolean mIsSearchMode;
    private boolean mIsSearch;
    public static final int FIRST_STICKY_VIEW = 0; // 第一行
    public static final int HAS_STICKY_VIEW = 2;//有头部字符
    public static final int NONE_STICKY_VIEW = 3;

    ContactsListAdapter(Context context, List<LinphoneContact> contactsList, ViewHolder.ClickListener clickListener, SelectableHelper helper,boolean isSearch) {
        super(helper);
        mContext = context;
        updateDataSet(contactsList);
        mClickListener = clickListener;
        mIsSearch = isSearch;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_cell, parent, false);
        return new ViewHolder(v, mClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        LinphoneContact contact = (LinphoneContact) getItem(position);
        if(mIsSearch){
            holder.contactCount.setVisibility(View.GONE);
        }
        if(contact==null){
            holder.contactCell.setVisibility(View.GONE);
            holder.contactCount.setText(String.format(mContext.getResources().getString(R.string.contact_count), mContacts.size()));
            return;
        }
        if (position == 0 ) {
            holder.headerTextView.setVisibility(View.VISIBLE);
            holder.headerTextView.setText(contact.getLetter());
            holder.itemView.setTag(FIRST_STICKY_VIEW);
        }else if(position < mContacts.size() ){
            if (!TextUtils.equals(contact.getLetter(), ( mContacts.get(position - 1).getLetter())) ){
                holder.headerTextView.setVisibility(View.VISIBLE);
                holder.headerTextView.setText(contact.getLetter());
                holder.itemView.setTag(HAS_STICKY_VIEW);
            } else {
                holder.headerTextView.setVisibility(View.GONE);
                holder.itemView.setTag(NONE_STICKY_VIEW);
            }
        }
        if(position < mContacts.size() ){
            holder.itemView.setContentDescription(contact.getLetter());
            for (LinphoneNumberOrAddress noa : contact.getNumbersOrAddresses()) {
                String value = noa.getValue();
                String displayednumberOrAddress = LinphoneUtils.getDisplayableUsernameFromAddress(value);
                holder.sipUri.setText(displayednumberOrAddress);
            }
            holder.name.setText(contact.getFullName());
            String currentLetter = contact.getLetter();
            String previousLetter = position >= 1 ? mContacts.get(position - 1).getLetter() : "";
            if (!TextUtils.equals(currentLetter, previousLetter)) {
                holder.separator.setVisibility(View.VISIBLE);
            } else {
                holder.separator.setVisibility(View.GONE);
            }
            holder.linphoneFriend.setVisibility(contact.isInFriendList() ? View.VISIBLE : View.GONE);

        holder.contactPicture.setImageBitmap(ContactsManager.getInstance().getDefaultAvatarBitmap());
        if (contact.hasPhoto()) {
            LinphoneUtils.setThumbnailPictureFromUri(BeautyMirrorActivity.instance(), holder.contactPicture, contact.getThumbnailUri());
        }

        boolean isOrgVisible = mContext.getResources().getBoolean(R.bool.display_contact_organization);
        String org = contact.getOrganization();
        if (org != null && !org.isEmpty() && isOrgVisible) {
            holder.organization.setText(org);
            holder.organization.setVisibility(View.VISIBLE);
        } else {
            holder.organization.setVisibility(View.GONE);
        }

            holder.delete.setVisibility(isEditionEnabled() ? View.VISIBLE : View.INVISIBLE);
            holder.delete.setChecked(isSelected(position));
            holder.contactCount.setVisibility(View.GONE);
            holder.contactCell.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mContacts.size()+1;
    }

    public Object getItem(int position) {
        if (position >= (getItemCount()-1)) return null;
        return mContacts.get(position);
    }

    public void setmIsSearchMode(boolean set) {
        mIsSearchMode = set;
    }

    public long getItemId(int position) {
        return position;
    }

    public void updateDataSet(List<LinphoneContact> contactsList) {
        mContacts = contactsList;

        mMap = new LinkedHashMap<>();
        String prevLetter = null;
        for (int i = 0; i < mContacts.size(); i++) {
            LinphoneContact contact = mContacts.get(i);
            String fullName = contact.getFullName();
            if (fullName == null || fullName.isEmpty()) {
                continue;
            }
            String firstLetter = fullName.substring(0, 1).toUpperCase(Locale.getDefault());
            if (!firstLetter.equals(prevLetter)) {
                prevLetter = firstLetter;
                mMap.put(firstLetter, i);
            }
        }
        mSectionsList = new ArrayList<>(mMap.keySet());
        mSections = new String[mSectionsList.size()];
        mSectionsList.toArray(mSections);

        notifyDataSetChanged();
    }

    @Override
    public Object[] getSections() {
        return mSections;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        if (sectionIndex >= mSections.length || sectionIndex < 0) {
            return 0;
        }
        return mMap.get(mSections[sectionIndex]);
    }

    @Override
    public int getSectionForPosition(int position) {
        if (position >= mContacts.size() || position < 0) {
            return 0;
        }
        LinphoneContact contact = mContacts.get(position);
        String fullName = contact.getFullName();
        if (fullName == null || fullName.isEmpty()) {
            return 0;
        }
        String letter = fullName.substring(0, 1).toUpperCase(Locale.getDefault());
        return mSectionsList.indexOf(letter);
    }
}