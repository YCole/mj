/*
ErrorInfo.java
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
  * Object representing full details about a signaling error or status. 
  */
public interface ErrorInfo {
    /**
      * Get textual phrase from the error info. 
      */
    public String getPhrase();

    /**
      * Assign phrase to a #LinphoneErrorInfo object. 
      */
    public void setPhrase(String phrase);

    /**
      * Get protocol from the error info. 
      */
    public String getProtocol();

    /**
      * Assign protocol name to a #LinphoneErrorInfo object. 
      */
    public void setProtocol(String proto);

    /**
      * Get the status code from the low level protocol (ex a SIP status code). 
      */
    public int getProtocolCode();

    /**
      * Assign protocol code to a #LinphoneErrorInfo object. 
      */
    public void setProtocolCode(int code);

    /**
      * Get reason code from the error info. 
      */
    public Reason getReason();

    /**
      * Assign reason LinphoneReason to a #LinphoneErrorInfo object. 
      */
    public void setReason(Reason reason);

    /**
      * Get pointer to chained #LinphoneErrorInfo set in sub_ei. 
      */
    public ErrorInfo getSubErrorInfo();

    /**
      * Set the sub_ei in #LinphoneErrorInfo to another LinphoneErrorInfo. 
      */
    public void setSubErrorInfo(ErrorInfo appendedEi);

    /**
      * Provides additional information regarding the failure. 
      */
    public String getWarnings();

    /**
      * Assign warnings to a #LinphoneErrorInfo object. 
      */
    public void setWarnings(String warnings);

    /**
      * Assign information to a #LinphoneErrorInfo object. 
      */
    public void set(String protocol, Reason reason, int code, String statusString, String warning);

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class ErrorInfoImpl implements ErrorInfo {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected ErrorInfoImpl(long ptr) {
        nativePtr = ptr;
    }


    private native String getPhrase(long nativePtr);
    @Override
    synchronized public String getPhrase()  {
        
        return getPhrase(nativePtr);
    }

    private native void setPhrase(long nativePtr, String phrase);
    @Override
    synchronized public void setPhrase(String phrase)  {
        
        setPhrase(nativePtr, phrase);
    }

    private native String getProtocol(long nativePtr);
    @Override
    synchronized public String getProtocol()  {
        
        return getProtocol(nativePtr);
    }

    private native void setProtocol(long nativePtr, String proto);
    @Override
    synchronized public void setProtocol(String proto)  {
        
        setProtocol(nativePtr, proto);
    }

    private native int getProtocolCode(long nativePtr);
    @Override
    synchronized public int getProtocolCode()  {
        
        return getProtocolCode(nativePtr);
    }

    private native void setProtocolCode(long nativePtr, int code);
    @Override
    synchronized public void setProtocolCode(int code)  {
        
        setProtocolCode(nativePtr, code);
    }

    private native int getReason(long nativePtr);
    @Override
    synchronized public Reason getReason()  {
        
        return Reason.fromInt(getReason(nativePtr));
    }

    private native void setReason(long nativePtr, int reason);
    @Override
    synchronized public void setReason(Reason reason)  {
        
        setReason(nativePtr, reason.toInt());
    }

    private native ErrorInfo getSubErrorInfo(long nativePtr);
    @Override
    synchronized public ErrorInfo getSubErrorInfo()  {
        
        return (ErrorInfo)getSubErrorInfo(nativePtr);
    }

    private native void setSubErrorInfo(long nativePtr, ErrorInfo appendedEi);
    @Override
    synchronized public void setSubErrorInfo(ErrorInfo appendedEi)  {
        
        setSubErrorInfo(nativePtr, appendedEi);
    }

    private native String getWarnings(long nativePtr);
    @Override
    synchronized public String getWarnings()  {
        
        return getWarnings(nativePtr);
    }

    private native void setWarnings(long nativePtr, String warnings);
    @Override
    synchronized public void setWarnings(String warnings)  {
        
        setWarnings(nativePtr, warnings);
    }

    private native void set(long nativePtr, String protocol, int reason, int code, String statusString, String warning);
    @Override
    synchronized public void set(String protocol, Reason reason, int code, String statusString, String warning)  {
        
        set(nativePtr, protocol, reason.toInt(), code, statusString, warning);
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
