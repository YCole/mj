/*
PayloadType.java
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
  * Object representing an RTP payload type. 
  */
public interface PayloadType {
    /**
      * Get the number of channels. 
      */
    public int getChannels();

    /**
      * Get the clock rate of a payload type. 
      */
    public int getClockRate();

    /**
      * Return a string describing a payload type. 
      */
    public String getDescription();

    /**
      * Get a description of the encoder used to provide a payload type. 
      */
    public String getEncoderDescription();

    /**
      * Check whether the payload is usable according the bandwidth targets set in the
      * core. 
      */
    public boolean isUsable();

    /**
      * Tells whether the specified payload type represents a variable bitrate codec. 
      */
    public boolean isVbr();

    /**
      * Get the mime type. 
      */
    public String getMimeType();

    /**
      * Get the normal bitrate in bits/s. 
      */
    public int getNormalBitrate();

    /**
      * Change the normal bitrate of a payload type. 
      */
    public void setNormalBitrate(int bitrate);

    /**
      * Returns the payload type number assigned for this codec. 
      */
    public int getNumber();

    /**
      * Force a number for a payload type. 
      */
    public void setNumber(int number);

    /**
      * Get the format parameters for incoming streams. 
      */
    public String getRecvFmtp();

    /**
      * Set the format parameters for incoming streams. 
      */
    public void setRecvFmtp(String recvFmtp);

    /**
      * Get the format parameters for outgoing streams. 
      */
    public String getSendFmtp();

    /**
      * Set the format parameters for outgoing streams. 
      */
    public void setSendFmtp(String sendFmtp);

    /**
      * Get the type of a payload type. 
      */
    public int getType();

    /**
      * Enable/disable a payload type. 
      */
    public int enable(boolean enabled);

    /**
      * Check whether a palyoad type is enabled. 
      */
    public boolean enabled();

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class PayloadTypeImpl implements PayloadType {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected PayloadTypeImpl(long ptr) {
        nativePtr = ptr;
    }


    private native int getChannels(long nativePtr);
    @Override
    synchronized public int getChannels()  {
        
        return getChannels(nativePtr);
    }

    private native int getClockRate(long nativePtr);
    @Override
    synchronized public int getClockRate()  {
        
        return getClockRate(nativePtr);
    }

    private native String getDescription(long nativePtr);
    @Override
    synchronized public String getDescription()  {
        
        return getDescription(nativePtr);
    }

    private native String getEncoderDescription(long nativePtr);
    @Override
    synchronized public String getEncoderDescription()  {
        
        return getEncoderDescription(nativePtr);
    }

    private native boolean isUsable(long nativePtr);
    @Override
    synchronized public boolean isUsable()  {
        
        return isUsable(nativePtr);
    }

    private native boolean isVbr(long nativePtr);
    @Override
    synchronized public boolean isVbr()  {
        
        return isVbr(nativePtr);
    }

    private native String getMimeType(long nativePtr);
    @Override
    synchronized public String getMimeType()  {
        
        return getMimeType(nativePtr);
    }

    private native int getNormalBitrate(long nativePtr);
    @Override
    synchronized public int getNormalBitrate()  {
        
        return getNormalBitrate(nativePtr);
    }

    private native void setNormalBitrate(long nativePtr, int bitrate);
    @Override
    synchronized public void setNormalBitrate(int bitrate)  {
        
        setNormalBitrate(nativePtr, bitrate);
    }

    private native int getNumber(long nativePtr);
    @Override
    synchronized public int getNumber()  {
        
        return getNumber(nativePtr);
    }

    private native void setNumber(long nativePtr, int number);
    @Override
    synchronized public void setNumber(int number)  {
        
        setNumber(nativePtr, number);
    }

    private native String getRecvFmtp(long nativePtr);
    @Override
    synchronized public String getRecvFmtp()  {
        
        return getRecvFmtp(nativePtr);
    }

    private native void setRecvFmtp(long nativePtr, String recvFmtp);
    @Override
    synchronized public void setRecvFmtp(String recvFmtp)  {
        
        setRecvFmtp(nativePtr, recvFmtp);
    }

    private native String getSendFmtp(long nativePtr);
    @Override
    synchronized public String getSendFmtp()  {
        
        return getSendFmtp(nativePtr);
    }

    private native void setSendFmtp(long nativePtr, String sendFmtp);
    @Override
    synchronized public void setSendFmtp(String sendFmtp)  {
        
        setSendFmtp(nativePtr, sendFmtp);
    }

    private native int getType(long nativePtr);
    @Override
    synchronized public int getType()  {
        
        return getType(nativePtr);
    }

    private native int enable(long nativePtr, boolean enabled);
    @Override
    synchronized public int enable(boolean enabled)  {
        
        return enable(nativePtr, enabled);
    }

    private native boolean enabled(long nativePtr);
    @Override
    synchronized public boolean enabled()  {
        
        return enabled(nativePtr);
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
