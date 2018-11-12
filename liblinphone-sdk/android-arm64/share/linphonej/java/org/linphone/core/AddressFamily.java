/*
AddressFamily.java
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
  * Enum describing Ip family. 
  */
public enum AddressFamily {
    /**
      * IpV4. 
      */
    Inet(0),

    /**
      * IpV6. 
      */
    Inet6(1),

    /**
      * Unknown. 
      */
    Unspec(2);

    protected final int mValue;

    private AddressFamily (int value) {
        mValue = value;
    }

    static public AddressFamily fromInt(int value) throws RuntimeException {
        switch(value) {
        case 0: return Inet;
        case 1: return Inet6;
        case 2: return Unspec;
        default:
            throw new RuntimeException("Unhandled enum value " + value + " for AddressFamily");
        }
    }

    public int toInt() {
        return mValue;
    }
}
