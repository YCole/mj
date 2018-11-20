package gome.beautymirror.contacts.newfriend;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gome.beautymirror.R;

import java.util.ArrayList;

import com.gome.beautymirror.data.DataService;

public class RequestAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;

    private ArrayList<RequestInfo> mData = new ArrayList();

    public RequestAdapter(Context context, ArrayList<RequestInfo> data) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.contact_friend_request_item, null);
            holder.mAvatar=convertView.findViewById(R.id.iv_avatar);
            holder.mName = convertView.findViewById(R.id.tv_name);
            holder.mNumber = convertView.findViewById(R.id.tv_number);
            holder.mAccept = convertView.findViewById(R.id.accept);
            holder.mRefuse = convertView.findViewById(R.id.refuse);
            holder.mTvHandleResult = convertView.findViewById(R.id.tv_handle_result);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        initView(holder, position);

        return convertView;
    }


    private void initView(final ViewHolder holder, final int position) {
        if(mData.get(position).getName()==null || mData.get(position).getName().equals("")){
            holder.mName.setText(mData.get(position).getAccount());
        }else{
            holder.mName.setText(mData.get(position).getName());
        }
        holder.mNumber.setText(mData.get(position).getMessage());

        Glide.with(mContext)
                .load(mData.get(position).getAvatar())
                .transform(new com.gome.beautymirror.contacts.GlideCircleTransform(mContext))
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.mAvatar);

        int mFlag = mData.get(position).getHandleFlag();
        if (mFlag == 0) {
            holder.mAccept.setVisibility(View.VISIBLE);
            holder.mRefuse.setVisibility(View.VISIBLE);
            holder.mTvHandleResult.setVisibility(View.GONE);
            holder.mAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataService.instance().confirmProposer(null, mData.get(position).getAccount(), 1, new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                if (DataService.checkResult(msg)) {
                                    holder.mAccept.setVisibility(View.GONE);
                                    holder.mRefuse.setVisibility(View.GONE);
                                    holder.mTvHandleResult.setVisibility(View.VISIBLE);
                                    holder.mTvHandleResult.setText(mContext.getResources().getString(R.string.contact_accepted));
                                } else {
                                    Toast.makeText(mContext, "Fail", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, 0);
                }
            });
            holder.mRefuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataService.instance().confirmProposer(null, mData.get(position).getAccount(), 0, new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                if (DataService.checkResult(msg)) {
                                    holder.mAccept.setVisibility(View.GONE);
                                    holder.mRefuse.setVisibility(View.GONE);
                                    holder.mTvHandleResult.setVisibility(View.VISIBLE);
                                    holder.mTvHandleResult.setText(mContext.getResources().getString(R.string.contact_refused));
                                } else {
                                    Toast.makeText(mContext, "Fail", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, 0);
                }
            });
        } else if (mFlag == 1) {
            holder.mAccept.setVisibility(View.GONE);
            holder.mRefuse.setVisibility(View.GONE);
            holder.mTvHandleResult.setVisibility(View.VISIBLE);
            holder.mTvHandleResult.setText(mContext.getResources().getString(R.string.contact_accepted));

        } else {
            holder.mAccept.setVisibility(View.GONE);
            holder.mRefuse.setVisibility(View.GONE);
            holder.mTvHandleResult.setVisibility(View.VISIBLE);
            holder.mTvHandleResult.setText(mContext.getResources().getString(R.string.contact_refused));
        }

    }


    private final class ViewHolder {
        private ImageView mAvatar;
        private TextView mName;
        private TextView mNumber;
        private TextView mAccept;
        private TextView mRefuse;
        private TextView mTvHandleResult;
    }

}
