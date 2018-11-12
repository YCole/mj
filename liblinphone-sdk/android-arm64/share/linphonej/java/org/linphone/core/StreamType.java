/*
StreamType.java
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
  * Enum describing the stream types. 
  */
public enum StreamType {
    /**
      */
    Audio(0),

    /**
      */
    Video(1),

    /**
      */
    Text(2),

    /**
      */
    Unknown(3);

    protected final int mValue;

    private StreamType (int value) {
        mValue = value;
    }

    static public StreamType fromInt(int value) throws RuntimeException {
        switch(value) {
        case 0: return Audio;
        case 1: return Video;
        case 2: return Text;
        case 3: return Unknown;
        default:
            throw new RuntimeException("Unhandled enum value " + value + " for StreamType");
        }
    }

    public int toInt() {
        return mValue;
    }
}
