/*
PresenceService.java
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
  * Presence service type holding information about a presence service. 
  */
public interface PresenceService {
    /**
      * Gets the basic status of a presence service. 
      */
    public PresenceBasicStatus getBasicStatus();

    /**
      * Sets the basic status of a presence service. 
      */
    public void setBasicStatus(PresenceBasicStatus basicStatus);

    /**
      * Gets the contact of a presence service. 
      */
    public String getContact();

    /**
      * Sets the contact of a presence service. 
      */
    public void setContact(String contact);

    /**
      * Gets the id of a presence service. 
      */
    public String getId();

    /**
      * Sets the id of a presence service. 
      */
    public void setId(String id);

    /**
      * Gets the number of notes included in the presence service. 
      */
    public int getNbNotes();

    /**
      * Adds a note to a presence service. 
      */
    public void addNote(PresenceNote note);

    /**
      * Clears the notes of a presence service. 
      */
    public void clearNotes();

    /**
      * Gets the nth note of a presence service. 
      */
    public PresenceNote getNthNote(int idx);

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class PresenceServiceImpl implements PresenceService {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected PresenceServiceImpl(long ptr) {
        nativePtr = ptr;
    }


    private native int getBasicStatus(long nativePtr);
    @Override
    synchronized public PresenceBasicStatus getBasicStatus()  {
        
        return PresenceBasicStatus.fromInt(getBasicStatus(nativePtr));
    }

    private native int setBasicStatus(long nativePtr, int basicStatus);
    @Override
    synchronized public void setBasicStatus(PresenceBasicStatus basicStatus)  {
        
        setBasicStatus(nativePtr, basicStatus.toInt());
    }

    private native String getContact(long nativePtr);
    @Override
    synchronized public String getContact()  {
        
        return getContact(nativePtr);
    }

    private native int setContact(long nativePtr, String contact);
    @Override
    synchronized public void setContact(String contact)  {
        
        setContact(nativePtr, contact);
    }

    private native String getId(long nativePtr);
    @Override
    synchronized public String getId()  {
        
        return getId(nativePtr);
    }

    private native int setId(long nativePtr, String id);
    @Override
    synchronized public void setId(String id)  {
        
        setId(nativePtr, id);
    }

    private native int getNbNotes(long nativePtr);
    @Override
    synchronized public int getNbNotes()  {
        
        return getNbNotes(nativePtr);
    }

    private native int addNote(long nativePtr, PresenceNote note);
    @Override
    synchronized public void addNote(PresenceNote note)  {
        
        addNote(nativePtr, note);
    }

    private native int clearNotes(long nativePtr);
    @Override
    synchronized public void clearNotes()  {
        
        clearNotes(nativePtr);
    }

    private native PresenceNote getNthNote(long nativePtr, int idx);
    @Override
    synchronized public PresenceNote getNthNote(int idx)  {
        
        return (PresenceNote)getNthNote(nativePtr, idx);
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
