/*
CoreListener.java
Copyright (C) 2010  Belledonne Communications, Grenoble, France

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

package org.linphone.core;


/**
  * That class holds all the callbacks which are called by LinphoneCore. 
  */
public interface CoreListener {
    /**
      * Callback for notifying progresses of transfers. 
      */
    public void onTransferStateChanged(Core lc, Call transfered, Call.State newCallState);

    /**
      * Callback prototype for reporting when a friend list has been added to the core
      * friends list. 
      */
    public void onFriendListCreated(Core lc, FriendList list);

    /**
      * Callback prototype for notifying the application about changes of subscription
      * states, including arrival of new subscriptions. 
      */
    public void onSubscriptionStateChanged(Core lc, Event lev, SubscriptionState state);

    /**
      * Callback to notify a new call-log entry has been added. 
      */
    public void onCallLogUpdated(Core lc, CallLog newcl);

    /**
      * Call state notification callback. 
      */
    public void onCallStateChanged(Core lc, Call call, Call.State cstate, String message);

    /**
      * Callback for requesting authentication information to application or user. 
      */
    public void onAuthenticationRequested(Core lc, AuthInfo authInfo, AuthMethod method);

    /**
      * Reports presence model change for a specific URI or phone number of a friend. 
      */
    public void onNotifyPresenceReceivedForUriOrTel(Core lc, Friend lf, String uriOrTel, PresenceModel presenceModel);

    /**
      * Callback prototype telling that a LinphoneChatRoom state has changed. 
      */
    public void onChatRoomStateChanged(Core lc, ChatRoom cr, ChatRoom.State state);

    /**
      * Callback prototype. 
      */
    public void onBuddyInfoUpdated(Core lc, Friend lf);

    /**
      * Callback prototype for reporting network change either automatically detected
      * or notified by linphone_core_set_network_reachable. 
      */
    public void onNetworkReachable(Core lc, boolean reachable);

    /**
      * Callback prototype for notifying the application about notification received
      * from the network. 
      */
    public void onNotifyReceived(Core lc, Event lev, String notifiedEvent, Content body);

    /**
      * Reports that a new subscription request has been received and wait for a
      * decision. 
      */
    public void onNewSubscriptionRequested(Core lc, Friend lf, String url);

    /**
      * Registration state notification callback prototype. 
      */
    public void onRegistrationStateChanged(Core lc, ProxyConfig cfg, RegistrationState cstate, String message);

    /**
      * Report status change for a friend previously added  to LinphoneCore. 
      */
    public void onNotifyPresenceReceived(Core lc, Friend lf);

    /**
      * Function prototype used by #linphone_core_cbs_set_ec_calibrator_audio_init(). 
      */
    public void onEcCalibrationAudioInit(Core lc);

    /**
      * Chat message callback prototype. 
      */
    public void onMessageReceived(Core lc, ChatRoom room, ChatMessage message);

    /**
      * Function prototype used by #linphone_core_cbs_set_ec_calibrator_result(). 
      */
    public void onEcCalibrationResult(Core lc, EcCalibratorStatus status, int delayMs);

    /**
      * Callback prototype for notifying the application about subscription received
      * from the network. 
      */
    public void onSubscribeReceived(Core lc, Event lev, String subscribeEvent, Content body);

    /**
      * Callback prototype for receiving info messages. 
      */
    public void onInfoReceived(Core lc, Call call, InfoMessage msg);

    /**
      * Callback for receiving quality statistics for calls. 
      */
    public void onCallStatsUpdated(Core lc, Call call, CallStats stats);

    /**
      * Callback prototype for reporting when a friend list has been removed from the
      * core friends list. 
      */
    public void onFriendListRemoved(Core lc, FriendList list);

    /**
      * Callback prototype. 
      */
    public void onReferReceived(Core lc, String referTo);

    /**
      * Callback prototype telling the result of decoded qrcode. 
      */
    public void onQrcodeFound(Core lc, String result);

    /**
      * Callback prototype for configuring status changes notification. 
      */
    public void onConfiguringStatus(Core lc, ConfiguringState status, String message);

    /**
      * Callback notifying that a new LinphoneCall (either incoming or outgoing) has
      * been created. 
      */
    public void onCallCreated(Core lc, Call call);

    /**
      * Callback prototype for notifying the application about changes of publish
      * states. 
      */
    public void onPublishStateChanged(Core lc, Event lev, PublishState state);

    /**
      * Call encryption changed callback. 
      */
    public void onCallEncryptionChanged(Core lc, Call call, boolean on, String authenticationToken);

    /**
      * Is composing notification callback prototype. 
      */
    public void onIsComposingReceived(Core lc, ChatRoom room);

    /**
      * Chat message not decrypted callback prototype. 
      */
    public void onMessageReceivedUnableDecrypt(Core lc, ChatRoom room, ChatMessage message);

    /**
      * Callback prototype for reporting log collection upload progress indication. 
      */
    public void onLogCollectionUploadProgressIndication(Core lc, int offset, int total);

    /**
      * Callback prototype for reporting the result of a version update check. 
      */
    public void onVersionUpdateCheckResultReceived(Core lc, VersionUpdateCheckResult result, String version, String url);

    /**
      * Function prototype used by #linphone_core_cbs_set_ec_calibrator_audio_uninit(). 
      */
    public void onEcCalibrationAudioUninit(Core lc);

    /**
      * Global state notification callback. 
      */
    public void onGlobalStateChanged(Core lc, GlobalState gstate, String message);

    /**
      * Callback prototype for reporting log collection upload state change. 
      */
    public void onLogCollectionUploadStateChanged(Core lc, Core.LogCollectionUploadState state, String info);

    /**
      * Callback for being notified of DTMFs received. 
      */
    public void onDtmfReceived(Core lc, Call call, int dtmf);

}