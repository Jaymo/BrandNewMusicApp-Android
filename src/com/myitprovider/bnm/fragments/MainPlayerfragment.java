
package com.myitprovider.bnm.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.myitprovider.bnm.R;
import com.myitprovider.bnm.util.BNMProgressDialog;
import com.myitprovider.bnm.util.Pref;
import com.myitprovider.bnm.util.Util;
import com.squareup.picasso.Picasso;


public class MainPlayerfragment  extends SherlockFragment implements OnCompletionListener,OnPreparedListener, OnBufferingUpdateListener{
	TextView tSongTitle,songTotalDurationLabel,songCurrentDurationLabel;
	ImageView IAlbumArt;
	ImageView play,pause,next,previous;
	ProgressBar progress = null;
	FragmentManager fragmentManager;
	FragmentTransaction fragmentTransaction;
	int value=0,length;
	public MediaPlayer mp;
	Boolean bound= false,reset=false;
	long totalDuration,currentDuration;
	private Handler mHandler = new Handler();
	ArrayList<HashMap<String, String>> Arraylist;
	HashMap<String, String> hash=null;
	BNMProgressDialog loader;
	InputStream inputstream;
	ArrayList<NameValuePair> namevaluepairs;
	String res;
	
	@Override
	 public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setHasOptionsMenu(true);
	  mp = new MediaPlayer();
	  loader = new BNMProgressDialog(getActivity(), R.drawable.spinner);
	 }
	
	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_bnmmusicplayer, container, false);
		Bundle bundles = getArguments();
		if (bundles != null) {
			Arraylist = (ArrayList<HashMap<String, String>>) bundles.getSerializable("hash");			
			value = bundles.getInt("position");
		}
		return view;
	}
	

	@Override
	 public void onActivityCreated(Bundle savedInstanceState) {
	  super.onActivityCreated(savedInstanceState);
	  
	 
	  
	  if(getActivity().getActionBar().getNavigationMode()==ActionBar.NAVIGATION_MODE_TABS){
	        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	    }
	  songTotalDurationLabel   = (TextView)getActivity().findViewById(R.id.songtotalduration);
      songCurrentDurationLabel = (TextView)getActivity().findViewById(R.id.songduration);
	  tSongTitle =(TextView)getActivity().findViewById(R.id.songTitle);
	  tSongTitle.setSelected(true);
	  IAlbumArt =(ImageView)getActivity().findViewById(R.id.album_art);
	  play =(ImageView)getActivity().findViewById(R.id.play);
	  next =(ImageView)getActivity().findViewById(R.id.next);
	  previous =(ImageView)getActivity().findViewById(R.id.back);
	  progress =(ProgressBar)getActivity().findViewById(R.id.songProgressBar);  
	  tSongTitle.setText("");
	  songTotalDurationLabel.setText("0.00");
	  songCurrentDurationLabel.setText("0.00");
	  playSong(value);
	  
	  play.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
	
				if(mp.isPlaying()){
					if(mp!=null){
						mp.pause();
						
						play.setImageResource(R.drawable.play);
					}
				}else{
					
					if(mp!=null){
						mp.start();
						
						play.setImageResource(R.drawable.pause);
					}
				}
				
			}
		});
	  
	  next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if(value < (Arraylist.size() - 1)){
					playSong(value + 1);
					value = value + 1;
				}else{
					
					playSong(0);
					value = 0;
				}
				
			}
			
		});
		
		
		previous.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				if(value > 0){
					playSong(value - 1);
					value = value - 1;
				}else{
					
					playSong(Arraylist.size() - 1);
					value = Arraylist.size() - 1;
				}
			}
		});
		
	  
	}
	
	
	
	public void  playSong(int songIndex){
		
		if(Util.isConnected(getActivity())){
		try {
			loader.show();
			hash = new HashMap<String, String>();
	        hash = Arraylist.get(songIndex);
        	mp.reset();
        	mp.setDataSource(hash.get("track_url"));
        	mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
		    mp.setOnBufferingUpdateListener(this);
            mp.setOnPreparedListener(this);
            mp.setOnCompletionListener(this);
            mp.prepareAsync();
			
			Picasso.with(getActivity())
		    .load(hash.get("album_artwork_url_large"))
		    .resize(300, 300)
		    .placeholder(R.drawable.stud_track)
		    .error(R.drawable.stud_track)
		    .into(IAlbumArt);
			
			
			
			updateProgressBar();			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
		else{
			AlertDialog.Builder errordialog = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_LIGHT);	
	    	errordialog.setTitle(R.string.no_internet);
	    	errordialog.setMessage(R.string.no_internet_describe);
	    	errordialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface arg0, int arg1) {
			        	getActivity().onBackPressed();
			        }
	    	});
	    	errordialog.show();
		}
	}
	
	
	public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);        
    }	
	
	
	private Runnable mUpdateTimeTask = new Runnable() {
		   public void run() {
			 
			   currentDuration = mp.getCurrentPosition();
			  
			   songTotalDurationLabel.setText(""+Util.milliSecondsToTimer(totalDuration));
			   
			   songCurrentDurationLabel.setText(""+Util.milliSecondsToTimer(currentDuration));
			   
			   int Songprogress = (int)(Util.getProgressPercentage(currentDuration, totalDuration));
			  
			   progress.setProgress(Songprogress);
			   
			   
		       mHandler.postDelayed(this, 100);
			 
			  
		   }
		};

	
	
	 public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		   super.onCreateOptionsMenu(menu, inflater);
	      
	        inflater.inflate(R.menu.bnmplayer, menu);
	        
	    }
	 public boolean onOptionsItemSelected(MenuItem item) {
	     switch (item.getItemId()) {
	        case R.id.tracklist:
	        	
	        	getActivity().onBackPressed();
	            return true;
	        case android.R.id.home:
	        	
	        	getActivity().onBackPressed();	        	
	            return true; 
	        default:
	            return false;
	     }
	}
	 
	 @Override
	    public void onViewCreated(View view, Bundle savedInstanceState) {
	        super.onViewCreated(view,savedInstanceState);
	        
	 }
	 
	 @Override
	  public void onAttach(Activity activity) {
	    super.onAttach(activity);
	  }
	 @Override
	 public void onDetach() {
	     super.onDetach();
	     
	 }

	 @Override
		public void onCompletion(MediaPlayer mp) {
		 AlertDialog.Builder errordialog = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_LIGHT);	
	    	errordialog.setTitle(R.string.track_sampled);
	    	errordialog.setMessage(R.string.track_played_message);
	    	errordialog.setNegativeButton("Next", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface arg0, int arg1) {
			        	if(value < (Arraylist.size() - 1)){
							playSong(value + 1);
							value = value + 1;
						}else{
							
							playSong(0);
							value = 0;
						}
			        }
	    	});
	    	errordialog.setPositiveButton("Download", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface arg0, int arg1) {
		        	String url =hash.get("download_link").trim();
		        	if (!url.startsWith("http://") && !url.startsWith("https://")){
		        		Intent i = new Intent(Intent.ACTION_VIEW);
		        		i.setData(Uri.parse(url));
		        		startActivity(i);
		        	}
		        	else{
		        		Intent i = new Intent(Intent.ACTION_VIEW);
		        		i.setData(Uri.parse("https://play.google.com/music/"));
		        		startActivity(i);
		        	}
		        }
	    	});
	    errordialog.show();
	    
		 	
			
			
		}
		
	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		
		
	}

	
	@Override
	public void onPrepared(final MediaPlayer mp) {
		String songTitle = hash.get("artist_name") + " -- " + hash.get("track_name");
		tSongTitle.setText(songTitle);
		String track_id = hash.get("track_id");
		new topten(){
			@Override
			protected void onPostExecute(final Void result) {
				loader.dismiss();
				
				if(res.equals("0")){
					reset=false;
					totalDuration = mp.getDuration();
					if(!mp.isPlaying()){
					mp.start();
					play.setImageResource(R.drawable.pause);
					progress.setProgress(0);
					progress.setMax(100);
					
				}
			 }
				else if(res.equals("1")) {
					reset=true;
					mp.stop();
					AlertDialog.Builder errordialog = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_LIGHT);	
			    	errordialog.setTitle(R.string.track_sampled);
			    	errordialog.setMessage(R.string.track_sampled_message);
			    	errordialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface arg0, int arg1) {
					        	getActivity().onBackPressed();
					        }
			    	});
			    	errordialog.setPositiveButton("Download", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface arg0, int arg1) {
				        	String url =hash.get("download_link").trim();
				        	if (!url.startsWith("http://") && !url.startsWith("https://")){
				        		Intent i = new Intent(Intent.ACTION_VIEW);
				        		i.setData(Uri.parse(url));
				        		startActivity(i);
				        	}
				        	else{
				        		Intent i = new Intent(Intent.ACTION_VIEW);
				        		i.setData(Uri.parse("https://play.google.com/music/"));
				        		startActivity(i);
				        	}
				        }
		    	});
			    errordialog.show();
				}
				else{
					
				}
			}
		}.execute(track_id,Pref.user);
		
		
	}
	
	 @Override
		public void onPause() {
	    	super.onPause();
	    	if(reset){
	    		
	    	}
	    	else{
	    		mp.pause();
	    	}
	    	 
	    	 
	    	
	 }
	 @Override
		public void onResume() {
	    	super.onResume();
	    	 
	    	 
	    	
	 }
	 
	 @Override
	    public void onSaveInstanceState(Bundle toSave) { 
	        super.onSaveInstanceState(toSave);
	        
	    }
	 @Override
	 public void onDestroy() {
	  super.onDestroy();
	 }
	 
	 public class topten extends AsyncTask<String, Integer, Void> {
		   @Override
			protected Void doInBackground(String ... params) {
			   String URL =getResources().getString(R.string.URL);
				try{
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(URL);
				namevaluepairs = new ArrayList<NameValuePair>();
				namevaluepairs.add (new BasicNameValuePair("do","topten"));
				namevaluepairs.add (new BasicNameValuePair("track_id",params[0]));
				namevaluepairs.add (new BasicNameValuePair("email",params[1]));
				httpPost.setEntity (new UrlEncodedFormEntity(namevaluepairs));
				HttpResponse response = httpclient.execute(httpPost);
				HttpEntity entity = response.getEntity();
				inputstream=entity.getContent();
			       }
		        catch(Exception e){
		            Log.e("BNM MediaPlayer", "Error kwa http connection "+e.toString());
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
	            Log.e("BNM MediaPlayer", "Error converting result "+e.toString());
	    }
				
				return null;
		}

	}
}
