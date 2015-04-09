package com.ouyang.music.showlock;

import android.content.Context;
import android.content.Intent;

import com.kubeiwu.baseclass.util.KLog;

public class ShowLockUtil {
//	
//	public static void startLockService(Context context)
//	{
//		KLog.d("Ouyang","启动LockService");
//		Intent intent = new  Intent();
//		intent.setClass(context, LockService.class);
//		context.startService(intent);
//	}
	
//	public static void stopLockService(Context context)
//	{
//		Intent intent = new  Intent();
//		intent.setClass(context, LockService.class);
//		context.stopService(intent);
//	}
	
	public static void startShowActivity (Context context)
	{
		 KLog.d("Ouyang","----------------- 启动Activity------");
		Intent showLockIntent = new Intent(context, ShowLockActivity.class);
		showLockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(showLockIntent);
	}

}
