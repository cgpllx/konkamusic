package com.konka.music.ui.fragment.local;

import java.util.ArrayList;

import android.os.Bundle;
import android.provider.MediaStore.Audio.Media;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.content.Loader;
import android.util.Log;

import com.konka.music.loader.MusicRetrieveLoader;
import com.konka.music.pojo.MusicInfo;
import com.konka.music.ui.fragment.abstractfragment.KBasePlayListFragment;

public class LocalFragment extends KBasePlayListFragment {
	private String mSortOrder = Media.TITLE_KEY;
	public static final String TAG = LocalFragment.class.getName();

	@Override
	public Loader<ArrayList<MusicInfo>> onCreateLoader(int arg0, Bundle arg1) {
		super.onCreateLoader(arg0, arg1);
		Log.i(TAG, "onCreateLoader");
		StringBuffer select = new StringBuffer(" 1=1 ");
		select.append(" and " + MediaColumns.SIZE + " > " + 1024 * 8);
		select.append(" and " + MediaColumns.DATA + " not like " + "'%.amr'");
		MusicRetrieveLoader loader = new MusicRetrieveLoader(getActivity(), select.toString(), null, mSortOrder);
		return loader;
	}
	// @Override
	// public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	// super.onCreateContextMenu(menu, v, menuInfo);
	// menu.add(0, CONTEXT_MENU_DELETE, Menu.NONE, "删除");
	// }
}
