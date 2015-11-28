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
import com.myitprovider.bnm.adapters.GenreArtistfragmentAdapter;
import com.myitprovider.bnm.util.JSONfunctions;
import com.myitprovider.bnm.util.Pref;
import com.myitprovider.bnm.util.Util;

public class AlbumsGenreListFragment extends  SherlockListFragment  implements OnRefreshListener {

	public static final String KEY_ALBUM_NAME = "album_name"; 
	public static final String KEY_ALBUM_ID = "album_id";
	public static final String KEY_ALBUM_ARTWORK = "album_artwork_url";
	private PullToRefreshLayout mPullToRefreshLayout;
	String  mNAME,mID;
	ArrayList<HashMap<String, String>> displaylist;
	ListView list;
	StringBuilder urlBuilder;
	GenreArtistfragmentAdapter adapter;
	loadListView task;
	FragmentManager fragmentManager;
	FragmentTransaction fragmentTransaction;
	int status =0;
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
        
        ((BNMfragmentHost)getActivity()).SetABTitle("BNM Genre Albums"); 
    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		     setHasOptionsMenu(true);
	        
	        
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
	        	Pref.loadSettings(getActivity());
	        	String URL =getResources().getString(R.string.URL);
	        	urlBuilder = new StringBuilder(URL);
	        	urlBuilder.append("?do=album-genres");
	        	urlBuilder.append("&genre_id="+Pref.genre_id);
	              displaylist = new ArrayList<HashMap<String, String>>();
				  JSONObject json = JSONfunctions.getJSONfromURL(urlBuilder.toString());

				  try{
			      	
			      	JSONArray  mot = json.getJSONArray("PAYLOAD");
			      	 status =0;
			      	 if (!mot.isNull(0)){
				        for(int i=0;i<mot.length();i++){						
				        	HashMap<String, String> map = new HashMap<String, String>();		
							JSONObject e = mot.getJSONObject(i);
							map.put(KEY_ALBUM_NAME, e.getString(KEY_ALBUM_NAME));
							map.put(KEY_ALBUM_ID , e.getString(KEY_ALBUM_ID ));
							map.put(KEY_ALBUM_ARTWORK, e.getString(KEY_ALBUM_ARTWORK)); 
						    displaylist.add(map);
						    
				        }
						status =1;
					}		
		         }catch(JSONException e)    {

			      }
	                return null;
	            } 
	        
		@Override
		protected void onProgressUpdate(final Integer... progress) {
			if (task.isCancelled()) {

				hideRefreshUI();
			}
		}

		@Override
		protected void onPostExecute(Integer args) {
			         hideRefreshUI();
		 	        if(status ==1){
	        		  list = getListView();
	        		  list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
				      adapter=new GenreArtistfragmentAdapter (getActivity(), displaylist);
				      list.setAdapter(adapter);
				      setListShown(true);
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

			
			else{
				AlertDialog.Builder errordialog = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_LIGHT);	
	    	errordialog.setTitle(R.string.no_albums);
	    	errordialog.setMessage(R.string.no_albums_describe);
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
