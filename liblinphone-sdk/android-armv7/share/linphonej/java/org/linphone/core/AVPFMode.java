/*
AVPFMode.java
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
  * Enum describing RTP AVPF activation modes. 
  */
public enum AVPFMode {
    /**
      * Use default value defined at upper level. 
      */
    Default(-1),

    /**
      * AVPF is disabled. 
      */
    Disabled(0),

    /**
      * AVPF is enabled. 
      */
    Enabled(1);

    protected final int mValue;

    private AVPFMode (int value) {
        mValue = value;
    }

    static public AVPFMode fromInt(int value) throws RuntimeException {
        switch(value) {
        case -1: return Default;
        case 0: return Disabled;
        case 1: return Enabled;
        default:
            throw new RuntimeException("Unhandled enum value " + value + " for AVPFMode");
        }
    }

    public int toInt() {
        return mValue;
    }
}
