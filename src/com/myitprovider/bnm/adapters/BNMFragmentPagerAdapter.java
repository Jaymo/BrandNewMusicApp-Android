
package com.myitprovider.bnm.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.myitprovider.bnm.fragments.ArtistListFragment;
import com.myitprovider.bnm.fragments.GenreListFragment;
import com.myitprovider.bnm.fragments.HomeListFragment;
import com.myitprovider.bnm.fragments.TopTenListfragment;

public class BNMFragmentPagerAdapter extends  FragmentPagerAdapter {
	
	final int PAGE_COUNT = 4;
	 
    public BNMFragmentPagerAdapter (FragmentManager fm) {
        super(fm);
    }
    
    
    @Override
    public Fragment getItem(int index) {
        
        switch(index){
            case 0:
            	HomeListFragment home = new HomeListFragment();	
				return home;
            	
            case 1:
            	TopTenListfragment topten = new TopTenListfragment();					
				return topten;
            	
            case 2:
            	GenreListFragment genre = new GenreListFragment();				
				return genre;
            case 3:
            	ArtistListFragment artist = new ArtistListFragment();				
				return artist;
               
        }
        return null;
    }
    

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

}
