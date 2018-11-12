/*
ChatMessage.java
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
  * An chat message is the object that is sent and received through
  * LinphoneChatRooms. 
  */
public interface ChatMessage {
    enum State {
        /**
        * Initial state. 
        */
        Idle(0),

        /**
        * Delivery in progress. 
        */
        InProgress(1),

        /**
        * Message successfully delivered and acknowledged by the server. 
        */
        Delivered(2),

        /**
        * Message was not delivered. 
        */
        NotDelivered(3),

        /**
        * Message was received and acknowledged but cannot get file from server. 
        */
        FileTransferError(4),

        /**
        * File transfer has been completed successfully. 
        */
        FileTransferDone(5),

        /**
        * Message successfully delivered an acknowledged by the remote user. 
        */
        DeliveredToUser(6),

        /**
        * Message successfully displayed to the remote user. 
        */
        Displayed(7);

        protected final int mValue;

        private State (int value) {
            mValue = value;
        }

        static public State fromInt(int value) throws RuntimeException {
            switch(value) {
            case 0: return Idle;
            case 1: return InProgress;
            case 2: return Delivered;
            case 3: return NotDelivered;
            case 4: return FileTransferError;
            case 5: return FileTransferDone;
            case 6: return DeliveredToUser;
            case 7: return Displayed;
            default:
                throw new RuntimeException("Unhandled enum value " + value + " for State");
            }
        }

        public int toInt() {
            return mValue;
        }
    };

    enum Direction {
        /**
        * Incoming message. 
        */
        Incoming(0),

        /**
        * Outgoing message. 
        */
        Outgoing(1);

        protected final int mValue;

        private Direction (int value) {
            mValue = value;
        }

        static public Direction fromInt(int value) throws RuntimeException {
            switch(value) {
            case 0: return Incoming;
            case 1: return Outgoing;
            default:
                throw new RuntimeException("Unhandled enum value " + value + " for Direction");
            }
        }

        public int toInt() {
            return mValue;
        }
    };

    /**
      * Linphone message has an app-specific field that can store a text. 
      */
    public String getAppdata();

    /**
      * Linphone message has an app-specific field that can store a text. 
      */
    public void setAppdata(String data);

    /**
      * Returns the chatroom this message belongs to. 
      */
    public ChatRoom getChatRoom();

    /**
      * Get the content type of a chat message. 
      */
    public String getContentType();

    /**
      * Set the content type of a chat message. 
      */
    public void setContentType(String contentType);

    /**
      * Returns the list of contents in the message. 
      */
    public Content[] getContents();

    /**
      * Get full details about delivery error of a chat message. 
      */
    public ErrorInfo getErrorInfo();

    /**
      * Linphone message can carry external body as defined by rfc2017. 
      */
    public String getExternalBodyUrl();

    /**
      * Linphone message can carry external body as defined by rfc2017. 
      */
    public void setExternalBodyUrl(String url);

    /**
      * Get the path to the file to read from or write to during the file transfer. 
      */
    @Deprecated
    public String getFileTransferFilepath();

    /**
      * Set the path to the file to read from or write to during the file transfer. 
      */
    @Deprecated
    public void setFileTransferFilepath(String filepath);

    /**
      * Get the file_transfer_information (used by call backs to recover informations
      * during a rcs file transfer) 
      */
    public Content getFileTransferInformation();

    /**
      * Get origin of the message. 
      */
    public Address getFromAddress();

    /**
      * Return whether or not a chat message is a file transfer. 
      */
    public boolean isFileTransfer();

    /**
      * Gets whether or not a file is currently being downloaded or uploaded. 
      */
    public boolean isFileTransferInProgress();

    /**
      * Returns TRUE if the message has been sent, returns TRUE if the message has been
      * received. 
      */
    public boolean isOutgoing();

    /**
      * Returns TRUE if the message has been read, otherwise returns TRUE. 
      */
    public boolean isRead();

    /**
      * Get if the message was encrypted when transfered. 
      */
    public boolean isSecured();

    /**
      * Return whether or not a chat message is a text. 
      */
    public boolean isText();

    /**
      * Returns the origin address of a message if it was a outgoing message, or the
      * destination address if it was an incoming message. 
      */
    public Address getLocalAddress();

    /**
      * Get the message identifier. 
      */
    public String getMessageId();

    /**
      * Get the state of the message. 
      */
    public ChatMessage.State getState();

    /**
      * Get text part of this message. 
      */
    @Deprecated
    public String getText();

    /**
      * Gets the text content if available as a string. 
      */
    public String getTextContent();

    /**
      * Get the time the message was sent. 
      */
    public long getTime();

    /**
      * Get destination of the message. 
      */
    public Address getToAddress();

