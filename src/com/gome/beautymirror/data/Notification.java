package com.gome.beautymirror.data;

import android.graphics.Bitmap;

import com.gome.beautymirror.data.provider.DatabaseUtil;

public class Notification {

    public Long mTime;
    public String mAccount;
    public String mId;
    public int mRequest;
    public int mRead;
    public String mName;
    public Bitmap mIcon;
    public String mComment;

    public Notification(Long time, String account, String id, int request, int read, String name, Bitmap icon, String comment) {
        mTime = time;
        mAccount = account;
        mId = id;
        mRequest = request;
        mRead = read;
        mName = name;
        mIcon = icon;
        mComment = comment;
    }

    public String toString() {
        return new StringBuilder().append("time:" + mTime + " ")
                .append("account:" + mAccount + " ")
                .append("id:" + mId + " ")
                .append("request:" + (mRequest == DatabaseUtil.Notification.REQUEST_FRIEND ? "Request"
                        : (mRequest == DatabaseUtil.Notification.REQUEST_CONFIRM ? "Confirm" : "Confirmed")) + " ")
                .append("read:" + (mRead == DatabaseUtil.Notification.READ_NEW ? "New" : "Old") + " ")
                .append("name:" + mName + " ")
                .append("icon:" + mIcon + " ")
                .append("comment:" + mComment + " ").toString();
    }
}
