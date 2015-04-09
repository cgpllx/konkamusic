package com.konka.music.ui.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.konka.music.R;
import com.konka.music.util.Assist;
import com.konka.music.util.Net;
import com.konka.music.util.ToastUtil;
import com.konka.music.util.Util;
import com.kubeiwu.baseclass.util.KLog;

/**
 * 反馈
 */
public class FeedBackActivity extends SuperActivity {
	
	private static final String TAG = "SettingFeedBackActivity";
	
	private TextView SubmitButton;
	private Context context = FeedBackActivity.this;
	private EditText inputCom1;
	private Spinner spi1, spi2;
	
	private ProgressBar mProgressBar;
	
	private String[] mAgeStrings = { "年龄", "18岁以下", "18-24岁", "25-30岁", "31-35岁", "36-40岁", "41-50岁", "50岁以上" };
	private String[] mSexStrings = { "性别", "男", "女" };

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				
				boolean isFeedbackSuccess = (Boolean)msg.obj;
				KLog.v(TAG, "FeedbackActivity->is feedback success: " + isFeedbackSuccess);
				hideProgressBar();
				if (isFeedbackSuccess) {
					ToastUtil.showToast(getApplicationContext(), R.string.feedback_upload_success);
					FeedBackActivity.this.finish();
				} else {
					ToastUtil.showToast(getApplicationContext(), R.string.feedback_upload_fail);
					FeedBackActivity.this.finish();
				}
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.feed_back);
		
		mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
		inputCom1 = (EditText) findViewById(R.id.umeng_fb_content);
		inputCom1.setSingleLine(false);
		spi1 = (Spinner) findViewById(R.id.umeng_fb_age_spinner);
		spi2 = (Spinner) findViewById(R.id.umeng_fb_gender_spinner);
		SubmitButton = (TextView) findViewById(R.id.umeng_fb_submit);
		
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mAgeStrings);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mSexStrings);

//		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spi1.setAdapter(adapter1);
		spi2.setAdapter(adapter2);
		
		SubmitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String str = inputCom1.getText().toString();
				String age = spi1.getSelectedItem().toString();
				String sex = spi2.getSelectedItem().toString();

				KLog.i("feedbackstring", str + age + sex);
				if (age.endsWith("年龄"))
					age = "";
				if (sex.equals("性别"))
					sex = "";
				if (str == null || str.equals("")) {
					ToastUtil.showToast(getApplicationContext(), R.string.feedback_not_input);
					return;
				}
				try {
					str = URLEncoder.encode(str, "UTF-8");
					age = URLEncoder.encode(age, "UTF-8");
					sex = URLEncoder.encode(sex, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				final String uilstr = str;
				KLog.d(TAG, "SettingFeedBackActivity->反馈内容： " + uilstr);
				if (!Util.checkIntent(context)) {
					ToastUtil.showToast(getApplicationContext(), R.string.no_network);
					return;
				}
				//Start post feedback to server
				new Thread(new Runnable() {

					@Override
					public void run() {
						Message msg = new Message();
						msg.what = 1;
						msg.obj = Net.postFeedbackMessage(Assist.FEEDBACK_URL, "反馈信息", uilstr);
						FeedBackActivity.this.handler.sendMessage(msg);
					}
				}).start();
				
				showProgressBar();
			}
		});

	}

	private void showProgressBar() {
		if (this.mProgressBar != null)
			this.mProgressBar.setVisibility(View.VISIBLE);
	}

	private void hideProgressBar() {
		if(this.mProgressBar != null)
			this.mProgressBar.setVisibility(View.GONE);
	}
}