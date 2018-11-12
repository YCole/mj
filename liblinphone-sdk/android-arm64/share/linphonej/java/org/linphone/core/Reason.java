/*
Reason.java
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
  * Enum describing various failure reasons or contextual information for some
  * events. 
  */
public enum Reason {
    /**
      * No reason has been set by the core. 
      */
    None(0),

    /**
      * No response received from remote. 
      */
    NoResponse(1),

    /**
      * Authentication failed due to bad credentials or resource forbidden. 
      */
    Forbidden(2),

    /**
      * The call has been declined. 
      */
    Declined(3),

    /**
      * Destination of the call was not found. 
      */
    NotFound(4),

    /**
      * The call was not answered in time (request timeout) 
      */
    NotAnswered(5),

    /**
      * Phone line was busy. 
      */
    Busy(6),

    /**
      * Unsupported content. 
      */
    UnsupportedContent(7),

    /**
      * Transport error: connection failures, disconnections etc... 
      */
    IOError(8),

    /**
      * Do not disturb reason. 
      */
    DoNotDisturb(9),

    /**
      * Operation is unauthorized because missing credential. 
      */
    Unauthorized(10),

    /**
      * Operation is rejected due to incompatible or unsupported media parameters. 
      */
    NotAcceptable(11),

    /**
      * Operation could not be executed by server or remote client because it didn't
      * have any context for it. 
      */
    NoMatch(12),

    /**
      * Resource moved permanently. 
      */
    MovedPermanently(13),

    /**
      * Resource no longer exists. 
      */
    Gone(14),

    /**
      * Temporarily unavailable. 
      */
    TemporarilyUnavailable(15),

    /**
      * Address incomplete. 
      */
    AddressIncomplete(16),

    /**
      * Not implemented. 
      */
    NotImplemented(17),

    /**
      * Bad gateway. 
      */
    BadGateway(18),

    /**
      * Server timeout. 
      */
    ServerTimeout(19),

    /**
      * Unknown reason. 
      */
    Unknown(20);

    protected final int mValue;

    private Reason (int value) {
        mValue = value;
    }

    static public Reason fromInt(int value) throws RuntimeException {
        switch(value) {
        case 0: return None;
        case 1: return NoResponse;
        case 2: return Forbidden;
        case 3: return Declined;
        case 4: return NotFound;
        case 5: return NotAnswered;
        case 6: return Busy;
        case 7: return UnsupportedContent;
        case 8: return IOError;
        case 9: return DoNotDisturb;
        case 10: return Unauthorized;
        case 11: return NotAcceptable;
        case 12: return NoMatch;
        case 13: return MovedPermanently;
        case 14: return Gone;
        case 15: return TemporarilyUnavailable;
        case 16: return AddressIncomplete;
        case 17: return NotImplemented;
        case 18: return BadGateway;
        case 19: return ServerTimeout;
        case 20: return Unknown;
        default:
            throw new RuntimeException("Unhandled enum value " + value + " for Reason");
        }
    }

    public int toInt() {
        return mValue;
    }
}
