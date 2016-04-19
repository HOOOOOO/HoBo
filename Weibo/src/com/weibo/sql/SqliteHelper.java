package com.weibo.sql;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.openapi.models.User;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqliteHelper extends SQLiteOpenHelper{
	
	
	public static final String USERID = "id";
	public static final String TOKEN = "token";
	public static final String REFRESHTOKEN = "refreshtoken";
	public static final String PHONENUM = "phonenum";
	public static final String ExpiresTime = "expirestime";
	
	public static final String USERNAME = "screen_name";
	public static final String LOCATION = "location";
	public static final String GENDER = "gender";
	public static final String FOLLOWER = "followers_count";
	public static final String FRIEND = "friends_count";
	public static final String STATUS = "statuses_count";
	public static final String VERIFIED = "verified_reason";
	public static final String USERICONLARGE = "avatar_large";
	public static final String USERICONHD = "avatar_hd";
	
	public static final String GROUPID = "group_id";
	public static final String GROUPNAME = "group_name";
	
	//用来保存 UserID、Access Token、ExpiresTime的表名
	public static final String TB_NAME="users";
	public static final String TB_NAME_TOKEN="users_token";
	public SqliteHelper(Context context, String name, CursorFactory factory, int version) {
	super(context, name, factory, version);
	}
	//创建表
	@Override
	public void onCreate(SQLiteDatabase db) {
    db.execSQL("CREATE TABLE IF NOT EXISTS " + TB_NAME + "(" + 
    	 "id varchar primary key," +   //0
    	 "screen_name varchar," +      //1
    	 "location varchar," +         //2  
    	 "gender varchar," +           //3
     	 "followers_count integer," +  //4
    	 "friends_count integer," +    //5
    	 "statuses_count integer," +   //6
    	 "verified_reason varchar," +  //7
    	 "avatar_large varchar," +     //8
    	 "avatar_hd varchar" +         //9
    	  ")");
    db.execSQL("CREATE TABLE IF NOT EXISTS " + TB_NAME_TOKEN + "(" + 
       	 "id varchar primary key," +   //0
       	 "token varchar," +            //1
       	 "refreshtoken varchar," +     //2
       	 "phonenum varchar," +         //3
       	 "expirestime integer" +       //4
       	  ")");
	Log.e("Database","onCreate");
	
	
	}
	
	
	
	
	//更新表
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
	onCreate(db);
	Log.e("Database","onUpgrade");
	}
	
	//更新列
	public void updateColumn(SQLiteDatabase db, String oldColumn, String newColumn, String typeColumn){
	try{
	db.execSQL("ALTER TABLE " +
	TB_NAME + " CHANGE " +
	oldColumn + " "+ newColumn +
	" " + typeColumn
	);
	}catch(Exception e){
	e.printStackTrace();
	}
	}
}
