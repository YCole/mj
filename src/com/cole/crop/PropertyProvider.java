package cole.crop;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Build;
import android.os.storage.StorageManager;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;
import android.text.TextUtils;
import android.util.Log;


import java.util.ArrayList;
import java.util.HashMap;

public class PropertyProvider {

    private static final String DATABASE_NAME = "prop.db";

    private static final String HIDE_TABLE = "hiden";
    private static final String TAG_TABLE = "tags";
    private static final String ITEM_TAG_TABLE = "item_tags";
    private static final String ALBUM_USAGE_TABLE = "album_usage";

    private StorageManager mStorageManager;

    DatabaseHelper mDatabase;
	Context mContext;
	private SQLiteDatabase mDb = null;
	
	

    public PropertyProvider(Context context) {
		mContext = context;
        mDatabase = new DatabaseHelper(context, DATABASE_NAME, null,
                getDatabaseVersion(context));
        mStorageManager = (StorageManager) context
                .getSystemService(Context.STORAGE_SERVICE);
        Log.w("yujun666", "PropertyProvider");
    }

    public void hideAlbum(String filePath) {
        FilePathInfo info = StorageManagerHelper.getFilePathInfo(
                mStorageManager, filePath);
        if (info == null)
            return;

        int volumeId = info.volumeId;
        String relativePath = info.relativePath.toLowerCase();
        SQLiteDatabase db = getWritableDatabase();

        String[] columns = new String[] {
                "_id"
        };
        String where = "volume_id=" + volumeId + " AND relative_path='"
                + relativePath + "'";
        Cursor cursor = null;
        try {
            cursor = db.query(HIDE_TABLE, columns, where, null, null, null,
                    null);
            if (cursor != null && cursor.getCount() > 0){
			    Log.i("hideAlbum", "returned");
			    db.close();
                return;
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        ContentValues values = new ContentValues();
        values.put("volume_id", volumeId);
        values.put("relative_path", relativePath);
        if(db!=null){
        	db.insert(HIDE_TABLE, null, values);
        	db.close();
        }
    }

    public void displayAlbum(int volumeId, String relativePath) {
        SQLiteDatabase db = getWritableDatabase();
        if (relativePath == null)
            return;

        relativePath = relativePath.toLowerCase();

        String where = "volume_id = " + volumeId + " AND relative_path='"
                + relativePath + "'";
        if(db!=null){
	        db.delete(HIDE_TABLE, where, null);
	        db.close();
        }
    }

    public void displayAlbum(String filePath) {
        FilePathInfo info = StorageManagerHelper.getFilePathInfo(
                mStorageManager, filePath);
        if (info == null)
            return;

        String relativePath = StorageManagerHelper.getRelativePath(filePath,
                info.volumePath);
        int volumeId = info.volumeId;
        displayAlbum(volumeId, relativePath);
    }

    public void displayAlbum(int recordId) {
        SQLiteDatabase db = getWritableDatabase();

        String where = "_id= " + recordId;
        if(db!=null){
	        db.delete(HIDE_TABLE, where, null);
	        db.close();
        }
    }

    public Cursor getHidenAlbums() {
    	//Cursor cursor = null;
        Integer[] ids = StorageManagerHelper
                .getMountedVolumeIds(mStorageManager);
        if (ids == null || ids.length < 1) {
            Log.e("zhy", "No storage mounted");
            return null;
        }
        StringBuilder whereBuilder = new StringBuilder();

        int volumeId = ids[0];
        whereBuilder.append("volume_id=" + volumeId);
        if (ids.length > 1) {
            for (int i = 0; i < ids.length; i++) {
                volumeId = ids[i];
                whereBuilder.append(" OR volume_id=" + volumeId);
            }
        }

        String[] projection = new String[] {
                "_id", "volume_id",
                "relative_path", "bucket_name"
        };
        
        if(mContext.getDatabasePath(DATABASE_NAME).exists()){
        	
        	try{
			SQLiteDatabase db = getReadableDatabase();
			mDb = db;
	        Log.w("yujun777", "mDb change 1");     
//        	cursor = db.query(HIDE_TABLE, projection, whereBuilder.toString(), null,
//                    null, null, null);
        	return db.query(HIDE_TABLE, projection, whereBuilder.toString(), null,
                    null, null, null);
        	}catch(SQLiteException e){
        		Log.d("PropertyProvider","get db failed" + e.getMessage());
        	}
        }else{
        	Log.d("PropertyProvider","db file is not exit");
        }
        return null;
 
    }

    private class DatabaseHelper extends SQLiteOpenHelper {

        private final int mVersion;

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public DatabaseHelper(Context context, String name,
                CursorFactory factory, int version) {
            super(context, name, factory, version);
            // TODO Auto-generated constructor stub
            mVersion = version;
            setWriteAheadLoggingEnabled(true);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            upgradeHiden(db, 0, mVersion);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            upgradeHiden(db, oldVersion, newVersion);
        }

        private void upgradeHiden(SQLiteDatabase db, int oldVersion,
                int newVersion) {
            if (oldVersion > newVersion) {
                throw new RuntimeException("Invalid version.");
            }
            if (newVersion != mVersion) {
                throw new RuntimeException("Invalid new version.");
            }

            if (oldVersion < 1) {
                db.execSQL("CREATE TABLE hiden (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "volume_id INTEGER, relative_path TEXT, bucket_name TEXT);");

                db.execSQL("CREATE TABLE album_usage (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "volume_id INTEGER, relative_path TEXT, use_count INTEGER)");
                db.execSQL("CREATE TABLE tags (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);");
                db.execSQL("CREATE TABLE item_tags (_id INTEGER PRIMARY KEY AUTOINCREMENT, volume_id INTEGER, relative_path TEXT, tag_id INTEGER);");
                db.execSQL("CREATE TRIGGER IF NOT EXISTS tags_cleanup DELETE ON tags "
                        + "BEGIN "
                        + "DELETE FROM item_tags WHERE tag_id = old._id;"
                        + "END");
            }
        }

    }

    public static int getDatabaseVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            throw new RuntimeException("couldn't get version code for "
                    + context);
        }
    }

