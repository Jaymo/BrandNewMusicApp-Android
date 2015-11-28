
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
import android.widget.TextView;

import com.myitprovider.bnm.R;
import com.myitprovider.bnm.fragments.TracksListFragment;
import com.myitprovider.bnm.util.ImageLoader;

public class AtristTracksfragmentAdapter extends BaseAdapter {
	private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
  
     public AtristTracksfragmentAdapter (Activity a, ArrayList<HashMap<String, String>> d) {
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
 	   TextView name;
 	   TextView track;
 	   ImageView thumb;
 	}
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        ViewHolder holder;
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
        hash = data.get(position);
        
        holder.name.setText(hash.get(TracksListFragment.KEY_ARTIST_NAME));
        holder.track.setText(hash.get(TracksListFragment.KEY_TRACK_NAME));
        imageLoader.DisplayImage(hash.get(TracksListFragment.KEY_ALBUM_URL), holder.thumb);
        return vi;
    }


}
