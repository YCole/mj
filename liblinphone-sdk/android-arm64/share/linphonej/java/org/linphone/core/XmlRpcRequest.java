/*
XmlRpcRequest.java
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
  * The #LinphoneXmlRpcRequest object representing a XML-RPC request to be sent. 
  */
public interface XmlRpcRequest {
    /**
      * Get the content of the XML-RPC request. 
      */
    public String getContent();

    /**
      * Get the response to an XML-RPC request sent with
      * linphone_xml_rpc_session_send_request() and returning an integer response. 
      */
    public int getIntResponse();

    /**
      * Get the status of the XML-RPC request. 
      */
    public XmlRpcStatus getStatus();

    /**
      * Get the response to an XML-RPC request sent with
      * linphone_xml_rpc_session_send_request() and returning a string response. 
      */
    public String getStringResponse();

    /**
      * Add an integer argument to an XML-RPC request. 
      */
    public void addIntArg(int value);

    /**
      * Add a string argument to an XML-RPC request. 
      */
    public void addStringArg(String value);

    public void setListener(XmlRpcRequestListener listener);

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class XmlRpcRequestImpl implements XmlRpcRequest {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected XmlRpcRequestImpl(long ptr) {
        nativePtr = ptr;
    }


    private native String getContent(long nativePtr);
    @Override
    synchronized public String getContent()  {
        
        return getContent(nativePtr);
    }

    private native int getIntResponse(long nativePtr);
    @Override
    synchronized public int getIntResponse()  {
        
        return getIntResponse(nativePtr);
    }

    private native int getStatus(long nativePtr);
    @Override
    synchronized public XmlRpcStatus getStatus()  {
        
        return XmlRpcStatus.fromInt(getStatus(nativePtr));
    }

    private native String getStringResponse(long nativePtr);
    @Override
    synchronized public String getStringResponse()  {
        
        return getStringResponse(nativePtr);
    }

    private native void addIntArg(long nativePtr, int value);
    @Override
    synchronized public void addIntArg(int value)  {
        
        addIntArg(nativePtr, value);
    }

    private native void addStringArg(long nativePtr, String value);
    @Override
    synchronized public void addStringArg(String value)  {
        
        addStringArg(nativePtr, value);
    }

    private native void setListener(long nativePtr, XmlRpcRequestListener listener);
    @Override
    synchronized public void setListener(XmlRpcRequestListener listener)  {
        
        setListener(nativePtr, listener);
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
