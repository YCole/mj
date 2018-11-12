package com.gome.beautymirror.data.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Binder;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

public class DatabaseProvider extends ContentProvider {

    private static final String TAG = "DatabaseProvider";

    private static final boolean VERBOSE_LOGGING = Log.isLoggable(TAG, Log.VERBOSE);

    /** The authority for the beautymirror provider */
    public static final String AUTHORITY = "com.gome.beautymirror";
    /** A content:// style uri to the authority for the beautymirror provider */
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    private Context mContext;
    private DatabaseOpenHelper mDbHelper;

    private final String[] mSelectionArgs1 = new String[1];

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int ACCOUNTS = 1;
    private static final int ACCOUNTS_ID = 2;
    private static final int ACCOUNTS_VIEW = 3;
    private static final int DEVICES = 4;
    private static final int DEVICES_ID = 5;
    private static final int FRIENDS = 6;
    private static final int FRIENDS_ID = 7;
    private static final int FRIENDS_VIEW = 8;
    private static final int FRIENDDEVICES = 9;
    private static final int FRIENDDEVICES_ID = 10;
    private static final int PEOPLES = 11;
    private static final int PEOPLES_ID = 12;
    private static final int PROPOSERS = 13;
    private static final int PROPOSERS_ID = 14;
    private static final int CALLLOGS = 15;
    private static final int CALLLOGS_ID = 16;
    private static final int CALLLOGS_VIEW = 17;
    private static final int NOTIFICATIONS = 18;
    private static final int NOTIFICATIONS_ID = 19;
    private static final int NOTIFICATIONS_VIEW = 20;
    private static final int FILES = 21;
    private static final int FILES_ID = 22;

    static {
        final UriMatcher matcher = sUriMatcher;

        matcher.addURI(AUTHORITY, DatabaseUtil.Tables.ACCOUNTS, ACCOUNTS);
        matcher.addURI(AUTHORITY, DatabaseUtil.Tables.ACCOUNTS + "/*", ACCOUNTS_ID);
        matcher.addURI(AUTHORITY, DatabaseUtil.Views.ACCOUNTS, ACCOUNTS_VIEW);
        matcher.addURI(AUTHORITY, DatabaseUtil.Tables.DEVICES, DEVICES);
        matcher.addURI(AUTHORITY, DatabaseUtil.Tables.DEVICES + "/*", DEVICES_ID);
        matcher.addURI(AUTHORITY, DatabaseUtil.Tables.FRIENDS, FRIENDS);
        matcher.addURI(AUTHORITY, DatabaseUtil.Tables.FRIENDS + "/*", FRIENDS_ID);
        matcher.addURI(AUTHORITY, DatabaseUtil.Views.FRIENDS, FRIENDS_VIEW);
        matcher.addURI(AUTHORITY, DatabaseUtil.Tables.FRIENDDEVICES, FRIENDDEVICES);
        matcher.addURI(AUTHORITY, DatabaseUtil.Tables.FRIENDDEVICES + "/*", FRIENDDEVICES_ID);
        matcher.addURI(AUTHORITY, DatabaseUtil.Tables.PEOPLES, PEOPLES);
        matcher.addURI(AUTHORITY, DatabaseUtil.Tables.PEOPLES + "/*", PEOPLES_ID);
        matcher.addURI(AUTHORITY, DatabaseUtil.Tables.PROPOSERS, PROPOSERS);
        matcher.addURI(AUTHORITY, DatabaseUtil.Tables.PROPOSERS + "/*", PROPOSERS_ID);
        matcher.addURI(AUTHORITY, DatabaseUtil.Tables.CALLLOGS, CALLLOGS);
        matcher.addURI(AUTHORITY, DatabaseUtil.Tables.CALLLOGS + "/*", CALLLOGS_ID);
        matcher.addURI(AUTHORITY, DatabaseUtil.Views.CALLLOGS, CALLLOGS_VIEW);
        matcher.addURI(AUTHORITY, DatabaseUtil.Tables.NOTIFICATIONS, NOTIFICATIONS);
        matcher.addURI(AUTHORITY, DatabaseUtil.Tables.NOTIFICATIONS + "/*", NOTIFICATIONS_ID);
        matcher.addURI(AUTHORITY, DatabaseUtil.Views.NOTIFICATIONS, NOTIFICATIONS_VIEW);
        matcher.addURI(AUTHORITY, DatabaseUtil.Tables.FILES, FILES);
        matcher.addURI(AUTHORITY, DatabaseUtil.Tables.FILES + "/*", FILES_ID);
    }

