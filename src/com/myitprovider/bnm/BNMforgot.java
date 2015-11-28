
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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.myitprovider.bnm.util.Util;

public class BNMforgot extends Activity {
	
	EditText mUsername;
	Button mSubmit;
	TextView mForgot;
	String sUsername,res="";
	private String mErrorMessage = "";
	private boolean mError = false;
	HttpPost httpPost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	InputStream inputstream;
	SharedPreferences app_preferences;
	ArrayList<NameValuePair> namevaluepairs;
	private static final String TAG = "BNM";
	 public static Context context;
	 
	 @Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.layout_bnmreset);
		       WindowManager.LayoutParams params = getWindow().getAttributes();  
		       params.x = -100;  
		       params.height = 800;  
		       params.width = 700;  
		       params.y = -50;  
		  
		       this.getWindow().setAttributes(params);
		      context = getApplicationContext();
			
			
			mUsername = (EditText)findViewById(R.id.resetemail);
	        
	        
			mSubmit = (Button)findViewById(R.id.submitreset);
	        
	        
			mSubmit.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(android.view.View v) {
					if(Util.isConnected(BNMforgot.this)){
					sUsername=mUsername.getText().toString();
				    
					InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);   
	            	imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
	            	mError = false;
	            	
	            	
	            	if (TextUtils.isEmpty(mUsername.getText())) {
	            		mUsername.setError(getString(R.string.empty_email));
	            		mErrorMessage = getString(R.string.empty_email)+"\n";
	                    mError = true;
	                }
	                
	                
	                
	                if (!mError) {
	                	
	                if (Util.isConnected(BNMforgot.this)) {
	                	
	                	new reset().execute();
	                	
	                	Intent intent = new Intent(BNMforgot.this, BNMfragmentHost.class);
	    	            startActivity(intent);
	    			    
	                }
	                else {
	                	Toast.makeText(BNMforgot.this, R.string.no_internet, Toast.LENGTH_LONG).show();
	                	
	                     }
	                }
	                else{
	                	Log.w("Brand New Music", mErrorMessage);
	                    mErrorMessage = "";
	            	}
	                
	                	        	
				}
				
				else{
					Toast.makeText(BNMforgot.this, R.string.no_internet, Toast.LENGTH_LONG).show(); 
	            }
			}
	     });
	        
	 }    
public class reset extends AsyncTask<Void, Integer, Void> {
				 private ProgressDialog dialog = new ProgressDialog(BNMforgot.this);
				 
		        
				@Override
		    	protected void onPreExecute() {
					
					 
					  dialog.setIndeterminate(true); 
			          dialog.setIndeterminateDrawable(getResources().getDrawable(R.anim.progress_dialog_animation));
			          dialog.setMessage("Reseting Password....");
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
						namevaluepairs.add (new BasicNameValuePair("do","reset"));
						namevaluepairs.add (new BasicNameValuePair("email",sUsername));
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
					if(reset.this.isCancelled())
					{
						dialog.dismiss();
						Toast.makeText(BNMforgot.this, R.string.no_internet, Toast.LENGTH_LONG).show();	
					}
				}
				@Override
			    protected void onCancelled(){
			        super.onCancelled();
			        dialog.dismiss();
			        Toast.makeText(BNMforgot.this, R.string.no_internet, Toast.LENGTH_LONG).show();
				}
				
				@Override
				protected void onPostExecute(Void result) {
					 
			       
			        if(res.equals("0")){
			        	dialog.dismiss();
			        	new AlertDialog.Builder(BNMforgot.this,AlertDialog.THEME_HOLO_LIGHT)
				        .setTitle(R.string.notification_prompt_login)
				        .setMessage(Html.fromHtml("<font color=\"#000000\">Sorry the Email provided could not have its asscoiated password reset </font>" ))
				        .setNegativeButton(R.string.ok_btn,
				                new DialogInterface.OnClickListener() {
				                    @Override
				                    public void onClick(DialogInterface dialog, int which) {
				                    
				                    }
				                })
				          
				                .show();
			        	return;
			        }
			        if(res.equals("1")){
			        	String notification ="Password reset Successful.Please Check Email";
	                    Util.generateNotification(BNMforgot.this,notification);
			            finish();
			         
			        }
			        
			        else{
			        	dialog.dismiss();
			        	Log.w("Brand New Music", "Password Reset Error Responce "+res);
			        	return;
			        }
			        
				}
			}

		}