    public boolean isTagExist(String tagName) {
        SQLiteDatabase db = getWritableDatabase();
        String where = "name=?";
        Cursor cursor = null;
        try {
            cursor = db.query(TAG_TABLE, new String[] {
                    "_id"
            }, where,
                    new String[] {
                        tagName
                    }, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
            	return true;
            }
        } finally {
            if (cursor != null)
                cursor.close();
            if(db!=null){
            	db.close();
            }
        }
        return false;
    }

    public int addTag(String tagName) {
        SQLiteDatabase db = getWritableDatabase();
        //String where = "name='" + tagName + "'";
        if (!TextUtils.isEmpty(tagName)) {
			tagName = tagName.trim();
		}
        String where = "name= ? ";
        String[] selectionArgs = new String[]{ tagName };
        Cursor cursor = null;
        try {
            cursor = db.query(TAG_TABLE, new String[] {
                    "_id"
            }, where, selectionArgs,
                    null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                int id = cursor.getInt(0);
                return id;
            }
            ContentValues values = new ContentValues();
            values.put("name", tagName);
            db.insert(TAG_TABLE, null, values);

            cursor = db.query(TAG_TABLE, new String[] {
                    "_id"
            }, where, selectionArgs,
                    null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                int id = cursor.getInt(0);
                return id;
            }

            return 0;
        } finally {
            if (cursor != null)
                cursor.close();
            if(db!=null){
            	db.close();
            }
        }

    }

    public void addTagToItem(String tagName, int volumeId, String relativePath) {
        if (relativePath == null)
            return;

        relativePath = relativePath.toLowerCase();

        int tagId = addTag(tagName);
        if (tagId == 0) {
            Log.e("zhy", "Failed to add tag.");
            return;
        }
        addTagToItem(tagId, volumeId, relativePath);
    }

