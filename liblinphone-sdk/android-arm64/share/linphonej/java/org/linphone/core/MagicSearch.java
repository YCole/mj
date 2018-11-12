/*
MagicSearch.java
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
  * A #LinphoneMagicSearch is used to do specifics searchs. 
  */
public interface MagicSearch {
    /**
      */
    public String getDelimiter();

    /**
      * Set the delimiter used to find matched filter word. 
      */
    public void setDelimiter(String delimiter);

    /**
      */
    public boolean getLimitedSearch();

    /**
      * Enable or disable the limited search. 
      */
    public void setLimitedSearch(boolean limited);

    /**
      */
    public int getMaxWeight();

    /**
      * Set the maximum value used to calculate the weight in search. 
      */
    public void setMaxWeight(int weight);

    /**
      */
    public int getMinWeight();

    /**
      * Set the minimum value used to calculate the weight in search. 
      */
    public void setMinWeight(int weight);

    /**
      */
    public int getSearchLimit();

    /**
      * Set the number of the maximum SearchResult which will be return. 
      */
    public void setSearchLimit(int limit);

    /**
      */
    public boolean getUseDelimiter();

    /**
      * Enable or disable the delimiter in search. 
      */
    public void setUseDelimiter(boolean enable);

    /**
      * Create a sorted list of SearchResult from SipUri, Contact name, Contact
      * displayname, Contact phone number, which match with a filter word The last item
      * list will be an address formed with &quot;filter&quot; if a proxy config exist During the
      * first search, a cache is created and used for the next search Use
      * linphone_magic_search_reset_search_cache() to begin a new search. 
      */
    public SearchResult[] getContactListFromFilter(String filter, String domain);

    /**
      * Reset the cache to begin a new search. 
      */
    public void resetSearchCache();

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class MagicSearchImpl implements MagicSearch {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected MagicSearchImpl(long ptr) {
        nativePtr = ptr;
    }


    private native String getDelimiter(long nativePtr);
    @Override
    synchronized public String getDelimiter()  {
        
        return getDelimiter(nativePtr);
    }

    private native void setDelimiter(long nativePtr, String delimiter);
    @Override
    synchronized public void setDelimiter(String delimiter)  {
        
        setDelimiter(nativePtr, delimiter);
    }

    private native boolean getLimitedSearch(long nativePtr);
    @Override
    synchronized public boolean getLimitedSearch()  {
        
        return getLimitedSearch(nativePtr);
    }

    private native void setLimitedSearch(long nativePtr, boolean limited);
    @Override
    synchronized public void setLimitedSearch(boolean limited)  {
        
        setLimitedSearch(nativePtr, limited);
    }

    private native int getMaxWeight(long nativePtr);
    @Override
    synchronized public int getMaxWeight()  {
        
        return getMaxWeight(nativePtr);
    }

    private native void setMaxWeight(long nativePtr, int weight);
    @Override
    synchronized public void setMaxWeight(int weight)  {
        
        setMaxWeight(nativePtr, weight);
    }

    private native int getMinWeight(long nativePtr);
    @Override
    synchronized public int getMinWeight()  {
        
        return getMinWeight(nativePtr);
    }

    private native void setMinWeight(long nativePtr, int weight);
    @Override
    synchronized public void setMinWeight(int weight)  {
        
        setMinWeight(nativePtr, weight);
    }

    private native int getSearchLimit(long nativePtr);
    @Override
    synchronized public int getSearchLimit()  {
        
        return getSearchLimit(nativePtr);
    }

    private native void setSearchLimit(long nativePtr, int limit);
    @Override
    synchronized public void setSearchLimit(int limit)  {
        
        setSearchLimit(nativePtr, limit);
    }

    private native boolean getUseDelimiter(long nativePtr);
    @Override
    synchronized public boolean getUseDelimiter()  {
        
        return getUseDelimiter(nativePtr);
    }

    private native void setUseDelimiter(long nativePtr, boolean enable);
    @Override
    synchronized public void setUseDelimiter(boolean enable)  {
        
        setUseDelimiter(nativePtr, enable);
    }

    private native SearchResult[] getContactListFromFilter(long nativePtr, String filter, String domain);
    @Override
    synchronized public SearchResult[] getContactListFromFilter(String filter, String domain)  {
        
        return getContactListFromFilter(nativePtr, filter, domain);
    }

    private native void resetSearchCache(long nativePtr);
    @Override
    synchronized public void resetSearchCache()  {
        
        resetSearchCache(nativePtr);
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
