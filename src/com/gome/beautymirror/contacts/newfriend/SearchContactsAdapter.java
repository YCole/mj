package gome.beautymirror.contacts.newfriend;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RelativeLayout;
import java.util.regex.Pattern;

import com.gome.beautymirror.LinphoneUtils;
import com.gome.beautymirror.R;
import com.gome.beautymirror.contacts.ContactsManager;
import com.gome.beautymirror.contacts.LinphoneContact;
import com.gome.beautymirror.contacts.LinphoneNumberOrAddress;

import java.util.List;

public class SearchContactsAdapter extends RecyclerView.Adapter<SearchContactsAdapter.ViewHolder>  {
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        public TextView name;
        public TextView mInputSipUri, mNoInputSipUri;
        public ImageView contactPicture;
        private ClickListener mListener;
        public RelativeLayout contactCall;

        private ViewHolder(View view ,ClickListener listener) {
            super(view);
            mListener = listener;
            name =  view.findViewById(R.id.name);
            mInputSipUri = view.findViewById(R.id.input_sip_uri);
            mNoInputSipUri = view.findViewById(R.id.no_input_sip_uri);
            contactPicture = view.findViewById(R.id.contact_picture);
            contactCall =  view.findViewById(R.id.contact_call);
            view.setOnClickListener(this);
            contactCall.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onSearchItemClicked(getAdapterPosition(),
                        view.getId()==R.id.contact_call);
            }
        }

        public boolean onLongClick(View v) {
            if (mListener != null) {
                return mListener.onItemLongClicked(getAdapterPosition());
            }
            return false;
        }

        public interface ClickListener {
            void onSearchItemClicked(int position ,boolean isCall);

            boolean onItemLongClicked(int position);
        }
    }

    private List<LinphoneContact> mContacts;
    private Context mContext;
    private ViewHolder.ClickListener mClickListener;
    private String mSearchKey;

    public SearchContactsAdapter(Context context, List<LinphoneContact> searchList, ViewHolder.ClickListener clickListener,String searchKey) {
        mContext = context;
        mContacts = searchList;
        mClickListener = clickListener;
        mSearchKey = searchKey;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_contacts_cell, parent, false);
        return new ViewHolder(v, mClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        LinphoneContact contact = (LinphoneContact) getItem(position);

        for (LinphoneNumberOrAddress noa : contact.getNumbersOrAddresses()) {
            String value = noa.getValue();
            String displayednumberOrAddress = LinphoneUtils.getDisplayableUsernameFromAddress(value);
            if( isInteger(mSearchKey)){
                displayednumberOrAddress =  displayednumberOrAddress.substring(mSearchKey.length(), displayednumberOrAddress.length());
                holder.mInputSipUri.setText(mSearchKey);
                holder.mInputSipUri.setTextColor(Color.parseColor("#FFFB5392"));
            }
            holder.mNoInputSipUri.setText(displayednumberOrAddress);
        }
        String remark = contact.getRemarkName();
        if (!TextUtils.isEmpty(remark)) {
            holder.name.setText(remark);
        } else
            holder.name.setText(contact.getFullName());

        holder.contactPicture.setImageBitmap(ContactsManager.getInstance().getDefaultAvatarBitmap());
        /*if (contact.hasPhoto()) {
            LinphoneUtils.setThumbnailPictureFromUri(BeautyMirrorActivity.instance(), holder.contactPicture, contact.getThumbnailUri());
        }*/
        byte[] icon = contact.getIcon();
        if (icon != null) {
            holder.contactPicture.setImageBitmap(com.gome.beautymirror.data.DataUtil.getImage(icon));
        }

    }

    private boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public Object getItem(int position) {
        if (position >= getItemCount() )return null;
        return mContacts.get(position);
    }

}
