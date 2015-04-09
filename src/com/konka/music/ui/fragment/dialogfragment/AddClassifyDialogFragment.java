package com.konka.music.ui.fragment.dialogfragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.konka.music.R;
import com.konka.music.util.ViewUtility;

public class AddClassifyDialogFragment extends DialogFragment {

	public static AddClassifyDialogFragment newInstance() {
		AddClassifyDialogFragment addClassifyDialogFragment = new AddClassifyDialogFragment();

		return addClassifyDialogFragment;
	}

	private View dialogContentView;
	private EditText editText;

	private PositiveButtonOnClickListener buttonOnClickListener = null;

	public AddClassifyDialogFragment setPositiveButtonOnClickListener(PositiveButtonOnClickListener buttonOnClickListener) {
		this.buttonOnClickListener = buttonOnClickListener;
		return this;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		dialogContentView = View.inflate(getActivity(), R.layout.classify_edittext_layout, null);
		editText = ViewUtility.findViewById(dialogContentView, R.id.add_classify_edittext);
		AlertDialog.Builder builder = new Builder(getActivity()).setTitle("新建类别")//
				.setView(dialogContentView)//
				.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (editText != null) {
							Editable editable = editText.getText();
							if (buttonOnClickListener != null && editable != null) {
								buttonOnClickListener.positiveOnClick(editable.toString());
							}
						}
					}
				}).setNegativeButton("取消", null);

		return builder.create();
	}

	public interface PositiveButtonOnClickListener {
		void positiveOnClick(String classifyname);
	}
}
