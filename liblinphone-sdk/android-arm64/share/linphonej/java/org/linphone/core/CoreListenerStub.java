/*
CoreListenerStub.java
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


public class CoreListenerStub implements CoreListener {
    @Override
    public void onTransferStateChanged(Core lc, Call transfered, Call.State newCallState) {
        // Auto-generated method stub
    }

    @Override
    public void onFriendListCreated(Core lc, FriendList list) {
        // Auto-generated method stub
    }

    @Override
    public void onSubscriptionStateChanged(Core lc, Event lev, SubscriptionState state) {
        // Auto-generated method stub
    }

    @Override
    public void onCallLogUpdated(Core lc, CallLog newcl) {
        // Auto-generated method stub
    }

    @Override
    public void onCallStateChanged(Core lc, Call call, Call.State cstate, String message) {
        // Auto-generated method stub
    }

    @Override
    public void onAuthenticationRequested(Core lc, AuthInfo authInfo, AuthMethod method) {
        // Auto-generated method stub
    }

    @Override
    public void onNotifyPresenceReceivedForUriOrTel(Core lc, Friend lf, String uriOrTel, PresenceModel presenceModel) {
        // Auto-generated method stub
    }

    @Override
    public void onChatRoomStateChanged(Core lc, ChatRoom cr, ChatRoom.State state) {
        // Auto-generated method stub
    }

    @Override
    public void onBuddyInfoUpdated(Core lc, Friend lf) {
        // Auto-generated method stub
    }

    @Override
    public void onNetworkReachable(Core lc, boolean reachable) {
        // Auto-generated method stub
    }

    @Override
    public void onNotifyReceived(Core lc, Event lev, String notifiedEvent, Content body) {
        // Auto-generated method stub
    }

    @Override
    public void onNewSubscriptionRequested(Core lc, Friend lf, String url) {
        // Auto-generated method stub
    }

    @Override
    public void onRegistrationStateChanged(Core lc, ProxyConfig cfg, RegistrationState cstate, String message) {
        // Auto-generated method stub
    }

    @Override
    public void onNotifyPresenceReceived(Core lc, Friend lf) {
        // Auto-generated method stub
    }

    @Override
    public void onEcCalibrationAudioInit(Core lc) {
        // Auto-generated method stub
    }

    @Override
    public void onMessageReceived(Core lc, ChatRoom room, ChatMessage message) {
        // Auto-generated method stub
    }

    @Override
    public void onEcCalibrationResult(Core lc, EcCalibratorStatus status, int delayMs) {
        // Auto-generated method stub
    }

    @Override
    public void onSubscribeReceived(Core lc, Event lev, String subscribeEvent, Content body) {
        // Auto-generated method stub
    }

    @Override
    public void onInfoReceived(Core lc, Call call, InfoMessage msg) {
        // Auto-generated method stub
    }

    @Override
    public void onCallStatsUpdated(Core lc, Call call, CallStats stats) {
        // Auto-generated method stub
    }

    @Override
    public void onFriendListRemoved(Core lc, FriendList list) {
        // Auto-generated method stub
    }

    @Override
    public void onReferReceived(Core lc, String referTo) {
        // Auto-generated method stub
    }

    @Override
    public void onQrcodeFound(Core lc, String result) {
        // Auto-generated method stub
    }

    @Override
    public void onConfiguringStatus(Core lc, ConfiguringState status, String message) {
        // Auto-generated method stub
    }

    @Override
    public void onCallCreated(Core lc, Call call) {
        // Auto-generated method stub
    }

    @Override
    public void onPublishStateChanged(Core lc, Event lev, PublishState state) {
        // Auto-generated method stub
    }

    @Override
    public void onCallEncryptionChanged(Core lc, Call call, boolean on, String authenticationToken) {
        // Auto-generated method stub
    }

    @Override
    public void onIsComposingReceived(Core lc, ChatRoom room) {
        // Auto-generated method stub
    }

    @Override
    public void onMessageReceivedUnableDecrypt(Core lc, ChatRoom room, ChatMessage message) {
        // Auto-generated method stub
    }

    @Override
    public void onLogCollectionUploadProgressIndication(Core lc, int offset, int total) {
        // Auto-generated method stub
    }

    @Override
    public void onVersionUpdateCheckResultReceived(Core lc, VersionUpdateCheckResult result, String version, String url) {
        // Auto-generated method stub
    }

    @Override
    public void onEcCalibrationAudioUninit(Core lc) {
        // Auto-generated method stub
    }

    @Override
    public void onGlobalStateChanged(Core lc, GlobalState gstate, String message) {
        // Auto-generated method stub
    }

    @Override
    public void onLogCollectionUploadStateChanged(Core lc, Core.LogCollectionUploadState state, String info) {
        // Auto-generated method stub
    }

    @Override
    public void onDtmfReceived(Core lc, Call call, int dtmf) {
        // Auto-generated method stub
    }

}