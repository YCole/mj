/*
EcCalibratorStatus.java
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
  * Enum describing the result of the echo canceller calibration process. 
  */
public enum EcCalibratorStatus {
    /**
      * The echo canceller calibration process is on going. 
      */
    InProgress(0),

    /**
      * The echo canceller calibration has been performed and produced an echo delay
      * measure. 
      */
    Done(1),

    /**
      * The echo canceller calibration process has failed. 
      */
    Failed(2),

    /**
      * The echo canceller calibration has been performed and no echo has been
      * detected. 
      */
    DoneNoEcho(3);

    protected final int mValue;

    private EcCalibratorStatus (int value) {
        mValue = value;
    }

    static public EcCalibratorStatus fromInt(int value) throws RuntimeException {
        switch(value) {
        case 0: return InProgress;
        case 1: return Done;
        case 2: return Failed;
        case 3: return DoneNoEcho;
        default:
            throw new RuntimeException("Unhandled enum value " + value + " for EcCalibratorStatus");
        }
    }

    public int toInt() {
        return mValue;
    }
}
