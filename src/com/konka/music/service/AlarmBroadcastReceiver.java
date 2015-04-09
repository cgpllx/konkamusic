package com.konka.music.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.konka.music.R;
import com.konka.music.util.AlarmUtil;
import com.konka.music.util.Assist;
import com.konka.music.util.ToastUtil;
import com.kubeiwu.baseclass.util.KLog;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		KLog.i("wangxu", "TimerBroadcastReceiver->action=" + action);

		if (action.equals(Assist.BROADCAST_ACTION_ALARM)) {
			int type = intent.getIntExtra("alarm_type", 0);
			if (type == 0) { // 停止播放
				ToastUtil.showToast(context, R.string.music_alarm_to_stop_timeover);
				MusicInfoManager.stopMusic(context);
			} else if (type == 1) {
				ToastUtil.showToast(context, R.string.music_alarm_to_exit_nonselected_timeover);
				MusicInfoManager.exitApp(context);
				// System.exit(0);
			}
		} else if (action.equals(Assist.BROADCAST_ACTION_PAUSE) || action.equals(Assist.BROADCAST_ACTION_STOP) || action.equals(Assist.BROADCAST_ACTION_EXIT)) {
			AlarmUtil.turnOffAlarm(context);
		}
	}
}
