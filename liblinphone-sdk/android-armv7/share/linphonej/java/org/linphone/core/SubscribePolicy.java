/*
SubscribePolicy.java
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
  * Enum controlling behavior for incoming subscription request. 
  */
public enum SubscribePolicy {
    /**
      * Does not automatically accept an incoming subscription request. 
      */
    SPWait(0),

    /**
      * Rejects incoming subscription request. 
      */
    SPDeny(1),

    /**
      * Automatically accepts a subscription request. 
      */
    SPAccept(2);

    protected final int mValue;

    private SubscribePolicy (int value) {
        mValue = value;
    }

    static public SubscribePolicy fromInt(int value) throws RuntimeException {
        switch(value) {
        case 0: return SPWait;
        case 1: return SPDeny;
        case 2: return SPAccept;
        default:
            throw new RuntimeException("Unhandled enum value " + value + " for SubscribePolicy");
        }
    }

    public int toInt() {
        return mValue;
    }
}
