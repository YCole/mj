/*
FriendListListener.java
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
  * An object to handle the callbacks for LinphoneFriend synchronization. 
  */
public interface FriendListListener {
    /**
      * Callback used to notify a contact has been updated on the CardDAV server. 
      */
    public void onContactUpdated(FriendList list, Friend newFriend, Friend oldFriend);

    /**
      * Callback used to notify a list with all friends that have received presence
      * information. 
      */
    public void onPresenceReceived(FriendList list, Friend[] friends);

    /**
      * Callback used to notify the status of the synchronization has changed. 
      */
    public void onSyncStatusChanged(FriendList list, FriendList.SyncStatus status, String msg);

    /**
      * Callback used to notify a new contact has been created on the CardDAV server
      * and downloaded locally. 
      */
    public void onContactCreated(FriendList list, Friend lf);

    /**
      * Callback used to notify a contact has been deleted on the CardDAV server. 
      */
    public void onContactDeleted(FriendList list, Friend lf);

}