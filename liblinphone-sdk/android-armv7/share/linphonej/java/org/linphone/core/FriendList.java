/*
FriendList.java
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
  * The #LinphoneFriendList object representing a list of friends. 
  */
public interface FriendList {
    enum SyncStatus {
        /**
        */
        Started(0),

        /**
        */
        Successful(1),

        /**
        */
        Failure(2);

        protected final int mValue;

        private SyncStatus (int value) {
            mValue = value;
        }

        static public SyncStatus fromInt(int value) throws RuntimeException {
            switch(value) {
            case 0: return Started;
            case 1: return Successful;
            case 2: return Failure;
            default:
                throw new RuntimeException("Unhandled enum value " + value + " for SyncStatus");
            }
        }

        public int toInt() {
            return mValue;
        }
    };

    enum Status {
        /**
        */
        OK(0),

        /**
        */
        NonExistentFriend(1),

        /**
        */
        InvalidFriend(2);

        protected final int mValue;

        private Status (int value) {
            mValue = value;
        }

        static public Status fromInt(int value) throws RuntimeException {
            switch(value) {
            case 0: return OK;
            case 1: return NonExistentFriend;
            case 2: return InvalidFriend;
            default:
                throw new RuntimeException("Unhandled enum value " + value + " for Status");
            }
        }

        public int toInt() {
            return mValue;
        }
    };

    /**
      * Returns the #LinphoneCore object attached to this LinphoneFriendList. 
      */
    public Core getCore();

    /**
      * Get the display name of the friend list. 
      */
    public String getDisplayName();

    /**
      * Set the display name of the friend list. 
      */
    public void setDisplayName(String displayName);

    /**
      * Retrieves the list of #LinphoneFriend from this LinphoneFriendList. 
      */
    public Friend[] getFriends();

    /**
      * Get wheter the subscription of the friend list is bodyless or not. 
      */
    public boolean isSubscriptionBodyless();

    /**
      * Get the RLS (Resource List Server) URI associated with the friend list to
      * subscribe to these friends presence. 
      */
    public Address getRlsAddress();

    /**
      * Set the RLS (Resource List Server) URI associated with the friend list to
      * subscribe to these friends presence. 
      */
    public void setRlsAddress(Address rlsAddr);

    /**
      * Get the RLS (Resource List Server) URI associated with the friend list to
      * subscribe to these friends presence. 
      */
    public String getRlsUri();

    /**
      * Set the RLS (Resource List Server) URI associated with the friend list to
      * subscribe to these friends presence. 
      */
    public void setRlsUri(String rlsUri);

    /**
      * Set wheter the subscription of the friend list is bodyless or not. 
      */
    public void setSubscriptionBodyless(boolean bodyless);

    /**
      * Gets whether subscription to NOTIFYes of all friends list are enabled or not. 
      */
    public boolean subscriptionsEnabled();

    /**
      * Enable subscription to NOTIFYes of all friends list. 
      */
    public void enableSubscriptions(boolean enabled);

    /**
      * Get the URI associated with the friend list. 
      */
    public String getUri();

    /**
      * Set the URI associated with the friend list. 
      */
    public void setUri(String uri);

    /**
      * Add a friend to a friend list. 
      */
    public FriendList.Status addFriend(Friend lf);

    /**
      * Add a friend to a friend list. 
      */
    public FriendList.Status addLocalFriend(Friend lf);

    /**
      * Creates and export #LinphoneFriend objects from #LinphoneFriendList to a file
      * using vCard 4 format. 
      */
    public void exportFriendsAsVcard4File(String vcardFile);

    /**
      * Find a friend in the friend list using a LinphoneAddress. 
      */
    public Friend findFriendByAddress(Address address);

    /**
      * Find a friend in the friend list using a ref key. 
      */
    public Friend findFriendByRefKey(String refKey);

    /**
      * Find a friend in the friend list using an URI string. 
      */
    public Friend findFriendByUri(String uri);

