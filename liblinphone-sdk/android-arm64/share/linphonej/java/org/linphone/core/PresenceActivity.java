/*
PresenceActivity.java
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
  * Presence activity type holding information about a presence activity. 
  */
public interface PresenceActivity {
    enum Type {
        /**
        * The person has a calendar appointment, without specifying exactly of what type. 
        */
        Appointment(0),

        /**
        * The person is physically away from all interactive communication devices. 
        */
        Away(1),

        /**
        * The person is eating the first meal of the day, usually eaten in the morning. 
        */
        Breakfast(2),

        /**
        * The person is busy, without further details. 
        */
        Busy(3),

        /**
        * The person is having his or her main meal of the day, eaten in the evening or
        * at midday. 
        */
        Dinner(4),

        /**
        * This is a scheduled national or local holiday. 
        */
        Holiday(5),

        /**
        * The person is riding in a vehicle, such as a car, but not steering. 
        */
        InTransit(6),

        /**
        * The person is looking for (paid) work. 
        */
        LookingForWork(7),

        /**
        * The person is eating his or her midday meal. 
        */
        Lunch(8),

        /**
        * The person is scheduled for a meal, without specifying whether it is breakfast,
        * lunch, or dinner, or some other meal. 
        */
        Meal(9),

        /**
        * The person is in an assembly or gathering of people, as for a business, social,
        * or religious purpose. 
        */
        Meeting(10),

        /**
        * The person is talking on the telephone. 
        */
        OnThePhone(11),

        /**
        * The person is engaged in an activity with no defined representation. 
        */
        Other(12),

        /**
        * A performance is a sub-class of an appointment and includes musical,
        * theatrical, and cinematic performances as well as lectures. 
        */
        Performance(13),

        /**
        * The person will not return for the foreseeable future, e.g., because it is no
        * longer working for the company. 
        */
        PermanentAbsence(14),

        /**
        * The person is occupying himself or herself in amusement, sport, or other
        * recreation. 
        */
        Playing(15),

        /**
        * The person is giving a presentation, lecture, or participating in a formal
        * round-table discussion. 
        */
        Presentation(16),

        /**
        * The person is visiting stores in search of goods or services. 
        */
        Shopping(17),

        /**
        * The person is sleeping. 
        */
        Sleeping(18),

        /**
        * The person is observing an event, such as a sports event. 
        */
        Spectator(19),

        /**
        * The person is controlling a vehicle, watercraft, or plane. 
        */
        Steering(20),

        /**
        * The person is on a business or personal trip, but not necessarily in-transit. 
        */
        Travel(21),

        /**
        * The person is watching television. 
        */
        TV(22),

        /**
        * The activity of the person is unknown. 
        */
        Unknown(23),

        /**
        * A period of time devoted to pleasure, rest, or relaxation. 
        */
        Vacation(24),

        /**
        * The person is engaged in, typically paid, labor, as part of a profession or
        * job. 
        */
        Working(25),

        /**
        * The person is participating in religious rites. 
        */
        Worship(26);

        protected final int mValue;

        private Type (int value) {
            mValue = value;
        }

        static public Type fromInt(int value) throws RuntimeException {
            switch(value) {
            case 0: return Appointment;
            case 1: return Away;
            case 2: return Breakfast;
            case 3: return Busy;
            case 4: return Dinner;
            case 5: return Holiday;
            case 6: return InTransit;
            case 7: return LookingForWork;
            case 8: return Lunch;
            case 9: return Meal;
            case 10: return Meeting;
            case 11: return OnThePhone;
            case 12: return Other;
            case 13: return Performance;
            case 14: return PermanentAbsence;
            case 15: return Playing;
            case 16: return Presentation;
            case 17: return Shopping;
            case 18: return Sleeping;
            case 19: return Spectator;
            case 20: return Steering;
            case 21: return Travel;
            case 22: return TV;
            case 23: return Unknown;
            case 24: return Vacation;
            case 25: return Working;
            case 26: return Worship;
            default:
                throw new RuntimeException("Unhandled enum value " + value + " for Type");
            }
        }

        public int toInt() {
            return mValue;
        }
    };

    /**
      * Gets the description of a presence activity. 
      */
    public String getDescription();

    /**
      * Sets the description of a presence activity. 
      */
    public void setDescription(String description);

    /**
      * Gets the activity type of a presence activity. 
      */
    public PresenceActivity.Type getType();

    /**
      * Sets the type of activity of a presence activity. 
      */
    public void setType(PresenceActivity.Type acttype);

    /**
      * Gets the string representation of a presence activity. 
      */
    public String toString();

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class PresenceActivityImpl implements PresenceActivity {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected PresenceActivityImpl(long ptr) {
        nativePtr = ptr;
    }


    private native String getDescription(long nativePtr);
    @Override
    synchronized public String getDescription()  {
        
        return getDescription(nativePtr);
    }

    private native int setDescription(long nativePtr, String description);
    @Override
    synchronized public void setDescription(String description)  {
        
        setDescription(nativePtr, description);
    }

    private native int getType(long nativePtr);
    @Override
    synchronized public PresenceActivity.Type getType()  {
        
        return PresenceActivity.Type.fromInt(getType(nativePtr));
    }

    private native int setType(long nativePtr, int acttype);
    @Override
    synchronized public void setType(PresenceActivity.Type acttype)  {
        
        setType(nativePtr, acttype.toInt());
    }

    private native String toString(long nativePtr);
    @Override
    synchronized public String toString()  {
        
        return toString(nativePtr);
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
