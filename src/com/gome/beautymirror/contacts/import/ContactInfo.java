package com.gome.beautymirror.contacts;

/**
 * 联系人Bean
 */

public class ContactInfo {

    /**
     * 联系人ID
     */
    private int contactId;

    private String account;

    /**
     * 联系人名称的首字母
     */
    private String letter;

    /**
     * 联系人显示的名称
     */
    private String contactName;
    /**
     * 账号昵称
     */
    private String name;
    /**
     * 联系人的手机号码, 有可能是多个. 同一个联系人的不同手机号码,视为多个联系人
     */
    private String phoneNumber;

    private byte[] photo;
    /**
     * 是否添加联系人
     */
    private boolean addStatu;



    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public Boolean getAddStatu() {
        return addStatu;
    }

    public void setAddStatu(Boolean addStatu) {
        this.addStatu = addStatu;
    }
}