    /**
      * Get if a chat message is to be stored. 
      */
    public boolean getToBeStored();

    /**
      * Set if a chat message is to be stored. 
      */
    public void setToBeStored(boolean toBeStored);

    /**
      * Add custom headers to the message. 
      */
    public void addCustomHeader(String headerName, String headerValue);

    /**
      * Adds a file content to the ChatMessage. 
      */
    public void addFileContent(Content cContent);

    /**
      * Adds a text content to the ChatMessage. 
      */
    public void addTextContent(String text);

    /**
      * Cancel an ongoing file transfer attached to this message. 
      */
    public void cancelFileTransfer();

    /**
      * Start the download of the #LinphoneContent referenced in the
      * #LinphoneChatMessage from remote server. 
      */
    public boolean downloadContent(Content cContent);

    /**
      * Start the download of the file referenced in a #LinphoneChatMessage from remote
      * server. 
      */
    @Deprecated
    public boolean downloadFile();

    /**
      * Retrieve a custom header value given its name. 
      */
    public String getCustomHeader(String headerName);

    /**
      * Gets the list of participants for which the imdn state has reached the
      * specified state and the time at which they did. 
      */
    public ParticipantImdnState[] getParticipantsByImdnState(ChatMessage.State state);

    /**
      * Returns true if the chat message has a text content. 
      */
    public boolean hasTextContent();

    /**
      * Fulfill a chat message char by char. 
      */
    public void putChar(int character);

    /**
      * Removes a content from the ChatMessage. 
      */
    public void removeContent(Content content);

    /**
      * Removes a custom header from the message. 
      */
    public void removeCustomHeader(String headerName);

    /**
      * Resend a chat message if it is in the 'not delivered' state for whatever
      * reason. 
      */
    public void resend();

    /**
      * Send a chat message. 
      */
    public void send();

