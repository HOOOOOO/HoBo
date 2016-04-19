package com.example.view;


import com.example.weibo.R;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MyToast extends Toast {

	public MyToast(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public static Toast makeText(Context context, CharSequence text, int duration) {
       Toast result = new Toast(context);

       LayoutInflater inflate = (LayoutInflater)
               context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       View v = inflate.inflate(R.layout.my_toast_layout, null);
       TextView tv = (TextView)v.findViewById(R.id.massage);
       tv.setText(text);
       
       result.setView(v);
       //setGravity方法用于设置位置，此处为垂直居中
       result.setGravity(Gravity.CENTER_VERTICAL, 0, -50);
       result.setDuration(duration);
       return result;
   }
	
}
