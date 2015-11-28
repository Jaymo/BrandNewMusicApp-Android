
package com.myitprovider.bnm.fragments;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.devspark.collapsiblesearchmenu.CollapsibleMenuUtils;
import com.myitprovider.bnm.BNMfragmentHost;
import com.myitprovider.bnm.BNMsettings;
import com.myitprovider.bnm.R;
import com.myitprovider.bnm.adapters.HomeListFragmentAdapter;
import com.myitprovider.bnm.util.JSONfunctions;
import com.myitprovider.bnm.util.Pref;
import com.myitprovider.bnm.util.Util;

public class HomeListFragment extends SherlockListFragment  implements OnRefreshListener {
	
	public static final String KEY_ARTIST_ID = "artist_id";
	public static final String KEY_ARTIST_NAME= "artist_name";
	public static final String KEY_COUNT = "artist_track_count"; 
	public static final String KEY_IMAGE_URL = "artist_image_url";
	public static final String KEY_STATUS_UPDATE = "artist_status";
	
	public static final String KEY_LATEST_TRACK = "artist_latest_track";
	
	public static final String KEY_TRACK_ID = "track_id";
	public static final String KEY_TRACK_NAME = "track_name";
	public static final String KEY_TRACK_URL = "track_url";
	
	public static final String KEY_INDEX = "index";
	
	private PullToRefreshLayout mPullToRefreshLayout;
	String  mNAME,mID;
	HttpPost httpPost;
	JSONObject json;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	InputStream inputstream;
	SharedPreferences app_preferences;
	ArrayList<NameValuePair> namevaluepairs;
	ArrayList<HashMap<String, String>> homelist;
	ProgressDialog progressDialog;
	loadListView task;
	StringBuilder urlBuilder;
	ListView list;
	HashMap<String, String> map ;
	HomeListFragmentAdapter adapter;
	@SuppressWarnings("unused")
	private MenuItem mSearchMenuItem;
	FragmentManager fragmentManager;
	FragmentTransaction fragmentTransaction;
	
	
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
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		     setHasOptionsMenu(true);
	        
	        return super.onCreateView(inflater, container, savedInstanceState);
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
	
	@Override
	 public void onActivityCreated(Bundle savedInstanceState) {
	  super.onActivityCreated(savedInstanceState);
	  if (Util.isConnected(getActivity())) {
			 setListShown(false);
			 task = new loadListView();
			 task.execute();
			 setListShownNoAnimation(true);
			  }
			  else{
				  task=null;
				  //prompt about Data Loss
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
	        	adapter.getFilter().filter(query);
	        }

	        @Override
	        public void onQueryTextChange(String query) {
	        	
	        	adapter.getFilter().filter(query); 
	        }
	    };	 
	 
	 
	 	 @Override
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
	        	urlBuilder.append("?do=following");
	        	urlBuilder.append("&email="+Pref.user);
				  JSONObject json = JSONfunctions.getJSONfromURL(urlBuilder.toString());
				  try{
					 homelist = new ArrayList<HashMap<String, String>>();
			      	JSONArray  mot = json.getJSONArray("PAYLOAD");
			      	
			      	 int index = 0; 
				        for(int i=0;i<mot.length();i++){						
				        	map = new HashMap<String, String>();		
							JSONObject e = mot.getJSONObject(i);
							map.put(KEY_ARTIST_ID, e.getString(KEY_ARTIST_ID));
							map.put(KEY_ARTIST_NAME, e.getString(KEY_ARTIST_NAME));		
							map.put(KEY_COUNT, e.getString(KEY_COUNT));
							map.put(KEY_IMAGE_URL, e.getString(KEY_IMAGE_URL));
							map.put(KEY_STATUS_UPDATE, e.getString(KEY_STATUS_UPDATE));
							map.put(KEY_INDEX, index+"");
							
							
							JSONArray  TRACK = e.getJSONArray(KEY_LATEST_TRACK);
							if(TRACK != null){
								
								for(int j=0;j<TRACK.length();j++){
									JSONObject f = TRACK.getJSONObject(j);
									map.put(KEY_TRACK_ID, f.getString(KEY_TRACK_ID));
									map.put(KEY_TRACK_NAME, f.getString(KEY_TRACK_NAME));
									map.put(KEY_TRACK_URL, f.getString(KEY_TRACK_URL));
								}
							}
							
						    homelist.add(map);
						    index++;
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
			      adapter=new HomeListFragmentAdapter (getActivity(), homelist);
			      list.setAdapter(adapter);
			      setListShown(true);
			      hideRefreshUI();
			      list.setOnItemClickListener(new OnItemClickListener() {
			        	public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
			        		
			        		@SuppressWarnings("unchecked")
							HashMap<String, String> o = (HashMap<String, String>) list.getAdapter().getItem(position);
			        
							 mNAME =o.get(KEY_ARTIST_NAME);
							 mID =o.get(KEY_ARTIST_ID);
				
							 Pref.loadSettings(getActivity());
							 Pref.artist_id = mID;
							 Pref.home_artist_name = mNAME;
							 Pref.saveSettings(getActivity());
							 fragmentManager = getFragmentManager();
				    		 fragmentTransaction = fragmentManager.beginTransaction();
				    		 AlbumsListFragment fragment = new AlbumsListFragment();
				    		 fragmentTransaction.replace(android.R.id.content, fragment);
				    		 fragmentTransaction.addToBackStack(null);
				    		 fragmentTransaction.commit();
			        		
				    		 
			        	} 
			      });
							 
	        	
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
