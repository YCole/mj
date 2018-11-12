/*
ProxyConfig.java
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
  * The #LinphoneProxyConfig object represents a proxy configuration to be used by
  * the #LinphoneCore object. 
  */
public interface ProxyConfig {
    /**
      * Indicates whether AVPF/SAVPF is being used for calls using this proxy config. 
      */
    public boolean avpfEnabled();

    /**
      * Get enablement status of RTCP feedback (also known as AVPF profile). 
      */
    public AVPFMode getAvpfMode();

    /**
      * Enable the use of RTCP feedback (also known as AVPF profile). 
      */
    public void setAvpfMode(AVPFMode mode);

    /**
      * Get the interval between regular RTCP reports when using AVPF/SAVPF. 
      */
    public int getAvpfRrInterval();

    /**
      * Set the interval between regular RTCP reports when using AVPF/SAVPF. 
      */
    public void setAvpfRrInterval(int interval);

    /**
      * Get the conference factory uri. 
      */
    public String getConferenceFactoryUri();

    /**
      * Set the conference factory uri. 
      */
    public void setConferenceFactoryUri(String uri);

    /**
      * Return the contact address of the proxy config. 
      */
    public Address getContact();

    /**
      */
    public String getContactParameters();

    /**
      * Set optional contact parameters that will be added to the contact information
      * sent in the registration. 
      */
    public void setContactParameters(String contactParams);

    /**
      */
    public String getContactUriParameters();

    /**
      * Set optional contact parameters that will be added to the contact information
      * sent in the registration, inside the URI. 
      */
    public void setContactUriParameters(String contactUriParams);

    /**
      * Get the #LinphoneCore object to which is associated the #LinphoneProxyConfig. 
      */
    public Core getCore();

    /**
      */
    public boolean getDialEscapePlus();

    /**
      * Sets whether liblinphone should replace &quot;+&quot; by international calling prefix in
      * dialed numbers (passed to linphone_core_invite ). 
      */
    public void setDialEscapePlus(boolean val);

    /**
      */
    public String getDialPrefix();

    /**
      * Sets a dialing prefix to be automatically prepended when inviting a number with
      * linphone_core_invite(); This dialing prefix shall usually be the country code
      * of the country where the user is living, without &quot;+&quot;. 
      */
    public void setDialPrefix(String prefix);

    /**
      * Get the domain name of the given proxy config. 
      */
    public String getDomain();

    /**
      * Get the reason why registration failed when the proxy config state is
      * LinphoneRegistrationFailed. 
      */
    public Reason getError();

    /**
      * Get detailed information why registration failed when the proxy config state is
      * LinphoneRegistrationFailed. 
      */
    public ErrorInfo getErrorInfo();

    /**
      */
    public int getExpires();

    /**
      * Sets the registration expiration time in seconds. 
      */
    public void setExpires(int expires);

    /**
      */
    public Address getIdentityAddress();

    /**
      * Sets the user identity as a SIP address. 
      */
    public void setIdentityAddress(Address identity);

    /**
      * Indicates whether to add to the contact parameters the push notification
      * information. 
      */
    public boolean isPushNotificationAllowed();

    /**
      * Get The policy that is used to pass through NATs/firewalls when using this
      * proxy config. 
      */
    public NatPolicy getNatPolicy();

    /**
      * Set the policy to use to pass through NATs/firewalls when using this proxy
      * config. 
      */
    public void setNatPolicy(NatPolicy policy);

    /**
      * Get default privacy policy for all calls routed through this proxy. 
      */
    public int getPrivacy();

    /**
      * Set default privacy policy for all calls routed through this proxy. 
      */
    public void setPrivacy(int privacy);

    /**
      */
    public boolean publishEnabled();

    /**
      * Indicates either or not, PUBLISH must be issued for this #LinphoneProxyConfig. 
      */
    public void enablePublish(boolean val);

    /**
      * get the publish expiration time in second. 
      */
    public int getPublishExpires();

    /**
      * Set the publish expiration time in second. 
      */
    public void setPublishExpires(int expires);

    /**
      * Indicates whether to add to the contact parameters the push notification
      * information. 
      */
    public void setPushNotificationAllowed(boolean allow);

    /**
      * Get the route of the collector end-point when using quality reporting. 
      */
    public String getQualityReportingCollector();

