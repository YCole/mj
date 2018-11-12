/*
SearchResult.java
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
  * The LinphoneSearchResult object represents a result of a search. 
  */
public interface SearchResult {
    /**
      */
    public Address getAddress();

    /**
      */
    public Friend getFriend();

    /**
      */
    public String getPhoneNumber();

    /**
      */
    public int getWeight();

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class SearchResultImpl implements SearchResult {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected SearchResultImpl(long ptr) {
        nativePtr = ptr;
    }


    private native Address getAddress(long nativePtr);
    @Override
    synchronized public Address getAddress()  {
        
        return (Address)getAddress(nativePtr);
    }

    private native Friend getFriend(long nativePtr);
    @Override
    synchronized public Friend getFriend()  {
        
        return (Friend)getFriend(nativePtr);
    }

    private native String getPhoneNumber(long nativePtr);
    @Override
    synchronized public String getPhoneNumber()  {
        
        return getPhoneNumber(nativePtr);
    }

    private native int getWeight(long nativePtr);
    @Override
    synchronized public int getWeight()  {
        
        return getWeight(nativePtr);
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
