//package com.ouyang.music.showlock;
//
//import com.kubeiwu.baseclass.util.KLog;
//
//import android.app.Service;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.IBinder;
//
//public class LockService1 extends Service {
////	private String TAG = "Ouyang";
////	private LockService lockServiceInstance = this;
////
////	@Override
////	public IBinder onBind(Intent arg0) {
////		return null;
////	}
////
////	public void onCreate() {
////		super.onCreate();
////		KLog.i(TAG, "----------------- onCreate------");
////		/* 注册广播 */
////		IntentFilter intentFilter = new IntentFilter();
////		intentFilter.addAction("android.intent.action.SCREEN_ON");
////		intentFilter.addAction("android.intent.action.SCREEN_OFF");
////		registerReceiver(mScreenOffReceiver, intentFilter);
////		String[] lackBroadcastActions = { "android.intent.action.SCREEN_ON", "android.intent.action.SCREEN_OFF" };
////	}
////
////	public void onDestroy() {
////		super.onDestroy();
////		KLog.i(TAG, "----------------- onDestroy------");
////		this.unregisterReceiver(mScreenOffReceiver);// 注销广播
////	}
////
////	// 屏幕变暗/变亮的广播 ， 我们要调用KeyguardManager类相应方法去解除屏幕锁定
////	private BroadcastReceiver mScreenOffReceiver = new BroadcastReceiver() {
////		@Override
////		public void onReceive(Context context, Intent intent) {
////			String action = intent.getAction();
////			if (action.equals("android.intent.action.SCREEN_ON")) {
////				KLog.i(TAG, "----------------- android.intent.action.SCREEN_ON------");
////				ShowLockUtil.startShowActivity(lockServiceInstance);
////			}
////			if (action.equals("android.intent.action.SCREEN_OFF")) {
////				KLog.i(TAG, "----------------- android.intent.action.SCREEN_OFF------");
////			}
////		}
////	};
//}
