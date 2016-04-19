package com.example.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.example.weibo.ImagePagerActivity;
import com.example.weibo.R;
import com.squareup.picasso.Picasso;
import com.weibo.tools.CropSquareTransformation;
import com.weibo.tools.FitInsideTransformation;
import com.weibo.tools.GetImageByBounds;
import com.weibo.tools.GetImageWidthAndHeigth;
import com.weibo.tools.MyApplication;

@SuppressWarnings("unused")
public class CustomImageView extends ImageView {
    private String url;
    private List<Image> listData;
    private boolean isAttachedToWindow;
	private int mPositive;
	private boolean mIsLargeImage = false;
	private Paint mPaint;

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
    }

    public CustomImageView(Context context) {
        super(context);
        mPaint = new Paint();
        setScaleType(ScaleType.CENTER);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

       /* switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Drawable drawable= getDrawable();
                if(drawable!=null) {
                    drawable.mutate().setColorFilter(Color.GRAY,
                            PorterDuff.Mode.MULTIPLY);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Drawable drawableUp= getDrawable();
                if(drawableUp!=null) {
                    drawableUp.mutate().clearColorFilter();
                }
                break;
        }*/

        return super.onTouchEvent(event);
    }

    @Override
    public void onAttachedToWindow() {
        isAttachedToWindow = true;
        setImageUrl(url, listData, mPositive);
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        Picasso.with(getContext()).cancelRequest(this);
        isAttachedToWindow = false;
        setImageBitmap(null);
        super.onDetachedFromWindow();
    }


    public void setImageUrl(String url, List<Image> imageList, int position) {
        final String imageUrl = url;
        mPositive = position;
    	if (!TextUtils.isEmpty(url)) {
            this.url = url;
            //System.out.println(url);
            listData = imageList;
            if (isAttachedToWindow) {
            	this.setOnClickListener(new OnClickListener() {
    				@Override
    				public void onClick(View v) {
    					// TODO Auto-generated method stub
    					Intent intent = new Intent();
        				intent.setClass(getContext(), ImagePagerActivity.class);
        				ArrayList<String> urls = new ArrayList<String>();
        				for(int k = 0; k < listData.size(); k++){
        					urls.add(listData.get(k).getUrl());
        				}
        				intent.putStringArrayListExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        				intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, mPositive);
        				//Toast.makeText(getContext(), String.valueOf(mPositive), Toast.LENGTH_LONG).show();;
        				getContext().startActivity(intent); 
    				}
    			});
            	
            	//loadImage(listData.size() == 1);
            	loadImage(true);
            }
        }
    }
    
    public void loadImage(boolean isOne) {
    	Picasso.with(getContext()).load(url)
		.transform(new FitInsideTransformation(getHeight(), isOne))
		.into(this);
	}
    
    @Override
    protected void onDraw(Canvas canvas) {
    	// TODO Auto-generated method stub
    	if(MyApplication.mIsNightMode)
    		canvas.drawColor(getResources().getColor(R.color.colorThemeBlue4));
    	else
    		canvas.drawColor(Color.parseColor("#f5f5f5"));
    	super.onDraw(canvas);
    }
}
