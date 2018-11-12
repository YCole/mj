/*
ConsolidatedPresence.java
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
  * Consolidated presence information: 'online' means the user is open for
  * communication, 'busy' means the user is open for communication but involved in
  * an other activity, 'do not disturb' means the user is not open for
  * communication, and 'offline' means that no presence information is available. 
  */
public enum ConsolidatedPresence {
    /**
      */
    Online(0),

    /**
      */
    Busy(1),

    /**
      */
    DoNotDisturb(2),

    /**
      */
    Offline(3);

    protected final int mValue;

    private ConsolidatedPresence (int value) {
        mValue = value;
    }

    static public ConsolidatedPresence fromInt(int value) throws RuntimeException {
        switch(value) {
        case 0: return Online;
        case 1: return Busy;
        case 2: return DoNotDisturb;
        case 3: return Offline;
        default:
            throw new RuntimeException("Unhandled enum value " + value + " for ConsolidatedPresence");
        }
    }

    public int toInt() {
        return mValue;
    }
}
