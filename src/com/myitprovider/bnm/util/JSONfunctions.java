package com.myitprovider.bnm.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONfunctions {

	public static JSONObject getJSONfromURL(String url){
		InputStream is = null;
		String result = "";
		JSONObject jArray = null;
		
		
		
		
	    try{
	            HttpClient httpclient = new DefaultHttpClient();
	            HttpPost httppost = new HttpPost(url);
	            HttpParams params = new BasicHttpParams();
			    HttpConnectionParams.setConnectionTimeout(params, 30000);
			    HttpConnectionParams.setSoTimeout(params, 30000); 
	            HttpResponse response = httpclient.execute(httppost);
	            HttpEntity entity = response.getEntity();
	            is = entity.getContent();
	            
	            

	    }
	    catch (ConnectTimeoutException e) {
			Log.e("BNM", "Error in ConnectTimeoutException "+e.toString());
			return null;
		} catch (SocketTimeoutException ste) {
			Log.e("BNM", "Error in SocketTimeoutException "+ste.toString());
			return null;
		} catch (NullPointerException nall) {
			Log.e("BNM", "Error in NullPointerException "+nall.toString());
			return null;
		} catch (MalformedURLException e) {
			Log.e("BNM", "Error in MalformedURLException "+e.toString());
			return null;
		} catch (IOException e) {
			Log.e("BNM", "Error in IOException "+e.toString());
			return null;
		} catch (Exception e) {
			Log.e("BNM", "Error in Intenert connection Exception "+e.toString());
			return null;
		}
	    
	  
	    try{
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                    sb.append(line + "\n");
	            }
	            is.close();
	            result=sb.toString();
	    }
	    catch(Exception e){
	            Log.e("JSONfunctions BNM", "Error converting result "+e.toString());
	    }
	    
	    try{
	    	
            jArray = new JSONObject(result);            
	    }catch(JSONException e){
	            Log.e("JSONfunctions BNM", "Error parsing data "+e.toString());
	    }
    
	    return jArray;
	}
}

