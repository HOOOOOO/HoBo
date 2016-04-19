package com.weibo.tools;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

import com.weibo.sql.DataHelper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;

public class GetUserImageByURL extends AsyncTask<String, Void, Bitmap>{

	@Override
	protected Bitmap doInBackground(String... params) {
		// TODO Auto-generated method stub
		RandomAccessFile raf = null;
		try {
			URL url = new URL(params[0]);
			String uerid = params[1];
			File filedire = new File(MyApplication.getContext().getFilesDir().getAbsolutePath()+MyApplication.PATH_OF_USERICON);
			if (!filedire.exists()) {
				filedire.mkdir();
			}
			File file = new File(filedire.getPath()+"/"+uerid+".jpg");
			if(file.exists()){
				FileInputStream fileInputStream = new FileInputStream(file);
				return BitmapFactory.decodeStream(fileInputStream);
			}
			URLConnection connection = url.openConnection();
			System.out.println(file.getPath());
			BufferedInputStream bufferstream = new BufferedInputStream(connection.getInputStream());
			raf = new RandomAccessFile(file, "rwd");
			raf.seek(0);
			byte[] buffer = new byte[1024];
			int len = 0;
			while((len = bufferstream.read(buffer, 0 , 1024)) != -1){
				raf.write(buffer, 0, len);
				System.out.println(len);
			}
			Bitmap bitmap = BitmapFactory.decodeStream(bufferstream);
			System.out.println("保存用户头像成功");
			return bitmap;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
