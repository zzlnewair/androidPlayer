package com.zzl.mobileplayer.ui.activity;

import java.util.ArrayList;

import com.zzl.mobileplayer.R;
import com.zzl.mobileplayer.R.layout;
import com.zzl.mobileplayer.adapter.MainPagerAdapter;
import com.zzl.mobileplayer.ui.fragment.AudioListFragment;
import com.zzl.mobileplayer.ui.fragment.VideoListFragment;
import com.nineoldandroids.view.ViewPropertyAnimator;

import android.os.Bundle;
import android.provider.MediaStore.Video.Media;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements OnClickListener{
	private TextView tab_video, tab_audio;
	private View indicate_line;
	private ViewPager viewPager;

	private MainPagerAdapter adapter;
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	private int indicateLineWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initListener();
		initData();
	}

	private void initView() {
		setContentView(R.layout.activity_main);
		tab_video = (TextView) findViewById(R.id.tab_video);
		tab_audio = (TextView) findViewById(R.id.tab_audio);
		indicate_line = findViewById(R.id.indicate_line);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
	}

	private void initListener() {
		tab_video.setOnClickListener(this);
		tab_audio.setOnClickListener(this);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				lightAndScaleTitle();// 高亮并缩放标题
			}
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				int targetPosition = position*indicateLineWidth + positionOffsetPixels/fragments.size();
				ViewPropertyAnimator.animate(indicate_line).translationX(targetPosition).setDuration(0);
			}
			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}
	
	/**
	 * 计算指示线的宽
	 */
	private void caculateIndicateLineWidth(){
		int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		indicateLineWidth = screenWidth/fragments.size();
		indicate_line.getLayoutParams().width = indicateLineWidth;
		indicate_line.requestLayout();
	}

	private void initData() {
		fragments.add(new VideoListFragment());
		fragments.add(new AudioListFragment());
		
		caculateIndicateLineWidth();// 计算指示线的宽
		
		adapter = new MainPagerAdapter(getSupportFragmentManager(), fragments);
		viewPager.setAdapter(adapter);

		lightAndScaleTitle();//高亮并缩放标题
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tab_video:
			viewPager.setCurrentItem(0);
			break;
		case R.id.tab_audio:
			viewPager.setCurrentItem(1);
			
			break;
		}
	}
	
	/**
	 * 高亮并缩放标题
	 */
	private void lightAndScaleTitle() {
		int currentPage = viewPager.getCurrentItem();

		tab_video.setTextColor(currentPage == 0 ? getResources().getColor(
				R.color.indicate_line) : getResources().getColor(
				R.color.gray_white));
		tab_audio.setTextColor(currentPage == 1 ? getResources().getColor(
				R.color.indicate_line) : getResources().getColor(
				R.color.gray_white));

		ViewPropertyAnimator.animate(tab_video)
				.scaleX(currentPage == 0 ? 1.2f : 1.0f).setDuration(200);
		ViewPropertyAnimator.animate(tab_video)
				.scaleY(currentPage == 0 ? 1.2f : 1.0f).setDuration(200);
		ViewPropertyAnimator.animate(tab_audio)
				.scaleX(currentPage == 1 ? 1.2f : 1.0f).setDuration(200);
		ViewPropertyAnimator.animate(tab_audio)
				.scaleY(currentPage == 1 ? 1.2f : 1.0f).setDuration(200);
	}


}
