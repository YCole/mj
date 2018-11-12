/*
Buffer.java
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
  * The #LinphoneContent object representing a data buffer. 
  */
public interface Buffer {
    /**
      * Get the content of the data buffer. 
      */
    public byte[] getContent();

    /**
      * Tell whether the #LinphoneBuffer is empty. 
      */
    public boolean isEmpty();

    /**
      * Get the size of the content of the data buffer. 
      */
    public int getSize();

    /**
      * Set the size of the content of the data buffer. 
      */
    public void setSize(int size);

    /**
      * Get the string content of the data buffer. 
      */
    public String getStringContent();

    /**
      * Set the string content of the data buffer. 
      */
    public void setStringContent(String content);

    /**
      * Set the content of the data buffer. 
      */
    public void setContent(byte[] content, int size);

    /**
      * Create a new #LinphoneBuffer object from existing data. 
      */
    public Buffer newFromData(byte[] data, int size);

    /**
      * Create a new #LinphoneBuffer object from a string. 
      */
    public Buffer newFromString(String data);

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class BufferImpl implements Buffer {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected BufferImpl(long ptr) {
        nativePtr = ptr;
    }


    private native byte[] getContent(long nativePtr);
    @Override
    synchronized public byte[] getContent()  {
        
        return getContent(nativePtr);
    }

    private native boolean isEmpty(long nativePtr);
    @Override
    synchronized public boolean isEmpty()  {
        
        return isEmpty(nativePtr);
    }

    private native int getSize(long nativePtr);
    @Override
    synchronized public int getSize()  {
        
        return getSize(nativePtr);
    }

    private native void setSize(long nativePtr, int size);
    @Override
    synchronized public void setSize(int size)  {
        
        setSize(nativePtr, size);
    }

    private native String getStringContent(long nativePtr);
    @Override
    synchronized public String getStringContent()  {
        
        return getStringContent(nativePtr);
    }

    private native void setStringContent(long nativePtr, String content);
    @Override
    synchronized public void setStringContent(String content)  {
        
        setStringContent(nativePtr, content);
    }

    private native void setContent(long nativePtr, byte[] content, int size);
    @Override
    synchronized public void setContent(byte[] content, int size)  {
        
        setContent(nativePtr, content, size);
    }

    private native Buffer newFromData(long nativePtr, byte[] data, int size);
    @Override
    synchronized public Buffer newFromData(byte[] data, int size)  {
        
        return (Buffer)newFromData(nativePtr, data, size);
    }

    private native Buffer newFromString(long nativePtr, String data);
    @Override
    synchronized public Buffer newFromString(String data)  {
        
        return (Buffer)newFromString(nativePtr, data);
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
