package com.weibo.sql;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.openapi.models.Group;
import com.sina.weibo.sdk.openapi.models.GroupList;
import com.sina.weibo.sdk.openapi.models.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class DataHelper {
	//数据库名称
	private static String DB_NAME = "mysinaweibo.db";
	//数据库版本
	private static int DB_VERSION = 2;
	private SQLiteDatabase db;
	private SqliteHelper dbHelper;

	public DataHelper(Context context){
	dbHelper = new SqliteHelper(context,DB_NAME, null, DB_VERSION);
	db = dbHelper.getWritableDatabase();
	}

	public void Close(){
		db.close();
	    dbHelper.close();
	}
	//获取users表中的的记录
	public List<User> GetUserList(Boolean isSimple)
	{
		ArrayList<User> userList = new ArrayList<User>();
		Cursor cursor = db.query(SqliteHelper.TB_NAME, null, null, null, null, null, null);//DESC降序
		cursor.moveToFirst();
		while(!cursor.isAfterLast()&& (cursor.getString(1)!=null)){
		User user=new User();
		user.id = cursor.getString(0);
	    user.screen_name = cursor.getString(1);
	    userList.add(user);
	    cursor.moveToNext();
	}
		cursor.close();
		return userList;
	}

	//判断users表中的是否包含某个UserID的记录
	public Boolean HaveUserInfo(String UserId)
	{
		Boolean b=false;
		Cursor cursor=db.query(SqliteHelper.TB_NAME, null, SqliteHelper.USERID + "=" + UserId, null, null, null, null);
		b=cursor.moveToFirst();
		Log.e("HaveUserInfo",b.toString());
		cursor.close();
		return b;
	}

	public User getUserByUid(String UserId) {
		Cursor cursor=db.query(SqliteHelper.TB_NAME, null, SqliteHelper.USERID + "=" + UserId, null, null, null, null);
		cursor.moveToFirst();
		User user = new User();
		user.screen_name = cursor.getString(1);
		user.gender = cursor.getString(3);
		user.followers_count = cursor.getInt(4);
		user.friends_count = cursor.getInt(5);
		user.statuses_count = cursor.getInt(6);
		user.verified_reason = cursor.getString(7);
		user.avatar_large = cursor.getString(8);
		user.avatar_hd = cursor.getString(9);
		return user;
	}
	
	//更新users表的记录，根据UserId更新用户信息
	public int UpdateUserInfo(User user)
	{
	ContentValues values = new ContentValues();
	values.put(SqliteHelper.USERNAME, user.screen_name);
	values.put(SqliteHelper.GENDER, user.gender);
	values.put(SqliteHelper.LOCATION, user.location);
	values.put(SqliteHelper.FOLLOWER, user.followers_count);
	values.put(SqliteHelper.STATUS, user.statuses_count);
	values.put(SqliteHelper.FRIEND, user.friends_count);
	values.put(SqliteHelper.VERIFIED, user.verified_reason);
	values.put(SqliteHelper.USERICONLARGE, user.avatar_large);
	values.put(SqliteHelper.USERICONHD, user.avatar_hd);
	int id= db.update(SqliteHelper.TB_NAME, values, SqliteHelper.USERID + "=" + user.id, null);
	Log.e("UpdateUserInfo2",id+"");
	return id;
	}

	//添加users表的记录  user.id user.token user.expirestime
	public Long SaveUserInfo(Oauth2AccessToken accessToken)
	{
		Log.e("accessToken.getUid()", accessToken.getUid());
		Log.e("accessToken.getToken()", accessToken.getToken());
		ContentValues values = new ContentValues();
		values.put(SqliteHelper.TOKEN, accessToken.getToken());
		values.put(SqliteHelper.REFRESHTOKEN, accessToken.getRefreshToken());
		values.put(SqliteHelper.PHONENUM, accessToken.getPhoneNum());
		values.put(SqliteHelper.ExpiresTime, accessToken.getExpiresTime());
		
		if(HaveUserInfo(accessToken.getUid())){
			int id= db.update(SqliteHelper.TB_NAME_TOKEN, values, SqliteHelper.USERID + "=" + accessToken.getUid(), null);
			return (long) id;
		}
	    values.put(SqliteHelper.USERID, accessToken.getUid());
	    Long uid = db.insert(SqliteHelper.TB_NAME_TOKEN, accessToken.getUid(), values);
	    Log.e("SaveUserInfo",uid+"");
	    ContentValues values1 = new ContentValues();
	    values1.put(SqliteHelper.USERID, accessToken.getUid());
	    db.insert(SqliteHelper.TB_NAME, accessToken.getUid(), values1);
	    return uid;
	}
	
	public Oauth2AccessToken getAccessToken(String Uid) {
		Oauth2AccessToken accessToken = new Oauth2AccessToken();
		Cursor cursor=db.query(SqliteHelper.TB_NAME_TOKEN, null, SqliteHelper.USERID + "=" + Uid, null, null, null, null);
		if(cursor.moveToFirst()) {
		Log.e("cursor.getString(0)", cursor.getString(0));
		accessToken.setUid(cursor.getString(0));
		accessToken.setToken(cursor.getString(1));
		accessToken.setRefreshToken(cursor.getString(2));
		accessToken.setExpiresTime(cursor.getLong(4));
		return accessToken;}
		//Log.e("cursor.moveToFirst()", ""+cursor.moveToFirst());
		return null;
	}
	
	public int DelTokenInfo(String userID) {
		int id= db.delete(SqliteHelper.TB_NAME_TOKEN, SqliteHelper.USERID +"="+userID, null);
		Log.e("DelUserInfo",id+"");
		return id;
	}

	//删除users表的记录
	public int DelUserInfo(String UserId){
	int id= db.delete(SqliteHelper.TB_NAME, SqliteHelper.USERID +"="+UserId, null);
	Log.e("DelUserInfo",id+"");
	return id;
	}
	
	public void CreateGroupTable(String uid) {
		db.execSQL("CREATE TABLE IF NOT EXISTS " + "group"+uid+  "(" + 
		    	 "group_id varchar primary key," +   //0
		    	 "group_name varchar," +      //1
		    	  ")");
	}
	
	public void SaveGroupInfo(String uid, ArrayList<Group> groupList) {
		db.delete(uid+"group", null, null);
		for(int i = 0; i < groupList.size(); i++){
			ContentValues contentValues = new ContentValues();
			contentValues.put(SqliteHelper.GROUPID, groupList.get(i).idStr);
			contentValues.put(SqliteHelper.GROUPNAME, groupList.get(i).name);
			Long result = db.insert("group"+uid, "0", contentValues);
		}
	}
	
	public ArrayList<Group> GetGroupInfo(String uid) {
		ArrayList<Group> groups = new ArrayList<Group>();
		Cursor cursor=db.query("group"+uid, null, null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()&& (cursor.getString(1)!=null)){
			Group group= new Group();
			group.idStr = cursor.getString(0);
			group.name = cursor.getString(1);
			groups.add(group);
			cursor.moveToNext();
		}
		return groups;
	}

}
