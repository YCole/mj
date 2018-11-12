/*
Core.java
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
  * Linphone core main object created by function linphone_core_new . 
  */
public interface Core {
    enum LogCollectionUploadState {
        /**
        * Delivery in progress. 
        */
        InProgress(0),

        /**
        * Log collection upload successfully delivered and acknowledged by remote end
        * point. 
        */
        Delivered(1),

        /**
        * Log collection upload was not delivered. 
        */
        NotDelivered(2);

        protected final int mValue;

        private LogCollectionUploadState (int value) {
            mValue = value;
        }

        static public LogCollectionUploadState fromInt(int value) throws RuntimeException {
            switch(value) {
            case 0: return InProgress;
            case 1: return Delivered;
            case 2: return NotDelivered;
            default:
                throw new RuntimeException("Unhandled enum value " + value + " for LogCollectionUploadState");
            }
        }

        public int toInt() {
            return mValue;
        }
    };

    /**
      * Gets the mediastreamer's factory
      */
    public org.linphone.mediastream.Factory getMediastreamerFactory();
    /**
      * Returns which adaptive rate algorithm is currently configured for future calls. 
      */
    public String getAdaptiveRateAlgorithm();

    /**
      * Sets adaptive rate algorithm. 
      */
    public void setAdaptiveRateAlgorithm(String algorithm);

    /**
      * Returns whether adaptive rate control is enabled. 
      */
    public boolean adaptiveRateControlEnabled();

    /**
      * Enable adaptive rate control. 
      */
    public void enableAdaptiveRateControl(boolean enabled);

    /**
      * Tells whether the audio adaptive jitter compensation is enabled. 
      */
    public boolean audioAdaptiveJittcompEnabled();

    /**
      * Enable or disable the audio adaptive jitter compensation. 
      */
    public void enableAudioAdaptiveJittcomp(boolean enable);

    /**
      * Get the DSCP field for outgoing audio streams. 
      */
    public int getAudioDscp();

    /**
      * Set the DSCP field for outgoing audio streams. 
      */
    public void setAudioDscp(int dscp);

    /**
      * Returns the nominal audio jitter buffer size in milliseconds. 
      */
    public int getAudioJittcomp();

    /**
      * Sets the nominal audio jitter buffer size in milliseconds. 
      */
    public void setAudioJittcomp(int milliseconds);

    /**
      * Use to get multicast address to be used for audio stream. 
      */
    public String getAudioMulticastAddr();

    /**
      * Use to set multicast address to be used for audio stream. 
      */
    public void setAudioMulticastAddr(String ip);

    /**
      * Use to get multicast state of audio stream. 
      */
    public boolean audioMulticastEnabled();

    /**
      * Use to enable multicast rtp for audio stream. 
      */
    public void enableAudioMulticast(boolean yesno);

    /**
      * Use to get multicast ttl to be used for audio stream. 
      */
    public int getAudioMulticastTtl();

    /**
      * Use to set multicast ttl to be used for audio stream. 
      */
    public void setAudioMulticastTtl(int ttl);

    /**
      * Return the list of the available audio payload types. 
      */
    public PayloadType[] getAudioPayloadTypes();

    /**
      * Redefine the list of the available payload types. 
      */
    public void setAudioPayloadTypes(PayloadType[] payloadTypes);

    /**
      * Gets the UDP port used for audio streaming. 
      */
    public int getAudioPort();

    /**
      * Sets the UDP port used for audio streaming. 
      */
    public void setAudioPort(int port);

    /**
      * Get the audio port range from which is randomly chosen the UDP port used for
      * audio streaming. 
      */
    public Range getAudioPortsRange();

    /**
      * Returns an unmodifiable list of currently entered #LinphoneAuthInfo. 
      */
    public AuthInfo[] getAuthInfoList();

    /**
      * Return AVPF enablement. 
      */
    public AVPFMode getAvpfMode();

    /**
      * Enable RTCP feedback (also known as RTP/AVPF profile). 
      */
    public void setAvpfMode(AVPFMode mode);

    /**
      * Return the avpf report interval in seconds. 
      */
    public int getAvpfRrInterval();

    /**
      * Set the avpf report interval in seconds. 
      */
    public void setAvpfRrInterval(int interval);

    /**
      * Get the list of call logs (past calls). 
      */
    public CallLog[] getCallLogs();

    /**
      * Sets the database filename where call logs will be stored. 
      */
    public void setCallLogsDatabaseFile(String path);

    /**
      * Gets the database filename where call logs will be stored. 
      */
    public String getCallLogsDatabasePath();

    /**
      * Sets the database path where call logs will be stored for beautymirror. 
      */
    public void setCallLogsDatabasePath(String path);

    /**
      * Gets the current list of calls. 
      */
    public Call[] getCalls();

    /**
      * Get the number of Call. 
      */
    public int getCallsNb();

    /**
      * Get the camera sensor rotation. 
      */
    public int getCameraSensorRotation();

    /**
      * Gets the name of the currently assigned sound device for capture. 
      */
    public String getCaptureDevice();

    /**
      * Sets the sound device used for capture. 
      */
    public void setCaptureDevice(String devid);

    /**
      * Get path to the database file used for storing chat messages. 
      */
    @Deprecated
    public String getChatDatabasePath();

    /**
      * Set the chat database path. 
      */
    @Deprecated
    public void setChatDatabasePath(String path);

    /**
      * Returns whether chat is enabled. 
      */
    public boolean chatEnabled();

    /**
      * Returns an list of chat rooms. 
      */
    public ChatRoom[] getChatRooms();

    /**
      * Get a pointer on the internal conference object. 
      */
    public Conference getConference();

    /**
      * Get the set input volume of the local participant. 
      */
    public float getConferenceLocalInputVolume();

    /**
      * Tells whether the conference server feature is enabled. 
      */
    public boolean conferenceServerEnabled();

    /**
      * Enable the conference server feature. 
      */
    public void enableConferenceServer(boolean enable);

    /**
      * Get the number of participant in the running conference. 
      */
    public int getConferenceSize();

    /**
      * Returns the LpConfig object used to manage the storage (config) file. 
      */
    public Config getConfig();

    /**
      * Get my consolidated presence. 
      */
    public ConsolidatedPresence getConsolidatedPresence();

    /**
      * Set my consolidated presence. 
      */
    public void setConsolidatedPresence(ConsolidatedPresence presence);

    /**
      * Gets the current call. 
      */
    public Call getCurrentCall();

    /**
      * Get the remote address of the current call. 
      */
    public Address getCurrentCallRemoteAddress();

    /**
      * Get the effective video definition provided by the camera for the captured
      * video. 
      */
    public VideoDefinition getCurrentPreviewVideoDefinition();

    /**
      * Retrieves the first list of #LinphoneFriend from the core. 
      */
    public FriendList getDefaultFriendList();

    /**
      */
    public ProxyConfig getDefaultProxyConfig();

    /**
      * Sets the default proxy. 
      */
    public void setDefaultProxyConfig(ProxyConfig config);

    /**
      * Gets the delayed timeout See linphone_core_set_delayed_timeout() for details. 
      */
    public int getDelayedTimeout();

    /**
      * Set the in delayed timeout in seconds. 
      */
    public void setDelayedTimeout(int seconds);

    /**
      * Gets the current device orientation. 
      */
    public int getDeviceRotation();

    /**
      * Tells the core the device current orientation. 
      */
    public void setDeviceRotation(int rotation);

    /**
      * Tells whether DNS search (use of local domain if the fully qualified name did
      * return results) is enabled. 
      */
    public boolean dnsSearchEnabled();

    /**
      * Enable or disable DNS search (use of local domain if the fully qualified name
      * did return results). 
      */
    public void enableDnsSearch(boolean enable);

    /**
      * Forces liblinphone to use the supplied list of dns servers, instead of system's
      * ones. 
      */
    public void setDnsServers(String[] servers);

    /**
      * Forces liblinphone to use the supplied list of dns servers, instead of system's
      * ones and set dns_set_by_app at true or false according to value of servers
      * list. 
      */
    public void setDnsServersApp(String[] servers);

    /**
      * Tells if the DNS was set by an application. 
      */
    public boolean getDnsSetByApp();

    /**
      * Tells whether DNS SRV resolution is enabled. 
      */
    public boolean dnsSrvEnabled();

    /**
      * Enable or disable DNS SRV resolution. 
      */
    public void enableDnsSrv(boolean enable);

    /**
      * Retrieve the maximum available download bandwidth. 
      */
    public int getDownloadBandwidth();

    /**
      * Sets maximum available download bandwidth This is IP bandwidth, in kbit/s. 
      */
    public void setDownloadBandwidth(int bw);

    /**
      * Get audio packetization time linphone expects to receive from peer. 
      */
    public int getDownloadPtime();

    /**
      * Set audio packetization time linphone expects to receive from peer. 
      */
    public void setDownloadPtime(int ptime);

    /**
      * Returns TRUE if echo cancellation is enabled. 
      */
    public boolean echoCancellationEnabled();

    /**
      * Enables or disable echo cancellation. 
      */
    public void enableEchoCancellation(boolean val);

    /**
      * Get the name of the mediastreamer2 filter used for echo cancelling. 
      */
    public String getEchoCancellerFilterName();

    /**
      * Set the name of the mediastreamer2 filter to be used for echo cancelling. 
      */
    public void setEchoCancellerFilterName(String filtername);

    /**
      * Tells whether echo limiter is enabled. 
      */
    public boolean echoLimiterEnabled();

    /**
      * Enables or disable echo limiter. 
      */
    public void enableEchoLimiter(boolean val);

    /**
      * Sets expected available upload bandwidth This is IP bandwidth, in kbit/s. 
      */
    public void setExpectedBandwidth(int bw);

    /**
      * Get the globaly set http file transfer server to be used for content type
      * application/vnd.gsma.rcs-ft-http+xml. 
      */
    public String getFileTransferServer();

    /**
      * Globaly set an http file transfer server to be used for content type
      * application/vnd.gsma.rcs-ft-http+xml. 
      */
    public void setFileTransferServer(String serverUrl);

    /**
      * Gets the database filename where friends will be stored. 
      */
    public String getFriendsDatabasePath();

    /**
      * Sets the database filename where friends will be stored. 
      */
    public void setFriendsDatabasePath(String path);

    /**
      * Retrieves the list of #LinphoneFriendList from the core. 
      */
    public FriendList[] getFriendsLists();

    /**
      * Returns TRUE if hostname part of primary contact is guessed automatically. 
      */
    public boolean getGuessHostname();

    /**
      * Tells #LinphoneCore to guess local hostname automatically in primary contact. 
      */
    public void setGuessHostname(boolean val);

    /**
      * Get http proxy address to be used for signaling. 
      */
    public String getHttpProxyHost();

    /**
      * Set http proxy address to be used for signaling during next channel connection. 
      */
    public void setHttpProxyHost(String host);

    /**
      * Get http proxy port to be used for signaling. 
      */
    public int getHttpProxyPort();

    /**
      * Set http proxy port to be used for signaling. 
      */
    public void setHttpProxyPort(int port);

    /**
      * Gets the default identity SIP address. 
      */
    public String getIdentity();

    /**
      * Get the #LinphoneImNotifPolicy object controlling the instant messaging
      * notifications. 
      */
    public ImNotifPolicy getImNotifPolicy();

    /**
      * Gets the in call timeout See linphone_core_set_in_call_timeout() for details. 
      */
    public int getInCallTimeout();

    /**
      * Set the in call timeout in seconds. 
      */
    public void setInCallTimeout(int seconds);

    /**
      * Returns the incoming call timeout See linphone_core_set_inc_timeout() for
      * details. 
      */
    public int getIncTimeout();

    /**
      * Set the incoming call timeout in seconds. 
      */
    public void setIncTimeout(int seconds);

    /**
      * Tells whether IPv6 is enabled or not. 
      */
    public boolean ipv6Enabled();

    /**
      * Turns IPv6 support on or off. 
      */
    public void enableIpv6(boolean val);

    /**
      * Check whether the device is echo canceller calibration is required. 
      */
    public boolean isEchoCancellerCalibrationRequired();

    /**
      * Indicates whether the local participant is part of a conference. 
      */
    public boolean isInConference();

    /**
      * Tells whether there is an incoming invite pending. 
      */
    public boolean isIncomingInvitePending();

    /**
      * Get behaviour when encryption parameters negociation fails on outgoing call. 
      */
    public boolean isMediaEncryptionMandatory();

    /**
      * return network state either as positioned by the application or by linphone
      * itself. 
      */
    public boolean isNetworkReachable();

    /**
      * Is signaling keep alive enabled. 
      */
    public boolean keepAliveEnabled();

    /**
      * Enables signaling keep alive, small udp packet sent periodically to keep udp
      * NAT association. 
      */
    public void enableKeepAlive(boolean enable);

    /**
      * Get the latest outgoing call log. 
      */
    public CallLog getLastOutgoingCallLog();

    /**
      * Get the linphone specs value telling what functionalities the linphone client
      * supports. 
      */
    public String getLinphoneSpecs();

    /**
      * Set the linphone specs value telling what functionalities the linphone client
      * supports. 
      */
    public void setLinphoneSpecs(String specs);

    /**
      * Gets the url of the server where to upload the collected log files. 
      */
    public String getLogCollectionUploadServerUrl();

    /**
      * Set the url of the server where to upload the collected log files. 
      */
    public void setLogCollectionUploadServerUrl(String serverUrl);

    /**
      * Get the maximum number of simultaneous calls Linphone core can manage at a
      * time. 
      */
    public int getMaxCalls();

    /**
      * Set the maximum number of simultaneous calls Linphone core can manage at a
      * time. 
      */
    public void setMaxCalls(int max);

    /**
      * Get the media encryption policy being used for RTP packets. 
      */
    public MediaEncryption getMediaEncryption();

    /**
      * Choose the media encryption policy to be used for RTP packets. 
      */
    public void setMediaEncryption(MediaEncryption menc);

    /**
      * Define behaviour when encryption parameters negociation fails on outgoing call. 
      */
    public void setMediaEncryptionMandatory(boolean m);

    /**
      * This method is called by the application to notify the linphone core library
      * when the media (RTP) network is reachable. 
      */
    public void setMediaNetworkReachable(boolean value);

    /**
      * Tells whether the microphone is enabled. 
      */
    public boolean micEnabled();

    /**
      * Enable or disable the microphone. 
      */
    public void enableMic(boolean enable);

    /**
      * Get microphone gain in db. 
      */
    public float getMicGainDb();

    /**
      * Allow to control microphone level: gain in db. 
      */
    public void setMicGainDb(float level);

    /**
      * Get the number of missed calls. 
      */
    public int getMissedCallsCount();

    /**
      * Returns the maximum transmission unit size in bytes. 
      */
    public int getMtu();

    /**
      * Sets the maximum transmission unit size in bytes. 
      */
    public void setMtu(int mtu);

    /**
      * Get the public IP address of NAT being used. 
      */
    public String getNatAddress();

    /**
      * Set the public IP address of NAT when using the firewall policy is set to use
      * NAT. 
      */
    public void setNatAddress(String addr);

    /**
      * Get The policy that is used to pass through NATs/firewalls. 
      */
    public NatPolicy getNatPolicy();

    /**
      * Set the policy to use to pass through NATs/firewalls. 
      */
    public void setNatPolicy(NatPolicy policy);

    /**
      * Get the native window handle of the video preview window. 
      */
    public void getNativePreviewWindowId();

    /**
      * Set the native window id where the preview video (local camera) is to be
      * displayed. 
      */
    public void setNativePreviewWindowId(Object id);

    /**
      * Get the native window handle of the video window. 
      */
    public void getNativeVideoWindowId();

    /**
      * Set the native video window id where the video is to be displayed. 
      */
    public void setNativeVideoWindowId(Object id);

    /**
      * This method is called by the application to notify the linphone core library
      * when network is reachable. 
      */
    public void setNetworkReachable(boolean value);

    /**
      * Gets the value of the no-rtp timeout. 
      */
    public int getNortpTimeout();

    /**
      * Sets the no-rtp timeout value in seconds. 
      */
    public void setNortpTimeout(int seconds);

    /**
      * Get the wav file that is played when putting somebody on hold, or when files
      * are used instead of soundcards (see linphone_core_set_use_files()). 
      */
    public String getPlayFile();

    /**
      * Sets a wav file to be played when putting somebody on hold, or when files are
      * used instead of soundcards (see linphone_core_set_use_files()). 
      */
    public void setPlayFile(String file);

    /**
      * Gets the name of the currently assigned sound device for playback. 
      */
    public String getPlaybackDevice();

    /**
      * Sets the sound device used for playback. 
      */
    public void setPlaybackDevice(String devid);

    /**
      * Get playback gain in db before entering sound card. 
      */
    public float getPlaybackGainDb();

    /**
      * Allow to control play level before entering sound card: gain in db. 
      */
    public void setPlaybackGainDb(float level);

