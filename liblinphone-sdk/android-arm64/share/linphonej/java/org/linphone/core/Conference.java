/*
Conference.java
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
  * #LinphoneConference class The _LinphoneConference struct does not exists, it's
  * the Conference C++ class that is used behind 
  */
public interface Conference {
    /**
      * Get the conference id as string. 
      */
    public String getId();

    /**
      * Set the conference id as string. 
      */
    public void setId(String conferenceID);

    /**
      * Get URIs of all participants of one conference The returned bctbx_list_t
      * contains URIs of all participant. 
      */
    public Address[] getParticipants();

    /**
      * Invite participants to the conference, by supplying a list of #LinphoneAddress. 
      */
    public void inviteParticipants(Address[] addresses, CallParams params);

    /**
      * Remove a participant from a conference. 
      */
    public void removeParticipant(Address uri);

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class ConferenceImpl implements Conference {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected ConferenceImpl(long ptr) {
        nativePtr = ptr;
    }


    private native String getId(long nativePtr);
    @Override
    synchronized public String getId()  {
        
        return getId(nativePtr);
    }

    private native void setId(long nativePtr, String conferenceID);
    @Override
    synchronized public void setId(String conferenceID)  {
        
        setId(nativePtr, conferenceID);
    }

    private native Address[] getParticipants(long nativePtr);
    @Override
    synchronized public Address[] getParticipants()  {
        
        return getParticipants(nativePtr);
    }

    private native int inviteParticipants(long nativePtr, Address[] addresses, CallParams params);
    @Override
    synchronized public void inviteParticipants(Address[] addresses, CallParams params)  {
        
        inviteParticipants(nativePtr, addresses, params);
    }

    private native int removeParticipant(long nativePtr, Address uri);
    @Override
    synchronized public void removeParticipant(Address uri)  {
        
        removeParticipant(nativePtr, uri);
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
