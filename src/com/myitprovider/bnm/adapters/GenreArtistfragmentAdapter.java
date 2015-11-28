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
import com.myitprovider.bnm.fragments.AlbumsGenreListFragment;
import com.myitprovider.bnm.util.ImageLoader;

public class GenreArtistfragmentAdapter extends BaseAdapter {
	private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
  
     public GenreArtistfragmentAdapter (Activity a, ArrayList<HashMap<String, String>> d) {
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
    	   TextView genre;
    	   ImageView thumb;
    	}
    public View getView(int position, View convertView, ViewGroup parent) {
    	ViewHolder holder;
        View vi=convertView;
        if(convertView==null){
            vi = inflater.inflate(R.layout.layout_bnmgenrelistrow, null);

        holder = new ViewHolder();
        holder.genre = (TextView)vi.findViewById(R.id.genrename); 
        holder.thumb=(ImageView)vi.findViewById(R.id.genre_image);
        vi.setTag(holder);
        }
        else {
            holder = (ViewHolder) vi.getTag();
        }
              
        HashMap<String, String> hash = new HashMap<String, String>();
        hash = data.get(position);
        
        holder.genre.setText(hash.get(AlbumsGenreListFragment.KEY_ALBUM_NAME));
        
        imageLoader.DisplayImage(hash.get(AlbumsGenreListFragment.KEY_ALBUM_ARTWORK), holder.thumb);
        
        return vi;
    }
    

}

