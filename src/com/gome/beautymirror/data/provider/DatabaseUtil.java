package com.gome.beautymirror.data.provider;

import android.net.Uri;
import android.provider.ContactsContract;

public final class DatabaseUtil {

    public static final String DB_NAME = "database.db";

    public static final int DB_VERSION = 1;

    public static final String PROVIDER_AUTHORITY = DatabaseProvider.AUTHORITY;
    public static final Uri PROVIDER_AUTHORITY_URI = DatabaseProvider.AUTHORITY_URI;

    public interface Tables {
        public static final String ACCOUNTS = "accounts";
        public static final String DEVICES = "devices";
        public static final String FRIENDS = "friends";
        public static final String FRIENDDEVICES = "frienddevices";
        public static final String PEOPLES = "peoples";
        public static final String PROPOSERS = "proposers";
        public static final String CALLLOGS = "calllogs";
        public static final String INFORMATIONS = "informations";
        public static final String FILES = "files";
    }

    public interface Triggers {
        public static final String CALLLOGS = "trigger_calllogs";
        public static final String INFORMATIONS = "trigger_informations";
    }

    public interface Views {
        public static final String ACCOUNTS = "view_accounts";
        public static final String FRIENDS = "view_friends";
        public static final String CALLLOGS = "view_calllogs";
        public static final String INFORMATIONS = "view_informations";
    }

    public interface AccountColumns {
        public static final String _ID = "_id";
        public static final String ACCOUNT = "account";
        public static final String PASSWORD = "password";
        public static final String NAME = "name";
        public static final String ICON = "icon";
        public static final String SIP = "sip";
        public static final String TIME = "time";
    }

    public interface DeviceColumns {
        public static final String ID = "id";
        public static final String TYPE = "type";
        public static final String PERMISSION = "permission";
        public static final String DEVICE_NAME = "device_name";
        public static final String DEVICE_SIP = "device_sip";
        public static final String DEVICE_TIME = "device_time";

        public static final int TYPE_UNKNOW = 0;
        public static final int TYPE_MIRROR = 1;
        public static final int TYPE_DEFAULT = TYPE_MIRROR;

        public static final int PERMISSION_PRIVITE = 0;
        public static final int PERMISSION_PUBLIC = 1;
        public static final int PERMISSION_DEFAULT = PERMISSION_PUBLIC;
    }

    /* friends table @{ */
    public static final class Account implements AccountColumns, DeviceColumns {
        public static final String INFO = "info";

        public static final String CREATE = "CREATE TABLE IF NOT EXISTS " + Tables.ACCOUNTS + " ("
                + _ID + " INTEGER PRIMARY KEY, "
                + ACCOUNT + " TEXT, "
                + PASSWORD + " TEXT, "
                + NAME + " TEXT, "
                + ICON + " BLOB, "
                + SIP + " TEXT, "
                + TIME + " LONG NOT NULL DEFAULT 0, "
                + INFO + " TEXT, "
                + ID + " TEXT);";

        public static final String CREATE_VIEW = "CREATE VIEW IF NOT EXISTS " + Views.ACCOUNTS
                + " AS SELECT a.*, d.type, d.permission, d.device_name, d.device_sip FROM " + Tables.ACCOUNTS + " a"
                + " LEFT OUTER JOIN " + Tables.DEVICES + " d ON (a.id = d.id)";

        public static final int COLUMN_ACCOUNT = 1;
        public static final int COLUMN_PASSWORD = 2;
        public static final int COLUMN_NAME = 3;
        public static final int COLUMN_ICON = 4;
        public static final int COLUMN_SIP = 5;
        public static final int COLUMN_TIME = 6;
        public static final int COLUMN_INFO = 7;
        public static final int COLUMN_ID = 8;
        public static final int COLUMN_DEVICE_TYPE = 9;
        public static final int COLUMN_DEVICE_PERMISSION = 10;
        public static final int COLUMN_DEVICE_NAME = 11;
        public static final int COLUMN_DEVICE_SIP = 12;
    }
    /* } */

