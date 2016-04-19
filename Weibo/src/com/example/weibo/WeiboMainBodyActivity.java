package com.example.weibo;

import android.os.Bundle;

public class WeiboMainBodyActivity extends SwipeBackActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weibo_main_body);
		
		//setDragEdge(DragEdge.LEFT);
	}
}
