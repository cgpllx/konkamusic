package com.konka.music.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class AnimatableImageButton extends ImageButton
{
	public AnimatableImageButton(Context paramContext)
	{
		super(paramContext);
	}

	public AnimatableImageButton(Context paramContext, AttributeSet paramAttributeSet)
	{
		super(paramContext, paramAttributeSet);
	}

	public AnimatableImageButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
	{
		super(paramContext, paramAttributeSet, paramInt);
	}

	public int getScrollHeight()
	{
		return ((RelativeLayout.LayoutParams)getLayoutParams()).bottomMargin;
	}

	public void setScrollHeight(int paramInt)
	{
		RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams)getLayoutParams();
		localLayoutParams.bottomMargin = paramInt;
		setLayoutParams(localLayoutParams);
		requestLayout();
	}
}