    /* devices table @{ */
    public static final class Device implements DeviceColumns {
        public static final String CREATE = "CREATE TABLE IF NOT EXISTS " + Tables.DEVICES + " ("
                + ID + " TEXT PRIMARY KEY, "
                + TYPE + " INTEGER NOT NULL DEFAULT " + TYPE_DEFAULT + ", "
                + PERMISSION + " INTEGER NOT NULL DEFAULT " + PERMISSION_DEFAULT + ", "
                + DEVICE_NAME + " TEXT, "
                + DEVICE_SIP + " TEXT, "
                + DEVICE_TIME + " LONG NOT NULL DEFAULT 0);";

        public static final int COLUMN_ID = 0;
        public static final int COLUMN_TYPE = 1;
        public static final int COLUMN_PERMISSION = 2;
        public static final int COLUMN_NAME = 3;
        public static final int COLUMN_SIP = 4;
        public static final int COLUMN_TIME = 5;
    }
    /* } */

    /* friends table @{ */
    public static final class Friend implements AccountColumns, DeviceColumns {
        public static final String COMMENT = "comment";

        public static final String CREATE = "CREATE TABLE IF NOT EXISTS " + Tables.FRIENDS + " ("
                + ACCOUNT + " TEXT PRIMARY KEY, "
                + NAME + " TEXT, "
                + ICON + " BLOB, "
                + TIME + " LONG NOT NULL DEFAULT 0, "
                + ID + " TEXT, "
                + COMMENT + " TEXT, "
                + SIP + " TEXT);";

        public static final String CREATE_VIEW = "CREATE VIEW IF NOT EXISTS " + Views.FRIENDS
                + " AS SELECT f.*, fd.type, fd.device_name, fd.device_sip FROM " + Tables.FRIENDS+ " f"
                + " LEFT OUTER JOIN " + Tables.FRIENDDEVICES + " fd ON (f.id = fd.id)";

        public static final int COLUMN_ACCOUNT = 0;
        public static final int COLUMN_NAME = 1;
        public static final int COLUMN_ICON = 2;
        public static final int COLUMN_TIME = 3;
        public static final int COLUMN_ID = 4;
        public static final int COLUMN_COMMENT = 5;
        public static final int COLUMN_SIP = 6;
        public static final int COLUMN_DEVICE_TYPE = 7;
        public static final int COLUMN_DEVICE_NAME = 8;
        public static final int COLUMN_DEVICE_SIP = 9;
    }
    /* } */

    /* friend's devices table @{ */
    public static final class FriendDevice implements DeviceColumns {
        public static final String CREATE = "CREATE TABLE IF NOT EXISTS " + Tables.FRIENDDEVICES + " ("
                + ID + " TEXT PRIMARY KEY, "
                + TYPE + " INTEGER NOT NULL DEFAULT " + TYPE_DEFAULT + ", "
                + DEVICE_NAME + " TEXT, "
                + DEVICE_SIP + " TEXT, "
                + DEVICE_TIME + " LONG NOT NULL DEFAULT 0);";

        public static final int COLUMN_ID = 0;
        public static final int COLUMN_TYPE = 1;
        public static final int COLUMN_NAME = 2;
        public static final int COLUMN_SIP = 3;
        public static final int COLUMN_TIME = 4;
    }
    /* } */

    /* peoples table @{ */
    public static final class People implements AccountColumns {
        public static final String CONTACT_ID = ContactsContract.RawContacts.CONTACT_ID;
        public static final String VERSION = ContactsContract.RawContacts.VERSION;
        public static final String CONTACT_NAME = ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY;
        public static final String NUMBER = "number";
        public static final String STATUS = "status";

        public static final int STATUS_UNKNOW = 0;
        public static final int STATUS_FRIEND = 1;
        public static final int STATUS_DEFAULT = STATUS_UNKNOW;

