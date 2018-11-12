/*
EventLog.java
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
  * Base object of events. 
  */
public interface EventLog {
    enum Type {
        /**
        * No defined event. 
        */
        None(0),

        /**
        * Conference (created) event. 
        */
        ConferenceCreated(1),

        /**
        * Conference (terminated) event. 
        */
        ConferenceTerminated(2),

        /**
        * Conference call (start) event. 
        */
        ConferenceCallStart(3),

        /**
        * Conference call (end) event. 
        */
        ConferenceCallEnd(4),

        /**
        * Conference chat message event. 
        */
        ConferenceChatMessage(5),

        /**
        * Conference participant (added) event. 
        */
        ConferenceParticipantAdded(6),

        /**
        * Conference participant (removed) event. 
        */
        ConferenceParticipantRemoved(7),

        /**
        * Conference participant (set admin) event. 
        */
        ConferenceParticipantSetAdmin(8),

        /**
        * Conference participant (unset admin) event. 
        */
        ConferenceParticipantUnsetAdmin(9),

        /**
        * Conference participant device (added) event. 
        */
        ConferenceParticipantDeviceAdded(10),

        /**
        * Conference participant device (removed) event. 
        */
        ConferenceParticipantDeviceRemoved(11),

        /**
        * Conference subject event. 
        */
        ConferenceSubjectChanged(12);

        protected final int mValue;

        private Type (int value) {
            mValue = value;
        }

        static public Type fromInt(int value) throws RuntimeException {
            switch(value) {
            case 0: return None;
            case 1: return ConferenceCreated;
            case 2: return ConferenceTerminated;
            case 3: return ConferenceCallStart;
            case 4: return ConferenceCallEnd;
            case 5: return ConferenceChatMessage;
            case 6: return ConferenceParticipantAdded;
            case 7: return ConferenceParticipantRemoved;
            case 8: return ConferenceParticipantSetAdmin;
            case 9: return ConferenceParticipantUnsetAdmin;
            case 10: return ConferenceParticipantDeviceAdded;
            case 11: return ConferenceParticipantDeviceRemoved;
            case 12: return ConferenceSubjectChanged;
            default:
                throw new RuntimeException("Unhandled enum value " + value + " for Type");
            }
        }

        public int toInt() {
            return mValue;
        }
    };

    /**
      * Returns the call of a conference call event. 
      */
    public Call getCall();

    /**
      * Returns the chat message of a conference chat message event. 
      */
    public ChatMessage getChatMessage();

    /**
      * Returns the creation time of a event log. 
      */
    public long getCreationTime();

    /**
      * Returns the device address of a conference participant device event. 
      */
    public Address getDeviceAddress();

    /**
      * Returns the local address of a conference event. 
      */
    public Address getLocalAddress();

    /**
      * Returns the notify id of a conference notified event. 
      */
    public int getNotifyId();

    /**
      * Returns the participant address of a conference participant event. 
      */
    public Address getParticipantAddress();

    /**
      * Returns the peer address of a conference event. 
      */
    public Address getPeerAddress();

    /**
      * Returns the subject of a conference subject event. 
      */
    public String getSubject();

    /**
      * Returns the type of a event log. 
      */
    public EventLog.Type getType();

    /**
      * Delete event log from database. 
      */
    public void deleteFromDatabase();

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class EventLogImpl implements EventLog {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected EventLogImpl(long ptr) {
        nativePtr = ptr;
    }


    private native Call getCall(long nativePtr);
    @Override
    synchronized public Call getCall()  {
        
        return (Call)getCall(nativePtr);
    }

    private native ChatMessage getChatMessage(long nativePtr);
    @Override
    synchronized public ChatMessage getChatMessage()  {
        
        return (ChatMessage)getChatMessage(nativePtr);
    }

    private native long getCreationTime(long nativePtr);
    @Override
    synchronized public long getCreationTime()  {
        
        return getCreationTime(nativePtr);
    }

    private native Address getDeviceAddress(long nativePtr);
    @Override
    synchronized public Address getDeviceAddress()  {
        
        return (Address)getDeviceAddress(nativePtr);
    }

    private native Address getLocalAddress(long nativePtr);
    @Override
    synchronized public Address getLocalAddress()  {
        
        return (Address)getLocalAddress(nativePtr);
    }

    private native int getNotifyId(long nativePtr);
    @Override
    synchronized public int getNotifyId()  {
        
        return getNotifyId(nativePtr);
    }

    private native Address getParticipantAddress(long nativePtr);
    @Override
    synchronized public Address getParticipantAddress()  {
        
        return (Address)getParticipantAddress(nativePtr);
    }

    private native Address getPeerAddress(long nativePtr);
    @Override
    synchronized public Address getPeerAddress()  {
        
        return (Address)getPeerAddress(nativePtr);
    }

    private native String getSubject(long nativePtr);
    @Override
    synchronized public String getSubject()  {
        
        return getSubject(nativePtr);
    }

    private native int getType(long nativePtr);
    @Override
    synchronized public EventLog.Type getType()  {
        
        return EventLog.Type.fromInt(getType(nativePtr));
    }

    private native void deleteFromDatabase(long nativePtr);
    @Override
    synchronized public void deleteFromDatabase()  {
        
        deleteFromDatabase(nativePtr);
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
