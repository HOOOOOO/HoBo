package com.weibo.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.example.view.RoundImageView;

import android.R.mipmap;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class GetUserImgByPath extends AsyncTask<RoundImageView, Void, Bitmap>{
	
	private String mId;
	private RoundImageView mView;
	public GetUserImgByPath(String id) {
		// TODO Auto-generated constructor stub
		this.mId = id;
	}
	@Override
	protected Bitmap doInBackground(RoundImageView... params) {
		// TODO Auto-generated method stub
		this.mView = params[0];
		File file = new File(MyApplication.getContext().getFilesDir().getAbsolutePath()+MyApplication.PATH_OF_USERICON+"/"+mId+".jpg");
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			return BitmapFactory.decodeStream(fileInputStream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Bitmap result) {
		// TODO Auto-generated method stub
		this.mView.setImageBitmap(result);
		super.onPostExecute(result);
	}

}
