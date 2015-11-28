
package com.myitprovider.bnm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.myitprovider.bnm.util.Pref;
import com.myitprovider.bnm.util.Util;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class BNMlogin extends Activity {
	
	EditText mUsername,mPassword;
	Button mLogin;
	TextView mForgot;
	String sUsername,sPassword,res="";
	private String mErrorMessage = "";
	private boolean mError = false;
	HttpPost httpPost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	InputStream inputstream;
	SharedPreferences app_preferences;
	ArrayList<NameValuePair> namevaluepairs;
	private static final String TAG = "Brand New Music";
	 public static Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_bnmlogin);
		 ActionBar bar = getActionBar();
		 bar.setIcon(R.color.transparent);
	     bar.setTitle("BNM Login");
	     bar.setDisplayHomeAsUpEnabled(true);
	     context = getApplicationContext();
	     
	      
		mUsername = (EditText)findViewById(R.id.username);
        mPassword =(EditText)findViewById(R.id.password);
        
        mLogin = (Button)findViewById(R.id.login);
        
        mForgot =(TextView)findViewById(R.id.forgot);
        mForgot .setOnClickListener(new View.OnClickListener() {
        
			@Override
			public void onClick(android.view.View v) {
				
				Intent intent = new Intent(BNMlogin.this, BNMforgot.class);
	        	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
	        	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	            startActivity(intent);
			}
        });
        
        mLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(android.view.View v) {
				if(Util.isConnected(BNMlogin.this)){
				sUsername=mUsername.getText().toString();
			    sPassword=mPassword.getText().toString();
			    
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);   
            	imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            	mError = false;
            	
            	
            	if (TextUtils.isEmpty(mUsername.getText())) {
            		mUsername.setError(getString(R.string.empty_username));
            		mErrorMessage = getString(R.string.empty_username)+"\n";
                    mError = true;
                }
                
                
                if (TextUtils.isEmpty(mPassword.getText())) {
                	mPassword.setError(getString(R.string.empty_password));
                	mErrorMessage += getString(R.string.empty_password)+"\n";
                    mError = true;
                }
                if (!mError) {
                	
                if (Util.isConnected(BNMlogin.this)) {
                	
                	new login().execute();
                	
       		    
                }
                else {
                	Toast.makeText(BNMlogin.this, R.string.no_internet, Toast.LENGTH_LONG).show();
                	
                     }
                }
                else{
            		Log.w("Brand New Music", mErrorMessage);
                    mErrorMessage = "";
            	}
                
                	        	
			}
			
			else{
				Toast.makeText(BNMlogin.this, R.string.no_internet, Toast.LENGTH_LONG).show(); 
            }
		}
     });
        
        
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	     switch (item.getItemId()) {
	        case android.R.id.home:
	        	Intent intent = new Intent(this, BNMwelcome.class);
	        	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
	        	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	            startActivity(intent);
	            return true;
	        default:
	            return false;
	     }
	}
	public class login extends AsyncTask<Void, Integer, Void> {
		private ProgressDialog dialog = new ProgressDialog(BNMlogin.this);
		
        
		@Override
    	protected void onPreExecute() {
			
			 
			  dialog.setIndeterminate(true); 
	          dialog.setIndeterminateDrawable(getResources().getDrawable(R.anim.progress_dialog_animation));
	          dialog.setMessage("Authenticating Credentials....");
	          dialog.setCancelable(false);
			  dialog.show(); 
			 
	         
    		         }
		@Override
		protected Void doInBackground(Void... arg0) {
			String URL =getResources().getString(R.string.URL);
			try
			{
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(URL);
				HttpParams params = new BasicHttpParams();
			    HttpConnectionParams.setConnectionTimeout(params, 30000);
			    HttpConnectionParams.setSoTimeout(params, 30000);
				//adding our data
				namevaluepairs = new ArrayList<NameValuePair>(3);
				namevaluepairs.add (new BasicNameValuePair("do","login"));
				namevaluepairs.add (new BasicNameValuePair("email",sUsername));
				namevaluepairs.add (new BasicNameValuePair("password",sPassword));
				httpPost.setEntity (new UrlEncodedFormEntity(namevaluepairs));
				
				//Execute post request
				HttpResponse response = httpclient.execute(httpPost);
				HttpEntity entity = response.getEntity();
				inputstream=entity.getContent();
			}
			catch (ConnectTimeoutException e) {
				return null;
			} catch (SocketTimeoutException ste) {
				return null;
			} catch (NullPointerException nall) {
				return null;
			} catch (MalformedURLException e) {
				return null;
			} catch (IOException e) {
				return null;
			} catch (Exception e) {
				return null;
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
			if (isCancelled())
			{
				return null;
			}
			
			return null;
		}
		@Override
		protected void onProgressUpdate(final Integer... progress) {
			if(login.this.isCancelled())
			{
				dialog.dismiss();
				Toast.makeText(BNMlogin.this, R.string.no_internet, Toast.LENGTH_LONG).show();	
			}
		}
		@Override
	    protected void onCancelled(){
	        super.onCancelled();
	        dialog.dismiss();
	        Toast.makeText(BNMlogin.this, R.string.no_internet, Toast.LENGTH_LONG).show();
		}
		
		@Override
		protected void onPostExecute(Void result) {
			 
	       
	        if(res.equals("0")){
	        	dialog.dismiss();
	        	new AlertDialog.Builder(BNMlogin.this,AlertDialog.THEME_HOLO_LIGHT)
		        .setTitle(R.string.notification_prompt_login)
		        .setMessage(Html.fromHtml("<font color=\"#000000\">Sorry the UserName and Password combination provided  are incorrect or Account is not activated </font>" ))
		        .setNegativeButton(R.string.ok_btn,
		                new DialogInterface.OnClickListener() {
		                    @Override
		                    public void onClick(DialogInterface dialog, int which) {
		                    
		                    } 
		                })
		          .setPositiveButton(R.string.reset,
		                new DialogInterface.OnClickListener() {
		                    @Override
		                    public void onClick(DialogInterface dialog, int which) {
		                    	Intent intent = new Intent(BNMlogin.this, BNMforgot.class);
		        	        	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		        	        	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        	            startActivity(intent);
		                    }
		                })    
		                .show();
	        	return;
	        }
	        if(res.equals("1")){
	        	Intent intent = new Intent(BNMlogin.this, BNMfragmentHost.class);
	            startActivity(intent);
	            dialog.dismiss();
	            Pref.loadSettings(context);
	            Pref.user=sUsername;
	            Pref.saveSettings(context);
	            finish();
	         
	        }
	        
	        else{
	        	dialog.dismiss();
	        	Log.i(TAG,"Login Error Responce"+res);
	        	return;
	        }
	        
		}
	}

}

