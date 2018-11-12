/*
CallParams.java
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
  * The #LinphoneCallParams is an object containing various call related
  * parameters. 
  */
public interface CallParams {
    /**
      * Refine bandwidth settings for this call by setting a bandwidth limit for audio
      * streams. 
      */
    public void setAudioBandwidthLimit(int bw);

    /**
      * Get the audio stream direction. 
      */
    public MediaDirection getAudioDirection();

    /**
      * Set the audio stream direction. 
      */
    public void setAudioDirection(MediaDirection dir);

    /**
      * Tell whether audio is enabled or not. 
      */
    public boolean audioEnabled();

    /**
      * Enable audio stream. 
      */
    public void enableAudio(boolean enabled);

    /**
      * Use to get multicast state of audio stream. 
      */
    public boolean audioMulticastEnabled();

    /**
      * Use to enable multicast rtp for audio stream. 
      */
    public void enableAudioMulticast(boolean yesno);

    /**
      * Indicate whether sending of early media was enabled. 
      */
    public boolean earlyMediaSendingEnabled();

    /**
      * Enable sending of real early media (during outgoing calls). 
      */
    public void enableEarlyMediaSending(boolean enabled);

    /**
      * Tell whether the call is part of the locally managed conference. 
      */
    public boolean getLocalConferenceMode();

    /**
      * Tell whether the call has been configured in low bandwidth mode or not. 
      */
    public boolean lowBandwidthEnabled();

    /**
      * Indicate low bandwith mode. 
      */
    public void enableLowBandwidth(boolean enabled);

    /**
      * Get the kind of media encryption selected for the call. 
      */
    public MediaEncryption getMediaEncryption();

    /**
      * Set requested media encryption for a call. 
      */
    public void setMediaEncryption(MediaEncryption enc);

    /**
      * Get requested level of privacy for the call. 
      */
    public int getPrivacy();

    /**
      * Set requested level of privacy for the call. 
      */
    public void setPrivacy(int privacy);

    /**
      * Use to get real time text following rfc4103. 
      */
    public boolean realtimeTextEnabled();

    /**
      * Use to enable real time text following rfc4103. 
      */
    public void enableRealtimeText(boolean yesno);

    /**
      * Get the framerate of the video that is received. 
      */
    public float getReceivedFramerate();

    /**
      * Get the definition of the received video. 
      */
    public VideoDefinition getReceivedVideoDefinition();

    /**
      * Get the path for the audio recording of the call. 
      */
    public String getRecordFile();

    /**
      * Enable recording of the call. 
      */
    public void setRecordFile(String path);

    /**
      * Get the RTP profile being used. 
      */
    public String getRtpProfile();

    /**
      * Get the framerate of the video that is sent. 
      */
    public float getSentFramerate();

    /**
      * Get the definition of the sent video. 
      */
    public VideoDefinition getSentVideoDefinition();

    /**
      * Get the session name of the media session (ie in SDP). 
      */
    public String getSessionName();

    /**
      * Set the session name of the media session (ie in SDP). 
      */
    public void setSessionName(String name);

    /**
      * Get the audio payload type that has been selected by a call. 
      */
    public PayloadType getUsedAudioPayloadType();

    /**
      * Get the text payload type that has been selected by a call. 
      */
    public PayloadType getUsedTextPayloadType();

    /**
      * Get the video payload type that has been selected by a call. 
      */
    public PayloadType getUsedVideoPayloadType();

    /**
      * Get the video stream direction. 
      */
    public MediaDirection getVideoDirection();

    /**
      * Set the video stream direction. 
      */
    public void setVideoDirection(MediaDirection dir);

    /**
      * Tell whether video is enabled or not. 
      */
    public boolean videoEnabled();

    /**
      * Enable video stream. 
      */
    public void enableVideo(boolean enabled);

    /**
      * Use to get multicast state of video stream. 
      */
    public boolean videoMulticastEnabled();

    /**
      * Use to enable multicast rtp for video stream. 
      */
    public void enableVideoMulticast(boolean yesno);

    /**
      * Add a custom SIP header in the INVITE for a call. 
      */
    public void addCustomHeader(String headerName, String headerValue);

    /**
      * Add a custom attribute related to all the streams in the SDP exchanged within
      * SIP messages during a call. 
      */
    public void addCustomSdpAttribute(String attributeName, String attributeValue);

    /**
      * Add a custom attribute related to a specific stream in the SDP exchanged within
      * SIP messages during a call. 
      */
    public void addCustomSdpMediaAttribute(StreamType type, String attributeName, String attributeValue);

    /**
      * Clear the custom SDP attributes related to all the streams in the SDP exchanged
      * within SIP messages during a call. 
      */
    public void clearCustomSdpAttributes();

    /**
      * Clear the custom SDP attributes related to a specific stream in the SDP
      * exchanged within SIP messages during a call. 
      */
    public void clearCustomSdpMediaAttributes(StreamType type);

    /**
      * Copy an existing #LinphoneCallParams object to a new #LinphoneCallParams
      * object. 
      */
    public CallParams copy();

