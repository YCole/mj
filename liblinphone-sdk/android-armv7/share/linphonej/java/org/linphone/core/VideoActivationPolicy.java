/*
VideoActivationPolicy.java
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
  * Structure describing policy regarding video streams establishments. 
  */
public interface VideoActivationPolicy {
    /**
      * Gets the value for the automatically accept video policy. 
      */
    public boolean getAutomaticallyAccept();

    /**
      * Sets the value for the automatically accept video policy. 
      */
    public void setAutomaticallyAccept(boolean enable);

    /**
      * Gets the value for the automatically initiate video policy. 
      */
    public boolean getAutomaticallyInitiate();

    /**
      * Sets the value for the automatically initiate video policy. 
      */
    public void setAutomaticallyInitiate(boolean enable);

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class VideoActivationPolicyImpl implements VideoActivationPolicy {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected VideoActivationPolicyImpl(long ptr) {
        nativePtr = ptr;
    }


    private native boolean getAutomaticallyAccept(long nativePtr);
    @Override
    synchronized public boolean getAutomaticallyAccept()  {
        
        return getAutomaticallyAccept(nativePtr);
    }

    private native void setAutomaticallyAccept(long nativePtr, boolean enable);
    @Override
    synchronized public void setAutomaticallyAccept(boolean enable)  {
        
        setAutomaticallyAccept(nativePtr, enable);
    }

    private native boolean getAutomaticallyInitiate(long nativePtr);
    @Override
    synchronized public boolean getAutomaticallyInitiate()  {
        
        return getAutomaticallyInitiate(nativePtr);
    }

    private native void setAutomaticallyInitiate(long nativePtr, boolean enable);
    @Override
    synchronized public void setAutomaticallyInitiate(boolean enable)  {
        
        setAutomaticallyInitiate(nativePtr, enable);
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