    public static final Uri ACCOUNT_URI = Uri.withAppendedPath(AUTHORITY_URI, DatabaseUtil.Tables.ACCOUNTS);
    public static final Uri ACCOUNT_VIEW_URI = Uri.withAppendedPath(AUTHORITY_URI, DatabaseUtil.Views.ACCOUNTS);
    public static final Uri DEVICES_URI = Uri.withAppendedPath(AUTHORITY_URI, DatabaseUtil.Tables.DEVICES);
    public static final Uri FRIENDS_URI = Uri.withAppendedPath(AUTHORITY_URI, DatabaseUtil.Tables.FRIENDS);
    public static final Uri FRIENDS_VIEW_URI = Uri.withAppendedPath(AUTHORITY_URI, DatabaseUtil.Views.FRIENDS);
    public static final Uri FRIENDDEVICES_URI = Uri.withAppendedPath(AUTHORITY_URI, DatabaseUtil.Tables.FRIENDDEVICES);
    public static final Uri PEOPLES_URI = Uri.withAppendedPath(AUTHORITY_URI, DatabaseUtil.Tables.PEOPLES);
    public static final Uri PROPOSERS_URI = Uri.withAppendedPath(AUTHORITY_URI, DatabaseUtil.Tables.PROPOSERS);
    public static final Uri CALLLOGS_URI = Uri.withAppendedPath(AUTHORITY_URI, DatabaseUtil.Tables.CALLLOGS);
    public static final Uri CALLLOGS_VIEW_URI = Uri.withAppendedPath(AUTHORITY_URI, DatabaseUtil.Views.CALLLOGS);
    public static final Uri NOTIFICATIONS_URI = Uri.withAppendedPath(AUTHORITY_URI, DatabaseUtil.Tables.NOTIFICATIONS);
    public static final Uri NOTIFICATIONS_VIEW_URI = Uri.withAppendedPath(AUTHORITY_URI, DatabaseUtil.Views.NOTIFICATIONS);
    public static final Uri FILES_URI = Uri.withAppendedPath(AUTHORITY_URI, DatabaseUtil.Tables.FILES);

    @Override
    public boolean onCreate() {
        Log.d(TAG, "[onCreate]");

        mContext = getContext();
        mDbHelper = DatabaseOpenHelper.getInstance(mContext);

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.close();

        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        try {
            Uri result = insertInTransaction(uri, values);
            return result;
        } finally {
            endTransaction();
        }
    }

