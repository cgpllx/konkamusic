package com.konka.music.ui.fragment;

import android.content.DialogInterface.OnClickListener;
import android.support.v4.app.DialogFragment;

public class BaseDialogFragment extends DialogFragment {
	protected OnClickListener clickListener;

	public void setPositiveOnClickListener(OnClickListener clickListener) {
		this.clickListener = clickListener;
	}
}
