package com.gome.beautymirror.fragments;

/*
FragmentsAvailable.java
Copyright (C) 2017  Belledonne Communications, Grenoble, France

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

public enum FragmentsAvailable {
    PHOTO,
    HISTORY_LIST,
    CONTACTS_LIST,
    MINE,
    DIALER,
    SETTINGS,
    UNKNOW,
    ACCOUNT_SETTINGS,
    EMPTY,
    CHAT_LIST,
    CHAT,
    CREATE_CHAT,
    INFO_GROUP_CHAT,
    GROUP_CHAT,
    MESSAGE_IMDN;

    public boolean shouldAddItselfToTheRightOf(FragmentsAvailable fragment) {
        switch (this) {
            case CHAT:
                return fragment == CHAT_LIST || fragment == CHAT;

            case GROUP_CHAT:
                return fragment == CHAT_LIST || fragment == GROUP_CHAT;

            case MESSAGE_IMDN:
                return fragment == GROUP_CHAT || fragment == MESSAGE_IMDN;
            case MINE:
                return fragment == MINE || fragment == MINE;

            default:
                return false;
        }
    }
}
