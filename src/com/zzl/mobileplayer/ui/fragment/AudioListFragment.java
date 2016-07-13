package com.zzl.mobileplayer.ui.fragment;

import java.io.Serializable;
import java.util.ArrayList;

//import io.vov.vitamio.MediaPlayer;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore.Audio.Media;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.zzl.mobileplayer.R;
import com.zzl.mobileplayer.adapter.AudioListAdapter;
import com.zzl.mobileplayer.base.BaseFragment;
import com.zzl.mobileplayer.bean.AudioItem;
import com.zzl.mobileplayer.db.SimpleQueryHandler;
import com.zzl.mobileplayer.ui.activity.AudioPlayerActivity;

public class AudioListFragment extends BaseFragment{
	private ListView listview;
	
	private AudioListAdapter adapter;
	private SimpleQueryHandler queryHandler;
	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_audio_list, null);
		listview = (ListView) view.findViewById(R.id.listview);
		return view;
	}

	@Override
	protected void initListener() {
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Cursor cursor = (Cursor) adapter.getItem(position);
				Bundle bundle = new Bundle();
				bundle.putInt("currentPosition", position);
				bundle.putSerializable("audioList", cursorToList(cursor));
				enterActivity(AudioPlayerActivity.class, bundle);
			}
		});
	}

	@Override
	protected void initData() {
		adapter = new AudioListAdapter(getActivity(), null);
		listview.setAdapter(adapter);
		
		queryHandler = new SimpleQueryHandler(getActivity().getContentResolver());
		
		String[] projection = {Media._ID,Media.DISPLAY_NAME,Media.ARTIST,Media.DATA,Media.DURATION};
		queryHandler.startQuery(0, adapter, Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
	}
	
	/**
	 * 将cursor中 的数据取出来放入一个集合
	 * @param cursor
	 * @return
	 */
	private ArrayList<AudioItem> cursorToList(Cursor cursor){
		ArrayList<AudioItem> list = new ArrayList<AudioItem>();
		cursor.moveToPosition(-1);
		while(cursor.moveToNext()){
			list.add(AudioItem.fromCursor(cursor));
		}
		
		return list;
	}

	@Override
	protected void processClick(View view) {
		
	}

}
