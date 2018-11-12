/*
PresenceBasicStatus.java
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
  * Basic status as defined in section 4.1.4 of RFC 3863. 
  */
public enum PresenceBasicStatus {
    /**
      * This value means that the associated contact element, if any, is ready to
      * accept communication. 
      */
    Open(0),

    /**
      * This value means that the associated contact element, if any, is unable to
      * accept communication. 
      */
    Closed(1);

    protected final int mValue;

    private PresenceBasicStatus (int value) {
        mValue = value;
    }

    static public PresenceBasicStatus fromInt(int value) throws RuntimeException {
        switch(value) {
        case 0: return Open;
        case 1: return Closed;
        default:
            throw new RuntimeException("Unhandled enum value " + value + " for PresenceBasicStatus");
        }
    }

    public int toInt() {
        return mValue;
    }
}
