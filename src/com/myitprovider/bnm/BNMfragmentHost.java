
package com.myitprovider.bnm;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.android.gcm.GCMRegistrar;
import com.myitprovider.bnm.fragments.ArtistListFragment;
import com.myitprovider.bnm.fragments.GenreListFragment;
import com.myitprovider.bnm.fragments.HomeListFragment;
import com.myitprovider.bnm.fragments.TopTenListfragment;


public class BNMfragmentHost extends  SherlockFragmentActivity  {	
	ActionBar bar;
	String regId;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    bar = getSupportActionBar();
	    bar.setDisplayHomeAsUpEnabled(true);
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.Tab tab1 = bar.newTab();
        ActionBar.Tab tab2 = bar.newTab();
        ActionBar.Tab tab3 = bar.newTab();
        ActionBar.Tab tab4 = bar.newTab();
        bar.setTitle("Brand New Music");
        bar.setIcon(R.color.transparent);
        tab1.setText("HOME");
        tab2.setText("TOP TEN");
        tab3.setText("GENRE");
        tab4.setText("ARTIST");
        tab1.setTabListener(new MyTabListener());
        tab2.setTabListener(new MyTabListener());
        tab3.setTabListener(new MyTabListener());
        tab4.setTabListener(new MyTabListener());
        bar.addTab(tab1);
        bar.addTab(tab2);
        bar.addTab(tab3);
        bar.addTab(tab4);
        GCMRegistrar.checkDevice(BNMfragmentHost.this);     
 		regId = GCMRegistrar.getRegistrationId(BNMfragmentHost.this);
 		if (regId.equals("")) {		
 			GCMRegistrar.register(BNMfragmentHost.this, BNMwelcome.SENDER_ID);
 		}
	
        
	}
	
	
	private class MyTabListener implements ActionBar.TabListener
    {

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if(tab.getPosition()==0)
			{
				HomeListFragment home = new HomeListFragment();	
				ft.replace(android.R.id.content, home);
				invalidateOptionsMenu();
				
			}
			if(tab.getPosition()==1)
			{
				TopTenListfragment topten = new TopTenListfragment();
				ft.replace(android.R.id.content, topten);
				invalidateOptionsMenu();
			}
			if(tab.getPosition()==2)
			{
				GenreListFragment genre = new GenreListFragment();
				ft.replace(android.R.id.content, genre);
				invalidateOptionsMenu();
			}
			if(tab.getPosition()==3)
			{
				ArtistListFragment artist = new ArtistListFragment();
				ft.replace(android.R.id.content, artist);
				invalidateOptionsMenu();
			}
		}
		
		
		

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			
			
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			
			
		}
		
	    
		
		}
	
	public void SetABTitle(String title){
		bar.setTitle(title);
	}
	
   @Override
   protected void onDestroy() {
        
        super.onDestroy();
    }
   @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
	    public void onSaveInstanceState(Bundle toSave) { 
	        super.onSaveInstanceState(toSave);
	       
	 }
    @Override
    protected void onRestart() {
        
        super.onRestart();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onStop() {
        finish();
        super.onStop();
    }
}