    /**
      * Returns the preferred video framerate, previously set by
      * linphone_core_set_preferred_framerate(). 
      */
    public float getPreferredFramerate();

    /**
      * Set the preferred frame rate for video. 
      */
    public void setPreferredFramerate(float fps);

    /**
      * Get the preferred video definition for the stream that is captured and sent to
      * the remote party. 
      */
    public VideoDefinition getPreferredVideoDefinition();

    /**
      * Set the preferred video definition for the stream that is captured and sent to
      * the remote party. 
      */
    public void setPreferredVideoDefinition(VideoDefinition vdef);

    /**
      * Sets the preferred video size by its name. 
      */
    @Deprecated
    public void setPreferredVideoSizeByName(String name);

    /**
      * Get my presence model. 
      */
    public PresenceModel getPresenceModel();

    /**
      * Set my presence model. 
      */
    public void setPresenceModel(PresenceModel presence);

    /**
      * Get the definition of the captured video. 
      */
    public VideoDefinition getPreviewVideoDefinition();

    /**
      * Set the video definition for the captured (preview) video. 
      */
    public void setPreviewVideoDefinition(VideoDefinition vdef);

    /**
      * Sets the preview video size by its name. 
      */
    @Deprecated
    public void setPreviewVideoSizeByName(String name);

    /**
      * Returns the default identity when no proxy configuration is used. 
      */
    public String getPrimaryContact();

    /**
      * Sets the local &quot;from&quot; identity. 
      */
    public void setPrimaryContact(String contact);

    /**
      * Same as linphone_core_get_primary_contact() but the result is a
      * #LinphoneAddress object instead of const char*. 
      */
    public Address getPrimaryContactParsed();

    /**
      * Get provisioning URI. 
      */
    public String getProvisioningUri();

    /**
      * Set URI where to download xml configuration file at startup. 
      */
    public void setProvisioningUri(String uri);

    /**
      * Returns an unmodifiable list of entered proxy configurations. 
      */
    public ProxyConfig[] getProxyConfigList();

    /**
      * Tells whether QRCode is enabled in the preview. 
      */
    public boolean qrcodeVideoPreviewEnabled();

    /**
      * Controls QRCode enablement. 
      */
    public void enableQrcodeVideoPreview(boolean val);

    /**
      * Gets if realtime text is enabled or not. 
      */
    public boolean realtimeTextEnabled();

    /**
      * Get the wav file where incoming stream is recorded, when files are used instead
      * of soundcards (see linphone_core_set_use_files()). 
      */
    public String getRecordFile();

    /**
      * Sets a wav file where incoming stream is to be recorded, when files are used
      * instead of soundcards (see linphone_core_set_use_files()). 
      */
    public void setRecordFile(String file);

    /**
      * Get the ring back tone played to far end during incoming calls. 
      */
    public String getRemoteRingbackTone();

    /**
      * Specify a ring back tone to be played to far end during incoming calls. 
      */
    public void setRemoteRingbackTone(String ring);

    /**
      * Returns the path to the wav file used for ringing. 
      */
    public String getRing();

    /**
      * Sets the path to a wav file used for ringing. 
      */
    public void setRing(String path);

    /**
      * Tells whether the ring play is enabled during an incoming early media call. 
      */
    public boolean getRingDuringIncomingEarlyMedia();

    /**
      * Enable or disable the ring play during an incoming early media call. 
      */
    public void setRingDuringIncomingEarlyMedia(boolean enable);

    /**
      * Returns the path to the wav file used for ringing back. 
      */
    public String getRingback();

    /**
      * Sets the path to a wav file used for ringing back. 
      */
    public void setRingback(String path);

    /**
      * Gets the name of the currently assigned sound device for ringing. 
      */
    public String getRingerDevice();

    /**
      * Sets the sound device used for ringing. 
      */
    public void setRingerDevice(String devid);

    /**
      * Gets the path to a file or folder containing the trusted root CAs (PEM format) 
      */
    public String getRootCa();

    /**
      * Sets the path to a file or folder containing trusted root CAs (PEM format) 
      */
    public void setRootCa(String path);

    /**
      * Sets the trusted root CAs (PEM format) 
      */
    public void setRootCaData(String data);

    /**
      * Media offer control param for SIP INVITE. 
      */
    public boolean sdp200AckEnabled();

    /**
      * Control when media offer is sent in SIP INVITE. 
      */
    public void enableSdp200Ack(boolean enable);

    /**
      * Tells whether video self view during call is enabled or not. 
      */
    public boolean selfViewEnabled();

    /**
      * Enables or disable self view during calls. 
      */
    public void enableSelfView(boolean val);

    /**
      * Get the DSCP field for SIP signaling channel. 
      */
    public int getSipDscp();

    /**
      * Set the DSCP field for SIP signaling channel. 
      */
    public void setSipDscp(int dscp);

    /**
      * This method is called by the application to notify the linphone core library
      * when the SIP network is reachable. 
      */
    public void setSipNetworkReachable(boolean value);

    /**
      * Get the SIP transport timeout. 
      */
    public int getSipTransportTimeout();

    /**
      * Set the SIP transport timeout. 
      */
    public void setSipTransportTimeout(int timeoutMs);

    /**
      * Gets the list of the available sound devices. 
      */
    public String[] getSoundDevicesList();

    /**
      * Get the path to the image file streamed when &quot;Static picture&quot; is set as the
      * video device. 
      */
    public String getStaticPicture();

    /**
      * Set the path to the image file to stream when &quot;Static picture&quot; is set as the
      * video device. 
      */
    public void setStaticPicture(String path);

    /**
      * Get the frame rate for static picture. 
      */
    public float getStaticPictureFps();

    /**
      * Set the frame rate for static picture. 
      */
    public void setStaticPictureFps(float fps);

    /**
      * Get the STUN server address being used. 
      */
    public String getStunServer();

    /**
      * Set the STUN server address to use when the firewall policy is set to STUN. 
      */
    public void setStunServer(String server);

    /**
      * Returns a null terminated table of strings containing the file format extension
      * supported for call recording. 
      */
    public String[] getSupportedFileFormatsList();

    /**
      * Return the list of the available text payload types. 
      */
    public PayloadType[] getTextPayloadTypes();

    /**
      * Redefine the list of the available payload types. 
      */
    public void setTextPayloadTypes(PayloadType[] payloadTypes);

    /**
      * Gets the UDP port used for text streaming. 
      */
    public int getTextPort();

    /**
      * Sets the UDP port used for text streaming. 
      */
    public void setTextPort(int port);

    /**
      * Get the text port range from which is randomly chosen the UDP port used for
      * text streaming. 
      */
    public Range getTextPortsRange();

    /**
      * Gets the TLS certificate. 
      */
    public String getTlsCert();

    /**
      * Sets a TLS certificate used for TLS authentication The certificate won't be
      * stored, you have to set it after each #LinphoneCore startup. 
      */
    public void setTlsCert(String tlsCert);

    /**
      * Gets the path to the TLS certificate file. 
      */
    public String getTlsCertPath();

    /**
      * Sets a TLS certificate path used for TLS authentication The path will be stored
      * in the rc file and automatically restored on startup. 
      */
    public void setTlsCertPath(String tlsCertPath);

    /**
      * Gets the TLS key. 
      */
    public String getTlsKey();

    /**
      * Sets a TLS key used for TLS authentication The key won't be stored, you have to
      * set it after each #LinphoneCore startup. 
      */
    public void setTlsKey(String tlsKey);

    /**
      * Gets the path to the TLS key file. 
      */
    public String getTlsKeyPath();

    /**
      * Sets a TLS key path used for TLS authentication The path will be stored in the
      * rc file and automatically restored on startup. 
      */
    public void setTlsKeyPath(String tlsKeyPath);

    /**
      * Retrieves the port configuration used for each transport (udp, tcp, tls). 
      */
    public Transports getTransports();

    /**
      * Sets the ports to be used for each of transport (UDP or TCP) A zero value port
      * for a given transport means the transport is not used. 
      */
    public void setTransports(Transports transports);

    /**
      * Retrieves the real port number assigned for each sip transport (udp, tcp, tls). 
      */
    public Transports getTransportsUsed();

    /**
      * get tunnel instance if available 
      */
    public Tunnel getTunnel();

    /**
      * Return the global unread chat message count. 
      */
    public int getUnreadChatMessageCount();

    /**
      * Return the unread chat message count for all active local address. 
      */
    public int getUnreadChatMessageCountFromActiveLocals();

    /**
      * Retrieve the maximum available upload bandwidth. 
      */
    public int getUploadBandwidth();

    /**
      * Sets maximum available upload bandwidth This is IP bandwidth, in kbit/s. 
      */
    public void setUploadBandwidth(int bw);

    /**
      * Set audio packetization time linphone will send (in absence of requirement from
      * peer) A value of 0 stands for the current codec default packetization time. 
      */
    public int getUploadPtime();

    /**
      * Set audio packetization time linphone will send (in absence of requirement from
      * peer) A value of 0 stands for the current codec default packetization time. 
      */
    public void setUploadPtime(int ptime);

    /**
      * Return the external ip address of router. 
      */
    public String getUpnpExternalIpaddress();

    /**
      * Return the internal state of uPnP. 
      */
    public UpnpState getUpnpState();

    /**
      * Gets whether linphone is currently streaming audio from and to files, rather
      * than using the soundcard. 
      */
    public boolean getUseFiles();

    /**
      * Ask the core to stream audio from and to files, instead of using the soundcard. 
      */
    public void setUseFiles(boolean yesno);

    /**
      * Indicates whether SIP INFO is used to send digits. 
      */
    public boolean getUseInfoForDtmf();

    /**
      * Sets whether SIP INFO is to be used to send digits. 
      */
    public void setUseInfoForDtmf(boolean useInfo);

    /**
      * Indicates whether RFC2833 is used to send digits. 
      */
    public boolean getUseRfc2833ForDtmf();

    /**
      * Sets whether RFC2833 is to be used to send digits. 
      */
    public void setUseRfc2833ForDtmf(boolean useRfc2833);

    /**
      */
    public String getUserAgent();

    /**
      * Get the path to the directory storing the user's certificates. 
      */
    public String getUserCertificatesPath();

    /**
      * Set the path to the directory storing the user's x509 certificates (used by
      * dtls) 
      */
    public void setUserCertificatesPath(String path);

    /**
      * Get the default policy for video. 
      */
    public VideoActivationPolicy getVideoActivationPolicy();

    /**
      * Sets the default policy for video. 
      */
    public void setVideoActivationPolicy(VideoActivationPolicy policy);

    /**
      * Tells whether the video adaptive jitter compensation is enabled. 
      */
    public boolean videoAdaptiveJittcompEnabled();

    /**
      * Enable or disable the video adaptive jitter compensation. 
      */
    public void enableVideoAdaptiveJittcomp(boolean enable);

    /**
      * Tells whether video capture is enabled. 
      */
    public boolean videoCaptureEnabled();

    /**
      * Enable or disable video capture. 
      */
    public void enableVideoCapture(boolean enable);

    /**
      * Returns the name of the currently active video device. 
      */
    public String getVideoDevice();

    /**
      * Sets the active video device. 
      */
    public void setVideoDevice(String id);

    /**
      * Gets the list of the available video capture devices. 
      */
    public String[] getVideoDevicesList();

    /**
      * Tells whether video display is enabled. 
      */
    public boolean videoDisplayEnabled();

    /**
      * Enable or disable video display. 
      */
    public void enableVideoDisplay(boolean enable);

    /**
      * Get the name of the mediastreamer2 filter used for rendering video. 
      */
    public String getVideoDisplayFilter();

    /**
      * Set the name of the mediastreamer2 filter to be used for rendering video. 
      */
    public void setVideoDisplayFilter(String filtername);

    /**
      * Get the DSCP field for outgoing video streams. 
      */
    public int getVideoDscp();

    /**
      * Set the DSCP field for outgoing video streams. 
      */
    public void setVideoDscp(int dscp);

    /**
      * Returns TRUE if either capture or display is enabled, TRUE otherwise. 
      */
    public boolean videoEnabled();

    /**
      * Returns the nominal video jitter buffer size in milliseconds. 
      */
    public int getVideoJittcomp();

    /**
      * Sets the nominal video jitter buffer size in milliseconds. 
      */
    public void setVideoJittcomp(int milliseconds);

    /**
      * Use to get multicast address to be used for video stream. 
      */
    public String getVideoMulticastAddr();

    /**
      * Use to set multicast address to be used for video stream. 
      */
    public void setVideoMulticastAddr(String ip);

    /**
      * Use to get multicast state of video stream. 
      */
    public boolean videoMulticastEnabled();

    /**
      * Use to enable multicast rtp for video stream. 
      */
    public void enableVideoMulticast(boolean yesno);

    /**
      * Use to get multicast ttl to be used for video stream. 
      */
    public int getVideoMulticastTtl();

    /**
      * Use to set multicast ttl to be used for video stream. 
      */
    public void setVideoMulticastTtl(int ttl);

    /**
      * Return the list of the available video payload types. 
      */
    public PayloadType[] getVideoPayloadTypes();

    /**
      * Redefine the list of the available video payload types. 
      */
    public void setVideoPayloadTypes(PayloadType[] payloadTypes);

    /**
      * Gets the UDP port used for video streaming. 
      */
    public int getVideoPort();

    /**
      * Sets the UDP port used for video streaming. 
      */
    public void setVideoPort(int port);

    /**
      * Get the video port range from which is randomly chosen the UDP port used for
      * video streaming. 
      */
    public Range getVideoPortsRange();

    /**
      * Get the video preset used for video calls. 
      */
    public String getVideoPreset();

    /**
      * Set the video preset to be used for video calls. 
      */
    public void setVideoPreset(String preset);

    /**
      * Tells whether video preview is enabled. 
      */
    public boolean videoPreviewEnabled();

    /**
      * Controls video preview enablement. 
      */
    public void enableVideoPreview(boolean val);

    /**
      * Enable or disable video source reuse when switching from preview to actual
      * video call. 
      */
    public void enableVideoSourceReuse(boolean enable);

    /**
      * Get the path to the file storing the zrtp secrets cache. 
      */
    public String getZrtpSecretsFile();

    /**
      * Set the path to the file storing the zrtp secrets cache. 
      */
    public void setZrtpSecretsFile(String file);

    /**
      * Accept an incoming call. 
      */
    @Deprecated
    public void acceptCall(Call call);

    /**
      * Accept call modifications initiated by other end. 
      */
    @Deprecated
    public void acceptCallUpdate(Call call, CallParams params);

    /**
      * Accept an incoming call, with parameters. 
      */
    @Deprecated
    public void acceptCallWithParams(Call call, CallParams params);

    /**
      * Accept an early media session for an incoming call. 
      */
    @Deprecated
    public void acceptEarlyMedia(Call call);

    /**
      * When receiving an incoming, accept to start a media session as early-media. 
      */
    @Deprecated
    public void acceptEarlyMediaWithParams(Call call, CallParams params);

    /**
      * Add all current calls into the conference. 
      */
    public void addAllToConference();

    /**
      * Adds authentication information to the #LinphoneCore. 
      */
    public void addAuthInfo(AuthInfo info);

    /**
      * Add a friend to the current buddy list, if subscription attribute  is set, a
      * SIP SUBSCRIBE message is sent. 
      */
    @Deprecated
    public void addFriend(Friend fr);

    /**
      * Add a friend list. 
      */
    public void addFriendList(FriendList list);

    /**
      * Add a proxy configuration. 
      */
    public void addProxyConfig(ProxyConfig config);

    /**
      * This function controls signaling features supported by the core. 
      */
    public void addSupportedTag(String tag);

    /**
      * Add a participant to the conference. 
      */
    public void addToConference(Call call);

    /**
      * Checks if a new version of the application is available. 
      */
    public void checkForUpdate(String currentVersion);

    /**
      * Clear all authentication information. 
      */
    public void clearAllAuthInfo();

    /**
      * Erase the call log. 
      */
    public void clearCallLogs();

    /**
      * Erase all proxies from config. 
      */
    public void clearProxyConfig();

    /**
      * Create a #LinphoneAccountCreator and set Linphone Request callbacks. 
      */
    public AccountCreator createAccountCreator(String xmlrpcUrl);

    /**
      * Create a #LinphoneAddress object by parsing the user supplied address, given as
      * a string. 
      */
    public Address createAddress(String address);

    /**
      * Create an authentication information with default values from Linphone core. 
      */
    public AuthInfo createAuthInfo(String username, String userid, String passwd, String ha1, String realm, String domain);

    /**
      * Creates a fake LinphoneCallLog. 
      */
    public CallLog createCallLog(Address from, Address to, Call.Dir dir, int duration, long startTime, long connectedTime, Call.Status status, boolean videoEnabled, float quality);

