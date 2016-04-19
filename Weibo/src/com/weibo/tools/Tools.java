package com.weibo.tools;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.weibo.UserPageActivity;

import android.R.string;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;

public class Tools {
	
	private final static String[] DAY= new String[]{
			"","星期一","星期二","星期三","星期四","星期五","星期六", "星期日"
	};
	private final static String[] MONTH= new String[]{
			"Jan","Feb","Mar","Apr","May","Jun","Jul", "Aug", "Sep","Oct", "Nov", "Dec"
	};
	
	@SuppressWarnings("deprecation")
	public static String getTimeFormat(String time) {
		// TODO Auto-generated method stub
		Calendar cal = Calendar.getInstance();
		int year =cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int date = cal.get(Calendar.DATE);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		if(year == Integer.parseInt(time.substring(26, 30))){
			if(MONTH[month].equals(time.substring(4, 7))){
				if(date == Integer.parseInt(time.substring(8, 10))){
					if(hour == Integer.parseInt(time.substring(11, 13))){
						if(minute == Integer.parseInt(time.substring(14, 16))){
							//System.out.println("同分钟");
							return "刚刚";
						}
						else{
							//System.out.println("同小时不同分钟");
							return String.valueOf(Math.abs(minute-Integer.parseInt(time.substring(14, 16))))+"分钟前";
						}
					}
					else{
						//System.out.println("同天不同小时");
						return String.valueOf(hour-Integer.parseInt(time.substring(11, 13)))+"小时前";
					}
				}
				else{
					//System.out.println("同月不同天");
					if((date - Integer.parseInt(time.substring(8, 10))) == 1){
						return "昨天 "+time.substring(11, 16);
					}
					
					else{
						return getNumMonth(time.substring(4, 7))+"."+time.substring(8, 16);
					}
				}
			}
			else{
				//System.out.println("同年不同月");
				return getNumMonth(time.substring(4, 7))+"."+time.substring(8, 16);
			}
		}else{
			//System.out.println("不同年");
			return time.substring(26, 30)+"."+getNumMonth(time.substring(4, 7))+"."+time.substring(8, 16);
		}
		
	}
	
	public static String getNumMonth(String mon){
		switch (mon) {
			case "Jan":
				return "1";
			case "Feb":
				return "2";
			case "Mar":
				return "3";
			case "Apr":
				return "4";
			case "May":
				return "5";
			case "Jun":
				return "6";
			case "Jul":
				return "7";
			case "Aug":
				return "8";
			case "Sep":
				return "9";
			case "Oct":
				return "10";
			case "Nov":
				return "11";
			case "Dec":
				return "12";
			default:
				return null;
		}
	}
	
	public static void goToUserPage(Context context, String[] strings) {
		// TODO Auto-generated method stub
    	Intent intent = new Intent();
    	intent.setClass(context, UserPageActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("screen_name", strings[0]);
		bundle.putString("avatar_large", strings[1]);
		bundle.putString("followers_count", strings[2]);
		bundle.putString("friends_count", strings[3]);
		bundle.putString("statuses_count", strings[4]);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}
	
	public static String getTagContent(String source) {
		
		if(source.length() == 0)
			return "";
		int indexA = 0, indexB = 0;
		for(int i = 0; i < source.length(); i++){
			if(source.charAt(i) == '>'){
				indexA = i;
				break;
			}
		}
		for(int i = 0; i < source.length(); i++){
			if(source.charAt(i) == '<' && i > indexA){
				indexB = i;
				break;
			}
		}
		return (String) source.subSequence(indexA+1, indexB);
	}
}
