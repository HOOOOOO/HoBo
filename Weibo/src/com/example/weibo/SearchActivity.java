package com.example.weibo;

import java.net.URLEncoder;

import com.example.view.SwipeBackLayout;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.SearchAPI;
import com.sina.weibo.sdk.openapi.legacy.TrendsAPI;
import com.weibo.tools.AccessTokenKeeper;
import com.weibo.tools.Constants;
import com.weibo.tools.MyApplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchActivity extends AppCompatActivity {

	private SwipeBackLayout mSwipeBackLayout;
	private LinearLayout mLinearLayout;
	private TextView mTitle;
	private EditText mEditText;
	private Button mButton;
	private Oauth2AccessToken mAccessToken;
	private TrendsAPI mTrendsAPI;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		mSwipeBackLayout = (SwipeBackLayout) findViewById(R.id.swpb_search);
		mLinearLayout = (LinearLayout) findViewById(R.id.ll_postBackground);
		mLinearLayout.setBackgroundColor(MyApplication.mThemeColor);
		mSwipeBackLayout.setContentView(mLinearLayout, this);
		mTitle = (TextView) findViewById(R.id.title_text);
		mTitle.setTextColor(MyApplication.mTitleColor);
		mTitle.setText("搜索");
		mTitle.setBackgroundColor(MyApplication.mThemeColor);
		
		mEditText = (EditText) findViewById(R.id.edtv_search);
		
		mAccessToken = AccessTokenKeeper.readAccessToken(getApplicationContext());
		mTrendsAPI = new TrendsAPI(this, Constants.APP_KEY, mAccessToken);
		//mTrendsAPI.daily(false, mListener);
		//mTrendsAPI.trends(Long.parseLong(mAccessToken.getUid()), 50, 1, mListener);
		SearchAPI mSearchAPI = new SearchAPI(this, Constants.APP_KEY, mAccessToken);
		String search = URLEncoder.encode("HOCHAN");
		System.out.println(search);
		search = URLEncoder.encode("五水刀木");
		System.out.println(search);
		mSearchAPI.users(search, 10, mListener);
		//StatusesAPI statusesAPI = new StatusesAPI(this, Constants.APP_KEY, mAccessToken);
		//statusesAPI.friendsTimeline(0L, 0L, 50, 1, false, 0, false, mListener);
	}
	
	private RequestListener mListener = new RequestListener() {
		
		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub
			System.out.println(arg0);
		}
		
		@Override
		public void onComplete(String arg0) {
			// TODO Auto-generated method stub
			System.out.println(arg0);
		}
	};
}
