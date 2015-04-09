package com.konka.music.ui.fragment.local;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.konka.music.loader.MusicLoader;
import com.konka.music.loader.MusicLoader.LoaderType;
import com.konka.music.pojo.MusicInfo;
import com.konka.music.ui.fragment.abstractfragment.KBasePlayListFragment;

public class FavouriteFragment extends KBasePlayListFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		getActivity().getActionBar().setTitle("我喜欢");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public Loader<ArrayList<MusicInfo>> onCreateLoader(int arg0, Bundle arg1) {
		super.onCreateLoader(arg0, arg1);
		return new MusicLoader(getActivity(), LoaderType.FAVOURITE);
	}

}
