
package com.myitprovider.bnm.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myitprovider.bnm.R;
import com.myitprovider.bnm.fragments.TopTenListfragment;
import com.myitprovider.bnm.util.ImageLoader;

public class TopTenfragmentAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
  
     public TopTenfragmentAdapter (Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
        
    }
     
 
	public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    public static class ViewHolder {
       RelativeLayout listrow;
  	   TextView name;
  	   TextView track;
  	   TextView number;
  	   ImageView thumb;
  	}
    public View getView(int position, View convertView, ViewGroup parent) {
    	ViewHolder holder;
        View vi=convertView;
        if(convertView==null){
            vi = inflater.inflate(R.layout.layout_bnmtoptenlistrow, null);

        holder = new ViewHolder();
        holder.name = (TextView)vi.findViewById(R.id.topten_artist_name); 
        holder.track = (TextView)vi.findViewById(R.id.topten_album_name); 
        holder.number= (TextView)vi.findViewById(R.id.topten_number);
        holder.thumb=(ImageView)vi.findViewById(R.id.topten_list_image); 
        holder.listrow=(RelativeLayout)vi.findViewById(R.id.topten_listrow);
        
        vi.setTag(holder);
        }
        else {
            holder = (ViewHolder) vi.getTag();
        }
        
        HashMap<String, String> hash = new HashMap<String, String>();
        hash = data.get(position);
       
        
        
        holder.name.setText("Artist: "+hash.get(TopTenListfragment.KEY_NAME));
        holder.track.setText("Track: "+hash.get(TopTenListfragment.KEY_TRACK_NAME));
        holder.number.setText(hash.get(TopTenListfragment.KEY_NUMBER)+" listens");
        imageLoader.DisplayImage(hash.get(TopTenListfragment.KEY_IMAGE_URL), holder.thumb);
        
       if( position==0){
    		
    		holder.listrow.setBackgroundResource(R.drawable.gold_gradient);
       }
    	else if( position==1){
    		holder.listrow.setBackgroundResource(R.drawable.silver_gradient);
       }
    	else if( position==2){
    		holder.listrow.setBackgroundResource(R.drawable.bronze_gradient);
       }
    	else{
    		holder.listrow.setBackgroundResource(R.drawable.bnm_list_selector);
    	}
        return vi;
    }
    
}
