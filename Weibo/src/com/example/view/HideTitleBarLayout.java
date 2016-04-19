package com.example.view;

import com.android.style.AndroidTitleBar;
import com.android.style.AndroidTitleBar.OnTitleBarClickListener;
import com.example.weibo.R;
import com.weibo.tools.MyApplication;
import com.weibo.tools.ScreenTools;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

public class HideTitleBarLayout extends RelativeLayout implements OnGestureListener{

	private final static int DOWN = 0;
	private final static int UP = 1;
	private static final int BOTTOM = 2;
	private int mHeightOfTitleBar;
	private Scroller mScroller;
	//private float mInterInitX;
	private float mInterInitY;
	private int mState = DOWN;
	private float mLastY;
	//private float mLastX;
	private long mDownTime;
	private long mUpTime;
	private GestureDetector detector;
	private float mDrawX;
	private float mDrawY;
	private Paint mPaint;
	private int mHeightOfSceen;
	private int mHeightOfThis;
	private boolean mFling = false;
	private int mColorSize;
	private boolean mDisable;
	private AndroidTitleBar mTitleBar;
	private boolean isHide = false;
	private ColorLayout mColorLayout;
	private int mHeightOfColorTable;
	private int mWidthOfThis;
	private boolean mShowColorTable = false;
	private ViewPager mViewPager;

