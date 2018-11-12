/*
ToneID.java
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
  * Enum listing frequent telephony tones. 
  */
public enum ToneID {
    /**
      * Not a tone. 
      */
    Undefined(0),

    /**
      * Busy tone. 
      */
    Busy(1),

    /**
      */
    CallWaiting(2),

    /**
      * Call waiting tone. 
      */
    CallOnHold(3),

    /**
      * Call on hold tone. 
      */
    CallLost(4);

    protected final int mValue;

    private ToneID (int value) {
        mValue = value;
    }

    static public ToneID fromInt(int value) throws RuntimeException {
        switch(value) {
        case 0: return Undefined;
        case 1: return Busy;
        case 2: return CallWaiting;
        case 3: return CallOnHold;
        case 4: return CallLost;
        default:
            throw new RuntimeException("Unhandled enum value " + value + " for ToneID");
        }
    }

    public int toInt() {
        return mValue;
    }
}
