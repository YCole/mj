/*
CallListener.java
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
  * That class holds all the callbacks which are called by LinphoneCall objects. 
  */
public interface CallListener {
    /**
      * Callback for notifying a snapshot taken. 
      */
    public void onSnapshotTaken(Call call, String filepath);

    /**
      * Call state notification callback. 
      */
    public void onStateChanged(Call call, Call.State cstate, String message);

    /**
      * Callback for notifying progresses of transfers. 
      */
    public void onTransferStateChanged(Call call, Call.State cstate);

    /**
      * Callback for notifying a received TMMBR. 
      */
    public void onTmmbrReceived(Call call, int streamIndex, int tmmbr);

    /**
      * Callback for receiving info messages. 
      */
    public void onInfoMessageReceived(Call call, InfoMessage msg);

    /**
      * Call encryption changed callback. 
      */
    public void onEncryptionChanged(Call call, boolean on, String authenticationToken);

    /**
      * Callback for notifying the processing SIP ACK messages. 
      */
    public void onAckProcessing(Call call, Headers ack, boolean isReceived);

    /**
      * Callback for being notified of received DTMFs. 
      */
    public void onDtmfReceived(Call call, int dtmf);

    /**
      * Callback to notify a next video frame has been decoded. 
      */
    public void onNextVideoFrameDecoded(Call call);

    /**
      * Callback for receiving quality statistics for calls. 
      */
    public void onStatsUpdated(Call call, CallStats stats);

}