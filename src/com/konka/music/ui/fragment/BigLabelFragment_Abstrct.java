package com.konka.music.ui.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.konka.music.R;
import com.konka.music.adapter.ArrayAdapter;
import com.konka.music.pojo.SmallLabelInfo;
import com.konka.music.ui.view.ProgressLayout;
import com.konka.music.util.ArrayUtils;
import com.konka.music.util.Assist;
import com.konka.music.util.CreateViewUtil;
import com.konka.music.util.FragmentManagerUtil;
import com.konka.music.util.ViewUtility;
import com.kubeiwu.baseclass.loader.BaseLoaderCallbacksFragment;

/**
 * 榜单和小标签
 * 
 * @author Administrator
 * 
 */
public class BigLabelFragment_Abstrct extends BaseLoaderCallbacksFragment<ArrayList<SmallLabelInfo>> implements OnClickListener {
	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
		rootView = CreateViewUtil.onCreateView(inflater, container, savedInstanceState, rootView, R.layout.net_yuecool_gradview_layout);
		return rootView;
	}

	class BigLabelViewHolder {
		private GridView gridView;
		private BigLabelAdapter labelAdapter;
		private ProgressLayout progressLayout;
		View fail;
		Button btn_refresh;
	}

	BigLabelViewHolder bigLabelViewHolder;
	private boolean loaded = false;// 数据加载成功的标志
	private boolean viewinited = false;

	@Override
	public void onViewCreated(View view,  Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if (rootView.getTag() == null) {
			bigLabelViewHolder = new BigLabelViewHolder();
			bigLabelViewHolder.gridView = ViewUtility.findViewById(view, R.id.net_yuecool_gridview_id);
			bigLabelViewHolder.progressLayout = ViewUtility.findViewById(view, R.id.progress_layout);
			bigLabelViewHolder.labelAdapter = new BigLabelAdapter();
			bigLabelViewHolder.gridView.setAdapter(bigLabelViewHolder.labelAdapter);
			bigLabelViewHolder.gridView.setEmptyView(ViewUtility.findViewById(view, R.id.empty_loading));
			bigLabelViewHolder.fail = ViewUtility.findViewById(view, R.id.refresh_layout);
			bigLabelViewHolder.btn_refresh = ViewUtility.findViewById(bigLabelViewHolder.fail, R.id.btn_refresh, this);
		
			bigLabelViewHolder.gridView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//					FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
					SmallLabelInfo smallLabel = bigLabelViewHolder.labelAdapter.getItem(position);
					int smallLabelId = smallLabel.getId();
					String imageurl = smallLabel.getImageurl();
					String name = smallLabel.getSmallLabelName();
//					ft.replace(R.id.content, SmallLablePalyListFragment.newInstance(smallLabelId, imageurl, name));
//					ft.addToBackStack(null);
//					ft.commit();
					FragmentManagerUtil.swichFragment(getActivity().getSupportFragmentManager(), SmallLablePalyListFragment.newInstance(smallLabelId, imageurl, name));
				}
			});
			// getLoaderManager().initLoader(Assist.BIGLABEL_LOADER_ID, null, this);
			rootView.setTag(bigLabelViewHolder);
		} else {
			bigLabelViewHolder = (BigLabelViewHolder) rootView.getTag();
		}
		System.out.println("BigLabelFragment_Abstrct--onViewCreated");
		viewinited = true;
		lazyLoad();

	}

	@Override
	protected void lazyLoad() {
		super.lazyLoad();
		if (viewinited && isVisible ) {
			getLoaderManager().initLoader(Assist.BIGLABEL_LOADER_ID, null, this);
		}
	}
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		Assist.imageLoader.clearMemoryCache();
	}
	@Override
	public Loader<ArrayList<SmallLabelInfo>> onCreateLoader(int arg0, Bundle arg1) {
		bigLabelViewHolder.progressLayout.setPressed(true);
		return super.onCreateLoader(arg0, arg1);
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<SmallLabelInfo>> arg0, ArrayList<SmallLabelInfo> arg1) {
		super.onLoadFinished(arg0, arg1);
		if (!ArrayUtils.isEmpty(arg1)) {
			bigLabelViewHolder.progressLayout.setPressed(false);
			bigLabelViewHolder.labelAdapter.setmItems(arg1);
		} else {
			if (bigLabelViewHolder.labelAdapter.getCount() == 0) {
				bigLabelViewHolder.fail.setVisibility(View.VISIBLE);
			}
		}
	}

	/**
	 * 大标签里面放的是小标签
	 * 
	 */
	class BigLabelAdapter extends ArrayAdapter<SmallLabelInfo> {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null || convertView.getTag() == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.net_yuecool_gradview_layout_item, parent, false);
				viewHolder.label_id = ViewUtility.findViewById(convertView, R.id.label_id);
				viewHolder.lableImage_id = ViewUtility.findViewById(convertView, R.id.lableImage_id);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			SmallLabelInfo smallLabelInfo = getItem(position);
			viewHolder.label_id.setText(getItem(position).getSmallLabelName());
			Assist.imageLoader.displayImage(smallLabelInfo.getImageurl(), viewHolder.lableImage_id, Assist.options);
			return convertView;
		}

		class ViewHolder {
			TextView label_id;
			ImageView lableImage_id;
		}
	}

	@Override
	public void onClick(View v) {
		getLoaderManager().restartLoader(Assist.BIGLABEL_LOADER_ID, null, this);
	}
}
