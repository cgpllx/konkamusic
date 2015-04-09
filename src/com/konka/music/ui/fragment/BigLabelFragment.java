package com.konka.music.ui.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.konka.music.loader.SmallLabelInfoLoader;
import com.konka.music.pojo.SmallLabelInfo;

/**
 * 榜单和小标签
 * 
 * @author Administrator
 * 
 */
public class BigLabelFragment extends BigLabelFragment_Abstrct {

	private static final String BIGLABELID_KEY = "BIGLABELID";

	public static BigLabelFragment newInstance(int bigLabelId) {
		BigLabelFragment bigLabelFragment = new BigLabelFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(BIGLABELID_KEY, bigLabelId);
		bigLabelFragment.setArguments(bundle);
		return bigLabelFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
		String title = "";
		switch (getArguments().getInt(BIGLABELID_KEY)) {
		case 1:
			title = "热门歌曲";
			break;
		case 9:
			title = "榜单";
			break;
		default:
			title = "乐库";
			break;
		}
		getActivity().getActionBar().setTitle(title);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public Loader<ArrayList<SmallLabelInfo>> onCreateLoader(int arg0, Bundle arg1) {
		super.onCreateLoader(arg0, arg1);
		return new SmallLabelInfoLoader(getActivity(), getArguments().getInt(BIGLABELID_KEY, 1));
	}

}
