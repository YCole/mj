/*
PublishState.java
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
  * Enum for publish states. 
  */
public enum PublishState {
    /**
      * Initial state, do not use. 
      */
    None(0),

    /**
      * An outgoing publish was created and submitted. 
      */
    Progress(1),

    /**
      * Publish is accepted. 
      */
    Ok(2),

    /**
      * Publish encoutered an error, linphone_event_get_reason() gives reason code. 
      */
    Error(3),

    /**
      * Publish is about to expire, only sent if [sip]-&gt;refresh_generic_publish
      * property is set to 0. 
      */
    Expiring(4),

    /**
      * Event has been un published. 
      */
    Cleared(5);

    protected final int mValue;

    private PublishState (int value) {
        mValue = value;
    }

    static public PublishState fromInt(int value) throws RuntimeException {
        switch(value) {
        case 0: return None;
        case 1: return Progress;
        case 2: return Ok;
        case 3: return Error;
        case 4: return Expiring;
        case 5: return Cleared;
        default:
            throw new RuntimeException("Unhandled enum value " + value + " for PublishState");
        }
    }

    public int toInt() {
        return mValue;
    }
}
