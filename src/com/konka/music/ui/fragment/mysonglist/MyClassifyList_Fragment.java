package com.konka.music.ui.fragment.mysonglist;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.konka.music.R;
import com.konka.music.adapter.ArrayAdapter;
import com.konka.music.loader.MyClassifyListLoader;
import com.konka.music.pojo.MyClassifyList;
import com.konka.music.pojo.MyClassifyName;
import com.konka.music.ui.fragment.dialogfragment.AddClassifyDialogFragment;
import com.konka.music.ui.fragment.dialogfragment.AddClassifyDialogFragment.PositiveButtonOnClickListener;
import com.konka.music.util.ArrayUtils;
import com.konka.music.util.Assist;
import com.konka.music.util.ToastUtil;
import com.konka.music.util.ViewUtility;
import com.konka.music.wedget.MusicApplication;
import com.kubeiwu.baseclass.loader.BaseLoaderCallbacksFragment;

public class MyClassifyList_Fragment extends BaseLoaderCallbacksFragment<ArrayList<MyClassifyList>> implements OnClickListener, PositiveButtonOnClickListener {

	private View rootView;
	private TextView count_text_view;

	@Override
	public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.mysonglist_fragment_layout, container, false);
		} else {
			ViewGroup group = (ViewGroup) rootView.getParent();
			if (group != null) {
				group.removeView(group);
			}
		}
		return rootView;
	}

	private ListView mysonglist_fragment_listview;
	private ClassifyListAdapter classifyListAdapter;

	@Override
	public void onViewCreated(View view,  Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mysonglist_fragment_listview = ViewUtility.findViewById(view, R.id.mysonglist_fragment_listview);
		count_text_view = ViewUtility.findViewById(view, R.id.count_text_view);

		ViewUtility.findViewById(view, R.id.add_playlist_item, this);
		classifyListAdapter = new ClassifyListAdapter();
		mysonglist_fragment_listview.setAdapter(classifyListAdapter);
		getLoaderManager().initLoader(Assist.CLASSIFYLIST_LOADER_ID, getArguments(), this);
	}

	@Override
	public Loader<ArrayList<MyClassifyList>> onCreateLoader(int arg0, Bundle arg1) {
		return new MyClassifyListLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<MyClassifyList>> arg0, ArrayList<MyClassifyList> arg1) {
		super.onLoadFinished(arg0, arg1);
		if (!ArrayUtils.isEmpty(arg1)) {
			classifyListAdapter.setmItems(arg1);
			count_text_view.setText(getString(R.string.classify_count, arg1.size()));
		} else {
			// 没有数据
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_playlist_item:
			AddClassifyDialogFragment.newInstance().setPositiveButtonOnClickListener(this).show(getChildFragmentManager(), "");
			break;

		}
	}

	class ClassifyListAdapter extends ArrayAdapter<MyClassifyList> {
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null || convertView.getTag() == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.music_zone_list_child_item, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.playlist_title = ViewUtility.findViewById(convertView, R.id.playlist_title);
				viewHolder.playlist_song_number = ViewUtility.findViewById(convertView, R.id.playlist_song_number);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			MyClassifyList myClassifyList = getItem(position);
			viewHolder.playlist_title.setText(myClassifyList.getMySongListName().getClassify_name());
			viewHolder.playlist_song_number.setText(getString(R.string.numofsongs, myClassifyList.getmMusicInfo().size()));
			// viewHolder.playlist_song_number.setText(""+myClassifyList.getmMusicInfo().size());
			return convertView;
		}

		class ViewHolder {
			TextView playlist_title;
			TextView playlist_song_number;
		}
	}

	@Override
	public void positiveOnClick(String classifyname) {
		if (!TextUtils.isEmpty(classifyname)) {
			MyClassifyName myClassifyName = new MyClassifyName(classifyname);
			ArrayList<MyClassifyName> lists = MusicApplication.mKCommonToolDb.findAllByWhere(MyClassifyName.class, "classify_name = '" + classifyname + "'");
			if (!ArrayUtils.isEmpty(lists)) {
				ToastUtil.showToast(getActivity(), "分类名称已经存在");
			} else {
				MusicApplication.mKCommonToolDb.insert(myClassifyName);
				getLoaderManager().restartLoader(Assist.CLASSIFYLIST_LOADER_ID, getArguments(), this);
			}
		} else {
			ToastUtil.showToast(getActivity(), "请输入有效的名称");
		}
	}
}
