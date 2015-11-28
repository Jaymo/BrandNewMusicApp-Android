
package com.myitprovider.bnm.fragments;

import java.util.ArrayList;
import java.util.HashMap;

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

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.myitprovider.bnm.BNMfragmentHost;
import com.myitprovider.bnm.BNMsettings;
import com.myitprovider.bnm.R;
import com.myitprovider.bnm.adapters.AtristTracksfragmentAdapter;
import com.myitprovider.bnm.util.JSONfunctions;
import com.myitprovider.bnm.util.Pref;
import com.myitprovider.bnm.util.Util;

public class TracksListFragment extends SherlockListFragment  implements OnRefreshListener {
	
	public static final String KEY_ARTIST_NAME ="artist_name";
	public static final String KEY_ALBUM_URL ="album_artwork_url";
	public static final String KEY_ALBUM_URL_LARGE ="album_artwork_url_large";
	public static final String KEY_TRACK_NAME = "track_name";  
	public static final String KEY_TRACK_URL = "track_url"; 
	public static final String KEY_GENRE = "track_genre";
	public static final String KEY_LINK = "download_link";
	public static final String KEY_TRACK_ID = "track_id";
	
	private PullToRefreshLayout mPullToRefreshLayout;
	String tname,aname,turl,durl,aurl;
	ArrayList<HashMap<String, String>> displaylist;
	ListView list;
	int status =0;
	loadListView task;
	StringBuilder urlBuilder;
	AtristTracksfragmentAdapter adapter;

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
        
        ((BNMfragmentHost)getActivity()).SetABTitle("BNM Album Tracks");
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
	        	urlBuilder.append("?do=tracks");
	        	urlBuilder.append("&album_id="+Pref.home_albums_id);
	        	
	              displaylist = new ArrayList<HashMap<String, String>>();
				  JSONObject json = JSONfunctions.getJSONfromURL(urlBuilder.toString());
				  try{
			      	JSONArray  mot = json.getJSONArray("PAYLOAD");
			      	 status =0;
			      	 if (!mot.isNull(0)){
				        for(int i=0;i<mot.length();i++){						
				        	HashMap<String, String> map = new HashMap<String, String>();		
							JSONObject e = mot.getJSONObject(i);
							map.put(KEY_ARTIST_NAME, e.getString(KEY_ARTIST_NAME));
							map.put(KEY_ALBUM_URL, e.getString(KEY_ALBUM_URL));
							map.put(KEY_ALBUM_URL_LARGE, e.getString(KEY_ALBUM_URL_LARGE));
							map.put(KEY_TRACK_NAME , e.getString(KEY_TRACK_NAME ));
							map.put(KEY_TRACK_URL, e.getString(KEY_TRACK_URL ));
							map.put(KEY_TRACK_ID , e.getString(KEY_TRACK_ID));
							map.put(KEY_GENRE, e.getString(KEY_GENRE ));
							map.put(KEY_LINK , e.getString(KEY_LINK));
						    displaylist.add(map);
						    
				        }
						status =1;
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
 			         hideRefreshUI();
	 	             if(status ==1){
	        		  list = getListView();
	        		  list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
				      adapter=new AtristTracksfragmentAdapter (getActivity(), displaylist);
				      list.setAdapter(adapter);
				      setListShown(true);
				      hideRefreshUI();
				      list.setOnItemClickListener(new OnItemClickListener() {
				        	public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				        		 Bundle bundle = new Bundle();
								 bundle.putSerializable("hash",displaylist );
								 bundle.putInt("position", position);
				        		 fragmentManager = getFragmentManager();
					    		 fragmentTransaction = fragmentManager.beginTransaction();
					    		 MainPlayerfragment fragment = new MainPlayerfragment();
					    		 fragment.setArguments(bundle);
					    		 fragmentTransaction.replace(android.R.id.content, fragment);
					    		 fragmentTransaction.addToBackStack(null);
					             fragmentTransaction.commit();
				        		
					
				}
			});

 		
	}

	
	else{
		AlertDialog.Builder errordialog = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_LIGHT);	
	errordialog.setTitle(R.string.no_tracks);
	errordialog.setMessage(R.string.no_tracks_describe);
	errordialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface arg0, int arg1) {
	        	getActivity().onBackPressed();
	        }
	});
	errordialog.show();
		
	}
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