/*
ChatRoomCapabilities.java
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
  * LinphoneChatRoomCapabilities is used to indicated the capabilities of a chat
  * room. 
  */
public enum ChatRoomCapabilities {
    /**
      * No server. 
      */
    Basic(1<<0),

    /**
      * Supports RTT. 
      */
    RealTimeText(1<<1),

    /**
      * Use server (supports group chat) 
      */
    Conference(1<<2),

    /**
      * Special proxy chat room flag. 
      */
    Proxy(1<<3),

    /**
      * Chat room migratable from Basic to Conference. 
      */
    Migratable(1<<4),

    /**
      * A communication between two participants (can be Basic or Conference) 
      */
    OneToOne(1<<5);

    protected final int mValue;

    private ChatRoomCapabilities (int value) {
        mValue = value;
    }

    static public ChatRoomCapabilities fromInt(int value) throws RuntimeException {
        switch(value) {
        case 1<<0: return Basic;
        case 1<<1: return RealTimeText;
        case 1<<2: return Conference;
        case 1<<3: return Proxy;
        case 1<<4: return Migratable;
        case 1<<5: return OneToOne;
        default:
            throw new RuntimeException("Unhandled enum value " + value + " for ChatRoomCapabilities");
        }
    }

    public int toInt() {
        return mValue;
    }
}