    public void addTagToItem(int tagId, int voluumeId, String relativePath) {
        if (relativePath == null)
            return;

        relativePath = relativePath.toLowerCase();

        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = null;
        try {
            String where = "volume_id=" + voluumeId + " AND relative_path= ?  AND tag_id=" + tagId;
            String[] selectionArgs = new String[]{relativePath};
            cursor = db.query(ITEM_TAG_TABLE, new String[] {
                    "_id"
            }, where,
            selectionArgs, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
            	db.close();
                return;
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        ContentValues values = new ContentValues();
        values.put("volume_id", voluumeId);
        values.put("relative_path", relativePath);
        values.put("tag_id", tagId);
        if(db!=null){
	        db.insert(ITEM_TAG_TABLE, null, values);
	        db.close();
        }
    }

    public Cursor getTags() {
        SQLiteDatabase db = getReadableDatabase();
        try {
            String[] projection = new String[] {
                    "_id", "name"
            };

            return db
                    .query(TAG_TABLE, projection, null, null, null, null, null);
        } finally {
        	if(db!=null){
             db.close();
        	}
        }

    }

    public Cursor getTagsWithCount() {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables("tags");

        SQLiteDatabase db = getReadableDatabase();
        try {
            String[] projection = new String[] {
                    "_id", "name",
                    "(select count(*) from item_tags where item_tags.tag_id=tags._id) as count"
            };

            return builder.query(db, projection, null, null, null, null, null);
            // return db
            // .query(TAG_TABLE, projection, null, null, null, null, null);
        } catch (Exception e) {
            Log.e("zhy", "exception :" + e);
            return null;
        } finally {
        	if(db!=null){
             db.close();
        	}
        }

    }

    public SQLiteDatabase getWritableDatabase() {
        synchronized (mDatabase) {
        	SQLiteDatabase db = null;
        	try{
        		db = mDatabase.getWritableDatabase();
        	}catch(Exception e){
        		Log.d("DB_ERROR", "get db error");
        	}
            return db; 
        }
    }

    public SQLiteDatabase getReadableDatabase() {
        synchronized (mDatabase) {
            return mDatabase.getReadableDatabase();
        }
    }
    
    public void closeDatabaseBygetHideAlbum()
    {
        synchronized (mDatabase) {
        	if (mDb != null) {
        		mDb.close();
        		mDb = null;
			};        	
        }
    }

    public String[] getItemTags(int volumeId, String relativePath) {
        if (relativePath == null)
            return null;
        relativePath = relativePath.toLowerCase().trim();

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables("item_tags, tags");
        qb.appendWhere("tags._id = tag_id");
        qb.appendWhere(" AND volume_id=" + volumeId);
        //qb.appendWhere(" AND relative_path='" + relativePath + "'");
        qb.appendWhere(" AND relative_path= ? ");
        String[] projection = new String[] {
                "tag_id", "name"
        };
        String[] selectionArgs = new String[]{relativePath};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        ArrayList<String> tagList = new ArrayList<String>();
        try {
            cursor = qb.query(db, projection, null, selectionArgs, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    tagList.add(cursor.getString(1));
                }
            }
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        int size = tagList.size();
        if (size <= 0) {
            return null;
        }

        return tagList.toArray(new String[size]);

    }

    public HashMap<Integer, HashMap<String, ArrayList<String>>> getTagsMap() {
        HashMap<Integer, HashMap<String, ArrayList<String>>> retVal = new HashMap<Integer, HashMap<String, ArrayList<String>>>();

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables("item_tags, tags");
        qb.appendWhere("tags._id = tag_id");
        String[] projection = new String[] {
                "volume_id", "relative_path",
                "tag_id", "name"
        };
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = qb.query(db, projection, null, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int volumeId = cursor.getInt(0);
                    String relativePath = cursor.getString(1);
                    String tagName = cursor.getString(3);
                    HashMap<String, ArrayList<String>> itemTags = null;
                    if (retVal.containsKey(volumeId)) {
                        itemTags = retVal.get(volumeId);
                    } else {
                        itemTags = new HashMap<String, ArrayList<String>>();
                        retVal.put(volumeId, itemTags);
                    }
                    ArrayList<String> tags = null;
                    if (itemTags.containsKey(relativePath)) {
                        tags = itemTags.get(relativePath);
                    } else {
                        tags = new ArrayList<String>();
                        itemTags.put(relativePath, tags);
                    }
                    tags.add(tagName);
                }
            }
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }
        return retVal;
    }

