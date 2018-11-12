/*
ChatMessageListener.java
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
  * An object to handle the callbacks for the handling a LinphoneChatMessage
  * objects. 
  */
public interface ChatMessageListener {
    /**
      * Call back used to notify participant IMDN state. 
      */
    public void onParticipantImdnStateChanged(ChatMessage msg, ParticipantImdnState state);

    /**
      * File transfer receive callback prototype. 
      */
    public void onFileTransferRecv(ChatMessage message, Content content, Buffer buffer);

    /**
      * File transfer send callback prototype. 
      */
    public Buffer onFileTransferSend(ChatMessage message, Content content, int offset, int size);

    /**
      * File transfer progress indication callback prototype. 
      */
    public void onFileTransferProgressIndication(ChatMessage message, Content content, int offset, int total);

    /**
      * Call back used to notify message delivery status. 
      */
    public void onMsgStateChanged(ChatMessage msg, ChatMessage.State state);

}