package com.konka.music.ui.fragment.singer;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.konka.music.R;
import com.konka.music.adapter.ArrayAdapter;
import com.konka.music.pojo.Singer;
import com.konka.music.util.ArrayUtils;
import com.konka.music.util.Assist;
import com.konka.music.util.FragmentManagerUtil;
import com.konka.music.util.ViewUtility;
import com.kubeiwu.baseclass.loader.BaseLoaderCallbacksFragment;

public abstract class SingerBaseFragment extends BaseLoaderCallbacksFragment<ArrayList<Singer>> implements OnItemClickListener {

	protected class SingerChileAdapter extends ArrayAdapter<Singer> {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			SingerChileViewHolder viewHolder;
			if (convertView == null || convertView.getTag() == null) {
				viewHolder = new SingerChileViewHolder();
				convertView = View.inflate(getActivity(), R.layout.singer_list_item, null);

				viewHolder.name = ViewUtility.findViewById(convertView, R.id.singer_title);
				viewHolder.imageurl = ViewUtility.findViewById(convertView, R.id.singer_icon);
				viewHolder.desc = ViewUtility.findViewById(convertView, R.id.songs_number);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (SingerChileViewHolder) convertView.getTag();
			}
			Singer singer = getItem(position);
			viewHolder.name.setText(singer.getName());
			viewHolder.desc.setText(singer.getDesc());
			Assist.imageLoader.displayImage(singer.getImageurl(), viewHolder.imageurl, Assist.options);
			return convertView;
		}

		class SingerChileViewHolder {
			TextView name;
			ImageView imageurl;
			TextView desc;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ArrayList<Singer> singers = getSingers();
		if (!ArrayUtils.isEmpty(singers)) {
			int i = position - getHeaderViewCount();
			if (i < 0) {
				return;
			}
			Singer singer = singers.get(i);
			FragmentManagerUtil.swichFragment(getActivity().getSupportFragmentManager(), SingerMusicFragment.newInstance(singer.getId()));
		}
	}

	protected abstract int getHeaderViewCount();

	protected abstract ArrayList<Singer> getSingers();
}
