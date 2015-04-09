package com.konka.music.ui.fragment.abstractfragment;

import android.widget.SeekBar;

public abstract class KBaseFragment<T> extends AbstractKBaseFragment<T>  {
	public static final String TAG = KBaseFragment.class.getSimpleName();






	private int lastProgress = 0;
	private int newProgress = 0;

	@Override
	public void onProgressChanged(SeekBar paramSeekBar, int progress, boolean fromUser) {
		if (fromUser && progress > newProgress + 20 || progress < newProgress - 20) {
			// newProgress = lastProgress;
			paramSeekBar.setProgress(newProgress);
			return;
		}
		newProgress = progress;
	}

	 

}
