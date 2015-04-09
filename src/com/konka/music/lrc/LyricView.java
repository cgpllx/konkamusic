package com.konka.music.lrc;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.konka.music.R;
import com.konka.music.util.ArrayUtils;

public class LyricView extends View {
	private final static String TAG = LyricView.class.getSimpleName();
	private Paint paint = new Paint();// 画笔，用于画不是高亮的歌词
	private Paint paintHL = new Paint(); // 画笔，用于画高亮的歌词，即当前唱到这句歌词
	private int SIZEWORD = 25;// 显示歌词文字的大小值
	private int SIZEWORDHL = 30;// 显示歌词文字的大小值
	public static final int INTERVAL = 40;// 歌词每行的间隔
	public int playIndex = 0;

	public LyricView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public LyricView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public LyricView(Context context) {
		super(context);
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
			for (int i = playIndex; i < lyricObjects.size(); i++) {
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
			for (int i = 0; i < lyricObjects.size(); i++) {
				canvas.drawText(lyricObjects.get(i).getLrc(), this.getWidth() >> 1, //
						INTERVAL * (i - playIndex) + (this.getHeight() >> 1), i == playIndex ? paintHL : paint);
			}
		} else {
			// canvas.drawText("没有找到歌词", this.getWidth() >> 1, this.getHeight() >> 1, paint);
		}
	}

	public void setLyricObjects(List<LyricObject> lyricObjects) {
		this.lyricObjects = lyricObjects;
		playIndex = 0;
		postInvalidate();
	}

	public List<LyricObject> getLyricObjects() {
		return lyricObjects;
	}
	private List<LyricObject> lyricObjects = null;
}
