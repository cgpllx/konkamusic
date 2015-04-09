package com.konka.music.ui.view;

import android.content.Context;
import android.widget.ListPopupWindow;

public class PlayListView extends ListPopupWindow {
	public static final int HEIGHT = 440;
	public static final int WIDTH = 300;

	public PlayListView(Context context) {
		super(context);
		setHeight(HEIGHT);
		setWidth(WIDTH);
	}

}