        public static final String CREATE = "CREATE TABLE IF NOT EXISTS " + Tables.PEOPLES + " ("
                + CONTACT_ID + " INTEGER PRIMARY KEY, "
                + VERSION + " INTEGER NOT NULL DEFAULT 1, "
                + CONTACT_NAME + " TEXT, "
                + NUMBER + " TEXT, "
                + ACCOUNT + " TEXT, "
                + NAME + " TEXT, "
                + ICON + " BLOB, "
                + TIME + " LONG NOT NULL DEFAULT 0, "
                + STATUS + " INTEGER NOT NULL DEFAULT " + STATUS_DEFAULT + ");";

        public static final int COLUMN_CONTACT_ID = 0;
        public static final int COLUMN_VERSION = 1;
        public static final int COLUMN_CONTACT_NAME = 2;
        public static final int COLUMN_NUMBER = 3;
        public static final int COLUMN_ACCOUNT = 4;
        public static final int COLUMN_NAME = 5;
        public static final int COLUMN_ICON = 6;
        public static final int COLUMN_TIME = 7;
        public static final int COLUMN_STATUS = 8;
    }
    /* } */

    /* proposers table @{ */
    public static final class Proposer implements AccountColumns {
        public static final String REQUEST_TIME = "request_time";
        public static final String MESSAGE = "message";
        public static final String STATUS = "status";

        public static final int STATUS_NEW = 0;
        public static final int STATUS_FRIEND = 1;
        public static final int STATUS_IGNORE = 2;
        public static final int STATUS_DEFAULT = STATUS_NEW;

        public static final String CREATE = "CREATE TABLE IF NOT EXISTS " + Tables.PROPOSERS + " ("
                + ACCOUNT + " TEXT PRIMARY KEY, "
                + NAME + " TEXT, "
                + ICON + " BLOB, "
                + TIME + " LONG NOT NULL DEFAULT 0, "
                + REQUEST_TIME + " LONG NOT NULL DEFAULT 0, "
                + MESSAGE + " TEXT, "
                + STATUS + " INTEGER NOT NULL DEFAULT " + STATUS_DEFAULT + ");";

        public static final int COLUMN_ACCOUNT = 0;
        public static final int COLUMN_NAME = 1;
        public static final int COLUMN_ICON = 2;
        public static final int COLUMN_TIME = 3;
        public static final int COLUMN_REQUEST_TIME = 4;
        public static final int COLUMN_MESSAGE = 5;
        public static final int COLUMN_STATUS = 6;
    }
    /* } */

    /* calllogs table @{ */
    public static final class Calllog implements AccountColumns, DeviceColumns {
        private static final int MAX_COUNT = 100;

        public static final String TIME = "time";
        public static final String END_TIME = "end_time";
        public static final String DURATION = "duration";
        public static final String STATUS = "status";
        public static final String READ = "read";

        public static final int STATUS_OUTGOING = 0;
        public static final int STATUS_INCOMING = 1;
        public static final int STATUS_MISSED = 2;
        public static final int STATUS_DEFAULT = STATUS_OUTGOING;

        public static final int READ_NEW = 0;
        public static final int READ_OLD = 1;
        public static final int READ_DEFAULT = READ_NEW;

        public static final String CREATE = "CREATE TABLE IF NOT EXISTS " + Tables.CALLLOGS + " ("
                + TIME + " LONG PRIMARY KEY, "
                + ACCOUNT + " TEXT, "
                + ID + " TEXT, "
                + END_TIME + " LONG, "
                + DURATION + " INTEGER NOT NULL DEFAULT 0, "
                + STATUS + " INTEGER NOT NULL DEFAULT " + STATUS_DEFAULT + ", "
                + READ + " INTEGER NOT NULL DEFAULT " + READ_DEFAULT + ");";

        public static final String CREATE_TRIGGER = "CREATE TRIGGER IF NOT EXISTS " + Triggers.CALLLOGS
                + " AFTER insert ON " + Tables.CALLLOGS + " for each row "
                + "BEGIN "
                + "delete FROM " + Tables.CALLLOGS
                + " where (select count(*) FROM " + Tables.CALLLOGS + ") > " + MAX_COUNT
                + " and " + TIME + " in (select " + TIME + " from " + Tables.CALLLOGS + " order by " + TIME + " asc limit 1);"
                + " END;";

