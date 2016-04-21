package com.weibo.fragment;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.example.weibo.R;
import com.squareup.picasso.Picasso;
import com.weibo.tools.FitcenterTransformation;
import com.weibo.tools.GetImageWidthAndHeigth;

/**
 * 单张图片显示Fragment
 */
public class ImageShowFragment extends Fragment {
	private String mImageUrl;
	private PhotoView mImageView;
	private ProgressBar mProgressBar;
	private PhotoViewAttacher mAttacher;
	private int picwith;
	private int picHeight;
	private WebView mWebViewLargeImage;
	private boolean isLargeImage;
	
	public static ImageShowFragment newInstance(String imageUrl) {
		final ImageShowFragment f = new ImageShowFragment();

		final Bundle args = new Bundle();
		args.putString("url", imageUrl);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.image_show_fragment, container, false);
		mImageView = (PhotoView) v.findViewById(R.id.image);
		mProgressBar = (ProgressBar) v.findViewById(R.id.loading);
		mWebViewLargeImage = (WebView) v.findViewById(R.id.wv_large_image);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
        System.out.println(mImageUrl);
        
        mProgressBar.setVisibility(View.VISIBLE);
		if(mImageUrl.contains("thumbnail")){
			mImageUrl = mImageUrl.replace("thumbnail", "bmiddle");
			System.out.println(mImageUrl);
		}
		else if(mImageUrl.contains("bmiddle")){
			mImageUrl = mImageUrl.replace("bmiddle", "large");
			System.out.println(mImageUrl);
		}
		if(mImageUrl.contains(".gif") || mImageUrl.contains(".GIF"))
			loadLargeImage();
		else {
			GetImageWidthAndHeigth getImageWidthAndHeigth = new GetImageWidthAndHeigth(){

				protected void onPostExecute(int[] result) {
					if(result != null){
					if((result[2] / result[1]) >= 2){
						isLargeImage = true;
						loadLargeImage();
						return;
					}
					else {
						isLargeImage = false;
						loadNormalImage();
					}
					}else{
						loadNormalImage();
					}
				};
			};
			getImageWidthAndHeigth.execute(mImageUrl, "0");
		}
	}
	
	protected void loadNormalImage() {
		mImageView.setVisibility(View.VISIBLE);
		mWebViewLargeImage.setVisibility(View.GONE);
		// TODO Auto-generated method stub
		try {
			Picasso.with(getActivity()).load(mImageUrl).transform(new FitcenterTransformation(getActivity())).into(mImageView);
			mProgressBar.setVisibility(View.GONE);
		} catch (OutOfMemoryError omException) {
			// TODO: handle exception
			System.out.println(""+omException);
			loadLargeImage();
		}
		//mAttacher = new PhotoViewAttacher(mImageView);
	}

	private void loadLargeImage(){
		mImageView.setVisibility(View.GONE);
		mWebViewLargeImage.setBackgroundColor(0); // 设置背景色
		mWebViewLargeImage.getBackground().setAlpha(255); // 设置填充透明度 范围：0-255
		mWebViewLargeImage.getSettings().setUseWideViewPort(true);
		mWebViewLargeImage.getSettings().setLoadWithOverviewMode(true);
		mWebViewLargeImage.getSettings().setBuiltInZoomControls(true);
		mWebViewLargeImage.getSettings().setDisplayZoomControls(false);
		String data = "<html>"
				+ "<style>body{background:#000000;text-align:center;}"
				+"img{width:100%; vertical-align:middle;}" 
				+"span{display:table-cell; vertical-align:middle;}"
				+"div{align:center; margin:0 auto; vertical-align:middle; text-align:center; display:table; width:100%; height:100%}</style>"
				+ "<body><div><span><img src = \""+mImageUrl+"\" onload=\"setCenter(this)\"></span></div></body></html>";
		String baseUrl = null;
		String mimeType = "text/html";
		String encoding = "utf-8";
		String historyUrl = null;
		mWebViewLargeImage.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
		mProgressBar.setVisibility(View.GONE);
		mWebViewLargeImage.setVisibility(View.VISIBLE);
	}
}
