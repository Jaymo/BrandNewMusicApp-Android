

package com.myitprovider.bnm.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.myitprovider.bnm.BNMfragmentHost;
import com.myitprovider.bnm.BNMsettings;
import com.myitprovider.bnm.R;
import com.myitprovider.bnm.adapters.ArtistProfileAdapter;
import com.myitprovider.bnm.util.BNMProgressDialog;
import com.myitprovider.bnm.util.ImageLoader;
import com.myitprovider.bnm.util.JSONfunctions;
import com.myitprovider.bnm.util.Pref;
import com.myitprovider.bnm.util.Util;

public class ArtistProfile extends SherlockFragment implements OnRefreshListener {
	
	
	public static final String KEY_ARTIST_NAME= "artist_name"; String username="";
	public static final String KEY_FOLLOW_COUNT = "artist_followers_count";String count="";
	public static final String KEY_FOLLOW_STATUS = "artist_follow_status";String status="";
	public static final String KEY_STATUS = "artist_status_update"; String update="";
	public static final String KEY_URL = "artist_image_url"; String url="",res="";
	public static final String KEY_ALBUM_NAME = "album_name";
	
	public static final String KEY_ARTIST_ALBUMS = "artist_albums"; 
	public static final String KEY_ALBUM_ID = "album_id";
	public static final String KEY_ALBUM_ARTWORK = "album_artwork_url";
	public static final String KEY_ALBUM_DATE = "album_date";
	
	StringBuilder urlBuilder;
	ArrayList<HashMap<String, String>> albumslist;
	ArtistProfileAdapter adapter;
	FragmentManager fragmentManager;
	FragmentTransaction fragmentTransaction;
	private PullToRefreshLayout mPullToRefreshLayout;
	ImageView iProfilePic,iFollowStatus;
	ListView list;String mNAME,mID;
	loadView task;
	follow following;
	TextView tArtistName,tArtistStatus,tFollowing;
	public ImageLoader imageLoader;
	
	HttpPost httpPost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	InputStream inputstream;
	SharedPreferences app_preferences;
	ArrayList<NameValuePair> namevaluepairs;
	private static final String TAG = "BNM";
	
