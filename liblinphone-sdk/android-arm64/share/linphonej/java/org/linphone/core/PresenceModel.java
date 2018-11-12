/*
PresenceModel.java
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
  * Presence model type holding information about the presence of a person. 
  */
public interface PresenceModel {
    /**
      * Gets the first activity of a presence model (there is usually only one). 
      */
    public PresenceActivity getActivity();

    /**
      * Gets the basic status of a presence model. 
      */
    public PresenceBasicStatus getBasicStatus();

    /**
      * Sets the basic status of a presence model. 
      */
    public void setBasicStatus(PresenceBasicStatus basicStatus);

    /**
      * Get the consolidated presence from a presence model. 
      */
    public ConsolidatedPresence getConsolidatedPresence();

    /**
      * Gets the contact of a presence model. 
      */
    public String getContact();

    /**
      * Sets the contact of a presence model. 
      */
    public void setContact(String contact);

    /**
      * Tells whether a presence model is considered online. 
      */
    public boolean isOnline();

    /**
      * Gets the number of activities included in the presence model. 
      */
    public int getNbActivities();

    /**
      * Gets the number of persons included in the presence model. 
      */
    public int getNbPersons();

    /**
      * Gets the number of services included in the presence model. 
      */
    public int getNbServices();

    /**
      * Gets the presentity of a presence model. 
      */
    public Address getPresentity();

    /**
      * Sets the presentity of a presence model. 
      */
    public void setPresentity(Address presentity);

    /**
      * Gets the timestamp of a presence model. 
      */
    public long getTimestamp();

    /**
      * Adds an activity to a presence model. 
      */
    public void addActivity(PresenceActivity activity);

    /**
      * Adds a note to a presence model. 
      */
    public void addNote(String noteContent, String lang);

    /**
      * Adds a person to a presence model. 
      */
    public void addPerson(PresencePerson person);

    /**
      * Adds a service to a presence model. 
      */
    public void addService(PresenceService service);

    /**
      * Clears the activities of a presence model. 
      */
    public void clearActivities();

    /**
      * Clears all the notes of a presence model. 
      */
    public void clearNotes();

    /**
      * Clears the persons of a presence model. 
      */
    public void clearPersons();

    /**
      * Clears the services of a presence model. 
      */
    public void clearServices();

    /**
      * Gets the first note of a presence model (there is usually only one). 
      */
    public PresenceNote getNote(String lang);

    /**
      * Gets the nth activity of a presence model. 
      */
    public PresenceActivity getNthActivity(int idx);

    /**
      * Gets the nth person of a presence model. 
      */
    public PresencePerson getNthPerson(int idx);

    /**
      * Gets the nth service of a presence model. 
      */
    public PresenceService getNthService(int idx);

    /**
      * Sets the activity of a presence model (limits to only one activity). 
      */
    public void setActivity(PresenceActivity.Type activity, String description);

    /**
      * Creates a presence model specifying an activity. 
      */
    public PresenceModel newWithActivity(PresenceActivity.Type activity, String description);

