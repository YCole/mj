/*
Player.java
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
  * Player interface. 
  */
public interface Player {
    enum State {
        /**
        * No file is opened for playing. 
        */
        Closed(0),

        /**
        * The player is paused. 
        */
        Paused(1),

        /**
        * The player is playing. 
        */
        Playing(2);

        protected final int mValue;

        private State (int value) {
            mValue = value;
        }

        static public State fromInt(int value) throws RuntimeException {
            switch(value) {
            case 0: return Closed;
            case 1: return Paused;
            case 2: return Playing;
            default:
                throw new RuntimeException("Unhandled enum value " + value + " for State");
            }
        }

        public int toInt() {
            return mValue;
        }
    };

    /**
      * Returns the #LinphoneCore object managing this player's call, if any. 
      */
    public Core getCore();

    /**
      * Get the current position in the opened file. 
      */
    public int getCurrentPosition();

    /**
      * Get the duration of the opened file. 
      */
    public int getDuration();

    /**
      * Get the current state of a player. 
      */
    public Player.State getState();

    /**
      * Close the opened file. 
      */
    public void close();

    /**
      * Open a file for playing. 
      */
    public void open(String filename);

    /**
      * Pause the playing of a file. 
      */
    public void pause();

    /**
      * Seek in an opened file. 
      */
    public void seek(int timeMs);

    /**
      * Start playing a file that has been opened with linphone_player_open(). 
      */
    public void start();

    public void setListener(PlayerListener listener);

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class PlayerImpl implements Player {

    protected long nativePtr = 0;
    protected Object userData = null;
    protected Core core = null;

    protected PlayerImpl(long ptr) {
        nativePtr = ptr;
        core = getCore();
    }


    private native Core getCore(long nativePtr);
    @Override
    synchronized public Core getCore()  {
        
        return (Core)getCore(nativePtr);
    }

    private native int getCurrentPosition(long nativePtr);
    @Override
    synchronized public int getCurrentPosition()  {
        synchronized(core) { 
        return getCurrentPosition(nativePtr);
        }
    }

    private native int getDuration(long nativePtr);
    @Override
    synchronized public int getDuration()  {
        synchronized(core) { 
        return getDuration(nativePtr);
        }
    }

    private native int getState(long nativePtr);
    @Override
    synchronized public Player.State getState()  {
        synchronized(core) { 
        return Player.State.fromInt(getState(nativePtr));
        }
    }

    private native void close(long nativePtr);
    @Override
    synchronized public void close()  {
        synchronized(core) { 
        close(nativePtr);
        }
    }

    private native int open(long nativePtr, String filename);
    @Override
    synchronized public void open(String filename)  {
        synchronized(core) { 
        open(nativePtr, filename);
        }
    }

    private native int pause(long nativePtr);
    @Override
    synchronized public void pause()  {
        synchronized(core) { 
        pause(nativePtr);
        }
    }

    private native int seek(long nativePtr, int timeMs);
    @Override
    synchronized public void seek(int timeMs)  {
        synchronized(core) { 
        seek(nativePtr, timeMs);
        }
    }

    private native int start(long nativePtr);
    @Override
    synchronized public void start()  {
        synchronized(core) { 
        start(nativePtr);
        }
    }

    private native void setListener(long nativePtr, PlayerListener listener);
    @Override
    synchronized public void setListener(PlayerListener listener)  {
        
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
