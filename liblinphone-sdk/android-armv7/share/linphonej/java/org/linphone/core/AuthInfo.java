/*
AuthInfo.java
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
  * Object holding authentication information. 
  */
public interface AuthInfo {
    /**
      * Gets the domain. 
      */
    public String getDomain();

    /**
      * Sets the domain for which this authentication is valid. 
      */
    public void setDomain(String domain);

    /**
      * Gets the ha1. 
      */
    public String getHa1();

    /**
      * Sets the ha1. 
      */
    public void setHa1(String ha1);

    /**
      * Gets the password. 
      */
    @Deprecated
    public String getPasswd();

    /**
      * Sets the password. 
      */
    @Deprecated
    public void setPasswd(String passwd);

    /**
      * Gets the password. 
      */
    public String getPassword();

    /**
      * Sets the password. 
      */
    public void setPassword(String passwd);

    /**
      * Gets the realm. 
      */
    public String getRealm();

    /**
      * Sets the realm. 
      */
    public void setRealm(String realm);

    /**
      * Gets the TLS certificate. 
      */
    public String getTlsCert();

    /**
      * Sets the TLS certificate. 
      */
    public void setTlsCert(String tlsCert);

    /**
      * Gets the TLS certificate path. 
      */
    public String getTlsCertPath();

    /**
      * Sets the TLS certificate path. 
      */
    public void setTlsCertPath(String tlsCertPath);

    /**
      * Gets the TLS key. 
      */
    public String getTlsKey();

    /**
      * Sets the TLS key. 
      */
    public void setTlsKey(String tlsKey);

    /**
      * Gets the TLS key path. 
      */
    public String getTlsKeyPath();

    /**
      * Sets the TLS key path. 
      */
    public void setTlsKeyPath(String tlsKeyPath);

    /**
      * Gets the userid. 
      */
    public String getUserid();

    /**
      * Sets the userid. 
      */
    public void setUserid(String userid);

    /**
      * Gets the username. 
      */
    public String getUsername();

    /**
      * Sets the username. 
      */
    public void setUsername(String username);

    /**
      * Instantiates a new auth info with values from source. 
      */
    public AuthInfo clone();

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class AuthInfoImpl implements AuthInfo {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected AuthInfoImpl(long ptr) {
        nativePtr = ptr;
    }


    private native String getDomain(long nativePtr);
    @Override
    synchronized public String getDomain()  {
        
        return getDomain(nativePtr);
    }

    private native void setDomain(long nativePtr, String domain);
    @Override
    synchronized public void setDomain(String domain)  {
        
        setDomain(nativePtr, domain);
    }

    private native String getHa1(long nativePtr);
    @Override
    synchronized public String getHa1()  {
        
        return getHa1(nativePtr);
    }

    private native void setHa1(long nativePtr, String ha1);
    @Override
    synchronized public void setHa1(String ha1)  {
        
        setHa1(nativePtr, ha1);
    }

    private native String getPasswd(long nativePtr);
    @Override
    synchronized public String getPasswd()  {
        
        return getPasswd(nativePtr);
    }

    private native void setPasswd(long nativePtr, String passwd);
    @Override
    synchronized public void setPasswd(String passwd)  {
        
        setPasswd(nativePtr, passwd);
    }

    private native String getPassword(long nativePtr);
    @Override
    synchronized public String getPassword()  {
        
        return getPassword(nativePtr);
    }

    private native void setPassword(long nativePtr, String passwd);
    @Override
    synchronized public void setPassword(String passwd)  {
        
        setPassword(nativePtr, passwd);
    }

    private native String getRealm(long nativePtr);
    @Override
    synchronized public String getRealm()  {
        
        return getRealm(nativePtr);
    }

    private native void setRealm(long nativePtr, String realm);
    @Override
    synchronized public void setRealm(String realm)  {
        
        setRealm(nativePtr, realm);
    }

    private native String getTlsCert(long nativePtr);
    @Override
    synchronized public String getTlsCert()  {
        
        return getTlsCert(nativePtr);
    }

    private native void setTlsCert(long nativePtr, String tlsCert);
    @Override
    synchronized public void setTlsCert(String tlsCert)  {
        
        setTlsCert(nativePtr, tlsCert);
    }

    private native String getTlsCertPath(long nativePtr);
    @Override
    synchronized public String getTlsCertPath()  {
        
        return getTlsCertPath(nativePtr);
    }

    private native void setTlsCertPath(long nativePtr, String tlsCertPath);
    @Override
    synchronized public void setTlsCertPath(String tlsCertPath)  {
        
        setTlsCertPath(nativePtr, tlsCertPath);
    }

    private native String getTlsKey(long nativePtr);
    @Override
    synchronized public String getTlsKey()  {
        
        return getTlsKey(nativePtr);
    }

    private native void setTlsKey(long nativePtr, String tlsKey);
    @Override
    synchronized public void setTlsKey(String tlsKey)  {
        
        setTlsKey(nativePtr, tlsKey);
    }

    private native String getTlsKeyPath(long nativePtr);
    @Override
    synchronized public String getTlsKeyPath()  {
        
        return getTlsKeyPath(nativePtr);
    }

    private native void setTlsKeyPath(long nativePtr, String tlsKeyPath);
    @Override
    synchronized public void setTlsKeyPath(String tlsKeyPath)  {
        
        setTlsKeyPath(nativePtr, tlsKeyPath);
    }

    private native String getUserid(long nativePtr);
    @Override
    synchronized public String getUserid()  {
        
        return getUserid(nativePtr);
    }

    private native void setUserid(long nativePtr, String userid);
    @Override
    synchronized public void setUserid(String userid)  {
        
        setUserid(nativePtr, userid);
    }

    private native String getUsername(long nativePtr);
    @Override
    synchronized public String getUsername()  {
        
        return getUsername(nativePtr);
    }

    private native void setUsername(long nativePtr, String username);
    @Override
    synchronized public void setUsername(String username)  {
        
        setUsername(nativePtr, username);
    }

    private native AuthInfo clone(long nativePtr);
    @Override
    synchronized public AuthInfo clone()  {
        
        return (AuthInfo)clone(nativePtr);
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