    private Uri insertInTransaction(Uri uri, ContentValues values) {
        if (VERBOSE_LOGGING) {
            Log.d(TAG, "insert: uri=" + uri + "  values=[" + values + "]" +
                    " CPID=" + Binder.getCallingPid());
        }

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        long id = 0;

        switch (match) {
            case ACCOUNTS:
                id = db.insert(DatabaseUtil.Tables.ACCOUNTS, null, values);
                break;

            case ACCOUNTS_ID:
                Log.d(TAG, "insert: ACCOUNTS is unsupported");
                break;

            case DEVICES:
                id = db.insert(DatabaseUtil.Tables.DEVICES, null, values);
                break;

            case DEVICES_ID:
                Log.d(TAG, "insert: DEVICES_ID is unsupported");
                break;

            case FRIENDS:
                id = db.insert(DatabaseUtil.Tables.FRIENDS, null, values);
                break;

            case FRIENDS_ID:
                Log.d(TAG, "insert: FRIENDS_ID is unsupported");
                break;

            case FRIENDDEVICES:
                id = db.insert(DatabaseUtil.Tables.FRIENDDEVICES, null, values);
                break;

            case FRIENDDEVICES_ID:
                Log.d(TAG, "insert: FRIENDDEVICES_ID is unsupported");
                break;

            case PEOPLES:
                id = db.insert(DatabaseUtil.Tables.PEOPLES, null, values);
                break;

            case PEOPLES_ID:
                Log.d(TAG, "insert: PEOPLES_ID is unsupported");
                break;

            case PROPOSERS:
                id = db.insert(DatabaseUtil.Tables.PROPOSERS, null, values);
                break;

            case PROPOSERS_ID:
                Log.d(TAG, "insert: PROPOSERS_ID is unsupported");
                break;

            case CALLLOGS:
                id = db.insert(DatabaseUtil.Tables.CALLLOGS, null, values);
                break;

            case CALLLOGS_ID:
                Log.d(TAG, "insert: CALLLOGS_ID is unsupported");
                break;

            case NOTIFICATIONS:
                id = db.insert(DatabaseUtil.Tables.NOTIFICATIONS, null, values);
                break;

            case NOTIFICATIONS_ID:
                Log.d(TAG, "insert: NOTIFICATIONS_ID is unsupported");
                break;

            case FILES:
                id = db.insert(DatabaseUtil.Tables.FILES, null, values);
                break;

            case FILES_ID:
                Log.d(TAG, "insert: FILES_ID is unsupported");
                break;

            default:
                Log.d(TAG, "insert: match is " + match);
                break;
        }

        if (id < 0) {
            return null;
        }

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        try {
            int result = updateInTransaction(uri, values, selection, selectionArgs);
            return result;
        } finally {
            endTransaction();
        }
    }

