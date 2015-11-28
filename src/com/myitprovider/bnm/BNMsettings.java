
package com.myitprovider.bnm;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;

import com.WazaBe.HoloEverywhere.widget.Toast;
import com.myitprovider.bnm.util.Pref;


public class BNMsettings extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	
	public static final String PREFS_NAME = "BNM";
	
	public static final String NOTIFICATIONS = "enable_notications";

	 public static final String SYNC = "enable_sync";

	 public static final String CACHE = "enable_cache";
	 
	 public static final String KEY_POWERED_PREFERENCE = "powered_preference";
	 
	 public static final String KEY_LOGOUT_PREFERENCE = "logout_preference";
	 
	 public static final String KEY_EMAIL_PREFERENCE = "email_preference";

	 private SharedPreferences settings;

	 private SharedPreferences.Editor editor;
	 
	 private CheckBoxPreference notifications;
	 private SwitchPreference notificationsv14;
	 private CheckBoxPreference sync;
	 private SwitchPreference syncv14;
	 private CheckBoxPreference cache;
	 private SwitchPreference cachev14;
	 private String versionName;

	 private StringBuilder versionLabel;
	 	 
     private Preference about;
     private Preference email;
	 private Preference logout;
	 
	 @SuppressWarnings("deprecation")
	@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        addPreferencesFromResource(R.xml.settings);
	        
	        try {
				versionName = getPackageManager().getPackageInfo(
						this.getPackageName(), 0).versionName;
				versionLabel = new StringBuilder(getString(R.string.app_name));
				versionLabel.append(" ");
				versionLabel.append("V ");
				versionLabel.append(versionName);
				versionLabel.append(" Beta");
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
	        if (Build.VERSION.SDK_INT < 14){
	        notifications = (CheckBoxPreference)getPreferenceScreen().findPreference(NOTIFICATIONS);
	        sync = (CheckBoxPreference)getPreferenceScreen().findPreference(SYNC);
	        cache = (CheckBoxPreference)getPreferenceScreen().findPreference(CACHE);}
	        if (Build.VERSION.SDK_INT > 14){
	        notificationsv14 = (SwitchPreference)getPreferenceScreen().findPreference(NOTIFICATIONS);
	        syncv14 = (SwitchPreference)getPreferenceScreen().findPreference(SYNC);
	        cachev14 = (SwitchPreference)getPreferenceScreen().findPreference(CACHE);}
	        
	        
	     // Save settings changes.
	       this.savePreferences();
	       
	         about = (Preference) getPreferenceScreen().findPreference(KEY_POWERED_PREFERENCE);
	   	     about.setTitle(versionLabel.toString());
	   	     about.setSummary(R.string.powered_by);
	   	     
	   	     email = (Preference) getPreferenceScreen().findPreference(KEY_EMAIL_PREFERENCE);
	   	     email.setTitle(R.string.email_feedback);
	   	    
	   	     
	   	  Preference emailPreference = findPreference(KEY_EMAIL_PREFERENCE);
			emailPreference
					.setOnPreferenceClickListener(new OnPreferenceClickListener() {
						public boolean onPreferenceClick(Preference preference) {
							Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			            	String aEmailList[] = { "feedback@bnmapp.com"};
			            	emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
			            	emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "BNM FeedBack");
			            	emailIntent.setType("plain/text");  
			            	startActivity(Intent.createChooser(emailIntent, "Select Email Client:"));
							return true;
						}
					});
	   	    
	   	  logout = (Preference) getPreferenceScreen().findPreference(KEY_LOGOUT_PREFERENCE);
	   	  logout.setTitle(R.string.logout_acc);
	   	  logout.setSummary("Account: "+Pref.user); 
	   	    
			Preference poweredPreference = findPreference(KEY_LOGOUT_PREFERENCE);
			poweredPreference
					.setOnPreferenceClickListener(new OnPreferenceClickListener() {
						public boolean onPreferenceClick(Preference preference) {
							Pref.loadSettings(BNMsettings.this);
				        	Pref.user="";
				        	Pref.saveSettings(BNMsettings.this);
				        	Intent intent = new Intent(BNMsettings.this, BNMlogin.class);
				            startActivity(intent);
				            finish();
				            Toast.makeText(BNMsettings.this, "Sucessfully Logged Out from BNM!", Toast.LENGTH_LONG).show(); 
							return true;
						}
					});
	   	    
	 }
	 
	 
	 @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	protected void savePreferences() {
		 settings = getSharedPreferences(PREFS_NAME, 0);

		 
		 editor = settings.edit();
		 if (Build.VERSION.SDK_INT < 14){
		 editor.putBoolean("Notifications", notifications.isChecked());
	     editor.putBoolean("Sync", sync.isChecked());
	     editor.putBoolean("Cache", cache.isChecked());
		 }
		 if (Build.VERSION.SDK_INT > 14){
			 editor.putBoolean("Notifications", notificationsv14.isChecked());
		     editor.putBoolean("Sync", syncv14.isChecked());
		     editor.putBoolean("Cache", cachev14.isChecked());
		}
			 
	     
	     editor.commit();
	 }
	 
	@Override
	public void onSharedPreferenceChanged(SharedPreferences prefs, String value) {
		if (value.equals(NOTIFICATIONS)) {
			 boolean NotificationsON = prefs.getBoolean(NOTIFICATIONS, false);
			 if(NotificationsON){
				 Pref.loadSettings(this);
				 Pref.notifications=true;
				 Pref.saveSettings(this);
				 Toast.makeText(this, "Notifications from Followed Artists ON", Toast.LENGTH_LONG).show();
			 }
			 else{
				 Pref.loadSettings(this);
				 Pref.notifications=false;
				 Pref.saveSettings(this);
				 Toast.makeText(this, "Notifications from Followed Artists OFF", Toast.LENGTH_LONG).show();
			 }
			 
		 }
		 
		 else if (value.equals(SYNC)) {
			 boolean SYNCON = prefs.getBoolean(SYNC, false);
			 if(SYNCON){
				 Pref.loadSettings(this);
				 Pref.sync=true;
				 Pref.saveSettings(this);
			 }
			 else{
				 Pref.loadSettings(this);
				 Pref.sync=false;
				 Pref.saveSettings(this);
			 }
			 
		 }
		 else if (value.equals(CACHE)) {
			 boolean CACHEON = prefs.getBoolean(CACHE, false);
			 if(CACHEON){
				 Pref.loadSettings(this);
				 Pref.cache=true;
				 Pref.saveSettings(this);
			 }
			 else{
				 Pref.loadSettings(this);
				 Pref.cache=false;
				 Pref.saveSettings(this);
			 }
			 
		 }
		
	}
	
	@SuppressWarnings("deprecation")
	@Override
    protected void onResume() {
        super.onResume();

        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }
	@SuppressWarnings("deprecation")
    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
                

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
    @Override
	public void onBackPressed(){
    	super.onBackPressed();
    	Intent i = new Intent(this, BNMfragmentHost.class); 
	    startActivity(i);
	    finish();
	}	
}
