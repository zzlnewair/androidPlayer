package com.zzl.mobileplayer.ui.activity;

import com.zzl.mobileplayer.R;
import com.zzl.mobileplayer.base.BaseActivity;
import com.zzl.mobileplayer.bean.VideoItem;
import com.zzl.mobileplayer.util.LogUtil;
import com.zzl.mobileplayer.util.StringUtil;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoPlayerActivity extends BaseActivity{
	VideoView videoView;
	//顶部控制面板控件
	private ImageView btn_exit,btn_pre,btn_play,btn_next,btn_screen;
	private TextView tv_name,tv_time;
	private ImageView iv_battery;
	private ImageView iv_volume;
	private SeekBar volumn_seekbar;
	//底部控制面板控件
	private TextView tv_current_position,tv_total_time;
	private SeekBar play_seekbar;
	
	private IntentFilter filter;
	private BatteryBroadcastReceiver batteryBroadcastReceiver;
	
	private final int MESSAGE_UPDATE_TIME = 0;
	private final int MESSAGE_UPDATE_PLAY_PROGRESS = 1;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MESSAGE_UPDATE_TIME:
				updateSystemTime();
				break;
			case MESSAGE_UPDATE_PLAY_PROGRESS:
				updatePlayProgress();
				break;
			}
		};
	};
	private int maxVolume;//最大音量
	private int currentVolume;//当前音量
	private AudioManager audioManager;
	private boolean isMute = false;//是否是静音模式
	private int screenWidth,screenHeight;
	
	@Override
	protected void initView() {
		setContentView(R.layout.activity_video_player);
		videoView = (VideoView) findViewById(R.id.videoView);
		btn_exit = (ImageView) findViewById(R.id.btn_exit);
		btn_pre = (ImageView) findViewById(R.id.btn_pre);
		btn_play = (ImageView) findViewById(R.id.btn_play);
		btn_next = (ImageView) findViewById(R.id.btn_next);
		btn_screen = (ImageView) findViewById(R.id.btn_screen);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_time = (TextView) findViewById(R.id.tv_time);
		iv_battery = (ImageView) findViewById(R.id.iv_battery);
		iv_volume = (ImageView) findViewById(R.id.iv_volume);
		volumn_seekbar = (SeekBar) findViewById(R.id.volumn_seekbar);
		tv_current_position = (TextView) findViewById(R.id.tv_current_position);
		tv_total_time = (TextView) findViewById(R.id.tv_total_time);
		play_seekbar = (SeekBar) findViewById(R.id.play_seekbar);
	}

	@Override
	protected void initListener() {
		iv_volume.setOnClickListener(this);
		btn_exit.setOnClickListener(this);
		btn_pre.setOnClickListener(this);
		btn_play.setOnClickListener(this);
		btn_next.setOnClickListener(this);
		btn_screen.setOnClickListener(this);
		volumn_seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if(fromUser){//是人为手动拖动改变进度
					isMute = false;
					currentVolume = progress;
					updateVolume();
				}
			}
		});
		play_seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if(fromUser){
					videoView.seekTo(progress);
				}
			}
		});
	}

	@Override
	protected void initData() {
		screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		screenHeight = getWindowManager().getDefaultDisplay().getHeight();
		registerBatteryBroadcastReceiver();
		updateSystemTime();
		initVolume();
		
		
		VideoItem videoItem = (VideoItem) getIntent().getExtras().getSerializable("videoItem");
		tv_name.setText(videoItem.getTitle());
		videoView.setVideoURI(Uri.parse(videoItem.getPath()));
		
		
		
		videoView.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				videoView.start();
				
				play_seekbar.setMax(videoView.getDuration());
				updatePlayProgress();
			}
		});
		
		
