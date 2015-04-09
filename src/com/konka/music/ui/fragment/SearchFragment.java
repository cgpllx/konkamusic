package com.konka.music.ui.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.konka.music.R;
import com.konka.music.adapter.TrackAdapter;
import com.konka.music.loader.SearchMusicLoader;
import com.konka.music.pojo.MusicInfo;
import com.konka.music.ui.fragment.abstractfragment.KBaseListFragment_MusicInfoArray;
import com.konka.music.ui.view.ProgressLayout;
import com.konka.music.util.Assist;
import com.konka.music.util.ToastUtil;
import com.konka.music.util.ViewUtility;

public class SearchFragment extends KBaseListFragment_MusicInfoArray {

	private static final String ARGUMENT_SEARCH_KEY = "argument_search_key";

	public static SearchFragment newInstance(String searchKey) {
		SearchFragment searchFragment = new SearchFragment();
		Bundle bundle = new Bundle();
		bundle.putString(ARGUMENT_SEARCH_KEY, searchKey);
		searchFragment.setArguments(bundle);
		return searchFragment;
	}


	private SearchFragmentHolder searchFragmentHolder;

	class SearchFragmentHolder extends KBaseHolder {
		TextView count_text_view;
		AutoCompleteTextView search_edit;
		ProgressLayout progress_layout;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getActivity().getActionBar().setTitle("搜索");
		View view = inflater.inflate(R.layout.searchfragment_layout, container, false);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		searchFragmentHolder = new SearchFragmentHolder();
		searchFragmentHolder.kBaseListView = ViewUtility.findViewById(view, R.id.searchResult_listview);
		searchFragmentHolder.count_text_view = ViewUtility.findViewById(view, R.id.count_text_view);
		searchFragmentHolder.progress_layout = ViewUtility.findViewById(view, R.id.progress_layout);
		searchFragmentHolder.search_edit = ViewUtility.findViewById(view, R.id.search_edit);
		ViewUtility.findViewById(view, R.id.search_button, this);
		searchFragmentHolder.listViewAdapter = new TrackAdapter(getActivity(), this);
		searchFragmentHolder.kBaseListView.setAdapter(searchFragmentHolder.listViewAdapter);
		registerForContextMenu(searchFragmentHolder.kBaseListView);
		searchFragmentHolder.kBaseListView.setOnItemClickListener(this);
		if (getArguments() != null && getArguments().containsKey(ARGUMENT_SEARCH_KEY)) {
			String searchKey = getArguments().getString(ARGUMENT_SEARCH_KEY);
			if (!TextUtils.isEmpty(searchKey)) {
				searchFragmentHolder.search_edit.setText(searchKey);
				getLoaderManager().initLoader(Assist.SEARCH_LOADER_ID, getArguments(), this);
			}
		} else {
			ToastUtil.showToast(getActivity(), getString(R.string.search_edit_toast));
		}
	}

	@Override
	public Loader<ArrayList<MusicInfo>> onCreateLoader(int arg0, Bundle arg1) {
		String defaultValue = "%E5%B0%8F%E8%8B%B9%E6%9E%9C";
		String searchkey = arg1.getString(ARGUMENT_SEARCH_KEY, defaultValue);
		searchFragmentHolder.listViewAdapter.clear();
		searchFragmentHolder.progress_layout.setProgress(true);
		return new SearchMusicLoader(getActivity(), searchkey);
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<MusicInfo>> arg0, ArrayList<MusicInfo> arg1) {
		super.onLoadFinished(arg0, arg1);
		if (arg1 != null && arg1.size() > 0 && searchFragmentHolder.listViewAdapter.getCount() == 0) {
			searchFragmentHolder.listViewAdapter.addAll(arg1);
			searchFragmentHolder.count_text_view.setText(String.format(getString(R.string.search_result_count), arg1.size()));
		} else {
			if (searchFragmentHolder.listViewAdapter.getCount() == 0) {
				searchFragmentHolder.count_text_view.setText(String.format(getString(R.string.search_result_count), 0));
			}
		}
		searchFragmentHolder.progress_layout.setProgress(false);
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<MusicInfo>> arg0) {
		super.onLoaderReset(arg0);
		if (searchFragmentHolder.listViewAdapter != null) {
			searchFragmentHolder.listViewAdapter.clear();
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.search_button) {
			handleSearch();
		}
	}

	private void handleSearch() {
		Bundle bundle = new Bundle();
		Editable editable = searchFragmentHolder.search_edit.getText();
		String searchKey = editable.toString();
		if (!TextUtils.isEmpty(searchKey)) {
			bundle.putString(ARGUMENT_SEARCH_KEY, searchKey);
			getLoaderManager().restartLoader(Assist.SEARCH_LOADER_ID, bundle, this);
		} else {
			ToastUtil.showToast(getActivity(), getString(R.string.search_edit_toast));
		}

	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, CONTEXT_MENU_DOWNLOAD, Menu.NONE, "下载");
	}
	@Override
	protected KBaseHolder getKBaseHolder() {
		return searchFragmentHolder;
	}

}
