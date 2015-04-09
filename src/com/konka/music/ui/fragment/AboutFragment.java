/*****************************************************************************
 * AboutActivity.java
 *****************************************************************************
 * Copyright © 2011-2012 VLC authors and VideoLAN
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston MA 02110-1301, USA.
 *****************************************************************************/

package com.konka.music.ui.fragment;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.konka.music.R;

public class AboutFragment extends Fragment {
    public final static String TAG = "AboutActivity";

//    private TabHost mTabHost;
//    FlingViewGroup mFlingViewGroup;
//    private int mCurrentTab = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("关于我们");

        View v = inflater.inflate(R.layout.about_main, container, false);
//
//        mTabHost = (TabHost) v.findViewById(android.R.id.tabhost);
//        mFlingViewGroup = (FlingViewGroup) v.findViewById(R.id.fling_view_group);
//
//        WebView t = (WebView)v.findViewById(R.id.webview);
//        String revision = Util.readAsset("revision.txt", "Unknown revision");
//        t.loadData(Util.readAsset("licence.htm", "").replace("!COMMITID!",revision), "text/html", "UTF8");
//
//        TextView link = (TextView) v.findViewById(R.id.main_link);
//        link.setText(Html.fromHtml(this.getString(R.string.about_link)));
//
//        String builddate = Util.readAsset("builddate.txt", "Unknown");
//        String builder = Util.readAsset("builder.txt", "unknown");
//
//        TextView compiled = (TextView) v.findViewById(R.id.main_compiled);
//        compiled.setText(builder + " (" + builddate + ")");
//        TextView textview_rev = (TextView) v.findViewById(R.id.main_revision);
//        textview_rev.setText(getResources().getString(R.string.revision) + " " + revision + " (" + builddate + ")");
//
//        final ImageView logo = (ImageView) v.findViewById(R.id.logo);
//        logo.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AnimationSet anim = new AnimationSet(true);
//                RotateAnimation rotate = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                rotate.setDuration(800);
//                rotate.setInterpolator(new DecelerateInterpolator());
//                anim.addAnimation(rotate);
//                logo.startAnimation(anim);
//            }
//        });
//
//        mTabHost.setup();
//
//        addNewTab(mTabHost, "about", getResources().getString(R.string.about));
//        addNewTab(mTabHost, "licence", getResources().getString(R.string.licence));
//
//        mTabHost.setCurrentTab(mCurrentTab);
//        mFlingViewGroup.snapToScreen(mCurrentTab);
//
//        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
//            @Override
//            public void onTabChanged(String tabId) {
//                mCurrentTab = mTabHost.getCurrentTab();
//                mFlingViewGroup.smoothScrollTo(mCurrentTab);
//            }
//        });
//
//        mFlingViewGroup.setOnViewSwitchedListener(new FlingViewGroup.ViewSwitchListener() {
//            @Override
//            public void onSwitching(float progress) { }
//            @Override
//            public void onSwitched(int position) {
//                mTabHost.setCurrentTab(position);
//            }
//            @Override
//            public void onTouchDown() {}
//            @Override
//            public void onTouchUp() {}
//            @Override
//            public void onTouchClick() {}
//            @Override
//            public void onBackSwitched() {
//                MainActivity activity = (MainActivity)getActivity();
//                activity.popSecondaryFragment();
//            }
//        });
//
        return v;
    }

//    private static class DummyContentFactory implements TabHost.TabContentFactory {
//        private final Context mContext;
//        public DummyContentFactory(Context ctx) {
//            mContext = ctx;
//        }
//        @Override
//        public View createTabContent(String tag) {
//            View dummy = new View(mContext);
//            return dummy;
//        }
//    }

//    private void addNewTab(TabHost tabHost, String tag, String title) {
//        DummyContentFactory dcf = new DummyContentFactory(tabHost.getContext());
//        TabSpec tabSpec = tabHost.newTabSpec(tag);
//        tabSpec.setIndicator(getNewTabIndicator(tabHost.getContext(), title));
//        tabSpec.setContent(dcf);
//        tabHost.addTab(tabSpec);
//    }

//    private View getNewTabIndicator(Context context, String title) {
//        View v = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
//        TextView tv = (TextView) v.findViewById(R.id.textView);
//        tv.setText(title);
//        return v;
//    }

    public static String getVersion(Context ctx) {
        String versionName = "";
        PackageInfo packageInfo;
        try {
            packageInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            versionName = "v" + packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
}
