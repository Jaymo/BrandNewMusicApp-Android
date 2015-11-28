
package com.myitprovider.bnm.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.myitprovider.bnm.fragments.Slide1Fragment;
import com.myitprovider.bnm.fragments.Slide2Fragment;
import com.myitprovider.bnm.fragments.Slide3Fragment;
import com.myitprovider.bnm.fragments.Slide4Fragment;
import com.myitprovider.bnm.fragments.Slide5Fragment;
import com.myitprovider.bnm.fragments.Slide6Fragment;
import com.myitprovider.bnm.fragments.Slide7Fragment;

public class BNMPagerAdapter extends FragmentPagerAdapter {
	 

	public  BNMPagerAdapter(FragmentManager fm) {
		super(fm);
		
		
	}
	
	
	@Override
	public Fragment getItem(int index) {
		switch (index) {
		case 0:
		
			return new Slide1Fragment();
		case 1:
			
			return new Slide2Fragment();
		case 2:
			
			return new Slide3Fragment();
		case 3:
			
			return new Slide4Fragment();
        case 4:
			
			return new Slide5Fragment();
        case 5:
			
			return new Slide6Fragment();
         case 6:
			
			return new Slide7Fragment();
		
		}
		

			

		return null;
		
	}

	@Override
	public int getCount() {
		return 7;
	}

}
