
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myitprovider.bnm.R;
import com.myitprovider.bnm.fragments.HomeListFragment;
import com.myitprovider.bnm.util.ImageLoader;

public class HomeListFragmentAdapter extends BaseAdapter implements Filterable
{
    private Activity activity;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader;
    public ArrayList<HashMap<String, String>> originalData;
    public ArrayList<HashMap<String, String>> filteredData;
    String status;
    public HomeListFragmentAdapter(Activity a, ArrayList<HashMap<String, String>> d) 
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
       RelativeLayout listrow;
 	   TextView name;
 	   TextView count;
 	   TextView update;
 	   TextView track;
 	   ImageView thumb;
 	}
    public View getView(int position, View convertView, ViewGroup parent) 
    {
        ViewHolder holder;
       View vi = convertView;
       if(convertView==null){
    	   vi = inflater.inflate(R.layout.layout_bnmhomelistrow, null);

       holder = new ViewHolder();
       holder.name = (TextView)vi.findViewById(R.id.artist_name); 
       holder.count= (TextView)vi.findViewById(R.id.count);
       holder.update= (TextView)vi.findViewById(R.id.status_update);
       holder.track= (TextView)vi.findViewById(R.id.latest_track);
       holder.thumb =(ImageView)vi.findViewById(R.id.artist_list_image); 
       holder.listrow =(RelativeLayout)vi.findViewById(R.id.list_row);
       
       vi.setTag(holder);
       }
       else {
           holder = (ViewHolder) vi.getTag();
       }
       
       HashMap<String, String> artist = new HashMap<String, String>();
       artist = filteredData.get(position);
  
       holder.name.setText(artist.get(HomeListFragment.KEY_ARTIST_NAME));
       holder.count.setText(artist.get(HomeListFragment.KEY_COUNT)+" tracks");
       holder.update.setText("Status: "+artist.get(HomeListFragment.KEY_STATUS_UPDATE));
       holder.track.setText("Latest: "+artist.get(HomeListFragment.KEY_TRACK_NAME));
       imageLoader.DisplayImage(artist.get(HomeListFragment.KEY_IMAGE_URL), holder.thumb);
      
       
      if(artist.get(HomeListFragment.KEY_INDEX).contentEquals("0")){
   		holder.listrow.setBackgroundResource(R.drawable.gold_gradient);
        }
      else{
    	  holder.listrow.setBackgroundResource(R.drawable.bnm_list_selector); 
      }
       
       
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
