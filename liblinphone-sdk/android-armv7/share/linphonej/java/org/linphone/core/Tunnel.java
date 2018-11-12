/*
Tunnel.java
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
  * Linphone tunnel object. 
  */
public interface Tunnel {
    enum Mode {
        /**
        * The tunnel is disabled. 
        */
        Disable(0),

        /**
        * The tunnel is enabled. 
        */
        Enable(1),

        /**
        * The tunnel is enabled automatically if it is required. 
        */
        Auto(2);

        protected final int mValue;

        private Mode (int value) {
            mValue = value;
        }

        static public Mode fromInt(int value) throws RuntimeException {
            switch(value) {
            case 0: return Disable;
            case 1: return Enable;
            case 2: return Auto;
            default:
                throw new RuntimeException("Unhandled enum value " + value + " for Mode");
            }
        }

        public int toInt() {
            return mValue;
        }
    };

    /**
      * Returns whether the tunnel is activated. 
      */
    public boolean getActivated();

    /**
      * Get the dual tunnel client mode. 
      */
    public boolean dualModeEnabled();

    /**
      * Sets whether or not to use the dual tunnel client mode. 
      */
    public void enableDualMode(boolean dualModeEnabled);

    /**
      * Get the tunnel mode. 
      */
    public Tunnel.Mode getMode();

    /**
      * Set the tunnel mode. 
      */
    public void setMode(Tunnel.Mode mode);

    /**
      * Get added servers. 
      */
    public TunnelConfig[] getServers();

    /**
      * Check whether tunnel is set to transport SIP packets. 
      */
    public boolean sipEnabled();

    /**
      * Set whether SIP packets must be directly sent to a UA or pass through the
      * tunnel. 
      */
    public void enableSip(boolean enable);

    /**
      * Add a tunnel server configuration. 
      */
    public void addServer(TunnelConfig tunnelConfig);

    /**
      * Remove all tunnel server addresses previously entered with
      * linphone_tunnel_add_server() 
      */
    public void cleanServers();

    /**
      * Check whether the tunnel is connected. 
      */
    public boolean connected();

    /**
      * Force reconnection to the tunnel server. 
      */
    public void reconnect();

    /**
      * Remove a tunnel server configuration. 
      */
    public void removeServer(TunnelConfig tunnelConfig);

    /**
      * Set an optional http proxy to go through when connecting to tunnel server. 
      */
    public void setHttpProxy(String host, int port, String username, String passwd);

    /**
      * Set authentication info for the http proxy. 
      */
    public void setHttpProxyAuthInfo(String username, String passwd);

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class TunnelImpl implements Tunnel {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected TunnelImpl(long ptr) {
        nativePtr = ptr;
    }


    private native boolean getActivated(long nativePtr);
    @Override
    synchronized public boolean getActivated()  {
        
        return getActivated(nativePtr);
    }

    private native boolean dualModeEnabled(long nativePtr);
    @Override
    synchronized public boolean dualModeEnabled()  {
        
        return dualModeEnabled(nativePtr);
    }

    private native void enableDualMode(long nativePtr, boolean dualModeEnabled);
    @Override
    synchronized public void enableDualMode(boolean dualModeEnabled)  {
        
        enableDualMode(nativePtr, dualModeEnabled);
    }

    private native int getMode(long nativePtr);
    @Override
    synchronized public Tunnel.Mode getMode()  {
        
        return Tunnel.Mode.fromInt(getMode(nativePtr));
    }

    private native void setMode(long nativePtr, int mode);
    @Override
    synchronized public void setMode(Tunnel.Mode mode)  {
        
        setMode(nativePtr, mode.toInt());
    }

    private native TunnelConfig[] getServers(long nativePtr);
    @Override
    synchronized public TunnelConfig[] getServers()  {
        
        return getServers(nativePtr);
    }

    private native boolean sipEnabled(long nativePtr);
    @Override
    synchronized public boolean sipEnabled()  {
        
        return sipEnabled(nativePtr);
    }

    private native void enableSip(long nativePtr, boolean enable);
    @Override
    synchronized public void enableSip(boolean enable)  {
        
        enableSip(nativePtr, enable);
    }

    private native void addServer(long nativePtr, TunnelConfig tunnelConfig);
    @Override
    synchronized public void addServer(TunnelConfig tunnelConfig)  {
        
        addServer(nativePtr, tunnelConfig);
    }

    private native void cleanServers(long nativePtr);
    @Override
    synchronized public void cleanServers()  {
        
        cleanServers(nativePtr);
    }

    private native boolean connected(long nativePtr);
    @Override
    synchronized public boolean connected()  {
        
        return connected(nativePtr);
    }

    private native void reconnect(long nativePtr);
    @Override
    synchronized public void reconnect()  {
        
        reconnect(nativePtr);
    }

    private native void removeServer(long nativePtr, TunnelConfig tunnelConfig);
    @Override
    synchronized public void removeServer(TunnelConfig tunnelConfig)  {
        
        removeServer(nativePtr, tunnelConfig);
    }

    private native void setHttpProxy(long nativePtr, String host, int port, String username, String passwd);
    @Override
    synchronized public void setHttpProxy(String host, int port, String username, String passwd)  {
        
        setHttpProxy(nativePtr, host, port, username, passwd);
    }

    private native void setHttpProxyAuthInfo(long nativePtr, String username, String passwd);
    @Override
    synchronized public void setHttpProxyAuthInfo(String username, String passwd)  {
        
        setHttpProxyAuthInfo(nativePtr, username, passwd);
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
