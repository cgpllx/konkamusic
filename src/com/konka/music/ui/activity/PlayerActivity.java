package com.konka.music.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.konka.music.pojo.MusicInfo;
import com.konka.music.service.MusicInfoManager;
import com.konka.music.ui.fragment.MusicPlayFragment;
import com.konka.music.util.MusicInfoUtil;
import com.konka.music.util.ObjectUtil;

public class PlayerActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		Uri uri = getIntent().getData();
		if (!ObjectUtil.isEmpty(uri)) {
			try {
				MusicInfo musicInfo = MusicInfoUtil.parseFile2MusicInfo(uri.getPath());
				MusicInfoManager.addMusic2PlayList(this, musicInfo, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment fragment = getFragment(this, MusicPlayFragment.class, null);
		ft.add(android.R.id.content, fragment, MusicPlayFragment.TAG);
		ft.commit();
	}

}
