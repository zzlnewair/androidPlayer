package com.zzl.mobileplayer.ui.fragment;

import java.util.ArrayList;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore.Video.Media;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.zzl.mobileplayer.R;
import com.zzl.mobileplayer.adapter.VideoListAdapter;
import com.zzl.mobileplayer.base.BaseFragment;
import com.zzl.mobileplayer.bean.VideoItem;
import com.zzl.mobileplayer.db.SimpleQueryHandler;
import com.zzl.mobileplayer.ui.activity.VideoPlayerActivity;
import com.zzl.mobileplayer.util.CursorUtil;

public class VideoListFragment extends BaseFragment{
	private ListView listview;
	
	private VideoListAdapter adapter;
	private SimpleQueryHandler queryHandler;
	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_video_list, null);
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
				 ArrayList<VideoItem> videoList = cursorToList(cursor);
				
				 Bundle bundle = new Bundle();
				 bundle.putInt("currentPosition", position);
				 bundle.putSerializable("videoList", videoList);
				 enterActivity(VideoPlayerActivity.class, bundle);
			}
		});
	}

	@Override
	protected void initData(){
		adapter = new VideoListAdapter(getActivity(), null);
		listview.setAdapter(adapter);
		
		queryHandler = new SimpleQueryHandler(getActivity().getContentResolver());
		String[] projection = {Media._ID,Media.SIZE,Media.DURATION,Media.TITLE,Media.DATA};
//		Cursor cursor = getActivity().getContentResolver().query(Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
		queryHandler.startQuery(0, adapter, Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
	}

	@Override
	protected void processClick(View view) {
		
	}
	
	/**
	 * 将cursor中的数据解析出并放入集合
	 * @param cursor
	 * @return
	 */
	private ArrayList<VideoItem> cursorToList(Cursor cursor){
		cursor.moveToPosition(-1);//将cursor移动到最初位置，否则获取到的数据很可能不全
		
		ArrayList<VideoItem> list = new ArrayList<VideoItem>();
		
		//遍历cursor的所有结果集，然后将每一条结果集转成VideoItem对象放入集合当中
		while(cursor.moveToNext()){
			list.add(VideoItem.fromCursor(cursor));
		}
		
		return list;
	}

}
