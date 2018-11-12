/*
CallListenerStub.java
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


public class CallListenerStub implements CallListener {
    @Override
    public void onSnapshotTaken(Call call, String filepath) {
        // Auto-generated method stub
    }

    @Override
    public void onStateChanged(Call call, Call.State cstate, String message) {
        // Auto-generated method stub
    }

    @Override
    public void onTransferStateChanged(Call call, Call.State cstate) {
        // Auto-generated method stub
    }

    @Override
    public void onTmmbrReceived(Call call, int streamIndex, int tmmbr) {
        // Auto-generated method stub
    }

    @Override
    public void onInfoMessageReceived(Call call, InfoMessage msg) {
        // Auto-generated method stub
    }

    @Override
    public void onEncryptionChanged(Call call, boolean on, String authenticationToken) {
        // Auto-generated method stub
    }

    @Override
    public void onAckProcessing(Call call, Headers ack, boolean isReceived) {
        // Auto-generated method stub
    }

    @Override
    public void onDtmfReceived(Call call, int dtmf) {
        // Auto-generated method stub
    }

    @Override
    public void onNextVideoFrameDecoded(Call call) {
        // Auto-generated method stub
    }

    @Override
    public void onStatsUpdated(Call call, CallStats stats) {
        // Auto-generated method stub
    }

}