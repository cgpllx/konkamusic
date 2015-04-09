package com.konka.music.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.konka.music.R;

public class FragmentManagerUtil {
	public static void swichFragment(FragmentManager fragmentManager, Fragment fragment) {
		swichFragment(fragmentManager, fragment, true);
	}

	public static void swichFragment(FragmentManager fragmentManager, Fragment fragment, boolean addToBackStack) {
		swichFragment(fragmentManager, fragment, addToBackStack, R.id.content);
	}

	public static void swichFragment(FragmentManager fragmentManager, Fragment fragment, int containerViewId) {
		swichFragment(fragmentManager, fragment, true, containerViewId);
	}

	public static void swichFragment(FragmentManager fragmentManager, Fragment fragment, boolean addToBackStack, int containerViewId) {
		System.out.println("父类应用之类的名字="+fragment.getClass().getName());
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_right_out);
		ft.replace(containerViewId, fragment, fragment.getClass().getName());
		if (addToBackStack) {
			ft.addToBackStack(fragment.getClass().getName());
		}
		fragment.setUserVisibleHint(true);
		ft.commitAllowingStateLoss();
	}
}
