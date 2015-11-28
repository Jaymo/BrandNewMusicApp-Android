package com.myitprovider.bnm;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.myitprovider.bnm.util.Pref;
import com.myitprovider.bnm.util.Util;

public class GCMIntentService extends GCMBaseIntentService {
	private static final String TAG = "BNM GCMIntentService";
    InputStream inputstream;
	ArrayList<NameValuePair> namevaluepairs;
	String res;
 
	
    public GCMIntentService() {
        super(BNMwelcome.SENDER_ID);
    }

    
    @Override
    protected void onRegistered(Context context, String registrationId) {
    	 String  email= Pref.user;
    	 new register().execute(registrationId,email);
    	 String msg ="Brand New Music";
    	 Util.generateNotification(context, msg);
    }

    
    @Override
    protected void onUnregistered(Context context, String registrationId) {
    	
    }

    
    @Override
    protected void onMessage(Context context, Intent intent) {
    	 
        String message = intent.getExtras().getString("message");
		
		if(message.contains("update")){
			
			Util.generateUpdateNotification(context, message);
			return;
		}
		else {
			if(Pref.notifications){
			Util.generateNotification(context, message);
			}
		}
    }
    
    
    @Override
    protected void onDeletedMessages(Context context, int total) {
        
    }

    
    @Override
    public void onError(Context context, String errorId) {
    	 Log.i(TAG, "Received error: " + errorId);
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
    	Log.i(TAG, "Received recoverable error: " + errorId);
    	 return super.onRecoverableError(context, errorId);
       
    }

    
    
    
    public class register extends AsyncTask<String, Integer, Void> {
	   @Override
		protected Void doInBackground(String ... params) {
		   String URL =getResources().getString(R.string.URL);
			try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(URL);
			namevaluepairs = new ArrayList<NameValuePair>();
			namevaluepairs.add (new BasicNameValuePair("do","gcmid"));
			namevaluepairs.add (new BasicNameValuePair("gcmid",params[0]));
			namevaluepairs.add (new BasicNameValuePair("email",params[1]));
			httpPost.setEntity (new UrlEncodedFormEntity(namevaluepairs));
			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			inputstream=entity.getContent();
		       }
	        catch(Exception e){
	            Log.e(TAG, "Error kwa http connection "+e.toString());
	    }
			try
			{
				
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
        }
            inputstream.close();
             res = sb.toString().trim();
             
             
		}
		
		catch(Exception e){
            Log.e(TAG, "Error converting result "+e.toString());
    }
			
			return null;
		}
		
		@Override
		protected void onPostExecute(final Void result) {
			
		}

		
		
	}

}
