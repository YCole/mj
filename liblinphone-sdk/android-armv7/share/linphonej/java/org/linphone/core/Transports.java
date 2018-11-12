/*
Transports.java
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
  * Linphone core SIP transport ports. 
  */
public interface Transports {
    /**
      * Gets the DTLS port in the #LinphoneTransports object. 
      */
    public int getDtlsPort();

    /**
      * Sets the DTLS port in the #LinphoneTransports object. 
      */
    public void setDtlsPort(int port);

    /**
      * Gets the TCP port in the #LinphoneTransports object. 
      */
    public int getTcpPort();

    /**
      * Sets the TCP port in the #LinphoneTransports object. 
      */
    public void setTcpPort(int port);

    /**
      * Gets the TLS port in the #LinphoneTransports object. 
      */
    public int getTlsPort();

    /**
      * Sets the TLS port in the #LinphoneTransports object. 
      */
    public void setTlsPort(int port);

    /**
      * Gets the UDP port in the #LinphoneTransports object. 
      */
    public int getUdpPort();

    /**
      * Sets the UDP port in the #LinphoneTransports object. 
      */
    public void setUdpPort(int port);

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class TransportsImpl implements Transports {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected TransportsImpl(long ptr) {
        nativePtr = ptr;
    }


    private native int getDtlsPort(long nativePtr);
    @Override
    synchronized public int getDtlsPort()  {
        
        return getDtlsPort(nativePtr);
    }

    private native void setDtlsPort(long nativePtr, int port);
    @Override
    synchronized public void setDtlsPort(int port)  {
        
        setDtlsPort(nativePtr, port);
    }

    private native int getTcpPort(long nativePtr);
    @Override
    synchronized public int getTcpPort()  {
        
        return getTcpPort(nativePtr);
    }

    private native void setTcpPort(long nativePtr, int port);
    @Override
    synchronized public void setTcpPort(int port)  {
        
        setTcpPort(nativePtr, port);
    }

    private native int getTlsPort(long nativePtr);
    @Override
    synchronized public int getTlsPort()  {
        
        return getTlsPort(nativePtr);
    }

    private native void setTlsPort(long nativePtr, int port);
    @Override
    synchronized public void setTlsPort(int port)  {
        
        setTlsPort(nativePtr, port);
    }

    private native int getUdpPort(long nativePtr);
    @Override
    synchronized public int getUdpPort()  {
        
        return getUdpPort(nativePtr);
    }

    private native void setUdpPort(long nativePtr, int port);
    @Override
    synchronized public void setUdpPort(int port)  {
        
        setUdpPort(nativePtr, port);
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
