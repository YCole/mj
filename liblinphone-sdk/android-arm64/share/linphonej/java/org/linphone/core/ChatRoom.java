/*
ChatRoom.java
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
  * A chat room is the place where text messages are exchanged. 
  */
public interface ChatRoom {
    enum State {
        /**
        * Initial state. 
        */
        None(0),

        /**
        * Chat room is now instantiated on local. 
        */
        Instantiated(1),

        /**
        * One creation request was sent to the server. 
        */
        CreationPending(2),

        /**
        * Chat room was created on the server. 
        */
        Created(3),

        /**
        * Chat room creation failed. 
        */
        CreationFailed(4),

        /**
        * Wait for chat room termination. 
        */
        TerminationPending(5),

        /**
        * Chat room exists on server but not in local. 
        */
        Terminated(6),

        /**
        * The chat room termination failed. 
        */
        TerminationFailed(7),

        /**
        * Chat room was deleted on the server. 
        */
        Deleted(8);

        protected final int mValue;

        private State (int value) {
            mValue = value;
        }

        static public State fromInt(int value) throws RuntimeException {
            switch(value) {
            case 0: return None;
            case 1: return Instantiated;
            case 2: return CreationPending;
            case 3: return Created;
            case 4: return CreationFailed;
            case 5: return TerminationPending;
            case 6: return Terminated;
            case 7: return TerminationFailed;
            case 8: return Deleted;
            default:
                throw new RuntimeException("Unhandled enum value " + value + " for State");
            }
        }

        public int toInt() {
            return mValue;
        }
    };

    /**
      * get Curent Call associated to this chatroom if any To commit a message, use
      * linphone_chat_room_send_message 
      */
    public Call getCall();

    /**
      * Get the capabilities of a chat room. 
      */
    public int getCapabilities();

    /**
      * When realtime text is enabled linphone_call_params_realtime_text_enabled,
      * LinphoneCoreIsComposingReceivedCb is call everytime a char is received from
      * peer. 
      */
    public int getChar();

    /**
      * Gets the list of participants that are currently composing. 
      */
    public Address[] getComposingAddresses();

    /**
      * Get the conference address of the chat room. 
      */
    public Address getConferenceAddress();

    /**
      * Set the conference address of a group chat room. 
      */
    public void setConferenceAddress(Address confAddr);

    /**
      * Returns back pointer to #LinphoneCore object. 
      */
    public Core getCore();

    /**
      * Gets the number of events in a chat room. 
      */
    public int getHistoryEventsSize();

    /**
      * Gets the number of messages in a chat room. 
      */
    public int getHistorySize();

    /**
      * Tells whether the remote is currently composing a message. 
      */
    public boolean isRemoteComposing();

    /**
      * Gets the last chat message sent or received in this chat room. 
      */
    public ChatMessage getLastMessageInHistory();

    /**
      * Return the last updated time for the chat room. 
      */
    public long getLastUpdateTime();

    /**
      * get local address associated to  this #LinphoneChatRoom 
      */
    public Address getLocalAddress();

    /**
      * Get the participant representing myself in the chat room. 
      */
    public Participant getMe();

    /**
      * Get the number of participants in the chat room (that is without ourselves). 
      */
    public int getNbParticipants();

    /**
      * Get the list of participants of a chat room. 
      */
    public Participant[] getParticipants();

    /**
      * get peer address associated to  this #LinphoneChatRoom 
      */
    public Address getPeerAddress();

    /**
      * Get the state of the chat room. 
      */
    public ChatRoom.State getState();

    /**
      * Get the subject of a chat room. 
      */
    public String getSubject();

    /**
      * Set the subject of a chat room. 
      */
    public void setSubject(String subject);

    /**
      * Gets the number of unread messages in the chatroom. 
      */
    public int getUnreadMessagesCount();

    /**
      * Set the participant device. 
      */
    public void addCompatibleParticipants(Address deviceAddr, Address[] participantsCompatible);

    /**
      * Add a participant to a chat room. 
      */
    public void addParticipant(Address addr);

