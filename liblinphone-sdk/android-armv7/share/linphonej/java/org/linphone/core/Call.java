/*
Call.java
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
  * The #LinphoneCall object represents a call issued or received by the
  * #LinphoneCore. 
  */
public interface Call {
    enum Status {
        /**
        * The call was sucessful. 
        */
        Success(0),

        /**
        * The call was aborted. 
        */
        Aborted(1),

        /**
        * The call was missed (unanswered) 
        */
        Missed(2),

        /**
        * The call was declined, either locally or by remote end. 
        */
        Declined(3),

        /**
        * The call was aborted before being advertised to the application - for protocol
        * reasons. 
        */
        EarlyAborted(4),

        /**
        * The call was answered on another device. 
        */
        AcceptedElsewhere(5),

        /**
        * The call was declined on another device. 
        */
        DeclinedElsewhere(6);

        protected final int mValue;

        private Status (int value) {
            mValue = value;
        }

        static public Status fromInt(int value) throws RuntimeException {
            switch(value) {
            case 0: return Success;
            case 1: return Aborted;
            case 2: return Missed;
            case 3: return Declined;
            case 4: return EarlyAborted;
            case 5: return AcceptedElsewhere;
            case 6: return DeclinedElsewhere;
            default:
                throw new RuntimeException("Unhandled enum value " + value + " for Status");
            }
        }

        public int toInt() {
            return mValue;
        }
    };

    enum Dir {
        /**
        * outgoing calls 
        */
        Outgoing(0),

        /**
        * incoming calls 
        */
        Incoming(1);

        protected final int mValue;

        private Dir (int value) {
            mValue = value;
        }

        static public Dir fromInt(int value) throws RuntimeException {
            switch(value) {
            case 0: return Outgoing;
            case 1: return Incoming;
            default:
                throw new RuntimeException("Unhandled enum value " + value + " for Dir");
            }
        }

        public int toInt() {
            return mValue;
        }
    };

    enum State {
        /**
        * Initial state. 
        */
        Idle(0),

        /**
        * Incoming call received. 
        */
        IncomingReceived(1),

        /**
        * Outgoing call initialized. 
        */
        OutgoingInit(2),

        /**
        * Outgoing call in progress. 
        */
        OutgoingProgress(3),

        /**
        * Outgoing call ringing. 
        */
        OutgoingRinging(4),

        /**
        * Outgoing call early media. 
        */
        OutgoingEarlyMedia(5),

        /**
        * Connected. 
        */
        Connected(6),

        /**
        * Streams running. 
        */
        StreamsRunning(7),

        /**
        * Pausing. 
        */
        Pausing(8),

        /**
        * Paused. 
        */
        Paused(9),

        /**
        * Resuming. 
        */
        Resuming(10),

        /**
        * Referred. 
        */
        Referred(11),

        /**
        * Error. 
        */
        Error(12),

        /**
        * Call end. 
        */
        End(13),

        /**
        * Paused by remote. 
        */
        PausedByRemote(14),

        /**
        * The call's parameters are updated for example when video is asked by remote. 
        */
        UpdatedByRemote(15),

        /**
        * We are proposing early media to an incoming call. 
        */
        IncomingEarlyMedia(16),

        /**
        * We have initiated a call update. 
        */
        Updating(17),

        /**
        * The call object is now released. 
        */
        Released(18),

        /**
        * The call is updated by remote while not yet answered (SIP UPDATE in early
        * dialog received) 
        */
        EarlyUpdatedByRemote(19),

        /**
        * We are updating the call while not yet answered (SIP UPDATE in early dialog
        * sent) 
        */
        EarlyUpdating(20);

        protected final int mValue;

        private State (int value) {
            mValue = value;
        }

        static public State fromInt(int value) throws RuntimeException {
            switch(value) {
            case 0: return Idle;
            case 1: return IncomingReceived;
            case 2: return OutgoingInit;
            case 3: return OutgoingProgress;
            case 4: return OutgoingRinging;
            case 5: return OutgoingEarlyMedia;
            case 6: return Connected;
            case 7: return StreamsRunning;
            case 8: return Pausing;
            case 9: return Paused;
            case 10: return Resuming;
            case 11: return Referred;
            case 12: return Error;
            case 13: return End;
            case 14: return PausedByRemote;
            case 15: return UpdatedByRemote;
            case 16: return IncomingEarlyMedia;
            case 17: return Updating;
            case 18: return Released;
            case 19: return EarlyUpdatedByRemote;
            case 20: return EarlyUpdating;
            default:
                throw new RuntimeException("Unhandled enum value " + value + " for State");
            }
        }

