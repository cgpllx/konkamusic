package com.konka.music.ui.activity;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.konka.music.R;
import com.konka.music.util.AlarmUtil;
import com.konka.music.util.Assist;
import com.konka.music.util.MyPreference;
import com.konka.music.util.StringHelper;

public class AlarmActivity extends SuperActivity implements OnClickListener {

	public static final String TAG = "TimmingActivity";
	private static int selectTime = AlarmUtil.TYPE_TUNRE_OFF;
	private static int alarm_type = AlarmUtil.TYPE_STOP_MUSIC;
	
	private TextView mViewCountDownTimer;
	private View mViewTenMin;
	private View mViewTwentyMin;
	private View mViewThirtyMin;
	private View mViewOneHour;
	private View mViewOneAndHalfHour;
	private View mViewTurnOff;
	private View mViewCustomTime;
	private View mViewExitApp;
	private View mViewStopMusic;
	private ImageView mViewMarkImg;
	private MyCount mMyCount;
	private Calendar mCalendar;
	
	private void initView() {
		mViewTurnOff = findViewById(R.id.music_alarm_turn_off);
		mViewTenMin = findViewById(R.id.kg_music_alarm_list_item1);
		mViewTwentyMin = findViewById(R.id.kg_music_alarm_list_item2);
		mViewThirtyMin = findViewById(R.id.kg_music_alarm_list_item3);
		mViewOneHour = findViewById(R.id.kg_music_alarm_list_item4);
		mViewOneAndHalfHour = findViewById(R.id.kg_music_alarm_list_item5);
		mViewCustomTime = findViewById(R.id.music_alarm_custom);
		mViewExitApp = findViewById(R.id.music_alarm_layout_exit);
		mViewStopMusic = findViewById(R.id.music_alarm_layout_stop);
	}
	
