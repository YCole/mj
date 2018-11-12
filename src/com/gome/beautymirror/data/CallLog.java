package com.gome.beautymirror.data;

import android.graphics.Bitmap;

import com.gome.beautymirror.data.provider.DatabaseUtil;

public class CallLog {

    public Long mTime;
    public String mAccount;
    public String mId;
    public Long mEndTime;
    public int mDuration;
    public int mStatus;
    public int mRead;
    public String mName;
    public Bitmap mIcon;
    public String mComment;

    public CallLog(Long time, String account, String id, Long end_time, int duration, int status, int read, String name, Bitmap icon, String comment) {
        mTime = time;
        mAccount = account;
        mId = id;
        mEndTime = end_time;
        mDuration = duration;
        mStatus = status;
        mRead = read;
        mName = name;
        mIcon = icon;
        mComment = comment;
    }

    public String toString() {
        return new StringBuilder().append("time:" + mTime + " ")
                .append("account:" + mAccount + " ")
                .append("id:" + mId + " ")
                .append("end_time:" + mEndTime + " ")
                .append("duration:" + mDuration + " ")
                .append("status:" + mStatus + " ")
                .append("read:" + (mRead == DatabaseUtil.Calllog.READ_NEW ? "New" : "Old") + " ")
                .append("name:" + mName + " ")
                .append("icon:" + mIcon + " ")
                .append("comment:" + mComment + " ").toString();
    }
}
