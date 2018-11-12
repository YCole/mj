/*
Address.java
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
  * Object that represents a SIP address. 
  */
public interface Address {
    /**
      * Returns the display name. 
      */
    public String getDisplayName();

    /**
      * Sets the display name. 
      */
    public void setDisplayName(String displayName);

    /**
      * Returns the domain name. 
      */
    public String getDomain();

    /**
      * Sets the domain. 
      */
    public void setDomain(String domain);

    /**
      * returns true if address is a routable sip address 
      */
    public boolean isSip();

    /**
      * Get the value of the method parameter. 
      */
    public String getMethodParam();

    /**
      * Set the value of the method parameter. 
      */
    public void setMethodParam(String methodParam);

    /**
      * Get the password encoded in the address. 
      */
    public String getPassword();

    /**
      * Set the password encoded in the address. 
      */
    public void setPassword(String password);

    /**
      * Get port number as an integer value, 0 if not present. 
      */
    public int getPort();

    /**
      * Sets the port number. 
      */
    public void setPort(int port);

    /**
      * Returns the address scheme, normally &quot;sip&quot;. 
      */
    public String getScheme();

    /**
      * Returns true if address refers to a secure location (sips) 
      */
    public boolean getSecure();

    /**
      * Make the address refer to a secure location (sips scheme) 
      */
    public void setSecure(boolean enabled);

    /**
      * Get the transport. 
      */
    public TransportType getTransport();

    /**
      * Set a transport. 
      */
    public void setTransport(TransportType transport);

    /**
      * Returns the username. 
      */
    public String getUsername();

    /**
      * Sets the username. 
      */
    public void setUsername(String username);

    /**
      * Returns the address as a string. 
      */
    public String asString();

    /**
      * Returns the SIP uri only as a string, that is display name is removed. 
      */
    public String asStringUriOnly();

    /**
      * Removes address's tags and uri headers so that it is displayable to the user. 
      */
    public void clean();

    /**
      * Clones a #LinphoneAddress object. 
      */
    public Address clone();

    /**
      * Compare two #LinphoneAddress taking the tags and headers into account. 
      */
    public boolean equal(Address address2);

    /**
      * Get the header encoded in the address. 
      */
    public String getHeader(String headerName);

    /**
      * Get the value of a parameter of the address. 
      */
    public String getParam(String paramName);

    /**
      * Get the value of a parameter of the URI of the address. 
      */
    public String getUriParam(String uriParamName);

    /**
      * Tell whether a parameter is present in the address. 
      */
    public boolean hasParam(String paramName);

    /**
      * Tell whether a parameter is present in the URI of the address. 
      */
    public boolean hasUriParam(String uriParamName);

    /**
      * Removes the value of a parameter of the URI of the address. 
      */
    public void removeUriParam(String uriParamName);

    /**
      * Set a header into the address. 
      */
    public void setHeader(String headerName, String headerValue);

    /**
      * Set the value of a parameter of the address. 
      */
    public void setParam(String paramName, String paramValue);

    /**
      * Set the value of a parameter of the URI of the address. 
      */
    public void setUriParam(String uriParamName, String uriParamValue);

