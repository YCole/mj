/*
GlobalState.java
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
  * LinphoneGlobalState describes the global state of the #LinphoneCore object. 
  */
public enum GlobalState {
    /**
      */
    Off(0),

    /**
      */
    Startup(1),

    /**
      */
    On(2),

    /**
      */
    Shutdown(3),

    /**
      */
    Configuring(4);

    protected final int mValue;

    private GlobalState (int value) {
        mValue = value;
    }

    static public GlobalState fromInt(int value) throws RuntimeException {
        switch(value) {
        case 0: return Off;
        case 1: return Startup;
        case 2: return On;
        case 3: return Shutdown;
        case 4: return Configuring;
        default:
            throw new RuntimeException("Unhandled enum value " + value + " for GlobalState");
        }
    }

    public int toInt() {
        return mValue;
    }
}
