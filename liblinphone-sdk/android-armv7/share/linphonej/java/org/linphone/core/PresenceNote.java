/*
PresenceNote.java
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
  * Presence note type holding information about a presence note. 
  */
public interface PresenceNote {
    /**
      * Gets the content of a presence note. 
      */
    public String getContent();

    /**
      * Sets the content of a presence note. 
      */
    public void setContent(String content);

    /**
      * Gets the language of a presence note. 
      */
    public String getLang();

    /**
      * Sets the language of a presence note. 
      */
    public void setLang(String lang);

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class PresenceNoteImpl implements PresenceNote {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected PresenceNoteImpl(long ptr) {
        nativePtr = ptr;
    }


    private native String getContent(long nativePtr);
    @Override
    synchronized public String getContent()  {
        
        return getContent(nativePtr);
    }

    private native int setContent(long nativePtr, String content);
    @Override
    synchronized public void setContent(String content)  {
        
        setContent(nativePtr, content);
    }

    private native String getLang(long nativePtr);
    @Override
    synchronized public String getLang()  {
        
        return getLang(nativePtr);
    }

    private native int setLang(long nativePtr, String lang);
    @Override
    synchronized public void setLang(String lang)  {
        
        setLang(nativePtr, lang);
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
