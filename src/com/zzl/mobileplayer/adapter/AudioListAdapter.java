package com.zzl.mobileplayer.adapter;

import com.zzl.mobileplayer.R;
import com.zzl.mobileplayer.bean.AudioItem;
import com.zzl.mobileplayer.util.StringUtil;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class AudioListAdapter extends CursorAdapter{

	public AudioListAdapter(Context context, Cursor c) {
		super(context, c);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return View.inflate(context, R.layout.adapter_audio_list, null);
	}
	ViewHolder holder;
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		holder = getHolder(view);
		
		AudioItem audioItem = AudioItem.fromCursor(cursor);
		
		holder.tv_artist.setText(audioItem.getArtist());
		holder.tv_name.setText(StringUtil.formatAudioName(audioItem.getTitle()));
	}
	
	private ViewHolder getHolder(View view){
		ViewHolder viewHolder = (ViewHolder) view.getTag();
		if(viewHolder==null){
			viewHolder = new ViewHolder(view);
			view.setTag(viewHolder);
		}
		return viewHolder;
	}
	
	class ViewHolder{
		TextView tv_name,tv_artist;
		public ViewHolder(View view){
			tv_name = (TextView) view.findViewById(R.id.tv_name);
			tv_artist = (TextView) view.findViewById(R.id.tv_artist);
		}
	}

}
