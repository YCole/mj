/*
Vcard.java
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
  * The #LinphoneVcard object. 
  */
public interface Vcard {
    /**
      * Gets the eTag of the vCard. 
      */
    public String getEtag();

    /**
      * Sets the eTAG of the vCard. 
      */
    public void setEtag(String etag);

    /**
      * Returns the family name in the N attribute of the vCard, or NULL if it isn't
      * set yet. 
      */
    public String getFamilyName();

    /**
      * Sets the family name in the N attribute of the vCard. 
      */
    public void setFamilyName(String name);

    /**
      * Returns the FN attribute of the vCard, or NULL if it isn't set yet. 
      */
    public String getFullName();

    /**
      * Sets the FN attribute of the vCard (which is mandatory). 
      */
    public void setFullName(String name);

    /**
      * Returns the given name in the N attribute of the vCard, or NULL if it isn't set
      * yet. 
      */
    public String getGivenName();

    /**
      * Sets the given name in the N attribute of the vCard. 
      */
    public void setGivenName(String name);

    /**
      * Gets the Organization of the vCard. 
      */
    public String getOrganization();

    /**
      * Fills the Organization field of the vCard. 
      */
    public void setOrganization(String organization);

    /**
      * Returns the list of phone numbers (as string) in the vCard (all the TEL
      * attributes) or NULL. 
      */
    public String[] getPhoneNumbers();

    /**
      * Returns the list of SIP addresses (as LinphoneAddress) in the vCard (all the
      * IMPP attributes that has an URI value starting by &quot;sip:&quot;) or NULL. 
      */
    public Address[] getSipAddresses();

    /**
      * Returns the skipFieldValidation property of the vcard. 
      */
    public boolean getSkipValidation();

    /**
      * Sets the skipFieldValidation property of the vcard. 
      */
    public void setSkipValidation(boolean skip);

    /**
      * Gets the UID of the vCard. 
      */
    public String getUid();

    /**
      * Sets the unique ID of the vCard. 
      */
    public void setUid(String uid);

    /**
      * Gets the URL of the vCard. 
      */
    public String getUrl();

    /**
      * Sets the URL of the vCard. 
      */
    public void setUrl(String url);

    /**
      * Adds a phone number in the vCard, using the TEL property. 
      */
    public void addPhoneNumber(String phone);

    /**
      * Adds a SIP address in the vCard, using the IMPP property. 
      */
    public void addSipAddress(String sipAddress);

    /**
      * Returns the vCard4 representation of the LinphoneVcard. 
      */
    public String asVcard4String();

    /**
      * Clone a #LinphoneVcard. 
      */
    public Vcard clone();

    /**
      * Edits the preferred SIP address in the vCard (or the first one), using the IMPP
      * property. 
      */
    public void editMainSipAddress(String sipAddress);

    /**
      * Generates a random unique id for the vCard. 
      */
    public boolean generateUniqueId();

    /**
      * Removes a phone number in the vCard (if it exists), using the TEL property. 
      */
    public void removePhoneNumber(String phone);

    /**
      * Removes a SIP address in the vCard (if it exists), using the IMPP property. 
      */
    public void removeSipAddress(String sipAddress);

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class VcardImpl implements Vcard {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected VcardImpl(long ptr) {
        nativePtr = ptr;
    }


    private native String getEtag(long nativePtr);
    @Override
    synchronized public String getEtag()  {
        
        return getEtag(nativePtr);
    }

    private native void setEtag(long nativePtr, String etag);
    @Override
    synchronized public void setEtag(String etag)  {
        
        setEtag(nativePtr, etag);
    }

    private native String getFamilyName(long nativePtr);
    @Override
    synchronized public String getFamilyName()  {
        
        return getFamilyName(nativePtr);
    }

    private native void setFamilyName(long nativePtr, String name);
    @Override
    synchronized public void setFamilyName(String name)  {
        
        setFamilyName(nativePtr, name);
    }

    private native String getFullName(long nativePtr);
    @Override
    synchronized public String getFullName()  {
        
        return getFullName(nativePtr);
    }

    private native void setFullName(long nativePtr, String name);
    @Override
    synchronized public void setFullName(String name)  {
        
        setFullName(nativePtr, name);
    }

    private native String getGivenName(long nativePtr);
    @Override
    synchronized public String getGivenName()  {
        
        return getGivenName(nativePtr);
    }

    private native void setGivenName(long nativePtr, String name);
    @Override
    synchronized public void setGivenName(String name)  {
        
        setGivenName(nativePtr, name);
    }

    private native String getOrganization(long nativePtr);
    @Override
    synchronized public String getOrganization()  {
        
        return getOrganization(nativePtr);
    }

    private native void setOrganization(long nativePtr, String organization);
    @Override
    synchronized public void setOrganization(String organization)  {
        
        setOrganization(nativePtr, organization);
    }

    private native String[] getPhoneNumbers(long nativePtr);
    @Override
    synchronized public String[] getPhoneNumbers()  {
        
        return getPhoneNumbers(nativePtr);
    }

    private native Address[] getSipAddresses(long nativePtr);
    @Override
    synchronized public Address[] getSipAddresses()  {
        
        return getSipAddresses(nativePtr);
    }

    private native boolean getSkipValidation(long nativePtr);
    @Override
    synchronized public boolean getSkipValidation()  {
        
        return getSkipValidation(nativePtr);
    }

    private native void setSkipValidation(long nativePtr, boolean skip);
    @Override
    synchronized public void setSkipValidation(boolean skip)  {
        
        setSkipValidation(nativePtr, skip);
    }

    private native String getUid(long nativePtr);
    @Override
    synchronized public String getUid()  {
        
        return getUid(nativePtr);
    }

    private native void setUid(long nativePtr, String uid);
    @Override
    synchronized public void setUid(String uid)  {
        
        setUid(nativePtr, uid);
    }

    private native String getUrl(long nativePtr);
    @Override
    synchronized public String getUrl()  {
        
        return getUrl(nativePtr);
    }

    private native void setUrl(long nativePtr, String url);
    @Override
    synchronized public void setUrl(String url)  {
        
        setUrl(nativePtr, url);
    }

    private native void addPhoneNumber(long nativePtr, String phone);
    @Override
    synchronized public void addPhoneNumber(String phone)  {
        
        addPhoneNumber(nativePtr, phone);
    }

    private native void addSipAddress(long nativePtr, String sipAddress);
    @Override
    synchronized public void addSipAddress(String sipAddress)  {
        
        addSipAddress(nativePtr, sipAddress);
    }

    private native String asVcard4String(long nativePtr);
    @Override
    synchronized public String asVcard4String()  {
        
        return asVcard4String(nativePtr);
    }

    private native Vcard clone(long nativePtr);
    @Override
    synchronized public Vcard clone()  {
        
        return (Vcard)clone(nativePtr);
    }

    private native void editMainSipAddress(long nativePtr, String sipAddress);
    @Override
    synchronized public void editMainSipAddress(String sipAddress)  {
        
        editMainSipAddress(nativePtr, sipAddress);
    }

    private native boolean generateUniqueId(long nativePtr);
    @Override
    synchronized public boolean generateUniqueId()  {
        
        return generateUniqueId(nativePtr);
    }

    private native void removePhoneNumber(long nativePtr, String phone);
    @Override
    synchronized public void removePhoneNumber(String phone)  {
        
        removePhoneNumber(nativePtr, phone);
    }

    private native void removeSipAddress(long nativePtr, String sipAddress);
    @Override
    synchronized public void removeSipAddress(String sipAddress)  {
        
        removeSipAddress(nativePtr, sipAddress);
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