    /**
      * Compare two #LinphoneAddress ignoring tags and headers, basically just domain,
      * username, and port. 
      */
    public boolean weakEqual(Address address2);

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class AddressImpl implements Address {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected AddressImpl(long ptr) {
        nativePtr = ptr;
    }


    private native String getDisplayName(long nativePtr);
    @Override
    synchronized public String getDisplayName()  {
        
        return getDisplayName(nativePtr);
    }

    private native int setDisplayName(long nativePtr, String displayName);
    @Override
    synchronized public void setDisplayName(String displayName)  {
        
        setDisplayName(nativePtr, displayName);
    }

    private native String getDomain(long nativePtr);
    @Override
    synchronized public String getDomain()  {
        
        return getDomain(nativePtr);
    }

    private native int setDomain(long nativePtr, String domain);
    @Override
    synchronized public void setDomain(String domain)  {
        
        setDomain(nativePtr, domain);
    }

    private native boolean isSip(long nativePtr);
    @Override
    synchronized public boolean isSip()  {
        
        return isSip(nativePtr);
    }

    private native String getMethodParam(long nativePtr);
    @Override
    synchronized public String getMethodParam()  {
        
        return getMethodParam(nativePtr);
    }

    private native void setMethodParam(long nativePtr, String methodParam);
    @Override
    synchronized public void setMethodParam(String methodParam)  {
        
        setMethodParam(nativePtr, methodParam);
    }

    private native String getPassword(long nativePtr);
    @Override
    synchronized public String getPassword()  {
        
        return getPassword(nativePtr);
    }

    private native void setPassword(long nativePtr, String password);
    @Override
    synchronized public void setPassword(String password)  {
        
        setPassword(nativePtr, password);
    }

    private native int getPort(long nativePtr);
    @Override
    synchronized public int getPort()  {
        
        return getPort(nativePtr);
    }

    private native int setPort(long nativePtr, int port);
    @Override
    synchronized public void setPort(int port)  {
        
        setPort(nativePtr, port);
    }

    private native String getScheme(long nativePtr);
    @Override
    synchronized public String getScheme()  {
        
        return getScheme(nativePtr);
    }

    private native boolean getSecure(long nativePtr);
    @Override
    synchronized public boolean getSecure()  {
        
        return getSecure(nativePtr);
    }

    private native void setSecure(long nativePtr, boolean enabled);
    @Override
    synchronized public void setSecure(boolean enabled)  {
        
        setSecure(nativePtr, enabled);
    }

    private native int getTransport(long nativePtr);
    @Override
    synchronized public TransportType getTransport()  {
        
        return TransportType.fromInt(getTransport(nativePtr));
    }

    private native int setTransport(long nativePtr, int transport);
    @Override
    synchronized public void setTransport(TransportType transport)  {
        
        setTransport(nativePtr, transport.toInt());
    }

    private native String getUsername(long nativePtr);
    @Override
    synchronized public String getUsername()  {
        
        return getUsername(nativePtr);
    }

    private native int setUsername(long nativePtr, String username);
    @Override
    synchronized public void setUsername(String username)  {
        
        setUsername(nativePtr, username);
    }

    private native String asString(long nativePtr);
    @Override
    synchronized public String asString()  {
        
        return asString(nativePtr);
    }

    private native String asStringUriOnly(long nativePtr);
    @Override
    synchronized public String asStringUriOnly()  {
        
        return asStringUriOnly(nativePtr);
    }

    private native void clean(long nativePtr);
    @Override
    synchronized public void clean()  {
        
        clean(nativePtr);
    }

    private native Address clone(long nativePtr);
    @Override
    synchronized public Address clone()  {
        
        return (Address)clone(nativePtr);
    }

    private native boolean equal(long nativePtr, Address address2);
    @Override
    synchronized public boolean equal(Address address2)  {
        
        return equal(nativePtr, address2);
    }

    private native String getHeader(long nativePtr, String headerName);
    @Override
    synchronized public String getHeader(String headerName)  {
        
        return getHeader(nativePtr, headerName);
    }

    private native String getParam(long nativePtr, String paramName);
    @Override
    synchronized public String getParam(String paramName)  {
        
        return getParam(nativePtr, paramName);
    }

    private native String getUriParam(long nativePtr, String uriParamName);
    @Override
    synchronized public String getUriParam(String uriParamName)  {
        
        return getUriParam(nativePtr, uriParamName);
    }

    private native boolean hasParam(long nativePtr, String paramName);
    @Override
    synchronized public boolean hasParam(String paramName)  {
        
        return hasParam(nativePtr, paramName);
    }

    private native boolean hasUriParam(long nativePtr, String uriParamName);
    @Override
    synchronized public boolean hasUriParam(String uriParamName)  {
        
        return hasUriParam(nativePtr, uriParamName);
    }

    private native void removeUriParam(long nativePtr, String uriParamName);
    @Override
    synchronized public void removeUriParam(String uriParamName)  {
        
        removeUriParam(nativePtr, uriParamName);
    }

    private native void setHeader(long nativePtr, String headerName, String headerValue);
    @Override
    synchronized public void setHeader(String headerName, String headerValue)  {
        
        setHeader(nativePtr, headerName, headerValue);
    }

    private native void setParam(long nativePtr, String paramName, String paramValue);
    @Override
    synchronized public void setParam(String paramName, String paramValue)  {
        
        setParam(nativePtr, paramName, paramValue);
    }

    private native void setUriParam(long nativePtr, String uriParamName, String uriParamValue);
    @Override
    synchronized public void setUriParam(String uriParamName, String uriParamValue)  {
        
        setUriParam(nativePtr, uriParamName, uriParamValue);
    }

    private native boolean weakEqual(long nativePtr, Address address2);
    @Override
    synchronized public boolean weakEqual(Address address2)  {
        
        return weakEqual(nativePtr, address2);
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
