/*
UpnpState.java
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
  * Enum describing uPnP states. 
  */
public enum UpnpState {
    /**
      * uPnP is not activate 
      */
    Idle(0),

    /**
      * uPnP process is in progress 
      */
    Pending(1),

    /**
      * Internal use: Only used by port binding. 
      */
    Adding(2),

    /**
      * Internal use: Only used by port binding. 
      */
    Removing(3),

    /**
      * uPnP is not available 
      */
    NotAvailable(4),

    /**
      * uPnP is enabled 
      */
    Ok(5),

    /**
      * uPnP processing has failed 
      */
    Ko(6),

    /**
      * IGD router is blacklisted. 
      */
    Blacklisted(7);

    protected final int mValue;

    private UpnpState (int value) {
        mValue = value;
    }

    static public UpnpState fromInt(int value) throws RuntimeException {
        switch(value) {
        case 0: return Idle;
        case 1: return Pending;
        case 2: return Adding;
        case 3: return Removing;
        case 4: return NotAvailable;
        case 5: return Ok;
        case 6: return Ko;
        case 7: return Blacklisted;
        default:
            throw new RuntimeException("Unhandled enum value " + value + " for UpnpState");
        }
    }

    public int toInt() {
        return mValue;
    }
}
