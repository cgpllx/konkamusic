package com.konka.music.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.konka.music.R;

public class NetFragment extends Fragment{
	@Override
	public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
		 View rootView=inflater.inflate(R.layout.netfragment_layout, container, false);
		return rootView;
	}
}
