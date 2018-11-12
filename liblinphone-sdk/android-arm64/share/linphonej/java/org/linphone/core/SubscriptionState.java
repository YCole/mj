/*
SubscriptionState.java
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
  * Enum for subscription states. 
  */
public enum SubscriptionState {
    /**
      * Initial state, should not be used. 
      */
    None(0),

    /**
      * An outgoing subcription was sent. 
      */
    OutgoingProgress(1),

    /**
      * An incoming subcription is received. 
      */
    IncomingReceived(2),

    /**
      * Subscription is pending, waiting for user approval. 
      */
    Pending(3),

    /**
      * Subscription is accepted. 
      */
    Active(4),

    /**
      * Subscription is terminated normally. 
      */
    Terminated(5),

    /**
      * Subscription was terminated by an error, indicated by
      * linphone_event_get_reason() 
      */
    Error(6),

    /**
      * Subscription is about to expire, only sent if [sip]-&gt;refresh_generic_subscribe
      * property is set to 0. 
      */
    Expiring(7);

    protected final int mValue;

    private SubscriptionState (int value) {
        mValue = value;
    }

    static public SubscriptionState fromInt(int value) throws RuntimeException {
        switch(value) {
        case 0: return None;
        case 1: return OutgoingProgress;
        case 2: return IncomingReceived;
        case 3: return Pending;
        case 4: return Active;
        case 5: return Terminated;
        case 6: return Error;
        case 7: return Expiring;
        default:
            throw new RuntimeException("Unhandled enum value " + value + " for SubscriptionState");
        }
    }

    public int toInt() {
        return mValue;
    }
}
