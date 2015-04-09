package com.konka.music.ui.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;

import com.konka.music.ui.fragment.AboutFragment;

public class AboutActivity extends SuperActivity {
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new AboutFragment()).commit();
		ActionBar actionbar = getActionBar();//getSupportActionBar();
		actionbar.setHomeButtonEnabled(true);
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setTitle("关于我们");
//		actionbar.setLogo(R.drawable.d_title_back_new);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
