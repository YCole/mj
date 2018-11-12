/*
Event.java
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
  * Object representing an event state, which is subcribed or published. 
  */
public interface Event {
    /**
      * Returns back pointer to the #LinphoneCore that created this #LinphoneEvent. 
      */
    public Core getCore();

    /**
      * Get full details about an error occured. 
      */
    public ErrorInfo getErrorInfo();

    /**
      * Get the &quot;from&quot; address of the subscription. 
      */
    public Address getFrom();

    /**
      * Get the name of the event as specified in the event package RFC. 
      */
    public String getName();

    /**
      * Get publish state. 
      */
    public PublishState getPublishState();

    /**
      * Return reason code (in case of error state reached). 
      */
    public Reason getReason();

    /**
      * Get the &quot;contact&quot; address of the subscription. 
      */
    public Address getRemoteContact();

    /**
      * Get the resource address of the subscription or publish. 
      */
    public Address getResource();

    /**
      * Get subscription direction. 
      */
    public SubscriptionDir getSubscriptionDir();

    /**
      * Get subscription state. 
      */
    public SubscriptionState getSubscriptionState();

    /**
      * Accept an incoming subcription. 
      */
    public void acceptSubscription();

    /**
      * Add a custom header to an outgoing susbscription or publish. 
      */
    public void addCustomHeader(String name, String value);

    /**
      * Deny an incoming subscription with given reason. 
      */
    public void denySubscription(Reason reason);

    /**
      * Obtain the value of a given header for an incoming subscription. 
      */
    public String getCustomHeader(String name);

    /**
      * Send a notification. 
      */
    public void notify(Content body);

    /**
      * Prevent an event from refreshing its publish. 
      */
    public void pausePublish();

    /**
      * Refresh an outgoing publish keeping the same body. 
      */
    public void refreshPublish();

    /**
      * Refresh an outgoing subscription keeping the same body. 
      */
    public void refreshSubscribe();

    /**
      * Send a publish created by linphone_core_create_publish(). 
      */
    public void sendPublish(Content body);

    /**
      * Send a subscription previously created by linphone_core_create_subscribe(). 
      */
    public void sendSubscribe(Content body);

    /**
      * Terminate an incoming or outgoing subscription that was previously acccepted,
      * or a previous publication. 
      */
    public void terminate();

    /**
      * Update (refresh) a publish. 
      */
    public void updatePublish(Content body);

    /**
      * Update (refresh) an outgoing subscription, changing the body. 
      */
    public void updateSubscribe(Content body);

    public void setListener(EventListener listener);

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class EventImpl implements Event {

    protected long nativePtr = 0;
    protected Object userData = null;
    protected Core core = null;

    protected EventImpl(long ptr) {
        nativePtr = ptr;
        core = getCore();
    }


    private native Core getCore(long nativePtr);
    @Override
    synchronized public Core getCore()  {
        
        return (Core)getCore(nativePtr);
    }

    private native ErrorInfo getErrorInfo(long nativePtr);
    @Override
    synchronized public ErrorInfo getErrorInfo()  {
        synchronized(core) { 
        return (ErrorInfo)getErrorInfo(nativePtr);
        }
    }

    private native Address getFrom(long nativePtr);
    @Override
    synchronized public Address getFrom()  {
        synchronized(core) { 
        return (Address)getFrom(nativePtr);
        }
    }

    private native String getName(long nativePtr);
    @Override
    synchronized public String getName()  {
        synchronized(core) { 
        return getName(nativePtr);
        }
    }

    private native int getPublishState(long nativePtr);
    @Override
    synchronized public PublishState getPublishState()  {
        synchronized(core) { 
        return PublishState.fromInt(getPublishState(nativePtr));
        }
    }

    private native int getReason(long nativePtr);
    @Override
    synchronized public Reason getReason()  {
        synchronized(core) { 
        return Reason.fromInt(getReason(nativePtr));
        }
    }

    private native Address getRemoteContact(long nativePtr);
    @Override
    synchronized public Address getRemoteContact()  {
        synchronized(core) { 
        return (Address)getRemoteContact(nativePtr);
        }
    }

    private native Address getResource(long nativePtr);
    @Override
    synchronized public Address getResource()  {
        synchronized(core) { 
        return (Address)getResource(nativePtr);
        }
    }

    private native int getSubscriptionDir(long nativePtr);
    @Override
    synchronized public SubscriptionDir getSubscriptionDir()  {
        synchronized(core) { 
        return SubscriptionDir.fromInt(getSubscriptionDir(nativePtr));
        }
    }

    private native int getSubscriptionState(long nativePtr);
    @Override
    synchronized public SubscriptionState getSubscriptionState()  {
        synchronized(core) { 
        return SubscriptionState.fromInt(getSubscriptionState(nativePtr));
        }
    }

    private native int acceptSubscription(long nativePtr);
    @Override
    synchronized public void acceptSubscription()  {
        synchronized(core) { 
        acceptSubscription(nativePtr);
        }
    }

    private native void addCustomHeader(long nativePtr, String name, String value);
    @Override
    synchronized public void addCustomHeader(String name, String value)  {
        synchronized(core) { 
        addCustomHeader(nativePtr, name, value);
        }
    }

    private native int denySubscription(long nativePtr, int reason);
    @Override
    synchronized public void denySubscription(Reason reason)  {
        synchronized(core) { 
        denySubscription(nativePtr, reason.toInt());
        }
    }

    private native String getCustomHeader(long nativePtr, String name);
    @Override
    synchronized public String getCustomHeader(String name)  {
        synchronized(core) { 
        return getCustomHeader(nativePtr, name);
        }
    }

    private native int notify(long nativePtr, Content body);
    @Override
    synchronized public void notify(Content body)  {
        synchronized(core) { 
        notify(nativePtr, body);
        }
    }

    private native void pausePublish(long nativePtr);
    @Override
    synchronized public void pausePublish()  {
        synchronized(core) { 
        pausePublish(nativePtr);
        }
    }

    private native int refreshPublish(long nativePtr);
    @Override
    synchronized public void refreshPublish()  {
        synchronized(core) { 
        refreshPublish(nativePtr);
        }
    }

    private native int refreshSubscribe(long nativePtr);
    @Override
    synchronized public void refreshSubscribe()  {
        synchronized(core) { 
        refreshSubscribe(nativePtr);
        }
    }

    private native int sendPublish(long nativePtr, Content body);
    @Override
    synchronized public void sendPublish(Content body)  {
        synchronized(core) { 
        sendPublish(nativePtr, body);
        }
    }

    private native int sendSubscribe(long nativePtr, Content body);
    @Override
    synchronized public void sendSubscribe(Content body)  {
        synchronized(core) { 
        sendSubscribe(nativePtr, body);
        }
    }

    private native void terminate(long nativePtr);
    @Override
    synchronized public void terminate()  {
        synchronized(core) { 
        terminate(nativePtr);
        }
    }

    private native int updatePublish(long nativePtr, Content body);
    @Override
    synchronized public void updatePublish(Content body)  {
        synchronized(core) { 
        updatePublish(nativePtr, body);
        }
    }

    private native int updateSubscribe(long nativePtr, Content body);
    @Override
    synchronized public void updateSubscribe(Content body)  {
        synchronized(core) { 
        updateSubscribe(nativePtr, body);
        }
    }

    private native void setListener(long nativePtr, EventListener listener);
    @Override
    synchronized public void setListener(EventListener listener)  {
        
        setListener(nativePtr, listener);
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
