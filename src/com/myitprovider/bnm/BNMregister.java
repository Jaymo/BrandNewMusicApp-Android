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

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
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
public class BNMregister extends Activity {
	
	public static  String SENDER_ID = "194836516470";
	private String sFirstname,sLastname,sEmail,sPswd,sConfirmpswd;
	private Button mRegister;
	private EditText mFirstname,mLastname,mEmail,mPswd,mConfirmpswd,mSecurity,mAnswer;
	private TextView mTerms;
	private boolean mError = false;
	private String mErrorMessage = "";
	HttpPost httpPost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	InputStream inputstream;
	SharedPreferences app_preferences;
	ArrayList<NameValuePair> namevaluepairs;
	String res="",Y,N,regId;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.layout_bnmregistration);
         ActionBar bar = getActionBar();
         bar.setIcon(R.color.transparent);
	     bar.setTitle("BNM Registration");
	     bar.setDisplayHomeAsUpEnabled(true);
	     
	        mFirstname = (EditText)findViewById(R.id.fname);
	        mLastname = (EditText)findViewById(R.id.lname);
	        mEmail = (EditText)findViewById(R.id.email);
	        mPswd = (EditText)findViewById(R.id.pswd);
	        mConfirmpswd = (EditText)findViewById(R.id.confirmpswd); 
	        mRegister =(Button)findViewById(R.id.register);
	     
	        mSecurity = (EditText)findViewById(R.id.security);
	        mAnswer = (EditText)findViewById(R.id.answer);
	        
	        mTerms  =(TextView)findViewById(R.id.terms);
	        mTerms.setText(Html.fromHtml(
	                "<b><font color=\"#000000\">By tapping on Sign Up,you are agreeing to the </font></b>" +
	                "<a href=\"http://www.bnmapp.com/privacy-policy.php\"><u>Terms of Service</u></a> " +
	                "<b><font color=\"#000000\">and</font></b> "+ "<a href=\"http://www.bnmapp.com/privacy-policy.php\"><u>Privacy Policy</u></a> " ));
	        mTerms.setMovementMethod(LinkMovementMethod.getInstance());
	        
	        mRegister.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);   
	            	imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
	            	sFirstname =mFirstname.getText().toString();
	            	sLastname =mLastname.getText().toString();
	            	sEmail=mEmail.getText().toString();
	            	sPswd =mPswd.getText().toString();
	            	sConfirmpswd =mConfirmpswd.getText().toString();
	            	mError = false;
	            	if (TextUtils.isEmpty(mFirstname.getText())) {
	            		mFirstname.setError(getString(R.string.empty_firstname));
	            		mErrorMessage = getString(R.string.empty_firstname)+"\n";
	                    mError = true;
	                }
	                if (TextUtils.isEmpty(mLastname.getText())) {
	                	mLastname.setError(getString(R.string.empty_lastname));
	                	mErrorMessage += getString(R.string.empty_lastname)+"\n";
	                    mError = true;
	                }
	                if (TextUtils.isEmpty(mEmail.getText())) {
	                	mEmail.setError(getString(R.string.empty_email));
	                	mErrorMessage += getString(R.string.empty_email)+"\n";
	                    mError = true;
	                }
	                if (TextUtils.isEmpty(mPswd.getText())) {
	                	mPswd.setError(getString(R.string.empty_password));
	                	mErrorMessage += getString(R.string.empty_password)+"\n";
	                    mError = true;
	                }
	                
	                if (!Util.validateEmail(sEmail)) {
	                	mEmail.setError(getString(R.string.invalid_email));
	                	mErrorMessage += getString(R.string.invalid_email)+"\n";
	                    mError = true;
	                }
	                
	                if(!sPswd.equals(sConfirmpswd)){
	                	mErrorMessage += getString(R.string.pswd_mismatch)+"\n";
	                	mError = true;
	                }
	                
	                if (TextUtils.isEmpty(mSecurity.getText())) {
	                	mPswd.setError(getString(R.string.securityquest));
	                	mErrorMessage += getString(R.string.securityquest)+"\n";
	                    mError = true;
	                }
	                if (TextUtils.isEmpty(mAnswer.getText())) {
	                	mPswd.setError(getString(R.string.empty_answer));
	                	mErrorMessage += getString(R.string.empty_answer)+"\n";
	                    mError = true;
	                }
	                
	                if (!mError) {	
	                	
	                	if (Util.isConnected(BNMregister.this)) {
	                    	new register().execute();
	                        }
	                        else{
	                        	Toast.makeText(BNMregister.this, R.string.no_internet, Toast.LENGTH_LONG).show();
	                     }
	                }
	                else{
	                	Log.w("Brand New Music", mErrorMessage);
	                    mErrorMessage = "";
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
	
	public class register extends
    AsyncTask<Void, Integer, Void> {
    	private ProgressDialog dialog = new ProgressDialog(BNMregister.this);
    	
    	protected void onPreExecute() {
			
    		  dialog.setIndeterminate(true); 
	          dialog.setIndeterminateDrawable(getResources().getDrawable(R.anim.progress_dialog_animation));
	          dialog.setMessage("Registering User....");
			  dialog.show();

    		         }
		@Override
		protected Void doInBackground(Void... arg0) {
			String URL =getResources().getString(R.string.URL);
			try
			{
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(URL );
				//adding our data
				namevaluepairs = new ArrayList<NameValuePair>();
				namevaluepairs.add (new BasicNameValuePair("do","register"));
				namevaluepairs.add (new BasicNameValuePair("username",sFirstname+" "+sLastname));
				namevaluepairs.add (new BasicNameValuePair("email",sEmail));
				namevaluepairs.add (new BasicNameValuePair("password",sPswd));
				httpPost.setEntity (new UrlEncodedFormEntity(namevaluepairs));
				
				//Execute post request
				HttpResponse response = httpclient.execute(httpPost);
				HttpEntity entity = response.getEntity();
				inputstream=entity.getContent();
			}
		    catch(Exception e){
		            Log.e("log_tag", "Error in http connection "+e.toString());
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
	            Log.e("log_tag", "Error converting result "+e.toString());
	    }
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			
			 if(res.equals("1")){
				    
				 
					 Intent intent = new Intent(BNMregister.this, BNMtutorial.class);
		             startActivity(intent);
		             
		             Pref.loadSettings(BNMregister.this);
		     		 Pref.user=sEmail;
		     		 Pref._REGISTERED=1;
		     		 Pref.saveSettings(BNMregister.this);
		     		 finish(); 
		     		
		    		dialog.dismiss();
		    		
		    		final Toast t = Toast.makeText(BNMregister.this, "Registration Submitted!\n Check Email to activate Account before Login" ,
                            Toast.LENGTH_LONG);
                    t.show();
                    String notification ="Registration Received.Check Email to activate Account";
                    Util.generateNotification(BNMregister.this,notification);
		    	return;	
			 }
			 else{
				   dialog.dismiss();
				 
				 new AlertDialog.Builder(BNMregister.this,AlertDialog.THEME_HOLO_LIGHT)
			        .setTitle(R.string.notification_prompt_reg)
			        .setMessage(Html.fromHtml("<font color=\"#000000\">Sorry registration failed,the Names provided may " +
			        		" not be valid </font>" +
			                "<font color=\"#000000\"> or email may already have been Registered.</font>" ))
			        .setNeutralButton(R.string.ok_btn,
			                new DialogInterface.OnClickListener() {
			                    @Override
			                    public void onClick(DialogInterface dialog, int which) {
			                    
			                    }
			                }).show();
		        	return;
				 
			 }
			 
			 
		}
    }
	
	

}
