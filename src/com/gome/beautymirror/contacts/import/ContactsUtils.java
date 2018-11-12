package com.gome.beautymirror.contacts;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import com.github.promeg.pinyinhelper.Pinyin;
import com.gome.beautymirror.data.DataService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static android.database.Cursor.FIELD_TYPE_STRING;

/**
 * 联系人工具类
 */

public class ContactsUtils {

    private static Map<String, byte[]> photoMap = new HashMap<>();


    /**
     * 异步返回联系人列表
     */
    public static ArrayList<ContactInfo> getContactList(Context context) {
        final ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = DataService.instance().getPeoples(null, null, null, null);
        ArrayList<ContactInfo> contactsInfos = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    String phoneNumber = cursor.getString(3);
                    if (phoneNumber == null || phoneNumber.trim().equals("")) {
                        continue;
                    } else {
                        ContactInfo info = new ContactInfo();
                        info.setContactId(cursor.getInt(0));
                        info.setContactName(cursor.getString(2));
                        info.setPhoneNumber(phoneNumber);
                        String name = cursor.getString(5);
                        if (name == null || name.trim().equals("")) {
                            name = phoneNumber;
                        }
                        info.setName(name);
                        info.setPhoto(cursor.getBlob(6));
                        info.setAddStatu(cursor.getInt(8) == 1 ? true : false);
                        String letter = String.valueOf(Pinyin.toPinyin(name.charAt(0)).toUpperCase().charAt(0));
                        //非字母开头的统一设置成 "#"
                        if (isLetter(letter)) {
                            info.setLetter(letter);
                        } else {
                            info.setLetter("#");
                        }
                        contactsInfos.add(info);
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        compare(contactsInfos);
        return contactsInfos;
    }

    /**
     * 把联系人按照a b c升序排列
     */
    private static ArrayList<ContactInfo> compare(ArrayList<ContactInfo> contactInfos) {
        Collections.sort(contactInfos, new Comparator<ContactInfo>() {
            @Override
            public int compare(ContactInfo o1, ContactInfo o2) {
                //升序排列
                if (o1.getLetter().equals("@")
                        || o2.getLetter().equals("#")) {
                    return -1;
                } else if (o1.getLetter().equals("#")
                        || o2.getLetter().equals("@")) {
                    return 1;
                }
                return o1.getLetter().compareTo(o2.getLetter());
            }
        });
        return contactInfos;
    }

    /**
     * 判断字符是否是字母
     */
    public static boolean isLetter(String s) {
        char c = s.charAt(0);
        int i = (int) c;
        if ((i >= 65 && i <= 90) || (i >= 97 && i <= 122)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 模糊搜索（按中文，数字，字母搜索）
     */
    public static boolean searchContact(String searchStr, String name ,String phone) {
        return name.contains(searchStr) || phone.contains(searchStr)
                || searchLowerByAlphabet(searchStr, name) || searchUpperByAlphabet(searchStr, name)
                || Pinyin.toPinyin(name, "").toLowerCase().contains(searchStr)
                || Pinyin.toPinyin(name, "").toUpperCase().contains(searchStr);
    }

    /**
     * 按中文首字母
     * 如“中国人”可以搜 “zgr”
     */
    private static boolean searchLowerByAlphabet(String searchStr, String name) {
        String[] temp = Pinyin.toPinyin(name, ",").toLowerCase().split(",");
        StringBuilder builder = new StringBuilder();
        for (String str : temp) {
            builder.append(str.charAt(0));
        }
        if (builder.toString().contains(searchStr)) {
            return true;
        }
        return false;
    }

    /**
     * 按中文首字母
     * 如“中国人”可以搜 “ZGR”
     */
    private static boolean searchUpperByAlphabet(String searchStr, String name) {
        String[] temp = Pinyin.toPinyin(name, ",").toUpperCase().split(",");
        StringBuilder builder = new StringBuilder();
        for (String str : temp) {
            builder.append(str.charAt(0));
        }
        if (builder.toString().contains(searchStr)) {
            return true;
        }
        return false;
    }

}
