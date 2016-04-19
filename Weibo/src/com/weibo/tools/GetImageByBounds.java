package com.weibo.tools;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.renderscript.Sampler.Value;

public class GetImageByBounds extends AsyncTask<String, Void, Bitmap>{
	
	private static final BitmapFactory.Options options = new BitmapFactory.Options();

    static
    {
        options.inPreferredConfig = Bitmap.Config.RGB_565;
    }

	@Override
	protected Bitmap doInBackground(String... params) {
		// TODO Auto-generated method stub
		try {
			params[0]=params[0].replace("thumbnail", "bmiddle");
			URL url = new URL(params[0]);
			URLConnection urlConnection;
			urlConnection = url.openConnection();
			BitmapFactory.Options tmpOptions = new BitmapFactory.Options();
            // Grab the bounds for the scene dimensions
            tmpOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(urlConnection.getInputStream(), null, tmpOptions);
            int imageWidth = tmpOptions.outWidth;
            int imageHeight = tmpOptions.outHeight;
            System.out.print(imageWidth+" "+imageHeight);
			BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(urlConnection.getInputStream(), false);
			return decoder.decodeRegion(new Rect(0, 0, imageWidth, imageWidth) ,options);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}

}
