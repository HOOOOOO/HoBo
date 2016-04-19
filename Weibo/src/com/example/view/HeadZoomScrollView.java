package com.example.view;

import com.example.view.RoundImageView;
import com.example.weibo.R;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

public class HeadZoomScrollView extends ScrollView{

	private static final int DRAG_EVENT_DELTA_XY = 20;
	private Context mContext;
	private LayoutInflater inflater;
	private View headView;
	private RoundImageView user_icon;
	private ImageView usericon_blur;
	private int headContentHeight;
	private int hideHeadHeight;
	private boolean isRecored;
	private int startX;
	private int startY;
	private int endX;
	private int endY;
	private boolean isDragUpEvent;
	private int tempY;
	private int tempX;
	
	public HeadZoomScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		init(mContext);
	}
	
	public HeadZoomScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		init(mContext);
	}

	private void init(Context mContext) {
		// TODO Auto-generated method stub
		if (Build.VERSION.SDK_INT >= 9) {
			setOverScrollMode(OVER_SCROLL_NEVER);//滑倒边界时是否发光
		}
		
		inflater = LayoutInflater.from(mContext);
		initHeaderView();
	}

	private void initHeaderView() {
		// TODO Auto-generated method stub
		headView = inflater.inflate(R.layout.me_usericon_show, null);
		user_icon = (RoundImageView) headView.findViewById(R.id.user_image);
		usericon_blur = (ImageView) headView.findViewById(R.id.imageView_bolur);
		
		measureView(headView);
		headContentHeight = headView.getMeasuredHeight();
		hideHeadHeight = headContentHeight / 3;
		addView(headView);
		
		headView.setPadding(0, -1 * hideHeadHeight, 0, 0);
		/*RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(0,0,0,-1*hideHeadHeight);
		headView.setLayoutParams(layoutParams);*/
		headView.invalidate();
	}
	
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		//ViewGroup.getChildMeasureSpec(int spec, int padding, int childDimension)
		//若spec，padding均为0，则子布局为实际大小
		//makeMeasureSpec(size,MeasureSpec.EXACTLY)得到的是size。
		//makeMeasureSpec(size,MeasureSpec.UNSPECIFIED)得到的是子布局的实际大小。
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);//参数为父布局的约束，然后child去设置自身的大小
	}
	
	//----------------------------------------------------------------------------------
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN://屏幕被按下
			if (!isRecored) {
				isRecored = true;
				startX = (int) event.getX();
				startY = (int) event.getY();
				//logv("ACTION_DOWN startX= " + startX + ",startY= " + startY);
				
			}
			break;

		case MotionEvent.ACTION_UP://离开屏幕
			endX = (int) event.getX();
			endY = (int) event.getY();
			int deltaX = endX - startX;
			
			//logv("ACTION_UP endX= " + endX + ",endY= " + endY + ",deltaX=" + deltaX);
			
			boolean isSlideEvent = isSlideEvent(startX, endX, startY, endY);
			if(isSlideEvent) {
				//横向滑动， 不响应纵向事件
		        return false;
				//logd("ACTION_UP: this is slide event");
			//不是横向滑动
			} else {
				//PaddingTopAsynTask task = new PaddingTopAsynTask(headView, hideHeadHeight);
				//task.setOnPaddingListener(onPaddingListener);
				//task.execute();
				headView.setPadding(0, -1 * hideHeadHeight, 0, 0);
			}
			isRecored = false;
			break;
        
		//===================================================================
	    //MOVE
		case MotionEvent.ACTION_MOVE:
			//Toast.makeText(getContext(), "在下拉", Toast.LENGTH_LONG).show();
			tempX = (int) event.getX();
			tempY = (int) event.getY();
			
			
			boolean isSlideMoveEvent = isSlideEvent(startX, tempX, startY, tempY);
			if(isSlideMoveEvent) {
				//横向滑动,不响应纵向事件
				return false;
			}
		
			if (!isRecored/* && firstItemIndex == 0*/) {
				isRecored = true;
				startY = tempY;
				startX = tempX;
			}
            
			headView.setPadding(0, getHeadPaddingContentHeight(startY, tempY), 0, 0);
			break;
		}
		return true;
	}
	
	private int getHeadPaddingContentHeight(int startY, int endY) {
		return  (endY - startY) / 3 - hideHeadHeight;
	}
	
	//判断是否为横向滑动，若是则返回true
		private boolean isSlideEvent(int startX, int endX, int startY, int endY) {
			int deltaX = endX - startX;
			int deltaY = endY - startY;
			return deltaX > DRAG_EVENT_DELTA_XY &&  Math.abs((deltaX)) > Math.abs((deltaY));
		}
		
		
		//-------------------------------------------------
		private class PaddingTopAsynTask extends AsyncTask<Integer, Integer, Void> {
			private static final String TAG = "PaddingTopAsynTask";
			private static final boolean DEBUG = false;
			private final static int STEP = 3;// 步伐
			private final static int TIME = 3;// 休眠时间

			// 距离（该距离指的是：mHeadView的PaddingTop+mHeadView的高度，及默认位置状态.）
			private int distance;
			// 循环设置padding执行次数.
			private int number;
			// 时时padding距离.
			private int disPadding;
			private View view = null;
			private int viewHeight = 0;
			int count = 0;
			OnPaddingListener onPaddingListener;

			//---------------------------------------------------------------------------------------
			public PaddingTopAsynTask(View view, int viewHeight) {
				this.view = view;
				this.viewHeight = viewHeight;
			}

			public void setOnPaddingListener(OnPaddingListener onPaddingListener) {
				this.onPaddingListener = onPaddingListener;
			}

			@Override
			protected Void doInBackground(Integer... params) {
				try {
					if (view == null) {
						throw new IllegalArgumentException("View is null !!");
					}
					if (viewHeight <= 0) {
						viewHeight = view.getMeasuredHeight();
					}
					distance = view.getPaddingTop() + Math.abs(viewHeight);

					// 获取循环次数.
					if (distance % STEP == 0) {
						number = distance / STEP;
					} else {
						number = distance / STEP + 1;
					}
					if(DEBUG) Log.i(TAG, "distance=" + distance + ",number=" + number
							+ "STEP=" + STEP + ",TIME=" + TIME);
					// 进行循环.
					for (int i = 0; i < number; i++) {
						Thread.sleep(TIME);
						publishProgress(STEP);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
				super.onProgressUpdate(values);
				disPadding = Math.max(view.getPaddingTop() - STEP, -1 * viewHeight);
				if(DEBUG) Log.i(TAG, "disPadding==" + disPadding + ",values: " + values[0]);
				if(view != null) {
					view.setPadding(0, disPadding, 0, 0);// 回归设置HeaderPadding
				}
				if(onPaddingListener != null) {
					if(count == 0) {
						onPaddingListener.onStartPadding(view);
					} else {
						count ++;
						if(count == number) {
							onPaddingListener.onEndPadding(view);
						}
					}
				}
			}

		}
		
		private interface OnPaddingListener {
			void onStartPadding(View view);
			void onEndPadding(View view);
		}

}
