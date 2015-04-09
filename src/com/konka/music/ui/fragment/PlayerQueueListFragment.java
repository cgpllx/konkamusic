package com.konka.music.ui.fragment;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.konka.music.R;
import com.konka.music.adapter.PlayerQueueListAdapter;
import com.konka.music.adapter.PlayerQueueListAdapter.SetIcon;
import com.konka.music.pojo.MusicInfo;
import com.konka.music.service.MusicInfoManager;
import com.konka.music.service.WeakHandler;
import com.konka.music.ui.fragment.abstractfragment.AbstractKBaseFragment;
import com.konka.music.util.ObjectUtil;
import com.kubeiwu.baseclass.util.KLog;

public class PlayerQueueListFragment extends AbstractKBaseFragment implements OnClickListener, SetIcon {

	private List<MusicInfo> mMusicInfos;
	private PlayerQueueListAdapter mAdapter;

	private View mContainer;
	private View mViewLine;
	private RelativeLayout mViewLayout; // 背景
	private ImageView mViewEmpty; // 空白
	private ImageButton mViewClean; // 清空
	private TextView mViewTitle; // 名称
	private ListView mViewList; // 数据列

	private static final int MSG_SHOW_EMPTY = 1;
	private static final int MSG_HIDE_EMPTY = 2;

	private Handler mHandler = new MyHandler(this);

	
	static class MyHandler extends WeakHandler<PlayerQueueListFragment> {

		public MyHandler(PlayerQueueListFragment owner) {
			super(owner);
		}
		@Override
		public void handleMessage(Message msg) {
			PlayerQueueListFragment fragment=getOwner();
			if(!ObjectUtil.isEmpty(fragment)){
				switch (msg.what) {
				case MSG_SHOW_EMPTY:
					fragment.showEmpty();
					break;
				case MSG_HIDE_EMPTY:
					fragment.hideEmpty();
					break;
				}
			}
		}
	};

	private OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// 播放选中音乐
			MusicInfoManager.playPositionInTheList(getActivity(), position);
			notifyDataSetChanged();
			removeSelf();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (mContainer == null) {
			mContainer = inflater.inflate(R.layout.player_queue_list, container, false);
		} else {
			container.removeView(mContainer);
		}
		return mContainer;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView();
		setMusicInfo();
		initListener();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.player_queue_cancel_layer:
			removeSelf();
			break;
		case R.id.player_queue_clean:
			removeSelf();
			cleanAllDialog();
			break;

		default:
			break;
		}
	}

	private void cleanAllDialog() {

		AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle("清空队列").setMessage("确定要清空播放队列?").setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				PlayerQueueListFragment.this.cleanAll(((Dialog) dialog).getContext());
				dialog.dismiss();
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).create();
		dialog.show();
		dialog.getContext();
	}

	/**
	 * getActivity 中也要清除
	 * */
	private void cleanAll(Context c) {
		// 此处应清除 getActivity中的 ArrayList
		MusicInfoManager.clearPalyList(c);
		mHandler.sendEmptyMessage(MSG_SHOW_EMPTY);
	}

	private void initView() {
		mViewLayout = (RelativeLayout) mContainer.findViewById(R.id.player_queue_cancel_layer);
		mViewEmpty = (ImageView) mContainer.findViewById(R.id.player_queue_empty);
		mViewClean = (ImageButton) mContainer.findViewById(R.id.player_queue_clean);
		mViewTitle = (TextView) mContainer.findViewById(R.id.player_queue_title);
		mViewList = (ListView) mContainer.findViewById(android.R.id.list);
		mViewLine = mContainer.findViewById(R.id.player_queue_line);
	}

	private void initListener() {
		if (mMusicInfos == null || mMusicInfos.size() == 0) {
			mHandler.sendEmptyMessage(MSG_SHOW_EMPTY);
			mViewLayout.setOnClickListener(this);
		} else {
			mHandler.sendEmptyMessage(MSG_HIDE_EMPTY);
			mViewLayout.setOnClickListener(this);
			mViewClean.setOnClickListener(this);
			mViewList.setAdapter(mAdapter);
			mViewList.setOnItemClickListener(mItemClickListener);
			notifyDataSetChanged();
		}
	}

	public void hideEmpty() {
		this.mViewEmpty.setVisibility(View.GONE);
		this.mViewClean.setVisibility(View.VISIBLE);
		this.mViewList.setVisibility(View.VISIBLE);
		this.mViewTitle.setVisibility(View.VISIBLE);
		this.mViewLine.setVisibility(View.VISIBLE);
		mViewTitle.setText("播放队列(" + mMusicInfos.size() + ")");
	}

	public void notifyDataSetChanged () {
		this.mAdapter.notifyDataSetChanged();
	}

	public void removeSelf() {
		getActivity().getSupportFragmentManager().popBackStack();
	}

	@Override
	public View getView(int position, View convertView) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(getActivity()).inflate(R.layout.player_queue_list_item, null);
			holder = new ViewHolder();
			holder.index = (TextView) convertView.findViewById(R.id.index);
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (mMusicInfos != null && mMusicInfos.size() > position) {
			holder.index.setText("" + (position + 1));
			holder.name.setText(mMusicInfos.get(position).getDisplayName());
			if (getPlayListIndex() == position) {
				holder.icon.setImageResource(R.drawable.icon);
				holder.name.setTextColor(getActivity().getResources().getColor(R.color.common_blue));
			} else {
				holder.icon.setImageBitmap(null);
				holder.name.setTextColor(getActivity().getResources().getColor(R.color.white));
			}
		} else {
			KLog.e("wangxu", "setImage->null");
		}
		return convertView;
	}

	public void showEmpty() {
		this.mViewClean.setVisibility(View.GONE);
		this.mViewList.setVisibility(View.GONE);
		this.mViewTitle.setVisibility(View.GONE);
		this.mViewLine.setVisibility(View.GONE);
		this.mViewEmpty.setVisibility(View.VISIBLE);
	}

	public void setMusicInfo() {
		this.mMusicInfos = getPlaylist();
		mAdapter = new PlayerQueueListAdapter();
		mAdapter.setMusicInfos(mMusicInfos);
		mAdapter.setIconInterface(this);
	}

//	public void setAdatper() {
//		this.mViewList.setAdapter(mAdapter);
//	}

	class ViewHolder {
		TextView index;
		TextView name;
		ImageView icon;
	}

}