    /**
      * Get a custom SIP header. 
      */
    public String getCustomHeader(String headerName);

    /**
      * Get a custom SDP attribute that is related to all the streams. 
      */
    public String getCustomSdpAttribute(String attributeName);

    /**
      * Get a custom SDP attribute that is related to a specific stream. 
      */
    public String getCustomSdpMediaAttribute(StreamType type, String attributeName);

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class CallParamsImpl implements CallParams {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected CallParamsImpl(long ptr) {
        nativePtr = ptr;
    }


    private native void setAudioBandwidthLimit(long nativePtr, int bw);
    @Override
    synchronized public void setAudioBandwidthLimit(int bw)  {
        
        setAudioBandwidthLimit(nativePtr, bw);
    }

    private native int getAudioDirection(long nativePtr);
    @Override
    synchronized public MediaDirection getAudioDirection()  {
        
        return MediaDirection.fromInt(getAudioDirection(nativePtr));
    }

    private native void setAudioDirection(long nativePtr, int dir);
    @Override
    synchronized public void setAudioDirection(MediaDirection dir)  {
        
        setAudioDirection(nativePtr, dir.toInt());
    }

    private native boolean audioEnabled(long nativePtr);
    @Override
    synchronized public boolean audioEnabled()  {
        
        return audioEnabled(nativePtr);
    }

    private native void enableAudio(long nativePtr, boolean enabled);
    @Override
    synchronized public void enableAudio(boolean enabled)  {
        
        enableAudio(nativePtr, enabled);
    }

    private native boolean audioMulticastEnabled(long nativePtr);
    @Override
    synchronized public boolean audioMulticastEnabled()  {
        
        return audioMulticastEnabled(nativePtr);
    }

    private native void enableAudioMulticast(long nativePtr, boolean yesno);
    @Override
    synchronized public void enableAudioMulticast(boolean yesno)  {
        
        enableAudioMulticast(nativePtr, yesno);
    }

    private native boolean earlyMediaSendingEnabled(long nativePtr);
    @Override
    synchronized public boolean earlyMediaSendingEnabled()  {
        
        return earlyMediaSendingEnabled(nativePtr);
    }

    private native void enableEarlyMediaSending(long nativePtr, boolean enabled);
    @Override
    synchronized public void enableEarlyMediaSending(boolean enabled)  {
        
        enableEarlyMediaSending(nativePtr, enabled);
    }

    private native boolean getLocalConferenceMode(long nativePtr);
    @Override
    synchronized public boolean getLocalConferenceMode()  {
        
        return getLocalConferenceMode(nativePtr);
    }

    private native boolean lowBandwidthEnabled(long nativePtr);
    @Override
    synchronized public boolean lowBandwidthEnabled()  {
        
        return lowBandwidthEnabled(nativePtr);
    }

    private native void enableLowBandwidth(long nativePtr, boolean enabled);
    @Override
    synchronized public void enableLowBandwidth(boolean enabled)  {
        
        enableLowBandwidth(nativePtr, enabled);
    }

    private native int getMediaEncryption(long nativePtr);
    @Override
    synchronized public MediaEncryption getMediaEncryption()  {
        
        return MediaEncryption.fromInt(getMediaEncryption(nativePtr));
    }

    private native void setMediaEncryption(long nativePtr, int enc);
    @Override
    synchronized public void setMediaEncryption(MediaEncryption enc)  {
        
        setMediaEncryption(nativePtr, enc.toInt());
    }

    private native int getPrivacy(long nativePtr);
    @Override
    synchronized public int getPrivacy()  {
        
        return getPrivacy(nativePtr);
    }

    private native void setPrivacy(long nativePtr, int privacy);
    @Override
    synchronized public void setPrivacy(int privacy)  {
        
        setPrivacy(nativePtr, privacy);
    }

    private native boolean realtimeTextEnabled(long nativePtr);
    @Override
    synchronized public boolean realtimeTextEnabled()  {
        
        return realtimeTextEnabled(nativePtr);
    }

    private native int enableRealtimeText(long nativePtr, boolean yesno);
    @Override
    synchronized public void enableRealtimeText(boolean yesno)  {
        
        enableRealtimeText(nativePtr, yesno);
    }

    private native float getReceivedFramerate(long nativePtr);
    @Override
    synchronized public float getReceivedFramerate()  {
        
        return getReceivedFramerate(nativePtr);
    }

    private native VideoDefinition getReceivedVideoDefinition(long nativePtr);
    @Override
    synchronized public VideoDefinition getReceivedVideoDefinition()  {
        
        return (VideoDefinition)getReceivedVideoDefinition(nativePtr);
    }

    private native String getRecordFile(long nativePtr);
    @Override
    synchronized public String getRecordFile()  {
        
        return getRecordFile(nativePtr);
    }

    private native void setRecordFile(long nativePtr, String path);
    @Override
    synchronized public void setRecordFile(String path)  {
        
        setRecordFile(nativePtr, path);
    }

    private native String getRtpProfile(long nativePtr);
    @Override
    synchronized public String getRtpProfile()  {
        
        return getRtpProfile(nativePtr);
    }

    private native float getSentFramerate(long nativePtr);
    @Override
    synchronized public float getSentFramerate()  {
        
        return getSentFramerate(nativePtr);
    }

    private native VideoDefinition getSentVideoDefinition(long nativePtr);
    @Override
    synchronized public VideoDefinition getSentVideoDefinition()  {
        
        return (VideoDefinition)getSentVideoDefinition(nativePtr);
    }

    private native String getSessionName(long nativePtr);
    @Override
    synchronized public String getSessionName()  {
        
        return getSessionName(nativePtr);
    }

    private native void setSessionName(long nativePtr, String name);
    @Override
    synchronized public void setSessionName(String name)  {
        
        setSessionName(nativePtr, name);
    }

    private native PayloadType getUsedAudioPayloadType(long nativePtr);
    @Override
    synchronized public PayloadType getUsedAudioPayloadType()  {
        
        return (PayloadType)getUsedAudioPayloadType(nativePtr);
    }

    private native PayloadType getUsedTextPayloadType(long nativePtr);
    @Override
    synchronized public PayloadType getUsedTextPayloadType()  {
        
        return (PayloadType)getUsedTextPayloadType(nativePtr);
    }

    private native PayloadType getUsedVideoPayloadType(long nativePtr);
    @Override
    synchronized public PayloadType getUsedVideoPayloadType()  {
        
        return (PayloadType)getUsedVideoPayloadType(nativePtr);
    }

    private native int getVideoDirection(long nativePtr);
    @Override
    synchronized public MediaDirection getVideoDirection()  {
        
        return MediaDirection.fromInt(getVideoDirection(nativePtr));
    }

    private native void setVideoDirection(long nativePtr, int dir);
    @Override
    synchronized public void setVideoDirection(MediaDirection dir)  {
        
        setVideoDirection(nativePtr, dir.toInt());
    }

    private native boolean videoEnabled(long nativePtr);
    @Override
    synchronized public boolean videoEnabled()  {
        
        return videoEnabled(nativePtr);
    }

    private native void enableVideo(long nativePtr, boolean enabled);
    @Override
    synchronized public void enableVideo(boolean enabled)  {
        
        enableVideo(nativePtr, enabled);
    }

    private native boolean videoMulticastEnabled(long nativePtr);
    @Override
    synchronized public boolean videoMulticastEnabled()  {
        
        return videoMulticastEnabled(nativePtr);
    }

    private native void enableVideoMulticast(long nativePtr, boolean yesno);
    @Override
    synchronized public void enableVideoMulticast(boolean yesno)  {
        
        enableVideoMulticast(nativePtr, yesno);
    }

    private native void addCustomHeader(long nativePtr, String headerName, String headerValue);
    @Override
    synchronized public void addCustomHeader(String headerName, String headerValue)  {
        
        addCustomHeader(nativePtr, headerName, headerValue);
    }

    private native void addCustomSdpAttribute(long nativePtr, String attributeName, String attributeValue);
    @Override
    synchronized public void addCustomSdpAttribute(String attributeName, String attributeValue)  {
        
        addCustomSdpAttribute(nativePtr, attributeName, attributeValue);
    }

    private native void addCustomSdpMediaAttribute(long nativePtr, int type, String attributeName, String attributeValue);
    @Override
    synchronized public void addCustomSdpMediaAttribute(StreamType type, String attributeName, String attributeValue)  {
        
        addCustomSdpMediaAttribute(nativePtr, type.toInt(), attributeName, attributeValue);
    }

    private native void clearCustomSdpAttributes(long nativePtr);
    @Override
    synchronized public void clearCustomSdpAttributes()  {
        
        clearCustomSdpAttributes(nativePtr);
    }

    private native void clearCustomSdpMediaAttributes(long nativePtr, int type);
    @Override
    synchronized public void clearCustomSdpMediaAttributes(StreamType type)  {
        
        clearCustomSdpMediaAttributes(nativePtr, type.toInt());
    }

    private native CallParams copy(long nativePtr);
    @Override
    synchronized public CallParams copy()  {
        
        return (CallParams)copy(nativePtr);
    }

    private native String getCustomHeader(long nativePtr, String headerName);
    @Override
    synchronized public String getCustomHeader(String headerName)  {
        
        return getCustomHeader(nativePtr, headerName);
    }

    private native String getCustomSdpAttribute(long nativePtr, String attributeName);
    @Override
    synchronized public String getCustomSdpAttribute(String attributeName)  {
        
        return getCustomSdpAttribute(nativePtr, attributeName);
    }

    private native String getCustomSdpMediaAttribute(long nativePtr, int type, String attributeName);
    @Override
    synchronized public String getCustomSdpMediaAttribute(StreamType type, String attributeName)  {
        
        return getCustomSdpMediaAttribute(nativePtr, type.toInt(), attributeName);
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
