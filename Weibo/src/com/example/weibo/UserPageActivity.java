package com.example.weibo;

import com.example.view.SwipeBackLayout;
import com.sina.weibo.sdk.openapi.models.User;
import com.weibo.fragmentmainactivity.StatusListFragment;
import com.weibo.fragmentmainactivity.StatusListFragment.OnStatusFragmentListener;
import com.weibo.tools.MyApplication;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserPageActivity extends FragmentActivity{

	public static String TAG = "UserPageActivity";
	private String status;
	private String followers;
	private String friends;
	private String avatar_large;
	private TextView mTextView;
	private View mTitle;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(MyApplication.mIsNightMode)
			setContentView(R.layout.activity_user_page_night);
		else
			setContentView(R.layout.activity_user_page);
		SwipeBackLayout wBackLayout = (SwipeBackLayout) findViewById(R.id.swipeback);
		LinearLayout layout = (LinearLayout) findViewById(R.id.ll_background);
		wBackLayout.setContentView(layout, this);
		layout.setBackgroundColor(MyApplication.mThemeColor);
		
		//View statusView = (View) findViewById(R.id.v_statusview);
		//statusView.setBackgroundColor(MyApplication.mThemeColor);
		
		Bundle bundle = this.getIntent().getExtras();
		String screen_name = bundle.getString("screen_name");
		avatar_large = bundle.getString("avatar_large");
		status = bundle.getString("statuses_count");
		followers = bundle.getString("followers_count");
		friends = bundle.getString("friends_count");
		
		mTextView = (TextView) findViewById(R.id.title_text);
		mTextView.setText(screen_name);
		mTextView.setTextColor(MyApplication.mTitleColor);
		mTextView.setBackgroundColor(MyApplication.mThemeColor);
		mTitle = (View) findViewById(R.id.stitle_userPage);
		
		//mTextView.getBackground().setAlpha(0);
		//mTextView.setAlpha(0);
		mTitle.setVisibility(View.INVISIBLE);
		
		StatusListFragment fragmentHomeActivity = StatusListFragment.newInstance(StatusListFragment.USER, screen_name);
		fragmentHomeActivity.setStatusListener(new OnStatusFragmentListener() {

			@Override
			public void setUser(User user) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setUserTitleBarVisable(int visibility) {
				// TODO Auto-generated method stub
				if(mTitle.getVisibility() != visibility)
					mTitle.setVisibility(visibility);
			}
			
		});
		getSupportFragmentManager().beginTransaction().add(R.id.rl_contain, fragmentHomeActivity).commit();
	}
}
