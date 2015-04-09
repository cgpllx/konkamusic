package com.konka.music.util;

import android.content.Context;

import com.kubeiwu.commontool.view.setting.annomotion.IsPara;
import com.kubeiwu.commontool.view.util.Para;
import com.kubeiwu.commontool.view.util.SettingsUtil;

public interface ParaSetting {
	@IsPara
	Para<Boolean> LOCK_SCREEN = new Para<Boolean>("lock_screen", false);// 1
	@IsPara
	Para<Boolean> WAVE_CUT_SONG = new Para<Boolean>("wave_cut_song", false);// 2
	@IsPara
	Para<Boolean> ONLY_WIFI_NETWORK = new Para<Boolean>("only_wifi_network", true);// 8
	@IsPara
	Para<String> CUSTOM_SONG_DOWNLOAD_DIRECTORY = new Para<String>("custom_song_download_directory", "");// 3
	@IsPara
	Para<String> CLEAR_CACHE = new Para<String>("clear_Cache", "");// 4
	@IsPara
	Para<String> FOR_MY_SCORE = new Para<String>("for_my_score", "");// 5
	@IsPara
	Para<String> CHECK_FOR_UPDATES = new Para<String>("check_for_updates", "");// 6
	@IsPara
	Para<String> ABOUT_US = new Para<String>("about_Us", "");// 7
	@IsPara
	Para<String> EXIT = new Para<String>("exit", "");// 7
	public class ParaUtil {
		public static void initPrar(Context context) {
			SettingsUtil.initPrar(context, ParaSetting.class);
		}
	}
}