    /**
      * Add several participants to a chat room at once. 
      */
    public void addParticipants(Address[] addresses);

    /**
      * Tells whether a chat room is able to handle participants. 
      */
    public boolean canHandleParticipants();

    /**
      * Notifies the destination of the chat message being composed that the user is
      * typing a new message. 
      */
    public void compose();

    /**
      * Creates an empty message attached to a dedicated chat room. 
      */
    public ChatMessage createEmptyMessage();

    /**
      * Creates a message attached to a dedicated chat room with a particular content. 
      */
    public ChatMessage createFileTransferMessage(Content initialContent);

    /**
      * Creates a message attached to a dedicated chat room. 
      */
    public ChatMessage createMessage(String message);

    /**
      * Delete all messages from the history. 
      */
    public void deleteHistory();

    /**
      * Delete a message from the chat room history. 
      */
    public void deleteMessage(ChatMessage msg);

    /**
      * Gets the chat message sent or received in this chat room that matches the
      * message_id. 
      */
    public ChatMessage findMessage(String messageId);

    /**
      * Find a participant of a chat room from its address. 
      */
    public Participant findParticipant(Address addr);

    /**
      * Gets nb_message most recent messages from cr chat room, sorted from oldest to
      * most recent. 
      */
    public ChatMessage[] getHistory(int nbMessage);

    /**
      * Gets nb_events most recent events from cr chat room, sorted from oldest to most
      * recent. 
      */
    public EventLog[] getHistoryEvents(int nbEvents);

    /**
      * Gets nb_events most recent chat message events from cr chat room, sorted from
      * oldest to most recent. 
      */
    public EventLog[] getHistoryMessageEvents(int nbEvents);

    /**
      * Gets the partial list of messages in the given range, sorted from oldest to
      * most recent. 
      */
    public ChatMessage[] getHistoryRange(int begin, int end);

    /**
      * Gets the partial list of events in the given range, sorted from oldest to most
      * recent. 
      */
    public EventLog[] getHistoryRangeEvents(int begin, int end);

    /**
      * Gets the partial list of chat message events in the given range, sorted from
      * oldest to most recent. 
      */
    public EventLog[] getHistoryRangeMessageEvents(int begin, int end);

    /**
      * Return whether or not the chat room has been left. 
      */
    public boolean hasBeenLeft();

    /**
      * Check if a chat room has given capabilities. 
      */
    public boolean hasCapability(int mask);

    /**
      * Leave a chat room. 
      */
    public void leave();

    /**
      * Returns true if lime is available for given peer. 
      */
    public boolean limeAvailable();

    /**
      * Mark all messages of the conversation as read. 
      */
    public void markAsRead();

    /**
      * Used to receive a chat message when using async mechanism with IM encryption
      * engine. 
      */
    public void receiveChatMessage(ChatMessage msg);

    /**
      * Remove a participant of a chat room. 
      */
    public void removeParticipant(Participant participant);

    /**
      * Remove several participants of a chat room at once. 
      */
    public void removeParticipants(Participant[] participants);

    /**
      * Send a message to peer member of this chat room. 
      */
    public void sendChatMessage(ChatMessage msg);

    /**
      * Change the admin status of a participant of a chat room (you need to be an
      * admin yourself to do this). 
      */
    public void setParticipantAdminStatus(Participant participant, boolean isAdmin);

    /**
      * Set the participant device. 
      */
    public void setParticipantDevices(Address partAddr, Address[] partDevices);

    public void addListener(ChatRoomListener listener);

