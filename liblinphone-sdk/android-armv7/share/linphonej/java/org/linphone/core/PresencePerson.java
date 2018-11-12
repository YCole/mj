/*
PresencePerson.java
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
  * Presence person holding information about a presence person. 
  */
public interface PresencePerson {
    /**
      * Gets the id of a presence person. 
      */
    public String getId();

    /**
      * Sets the id of a presence person. 
      */
    public void setId(String id);

    /**
      * Gets the number of activities included in the presence person. 
      */
    public int getNbActivities();

    /**
      * Gets the number of activities notes included in the presence person. 
      */
    public int getNbActivitiesNotes();

    /**
      * Gets the number of notes included in the presence person. 
      */
    public int getNbNotes();

    /**
      * Adds an activities note to a presence person. 
      */
    public void addActivitiesNote(PresenceNote note);

    /**
      * Adds an activity to a presence person. 
      */
    public void addActivity(PresenceActivity activity);

    /**
      * Adds a note to a presence person. 
      */
    public void addNote(PresenceNote note);

    /**
      * Clears the activities of a presence person. 
      */
    public void clearActivities();

    /**
      * Clears the activities notes of a presence person. 
      */
    public void clearActivitiesNotes();

    /**
      * Clears the notes of a presence person. 
      */
    public void clearNotes();

    /**
      * Gets the nth activities note of a presence person. 
      */
    public PresenceNote getNthActivitiesNote(int idx);

    /**
      * Gets the nth activity of a presence person. 
      */
    public PresenceActivity getNthActivity(int idx);

    /**
      * Gets the nth note of a presence person. 
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

class PresencePersonImpl implements PresencePerson {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected PresencePersonImpl(long ptr) {
        nativePtr = ptr;
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

    private native int getNbActivities(long nativePtr);
    @Override
    synchronized public int getNbActivities()  {
        
        return getNbActivities(nativePtr);
    }

    private native int getNbActivitiesNotes(long nativePtr);
    @Override
    synchronized public int getNbActivitiesNotes()  {
        
        return getNbActivitiesNotes(nativePtr);
    }

    private native int getNbNotes(long nativePtr);
    @Override
    synchronized public int getNbNotes()  {
        
        return getNbNotes(nativePtr);
    }

    private native int addActivitiesNote(long nativePtr, PresenceNote note);
    @Override
    synchronized public void addActivitiesNote(PresenceNote note)  {
        
        addActivitiesNote(nativePtr, note);
    }

    private native int addActivity(long nativePtr, PresenceActivity activity);
    @Override
    synchronized public void addActivity(PresenceActivity activity)  {
        
        addActivity(nativePtr, activity);
    }

    private native int addNote(long nativePtr, PresenceNote note);
    @Override
    synchronized public void addNote(PresenceNote note)  {
        
        addNote(nativePtr, note);
    }

    private native int clearActivities(long nativePtr);
    @Override
    synchronized public void clearActivities()  {
        
        clearActivities(nativePtr);
    }

    private native int clearActivitiesNotes(long nativePtr);
    @Override
    synchronized public void clearActivitiesNotes()  {
        
        clearActivitiesNotes(nativePtr);
    }

    private native int clearNotes(long nativePtr);
    @Override
    synchronized public void clearNotes()  {
        
        clearNotes(nativePtr);
    }

    private native PresenceNote getNthActivitiesNote(long nativePtr, int idx);
    @Override
    synchronized public PresenceNote getNthActivitiesNote(int idx)  {
        
        return (PresenceNote)getNthActivitiesNote(nativePtr, idx);
    }

    private native PresenceActivity getNthActivity(long nativePtr, int idx);
    @Override
    synchronized public PresenceActivity getNthActivity(int idx)  {
        
        return (PresenceActivity)getNthActivity(nativePtr, idx);
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
