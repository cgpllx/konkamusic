package com.konka.music.loader;

import java.util.ArrayList;

import android.content.Context;

import com.konka.music.pojo.MusicInfo;
import com.konka.music.pojo.MyClassifyList;
import com.konka.music.pojo.MyClassifyName;
import com.konka.music.util.ArrayUtils;
import com.konka.music.wedget.MusicApplication;
import com.kubeiwu.baseclass.loader.BaseLoader;

public class MyClassifyListLoader extends BaseLoader<ArrayList<MyClassifyList>> {

	public MyClassifyListLoader(Context context) {
		super(context);
	}

	@Override
	public ArrayList<MyClassifyList> loadInBackground() {
		ArrayList<MyClassifyList> mMyClassifyLists = new ArrayList<MyClassifyList>();

		ArrayList<MyClassifyName> mMyClassifyListName = MusicApplication.mKCommonToolDb.findAll(MyClassifyName.class);
		if (!ArrayUtils.isEmpty(mMyClassifyListName)) {
			for (MyClassifyName myClassifyName : mMyClassifyListName) {

				ArrayList<MusicInfo> mMusicInfos = MusicApplication.mKCommonToolDb.findAllByWhere(MusicInfo.class, "myClassify_id = " + myClassifyName.getId());

				if (ArrayUtils.isEmpty(mMusicInfos)) {
					mMusicInfos = new ArrayList<MusicInfo>();
				}
				MyClassifyList mySongList = new MyClassifyList(mMusicInfos, myClassifyName);
				mMyClassifyLists.add(mySongList);
			}
		}
		// {// 测试
		// ArrayList<MusicInfo> mMusicInfos = new ArrayList<>();
		// mMyClassifyLists.add(new MyClassifyList(mMusicInfos, new MyClassifyName()));
		// mMyClassifyLists.add(new MyClassifyList(mMusicInfos, new MyClassifyName()));
		// mMyClassifyLists.add(new MyClassifyList(mMusicInfos, new MyClassifyName()));
		// }
		return mMyClassifyLists;
	}
}