    /**
      * Set the route of the collector end-point when using quality reporting. 
      */
    public void setQualityReportingCollector(String collector);

    /**
      * Indicates whether quality statistics during call should be stored and sent to a
      * collector according to RFC 6035. 
      */
    public boolean qualityReportingEnabled();

    /**
      * Indicates whether quality statistics during call should be stored and sent to a
      * collector according to RFC 6035. 
      */
    public void enableQualityReporting(boolean enable);

    /**
      * Get the interval between interval reports when using quality reporting. 
      */
    public int getQualityReportingInterval();

    /**
      * Set the interval between 2 interval reports sending when using quality
      * reporting. 
      */
    public void setQualityReportingInterval(int interval);

    /**
      * Get the realm of the given proxy config. 
      */
    public String getRealm();

    /**
      * Set the realm of the given proxy config. 
      */
    public void setRealm(String realm);

    /**
      * Get the persistent reference key associated to the proxy config. 
      */
    public String getRefKey();

    /**
      * Associate a persistent reference key to the proxy config. 
      */
    public void setRefKey(String refkey);

    /**
      */
    public boolean registerEnabled();

    /**
      * Indicates either or not, REGISTRATION must be issued for this
      * #LinphoneProxyConfig . 
      */
    public void enableRegister(boolean val);

    /**
      */
    @Deprecated
    public String getRoute();

    /**
      * Sets a SIP route. 
      */
    public void setRoute(String route);

    /**
      * Gets the list of the routes set for this proxy config. 
      */
    public String[] getRoutes();

    /**
      * Sets a list of SIP route. 
      */
    public void setRoutes(String[] routes);

    /**
      */
    public String getServerAddr();

    /**
      * Sets the proxy address. 
      */
    public void setServerAddr(String serverAddr);

    /**
      * Get the registration state of the given proxy config. 
      */
    public RegistrationState getState();

    /**
      * Get the transport from either service route, route or addr. 
      */
    public String getTransport();

    /**
      * Return the unread chat message count for a given proxy config. 
      */
    public int getUnreadChatMessageCount();

    /**
      * Commits modification made to the proxy configuration. 
      */
    public void done();

    /**
      * Starts editing a proxy configuration. 
      */
    public void edit();

    /**
      * Find authentication info matching proxy config, if any, similarly to
      * linphone_core_find_auth_info. 
      */
    public AuthInfo findAuthInfo();

    /**
      * Obtain the value of a header sent by the server in last answer to REGISTER. 
      */
    public String getCustomHeader(String headerName);

    /**
      * Detect if the given input is a phone number or not. 
      */
    public boolean isPhoneNumber(String username);

    /**
      * Normalize a human readable phone number into a basic string. 
      */
    public String normalizePhoneNumber(String username);

    /**
      * Normalize a human readable sip uri into a fully qualified LinphoneAddress. 
      */
    public Address normalizeSipUri(String username);

    /**
      * Prevent a proxy config from refreshing its registration. 
      */
    public void pauseRegister();

    /**
      * Refresh a proxy registration. 
      */
    public void refreshRegister();

