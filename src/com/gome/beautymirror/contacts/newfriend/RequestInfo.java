package gome.beautymirror.contacts.newfriend;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import com.gome.beautymirror.data.DataUtil;

public class RequestInfo implements Parcelable {
    private String account;
    private byte[] avatar;
    private String name;
    private String message;
    private int handleFlag;    // 未处理：0 接受：1 拒绝：2

    public RequestInfo(byte[] avatar ,String account, String name, String message, int flag) {
        this.avatar = avatar;
        this.account = account;
        this.name = name;
        this.message = message;
        this.handleFlag = flag;
    }

    public RequestInfo() {
    }

    public void setHandleFlag(int handleFlag) {
        this.handleFlag = handleFlag;
    }

    public int getHandleFlag() {
        return handleFlag;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setMessage(String info) {
        this.message = info;
    }

    public String getMessage() {
        return message;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public byte[] getAvatar() {
        return avatar;
    }


    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(avatar.length);
        out.writeByteArray(avatar);
        out.writeString(account);
        out.writeString(name);
        out.writeString(message);
        out.writeInt(handleFlag);
    }

    public static final Parcelable.Creator<RequestInfo> CREATOR = new Parcelable.Creator<RequestInfo>() {
        public RequestInfo createFromParcel(Parcel source) {
            RequestInfo requestInfo = new RequestInfo();
            byte[] bytes=new byte[source.readInt()];
            source.readByteArray(bytes);
            requestInfo.setAvatar(bytes);
            requestInfo.setAccount(source.readString());
            requestInfo.setName(source.readString());
            requestInfo.setMessage(source.readString());
            requestInfo.setHandleFlag(source.readInt());
            return requestInfo;
        }

        public RequestInfo[] newArray(int size) {
            return new RequestInfo[size];
        }
    };

}
