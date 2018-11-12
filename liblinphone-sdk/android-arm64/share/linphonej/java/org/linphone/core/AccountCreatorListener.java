/*
AccountCreatorListener.java
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
  * An object to handle the responses callbacks for handling the
  * LinphoneAccountCreator operations. 
  */
public interface AccountCreatorListener {
    /**
      * Callback to notify a response of server. 
      */
    public void onActivateAccount(AccountCreator creator, AccountCreator.Status status, String resp);

    /**
      * Callback to notify a response of server. 
      */
    public void onActivateAlias(AccountCreator creator, AccountCreator.Status status, String resp);

    /**
      * Callback to notify a response of server. 
      */
    public void onIsAccountLinked(AccountCreator creator, AccountCreator.Status status, String resp);

    /**
      * Callback to notify a response of server. 
      */
    public void onLinkAccount(AccountCreator creator, AccountCreator.Status status, String resp);

    /**
      * Callback to notify a response of server. 
      */
    public void onIsAliasUsed(AccountCreator creator, AccountCreator.Status status, String resp);

    /**
      * Callback to notify a response of server. 
      */
    public void onIsAccountActivated(AccountCreator creator, AccountCreator.Status status, String resp);

    /**
      * Callback to notify a response of server. 
      */
    public void onIsAccountExist(AccountCreator creator, AccountCreator.Status status, String resp);

    /**
      * Callback to notify a response of server. 
      */
    public void onUpdateAccount(AccountCreator creator, AccountCreator.Status status, String resp);

    /**
      * Callback to notify a response of server. 
      */
    public void onRecoverAccount(AccountCreator creator, AccountCreator.Status status, String resp);

    /**
      * Callback to notify a response of server. 
      */
    public void onCreateAccount(AccountCreator creator, AccountCreator.Status status, String resp);

}