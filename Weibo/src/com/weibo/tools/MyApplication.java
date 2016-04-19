package com.weibo.tools;

import com.example.weibo.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sina.weibo.sdk.openapi.models.User;

import android.app.Application;
import android.app.Notification.Style;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MyApplication extends Application {
	
	private static MyApplication mInstance;
	public static int mThemeColor;
	public static final String PATH_OF_USERICON = "/hobousericon";
	public static final String PATH_OF_SAVEDIMG = "/hobo";
	public static final String PATH_OF_SAVESTATUS = "/status";
	
	public static final String NAME_OF_FAV = "fav";
	public static final String NAME_OF_HOME = "home";
	
	public final static String ISNIGHTMODE = "isNightMode";
	public final static String THEMECOLOR = "themeColor";
	public final static String TITLECOLOR = "titleColor";
	
	public static int mTitleColor;
	public static int mHighLightTextColor;
	public static int mWidthOfScreen;
	public static int mHeightOfScreen;
	public static float mRate;
	
	public static boolean mCanHide = false;
	
	public static boolean mIsNightMode = true;
	
	@Override
	public void onCreate() {
		super.onCreate();
		this.mInstance = this;
		SharedPreferences sp = getSharedPreferences("hobo", MODE_PRIVATE);
		//int styleId = sp.getInt("theme", R.style.BlueTheme);
		
		mIsNightMode = sp.getBoolean(ISNIGHTMODE, false);
		//
		if(mIsNightMode){
			mThemeColor = getResources().getColor(R.color.colorThemeBlue5);
			mTitleColor = getResources().getColor(R.color.colorNightWhite);
		}
		else{
			mThemeColor = sp.getInt("themeColor", getResources().getColor(R.color.colorThemeWhite1));
			mTitleColor = sp.getInt("titleColor", getResources().getColor(R.color.colorTitle1));	
		}
		
		mHighLightTextColor = getResources().getColor(R.color.colorWeiboText1);
		
		ScreenTools screenTools = ScreenTools.instance(this);
		mWidthOfScreen = screenTools.getScreenWidth();
		mHeightOfScreen = screenTools.getScreenHeight()-screenTools.dip2px(25);
		mRate = (float)mHeightOfScreen/mWidthOfScreen;
		
	}
	
	public static MyApplication getContext(){
		return mInstance;
	}
}
