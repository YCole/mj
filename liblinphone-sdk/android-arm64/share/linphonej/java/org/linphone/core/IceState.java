/*
IceState.java
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
  * Enum describing ICE states. 
  */
public enum IceState {
    /**
      * ICE has not been activated for this call or stream. 
      */
    NotActivated(0),

    /**
      * ICE processing has failed. 
      */
    Failed(1),

    /**
      * ICE process is in progress. 
      */
    InProgress(2),

    /**
      * ICE has established a direct connection to the remote host. 
      */
    HostConnection(3),

    /**
      * ICE has established a connection to the remote host through one or several
      * NATs. 
      */
    ReflexiveConnection(4),

    /**
      * ICE has established a connection through a relay. 
      */
    RelayConnection(5);

    protected final int mValue;

    private IceState (int value) {
        mValue = value;
    }

    static public IceState fromInt(int value) throws RuntimeException {
        switch(value) {
        case 0: return NotActivated;
        case 1: return Failed;
        case 2: return InProgress;
        case 3: return HostConnection;
        case 4: return ReflexiveConnection;
        case 5: return RelayConnection;
        default:
            throw new RuntimeException("Unhandled enum value " + value + " for IceState");
        }
    }

    public int toInt() {
        return mValue;
    }
}
