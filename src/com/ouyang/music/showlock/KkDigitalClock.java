package com.ouyang.music.showlock;

import java.util.Calendar;

import android.content.Context;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.DigitalClock;

@SuppressWarnings("deprecation")
public class KkDigitalClock extends DigitalClock{
	
	Calendar mCalendar;
//    private final static String m12 = "h:mm aa";//h:mm:ss aa
    private final static String m12 = "hh:mm";//h:mm:ss 不显示上午下午
    private final static String m24 = "k:mm";//k:mm:ss
    private FormatChangeObserver mFormatChangeObserver;

    private Runnable mTicker;
    private Handler mHandler;
    
    private boolean mTickerStopped = false;
    String mFormat;

	public KkDigitalClock(Context context) {
		super(context);
		 initClock(context);
		// TODO Auto-generated constructor stub
	}

	
	public KkDigitalClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        initClock(context);
    }
	
	private void initClock(Context context) {
        Resources r = context.getResources();

        if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
        }

        mFormatChangeObserver = new FormatChangeObserver();
        getContext().getContentResolver().registerContentObserver(
                Settings.System.CONTENT_URI, true, mFormatChangeObserver);

        setFormat();
    }
	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
//		super.onAttachedToWindow();
		 mTickerStopped = false;
	        super.onAttachedToWindow();
	        mHandler = new Handler();

	        /**
	         * requests a tick on the next hard-second boundary
	         */
	        mTicker = new Runnable() {
	                @Override
					public void run() {
	                    if (mTickerStopped) return;
	                    mCalendar.setTimeInMillis(System.currentTimeMillis());
	                    setText(DateFormat.format(mFormat, mCalendar));
	                    invalidate();
	                    long now = SystemClock.uptimeMillis();
	                    long next = now + (1000 - now % 1000);
	                    mHandler.postAtTime(mTicker, next);
	                }
	            };
	        mTicker.run();
	}
	
	@Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTickerStopped = true;
    }
	
	 private boolean get24HourMode() {
	        return android.text.format.DateFormat.is24HourFormat(getContext());
	    }

	    private void setFormat() {
	        if (get24HourMode()) {
	            mFormat = m24;
	        } else {
	            mFormat = m12;
//	            mFormat = m24;
	        }
	    }

	    private class FormatChangeObserver extends ContentObserver {
	        public FormatChangeObserver() {
	            super(new Handler());
	        }

	        @Override
	        public void onChange(boolean selfChange) {
	            setFormat();
	        }
	    }
	

}
