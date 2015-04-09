package com.konka.music.util;

import android.content.Context;
import android.widget.Toast;

/**
 * @author wangxu
 * @description Toast多次点击只显示一次
 * */
public class ToastUtil {
	private static String oldMsg;
	protected static Toast toast = null;
	private static long oneTime = 0;
	private static long twoTime = 0;

	public static void showToast (Context context, String s) {
		if (toast == null) {
			toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
			toast.show();
			oneTime = System.currentTimeMillis();
		} else {
			twoTime = System.currentTimeMillis();
			if (s.equals(oldMsg)) {
				if ((twoTime - oneTime) > Toast.LENGTH_SHORT) {
					toast.show();
				}
			} else {
				oldMsg = s;
				toast.setText(s);
				toast.show();
			}
		}
		oneTime = twoTime;
	}

	public static void showToast(Context context, int resId) {
		showToast(context, context.getString(resId));
	}

	public static void showLongToast(Context context, String text) {
		if(toast==null){
			toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		}else{
			toast.setText(text);
		}
		toast.show();
	}
	public static void cancelToast(){
		if(toast!=null){
			toast.cancel();
		}
	}
}
