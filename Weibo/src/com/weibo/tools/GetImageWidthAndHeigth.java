package com.weibo.tools;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class GetImageWidthAndHeigth extends AsyncTask<String, Void, int[]>{

	@Override
	protected int[] doInBackground(String... params) {
		// TODO Auto-generated method stub
		try {
			URL url = new URL(params[0]);
			URLConnection connection = url.openConnection();
			int[] l = new int[4];
			l[0] = connection.getContentLength();
			
			BitmapFactory.Options tmpOptions = new BitmapFactory.Options();
            // Grab the bounds for the scene dimensions
            tmpOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(connection.getInputStream(), null, tmpOptions);
            l[1] = tmpOptions.outWidth;
            l[2] = tmpOptions.outHeight;
            l[3] = Integer.parseInt(params[1]);
			return l;
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