//		videoView.setMediaController(new MediaController(this));
	}
	
	
	private void updatePlayProgress(){
		LogUtil.e(this, "updatePlayProgress");
		play_seekbar.setProgress(videoView.getCurrentPosition());
		handler.sendEmptyMessageDelayed(MESSAGE_UPDATE_PLAY_PROGRESS, 1000);
	}
	
	/**
	 * 初始化音量
	 */
	private void initVolume(){
		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		//maxVolume最大是15，
		maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		volumn_seekbar.setMax(maxVolume);
		volumn_seekbar.setProgress(currentVolume);
	}
	
	/**
	 * 更新音量
	 */
	private void updateVolume(){
		if(isMute){
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
			volumn_seekbar.setProgress(0);
		}else {
			//第三个参数为1的时候，会显示一个view指示当前音量的变化，一般用0
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
			volumn_seekbar.setProgress(currentVolume);
		}
	}
	
	/**
	 * 注册电量变化广播接受者
	 */
	private void registerBatteryBroadcastReceiver(){
		filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		batteryBroadcastReceiver = new BatteryBroadcastReceiver();
		registerReceiver(batteryBroadcastReceiver, filter);
	}

	/**
	 * 更新系统时间
	 */
	private void updateSystemTime(){
		tv_time.setText(StringUtil.formatSystemTime());
		handler.sendEmptyMessageDelayed(MESSAGE_UPDATE_TIME, 1000);
	}
	
	@Override
	protected void processClick(View view) {
		switch (view.getId()) {
		case R.id.btn_play:
			if(videoView.isPlaying()){
				videoView.pause();
				handler.removeMessages(MESSAGE_UPDATE_PLAY_PROGRESS);
			}else {
				videoView.start();
				handler.sendEmptyMessage(MESSAGE_UPDATE_PLAY_PROGRESS);
			}
			updatePlayBtnBg();
			break;
		case R.id.iv_volume:
			isMute = !isMute;
			updateVolume();
			break;
		}
	}
		
	private float downY;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downY = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			float moveY = event.getY();
			float moveDistance = moveY - downY;//有可能是负值
			
			//如果用户滑动的距离小于15，则认为不想滑动改变音量
			if(Math.abs(moveDistance)<15)break;
			
			isMute = false;
			int totalDistance = Math.min(screenHeight, screenWidth);
			float movePercent = Math.abs(moveDistance)/totalDistance;
			float moveVolume = movePercent * maxVolume;//是特别小的一个值
			if(moveDistance>0){
				//减小音量
				currentVolume -= 1;
			}else {
				//增减音量
				currentVolume += 1;
			}
			
			updateVolume();
			
			downY = moveY;
			break;
		case MotionEvent.ACTION_UP:
			
			break;
		}
		return super.onTouchEvent(event);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
		unregisterReceiver(batteryBroadcastReceiver);
	}
	
	/**
	 * 更新播放按钮的背景图片
	 */
	private void updatePlayBtnBg(){
		btn_play.setBackgroundResource(videoView.isPlaying()?
				R.drawable.selector_btn_pause:R.drawable.selector_btn_play);
	}

	private class BatteryBroadcastReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			//level:0-100
			int level = intent.getIntExtra("level", 0);
//			LogUtil.e(this, "level: "+level);
			updateBatteryImage(level);
		}
		
	}
	
	/**
	 * 根据电量level设置不同的图片
	 * @param level
	 */
	private void updateBatteryImage(int level){
		if(level<=0){
			iv_battery.setBackgroundResource(R.drawable.ic_battery_0);
		}else if (level>0 && level<=10) {
			iv_battery.setBackgroundResource(R.drawable.ic_battery_10);
		}else if (level>10 && level<20) {
			iv_battery.setBackgroundResource(R.drawable.ic_battery_20);
		}else if (level>20 && level<=40) {
			iv_battery.setBackgroundResource(R.drawable.ic_battery_40);
		}else if (level>40 && level<=60) {
			iv_battery.setBackgroundResource(R.drawable.ic_battery_60);
		}else if (level>60 && level<=80) {
			iv_battery.setBackgroundResource(R.drawable.ic_battery_80);
		}else  {
			iv_battery.setBackgroundResource(R.drawable.ic_battery_100);
		}
	}
	
}
