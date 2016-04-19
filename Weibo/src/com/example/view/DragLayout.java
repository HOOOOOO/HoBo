package com.example.view;

import com.example.weibo.R;
import com.weibo.tools.ScreenTools;

import android.content.Context;
import android.graphics.Point;
import android.location.GpsStatus.Listener;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

public class DragLayout extends RelativeLayout{
	private LinearLayout left_drawer;
	private Scroller scroller;
	
	private Point initPoint = new Point();
	private LeftDrawListener leftDrawListener;
	private int widthOfLeftDraw;
	private boolean moveToRight;
	private boolean evToDragLayout = false;
	private float iniEventX;
	private float iniEvnetY;
	private int edge;
	
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
        left_drawer = (LinearLayout) findViewById(R.id.left_drawer);
        scroller = new Scroller(getContext());
		super.onFinishInflate();
	}
	
	public DragLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void smoothMove(boolean isLeftToRight) {
		moveToRight = isLeftToRight;
		if(isLeftToRight) {
			scroller.startScroll(0, 0, 0-widthOfLeftDraw, 0, 1000);
		}
		else
			scroller.startScroll(0-widthOfLeftDraw, 0, widthOfLeftDraw, 0, 1000);
		invalidate();
	}
	
	public void doScroll(int sx, int sy, int dx, int dy) {
		scroller.startScroll(sx, sy, dx, dy, 500);
		invalidate();
	}
	
	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		if(scroller.computeScrollOffset()){
			scrollTo(scroller.getCurrX(), 0);
			
			Log.e("alpha", ""+getShadowAlpha());
			if(moveToRight)
				leftDrawListener.setShadowAlpha(getShadowAlpha());
			else {
				leftDrawListener.setShadowAlpha(getShadowAlpha());
			}
			invalidate();
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		LinearLayout child = (LinearLayout) getChildAt(0);
		LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
		ScreenTools screenTools = ScreenTools.instance(getContext());
		layoutParams.width = widthOfLeftDraw = (int) (screenTools.getScreenWidth() * 0.75);
		layoutParams.height = screenTools.getScreenHeight();
		child.setLayoutParams(layoutParams);
		edge = screenTools.dip2px(15); 
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		LinearLayout child = (LinearLayout) getChildAt(0);
		child.layout(-child.getMeasuredWidth(), 0, 0, child.getMeasuredHeight());
		initPoint.x = left_drawer.getLeft();
		initPoint.y = left_drawer.getTop();
	}
	
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
    	// TODO Auto-generated method stub
    	return super.onInterceptTouchEvent(ev);
    }
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.e("draglayout.getScrollX()", ""+getScrollX());
		
		//if(event.getX() < edge)
			//leftDrawListener.setShadowAlpha(-1);
		if(event.getX() < edge || getScrollX() < 0){
			int action = event.getAction();
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				iniEventX = event.getX();
				iniEvnetY = event.getY();
				//Log.e("event.getX()", ""+iniEventX);
				return true;
			case MotionEvent.ACTION_MOVE:
				Log.e("iniEventX", ""+iniEventX);
				Log.e("event.getX()", ""+event.getX());
				
				float tmp = event.getX() - iniEventX;
				iniEventX = event.getX();
				//Log.e("Distance", ""+tmp);
				if(getScrollX()-tmp < -left_drawer.getMeasuredWidth())
					scrollTo((int) (-left_drawer.getMeasuredWidth()), 0);
				else 
				    scrollTo((int) (getScrollX()-tmp), 0);
				leftDrawListener.setShadowAlpha(getShadowAlpha());
				break;
			case MotionEvent.ACTION_UP:
				int endX = (int) event.getX();
				int endY = (int) event.getY();
				if(endX == iniEventX && endY == iniEvnetY && iniEventX > left_drawer.getMeasuredWidth()) {
					smoothMove(false);
				}
				else {
					if(getScrollX() < (-left_drawer.getMeasuredWidth()/2))
						scroller.startScroll(getScrollX(), 0, (int) (-left_drawer.getMeasuredWidth()-getScrollX()), 0, 500);
					else 
						scroller.startScroll(getScrollX(), 0, (int) (0-getScrollX()), 0, 500);
					break;
				}
				
			default:
				break;
			}
			invalidate();
			return true;
		}
		return false;
	}
	
	public int getShadowAlpha() {
		int currentScroll = getScrollX();
		if(currentScroll > 0)
			currentScroll = 0;
		if(-currentScroll > widthOfLeftDraw)
			currentScroll = -widthOfLeftDraw;
		int alpha = (int) (-currentScroll*1.0/widthOfLeftDraw * 100);
		return alpha;
	}
	
	public void setLeftDrawListener (LeftDrawListener leftDrawListener) {
		this.leftDrawListener = leftDrawListener;
	}
	
	public interface LeftDrawListener {
		public void setShadowAlpha(int alpha);
		
	}
}