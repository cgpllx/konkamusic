package com.konka.music.ui.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.konka.music.R;

public class PlayerLyricToggleImageButton extends AnimatableImageButton
{
	public PlayerLyricToggleImageButton(Context paramContext, AttributeSet paramAttributeSet)
	{
		this(paramContext, paramAttributeSet, 0);
	}

	public PlayerLyricToggleImageButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
	{
		super(paramContext, paramAttributeSet, paramInt);
//		if (!i.a().aw())
			setImageResource(R.drawable.player_lyric_expand_flash_anim);
//		else
//			setImageResource(R.drawable.ic_player_lyric_expand);
	}

	public void a()
	{
		if ((getWidth() > 0) && (getVisibility() == 0))
		{
			Drawable localDrawable = getDrawable();
			if ((localDrawable != null) && ((localDrawable instanceof AnimationDrawable)) && (!((AnimationDrawable)localDrawable).isRunning()))
				((AnimationDrawable)localDrawable).start();
		}
	}

	public void b()
	{
		Drawable localDrawable = getDrawable();
		if ((localDrawable != null) && ((localDrawable instanceof AnimationDrawable)) && (((AnimationDrawable)localDrawable).isRunning()))
			((AnimationDrawable)localDrawable).stop();
	}

	@Override
	protected void onAttachedToWindow()
	{
		super.onAttachedToWindow();
		a();
	}

	@Override
	protected void onDetachedFromWindow()
	{
		super.onDetachedFromWindow();
		b();
	}

	@Override
	protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
	{
		super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
		if (paramInt1 > 0)
			a();
	}

	@Override
	protected void onVisibilityChanged(View paramView, int paramInt)
	{
		super.onVisibilityChanged(paramView, paramInt);
		if ((paramInt == 4) || (paramInt == 8))
			b();
		a();
	}
}