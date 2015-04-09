package com.konka.music.util;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

public class ViewUtility {

	@SuppressWarnings("unchecked")
	public static <T extends View> T findViewById(View view, int id) {
		return (T) view.findViewById(id);
	}

	@SuppressWarnings("unchecked")
	public static <T extends View> T findViewById(Activity activity, int id) {
		return (T) activity.findViewById(id);
	}

	@SuppressWarnings("unchecked")
	public static <T extends View> T findViewById(View view, int id, OnClickListener onClickListener) {
		T t = (T) view.findViewById(id);
		if (onClickListener != null) {
			t.setOnClickListener(onClickListener);
		}
		return t;
	}

	@SuppressWarnings("unchecked")
	public static <T extends View> T findViewById(Activity activity, int id, OnClickListener onClickListener) {
		T t = (T) activity.findViewById(id);
		if (onClickListener != null) {
			t.setOnClickListener(onClickListener);
		}
		return t;
	}
}