	public HideTitleBarLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		ScreenTools screenTools = ScreenTools.instance(context);
		mHeightOfSceen = screenTools.getScreenHeight();
		mScroller = new Scroller(context);
		detector = new GestureDetector(this);
		detector.setIsLongpressEnabled(false);
		mPaint = new Paint();
		mPaint.setColor(MyApplication.mThemeColor);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		mTitleBar = (AndroidTitleBar) findViewById(R.id.titlebar);
		mTitleBar.bringToFront();
		mTitleBar.setBarClickListener(new OnTitleBarClickListener() {
			
			@Override
			public void showColorTable() {
				// TODO Auto-generated method stub
				if(getScrollY() == -mHeightOfColorTable){
					mScroller.startScroll(0, getScrollY(), 0, 0-getScrollY(), 1000);
					invalidate();
				}
				else if(getScrollY() == 0){
					mScroller.startScroll(0, getScrollY(), 0, -mHeightOfColorTable-getScrollY(), 1000);
					invalidate();
				}
			}
		});
		mColorLayout = (ColorLayout) findViewById(R.id.cl_colortable);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		mHeightOfThis = getMeasuredHeight();
		ScreenTools s = ScreenTools.instance(getContext());
		mColorLayout.setLayoutParams(new LayoutParams(s.getScreenWidth(), getMeasuredHeight()-s.dip2px(55)));
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//Log.e(" getMeasuredHeight()", ""+getMeasuredHeight());
		mHeightOfTitleBar = mTitleBar.getMeasuredHeight();
		//System.out.println(mHeightOfTitleBar);
		mHeightOfThis = getMeasuredHeight();
		mWidthOfThis = getMeasuredWidth();
		mHeightOfColorTable = mColorLayout.getMeasuredHeight();
		//System.out.println(mHeightOfThis + " "+mHeightOfTitleBar+" "+mHeightOfColorTable);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		//System.out.println("onlayout");
		for(int i = 0; i < getChildCount(); i++){
			if(getChildAt(i) instanceof ViewPager){
				mViewPager = (ViewPager)getChildAt(i);
				break;
			}
		}
		View child = getChildAt(0);
		//System.out.println(mWidthOfThis+" "+mHeightOfThis);
		mHeightOfColorTable = mColorLayout.getMeasuredHeight();
		//child.layout(0, 0, mWidthOfThis, (int) (mHeightOfThis*0.2));
		//child = getChildAt(1);
		//child.layout(0, 0, mWidthOfThis, 1300);
		//child = getChildAt(2);
		//child.layout(0, (int) (mHeightOfThis*0.6), mWidthOfThis, 0);
		if(mHeightOfColorTable != 0){
			mColorLayout.layout(0, -mHeightOfColorTable, mWidthOfThis, 0);
		}
	}	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			detector.onTouchEvent(ev);
			mInterInitY = ev.getY();
			mLastY = ev.getY();
			//System.out.println("Down");
			break;
		case MotionEvent.ACTION_MOVE:
			//System.out.println("Move");
			if(getScrollY() == 0 && mInterInitY > mHeightOfTitleBar){
				if(mInterInitY > mLastY && ev.getY() > mLastY)
					mInterInitY = mLastY;
			
				if(mInterInitY < mLastY && ev.getY() < mLastY)
					mInterInitY = mLastY;
			
				if(ev.getY() - mInterInitY < -200 && !isHide){
					//System.out.println("HideTitleBarLayout " + ((RecyclerView)(((SwipeRefreshLayout)(mViewPager.getChildAt(mViewPager.getCurrentItem()))).getChildAt(1))).getChildAt(0));
					//System.out.println("hildetitlelayout mViewPager.getChildCount()"+mViewPager.getChildCount());
					//System.out.println("mViewPager.getCurrentItem()" + mViewPager.getCurrentItem());
					//RecyclerView tmpReclView = (RecyclerView)(((SwipeRefreshLayout)(mViewPager.getChildAt(mViewPager.getCurrentItem()))).getChildAt(1));
					
					//LinearLayoutManager layoutManager = ((LinearLayoutManager)tmpReclView.getLayoutManager());
					
					//System.out.println("HideTitleBarLayout position "+layoutManager.findFirstVisibleItemPosition());
					//System.out.println("HideTitleBarLayout FirstCompletelyVisibleItem position "+layoutManager.findFirstCompletelyVisibleItemPosition());
					//if(tmpReclView.getChildAt(0) != null){
						//System.out.println("tmpReclView.getChildAt(0).getTag()"+tmpReclView.getChildAt(0).getTag());
						//System.out.println("tmpReclView.getChildAt(1).getTag()"+tmpReclView.getChildAt(1).getTag());
					if(MyApplication.mCanHide){	
						mTitleBar.hide();
						isHide  = true;
					}
					//}
					//System.out.println(ev.getY() - mInterInitY);
				}
				if(ev.getY() > mLastY+10 && isHide){
					mTitleBar.show();
					isHide=false;
					//System.out.println(ev.getY()+" "+mLastY);
				}
				if(ev.getY() - mInterInitY > 10 && isHide){
					mTitleBar.show();
					isHide=false;
					//System.out.println(ev.getY()+" "+mInterInitY);
				}
				mLastY = ev.getY();
			}
			if(!isHide){
				if(mInterInitY <= mHeightOfTitleBar || mShowColorTable){
					//System.out.println(mLastY + " "+ ev.getY());
					if((getScrollY() + mLastY - ev.getY()) > 0)
						scrollTo(0, 0);
					else
						scrollBy(0, (int) (mLastY - ev.getY()));
					mLastY = ev.getY();
				}
			}
			if(ev.getY() - mInterInitY > 10 && isHide){
				mTitleBar.show();
				isHide=false;
				//System.out.println(ev.getY()+" "+mInterInitY);
			}
			detector.onTouchEvent(ev);
			break;
		case MotionEvent.ACTION_UP:
			detector.onTouchEvent(ev);
			if(getScrollY() > -mHeightOfColorTable/2){
				mScroller.startScroll(0, getScrollY(), 0, 0-getScrollY(), 500);
				invalidate();
				mShowColorTable = false;
			}
			else {
				mScroller.startScroll(0, getScrollY(), 0, -mHeightOfColorTable-getScrollY(), 500);
				invalidate();
				mShowColorTable = true;
			}
			break;
		case MotionEvent.ACTION_CANCEL:
			//System.out.println("cancle");
			
			break;
		default:
			break;
		}
		//if(getScrollY() != 0)
			//return true;
		return super.dispatchTouchEvent(ev);
	}
	
	
	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		if(mScroller.computeScrollOffset()){
			scrollTo(0, mScroller.getCurrY());
			invalidate();
		}
	}
	
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		// TODO Auto-generated method stub
		if(getScaleY() == 0){
			if(velocityY < -1000){
				mTitleBar.hide();
				//System.out.println("hide"+velocityY);
				isHide=true;
			}
			if(velocityY > 1000){
				mTitleBar.show();
				//System.out.println("show"+velocityY);
				isHide=false;
			}
		}
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void showTitleBar() {
		isHide = false;
		mTitleBar.show();
		
	}
}
