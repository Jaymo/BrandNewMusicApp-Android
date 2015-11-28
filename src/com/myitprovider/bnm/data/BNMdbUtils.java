
package com.myitprovider.bnm.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class BNMdbUtils {
	/**
     *  Constants that specify the different columns on the BNM Database
     *  and corresponding column indexes
     */
	
	
	/**
     * @Table Home Tab 
     */
	public static final String KEY_ID = "_id";

	public static final String KEY_NAME_ID = "artist_id";
	
	public static final String KEY_NAME = "artist_name";
	
    public static final String KEY_TRACK_COUNT = "track_count";
    
    public static final String KEY_ARTIST_IMAGE_URL = "image_url";
    
   
    /**
     * @Table Artists Tab
     */
   
    public static final String KEY_ARTIST_ID = "_id";
    
    public static final String KEY_UNIQUE_ID = "Artist_artist_id";

	public static final String KEY_ARTIST_NAME = "Artist_artist_name";
	
	public static final String KEY_ARTIST_TRACK_COUNT = "Artist_track_count";
	    
	public static final String KEY_ARTIST_ARTIST_IMAGE_URL = "Artist_image_url";
	
    public static final String KEY_ARTIST_STATE = "Artist_state";
    
    public static final String KEY_FOLLOW_STATUS = "Artist_follow";
    
    /**
     * @Table Artists Albums
     */
    
    public static final String KEY_ARTIST_ALBUMS_ID = "_id";

    public static final String KEY_ARTIST_ALBUM_NAME = "Artist_album_name";
	
	public static final String KEY_ARTIST_ALBUM_ID = "Artist_album_id";
	
	public static final String KEY_ARTIST_ALBUM_ARTWORK_URL = "Artist_album_artwork_url";
	 
    public static final String KEY_ARTIST_ALBUM_DATE = "Artist_album_date";
    
    
    public static final String[] TBL_HOME_COLUMNS = new String[] {
    	KEY_ID,
    	KEY_NAME_ID,
    	KEY_NAME,
    	KEY_TRACK_COUNT,
    	KEY_ARTIST_IMAGE_URL};
    
    public static final String[] TBL_ARTIST_COLUMNS = new String[] {
    	KEY_ARTIST_ID,
    	KEY_UNIQUE_ID, 
    	KEY_ARTIST_NAME, 
    	KEY_ARTIST_TRACK_COUNT,
    	KEY_ARTIST_ARTIST_IMAGE_URL,
    	KEY_ARTIST_STATE,
    	KEY_FOLLOW_STATUS
    	};
    public static final String [] TBL_ARTIST_ALBUMS= new String []{
    	KEY_ARTIST_ALBUMS_ID,
    	KEY_UNIQUE_ID, 
    	KEY_ARTIST_NAME,
    	KEY_ARTIST_ALBUM_NAME,
    	KEY_ARTIST_ALBUM_ID,
    	KEY_ARTIST_ALBUM_ARTWORK_URL,
    	KEY_ARTIST_ALBUM_DATE
    	
    	
    	
    };
    
    public static final String HOME_TABLE = "home";
    
    public static final String ARTIST_TABLE = "artist";
    
    public static final String ARTIST_ALBUMS_TABLE = "albums";
    
    
    private static final String HOME_TABLE_CREATE = 
        	"CREATE TABLE IF NOT EXISTS  "
            + HOME_TABLE 
            + " (" + KEY_ID 
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_NAME_ID 
            + " VARCHAR(100) NOT NULL, "
            + KEY_NAME 
            + " VARCHAR(150) NOT NULL, " 
            + KEY_TRACK_COUNT 
            + " VARCHAR(150) NOT NULL,"
            + KEY_ARTIST_IMAGE_URL
            +" VARCHAR(300) NOT NULL)";
    
    private static final String ARTIST_TABLE_CREATE = 
        	"CREATE TABLE IF NOT EXISTS  "
            + ARTIST_TABLE 
            + " (" + KEY_ARTIST_ID 
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_UNIQUE_ID 
            + " VARCHAR(150) NOT NULL, " 
            + KEY_ARTIST_NAME 
            + " VARCHAR(150) NOT NULL, " 
            + KEY_ARTIST_TRACK_COUNT 
            + " VARCHAR(150) NOT NULL,"
            + KEY_ARTIST_STATE 
            + " VARCHAR(150) NOT NULL,"
            + KEY_FOLLOW_STATUS 
            + " VARCHAR(150) NOT NULL,"
            + KEY_ARTIST_ARTIST_IMAGE_URL
            +" VARCHAR(300) NOT NULL)";
    
    private static final String ARTIST_ALBUMS_TABLE_CREATE = 
        	"CREATE TABLE IF NOT EXISTS  "
            + ARTIST_ALBUMS_TABLE 
            + " (" + KEY_ARTIST_ALBUMS_ID 
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_UNIQUE_ID 
            + " VARCHAR(150) NOT NULL, " 
            + KEY_ARTIST_NAME 
            + " VARCHAR(150) NOT NULL, " 
            + KEY_ARTIST_ALBUM_NAME 
            + " VARCHAR(150) NOT NULL,"
            + KEY_ARTIST_ALBUM_ID 
            + " VARCHAR(150) NOT NULL,"
            + KEY_ARTIST_ALBUM_ARTWORK_URL 
            + " VARCHAR(150) NOT NULL,"
            + KEY_ARTIST_ALBUM_DATE
            +" VARCHAR(300) NOT NULL)";
    
    private DatabaseHelper mDbHelper;

    private SQLiteDatabase mDb;
    
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "BrandNewMusic_DB";

    
    private final Context mContext;
    
    private static final String TAG = "BrandNewMusic";
    
    public BNMdbUtils (Context context) {
		this.mContext = context;
	}
    
    public BNMdbUtils open() throws SQLException {
		mDbHelper = new DatabaseHelper(mContext);
		mDb = mDbHelper.getWritableDatabase();

		return this;
	}

	public void close() {
		mDbHelper.close();
	}
    
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            
        }
        
        @Override
		public void onCreate(SQLiteDatabase db) {
        	Log.v("Creating Database", DATABASE_NAME);
			db.execSQL(HOME_TABLE_CREATE);
			db.execSQL(ARTIST_TABLE_CREATE);
			db.execSQL(ARTIST_ALBUMS_TABLE_CREATE);
		}
        
        @Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
                    + " which destroys all old data");
			
			List<String> HomeColumns;
			db.execSQL(HOME_TABLE_CREATE);
			HomeColumns =  BNMdbUtils.getColumns(db, HOME_TABLE);
			db.execSQL("ALTER TABLE " + HOME_TABLE + " RENAME TO temp_" + HOME_TABLE);
			db.execSQL(HOME_TABLE_CREATE);
			HomeColumns.retainAll(BNMdbUtils.getColumns(db, HOME_TABLE));
			String homecols = BNMdbUtils.join(HomeColumns, ",");
			db.execSQL(String.format("INSERT INTO %s (%s) SELECT %s FROM temp_%s", HOME_TABLE, homecols, homecols, HOME_TABLE));
			db.execSQL("DROP TABLE IF EXISTS temp_" + HOME_TABLE);
			onCreate(db);
			
			List<String> ArtistsColumns;
            db.execSQL(ARTIST_TABLE_CREATE);
            ArtistsColumns = BNMdbUtils.getColumns(db, ARTIST_TABLE);
            db.execSQL("ALTER TABLE " + ARTIST_TABLE + " RENAME TO temp_" + ARTIST_TABLE);
            db.execSQL(ARTIST_TABLE_CREATE);
            ArtistsColumns.retainAll(BNMdbUtils.getColumns(db,ARTIST_TABLE));
            String artistcols = BNMdbUtils.join(ArtistsColumns, ",");
            db.execSQL(String.format("INSERT INTO %s (%s) SELECT %s FROM temp_%s", ARTIST_TABLE, artistcols, artistcols, ARTIST_TABLE));   
            db.execSQL("DROP TABLE IF EXISTS temp_" + ARTIST_TABLE);
            onCreate(db);
			
            List<String> AlbumsColumns;
            db.execSQL(ARTIST_ALBUMS_TABLE_CREATE);
            AlbumsColumns = BNMdbUtils.getColumns(db, ARTIST_ALBUMS_TABLE);
            db.execSQL("ALTER TABLE " + ARTIST_ALBUMS_TABLE + " RENAME TO temp_" + ARTIST_ALBUMS_TABLE);
            db.execSQL(ARTIST_ALBUMS_TABLE_CREATE);
            AlbumsColumns.retainAll(BNMdbUtils.getColumns(db,ARTIST_ALBUMS_TABLE));
            String albumscols = BNMdbUtils.join(AlbumsColumns, ",");
            db.execSQL(String.format("INSERT INTO %s (%s) SELECT %s FROM temp_%s", ARTIST_ALBUMS_TABLE, albumscols, albumscols, ARTIST_ALBUMS_TABLE));   
            db.execSQL("DROP TABLE IF EXISTS temp_" + ARTIST_ALBUMS_TABLE);
            onCreate(db);
			
		}
    }
        /**
	     * Credits http://goo.gl/7kOpU
	     * @param db
	     * @param tableName
	     * @return List<String>
	     */
	    public static List<String> getColumns(SQLiteDatabase db, String tableName) {
	    	Log.v("List<String>", tableName);
	        List<String> ar = null;
	        Cursor c = null;

	        try {
	            c = db.rawQuery("SELECT * FROM " + tableName + " LIMIT 1", null);

	            if (c != null) {
	                ar = new ArrayList<String>(Arrays.asList(c.getColumnNames()));
	            }

	        } catch (Exception e) {
	            Log.v(tableName, e.getMessage(), e);
	            e.printStackTrace();
	        } finally {
	            if (c != null)
	                c.close();
	        }
	        return ar;
	    }

	    public static String join(List<String> list, String delim) {
	        StringBuilder buf = new StringBuilder();
	        int num = list.size();
	        for (int i = 0; i < num; i++) {
	            if (i != 0)
	                buf.append(delim);
	            buf.append((String)list.get(i));
	        }
	        return buf.toString();
	    }
	    
	    
	    
	    
	    
	    /**
	     * The Below code will Insert  the Artists  
	     * @param  ContentValues values 
	     * @return
	     */
	    public void InsertArtists(ContentValues value) {
            mDb.insertOrThrow(ARTIST_TABLE, null, value);
            

	    }
	    
	    /**
		 * Convenience method for deleting rows in the database
		 * @param String table
		 * @return
		 */
		public void clearDB(String table) {
			mDb.delete(table, null, null);

		}
	    /**
	     * The Below code will Insert  the Artist Albums 
	     * @param  ContentValues values 
	     * @return
	     */
	    public void InsertArtistAlbums(ContentValues value) {
            mDb.insertOrThrow(ARTIST_ALBUMS_TABLE, null, value);
            Log.i("PostInsert Albums", value.toString());

	    }
	    

	    /**
	     * The Below code will Insert  the Home Tab 
	     * @param  ContentValues values 
	     * @return
	     */
	public void InsertHome(ContentValues value) {
		mDb.insertOrThrow(HOME_TABLE, null, value);

	}

	/**
	 * The Below code will Delete Home Elements
	 * 
	 * @param
	 * @return
	 */
	public void deleteDB(String KEY_UNIQUE_ID) {
		String where = "artist_id='" + KEY_UNIQUE_ID + "'";
		mDb.delete(HOME_TABLE, where, null);

	}

	/**
	 * The Below code will Update Artist Elements
	 * 
	 * @param ContentValues
	 *            value,String KEY_UNIQUE_ID
	 * @return
	 */
	public void update(ContentValues value, String KEY_UNIQUE) {

		mDb.update(ARTIST_TABLE, value, KEY_UNIQUE_ID + "="+ KEY_UNIQUE, null);
				
	}

	/**
	 * The Below code will Update the Artists followed in the Home Tab
	 * 
	 * @param ContentValues
	 *            values (artist_id,artist_name,track_count,image_url)
	 * @return
	 */
	public void UpdateFeaturedArtist(ContentValues values) {
		mDb.update(HOME_TABLE, values, "artist_id = '1'", null);

	}
	    
	    /**
	     * The Below code will ...Export a List Array from @Table Home Tab 
	     * @param
	     * @return List<String[]>
	     */
	    
	    public List<String[]> ExportHomeTable ()
	    {
	    List<String[]> list = new ArrayList<String[]>();
	    String sql = "SELECT * FROM " + HOME_TABLE ;
        Cursor cursor = mDb.rawQuery(sql, null); 
	    int x=0;
	    if (cursor.moveToFirst()) {
	       do {
	        String[] b1=new String[]{cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4)}; 
	        list.add(b1);
	        x=x+1;
	       } while (cursor.moveToNext());
	    }
	    if (cursor != null && !cursor.isClosed()) {
	       cursor.close();
	    } 
	    cursor.close();
	    
	    return list;
	   }
	
	    
	    /**
	     * The Below code will ...Export a List Array from @Table ARTIST_TABLE
	     * @param
	     * @return List<String[]> 

	     * 
	     */
  
	    public List<String[]> ExportArtistTable ()
	    {
	    List<String[]> list = new ArrayList<String[]>();
	    String sql = "SELECT * FROM " + ARTIST_TABLE ;
        Cursor cursor = mDb.rawQuery(sql, null); 
	    int x=0;
	    if (cursor.moveToFirst()) {
	       do {
	        String[] b1=new String[]{cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6)}; 
	        list.add(b1);
	        x=x+1;
	       } while (cursor.moveToNext());
	    }
	    if (cursor != null && !cursor.isClosed()) {
	       cursor.close();
	    } 
	    cursor.close();
	    
	    return list;
	   }
	    
	    /**
	     * The Below code will ...Export a List Array from @Table ALBUMS_TABLE
	     * @param
	     * @return List<String[]> 

	     * 
	     */
  
	    public List<String[]> ExportAlbumsTable (String artist_id)
	    {
	    List<String[]> list = new ArrayList<String[]>();
	    String sql = "SELECT * FROM " + ARTIST_ALBUMS_TABLE +" WHERE " +KEY_UNIQUE_ID +" = "+artist_id;
        Cursor cursor = mDb.rawQuery(sql, null); 
	    int x=0;
	    if (cursor.moveToFirst()) {
	       do {
	        String[] b1=new String[]{cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6)}; 
	        list.add(b1);
	        x=x+1;
	       } while (cursor.moveToNext());
	    }
	    if (cursor != null && !cursor.isClosed()) {
	       cursor.close();
	    } 
	    cursor.close();
	    
	    return list;
	   }

}
    

