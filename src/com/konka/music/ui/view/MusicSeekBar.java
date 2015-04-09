package com.konka.music.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewDebug.ExportedProperty;
import android.widget.SeekBar;

public class MusicSeekBar extends SeekBar {

	public MusicSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MusicSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MusicSeekBar(Context context) {
		super(context);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}
	@Override
	@ExportedProperty(category = "progress")
	public synchronized int getProgress() {
		// TODO Auto-generated method stub
		return super.getProgress();
	}
	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
//			if(){
//				
//			}
			break;
		}
		return super.onTrackballEvent(event);
	}

}