    private int updateInTransaction(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (VERBOSE_LOGGING) {
            Log.d(TAG, "update: uri=" + uri +
                    "  selection=[" + selection + "]  args=" + Arrays.toString(selectionArgs) +
                    "  values=[" + values + "] CPID=" + Binder.getCallingPid());
        }

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        int count = 0;

        switch (match) {
            case ACCOUNTS:
                count = db.update(DatabaseUtil.Tables.ACCOUNTS, values, selection, selectionArgs);
                break;

            case ACCOUNTS_ID: {
                final List<String> pathSegments = uri.getPathSegments();
                final int segmentCount = pathSegments.size();
                if (segmentCount < 2) {
                    throw new IllegalArgumentException("Missing a account id");
                }
                final String accountId = pathSegments.get(1);
                mSelectionArgs1[0] = accountId;
                count = db.update(DatabaseUtil.Tables.ACCOUNTS, values, DatabaseUtil.Account.ACCOUNT + " = ?", mSelectionArgs1);
                break;
            }

            case DEVICES:
                count = db.update(DatabaseUtil.Tables.DEVICES, values, selection, selectionArgs);
                break;

            case DEVICES_ID: {
                final List<String> pathSegments = uri.getPathSegments();
                final int segmentCount = pathSegments.size();
                if (segmentCount < 2) {
                    throw new IllegalArgumentException("Missing a device id");
                }
                final String deviceId = pathSegments.get(1);
                mSelectionArgs1[0] = deviceId;
                count = db.update(DatabaseUtil.Tables.DEVICES, values, DatabaseUtil.Device.ID + " = ?", mSelectionArgs1);
                break;
            }

            case FRIENDS:
                count = db.update(DatabaseUtil.Tables.FRIENDS, values, selection, selectionArgs);
                break;

            case FRIENDS_ID: {
                final List<String> pathSegments = uri.getPathSegments();
                final int segmentCount = pathSegments.size();
                if (segmentCount < 2) {
                    throw new IllegalArgumentException("Missing a friend id");
                }
                final String friendId = pathSegments.get(1);
                mSelectionArgs1[0] = friendId;
                count = db.update(DatabaseUtil.Tables.FRIENDS, values, DatabaseUtil.Friend.ACCOUNT + " = ?", mSelectionArgs1);
                break;
            }

            case FRIENDDEVICES:
                count = db.update(DatabaseUtil.Tables.FRIENDDEVICES, values, selection, selectionArgs);
                break;

            case FRIENDDEVICES_ID: {
                final List<String> pathSegments = uri.getPathSegments();
                final int segmentCount = pathSegments.size();
                if (segmentCount < 2) {
                    throw new IllegalArgumentException("Missing a device id");
                }
                final String deviceId = pathSegments.get(1);
                mSelectionArgs1[0] = deviceId;
                count = db.update(DatabaseUtil.Tables.FRIENDDEVICES, values, DatabaseUtil.FriendDevice.ID + " = ?", mSelectionArgs1);
                break;
            }

            case PEOPLES:
                count = db.update(DatabaseUtil.Tables.PEOPLES, values, selection, selectionArgs);
                break;

            case PEOPLES_ID: {
                final List<String> pathSegments = uri.getPathSegments();
                final int segmentCount = pathSegments.size();
                if (segmentCount < 2) {
                    throw new IllegalArgumentException("Missing a people id");
                }
                final String peopleId = pathSegments.get(1);
                mSelectionArgs1[0] = peopleId;
                count = db.update(DatabaseUtil.Tables.PEOPLES, values, DatabaseUtil.People.CONTACT_ID + " = ?", mSelectionArgs1);
                break;
            }

            case PROPOSERS:
                count = db.update(DatabaseUtil.Tables.PROPOSERS, values, selection, selectionArgs);
                break;

            case PROPOSERS_ID:
                Log.d(TAG, "update: PROPOSERS_ID is unsupported");
                break;

            case CALLLOGS:
                count = db.update(DatabaseUtil.Tables.CALLLOGS, values, selection, selectionArgs);
                break;

            case CALLLOGS_ID:
                Log.d(TAG, "update: CALLLOGS_ID is unsupported");
                break;

            case NOTIFICATIONS:
                count = db.update(DatabaseUtil.Tables.NOTIFICATIONS, values, selection, selectionArgs);
                break;

            case NOTIFICATIONS_ID:
                Log.d(TAG, "update: NOTIFICATIONS_ID is unsupported");
                break;

            case FILES:
                count = db.update(DatabaseUtil.Tables.FILES, values, selection, selectionArgs);
                break;

            case FILES_ID: {
                final List<String> pathSegments = uri.getPathSegments();
                final int segmentCount = pathSegments.size();
                if (segmentCount < 2) {
                    throw new IllegalArgumentException("Missing a file id");
                }
                final String fileId = pathSegments.get(1);
                mSelectionArgs1[0] = fileId;
                count = db.update(DatabaseUtil.Tables.FILES, values, DatabaseUtil.File._ID + " = ?", mSelectionArgs1);
                break;
            }

            default:
                Log.d(TAG, "update: match is " + match);
                break;
        }

        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        try {
            int result = deleteInTransaction(uri, selection, selectionArgs);
            return result;
        } finally {
            endTransaction();
        }
    }

