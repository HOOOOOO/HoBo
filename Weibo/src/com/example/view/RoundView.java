package com.example.view;

import com.example.weibo.MainActivity;
import com.example.weibo.R;
import com.weibo.tools.MyApplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class RoundView extends View implements OnClickListener{
	
	private int mColor;
	private Paint mPaint;
	private boolean mBeChoose = false;
	private boolean mClicked = false;
	
	

	public RoundView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mColor = Color.parseColor("#ffffff");
		mPaint = new Paint();
		mPaint.setColor(mColor);
		mPaint.setAntiAlias(true);
		setOnClickListener(this);
	}

	public RoundView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mColor = Color.parseColor("#ffffff");
		mPaint = new Paint();
		mPaint.setColor(mColor);
		mPaint.setAntiAlias(true);
		setOnClickListener(this);
		// TODO Auto-generated constructor stub
	}
	
	public void setColor(int color) {
		mColor = color;
		//if(color == MyApplication.mThemeColor){
		//	beChoosed();
		//	return;
		//}
		invalidate();
	}
	
	public void beChoosed() {
		mBeChoose = true;
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		if(mBeChoose){
			mPaint.setColor(Color.parseColor("#ffffff"));
			canvas.drawOval(new RectF(0, 0, getMeasuredWidth(), getMeasuredWidth()), mPaint);
			mPaint.setColor(mColor);
			canvas.drawOval(new RectF(5, 5, getMeasuredWidth()-5, getMeasuredWidth()-5), mPaint);
			return;
		}
		if(mClicked){
			mPaint.setColor(Color.parseColor("#ffffff"));
			canvas.drawOval(new RectF(0, 0, getMeasuredWidth(), getMeasuredWidth()), mPaint);
			mPaint.setColor(mColor);
			canvas.drawOval(new RectF(10, 10, getMeasuredWidth()-10, getMeasuredWidth()-10), mPaint);
			return;
		}
		mPaint.setColor(mColor);
		canvas.drawOval(new RectF(0, 0, getMeasuredWidth(), getMeasuredWidth()), mPaint);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//Toast.makeText(MyApplication.getContext(), "点击了"+mColor, Toast.LENGTH_LONG).show();
		mClicked = true;
		invalidate();
		SharedPreferences sp = getContext().getSharedPreferences("hobo", MyApplication.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putInt("themeColor", mColor);
		System.out.print(mColor+" "+getResources().getColor(R.color.colorThemeWhite1));
		if(mColor == getResources().getColor(R.color.colorThemeBlue5)){
			MyApplication.mIsNightMode = true;
			editor.putBoolean("isNightMode", true);
			MyApplication.mTitleColor = getResources().getColor(R.color.colorNightWhite);
		}else{
			MyApplication.mIsNightMode = false;
			editor.putBoolean("isNightMode", false);
			if(mColor == getResources().getColor(R.color.colorThemeWhite1)){	
				editor.putInt("titleColor", getResources().getColor(R.color.colorTitle1));
				MyApplication.mTitleColor = getResources().getColor(R.color.colorTitle1);
			}
			else{
				editor.putInt("titleColor", Color.parseColor("#ffffff"));
				MyApplication.mTitleColor = Color.parseColor("#ffffff");
			}
		}
		editor.commit();
		MyApplication.mThemeColor = mColor;
		
		Intent intent = new Intent();
		intent.setClass(getContext(), MainActivity.class);
		((MainActivity)getContext()).startActivity(intent, true);
		((MainActivity)getContext()).finish();
	}

}
