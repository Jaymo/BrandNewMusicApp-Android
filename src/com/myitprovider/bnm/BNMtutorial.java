
package com.myitprovider.bnm;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.WazaBe.HoloEverywhere.sherlock.SActivity;
import com.actionbarsherlock.app.ActionBar;
import com.myitprovider.bnm.adapters.BNMPagerAdapter;
import com.viewpagerindicator.LinePageIndicator;


public class BNMtutorial extends SActivity  {
	
	private ViewPager viewPager;
	private BNMPagerAdapter mAdapter;
	LinePageIndicator lIndicator;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_bnm_welcome);
		ActionBar bar = getSupportActionBar();
	    bar.hide();
		viewPager = (ViewPager) findViewById(R.id.pager);
		
		mAdapter = new BNMPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(mAdapter);
		lIndicator = (LinePageIndicator) findViewById(R.id.indicator);
		lIndicator.setViewPager(viewPager);
	
		lIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

  protected void onDestroy() {
        super.onDestroy();
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onRestart() {
        super.onRestart();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onStop() {
        super.onStop();
    }

}
