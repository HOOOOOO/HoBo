package com.example.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

public class MyImageView extends ImageView {
     
    private int picwith;
	private int picHeight;

	public MyImageView(Context context) {
        super(context);
    }
     
    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    public MyImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
     
    @Override
    public void setImageBitmap(Bitmap bm) {
        
        /*WindowManager wm= (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        picwith = metrics.widthPixels;
        picHeight = metrics.heightPixels;
        if(picwith / bm.getWidth() * bm.getHeight() > picHeight){
        	Matrix matrix = new Matrix(); 
			matrix.postScale((float)(picwith / bm.getWidth()),(float)(picwith / bm.getWidth()));
			
			bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
	    	//ImageView userimage = (ImageView) findViewById(R.id.imageView_bolur);
			Log.e("setImageBitmap", "ing");
        }*/
        super.setImageBitmap(bm);
    }
 
}