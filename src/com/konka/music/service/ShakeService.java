package com.konka.music.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;

import com.konka.music.util.ParaSetting;
import com.konka.music.util.ShakeUtil;
import com.konka.music.util.ToastUtil;

public class ShakeService  extends Service {

	private SensorManager sensorManager;
	private Vibrator vibrator;
	private static final int SENSOR_SHAKE = 10;

	public static final String ACTION_START_SERVICE = "com.konka.music.action_start_service";
	public static final String ACTION_STOP_SERVICE = "com.konka.music.action_stop_service";

	// protected int i = 0;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SENSOR_SHAKE:
				if (ParaSetting.WAVE_CUT_SONG.value) {
					ToastUtil.showToast(getApplicationContext(), "摇一摇切歌");
					MusicInfoManager.playNext(getApplicationContext());
				}
				break;
			}
		}

	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			String action = intent.getAction();
			System.out.println("action="+action);
			if (action.equals(ACTION_START_SERVICE)) {
				processStartAction();
			} else if (action.equals(ACTION_STOP_SERVICE)) {
				processStopAction();
			}
		}

		return START_NOT_STICKY;
	}

	private void processStartAction() {
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		if (sensorManager != null) {
			sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
		}
	}

	private void processStopAction() {
		if (sensorManager != null){
			sensorManager.unregisterListener(sensorEventListener);
		}
		stopSelf();
	}

	 
	private SensorEventListener sensorEventListener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			int sensorType = event.sensor.getType();
			float[] values = event.values;
			float x = values[0];
			float y = values[1];
			float z = values[2];
			int medumValue = 17;
			if (sensorType == Sensor.TYPE_ACCELEROMETER) {
				boolean isShake;
				if (medumValue != 15)
					isShake = (Math.abs(x) > medumValue) || (Math.abs(y) > medumValue) || (Math.abs(z) > medumValue);
				else
					isShake = (Math.abs(x) > medumValue && Math.abs(y) > medumValue) || (Math.abs(y) > medumValue && Math.abs(z) > medumValue) || (Math.abs(x) > medumValue && Math.abs(z) > medumValue);

				if (isShake) {
					if (ShakeUtil.isAllow()) {
						vibrator.vibrate(200);
						Message msg = new Message();
						msg.what = SENSOR_SHAKE;
						handler.sendMessage(msg);
					} else {
						// KLog.e(, "800ms");
					}
				}
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}
	};

	public static void actionStart(Context context) {
		Intent intent = new Intent(ACTION_START_SERVICE);
		context.startService(intent);
	}

	public static void actionStop(Context context) {
		Intent intent = new Intent(ACTION_STOP_SERVICE);
		context.startService(intent);
	}
}