        public int toInt() {
            return mValue;
        }
    };

    /**
      * Returns the ZRTP authentication token to verify. 
      */
    public String getAuthenticationToken();

    /**
      * Returns whether ZRTP authentication token is verified. 
      */
    public boolean getAuthenticationTokenVerified();

    /**
      * Set the result of ZRTP short code verification by user. 
      */
    public void setAuthenticationTokenVerified(boolean verified);

    /**
      * Returns call quality averaged over all the duration of the call. 
      */
    public float getAverageQuality();

    /**
      * Gets the call log associated to this call. 
      */
    public CallLog getCallLog();

    /**
      * Returns TRUE if camera pictures are allowed to be sent to the remote party. 
      */
    public boolean cameraEnabled();

    /**
      * Indicate whether camera input should be sent to remote end. 
      */
    public void enableCamera(boolean enabled);

    /**
      * Create a new chat room for messaging from a call if not already existing, else
      * return existing one. 
      */
    public ChatRoom getChatRoom();

    /**
      * Return the associated conference object. 
      */
    public Conference getConference();

    /**
      * Get the core that has created the specified call. 
      */
    public Core getCore();

    /**
      * Returns current parameters associated to the call. 
      */
    public CallParams getCurrentParams();

    /**
      * Obtain real-time quality rating of the call. 
      */
    public float getCurrentQuality();

    /**
      * Returns direction of the call (incoming or outgoing). 
      */
    public Call.Dir getDir();

    /**
      * Returns the diversion address associated to this call. 
      */
    public Address getDiversionAddress();

    /**
      * Returns call's duration in seconds. 
      */
    public int getDuration();

    /**
      * Returns TRUE if echo cancellation is enabled. 
      */
    public boolean echoCancellationEnabled();

    /**
      * Enables or disable echo cancellation for this call. 
      */
    public void enableEchoCancellation(boolean val);

    /**
      * Returns TRUE if echo limiter is enabled. 
      */
    public boolean echoLimiterEnabled();

    /**
      * Enables or disable echo limiter for this call. 
      */
    public void enableEchoLimiter(boolean val);

    /**
      * Returns full details about call errors or termination reasons. 
      */
    public ErrorInfo getErrorInfo();

    /**
      * Get microphone volume gain. 
      */
    public float getMicrophoneVolumeGain();

    /**
      * Set microphone volume gain. 
      */
    public void setMicrophoneVolumeGain(float volume);

    /**
      * Get the native window handle of the video window, casted as an unsigned long. 
      */
    public void getNativeVideoWindowId();

    /**
      * Set the native video window id where the video is to be displayed. 
      */
    public void setNativeVideoWindowId(Object id);

    /**
      * Returns local parameters associated with the call. 
      */
    public CallParams getParams();

    /**
      * Set call parameters - advanced and not recommended feature - use with caution. 
      */
    public void setParams(CallParams params);

    /**
      * Get the mesured playback volume level (received from remote) in dbm0. 
      */
    public float getPlayVolume();

    /**
      * Get a player associated with the call to play a local file and stream it to the
      * remote peer. 
      */
    public Player getPlayer();

    /**
      * Returns the reason for a call termination (either error or normal termination) 
      */
    public Reason getReason();

    /**
      * Get the mesured record volume level (sent to remote) in dbm0. 
      */
    public float getRecordVolume();

    /**
      * Gets the refer-to uri (if the call was transfered). 
      */
    public String getReferTo();

    /**
      * Returns the remote address associated to this call. 
      */
    public Address getRemoteAddress();

    /**
      * Returns the remote address associated to this call as a string. 
      */
    public String getRemoteAddressAsString();

    /**
      * Returns the far end's sip contact as a string, if available. 
      */
    public String getRemoteContact();

    /**
      * Returns call parameters proposed by remote. 
      */
    public CallParams getRemoteParams();

    /**
      * Returns the far end's user agent description string, if available. 
      */
    public String getRemoteUserAgent();

    /**
      * Returns the call object this call is replacing, if any. 
      */
    public Call getReplacedCall();

