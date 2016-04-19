package com.weibo.fragmentmainactivity;  
  
import java.util.ArrayList;
import java.util.List;
import com.example.view.CustomDialogFragment;
import com.example.view.CustomDialogFragment.DialogClickListener;
import com.example.weibo.R;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.legacy.FavoritesAPI;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.FavoriteList;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.sina.weibo.sdk.openapi.models.User;
import com.sina.weibo.sdk.utils.LogUtil;
import com.weibo.adapter.MainAdapter;
import com.weibo.adapter.MainAdapter.DialogListen;
import com.weibo.tools.AccessTokenKeeper;
import com.weibo.tools.Constants;
import com.weibo.tools.GetStatus;
import com.weibo.tools.MyApplication;
import com.weibo.tools.SaveStatus;
import com.weibo.tools.ScreenTools;

import android.widget.Button;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;  
  
public class FragmentHomeActivity extends Fragment implements OnRefreshListener{  
  
	public static final String HOME = "home";
	public static final String HOT = "hot";
	public static final String FAVORITE = "favorite";
	public static final String USER = "user";
	
    private View mParent;  
    private FragmentActivity mActivity;  
    private TextView mText;  
    private StatusesAPI mStatusesAPI;
    public Oauth2AccessToken mAccessToken;
    public static String TAG = "HomeActivity";
	private ListView mListView;
	private MainAdapter mAdapter;
	private List<Status> wblist;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private String mIndex;
	protected int start_index;
	protected int end_index;
	private boolean mLoadMore = false;
	protected boolean mLoadMoreFinish = false;
	protected int last_start_index = 0;
	protected int last_end_index = 0;
	private FavoritesAPI mFavoritesAPI;
	
	private boolean mLoadLocal = false;
	private boolean mLoadNewest = false;
	private long mNewestTime = 0L;
	private long mLatestTime = 0L;
	private Button mBtnLoadmore;
	private String mScreenName;
	protected boolean mLoadNewestFinished;
	private UsersAPI mUserAPI;
	
