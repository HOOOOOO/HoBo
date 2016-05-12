package com.example.view;

import java.util.ArrayList;

import com.example.weibo.R;
import com.weibo.tools.MyApplication;
import com.weibo.tools.ScreenTools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class ColorLayout extends RelativeLayout{

	private int space;
	private int size;
	private int rows = 6;
	private int columns = 4;
	private int padX;
	private int marginY;
	private Paint mPaint;
	private ScreenTools s;
	private int mNightModeStart;
	private int padY;
	private int marginX;
	
	private final static String COLORS[] = new String[]{
		"#eaebeb","#bdc3c7","#95a5a6","#7f8c8d",
		"#2ecc71", "#27ae60", "#1abc9c", "#16a085",
		"#3498db", "#2980b9", "#34495e", "#2c3e50",
		"#ffe900", "#f9df19", "#F1982E", "#e67e22",
		"#e98d70", "#C93756", "#c0392b", "#aa4630",
		"#283949"
	};
	private static final String TAG = "ColorLayout";

	public ColorLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ColorLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setWillNotDraw(false);
		// TODO Auto-generated constructor stub
		mPaint = new Paint();
		mPaint.setColor(Color.parseColor("#ffffff"));
		s = ScreenTools.instance(context);
		padX = s.dip2px(8)*3;
		marginX = s.dip2px(8);
 		space = s.dip2px(12);
 		marginY = s.dip2px(12);
 		int countOfColor = COLORS.length;
 		//System.out.println(TAG+" countOfColor:"+countOfColor);
// 		int colors[] = new int[countOfColor];
// 		colors[0] = context.getResources().getColor(R.color.colorThemeWhite1);
// 		colors[1] = context.getResources().getColor(R.color.colorThemeWhite2);
// 		colors[2] = context.getResources().getColor(R.color.colorThemeGray1);
// 		colors[3] = context.getResources().getColor(R.color.colorThemeGray2);
// 		
// 		colors[4] = context.getResources().getColor(R.color.colorThemeGreen1);
// 		colors[5] = context.getResources().getColor(R.color.colorThemeGreen2);
// 		colors[6] = context.getResources().getColor(R.color.colorThemeGreen3);
// 		colors[7] = context.getResources().getColor(R.color.colorThemeGreen4);
// 		
// 		colors[8] = context.getResources().getColor(R.color.colorThemeBlue1);
// 		colors[9] = context.getResources().getColor(R.color.colorThemeBlue2);
// 		colors[10] = context.getResources().getColor(R.color.colorThemeBlue3);
// 		colors[11] = context.getResources().getColor(R.color.colorThemeBlue4);
// 		
// 		colors[12] = context.getResources().getColor(R.color.colorThemeYellow1);
// 		colors[13] = context.getResources().getColor(R.color.colorThemeYellow2);
// 		colors[14] = context.getResources().getColor(R.color.colorThemeOrange1);
// 		colors[15] = context.getResources().getColor(R.color.colorThemeOrange2);
// 		
// 		colors[16] = context.getResources().getColor(R.color.colorThemeRed1);
// 		colors[17] = context.getResources().getColor(R.color.colorThemeRed2);
// 		colors[18] = context.getResources().getColor(R.color.colorThemeRed3);
// 		colors[19] = context.getResources().getColor(R.color.colorThemeRed4);
// 		colors[20] = context.getResources().getColor(R.color.colorThemeBlue5);
 		int tmp = (int) Math.min(s.getScreenWidth(), s.getScreenHeight()*0.6);
		size = (int) (((tmp - padX*2)- space*(columns-1))/columns+0.5);
		
		for(int i = 0; i < countOfColor; i++){
			RoundView roundView = new RoundView(context);
			LayoutParams layoutParams = new LayoutParams(size, size);
			roundView.setLayoutParams(layoutParams);
			roundView.setColor(Color.parseColor(COLORS[i]));
			addView(roundView);
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
//		//System.out.println(TAG+" getMeasuredHeight():"+getMeasuredHeight());
//		System.out.println(TAG+" marginY:"+ marginY);
//		System.out.println(TAG+" rows:"+ rows);
//		System.out.println(TAG+" space:"+ space);
//		System.out.println(TAG+" size:"+ size);
		padY = (getMeasuredHeight()-marginY*2-size*rows-space*(rows-1))/2;
		//System.out.println(TAG+" padY:"+padX );
		for (int i = 0; i < getChildCount(); i++) {
			if(i == getChildCount()-1){
				View child = getChildAt(i);
				int left = getMeasuredWidth()/2 - size/2;
				int right = left + size;
				int top = (size + space) * 5 + marginY+padY;
				int bottom = top + size;
				child.layout(left, top, right, bottom);
				break;
			}
            View child = getChildAt(i);
			int[] position = findPosition(i);
            int left = (size + space) * position[1] + padX;
            int top = (size + space) * position[0] + marginY+padY;
            int right = left + size;
            int bottom = top + size;
            child.layout(left, top, right, bottom);
		}
	}
	
	private int[] findPosition(int childNum) {
        int[] position = new int[2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if ((i * columns + j) == childNum) {
                    position[0] = i;//行
                    position[1] = j;//列
                    break;
                }
            }
        }
        return position;
    }
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		//System.out.print("OnDraw");
		//System.out.println("Colortable"+getMeasuredWidth()+" "+getMeasuredHeight());
		mPaint.setColor(Color.parseColor("#ffffff"));
		canvas.drawRoundRect(new RectF(marginX, marginY, getMeasuredWidth()-marginX, getMeasuredHeight()-marginY/*(padY-padx2*2)*/), 
				4, 4, mPaint);
//		mPaint.setColor(MyApplication.mThemeColor);
//		canvas.drawRoundRect(new RectF(padX, mNightModeStart, getMeasuredWidth()-padX, getMeasuredHeight()-padY-padX/*(padY-padx2*2)*/), 
//				10, 10, mPaint);
	}
}
