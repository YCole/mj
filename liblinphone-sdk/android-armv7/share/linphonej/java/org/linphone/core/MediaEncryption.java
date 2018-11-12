/*
MediaEncryption.java
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
  * Enum describing type of media encryption types. 
  */
public enum MediaEncryption {
    /**
      * No media encryption is used. 
      */
    None(0),

    /**
      * Use SRTP media encryption. 
      */
    SRTP(1),

    /**
      * Use ZRTP media encryption. 
      */
    ZRTP(2),

    /**
      * Use DTLS media encryption. 
      */
    DTLS(3);

    protected final int mValue;

    private MediaEncryption (int value) {
        mValue = value;
    }

    static public MediaEncryption fromInt(int value) throws RuntimeException {
        switch(value) {
        case 0: return None;
        case 1: return SRTP;
        case 2: return ZRTP;
        case 3: return DTLS;
        default:
            throw new RuntimeException("Unhandled enum value " + value + " for MediaEncryption");
        }
    }

    public int toInt() {
        return mValue;
    }
}
