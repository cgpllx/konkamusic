package com.konka.music.util;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmUtil {
	
	public static final String SELECTTIME = "selecttime";
	public static final String TIMEOVER = "timeover";
	public static final String ALERMTYPE = "alarm_type";
	
	public static final int MULTIPLE = 60*1000;
	public static final int INTERVAL = 1000;
	
	public static final int TYPE_TUNRE_OFF = 0;
	public static final int TYPE_TEN_MINUTES = 10;  
	public static final int TYPE_TWENTY_MINUTES = 20;
	public static final int TYPE_THIRTY_MINUTES = 30;
	public static final int TYPE_ONE_HOUR = 60;
	public static final int TYPE_ONE_HALF_HOUR = 90;
	public static final int TYPE_CUSTOM_TIME = 5;
	
	public static final int TYPE_EXIT_APP = 1;
	public static final int TYPE_STOP_MUSIC = 0;
	
	/**
	 * 将定时复位
	 * */
	public static void turnOffAlarm (Context context) {
//		Long begin_time = System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(0);
		calendar.add(Calendar.MILLISECOND, 0);
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent("");
//		intent.putExtra(ALERMTYPE, TYPE_STOP_MUSIC);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
		MyPreference.putPref(TIMEOVER, 0L);
	}
}
