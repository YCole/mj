package com.gome.beautymirror.contacts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gome.beautymirror.R;

import java.util.ArrayList;

import gome.beautymirror.ui.MyToast;

import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * 联系人Adapter
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHodle> {

    private Context mContext;
    private ArrayList<ContactInfo> mContactList;

    public static final int FIRST_STICKY_VIEW = 0; // 第一行
    public static final int HAS_STICKY_VIEW = 2;//有头部字符
    public static final int NONE_STICKY_VIEW = 3;

    RequestOptions mRequestOptions;

    public ContactAdapter(Context context, ArrayList<ContactInfo> list) {
        this.mContext = context;
        this.mContactList = list;
        mRequestOptions = RequestOptions.circleCropTransform()
                .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
                .skipMemoryCache(true);//不做内存缓存
        mRequestOptions.placeholder(R.drawable.image_head)
                .error(R.drawable.image_head);
    }

    @Override
    public ContactAdapter.MyViewHodle onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHodle(LayoutInflater.from(mContext).inflate(R.layout.item_contacts_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ContactAdapter.MyViewHodle holder, final int position) {
        ContactInfo contactInfo = mContactList.get(position);
        holder.name.setText(contactInfo.getName());
        holder.phone.setText(contactInfo.getPhoneNumber());

        //判断是否显示索引字母
        String currentLetter = contactInfo.getLetter();
        String previousLetter = position >= 1 ? mContactList.get(position - 1).getLetter() : "";

        if (position == 0) {
            holder.letter.setVisibility(View.VISIBLE);
            holder.letter.setText(currentLetter);
            holder.itemView.setTag(FIRST_STICKY_VIEW);
        } else if (position < mContactList.size()) {
            if (!TextUtils.equals(currentLetter, previousLetter)) {
                holder.letter.setVisibility(View.VISIBLE);
                holder.letter.setText(currentLetter);
                holder.itemView.setTag(HAS_STICKY_VIEW);
            } else {
                holder.letter.setVisibility(View.GONE);
                holder.itemView.setTag(NONE_STICKY_VIEW);
            }
        }
        holder.itemView.setContentDescription(currentLetter);

        //是否已添加及选择模式
        boolean addStatu = contactInfo.getAddStatu();
        if (addStatu) {
            holder.add.setVisibility(View.VISIBLE);
            holder.add.setText(mContext.getResources().getString(R.string.added_contact));
        } else {
            holder.add.setVisibility(View.VISIBLE);
            holder.add.setText(mContext.getResources().getString(R.string.add_contact));
        }

        //加载联系人头像
        Glide.with(mContext)
                .load(contactInfo.getPhoto())
                .apply(mRequestOptions)
                .into(holder.iv);

        //条目点击事件
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContactList.get(position).getAddStatu()) {
                    MyToast.showToast(mContext, mContext.getResources().getString(R.string.added_contact), Toast.LENGTH_SHORT);
                    return;
                }
                mOnItemClickListener.onItemClick(mContactList.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mContactList == null ? 0 : mContactList.size();
    }

    public void setContactList(ArrayList<ContactInfo> contactList) {
        mContactList = contactList;
        notifyDataSetChanged();
    }

    /**
     * 刷新数据
     */
    public void notifyRefreshData() {
        notifyDataSetChanged();
    }

    public class MyViewHodle extends RecyclerView.ViewHolder {

        private LinearLayout itemLayout;
        private TextView letter;
        private ImageView iv;
        private TextView name;
        private TextView phone;
        private TextView add;

        public MyViewHodle(View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.item_layout);
            letter = itemView.findViewById(R.id.tv_contact_header);
            add = itemView.findViewById(R.id.add);
            iv = itemView.findViewById(R.id.iv);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ContactInfo info, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
