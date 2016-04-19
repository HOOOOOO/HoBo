package com.android.style;

import com.example.weibo.MainActivity;
import com.example.weibo.R;
import com.example.weibo.SearchActivity;
import com.weibo.tools.MyApplication;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

public class AndroidTitleBar extends RelativeLayout implements View.OnClickListener {
	
	private ImageButton slide_btn, left_btn, right_btn, me_btn;
	private OnClickButtonListener mOnClickButtonListener;
	private Scroller mScroller;
	private int mHeight;
	private OnTitleBarClickListener mBarClickListener;
	private TextView mTitle;
	
	public AndroidTitleBar(Context context) {
		this(context, null);
	}

	public AndroidTitleBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AndroidTitleBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_titlebar, this, true);
		
		mTitle = (TextView) findViewById(R.id.tv_username);
		
		slide_btn = (ImageButton) findViewById(R.id.slide_btn);
		left_btn = (ImageButton) findViewById(R.id.left_btn);
		right_btn = (ImageButton) findViewById(R.id.right_btn);
		me_btn = (ImageButton) findViewById(R.id.btn_search);
		
		slide_btn.setOnClickListener(this);
		left_btn.setOnClickListener(this);
		right_btn.setOnClickListener(this);
		me_btn.setOnClickListener(this);
		
		setFocusButton(0);
		
		mScroller = new Scroller(context);
	}
	
	public void setTitle(String s) {
		mTitle.setText(s);
	}
	
	public void setTitleColor(int color) {
		mTitle.setTextColor(color);
		left_btn.setColorFilter(color);
		right_btn.setColorFilter(color);
		slide_btn.setColorFilter(color);
		me_btn.setColorFilter(color);
	}
	
	
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		View view = (View)findViewById(R.id.tb_background);
		view.setBackgroundColor(MyApplication.mThemeColor);
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mBarClickListener.showColorTable();
			}
		});
	}
	
	public void setBarClickListener(OnTitleBarClickListener clickListener) {
		this.mBarClickListener = clickListener;
	}

	public void setFocusButton(int which) {
		switch (which) {
		case 0:
			left_btn.setAlpha(255);
			right_btn.setAlpha(255/2);
			break;
        case 1:
        	left_btn.setAlpha(255/2);
        	right_btn.setAlpha(255);
			break;
		default:
			break;
		}
	}
	
	public interface OnClickButtonListener{
		public void buttonCLick(View v, int index);
	}
	
	public void setOnClickButtonListener(OnClickButtonListener onClickListener) {
		this.mOnClickButtonListener = onClickListener;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.slide_btn:
			mOnClickButtonListener.buttonCLick(v, 3);
			break;
        
        case R.id.btn_search:
        	Intent intent = new Intent();
        	intent.setClass((MainActivity)getContext(), SearchActivity.class);
        	((MainActivity)getContext()).startActivity(intent, false);
        	break;
		default:
			break;
		}
		
	}
	
	public void setButtonAphla(int from, int to, float arg) {
       int alphaDown = (int) (255 - 112 * arg);
       int alphaUp = (int) (112 + 112 * arg);
		switch (from) {
	case 0:
		left_btn.setAlpha(alphaDown);
		break;
	case 2:
		right_btn.setAlpha(alphaDown);
	case 1:
		if(to == 0){
			left_btn.setAlpha(alphaUp);
		}
		else {
			right_btn.setAlpha(alphaUp);
		}
	default:
		break;
	}		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mHeight = getMeasuredHeight();
	}
	
	public void hide() {
		mScroller.startScroll(0, getScrollY(), 0, (int) (mHeight-getScrollY()), 1000);
		invalidate();
	}
	
	public void show() {
		mScroller.startScroll(0, getScrollY(), 0, 0-getScrollY(), 1000);
		invalidate();
	}
	
	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		super.computeScroll();
		if(mScroller.computeScrollOffset()){
			scrollTo(0, mScroller.getCurrY());
			invalidate();
		}
	}
	
	public interface OnTitleBarClickListener{
		public void showColorTable();
	}
}
