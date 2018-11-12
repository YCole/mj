/*
ConferenceParams.java
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
  * Parameters for initialization of conferences The _LinphoneConferenceParams
  * struct does not exists, it's the ConferenceParams C++ class that is used
  * behind. 
  */
public interface ConferenceParams {
    /**
      * Enable video when starting a conference. 
      */
    public void enableVideo(boolean enable);

    /**
      * Clone a #LinphoneConferenceParams. 
      */
    public ConferenceParams clone();

    /**
      * Check whether video will be enable at conference starting. 
      */
    public boolean videoRequested();

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class ConferenceParamsImpl implements ConferenceParams {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected ConferenceParamsImpl(long ptr) {
        nativePtr = ptr;
    }


    private native void enableVideo(long nativePtr, boolean enable);
    @Override
    synchronized public void enableVideo(boolean enable)  {
        
        enableVideo(nativePtr, enable);
    }

    private native ConferenceParams clone(long nativePtr);
    @Override
    synchronized public ConferenceParams clone()  {
        
        return (ConferenceParams)clone(nativePtr);
    }

    private native boolean videoRequested(long nativePtr);
    @Override
    synchronized public boolean videoRequested()  {
        
        return videoRequested(nativePtr);
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
