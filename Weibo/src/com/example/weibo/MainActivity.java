package com.example.weibo;

import com.example.view.AndroidTitleBar;
import com.example.view.AndroidTitleBar.OnClickButtonListener;
import com.example.view.HideTitleBarLayout;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.openapi.models.User;
import com.weibo.fragment.StatusListViewActivity;
import com.weibo.fragment.LeftMenuFragment;
import com.weibo.fragment.StatusRecyclerViewFragment;
import com.weibo.sql.DataHelper;
import com.weibo.tools.AccessTokenKeeper;
import com.weibo.tools.MyApplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends FragmentActivity{

	private ViewPager vpFragments;
	public AndroidTitleBar atbTitleBar;
	private Oauth2AccessToken mAccessToken;
	private DrawerLayout dlDrawer;
	private HideTitleBarLayout htblHideTitleBar;
	
	private boolean isExit;
	
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawable(new ColorDrawable(MyApplication.mThemeColor));
		if(MyApplication.mThemeColor == getResources().getColor(R.color.colorThemeWhite1))
			getWindow().setStatusBarColor(getResources().getColor(R.color.colorTitle1));
		else
			getWindow().setStatusBarColor(MyApplication.mThemeColor);
		mAccessToken = AccessTokenKeeper.readAccessToken(this);
		if (!mAccessToken.isSessionValid()) {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, FirstLoginActivity.class);
        	startActivity(intent);
            finish();
		}else{
			DataHelper dataHelper = new DataHelper(getApplicationContext());
			User user = dataHelper.getUserByUid(mAccessToken.getUid());
			if(MyApplication.mIsNightMode)
				setContentView(R.layout.activity_main_night);
			else
				setContentView(R.layout.activity_main);
			
			View view = findViewById(R.id.v_status_background);
			view.setBackgroundColor(MyApplication.mThemeColor);
			System.out.println(Environment.getExternalStorageDirectory().getPath());
			atbTitleBar = (AndroidTitleBar) findViewById(R.id.titlebar);
			atbTitleBar.setTitleColor(MyApplication.mTitleColor);
			atbTitleBar.setTitle(user.screen_name);
			atbTitleBar.setFocusButton(0);
			dlDrawer = (DrawerLayout) findViewById(R.id.dl_drawerLayout);
			vpFragments = (ViewPager) findViewById(R.id.pager);
			
		    MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
		    vpFragments.setAdapter(myViewPagerAdapter);
		    vpFragments.setOffscreenPageLimit(2);
		    
		    htblHideTitleBar = (HideTitleBarLayout) findViewById(R.id.hidetitlelayout);
		    
		    FragmentManager fragmentManager = getSupportFragmentManager();
		    fragmentManager.beginTransaction().add(R.id.left_drawer, new LeftMenuFragment(), "left menu").commit();
		    
		    vpFragments.setOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageSelected(int arg0) {
					// TODO Auto-generated method stub
					System.out.println("mHideTitleLayout.setTitleBar()");
					atbTitleBar.setFocusButton(arg0);


				}

				@Override
				public void onPageScrolled(int position, float arg1, int positionOffsetPixels) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onPageScrollStateChanged(int state) {
					System.out.println("onPageScrollStateChanged");
					MyApplication.mCanHide = false;
					htblHideTitleBar.showTitleBar();

				}
			});
		    
		    
		    atbTitleBar.setOnClickButtonListener(new OnClickButtonListener() {

				@Override
				public void buttonCLick(View v, int index) {
					// TODO Auto-generated method stub
					switch (index) {
						case 0:
							vpFragments.setCurrentItem(0, true);
							break;
						case 1:
							vpFragments.setCurrentItem(1, true);
							break;
						case 2:
							vpFragments.setCurrentItem(2, true);
							break;
						case 3:
							dlDrawer.openDrawer(Gravity.LEFT);
							break;
						case 4:
							Intent intent = new Intent();
							intent.setClass(MainActivity.this, UserPageActivity.class);
							startActivity(intent);
							break;
						default:
							break;
					}
				}
			});
		}
	}
	
	public class MyViewPagerAdapter extends FragmentPagerAdapter{

		public MyViewPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			switch (arg0) {
			case 0:
				return StatusRecyclerViewFragment.newInstance(StatusRecyclerViewFragment.HOME, null);
            case 1:
            	return StatusRecyclerViewFragment.newInstance(StatusRecyclerViewFragment.HOME, null);
            default :
            	return StatusListViewActivity.newInstance(StatusListViewActivity.HOT, null);
			}
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 2;
		}
		
	}
	
	public void startActivity(Intent intent ,boolean b) {
		if(b){
			super.startActivity(intent);
			overridePendingTransition(R.anim.base_slide_bottom_in, R.anim.base_slide_remain);
		}
		else{
			super.startActivity(intent);
			overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(!isExit){
				isExit = true;
				Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_LONG).show();
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						isExit = false;
					}
				}, 2000);
				return false;
			}
			else 
				System.exit(0);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(arg0);
	}

}
