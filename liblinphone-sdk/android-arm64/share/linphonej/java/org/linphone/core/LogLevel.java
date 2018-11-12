/*
LogLevel.java
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
  * Verbosity levels of log messages. 
  */
public enum LogLevel {
    /**
      * Level for debug messages. 
      */
    Debug(1),

    /**
      * Level for traces. 
      */
    Trace(1<<1),

    /**
      * Level for information messages. 
      */
    Message(1<<2),

    /**
      * Level for warning messages. 
      */
    Warning(1<<3),

    /**
      * Level for error messages. 
      */
    Error(1<<4),

    /**
      * Level for fatal error messages. 
      */
    Fatal(1<<5);

    protected final int mValue;

    private LogLevel (int value) {
        mValue = value;
    }

    static public LogLevel fromInt(int value) throws RuntimeException {
        switch(value) {
        case 1: return Debug;
        case 1<<1: return Trace;
        case 1<<2: return Message;
        case 1<<3: return Warning;
        case 1<<4: return Error;
        case 1<<5: return Fatal;
        default:
            throw new RuntimeException("Unhandled enum value " + value + " for LogLevel");
        }
    }

    public int toInt() {
        return mValue;
    }
}
