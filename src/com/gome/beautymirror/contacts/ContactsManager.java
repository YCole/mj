package com.gome.beautymirror.contacts;

/*
ContactsManager.java
Copyright (C) 2017  Belledonne Communications, Grenoble, France

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

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.promeg.pinyinhelper.Pinyin;
import com.gome.beautymirror.LinphoneManager;
import com.gome.beautymirror.LinphonePreferences;
import com.gome.beautymirror.LinphoneService;
import com.gome.beautymirror.LinphoneUtils;
import com.gome.beautymirror.R;
import org.linphone.core.Address;
import org.linphone.core.Core;
import org.linphone.core.Friend;
import org.linphone.core.FriendList;
import org.linphone.core.FriendListListener;
import org.linphone.core.MagicSearch;
import org.linphone.core.ProxyConfig;
import org.linphone.mediastream.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.gome.beautymirror.data.provider.DatabaseUtil;

public class ContactsManager extends ContentObserver implements FriendListListener, LoaderManager.LoaderCallbacks<Cursor> {
    private static ContactsManager instance;

    private List<com.gome.beautymirror.contacts.LinphoneContact> mSipContacts;
    private MagicSearch magicSearch;
    private Activity mActivity;
    private Bitmap defaultAvatar;

    private static ArrayList<com.gome.beautymirror.contacts.ContactsUpdatedListener> contactsUpdatedListeners;

    public static void addContactsListener(com.gome.beautymirror.contacts.ContactsUpdatedListener listener) {
        contactsUpdatedListeners.add(listener);
    }

    public static void removeContactsListener(com.gome.beautymirror.contacts.ContactsUpdatedListener listener) {
        contactsUpdatedListeners.remove(listener);
    }

    private ContactsManager() {
        super(LinphoneService.instance().mHandler);
        defaultAvatar = BitmapFactory.decodeResource(LinphoneService.instance().getResources(), R.drawable.image_head);
        contactsUpdatedListeners = new ArrayList<>();
        mSipContacts = new ArrayList<>();
        if (LinphoneManager.getLcIfManagerNotDestroyedOrNull() != null) {
            magicSearch = LinphoneManager.getLcIfManagerNotDestroyedOrNull().createMagicSearch();
        }
    }

    public void destroy() {
        Core lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
        if (lc != null) {
            for (FriendList list : lc.getFriendsLists()) {
                list.setListener(null);
            }
        }
        defaultAvatar.recycle();
        instance = null;
    }

    public MagicSearch getMagicSearch() {
        return magicSearch;
    }

    public Bitmap getDefaultAvatarBitmap() {
        return defaultAvatar;
    }

    @Override
    public void onChange(boolean selfChange) {
        onChange(selfChange, null);
    }


    @Override
    public void onChange(boolean selfChange, Uri uri) {
        fetchContactsSync();
    }

    public static final ContactsManager getInstance() {
        if (instance == null) instance = new ContactsManager();
        return instance;
    }

    public synchronized List<com.gome.beautymirror.contacts.LinphoneContact> getSIPContacts() {
        return mSipContacts;
    }

    public void enableContactsAccess() {
        LinphonePreferences.instance().disableFriendsStorage();
    }

    public boolean hasContactsAccess() {
        if (mActivity == null) {
            return false;
        }
        boolean contactsR = (PackageManager.PERMISSION_GRANTED ==
                mActivity.getPackageManager().checkPermission(android.Manifest.permission.READ_CONTACTS, mActivity.getPackageName()));
        return contactsR && !mActivity.getResources().getBoolean(R.bool.force_use_of_linphone_friends);
    }

    public void initializeContactManager(Activity activity) {
        if (mActivity == null) {
            mActivity = activity;
            mActivity.getLoaderManager().initLoader(CONTACTS_LOADER, null, this);
        } else if (mActivity != activity){
            mActivity = activity;
        }
    }

    public void initializeSyncAccount(Activity activity) {
        initializeContactManager(activity);
        AccountManager accountManager = (AccountManager) activity.getSystemService(Context.ACCOUNT_SERVICE);

        Account[] accounts = accountManager.getAccountsByType(activity.getPackageName());

        if (accounts != null && accounts.length == 0) {
            Account newAccount = new Account(mActivity.getString(R.string.sync_account_name), activity.getPackageName());
            try {
                accountManager.addAccountExplicitly(newAccount, null, null);
            } catch (Exception e) {
                Log.e(e);
            }
        }
    }

    public synchronized com.gome.beautymirror.contacts.LinphoneContact findContactFromAddress(Address address) {
        if (address == null) return null;
        Core lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
        Friend lf = lc.findFriend(address);
        if (lf != null) {
            com.gome.beautymirror.contacts.LinphoneContact contact = (com.gome.beautymirror.contacts.LinphoneContact) lf.getUserData();
            return contact;
        }
        return findContactFromPhoneNumber(address.getUsername());
    }

    public synchronized com.gome.beautymirror.contacts.LinphoneContact findContactFromPhoneNumber(String phoneNumber) {
        Core lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
        ProxyConfig lpc = null;
        if (lc != null) {
            lpc = lc.getDefaultProxyConfig();
        }
        if (lpc == null) return null;
        String normalized = lpc.normalizePhoneNumber(phoneNumber);
        if (normalized == null) normalized = phoneNumber;

        Address addr = lpc.normalizeSipUri(normalized);
        if (addr == null) {
            return null;
        }
        addr.setUriParam("user", "phone");
        Friend lf = lc.findFriend(addr); // Without this, the hashmap inside liblinphone won't find it...
        if (lf != null) {
            com.gome.beautymirror.contacts.LinphoneContact contact = (com.gome.beautymirror.contacts.LinphoneContact) lf.getUserData();
            return contact;
        }
        return null;
    }

    public synchronized boolean refreshSipContact(Friend lf) {
        com.gome.beautymirror.contacts.LinphoneContact contact = (com.gome.beautymirror.contacts.LinphoneContact) lf.getUserData();
        if (contact != null && !mSipContacts.contains(contact)) {
            mSipContacts.add(contact);
            Collections.sort(mSipContacts);
            return true;
        }
        return false;
    }

    public static String getAddressOrNumberForAndroidContact(ContentResolver resolver, Uri contactUri) {
        // Phone Numbers
        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor c = resolver.query(contactUri, projection, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                int numberIndex = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = c.getString(numberIndex);
                c.close();
                return number;
            }
        }

        // SIP addresses
        projection = new String[]{ContactsContract.CommonDataKinds.SipAddress.SIP_ADDRESS};
        c = resolver.query(contactUri, projection, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                int numberIndex = c.getColumnIndex(ContactsContract.CommonDataKinds.SipAddress.SIP_ADDRESS);
                String address = c.getString(numberIndex);
                c.close();
                return address;
            }
            c.close();
        }
        return null;
    }

    public void delete(String id) {
        ArrayList<String> ids = new ArrayList<>();
        ids.add(id);
        deleteMultipleContactsAtOnce(ids);
    }

    public void deleteMultipleContactsAtOnce(List<String> ids) {
        String select = ContactsContract.Data.CONTACT_ID + " = ?";
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        for (String id : ids) {
            String[] args = new String[]{id};
            ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI).withSelection(select, args).build());
        }

        try {
            mActivity.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            Log.e(e);
        }
    }

    public String getString(int resourceID) {
        if (mActivity == null) return null;
        return mActivity.getString(resourceID);
    }

    @Override
    public void onContactCreated(FriendList list, Friend lf) {

    }

    @Override
    public void onContactDeleted(FriendList list, Friend lf) {
    }

    @Override
    public void onContactUpdated(FriendList list, Friend newFriend, Friend oldFriend) {

    }

    @Override
    public void onSyncStatusChanged(FriendList list, FriendList.SyncStatus status, String msg) {

    }

    @Override
    public void onPresenceReceived(FriendList list, Friend[] friends) {
        for (Friend lf : friends) {
            boolean newContact = ContactsManager.getInstance().refreshSipContact(lf);
            if (newContact) {
                for (com.gome.beautymirror.contacts.ContactsUpdatedListener listener : contactsUpdatedListeners) {
                    listener.onContactsUpdated();
                }
            }
        }
    }

    public void fetchContactsSync() {
      mActivity.getLoaderManager().restartLoader(CONTACTS_LOADER, null, this);
    }

    public void fetchContactsAsync() {
        fetchContactsSync();
    }

    private static final int CONTACTS_LOADER = 1;

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == CONTACTS_LOADER) {
            return new CursorLoader(
                    mActivity,
                    com.gome.beautymirror.data.provider.DatabaseProvider.FRIENDS_URI,
                    null,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor c) {
        Date contactsTime = new Date();
        List<com.gome.beautymirror.contacts.LinphoneContact> contacts = new ArrayList<>();
        List<com.gome.beautymirror.contacts.LinphoneContact> sipContacts = new ArrayList<>();
        if (c != null) {
            while (c.moveToNext()) {
                com.gome.beautymirror.contacts.LinphoneContact contact = new com.gome.beautymirror.contacts.LinphoneContact();
                contact.setAccount(c.getString(DatabaseUtil.Friend.COLUMN_ACCOUNT));
                String displayName = c.getString(DatabaseUtil.Friend.COLUMN_NAME);
                String deviceId =  c.getString(DatabaseUtil.Friend.COLUMN_ID);
                if(displayName==null || displayName.equals("")){
                    displayName=contact.getAccount();
                }
                contact.setFullName(displayName);
                contact.addNumberOrAddress(new com.gome.beautymirror.contacts.LinphoneNumberOrAddress(contact.getAccount(), true));
                String remarkName = c.getString(DatabaseUtil.Friend.COLUMN_COMMENT);
                contact.setRemarkName(remarkName);
                String letter;
                if(remarkName!=null && !remarkName.equals("")){
                    letter = String.valueOf(Pinyin.toPinyin(remarkName.charAt(0)).toUpperCase().charAt(0));
                }else {
                    letter = String.valueOf(Pinyin.toPinyin(displayName.charAt(0)).toUpperCase().charAt(0));
                }
                //非字母开头的统一设置成 "#"
                if (isLetter(letter)) {
                    contact.setLetter(letter);
                } else {
                    contact.setLetter("#");
                }
                contact.setIcon(c.getBlob(DatabaseUtil.Friend.COLUMN_ICON));
                contact.setDevice(false);
                if (!sipContacts.contains(contact)) {
                    sipContacts.add(contact);
                    if(deviceId !=null && !deviceId.equals("")){
                        com.gome.beautymirror.contacts.LinphoneContact contactDevice = new com.gome.beautymirror.contacts.LinphoneContact();
                        contactDevice.setAccount(c.getString(DatabaseUtil.Friend.COLUMN_ACCOUNT));
                        contactDevice.setFullName(displayName);
                        contactDevice.setRemarkName(c.getString(DatabaseUtil.Friend.COLUMN_COMMENT));
                        contactDevice.addNumberOrAddress(new com.gome.beautymirror.contacts.LinphoneNumberOrAddress(contact.getAccount(), true));
                        contactDevice.setDevice(true);
                        if (isLetter(letter)) {
                            contactDevice.setLetter(letter);
                        } else {
                            contactDevice.setLetter("#");
                        }
                        sipContacts.add(contactDevice);
                    }
                }
            }
            mSipContacts=sipContacts;
            compare(mSipContacts);
        }

        if (LinphonePreferences.instance() != null && LinphonePreferences.instance().isFriendlistsubscriptionEnabled()) {
            String rls = mActivity.getString(R.string.rls_uri);
            for (FriendList list : LinphoneManager.getLc().getFriendsLists()) {
                if (rls != null && (list.getRlsAddress() == null || !list.getRlsAddress().asStringUriOnly().equals(rls))) {
                    list.setRlsUri(rls);
                }
                list.setListener(this);
                list.updateSubscriptions();
            }
        }

        long timeElapsed = (new Date()).getTime() - contactsTime.getTime();
        String time = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(timeElapsed),
                TimeUnit.MILLISECONDS.toSeconds(timeElapsed) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeElapsed)));
        Log.i("[ContactsManager] For " + contacts.size() + " contacts: " + time + " elapsed since starting");
        for (com.gome.beautymirror.contacts.ContactsUpdatedListener listener : contactsUpdatedListeners) {
            listener.onContactsUpdated();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }

    public com.gome.beautymirror.contacts.LinphoneContact getContactFromAccount(String mAccount){
        for(com.gome.beautymirror.contacts.LinphoneContact contact : mSipContacts){
            for (com.gome.beautymirror.contacts.LinphoneNumberOrAddress noa : contact.getNumbersOrAddresses()) {
                String value = noa.getValue();
                String displayednumberOrAddress = LinphoneUtils.getDisplayableUsernameFromAddress(value);
                if(mAccount.equals(displayednumberOrAddress)){
                    return contact;
                }
            }
        }
        return null;
    }

    public synchronized void setSipContacts(List<com.gome.beautymirror.contacts.LinphoneContact> c) {
        if (mSipContacts.isEmpty() || mSipContacts.size() > c.size()) {
            mSipContacts = c;
        } else {
            for (com.gome.beautymirror.contacts.LinphoneContact contact : c) {
                if (!mSipContacts.contains(contact)) {
                    mSipContacts.add(contact);
                }
            }
        }
        compare(mSipContacts);
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
     * 把联系人按照a b c升序排列
     */
    public static List<com.gome.beautymirror.contacts.LinphoneContact> compare(List<com.gome.beautymirror.contacts.LinphoneContact> c) {
        Collections.sort(c, new Comparator<com.gome.beautymirror.contacts.LinphoneContact>() {
            @Override
            public int compare(com.gome.beautymirror.contacts.LinphoneContact o1, com.gome.beautymirror.contacts.LinphoneContact o2) {
                //升序排列
                if (o1.getLetter().equals("@")
                        || o2.getLetter().equals("#")) {
                    if(o1.getFullName().equals(o2.getFullName())){
                        return 1;
                    }
                    return -1;
                } else if (o1.getLetter().equals("#")
                        || o2.getLetter().equals("@")) {

                    return 1;
                }

                return o1.getLetter().compareTo(o2.getLetter());
            }
        });
        return c;
    }

}