    /**
      * Set the value of a custom header sent to the server in REGISTERs request. 
      */
    public void setCustomHeader(String headerName, String headerValue);

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class ProxyConfigImpl implements ProxyConfig {

    protected long nativePtr = 0;
    protected Object userData = null;
    protected Core core = null;

    protected ProxyConfigImpl(long ptr) {
        nativePtr = ptr;
        core = getCore();
    }


    private native boolean avpfEnabled(long nativePtr);
    @Override
    synchronized public boolean avpfEnabled()  {
        synchronized(core) { 
        return avpfEnabled(nativePtr);
        }
    }

    private native int getAvpfMode(long nativePtr);
    @Override
    synchronized public AVPFMode getAvpfMode()  {
        synchronized(core) { 
        return AVPFMode.fromInt(getAvpfMode(nativePtr));
        }
    }

    private native void setAvpfMode(long nativePtr, int mode);
    @Override
    synchronized public void setAvpfMode(AVPFMode mode)  {
        synchronized(core) { 
        setAvpfMode(nativePtr, mode.toInt());
        }
    }

    private native int getAvpfRrInterval(long nativePtr);
    @Override
    synchronized public int getAvpfRrInterval()  {
        synchronized(core) { 
        return getAvpfRrInterval(nativePtr);
        }
    }

    private native void setAvpfRrInterval(long nativePtr, int interval);
    @Override
    synchronized public void setAvpfRrInterval(int interval)  {
        synchronized(core) { 
        setAvpfRrInterval(nativePtr, interval);
        }
    }

    private native String getConferenceFactoryUri(long nativePtr);
    @Override
    synchronized public String getConferenceFactoryUri()  {
        synchronized(core) { 
        return getConferenceFactoryUri(nativePtr);
        }
    }

    private native void setConferenceFactoryUri(long nativePtr, String uri);
    @Override
    synchronized public void setConferenceFactoryUri(String uri)  {
        synchronized(core) { 
        setConferenceFactoryUri(nativePtr, uri);
        }
    }

    private native Address getContact(long nativePtr);
    @Override
    synchronized public Address getContact()  {
        synchronized(core) { 
        return (Address)getContact(nativePtr);
        }
    }

    private native String getContactParameters(long nativePtr);
    @Override
    synchronized public String getContactParameters()  {
        synchronized(core) { 
        return getContactParameters(nativePtr);
        }
    }

    private native void setContactParameters(long nativePtr, String contactParams);
    @Override
    synchronized public void setContactParameters(String contactParams)  {
        synchronized(core) { 
        setContactParameters(nativePtr, contactParams);
        }
    }

    private native String getContactUriParameters(long nativePtr);
    @Override
    synchronized public String getContactUriParameters()  {
        synchronized(core) { 
        return getContactUriParameters(nativePtr);
        }
    }

    private native void setContactUriParameters(long nativePtr, String contactUriParams);
    @Override
    synchronized public void setContactUriParameters(String contactUriParams)  {
        synchronized(core) { 
        setContactUriParameters(nativePtr, contactUriParams);
        }
    }

    private native Core getCore(long nativePtr);
    @Override
    synchronized public Core getCore()  {
        
        return (Core)getCore(nativePtr);
    }

    private native boolean getDialEscapePlus(long nativePtr);
    @Override
    synchronized public boolean getDialEscapePlus()  {
        synchronized(core) { 
        return getDialEscapePlus(nativePtr);
        }
    }

    private native void setDialEscapePlus(long nativePtr, boolean val);
    @Override
    synchronized public void setDialEscapePlus(boolean val)  {
        synchronized(core) { 
        setDialEscapePlus(nativePtr, val);
        }
    }

    private native String getDialPrefix(long nativePtr);
    @Override
    synchronized public String getDialPrefix()  {
        synchronized(core) { 
        return getDialPrefix(nativePtr);
        }
    }

    private native void setDialPrefix(long nativePtr, String prefix);
    @Override
    synchronized public void setDialPrefix(String prefix)  {
        synchronized(core) { 
        setDialPrefix(nativePtr, prefix);
        }
    }

    private native String getDomain(long nativePtr);
    @Override
    synchronized public String getDomain()  {
        synchronized(core) { 
        return getDomain(nativePtr);
        }
    }

    private native int getError(long nativePtr);
    @Override
    synchronized public Reason getError()  {
        synchronized(core) { 
        return Reason.fromInt(getError(nativePtr));
        }
    }

    private native ErrorInfo getErrorInfo(long nativePtr);
    @Override
    synchronized public ErrorInfo getErrorInfo()  {
        synchronized(core) { 
        return (ErrorInfo)getErrorInfo(nativePtr);
        }
    }

    private native int getExpires(long nativePtr);
    @Override
    synchronized public int getExpires()  {
        synchronized(core) { 
        return getExpires(nativePtr);
        }
    }

    private native void setExpires(long nativePtr, int expires);
    @Override
    synchronized public void setExpires(int expires)  {
        synchronized(core) { 
        setExpires(nativePtr, expires);
        }
    }

    private native Address getIdentityAddress(long nativePtr);
    @Override
    synchronized public Address getIdentityAddress()  {
        synchronized(core) { 
        return (Address)getIdentityAddress(nativePtr);
        }
    }

    private native int setIdentityAddress(long nativePtr, Address identity);
    @Override
    synchronized public void setIdentityAddress(Address identity)  {
        synchronized(core) { 
        setIdentityAddress(nativePtr, identity);
        }
    }

    private native boolean isPushNotificationAllowed(long nativePtr);
    @Override
    synchronized public boolean isPushNotificationAllowed()  {
        synchronized(core) { 
        return isPushNotificationAllowed(nativePtr);
        }
    }

    private native NatPolicy getNatPolicy(long nativePtr);
    @Override
    synchronized public NatPolicy getNatPolicy()  {
        synchronized(core) { 
        return (NatPolicy)getNatPolicy(nativePtr);
        }
    }

    private native void setNatPolicy(long nativePtr, NatPolicy policy);
    @Override
    synchronized public void setNatPolicy(NatPolicy policy)  {
        synchronized(core) { 
        setNatPolicy(nativePtr, policy);
        }
    }

    private native int getPrivacy(long nativePtr);
    @Override
    synchronized public int getPrivacy()  {
        synchronized(core) { 
        return getPrivacy(nativePtr);
        }
    }

    private native void setPrivacy(long nativePtr, int privacy);
    @Override
    synchronized public void setPrivacy(int privacy)  {
        synchronized(core) { 
        setPrivacy(nativePtr, privacy);
        }
    }

    private native boolean publishEnabled(long nativePtr);
    @Override
    synchronized public boolean publishEnabled()  {
        synchronized(core) { 
        return publishEnabled(nativePtr);
        }
    }

    private native void enablePublish(long nativePtr, boolean val);
    @Override
    synchronized public void enablePublish(boolean val)  {
        synchronized(core) { 
        enablePublish(nativePtr, val);
        }
    }

    private native int getPublishExpires(long nativePtr);
    @Override
    synchronized public int getPublishExpires()  {
        synchronized(core) { 
        return getPublishExpires(nativePtr);
        }
    }

    private native void setPublishExpires(long nativePtr, int expires);
    @Override
    synchronized public void setPublishExpires(int expires)  {
        synchronized(core) { 
        setPublishExpires(nativePtr, expires);
        }
    }

    private native void setPushNotificationAllowed(long nativePtr, boolean allow);
    @Override
    synchronized public void setPushNotificationAllowed(boolean allow)  {
        synchronized(core) { 
        setPushNotificationAllowed(nativePtr, allow);
        }
    }

    private native String getQualityReportingCollector(long nativePtr);
    @Override
    synchronized public String getQualityReportingCollector()  {
        synchronized(core) { 
        return getQualityReportingCollector(nativePtr);
        }
    }

    private native void setQualityReportingCollector(long nativePtr, String collector);
    @Override
    synchronized public void setQualityReportingCollector(String collector)  {
        synchronized(core) { 
        setQualityReportingCollector(nativePtr, collector);
        }
    }

    private native boolean qualityReportingEnabled(long nativePtr);
    @Override
    synchronized public boolean qualityReportingEnabled()  {
        synchronized(core) { 
        return qualityReportingEnabled(nativePtr);
        }
    }

    private native void enableQualityReporting(long nativePtr, boolean enable);
    @Override
    synchronized public void enableQualityReporting(boolean enable)  {
        synchronized(core) { 
        enableQualityReporting(nativePtr, enable);
        }
    }

    private native int getQualityReportingInterval(long nativePtr);
    @Override
    synchronized public int getQualityReportingInterval()  {
        synchronized(core) { 
        return getQualityReportingInterval(nativePtr);
        }
    }

    private native void setQualityReportingInterval(long nativePtr, int interval);
    @Override
    synchronized public void setQualityReportingInterval(int interval)  {
        synchronized(core) { 
        setQualityReportingInterval(nativePtr, interval);
        }
    }

    private native String getRealm(long nativePtr);
    @Override
    synchronized public String getRealm()  {
        synchronized(core) { 
        return getRealm(nativePtr);
        }
    }

    private native void setRealm(long nativePtr, String realm);
    @Override
    synchronized public void setRealm(String realm)  {
        synchronized(core) { 
        setRealm(nativePtr, realm);
        }
    }

    private native String getRefKey(long nativePtr);
    @Override
    synchronized public String getRefKey()  {
        synchronized(core) { 
        return getRefKey(nativePtr);
        }
    }

    private native void setRefKey(long nativePtr, String refkey);
    @Override
    synchronized public void setRefKey(String refkey)  {
        synchronized(core) { 
        setRefKey(nativePtr, refkey);
        }
    }

    private native boolean registerEnabled(long nativePtr);
    @Override
    synchronized public boolean registerEnabled()  {
        synchronized(core) { 
        return registerEnabled(nativePtr);
        }
    }

    private native void enableRegister(long nativePtr, boolean val);
    @Override
    synchronized public void enableRegister(boolean val)  {
        synchronized(core) { 
        enableRegister(nativePtr, val);
        }
    }

    private native String getRoute(long nativePtr);
    @Override
    synchronized public String getRoute()  {
        synchronized(core) { 
        return getRoute(nativePtr);
        }
    }

    private native int setRoute(long nativePtr, String route);
    @Override
    synchronized public void setRoute(String route)  {
        synchronized(core) { 
        setRoute(nativePtr, route);
        }
    }

    private native String[] getRoutes(long nativePtr);
    @Override
    synchronized public String[] getRoutes()  {
        synchronized(core) { 
        return getRoutes(nativePtr);
        }
    }

    private native int setRoutes(long nativePtr, String[] routes);
    @Override
    synchronized public void setRoutes(String[] routes)  {
        synchronized(core) { 
        setRoutes(nativePtr, routes);
        }
    }

    private native String getServerAddr(long nativePtr);
    @Override
    synchronized public String getServerAddr()  {
        synchronized(core) { 
        return getServerAddr(nativePtr);
        }
    }

    private native int setServerAddr(long nativePtr, String serverAddr);
    @Override
    synchronized public void setServerAddr(String serverAddr)  {
        synchronized(core) { 
        setServerAddr(nativePtr, serverAddr);
        }
    }

    private native int getState(long nativePtr);
    @Override
    synchronized public RegistrationState getState()  {
        synchronized(core) { 
        return RegistrationState.fromInt(getState(nativePtr));
        }
    }

    private native String getTransport(long nativePtr);
    @Override
    synchronized public String getTransport()  {
        synchronized(core) { 
        return getTransport(nativePtr);
        }
    }

    private native int getUnreadChatMessageCount(long nativePtr);
    @Override
    synchronized public int getUnreadChatMessageCount()  {
        synchronized(core) { 
        return getUnreadChatMessageCount(nativePtr);
        }
    }

    private native int done(long nativePtr);
    @Override
    synchronized public void done()  {
        synchronized(core) { 
        done(nativePtr);
        }
    }

    private native void edit(long nativePtr);
    @Override
    synchronized public void edit()  {
        synchronized(core) { 
        edit(nativePtr);
        }
    }

    private native AuthInfo findAuthInfo(long nativePtr);
    @Override
    synchronized public AuthInfo findAuthInfo()  {
        synchronized(core) { 
        return (AuthInfo)findAuthInfo(nativePtr);
        }
    }

    private native String getCustomHeader(long nativePtr, String headerName);
    @Override
    synchronized public String getCustomHeader(String headerName)  {
        synchronized(core) { 
        return getCustomHeader(nativePtr, headerName);
        }
    }

    private native boolean isPhoneNumber(long nativePtr, String username);
    @Override
    synchronized public boolean isPhoneNumber(String username)  {
        synchronized(core) { 
        return isPhoneNumber(nativePtr, username);
        }
    }

    private native String normalizePhoneNumber(long nativePtr, String username);
    @Override
    synchronized public String normalizePhoneNumber(String username)  {
        synchronized(core) { 
        return normalizePhoneNumber(nativePtr, username);
        }
    }

    private native Address normalizeSipUri(long nativePtr, String username);
    @Override
    synchronized public Address normalizeSipUri(String username)  {
        synchronized(core) { 
        return (Address)normalizeSipUri(nativePtr, username);
        }
    }

    private native void pauseRegister(long nativePtr);
    @Override
    synchronized public void pauseRegister()  {
        synchronized(core) { 
        pauseRegister(nativePtr);
        }
    }

    private native void refreshRegister(long nativePtr);
    @Override
    synchronized public void refreshRegister()  {
        synchronized(core) { 
        refreshRegister(nativePtr);
        }
    }

    private native void setCustomHeader(long nativePtr, String headerName, String headerValue);
    @Override
    synchronized public void setCustomHeader(String headerName, String headerValue)  {
        synchronized(core) { 
        setCustomHeader(nativePtr, headerName, headerValue);
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