    /** 
     * Create a new instance of DetailsFragment, initialized to show the text at 
     * 'index'. 
     */  
    public static FragmentHomeActivity newInstance(String index , String userName) {  
        FragmentHomeActivity f = new FragmentHomeActivity();  
        // Supply index input as an argument.  
        Bundle args = new Bundle();  
        args.putString("index", index);  
        if(index == USER)
        	args.putString("screen_name", userName);  
        f.setArguments(args);  
        return f;  
    }  
  
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        View view = inflater.inflate(R.layout.activity_fragment_home, container, false); 
        mIndex = getArguments().getString("index", null);
        if(mIndex == USER)
        	mScreenName = getArguments().getString("screen_name");
        return view;  
    }  
  
    @Override  
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
        init();
        //loadStatus();
    }
    
    private void init() {
		// TODO Auto-generated method stub
    	mActivity = getActivity();  
        mParent = getView();  
        mListView = (ListView) mParent.findViewById(R.id.listView1);
        ScreenTools s = ScreenTools.instance(mActivity);
        
        mSwipeRefreshLayout = (SwipeRefreshLayout) mParent.findViewById(R.id.srl_refresh);
        mSwipeRefreshLayout.setColorSchemeColors(MyApplication.mTitleColor);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(MyApplication.mThemeColor);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        
        LayoutParams layoutParams = null;
        if(mIndex != USER){
        	View view = new View(mActivity);
        	layoutParams = new LayoutParams(LayoutParams.FILL_PARENT, s.dip2px(55));
        	view.setLayoutParams(layoutParams);
        	view.setBackgroundColor(Color.parseColor("#00000000"));
        	mListView.addHeaderView(view);
        	mSwipeRefreshLayout.setProgressViewOffset(false, s.dip2px(20), s.dip2px(65));
        }
        mBtnLoadmore = new Button(mActivity);
        mBtnLoadmore.setGravity(Gravity.CENTER);
        mBtnLoadmore.setText(" ");
        mBtnLoadmore.setTextSize(15);
        mBtnLoadmore.setTextColor(getResources().getColor(R.color.colorWeiboText1));
       // mBtnLoadmore.getPaint().setFakeBoldText(true);
        mBtnLoadmore.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        layoutParams = new LayoutParams(LayoutParams.FILL_PARENT, s.dip2px(50));
        mBtnLoadmore.setLayoutParams(layoutParams);
        mListView.addFooterView(mBtnLoadmore);
        
        mBtnLoadmore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("加载更多");
				mLoadMore = true;
				mLoadMoreFinish = false;
				mBtnLoadmore.setText("正在加载...");
				loadStatus();
			}
		});
        
        
        mAccessToken = AccessTokenKeeper.readAccessToken(mActivity);
        mStatusesAPI = new StatusesAPI(mActivity, Constants.APP_KEY, mAccessToken);
        mFavoritesAPI = new FavoritesAPI(mActivity, Constants.APP_KEY, mAccessToken);
        UsersAPI usersAPI = new UsersAPI(mActivity, Constants.APP_KEY, mAccessToken);
        
        wblist = new ArrayList<Status>();
        
        mAdapter = new MainAdapter(mActivity, wblist); 
        
		mAdapter.setDialogListen(new DialogListen() {
			
			@Override
			public void repost(final int position) {
				// TODO Auto-generated method stub
				Log.e("Fragment", wblist.get(position).user.screen_name);
				//AlertDialog dialog = AlertDialog.Builder(getActivity())
				CustomDialogFragment dialogFragment = (CustomDialogFragment) getFragmentManager().findFragmentByTag("repost");
				if(dialogFragment == null){
					dialogFragment = CustomDialogFragment.newInstance("直接转发", "编辑转发");
					dialogFragment.setListener(new DialogClickListener() {
						
						@Override
						public void choose(int p) {
							// TODO Auto-generated method stub
							if(p == 1){
								
								mStatusesAPI.repost(Long.parseLong(wblist.get(position).idstr), null, 0, new RequestListener() {
									
									@Override
									public void onWeiboException(WeiboException arg0) {
										// TODO Auto-generated method stub
										
									}
									
									@Override
									public void onComplete(String arg0) {
										// TODO Auto-generated method stub
										System.out.println(arg0);
										Toast.makeText(mActivity, "转发成功", Toast.LENGTH_LONG).show();
									}
								});
							}
							if(p == 2){
								Toast.makeText(mActivity, "编辑转发"+wblist.get(position).idstr, Toast.LENGTH_LONG).show();
							}
						}
					});
				}
				dialogFragment.show(getFragmentManager(), "repost");
				
			}

			@Override
			public void dianzan(int position) {
				
			}

			@Override
			public void more(final int position) {
				// TODO Auto-generated method stub
				if(wblist.get(position).favorited){
					CustomDialogFragment dialogFragment = (CustomDialogFragment) getFragmentManager().findFragmentByTag("canclemore");
					if(dialogFragment == null)
						dialogFragment = CustomDialogFragment.newInstance("取消收藏", "查看源微博");
					dialogFragment.setListener(new DialogClickListener() {
					
						@Override
						public void choose(int p) {
							// TODO Auto-generated method stub
							if(p == 1){
								mFavoritesAPI.destroy(Long.parseLong(wblist.get(position).idstr), new RequestListener() {
									
									@Override
									public void onWeiboException(WeiboException arg0) {
										// TODO Auto-generated method stub
										Toast.makeText(mActivity, "取消失败", Toast.LENGTH_SHORT).show();
									}
									
									@Override
									public void onComplete(String arg0) {
										// TODO Auto-generated method stub
										if(!TextUtils.isEmpty(arg0)){
											if(arg0.startsWith("{\"status\""))
												Toast.makeText(mActivity, "已取消收藏", Toast.LENGTH_SHORT).show();
										}
									}
								});
								wblist.get(position).favorited = false;
							}
							if(p == 2){
								Toast.makeText(mActivity, "查看原微博"+wblist.get(position).idstr, Toast.LENGTH_LONG).show();
							}
						}
					});
					dialogFragment.show(getFragmentManager(), "canclemore");
				}else{
					CustomDialogFragment dialogFragment = (CustomDialogFragment) getFragmentManager().findFragmentByTag("favmore");
					if(dialogFragment == null)
						dialogFragment = CustomDialogFragment.newInstance("收藏", "查看源微博");
					dialogFragment.setListener(new DialogClickListener() {
					
						@Override
						public void choose(int p) {
							// TODO Auto-generated method stub
							if(p == 1){
								mFavoritesAPI.create(Long.parseLong(wblist.get(position).idstr), new RequestListener() {
									
									@Override
									public void onWeiboException(WeiboException arg0) {
										// TODO Auto-generated method stub
										System.out.println(arg0);
										Toast.makeText(mActivity, "收藏失败", Toast.LENGTH_SHORT).show();
									}
									
									@Override
									public void onComplete(String arg0) {
										// TODO Auto-generated method stub
										if(!TextUtils.isEmpty(arg0)){
											if(arg0.startsWith("{\"status\""))
												Toast.makeText(mActivity, "收藏成功", Toast.LENGTH_SHORT).show();
										}
									}
								});
								wblist.get(position).favorited = true;
							}
							if(p == 2){
								Toast.makeText(mActivity, "查看原微博"+wblist.get(position).idstr, Toast.LENGTH_LONG).show();
							}
						}
					});
					dialogFragment.show(getFragmentManager(), "favmore");
				}
			}

			@Override
			public void comment(int position) {
				// TODO Auto-generated method stub
				CommentFragment commentFragment = CommentFragment.newInstance(wblist.get(position).idstr);
				commentFragment.show(getFragmentManager(), "comments");
			}
		});
        mListView.setAdapter(mAdapter);
        
        if(mIndex != USER){
        	GetStatus getStatus = new GetStatus(){
        		protected void onPostExecute(String result) {
        			if(TextUtils.isEmpty(result)){
        				mLoadNewest = true;
        				mLoadNewestFinished = false;
        				loadStatus();
        			}else {
        				//System.out.println("____handle"+result);
        				mLoadLocal = true;
        				handleStatus(result, true);
        			}
        		};
        	};
        	getStatus.execute(mIndex+mAccessToken.getUid());
        }
        else{
        	mUserAPI = new UsersAPI(getActivity(), Constants.APP_KEY, mAccessToken);
        	mUserAPI.show(mScreenName, new RequestListener() {
				
				@Override
				public void onWeiboException(WeiboException arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onComplete(String arg0) {
					// TODO Auto-generated method stub
					System.out.println("fragmenthome get user "+arg0);
					User tmpUser = User.parse(arg0);
					if(mGetUserInfo != null){
						System.out.println("mGetUserInfo is not null");
						mGetUserInfo.setUser(User.parse(arg0));
					}
					else {
						System.out.println("mGetUserInfo is null");
					}
				}
			});
        	mStatusesAPI.userTimeline(mScreenName, 0L, 0L, 50, 1, false, 0, false, mListener);
        }
	}

	protected void handleStatus(String response, boolean local) {
		// TODO Auto-generated method stub
		if (!TextUtils.isEmpty(response)) {
            LogUtil.i(TAG, response);
            if (response.startsWith("{\"statuses\"")) {
                StatusList statuses = StatusList.parse(response);
                if (statuses != null && statuses.total_number > 0) {
                	if(statuses.statusList != null){
                		getWbList(statuses.statusList);
                    	mSwipeRefreshLayout.setRefreshing(false);
                    	
                    	if(!local && mIndex != USER){
                    		SaveStatus saveStatus = new SaveStatus();
                    		saveStatus.execute(response, mIndex+mAccessToken.getUid());
                    	}
                	}
                }
            } else if (response.startsWith("{\"favorites\"")) {
            	mSwipeRefreshLayout.setRefreshing(false);
                FavoriteList favorites = FavoriteList.parse(response);
                if(favorites != null && favorites.total_number > 0){
                	if(favorites.favoriteList != null){
                		ArrayList<Status> statusList = new ArrayList<Status>();
                		for(int i = 0; i < favorites.favoriteList.size(); i++){
                			statusList.add(favorites.favoriteList.get(i).status);
                		}
                		getWbList(statusList);
                		
                		if(!local){
                    		SaveStatus saveStatus = new SaveStatus();
                    		saveStatus.execute(response, mIndex+mAccessToken.getUid());
                    	}
                	}
                }
            } else if (response.startsWith("{\"created_at\"")) {
//            	System.out.println(response);
//            	statuses = StatusList.parse(response);
//                if (statuses != null && statuses.total_number > 0) {
//                    getWbList(statuses);
//                    mSwipeRefreshLayout.setRefreshing(false);
//                }
			} 
            else {
            	System.out.println(response);
            	mSwipeRefreshLayout.setRefreshing(false);
               // Toast.makeText(mActivity, response, Toast.LENGTH_LONG).show();
                mLoadMore = false;
    			mLoadMoreFinish = false;
    			mLoadNewest = false;
    			mLoadNewestFinished = false;
            }
        }
		else{
        	mSwipeRefreshLayout.setRefreshing(false);
        	System.out.println(response);
        	mLoadMore = false;
			mLoadMoreFinish = false;
			mLoadNewest = false;
			mLoadNewestFinished = false;
			}
	}

	private RequestListener mListener = new RequestListener() {
		@Override
        public void onComplete(String response) {
            handleStatus(response, false);
        }
		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub
			if(arg0.toString().contains("User requests out of rate limit!")){
				Toast.makeText(getActivity(), "User requests out of rate limit!", Toast.LENGTH_LONG).show();
			}
			System.out.println(arg0);
			mLoadMore = false;
			mLoadMoreFinish = false;
			mLoadNewest = false;
			mLoadNewestFinished = false;
			mSwipeRefreshLayout.setRefreshing(false);
			mBtnLoadmore.setText("加载更多");
			mBtnLoadmore.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rounded_rectangle));
		}
    };
	
    private void getWbList(ArrayList<Status> statusList) {
		// TODO Auto-generated method stub
    	if(statusList != null){
    		System.out.println("statusList.size() "+statusList.size());
    		System.out.println(""+mLoadLocal);
    		if(mLoadMore || mLoadLocal){
    			mLoadLocal = false;
    			//mLoadMore = false;
    			for(int i = 1; i < statusList.size(); i++) {
    				wblist.add(statusList.get(i));
    				//System.out.println(statusList.get(i).user.screen_name);
    			}
    		}
    		if(mLoadNewest){
    				for(int i = statusList.size()-1; i >=0; i--) {
    					wblist.add(0, statusList.get(i));
    					//System.out.println(statusList.get(i).user.screen_name);
    				}
    		}
    		
    	}
    	//System.out.println("statuses.statusList.size() "+statuses.statusList.size());
    	showWbList();
    }

	private void showWbList() {
			mAdapter.notifyDataSetChanged();
			mBtnLoadmore.setText("加载更多");
			mBtnLoadmore.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rounded_rectangle));
			
			if(wblist.size() > 0){
				mNewestTime  = Long.parseLong(wblist.get(0).idstr);
				mLatestTime = Long.parseLong(wblist.get(wblist.size()-1).idstr);
				System.out.println(mNewestTime+" "+mLatestTime+""+wblist.size());
			}
			mLoadMore = mLoadNewest = false;
			mLoadMoreFinish = mLoadNewestFinished  = true;
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		mLoadNewest = true;
		mLoadNewestFinished = false;
		loadStatus();
		System.out.println("加载微博");
	}
	
	private void loadStatus() {
		switch (mIndex) {
		case HOME:
			System.out.println("HOME");
			if(mLoadNewest)
				mStatusesAPI.friendsTimeline(mNewestTime, 0L, 50, 1, false, 0, false, mListener);
			else if(mLoadMore)
				mStatusesAPI.friendsTimeline(0L, mLatestTime, 50, 1, false, 0, false, mListener);
			break;
		case HOT:
			mStatusesAPI.userTimeline(0, 0, 50, 1, false, 0, false, mListener);
			break;
		case FAVORITE:
			mFavoritesAPI.favorites(50, 1, mListener);
			break;
		case USER:
			mStatusesAPI.userTimeline(0, 0, 50, 1, false, 0, false, mListener);
		default:
			break;
		}
	}
	
	private GetUserInfo mGetUserInfo;
	
	public void setOnGetUserInfo(GetUserInfo getUserInfo) {
		this.mGetUserInfo = getUserInfo;
	}
	
	public interface GetUserInfo{
		public void setUser(User user);
	}
}  