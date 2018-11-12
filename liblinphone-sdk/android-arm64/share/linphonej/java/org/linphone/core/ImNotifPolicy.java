/*
ImNotifPolicy.java
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
  * Policy to use to send/receive instant messaging composing/delivery/display
  * notifications. 
  */
public interface ImNotifPolicy {
    /**
      * Tell whether imdn delivered notifications are being notified when received. 
      */
    public boolean getRecvImdnDelivered();

    /**
      * Enable imdn delivered notifications receiving. 
      */
    public void setRecvImdnDelivered(boolean enable);

    /**
      * Tell whether imdn displayed notifications are being notified when received. 
      */
    public boolean getRecvImdnDisplayed();

    /**
      * Enable imdn displayed notifications receiving. 
      */
    public void setRecvImdnDisplayed(boolean enable);

    /**
      * Tell whether is_composing notifications are being notified when received. 
      */
    public boolean getRecvIsComposing();

    /**
      * Enable is_composing notifications receiving. 
      */
    public void setRecvIsComposing(boolean enable);

    /**
      * Tell whether imdn delivered notifications are being sent. 
      */
    public boolean getSendImdnDelivered();

    /**
      * Enable imdn delivered notifications sending. 
      */
    public void setSendImdnDelivered(boolean enable);

    /**
      * Tell whether imdn displayed notifications are being sent. 
      */
    public boolean getSendImdnDisplayed();

    /**
      * Enable imdn displayed notifications sending. 
      */
    public void setSendImdnDisplayed(boolean enable);

    /**
      * Tell whether is_composing notifications are being sent. 
      */
    public boolean getSendIsComposing();

    /**
      * Enable is_composing notifications sending. 
      */
    public void setSendIsComposing(boolean enable);

    /**
      * Clear an IM notif policy (deactivate all receiving and sending of
      * notifications). 
      */
    public void clear();

    /**
      * Enable all receiving and sending of notifications. 
      */
    public void enableAll();

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class ImNotifPolicyImpl implements ImNotifPolicy {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected ImNotifPolicyImpl(long ptr) {
        nativePtr = ptr;
    }


    private native boolean getRecvImdnDelivered(long nativePtr);
    @Override
    synchronized public boolean getRecvImdnDelivered()  {
        
        return getRecvImdnDelivered(nativePtr);
    }

    private native void setRecvImdnDelivered(long nativePtr, boolean enable);
    @Override
    synchronized public void setRecvImdnDelivered(boolean enable)  {
        
        setRecvImdnDelivered(nativePtr, enable);
    }

    private native boolean getRecvImdnDisplayed(long nativePtr);
    @Override
    synchronized public boolean getRecvImdnDisplayed()  {
        
        return getRecvImdnDisplayed(nativePtr);
    }

    private native void setRecvImdnDisplayed(long nativePtr, boolean enable);
    @Override
    synchronized public void setRecvImdnDisplayed(boolean enable)  {
        
        setRecvImdnDisplayed(nativePtr, enable);
    }

    private native boolean getRecvIsComposing(long nativePtr);
    @Override
    synchronized public boolean getRecvIsComposing()  {
        
        return getRecvIsComposing(nativePtr);
    }

    private native void setRecvIsComposing(long nativePtr, boolean enable);
    @Override
    synchronized public void setRecvIsComposing(boolean enable)  {
        
        setRecvIsComposing(nativePtr, enable);
    }

    private native boolean getSendImdnDelivered(long nativePtr);
    @Override
    synchronized public boolean getSendImdnDelivered()  {
        
        return getSendImdnDelivered(nativePtr);
    }

    private native void setSendImdnDelivered(long nativePtr, boolean enable);
    @Override
    synchronized public void setSendImdnDelivered(boolean enable)  {
        
        setSendImdnDelivered(nativePtr, enable);
    }

    private native boolean getSendImdnDisplayed(long nativePtr);
    @Override
    synchronized public boolean getSendImdnDisplayed()  {
        
        return getSendImdnDisplayed(nativePtr);
    }

    private native void setSendImdnDisplayed(long nativePtr, boolean enable);
    @Override
    synchronized public void setSendImdnDisplayed(boolean enable)  {
        
        setSendImdnDisplayed(nativePtr, enable);
    }

    private native boolean getSendIsComposing(long nativePtr);
    @Override
    synchronized public boolean getSendIsComposing()  {
        
        return getSendIsComposing(nativePtr);
    }

    private native void setSendIsComposing(long nativePtr, boolean enable);
    @Override
    synchronized public void setSendIsComposing(boolean enable)  {
        
        setSendIsComposing(nativePtr, enable);
    }

    private native void clear(long nativePtr);
    @Override
    synchronized public void clear()  {
        
        clear(nativePtr);
    }

    private native void enableAll(long nativePtr);
    @Override
    synchronized public void enableAll()  {
        
        enableAll(nativePtr);
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
