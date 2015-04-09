package com.konka.music.listener;

import java.util.ArrayList;

import com.konka.music.pojo.MusicInfo;

public interface ViewTagClickListen {
	void onViewTagClick(String viewTag,ArrayList<MusicInfo> musicInfos);
}
