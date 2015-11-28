
package com.myitprovider.bnm;

import android.app.Application;

import com.myitprovider.bnm.data.BNMdbUtils;

public class BNMapplication extends Application {
	 public static final String baseurl = "http://bnmapp.com/";
	 public static BNMdbUtils mDb;
	 public static final String TAG = "BNM";
	 
	@Override
   public void onCreate() {
       super.onCreate();

       mDb = new BNMdbUtils(this);
       mDb.open();


   }
	@Override
   public void onTerminate() {
       mDb.close();

       super.onTerminate();
   }

}
