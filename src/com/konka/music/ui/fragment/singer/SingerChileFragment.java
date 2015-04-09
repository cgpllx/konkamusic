package com.konka.music.ui.fragment.singer;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.konka.music.R;
import com.konka.music.loader.HotSingerLoader;
import com.konka.music.pojo.Singer;
import com.konka.music.util.ApiCompatibleUtil;
import com.konka.music.util.ArrayUtils;
import com.konka.music.util.Assist;
import com.konka.music.util.FragmentManagerUtil;
import com.konka.music.util.ViewUtility;

public class SingerChileFragment extends SingerBaseFragment implements OnClickListener {
	private View rootView;

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

	class SingerChileHolder {
		ListView singerchile_fragment_listview;
		SingerChileAdapter singerChileAdapter;

		View singer_type_Holder_man;
		View singer_type_Holder_woman;
		View singer_type_Holder_group;

		View refresh_layout;
	}

	private SingerChileHolder singerChileHolder;
	View headerView ;
	@Override
	public void onViewCreated(View view,  Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (rootView.getTag() == null) {
			singerChileHolder = new SingerChileHolder();
			singerChileHolder.singerChileAdapter = new SingerChileAdapter();
			singerChileHolder.singerchile_fragment_listview = ViewUtility.findViewById(view, R.id.singerchile_fragment_listview_id);
			singerChileHolder.refresh_layout = ViewUtility.findViewById(view, R.id.refresh_layout);
			ViewUtility.findViewById(singerChileHolder.refresh_layout, R.id.btn_refresh, new OnClickListener() {
				@Override
				public void onClick(View v) {
					restartLoader();
				}
			});
			singerChileHolder.singerchile_fragment_listview.setEmptyView(ViewUtility.findViewById(view, R.id.empty_loading));
			  headerView = View.inflate(getActivity(), R.layout.singer_type_sex_select_layout, null);
			
			singerChileHolder.singerchile_fragment_listview.addHeaderView(headerView);
			singerChileHolder.singerchile_fragment_listview.setOnItemClickListener(this);
			headerView.setEnabled(false);
			singerChileHolder.singer_type_Holder_man = ViewUtility.findViewById(headerView, R.id.singer_type_man, this);
			singerChileHolder.singer_type_Holder_woman = ViewUtility.findViewById(headerView, R.id.singer_type_woman, this);
			singerChileHolder.singer_type_Holder_group = ViewUtility.findViewById(headerView, R.id.singer_type_group, this);

			initsinger_type_sex(singerChileHolder.singer_type_Holder_man);
			initsinger_type_sex(singerChileHolder.singer_type_Holder_woman);
			initsinger_type_sex(singerChileHolder.singer_type_Holder_group);

			singerChileHolder.singerchile_fragment_listview.setAdapter(singerChileHolder.singerChileAdapter);

			rootView.setTag(singerChileHolder);
		} else {
			singerChileHolder = (SingerChileHolder) rootView.getTag();
		}

		initLoader();
	}

	private void initLoader() {
		getLoaderManager().initLoader(Assist.SINGERCHILE_LOADER_ID, getArguments(), this);
	};

	private void restartLoader() {
		getLoaderManager().restartLoader(Assist.SINGERCHILE_LOADER_ID, getArguments(), SingerChileFragment.this);
	};

	private void initsinger_type_sex(View type_Holder) {

		View bg = ViewUtility.findViewById(type_Holder, R.id.singer_type_sex_bg);
		TextView textview = ViewUtility.findViewById(type_Holder, R.id.singer_type_sex_text);
		Drawable start = null;
		switch (type_Holder.getId()) {
		case R.id.singer_type_man:
			textview.setText("男");
			start = getResources().getDrawable(R.drawable.singer_type_man_img);
			bg.setBackgroundResource(R.drawable.singer_type_man_bg);
			break;
		case R.id.singer_type_woman:
			textview.setText("女");
			start = getResources().getDrawable(R.drawable.singer_type_woman_img);
			bg.setBackgroundResource(R.drawable.singer_type_woman_bg);
			break;
		case R.id.singer_type_group:
			textview.setText("组合");
			start = getResources().getDrawable(R.drawable.singer_type_group_img);
			bg.setBackgroundResource(R.drawable.singer_type_group_bg);
			break;
		}
		if (start != null) {
			ApiCompatibleUtil.setTextViewCompoundDrawables(textview, start, null, null, null);
		}
	}

	@Override
	public Loader<ArrayList<Singer>> onCreateLoader(int arg0, Bundle arg1) {// area
		singerChileHolder.refresh_layout.setVisibility(View.GONE);
		return new HotSingerLoader(getActivity(), String.format(Assist.HOTSINGER_URL, arg1.getString("area", "cn")));
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<Singer>> arg0, ArrayList<Singer> arg1) {
		super.onLoadFinished(arg0, arg1);
		if (!ArrayUtils.isEmpty(arg1) && singerChileHolder.singerChileAdapter.getCount() == 0) {
			singerChileHolder.singerChileAdapter.addAll(arg1);
		} else {
			if (singerChileHolder.singerChileAdapter.getCount() == 0) {
				singerChileHolder.refresh_layout.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void onClick(View v) {
		String area = getArguments().getString("area", "cn");
		String category = "group";
		switch (v.getId()) {
		case R.id.singer_type_man:
			category = "male";
			break;
		case R.id.singer_type_woman:
			category = "female";
			break;
		case R.id.singer_type_group:
			category = "group";
			break;
		}
		FragmentManagerUtil.swichFragment(getActivity().getSupportFragmentManager(), Singer_Sex_Fragment.newInstance(area, category));
	}

	@Override
	protected ArrayList<Singer> getSingers() {
		return singerChileHolder.singerChileAdapter.getAll();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		getLoaderManager().destroyLoader(Assist.SINGERCHILE_LOADER_ID);
	}

	@Override
	protected int getHeaderViewCount() {
		return singerChileHolder.singerchile_fragment_listview.getHeaderViewsCount();
	}
}
