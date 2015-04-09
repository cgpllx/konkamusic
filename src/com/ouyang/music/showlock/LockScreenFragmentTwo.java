package com.ouyang.music.showlock;


import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.konka.music.R;
import com.konka.music.pojo.MusicInfo;
import com.konka.music.service.MusicInfoManager;
import com.konka.music.ui.fragment.MusicPlayFragment;
import com.konka.music.util.TimeHelper;

@SuppressWarnings("unchecked")
public class LockScreenFragmentTwo extends MusicPlayFragment implements OnClickListener{

	private ImageView  nextBtn;
	private ImageView  pauseOrPlayBtn;
	private ImageView lockscreen_iv;
	private TextView date_tv;
	private TextView title_tv;
	private TextView currentPosition_tv,duration_tv;
	private AnimationDrawable animationDrawable; 
//	private MusicInfo mMusicInfo;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View viewRoot =  inflater.inflate(R.layout.lockscreen_page_two, container,false);
		nextBtn = (ImageView) viewRoot.findViewById(R.id.nextBtn);
		nextBtn.setOnClickListener(this);
		pauseOrPlayBtn = (ImageView) viewRoot.findViewById(R.id.pauseOrPlay_Btn);
		pauseOrPlayBtn.setOnClickListener(this);
		date_tv=(TextView) viewRoot.findViewById(R.id.date_tv);
		title_tv=(TextView) viewRoot.findViewById(R.id.title_tv);
		currentPosition_tv=(TextView) viewRoot.findViewById(R.id.current_position_tv);
		duration_tv = (TextView) viewRoot.findViewById(R.id.duration_tv);
		lockscreen_iv= (ImageView) viewRoot.findViewById(R.id.lockscreen);
		showDate();
		beginAnimation();
		return viewRoot;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	}
	
	
	@Override
	public void onMusicStop() {
		// TODO Auto-generated method stub
//		super.onMusicStop();
	}

	@Override
	public void onMusicBufferingUpdateProgress(int progress) {
		// TODO Auto-generated method stub
//		super.onMusicBufferingUpdateProgress(progress);
	}

	@Override
	public void onClick(View v) {

		if(v.getId()==R.id.nextBtn)
		{
			MusicInfoManager.playNext(getActivity());
		}
		if(v.getId()==R.id.pauseOrPlay_Btn)
		{
			MusicInfoManager.playOrPauseMusic(getActivity());
		}
		
	}
	
	@SuppressLint("SimpleDateFormat") private void showDate()
	{
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("MM月dd日  E");
        String formattedDate = df.format(calendar.getTime());
        date_tv.setText(formattedDate);
	}
	
	//滑动解锁图片动画
	private void beginAnimation()
	{
		animationDrawable = (AnimationDrawable) lockscreen_iv.getDrawable();  
        animationDrawable.start();
	}
	
	private void showTitle(String title)
	{
		title_tv.setText(title);
	}
	
	private void setPauseOrPlayBtnBg(boolean isPlaying)
	{
		if(isPlaying)
			pauseOrPlayBtn.setBackgroundResource(R.drawable.lock_screen_pause_btn_selector);	
		else			
	    	pauseOrPlayBtn.setBackgroundResource(R.drawable.lock_screen_play_btn_selector);		
	}
	

	@Override
	public void onMusicPaly(MusicInfo musicinfo) {
//		super.onMusicPaly(musicinfo);
		setPauseOrPlayBtnBg(true);
	}

	@Override
	public void onMusicPause() {
//		super.onMusicPause();
		setPauseOrPlayBtnBg(false);
	}


	@Override
	public void onMusicPlaybackProgress(int progress) {
//		super.onMusicPlaybackProgress(progress);
		currentPosition_tv.setText(TimeHelper.milliSecondsToFormatTimeString(getCurPosition()));
		duration_tv.setText("/"+TimeHelper.milliSecondsToFormatTimeString(getDuration()));
	}

	@Override
	public void onServiceBindComplete() {
//		super.onServiceBindComplete();
		// initPlaybuttonView();
		MusicInfo musicInfo = getMusicInfo();
		showTitle(musicInfo.getTitle());
		setPauseOrPlayBtnBg(isPlaying());
	}

	@Override
	public void introductionData(MusicInfo musicinfo) {
//		super.introductionData(musicinfo);
		showTitle(musicinfo.getTitle());
	}
	
	

}