    /**
      * Get speaker volume gain. 
      */
    public float getSpeakerVolumeGain();

    /**
      * Set speaker volume gain. 
      */
    public void setSpeakerVolumeGain(float volume);

    /**
      * Retrieves the call's current state. 
      */
    public Call.State getState();

    /**
      * Returns the number of stream for the given call. 
      */
    public int getStreamCount();

    /**
      * Returns the to address with its headers associated to this call. 
      */
    public Address getToAddress();

    /**
      * Returns the current transfer state, if a transfer has been initiated from this
      * call. 
      */
    public Call.State getTransferState();

    /**
      * When this call has received a transfer request, returns the new call that was
      * automatically created as a result of the transfer. 
      */
    public Call getTransferTargetCall();

    /**
      * Gets the transferer if this call was started automatically as a result of an
      * incoming transfer request. 
      */
    public Call getTransfererCall();

    /**
      * Accept an incoming call. 
      */
    public void accept();

    /**
      * Accept an early media session for an incoming call. 
      */
    public void acceptEarlyMedia();

    /**
      * When receiving an incoming, accept to start a media session as early-media. 
      */
    public void acceptEarlyMediaWithParams(CallParams params);

    /**
      * Accept call modifications initiated by other end. 
      */
    public void acceptUpdate(CallParams params);

    /**
      * Accept an incoming call, with parameters. 
      */
    public void acceptWithParams(CallParams params);

    /**
      * Tell whether a call has been asked to autoanswer. 
      */
    public boolean askedToAutoanswer();

    /**
      * Stop current DTMF sequence sending. 
      */
    public void cancelDtmfs();

    /**
      * Decline a pending incoming call, with a reason. 
      */
    public void decline(Reason reason);

    /**
      * Decline a pending incoming call, with a #LinphoneErrorInfo object. 
      */
    public int declineWithErrorInfo(ErrorInfo ei);

    /**
      * When receiving a #LinphoneCallUpdatedByRemote state notification, prevent
      * #LinphoneCore from performing an automatic answer. 
      */
    public void deferUpdate();

    /**
      * Return a copy of the call statistics for a particular stream type. 
      */
    public CallStats getStats(StreamType type);

    /**
      * Returns the value of the header name. 
      */
    public String getToHeader(String name);

    /**
      * Returns true if this calls has received a transfer that has not been executed
      * yet. 
      */
    public boolean hasTransferPending();

    /**
      * Indicates whether an operation is in progress at the media side. 
      */
    public boolean mediaInProgress();

    /**
      * Call generic OpenGL render for a given call. 
      */
    public void oglRender();

    /**
      * Pauses the call. 
      */
    public void pause();

    /**
      * Redirect the specified call to the given redirect URI. 
      */
    public void redirect(String redirectUri);

    /**
      * Request the callback passed to linphone_call_cbs_set_next_video_frame_decoded
      * to be called the next time the video decoder properly decodes a video frame. 
      */
    public void requestNotifyNextVideoFrameDecoded();

    /**
      * Resumes a call. 
      */
    public void resume();

    /**
      * Send the specified dtmf. 
      */
    public void sendDtmf(char dtmf);

    /**
      * Send a list of dtmf. 
      */
    public void sendDtmfs(String dtmfs);

    /**
      * Send a #LinphoneInfoMessage through an established call. 
      */
    public void sendInfoMessage(InfoMessage info);

    /**
      * Request remote side to send us a Video Fast Update. 
      */
    public void sendVfuRequest();

    /**
      * Start call recording. 
      */
    public void startRecording();

    /**
      * Stop call recording. 
      */
    public void stopRecording();

    /**
      * Take a photo of currently captured video and write it into a jpeg file. 
      */
    public void takePreviewSnapshot(String file);

    /**
      * Take a photo of currently received video and write it into a jpeg file. 
      */
    public void takeVideoSnapshot(String file);

    /**
      * Terminates a call. 
      */
    public void terminate();

    /**
      * Terminates a call. 
      */
    public void terminateWithErrorInfo(ErrorInfo ei);

    /**
      * Performs a simple call transfer to the specified destination. 
      */
    public void transfer(String referTo);

    /**
      * Transfers a call to destination of another running call. 
      */
    public void transferToAnother(Call dest);

    /**
      * Updates a running call according to supplied call parameters or parameters
      * changed in the LinphoneCore. 
      */
    public void update(CallParams params);