    /**
      * Creates a presence model specifying an activity and adding a note. 
      */
    public PresenceModel newWithActivityAndNote(PresenceActivity.Type activity, String description, String note, String lang);

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class PresenceModelImpl implements PresenceModel {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected PresenceModelImpl(long ptr) {
        nativePtr = ptr;
    }


    private native PresenceActivity getActivity(long nativePtr);
    @Override
    synchronized public PresenceActivity getActivity()  {
        
        return (PresenceActivity)getActivity(nativePtr);
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

    private native int getConsolidatedPresence(long nativePtr);
    @Override
    synchronized public ConsolidatedPresence getConsolidatedPresence()  {
        
        return ConsolidatedPresence.fromInt(getConsolidatedPresence(nativePtr));
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

    private native boolean isOnline(long nativePtr);
    @Override
    synchronized public boolean isOnline()  {
        
        return isOnline(nativePtr);
    }

    private native int getNbActivities(long nativePtr);
    @Override
    synchronized public int getNbActivities()  {
        
        return getNbActivities(nativePtr);
    }

    private native int getNbPersons(long nativePtr);
    @Override
    synchronized public int getNbPersons()  {
        
        return getNbPersons(nativePtr);
    }

    private native int getNbServices(long nativePtr);
    @Override
    synchronized public int getNbServices()  {
        
        return getNbServices(nativePtr);
    }

    private native Address getPresentity(long nativePtr);
    @Override
    synchronized public Address getPresentity()  {
        
        return (Address)getPresentity(nativePtr);
    }

    private native int setPresentity(long nativePtr, Address presentity);
    @Override
    synchronized public void setPresentity(Address presentity)  {
        
        setPresentity(nativePtr, presentity);
    }

    private native long getTimestamp(long nativePtr);
    @Override
    synchronized public long getTimestamp()  {
        
        return getTimestamp(nativePtr);
    }

    private native int addActivity(long nativePtr, PresenceActivity activity);
    @Override
    synchronized public void addActivity(PresenceActivity activity)  {
        
        addActivity(nativePtr, activity);
    }

    private native int addNote(long nativePtr, String noteContent, String lang);
    @Override
    synchronized public void addNote(String noteContent, String lang)  {
        
        addNote(nativePtr, noteContent, lang);
    }

    private native int addPerson(long nativePtr, PresencePerson person);
    @Override
    synchronized public void addPerson(PresencePerson person)  {
        
        addPerson(nativePtr, person);
    }

    private native int addService(long nativePtr, PresenceService service);
    @Override
    synchronized public void addService(PresenceService service)  {
        
        addService(nativePtr, service);
    }

    private native int clearActivities(long nativePtr);
    @Override
    synchronized public void clearActivities()  {
        
        clearActivities(nativePtr);
    }

    private native int clearNotes(long nativePtr);
    @Override
    synchronized public void clearNotes()  {
        
        clearNotes(nativePtr);
    }

    private native int clearPersons(long nativePtr);
    @Override
    synchronized public void clearPersons()  {
        
        clearPersons(nativePtr);
    }

    private native int clearServices(long nativePtr);
    @Override
    synchronized public void clearServices()  {
        
        clearServices(nativePtr);
    }

    private native PresenceNote getNote(long nativePtr, String lang);
    @Override
    synchronized public PresenceNote getNote(String lang)  {
        
        return (PresenceNote)getNote(nativePtr, lang);
    }

    private native PresenceActivity getNthActivity(long nativePtr, int idx);
    @Override
    synchronized public PresenceActivity getNthActivity(int idx)  {
        
        return (PresenceActivity)getNthActivity(nativePtr, idx);
    }

    private native PresencePerson getNthPerson(long nativePtr, int idx);
    @Override
    synchronized public PresencePerson getNthPerson(int idx)  {
        
        return (PresencePerson)getNthPerson(nativePtr, idx);
    }

    private native PresenceService getNthService(long nativePtr, int idx);
    @Override
    synchronized public PresenceService getNthService(int idx)  {
        
        return (PresenceService)getNthService(nativePtr, idx);
    }

    private native int setActivity(long nativePtr, int activity, String description);
    @Override
    synchronized public void setActivity(PresenceActivity.Type activity, String description)  {
        
        setActivity(nativePtr, activity.toInt(), description);
    }

    private native PresenceModel newWithActivity(long nativePtr, int activity, String description);
    @Override
    synchronized public PresenceModel newWithActivity(PresenceActivity.Type activity, String description)  {
        
        return (PresenceModel)newWithActivity(nativePtr, activity.toInt(), description);
    }

    private native PresenceModel newWithActivityAndNote(long nativePtr, int activity, String description, String note, String lang);
    @Override
    synchronized public PresenceModel newWithActivityAndNote(PresenceActivity.Type activity, String description, String note, String lang)  {
        
        return (PresenceModel)newWithActivityAndNote(nativePtr, activity.toInt(), description, note, lang);
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