    public void setListener(ChatMessageListener listener);

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class ChatMessageImpl implements ChatMessage {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected ChatMessageImpl(long ptr) {
        nativePtr = ptr;
    }


    private native String getAppdata(long nativePtr);
    @Override
    synchronized public String getAppdata()  {
        
        return getAppdata(nativePtr);
    }

    private native void setAppdata(long nativePtr, String data);
    @Override
    synchronized public void setAppdata(String data)  {
        
        setAppdata(nativePtr, data);
    }

    private native ChatRoom getChatRoom(long nativePtr);
    @Override
    synchronized public ChatRoom getChatRoom()  {
        
        return (ChatRoom)getChatRoom(nativePtr);
    }

    private native String getContentType(long nativePtr);
    @Override
    synchronized public String getContentType()  {
        
        return getContentType(nativePtr);
    }

    private native void setContentType(long nativePtr, String contentType);
    @Override
    synchronized public void setContentType(String contentType)  {
        
        setContentType(nativePtr, contentType);
    }

    private native Content[] getContents(long nativePtr);
    @Override
    synchronized public Content[] getContents()  {
        
        return getContents(nativePtr);
    }

    private native ErrorInfo getErrorInfo(long nativePtr);
    @Override
    synchronized public ErrorInfo getErrorInfo()  {
        
        return (ErrorInfo)getErrorInfo(nativePtr);
    }

    private native String getExternalBodyUrl(long nativePtr);
    @Override
    synchronized public String getExternalBodyUrl()  {
        
        return getExternalBodyUrl(nativePtr);
    }

    private native void setExternalBodyUrl(long nativePtr, String url);
    @Override
    synchronized public void setExternalBodyUrl(String url)  {
        
        setExternalBodyUrl(nativePtr, url);
    }

    private native String getFileTransferFilepath(long nativePtr);
    @Override
    synchronized public String getFileTransferFilepath()  {
        
        return getFileTransferFilepath(nativePtr);
    }

    private native void setFileTransferFilepath(long nativePtr, String filepath);
    @Override
    synchronized public void setFileTransferFilepath(String filepath)  {
        
        setFileTransferFilepath(nativePtr, filepath);
    }

    private native Content getFileTransferInformation(long nativePtr);
    @Override
    synchronized public Content getFileTransferInformation()  {
        
        return (Content)getFileTransferInformation(nativePtr);
    }

    private native Address getFromAddress(long nativePtr);
    @Override
    synchronized public Address getFromAddress()  {
        
        return (Address)getFromAddress(nativePtr);
    }

    private native boolean isFileTransfer(long nativePtr);
    @Override
    synchronized public boolean isFileTransfer()  {
        
        return isFileTransfer(nativePtr);
    }

    private native boolean isFileTransferInProgress(long nativePtr);
    @Override
    synchronized public boolean isFileTransferInProgress()  {
        
        return isFileTransferInProgress(nativePtr);
    }

    private native boolean isOutgoing(long nativePtr);
    @Override
    synchronized public boolean isOutgoing()  {
        
        return isOutgoing(nativePtr);
    }

    private native boolean isRead(long nativePtr);
    @Override
    synchronized public boolean isRead()  {
        
        return isRead(nativePtr);
    }

    private native boolean isSecured(long nativePtr);
    @Override
    synchronized public boolean isSecured()  {
        
        return isSecured(nativePtr);
    }

    private native boolean isText(long nativePtr);
    @Override
    synchronized public boolean isText()  {
        
        return isText(nativePtr);
    }

    private native Address getLocalAddress(long nativePtr);
    @Override
    synchronized public Address getLocalAddress()  {
        
        return (Address)getLocalAddress(nativePtr);
    }

    private native String getMessageId(long nativePtr);
    @Override
    synchronized public String getMessageId()  {
        
        return getMessageId(nativePtr);
    }

    private native int getState(long nativePtr);
    @Override
    synchronized public ChatMessage.State getState()  {
        
        return ChatMessage.State.fromInt(getState(nativePtr));
    }

    private native String getText(long nativePtr);
    @Override
    synchronized public String getText()  {
        
        return getText(nativePtr);
    }

    private native String getTextContent(long nativePtr);
    @Override
    synchronized public String getTextContent()  {
        
        return getTextContent(nativePtr);
    }

    private native long getTime(long nativePtr);
    @Override
    synchronized public long getTime()  {
        
        return getTime(nativePtr);
    }

    private native Address getToAddress(long nativePtr);
    @Override
    synchronized public Address getToAddress()  {
        
        return (Address)getToAddress(nativePtr);
    }

    private native boolean getToBeStored(long nativePtr);
    @Override
    synchronized public boolean getToBeStored()  {
        
        return getToBeStored(nativePtr);
    }

    private native void setToBeStored(long nativePtr, boolean toBeStored);
    @Override
    synchronized public void setToBeStored(boolean toBeStored)  {
        
        setToBeStored(nativePtr, toBeStored);
    }

    private native void addCustomHeader(long nativePtr, String headerName, String headerValue);
    @Override
    synchronized public void addCustomHeader(String headerName, String headerValue)  {
        
        addCustomHeader(nativePtr, headerName, headerValue);
    }

    private native void addFileContent(long nativePtr, Content cContent);
    @Override
    synchronized public void addFileContent(Content cContent)  {
        
        addFileContent(nativePtr, cContent);
    }

    private native void addTextContent(long nativePtr, String text);
    @Override
    synchronized public void addTextContent(String text)  {
        
        addTextContent(nativePtr, text);
    }

    private native void cancelFileTransfer(long nativePtr);
    @Override
    synchronized public void cancelFileTransfer()  {
        
        cancelFileTransfer(nativePtr);
    }

    private native boolean downloadContent(long nativePtr, Content cContent);
    @Override
    synchronized public boolean downloadContent(Content cContent)  {
        
        return downloadContent(nativePtr, cContent);
    }

    private native boolean downloadFile(long nativePtr);
    @Override
    synchronized public boolean downloadFile()  {
        
        return downloadFile(nativePtr);
    }

    private native String getCustomHeader(long nativePtr, String headerName);
    @Override
    synchronized public String getCustomHeader(String headerName)  {
        
        return getCustomHeader(nativePtr, headerName);
    }

    private native ParticipantImdnState[] getParticipantsByImdnState(long nativePtr, int state);
    @Override
    synchronized public ParticipantImdnState[] getParticipantsByImdnState(ChatMessage.State state)  {
        
        return getParticipantsByImdnState(nativePtr, state.toInt());
    }

    private native boolean hasTextContent(long nativePtr);
    @Override
    synchronized public boolean hasTextContent()  {
        
        return hasTextContent(nativePtr);
    }

    private native int putChar(long nativePtr, int character);
    @Override
    synchronized public void putChar(int character)  {
        
        putChar(nativePtr, character);
    }

    private native void removeContent(long nativePtr, Content content);
    @Override
    synchronized public void removeContent(Content content)  {
        
        removeContent(nativePtr, content);
    }

    private native void removeCustomHeader(long nativePtr, String headerName);
    @Override
    synchronized public void removeCustomHeader(String headerName)  {
        
        removeCustomHeader(nativePtr, headerName);
    }

    private native void resend2(long nativePtr);
    @Override
    synchronized public void resend()  {
        
        resend2(nativePtr);
    }

    private native void send(long nativePtr);
    @Override
    synchronized public void send()  {
        
        send(nativePtr);
    }

    private native void setListener(long nativePtr, ChatMessageListener listener);
    @Override
    synchronized public void setListener(ChatMessageListener listener)  {
        
        setListener(nativePtr, listener);
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
