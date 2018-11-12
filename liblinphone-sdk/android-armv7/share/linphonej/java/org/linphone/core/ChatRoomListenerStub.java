/*
ChatRoomListenerStub.java
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


public class ChatRoomListenerStub implements ChatRoomListener {
    @Override
    public void onUndecryptableMessageReceived(ChatRoom cr, ChatMessage msg) {
        // Auto-generated method stub
    }

    @Override
    public void onConferenceLeft(ChatRoom cr, EventLog eventLog) {
        // Auto-generated method stub
    }

    @Override
    public void onStateChanged(ChatRoom cr, ChatRoom.State newState) {
        // Auto-generated method stub
    }

    @Override
    public void onParticipantAdded(ChatRoom cr, EventLog eventLog) {
        // Auto-generated method stub
    }

    @Override
    public void onSubjectChanged(ChatRoom cr, EventLog eventLog) {
        // Auto-generated method stub
    }

    @Override
    public void onIsComposingReceived(ChatRoom cr, Address remoteAddr, boolean isComposing) {
        // Auto-generated method stub
    }

    @Override
    public void onConferenceAddressGeneration(ChatRoom cr) {
        // Auto-generated method stub
    }

    @Override
    public void onChatMessageSent(ChatRoom cr, EventLog eventLog) {
        // Auto-generated method stub
    }

    @Override
    public void onParticipantRegistrationSubscriptionRequested(ChatRoom cr, Address participantAddr) {
        // Auto-generated method stub
    }

    @Override
    public void onChatMessageReceived(ChatRoom cr, EventLog eventLog) {
        // Auto-generated method stub
    }

    @Override
    public void onChatMessageShouldBeStored(ChatRoom cr, ChatMessage msg) {
        // Auto-generated method stub
    }

    @Override
    public void onParticipantAdminStatusChanged(ChatRoom cr, EventLog eventLog) {
        // Auto-generated method stub
    }

    @Override
    public void onMessageReceived(ChatRoom cr, ChatMessage msg) {
        // Auto-generated method stub
    }

    @Override
    public void onParticipantDeviceRemoved(ChatRoom cr, EventLog eventLog) {
        // Auto-generated method stub
    }

    @Override
    public void onParticipantsCapabilitiesChecked(ChatRoom cr, Address deviceAddr, Address[] participantsAddr) {
        // Auto-generated method stub
    }

    @Override
    public void onParticipantRemoved(ChatRoom cr, EventLog eventLog) {
        // Auto-generated method stub
    }

    @Override
    public void onParticipantRegistrationUnsubscriptionRequested(ChatRoom cr, Address participantAddr) {
        // Auto-generated method stub
    }

    @Override
    public void onConferenceJoined(ChatRoom cr, EventLog eventLog) {
        // Auto-generated method stub
    }

    @Override
    public void onParticipantDeviceFetchRequested(ChatRoom cr, Address participantAddr) {
        // Auto-generated method stub
    }

    @Override
    public void onParticipantDeviceAdded(ChatRoom cr, EventLog eventLog) {
        // Auto-generated method stub
    }

}