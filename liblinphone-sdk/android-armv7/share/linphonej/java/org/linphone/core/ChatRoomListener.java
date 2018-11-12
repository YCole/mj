/*
ChatRoomListener.java
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
  * An object to handle the callbacks for the handling a LinphoneChatRoom objects. 
  */
public interface ChatRoomListener {
    /**
      * Callback used to notify a chat room that a message has been received but we
      * were unable to decrypt it. 
      */
    public void onUndecryptableMessageReceived(ChatRoom cr, ChatMessage msg);

    /**
      * Callback used to notify a chat room has been left. 
      */
    public void onConferenceLeft(ChatRoom cr, EventLog eventLog);

    /**
      * Callback used to notify a chat room state has changed. 
      */
    public void onStateChanged(ChatRoom cr, ChatRoom.State newState);

    /**
      * Callback used to notify a chat room that a participant has been added. 
      */
    public void onParticipantAdded(ChatRoom cr, EventLog eventLog);

    /**
      * Callback used to notify that the subject of a chat room has changed. 
      */
    public void onSubjectChanged(ChatRoom cr, EventLog eventLog);

    /**
      * Is composing notification callback prototype. 
      */
    public void onIsComposingReceived(ChatRoom cr, Address remoteAddr, boolean isComposing);

    /**
      * Callback used when a group chat room is created server-side to generate the
      * address of the chat room. 
      */
    public void onConferenceAddressGeneration(ChatRoom cr);

    /**
      * Callback used to notify a chat room that a chat message is being sent. 
      */
    public void onChatMessageSent(ChatRoom cr, EventLog eventLog);

    /**
      * Callback used when a group chat room server is subscribing to registration
      * state of a participant. 
      */
    public void onParticipantRegistrationSubscriptionRequested(ChatRoom cr, Address participantAddr);

    /**
      * Callback used to notify a chat room that a chat message has been received. 
      */
    public void onChatMessageReceived(ChatRoom cr, EventLog eventLog);

    /**
      * Callback used to tell the core whether or not to store the incoming message in
      * db or not using linphone_chat_message_set_to_be_stored. 
      */
    public void onChatMessageShouldBeStored(ChatRoom cr, ChatMessage msg);

    /**
      * Callback used to notify a chat room that the admin status of a participant has
      * been changed. 
      */
    public void onParticipantAdminStatusChanged(ChatRoom cr, EventLog eventLog);

    /**
      * Callback used to notify a chat room that a message has been received. 
      */
    public void onMessageReceived(ChatRoom cr, ChatMessage msg);

    /**
      * Callback used to notify a chat room that a participant has been removed. 
      */
    public void onParticipantDeviceRemoved(ChatRoom cr, EventLog eventLog);

    /**
      * Callback used when a group chat room server is checking participants
      * capabilities. 
      */
    public void onParticipantsCapabilitiesChecked(ChatRoom cr, Address deviceAddr, Address[] participantsAddr);

    /**
      * Callback used to notify a chat room that a participant has been removed. 
      */
    public void onParticipantRemoved(ChatRoom cr, EventLog eventLog);

    /**
      * Callback used when a group chat room server is unsubscribing to registration
      * state of a participant. 
      */
    public void onParticipantRegistrationUnsubscriptionRequested(ChatRoom cr, Address participantAddr);

    /**
      * Callback used to notify a chat room has been joined. 
      */
    public void onConferenceJoined(ChatRoom cr, EventLog eventLog);

    /**
      * Callback used when a group chat room server is adding participant to fetch all
      * device information from participant. 
      */
    public void onParticipantDeviceFetchRequested(ChatRoom cr, Address participantAddr);

    /**
      * Callback used to notify a chat room that a participant has been added. 
      */
    public void onParticipantDeviceAdded(ChatRoom cr, EventLog eventLog);

}