    /**
      * Creates and adds #LinphoneFriend objects to #LinphoneFriendList from a buffer
      * that contains the vCard(s) to parse. 
      */
    public void importFriendsFromVcard4Buffer(String vcardBuffer);

    /**
      * Creates and adds #LinphoneFriend objects to #LinphoneFriendList from a file
      * that contains the vCard(s) to parse. 
      */
    public void importFriendsFromVcard4File(String vcardFile);

    /**
      * Notify our presence to all the friends in the friend list that have subscribed
      * to our presence directly (not using a RLS). 
      */
    public void notifyPresence(PresenceModel presence);

    /**
      * Remove a friend from a friend list. 
      */
    public FriendList.Status removeFriend(Friend lf);

    /**
      * Starts a CardDAV synchronization using value set using
      * linphone_friend_list_set_uri. 
      */
    public void synchronizeFriendsFromServer();

    /**
      * Goes through all the #LinphoneFriend that are dirty and does a CardDAV PUT to
      * update the server. 
      */
    public void updateDirtyFriends();

    /**
      * Sets the revision from the last synchronization. 
      */
    public void updateRevision(int rev);

    /**
      * Update presence subscriptions for the entire list. 
      */
    public void updateSubscriptions();

    public void setListener(FriendListListener listener);

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class FriendListImpl implements FriendList {

    protected long nativePtr = 0;
    protected Object userData = null;
    protected Core core = null;

    protected FriendListImpl(long ptr) {
        nativePtr = ptr;
        core = getCore();
    }


    private native Core getCore(long nativePtr);
    @Override
    synchronized public Core getCore()  {
        
        return (Core)getCore(nativePtr);
    }

    private native String getDisplayName(long nativePtr);
    @Override
    synchronized public String getDisplayName()  {
        synchronized(core) { 
        return getDisplayName(nativePtr);
        }
    }

    private native void setDisplayName(long nativePtr, String displayName);
    @Override
    synchronized public void setDisplayName(String displayName)  {
        synchronized(core) { 
        setDisplayName(nativePtr, displayName);
        }
    }

    private native Friend[] getFriends(long nativePtr);
    @Override
    synchronized public Friend[] getFriends()  {
        synchronized(core) { 
        return getFriends(nativePtr);
        }
    }

    private native boolean isSubscriptionBodyless(long nativePtr);
    @Override
    synchronized public boolean isSubscriptionBodyless()  {
        synchronized(core) { 
        return isSubscriptionBodyless(nativePtr);
        }
    }

    private native Address getRlsAddress(long nativePtr);
    @Override
    synchronized public Address getRlsAddress()  {
        synchronized(core) { 
        return (Address)getRlsAddress(nativePtr);
        }
    }

    private native void setRlsAddress(long nativePtr, Address rlsAddr);
    @Override
    synchronized public void setRlsAddress(Address rlsAddr)  {
        synchronized(core) { 
        setRlsAddress(nativePtr, rlsAddr);
        }
    }

    private native String getRlsUri(long nativePtr);
    @Override
    synchronized public String getRlsUri()  {
        synchronized(core) { 
        return getRlsUri(nativePtr);
        }
    }

    private native void setRlsUri(long nativePtr, String rlsUri);
    @Override
    synchronized public void setRlsUri(String rlsUri)  {
        synchronized(core) { 
        setRlsUri(nativePtr, rlsUri);
        }
    }

    private native void setSubscriptionBodyless(long nativePtr, boolean bodyless);
    @Override
    synchronized public void setSubscriptionBodyless(boolean bodyless)  {
        synchronized(core) { 
        setSubscriptionBodyless(nativePtr, bodyless);
        }
    }

    private native boolean subscriptionsEnabled(long nativePtr);
    @Override
    synchronized public boolean subscriptionsEnabled()  {
        synchronized(core) { 
        return subscriptionsEnabled(nativePtr);
        }
    }

    private native void enableSubscriptions(long nativePtr, boolean enabled);
    @Override
    synchronized public void enableSubscriptions(boolean enabled)  {
        synchronized(core) { 
        enableSubscriptions(nativePtr, enabled);
        }
    }

    private native String getUri(long nativePtr);
    @Override
    synchronized public String getUri()  {
        synchronized(core) { 
        return getUri(nativePtr);
        }
    }

    private native void setUri(long nativePtr, String uri);
    @Override
    synchronized public void setUri(String uri)  {
        synchronized(core) { 
        setUri(nativePtr, uri);
        }
    }

    private native int addFriend(long nativePtr, Friend lf);
    @Override
    synchronized public FriendList.Status addFriend(Friend lf)  {
        synchronized(core) { 
        return FriendList.Status.fromInt(addFriend(nativePtr, lf));
        }
    }

    private native int addLocalFriend(long nativePtr, Friend lf);
    @Override
    synchronized public FriendList.Status addLocalFriend(Friend lf)  {
        synchronized(core) { 
        return FriendList.Status.fromInt(addLocalFriend(nativePtr, lf));
        }
    }

    private native void exportFriendsAsVcard4File(long nativePtr, String vcardFile);
    @Override
    synchronized public void exportFriendsAsVcard4File(String vcardFile)  {
        synchronized(core) { 
        exportFriendsAsVcard4File(nativePtr, vcardFile);
        }
    }

    private native Friend findFriendByAddress(long nativePtr, Address address);
    @Override
    synchronized public Friend findFriendByAddress(Address address)  {
        synchronized(core) { 
        return (Friend)findFriendByAddress(nativePtr, address);
        }
    }

    private native Friend findFriendByRefKey(long nativePtr, String refKey);
    @Override
    synchronized public Friend findFriendByRefKey(String refKey)  {
        synchronized(core) { 
        return (Friend)findFriendByRefKey(nativePtr, refKey);
        }
    }

    private native Friend findFriendByUri(long nativePtr, String uri);
    @Override
    synchronized public Friend findFriendByUri(String uri)  {
        synchronized(core) { 
        return (Friend)findFriendByUri(nativePtr, uri);
        }
    }

    private native int importFriendsFromVcard4Buffer(long nativePtr, String vcardBuffer);
    @Override
    synchronized public void importFriendsFromVcard4Buffer(String vcardBuffer)  {
        synchronized(core) { 
        importFriendsFromVcard4Buffer(nativePtr, vcardBuffer);
        }
    }

    private native int importFriendsFromVcard4File(long nativePtr, String vcardFile);
    @Override
    synchronized public void importFriendsFromVcard4File(String vcardFile)  {
        synchronized(core) { 
        importFriendsFromVcard4File(nativePtr, vcardFile);
        }
    }

    private native void notifyPresence(long nativePtr, PresenceModel presence);
    @Override
    synchronized public void notifyPresence(PresenceModel presence)  {
        synchronized(core) { 
        notifyPresence(nativePtr, presence);
        }
    }

    private native int removeFriend(long nativePtr, Friend lf);
    @Override
    synchronized public FriendList.Status removeFriend(Friend lf)  {
        synchronized(core) { 
        return FriendList.Status.fromInt(removeFriend(nativePtr, lf));
        }
    }

    private native void synchronizeFriendsFromServer(long nativePtr);
    @Override
    synchronized public void synchronizeFriendsFromServer()  {
        synchronized(core) { 
        synchronizeFriendsFromServer(nativePtr);
        }
    }

    private native void updateDirtyFriends(long nativePtr);
    @Override
    synchronized public void updateDirtyFriends()  {
        synchronized(core) { 
        updateDirtyFriends(nativePtr);
        }
    }

    private native void updateRevision(long nativePtr, int rev);
    @Override
    synchronized public void updateRevision(int rev)  {
        synchronized(core) { 
        updateRevision(nativePtr, rev);
        }
    }

    private native void updateSubscriptions(long nativePtr);
    @Override
    synchronized public void updateSubscriptions()  {
        synchronized(core) { 
        updateSubscriptions(nativePtr);
        }
    }

    private native void setListener(long nativePtr, FriendListListener listener);
    @Override
    synchronized public void setListener(FriendListListener listener)  {
        
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
