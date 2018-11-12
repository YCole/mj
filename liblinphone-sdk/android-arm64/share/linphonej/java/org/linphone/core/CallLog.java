/*
CallLog.java
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
  * Structure representing a call log. 
  */
public interface CallLog {
    /**
      * Get the call ID used by the call. 
      */
    public String getCallId();

    /**
      * Get the direction of the call. 
      */
    public Call.Dir getDir();

    /**
      * Get the duration of the call since connected. 
      */
    public int getDuration();

    /**
      * When the call was failed, return an object describing the failure. 
      */
    public ErrorInfo getErrorInfo();

    /**
      * Get the origin address (ie from) of the call. 
      */
    public Address getFromAddress();

    /**
      * Get the local address (that is from or to depending on call direction) 
      */
    public Address getLocalAddress();

    /**
      * Get the overall quality indication of the call. 
      */
    public float getQuality();

    /**
      * Get the persistent reference key associated to the call log. 
      */
    public String getRefKey();

    /**
      * Associate a persistent reference key to the call log. 
      */
    public void setRefKey(String refkey);

    /**
      * Get the remote address (that is from or to depending on call direction). 
      */
    public Address getRemoteAddress();

    /**
      * Get the start date of the call. 
      */
    public long getStartDate();

    /**
      * Get the status of the call. 
      */
    public Call.Status getStatus();

    /**
      * Get the destination address (ie to) of the call. 
      */
    public Address getToAddress();

    /**
      * Tell whether video was enabled at the end of the call or not. 
      */
    public boolean videoEnabled();

    /**
      * Get a human readable string describing the call. 
      */
    public String toStr();

    /**
      * Tells whether that call was a call to a conference server. 
      */
    public boolean wasConference();

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class CallLogImpl implements CallLog {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected CallLogImpl(long ptr) {
        nativePtr = ptr;
    }


    private native String getCallId(long nativePtr);
    @Override
    synchronized public String getCallId()  {
        
        return getCallId(nativePtr);
    }

    private native int getDir(long nativePtr);
    @Override
    synchronized public Call.Dir getDir()  {
        
        return Call.Dir.fromInt(getDir(nativePtr));
    }

    private native int getDuration(long nativePtr);
    @Override
    synchronized public int getDuration()  {
        
        return getDuration(nativePtr);
    }

    private native ErrorInfo getErrorInfo(long nativePtr);
    @Override
    synchronized public ErrorInfo getErrorInfo()  {
        
        return (ErrorInfo)getErrorInfo(nativePtr);
    }

    private native Address getFromAddress(long nativePtr);
    @Override
    synchronized public Address getFromAddress()  {
        
        return (Address)getFromAddress(nativePtr);
    }

    private native Address getLocalAddress(long nativePtr);
    @Override
    synchronized public Address getLocalAddress()  {
        
        return (Address)getLocalAddress(nativePtr);
    }

    private native float getQuality(long nativePtr);
    @Override
    synchronized public float getQuality()  {
        
        return getQuality(nativePtr);
    }

    private native String getRefKey(long nativePtr);
    @Override
    synchronized public String getRefKey()  {
        
        return getRefKey(nativePtr);
    }

    private native void setRefKey(long nativePtr, String refkey);
    @Override
    synchronized public void setRefKey(String refkey)  {
        
        setRefKey(nativePtr, refkey);
    }

    private native Address getRemoteAddress(long nativePtr);
    @Override
    synchronized public Address getRemoteAddress()  {
        
        return (Address)getRemoteAddress(nativePtr);
    }

    private native long getStartDate(long nativePtr);
    @Override
    synchronized public long getStartDate()  {
        
        return getStartDate(nativePtr);
    }

    private native int getStatus(long nativePtr);
    @Override
    synchronized public Call.Status getStatus()  {
        
        return Call.Status.fromInt(getStatus(nativePtr));
    }

    private native Address getToAddress(long nativePtr);
    @Override
    synchronized public Address getToAddress()  {
        
        return (Address)getToAddress(nativePtr);
    }

    private native boolean videoEnabled(long nativePtr);
    @Override
    synchronized public boolean videoEnabled()  {
        
        return videoEnabled(nativePtr);
    }

    private native String toStr(long nativePtr);
    @Override
    synchronized public String toStr()  {
        
        return toStr(nativePtr);
    }

    private native boolean wasConference(long nativePtr);
    @Override
    synchronized public boolean wasConference()  {
        
        return wasConference(nativePtr);
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
