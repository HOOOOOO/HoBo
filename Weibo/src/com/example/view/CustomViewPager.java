package com.example.view;

import com.weibo.tools.ScreenTools;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager{

	private float initX;
	private float initY;
	private ViewPagerListener viewPagerListener;
	private boolean first = true;
	private boolean isleft_draw = false;
	private int mScrollY;
	
	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		ScreenTools screenTools = ScreenTools.instance(context);
		//mScrollY = screenTools.dip2px();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return super.onInterceptTouchEvent(arg0);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
//		//if(((HideTitleBarLayout)getParent()).getScrollY() == 0 ||
//				(HideTitleBarLayout)getParent()).getScrollY() == )
			return super.onTouchEvent(event);
		//else
		//	return false;
	}
	
	public void setViewPagerListener(ViewPagerListener viewPagerListener) {
		this.viewPagerListener = viewPagerListener;
	}
	
	public interface ViewPagerListener {
		public void setScroll(int s, boolean isAction_UP);
	}
}