    /**
      * Create a #LinphoneCallParams suitable for linphone_core_invite_with_params(),
      * linphone_core_accept_call_with_params(),
      * linphone_core_accept_early_media_with_params(),
      * linphone_core_accept_call_update(). 
      */
    public CallParams createCallParams(Call call);

    /**
      * Create a client-side group chat room. 
      */
    public ChatRoom createClientGroupChatRoom(String subject, boolean fallback);

    /**
      * Create some default conference parameters for instanciating a a conference with
      * linphone_core_create_conference_with_params(). 
      */
    public ConferenceParams createConferenceParams();

    /**
      * Create a conference. 
      */
    public Conference createConferenceWithParams(ConferenceParams params);

    /**
      * Create a #LinphoneConfig object from a user config file. 
      */
    public Config createConfig(String filename);

    /**
      * Create a content with default values from Linphone core. 
      */
    public Content createContent();

    /**
      * Create a default LinphoneFriend. 
      */
    public Friend createFriend();

    /**
      * Create a new empty #LinphoneFriendList object. 
      */
    public FriendList createFriendList();

    /**
      * Create a #LinphoneFriend from the given address. 
      */
    public Friend createFriendWithAddress(String address);

    /**
      * Creates an empty info message. 
      */
    public InfoMessage createInfoMessage();

    /**
      * Create an independent media file player. 
      */
    public Player createLocalPlayer(String soundCardName, String videoDisplayName, Object windowId);

    /**
      * Create a #LinphoneMagicSearch object. 
      */
    public MagicSearch createMagicSearch();

    /**
      * Create a new #LinphoneNatPolicy object with every policies being disabled. 
      */
    public NatPolicy createNatPolicy();

    /**
      * Create a new #LinphoneNatPolicy by reading the config of a #LinphoneCore
      * according to the passed ref. 
      */
    public NatPolicy createNatPolicyFromConfig(String ref);

    /**
      * Create an out-of-dialog notification, specifying the destination resource, the
      * event name. 
      */
    public Event createNotify(Address resource, String event);

    /**
      * Create a publish context for a one-shot publish. 
      */
    public Event createOneShotPublish(Address resource, String event);

    /**
      * Create a #LinphonePresenceActivity with the given type and description. 
      */
    public PresenceActivity createPresenceActivity(PresenceActivity.Type acttype, String description);

    /**
      * Create a default LinphonePresenceModel. 
      */
    public PresenceModel createPresenceModel();

    /**
      * Create a #LinphonePresenceModel with the given activity type and activity
      * description. 
      */
    public PresenceModel createPresenceModelWithActivity(PresenceActivity.Type acttype, String description);

    /**
      * Create a #LinphonePresenceModel with the given activity type, activity
      * description, note content and note language. 
      */
    public PresenceModel createPresenceModelWithActivityAndNote(PresenceActivity.Type acttype, String description, String note, String lang);

    /**
      * Create a #LinphonePresenceNote with the given content and language. 
      */
    public PresenceNote createPresenceNote(String content, String lang);

    /**
      * Create a #LinphonePresencePerson with the given id. 
      */
    public PresencePerson createPresencePerson(String id);

    /**
      * Create a #LinphonePresenceService with the given id, basic status and contact. 
      */
    public PresenceService createPresenceService(String id, PresenceBasicStatus basicStatus, String contact);

    /**
      * Create a proxy config with default values from Linphone core. 
      */
    public ProxyConfig createProxyConfig();

    /**
      * Create a publish context for an event state. 
      */
    public Event createPublish(Address resource, String event, int expires);

    /**
      * Create an outgoing subscription, specifying the destination resource, the event
      * name, and an optional content body. 
      */
    public Event createSubscribe(Address resource, String event, int expires);

    /**
      * Create a #LinphoneXmlRpcSession for a given url. 
      */
    public XmlRpcSession createXmlRpcSession(String url);

    /**
      * Decline a pending incoming call, with a reason. 
      */
    @Deprecated
    public void declineCall(Call call, Reason reason);

    /**
      * When receiving a #LinphoneCallUpdatedByRemote state notification, prevent
      * #LinphoneCore from performing an automatic answer. 
      */
    @Deprecated
    public void deferCallUpdate(Call call);

    /**
      * Removes a chatroom including all message history from the LinphoneCore. 
      */
    public void deleteChatRoom(ChatRoom cr);

    /**
      * Inconditionnaly disable incoming chat messages. 
      */
    public void disableChat(Reason denyReason);

    /**
      * Enable reception of incoming chat messages. 
      */
    public void enableChat();

    /**
      * Tells to #LinphoneCore to use Linphone Instant Messaging encryption. 
      */
    public void enableLime(LimeState val);

    /**
      * This method is called by the application to notify the linphone core library
      * when it enters background mode. 
      */
    public void enterBackground();

    /**
      * Join the local participant to the running conference. 
      */
    public void enterConference();

    /**
      * This method is called by the application to notify the linphone core library
      * when it enters foreground mode. 
      */
    public void enterForeground();

    /**
      * Returns whether a specific file format is supported. 
      */
    public boolean fileFormatSupported(String fmt);

    /**
      * Find authentication info matching realm, username, domain criteria. 
      */
    public AuthInfo findAuthInfo(String realm, String username, String sipDomain);

    /**
      * Search from the list of current calls if a remote address match uri. 
      */
    public Call findCallFromUri(String uri);

    /**
      * Get the call log matching the call id, or NULL if can't be found. 
      */
    public CallLog findCallLogFromCallId(String callId);

    /**
      * Find a chat room. 
      */
    public ChatRoom findChatRoom(Address peerAddr, Address localAddr);

    /**
      * Retrieves a list of #LinphoneAddress sort and filter. 
      */
    public Address[] findContactsByChar(String filter, boolean sipOnly);

    /**
      * Search a #LinphoneFriend by its address. 
      */
    public Friend findFriend(Address addr);

    /**
      * Find a one to one chat room. 
      */
    public ChatRoom findOneToOneChatRoom(Address localAddr, Address participantAddr);

    /**
      * Get the call with the remote_address specified. 
      */
    public Call getCallByRemoteAddress(String remoteAddress);

    /**
      * Get the call with the remote_address specified. 
      */
    public Call getCallByRemoteAddress2(Address remoteAddress);

    /**
      * Get the list of call logs (past calls) that matches the given #LinphoneAddress. 
      */
    public CallLog[] getCallHistoryForAddress(Address addr);

    /**
      * Get a basic chat room. 
      */
    public ChatRoom getChatRoom(Address peerAddr, Address localAddr);

    /**
      * Get a basic chat room whose peer is the supplied address. 
      */
    public ChatRoom getChatRoom(Address addr);

    /**
      * Get a basic chat room for messaging from a sip uri like
      * sip:joe@sip.linphone.org. 
      */
    public ChatRoom getChatRoomFromUri(String to);

    /**
      * Search a #LinphoneFriend by its reference key. 
      */
    public Friend getFriendByRefKey(String key);

    /**
      * Retrieves the list of #LinphoneFriend from the core that has the given display
      * name. 
      */
    public FriendList getFriendListByName(String name);

    /**
      * Get payload type from mime type and clock rate. 
      */
    public PayloadType getPayloadType(String type, int rate, int channels);

    /**
      * Return the unread chat message count for a given local address. 
      */
    public int getUnreadChatMessageCountFromLocal(Address address);

    /**
      * Check whether the device has a hardware echo canceller. 
      */
    public boolean hasBuiltinEchoCanceller();

    /**
      * Check whether the device is flagged has crappy opengl. 
      */
    public boolean hasCrappyOpengl();

    /**
      * Tells whether there is a call running. 
      */
    public boolean inCall();

    /**
      * See linphone_proxy_config_normalize_sip_uri for documentation. 
      */
    public Address interpretUrl(String url);

    /**
      * Initiates an outgoing call. 
      */
    public Call invite(String url);

    /**
      * Initiates an outgoing call given a destination #LinphoneAddress The
      * #LinphoneAddress can be constructed directly using linphone_address_new, or
      * created by linphone_core_interpret_url(). 
      */
    public Call inviteAddress(Address addr);

    /**
      * Initiates an outgoing call given a destination #LinphoneAddress The
      * #LinphoneAddress can be constructed directly using linphone_address_new, or
      * created by linphone_core_interpret_url(). 
      */
    public Call inviteAddressWithParams(Address addr, CallParams params);

    /**
      * Initiates an outgoing call according to supplied call parameters The
      * application doesn't own a reference to the returned #LinphoneCall object. 
      */
    public Call inviteWithParams(String url, CallParams params);

    /**
      * Main loop function. 
      */
    public void iterate();

    /**
      * Make the local participant leave the running conference. 
      */
    public void leaveConference();

    /**
      * Tells if lime is available. 
      */
    public boolean limeAvailable();

    /**
      * Returns the lime state. 
      */
    public LimeState limeEnabled();

    /**
      * Check if a media encryption type is supported. 
      */
    public boolean mediaEncryptionSupported(MediaEncryption menc);

    /**
      * Migrates the call logs from the linphonerc to the database if not done yet. 
      */
    public void migrateLogsFromRcToDb();

    /**
      * Migrate configuration so that all SIP transports are enabled. 
      */
    public void migrateToMultiTransport();

    /**
      * Notify all friends that have subscribed. 
      */
    public void notifyAllFriends(PresenceModel presence);

    /**
      * Notifies the upper layer that a presence status has been received by calling
      * the appropriate callback if one has been set. 
      */
    public void notifyNotifyPresenceReceived(Friend lf);

    /**
      * Notifies the upper layer that a presence model change has been received for the
      * uri or telephone number given as a parameter, by calling the appropriate
      * callback if one has been set. 
      */
    public void notifyNotifyPresenceReceivedForUriOrTel(Friend lf, String uriOrTel, PresenceModel presenceModel);

    /**
      * Pause all currently running calls. 
      */
    public void pauseAllCalls();

    /**
      * Pauses the call. 
      */
    @Deprecated
    public void pauseCall(Call call);

    /**
      * Plays a dtmf sound to the local user. 
      */
    public void playDtmf(char dtmf, int durationMs);

    /**
      * Plays an audio file to the local user. 
      */
    public void playLocal(String audiofile);

    /**
      * Call generic OpenGL render for a given core. 
      */
    public void previewOglRender();

    /**
      * Publish an event state. 
      */
    public Event publish(Address resource, String event, int expires, Content body);

    /**
      * Redirect the specified call to the given redirect URI. 
      */
    @Deprecated
    public void redirectCall(Call call, String redirectUri);

    /**
      * force registration refresh to be initiated upon next iterate 
      */
    public void refreshRegisters();

    /**
      * Black list a friend. 
      */
    public void rejectSubscriber(Friend lf);

    /**
      * Reload mediastreamer2 plugins from specified directory. 
      */
    public void reloadMsPlugins(String path);

    /**
      * Update detection of sound devices. 
      */
    public void reloadSoundDevices();

    /**
      * Update detection of camera devices. 
      */
    public void reloadVideoDevices();

    /**
      * Removes an authentication information object. 
      */
    public void removeAuthInfo(AuthInfo info);

    /**
      * Remove a specific call log from call history list. 
      */
    public void removeCallLog(CallLog callLog);

    /**
      * Removes a friend list. 
      */
    public void removeFriendList(FriendList list);

    /**
      * Remove a call from the conference. 
      */
    public void removeFromConference(Call call);

    /**
      * Removes a proxy configuration. 
      */
    public void removeProxyConfig(ProxyConfig config);

    /**
      * Remove a supported tag. 
      */
    public void removeSupportedTag(String tag);

    /**
      * Reset the counter of missed calls. 
      */
    public void resetMissedCallsCount();

    /**
      * Resumes a call. 
      */
    @Deprecated
    public void resumeCall(Call call);

    /**
      * Sets the UDP port range from which to randomly select the port used for audio
      * streaming. 
      */
    public void setAudioPortRange(int minPort, int maxPort);

    /**
      * Assign an audio file to be played locally upon call failure, for a given
      * reason. 
      */
    public void setCallErrorTone(Reason reason, String audiofile);

    /**
      * Sets the account and device for friend where call logs will be stored. 
      */
    public void setCallLogsFriend(String account, String device);

    /**
      * Set the rectangle where the decoder will search a QRCode. 
      */
    public void setQrcodeDecodeRect(int x, int y, int w, int h);

    /**
      * Sets the UDP port range from which to randomly select the port used for text
      * streaming. 
      */
    public void setTextPortRange(int minPort, int maxPort);

    /**
      * Sets the user agent string used in SIP messages, ideally called just after
      * linphone_core_new or linphone_core_init(). 
      */
    public void setUserAgent(String uaName, String version);

    /**
      * Sets the UDP port range from which to randomly select the port used for video
      * streaming. 
      */
    public void setVideoPortRange(int minPort, int maxPort);

    /**
      * Tells whether a specified sound device can capture sound. 
      */
    public boolean soundDeviceCanCapture(String device);

    /**
      * Tells whether a specified sound device can play sound. 
      */
    public boolean soundDeviceCanPlayback(String device);

    /**
      * Check if a call will need the sound resources in near future (typically an
      * outgoing call that is awaiting response). 
      */
    public boolean soundResourcesLocked();

    /**
      * Start a #LinphoneCore object after it has been instantiated. 
      */
    public void start();

    /**
      * Start recording the running conference. 
      */
    public void startConferenceRecording(String path);

    /**
      * Special function to warm up dtmf feeback stream. 
      */
    public void startDtmfStream();

    /**
      * Starts an echo calibration of the sound devices, in order to find adequate
      * settings for the echo canceler automatically. 
      */
    public void startEchoCancellerCalibration();

    /**
      * Start the simulation of call to test the latency with an external device. 
      */
    public void startEchoTester(int rate);

    /**
      * Stop recording the running conference. 
      */
    public void stopConferenceRecording();

    /**
      * Stops playing a dtmf started by linphone_core_play_dtmf(). 
      */
    public void stopDtmf();

    /**
      * Special function to stop dtmf feed back function. 
      */
    public void stopDtmfStream();

    /**
      * Stop the simulation of call. 
      */
    public void stopEchoTester();

    /**
      * Whenever the liblinphone is playing a ring to advertise an incoming call or
      * ringback of an outgoing call, this function stops the ringing. 
      */
    public void stopRinging();

    /**
      * Create an outgoing subscription, specifying the destination resource, the event
      * name, and an optional content body. 
      */
    public Event subscribe(Address resource, String event, int expires, Content body);

    /**
      * Take a photo of currently from capture device and write it into a jpeg file. 
      */
    public void takePreviewSnapshot(String file);

    /**
      * Terminates all the calls. 
      */
    public void terminateAllCalls();

    /**
      * Terminates a call. 
      */
    @Deprecated
    public void terminateCall(Call call);

    /**
      * Terminate the running conference. 
      */
    public void terminateConference();

    /**
      * Performs a simple call transfer to the specified destination. 
      */
    @Deprecated
    public void transferCall(Call call, String referTo);

    /**
      * Transfers a call to destination of another running call. 
      */
    @Deprecated
    public void transferCallToAnother(Call call, Call dest);

    /**
      * Updates a running call according to supplied call parameters or parameters
      * changed in the LinphoneCore. 
      */
    @Deprecated
    public void updateCall(Call call, CallParams params);

    /**
      * Upload the log collection to the configured server url. 
      */
    public void uploadLogCollection();

    /**
      * Tells the core to use a separate window for local camera preview video, instead
      * of inserting local view within the remote video window. 
      */
    public void usePreviewWindow(boolean yesno);

    /**
      * Specify whether the tls server certificate must be verified when connecting to
      * a SIP/TLS server. 
      */
    public void verifyServerCertificates(boolean yesno);

    /**
      * Specify whether the tls server certificate common name must be verified when
      * connecting to a SIP/TLS server. 
      */
    public void verifyServerCn(boolean yesno);

    /**
      * Test if video is supported. 
      */
    public boolean videoSupported();

    /**
      * Compress the log collection in a single file. 
      */
    public String compressLogCollection();

    /**
      * Enable the linphone core log collection to upload logs on a server. 
      */
    public void enableLogCollection(LogCollectionState state);

    /**
      * Get the max file size in bytes of the files used for log collection. 
      */
    public int getLogCollectionMaxFileSize();

    /**
      * Get the path where the log files will be written for log collection. 
      */
    public String getLogCollectionPath();

    /**
      * Get the prefix of the filenames that will be used for log collection. 
      */
    public String getLogCollectionPrefix();

    /**
      * Get defined log level mask. 
      */
    @Deprecated
    public int getLogLevelMask();

    /**
      * Returns liblinphone's version as a string. 
      */
    public String getVersion();

    /**
      * Tells whether the linphone core log collection is enabled. 
      */
    public LogCollectionState logCollectionEnabled();

    /**
      * Reset the log collection by removing the log files. 
      */
    public void resetLogCollection();

    /**
      * Enable logs serialization (output logs from either the thread that creates the
      * linphone core or the thread that calls linphone_core_iterate()). 
      */
    public void serializeLogs();

