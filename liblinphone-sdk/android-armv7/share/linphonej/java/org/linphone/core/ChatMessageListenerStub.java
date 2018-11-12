/*
ChatMessageListenerStub.java
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


public class ChatMessageListenerStub implements ChatMessageListener {
    @Override
    public void onParticipantImdnStateChanged(ChatMessage msg, ParticipantImdnState state) {
        // Auto-generated method stub
    }

    @Override
    public void onFileTransferRecv(ChatMessage message, Content content, Buffer buffer) {
        // Auto-generated method stub
    }

    @Override
    public Buffer onFileTransferSend(ChatMessage message, Content content, int offset, int size) {
        // Auto-generated method stub
        return null;
    }

    @Override
    public void onFileTransferProgressIndication(ChatMessage message, Content content, int offset, int total) {
        // Auto-generated method stub
    }

    @Override
    public void onMsgStateChanged(ChatMessage msg, ChatMessage.State state) {
        // Auto-generated method stub
    }

}