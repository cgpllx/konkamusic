package com.konka.music.util;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

public class ApiCompatibleUtil {
	@SuppressWarnings("deprecation")
	public static void setViewBackground(View view, Drawable background) {
		if (android.os.Build.VERSION.SDK_INT >= 17) {
			view.setBackground(background);
		} else {
			view.setBackgroundDrawable(background);
		}
	}

 
	public static void setTextViewCompoundDrawables(TextView textview, Drawable start, Drawable top, Drawable end, Drawable bottom) {
		if (android.os.Build.VERSION.SDK_INT >= 17) {
			textview.setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom);
		} else {
			textview.setCompoundDrawables(start, top, end, bottom);
		}
	}
}
