package com.example.view;

import com.weibo.tools.GetImageWidthAndHeigth;

/**
 * Created by Pan_ on 2015/2/3.
 */
public class Image {
    private String url;
    private int width;
    private int height;
    private int size;

    public Image(String url, int width, int height) {
        this.url = url;
        this.width = width;
        this.height = height;
        /*GetImageWidthAndHeigth getImageWidthAndHeigth = new GetImageWidthAndHeigth(){
        	protected void onPostExecute(int[] result) {
        		size = result[0];
        		System.out.print("Image size:"+size);
        	};
        };
        getImageWidthAndHeigth.execute(url);*/
        //L.i(toString());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {

        return "image---->>url="+url+"width="+width+"height"+height;
    }
}