    private int deleteInTransaction(Uri uri, String selection, String[] selectionArgs) {
        if (VERBOSE_LOGGING) {
            Log.d(TAG, "delete: uri=" + uri +
                    "  selection=[" + selection + "]  args=" + Arrays.toString(selectionArgs) +
                    " CPID=" + Binder.getCallingPid());
        }

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        int count = 0;

        switch (match) {
            case ACCOUNTS:
                count = db.delete(DatabaseUtil.Tables.ACCOUNTS, selection, selectionArgs);
                break;

            case ACCOUNTS_ID: {
                final List<String> pathSegments = uri.getPathSegments();
                final int segmentCount = pathSegments.size();
                if (segmentCount < 2) {
                    throw new IllegalArgumentException("Missing a account id");
                }
                final String accountId = pathSegments.get(1);
                mSelectionArgs1[0] = accountId;
                count = db.delete(DatabaseUtil.Tables.ACCOUNTS, DatabaseUtil.Account.ACCOUNT + " = ?", mSelectionArgs1);
                break;
            }

            case DEVICES:
                count = db.delete(DatabaseUtil.Tables.DEVICES, selection, selectionArgs);
                break;

            case DEVICES_ID: {
                final List<String> pathSegments = uri.getPathSegments();
                final int segmentCount = pathSegments.size();
                if (segmentCount < 2) {
                    throw new IllegalArgumentException("Missing a device id");
                }
                final String deviceId = pathSegments.get(1);
                mSelectionArgs1[0] = deviceId;
                count = db.delete(DatabaseUtil.Tables.DEVICES, DatabaseUtil.Device.ID + " = ?", mSelectionArgs1);
                break;
            }

            case FRIENDS:
                count = db.delete(DatabaseUtil.Tables.FRIENDS, selection, selectionArgs);
                break;

            case FRIENDS_ID: {
                final List<String> pathSegments = uri.getPathSegments();
                final int segmentCount = pathSegments.size();
                if (segmentCount < 2) {
                    throw new IllegalArgumentException("Missing a friend id");
                }
                final String friendId = pathSegments.get(1);
                mSelectionArgs1[0] = friendId;
                count = db.delete(DatabaseUtil.Tables.FRIENDS, DatabaseUtil.Friend.ACCOUNT + " = ?", mSelectionArgs1);
                break;
            }

            case FRIENDDEVICES:
                count = db.delete(DatabaseUtil.Tables.FRIENDDEVICES, selection, selectionArgs);
                break;

            case FRIENDDEVICES_ID: {
                final List<String> pathSegments = uri.getPathSegments();
                final int segmentCount = pathSegments.size();
                if (segmentCount < 2) {
                    throw new IllegalArgumentException("Missing a device id");
                }
                final String deviceId = pathSegments.get(1);
                mSelectionArgs1[0] = deviceId;
                count = db.delete(DatabaseUtil.Tables.FRIENDDEVICES, DatabaseUtil.FriendDevice.ID + " = ?", mSelectionArgs1);
                break;
            }

            case PEOPLES:
                count = db.delete(DatabaseUtil.Tables.PEOPLES, selection, selectionArgs);
                break;

            case PEOPLES_ID: {
                final List<String> pathSegments = uri.getPathSegments();
                final int segmentCount = pathSegments.size();
                if (segmentCount < 2) {
                    throw new IllegalArgumentException("Missing a people id");
                }
                final String peopleId = pathSegments.get(1);
                mSelectionArgs1[0] = peopleId;
                count = db.delete(DatabaseUtil.Tables.PEOPLES,
                        DatabaseUtil.People.CONTACT_ID + " = ?", mSelectionArgs1);
                break;
            }

            case PROPOSERS:
                count = db.delete(DatabaseUtil.Tables.PROPOSERS, selection, selectionArgs);
                break;

            case PROPOSERS_ID:
                Log.d(TAG, "delete: PROPOSERS_ID is unsupported");
                break;

            case CALLLOGS:
                count = db.delete(DatabaseUtil.Tables.CALLLOGS, selection, selectionArgs);
                break;

            case CALLLOGS_ID:
                Log.d(TAG, "delete: CALLLOGS_ID is unsupported");
                break;

            case NOTIFICATIONS:
                count = db.delete(DatabaseUtil.Tables.NOTIFICATIONS, selection, selectionArgs);
                break;

            case NOTIFICATIONS_ID:
                Log.d(TAG, "delete: NOTIFICATIONS_ID is unsupported");
                break;

            case FILES:
                count = db.delete(DatabaseUtil.Tables.FILES, selection, selectionArgs);
                break;

            case FILES_ID: {
                final List<String> pathSegments = uri.getPathSegments();
                final int segmentCount = pathSegments.size();
                if (segmentCount < 2) {
                    throw new IllegalArgumentException("Missing a file id");
                }
                final String fileId = pathSegments.get(1);
                mSelectionArgs1[0] = fileId;
                count = db.delete(DatabaseUtil.Tables.FILES,
                        DatabaseUtil.File._ID + " = ?", mSelectionArgs1);
                break;
            }

            default:
                Log.d(TAG, "delete: match is " + match);
                break;
        }

        return count;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        try {
            Cursor result = queryInTransaction(uri, projection, selection, selectionArgs, sortOrder);
            return result;
        } finally {
            endTransaction();
        }
    }

