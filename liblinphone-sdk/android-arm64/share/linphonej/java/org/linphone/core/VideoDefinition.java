/*
VideoDefinition.java
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
  * The #LinphoneVideoDefinition object represents a video definition, eg. 
  */
public interface VideoDefinition {
    /**
      * Get the height of the video definition. 
      */
    public int getHeight();

    /**
      * Set the height of the video definition. 
      */
    public void setHeight(int height);

    /**
      * Tells whether a #LinphoneVideoDefinition is undefined. 
      */
    public boolean isUndefined();

    /**
      * Get the name of the video definition. 
      */
    public String getName();

    /**
      * Set the name of the video definition. 
      */
    public void setName(String name);

    /**
      * Get the width of the video definition. 
      */
    public int getWidth();

    /**
      * Set the width of the video definition. 
      */
    public void setWidth(int width);

    /**
      * Clone a video definition. 
      */
    public VideoDefinition clone();

    /**
      * Tells whether two #LinphoneVideoDefinition objects are equal (the widths and
      * the heights are the same but can be switched). 
      */
    public boolean equals(VideoDefinition vdef2);

    /**
      * Set the width and the height of the video definition. 
      */
    public void setDefinition(int width, int height);

    /**
      * Tells whether two #LinphoneVideoDefinition objects are strictly equal (the
      * widths are the same and the heights are the same). 
      */
    public boolean strictEquals(VideoDefinition vdef2);

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class VideoDefinitionImpl implements VideoDefinition {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected VideoDefinitionImpl(long ptr) {
        nativePtr = ptr;
    }


    private native int getHeight(long nativePtr);
    @Override
    synchronized public int getHeight()  {
        
        return getHeight(nativePtr);
    }

    private native void setHeight(long nativePtr, int height);
    @Override
    synchronized public void setHeight(int height)  {
        
        setHeight(nativePtr, height);
    }

    private native boolean isUndefined(long nativePtr);
    @Override
    synchronized public boolean isUndefined()  {
        
        return isUndefined(nativePtr);
    }

    private native String getName(long nativePtr);
    @Override
    synchronized public String getName()  {
        
        return getName(nativePtr);
    }

    private native void setName(long nativePtr, String name);
    @Override
    synchronized public void setName(String name)  {
        
        setName(nativePtr, name);
    }

    private native int getWidth(long nativePtr);
    @Override
    synchronized public int getWidth()  {
        
        return getWidth(nativePtr);
    }

    private native void setWidth(long nativePtr, int width);
    @Override
    synchronized public void setWidth(int width)  {
        
        setWidth(nativePtr, width);
    }

    private native VideoDefinition clone(long nativePtr);
    @Override
    synchronized public VideoDefinition clone()  {
        
        return (VideoDefinition)clone(nativePtr);
    }

    private native boolean equals(long nativePtr, VideoDefinition vdef2);
    @Override
    synchronized public boolean equals(VideoDefinition vdef2)  {
        
        return equals(nativePtr, vdef2);
    }

    private native void setDefinition(long nativePtr, int width, int height);
    @Override
    synchronized public void setDefinition(int width, int height)  {
        
        setDefinition(nativePtr, width, height);
    }

    private native boolean strictEquals(long nativePtr, VideoDefinition vdef2);
    @Override
    synchronized public boolean strictEquals(VideoDefinition vdef2)  {
        
        return strictEquals(nativePtr, vdef2);
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
