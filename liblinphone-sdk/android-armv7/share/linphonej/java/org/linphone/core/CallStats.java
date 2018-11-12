/*
CallStats.java
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
  * The #LinphoneCallStats objects carries various statistic informations regarding
  * quality of audio or video streams. 
  */
public interface CallStats {
    /**
      * Get the bandwidth measurement of the received stream, expressed in kbit/s,
      * including IP/UDP/RTP headers. 
      */
    public float getDownloadBandwidth();

    /**
      * Get the estimated bandwidth measurement of the received stream, expressed in
      * kbit/s, including IP/UDP/RTP headers. 
      */
    public float getEstimatedDownloadBandwidth();

    /**
      * Get the state of ICE processing. 
      */
    public IceState getIceState();

    /**
      * Get the IP address family of the remote peer. 
      */
    public AddressFamily getIpFamilyOfRemote();

    /**
      * Get the jitter buffer size in ms. 
      */
    public float getJitterBufferSizeMs();

    /**
      * Gets the cumulative number of late packets. 
      */
    public int getLatePacketsCumulativeNumber();

    /**
      * Gets the local late rate since last report. 
      */
    public float getLocalLateRate();

    /**
      * Get the local loss rate since last report. 
      */
    public float getLocalLossRate();

    /**
      * Gets the remote reported interarrival jitter. 
      */
    public float getReceiverInterarrivalJitter();

    /**
      * Gets the remote reported loss rate since last report. 
      */
    public float getReceiverLossRate();

    /**
      * Get the round trip delay in s. 
      */
    public float getRoundTripDelay();

    /**
      * Get the bandwidth measurement of the received RTCP, expressed in kbit/s,
      * including IP/UDP/RTP headers. 
      */
    public float getRtcpDownloadBandwidth();

    /**
      * Get the bandwidth measurement of the sent RTCP, expressed in kbit/s, including
      * IP/UDP/RTP headers. 
      */
    public float getRtcpUploadBandwidth();

    /**
      * Gets the local interarrival jitter. 
      */
    public float getSenderInterarrivalJitter();

    /**
      * Get the local loss rate since last report. 
      */
    public float getSenderLossRate();

    /**
      * Get the type of the stream the stats refer to. 
      */
    public StreamType getType();

    /**
      * Get the bandwidth measurement of the sent stream, expressed in kbit/s,
      * including IP/UDP/RTP headers. 
      */
    public float getUploadBandwidth();

    /**
      * Get the state of uPnP processing. 
      */
    public UpnpState getUpnpState();

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class CallStatsImpl implements CallStats {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected CallStatsImpl(long ptr) {
        nativePtr = ptr;
    }


    private native float getDownloadBandwidth(long nativePtr);
    @Override
    synchronized public float getDownloadBandwidth()  {
        
        return getDownloadBandwidth(nativePtr);
    }

    private native float getEstimatedDownloadBandwidth(long nativePtr);
    @Override
    synchronized public float getEstimatedDownloadBandwidth()  {
        
        return getEstimatedDownloadBandwidth(nativePtr);
    }

    private native int getIceState(long nativePtr);
    @Override
    synchronized public IceState getIceState()  {
        
        return IceState.fromInt(getIceState(nativePtr));
    }

    private native int getIpFamilyOfRemote(long nativePtr);
    @Override
    synchronized public AddressFamily getIpFamilyOfRemote()  {
        
        return AddressFamily.fromInt(getIpFamilyOfRemote(nativePtr));
    }

    private native float getJitterBufferSizeMs(long nativePtr);
    @Override
    synchronized public float getJitterBufferSizeMs()  {
        
        return getJitterBufferSizeMs(nativePtr);
    }

    private native int getLatePacketsCumulativeNumber(long nativePtr);
    @Override
    synchronized public int getLatePacketsCumulativeNumber()  {
        
        return getLatePacketsCumulativeNumber(nativePtr);
    }

    private native float getLocalLateRate(long nativePtr);
    @Override
    synchronized public float getLocalLateRate()  {
        
        return getLocalLateRate(nativePtr);
    }

    private native float getLocalLossRate(long nativePtr);
    @Override
    synchronized public float getLocalLossRate()  {
        
        return getLocalLossRate(nativePtr);
    }

    private native float getReceiverInterarrivalJitter(long nativePtr);
    @Override
    synchronized public float getReceiverInterarrivalJitter()  {
        
        return getReceiverInterarrivalJitter(nativePtr);
    }

    private native float getReceiverLossRate(long nativePtr);
    @Override
    synchronized public float getReceiverLossRate()  {
        
        return getReceiverLossRate(nativePtr);
    }

    private native float getRoundTripDelay(long nativePtr);
    @Override
    synchronized public float getRoundTripDelay()  {
        
        return getRoundTripDelay(nativePtr);
    }

    private native float getRtcpDownloadBandwidth(long nativePtr);
    @Override
    synchronized public float getRtcpDownloadBandwidth()  {
        
        return getRtcpDownloadBandwidth(nativePtr);
    }

    private native float getRtcpUploadBandwidth(long nativePtr);
    @Override
    synchronized public float getRtcpUploadBandwidth()  {
        
        return getRtcpUploadBandwidth(nativePtr);
    }

    private native float getSenderInterarrivalJitter(long nativePtr);
    @Override
    synchronized public float getSenderInterarrivalJitter()  {
        
        return getSenderInterarrivalJitter(nativePtr);
    }

    private native float getSenderLossRate(long nativePtr);
    @Override
    synchronized public float getSenderLossRate()  {
        
        return getSenderLossRate(nativePtr);
    }

    private native int getType(long nativePtr);
    @Override
    synchronized public StreamType getType()  {
        
        return StreamType.fromInt(getType(nativePtr));
    }

    private native float getUploadBandwidth(long nativePtr);
    @Override
    synchronized public float getUploadBandwidth()  {
        
        return getUploadBandwidth(nativePtr);
    }

    private native int getUpnpState(long nativePtr);
    @Override
    synchronized public UpnpState getUpnpState()  {
        
        return UpnpState.fromInt(getUpnpState(nativePtr));
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
