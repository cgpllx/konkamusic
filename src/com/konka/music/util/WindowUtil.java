package com.konka.music.util;

import android.view.View;

public class WindowUtil {
	public static int[] getViewInTheWindowPosition(View view) {
		int[] location = new int[2];
		if (view != null && view.getTag() != null) {
			view.getLocationInWindow(location); // 获取在当前窗口内的绝对坐标
			view.getLocationOnScreen(location);// 获取在整个屏幕内的绝对坐标
		}
		return location;
	}
}
