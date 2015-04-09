package com.konka.music.ui.fragment.dialogfragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ClearHistoryDialogFragment extends DialogFragment {

	public static ClearHistoryDialogFragment newInstance() {
		ClearHistoryDialogFragment addClassifyDialogFragment = new ClearHistoryDialogFragment();

		return addClassifyDialogFragment;
	}

	private DialogInterface.OnClickListener buttonOnClickListener = null;

	public ClearHistoryDialogFragment setPositiveButtonOnClickListener(DialogInterface.OnClickListener buttonOnClickListener) {
		this.buttonOnClickListener = buttonOnClickListener;
		return this;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new Builder(getActivity()).setTitle("提示")//
				.setMessage("确定要清空历史记录吗?")//
				.setPositiveButton("确定", buttonOnClickListener)//
				.setNegativeButton("取消", null);

		return builder.create();
	}

}
