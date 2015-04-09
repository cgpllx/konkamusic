package com.konka.music.util;

import java.util.HashMap;

import android.support.v4.app.Fragment;

public class FragmentCache {
	private static final HashMap<String, Fragment> fragmentContainer = new HashMap<>();

	public static void addFragment(String key, Fragment fragment) {
		fragmentContainer.put(key, fragment);
	}

	public static Fragment getFragment(String key) {
		return fragmentContainer.get(key);
	}
}
