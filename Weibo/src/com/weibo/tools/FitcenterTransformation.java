package com.weibo.tools;

import com.squareup.picasso.Transformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

public class FitcenterTransformation implements Transformation{

	public Context mContext;
	public FitcenterTransformation(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
	}
	
	@Override
	public String key() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bitmap transform(Bitmap source) {
		// TODO Auto-generated method stub
		ScreenTools screenTools=ScreenTools.instance(mContext);
        int totalWidth=screenTools.getScreenWidth();
        int totalHeight = screenTools.getScreenHeight();
        Matrix matrix = new Matrix(); 
		
		float f = (float)totalWidth / source.getWidth();
		matrix.postScale(f,f);
		Bitmap result = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
		if (result != source) {
		      source.recycle();
		    }
		return result;
	}

}