        public static final String CREATE_VIEW = "CREATE VIEW IF NOT EXISTS " + Views.CALLLOGS
                + " AS SELECT c.*, f.name, f.icon, f.comment FROM " + Tables.CALLLOGS + " c"
                + " LEFT OUTER JOIN " + Tables.FRIENDS + " f ON (c.account = f.account)";

        public static final int COLUMN_TIME = 0;
        public static final int COLUMN_ACCOUNT = 1;
        public static final int COLUMN_ID = 2;
        public static final int COLUMN_END_TIME = 3;
        public static final int COLUMN_DURATION = 4;
        public static final int COLUMN_STATUS = 5;
        public static final int COLUMN_READ = 6;
        public static final int COLUMN_ACCOUNT_NAME = 7;
        public static final int COLUMN_ACCOUNT_ICON = 8;
        public static final int COLUMN_ACCOUNT_COMMENT = 9;
    }
    /* } */

    /* notifications table @{ */
    public static final class Information implements AccountColumns, DeviceColumns {
        private static final int MAX_COUNT = 100;

        public static final String TIME = "time";
        public static final String REQUEST = "request";
        public static final String READ = "read";

        public static final int REQUEST_FRIEND = 0;
        public static final int REQUEST_CONFIRM = 1;
        public static final int REQUEST_CONFIRMED = 2;
        public static final int REQUEST_DEFAULT = REQUEST_FRIEND;

        public static final int READ_NEW = 0;
        public static final int READ_OLD = 1;
        public static final int READ_DEFAULT = READ_NEW;

        public static final String CREATE = "CREATE TABLE IF NOT EXISTS " + Tables.INFORMATIONS + " ("
                + TIME + " LONG PRIMARY KEY, "
                + ACCOUNT + " TEXT UNIQUE, "
                + ID + " TEXT, "
                + REQUEST + " INTEGER NOT NULL DEFAULT " + REQUEST_DEFAULT + ", "
                + READ + " INTEGER NOT NULL DEFAULT " + READ_DEFAULT + ");";

        public static final String CREATE_TRIGGER = "CREATE TRIGGER IF NOT EXISTS " + Triggers.INFORMATIONS
                + " AFTER insert ON " + Tables.INFORMATIONS + " for each row "
                + "BEGIN "
                + "delete FROM " + Tables.INFORMATIONS
                + " where (select count(*) FROM " + Tables.INFORMATIONS + ") > " + MAX_COUNT
                + " and " + TIME + " in (select " + TIME + " from " + Tables.INFORMATIONS + " order by " + TIME + " asc limit 1);"
                + " END;";

        public static final String CREATE_VIEW = "CREATE VIEW IF NOT EXISTS " + Views.INFORMATIONS
                + " AS SELECT n.*, f.name, f.icon, f.comment FROM " + Tables.INFORMATIONS+ " n"
                + " LEFT OUTER JOIN " + Tables.FRIENDS + " f ON (n.account = f.account)";

        public static final int COLUMN_TIME = 0;
        public static final int COLUMN_ACCOUNT = 1;
        public static final int COLUMN_ID = 2;
        public static final int COLUMN_REQUEST = 3;
        public static final int COLUMN_READ = 4;
        public static final int COLUMN_ACCOUNT_NAME = 5;
        public static final int COLUMN_ACCOUNT_ICON = 6;
        public static final int COLUMN_ACCOUNT_COMMENT = 7;
    }
    /* } */

    /* files table @{ */
    public interface File {
        public static final String _ID = "_id";
        public static final String NAME = "name";
        public static final String STATUS = "status";

        public static final int STATUS_NEW = 0;
        public static final int STATUS_SYNCED = 1;
        public static final int STATUS_DEFAULT = STATUS_NEW;

        public static final String CREATE = "CREATE TABLE IF NOT EXISTS " + Tables.FILES + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " TEXT, "
                + STATUS + " INTEGER NOT NULL DEFAULT " + STATUS_DEFAULT + ");";
    }
    /* } */
}
