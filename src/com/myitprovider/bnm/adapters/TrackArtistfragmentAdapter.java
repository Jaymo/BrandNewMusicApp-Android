
package com.myitprovider.bnm.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.myitprovider.bnm.R;
import com.myitprovider.bnm.fragments.HomeListFragment;
import com.myitprovider.bnm.fragments.TracksListFragment;
import com.myitprovider.bnm.util.ImageLoader;

public class TrackArtistfragmentAdapter extends BaseAdapter implements Filterable
{
    private Activity activity;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader;
    public ArrayList<HashMap<String, String>> originalData;
    public ArrayList<HashMap<String, String>> filteredData;
    String status;
    public TrackArtistfragmentAdapter(Activity a, ArrayList<HashMap<String, String>> d) 
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
 	   TextView track;
 	   ImageView thumb;
 	}
    public View getView(int position, View convertView, ViewGroup parent) 
    {
        ViewHolder holder;
       View vi = convertView;
       if(convertView==null){
    	   vi = inflater.inflate(R.layout.layout_bnmartisttracks, null);

    	   holder = new ViewHolder();
           holder.name = (TextView)vi.findViewById(R.id.artisttrack_artist_name);
           holder.track = (TextView)vi.findViewById(R.id.artisttrack_track_name); 
           holder.thumb=(ImageView)vi.findViewById(R.id.artisttrack_list_image);

   			vi.setTag(holder);
   		} else {
   			holder = (ViewHolder) vi.getTag();
   		}
       
       HashMap<String, String> hash = new HashMap<String, String>();
       hash = filteredData.get(position);
 
       holder.name.setText(hash.get(TracksListFragment.KEY_ARTIST_NAME));
       holder.track.setText(hash.get(TracksListFragment.KEY_TRACK_NAME));
       imageLoader.DisplayImage(hash.get(TracksListFragment.KEY_ALBUM_URL), holder.thumb);
       return vi;

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
                    	 String filterableString = searchdata.get(HomeListFragment.KEY_ARTIST_NAME);
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
                notifyDataSetChanged();
            }
        };
    }
}
