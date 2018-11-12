/*
Range.java
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
  * Structure describing a range of integers. 
  */
public interface Range {
    /**
      * Gets the higher value of the range. 
      */
    public int getMax();

    /**
      * Sets the higher value of the range. 
      */
    public void setMax(int max);

    /**
      * Gets the lower value of the range. 
      */
    public int getMin();

    /**
      * Sets the lower value of the range. 
      */
    public void setMin(int min);

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class RangeImpl implements Range {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected RangeImpl(long ptr) {
        nativePtr = ptr;
    }


    private native int getMax(long nativePtr);
    @Override
    synchronized public int getMax()  {
        
        return getMax(nativePtr);
    }

    private native void setMax(long nativePtr, int max);
    @Override
    synchronized public void setMax(int max)  {
        
        setMax(nativePtr, max);
    }

    private native int getMin(long nativePtr);
    @Override
    synchronized public int getMin()  {
        
        return getMin(nativePtr);
    }

    private native void setMin(long nativePtr, int min);
    @Override
    synchronized public void setMin(int min)  {
        
        setMin(nativePtr, min);
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
