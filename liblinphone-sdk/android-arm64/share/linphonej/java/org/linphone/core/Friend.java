/*
Friend.java
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
  * Represents a buddy, all presence actions like subscription and status change
  * notification are performed on this object. 
  */
public interface Friend {
    /**
      * Get address of this friend. 
      */
    public Address getAddress();

    /**
      * Set #LinphoneAddress for this friend. 
      */
    public void setAddress(Address address);

    /**
      * Returns a list of #LinphoneAddress for this friend. 
      */
    public Address[] getAddresses();

    /**
      * Get the consolidated presence of a friend. 
      */
    public ConsolidatedPresence getConsolidatedPresence();

    /**
      * Returns the #LinphoneCore object managing this friend, if any. 
      */
    public Core getCore();

    /**
      * get current subscription policy for this #LinphoneFriend 
      */
    public SubscribePolicy getIncSubscribePolicy();

    /**
      * Configure incoming subscription policy for this friend. 
      */
    public void setIncSubscribePolicy(SubscribePolicy pol);

    /**
      * Tells whether we already received presence information for a friend. 
      */
    public boolean isPresenceReceived();

    /**
      * Get the display name for this friend. 
      */
    public String getName();

    /**
      * Set the display name for this friend. 
      */
    public void setName(String name);

    /**
      * Returns a list of phone numbers for this friend. 
      */
    public String[] getPhoneNumbers();

    /**
      * Get the presence model of a friend. 
      */
    public PresenceModel getPresenceModel();

    /**
      * Set the presence model of a friend. 
      */
    public void setPresenceModel(PresenceModel presence);

    /**
      * Get the reference key of a friend. 
      */
    public String getRefKey();

    /**
      * Set the reference key of a friend. 
      */
    public void setRefKey(String key);

    /**
      * get subscription flag value 
      */
    public boolean subscribesEnabled();

    /**
      * Configure #LinphoneFriend to subscribe to presence information. 
      */
    public void enableSubscribes(boolean val);

    /**
      * Get subscription state of a friend. 
      */
    public SubscriptionState getSubscriptionState();

    /**
      * Returns the vCard object associated to this friend, if any. 
      */
    public Vcard getVcard();

    /**
      * Binds a vCard object to a friend. 
      */
    public void setVcard(Vcard vcard);

    /**
      * Adds an address in this friend. 
      */
    public void addAddress(Address addr);

    /**
      * Adds a phone number in this friend. 
      */
    public void addPhoneNumber(String phone);

    /**
      * Creates a vCard object associated to this friend if there isn't one yet and if
      * the full name is available, either by the parameter or the one in the friend's
      * SIP URI. 
      */
    public boolean createVcard(String name);

    /**
      * Commits modification made to the friend configuration. 
      */
    public void done();

    /**
      * Starts editing a friend configuration. 
      */
    public void edit();

    /**
      * Get the presence model for a specific SIP URI or phone number of a friend. 
      */
    public PresenceModel getPresenceModelForUriOrTel(String uriOrTel);

    /**
      * Check that the given friend is in a friend list. 
      */
    public boolean inList();

    /**
      * Removes an address in this friend. 
      */
    public void removeAddress(Address addr);

    /**
      * Removes a phone number in this friend. 
      */
    public void removePhoneNumber(String phone);

    /**
      * Saves a friend either in database if configured, otherwise in linphonerc. 
      */
    public void save(Core lc);

    /**
      * Set the presence model for a specific SIP URI or phone number of a friend. 
      */
    public void setPresenceModelForUriOrTel(String uriOrTel, PresenceModel presence);

