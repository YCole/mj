/*
Content.java
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
  * The LinphoneContent object holds data that can be embedded in a signaling
  * message. 
  */
public interface Content {
    /**
      * Get the content data buffer, usually a string. 
      */
    public byte[] getBuffer();

    /**
      * Get the encoding of the data buffer, for example &quot;gzip&quot;. 
      */
    public String getEncoding();

    /**
      * Set the encoding of the data buffer, for example &quot;gzip&quot;. 
      */
    public void setEncoding(String encoding);

    /**
      * Get the file transfer filepath set for this content (replace
      * linphone_chat_message_get_file_transfer_filepath). 
      */
    public String getFilePath();

    /**
      * Set the file transfer filepath for this content (replace
      * linphone_chat_message_set_file_transfer_filepath). 
      */
    public void setFilePath(String filePath);

    /**
      * Get the file size if content is either a FileContent or a FileTransferContent. 
      */
    public int getFileSize();

    /**
      * Tells whether or not this content contains a file. 
      */
    public boolean isFile();

    /**
      * Tells whether or not this content is a file transfer. 
      */
    public boolean isFileTransfer();

    /**
      * Tell whether a content is a multipart content. 
      */
    public boolean isMultipart();

    /**
      * Tells whether or not this content contains text. 
      */
    public boolean isText();

    /**
      * Get the key associated with a RCS file transfer message if encrypted. 
      */
    public String getKey();

    /**
      * Get the size of key associated with a RCS file transfer message if encrypted. 
      */
    public int getKeySize();

    /**
      * Get the name associated with a RCS file transfer message. 
      */
    public String getName();

    /**
      * Set the name associated with a RCS file transfer message. 
      */
    public void setName(String name);

    /**
      * Get the content data buffer size, excluding null character despite null
      * character is always set for convenience. 
      */
    public int getSize();

    /**
      * Set the content data size, excluding null character despite null character is
      * always set for convenience. 
      */
    public void setSize(int size);

    /**
      * Get the string content data buffer. 
      */
    public String getStringBuffer();

    /**
      * Set the string content data buffer. 
      */
    public void setStringBuffer(String buffer);

    /**
      * Get the mime subtype of the content data. 
      */
    public String getSubtype();

    /**
      * Set the mime subtype of the content data. 
      */
    public void setSubtype(String subtype);

    /**
      * Get the mime type of the content data. 
      */
    public String getType();

    /**
      * Set the mime type of the content data. 
      */
    public void setType(String type);

    /**
      * Adds a parameter to the ContentType header. 
      */
    public void addContentTypeParameter(String name, String value);

    /**
      * Find a part from a multipart content looking for a part header with a specified
      * value. 
      */
    public Content findPartByHeader(String headerName, String headerValue);

    /**
      * Get a custom header value of a content. 
      */
    public String getCustomHeader(String headerName);

    /**
      * Get a part from a multipart content according to its index. 
      */
    public Content getPart(int idx);

    /**
      * Set the content data buffer, usually a string. 
      */
    public void setBuffer(byte[] buffer, int size);

    /**
      * Set the key associated with a RCS file transfer message if encrypted. 
      */
    public void setKey(String key, int keyLength);

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class ContentImpl implements Content {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected ContentImpl(long ptr) {
        nativePtr = ptr;
    }


    private native byte[] getBuffer(long nativePtr);
    @Override
    synchronized public byte[] getBuffer()  {
        
        return getBuffer(nativePtr);
    }

    private native String getEncoding(long nativePtr);
    @Override
    synchronized public String getEncoding()  {
        
        return getEncoding(nativePtr);
    }

    private native void setEncoding(long nativePtr, String encoding);
    @Override
    synchronized public void setEncoding(String encoding)  {
        
        setEncoding(nativePtr, encoding);
    }

    private native String getFilePath(long nativePtr);
    @Override
    synchronized public String getFilePath()  {
        
        return getFilePath(nativePtr);
    }

    private native void setFilePath(long nativePtr, String filePath);
    @Override
    synchronized public void setFilePath(String filePath)  {
        
        setFilePath(nativePtr, filePath);
    }

    private native int getFileSize(long nativePtr);
    @Override
    synchronized public int getFileSize()  {
        
        return getFileSize(nativePtr);
    }

    private native boolean isFile(long nativePtr);
    @Override
    synchronized public boolean isFile()  {
        
        return isFile(nativePtr);
    }

    private native boolean isFileTransfer(long nativePtr);
    @Override
    synchronized public boolean isFileTransfer()  {
        
        return isFileTransfer(nativePtr);
    }

    private native boolean isMultipart(long nativePtr);
    @Override
    synchronized public boolean isMultipart()  {
        
        return isMultipart(nativePtr);
    }

    private native boolean isText(long nativePtr);
    @Override
    synchronized public boolean isText()  {
        
        return isText(nativePtr);
    }

    private native String getKey(long nativePtr);
    @Override
    synchronized public String getKey()  {
        
        return getKey(nativePtr);
    }

    private native int getKeySize(long nativePtr);
    @Override
    synchronized public int getKeySize()  {
        
        return getKeySize(nativePtr);
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

    private native int getSize(long nativePtr);
    @Override
    synchronized public int getSize()  {
        
        return getSize(nativePtr);
    }

    private native void setSize(long nativePtr, int size);
    @Override
    synchronized public void setSize(int size)  {
        
        setSize(nativePtr, size);
    }

    private native String getStringBuffer(long nativePtr);
    @Override
    synchronized public String getStringBuffer()  {
        
        return getStringBuffer(nativePtr);
    }

    private native void setStringBuffer(long nativePtr, String buffer);
    @Override
    synchronized public void setStringBuffer(String buffer)  {
        
        setStringBuffer(nativePtr, buffer);
    }

    private native String getSubtype(long nativePtr);
    @Override
    synchronized public String getSubtype()  {
        
        return getSubtype(nativePtr);
    }

    private native void setSubtype(long nativePtr, String subtype);
    @Override
    synchronized public void setSubtype(String subtype)  {
        
        setSubtype(nativePtr, subtype);
    }

    private native String getType(long nativePtr);
    @Override
    synchronized public String getType()  {
        
        return getType(nativePtr);
    }

    private native void setType(long nativePtr, String type);
    @Override
    synchronized public void setType(String type)  {
        
        setType(nativePtr, type);
    }

    private native void addContentTypeParameter(long nativePtr, String name, String value);
    @Override
    synchronized public void addContentTypeParameter(String name, String value)  {
        
        addContentTypeParameter(nativePtr, name, value);
    }

    private native Content findPartByHeader(long nativePtr, String headerName, String headerValue);
    @Override
    synchronized public Content findPartByHeader(String headerName, String headerValue)  {
        
        return (Content)findPartByHeader(nativePtr, headerName, headerValue);
    }

    private native String getCustomHeader(long nativePtr, String headerName);
    @Override
    synchronized public String getCustomHeader(String headerName)  {
        
        return getCustomHeader(nativePtr, headerName);
    }

    private native Content getPart(long nativePtr, int idx);
    @Override
    synchronized public Content getPart(int idx)  {
        
        return (Content)getPart(nativePtr, idx);
    }

    private native void setBuffer(long nativePtr, byte[] buffer, int size);
    @Override
    synchronized public void setBuffer(byte[] buffer, int size)  {
        
        setBuffer(nativePtr, buffer, size);
    }

    private native void setKey(long nativePtr, String key, int keyLength);
    @Override
    synchronized public void setKey(String key, int keyLength)  {
        
        setKey(nativePtr, key, keyLength);
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
