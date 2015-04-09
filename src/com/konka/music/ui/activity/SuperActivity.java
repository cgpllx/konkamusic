package com.konka.music.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.konka.music.R;
import com.konka.music.util.Assist;

public class SuperActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Assist.BROADCAST_ACTION_EXIT);
		registerReceiver(receiver1, intentFilter);
	}

	BroadcastReceiver receiver1 = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (!TextUtils.isEmpty(action) && Assist.BROADCAST_ACTION_EXIT.equals(action)) {
				SuperActivity.this.finish();
			}
		}
	};

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.anim.next_enter_anim, R.anim.next_exist_anim);
	};

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.pre_enter_anim, R.anim.pre_exist_anim);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (receiver1 != null) {
			unregisterReceiver(receiver1);
		}

	}
}