    /**
      * Set the max file size in bytes of the files used for log collection. 
      */
    public void setLogCollectionMaxFileSize(int size);

    /**
      * Set the path of a directory where the log files will be written for log
      * collection. 
      */
    public void setLogCollectionPath(String path);

    /**
      * Set the prefix of the filenames that will be used for log collection. 
      */
    public void setLogCollectionPrefix(String prefix);

    /**
      * Define the log level using mask. 
      */
    @Deprecated
    public void setLogLevelMask(int mask);

    /**
      * True if tunnel support was compiled. 
      */
    public boolean tunnelAvailable();

    /**
      * Return the availability of uPnP. 
      */
    public boolean upnpAvailable();

    /**
      * Tells whether VCARD support is builtin. 
      */
    public boolean vcardSupported();

    public void addListener(CoreListener listener);

    public void removeListener(CoreListener listener);

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class CoreImpl implements Core {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected CoreImpl(long ptr) {
        nativePtr = ptr;
    }


    private native String getAdaptiveRateAlgorithm(long nativePtr);
    @Override
    synchronized public String getAdaptiveRateAlgorithm()  {
        
        return getAdaptiveRateAlgorithm(nativePtr);
    }

    private native void setAdaptiveRateAlgorithm(long nativePtr, String algorithm);
    @Override
    synchronized public void setAdaptiveRateAlgorithm(String algorithm)  {
        
        setAdaptiveRateAlgorithm(nativePtr, algorithm);
    }

    private native boolean adaptiveRateControlEnabled(long nativePtr);
    @Override
    synchronized public boolean adaptiveRateControlEnabled()  {
        
        return adaptiveRateControlEnabled(nativePtr);
    }

    private native void enableAdaptiveRateControl(long nativePtr, boolean enabled);
    @Override
    synchronized public void enableAdaptiveRateControl(boolean enabled)  {
        
        enableAdaptiveRateControl(nativePtr, enabled);
    }

    private native boolean audioAdaptiveJittcompEnabled(long nativePtr);
    @Override
    synchronized public boolean audioAdaptiveJittcompEnabled()  {
        
        return audioAdaptiveJittcompEnabled(nativePtr);
    }

    private native void enableAudioAdaptiveJittcomp(long nativePtr, boolean enable);
    @Override
    synchronized public void enableAudioAdaptiveJittcomp(boolean enable)  {
        
        enableAudioAdaptiveJittcomp(nativePtr, enable);
    }

    private native int getAudioDscp(long nativePtr);
    @Override
    synchronized public int getAudioDscp()  {
        
        return getAudioDscp(nativePtr);
    }

    private native void setAudioDscp(long nativePtr, int dscp);
    @Override
    synchronized public void setAudioDscp(int dscp)  {
        
        setAudioDscp(nativePtr, dscp);
    }

    private native int getAudioJittcomp(long nativePtr);
    @Override
    synchronized public int getAudioJittcomp()  {
        
        return getAudioJittcomp(nativePtr);
    }

    private native void setAudioJittcomp(long nativePtr, int milliseconds);
    @Override
    synchronized public void setAudioJittcomp(int milliseconds)  {
        
        setAudioJittcomp(nativePtr, milliseconds);
    }

    private native String getAudioMulticastAddr(long nativePtr);
    @Override
    synchronized public String getAudioMulticastAddr()  {
        
        return getAudioMulticastAddr(nativePtr);
    }

    private native int setAudioMulticastAddr(long nativePtr, String ip);
    @Override
    synchronized public void setAudioMulticastAddr(String ip)  {
        
        setAudioMulticastAddr(nativePtr, ip);
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

    private native int getAudioMulticastTtl(long nativePtr);
    @Override
    synchronized public int getAudioMulticastTtl()  {
        
        return getAudioMulticastTtl(nativePtr);
    }

    private native int setAudioMulticastTtl(long nativePtr, int ttl);
    @Override
    synchronized public void setAudioMulticastTtl(int ttl)  {
        
        setAudioMulticastTtl(nativePtr, ttl);
    }

    private native PayloadType[] getAudioPayloadTypes(long nativePtr);
    @Override
    synchronized public PayloadType[] getAudioPayloadTypes()  {
        
        return getAudioPayloadTypes(nativePtr);
    }

    private native void setAudioPayloadTypes(long nativePtr, PayloadType[] payloadTypes);
    @Override
    synchronized public void setAudioPayloadTypes(PayloadType[] payloadTypes)  {
        
        setAudioPayloadTypes(nativePtr, payloadTypes);
    }

    private native int getAudioPort(long nativePtr);
    @Override
    synchronized public int getAudioPort()  {
        
        return getAudioPort(nativePtr);
    }

    private native void setAudioPort(long nativePtr, int port);
    @Override
    synchronized public void setAudioPort(int port)  {
        
        setAudioPort(nativePtr, port);
    }

    private native Range getAudioPortsRange(long nativePtr);
    @Override
    synchronized public Range getAudioPortsRange()  {
        
        return (Range)getAudioPortsRange(nativePtr);
    }

    private native AuthInfo[] getAuthInfoList(long nativePtr);
    @Override
    synchronized public AuthInfo[] getAuthInfoList()  {
        
        return getAuthInfoList(nativePtr);
    }

    private native int getAvpfMode(long nativePtr);
    @Override
    synchronized public AVPFMode getAvpfMode()  {
        
        return AVPFMode.fromInt(getAvpfMode(nativePtr));
    }

    private native void setAvpfMode(long nativePtr, int mode);
    @Override
    synchronized public void setAvpfMode(AVPFMode mode)  {
        
        setAvpfMode(nativePtr, mode.toInt());
    }

    private native int getAvpfRrInterval(long nativePtr);
    @Override
    synchronized public int getAvpfRrInterval()  {
        
        return getAvpfRrInterval(nativePtr);
    }

    private native void setAvpfRrInterval(long nativePtr, int interval);
    @Override
    synchronized public void setAvpfRrInterval(int interval)  {
        
        setAvpfRrInterval(nativePtr, interval);
    }

    private native CallLog[] getCallLogs(long nativePtr);
    @Override
    synchronized public CallLog[] getCallLogs()  {
        
        return getCallLogs(nativePtr);
    }

    private native void setCallLogsDatabaseFile(long nativePtr, String path);
    @Override
    synchronized public void setCallLogsDatabaseFile(String path)  {
        
        setCallLogsDatabaseFile(nativePtr, path);
    }

    private native String getCallLogsDatabasePath(long nativePtr);
    @Override
    synchronized public String getCallLogsDatabasePath()  {
        
        return getCallLogsDatabasePath(nativePtr);
    }

    private native void setCallLogsDatabasePath(long nativePtr, String path);
    @Override
    synchronized public void setCallLogsDatabasePath(String path)  {
        
        setCallLogsDatabasePath(nativePtr, path);
    }

    private native Call[] getCalls(long nativePtr);
    @Override
    synchronized public Call[] getCalls()  {
        
        return getCalls(nativePtr);
    }

    private native int getCallsNb(long nativePtr);
    @Override
    synchronized public int getCallsNb()  {
        
        return getCallsNb(nativePtr);
    }

    private native int getCameraSensorRotation(long nativePtr);
    @Override
    synchronized public int getCameraSensorRotation()  {
        
        return getCameraSensorRotation(nativePtr);
    }

    private native String getCaptureDevice(long nativePtr);
    @Override
    synchronized public String getCaptureDevice()  {
        
        return getCaptureDevice(nativePtr);
    }

    private native int setCaptureDevice(long nativePtr, String devid);
    @Override
    synchronized public void setCaptureDevice(String devid)  {
        
        setCaptureDevice(nativePtr, devid);
    }

    private native String getChatDatabasePath(long nativePtr);
    @Override
    synchronized public String getChatDatabasePath()  {
        
        return getChatDatabasePath(nativePtr);
    }

    private native void setChatDatabasePath(long nativePtr, String path);
    @Override
    synchronized public void setChatDatabasePath(String path)  {
        
        setChatDatabasePath(nativePtr, path);
    }

    private native boolean chatEnabled(long nativePtr);
    @Override
    synchronized public boolean chatEnabled()  {
        
        return chatEnabled(nativePtr);
    }

    private native ChatRoom[] getChatRooms(long nativePtr);
    @Override
    synchronized public ChatRoom[] getChatRooms()  {
        
        return getChatRooms(nativePtr);
    }

    private native Conference getConference(long nativePtr);
    @Override
    synchronized public Conference getConference()  {
        
        return (Conference)getConference(nativePtr);
    }

    private native float getConferenceLocalInputVolume(long nativePtr);
    @Override
    synchronized public float getConferenceLocalInputVolume()  {
        
        return getConferenceLocalInputVolume(nativePtr);
    }

    private native boolean conferenceServerEnabled(long nativePtr);
    @Override
    synchronized public boolean conferenceServerEnabled()  {
        
        return conferenceServerEnabled(nativePtr);
    }

    private native void enableConferenceServer(long nativePtr, boolean enable);
    @Override
    synchronized public void enableConferenceServer(boolean enable)  {
        
        enableConferenceServer(nativePtr, enable);
    }

    private native int getConferenceSize(long nativePtr);
    @Override
    synchronized public int getConferenceSize()  {
        
        return getConferenceSize(nativePtr);
    }

    private native Config getConfig(long nativePtr);
    @Override
    synchronized public Config getConfig()  {
        
        return (Config)getConfig(nativePtr);
    }

    private native int getConsolidatedPresence(long nativePtr);
    @Override
    synchronized public ConsolidatedPresence getConsolidatedPresence()  {
        
        return ConsolidatedPresence.fromInt(getConsolidatedPresence(nativePtr));
    }

    private native void setConsolidatedPresence(long nativePtr, int presence);
    @Override
    synchronized public void setConsolidatedPresence(ConsolidatedPresence presence)  {
        
        setConsolidatedPresence(nativePtr, presence.toInt());
    }

    private native Call getCurrentCall(long nativePtr);
    @Override
    synchronized public Call getCurrentCall()  {
        
        return (Call)getCurrentCall(nativePtr);
    }

    private native Address getCurrentCallRemoteAddress(long nativePtr);
    @Override
    synchronized public Address getCurrentCallRemoteAddress()  {
        
        return (Address)getCurrentCallRemoteAddress(nativePtr);
    }

    private native VideoDefinition getCurrentPreviewVideoDefinition(long nativePtr);
    @Override
    synchronized public VideoDefinition getCurrentPreviewVideoDefinition()  {
        
        return (VideoDefinition)getCurrentPreviewVideoDefinition(nativePtr);
    }

    private native FriendList getDefaultFriendList(long nativePtr);
    @Override
    synchronized public FriendList getDefaultFriendList()  {
        
        return (FriendList)getDefaultFriendList(nativePtr);
    }

    private native ProxyConfig getDefaultProxyConfig(long nativePtr);
    @Override
    synchronized public ProxyConfig getDefaultProxyConfig()  {
        
        return (ProxyConfig)getDefaultProxyConfig(nativePtr);
    }

    private native void setDefaultProxyConfig(long nativePtr, ProxyConfig config);
    @Override
    synchronized public void setDefaultProxyConfig(ProxyConfig config)  {
        
        setDefaultProxyConfig(nativePtr, config);
    }

    private native int getDelayedTimeout(long nativePtr);
    @Override
    synchronized public int getDelayedTimeout()  {
        
        return getDelayedTimeout(nativePtr);
    }

    private native void setDelayedTimeout(long nativePtr, int seconds);
    @Override
    synchronized public void setDelayedTimeout(int seconds)  {
        
        setDelayedTimeout(nativePtr, seconds);
    }

    private native int getDeviceRotation(long nativePtr);
    @Override
    synchronized public int getDeviceRotation()  {
        
        return getDeviceRotation(nativePtr);
    }

    private native void setDeviceRotation(long nativePtr, int rotation);
    @Override
    synchronized public void setDeviceRotation(int rotation)  {
        
        setDeviceRotation(nativePtr, rotation);
    }

    private native boolean dnsSearchEnabled(long nativePtr);
    @Override
    synchronized public boolean dnsSearchEnabled()  {
        
        return dnsSearchEnabled(nativePtr);
    }

    private native void enableDnsSearch(long nativePtr, boolean enable);
    @Override
    synchronized public void enableDnsSearch(boolean enable)  {
        
        enableDnsSearch(nativePtr, enable);
    }

    private native void setDnsServers(long nativePtr, String[] servers);
    @Override
    synchronized public void setDnsServers(String[] servers)  {
        
        setDnsServers(nativePtr, servers);
    }

    private native void setDnsServersApp(long nativePtr, String[] servers);
    @Override
    synchronized public void setDnsServersApp(String[] servers)  {
        
        setDnsServersApp(nativePtr, servers);
    }

    private native boolean getDnsSetByApp(long nativePtr);
    @Override
    synchronized public boolean getDnsSetByApp()  {
        
        return getDnsSetByApp(nativePtr);
    }

    private native boolean dnsSrvEnabled(long nativePtr);
    @Override
    synchronized public boolean dnsSrvEnabled()  {
        
        return dnsSrvEnabled(nativePtr);
    }

    private native void enableDnsSrv(long nativePtr, boolean enable);
    @Override
    synchronized public void enableDnsSrv(boolean enable)  {
        
        enableDnsSrv(nativePtr, enable);
    }

    private native int getDownloadBandwidth(long nativePtr);
    @Override
    synchronized public int getDownloadBandwidth()  {
        
        return getDownloadBandwidth(nativePtr);
    }

    private native void setDownloadBandwidth(long nativePtr, int bw);
    @Override
    synchronized public void setDownloadBandwidth(int bw)  {
        
        setDownloadBandwidth(nativePtr, bw);
    }

    private native int getDownloadPtime(long nativePtr);
    @Override
    synchronized public int getDownloadPtime()  {
        
        return getDownloadPtime(nativePtr);
    }

    private native void setDownloadPtime(long nativePtr, int ptime);
    @Override
    synchronized public void setDownloadPtime(int ptime)  {
        
        setDownloadPtime(nativePtr, ptime);
    }

    private native boolean echoCancellationEnabled(long nativePtr);
    @Override
    synchronized public boolean echoCancellationEnabled()  {
        
        return echoCancellationEnabled(nativePtr);
    }

    private native void enableEchoCancellation(long nativePtr, boolean val);
    @Override
    synchronized public void enableEchoCancellation(boolean val)  {
        
        enableEchoCancellation(nativePtr, val);
    }

    private native String getEchoCancellerFilterName(long nativePtr);
    @Override
    synchronized public String getEchoCancellerFilterName()  {
        
        return getEchoCancellerFilterName(nativePtr);
    }

    private native void setEchoCancellerFilterName(long nativePtr, String filtername);
    @Override
    synchronized public void setEchoCancellerFilterName(String filtername)  {
        
        setEchoCancellerFilterName(nativePtr, filtername);
    }

    private native boolean echoLimiterEnabled(long nativePtr);
    @Override
    synchronized public boolean echoLimiterEnabled()  {
        
        return echoLimiterEnabled(nativePtr);
    }

    private native void enableEchoLimiter(long nativePtr, boolean val);
    @Override
    synchronized public void enableEchoLimiter(boolean val)  {
        
        enableEchoLimiter(nativePtr, val);
    }

    private native void setExpectedBandwidth(long nativePtr, int bw);
    @Override
    synchronized public void setExpectedBandwidth(int bw)  {
        
        setExpectedBandwidth(nativePtr, bw);
    }

    private native String getFileTransferServer(long nativePtr);
    @Override
    synchronized public String getFileTransferServer()  {
        
        return getFileTransferServer(nativePtr);
    }

    private native void setFileTransferServer(long nativePtr, String serverUrl);
    @Override
    synchronized public void setFileTransferServer(String serverUrl)  {
        
        setFileTransferServer(nativePtr, serverUrl);
    }

    private native String getFriendsDatabasePath(long nativePtr);
    @Override
    synchronized public String getFriendsDatabasePath()  {
        
        return getFriendsDatabasePath(nativePtr);
    }

    private native void setFriendsDatabasePath(long nativePtr, String path);
    @Override
    synchronized public void setFriendsDatabasePath(String path)  {
        
        setFriendsDatabasePath(nativePtr, path);
    }

    private native FriendList[] getFriendsLists(long nativePtr);
    @Override
    synchronized public FriendList[] getFriendsLists()  {
        
        return getFriendsLists(nativePtr);
    }

    private native boolean getGuessHostname(long nativePtr);
    @Override
    synchronized public boolean getGuessHostname()  {
        
        return getGuessHostname(nativePtr);
    }

    private native void setGuessHostname(long nativePtr, boolean val);
    @Override
    synchronized public void setGuessHostname(boolean val)  {
        
        setGuessHostname(nativePtr, val);
    }

    private native String getHttpProxyHost(long nativePtr);
    @Override
    synchronized public String getHttpProxyHost()  {
        
        return getHttpProxyHost(nativePtr);
    }

    private native void setHttpProxyHost(long nativePtr, String host);
    @Override
    synchronized public void setHttpProxyHost(String host)  {
        
        setHttpProxyHost(nativePtr, host);
    }

    private native int getHttpProxyPort(long nativePtr);
    @Override
    synchronized public int getHttpProxyPort()  {
        
        return getHttpProxyPort(nativePtr);
    }

    private native void setHttpProxyPort(long nativePtr, int port);
    @Override
    synchronized public void setHttpProxyPort(int port)  {
        
        setHttpProxyPort(nativePtr, port);
    }

    private native String getIdentity(long nativePtr);
    @Override
    synchronized public String getIdentity()  {
        
        return getIdentity(nativePtr);
    }

    private native ImNotifPolicy getImNotifPolicy(long nativePtr);
    @Override
    synchronized public ImNotifPolicy getImNotifPolicy()  {
        
        return (ImNotifPolicy)getImNotifPolicy(nativePtr);
    }

    private native int getInCallTimeout(long nativePtr);
    @Override
    synchronized public int getInCallTimeout()  {
        
        return getInCallTimeout(nativePtr);
    }

    private native void setInCallTimeout(long nativePtr, int seconds);
    @Override
    synchronized public void setInCallTimeout(int seconds)  {
        
        setInCallTimeout(nativePtr, seconds);
    }

    private native int getIncTimeout(long nativePtr);
    @Override
    synchronized public int getIncTimeout()  {
        
        return getIncTimeout(nativePtr);
    }

    private native void setIncTimeout(long nativePtr, int seconds);
    @Override
    synchronized public void setIncTimeout(int seconds)  {
        
        setIncTimeout(nativePtr, seconds);
    }

    private native boolean ipv6Enabled(long nativePtr);
    @Override
    synchronized public boolean ipv6Enabled()  {
        
        return ipv6Enabled(nativePtr);
    }

    private native void enableIpv6(long nativePtr, boolean val);
    @Override
    synchronized public void enableIpv6(boolean val)  {
        
        enableIpv6(nativePtr, val);
    }

    private native boolean isEchoCancellerCalibrationRequired(long nativePtr);
    @Override
    synchronized public boolean isEchoCancellerCalibrationRequired()  {
        
        return isEchoCancellerCalibrationRequired(nativePtr);
    }

    private native boolean isInConference(long nativePtr);
    @Override
    synchronized public boolean isInConference()  {
        
        return isInConference(nativePtr);
    }

    private native boolean isIncomingInvitePending(long nativePtr);
    @Override
    synchronized public boolean isIncomingInvitePending()  {
        
        return isIncomingInvitePending(nativePtr);
    }

    private native boolean isMediaEncryptionMandatory(long nativePtr);
    @Override
    synchronized public boolean isMediaEncryptionMandatory()  {
        
        return isMediaEncryptionMandatory(nativePtr);
    }

    private native boolean isNetworkReachable(long nativePtr);
    @Override
    synchronized public boolean isNetworkReachable()  {
        
        return isNetworkReachable(nativePtr);
    }

    private native boolean keepAliveEnabled(long nativePtr);
    @Override
    synchronized public boolean keepAliveEnabled()  {
        
        return keepAliveEnabled(nativePtr);
    }

    private native void enableKeepAlive(long nativePtr, boolean enable);
    @Override
    synchronized public void enableKeepAlive(boolean enable)  {
        
        enableKeepAlive(nativePtr, enable);
    }

    private native CallLog getLastOutgoingCallLog(long nativePtr);
    @Override
    synchronized public CallLog getLastOutgoingCallLog()  {
        
        return (CallLog)getLastOutgoingCallLog(nativePtr);
    }

    private native String getLinphoneSpecs(long nativePtr);
    @Override
    synchronized public String getLinphoneSpecs()  {
        
        return getLinphoneSpecs(nativePtr);
    }

    private native void setLinphoneSpecs(long nativePtr, String specs);
    @Override
    synchronized public void setLinphoneSpecs(String specs)  {
        
        setLinphoneSpecs(nativePtr, specs);
    }

    private native String getLogCollectionUploadServerUrl(long nativePtr);
    @Override
    synchronized public String getLogCollectionUploadServerUrl()  {
        
        return getLogCollectionUploadServerUrl(nativePtr);
    }

    private native void setLogCollectionUploadServerUrl(long nativePtr, String serverUrl);
    @Override
    synchronized public void setLogCollectionUploadServerUrl(String serverUrl)  {
        
        setLogCollectionUploadServerUrl(nativePtr, serverUrl);
    }

    private native int getMaxCalls(long nativePtr);
    @Override
    synchronized public int getMaxCalls()  {
        
        return getMaxCalls(nativePtr);
    }

    private native void setMaxCalls(long nativePtr, int max);
    @Override
    synchronized public void setMaxCalls(int max)  {
        
        setMaxCalls(nativePtr, max);
    }

    private native int getMediaEncryption(long nativePtr);
    @Override
    synchronized public MediaEncryption getMediaEncryption()  {
        
        return MediaEncryption.fromInt(getMediaEncryption(nativePtr));
    }

    private native int setMediaEncryption(long nativePtr, int menc);
    @Override
    synchronized public void setMediaEncryption(MediaEncryption menc)  {
        
        setMediaEncryption(nativePtr, menc.toInt());
    }

    private native void setMediaEncryptionMandatory(long nativePtr, boolean m);
    @Override
    synchronized public void setMediaEncryptionMandatory(boolean m)  {
        
        setMediaEncryptionMandatory(nativePtr, m);
    }

    private native void setMediaNetworkReachable(long nativePtr, boolean value);
    @Override
    synchronized public void setMediaNetworkReachable(boolean value)  {
        
        setMediaNetworkReachable(nativePtr, value);
    }

    private native boolean micEnabled(long nativePtr);
    @Override
    synchronized public boolean micEnabled()  {
        
        return micEnabled(nativePtr);
    }

    private native void enableMic(long nativePtr, boolean enable);
    @Override
    synchronized public void enableMic(boolean enable)  {
        
        enableMic(nativePtr, enable);
    }

    private native float getMicGainDb(long nativePtr);
    @Override
    synchronized public float getMicGainDb()  {
        
        return getMicGainDb(nativePtr);
    }

    private native void setMicGainDb(long nativePtr, float level);
    @Override
    synchronized public void setMicGainDb(float level)  {
        
        setMicGainDb(nativePtr, level);
    }

    private native int getMissedCallsCount(long nativePtr);
    @Override
    synchronized public int getMissedCallsCount()  {
        
        return getMissedCallsCount(nativePtr);
    }

    private native int getMtu(long nativePtr);
    @Override
    synchronized public int getMtu()  {
        
        return getMtu(nativePtr);
    }

    private native void setMtu(long nativePtr, int mtu);
    @Override
    synchronized public void setMtu(int mtu)  {
        
        setMtu(nativePtr, mtu);
    }

    private native String getNatAddress(long nativePtr);
    @Override
    synchronized public String getNatAddress()  {
        
        return getNatAddress(nativePtr);
    }

    private native void setNatAddress(long nativePtr, String addr);
    @Override
    synchronized public void setNatAddress(String addr)  {
        
        setNatAddress(nativePtr, addr);
    }

    private native NatPolicy getNatPolicy(long nativePtr);
    @Override
    synchronized public NatPolicy getNatPolicy()  {
        
        return (NatPolicy)getNatPolicy(nativePtr);
    }

    private native void setNatPolicy(long nativePtr, NatPolicy policy);
    @Override
    synchronized public void setNatPolicy(NatPolicy policy)  {
        
        setNatPolicy(nativePtr, policy);
    }

    private native void getNativePreviewWindowId(long nativePtr);
    @Override
    synchronized public void getNativePreviewWindowId()  {
        
        getNativePreviewWindowId(nativePtr);
    }

    private native void setNativePreviewWindowId(long nativePtr, Object id);
    @Override
    synchronized public void setNativePreviewWindowId(Object id)  {
        
        setNativePreviewWindowId(nativePtr, id);
    }

    private native void getNativeVideoWindowId(long nativePtr);
    @Override
    synchronized public void getNativeVideoWindowId()  {
        
        getNativeVideoWindowId(nativePtr);
    }

    private native void setNativeVideoWindowId(long nativePtr, Object id);
    @Override
    synchronized public void setNativeVideoWindowId(Object id)  {
        
        setNativeVideoWindowId(nativePtr, id);
    }

    private native void setNetworkReachable(long nativePtr, boolean value);
    @Override
    synchronized public void setNetworkReachable(boolean value)  {
        
        setNetworkReachable(nativePtr, value);
    }

    private native int getNortpTimeout(long nativePtr);
    @Override
    synchronized public int getNortpTimeout()  {
        
        return getNortpTimeout(nativePtr);
    }

    private native void setNortpTimeout(long nativePtr, int seconds);
    @Override
    synchronized public void setNortpTimeout(int seconds)  {
        
        setNortpTimeout(nativePtr, seconds);
    }

    private native String getPlayFile(long nativePtr);
    @Override
    synchronized public String getPlayFile()  {
        
        return getPlayFile(nativePtr);
    }

    private native void setPlayFile(long nativePtr, String file);
    @Override
    synchronized public void setPlayFile(String file)  {
        
        setPlayFile(nativePtr, file);
    }

    private native String getPlaybackDevice(long nativePtr);
    @Override
    synchronized public String getPlaybackDevice()  {
        
        return getPlaybackDevice(nativePtr);
    }

    private native int setPlaybackDevice(long nativePtr, String devid);
    @Override
    synchronized public void setPlaybackDevice(String devid)  {
        
        setPlaybackDevice(nativePtr, devid);
    }

    private native float getPlaybackGainDb(long nativePtr);
    @Override
    synchronized public float getPlaybackGainDb()  {
        
        return getPlaybackGainDb(nativePtr);
    }

    private native void setPlaybackGainDb(long nativePtr, float level);
    @Override
    synchronized public void setPlaybackGainDb(float level)  {
        
        setPlaybackGainDb(nativePtr, level);
    }

    private native float getPreferredFramerate(long nativePtr);
    @Override
    synchronized public float getPreferredFramerate()  {
        
        return getPreferredFramerate(nativePtr);
    }

    private native void setPreferredFramerate(long nativePtr, float fps);
    @Override
    synchronized public void setPreferredFramerate(float fps)  {
        
        setPreferredFramerate(nativePtr, fps);
    }

    private native VideoDefinition getPreferredVideoDefinition(long nativePtr);
    @Override
    synchronized public VideoDefinition getPreferredVideoDefinition()  {
        
        return (VideoDefinition)getPreferredVideoDefinition(nativePtr);
    }

    private native void setPreferredVideoDefinition(long nativePtr, VideoDefinition vdef);
    @Override
    synchronized public void setPreferredVideoDefinition(VideoDefinition vdef)  {
        
        setPreferredVideoDefinition(nativePtr, vdef);
    }

    private native void setPreferredVideoSizeByName(long nativePtr, String name);
    @Override
    synchronized public void setPreferredVideoSizeByName(String name)  {
        
        setPreferredVideoSizeByName(nativePtr, name);
    }

    private native PresenceModel getPresenceModel(long nativePtr);
    @Override
    synchronized public PresenceModel getPresenceModel()  {
        
        return (PresenceModel)getPresenceModel(nativePtr);
    }

    private native void setPresenceModel(long nativePtr, PresenceModel presence);
    @Override
    synchronized public void setPresenceModel(PresenceModel presence)  {
        
        setPresenceModel(nativePtr, presence);
    }

    private native VideoDefinition getPreviewVideoDefinition(long nativePtr);
    @Override
    synchronized public VideoDefinition getPreviewVideoDefinition()  {
        
        return (VideoDefinition)getPreviewVideoDefinition(nativePtr);
    }

    private native void setPreviewVideoDefinition(long nativePtr, VideoDefinition vdef);
    @Override
    synchronized public void setPreviewVideoDefinition(VideoDefinition vdef)  {
        
        setPreviewVideoDefinition(nativePtr, vdef);
    }

    private native void setPreviewVideoSizeByName(long nativePtr, String name);
    @Override
    synchronized public void setPreviewVideoSizeByName(String name)  {
        
        setPreviewVideoSizeByName(nativePtr, name);
    }

    private native String getPrimaryContact(long nativePtr);
    @Override
    synchronized public String getPrimaryContact()  {
        
        return getPrimaryContact(nativePtr);
    }

    private native int setPrimaryContact(long nativePtr, String contact);
    @Override
    synchronized public void setPrimaryContact(String contact)  {
        
        setPrimaryContact(nativePtr, contact);
    }

    private native Address getPrimaryContactParsed(long nativePtr);
    @Override
    synchronized public Address getPrimaryContactParsed()  {
        
        return (Address)getPrimaryContactParsed(nativePtr);
    }

    private native String getProvisioningUri(long nativePtr);
    @Override
    synchronized public String getProvisioningUri()  {
        
        return getProvisioningUri(nativePtr);
    }

    private native int setProvisioningUri(long nativePtr, String uri);
    @Override
    synchronized public void setProvisioningUri(String uri)  {
        
        setProvisioningUri(nativePtr, uri);
    }

    private native ProxyConfig[] getProxyConfigList(long nativePtr);
    @Override
    synchronized public ProxyConfig[] getProxyConfigList()  {
        
        return getProxyConfigList(nativePtr);
    }

    private native boolean qrcodeVideoPreviewEnabled(long nativePtr);
    @Override
    synchronized public boolean qrcodeVideoPreviewEnabled()  {
        
        return qrcodeVideoPreviewEnabled(nativePtr);
    }

    private native void enableQrcodeVideoPreview(long nativePtr, boolean val);
    @Override
    synchronized public void enableQrcodeVideoPreview(boolean val)  {
        
        enableQrcodeVideoPreview(nativePtr, val);
    }

    private native boolean realtimeTextEnabled(long nativePtr);
    @Override
    synchronized public boolean realtimeTextEnabled()  {
        
        return realtimeTextEnabled(nativePtr);
    }

    private native String getRecordFile(long nativePtr);
    @Override
    synchronized public String getRecordFile()  {
        
        return getRecordFile(nativePtr);
    }

    private native void setRecordFile(long nativePtr, String file);
    @Override
    synchronized public void setRecordFile(String file)  {
        
        setRecordFile(nativePtr, file);
    }

    private native String getRemoteRingbackTone(long nativePtr);
    @Override
    synchronized public String getRemoteRingbackTone()  {
        
        return getRemoteRingbackTone(nativePtr);
    }

    private native void setRemoteRingbackTone(long nativePtr, String ring);
    @Override
    synchronized public void setRemoteRingbackTone(String ring)  {
        
        setRemoteRingbackTone(nativePtr, ring);
    }

    private native String getRing(long nativePtr);
    @Override
    synchronized public String getRing()  {
        
        return getRing(nativePtr);
    }

    private native void setRing(long nativePtr, String path);
    @Override
    synchronized public void setRing(String path)  {
        
        setRing(nativePtr, path);
    }

    private native boolean getRingDuringIncomingEarlyMedia(long nativePtr);
    @Override
    synchronized public boolean getRingDuringIncomingEarlyMedia()  {
        
        return getRingDuringIncomingEarlyMedia(nativePtr);
    }

    private native void setRingDuringIncomingEarlyMedia(long nativePtr, boolean enable);
    @Override
    synchronized public void setRingDuringIncomingEarlyMedia(boolean enable)  {
        
        setRingDuringIncomingEarlyMedia(nativePtr, enable);
    }

    private native String getRingback(long nativePtr);
    @Override
    synchronized public String getRingback()  {
        
        return getRingback(nativePtr);
    }

    private native void setRingback(long nativePtr, String path);
    @Override
    synchronized public void setRingback(String path)  {
        
        setRingback(nativePtr, path);
    }

    private native String getRingerDevice(long nativePtr);
    @Override
    synchronized public String getRingerDevice()  {
        
        return getRingerDevice(nativePtr);
    }

    private native int setRingerDevice(long nativePtr, String devid);
    @Override
    synchronized public void setRingerDevice(String devid)  {
        
        setRingerDevice(nativePtr, devid);
    }

    private native String getRootCa(long nativePtr);
    @Override
    synchronized public String getRootCa()  {
        
        return getRootCa(nativePtr);
    }

    private native void setRootCa(long nativePtr, String path);
    @Override
    synchronized public void setRootCa(String path)  {
        
        setRootCa(nativePtr, path);
    }

    private native void setRootCaData(long nativePtr, String data);
    @Override
    synchronized public void setRootCaData(String data)  {
        
        setRootCaData(nativePtr, data);
    }

    private native boolean sdp200AckEnabled(long nativePtr);
    @Override
    synchronized public boolean sdp200AckEnabled()  {
        
        return sdp200AckEnabled(nativePtr);
    }

    private native void enableSdp200Ack(long nativePtr, boolean enable);
    @Override
    synchronized public void enableSdp200Ack(boolean enable)  {
        
        enableSdp200Ack(nativePtr, enable);
    }

    private native boolean selfViewEnabled(long nativePtr);
    @Override
    synchronized public boolean selfViewEnabled()  {
        
        return selfViewEnabled(nativePtr);
    }

    private native void enableSelfView(long nativePtr, boolean val);
    @Override
    synchronized public void enableSelfView(boolean val)  {
        
        enableSelfView(nativePtr, val);
    }

    private native int getSipDscp(long nativePtr);
    @Override
    synchronized public int getSipDscp()  {
        
        return getSipDscp(nativePtr);
    }

    private native void setSipDscp(long nativePtr, int dscp);
    @Override
    synchronized public void setSipDscp(int dscp)  {
        
        setSipDscp(nativePtr, dscp);
    }

    private native void setSipNetworkReachable(long nativePtr, boolean value);
    @Override
    synchronized public void setSipNetworkReachable(boolean value)  {
        
        setSipNetworkReachable(nativePtr, value);
    }

    private native int getSipTransportTimeout(long nativePtr);
    @Override
    synchronized public int getSipTransportTimeout()  {
        
        return getSipTransportTimeout(nativePtr);
    }

    private native void setSipTransportTimeout(long nativePtr, int timeoutMs);
    @Override
    synchronized public void setSipTransportTimeout(int timeoutMs)  {
        
        setSipTransportTimeout(nativePtr, timeoutMs);
    }

    private native String[] getSoundDevicesList(long nativePtr);
    @Override
    synchronized public String[] getSoundDevicesList()  {
        
        return getSoundDevicesList(nativePtr);
    }

    private native String getStaticPicture(long nativePtr);
    @Override
    synchronized public String getStaticPicture()  {
        
        return getStaticPicture(nativePtr);
    }

    private native int setStaticPicture(long nativePtr, String path);
    @Override
    synchronized public void setStaticPicture(String path)  {
        
        setStaticPicture(nativePtr, path);
    }

    private native float getStaticPictureFps(long nativePtr);
    @Override
    synchronized public float getStaticPictureFps()  {
        
        return getStaticPictureFps(nativePtr);
    }

    private native int setStaticPictureFps(long nativePtr, float fps);
    @Override
    synchronized public void setStaticPictureFps(float fps)  {
        
        setStaticPictureFps(nativePtr, fps);
    }

    private native String getStunServer(long nativePtr);
    @Override
    synchronized public String getStunServer()  {
        
        return getStunServer(nativePtr);
    }

    private native void setStunServer(long nativePtr, String server);
    @Override
    synchronized public void setStunServer(String server)  {
        
        setStunServer(nativePtr, server);
    }

    private native String[] getSupportedFileFormatsList(long nativePtr);
    @Override
    synchronized public String[] getSupportedFileFormatsList()  {
        
        return getSupportedFileFormatsList(nativePtr);
    }

    private native PayloadType[] getTextPayloadTypes(long nativePtr);
    @Override
    synchronized public PayloadType[] getTextPayloadTypes()  {
        
        return getTextPayloadTypes(nativePtr);
    }

    private native void setTextPayloadTypes(long nativePtr, PayloadType[] payloadTypes);
    @Override
    synchronized public void setTextPayloadTypes(PayloadType[] payloadTypes)  {
        
        setTextPayloadTypes(nativePtr, payloadTypes);
    }

    private native int getTextPort(long nativePtr);
    @Override
    synchronized public int getTextPort()  {
        
        return getTextPort(nativePtr);
    }

    private native void setTextPort(long nativePtr, int port);
    @Override
    synchronized public void setTextPort(int port)  {
        
        setTextPort(nativePtr, port);
    }

    private native Range getTextPortsRange(long nativePtr);
    @Override
    synchronized public Range getTextPortsRange()  {
        
        return (Range)getTextPortsRange(nativePtr);
    }

    private native String getTlsCert(long nativePtr);
    @Override
    synchronized public String getTlsCert()  {
        
        return getTlsCert(nativePtr);
    }

    private native void setTlsCert(long nativePtr, String tlsCert);
    @Override
    synchronized public void setTlsCert(String tlsCert)  {
        
        setTlsCert(nativePtr, tlsCert);
    }

    private native String getTlsCertPath(long nativePtr);
    @Override
    synchronized public String getTlsCertPath()  {
        
        return getTlsCertPath(nativePtr);
    }

    private native void setTlsCertPath(long nativePtr, String tlsCertPath);
    @Override
    synchronized public void setTlsCertPath(String tlsCertPath)  {
        
        setTlsCertPath(nativePtr, tlsCertPath);
    }

    private native String getTlsKey(long nativePtr);
    @Override
    synchronized public String getTlsKey()  {
        
        return getTlsKey(nativePtr);
    }

    private native void setTlsKey(long nativePtr, String tlsKey);
    @Override
    synchronized public void setTlsKey(String tlsKey)  {
        
        setTlsKey(nativePtr, tlsKey);
    }

    private native String getTlsKeyPath(long nativePtr);
    @Override
    synchronized public String getTlsKeyPath()  {
        
        return getTlsKeyPath(nativePtr);
    }

    private native void setTlsKeyPath(long nativePtr, String tlsKeyPath);
    @Override
    synchronized public void setTlsKeyPath(String tlsKeyPath)  {
        
        setTlsKeyPath(nativePtr, tlsKeyPath);
    }

    private native Transports getTransports(long nativePtr);
    @Override
    synchronized public Transports getTransports()  {
        
        return (Transports)getTransports(nativePtr);
    }

    private native int setTransports(long nativePtr, Transports transports);
    @Override
    synchronized public void setTransports(Transports transports)  {
        
        setTransports(nativePtr, transports);
    }

    private native Transports getTransportsUsed(long nativePtr);
    @Override
    synchronized public Transports getTransportsUsed()  {
        
        return (Transports)getTransportsUsed(nativePtr);
    }

    private native Tunnel getTunnel(long nativePtr);
    @Override
    synchronized public Tunnel getTunnel()  {
        
        return (Tunnel)getTunnel(nativePtr);
    }

    private native int getUnreadChatMessageCount(long nativePtr);
    @Override
    synchronized public int getUnreadChatMessageCount()  {
        
        return getUnreadChatMessageCount(nativePtr);
    }

    private native int getUnreadChatMessageCountFromActiveLocals(long nativePtr);
    @Override
    synchronized public int getUnreadChatMessageCountFromActiveLocals()  {
        
        return getUnreadChatMessageCountFromActiveLocals(nativePtr);
    }

    private native int getUploadBandwidth(long nativePtr);
    @Override
    synchronized public int getUploadBandwidth()  {
        
        return getUploadBandwidth(nativePtr);
    }

    private native void setUploadBandwidth(long nativePtr, int bw);
    @Override
    synchronized public void setUploadBandwidth(int bw)  {
        
        setUploadBandwidth(nativePtr, bw);
    }

    private native int getUploadPtime(long nativePtr);
    @Override
    synchronized public int getUploadPtime()  {
        
        return getUploadPtime(nativePtr);
    }

    private native void setUploadPtime(long nativePtr, int ptime);
    @Override
    synchronized public void setUploadPtime(int ptime)  {
        
        setUploadPtime(nativePtr, ptime);
    }

    private native String getUpnpExternalIpaddress(long nativePtr);
    @Override
    synchronized public String getUpnpExternalIpaddress()  {
        
        return getUpnpExternalIpaddress(nativePtr);
    }

    private native int getUpnpState(long nativePtr);
    @Override
    synchronized public UpnpState getUpnpState()  {
        
        return UpnpState.fromInt(getUpnpState(nativePtr));
    }

    private native boolean getUseFiles(long nativePtr);
    @Override
    synchronized public boolean getUseFiles()  {
        
        return getUseFiles(nativePtr);
    }

    private native void setUseFiles(long nativePtr, boolean yesno);
    @Override
    synchronized public void setUseFiles(boolean yesno)  {
        
        setUseFiles(nativePtr, yesno);
    }

    private native boolean getUseInfoForDtmf(long nativePtr);
    @Override
    synchronized public boolean getUseInfoForDtmf()  {
        
        return getUseInfoForDtmf(nativePtr);
    }

    private native void setUseInfoForDtmf(long nativePtr, boolean useInfo);
    @Override
    synchronized public void setUseInfoForDtmf(boolean useInfo)  {
        
        setUseInfoForDtmf(nativePtr, useInfo);
    }

    private native boolean getUseRfc2833ForDtmf(long nativePtr);
    @Override
    synchronized public boolean getUseRfc2833ForDtmf()  {
        
        return getUseRfc2833ForDtmf(nativePtr);
    }

    private native void setUseRfc2833ForDtmf(long nativePtr, boolean useRfc2833);
    @Override
    synchronized public void setUseRfc2833ForDtmf(boolean useRfc2833)  {
        
        setUseRfc2833ForDtmf(nativePtr, useRfc2833);
    }

    private native String getUserAgent(long nativePtr);
    @Override
    synchronized public String getUserAgent()  {
        
        return getUserAgent(nativePtr);
    }

    private native String getUserCertificatesPath(long nativePtr);
    @Override
    synchronized public String getUserCertificatesPath()  {
        
        return getUserCertificatesPath(nativePtr);
    }

    private native void setUserCertificatesPath(long nativePtr, String path);
    @Override
    synchronized public void setUserCertificatesPath(String path)  {
        
        setUserCertificatesPath(nativePtr, path);
    }

    private native VideoActivationPolicy getVideoActivationPolicy(long nativePtr);
    @Override
    synchronized public VideoActivationPolicy getVideoActivationPolicy()  {
        
        return (VideoActivationPolicy)getVideoActivationPolicy(nativePtr);
    }

    private native void setVideoActivationPolicy(long nativePtr, VideoActivationPolicy policy);
    @Override
    synchronized public void setVideoActivationPolicy(VideoActivationPolicy policy)  {
        
        setVideoActivationPolicy(nativePtr, policy);
    }

    private native boolean videoAdaptiveJittcompEnabled(long nativePtr);
    @Override
    synchronized public boolean videoAdaptiveJittcompEnabled()  {
        
        return videoAdaptiveJittcompEnabled(nativePtr);
    }

    private native void enableVideoAdaptiveJittcomp(long nativePtr, boolean enable);
    @Override
    synchronized public void enableVideoAdaptiveJittcomp(boolean enable)  {
        
        enableVideoAdaptiveJittcomp(nativePtr, enable);
    }

    private native boolean videoCaptureEnabled(long nativePtr);
    @Override
    synchronized public boolean videoCaptureEnabled()  {
        
        return videoCaptureEnabled(nativePtr);
    }

    private native void enableVideoCapture(long nativePtr, boolean enable);
    @Override
    synchronized public void enableVideoCapture(boolean enable)  {
        
        enableVideoCapture(nativePtr, enable);
    }

    private native String getVideoDevice(long nativePtr);
    @Override
    synchronized public String getVideoDevice()  {
        
        return getVideoDevice(nativePtr);
    }

    private native int setVideoDevice(long nativePtr, String id);
    @Override
    synchronized public void setVideoDevice(String id)  {
        
        setVideoDevice(nativePtr, id);
    }

    private native String[] getVideoDevicesList(long nativePtr);
    @Override
    synchronized public String[] getVideoDevicesList()  {
        
        return getVideoDevicesList(nativePtr);
    }

    private native boolean videoDisplayEnabled(long nativePtr);
    @Override
    synchronized public boolean videoDisplayEnabled()  {
        
        return videoDisplayEnabled(nativePtr);
    }

    private native void enableVideoDisplay(long nativePtr, boolean enable);
    @Override
    synchronized public void enableVideoDisplay(boolean enable)  {
        
        enableVideoDisplay(nativePtr, enable);
    }

    private native String getVideoDisplayFilter(long nativePtr);
    @Override
    synchronized public String getVideoDisplayFilter()  {
        
        return getVideoDisplayFilter(nativePtr);
    }

    private native void setVideoDisplayFilter(long nativePtr, String filtername);
    @Override
    synchronized public void setVideoDisplayFilter(String filtername)  {
        
        setVideoDisplayFilter(nativePtr, filtername);
    }

    private native int getVideoDscp(long nativePtr);
    @Override
    synchronized public int getVideoDscp()  {
        
        return getVideoDscp(nativePtr);
    }

    private native void setVideoDscp(long nativePtr, int dscp);
    @Override
    synchronized public void setVideoDscp(int dscp)  {
        
        setVideoDscp(nativePtr, dscp);
    }

    private native boolean videoEnabled(long nativePtr);
    @Override
    synchronized public boolean videoEnabled()  {
        
        return videoEnabled(nativePtr);
    }

    private native int getVideoJittcomp(long nativePtr);
    @Override
    synchronized public int getVideoJittcomp()  {
        
        return getVideoJittcomp(nativePtr);
    }

    private native void setVideoJittcomp(long nativePtr, int milliseconds);
    @Override
    synchronized public void setVideoJittcomp(int milliseconds)  {
        
        setVideoJittcomp(nativePtr, milliseconds);
    }

    private native String getVideoMulticastAddr(long nativePtr);
    @Override
    synchronized public String getVideoMulticastAddr()  {
        
        return getVideoMulticastAddr(nativePtr);
    }

    private native int setVideoMulticastAddr(long nativePtr, String ip);
    @Override
    synchronized public void setVideoMulticastAddr(String ip)  {
        
        setVideoMulticastAddr(nativePtr, ip);
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

    private native int getVideoMulticastTtl(long nativePtr);
    @Override
    synchronized public int getVideoMulticastTtl()  {
        
        return getVideoMulticastTtl(nativePtr);
    }

    private native int setVideoMulticastTtl(long nativePtr, int ttl);
    @Override
    synchronized public void setVideoMulticastTtl(int ttl)  {
        
        setVideoMulticastTtl(nativePtr, ttl);
    }

    private native PayloadType[] getVideoPayloadTypes(long nativePtr);
    @Override
    synchronized public PayloadType[] getVideoPayloadTypes()  {
        
        return getVideoPayloadTypes(nativePtr);
    }

    private native void setVideoPayloadTypes(long nativePtr, PayloadType[] payloadTypes);
    @Override
    synchronized public void setVideoPayloadTypes(PayloadType[] payloadTypes)  {
        
        setVideoPayloadTypes(nativePtr, payloadTypes);
    }

    private native int getVideoPort(long nativePtr);
    @Override
    synchronized public int getVideoPort()  {
        
        return getVideoPort(nativePtr);
    }

    private native void setVideoPort(long nativePtr, int port);
    @Override
    synchronized public void setVideoPort(int port)  {
        
        setVideoPort(nativePtr, port);
    }

    private native Range getVideoPortsRange(long nativePtr);
    @Override
    synchronized public Range getVideoPortsRange()  {
        
        return (Range)getVideoPortsRange(nativePtr);
    }

    private native String getVideoPreset(long nativePtr);
    @Override
    synchronized public String getVideoPreset()  {
        
        return getVideoPreset(nativePtr);
    }

    private native void setVideoPreset(long nativePtr, String preset);
    @Override
    synchronized public void setVideoPreset(String preset)  {
        
        setVideoPreset(nativePtr, preset);
    }

    private native boolean videoPreviewEnabled(long nativePtr);
    @Override
    synchronized public boolean videoPreviewEnabled()  {
        
        return videoPreviewEnabled(nativePtr);
    }

    private native void enableVideoPreview(long nativePtr, boolean val);
    @Override
    synchronized public void enableVideoPreview(boolean val)  {
        
        enableVideoPreview(nativePtr, val);
    }

    private native void enableVideoSourceReuse(long nativePtr, boolean enable);
    @Override
    synchronized public void enableVideoSourceReuse(boolean enable)  {
        
        enableVideoSourceReuse(nativePtr, enable);
    }

    private native String getZrtpSecretsFile(long nativePtr);
    @Override
    synchronized public String getZrtpSecretsFile()  {
        
        return getZrtpSecretsFile(nativePtr);
    }

    private native void setZrtpSecretsFile(long nativePtr, String file);
    @Override
    synchronized public void setZrtpSecretsFile(String file)  {
        
        setZrtpSecretsFile(nativePtr, file);
    }

    private native int acceptCall(long nativePtr, Call call);
    @Override
    synchronized public void acceptCall(Call call)  {
        
        acceptCall(nativePtr, call);
    }

    private native int acceptCallUpdate(long nativePtr, Call call, CallParams params);
    @Override
    synchronized public void acceptCallUpdate(Call call, CallParams params)  {
        
        acceptCallUpdate(nativePtr, call, params);
    }

    private native int acceptCallWithParams(long nativePtr, Call call, CallParams params);
    @Override
    synchronized public void acceptCallWithParams(Call call, CallParams params)  {
        
        acceptCallWithParams(nativePtr, call, params);
    }

    private native int acceptEarlyMedia(long nativePtr, Call call);
    @Override
    synchronized public void acceptEarlyMedia(Call call)  {
        
        acceptEarlyMedia(nativePtr, call);
    }

    private native int acceptEarlyMediaWithParams(long nativePtr, Call call, CallParams params);
    @Override
    synchronized public void acceptEarlyMediaWithParams(Call call, CallParams params)  {
        
        acceptEarlyMediaWithParams(nativePtr, call, params);
    }

    private native int addAllToConference(long nativePtr);
    @Override
    synchronized public void addAllToConference()  {
        
        addAllToConference(nativePtr);
    }

    private native void addAuthInfo(long nativePtr, AuthInfo info);
    @Override
    synchronized public void addAuthInfo(AuthInfo info)  {
        
        addAuthInfo(nativePtr, info);
    }

    private native void addFriend(long nativePtr, Friend fr);
    @Override
    synchronized public void addFriend(Friend fr)  {
        
        addFriend(nativePtr, fr);
    }

    private native void addFriendList(long nativePtr, FriendList list);
    @Override
    synchronized public void addFriendList(FriendList list)  {
        
        addFriendList(nativePtr, list);
    }

    private native int addProxyConfig(long nativePtr, ProxyConfig config);
    @Override
    synchronized public void addProxyConfig(ProxyConfig config)  {
        
        addProxyConfig(nativePtr, config);
    }

    private native void addSupportedTag(long nativePtr, String tag);
    @Override
    synchronized public void addSupportedTag(String tag)  {
        
        addSupportedTag(nativePtr, tag);
    }

    private native int addToConference(long nativePtr, Call call);
    @Override
    synchronized public void addToConference(Call call)  {
        
        addToConference(nativePtr, call);
    }

    private native void checkForUpdate(long nativePtr, String currentVersion);
    @Override
    synchronized public void checkForUpdate(String currentVersion)  {
        
        checkForUpdate(nativePtr, currentVersion);
    }

    private native void clearAllAuthInfo(long nativePtr);
    @Override
    synchronized public void clearAllAuthInfo()  {
        
        clearAllAuthInfo(nativePtr);
    }

    private native void clearCallLogs(long nativePtr);
    @Override
    synchronized public void clearCallLogs()  {
        
        clearCallLogs(nativePtr);
    }

    private native void clearProxyConfig(long nativePtr);
    @Override
    synchronized public void clearProxyConfig()  {
        
        clearProxyConfig(nativePtr);
    }

    private native AccountCreator createAccountCreator(long nativePtr, String xmlrpcUrl);
    @Override
    synchronized public AccountCreator createAccountCreator(String xmlrpcUrl)  {
        
        return (AccountCreator)createAccountCreator(nativePtr, xmlrpcUrl);
    }

    private native Address createAddress(long nativePtr, String address);
    @Override
    synchronized public Address createAddress(String address)  {
        
        return (Address)createAddress(nativePtr, address);
    }

    private native AuthInfo createAuthInfo(long nativePtr, String username, String userid, String passwd, String ha1, String realm, String domain);
    @Override
    synchronized public AuthInfo createAuthInfo(String username, String userid, String passwd, String ha1, String realm, String domain)  {
        
        return (AuthInfo)createAuthInfo(nativePtr, username, userid, passwd, ha1, realm, domain);
    }

    private native CallLog createCallLog(long nativePtr, Address from, Address to, int dir, int duration, long startTime, long connectedTime, int status, boolean videoEnabled, float quality);
    @Override
    synchronized public CallLog createCallLog(Address from, Address to, Call.Dir dir, int duration, long startTime, long connectedTime, Call.Status status, boolean videoEnabled, float quality)  {
        
        return (CallLog)createCallLog(nativePtr, from, to, dir.toInt(), duration, startTime, connectedTime, status.toInt(), videoEnabled, quality);
    }

    private native CallParams createCallParams(long nativePtr, Call call);
    @Override
    synchronized public CallParams createCallParams(Call call)  {
        
        return (CallParams)createCallParams(nativePtr, call);
    }

    private native ChatRoom createClientGroupChatRoom(long nativePtr, String subject, boolean fallback);
    @Override
    synchronized public ChatRoom createClientGroupChatRoom(String subject, boolean fallback)  {
        
        return (ChatRoom)createClientGroupChatRoom(nativePtr, subject, fallback);
    }

    private native ConferenceParams createConferenceParams(long nativePtr);
    @Override
    synchronized public ConferenceParams createConferenceParams()  {
        
        return (ConferenceParams)createConferenceParams(nativePtr);
    }

    private native Conference createConferenceWithParams(long nativePtr, ConferenceParams params);
    @Override
    synchronized public Conference createConferenceWithParams(ConferenceParams params)  {
        
        return (Conference)createConferenceWithParams(nativePtr, params);
    }

    private native Config createConfig(long nativePtr, String filename);
    @Override
    synchronized public Config createConfig(String filename)  {
        
        return (Config)createConfig(nativePtr, filename);
    }

    private native Content createContent(long nativePtr);
    @Override
    synchronized public Content createContent()  {
        
        return (Content)createContent(nativePtr);
    }

    private native Friend createFriend(long nativePtr);
    @Override
    synchronized public Friend createFriend()  {
        
        return (Friend)createFriend(nativePtr);
    }

    private native FriendList createFriendList(long nativePtr);
    @Override
    synchronized public FriendList createFriendList()  {
        
        return (FriendList)createFriendList(nativePtr);
    }

    private native Friend createFriendWithAddress(long nativePtr, String address);
    @Override
    synchronized public Friend createFriendWithAddress(String address)  {
        
        return (Friend)createFriendWithAddress(nativePtr, address);
    }

    private native InfoMessage createInfoMessage(long nativePtr);
    @Override
    synchronized public InfoMessage createInfoMessage()  {
        
        return (InfoMessage)createInfoMessage(nativePtr);
    }

    private native Player createLocalPlayer(long nativePtr, String soundCardName, String videoDisplayName, Object windowId);
    @Override
    synchronized public Player createLocalPlayer(String soundCardName, String videoDisplayName, Object windowId)  {
        
        return (Player)createLocalPlayer(nativePtr, soundCardName, videoDisplayName, windowId);
    }

    private native MagicSearch createMagicSearch(long nativePtr);
    @Override
    synchronized public MagicSearch createMagicSearch()  {
        
        return (MagicSearch)createMagicSearch(nativePtr);
    }

    private native NatPolicy createNatPolicy(long nativePtr);
    @Override
    synchronized public NatPolicy createNatPolicy()  {
        
        return (NatPolicy)createNatPolicy(nativePtr);
    }

    private native NatPolicy createNatPolicyFromConfig(long nativePtr, String ref);
    @Override
    synchronized public NatPolicy createNatPolicyFromConfig(String ref)  {
        
        return (NatPolicy)createNatPolicyFromConfig(nativePtr, ref);
    }

    private native Event createNotify(long nativePtr, Address resource, String event);
    @Override
    synchronized public Event createNotify(Address resource, String event)  {
        
        return (Event)createNotify(nativePtr, resource, event);
    }

    private native Event createOneShotPublish(long nativePtr, Address resource, String event);
    @Override
    synchronized public Event createOneShotPublish(Address resource, String event)  {
        
        return (Event)createOneShotPublish(nativePtr, resource, event);
    }

    private native PresenceActivity createPresenceActivity(long nativePtr, int acttype, String description);
    @Override
    synchronized public PresenceActivity createPresenceActivity(PresenceActivity.Type acttype, String description)  {
        
        return (PresenceActivity)createPresenceActivity(nativePtr, acttype.toInt(), description);
    }

    private native PresenceModel createPresenceModel(long nativePtr);
    @Override
    synchronized public PresenceModel createPresenceModel()  {
        
        return (PresenceModel)createPresenceModel(nativePtr);
    }

    private native PresenceModel createPresenceModelWithActivity(long nativePtr, int acttype, String description);
    @Override
    synchronized public PresenceModel createPresenceModelWithActivity(PresenceActivity.Type acttype, String description)  {
        
        return (PresenceModel)createPresenceModelWithActivity(nativePtr, acttype.toInt(), description);
    }

    private native PresenceModel createPresenceModelWithActivityAndNote(long nativePtr, int acttype, String description, String note, String lang);
    @Override
    synchronized public PresenceModel createPresenceModelWithActivityAndNote(PresenceActivity.Type acttype, String description, String note, String lang)  {
        
        return (PresenceModel)createPresenceModelWithActivityAndNote(nativePtr, acttype.toInt(), description, note, lang);
    }

    private native PresenceNote createPresenceNote(long nativePtr, String content, String lang);
    @Override
    synchronized public PresenceNote createPresenceNote(String content, String lang)  {
        
        return (PresenceNote)createPresenceNote(nativePtr, content, lang);
    }

    private native PresencePerson createPresencePerson(long nativePtr, String id);
    @Override
    synchronized public PresencePerson createPresencePerson(String id)  {
        
        return (PresencePerson)createPresencePerson(nativePtr, id);
    }

    private native PresenceService createPresenceService(long nativePtr, String id, int basicStatus, String contact);
    @Override
    synchronized public PresenceService createPresenceService(String id, PresenceBasicStatus basicStatus, String contact)  {
        
        return (PresenceService)createPresenceService(nativePtr, id, basicStatus.toInt(), contact);
    }

    private native ProxyConfig createProxyConfig(long nativePtr);
    @Override
    synchronized public ProxyConfig createProxyConfig()  {
        
        return (ProxyConfig)createProxyConfig(nativePtr);
    }

    private native Event createPublish(long nativePtr, Address resource, String event, int expires);
    @Override
    synchronized public Event createPublish(Address resource, String event, int expires)  {
        
        return (Event)createPublish(nativePtr, resource, event, expires);
    }

    private native Event createSubscribe(long nativePtr, Address resource, String event, int expires);
    @Override
    synchronized public Event createSubscribe(Address resource, String event, int expires)  {
        
        return (Event)createSubscribe(nativePtr, resource, event, expires);
    }

    private native XmlRpcSession createXmlRpcSession(long nativePtr, String url);
    @Override
    synchronized public XmlRpcSession createXmlRpcSession(String url)  {
        
        return (XmlRpcSession)createXmlRpcSession(nativePtr, url);
    }

    private native int declineCall(long nativePtr, Call call, int reason);
    @Override
    synchronized public void declineCall(Call call, Reason reason)  {
        
        declineCall(nativePtr, call, reason.toInt());
    }

    private native int deferCallUpdate(long nativePtr, Call call);
    @Override
    synchronized public void deferCallUpdate(Call call)  {
        
        deferCallUpdate(nativePtr, call);
    }

    private native void deleteChatRoom(long nativePtr, ChatRoom cr);
    @Override
    synchronized public void deleteChatRoom(ChatRoom cr)  {
        
        deleteChatRoom(nativePtr, cr);
    }

    private native void disableChat(long nativePtr, int denyReason);
    @Override
    synchronized public void disableChat(Reason denyReason)  {
        
        disableChat(nativePtr, denyReason.toInt());
    }

    private native void enableChat(long nativePtr);
    @Override
    synchronized public void enableChat()  {
        
        enableChat(nativePtr);
    }

    private native void enableLime(long nativePtr, int val);
    @Override
    synchronized public void enableLime(LimeState val)  {
        
        enableLime(nativePtr, val.toInt());
    }

    private native void enterBackground(long nativePtr);
    @Override
    synchronized public void enterBackground()  {
        
        enterBackground(nativePtr);
    }

    private native int enterConference(long nativePtr);
    @Override
    synchronized public void enterConference()  {
        
        enterConference(nativePtr);
    }

    private native void enterForeground(long nativePtr);
    @Override
    synchronized public void enterForeground()  {
        
        enterForeground(nativePtr);
    }

    private native boolean fileFormatSupported(long nativePtr, String fmt);
    @Override
    synchronized public boolean fileFormatSupported(String fmt)  {
        
        return fileFormatSupported(nativePtr, fmt);
    }

    private native AuthInfo findAuthInfo(long nativePtr, String realm, String username, String sipDomain);
    @Override
    synchronized public AuthInfo findAuthInfo(String realm, String username, String sipDomain)  {
        
        return (AuthInfo)findAuthInfo(nativePtr, realm, username, sipDomain);
    }

    private native Call findCallFromUri(long nativePtr, String uri);
    @Override
    synchronized public Call findCallFromUri(String uri)  {
        
        return (Call)findCallFromUri(nativePtr, uri);
    }

    private native CallLog findCallLogFromCallId(long nativePtr, String callId);
    @Override
    synchronized public CallLog findCallLogFromCallId(String callId)  {
        
        return (CallLog)findCallLogFromCallId(nativePtr, callId);
    }

    private native ChatRoom findChatRoom(long nativePtr, Address peerAddr, Address localAddr);
    @Override
    synchronized public ChatRoom findChatRoom(Address peerAddr, Address localAddr)  {
        
        return (ChatRoom)findChatRoom(nativePtr, peerAddr, localAddr);
    }

    private native Address[] findContactsByChar(long nativePtr, String filter, boolean sipOnly);
    @Override
    synchronized public Address[] findContactsByChar(String filter, boolean sipOnly)  {
        
        return findContactsByChar(nativePtr, filter, sipOnly);
    }

    private native Friend findFriend(long nativePtr, Address addr);
    @Override
    synchronized public Friend findFriend(Address addr)  {
        
        return (Friend)findFriend(nativePtr, addr);
    }

    private native ChatRoom findOneToOneChatRoom(long nativePtr, Address localAddr, Address participantAddr);
    @Override
    synchronized public ChatRoom findOneToOneChatRoom(Address localAddr, Address participantAddr)  {
        
        return (ChatRoom)findOneToOneChatRoom(nativePtr, localAddr, participantAddr);
    }

    private native Call getCallByRemoteAddress(long nativePtr, String remoteAddress);
    @Override
    synchronized public Call getCallByRemoteAddress(String remoteAddress)  {
        
        return (Call)getCallByRemoteAddress(nativePtr, remoteAddress);
    }

    private native Call getCallByRemoteAddress22(long nativePtr, Address remoteAddress);
    @Override
    synchronized public Call getCallByRemoteAddress2(Address remoteAddress)  {
        
        return (Call)getCallByRemoteAddress22(nativePtr, remoteAddress);
    }

    private native CallLog[] getCallHistoryForAddress(long nativePtr, Address addr);
    @Override
    synchronized public CallLog[] getCallHistoryForAddress(Address addr)  {
        
        return getCallHistoryForAddress(nativePtr, addr);
    }

    private native ChatRoom getChatRoom2(long nativePtr, Address peerAddr, Address localAddr);
    @Override
    synchronized public ChatRoom getChatRoom(Address peerAddr, Address localAddr)  {
        
        return (ChatRoom)getChatRoom2(nativePtr, peerAddr, localAddr);
    }

    private native ChatRoom getChatRoom(long nativePtr, Address addr);
    @Override
    synchronized public ChatRoom getChatRoom(Address addr)  {
        
        return (ChatRoom)getChatRoom(nativePtr, addr);
    }

    private native ChatRoom getChatRoomFromUri(long nativePtr, String to);
    @Override
    synchronized public ChatRoom getChatRoomFromUri(String to)  {
        
        return (ChatRoom)getChatRoomFromUri(nativePtr, to);
    }

    private native Friend getFriendByRefKey(long nativePtr, String key);
    @Override
    synchronized public Friend getFriendByRefKey(String key)  {
        
        return (Friend)getFriendByRefKey(nativePtr, key);
    }

    private native FriendList getFriendListByName(long nativePtr, String name);
    @Override
    synchronized public FriendList getFriendListByName(String name)  {
        
        return (FriendList)getFriendListByName(nativePtr, name);
    }

    private native PayloadType getPayloadType(long nativePtr, String type, int rate, int channels);
    @Override
    synchronized public PayloadType getPayloadType(String type, int rate, int channels)  {
        
        return (PayloadType)getPayloadType(nativePtr, type, rate, channels);
    }

    private native int getUnreadChatMessageCountFromLocal(long nativePtr, Address address);
    @Override
    synchronized public int getUnreadChatMessageCountFromLocal(Address address)  {
        
        return getUnreadChatMessageCountFromLocal(nativePtr, address);
    }

    private native boolean hasBuiltinEchoCanceller(long nativePtr);
    @Override
    synchronized public boolean hasBuiltinEchoCanceller()  {
        
        return hasBuiltinEchoCanceller(nativePtr);
    }

    private native boolean hasCrappyOpengl(long nativePtr);
    @Override
    synchronized public boolean hasCrappyOpengl()  {
        
        return hasCrappyOpengl(nativePtr);
    }

    private native boolean inCall(long nativePtr);
    @Override
    synchronized public boolean inCall()  {
        
        return inCall(nativePtr);
    }

    private native Address interpretUrl(long nativePtr, String url);
    @Override
    synchronized public Address interpretUrl(String url)  {
        
        return (Address)interpretUrl(nativePtr, url);
    }

    private native Call invite(long nativePtr, String url);
    @Override
    synchronized public Call invite(String url)  {
        
        return (Call)invite(nativePtr, url);
    }

    private native Call inviteAddress(long nativePtr, Address addr);
    @Override
    synchronized public Call inviteAddress(Address addr)  {
        
        return (Call)inviteAddress(nativePtr, addr);
    }

    private native Call inviteAddressWithParams(long nativePtr, Address addr, CallParams params);
    @Override
    synchronized public Call inviteAddressWithParams(Address addr, CallParams params)  {
        
        return (Call)inviteAddressWithParams(nativePtr, addr, params);
    }

    private native Call inviteWithParams(long nativePtr, String url, CallParams params);
    @Override
    synchronized public Call inviteWithParams(String url, CallParams params)  {
        
        return (Call)inviteWithParams(nativePtr, url, params);
    }

    private native void iterate(long nativePtr);
    @Override
    synchronized public void iterate()  {
        
        iterate(nativePtr);
    }

    private native int leaveConference(long nativePtr);
    @Override
    synchronized public void leaveConference()  {
        
        leaveConference(nativePtr);
    }

    private native boolean limeAvailable(long nativePtr);
    @Override
    synchronized public boolean limeAvailable()  {
        
        return limeAvailable(nativePtr);
    }

    private native int limeEnabled(long nativePtr);
    @Override
    synchronized public LimeState limeEnabled()  {
        
        return LimeState.fromInt(limeEnabled(nativePtr));
    }

    private native boolean mediaEncryptionSupported(long nativePtr, int menc);
    @Override
    synchronized public boolean mediaEncryptionSupported(MediaEncryption menc)  {
        
        return mediaEncryptionSupported(nativePtr, menc.toInt());
    }

    private native void migrateLogsFromRcToDb(long nativePtr);
    @Override
    synchronized public void migrateLogsFromRcToDb()  {
        
        migrateLogsFromRcToDb(nativePtr);
    }

    private native int migrateToMultiTransport(long nativePtr);
    @Override
    synchronized public void migrateToMultiTransport()  {
        
        migrateToMultiTransport(nativePtr);
    }

    private native void notifyAllFriends(long nativePtr, PresenceModel presence);
    @Override
    synchronized public void notifyAllFriends(PresenceModel presence)  {
        
        notifyAllFriends(nativePtr, presence);
    }

    private native void notifyNotifyPresenceReceived(long nativePtr, Friend lf);
    @Override
    synchronized public void notifyNotifyPresenceReceived(Friend lf)  {
        
        notifyNotifyPresenceReceived(nativePtr, lf);
    }

    private native void notifyNotifyPresenceReceivedForUriOrTel(long nativePtr, Friend lf, String uriOrTel, PresenceModel presenceModel);
    @Override
    synchronized public void notifyNotifyPresenceReceivedForUriOrTel(Friend lf, String uriOrTel, PresenceModel presenceModel)  {
        
        notifyNotifyPresenceReceivedForUriOrTel(nativePtr, lf, uriOrTel, presenceModel);
    }

    private native int pauseAllCalls(long nativePtr);
    @Override
    synchronized public void pauseAllCalls()  {
        
        pauseAllCalls(nativePtr);
    }

    private native int pauseCall(long nativePtr, Call call);
    @Override
    synchronized public void pauseCall(Call call)  {
        
        pauseCall(nativePtr, call);
    }

    private native void playDtmf(long nativePtr, char dtmf, int durationMs);
    @Override
    synchronized public void playDtmf(char dtmf, int durationMs)  {
        
        playDtmf(nativePtr, dtmf, durationMs);
    }

    private native int playLocal(long nativePtr, String audiofile);
    @Override
    synchronized public void playLocal(String audiofile)  {
        
        playLocal(nativePtr, audiofile);
    }

    private native void previewOglRender(long nativePtr);
    @Override
    synchronized public void previewOglRender()  {
        
        previewOglRender(nativePtr);
    }

    private native Event publish(long nativePtr, Address resource, String event, int expires, Content body);
    @Override
    synchronized public Event publish(Address resource, String event, int expires, Content body)  {
        
        return (Event)publish(nativePtr, resource, event, expires, body);
    }

    private native int redirectCall(long nativePtr, Call call, String redirectUri);
    @Override
    synchronized public void redirectCall(Call call, String redirectUri)  {
        
        redirectCall(nativePtr, call, redirectUri);
    }

    private native void refreshRegisters(long nativePtr);
    @Override
    synchronized public void refreshRegisters()  {
        
        refreshRegisters(nativePtr);
    }

    private native void rejectSubscriber(long nativePtr, Friend lf);
    @Override
    synchronized public void rejectSubscriber(Friend lf)  {
        
        rejectSubscriber(nativePtr, lf);
    }

    private native void reloadMsPlugins(long nativePtr, String path);
    @Override
    synchronized public void reloadMsPlugins(String path)  {
        
        reloadMsPlugins(nativePtr, path);
    }

    private native void reloadSoundDevices(long nativePtr);
    @Override
    synchronized public void reloadSoundDevices()  {
        
        reloadSoundDevices(nativePtr);
    }

    private native void reloadVideoDevices(long nativePtr);
    @Override
    synchronized public void reloadVideoDevices()  {
        
        reloadVideoDevices(nativePtr);
    }

    private native void removeAuthInfo(long nativePtr, AuthInfo info);
    @Override
    synchronized public void removeAuthInfo(AuthInfo info)  {
        
        removeAuthInfo(nativePtr, info);
    }

    private native void removeCallLog(long nativePtr, CallLog callLog);
    @Override
    synchronized public void removeCallLog(CallLog callLog)  {
        
        removeCallLog(nativePtr, callLog);
    }

    private native void removeFriendList(long nativePtr, FriendList list);
    @Override
    synchronized public void removeFriendList(FriendList list)  {
        
        removeFriendList(nativePtr, list);
    }

    private native int removeFromConference(long nativePtr, Call call);
    @Override
    synchronized public void removeFromConference(Call call)  {
        
        removeFromConference(nativePtr, call);
    }

    private native void removeProxyConfig(long nativePtr, ProxyConfig config);
    @Override
    synchronized public void removeProxyConfig(ProxyConfig config)  {
        
        removeProxyConfig(nativePtr, config);
    }

    private native void removeSupportedTag(long nativePtr, String tag);
    @Override
    synchronized public void removeSupportedTag(String tag)  {
        
        removeSupportedTag(nativePtr, tag);
    }

    private native void resetMissedCallsCount(long nativePtr);
    @Override
    synchronized public void resetMissedCallsCount()  {
        
        resetMissedCallsCount(nativePtr);
    }

    private native int resumeCall(long nativePtr, Call call);
    @Override
    synchronized public void resumeCall(Call call)  {
        
        resumeCall(nativePtr, call);
    }

    private native void setAudioPortRange(long nativePtr, int minPort, int maxPort);
    @Override
    synchronized public void setAudioPortRange(int minPort, int maxPort)  {
        
        setAudioPortRange(nativePtr, minPort, maxPort);
    }

    private native void setCallErrorTone(long nativePtr, int reason, String audiofile);
    @Override
    synchronized public void setCallErrorTone(Reason reason, String audiofile)  {
        
        setCallErrorTone(nativePtr, reason.toInt(), audiofile);
    }

    private native void setCallLogsFriend(long nativePtr, String account, String device);
    @Override
    synchronized public void setCallLogsFriend(String account, String device)  {
        
        setCallLogsFriend(nativePtr, account, device);
    }

    private native void setQrcodeDecodeRect(long nativePtr, int x, int y, int w, int h);
    @Override
    synchronized public void setQrcodeDecodeRect(int x, int y, int w, int h)  {
        
        setQrcodeDecodeRect(nativePtr, x, y, w, h);
    }

    private native void setTextPortRange(long nativePtr, int minPort, int maxPort);
    @Override
    synchronized public void setTextPortRange(int minPort, int maxPort)  {
        
        setTextPortRange(nativePtr, minPort, maxPort);
    }

    private native void setUserAgent(long nativePtr, String uaName, String version);
    @Override
    synchronized public void setUserAgent(String uaName, String version)  {
        
        setUserAgent(nativePtr, uaName, version);
    }

    private native void setVideoPortRange(long nativePtr, int minPort, int maxPort);
    @Override
    synchronized public void setVideoPortRange(int minPort, int maxPort)  {
        
        setVideoPortRange(nativePtr, minPort, maxPort);
    }

    private native boolean soundDeviceCanCapture(long nativePtr, String device);
    @Override
    synchronized public boolean soundDeviceCanCapture(String device)  {
        
        return soundDeviceCanCapture(nativePtr, device);
    }

    private native boolean soundDeviceCanPlayback(long nativePtr, String device);
    @Override
    synchronized public boolean soundDeviceCanPlayback(String device)  {
        
        return soundDeviceCanPlayback(nativePtr, device);
    }

    private native boolean soundResourcesLocked(long nativePtr);
    @Override
    synchronized public boolean soundResourcesLocked()  {
        
        return soundResourcesLocked(nativePtr);
    }

    private native void start(long nativePtr);
    @Override
    synchronized public void start()  {
        
        start(nativePtr);
    }

    private native int startConferenceRecording(long nativePtr, String path);
    @Override
    synchronized public void startConferenceRecording(String path)  {
        
        startConferenceRecording(nativePtr, path);
    }

    private native void startDtmfStream(long nativePtr);
    @Override
    synchronized public void startDtmfStream()  {
        
        startDtmfStream(nativePtr);
    }

    private native int startEchoCancellerCalibration(long nativePtr);
    @Override
    synchronized public void startEchoCancellerCalibration()  {
        
        startEchoCancellerCalibration(nativePtr);
    }

    private native int startEchoTester(long nativePtr, int rate);
    @Override
    synchronized public void startEchoTester(int rate)  {
        
        startEchoTester(nativePtr, rate);
    }

    private native int stopConferenceRecording(long nativePtr);
    @Override
    synchronized public void stopConferenceRecording()  {
        
        stopConferenceRecording(nativePtr);
    }

    private native void stopDtmf(long nativePtr);
    @Override
    synchronized public void stopDtmf()  {
        
        stopDtmf(nativePtr);
    }

    private native void stopDtmfStream(long nativePtr);
    @Override
    synchronized public void stopDtmfStream()  {
        
        stopDtmfStream(nativePtr);
    }

    private native int stopEchoTester(long nativePtr);
    @Override
    synchronized public void stopEchoTester()  {
        
        stopEchoTester(nativePtr);
    }

    private native void stopRinging(long nativePtr);
    @Override
    synchronized public void stopRinging()  {
        
        stopRinging(nativePtr);
    }

    private native Event subscribe(long nativePtr, Address resource, String event, int expires, Content body);
    @Override
    synchronized public Event subscribe(Address resource, String event, int expires, Content body)  {
        
        return (Event)subscribe(nativePtr, resource, event, expires, body);
    }

    private native int takePreviewSnapshot(long nativePtr, String file);
    @Override
    synchronized public void takePreviewSnapshot(String file)  {
        
        takePreviewSnapshot(nativePtr, file);
    }

    private native int terminateAllCalls(long nativePtr);
    @Override
    synchronized public void terminateAllCalls()  {
        
        terminateAllCalls(nativePtr);
    }

    private native int terminateCall(long nativePtr, Call call);
    @Override
    synchronized public void terminateCall(Call call)  {
        
        terminateCall(nativePtr, call);
    }

    private native int terminateConference(long nativePtr);
    @Override
    synchronized public void terminateConference()  {
        
        terminateConference(nativePtr);
    }

    private native int transferCall(long nativePtr, Call call, String referTo);
    @Override
    synchronized public void transferCall(Call call, String referTo)  {
        
        transferCall(nativePtr, call, referTo);
    }

    private native int transferCallToAnother(long nativePtr, Call call, Call dest);
    @Override
    synchronized public void transferCallToAnother(Call call, Call dest)  {
        
        transferCallToAnother(nativePtr, call, dest);
    }

    private native int updateCall(long nativePtr, Call call, CallParams params);
    @Override
    synchronized public void updateCall(Call call, CallParams params)  {
        
        updateCall(nativePtr, call, params);
    }

    private native void uploadLogCollection(long nativePtr);
    @Override
    synchronized public void uploadLogCollection()  {
        
        uploadLogCollection(nativePtr);
    }

    private native void usePreviewWindow(long nativePtr, boolean yesno);
    @Override
    synchronized public void usePreviewWindow(boolean yesno)  {
        
        usePreviewWindow(nativePtr, yesno);
    }

    private native void verifyServerCertificates(long nativePtr, boolean yesno);
    @Override
    synchronized public void verifyServerCertificates(boolean yesno)  {
        
        verifyServerCertificates(nativePtr, yesno);
    }

    private native void verifyServerCn(long nativePtr, boolean yesno);
    @Override
    synchronized public void verifyServerCn(boolean yesno)  {
        
        verifyServerCn(nativePtr, yesno);
    }

    private native boolean videoSupported(long nativePtr);
    @Override
    synchronized public boolean videoSupported()  {
        
        return videoSupported(nativePtr);
    }

    private native String compressLogCollection(long nativePtr);
    @Override
    synchronized public String compressLogCollection()  {
        
        return compressLogCollection(nativePtr);
    }

    private native void enableLogCollection(long nativePtr, int state);
    @Override
    synchronized public void enableLogCollection(LogCollectionState state)  {
        
        enableLogCollection(nativePtr, state.toInt());
    }

    private native int getLogCollectionMaxFileSize(long nativePtr);
    @Override
    synchronized public int getLogCollectionMaxFileSize()  {
        
        return getLogCollectionMaxFileSize(nativePtr);
    }

    private native String getLogCollectionPath(long nativePtr);
    @Override
    synchronized public String getLogCollectionPath()  {
        
        return getLogCollectionPath(nativePtr);
    }

    private native String getLogCollectionPrefix(long nativePtr);
    @Override
    synchronized public String getLogCollectionPrefix()  {
        
        return getLogCollectionPrefix(nativePtr);
    }

    private native int getLogLevelMask(long nativePtr);
    @Override
    synchronized public int getLogLevelMask()  {
        
        return getLogLevelMask(nativePtr);
    }

    private native String getVersion(long nativePtr);
    @Override
    synchronized public String getVersion()  {
        
        return getVersion(nativePtr);
    }

    private native int logCollectionEnabled(long nativePtr);
    @Override
    synchronized public LogCollectionState logCollectionEnabled()  {
        
        return LogCollectionState.fromInt(logCollectionEnabled(nativePtr));
    }

    private native void resetLogCollection(long nativePtr);
    @Override
    synchronized public void resetLogCollection()  {
        
        resetLogCollection(nativePtr);
    }

    private native void serializeLogs(long nativePtr);
    @Override
    synchronized public void serializeLogs()  {
        
        serializeLogs(nativePtr);
    }

    private native void setLogCollectionMaxFileSize(long nativePtr, int size);
    @Override
    synchronized public void setLogCollectionMaxFileSize(int size)  {
        
        setLogCollectionMaxFileSize(nativePtr, size);
    }

    private native void setLogCollectionPath(long nativePtr, String path);
    @Override
    synchronized public void setLogCollectionPath(String path)  {
        
        setLogCollectionPath(nativePtr, path);
    }

    private native void setLogCollectionPrefix(long nativePtr, String prefix);
    @Override
    synchronized public void setLogCollectionPrefix(String prefix)  {
        
        setLogCollectionPrefix(nativePtr, prefix);
    }

    private native void setLogLevelMask(long nativePtr, int mask);
    @Override
    synchronized public void setLogLevelMask(int mask)  {
        
        setLogLevelMask(nativePtr, mask);
    }

    private native boolean tunnelAvailable(long nativePtr);
    @Override
    synchronized public boolean tunnelAvailable()  {
        
        return tunnelAvailable(nativePtr);
    }

    private native boolean upnpAvailable(long nativePtr);
    @Override
    synchronized public boolean upnpAvailable()  {
        
        return upnpAvailable(nativePtr);
    }

    private native boolean vcardSupported(long nativePtr);
    @Override
    synchronized public boolean vcardSupported()  {
        
        return vcardSupported(nativePtr);
    }

    private native void addListener(long nativePtr, CoreListener listener);
    @Override
    synchronized public void addListener(CoreListener listener)  {
        
        addListener(nativePtr, listener);
    }

    private native void removeListener(long nativePtr, CoreListener listener);
    @Override
    synchronized public void removeListener(CoreListener listener)  {
        
        removeListener(nativePtr, listener);
    }

    private native org.linphone.mediastream.Factory getMediastreamerFactory(long nativePtr);
    public org.linphone.mediastream.Factory getMediastreamerFactory() {
        return getMediastreamerFactory(nativePtr);
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
