package com.konka.music.ui.fragment.local;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.konka.music.loader.MusicLoader;
import com.konka.music.loader.MusicLoader.LoaderType;
import com.konka.music.pojo.MusicInfo;
import com.konka.music.ui.fragment.abstractfragment.KBasePlayListFragment;
import com.konka.music.ui.fragment.dialogfragment.ClearHistoryDialogFragment;
import com.konka.music.util.MusicInfoUtil;

public class HistoryFragment extends KBasePlayListFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		getActivity().getActionBar().setTitle("播放记录");
		setHasOptionsMenu(true);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public Loader<ArrayList<MusicInfo>> onCreateLoader(int arg0, Bundle arg1) {
		super.onCreateLoader(arg0, arg1);
		return new MusicLoader(getActivity(), LoaderType.HISTORY);
	}

	public static final int CLEARHISTORY = 0x000001;

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.add(0, 1, 1, "清空记录");
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case CLEARHISTORY:
			ClearHistoryDialogFragment.newInstance().setPositiveButtonOnClickListener(new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					MusicInfoUtil.clearAllHistory();
					Loader<Object> loader = getLoaderManager().getLoader(MUSIC_RETRIEVE_LOADER);
					if (loader != null) {
						loader.onContentChanged();
					}
				}
			}).show(getChildFragmentManager(), TAG);
			break;
		}

		return true;
	}
}
