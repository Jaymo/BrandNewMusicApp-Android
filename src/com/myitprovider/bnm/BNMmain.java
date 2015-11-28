
package com.myitprovider.bnm;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.myitprovider.bnm.adapters.BNMFragmentPagerAdapter;
import com.myitprovider.bnm.util.Pref;


public class BNMmain extends  SherlockFragmentActivity  {
    
	
	
	private ViewPager viewPager;
    private BNMFragmentPagerAdapter mAdapter;
    private ActionBar actionBar;
    StringBuilder urlBuilder;
    private String[] tabs = { "HOME", "TOP TEN", "GENRE", "ARTIST" };
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setHomeButtonEnabled(true);
        
        
        FragmentManager fm = getSupportFragmentManager();
        Pref.loadSettings(this);
        ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener(){
        	@Override
        	public void onPageSelected(int position) {        		
        		super.onPageSelected(position);
        		 actionBar.setSelectedNavigationItem(position);        		
        	}
        	
        };
        
        viewPager.setOnPageChangeListener(pageChangeListener);
        
        mAdapter = new BNMFragmentPagerAdapter(fm);
        
        viewPager.setAdapter(mAdapter);

        
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			
			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {				
			}
			
			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				viewPager.setCurrentItem(tab.getPosition());
				
			}
			
			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
			}
		};
        
    
    for (String tab_name : tabs) {
    	
    	actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(tabListener));
                
    }       
    
    }
    
   
    
    

}


