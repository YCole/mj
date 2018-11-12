/*
DialPlan.java
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
  * Represents a dial plan. 
  */
public interface DialPlan {
    /**
      * Returns the country name of the dialplan. 
      */
    public String getCountry();

    /**
      * Returns the country calling code of the dialplan. 
      */
    public String getCountryCallingCode();

    /**
      * Returns the international call prefix of the dialplan. 
      */
    public String getInternationalCallPrefix();

    /**
      * Return if given plan is generic. 
      */
    public boolean isGeneric();

    /**
      * Returns the iso country code of the dialplan. 
      */
    public String getIsoCountryCode();

    /**
      * Returns the national number length of the dialplan. 
      */
    public int getNationalNumberLength();

    /**
      * Find best match for given CCC. 
      */
    public DialPlan byCcc(String ccc);

    /**
      * Find best match for given CCC. 
      */
    public DialPlan byCccAsInt(int ccc);

    /**
      */
    public DialPlan[] getAllList();

    /**
      * Function to get call country code from an e164 number, ex: +33952650121 will
      * return 33. 
      */
    public int lookupCccFromE164(String e164);

    /**
      * Function to get call country code from ISO 3166-1 alpha-2 code, ex: FR returns
      * 33. 
      */
    public int lookupCccFromIso(String iso);

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class DialPlanImpl implements DialPlan {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected DialPlanImpl(long ptr) {
        nativePtr = ptr;
    }


    private native String getCountry(long nativePtr);
    @Override
    synchronized public String getCountry()  {
        
        return getCountry(nativePtr);
    }

    private native String getCountryCallingCode(long nativePtr);
    @Override
    synchronized public String getCountryCallingCode()  {
        
        return getCountryCallingCode(nativePtr);
    }

    private native String getInternationalCallPrefix(long nativePtr);
    @Override
    synchronized public String getInternationalCallPrefix()  {
        
        return getInternationalCallPrefix(nativePtr);
    }

    private native boolean isGeneric(long nativePtr);
    @Override
    synchronized public boolean isGeneric()  {
        
        return isGeneric(nativePtr);
    }

    private native String getIsoCountryCode(long nativePtr);
    @Override
    synchronized public String getIsoCountryCode()  {
        
        return getIsoCountryCode(nativePtr);
    }

    private native int getNationalNumberLength(long nativePtr);
    @Override
    synchronized public int getNationalNumberLength()  {
        
        return getNationalNumberLength(nativePtr);
    }

    private native DialPlan byCcc(long nativePtr, String ccc);
    @Override
    synchronized public DialPlan byCcc(String ccc)  {
        
        return (DialPlan)byCcc(nativePtr, ccc);
    }

    private native DialPlan byCccAsInt(long nativePtr, int ccc);
    @Override
    synchronized public DialPlan byCccAsInt(int ccc)  {
        
        return (DialPlan)byCccAsInt(nativePtr, ccc);
    }

    private native DialPlan[] getAllList(long nativePtr);
    @Override
    synchronized public DialPlan[] getAllList()  {
        
        return getAllList(nativePtr);
    }

    private native int lookupCccFromE164(long nativePtr, String e164);
    @Override
    synchronized public int lookupCccFromE164(String e164)  {
        
        return lookupCccFromE164(nativePtr, e164);
    }

    private native int lookupCccFromIso(long nativePtr, String iso);
    @Override
    synchronized public int lookupCccFromIso(String iso)  {
        
        return lookupCccFromIso(nativePtr, iso);
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
