package com.konka.music.ui.fragment.singer;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.konka.music.R;
import com.konka.music.loader.HotSingerLoader;
import com.konka.music.pojo.Singer;
import com.konka.music.util.ArrayUtils;
import com.konka.music.util.Assist;
import com.konka.music.util.ViewUtility;

public class Singer_Sex_Fragment extends SingerBaseFragment implements OnClickListener {
	private View rootView;
	public static final String AREAKEY = "areaKey";
	public static final String CATEGORYKEY = "categoryKey";

	@Override
	public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.singerchilefragment_layout, container, false);
		} else {
			ViewGroup viewGroup = (ViewGroup) rootView.getParent();
			if (viewGroup != null) {
				viewGroup.removeView(rootView);
			}
		}
		return rootView;
	}

	public static Singer_Sex_Fragment newInstance(String area, String category) {
		Singer_Sex_Fragment singer_Sex_Fragment = new Singer_Sex_Fragment();
		Bundle bundle = new Bundle();

		bundle.putString(AREAKEY, area);
		bundle.putString(CATEGORYKEY, category);
		singer_Sex_Fragment.setArguments(bundle);

		return singer_Sex_Fragment;
	}

	class SingerChileHolder {
		ListView singerchile_fragment_listview_id;
		SingerChileAdapter singerChileAdapter;
		View empty_loading;
		View refresh_layout;
		Button btn_refresh;
	}

	private SingerChileHolder singerChileHolder;

	@Override
	public void onViewCreated(View view,  Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (rootView.getTag() == null) {
			singerChileHolder = new SingerChileHolder();
			singerChileHolder.singerChileAdapter = new SingerChileAdapter();
			singerChileHolder.singerchile_fragment_listview_id = ViewUtility.findViewById(view, R.id.singerchile_fragment_listview_id);
			singerChileHolder.empty_loading = ViewUtility.findViewById(view, R.id.empty_loading);
			singerChileHolder.refresh_layout = ViewUtility.findViewById(view, R.id.refresh_layout);
			singerChileHolder.btn_refresh = ViewUtility.findViewById(view, R.id.btn_refresh, this);
			singerChileHolder.singerchile_fragment_listview_id.setOnItemClickListener(this);
			singerChileHolder.singerchile_fragment_listview_id.setEmptyView(singerChileHolder.empty_loading);
			singerChileHolder.singerchile_fragment_listview_id.setAdapter(singerChileHolder.singerChileAdapter);

			rootView.setTag(singerChileHolder);
		} else {
			singerChileHolder = (SingerChileHolder) rootView.getTag();
		}

		getLoaderManager().initLoader(Assist.SINGERSEX_LOADER_ID, getArguments(), this);
	}

	@Override
	public Loader<ArrayList<Singer>> onCreateLoader(int arg0, Bundle arg1) {
		singerChileHolder.refresh_layout.setVisibility(View.GONE);
		String area = arg1.getString(AREAKEY, "cn");
		String category = arg1.getString(CATEGORYKEY, "male");
		return new HotSingerLoader(getActivity(), String.format(Assist.SINGER_URL, area, category));
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<Singer>> arg0, ArrayList<Singer> arg1) {
		super.onLoadFinished(arg0, arg1);
		if (!ArrayUtils.isEmpty(arg1)) {
			singerChileHolder.singerChileAdapter.addAll(arg1);
		} else {
			singerChileHolder.refresh_layout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected ArrayList<Singer> getSingers() {
		return singerChileHolder.singerChileAdapter.getAll();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_refresh:
			getLoaderManager().restartLoader(Assist.SINGERSEX_LOADER_ID, getArguments(), this);
			break;
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		getLoaderManager().destroyLoader(Assist.SINGERSEX_LOADER_ID);
	}

	@Override
	protected int getHeaderViewCount() {
		return singerChileHolder.singerchile_fragment_listview_id.getHeaderViewsCount();
	}

}