    /**
      * Perform a zoom of the video displayed during a call. 
      */
    public void zoom(float zoomFactor, float cx, float cy);

    public void addListener(CallListener listener);

    public void removeListener(CallListener listener);

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class CallImpl implements Call {

    protected long nativePtr = 0;
    protected Object userData = null;
    protected Core core = null;

    protected CallImpl(long ptr) {
        nativePtr = ptr;
        core = getCore();
    }


    private native String getAuthenticationToken(long nativePtr);
    @Override
    synchronized public String getAuthenticationToken()  {
        synchronized(core) { 
        return getAuthenticationToken(nativePtr);
        }
    }

    private native boolean getAuthenticationTokenVerified(long nativePtr);
    @Override
    synchronized public boolean getAuthenticationTokenVerified()  {
        synchronized(core) { 
        return getAuthenticationTokenVerified(nativePtr);
        }
    }

    private native void setAuthenticationTokenVerified(long nativePtr, boolean verified);
    @Override
    synchronized public void setAuthenticationTokenVerified(boolean verified)  {
        synchronized(core) { 
        setAuthenticationTokenVerified(nativePtr, verified);
        }
    }

    private native float getAverageQuality(long nativePtr);
    @Override
    synchronized public float getAverageQuality()  {
        synchronized(core) { 
        return getAverageQuality(nativePtr);
        }
    }

    private native CallLog getCallLog(long nativePtr);
    @Override
    synchronized public CallLog getCallLog()  {
        synchronized(core) { 
        return (CallLog)getCallLog(nativePtr);
        }
    }

    private native boolean cameraEnabled(long nativePtr);
    @Override
    synchronized public boolean cameraEnabled()  {
        synchronized(core) { 
        return cameraEnabled(nativePtr);
        }
    }

    private native void enableCamera(long nativePtr, boolean enabled);
    @Override
    synchronized public void enableCamera(boolean enabled)  {
        synchronized(core) { 
        enableCamera(nativePtr, enabled);
        }
    }

    private native ChatRoom getChatRoom(long nativePtr);
    @Override
    synchronized public ChatRoom getChatRoom()  {
        synchronized(core) { 
        return (ChatRoom)getChatRoom(nativePtr);
        }
    }

    private native Conference getConference(long nativePtr);
    @Override
    synchronized public Conference getConference()  {
        synchronized(core) { 
        return (Conference)getConference(nativePtr);
        }
    }

    private native Core getCore(long nativePtr);
    @Override
    synchronized public Core getCore()  {
        
        return (Core)getCore(nativePtr);
    }

    private native CallParams getCurrentParams(long nativePtr);
    @Override
    synchronized public CallParams getCurrentParams()  {
        synchronized(core) { 
        return (CallParams)getCurrentParams(nativePtr);
        }
    }

    private native float getCurrentQuality(long nativePtr);
    @Override
    synchronized public float getCurrentQuality()  {
        synchronized(core) { 
        return getCurrentQuality(nativePtr);
        }
    }

    private native int getDir(long nativePtr);
    @Override
    synchronized public Call.Dir getDir()  {
        synchronized(core) { 
        return Call.Dir.fromInt(getDir(nativePtr));
        }
    }

    private native Address getDiversionAddress(long nativePtr);
    @Override
    synchronized public Address getDiversionAddress()  {
        synchronized(core) { 
        return (Address)getDiversionAddress(nativePtr);
        }
    }

    private native int getDuration(long nativePtr);
    @Override
    synchronized public int getDuration()  {
        synchronized(core) { 
        return getDuration(nativePtr);
        }
    }

    private native boolean echoCancellationEnabled(long nativePtr);
    @Override
    synchronized public boolean echoCancellationEnabled()  {
        synchronized(core) { 
        return echoCancellationEnabled(nativePtr);
        }
    }

    private native void enableEchoCancellation(long nativePtr, boolean val);
    @Override
    synchronized public void enableEchoCancellation(boolean val)  {
        synchronized(core) { 
        enableEchoCancellation(nativePtr, val);
        }
    }

    private native boolean echoLimiterEnabled(long nativePtr);
    @Override
    synchronized public boolean echoLimiterEnabled()  {
        synchronized(core) { 
        return echoLimiterEnabled(nativePtr);
        }
    }

    private native void enableEchoLimiter(long nativePtr, boolean val);
    @Override
    synchronized public void enableEchoLimiter(boolean val)  {
        synchronized(core) { 
        enableEchoLimiter(nativePtr, val);
        }
    }

    private native ErrorInfo getErrorInfo(long nativePtr);
    @Override
    synchronized public ErrorInfo getErrorInfo()  {
        synchronized(core) { 
        return (ErrorInfo)getErrorInfo(nativePtr);
        }
    }

    private native float getMicrophoneVolumeGain(long nativePtr);
    @Override
    synchronized public float getMicrophoneVolumeGain()  {
        synchronized(core) { 
        return getMicrophoneVolumeGain(nativePtr);
        }
    }

    private native void setMicrophoneVolumeGain(long nativePtr, float volume);
    @Override
    synchronized public void setMicrophoneVolumeGain(float volume)  {
        synchronized(core) { 
        setMicrophoneVolumeGain(nativePtr, volume);
        }
    }

    private native void getNativeVideoWindowId(long nativePtr);
    @Override
    synchronized public void getNativeVideoWindowId()  {
        synchronized(core) { 
        getNativeVideoWindowId(nativePtr);
        }
    }

    private native void setNativeVideoWindowId(long nativePtr, Object id);
    @Override
    synchronized public void setNativeVideoWindowId(Object id)  {
        synchronized(core) { 
        setNativeVideoWindowId(nativePtr, id);
        }
    }

    private native CallParams getParams(long nativePtr);
    @Override
    synchronized public CallParams getParams()  {
        synchronized(core) { 
        return (CallParams)getParams(nativePtr);
        }
    }

    private native void setParams(long nativePtr, CallParams params);
    @Override
    synchronized public void setParams(CallParams params)  {
        synchronized(core) { 
        setParams(nativePtr, params);
        }
    }

    private native float getPlayVolume(long nativePtr);
    @Override
    synchronized public float getPlayVolume()  {
        synchronized(core) { 
        return getPlayVolume(nativePtr);
        }
    }

    private native Player getPlayer(long nativePtr);
    @Override
    synchronized public Player getPlayer()  {
        synchronized(core) { 
        return (Player)getPlayer(nativePtr);
        }
    }

    private native int getReason(long nativePtr);
    @Override
    synchronized public Reason getReason()  {
        synchronized(core) { 
        return Reason.fromInt(getReason(nativePtr));
        }
    }

    private native float getRecordVolume(long nativePtr);
    @Override
    synchronized public float getRecordVolume()  {
        synchronized(core) { 
        return getRecordVolume(nativePtr);
        }
    }

    private native String getReferTo(long nativePtr);
    @Override
    synchronized public String getReferTo()  {
        synchronized(core) { 
        return getReferTo(nativePtr);
        }
    }

    private native Address getRemoteAddress(long nativePtr);
    @Override
    synchronized public Address getRemoteAddress()  {
        synchronized(core) { 
        return (Address)getRemoteAddress(nativePtr);
        }
    }

    private native String getRemoteAddressAsString(long nativePtr);
    @Override
    synchronized public String getRemoteAddressAsString()  {
        synchronized(core) { 
        return getRemoteAddressAsString(nativePtr);
        }
    }

    private native String getRemoteContact(long nativePtr);
    @Override
    synchronized public String getRemoteContact()  {
        synchronized(core) { 
        return getRemoteContact(nativePtr);
        }
    }

    private native CallParams getRemoteParams(long nativePtr);
    @Override
    synchronized public CallParams getRemoteParams()  {
        synchronized(core) { 
        return (CallParams)getRemoteParams(nativePtr);
        }
    }

    private native String getRemoteUserAgent(long nativePtr);
    @Override
    synchronized public String getRemoteUserAgent()  {
        synchronized(core) { 
        return getRemoteUserAgent(nativePtr);
        }
    }

    private native Call getReplacedCall(long nativePtr);
    @Override
    synchronized public Call getReplacedCall()  {
        synchronized(core) { 
        return (Call)getReplacedCall(nativePtr);
        }
    }

    private native float getSpeakerVolumeGain(long nativePtr);
    @Override
    synchronized public float getSpeakerVolumeGain()  {
        synchronized(core) { 
        return getSpeakerVolumeGain(nativePtr);
        }
    }

    private native void setSpeakerVolumeGain(long nativePtr, float volume);
    @Override
    synchronized public void setSpeakerVolumeGain(float volume)  {
        synchronized(core) { 
        setSpeakerVolumeGain(nativePtr, volume);
        }
    }

    private native int getState(long nativePtr);
    @Override
    synchronized public Call.State getState()  {
        synchronized(core) { 
        return Call.State.fromInt(getState(nativePtr));
        }
    }

    private native int getStreamCount(long nativePtr);
    @Override
    synchronized public int getStreamCount()  {
        synchronized(core) { 
        return getStreamCount(nativePtr);
        }
    }

    private native Address getToAddress(long nativePtr);
    @Override
    synchronized public Address getToAddress()  {
        synchronized(core) { 
        return (Address)getToAddress(nativePtr);
        }
    }

    private native int getTransferState(long nativePtr);
    @Override
    synchronized public Call.State getTransferState()  {
        synchronized(core) { 
        return Call.State.fromInt(getTransferState(nativePtr));
        }
    }

    private native Call getTransferTargetCall(long nativePtr);
    @Override
    synchronized public Call getTransferTargetCall()  {
        synchronized(core) { 
        return (Call)getTransferTargetCall(nativePtr);
        }
    }

    private native Call getTransfererCall(long nativePtr);
    @Override
    synchronized public Call getTransfererCall()  {
        synchronized(core) { 
        return (Call)getTransfererCall(nativePtr);
        }
    }

    private native int accept(long nativePtr);
    @Override
    synchronized public void accept()  {
        synchronized(core) { 
        accept(nativePtr);
        }
    }

    private native int acceptEarlyMedia(long nativePtr);
    @Override
    synchronized public void acceptEarlyMedia()  {
        synchronized(core) { 
        acceptEarlyMedia(nativePtr);
        }
    }

    private native int acceptEarlyMediaWithParams(long nativePtr, CallParams params);
    @Override
    synchronized public void acceptEarlyMediaWithParams(CallParams params)  {
        synchronized(core) { 
        acceptEarlyMediaWithParams(nativePtr, params);
        }
    }

    private native int acceptUpdate(long nativePtr, CallParams params);
    @Override
    synchronized public void acceptUpdate(CallParams params)  {
        synchronized(core) { 
        acceptUpdate(nativePtr, params);
        }
    }

    private native int acceptWithParams(long nativePtr, CallParams params);
    @Override
    synchronized public void acceptWithParams(CallParams params)  {
        synchronized(core) { 
        acceptWithParams(nativePtr, params);
        }
    }

    private native boolean askedToAutoanswer(long nativePtr);
    @Override
    synchronized public boolean askedToAutoanswer()  {
        synchronized(core) { 
        return askedToAutoanswer(nativePtr);
        }
    }

    private native void cancelDtmfs(long nativePtr);
    @Override
    synchronized public void cancelDtmfs()  {
        synchronized(core) { 
        cancelDtmfs(nativePtr);
        }
    }

    private native int decline(long nativePtr, int reason);
    @Override
    synchronized public void decline(Reason reason)  {
        synchronized(core) { 
        decline(nativePtr, reason.toInt());
        }
    }

    private native int declineWithErrorInfo(long nativePtr, ErrorInfo ei);
    @Override
    synchronized public int declineWithErrorInfo(ErrorInfo ei)  {
        synchronized(core) { 
        return declineWithErrorInfo(nativePtr, ei);
        }
    }

    private native int deferUpdate(long nativePtr);
    @Override
    synchronized public void deferUpdate()  {
        synchronized(core) { 
        deferUpdate(nativePtr);
        }
    }

    private native CallStats getStats(long nativePtr, int type);
    @Override
    synchronized public CallStats getStats(StreamType type)  {
        synchronized(core) { 
        return (CallStats)getStats(nativePtr, type.toInt());
        }
    }

    private native String getToHeader(long nativePtr, String name);
    @Override
    synchronized public String getToHeader(String name)  {
        synchronized(core) { 
        return getToHeader(nativePtr, name);
        }
    }

    private native boolean hasTransferPending(long nativePtr);
    @Override
    synchronized public boolean hasTransferPending()  {
        synchronized(core) { 
        return hasTransferPending(nativePtr);
        }
    }

    private native boolean mediaInProgress(long nativePtr);
    @Override
    synchronized public boolean mediaInProgress()  {
        synchronized(core) { 
        return mediaInProgress(nativePtr);
        }
    }

    private native void oglRender(long nativePtr);
    @Override
    synchronized public void oglRender()  {
        synchronized(core) { 
        oglRender(nativePtr);
        }
    }

    private native int pause(long nativePtr);
    @Override
    synchronized public void pause()  {
        synchronized(core) { 
        pause(nativePtr);
        }
    }

    private native int redirect(long nativePtr, String redirectUri);
    @Override
    synchronized public void redirect(String redirectUri)  {
        synchronized(core) { 
        redirect(nativePtr, redirectUri);
        }
    }

    private native void requestNotifyNextVideoFrameDecoded(long nativePtr);
    @Override
    synchronized public void requestNotifyNextVideoFrameDecoded()  {
        synchronized(core) { 
        requestNotifyNextVideoFrameDecoded(nativePtr);
        }
    }

    private native int resume(long nativePtr);
    @Override
    synchronized public void resume()  {
        synchronized(core) { 
        resume(nativePtr);
        }
    }

    private native int sendDtmf(long nativePtr, char dtmf);
    @Override
    synchronized public void sendDtmf(char dtmf)  {
        synchronized(core) { 
        sendDtmf(nativePtr, dtmf);
        }
    }

    private native int sendDtmfs(long nativePtr, String dtmfs);
    @Override
    synchronized public void sendDtmfs(String dtmfs)  {
        synchronized(core) { 
        sendDtmfs(nativePtr, dtmfs);
        }
    }

    private native int sendInfoMessage(long nativePtr, InfoMessage info);
    @Override
    synchronized public void sendInfoMessage(InfoMessage info)  {
        synchronized(core) { 
        sendInfoMessage(nativePtr, info);
        }
    }

    private native void sendVfuRequest(long nativePtr);
    @Override
    synchronized public void sendVfuRequest()  {
        synchronized(core) { 
        sendVfuRequest(nativePtr);
        }
    }

    private native void startRecording(long nativePtr);
    @Override
    synchronized public void startRecording()  {
        synchronized(core) { 
        startRecording(nativePtr);
        }
    }

    private native void stopRecording(long nativePtr);
    @Override
    synchronized public void stopRecording()  {
        synchronized(core) { 
        stopRecording(nativePtr);
        }
    }

    private native int takePreviewSnapshot(long nativePtr, String file);
    @Override
    synchronized public void takePreviewSnapshot(String file)  {
        synchronized(core) { 
        takePreviewSnapshot(nativePtr, file);
        }
    }

    private native int takeVideoSnapshot(long nativePtr, String file);
    @Override
    synchronized public void takeVideoSnapshot(String file)  {
        synchronized(core) { 
        takeVideoSnapshot(nativePtr, file);
        }
    }

    private native int terminate(long nativePtr);
    @Override
    synchronized public void terminate()  {
        synchronized(core) { 
        terminate(nativePtr);
        }
    }

    private native int terminateWithErrorInfo(long nativePtr, ErrorInfo ei);
    @Override
    synchronized public void terminateWithErrorInfo(ErrorInfo ei)  {
        synchronized(core) { 
        terminateWithErrorInfo(nativePtr, ei);
        }
    }

    private native int transfer(long nativePtr, String referTo);
    @Override
    synchronized public void transfer(String referTo)  {
        synchronized(core) { 
        transfer(nativePtr, referTo);
        }
    }

    private native int transferToAnother(long nativePtr, Call dest);
    @Override
    synchronized public void transferToAnother(Call dest)  {
        synchronized(core) { 
        transferToAnother(nativePtr, dest);
        }
    }

    private native int update(long nativePtr, CallParams params);
    @Override
    synchronized public void update(CallParams params)  {
        synchronized(core) { 
        update(nativePtr, params);
        }
    }

    private native void zoom(long nativePtr, float zoomFactor, float cx, float cy);
    @Override
    synchronized public void zoom(float zoomFactor, float cx, float cy)  {
        synchronized(core) { 
        zoom(nativePtr, zoomFactor, cx, cy);
        }
    }

    private native void addListener(long nativePtr, CallListener listener);
    @Override
    synchronized public void addListener(CallListener listener)  {
        
        addListener(nativePtr, listener);
    }

    private native void removeListener(long nativePtr, CallListener listener);
    @Override
    synchronized public void removeListener(CallListener listener)  {
        
        removeListener(nativePtr, listener);
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
