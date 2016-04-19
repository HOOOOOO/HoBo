package com.example.weibo;

import com.example.view.SwipeBackLayout;
import com.weibo.fragmentmainactivity.FragmentHomeActivity;
import com.weibo.fragmentmainactivity.StatusListFragment;
import com.weibo.tools.MyApplication;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FavoriteActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(MyApplication.mIsNightMode)
			setContentView(R.layout.activity_favorite_night);
		else
			setContentView(R.layout.activity_favorite);
		SwipeBackLayout wBackLayout = (SwipeBackLayout) findViewById(R.id.sl_swipeback);
		
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.rl_background);
		wBackLayout.setContentView(layout, this);
		layout.setBackgroundColor(MyApplication.mThemeColor);
		
		TextView textView = (TextView) findViewById(R.id.title_text);
		textView.setBackgroundColor(MyApplication.mThemeColor);
		textView.setText("我的收藏");
		textView.setTextColor(MyApplication.mTitleColor);
		
		android.support.v4.app.FragmentManager fm =  getSupportFragmentManager();
		fm.beginTransaction().add(R.id.rl_content, StatusListFragment.newInstance(StatusListFragment.FAVORITE, null), null).commit();
		View tmpView = (View) findViewById(R.id.simpletitlebar);
		tmpView.bringToFront();
	}
}
