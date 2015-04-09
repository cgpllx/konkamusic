package com.konka.music.ui.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

public class KGSeekBar extends SeekBar {
	private Drawable mDrawable;
	private boolean mPreventTapping = false; // true-禁止点击
	private boolean canDrag = true;// 可以拖动

	public KGSeekBar(Context paramContext) {
		super(paramContext);
	}

	public KGSeekBar(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
	}

	public KGSeekBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
	}

	public void setCanDrag(boolean canDrag) {
		this.canDrag = canDrag;
	}

	/**
	 * 
	 * */
	public boolean a() {
		if ("M9".equals(Build.MODEL))
			return true;
		return this.mPreventTapping;
	}

	/**
	 * 是否点击了thumb图片
	 * */
	final boolean a(int x, int y) {
		if (this.mDrawable != null) {
			Rect localRect = this.mDrawable.getBounds();
			int j = x - getPaddingLeft();
			int k = y - getPaddingTop();
			if ((j < localRect.left) || (j > localRect.right) || (k < localRect.top) || (k > localRect.bottom))
				return true;
			;
		}
		return false;
	}

	final boolean b(int x, int y) {
		if ((y >= 0) && (y <= getHeight()) && (x >= 0) && (x < getPaddingLeft()))
			return true;
		return false;
	}

	final boolean c(int x, int y) {
		if ((y >= 0) && (y <= getHeight()) && (x > getWidth() - getPaddingRight()) && (x <= getWidth()))
			return true;
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent motionEvent) {
		int x = (int) motionEvent.getX();
		int y = (int) motionEvent.getY();
//		if (!canDrag) {
//			return false;
//		}
		System.out.println(canDrag);
		switch (motionEvent.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (a(x, y) && a())
				return false;
			break;
		case MotionEvent.ACTION_MOVE:
//			if (!canDrag) {
//				return true;
//			} 
		case MotionEvent.ACTION_UP:
//			if (!canDrag) {
//				super.onTouchEvent(motionEvent);
//				return false;
//			} 
			// break;
		}
		return super.onTouchEvent(motionEvent);
	}

	/**
	 * 设置 seekbar 禁止点击
	 * */
	public void setPreventTapping(boolean paramBoolean) {
		this.mPreventTapping = paramBoolean;
	}

	@Override
	public void setThumb(Drawable paramDrawable) {
		super.setThumb(paramDrawable);
		this.mDrawable = paramDrawable;
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mDrawable = null;
	}
}