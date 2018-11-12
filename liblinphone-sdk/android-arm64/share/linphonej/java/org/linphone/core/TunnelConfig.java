/*
TunnelConfig.java
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
  * Tunnel settings. 
  */
public interface TunnelConfig {
    /**
      * Get the UDP packet round trip delay in ms for a tunnel configuration. 
      */
    public int getDelay();

    /**
      * Set the UDP packet round trip delay in ms for a tunnel configuration. 
      */
    public void setDelay(int delay);

    /**
      * Get the IP address or hostname of the tunnel server. 
      */
    public String getHost();

    /**
      * Set the IP address or hostname of the tunnel server. 
      */
    public void setHost(String host);

    /**
      * Get the IP address or hostname of the second tunnel server when using dual
      * tunnel client. 
      */
    public String getHost2();

    /**
      * Set the IP address or hostname of the second tunnel server when using dual
      * tunnel client. 
      */
    public void setHost2(String host);

    /**
      * Get the TLS port of the tunnel server. 
      */
    public int getPort();

    /**
      * Set tls port of server. 
      */
    public void setPort(int port);

    /**
      * Get the TLS port of the second tunnel server when using dual tunnel client. 
      */
    public int getPort2();

    /**
      * Set tls port of the second server when using dual tunnel client. 
      */
    public void setPort2(int port);

    /**
      * Get the remote port on the tunnel server side used to test UDP reachability. 
      */
    public int getRemoteUdpMirrorPort();

    /**
      * Set the remote port on the tunnel server side used to test UDP reachability. 
      */
    public void setRemoteUdpMirrorPort(int remoteUdpMirrorPort);

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class TunnelConfigImpl implements TunnelConfig {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected TunnelConfigImpl(long ptr) {
        nativePtr = ptr;
    }


    private native int getDelay(long nativePtr);
    @Override
    synchronized public int getDelay()  {
        
        return getDelay(nativePtr);
    }

    private native void setDelay(long nativePtr, int delay);
    @Override
    synchronized public void setDelay(int delay)  {
        
        setDelay(nativePtr, delay);
    }

    private native String getHost(long nativePtr);
    @Override
    synchronized public String getHost()  {
        
        return getHost(nativePtr);
    }

    private native void setHost(long nativePtr, String host);
    @Override
    synchronized public void setHost(String host)  {
        
        setHost(nativePtr, host);
    }

    private native String getHost22(long nativePtr);
    @Override
    synchronized public String getHost2()  {
        
        return getHost22(nativePtr);
    }

    private native void setHost22(long nativePtr, String host);
    @Override
    synchronized public void setHost2(String host)  {
        
        setHost22(nativePtr, host);
    }

    private native int getPort(long nativePtr);
    @Override
    synchronized public int getPort()  {
        
        return getPort(nativePtr);
    }

    private native void setPort(long nativePtr, int port);
    @Override
    synchronized public void setPort(int port)  {
        
        setPort(nativePtr, port);
    }

    private native int getPort22(long nativePtr);
    @Override
    synchronized public int getPort2()  {
        
        return getPort22(nativePtr);
    }

    private native void setPort22(long nativePtr, int port);
    @Override
    synchronized public void setPort2(int port)  {
        
        setPort22(nativePtr, port);
    }

    private native int getRemoteUdpMirrorPort(long nativePtr);
    @Override
    synchronized public int getRemoteUdpMirrorPort()  {
        
        return getRemoteUdpMirrorPort(nativePtr);
    }

    private native void setRemoteUdpMirrorPort(long nativePtr, int remoteUdpMirrorPort);
    @Override
    synchronized public void setRemoteUdpMirrorPort(int remoteUdpMirrorPort)  {
        
        setRemoteUdpMirrorPort(nativePtr, remoteUdpMirrorPort);
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
