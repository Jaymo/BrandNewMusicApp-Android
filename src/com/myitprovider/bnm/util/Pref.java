package com.myitprovider.bnm.util;

import android.content.Context;
import android.content.SharedPreferences;

public class Pref {
	
	private static SharedPreferences settings;

    private static SharedPreferences.Editor editor;
    
    public static String user="";
    
    public static int _REGISTERED = 0;
    
    public static int _ArtistImport = 0;
    
    public static final String PREFS_NAME = "BNM";
    
    public static int clicked = 0;
    
    public static String home_artist_name="";
    
    public static String home_artist_id="";
    
    public static String genre_id="";
    
    public static String genre_name="";
    
    public static String home_albums_name="";
    
    public static String home_albums_id="";
    
    public static String artist_id="";
    
    public static String track_name="";
    public static String track_artist_name="";
    public static String track_album_art="";
    
    
    public static boolean notifications =true;
    
    public static boolean sync =true;
    
    public static boolean cache =false;
    
    public static void loadSettings(Context context) {
        final SharedPreferences settings = (SharedPreferences) context.getSharedPreferences(PREFS_NAME, 0);     
        user=settings.getString("User", user);
        home_artist_name=settings.getString("Home_Artist_Name", home_artist_name);
        home_artist_id=settings.getString("Home_Artist_ID", home_artist_id);
        artist_id=settings.getString("Artist_ID", artist_id);
        home_albums_name=settings.getString("Home_Albums_Name", home_albums_name);
        home_albums_id=settings.getString("Home_Albums_ID", home_albums_id);
        genre_id=settings.getString("Genre_ID", genre_id);
        genre_name=settings.getString("Genre_Name", genre_name);
        _REGISTERED =settings.getInt("ID", _REGISTERED);
        _ArtistImport =settings.getInt("AI", _ArtistImport);
        clicked =settings.getInt("Clicked", clicked);
        
        notifications = settings.getBoolean("Notifications", notifications);
        sync = settings.getBoolean("Sync", sync);
        cache = settings.getBoolean("Cache", cache);
        
        track_name=settings.getString("Track_Name", track_name);
        track_artist_name=settings.getString("Track_Artist_Name", track_artist_name);
        track_album_art=settings.getString("Track_Album_Art", track_album_art);
    }
    
    public static void saveSettings(Context context) {
        settings = (SharedPreferences) context.getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();       
        editor.putString("User", user);
        editor.putString("Home_Artist_Name", home_artist_name);
        editor.putString("Home_Artist_ID", home_artist_id);
        editor.putString("Home_Albums_Name", home_albums_name);
        editor.putString("Home_Albums_ID", home_albums_id);
        editor.putString("Artist_ID", artist_id);
        editor.putString("Genre_ID", genre_id);
        editor.putString("Genre_Name", genre_name);
        editor.putInt("ID",_REGISTERED);
        editor.putInt("AI",_ArtistImport);
        editor.putInt("Clicked", clicked);
 
        editor.putBoolean("Notifications", notifications);
	    editor.putBoolean("Sync", sync);
	    editor.putBoolean("Cache", cache);
	    
	    editor.putString("Track_Name", track_name);
        editor.putString("Track_Artist_Name", track_artist_name);
        editor.putString("Track_Album_Art", track_album_art);
        editor.commit();
    }


}
