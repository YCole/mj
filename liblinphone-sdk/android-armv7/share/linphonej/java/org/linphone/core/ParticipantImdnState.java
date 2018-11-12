/*
ParticipantImdnState.java
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
  * The LinphoneParticipantImdnState object represents the state of chat message
  * for a participant of a conference chat room. 
  */
public interface ParticipantImdnState {
    /**
      * Get the participant concerned by a LinphoneParticipantImdnState. 
      */
    public Participant getParticipant();

    /**
      * Get the chat message state the participant is in. 
      */
    public ChatMessage.State getState();

    /**
      * Get the timestamp at which a participant has reached the state described by a
      * LinphoneParticipantImdnState. 
      */
    public long getStateChangeTime();

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class ParticipantImdnStateImpl implements ParticipantImdnState {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected ParticipantImdnStateImpl(long ptr) {
        nativePtr = ptr;
    }


    private native Participant getParticipant(long nativePtr);
    @Override
    synchronized public Participant getParticipant()  {
        
        return (Participant)getParticipant(nativePtr);
    }

    private native int getState(long nativePtr);
    @Override
    synchronized public ChatMessage.State getState()  {
        
        return ChatMessage.State.fromInt(getState(nativePtr));
    }

    private native long getStateChangeTime(long nativePtr);
    @Override
    synchronized public long getStateChangeTime()  {
        
        return getStateChangeTime(nativePtr);
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