    public Cursor getItemUnsetTags(int volumeId, String relativePath) {
        if (relativePath == null)
            return null;
        relativePath = relativePath.toLowerCase().trim();

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables("item_tags");
        qb.appendWhere(" AND volume_id=" + volumeId);
        //qb.appendWhere(" AND relative_path='" + relativePath + "'");
        qb.appendWhere(" AND relative_path= ? ");
        String[] selectionArgs = new String[]{relativePath};
        String[] projection = new String[] {
                "tag_id"
        };
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        String where = null;
        try {
            cursor = qb.query(db, projection, null, selectionArgs, null, null, null);
            ArrayList<Integer> settedTags = new ArrayList<Integer>();
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    settedTags.add(cursor.getInt(0));
                }
            }
            int size = settedTags.size();
            if (size > 0) {
                where = "_id!=" + settedTags.get(0);
                if (size > 1) {
                    for (int i = 1; i < size; i++)
                        where += " AND _id!=" + settedTags.get(i);
                }
            }
            projection = new String[] {
                    "_id", "name"
            };
            return db.query(TAG_TABLE, projection, where, null, null, null,
                    null);

        } finally {
            if (cursor != null)
                cursor.close();
            // db.close();
        }

    }

    public void close() {
        if (mDatabase != null) {
            mDatabase.close();
            mDatabase = null;
            mStorageManager = null;
        }
    }

    public void updateTagAfterRename(String oldPath, String newPath) {
        FilePathInfo info = StorageManagerHelper.getFilePathInfo(
                mStorageManager, oldPath);
        if (info == null)
            return;

        String relativePath = StorageManagerHelper.getRelativePath(oldPath,
                info.volumePath);
        String newRelativePath = StorageManagerHelper.getRelativePath(newPath,
                info.volumePath);
        int volumeId = info.volumeId;

        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = null;
        try {
            String where = "volume_id=? AND relative_path=?";
            String[] args = new String[] {
                    "" + volumeId, relativePath
            };
            ContentValues values = new ContentValues();
            values.put("relative_path", newRelativePath);
            db.update(ITEM_TAG_TABLE, values, where, args);
        } finally {
            if (cursor != null)
                cursor.close();
            if(db!=null){
            	db.close();
            }
        }
    }

    public ArrayList<Integer> getMostUsedAlbum(int[] bucketIds) {
        ArrayList<Integer> retVal = new ArrayList<Integer>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        String where = null;
        if (bucketIds != null) {
            where = "bucket_id IN (";
            int size = bucketIds.length;
            for (int i = 0; i < size - 1; i++) {
                where += bucketIds[i] + ",";
            }
            where += bucketIds[size - 1] + ")";

        }
        try {
            cursor = db.query(ALBUM_USAGE_TABLE, new String[] {
                    "_id",
                    "bucket_id"
            }, where, null, null, null, "use_count DESC");
            int i = 0;
            while (cursor.moveToNext() && i < 2) {
                retVal.add(cursor.getInt(1));
                i++;
            }

        } finally {
            if (cursor != null)
                cursor.close();
            if(db!=null){
            	db.close();
            }
        }
        return retVal;
    }

    public void plusAlbumUseAge(int volumeId, String relativePath) {
        SQLiteDatabase db = getWritableDatabase();
        String where = "volume_id=" + volumeId + " AND relative_path='"
                + relativePath + "'";
        Cursor cursor = db.query(ALBUM_USAGE_TABLE, new String[] {
                "_id",
                "use_count"
        }, where, null, null, null, null);
        boolean exist = false;
        int count = 0;
        try {

            if (cursor != null && cursor.moveToFirst()) {
                exist = true;
                count = cursor.getInt(1);
            }

        } finally {
            cursor.close();
        }
        try {
            if (exist) {
                ContentValues values = new ContentValues();
                values.put("use_count", count + 1);
                db.update(ALBUM_USAGE_TABLE, values, where, null);
            } else {
                ContentValues values = new ContentValues();
                values.put("use_count", count + 1);
                values.put("volume_id", volumeId);
                values.put("relative_path", relativePath);
                db.insertOrThrow(ALBUM_USAGE_TABLE, null, values);
            }
        } finally {
        	if(db!=null){
        		db.close();
        	}
        }

    }

    public void deleteTag(int id) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            String where = "_id=" + id;
            db.delete("tags", where, null);
			try {
				if (mContext != null) {
					mContext.getContentResolver().notifyChange(Images.Media.EXTERNAL_CONTENT_URI, null);
					mContext.getContentResolver().notifyChange(Video.Media.EXTERNAL_CONTENT_URI, null);
				}
			} catch (Exception e) {
				Log.e("PropertyProvider",
						"delete tag, notify change,catch exception,ignore",e);
			}
        } finally {
        	if(db!=null){
        		db.close();
        	}
        }
    }

    /**
     * @param volumeId
     * @param relativePath
     */
    public void deleteTagItems(int volumeId, String relativePath) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            String where = " volume_id=? AND relative_path=? ";
            String[] whereArgs = new String[] {
                    String.valueOf(volumeId),
                    relativePath.toLowerCase()
            };
            int resultCount = db.delete(ITEM_TAG_TABLE, where, whereArgs);
            Log.d("PropertyProvider", "delete TAG Items numbers=" + resultCount);
        } catch (Exception ex) {
            Log.w("PropertyProvider", "delete TAG Items error ", ex);
        } finally {
        	if(db!=null){
        		db.close();
        	}
        }
    }
    
    //add by yujun for bug: 617003151897
    public void addTagsByID(int Id, String Name) {
        SQLiteDatabase db = getWritableDatabase();

        String where = " _id=" + Id;
        Cursor cursor = null;
        try {
        	
            cursor = db.query(TAG_TABLE, new String[] {"name"}, where, null,  null, null, null);
            Log.w("yujun", "cursor: "+ cursor);
            
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();                
                String name = cursor.getString(0);
                Log.i("yujun", "old name: " + name);
                
                //update value
                ContentValues cv = new ContentValues();
                cv.put("name", Name);
                db.update(TAG_TABLE, cv, "_id="+Id, null);
                Log.i("yujun", "new name: " + Name);
            }
            else {
            	Log.i("yujun", "cursor is null");

			}
        } finally {
            if (cursor != null)
                cursor.close();
            if(db!=null){
            	db.close();
            }
        }
    }    
}
