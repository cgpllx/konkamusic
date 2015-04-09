package com.konka.music.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkManager {
	public interface netWorkType {
		int TYPE_NO_NETWORK = 0;
		int TYPE_WIFI = 1;
		int TYPE_MOBILE = 1 << 1;
		int TYPE_OTHER = 1 << 2;
	}

	public static int getNetWorkType(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (null != activeNetwork) {
			switch (activeNetwork.getType()) {
			case ConnectivityManager.TYPE_WIFI:
				return netWorkType.TYPE_WIFI;
			case ConnectivityManager.TYPE_MOBILE:
				return netWorkType.TYPE_MOBILE;
			default:
				return netWorkType.TYPE_OTHER;
			}
		} else {
			return netWorkType.TYPE_NO_NETWORK;
		}
	}

	public static boolean isConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			return false;
		}
	}

}
