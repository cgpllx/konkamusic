package com.konka.music.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class TouchableRelativeLayout extends RelativeLayout
{
  private boolean a = true;

  public TouchableRelativeLayout(Context paramContext)
  {
    super(paramContext);
  }

  public TouchableRelativeLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public TouchableRelativeLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  @Override
public boolean dispatchTouchEvent(MotionEvent paramMotionEvent)
  {
    if (!this.a) return false;
    return super.dispatchTouchEvent(paramMotionEvent);
  }

  public void setTouchable(boolean paramBoolean)
  {
	  if(this.a != paramBoolean)
		  this.a = paramBoolean;
	  
	  setFocusable(this.a);
	  setFocusableInTouchMode(this.a);
	  
//    if (this.a != paramBoolean)
//    {
//      this.a = paramBoolean;
//      if (!this.a)
//        break label31;
//      setFocusable(true);
//      setFocusableInTouchMode(true);
//    }
//    while (true)
//    {
//      return;
//      label31: setFocusable(false);
//      setFocusableInTouchMode(false);
//    }
  }
}