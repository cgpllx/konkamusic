package com.konka.music.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class KGImageView extends ImageView
{
	private int a = 0;

	public KGImageView(Context paramContext)
	{
		super(paramContext);
	}

	public KGImageView(Context paramContext, AttributeSet paramAttributeSet)
	{
		this(paramContext, paramAttributeSet, 0);
	}

	public KGImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
	{
		super(paramContext, paramAttributeSet, paramInt);
	}

	@Override
	protected void onDraw(Canvas paramCanvas)
	{
		Drawable localDrawable = getDrawable();
		if ((localDrawable != null) && ((localDrawable instanceof BitmapDrawable)))
		{
			Bitmap localBitmap = ((BitmapDrawable)localDrawable).getBitmap();
			if ((localBitmap != null) && (localBitmap.isRecycled()))
			{
				if (this.a == 0)
//					break label56;
					setImageDrawable(null);
				else
					setImageResource(this.a);
			}
		}else {
			super.onDraw(paramCanvas);
		}
//		while (true)
//		{
//			super.onDraw(paramCanvas);
//			return;
//			label56: setImageDrawable(null);
//		}
	}

	public void setDefaultImageResource(int paramInt)
	{
		this.a = paramInt;
	}

	@Override
	public void setImageBitmap(Bitmap paramBitmap)
	{
		super.setImageBitmap(paramBitmap);
	}

	@Override
	public void setImageResource(int paramInt)
	{
		super.setImageResource(paramInt);
	}
}