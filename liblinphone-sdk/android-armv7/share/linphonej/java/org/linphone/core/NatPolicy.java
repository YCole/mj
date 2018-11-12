/*
NatPolicy.java
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
  * Policy to use to pass through NATs/firewalls. 
  */
public interface NatPolicy {
    /**
      * Returns the #LinphoneCore object managing this nat policy, if any. 
      */
    public Core getCore();

    /**
      * Tell whether ICE is enabled. 
      */
    public boolean iceEnabled();

    /**
      * Enable ICE. 
      */
    public void enableIce(boolean enable);

    /**
      * Tell whether STUN is enabled. 
      */
    public boolean stunEnabled();

    /**
      * Enable STUN. 
      */
    public void enableStun(boolean enable);

    /**
      * Get the STUN/TURN server to use with this NAT policy. 
      */
    public String getStunServer();

    /**
      * Set the STUN/TURN server to use with this NAT policy. 
      */
    public void setStunServer(String stunServer);

    /**
      * Get the username used to authenticate with the STUN/TURN server. 
      */
    public String getStunServerUsername();

    /**
      * Set the username used to authenticate with the STUN/TURN server. 
      */
    public void setStunServerUsername(String username);

    /**
      * Tell whether TURN is enabled. 
      */
    public boolean turnEnabled();

    /**
      * Enable TURN. 
      */
    public void enableTurn(boolean enable);

    /**
      * Tell whether uPnP is enabled. 
      */
    public boolean upnpEnabled();

    /**
      * Enable uPnP. 
      */
    public void enableUpnp(boolean enable);

    /**
      * Clear a NAT policy (deactivate all protocols and unset the STUN server). 
      */
    public void clear();

    /**
      * Start a STUN server DNS resolution. 
      */
    public void resolveStunServer();

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class NatPolicyImpl implements NatPolicy {

    protected long nativePtr = 0;
    protected Object userData = null;
    protected Core core = null;

    protected NatPolicyImpl(long ptr) {
        nativePtr = ptr;
        core = getCore();
    }


    private native Core getCore(long nativePtr);
    @Override
    synchronized public Core getCore()  {
        
        return (Core)getCore(nativePtr);
    }

    private native boolean iceEnabled(long nativePtr);
    @Override
    synchronized public boolean iceEnabled()  {
        synchronized(core) { 
        return iceEnabled(nativePtr);
        }
    }

    private native void enableIce(long nativePtr, boolean enable);
    @Override
    synchronized public void enableIce(boolean enable)  {
        synchronized(core) { 
        enableIce(nativePtr, enable);
        }
    }

    private native boolean stunEnabled(long nativePtr);
    @Override
    synchronized public boolean stunEnabled()  {
        synchronized(core) { 
        return stunEnabled(nativePtr);
        }
    }

    private native void enableStun(long nativePtr, boolean enable);
    @Override
    synchronized public void enableStun(boolean enable)  {
        synchronized(core) { 
        enableStun(nativePtr, enable);
        }
    }

    private native String getStunServer(long nativePtr);
    @Override
    synchronized public String getStunServer()  {
        synchronized(core) { 
        return getStunServer(nativePtr);
        }
    }

    private native void setStunServer(long nativePtr, String stunServer);
    @Override
    synchronized public void setStunServer(String stunServer)  {
        synchronized(core) { 
        setStunServer(nativePtr, stunServer);
        }
    }

    private native String getStunServerUsername(long nativePtr);
    @Override
    synchronized public String getStunServerUsername()  {
        synchronized(core) { 
        return getStunServerUsername(nativePtr);
        }
    }

    private native void setStunServerUsername(long nativePtr, String username);
    @Override
    synchronized public void setStunServerUsername(String username)  {
        synchronized(core) { 
        setStunServerUsername(nativePtr, username);
        }
    }

    private native boolean turnEnabled(long nativePtr);
    @Override
    synchronized public boolean turnEnabled()  {
        synchronized(core) { 
        return turnEnabled(nativePtr);
        }
    }

    private native void enableTurn(long nativePtr, boolean enable);
    @Override
    synchronized public void enableTurn(boolean enable)  {
        synchronized(core) { 
        enableTurn(nativePtr, enable);
        }
    }

    private native boolean upnpEnabled(long nativePtr);
    @Override
    synchronized public boolean upnpEnabled()  {
        synchronized(core) { 
        return upnpEnabled(nativePtr);
        }
    }

    private native void enableUpnp(long nativePtr, boolean enable);
    @Override
    synchronized public void enableUpnp(boolean enable)  {
        synchronized(core) { 
        enableUpnp(nativePtr, enable);
        }
    }

    private native void clear(long nativePtr);
    @Override
    synchronized public void clear()  {
        synchronized(core) { 
        clear(nativePtr);
        }
    }

    private native void resolveStunServer(long nativePtr);
    @Override
    synchronized public void resolveStunServer()  {
        synchronized(core) { 
        resolveStunServer(nativePtr);
        }
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
