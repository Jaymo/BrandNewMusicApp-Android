
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
import android.app.ProgressDialog;
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
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.devspark.collapsiblesearchmenu.CollapsibleMenuUtils;
import com.myitprovider.bnm.BNMfragmentHost;
import com.myitprovider.bnm.BNMsettings;
import com.myitprovider.bnm.R;
import com.myitprovider.bnm.adapters.ArtistfragmentAdapter;
import com.myitprovider.bnm.util.JSONfunctions;
import com.myitprovider.bnm.util.Pref;
import com.myitprovider.bnm.util.Util;

public class ArtistListFragment extends SherlockListFragment  implements OnRefreshListener {
	
	public static final String KEY_ARTIST_ID = "artist_id"; 
	public static final String KEY_ARTIST_NAME = "artist_name";
	public static final String KEY_IMAGE_URL = "artist_image_url";
	public static final String KEY_FOLLOW_STATUS = "artist_follow_status";
	String mArtistId,res="";;
	private PullToRefreshLayout mPullToRefreshLayout;
	ArrayList<HashMap<String, String>> currentartistlist;
	loadListView task;
	ActionBar bar;
	StringBuilder urlBuilder;
	ArrayList<HashMap<String, String>> maplist,mapslist;
	HttpPost httpPost;
	JSONObject json;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	InputStream inputstream;
	SharedPreferences app_preferences;
	ArrayList<NameValuePair> namevaluepairs;
	ProgressDialog progressDialog;
	ListView list;String mFollow,sFollow;
	ArtistfragmentAdapter artistadapter;
	FragmentManager fragmentManager;
	private static final String TAG = "BNM";
	FragmentTransaction fragmentTransaction;
	@SuppressWarnings("unused")
	private MenuItem mSearchMenuItem;
	@Override
	 public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setHasOptionsMenu(true);
	 }
	@Override
    public void onRefreshStarted(View view) {
		if (Util.isConnected(getActivity())) {
			 setListShown(false);
			 task = new loadListView();
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

    private CollapsibleMenuUtils.OnQueryTextListener mOnQueryTextListener = new CollapsibleMenuUtils.OnQueryTextListener() {

        @Override
        public void onQueryTextSubmit(String query) {
        	artistadapter.getFilter().filter(query);
        }

        @Override
        public void onQueryTextChange(String query) {
        	
        	artistadapter.getFilter().filter(query); 
        }
    };
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	 public void onActivityCreated(Bundle savedInstanceState) {
	  super.onActivityCreated(savedInstanceState);
	  if (Util.isConnected(getActivity())) {
			 setListShown(false);
			 task = new loadListView();
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
	  setListShownNoAnimation(true);
	   
	 }

	 
	
	 public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		   super.onCreateOptionsMenu(menu, inflater);
	        mSearchMenuItem = CollapsibleMenuUtils.addSearchMenuItem(menu, true, mOnQueryTextListener);
	        
	        inflater.inflate(R.menu.bnmother, menu);
	        
	    }
	 public boolean onOptionsItemSelected(MenuItem item) {
	     switch (item.getItemId()) {
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        ViewGroup viewGroup = (ViewGroup) view;
        mPullToRefreshLayout = new PullToRefreshLayout(viewGroup.getContext());

        ActionBarPullToRefresh.from(getActivity())
                .insertLayoutInto(viewGroup)
                .theseChildrenArePullable(android.R.id.list, android.R.id.empty)
                .listener(this)
                .setup(mPullToRefreshLayout);
        
        ((BNMfragmentHost)getActivity()).SetABTitle("Brand New Music");
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

	 public class loadListView extends AsyncTask<Void, Integer, Integer> {

		 
 		@Override
     	protected void onPreExecute() {
 			showRefreshUI();
             }

 		@Override
 		protected Integer doInBackground(Void... arg0) {
 			String URL =getResources().getString(R.string.URL);
 			 Pref.loadSettings(getActivity());
        	urlBuilder = new StringBuilder(URL);
        	urlBuilder.append("?do=artists");
        	urlBuilder.append("&email="+Pref.user);
			JSONObject json = JSONfunctions.getJSONfromURL(urlBuilder.toString());
	         currentartistlist = new ArrayList<HashMap<String, String>>();
				  try{
					  
			      	JSONArray  artists = json.getJSONArray("PAYLOAD");
				        for(int i=0;i<artists.length();i++){						
				        	HashMap<String, String> map = new HashMap<String, String>();		
							JSONObject e = artists.getJSONObject(i);
							map.put(KEY_ARTIST_NAME, e.getString(KEY_ARTIST_NAME));
							map.put(KEY_ARTIST_ID, e.getString(KEY_ARTIST_ID));
							map.put(KEY_IMAGE_URL, e.getString(KEY_IMAGE_URL));
							map.put(KEY_FOLLOW_STATUS, e.getString(KEY_FOLLOW_STATUS));
							currentartistlist.add(map);
						}		
			      }catch(JSONException e)      {

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
	        	  list = getListView();
 		          list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
 		          artistadapter=new ArtistfragmentAdapter (getActivity(), currentartistlist);
			      list.setAdapter(artistadapter);
			      setListShown(true);
			      hideRefreshUI();

			      list.setOnItemClickListener(new OnItemClickListener() {
			        	public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
			        		@SuppressWarnings("unchecked")
							HashMap<String, String> o = (HashMap<String, String>) list.getAdapter().getItem(position);
							 mArtistId =o.get(KEY_ARTIST_ID);
			        		 Pref.loadSettings(getActivity());
							 Pref.artist_id = mArtistId;
							 Pref.saveSettings(getActivity());
							 
							 fragmentManager = getFragmentManager();
				    		 fragmentTransaction = fragmentManager.beginTransaction();
				    		 ArtistProfile fragment = new ArtistProfile();
				    		 fragmentTransaction.replace(android.R.id.content, fragment);
				    		 fragmentTransaction.addToBackStack(null);
				    		 fragmentTransaction.commit();
			        		
				}
			});

		}
	}
		public class follow extends AsyncTask<Void, Integer, Void> {
			 private ProgressDialog dialog = new ProgressDialog(getActivity());
			 
	       
			@Override
	   	protected void onPreExecute() {
				
				 
				  dialog.setIndeterminate(true); 
		          dialog.setIndeterminateDrawable(getResources().getDrawable(R.anim.progress_dialog_animation));
		          dialog.setMessage("Processing Request....");
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
					namevaluepairs = new ArrayList<NameValuePair>(3);
					namevaluepairs.add (new BasicNameValuePair("do","follow"));
					namevaluepairs.add (new BasicNameValuePair("email",Pref.user));
					namevaluepairs.add (new BasicNameValuePair("artist_id",Pref.artist_id));
					httpPost.setEntity (new UrlEncodedFormEntity(namevaluepairs));
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
		        
			}
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
			if(task!= null){
			if(task.getStatus()!=AsyncTask.Status.FINISHED){
			task.cancel(true);
			}
			}
			hideRefreshUI();
		}
	 
	 public void onResume() {
		  super.onResume();
		  if(getActivity().getActionBar().getNavigationMode()==ActionBar.NAVIGATION_MODE_STANDARD){
		        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		    }
	}
}