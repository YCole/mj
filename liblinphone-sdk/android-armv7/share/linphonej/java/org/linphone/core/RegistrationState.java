/*
RegistrationState.java
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
  * LinphoneRegistrationState describes proxy registration states. 
  */
public enum RegistrationState {
    /**
      * Initial state for registrations. 
      */
    None(0),

    /**
      * Registration is in progress. 
      */
    Progress(1),

    /**
      * Registration is successful. 
      */
    Ok(2),

    /**
      * Unregistration succeeded. 
      */
    Cleared(3),

    /**
      * Registration failed. 
      */
    Failed(4);

    protected final int mValue;

    private RegistrationState (int value) {
        mValue = value;
    }

    static public RegistrationState fromInt(int value) throws RuntimeException {
        switch(value) {
        case 0: return None;
        case 1: return Progress;
        case 2: return Ok;
        case 3: return Cleared;
        case 4: return Failed;
        default:
            throw new RuntimeException("Unhandled enum value " + value + " for RegistrationState");
        }
    }

    public int toInt() {
        return mValue;
    }
}