	private void initListener() {
		// TODO Auto-generated method stub
		//退出应用
		mViewExitApp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (alarm_type != AlarmUtil.TYPE_EXIT_APP) {
					alarm_type = AlarmUtil.TYPE_EXIT_APP;
					MyPreference.putPref(AlarmUtil.ALERMTYPE, alarm_type);
					setViewCountTimer();
				}
			}
		});
		//停止播放
		mViewStopMusic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (alarm_type != AlarmUtil.TYPE_STOP_MUSIC) {
					alarm_type = AlarmUtil.TYPE_STOP_MUSIC;
					setViewCountTimer();
					MyPreference.putPref(AlarmUtil.ALERMTYPE, alarm_type);
				}
			}
		});
	}
	
	private void initData() {
		mCalendar = Calendar.getInstance();
		selectTime = MyPreference.getPref(AlarmUtil.SELECTTIME, AlarmUtil.TYPE_TUNRE_OFF);
		alarm_type = MyPreference.getPref(AlarmUtil.ALERMTYPE, AlarmUtil.TYPE_STOP_MUSIC);
		Long cur_time = System.currentTimeMillis();
		Long over_time = MyPreference.getPref(AlarmUtil.TIMEOVER, cur_time);
		Long left_time = over_time - cur_time;
		if (left_time > 0) {
			startMC(left_time, AlarmUtil.INTERVAL);
		} else {
			selectTime = AlarmUtil.TYPE_TUNRE_OFF;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music_alarm_activity);
		initView();
		initListener();
		initData();
		setViewCountTimer();
		setViewMarkImg();
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		mViewMarkImg.setVisibility(View.GONE);
		switch (v.getId()) {

		case R.id.music_alarm_turn_off:
			selectTime = AlarmUtil.TYPE_TUNRE_OFF;
			break;
		case R.id.kg_music_alarm_list_item1:
			selectTime = AlarmUtil.TYPE_TEN_MINUTES;
			break;
		case R.id.kg_music_alarm_list_item2:
			selectTime = AlarmUtil.TYPE_TWENTY_MINUTES;
			break;
		case R.id.kg_music_alarm_list_item3:
			selectTime = AlarmUtil.TYPE_THIRTY_MINUTES;
			break;
		case R.id.kg_music_alarm_list_item4:
			selectTime = AlarmUtil.TYPE_ONE_HOUR;
			break;
		case R.id.kg_music_alarm_list_item5:
			selectTime = AlarmUtil.TYPE_ONE_HALF_HOUR;
			break;
		case R.id.music_alarm_custom:
			selectTime = AlarmUtil.TYPE_CUSTOM_TIME;
			break;
		default:
			break;
		}
		MyPreference.putPref(AlarmUtil.SELECTTIME, selectTime);
		setViewMarkImg();
		setAlarm(selectTime * AlarmUtil.MULTIPLE);
		startMC(selectTime * AlarmUtil.MULTIPLE, AlarmUtil.INTERVAL);
	}


	private void setAlarm(long time) {
		Long begin_time = System.currentTimeMillis();
		mCalendar.setTimeInMillis(begin_time);
		mCalendar.add(Calendar.MILLISECOND, (int)time);
		AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(Assist.BROADCAST_ACTION_ALARM);
		intent.putExtra(AlarmUtil.ALERMTYPE, alarm_type);
		PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		am.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), sender);
		MyPreference.putPref(AlarmUtil.TIMEOVER, begin_time + time);
	}

	private void setViewMarkImg() {
		switch (selectTime) {
		case AlarmUtil.TYPE_TUNRE_OFF:
			mViewMarkImg = (ImageView) mViewTurnOff.findViewById(R.id.music_alarm_item_checked);
			break;
		case AlarmUtil.TYPE_TEN_MINUTES:
			mViewMarkImg = (ImageView) mViewTenMin.findViewById(R.id.music_alarm_item_checked);
			break;
		case AlarmUtil.TYPE_TWENTY_MINUTES:
			mViewMarkImg = (ImageView) mViewTwentyMin.findViewById(R.id.music_alarm_item_checked);
			break;
		case AlarmUtil.TYPE_THIRTY_MINUTES:
			mViewMarkImg = (ImageView) mViewThirtyMin.findViewById(R.id.music_alarm_item_checked);
			break;
		case AlarmUtil.TYPE_ONE_HOUR:
			mViewMarkImg = (ImageView) mViewOneHour.findViewById(R.id.music_alarm_item_checked);
			break;
		case AlarmUtil.TYPE_ONE_HALF_HOUR:
			mViewMarkImg = (ImageView) mViewOneAndHalfHour.findViewById(R.id.music_alarm_item_checked);
			break;
		case AlarmUtil.TYPE_CUSTOM_TIME:
			mViewMarkImg = (ImageView) mViewCustomTime.findViewById(R.id.music_alarm_item_checked);
			break;
		default:
			break;
		}
		mViewMarkImg.setVisibility(View.VISIBLE);
	}

	private void setViewCountTimer() {
		if (mViewCountDownTimer != null)
			mViewCountDownTimer.setVisibility(View.GONE);
		if (alarm_type == AlarmUtil.TYPE_STOP_MUSIC) {
			mViewCountDownTimer = (TextView) mViewStopMusic.findViewById(R.id.music_alarm_timer);
		} else {
			mViewCountDownTimer = (TextView) mViewExitApp.findViewById(R.id.music_alarm_timer);
		}
		mViewCountDownTimer.setVisibility(View.VISIBLE);
		mViewCountDownTimer.setText(R.string.music_alarm_init_countdown);
	}
	
	private void startMC(long int1, long int2) {
		if (mMyCount != null) {
			mMyCount.cancel();
		}
		mMyCount = new MyCount(int1, int2 );
		mMyCount.start();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mMyCount != null)
			mMyCount.cancel();
		finish();
	}

	class MyCount extends CountDownTimer {
		
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
//			mViewMarkImg.setVisibility(View.GONE);
			selectTime = 0;
			setViewMarkImg();
			setViewCountTimer();
//			mViewMarkImg.setVisibility(View.VISIBLE);
//			mViewCountDownTimer.setText("00:00");
		}

		@Override
		public void onTick(long millisUntilFinished) {
			mViewCountDownTimer.setText(StringHelper.generateTime(millisUntilFinished));
		}
	}
}
