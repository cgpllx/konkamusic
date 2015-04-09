package com.konka.music.newlrc;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.konka.music.R;
import com.konka.music.lrc.LyricObject;
import com.konka.music.util.ArrayUtils;
 

public class LyricView extends View {
	private final static String TAG = LyricView.class.getSimpleName();
	private Paint paint = new Paint();// 画笔，用于画不是高亮的歌词
	private Paint paintHL = new Paint(); // 画笔，用于画高亮的歌词，即当前唱到这句歌词
	private int SIZEWORD = 25;// 显示歌词文字的大小值
	private int SIZEWORDHL = 30;// 显示歌词文字的大小值
	public static final int INTERVAL = 40;// 歌词每行的间隔
	public int playIndex = 0;
	public int lastPlayIndex = 0;
	public int flagIndex = 0;
	private boolean isSliding = false;

	int mLastY = 0;
	int mCurrentY = 0;
	int currentProgress = 0;
	int mMaxProgress = 0;

	public ISetLrcProgress iSetProgress;

	public LyricView(Context context) {
		super(context, null);
	}

	public LyricView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
	}

	public LyricView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public void init() {

		paint = new Paint();
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setColor(getResources().getColor(R.color.lyrics));
		paint.setTextSize(SIZEWORD);
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setAlpha(180);

		paintHL = new Paint();
		paintHL.setTextAlign(Paint.Align.CENTER);

		paintHL.setColor(getResources().getColor(R.color.lyrics_hl));
		paintHL.setTextSize(SIZEWORDHL);
		paintHL.setAntiAlias(true);
		paintHL.setAlpha(255);

	}

	/**
	 * 讲歌词已经播放的时间传入这里就可以实现歌词滚动了
	 * 
	 * @param currentTimeMillis
	 */
	public void setCurPosition(int currentTimeMillis) {
		if (!ArrayUtils.isEmpty(lyricObjects)) {
			if (playIndex >= lyricObjects.size())
				return;
			if (isSliding == true)
				return;
			// for (int i = playIndex; i < lyricObjects.size(); i++) {
			mCurrentY = 0;
			// currentProgress=currentTimeMillis;
			for (int i = 0; i < lyricObjects.size(); i++) {
				if (currentTimeMillis >= lyricObjects.get(i).getBegintime()) {
					if (i + 1 < lyricObjects.size()) {
						if (currentTimeMillis <= lyricObjects.get(i + 1).getBegintime()) {
							playIndex = i;
							postInvalidate();
							return;
						}
					} else {
						playIndex = i;
						postInvalidate();
						return;
					}
				}
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (!ArrayUtils.isEmpty(lyricObjects)) {

			if (playIndex == lyricObjects.size() - 1 && mCurrentY < 0)
				mCurrentY = 0;
			if (playIndex == 0 && mCurrentY > 0)
				mCurrentY = 0;

			for (int i = 0; i < lyricObjects.size(); i++) {
				int heigth = INTERVAL * (i - playIndex) + (this.getHeight() >> 1) + mCurrentY;
				if (i == 0 && heigth > (this.getHeight() >> 1))// 歌词超过上界
					playIndex = 0;
				if (i == lyricObjects.size() - 1 && heigth < (this.getHeight() >> 1))// 歌词超过下界
					playIndex = lyricObjects.size() - 1;
				if (Math.abs(INTERVAL * (i - playIndex) + mCurrentY) < 20)// 表示到达中间
				{
					canvas.drawText(lyricObjects.get(i).getLrc(), this.getWidth() >> 1, INTERVAL * (i - playIndex) + (this.getHeight() >> 1) + mCurrentY, paintHL);
					flagIndex = i;// 用以记录到达中间的index
					Log.d("Ouyang", "flagIndex:" + flagIndex);
				} else {
					canvas.drawText(lyricObjects.get(i).getLrc(), this.getWidth() >> 1, INTERVAL * (i - playIndex) + (this.getHeight() >> 1) + mCurrentY, paint);
				}

				// canvas.drawText(
				// lyricObjects.get(i).getLrc(),
				// this.getWidth() >> 1, //
				// INTERVAL * (i - playIndex) + (this.getHeight() >>
				// 1)+mCurrentY,
				// i == playIndex ? paintHL : paint);
			}
		} else {
			canvas.drawText("没有找到歌词", this.getWidth() >> 1, this.getHeight() >> 1, paint);
		}
	}

	public void setLyricObjects(List<LyricObject> lyricObjects) {
		this.lyricObjects = lyricObjects;
		playIndex = 0;
		setProgressMax();
		postInvalidate();
	}

	public List<LyricObject> getLyricObjects() {
		return lyricObjects;
	}

	private List<LyricObject> lyricObjects = null;

	private void setProgressMax() {
		if (lyricObjects != null && lyricObjects.size() != 0) {
			mMaxProgress = lyricObjects.get(lyricObjects.size() - 1).getBegintime();
			Log.d("Ouyang", "mMaxProgress:" + mMaxProgress);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (MotionEvent.ACTION_MOVE == event.getAction()) {
			mCurrentY = (int) event.getY() - mLastY;
			Log.d("Ouyang", "当前playIndex" + playIndex);
			// mLastY = (int) event.getY();
			postInvalidate();
			// int distance= Math.abs(mCurrentY);
			// if (Math.abs(mCurrentY) >= 1) {
			// if (mCurrentY > 0) {
			// if (currentProgress > 0)
			// // currentProgress -= mMaxProgress/this.getHeight()*distance;
			// if (playIndex >= 1)
			// if (lastPlayIndex - playIndex <= 1)
			// currentProgress = currentProgress- distance*
			// (lyricObjects.get(playIndex).getBegintime() -
			// lyricObjects.get(playIndex - 1).getBegintime()) / INTERVAL;
			// else if (lastPlayIndex - playIndex > 1)
			// currentProgress =
			// currentProgress-distance*(lyricObjects.get(lastPlayIndex).getBegintime()-lyricObjects.get(playIndex).getBegintime())/(INTERVAL*(lastPlayIndex-playIndex));
			// } else {
			// if (currentProgress < mMaxProgress)
			// // currentProgress += mMaxProgress/this.getHeight()*distance;
			// if(playIndex<=lyricObjects.size()-2)
			// if(playIndex-lastPlayIndex<=1)
			// currentProgress=
			// currentProgress+distance*(lyricObjects.get(playIndex+1).getBegintime()-lyricObjects.get(playIndex).getBegintime())/INTERVAL;
			// else if(playIndex-lastPlayIndex>=1)
			// currentProgress=
			// currentProgress+distance*(lyricObjects.get(playIndex).getBegintime()-lyricObjects.get(lastPlayIndex).getBegintime())/(INTERVAL*(playIndex-lastPlayIndex));
			// }
			// setCurPosition(currentProgress);
			// }
		}

		if (MotionEvent.ACTION_UP == event.getAction()) {
			if (iSetProgress != null) {
				isSliding = false;
				int setCurrentProgress = lyricObjects.get(flagIndex).getBegintime();
				iSetProgress.setLrcProgress(setCurrentProgress);
			}
		}

		if (MotionEvent.ACTION_DOWN == event.getAction()) {
			Log.d("Ouyang", "MotionEvent.ACTION_DOWN");
			playIndex = flagIndex;
			mLastY = (int) event.getY();
			isSliding = true;
		}
		return true;
	}

	public void setLrcInterface(ISetLrcProgress iSetProgress) {
		this.iSetProgress = iSetProgress;
	}
}
