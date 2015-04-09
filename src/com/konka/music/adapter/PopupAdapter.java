package com.konka.music.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.konka.music.pojo.MusicInfo;

public class PopupAdapter extends ArrayAdapter<MusicInfo> {

	public PopupAdapter(Context context) {
		super(context, 0);
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
			viewHolder.textView = (TextView) convertView.findViewById(android.R.id.text1);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String title = getItem(position).getTitle();
		viewHolder.textView.setText(title);
		// viewHolder.textView.setCompoundDrawablesRelativeWithIntrinsicBounds(getContext().getResources().//
		// getDrawable(R.drawable.playlisticon), null, null, null);
		return convertView;
	}

	class ViewHolder {
		TextView textView;
	}
}
