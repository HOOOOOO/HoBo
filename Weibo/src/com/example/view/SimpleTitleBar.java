package com.example.view;

import com.example.weibo.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SimpleTitleBar extends RelativeLayout{
	
	private TextView mTvTitleText;

	public SimpleTitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		View view = LayoutInflater.from(context).inflate(R.layout.simple_title, this, false);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		mTvTitleText = (TextView) findViewById(R.id.title_text);
		
	}

}
