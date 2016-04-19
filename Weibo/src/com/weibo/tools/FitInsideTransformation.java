package com.weibo.tools;

import com.squareup.picasso.Transformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

public class FitInsideTransformation implements Transformation{
	
	private int mSize;
	private boolean mIsOne;
	public FitInsideTransformation(int size, boolean isOne) {
		// TODO Auto-generated constructor stub
		mSize = size;
		mIsOne = isOne;
	}
	
	@Override
	public Bitmap transform(Bitmap source) {
		// TODO Auto-generated method stub
		//System.out.println("fitInsize " + mSize);
		Matrix matrix = new Matrix();
		
		if(mIsOne){
			int height = source.getHeight();
			if(height > source.getWidth()*MyApplication.mRate)
				height = (int) (source.getWidth()*MyApplication.mRate);
			ScreenTools screentools = ScreenTools.instance(MyApplication.getContext());
	        int totalWidth = screentools.getScreenWidth() - screentools.dip2px(16);
			float f = (float) totalWidth/source.getWidth();
			matrix.postScale(f,f);
			Bitmap result = Bitmap.createBitmap(source, 0, 0, source.getWidth(), height, matrix, true);
			if(result != source)
				source.recycle();
			//System.out.println("fitInsize " + totalWidth);
		    return result;
		}
			//System.out.println("fitInsize " + mSize);
			int size = Math.min(source.getWidth(), source.getHeight());
		    int x = (source.getWidth() - size) / 2;
		    int y = (source.getHeight() - size) / 2;
		    float f = (float) mSize/size;
		    matrix.postScale(f,f);
		    Bitmap result = Bitmap.createBitmap(source, x, y, size, size, matrix, true);
		   // source.recycle();
		    if(result != source)
				source.recycle();
		    return result;
	}

	@Override
	public String key() {
		// TODO Auto-generated method stub
		return null;
	}

}
