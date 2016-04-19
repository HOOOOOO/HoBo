package com.example.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.weibo.R;
import com.example.weibo.UserPageActivity;
import com.weibo.tools.EmotionUtils;
import com.weibo.tools.MyApplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class WeiboTextView extends TextView{

	
	// 定义正则表达式
    private static final String AT = "@[\u4e00-\u9fa5\\-\\_\\w]+";// @人
    private static final String TOPIC = "#[\u4e00-\u9fa5\\w]+#";// ##话题
    private static final String EMOJI = "\\[[\u4e00-\u9fa5\\w]+\\]";// 表情
    private static final String URL = "http://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";// url
    private static final String REGEX = "("+AT+")"+"|"+"("+TOPIC+")"+"|"+"("+EMOJI+")"+"|"+"("+URL+")";
    
	public WeiboTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public WeiboTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public WeiboTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public void setText(String text) {
		// TODO Auto-generated method stub
		SpannableString spannableString = spanWeiboText(text);
		setHighlightColor(Color.GRAY);
		super.setText(spannableString);
	}
	
	private SpannableString spanWeiboText(String wString){
		
		SpannableString wSpannableString = new SpannableString(wString);
		Pattern tmpPattern = Pattern.compile(URL);
		Matcher tmpMatcher = tmpPattern.matcher(wSpannableString);
//		while(tmpMatcher.find()){
//			int start = tmpMatcher.start();
//			int end = tmpMatcher.end();
//			String url = wString.substring(start, end);
//			wString = wString.replace(url, "[链接]链接");
//			
//		}
		
		
		
		Pattern pattern = Pattern.compile(REGEX);
		Matcher matcher = pattern.matcher(wSpannableString);
		
		if(matcher.find()){
			setMovementMethod(LinkMovementMethod.getInstance());
			matcher.reset();
		}
		while(matcher.find()){
			 final String at = matcher.group(1);
			 final String topic = matcher.group(2);
			 String emoji = matcher.group(3);
			 final String url = matcher.group(4);
			 
			 if(at != null){
				// System.out.println("@"+at);
				 wSpannableString.setSpan(new ClickableSpan() {
					
					@Override
					public void onClick(View widget) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
				    	intent.setClass(getContext(), UserPageActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("screen_name", at.substring(1));
						bundle.putString("avatar_large", null);
						bundle.putInt("followers_count", 0);
						bundle.putInt("friends_count", 0);
						bundle.putInt("statuses_count", 0);
						intent.putExtras(bundle);
						getContext().startActivity(intent);
					}
					public void updateDrawState(android.text.TextPaint ds) {
						super.updateDrawState(ds);
						ds.setUnderlineText(false);
						//ds.setColor(MyApplication.mHighLightTextColor);
						ds.setFakeBoldText(true);
					};
				}, matcher.start(1), matcher.start(1)+at.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			 }
			 
			 if(topic != null){
				 //System.out.println("#"+topic+"#");
				 wSpannableString.setSpan(new ClickableSpan() {
						
						@Override
						public void onClick(View widget) {
							// TODO Auto-generated method stub
							//widget.setBackgroundColor(Color.BLUE);
							//avoidHintColor(widget);
						}
						public void updateDrawState(android.text.TextPaint ds) {
							super.updateDrawState(ds);
							//ds.setColor(MyApplication.mHighLightTextColor);
							ds.setFakeBoldText(true);
							ds.setUnderlineText(false);
						};
					}, matcher.start(2), matcher.start(2)+topic.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			 }
			 
			 if(emoji != null){
				 int emojiId = EmotionUtils.getImgByName(emoji);
				 Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), emojiId);
				 if(bitmap != null){
					 bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/2, bitmap.getHeight()/2, true);
					 wSpannableString.setSpan(new ImageSpan(getContext(), bitmap, ImageSpan.ALIGN_BASELINE), matcher.start(3), matcher.start(3)+emoji.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				 }
			 }
			 
			 if(url != null){
				// Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.timeline_card_small_web);
				 //wSpannableString.setSpan(new ImageSpan(getContext(), bitmap), matcher.start(4), matcher.start(4)+url.length(), ImageSpan.ALIGN_BASELINE);
				 
				 
				 wSpannableString.setSpan(new URLSpan(url){
					 @Override
					public void updateDrawState(TextPaint ds) {
						// TODO Auto-generated method stub
						super.updateDrawState(ds);
						//ds.setColor(MyApplication.mHighLightTextColor);
						//ds.setUnderlineText(false);
						
					}
				 }, matcher.start(4), matcher.start(4)+url.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				 /* wSpannableString.setSpan(new ClickableSpan() {
					
					@Override
					public void onClick(View widget) {
						// TODO Auto-generated method stub
						
					}
					public void updateDrawState(android.text.TextPaint ds) {
						super.updateDrawState(ds);
						ds.setUnderlineText(false);
					};
					
					
				}, matcher.start(4), matcher.start(4)+url.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);*/
			 }
		}
		return wSpannableString;
	}
	
	 private void avoidHintColor(View view){
         if(view instanceof TextView)
             ((TextView)view).setHighlightColor(Color.BLUE);
     }
}
