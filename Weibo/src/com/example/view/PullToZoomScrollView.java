package com.example.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

/**
 * Created by JIYI on 2015/8/10.
 */
public class PullToZoomScrollView extends ScrollView{
    private  boolean isonce;//加载该View的布局时是否是第一次加载，是第一次就让其实现OnMeasure里的代码

    private LinearLayout mParentView;//布局的父布局，ScrollView内部只能有一个根ViewGroup，就是此View
    private ViewGroup mTopView;//这个是带背景的上半部分的View，下半部分的View用不到的

    private int mScreenHeight;//整个手机屏幕的高度，这是为了初始化该View时设置mTopView用的
    private float mTopViewHeight;//这个就是mTopView的高度

    private int mCurrentOffset=0;//当前右侧滚条顶点的偏移量。ScrollView右侧是有滚动条的，当下拉时，
    //滚动条向上滑，当向下滑动时，滚动条向下滑动。

    private ObjectAnimator oa;//这个是对象动画，这个在本View里很简单，也很独立，就在这里申明一下，后面有两个方法
    //两个方法是：setT(int t),reset()两个方法用到，其他都和它无关了。

	private int mScreenWidth;
	private ChooseBarFloat mChooseBarFloatListenner;

	private final static int DRAG_EVENT_DELTA_XY = 20;
    /**
     * 初始化获取高度值，并记录
     * @param context
     * @param attrs
     */
    public PullToZoomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOverScrollMode(View.OVER_SCROLL_NEVER);
        WindowManager wm= (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        mScreenHeight=metrics.heightPixels;
        mScreenWidth = metrics.widthPixels;
        mTopViewHeight=mScreenHeight/2-(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, context.getResources().getDisplayMetrics());

    }
    
    public interface ChooseBarFloat{
    	public void setChooseBarFloat();
    }

    /**
     * 将记录的值设置到控件上，并只让控件设置一次
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(!isonce) {
            mParentView = (LinearLayout) this.getChildAt(0);
            mTopView = (ViewGroup) mParentView.getChildAt(0);
            mTopView.getLayoutParams().height = (int) mTopViewHeight;
            isonce=true;
        }
    }
    private OnScrollViewListener onScrollListener; 
    /** 
     * 主要是用在用户手指离开MyScrollView，MyScrollView还在继续滑动，我们用来保存Y的距离，然后做比较 
     */  
    private int lastScrollY;
    /** 
     * 用于用户手指离开MyScrollView的时候获取MyScrollView滚动的Y距离，然后回调给onScroll方法中 
     */  
    private Handler handler = new Handler() {  
  
        public void handleMessage(android.os.Message msg) {  
            int scrollY = PullToZoomScrollView.this.getScrollY();  
              
            //此时的距离和记录下的距离不相等，在隔5毫秒给handler发送消息  
            if(lastScrollY != scrollY){  
                lastScrollY = scrollY;  
                handler.sendMessageDelayed(handler.obtainMessage(), 5);    
            }  
            if(onScrollListener != null){  
                onScrollListener.onScroll(scrollY, mTopViewHeight);  
            }  
              
        };  
  
    }; 
    

    private int startY=0;//向下拉动要放大，手指向下滑时，点击的第一个点的Y坐标
    private boolean isBig;//是否正在向下拉放大上半部分View
    private boolean isTouchOne;//是否是一次连续的MOVE，默认为false,
    //在MoVe时，如果发现滑动标签位移量为0，则获取此时的Y坐标，作为起始坐标，然后置为true,为了在连续的Move中只获取一次起始坐标
    //当Up弹起时，一次触摸移动完成，将isTouchOne置为false
    private int distance=0;//向下滑动到释放的高度差

	private int startX = 0;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
    	//Log.e("子控件响应", String.valueOf(ev.getAction()));
        int action =ev.getAction();
        if(onScrollListener != null){  
            onScrollListener.onScroll(lastScrollY = this.getScrollY(), mTopViewHeight);  
        } 
        switch (action){
            case MotionEvent.ACTION_DOWN:
            	Log.e("子控件响应按下", String.valueOf(ev.getAction()));
            	//return true;
            	//return super.onTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:
            	Log.e("子控件响应移动", String.valueOf(ev.getAction()));
                if(mCurrentOffset<=0){
                    if(!isTouchOne){
                        startY=(int)ev.getY();
                        startX =(int)ev.getY();
                        isTouchOne=true;
                    }
                    boolean isSlideEvent = isSlideEvent(startX, (int)ev.getY(), startY, (int)ev.getY());
        			distance=(int)(ev.getY()-startY);
                    if(distance>0){
                        if(distance/2 < (mScreenWidth - mTopViewHeight)){
                    	isBig=true;
                        setT((int)-distance/2);
                        }
                    }
                    
                }
                break;
                //return super.onTouchEvent(ev);
                //return true;
            case MotionEvent.ACTION_UP:
            	Log.e("子控件响应提起", String.valueOf(ev.getAction()));
            	handler.sendMessageDelayed(handler.obtainMessage(), 20); 
            	if(isBig) {
                    reset();
                    isBig=false;
                }
                isTouchOne=false;
                break;
                //return super.onTouchEvent(ev);
            //default:
            	//return super.onTouchEvent(ev);
            	
                //return true;
        }
        return super.onTouchEvent(ev);
    }
    
  //判断是否为横向滑动，若是则返回true
  	private boolean isSlideEvent(int startX, int endX, int startY, int endY) {
  		int deltaX = endX - startX;
  		int deltaY = endY - startY;
  		return deltaX > DRAG_EVENT_DELTA_XY &&  Math.abs((deltaX)) > Math.abs((deltaY));
  	}

    /**
     * 对象动画要有的设置方法
     * @param t
     */
    public void setT(int t) {
        scrollTo(0, 0);
        if (t < 0) {
            mTopView.getLayoutParams().height = (int) (mTopViewHeight-t);
            mTopView.requestLayout();
        }
    }

    /**
     * 主要用于释放手指后的回弹效果
     */
    private void reset() {
        if (oa != null && oa.isRunning()) {
            return;
        }
        oa = ObjectAnimator.ofInt(this, "t", (int)-distance / 2, 0);
        oa.setDuration(150);
        oa.start();
    }

    /**
     * 这个是设置向上滑动时，上半部分View滑动速度让其小于下半部分
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        mCurrentOffset = t;//右边滑动标签相对于顶端的偏移量
        //当手势上滑，则右侧滚动条下滑，下滑的高度小于TopView的高度，则让TopView的上滑速度小于DownView的上滑速度
        //DownView的上滑速度是滚动条的速度，也就是滚动的距离是右侧滚动条的距离
        //则TopView的速度要小，只需要将右侧滚动条的偏移量也就是t缩小一定倍数就行了。我这里除以2速度减小1倍
        if (t <= mTopViewHeight&&t>=0&&!isBig) {
            mTopView.setTranslationY(t / 2);//使得TopView滑动的速度小于滚轮滚动的速度
        }
        if(isBig){
            scrollTo(0,0);
        }

    }
    
    public void setChooseBarFloatListrner(ChooseBarFloat chooseBarFloat) {
		this.mChooseBarFloatListenner = chooseBarFloat;
	}
    
    /** 
     * 滚动的回调接口 
     */  
    public interface OnScrollViewListener{  
        /** 
         * 回调方法， 返回MyScrollView滑动的Y方向距离 
         */  
        public void onScroll(float scrollY, float topheight);  
    }
    
    public void setOnScrollViewListener(OnScrollViewListener onScrollViewListener) {
		this.onScrollListener = onScrollViewListener;
	}
}