    private Cursor queryInTransaction(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        if (VERBOSE_LOGGING) {
            Log.d(TAG, "query: uri=" + uri + "  projection=" + Arrays.toString(projection) +
                    "  selection=[" + selection + "]  args=" + Arrays.toString(selectionArgs) +
                    "  order=[" + sortOrder + "] CPID=" + Binder.getCallingPid());
        }

        final SQLiteDatabase db = mDbHelper.getReadableDatabase();

        final int match = sUriMatcher.match(uri);
        Cursor cursor = null;

        switch (match) {
            case ACCOUNTS: {
                cursor = db.query(DatabaseUtil.Tables.ACCOUNTS,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }

            case ACCOUNTS_ID: {
                final List<String> pathSegments = uri.getPathSegments();
                final int segmentCount = pathSegments.size();
                if (segmentCount < 2) {
                    throw new IllegalArgumentException("Missing a account id");
                }
                final String accountId = pathSegments.get(1);
                mSelectionArgs1[0] = accountId;
                cursor = db.query(DatabaseUtil.Tables.ACCOUNTS,
                        projection, DatabaseUtil.Account.ACCOUNT + "= ? ", mSelectionArgs1,
                        null, null, null);
                break;
            }

            case ACCOUNTS_VIEW:
                cursor = db.query(DatabaseUtil.Views.ACCOUNTS,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case DEVICES:
                cursor = db.query(DatabaseUtil.Tables.DEVICES,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case DEVICES_ID: {
                final List<String> pathSegments = uri.getPathSegments();
                final int segmentCount = pathSegments.size();
                if (segmentCount < 2) {
                    throw new IllegalArgumentException("Missing a device id");
                }
                final String deviceId = pathSegments.get(1);
                mSelectionArgs1[0] = deviceId;
                cursor = db.query(DatabaseUtil.Tables.DEVICES,
                        projection, DatabaseUtil.Device.ID + "= ? ", mSelectionArgs1,
                        null, null, null);
                break;
            }

            case FRIENDS: {
                cursor = db.query(DatabaseUtil.Tables.FRIENDS,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }

            case FRIENDS_ID: {
                final List<String> pathSegments = uri.getPathSegments();
                final int segmentCount = pathSegments.size();
                if (segmentCount < 2) {
                    throw new IllegalArgumentException("Missing a friend id");
                }
                final String friendId = pathSegments.get(1);
                mSelectionArgs1[0] = friendId;
                cursor = db.query(DatabaseUtil.Tables.FRIENDS,
                        projection, DatabaseUtil.Friend.ACCOUNT + "= ? ", mSelectionArgs1,
                        null, null, null);
                break;
            }

            case FRIENDS_VIEW:
                cursor = db.query(DatabaseUtil.Views.FRIENDS,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case FRIENDDEVICES:
                cursor = db.query(DatabaseUtil.Tables.FRIENDDEVICES,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case FRIENDDEVICES_ID: {
                final List<String> pathSegments = uri.getPathSegments();
                final int segmentCount = pathSegments.size();
                if (segmentCount < 2) {
                    throw new IllegalArgumentException("Missing a device id");
                }
                final String deviceId = pathSegments.get(1);
                mSelectionArgs1[0] = deviceId;
                cursor = db.query(DatabaseUtil.Tables.FRIENDDEVICES,
                        projection, DatabaseUtil.FriendDevice.ID + "= ? ", mSelectionArgs1,
                        null, null, null);
                break;
            }

            case PEOPLES: {
                cursor = db.query(DatabaseUtil.Tables.PEOPLES,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }

            case PEOPLES_ID: {
                final List<String> pathSegments = uri.getPathSegments();
                final int segmentCount = pathSegments.size();
                if (segmentCount < 2) {
                    throw new IllegalArgumentException("Missing a people id");
                }
                final String peopleId = pathSegments.get(1);
                mSelectionArgs1[0] = peopleId;
                cursor = db.query(DatabaseUtil.Tables.PEOPLES,
                        projection, DatabaseUtil.People.CONTACT_ID + " = ?", mSelectionArgs1, null, null, sortOrder);
                break;
            }

            case PROPOSERS:
                cursor = db.query(DatabaseUtil.Tables.PROPOSERS,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case PROPOSERS_ID:
                Log.d(TAG, "query: PROPOSERS_ID is unsupported");
                break;

            case CALLLOGS:
                cursor = db.query(DatabaseUtil.Tables.CALLLOGS,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case CALLLOGS_ID:
                Log.d(TAG, "query: CALLLOGS_ID is unsupported");
                break;

            case CALLLOGS_VIEW:
                cursor = db.query(DatabaseUtil.Views.CALLLOGS,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case NOTIFICATIONS:
                cursor = db.query(DatabaseUtil.Tables.NOTIFICATIONS,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case NOTIFICATIONS_ID:
                Log.d(TAG, "query: NOTIFICATIONS_ID is unsupported");
                break;

            case NOTIFICATIONS_VIEW:
                cursor = db.query(DatabaseUtil.Views.NOTIFICATIONS,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case FILES:
                cursor = db.query(DatabaseUtil.Tables.FILES,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case FILES_ID: {
                final List<String> pathSegments = uri.getPathSegments();
                final int segmentCount = pathSegments.size();
                if (segmentCount < 2) {
                    throw new IllegalArgumentException("Missing a file id");
                }
                final String fileId = pathSegments.get(1);
                mSelectionArgs1[0] = fileId;
                cursor = db.query(DatabaseUtil.Tables.FILES,
                        projection, DatabaseUtil.File._ID + " = ?", mSelectionArgs1, null, null, sortOrder);
                break;
            }

            default:
                Log.d(TAG, "query: match is " + match);
                break;
        }

        return cursor;
    }

    private void endTransaction() {
        if (VERBOSE_LOGGING) {
            Log.i(TAG, "endTransaction " + getClass().getSimpleName(), new RuntimeException("endTransaction"));
        }
        mContext.getContentResolver().notifyChange(AUTHORITY_URI, null);
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case ACCOUNTS:
            case ACCOUNTS_VIEW:
            case DEVICES:
            case FRIENDS:
            case FRIENDS_VIEW:
            case FRIENDDEVICES:
            case PEOPLES:
            case PROPOSERS:
            case CALLLOGS:
            case CALLLOGS_VIEW:
            case NOTIFICATIONS:
            case NOTIFICATIONS_VIEW:
            case FILES:
                return "vnd.android.cursor.dir/multi";

            case ACCOUNTS_ID:
            case DEVICES_ID:
            case FRIENDS_ID:
            case FRIENDDEVICES_ID:
            case PEOPLES_ID:
            case PROPOSERS_ID:
            case CALLLOGS_ID:
            case NOTIFICATIONS_ID:
            case FILES_ID:
                return "vnd.android.cursor.item/single";

            default:
                Log.d(TAG, "getType: match is " + match);
                break;
        }
        return null;
    }
}
