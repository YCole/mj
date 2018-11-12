/*
Privacy.java
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
  * Defines privacy policy to apply as described by rfc3323. 
  */
public enum Privacy {
    /**
      * Privacy services must not perform any privacy function. 
      */
    None(0),

    /**
      * Request that privacy services provide a user-level privacy function. 
      */
    User(1),

    /**
      * Request that privacy services modify headers that cannot be set arbitrarily by
      * the user (Contact/Via). 
      */
    Header(2),

    /**
      * Request that privacy services provide privacy for session media. 
      */
    Session(4),

    /**
      * rfc3325 The presence of this privacy type in a Privacy header field indicates
      * that the user would like the Network Asserted Identity to be kept private with
      * respect to SIP entities outside the Trust Domain with which the user
      * authenticated. 
      */
    Id(8),

    /**
      * Privacy service must perform the specified services or fail the request. 
      */
    Critical(16),

    /**
      * Special keyword to use privacy as defined either globally or by proxy using
      * linphone_proxy_config_set_privacy() 
      */
    Default(32768);

    protected final int mValue;

    private Privacy (int value) {
        mValue = value;
    }

    static public Privacy fromInt(int value) throws RuntimeException {
        switch(value) {
        case 0: return None;
        case 1: return User;
        case 2: return Header;
        case 4: return Session;
        case 8: return Id;
        case 16: return Critical;
        case 32768: return Default;
        default:
            throw new RuntimeException("Unhandled enum value " + value + " for Privacy");
        }
    }

    public int toInt() {
        return mValue;
    }
}