    /**
      * Contructor same as linphone_friend_new + linphone_friend_set_address() 
      */
    public Friend newFromVcard(Vcard vcard);

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class FriendImpl implements Friend {

    protected long nativePtr = 0;
    protected Object userData = null;
    protected Core core = null;

    protected FriendImpl(long ptr) {
        nativePtr = ptr;
        core = getCore();
    }


    private native Address getAddress(long nativePtr);
    @Override
    synchronized public Address getAddress()  {
        synchronized(core) { 
        return (Address)getAddress(nativePtr);
        }
    }

    private native int setAddress(long nativePtr, Address address);
    @Override
    synchronized public void setAddress(Address address)  {
        synchronized(core) { 
        setAddress(nativePtr, address);
        }
    }

    private native Address[] getAddresses(long nativePtr);
    @Override
    synchronized public Address[] getAddresses()  {
        synchronized(core) { 
        return getAddresses(nativePtr);
        }
    }

    private native int getConsolidatedPresence(long nativePtr);
    @Override
    synchronized public ConsolidatedPresence getConsolidatedPresence()  {
        synchronized(core) { 
        return ConsolidatedPresence.fromInt(getConsolidatedPresence(nativePtr));
        }
    }

    private native Core getCore(long nativePtr);
    @Override
    synchronized public Core getCore()  {
        
        return (Core)getCore(nativePtr);
    }

    private native int getIncSubscribePolicy(long nativePtr);
    @Override
    synchronized public SubscribePolicy getIncSubscribePolicy()  {
        synchronized(core) { 
        return SubscribePolicy.fromInt(getIncSubscribePolicy(nativePtr));
        }
    }

    private native int setIncSubscribePolicy(long nativePtr, int pol);
    @Override
    synchronized public void setIncSubscribePolicy(SubscribePolicy pol)  {
        synchronized(core) { 
        setIncSubscribePolicy(nativePtr, pol.toInt());
        }
    }

    private native boolean isPresenceReceived(long nativePtr);
    @Override
    synchronized public boolean isPresenceReceived()  {
        synchronized(core) { 
        return isPresenceReceived(nativePtr);
        }
    }

    private native String getName(long nativePtr);
    @Override
    synchronized public String getName()  {
        synchronized(core) { 
        return getName(nativePtr);
        }
    }

    private native int setName(long nativePtr, String name);
    @Override
    synchronized public void setName(String name)  {
        synchronized(core) { 
        setName(nativePtr, name);
        }
    }

    private native String[] getPhoneNumbers(long nativePtr);
    @Override
    synchronized public String[] getPhoneNumbers()  {
        synchronized(core) { 
        return getPhoneNumbers(nativePtr);
        }
    }

    private native PresenceModel getPresenceModel(long nativePtr);
    @Override
    synchronized public PresenceModel getPresenceModel()  {
        synchronized(core) { 
        return (PresenceModel)getPresenceModel(nativePtr);
        }
    }

    private native void setPresenceModel(long nativePtr, PresenceModel presence);
    @Override
    synchronized public void setPresenceModel(PresenceModel presence)  {
        synchronized(core) { 
        setPresenceModel(nativePtr, presence);
        }
    }

    private native String getRefKey(long nativePtr);
    @Override
    synchronized public String getRefKey()  {
        synchronized(core) { 
        return getRefKey(nativePtr);
        }
    }

    private native void setRefKey(long nativePtr, String key);
    @Override
    synchronized public void setRefKey(String key)  {
        synchronized(core) { 
        setRefKey(nativePtr, key);
        }
    }

    private native boolean subscribesEnabled(long nativePtr);
    @Override
    synchronized public boolean subscribesEnabled()  {
        synchronized(core) { 
        return subscribesEnabled(nativePtr);
        }
    }

    private native int enableSubscribes(long nativePtr, boolean val);
    @Override
    synchronized public void enableSubscribes(boolean val)  {
        synchronized(core) { 
        enableSubscribes(nativePtr, val);
        }
    }

    private native int getSubscriptionState(long nativePtr);
    @Override
    synchronized public SubscriptionState getSubscriptionState()  {
        synchronized(core) { 
        return SubscriptionState.fromInt(getSubscriptionState(nativePtr));
        }
    }

    private native Vcard getVcard(long nativePtr);
    @Override
    synchronized public Vcard getVcard()  {
        synchronized(core) { 
        return (Vcard)getVcard(nativePtr);
        }
    }

    private native void setVcard(long nativePtr, Vcard vcard);
    @Override
    synchronized public void setVcard(Vcard vcard)  {
        synchronized(core) { 
        setVcard(nativePtr, vcard);
        }
    }

    private native void addAddress(long nativePtr, Address addr);
    @Override
    synchronized public void addAddress(Address addr)  {
        synchronized(core) { 
        addAddress(nativePtr, addr);
        }
    }

    private native void addPhoneNumber(long nativePtr, String phone);
    @Override
    synchronized public void addPhoneNumber(String phone)  {
        synchronized(core) { 
        addPhoneNumber(nativePtr, phone);
        }
    }

    private native boolean createVcard(long nativePtr, String name);
    @Override
    synchronized public boolean createVcard(String name)  {
        synchronized(core) { 
        return createVcard(nativePtr, name);
        }
    }

    private native void done(long nativePtr);
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

    private native PresenceModel getPresenceModelForUriOrTel(long nativePtr, String uriOrTel);
    @Override
    synchronized public PresenceModel getPresenceModelForUriOrTel(String uriOrTel)  {
        synchronized(core) { 
        return (PresenceModel)getPresenceModelForUriOrTel(nativePtr, uriOrTel);
        }
    }

    private native boolean inList(long nativePtr);
    @Override
    synchronized public boolean inList()  {
        synchronized(core) { 
        return inList(nativePtr);
        }
    }

    private native void removeAddress(long nativePtr, Address addr);
    @Override
    synchronized public void removeAddress(Address addr)  {
        synchronized(core) { 
        removeAddress(nativePtr, addr);
        }
    }

    private native void removePhoneNumber(long nativePtr, String phone);
    @Override
    synchronized public void removePhoneNumber(String phone)  {
        synchronized(core) { 
        removePhoneNumber(nativePtr, phone);
        }
    }

    private native void save(long nativePtr, Core lc);
    @Override
    synchronized public void save(Core lc)  {
        synchronized(core) { 
        save(nativePtr, lc);
        }
    }

    private native void setPresenceModelForUriOrTel(long nativePtr, String uriOrTel, PresenceModel presence);
    @Override
    synchronized public void setPresenceModelForUriOrTel(String uriOrTel, PresenceModel presence)  {
        synchronized(core) { 
        setPresenceModelForUriOrTel(nativePtr, uriOrTel, presence);
        }
    }

    private native Friend newFromVcard(long nativePtr, Vcard vcard);
    @Override
    synchronized public Friend newFromVcard(Vcard vcard)  {
        synchronized(core) { 
        return (Friend)newFromVcard(nativePtr, vcard);
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
