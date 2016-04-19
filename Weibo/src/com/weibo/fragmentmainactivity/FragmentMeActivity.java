package com.weibo.fragmentmainactivity;  
  
import com.example.view.PullToZoomScrollView;
import com.example.view.PullToZoomScrollView.OnScrollViewListener;
import com.example.view.RoundImageView;
import com.example.weibo.R;
import com.weibo.sql.DataHelper;
import com.weibo.tools.Blur;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;  
  
/** 
 * @author yangyu 
 *  功能描述：首页fragment页面 
 */  
public class FragmentMeActivity extends Fragment implements OnScrollViewListener{  
  
	private String BLURRED_IMG_PATH = "blurred_user1123.png";
    private View mParent;  
    private FragmentActivity mActivity;  
    //private ListView mListView;
    //private List<WeiBoInfo> wblist;
    //private ImageView imageView;
	private int screenWidth;
	private Drawable drawable;
	private View mView;
	private LinearLayout lin1, lin2 , barfloat;
	private PullToZoomScrollView pullToZoomScrollView;
	//private TitleView titleView;
	private float titleViewHeight;
	private ViewTreeObserver vto;
	private boolean isMeasured = false;
	private View view;
    private TextView mTitleText;
	//private RelativeLayout mRelativeLayout;
    /** 
     * Create a new instance of DetailsFragment, initialized to show the text at 
     * 'index'. 
     */  
    public static FragmentMeActivity newInstance(int index) {  
        FragmentMeActivity f = new FragmentMeActivity();  
        // Supply index input as an argument.  
        Bundle args = new Bundle();  
        args.putInt("index", index);  
        f.setArguments(args);  
        return f;  
    }  
  
    public int getShownIndex() {  
        return getArguments().getInt("index", 0);  
    }  
  
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        View view = inflater.inflate(R.layout.activity_fragment_me, container, false);  
        return view;  
    }  
  
    @Override  
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);

        mView = getView();
        mActivity = getActivity();
        
        //titleView = (TitleView) mView.findViewById(R.id.title);
       // titleView.bringToFront();
       // titleView.setTitle("我的主页");
        
        mTitleText = (TextView) mView.findViewById(R.id.titletext);
        mTitleText.bringToFront();
        
        view = (View) mView.findViewById(R.id.titlebackground);
        lin1 = (LinearLayout) mView.findViewById(R.id.lin1);
        lin1.bringToFront();
        lin2 = (LinearLayout)mView.findViewById(R.id.lin2);
        barfloat = (LinearLayout) mView.findViewById(R.id.me_choose_bar);
        DataHelper dataHelper = new DataHelper(mActivity);
    	//List<UserInfo> user = dataHelper.GetUserList(false);
    	//UserInfo userInfo = new UserInfo();
    	//userInfo = user.get(0);
    	//String userName = userInfo.userName;
    	//Bitmap usericon = userInfo.userIcon;
    	
    	RoundImageView roundImageView = (RoundImageView) mView.findViewById(R.id.user_image);
    	//roundImageView.setImageBitmap(usericon);
    	//roundImageView.setVisibility(View.INVISIBLE);
    	//roundImageView.setImageDrawable(useicon);
    	Blur blur = new Blur();
    	//Bitmap usericon_blur = blur.fastblur(mActivity, usericon, 20);
    	WindowManager wm= (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
    	int picwith = metrics.widthPixels;
    	Log.e("picwith", String.valueOf(metrics.widthPixels));
    	//Log.e("usericonblurwidth", String.valueOf(usericon_blur.getWidth()));
    	//Log.e("usericonblurheigth", String.valueOf(usericon_blur.getHeight()));
    	Matrix matrix = new Matrix(); 
		//matrix.postScale((float)(picwith / usericon_blur.getWidth()),(float)(picwith / usericon_blur.getWidth()));
		//Bitmap result = Bitmap.createBitmap(usericon_blur, 0, 0, usericon_blur.getWidth(), usericon_blur.getHeight(), matrix, true);
    	ImageView userimage = (ImageView) mView.findViewById(R.id.imageView_bolur);
    	//userimage.setImageBitmap(result);
    	
    	ImageButton zhuxiao = (ImageButton) mView.findViewById(R.id.zhuxiao);
    	zhuxiao.bringToFront();
    	zhuxiao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*AccessTokenKeeper.clear(mActivity);
				Intent intent = new Intent();
				intent.setClass(mActivity, AuthorizeActivity.class);
				startActivity(intent);*/
				LinearLayout layout = (LinearLayout) mView.findViewById(R.id.me_choose_bar);
				layout.bringToFront();
				layout.setVisibility(View.VISIBLE);
			}
		});
    	
    	pullToZoomScrollView = (PullToZoomScrollView) mView.findViewById(R.id.pullzoom);
    	pullToZoomScrollView.setOnScrollViewListener(this);

    	int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        //titleView.measure(w, h);
        //titleViewHeight = titleView.getMeasuredHeight();
        mTitleText.measure(w, h);
        titleViewHeight = mTitleText.getMeasuredHeight();
        //Log.e("titleViewHeight", String.valueOf(titleView.getMeasuredHeight()));
        //int width = imageView.getMeasuredWidth();
    }
    
  //监听滚动Y值变化，通过addView和removeView来实现悬停效果
  	@Override
  	public void onScroll(float scrollY, float topheight) {
  		Log.e(String.valueOf(scrollY), String.valueOf(topheight));
  		if(scrollY >= (topheight - titleViewHeight)){  
  			if (barfloat.getParent()!= lin1) {
  				//titleView.setVisibility(View.VISIBLE);
  				//view.bringToFront();
  				//titleView.bringToFront();
  				//titleView.setVisibility(View.VISIBLE);
  				mTitleText.setVisibility(View.VISIBLE);
  				view.setVisibility(View.VISIBLE);
  				lin2.removeView(barfloat);
  				lin1.addView(barfloat);
  			}
          }else{
          	if (barfloat.getParent()!=lin2) {
          		mTitleText.setVisibility(View.INVISIBLE);
          		//titleView.setVisibility(View.INVISIBLE);
          		//view.setVisibility(View.INVISIBLE);
          		lin1.removeView(barfloat);
          		lin2.addView(barfloat);
  			}
          }
  	}
    
  
}  