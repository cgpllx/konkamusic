package com.konka.music.ui.fragment.singer;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.content.Loader;

import com.konka.music.loader.SmallLablePlayListLoader;
import com.konka.music.pojo.MusicInfo;
import com.konka.music.ui.fragment.abstractfragment.KBasePlayListFragment;
import com.konka.music.util.Assist;

public class SingerMusicFragment extends KBasePlayListFragment {
	public static final String SINGER_ID_KEY = "singer_id";

	public static SingerMusicFragment newInstance(int singer_id) {
		SingerMusicFragment singerMusicFragment = new SingerMusicFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(SINGER_ID_KEY, singer_id);
		singerMusicFragment.setArguments(bundle);
		return singerMusicFragment;
	}

	@Override
	public Loader<ArrayList<MusicInfo>> onCreateLoader(int arg0, Bundle arg1) {
		super.onCreateLoader(arg0, arg1);

		int id = arg1.getInt(SINGER_ID_KEY, 0);

		return new SmallLablePlayListLoader(getActivity(), String.format(Assist.SINGERSMUSIC, id));
	}
}