	@Override
	 public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setHasOptionsMenu(true);
	 }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved)
	{
		View view = inflater.inflate(R.layout.layout_bnmprofile, group, false);	
		mPullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.ptr_layout);
		ActionBarPullToRefresh.from(getActivity())
        .allChildrenArePullable()
        .listener(this)
        .setup(mPullToRefreshLayout);		
		((BNMfragmentHost)getActivity()).SetABTitle("BNM Profile");
		return view;
		
	}
	
	
	@Override
	 public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		   super.onCreateOptionsMenu(menu, inflater);
	        inflater.inflate(R.menu.bnmother, menu);
	        

	   }
	public boolean onOptionsItemSelected(MenuItem item) {
	     switch (item.getItemId()) {
	      case android.R.id.home:
	        	getActivity().onBackPressed();
	    	 return true;
	        case R.id.action_setting:
	        	Intent intent = new Intent(getActivity(), BNMsettings.class);
	        	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
	        	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	            startActivity(intent);
	            return true;
	        default:
	            return false;
	     }
	}
	@Override
	public void onActivityCreated (Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);	
		if (Util.isConnected(getActivity())) {
			 task = new loadView();
			 task.execute();
			  }
			  else{
				  task=null;
	
				AlertDialog.Builder errordialog = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_LIGHT);	
		    	errordialog.setTitle(R.string.no_internet);
		    	errordialog.setMessage(R.string.no_internet_describe);
		    	errordialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface arg0, int arg1) {
				        	
				        }
		    	});
		    	errordialog.show();
			  }
		
		
	}
	
	
	@Override
    public void onRefreshStarted(View view) {
		if (Util.isConnected(getActivity())) {
			 task = new loadView();
			 task.execute();
			  }
			  else{
				  task=null;

				AlertDialog.Builder errordialog = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_LIGHT);	
		    	errordialog.setTitle(R.string.no_internet);
		    	errordialog.setMessage(R.string.no_internet_describe);
		    	errordialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface arg0, int arg1) {
				        	
				        }
		    	});
		    	errordialog.show();
			  }
		
	}
	
	public class loadView extends AsyncTask<Void, Integer, Integer> {

		@Override
    	protected void onPreExecute() {
			  
			showRefreshUI();  
	     }

	 		@Override
	 		protected Integer doInBackground(Void... arg0) {
	 			String URL =getResources().getString(R.string.URL);
	 			 Pref.loadSettings(getActivity());
		    	urlBuilder = new StringBuilder(URL);
		    	urlBuilder.append("?do=profile");
		    	urlBuilder.append("&artist_id="+Pref.artist_id);
		    	urlBuilder.append("&email="+Pref.user);
				JSONObject json = JSONfunctions.getJSONfromURL(urlBuilder.toString());
				 try{
					      albumslist = new ArrayList<HashMap<String, String>>();
				      	  JSONArray  artists = json.getJSONArray("PAYLOAD");
				      	
					        for(int i=0;i<artists.length();i++){
					        			
								JSONObject e = artists.getJSONObject(i);
								
								username = e.getString(KEY_ARTIST_NAME);
								count = e.getString(KEY_FOLLOW_COUNT);
								status = e.getString(KEY_FOLLOW_STATUS);
								update = e.getString(KEY_STATUS);
								url = e.getString(KEY_URL);

								
								JSONArray  albums = e.getJSONArray(KEY_ARTIST_ALBUMS);
								if(albums != null){
								 for(int j=0;j<albums.length();j++){
									    JSONObject f = albums.getJSONObject(j); 
									    HashMap<String, String> map = new HashMap<String, String>();
										
										map.put(KEY_ALBUM_NAME, f.getString(KEY_ALBUM_NAME));
										map.put(KEY_ALBUM_ID, f.getString(KEY_ALBUM_ID));
										map.put(KEY_ALBUM_ARTWORK, f.getString(KEY_ALBUM_ARTWORK));
										map.put(KEY_ALBUM_DATE, f.getString(KEY_ALBUM_DATE));
										albumslist.add(map);
										
								 }
								}
								
					  }		
			      }
				 catch(JSONException e)      {

			      }
		            return null;
        } 
    
	 		@Override
	    	protected void onProgressUpdate(final Integer... progress) {
	    		if ( task.isCancelled()) {
	    			
	    			hideRefreshUI();
	    		}
	    	}


	 		@Override
	        protected void onPostExecute(Integer args) {
	 	   UpdateUI();
	 	  adapter=new ArtistProfileAdapter (getActivity(), albumslist);
	      list.setAdapter(adapter);      
	      hideRefreshUI();      
	      list.setOnItemClickListener(new OnItemClickListener() {
	        	public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
	        		
	        		@SuppressWarnings("unchecked")
					HashMap<String, String> o = (HashMap<String, String>) list.getAdapter().getItem(position);
					 mNAME =o.get(KEY_ALBUM_NAME);
					 mID =o.get(KEY_ALBUM_ID);
					 Pref.loadSettings(getActivity());
					 Pref.home_albums_id = mID;
					 Pref.home_albums_name = mNAME;
					 Pref.saveSettings(getActivity());
					 
					 fragmentManager = getFragmentManager();
		    		 fragmentTransaction = fragmentManager.beginTransaction();
		    		 TracksListFragment fragment = new TracksListFragment();
		    		 fragmentTransaction.replace(android.R.id.content, fragment);
		    		 fragmentTransaction.addToBackStack(null);
		    		 fragmentTransaction.commit();
					 
	        	} 
	      });	 
    	
    }

}
	public class follow extends AsyncTask<Void, Integer, Void> {
		 private BNMProgressDialog  dialog = new BNMProgressDialog(getActivity(), R.drawable.spinner);
		 
       
		@Override
   	protected void onPreExecute() {
			
			 
			  
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
				namevaluepairs.add (new BasicNameValuePair("do","follow"));
				namevaluepairs.add (new BasicNameValuePair("email",Pref.user));
				namevaluepairs.add (new BasicNameValuePair("artist_id",Pref.artist_id));
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
			if(follow.this.isCancelled())
			{
				dialog.dismiss();
				Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_LONG).show();	
			}
		}
		@Override
	    protected void onCancelled(){
	        super.onCancelled();
	        dialog.dismiss();
	        Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_LONG).show();
		}
		
		@Override
		protected void onPostExecute(Void result) {
			dialog.dismiss();

	        if(res.equals("0")){
	        	iFollowStatus.setImageResource(R.drawable.follow);
	        	
	        }
	        if(res.equals("1")){
	        	 iFollowStatus.setImageResource(R.drawable.unfollow);
	        	 
	        }
	        
	        else{
	        	Log.w("Brand New Music","Artist Profile Responce"+res);
	        	return;
	        }
	        
		}
	}

	private void showRefreshUI() {
		if (mPullToRefreshLayout != null) {
			mPullToRefreshLayout.setRefreshing(true);
		}
	}
	
	private void hideRefreshUI() {
		if (mPullToRefreshLayout != null) {
			mPullToRefreshLayout.setRefreshComplete();
		}
	}

	private void UpdateUI() {
		 list  =(ListView)getActivity().findViewById(R.id.list);
		 tArtistName  =(TextView)getActivity().findViewById(R.id.artist_name);
		 tArtistName.setText(username);
	     tArtistStatus  =(TextView)getActivity().findViewById(R.id.artist_status);
	     tArtistStatus.setText(update);
	     tFollowing  =(TextView)getActivity().findViewById(R.id.following);
	     tFollowing.setText(count+" Following");
	     iProfilePic  =(ImageView)getActivity().findViewById(R.id.artist_profile);
	     imageLoader = new ImageLoader(getActivity());
	     imageLoader.DisplayImage(url, iProfilePic);
	     iFollowStatus  =(ImageView)getActivity().findViewById(R.id.follow_status);
	     if(status.contentEquals("1")){
	    	 iFollowStatus.setImageResource(R.drawable.unfollow);
	     }
	     else if(status.contentEquals("0")){
	    	 iFollowStatus.setImageResource(R.drawable.follow);
	     }
	     
	     
	     iFollowStatus.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(android.view.View v) {
					following =new follow();
					following.execute();
				}
	     });
	}
	
	@Override
	  public void onAttach(Activity activity) {
	    super.onAttach(activity);
	  }
	@Override
	public void onDetach() {
		super.onDetach();
		if (task != null) {
			if (task.getStatus() != AsyncTask.Status.FINISHED) {
				task.cancel(true);
			}
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (task != null) {
			if (task.getStatus() != AsyncTask.Status.FINISHED) {
				task.cancel(true);
			}
		}
		hideRefreshUI();
	}
	 
	public void onResume() {
		  super.onResume();
		  
	}
}
