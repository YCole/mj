/*
LoggingService.java
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
  * Singleton class giving access to logging features. 
  */
public interface LoggingService {
    /**
      * Set the verbosity of the log. 
      */
    public void setLogLevel(LogLevel level);

    /**
      * Gets the log level mask. 
      */
    public int getLogLevelMask();

    /**
      * Sets the types of messages that will be authorized to be written in the log. 
      */
    public void setLogLevelMask(int mask);

    /**
      * Enables logging in a file. 
      */
    public void setLogFile(String dir, String filename, int maxSize);

    /**
      * Gets the singleton logging service object. 
      */
    public LoggingService get();

    public void setListener(LoggingServiceListener listener);

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class LoggingServiceImpl implements LoggingService {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected LoggingServiceImpl(long ptr) {
        nativePtr = ptr;
    }


    private native void setLogLevel(long nativePtr, int level);
    @Override
    synchronized public void setLogLevel(LogLevel level)  {
        
        setLogLevel(nativePtr, level.toInt());
    }

    private native int getLogLevelMask(long nativePtr);
    @Override
    synchronized public int getLogLevelMask()  {
        
        return getLogLevelMask(nativePtr);
    }

    private native void setLogLevelMask(long nativePtr, int mask);
    @Override
    synchronized public void setLogLevelMask(int mask)  {
        
        setLogLevelMask(nativePtr, mask);
    }

    private native void setLogFile(long nativePtr, String dir, String filename, int maxSize);
    @Override
    synchronized public void setLogFile(String dir, String filename, int maxSize)  {
        
        setLogFile(nativePtr, dir, filename, maxSize);
    }

    private native LoggingService get(long nativePtr);
    @Override
    synchronized public LoggingService get()  {
        
        return (LoggingService)get(nativePtr);
    }

    private native void setListener(long nativePtr, LoggingServiceListener listener);
    @Override
    synchronized public void setListener(LoggingServiceListener listener)  {
        
        setListener(nativePtr, listener);
    }

    private native void unref(long ptr);
    protected void finalize() throws Throwable {
		if (nativePtr != 0) {
			unref(nativePtr);
			nativePtr = 0;
		}
		super.finalize();
	}

    @Override
    public void setUserData(Object data) {
        userData = data;
    }

    @Override
    public Object getUserData() {
        return userData;
    }
}
