
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
import com.myitprovider.bnm.fragments.AlbumsListFragment;
import com.myitprovider.bnm.util.ImageLoader;

public class AlbumsfragmentAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
  
     public AlbumsfragmentAdapter (Activity a, ArrayList<HashMap<String, String>> d) {
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
   	   TextView date;
   	   TextView album;
   	   ImageView thumb;
   	}
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        ViewHolder holder;
        if(convertView==null) {
            vi = inflater.inflate(R.layout.layout_bnmartistalbums, null);

        holder = new ViewHolder();
        holder.album = (TextView)vi.findViewById(R.id.album_name); 
        holder.date = (TextView)vi.findViewById(R.id.album_date); 
        holder.thumb=(ImageView)vi.findViewById(R.id.album_list_image); 
        
        vi.setTag(holder);
		} else {
			holder = (ViewHolder) vi.getTag();
		}
        HashMap<String, String> hash = new HashMap<String, String>();
        hash = data.get(position);      
        
        holder.album.setText("Album: "+hash.get(AlbumsListFragment.KEY_ALBUM_NAME));
        holder.date.setText("Date: "+hash.get(AlbumsListFragment.KEY_ALBUM_DATE));
        imageLoader.DisplayImage(hash.get(AlbumsListFragment.KEY_ALBUM_ARTWORK), holder.thumb);
        
        return vi;
    }
    
    
    
}
