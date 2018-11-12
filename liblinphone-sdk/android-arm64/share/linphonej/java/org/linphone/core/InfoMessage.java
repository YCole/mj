/*
InfoMessage.java
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
  * The #LinphoneInfoMessage is an object representing an informational message
  * sent or received by the core. 
  */
public interface InfoMessage {
    /**
      * Returns the info message's content as a #LinphoneContent structure. 
      */
    public Content getContent();

    /**
      * Assign a content to the info message. 
      */
    public void setContent(Content content);

    /**
      * Add a header to an info message to be sent. 
      */
    public void addHeader(String name, String value);

    /**
      * Obtain a header value from a received info message. 
      */
    public String getHeader(String name);

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class InfoMessageImpl implements InfoMessage {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected InfoMessageImpl(long ptr) {
        nativePtr = ptr;
    }


    private native Content getContent(long nativePtr);
    @Override
    synchronized public Content getContent()  {
        
        return (Content)getContent(nativePtr);
    }

    private native void setContent(long nativePtr, Content content);
    @Override
    synchronized public void setContent(Content content)  {
        
        setContent(nativePtr, content);
    }

    private native void addHeader(long nativePtr, String name, String value);
    @Override
    synchronized public void addHeader(String name, String value)  {
        
        addHeader(nativePtr, name, value);
    }

    private native String getHeader(long nativePtr, String name);
    @Override
    synchronized public String getHeader(String name)  {
        
        return getHeader(nativePtr, name);
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
