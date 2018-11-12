/*
VersionUpdateCheckResult.java
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
  * Enum describing the result of a version update check. 
  */
public enum VersionUpdateCheckResult {
    /**
      */
    UpToDate(0),

    /**
      */
    NewVersionAvailable(1),

    /**
      */
    Error(2);

    protected final int mValue;

    private VersionUpdateCheckResult (int value) {
        mValue = value;
    }

    static public VersionUpdateCheckResult fromInt(int value) throws RuntimeException {
        switch(value) {
        case 0: return UpToDate;
        case 1: return NewVersionAvailable;
        case 2: return Error;
        default:
            throw new RuntimeException("Unhandled enum value " + value + " for VersionUpdateCheckResult");
        }
    }

    public int toInt() {
        return mValue;
    }
}
