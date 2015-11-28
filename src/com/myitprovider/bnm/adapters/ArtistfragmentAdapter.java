
package com.myitprovider.bnm.adapters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

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
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.myitprovider.bnm.R;
import com.myitprovider.bnm.fragments.ArtistListFragment;
import com.myitprovider.bnm.util.BNMProgressDialog;
import com.myitprovider.bnm.util.ImageLoader;
import com.myitprovider.bnm.util.Pref;

public class ArtistfragmentAdapter extends BaseAdapter implements Filterable
{
    private Activity activity;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader;
    public ArrayList<HashMap<String, String>> originalData;
    public ArrayList<HashMap<String, String>> filteredData;
    String status;
    ImageView follow_image;
    HttpPost httpPost;
	JSONObject json;
	String res;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	InputStream inputstream;
	SharedPreferences app_preferences;
	ArrayList<NameValuePair> namevaluepairs;
	private static final String TAG = "BNM";
	
    public ArtistfragmentAdapter(Activity a, ArrayList<HashMap<String, String>> d) 
    {
        activity = a;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity.getApplicationContext());

        originalData = d;
        filteredData = d;
    }

    public int getCount() 
    {
        return filteredData.size();
    }

    public Object getItem(int position) 
    {
        return filteredData.get(position);
    }

    public long getItemId(int position) 
    {
        return position;
    }
 
    public static class ViewHolder {
    	   TextView name;
    	   ImageView thumb;
    	   ImageView follow;
    	}
    
    public View getView(int position, View convertView, ViewGroup parent) 
    {
       final ViewHolder holder;
       View vi = convertView;
       if(convertView==null){
           vi = inflater.inflate(R.layout.layout_bnmartistlistrow, null);

       holder = new ViewHolder(); 
       holder.name = (TextView)vi.findViewById(R.id.artistname); 
       holder.thumb=(ImageView)vi.findViewById(R.id.artist_image);  
       holder.follow=(ImageView)vi.findViewById(R.id.follow);
       vi.setTag(holder);
       }
       else {
           holder = (ViewHolder) vi.getTag();
       }
       
       HashMap<String, String> artist = new HashMap<String, String>();
       artist = filteredData.get(position);
       
       final String ArtistID=  artist.get(ArtistListFragment.KEY_ARTIST_ID);
       if(artist.get(ArtistListFragment.KEY_FOLLOW_STATUS).contentEquals("1")){
    	   holder.follow.setImageResource(R.drawable.unfollow);
         }
         else{
        	 holder.follow.setImageResource(R.drawable.follow);
         }
      holder.name.setText( artist.get(ArtistListFragment.KEY_ARTIST_NAME));
      
      imageLoader.DisplayImage( artist.get(ArtistListFragment.KEY_IMAGE_URL), holder.thumb);
    
      holder.follow.setOnClickListener(new OnClickListener() 
      { 
          @Override
          public void onClick(View v) 
          {
              new follow(holder).execute(Pref.user,ArtistID);
          }

      });
      

      return vi;

    }
    public class follow extends AsyncTask<String, Integer, Void> {
		 BNMProgressDialog dialog = new BNMProgressDialog(activity, R.drawable.spinner);
		 private ViewHolder mV;
		 public follow (ViewHolder holder){
			 mV =holder;
		 }
		@Override
  	protected void onPreExecute() {
			 
			
			  dialog.show();

	         
  		         }
		@Override
		protected Void doInBackground(String... param) {
			String URL =activity.getResources().getString(R.string.URL);
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
				namevaluepairs.add (new BasicNameValuePair("email",param[0]));
				namevaluepairs.add (new BasicNameValuePair("artist_id",param[1]));
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
				Toast.makeText(activity, R.string.no_internet, Toast.LENGTH_LONG).show();	
			}
		}
		@Override
	    protected void onCancelled(){
	        super.onCancelled();
	        dialog.dismiss();
	        Toast.makeText(activity, R.string.no_internet, Toast.LENGTH_LONG).show();
		}
		
		@Override
		protected void onPostExecute(Void result) {
			dialog.dismiss();
			
			if(res.equals("0")){
				mV.follow.setImageDrawable(activity.getResources().getDrawable(R.drawable.follow));
	        	
	        }
			else if(res.equals("1")){
				mV.follow.setImageDrawable(activity.getResources().getDrawable(R.drawable.unfollow));	

	        }

	        
		}
	}
    @Override
	public Filter getFilter() {
    	
       return new Filter()
       {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                FilterResults results = new FilterResults();

                if(charSequence == null || charSequence.length() == 0 || charSequence=="")
                {
                    results.values = originalData;
                    results.count = originalData.size();
                }
                else
                {
                    ArrayList<HashMap<String,String>> filterResultsData = new ArrayList<HashMap<String,String>>();
                    
                    int index = 0;
                    for(HashMap<String,String> data : originalData)
                    {
                    	 HashMap<String,String> searchdata = originalData.get(index);
                    	 String filterableString = searchdata.get(ArtistListFragment.KEY_ARTIST_NAME);
                      	 if(filterableString.toLowerCase(Locale.getDefault()).startsWith(charSequence.toString().trim().toLowerCase(Locale.getDefault()))){
                      
                           filterResultsData.add(data);
                  }           

                    results.values = filterResultsData;
                    results.count = filterResultsData.size();
                    
                    index++;
                }
                    
              }

                return results;
            }
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
            {
                filteredData = (ArrayList<HashMap<String,String>>)filterResults.values;
                if (filterResults.count > 0) {
                    notifyDataSetChanged();
                   } else {
                       notifyDataSetInvalidated();
                   }  
            }
        };
    }
}
