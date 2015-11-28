
package com.myitprovider.bnm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.WazaBe.HoloEverywhere.sherlock.SActivity;
import com.actionbarsherlock.app.ActionBar;
import com.crashlytics.android.Crashlytics;
import com.myitprovider.bnm.util.Pref;

public class BNMwelcome extends SActivity {
	Button mSignIn, mSignUp;	
	public static  String SENDER_ID = "1051848311907";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Crashlytics.start(this);
        setForceThemeApply(true);
		setContentView(R.layout.layout_bnmwelcome);
         ActionBar bar = getSupportActionBar();
	     bar.hide();
	     Pref.loadSettings(BNMwelcome.this);
	     if(!(Pref.user=="")){
	    	 Intent i = new Intent(BNMwelcome .this, BNMfragmentHost.class); 
	    	 startActivity(i);
	    	 finish();
	     }
		
        mSignIn = (Button)findViewById(R.id.welcomesignin);
        mSignUp = (Button)findViewById(R.id.welcomesignup);
        
        mSignIn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(android.view.View v) {
				Intent j = new Intent(BNMwelcome .this, BNMlogin.class); 
			    startActivity(j);
			   finish();
				
			}
        });
        mSignUp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(android.view.View v) {
				Intent k = new Intent(BNMwelcome .this, BNMregister.class);  
			    startActivity(k);
			   finish();
			}
        });
    
    }
    
    
			     

}





