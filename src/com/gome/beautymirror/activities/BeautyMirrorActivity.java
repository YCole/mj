package com.gome.beautymirror.activities;

/*
 BeautyMirrorActivity.java
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

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.beautymirror.LinphoneManager;
import com.gome.beautymirror.LinphoneManager.AddressType;
import com.gome.beautymirror.LinphonePreferences;
import com.gome.beautymirror.LinphoneService;
import com.gome.beautymirror.LinphoneUtils;
import com.gome.beautymirror.R;
import com.gome.beautymirror.assistant.AssistantActivity;
import com.gome.beautymirror.assistant.RemoteProvisioningLoginActivity;
import com.gome.beautymirror.call.CallActivity;
import com.gome.beautymirror.call.CallIncomingActivity;
import com.gome.beautymirror.call.CallOutgoingActivity;
import com.gome.beautymirror.chat.ChatCreationFragment;
import com.gome.beautymirror.chat.ChatListFragment;
import com.gome.beautymirror.chat.GroupChatFragment;
import com.gome.beautymirror.chat.GroupInfoFragment;
import com.gome.beautymirror.chat.ImdnFragment;
import com.gome.beautymirror.compatibility.Compatibility;
import com.gome.beautymirror.contacts.ContactAddress;
import com.gome.beautymirror.contacts.ContactPicked;
import com.gome.beautymirror.contacts.ContactsListFragment;
import com.gome.beautymirror.contacts.ContactsManager;
import com.gome.beautymirror.contacts.LinphoneContact;
import org.linphone.core.Address;
import org.linphone.core.AuthInfo;
import org.linphone.core.Call;
import org.linphone.core.Call.State;
import org.linphone.core.ChatMessage;
import org.linphone.core.ChatRoom;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.ProxyConfig;
import org.linphone.core.Reason;
import org.linphone.core.RegistrationState;
import com.gome.beautymirror.fragments.AccountPreferencesFragment;
import com.gome.beautymirror.fragments.DialerFragment;
import com.gome.beautymirror.fragments.EmptyFragment;
import com.gome.beautymirror.fragments.FragmentsAvailable;
import com.gome.beautymirror.fragments.HistoryListFragment;
import com.gome.beautymirror.fragments.SettingsFragment;
//import com.gome.beautymirror.fragments.PhotoFragment;
import org.linphone.mediastream.Log;
import com.gome.beautymirror.purchase.InAppPurchaseActivity;
import com.gome.beautymirror.ui.AddressText;
import com.gome.beautymirror.xmlrpc.XmlRpcHelper;
import com.gome.beautymirror.xmlrpc.XmlRpcListenerBase;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.gome.beautymirror.activities.BeautyMirrorGenericActivity;
import com.gome.beautymirror.data.DataService;
import com.gome.beautymirror.contacts.LinphoneContact;

import cole.utils.SaveUtils;
import cole.activities.RigisterAndLoginMainActivity;
import cole.fragments.MineFragment;
import gome.beautymirror.fragments.PhotoFragment;
import gome.beautymirror.ui.BadgeRadioButton;

public class BeautyMirrorActivity extends BeautyMirrorGenericActivity implements OnClickListener, ContactPicked, ActivityCompat.OnRequestPermissionsResultCallback ,MineFragment.OnFragmentInteractionListener {
    private static final int SETTINGS_ACTIVITY = 123;
    private static final int CALL_ACTIVITY = 19;
    private static final int PERMISSIONS_REQUEST_OVERLAY = 206;
    private static final int PERMISSIONS_REQUEST_SYNC = 207;
    private static final int PERMISSIONS_REQUEST_CONTACTS = 208;
    private static final int PERMISSIONS_RECORD_AUDIO_ECHO_CANCELLER = 209;
    private static final int PERMISSIONS_READ_EXTERNAL_STORAGE_DEVICE_RINGTONE = 210;
    private static final int PERMISSIONS_RECORD_AUDIO_ECHO_TESTER = 211;

    private static BeautyMirrorActivity instance;

    private RelativeLayout mTopBar;
    private ImageView cancel;
    private FragmentsAvailable pendingFragmentTransaction, currentFragment, leftFragment;
    private Fragment fragment;
    private List<FragmentsAvailable> fragmentsHistory;
    private Fragment.SavedState dialerSavedState;
    private boolean newProxyConfig;
    private boolean emptyFragment = false;
    private boolean isTrialAccount = false;
    private OrientationEventListener mOrientationHelper;
    private CoreListenerStub mListener;
    private LinearLayout mTabBar;

    private DrawerLayout sideMenu;
    private RelativeLayout sideMenuContent, defaultAccount;
    private ListView accountsList, sideMenuItemList;
    private ImageView menu;
    private boolean doNotGoToCallActivity = false;
    private List<String> sideMenuItems;
    private boolean callTransfer = false;
    private boolean isOnBackground = false;

    public String mAddressWaitingToBeCalled;


    private RadioGroup mRadionGroupMain;
    private BadgeRadioButton mHome, mContacts, mNotification, mDialer,mMine;
    private LinearLayout ll_ff;
    private ViewPager viewPager;
    private MyPagerAdapter adapter;

    public static final int FRAGMENGT_PHOTO = 0;
    public static final int FRAGMENGT_NOFITICATION = 1;
    public static final int FRAGMENGT_CONTACT = 2;
    public static final int FRAGMENGT_MINE = 3;
    public static final int FRAGMENGT_DIALER = 4;
    public static final int FRAGMENGT_SETTINGS = 5;
    public static final int FRAGMENGT_ACCOUNT_SETTINGS = 6;

    private PhotoFragment mPhotoFragment = null;
    private HistoryListFragment mHistoryListFragment = null;
    private ContactsListFragment mContactsListFragment = null;
    private MineFragment mMineFragment = null;
    private DialerFragment mDialerFragment = null;
    private SettingsFragment mSettingsFragment = null;
    private AccountPreferencesFragment mAccountPreferencesFragment = null;

    private int mMissedCount;

    static public final boolean isInstanciated() {
        return instance != null;
    }

    public static final BeautyMirrorActivity instance() {
        if (instance != null)
            return instance;
        throw new RuntimeException("BeautyMirrorActivity not instantiated yet");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //This must be done before calling super.onCreate().
        super.onCreate(savedInstanceState);

        if (getResources().getBoolean(R.bool.orientation_portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        boolean useFirstLoginActivity = getResources().getBoolean(R.bool.display_account_assistant_at_first_start);
        if (LinphonePreferences.instance().isProvisioningLoginViewEnabled()) {
            Intent wizard = new Intent();
            wizard.setClass(this, RemoteProvisioningLoginActivity.class);
            wizard.putExtra("Domain", LinphoneManager.getInstance().wizardLoginViewDomain);
            startActivity(wizard);
            finish();
            return;
        } else if (savedInstanceState == null && (useFirstLoginActivity && LinphoneManager.getLcIfManagerNotDestroyedOrNull() != null
                && LinphonePreferences.instance().isFirstLaunch())) {
            if (LinphonePreferences.instance().getAccountCount() > 0) {
                LinphonePreferences.instance().firstLaunchSuccessful();
            } else {
                //startActivity(new Intent().setClass(this, AssistantActivity.class));
                //finish();
                //return;
            }
        }

        if (getResources().getBoolean(R.bool.use_linphone_tag)) {
            if (getPackageManager().checkPermission(Manifest.permission.WRITE_SYNC_SETTINGS, getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                checkSyncPermission();
            } else {
                if (LinphoneService.isReady())
                    ContactsManager.getInstance().initializeSyncAccount(this);
            }
        } else {
            if (LinphoneService.isReady())
                ContactsManager.getInstance().initializeContactManager(this);
        }

        if (TextUtils.isEmpty(SaveUtils.readUser(this, "account"))) {
            startActivity(new Intent(this, RigisterAndLoginMainActivity.class));
        }

        setContentView(R.layout.main);
        instance = this;
        fragmentsHistory = new ArrayList<>();
        pendingFragmentTransaction = FragmentsAvailable.UNKNOW;

        initButtons();
        initSideMenu();


        initTab();
        initTabClick();

        currentFragment = FragmentsAvailable.EMPTY;
        if (savedInstanceState == null) {
            viewPager.setCurrentItem(FRAGMENGT_PHOTO);
        } else {
            currentFragment = (FragmentsAvailable) savedInstanceState.getSerializable("currentFragment");
        }

        mListener = new CoreListenerStub() {
            @Override
            public void onMessageReceived(Core lc, ChatRoom cr, ChatMessage message) {
                displayMissedChats(LinphoneManager.getInstance().getUnreadMessageCount());
            }

            @Override
            public void onRegistrationStateChanged(Core lc, ProxyConfig proxy, RegistrationState state, String smessage) {
                AuthInfo authInfo = lc.findAuthInfo(proxy.getRealm(), proxy.getIdentityAddress().getUsername(), proxy.getDomain());

                refreshAccounts();

                if (getResources().getBoolean(R.bool.use_phone_number_validation)
                        && authInfo != null && authInfo.getDomain().equals(getString(R.string.default_domain))) {
                    if (state.equals(RegistrationState.Ok)) {
                        LinphoneManager.getInstance().isAccountWithAlias();
                    }
                }

                if (state.equals(RegistrationState.Failed) && newProxyConfig) {
                    newProxyConfig = false;
                    if (proxy.getError() == Reason.Forbidden) {
                        //displayCustomToast(getString(R.string.error_bad_credentials), Toast.LENGTH_LONG);
                    }
                    if (proxy.getError() == Reason.Unauthorized) {
                        displayCustomToast(getString(R.string.error_unauthorized), Toast.LENGTH_LONG);
                    }
                    if (proxy.getError() == Reason.IOError) {
                        displayCustomToast(getString(R.string.error_io_error), Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onCallStateChanged(Core lc, Call call, Call.State state, String message) {
                if (state == State.IncomingReceived) {
                    startActivity(new Intent(BeautyMirrorActivity.instance(), CallIncomingActivity.class));
                } else if (state == State.OutgoingInit || state == State.OutgoingProgress) {
                    startActivity(new Intent(BeautyMirrorActivity.instance(), CallOutgoingActivity.class));
                } else if (state == State.End || state == State.Error || state == State.Released) {
                    resetClassicMenuLayoutAndGoBackToCallIfStillRunning();
                }

                int missedCalls = LinphoneManager.getLc().getMissedCallsCount();
                displayMissedCalls(missedCalls);
            }
        };

        Core lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
        if (lc != null) {
            int missedCalls = lc.getMissedCallsCount();
            displayMissedCalls(missedCalls);
        }

        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                rotation = 0;
                break;
            case Surface.ROTATION_90:
                rotation = 90;
                break;
            case Surface.ROTATION_180:
                rotation = 180;
                break;
            case Surface.ROTATION_270:
                rotation = 270;
                break;
        }

        if (LinphoneManager.isInstanciated()) {
            LinphoneManager.getLc().setDeviceRotation(rotation);
        }
        mAlwaysChangingPhoneAngle = rotation;

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getBoolean("GoToChat", false)) {
            onNewIntent(getIntent());
        }
        mMissedCount = mHistoryListFragment.getMissedCallLogsAndNotifications();
        refreshBagdeNumber(mMissedCount);
    }

    public void refreshBagdeNumber(int count){
        mNotification.setBadgeNumber(count);
    }

    private void initTabClick() {
        mRadionGroupMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {

                if (id == R.id.tab_main_home) {
                    viewPager.setCurrentItem(FRAGMENGT_PHOTO);
                } else if (id == R.id.tab_main_contacts) {
                    viewPager.setCurrentItem(FRAGMENGT_CONTACT);
                } else if (id == R.id.tab_main_notification) {
                    viewPager.setCurrentItem(FRAGMENGT_NOFITICATION);
                    LinphoneManager.getLc().resetMissedCallsCount();
                    displayMissedCalls(0);
                } else if (id == R.id.tab_main_mine) {
                    viewPager.setCurrentItem(FRAGMENGT_MINE);
                }else if (id == R.id.tab_main_dialer) {
                    viewPager.setCurrentItem(FRAGMENGT_DIALER);
                }
            }
        });
    }

    private void initButtons() {
        mTabBar = findViewById(R.id.footer);
        mTopBar = findViewById(R.id.top_bar);

        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
    }

    public boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }

    public void isNewProxyConfig() {
        newProxyConfig = true;
    }

    public void displayEmptyFragment() {
        //changeCurrentFragment(FragmentsAvailable.EMPTY, new Bundle());
    }

    @SuppressLint("SimpleDateFormat")
    private String secondsToDisplayableString(int secs) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.set(0, 0, 0, 0, 0, secs);
        return dateFormat.format(cal.getTime());
    }

    public void displayContact(LinphoneContact contact, boolean chatOnly) {
        Bundle extras = new Bundle();
        extras.putSerializable("Contact", contact);
        Intent intent = new Intent(this, com.gome.beautymirror.activities.ContactDetailsActivity.class);
        intent.putExtras(extras);
        startActivity(intent);
    }

    public void displayAssistant() {
        startActivity(new Intent(BeautyMirrorActivity.this, AssistantActivity.class));
    }


    public void displayInapp() {
        startActivity(new Intent(BeautyMirrorActivity.this, InAppPurchaseActivity.class));
    }

    private void displayChat(String sipUri, String message, String fileUri, String pictureUri, String thumbnailUri, String displayName, Address lAddress) {
        Bundle extras = new Bundle();
        extras.putString("SipUri", sipUri);

        if (message != null)
            extras.putString("messageDraft", message);
        if (fileUri != null)
            extras.putString("fileSharedUri", fileUri);
        if (sipUri != null && lAddress.getDisplayName() != null) {
            extras.putString("DisplayName", displayName);
            extras.putString("PictureUri", pictureUri);
            extras.putString("ThumbnailUri", thumbnailUri);
        }

        if (sipUri == null) {
          //  changeCurrentFragment(FragmentsAvailable.CREATE_CHAT, extras);
        } else {
            //changeCurrentFragment(FragmentsAvailable.GROUP_CHAT, extras);
        }
    }

    public void goToChatCreator(String address, ArrayList<ContactAddress> selectedContacts, String subject, boolean isGoBack, Bundle shareInfos) {
        if (currentFragment == FragmentsAvailable.INFO_GROUP_CHAT && isGoBack) {
            getSupportFragmentManager().popBackStackImmediate();
            getSupportFragmentManager().popBackStackImmediate();
        }
        Bundle extras = new Bundle();
        extras.putSerializable("selectedContacts", selectedContacts);
        extras.putString("subject", subject);
        extras.putString("groupChatRoomAddress", address);

        if (shareInfos != null) {
            if (shareInfos.getString("fileSharedUri") != null)
                extras.putString("fileSharedUri", shareInfos.getString("fileSharedUri"));
            if (shareInfos.getString("messageDraft") != null)
                extras.putString("messageDraft", shareInfos.getString("messageDraft"));
        }

        //changeCurrentFragment(FragmentsAvailable.CREATE_CHAT, extras);
    }

    public void goToChat(String sipUri, Bundle shareInfos) {
        Bundle extras = new Bundle();
        extras.putString("SipUri", sipUri);

        if (shareInfos != null) {
            if (shareInfos.getString("fileSharedUri") != null)
                extras.putString("fileSharedUri", shareInfos.getString("fileSharedUri"));
            if (shareInfos.getString("messageDraft") != null)
                extras.putString("messageDraft", shareInfos.getString("messageDraft"));
        }

  /*      if (isTablet()) {
            Fragment fragment2 = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer2);
            if (fragment2 != null && fragment2.isVisible() && currentFragment == FragmentsAvailable.GROUP_CHAT && !emptyFragment) {
                GroupChatFragment chatFragment = (GroupChatFragment) fragment2;
                chatFragment.changeDisplayedChat(sipUri);
            } else {
                changeCurrentFragment(FragmentsAvailable.GROUP_CHAT, extras);
            }
        } else {
            changeCurrentFragment(FragmentsAvailable.GROUP_CHAT, extras);
        }*/

        LinphoneManager.getInstance().updateUnreadCountForChatRoom(sipUri, 0);
        displayMissedChats(LinphoneManager.getInstance().getUnreadMessageCount());
    }

    public void goToChatGroupInfos(String address, ArrayList<ContactAddress> contacts, String subject, boolean isEditionEnabled, boolean isGoBack, Bundle shareInfos) {
        if (currentFragment == FragmentsAvailable.CREATE_CHAT && isGoBack) {
            getSupportFragmentManager().popBackStackImmediate();
            getSupportFragmentManager().popBackStackImmediate();
        }
        Bundle extras = new Bundle();
        extras.putString("groupChatRoomAddress", address);
        extras.putBoolean("isEditionEnabled", isEditionEnabled);
        extras.putSerializable("ContactAddress", contacts);
        extras.putString("subject", subject);

        if (shareInfos != null) {
            if (shareInfos.getString("fileSharedUri") != null)
                extras.putString("fileSharedUri", shareInfos.getString("fileSharedUri"));
            if (shareInfos.getString("messageDraft") != null)
                extras.putString("messageDraft", shareInfos.getString("messageDraft"));
        }

        //changeCurrentFragment(FragmentsAvailable.INFO_GROUP_CHAT, extras);
    }

    public void goToChatMessageImdnInfos(String sipUri, String messageId) {
        Bundle extras = new Bundle();
        extras.putSerializable("SipUri", sipUri);
        extras.putString("MessageId", messageId);
      //  changeCurrentFragment(FragmentsAvailable.MESSAGE_IMDN, extras);
    }

    public void goToChatList() {
       // changeCurrentFragment(FragmentsAvailable.CHAT_LIST, null);
    }

    public void displayChat(String sipUri, String message, String fileUri) {
        if (getResources().getBoolean(R.bool.disable_chat)) {
            return;
        }

        String pictureUri = null;
        String thumbnailUri = null;
        String displayName = null;

        Address lAddress = null;
        if (sipUri != null) {
            lAddress = LinphoneManager.getLc().interpretUrl(sipUri);
            if (lAddress == null) return;
            LinphoneContact contact = ContactsManager.getInstance().findContactFromAddress(lAddress);
            displayName = contact != null ? contact.getFullName() : null;

            if (contact != null && contact.getPhotoUri() != null) {
                pictureUri = contact.getPhotoUri().toString();
                thumbnailUri = contact.getThumbnailUri().toString();
            }
        }

 /*       if (currentFragment == FragmentsAvailable.CHAT_LIST || currentFragment == FragmentsAvailable.GROUP_CHAT) {
            Fragment fragment2 = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer2);
            if (fragment2 != null && fragment2.isVisible() && currentFragment == FragmentsAvailable.GROUP_CHAT && !emptyFragment) {
                GroupChatFragment chatFragment = (GroupChatFragment) fragment2;
                chatFragment.changeDisplayedChat(sipUri);
            } else {
                displayChat(sipUri, message, fileUri, pictureUri, thumbnailUri, displayName, lAddress);
            }
        } else {
            if (isTablet()) {
                changeCurrentFragment(FragmentsAvailable.CHAT_LIST, null);
            } else {
                displayChat(sipUri, message, fileUri, pictureUri, thumbnailUri, displayName, lAddress);
            }
        }*/

        LinphoneManager.getInstance().updateUnreadCountForChatRoom(sipUri, 0);
        displayMissedChats(LinphoneManager.getInstance().getUnreadMessageCount());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cancel) {

            hideTopBar();
            displayDialer();
        }
    }


    public void hideTabBar(Boolean hide) {
//        if (hide) {
//            mTabBar.setVisibility(View.GONE);
//        } else {
//            mTabBar.setVisibility(View.VISIBLE);
//        }
    }

    public void hideTopBar() {
        mTopBar.setVisibility(View.GONE);
    }

    @SuppressWarnings("incomplete-switch")
    public void selectMenu(FragmentsAvailable menuToSelect) {
        currentFragment = menuToSelect;

        switch (menuToSelect) {
            case HISTORY_LIST:
                mHome.setVisibility(View.VISIBLE);
                break;
            case CONTACTS_LIST:
                mContacts.setVisibility(View.VISIBLE);
                break;
            case DIALER:
                mMine.setVisibility(View.VISIBLE);
                break;
            case SETTINGS:
            case ACCOUNT_SETTINGS:
                hideTabBar(true);
                mTopBar.setVisibility(View.VISIBLE);
                break;
            case CHAT_LIST:
            case CREATE_CHAT:
            case GROUP_CHAT:
            case INFO_GROUP_CHAT:
            case MESSAGE_IMDN:
            case CHAT:
            //    chat_selected.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void updateDialerFragment(DialerFragment fragment) {
        // Hack to maintain soft input flags
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public void goToDialerFragment() {
        viewPager.setCurrentItem(FRAGMENGT_DIALER);
        mMine.setVisibility(View.VISIBLE);
    }

    public void displaySettings() {
        viewPager.setCurrentItem(FRAGMENGT_SETTINGS);
    }

    public void displayDialer() {
        viewPager.setCurrentItem(FRAGMENGT_DIALER);
    }

    public void displayAccountSettings(int accountNumber) {
        Bundle bundle = new Bundle();
        bundle.putInt("Account", accountNumber);
        mAccountPreferencesFragment.setArguments(bundle);
        viewPager.setCurrentItem(FRAGMENGT_ACCOUNT_SETTINGS);
    }

    public void refreshMissedChatCountDisplay() {
        displayMissedChats(LinphoneManager.getInstance().getUnreadMessageCount());
    }

    public void displayMissedCalls(final int missedCallsCount) {
        if (missedCallsCount > 0) {
//            missedCalls.setText(missedCallsCount + "");
//            missedCalls.setVisibility(View.VISIBLE);
        } else {
            if (LinphoneManager.isInstanciated()) LinphoneManager.getLc().resetMissedCallsCount();
//            missedCalls.clearAnimation();
//            missedCalls.setVisibility(View.GONE);
        }
    }

    private void displayMissedChats(final int missedChatCount) {
        if (missedChatCount > 0) {
//            missedChats.setText(missedChatCount + "");
//            missedChats.setVisibility(View.VISIBLE);
        } else {
//            missedChats.clearAnimation();
//            missedChats.setVisibility(View.GONE);
        }
    }

    public void displayCustomToast(final String message, final int duration) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast, (ViewGroup) findViewById(R.id.toastRoot));

        TextView toastText = (TextView) layout.findViewById(R.id.toastMessage);
        toastText.setText(message);

        final Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }

    public void displayChatRoomError() {
        final Dialog dialog = BeautyMirrorActivity.instance().displayDialog(getString(R.string.chat_room_creation_failed));
        Button delete = dialog.findViewById(R.id.delete_button);
        Button cancel = dialog.findViewById(R.id.cancel);
        delete.setVisibility(View.GONE);
        cancel.setText(getString(R.string.ok));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public Dialog displayDialog(String text) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Drawable d = new ColorDrawable(ContextCompat.getColor(this, R.color.colorC));
        d.setAlpha(200);
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(d);

        TextView customText = (TextView) dialog.findViewById(R.id.customText);
        customText.setText(text);
        return dialog;
    }

    @Override
    public void setAddresGoToDialerAndCall(String number, String name, Uri photo) {
//      Bundle extras = new Bundle();
//      extras.putString("SipUri", number);
//      extras.putString("DisplayName", name);
//      extras.putString("Photo", photo == null ? null : photo.toString());
//      changeCurrentFragment(FragmentsAvailable.DIALER, extras);
        android.util.Log.d("zlm","name=="+name+"number=="+number);
        AddressType address = new AddressText(this, null);
        address.setDisplayedName(name);
        address.setText(number);
        LinphoneManager.getInstance().newOutgoingCall(address);
    }

    public void startIncallActivity(Call currentCall) {
        Intent intent = new Intent(this, CallActivity.class);
        startOrientationSensor();
        startActivityForResult(intent, CALL_ACTIVITY);
    }

    /**
     * Register a sensor to track phoneOrientation changes
     */
    private synchronized void startOrientationSensor() {
        if (mOrientationHelper == null) {
            mOrientationHelper = new LocalOrientationEventListener(this);
        }
        mOrientationHelper.enable();
    }

    private int mAlwaysChangingPhoneAngle = -1;

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class LocalOrientationEventListener extends OrientationEventListener {
        public LocalOrientationEventListener(Context context) {
            super(context);
        }

        @Override
        public void onOrientationChanged(final int o) {
            if (o == OrientationEventListener.ORIENTATION_UNKNOWN) {
                return;
            }

            int degrees = 270;
            if (o < 45 || o > 315)
                degrees = 0;
            else if (o < 135)
                degrees = 90;
            else if (o < 225)
                degrees = 180;

            if (mAlwaysChangingPhoneAngle == degrees) {
                return;
            }
            mAlwaysChangingPhoneAngle = degrees;

            Log.d("Phone orientation changed to ", degrees);
            int rotation = (360 - degrees) % 360;
            Core lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
            if (lc != null) {
                lc.setDeviceRotation(rotation);
                Call currentCall = lc.getCurrentCall();
                if (currentCall != null && currentCall.cameraEnabled() && currentCall.getCurrentParams().videoEnabled()) {
                    lc.updateCall(currentCall, null);
                }
            }
        }
    }

    public Boolean isCallTransfer() {
        return callTransfer;
    }

    private void initInCallMenuLayout(final boolean callTransfer) {
        selectMenu(FragmentsAvailable.DIALER);
        DialerFragment dialerFragment = DialerFragment.instance();
        if (dialerFragment != null) {
            ((DialerFragment) dialerFragment).resetLayout(callTransfer);
        }
    }

    public void resetClassicMenuLayoutAndGoBackToCallIfStillRunning() {
        DialerFragment dialerFragment = DialerFragment.instance();
        if (dialerFragment != null) {
            ((DialerFragment) dialerFragment).resetLayout(true);
        }

        if (LinphoneManager.isInstanciated() && LinphoneManager.getLc().getCallsNb() > 0) {
            Call call = LinphoneManager.getLc().getCalls()[0];
            if (call.getState() == Call.State.IncomingReceived) {
                startActivity(new Intent(BeautyMirrorActivity.this, CallIncomingActivity.class));
            } else {
                startIncallActivity(call);
            }
        }
    }

    public FragmentsAvailable getCurrentFragment() {
        return currentFragment;
    }

    public void quit() {
        finish();
        stopService(new Intent(Intent.ACTION_MAIN).setClass(this, LinphoneService.class));
        stopService(new Intent(Intent.ACTION_MAIN).setClass(this, DataService.class));
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        am.killBackgroundProcesses(getString(R.string.sync_account_type));
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (pendingFragmentTransaction != FragmentsAvailable.UNKNOW) {
            viewPager.setCurrentItem(pendingFragmentTransaction.ordinal());
            selectMenu(pendingFragmentTransaction);
            pendingFragmentTransaction = FragmentsAvailable.UNKNOW;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (getIntent() != null && getIntent().getExtras() != null) {
            newProxyConfig = getIntent().getExtras().getBoolean("isNewProxyConfig");
        }

        if (resultCode == Activity.RESULT_FIRST_USER && requestCode == SETTINGS_ACTIVITY) {
            if (data.getExtras().getBoolean("Exit", false)) {
                quit();
            } else {
                pendingFragmentTransaction = (FragmentsAvailable) data.getExtras().getSerializable("FragmentToDisplay");
            }
        } else if (resultCode == Activity.RESULT_FIRST_USER && requestCode == CALL_ACTIVITY) {
            getIntent().putExtra("PreviousActivity", CALL_ACTIVITY);
            callTransfer = data != null && data.getBooleanExtra("Transfer", false);
            boolean chat = data != null && data.getBooleanExtra("chat", false);
            if (chat) {
                pendingFragmentTransaction = FragmentsAvailable.CHAT_LIST;
            }
            if (LinphoneManager.getLc().getCallsNb() > 0) {
                initInCallMenuLayout(callTransfer);
            } else {
                resetClassicMenuLayoutAndGoBackToCallIfStillRunning();
            }
        } else if (requestCode == PERMISSIONS_REQUEST_OVERLAY) {
            if (Compatibility.canDrawOverlays(this)) {
                LinphonePreferences.instance().enableOverlay(true);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onPause() {
        getIntent().putExtra("PreviousActivity", 0);

        Core lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
        if (lc != null) {
            lc.removeListener(mListener);
        }
        callTransfer = false;
        isOnBackground = true;

        super.onPause();
    }

    public boolean checkAndRequestOverlayPermission() {
        Log.i("[Permission] Draw overlays permission is " + (Compatibility.canDrawOverlays(this) ? "granted" : "denied"));
        if (!Compatibility.canDrawOverlays(this)) {
            Log.i("[Permission] Asking for overlay");
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, PERMISSIONS_REQUEST_OVERLAY);
            return false;
        }
        return true;
    }

    public void checkAndRequestExternalStoragePermission() {
        checkAndRequestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 0);
    }

    public void checkAndRequestCameraPermission() {
        checkAndRequestPermission(Manifest.permission.CAMERA, 0);
    }

    public void checkAndRequestWriteContactsPermission() {
        checkAndRequestPermission(Manifest.permission.WRITE_CONTACTS, 0);
    }

    public void checkAndRequestRecordAudioPermissionForEchoCanceller() {
        checkAndRequestPermission(Manifest.permission.RECORD_AUDIO, PERMISSIONS_RECORD_AUDIO_ECHO_CANCELLER);
    }

    public void checkAndRequestRecordAudioPermissionsForEchoTester() {
        checkAndRequestPermission(Manifest.permission.RECORD_AUDIO, PERMISSIONS_RECORD_AUDIO_ECHO_TESTER);
    }

    public void checkAndRequestReadExternalStoragePermissionForDeviceRingtone() {
        checkAndRequestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, PERMISSIONS_READ_EXTERNAL_STORAGE_DEVICE_RINGTONE);
    }

    public void checkAndRequestPermissionsToSendImage() {
        ArrayList<String> permissionsList = new ArrayList<String>();

        int readExternalStorage = getPackageManager().checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, getPackageName());
        Log.i("[Permission] Read external storage permission is " + (readExternalStorage == PackageManager.PERMISSION_GRANTED ? "granted" : "denied"));
        int camera = getPackageManager().checkPermission(Manifest.permission.CAMERA, getPackageName());
        Log.i("[Permission] Camera permission is " + (camera == PackageManager.PERMISSION_GRANTED ? "granted" : "denied"));

        if (readExternalStorage != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            Log.i("[Permission] Asking for read external storage");
            permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA);
            Log.i("[Permission] Asking for camera");
            permissionsList.add(Manifest.permission.CAMERA);
        }
        if (permissionsList.size() > 0) {
            String[] permissions = new String[permissionsList.size()];
            permissions = permissionsList.toArray(permissions);
            ActivityCompat.requestPermissions(this, permissions, 0);
        }
    }

    private void checkSyncPermission() {
        checkAndRequestPermission(Manifest.permission.WRITE_SYNC_SETTINGS, PERMISSIONS_REQUEST_SYNC);
    }

    public void checkAndRequestPermission(String permission, int result) {
        int permissionGranted = getPackageManager().checkPermission(permission, getPackageName());
        Log.i("[Permission] " + permission + " is " + (permissionGranted == PackageManager.PERMISSION_GRANTED ? "granted" : "denied"));

        if (permissionGranted != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
            Log.i("[Permission] Asking for " + permission);
            ActivityCompat.requestPermissions(this, new String[]{permission}, result);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (permissions.length <= 0)
            return;

        int readContactsI = -1;
        for (int i = 0; i < permissions.length; i++) {
            Log.i("[Permission] " + permissions[i] + " is " + (grantResults[i] == PackageManager.PERMISSION_GRANTED ? "granted" : "denied"));
            if (permissions[i].compareTo(Manifest.permission.READ_CONTACTS) == 0 ||
                    permissions[i].compareTo(Manifest.permission.WRITE_CONTACTS) == 0)
                readContactsI = i;
        }

        switch (requestCode) {
            case PERMISSIONS_REQUEST_SYNC:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ContactsManager.getInstance().initializeSyncAccount(this);
                } else {
                    ContactsManager.getInstance().initializeContactManager(this);
                }
                break;
            case PERMISSIONS_RECORD_AUDIO_ECHO_CANCELLER:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ((SettingsFragment) fragment).startEchoCancellerCalibration();
                } else {
                    ((SettingsFragment) fragment).echoCalibrationFail();
                }
                break;
            case PERMISSIONS_READ_EXTERNAL_STORAGE_DEVICE_RINGTONE:
                if (permissions[0].compareTo(Manifest.permission.READ_EXTERNAL_STORAGE) != 0)
                    break;
                boolean enableRingtone = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
                LinphonePreferences.instance().enableDeviceRingtone(enableRingtone);
                LinphoneManager.getInstance().enableDeviceRingtone(enableRingtone);
                break;
            case PERMISSIONS_RECORD_AUDIO_ECHO_TESTER:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    ((SettingsFragment) fragment).startEchoTester();
                break;
        }
        if (readContactsI >= 0 && grantResults[readContactsI] == PackageManager.PERMISSION_GRANTED) {
            ContactsManager.getInstance().enableContactsAccess();
           if (/* !ContactsManager.getInstance().contactsFetchedOnce()*/true) {
                ContactsManager.getInstance().enableContactsAccess();
                ContactsManager.getInstance().fetchContactsAsync();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ArrayList<String> permissionsList = new ArrayList<String>();

        int contacts = getPackageManager().checkPermission(Manifest.permission.READ_CONTACTS, getPackageName());
        Log.i("[Permission] Contacts permission is " + (contacts == PackageManager.PERMISSION_GRANTED ? "granted" : "denied"));

        int readPhone = getPackageManager().checkPermission(Manifest.permission.READ_PHONE_STATE, getPackageName());
        Log.i("[Permission] Read phone state permission is " + (readPhone == PackageManager.PERMISSION_GRANTED ? "granted" : "denied"));

        int ringtone = getPackageManager().checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, getPackageName());
        Log.i("[Permission] Read external storage for ring tone permission is " + (ringtone == PackageManager.PERMISSION_GRANTED ? "granted" : "denied"));

        if (ringtone != PackageManager.PERMISSION_GRANTED) {
            if (LinphonePreferences.instance().firstTimeAskingForPermission(Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Log.i("[Permission] Asking for read external storage for ring tone");
                permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
        if (readPhone != PackageManager.PERMISSION_GRANTED) {
            if (LinphonePreferences.instance().firstTimeAskingForPermission(Manifest.permission.READ_PHONE_STATE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                Log.i("[Permission] Asking for read phone state");
                permissionsList.add(Manifest.permission.READ_PHONE_STATE);
            }
        }
        if (contacts != PackageManager.PERMISSION_GRANTED) {
            if (LinphonePreferences.instance().firstTimeAskingForPermission(Manifest.permission.READ_CONTACTS) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                Log.i("[Permission] Asking for contacts");
                permissionsList.add(Manifest.permission.READ_CONTACTS);
            }
        } else {
            if (/*!ContactsManager.getInstance().contactsFetchedOnce()*/true) {
                ContactsManager.getInstance().enableContactsAccess();
                ContactsManager.getInstance().fetchContactsAsync();
            }
        }

        if (permissionsList.size() > 0) {
            String[] permissions = new String[permissionsList.size()];
            permissions = permissionsList.toArray(permissions);
            ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_READ_EXTERNAL_STORAGE_DEVICE_RINGTONE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("currentFragment", currentFragment);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void disableGoToCall() {
        doNotGoToCallActivity = true;
    }

    @Override
    protected void onResume() {
        super.onResume();


        if (!LinphoneService.isReady()) {
            startService(new Intent(Intent.ACTION_MAIN).setClass(this, LinphoneService.class));
        }
        if (!DataService.isReady()) {
            startService(new Intent(Intent.ACTION_MAIN).setClass(this, DataService.class));
        }

        Core lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
        if (lc != null) {
            lc.addListener(mListener);
            if (!LinphoneService.instance().displayServiceNotification()) {
                lc.refreshRegisters();
            }
        }

        refreshAccounts();

        if (getResources().getBoolean(R.bool.enable_in_app_purchase)) {
            isTrialAccount();
        }

        displayMissedChats(LinphoneManager.getInstance().getUnreadMessageCount());
        displayMissedCalls(LinphoneManager.getLc().getMissedCallsCount());

        LinphoneManager.getInstance().changeStatusToOnline();

        if (getIntent().getIntExtra("PreviousActivity", 0) != CALL_ACTIVITY && !doNotGoToCallActivity) {
            if (LinphoneManager.getLc().getCalls().length > 0) {
                Call call = LinphoneManager.getLc().getCalls()[0];
                Call.State onCallStateChanged = call.getState();

                if (onCallStateChanged == State.IncomingReceived) {
                    startActivity(new Intent(this, CallIncomingActivity.class));
                } else if (onCallStateChanged == State.OutgoingInit || onCallStateChanged == State.OutgoingProgress || onCallStateChanged == State.OutgoingRinging) {
                    startActivity(new Intent(this, CallOutgoingActivity.class));
                } else {
                    startIncallActivity(call);
                }
            }
        }

        Intent intent = getIntent();

        if (intent.getStringExtra("msgShared") != null) {
            displayChat(null, intent.getStringExtra("msgShared"), null);
            intent.putExtra("msgShared", "");
        }
        if (intent.getStringExtra("fileShared") != null && intent.getStringExtra("fileShared") != "") {
            displayChat(null, null, intent.getStringExtra("fileShared"));
            intent.putExtra("fileShared", "");
        }
        doNotGoToCallActivity = false;
        isOnBackground = false;

        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null && extras.containsKey("SipUriOrNumber")) {
                mAddressWaitingToBeCalled = extras.getString("SipUriOrNumber");
                intent.removeExtra("SipUriOrNumber");
                goToDialerFragment();
            }
        }

        if (DataService.isReady()) {
            DataService.instance().initialise(this);

            /* test */
            DataService.instance().requestFriend("18055835997", "15901801385", "Add test friend", new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        android.util.Log.d("xw", "xiongwei1 requestFriend: msg is " + msg);
                    }
            }, 0);
        }
    }

    @Override
    protected void onDestroy() {
        if (mOrientationHelper != null) {
            mOrientationHelper.disable();
            mOrientationHelper = null;
        }

        instance = null;
        super.onDestroy();

        unbindDrawables(findViewById(R.id.topLayout));
        System.gc();
    }

    private void unbindDrawables(View view) {
        if (view != null && view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (getCurrentFragment() == FragmentsAvailable.SETTINGS) {
            if (fragment instanceof SettingsFragment) {
                ((SettingsFragment) fragment).closePreferenceScreen();
            }
        }
        Bundle extras = intent.getExtras();
        if (extras != null && extras.getBoolean("GoToChat", false)) {
            String sipUri = extras.getString("ChatContactSipUri");
            doNotGoToCallActivity = true;
            displayChat(sipUri, null, null);
        } else if (extras != null && extras.getBoolean("GoToHistory", false)) {
            doNotGoToCallActivity = true;
            viewPager.setCurrentItem(FRAGMENGT_NOFITICATION);
        } else if (extras != null && extras.getBoolean("GoToInapp", false)) {
            doNotGoToCallActivity = true;
            displayInapp();
        } else if (extras != null && extras.getBoolean("Notification", false)) {
            if (LinphoneManager.getLc().getCallsNb() > 0) {
                Call call = LinphoneManager.getLc().getCalls()[0];
                startIncallActivity(call);
            }
        } else if (extras != null && extras.getBoolean("StartCall", false)) {
            boolean extraBool = extras.getBoolean("StartCall", false);
            if (CallActivity.isInstanciated()) {
                CallActivity.instance().startIncomingCallActivity();
            } else {
                mAddressWaitingToBeCalled = extras.getString("NumberToCall");
                goToDialerFragment();
                //startActivity(new Intent(this, CallIncomingActivity.class));
            }
        } else {
            DialerFragment dialerFragment = DialerFragment.instance();
            if (dialerFragment != null) {
                if (extras != null && extras.containsKey("SipUriOrNumber")) {
                    if (getResources().getBoolean(R.bool.automatically_start_intercepted_outgoing_gsm_call)) {
                        ((DialerFragment) dialerFragment).newOutgoingCall(extras.getString("SipUriOrNumber"));
                    } else {
                        ((DialerFragment) dialerFragment).displayTextInAddressBar(extras.getString("SipUriOrNumber"));
                    }
                } else {
                    ((DialerFragment) dialerFragment).newOutgoingCall(intent);
                }
            } else {
                if (extras != null && extras.containsKey("SipUriOrNumber")) {
                    mAddressWaitingToBeCalled = extras.getString("SipUriOrNumber");
                    goToDialerFragment();
                }
            }
            if (LinphoneManager.getLc().getCalls().length > 0) {
                // If a call is ringing, start incomingcallactivity
                Collection<Call.State> incoming = new ArrayList<Call.State>();
                incoming.add(Call.State.IncomingReceived);
                if (LinphoneUtils.getCallsInState(LinphoneManager.getLc(), incoming).size() > 0) {
                    if (CallActivity.isInstanciated()) {
                        CallActivity.instance().startIncomingCallActivity();
                    } else {
                        startActivity(new Intent(this, CallIncomingActivity.class));
                    }
                }
            }
        }
    }

    public boolean isOnBackground() {
        return isOnBackground;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            switch (currentFragment) {
                case DIALER:
                case CONTACTS_LIST:
                case HISTORY_LIST:
                case CHAT_LIST:
                    boolean isBackgroundModeActive = LinphonePreferences.instance().isBackgroundModeEnabled();
                    if (!isBackgroundModeActive) {
                        stopService(new Intent(Intent.ACTION_MAIN).setClass(this, LinphoneService.class));
                        stopService(new Intent(Intent.ACTION_MAIN).setClass(this, DataService.class));
                        finish();
                    } else if (LinphoneUtils.onKeyBackGoHome(this, keyCode, event)) {
                        return true;
                    }
                    break;
                case GROUP_CHAT:
                    BeautyMirrorActivity.instance().goToChatList();
                    return true;
                default:
                    break;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    //SIDE MENU
    public void openOrCloseSideMenu(boolean open) {
        if (open) {
            sideMenu.openDrawer(sideMenuContent);
        } else {
            sideMenu.closeDrawer(sideMenuContent);
        }
    }

    public void initSideMenu() {
        sideMenu = (DrawerLayout) findViewById(R.id.side_menu);
        sideMenuItems = new ArrayList<String>();
        sideMenuItems.add(getResources().getString(R.string.menu_assistant));
        sideMenuItems.add(getResources().getString(R.string.menu_settings));
        if (getResources().getBoolean(R.bool.enable_in_app_purchase)) {
            sideMenuItems.add(getResources().getString(R.string.inapp));
        }
        sideMenuContent = (RelativeLayout) findViewById(R.id.side_menu_content);
        sideMenuItemList = (ListView) findViewById(R.id.item_list);

        sideMenuItemList.setAdapter(new ArrayAdapter<String>(this, R.layout.side_menu_item_cell, sideMenuItems));
        sideMenuItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (sideMenuItemList.getAdapter().getItem(i).toString().equals(getString(R.string.menu_settings))) {
                    BeautyMirrorActivity.instance().displaySettings();
                }
                if (sideMenuItemList.getAdapter().getItem(i).toString().equals(getString(R.string.menu_assistant))) {
                    BeautyMirrorActivity.instance().displayAssistant();
                }
                if (getResources().getBoolean(R.bool.enable_in_app_purchase)) {
                    if (sideMenuItemList.getAdapter().getItem(i).toString().equals(getString(R.string.inapp))) {
                        BeautyMirrorActivity.instance().displayInapp();
                    }
                }
                openOrCloseSideMenu(false);
            }
        });

        initAccounts();
    }

    private int getStatusIconResource(RegistrationState state) {
        try {
            if (state == RegistrationState.Ok) {
                return R.drawable.led_connected;
            } else if (state == RegistrationState.Progress) {
                return R.drawable.led_inprogress;
            } else if (state == RegistrationState.Failed) {
                return R.drawable.led_error;
            } else {
                return R.drawable.led_disconnected;
            }
        } catch (Exception e) {
            Log.e(e);
        }

        return R.drawable.led_disconnected;
    }

    private void displayMainAccount() {
        defaultAccount.setVisibility(View.VISIBLE);
        ImageView status = (ImageView) defaultAccount.findViewById(R.id.main_account_status);
        TextView address = (TextView) defaultAccount.findViewById(R.id.main_account_address);
        TextView displayName = (TextView) defaultAccount.findViewById(R.id.main_account_display_name);


        ProxyConfig proxy = LinphoneManager.getLc().getDefaultProxyConfig();
        if (proxy == null) {
            displayName.setText(getString(R.string.no_account));
            status.setVisibility(View.GONE);
            address.setText("");
            LinphoneManager.getInstance().subscribeFriendList(false);

            defaultAccount.setOnClickListener(null);
        } else {
            address.setText(proxy.getIdentityAddress().asStringUriOnly());
            displayName.setText(LinphoneUtils.getAddressDisplayName(proxy.getIdentityAddress()));
            status.setImageResource(getStatusIconResource(proxy.getState()));
            status.setVisibility(View.VISIBLE);

            defaultAccount.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    BeautyMirrorActivity.instance().displayAccountSettings(LinphonePreferences.instance().getDefaultAccountIndex());
                    openOrCloseSideMenu(false);
                }
            });
        }
    }

    public void refreshAccounts() {
        if (LinphoneManager.getLc().getProxyConfigList() != null &&
                LinphoneManager.getLc().getProxyConfigList().length > 1) {
            accountsList.setVisibility(View.VISIBLE);
            accountsList.setAdapter(new AccountsListAdapter());
            accountsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (view != null && view.getTag() != null) {
                        int position = Integer.parseInt(view.getTag().toString());
                        BeautyMirrorActivity.instance().displayAccountSettings(position);
                    }
                    openOrCloseSideMenu(false);
                }
            });
        } else {
            accountsList.setVisibility(View.GONE);
        }
        displayMainAccount();
    }

    private void initAccounts() {
        accountsList = (ListView) findViewById(R.id.accounts_list);
        defaultAccount = (RelativeLayout) findViewById(R.id.default_account);
    }

    class AccountsListAdapter extends BaseAdapter {
        List<ProxyConfig> proxy_list;

        AccountsListAdapter() {
            proxy_list = new ArrayList<ProxyConfig>();
            refresh();
        }

        public void refresh() {
            proxy_list = new ArrayList<ProxyConfig>();
            for (ProxyConfig proxyConfig : LinphoneManager.getLc().getProxyConfigList()) {
                if (proxyConfig != LinphoneManager.getLc().getDefaultProxyConfig()) {
                    proxy_list.add(proxyConfig);
                }
            }
        }

        public int getCount() {
            if (proxy_list != null) {
                return proxy_list.size();
            } else {
                return 0;
            }
        }

        public Object getItem(int position) {
            return proxy_list.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = null;
            ProxyConfig lpc = (ProxyConfig) getItem(position);
            if (convertView != null) {
                view = convertView;
            } else {
                view = getLayoutInflater().inflate(R.layout.side_menu_account_cell, parent, false);
            }

            ImageView status = (ImageView) view.findViewById(R.id.account_status);
            TextView address = (TextView) view.findViewById(R.id.account_address);
            String sipAddress = lpc.getIdentityAddress().asStringUriOnly();

            address.setText(sipAddress);

            int nbAccounts = LinphonePreferences.instance().getAccountCount();
            int accountIndex = 0;

            for (int i = 0; i < nbAccounts; i++) {
                String username = LinphonePreferences.instance().getAccountUsername(i);
                String domain = LinphonePreferences.instance().getAccountDomain(i);
                String id = "sip:" + username + "@" + domain;
                if (id.equals(sipAddress)) {
                    accountIndex = i;
                    view.setTag(accountIndex);
                    break;
                }
            }
            status.setImageResource(getStatusIconResource(lpc.getState()));
            return view;
        }
    }

    //Inapp Purchase
    private void isTrialAccount() {
        if (LinphoneManager.getLc().getDefaultProxyConfig() != null && LinphonePreferences.instance().getInappPopupTime() != null) {
            XmlRpcHelper helper = new XmlRpcHelper();
            helper.isTrialAccountAsync(new XmlRpcListenerBase() {
                @Override
                public void onTrialAccountFetched(boolean isTrial) {
                    isTrialAccount = isTrial;
                    getExpirationAccount();
                }

                @Override
                public void onError(String error) {
                }
            }, LinphonePreferences.instance().getAccountUsername(LinphonePreferences.instance().getDefaultAccountIndex()), LinphonePreferences.instance().getAccountHa1(LinphonePreferences.instance().getDefaultAccountIndex()));
        }
    }

    private void getExpirationAccount() {
        if (LinphoneManager.getLc().getDefaultProxyConfig() != null && LinphonePreferences.instance().getInappPopupTime() != null) {
            XmlRpcHelper helper = new XmlRpcHelper();
            helper.getAccountExpireAsync(new XmlRpcListenerBase() {
                @Override
                public void onAccountExpireFetched(String result) {
                    if (result != null) {
                        long timestamp = Long.parseLong(result);

                        Calendar calresult = Calendar.getInstance();
                        calresult.setTimeInMillis(timestamp);

                        int diff = getDiffDays(calresult, Calendar.getInstance());
                        if (diff != -1 && diff <= getResources().getInteger(R.integer.days_notification_shown)) {
                            displayInappNotification(timestampToHumanDate(calresult));
                        }
                    }
                }

                @Override
                public void onError(String error) {
                }
            }, LinphonePreferences.instance().getAccountUsername(LinphonePreferences.instance().getDefaultAccountIndex()), LinphonePreferences.instance().getAccountHa1(LinphonePreferences.instance().getDefaultAccountIndex()));
        }
    }

    public void displayInappNotification(String date) {
        Timestamp now = new Timestamp(new Date().getTime());
        if (LinphonePreferences.instance().getInappPopupTime() != null && Long.parseLong(LinphonePreferences.instance().getInappPopupTime()) > now.getTime()) {
            return;
        } else {
            long newDate = now.getTime() + getResources().getInteger(R.integer.time_between_inapp_notification);
            LinphonePreferences.instance().setInappPopupTime(String.valueOf(newDate));
        }
        if (isTrialAccount) {
            LinphoneService.instance().displayInappNotification(String.format(getString(R.string.inapp_notification_trial_expire), date));
        } else {
            LinphoneService.instance().displayInappNotification(String.format(getString(R.string.inapp_notification_account_expire), date));
        }

    }

    private String timestampToHumanDate(Calendar cal) {
        SimpleDateFormat dateFormat;
        dateFormat = new SimpleDateFormat(getResources().getString(R.string.inapp_popup_date_format));
        return dateFormat.format(cal.getTime());
    }

    private int getDiffDays(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            return -1;
        }
        if (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) {
            return cal1.get(Calendar.DAY_OF_YEAR) - cal2.get(Calendar.DAY_OF_YEAR);
        }
        return -1;
    }


    private void initTab() {
        initViewPager();
        initMainTabsViewID();
        initTabRes();
    }

    private void initViewPager(){
        viewPager=findViewById(R.id.viewPager);
        adapter=new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
            public void onPageSelected(int position) {
            }
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }
            public void onPageScrollStateChanged(int state) {
                if (state == 2) {
                    switch (viewPager.getCurrentItem()) {
                        case FRAGMENGT_PHOTO:
                            mHome.setChecked(true);
                            break;
                        case FRAGMENGT_NOFITICATION:
                            mNotification.setChecked(true);
                            break;
                        case FRAGMENGT_CONTACT:
                            mContacts.setChecked(true);
                            break;
                        case FRAGMENGT_MINE:
                            mMine.setChecked(true);
                            break;
                    }
                    if(viewPager.getCurrentItem() == FRAGMENGT_NOFITICATION ){
                        mHistoryListFragment.setReadedCallLogsAndNotifications();
                        mNotification.setBadgeNumber(0);
                    }else {
                        mMissedCount = mHistoryListFragment.getMissedCallLogsAndNotifications();
                        refreshBagdeNumber(mMissedCount);
                    }

                }
                LinphoneUtils.hideKeyboard(BeautyMirrorActivity.this);
            }
        });
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            mPhotoFragment = new PhotoFragment();
            mHistoryListFragment = new HistoryListFragment();
            mContactsListFragment = new ContactsListFragment();
            mMineFragment = new MineFragment();
            mDialerFragment = new DialerFragment();
            mSettingsFragment = new SettingsFragment();
            mAccountPreferencesFragment = new AccountPreferencesFragment();
        }
        @Override
        public Fragment getItem(int arg0) {
            switch (arg0) {
                case FRAGMENGT_PHOTO:
                    fragment = mPhotoFragment;
                    currentFragment = FragmentsAvailable.PHOTO;
                    break;
                case FRAGMENGT_NOFITICATION:
                    fragment = mHistoryListFragment;
                    currentFragment = FragmentsAvailable.HISTORY_LIST;
                    break;
                case FRAGMENGT_CONTACT:
                    fragment = mContactsListFragment;
                    currentFragment = FragmentsAvailable.CONTACTS_LIST;
                    break;
                case FRAGMENGT_MINE:
                    fragment = mMineFragment;
                    currentFragment = FragmentsAvailable.MINE;
                    break;
                case FRAGMENGT_DIALER:
                    fragment = mDialerFragment;
                    currentFragment = FragmentsAvailable.DIALER;
                    break;
                case FRAGMENGT_SETTINGS:
                    fragment = mSettingsFragment;
                    currentFragment = FragmentsAvailable.SETTINGS;
                    break;
                case FRAGMENGT_ACCOUNT_SETTINGS:
                    fragment = mAccountPreferencesFragment;
                    currentFragment = FragmentsAvailable.ACCOUNT_SETTINGS;
                    break;
                default:
                    fragment=null;
                   break;
            }
            return fragment;
        }
        @Override
        public int getCount() {
            return 7;//fragments.size();
        }

        @Override
        public Object instantiateItem(ViewGroup vg, int position) {
            return super.instantiateItem(vg, position);
//            Fragment f = (Fragment) super.instantiateItem(vg, position);
//            //f.setTitle(title);
//            return f;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }


    private void initTabRes() {
        Drawable drawable = getResources().getDrawable(R.drawable.menu_home_selector_tab);
        drawable.setBounds(0, 0, 40, 40);

        Drawable drawable1 = getResources().getDrawable(R.drawable.menu_message_selector_tab);
        drawable1.setBounds(0, 0, 40, 40);

        Drawable drawable2 = getResources().getDrawable(R.drawable.menu_contacts_selector_tab
                );
        drawable2.setBounds(0, 0, 40, 40);

        Drawable drawable3 = getResources().getDrawable(R.drawable.menu_mine_selector_tab);
        drawable3.setBounds(0, 0, 40, 40);

        mHome.setCompoundDrawables(null, drawable, null, null);
        mContacts.setCompoundDrawables(null, drawable2, null, null);
        mNotification.setCompoundDrawables(null, drawable1, null, null);
        mMine.setCompoundDrawables(null, drawable3, null, null);

    }

    public void initMainTabsViewID() {
        mRadionGroupMain = (RadioGroup) findViewById(R.id.rg_home_radiogroup);
        mHome = (BadgeRadioButton) findViewById(R.id.tab_main_home);
        mContacts = (BadgeRadioButton) findViewById(R.id.tab_main_contacts);
        mNotification = (BadgeRadioButton) findViewById(R.id.tab_main_notification);
        mDialer = (BadgeRadioButton) findViewById(R.id.tab_main_dialer);
        mMine = (BadgeRadioButton) findViewById(R.id.tab_main_mine);

    }
}