    public void removeListener(ChatRoomListener listener);

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class ChatRoomImpl implements ChatRoom {

    protected long nativePtr = 0;
    protected Object userData = null;
    protected Core core = null;

    protected ChatRoomImpl(long ptr) {
        nativePtr = ptr;
        core = getCore();
    }


    private native Call getCall(long nativePtr);
    @Override
    synchronized public Call getCall()  {
        synchronized(core) { 
        return (Call)getCall(nativePtr);
        }
    }

    private native int getCapabilities(long nativePtr);
    @Override
    synchronized public int getCapabilities()  {
        synchronized(core) { 
        return getCapabilities(nativePtr);
        }
    }

    private native int getChar(long nativePtr);
    @Override
    synchronized public int getChar()  {
        synchronized(core) { 
        return getChar(nativePtr);
        }
    }

    private native Address[] getComposingAddresses(long nativePtr);
    @Override
    synchronized public Address[] getComposingAddresses()  {
        synchronized(core) { 
        return getComposingAddresses(nativePtr);
        }
    }

    private native Address getConferenceAddress(long nativePtr);
    @Override
    synchronized public Address getConferenceAddress()  {
        synchronized(core) { 
        return (Address)getConferenceAddress(nativePtr);
        }
    }

    private native void setConferenceAddress(long nativePtr, Address confAddr);
    @Override
    synchronized public void setConferenceAddress(Address confAddr)  {
        synchronized(core) { 
        setConferenceAddress(nativePtr, confAddr);
        }
    }

    private native Core getCore(long nativePtr);
    @Override
    synchronized public Core getCore()  {
        
        return (Core)getCore(nativePtr);
    }

    private native int getHistoryEventsSize(long nativePtr);
    @Override
    synchronized public int getHistoryEventsSize()  {
        synchronized(core) { 
        return getHistoryEventsSize(nativePtr);
        }
    }

    private native int getHistorySize(long nativePtr);
    @Override
    synchronized public int getHistorySize()  {
        synchronized(core) { 
        return getHistorySize(nativePtr);
        }
    }

    private native boolean isRemoteComposing(long nativePtr);
    @Override
    synchronized public boolean isRemoteComposing()  {
        synchronized(core) { 
        return isRemoteComposing(nativePtr);
        }
    }

    private native ChatMessage getLastMessageInHistory(long nativePtr);
    @Override
    synchronized public ChatMessage getLastMessageInHistory()  {
        synchronized(core) { 
        return (ChatMessage)getLastMessageInHistory(nativePtr);
        }
    }

    private native long getLastUpdateTime(long nativePtr);
    @Override
    synchronized public long getLastUpdateTime()  {
        synchronized(core) { 
        return getLastUpdateTime(nativePtr);
        }
    }

    private native Address getLocalAddress(long nativePtr);
    @Override
    synchronized public Address getLocalAddress()  {
        synchronized(core) { 
        return (Address)getLocalAddress(nativePtr);
        }
    }

    private native Participant getMe(long nativePtr);
    @Override
    synchronized public Participant getMe()  {
        synchronized(core) { 
        return (Participant)getMe(nativePtr);
        }
    }

    private native int getNbParticipants(long nativePtr);
    @Override
    synchronized public int getNbParticipants()  {
        synchronized(core) { 
        return getNbParticipants(nativePtr);
        }
    }

    private native Participant[] getParticipants(long nativePtr);
    @Override
    synchronized public Participant[] getParticipants()  {
        synchronized(core) { 
        return getParticipants(nativePtr);
        }
    }

    private native Address getPeerAddress(long nativePtr);
    @Override
    synchronized public Address getPeerAddress()  {
        synchronized(core) { 
        return (Address)getPeerAddress(nativePtr);
        }
    }

    private native int getState(long nativePtr);
    @Override
    synchronized public ChatRoom.State getState()  {
        synchronized(core) { 
        return ChatRoom.State.fromInt(getState(nativePtr));
        }
    }

    private native String getSubject(long nativePtr);
    @Override
    synchronized public String getSubject()  {
        synchronized(core) { 
        return getSubject(nativePtr);
        }
    }

    private native void setSubject(long nativePtr, String subject);
    @Override
    synchronized public void setSubject(String subject)  {
        synchronized(core) { 
        setSubject(nativePtr, subject);
        }
    }

    private native int getUnreadMessagesCount(long nativePtr);
    @Override
    synchronized public int getUnreadMessagesCount()  {
        synchronized(core) { 
        return getUnreadMessagesCount(nativePtr);
        }
    }

    private native void addCompatibleParticipants(long nativePtr, Address deviceAddr, Address[] participantsCompatible);
    @Override
    synchronized public void addCompatibleParticipants(Address deviceAddr, Address[] participantsCompatible)  {
        synchronized(core) { 
        addCompatibleParticipants(nativePtr, deviceAddr, participantsCompatible);
        }
    }

    private native void addParticipant(long nativePtr, Address addr);
    @Override
    synchronized public void addParticipant(Address addr)  {
        synchronized(core) { 
        addParticipant(nativePtr, addr);
        }
    }

    private native void addParticipants(long nativePtr, Address[] addresses);
    @Override
    synchronized public void addParticipants(Address[] addresses)  {
        synchronized(core) { 
        addParticipants(nativePtr, addresses);
        }
    }

    private native boolean canHandleParticipants(long nativePtr);
    @Override
    synchronized public boolean canHandleParticipants()  {
        synchronized(core) { 
        return canHandleParticipants(nativePtr);
        }
    }

    private native void compose(long nativePtr);
    @Override
    synchronized public void compose()  {
        synchronized(core) { 
        compose(nativePtr);
        }
    }

    private native ChatMessage createEmptyMessage(long nativePtr);
    @Override
    synchronized public ChatMessage createEmptyMessage()  {
        synchronized(core) { 
        return (ChatMessage)createEmptyMessage(nativePtr);
        }
    }

    private native ChatMessage createFileTransferMessage(long nativePtr, Content initialContent);
    @Override
    synchronized public ChatMessage createFileTransferMessage(Content initialContent)  {
        synchronized(core) { 
        return (ChatMessage)createFileTransferMessage(nativePtr, initialContent);
        }
    }

    private native ChatMessage createMessage(long nativePtr, String message);
    @Override
    synchronized public ChatMessage createMessage(String message)  {
        synchronized(core) { 
        return (ChatMessage)createMessage(nativePtr, message);
        }
    }

    private native void deleteHistory(long nativePtr);
    @Override
    synchronized public void deleteHistory()  {
        synchronized(core) { 
        deleteHistory(nativePtr);
        }
    }

    private native void deleteMessage(long nativePtr, ChatMessage msg);
    @Override
    synchronized public void deleteMessage(ChatMessage msg)  {
        synchronized(core) { 
        deleteMessage(nativePtr, msg);
        }
    }

    private native ChatMessage findMessage(long nativePtr, String messageId);
    @Override
    synchronized public ChatMessage findMessage(String messageId)  {
        synchronized(core) { 
        return (ChatMessage)findMessage(nativePtr, messageId);
        }
    }

    private native Participant findParticipant(long nativePtr, Address addr);
    @Override
    synchronized public Participant findParticipant(Address addr)  {
        synchronized(core) { 
        return (Participant)findParticipant(nativePtr, addr);
        }
    }

    private native ChatMessage[] getHistory(long nativePtr, int nbMessage);
    @Override
    synchronized public ChatMessage[] getHistory(int nbMessage)  {
        synchronized(core) { 
        return getHistory(nativePtr, nbMessage);
        }
    }

    private native EventLog[] getHistoryEvents(long nativePtr, int nbEvents);
    @Override
    synchronized public EventLog[] getHistoryEvents(int nbEvents)  {
        synchronized(core) { 
        return getHistoryEvents(nativePtr, nbEvents);
        }
    }

    private native EventLog[] getHistoryMessageEvents(long nativePtr, int nbEvents);
    @Override
    synchronized public EventLog[] getHistoryMessageEvents(int nbEvents)  {
        synchronized(core) { 
        return getHistoryMessageEvents(nativePtr, nbEvents);
        }
    }

    private native ChatMessage[] getHistoryRange(long nativePtr, int begin, int end);
    @Override
    synchronized public ChatMessage[] getHistoryRange(int begin, int end)  {
        synchronized(core) { 
        return getHistoryRange(nativePtr, begin, end);
        }
    }

    private native EventLog[] getHistoryRangeEvents(long nativePtr, int begin, int end);
    @Override
    synchronized public EventLog[] getHistoryRangeEvents(int begin, int end)  {
        synchronized(core) { 
        return getHistoryRangeEvents(nativePtr, begin, end);
        }
    }

    private native EventLog[] getHistoryRangeMessageEvents(long nativePtr, int begin, int end);
    @Override
    synchronized public EventLog[] getHistoryRangeMessageEvents(int begin, int end)  {
        synchronized(core) { 
        return getHistoryRangeMessageEvents(nativePtr, begin, end);
        }
    }

    private native boolean hasBeenLeft(long nativePtr);
    @Override
    synchronized public boolean hasBeenLeft()  {
        synchronized(core) { 
        return hasBeenLeft(nativePtr);
        }
    }

    private native boolean hasCapability(long nativePtr, int mask);
    @Override
    synchronized public boolean hasCapability(int mask)  {
        synchronized(core) { 
        return hasCapability(nativePtr, mask);
        }
    }

    private native void leave(long nativePtr);
    @Override
    synchronized public void leave()  {
        synchronized(core) { 
        leave(nativePtr);
        }
    }

    private native boolean limeAvailable(long nativePtr);
    @Override
    synchronized public boolean limeAvailable()  {
        synchronized(core) { 
        return limeAvailable(nativePtr);
        }
    }

    private native void markAsRead(long nativePtr);
    @Override
    synchronized public void markAsRead()  {
        synchronized(core) { 
        markAsRead(nativePtr);
        }
    }

    private native void receiveChatMessage(long nativePtr, ChatMessage msg);
    @Override
    synchronized public void receiveChatMessage(ChatMessage msg)  {
        synchronized(core) { 
        receiveChatMessage(nativePtr, msg);
        }
    }

    private native void removeParticipant(long nativePtr, Participant participant);
    @Override
    synchronized public void removeParticipant(Participant participant)  {
        synchronized(core) { 
        removeParticipant(nativePtr, participant);
        }
    }

    private native void removeParticipants(long nativePtr, Participant[] participants);
    @Override
    synchronized public void removeParticipants(Participant[] participants)  {
        synchronized(core) { 
        removeParticipants(nativePtr, participants);
        }
    }

    private native void sendChatMessage2(long nativePtr, ChatMessage msg);
    @Override
    synchronized public void sendChatMessage(ChatMessage msg)  {
        synchronized(core) { 
        sendChatMessage2(nativePtr, msg);
        }
    }

    private native void setParticipantAdminStatus(long nativePtr, Participant participant, boolean isAdmin);
    @Override
    synchronized public void setParticipantAdminStatus(Participant participant, boolean isAdmin)  {
        synchronized(core) { 
        setParticipantAdminStatus(nativePtr, participant, isAdmin);
        }
    }

    private native void setParticipantDevices(long nativePtr, Address partAddr, Address[] partDevices);
    @Override
    synchronized public void setParticipantDevices(Address partAddr, Address[] partDevices)  {
        synchronized(core) { 
        setParticipantDevices(nativePtr, partAddr, partDevices);
        }
    }

    private native void addListener(long nativePtr, ChatRoomListener listener);
    @Override
    synchronized public void addListener(ChatRoomListener listener)  {
        
        addListener(nativePtr, listener);
    }

    private native void removeListener(long nativePtr, ChatRoomListener listener);
    @Override
    synchronized public void removeListener(ChatRoomListener listener)  {
        
        removeListener(nativePtr, listener);
    }

    private native void unref(long ptr);
    protected void finalize() throws Throwable {
		if (nativePtr != 0) {
			unref(nativePtr);
			nativePtr = 0;
		}
		super.finalize();
	}

    @Override
    public void setUserData(Object data) {
        userData = data;
    }

    @Override
    public Object getUserData() {
        return userData;
    }
}
