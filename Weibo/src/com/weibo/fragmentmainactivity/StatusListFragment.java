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
import com.squareup.picasso.Picasso.RequestTransformer;
import com.weibo.adapter.RecyclerAdapter;
import com.weibo.adapter.RecyclerAdapter.OnItemViewClickListener;
import com.weibo.tools.AccessTokenKeeper;
import com.weibo.tools.Constants;
import com.weibo.tools.GetStatus;
import com.weibo.tools.MyApplication;
import com.weibo.tools.SaveStatus;
import com.weibo.tools.ScreenTools;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.Toast;  
  
public class StatusListFragment extends Fragment implements OnRefreshListener{  
  
	public static final String HOME = "home";
	public static final String HOT = "hot";
	public static final String FAVORITE = "favorite";
	public static final String USER = "user";
	
	private String mTag;
    private View mParent;  
    private FragmentActivity mActivity;  
    
    private StatusesAPI mStatusesAPI;
    private FavoritesAPI mFavoritesAPI;
	private UsersAPI mUserAPI;
    public Oauth2AccessToken mAccessToken;
    
	private RecyclerView mReclStatusList;
	private RecyclerAdapter mStatusAdapter;
	private ArrayList<Status> mStatusList;
	
	private SwipeRefreshLayout mSwipeRefreshLayout;
	
	private boolean mLoadMore = false;
	protected boolean mLoadMoreFinish = false;
	
	private boolean mLoadLocal = false;
	private boolean mLoadNewest = false;
	private long mNewestTime = 0L;
	private long mLatestTime = 0L;
	private String mScreenName;
	protected boolean mLoadNewestFinished;
	
	private Status mLoadMoreStatus;
	private int mScrollY;
	private int mDistance;
	
	
	
    /** 
     * Create a new instance of DetailsFragment, initialized to show the text at 
     * 'index'. 
     */  
    public static StatusListFragment newInstance(String tag , String userName) {  
        StatusListFragment f = new StatusListFragment();  
        // Supply index input as an argument.  
        Bundle args = new Bundle();  
        args.putString("tag", tag);  
        if(tag == USER)
        	args.putString("screen_name", userName);  
        f.setArguments(args);  
        return f;  
    }  
  
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
    	MyApplication.mCanHide = false;
        View view = inflater.inflate(R.layout.status_list_fragment, container, false); 
        mTag = getArguments().getString("tag", null);
        if(mTag == USER)
        	mScreenName = getArguments().getString("screen_name");
        return view;  
    }  
  
    @Override  
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
        init();
    }
    
    private void init() {
		// TODO Auto-generated method stub
    	mActivity = getActivity();  
        mParent = getView();  
        ScreenTools s = ScreenTools.instance(mActivity);
        mDistance = s.dip2px(150);
        
        mLoadMoreStatus = new Status();
        mLoadMoreStatus.text = "加载更多";
        
        
        mReclStatusList = (RecyclerView) mParent.findViewById(R.id.recl_statusList);
        mReclStatusList.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mReclStatusList.setLayoutManager(layoutManager);
        
        mReclStatusList.setOnScrollListener(new OnScrollListener() {
        	public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        		//System.out.println(((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition());
				//System.out.println("onScrolled"+" "+dx+" "+dy);
        		int firstPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();
        		
				if(mStatusListener != null){
					if(((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition() == 0){
						if(((LinearLayoutManager)layoutManager).findViewByPosition(0) != null){
							if(((LinearLayoutManager)layoutManager).findViewByPosition(0).getTop() < -mDistance)
								mStatusListener.setUserTitleBarVisable(View.VISIBLE);
							if((((LinearLayoutManager)layoutManager).findViewByPosition(0).getTop() > -mDistance))
								mStatusListener.setUserTitleBarVisable(View.INVISIBLE);
						}
					}
				}
        		if(firstPosition == 0)
        			MyApplication.mCanHide = false;
				else{
					if(!MyApplication.mCanHide)
						MyApplication.mCanHide = true;
					if(firstPosition > 3)
						mSwipeRefreshLayout.setRefreshing(false);
				}
        	};
		});    
        mSwipeRefreshLayout = (SwipeRefreshLayout) mParent.findViewById(R.id.srl_refresh);
        mSwipeRefreshLayout.setColorSchemeColors(MyApplication.mTitleColor);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(MyApplication.mThemeColor);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        
        
        
        mAccessToken = AccessTokenKeeper.readAccessToken(mActivity);
        mStatusesAPI = new StatusesAPI(mActivity, Constants.APP_KEY, mAccessToken);
        mFavoritesAPI = new FavoritesAPI(mActivity, Constants.APP_KEY, mAccessToken);
        UsersAPI usersAPI = new UsersAPI(mActivity, Constants.APP_KEY, mAccessToken);
        
        mStatusList = new ArrayList<Status>();
        
        mStatusAdapter = new RecyclerAdapter(mActivity, mStatusList, mTag); 
        mStatusAdapter.setOnItemViewClickListener(new OnItemViewClickListener() {
			
			@Override
			public void click(int index, final int position) {
				// TODO Auto-generated method stub
				
				switch (index) {
				case RecyclerAdapter.MORECHOICE:
					System.out.println("StatusListFragment MORECHOICE");
					if(mStatusList.get(position).favorited){
						CustomDialogFragment dialogFragment = (CustomDialogFragment) getFragmentManager().findFragmentByTag("canclemore");
						if(dialogFragment == null)
							dialogFragment = CustomDialogFragment.newInstance("取消收藏", "查看源微博");
						dialogFragment.setListener(new DialogClickListener() {
						
							@Override
							public void choose(int p) {
								// TODO Auto-generated method stub
								if(p == 1){
									mFavoritesAPI.destroy(Long.parseLong(mStatusList.get(position).idstr), new RequestListener() {
										
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
									mStatusList.get(position).favorited = false;
								}
								if(p == 2){
									Toast.makeText(mActivity, "查看原微博"+mStatusList.get(position).idstr, Toast.LENGTH_LONG).show();
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
									mFavoritesAPI.create(Long.parseLong(mStatusList.get(position).idstr), new RequestListener() {
										
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
									mStatusList.get(position).favorited = true;
								}
								if(p == 2){
									Toast.makeText(mActivity, "查看原微博"+mStatusList.get(position).idstr, Toast.LENGTH_LONG).show();
								}
							}
						});
						dialogFragment.show(getFragmentManager(), "favmore");
					}
					break;
				case RecyclerAdapter.REPOST:
					CustomDialogFragment dialogFragment = (CustomDialogFragment) getFragmentManager().findFragmentByTag("repost");
					if(dialogFragment == null){
						dialogFragment = CustomDialogFragment.newInstance("直接转发", "编辑转发");
						dialogFragment.setListener(new DialogClickListener() {
							
							@Override
							public void choose(int p) {
								// TODO Auto-generated method stub
								if(p == 1){
									
									mStatusesAPI.repost(Long.parseLong(mStatusList.get(position).idstr), null, 0, new RequestListener() {
										
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
									Toast.makeText(mActivity, "编辑转发"+mStatusList.get(position).idstr, Toast.LENGTH_LONG).show();
								}
							}
						});
					}
					dialogFragment.show(getFragmentManager(), "repost");
					break;
				case RecyclerAdapter.COMMENT:
					CommentFragment commentFragment = CommentFragment.newInstance(mStatusList.get(position).idstr);
					commentFragment.show(getFragmentManager(), "comments");
					break;
				case RecyclerAdapter.RETWEETCOMMENT:
					CommentFragment commentRetweetFragment = CommentFragment.newInstance(mStatusList.get(position).retweeted_status.idstr);
					commentRetweetFragment.show(getFragmentManager(), "retweetComments");
					break;
				case RecyclerAdapter.ATTITUDE:
					
					break;
				case RecyclerAdapter.LOADMORE:
					mLoadMore = true;
					System.out.println(mNewestTime+" "+mLatestTime+""+mStatusList.size());
					loadStatus();
					break;
				default:
					break;
				}
			}
		});
        mReclStatusList.setAdapter(mStatusAdapter);
        
        if(mTag != USER){
        	mSwipeRefreshLayout.setProgressViewOffset(false, s.dip2px(10), s.dip2px(65));
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
        	getStatus.execute(mTag+mAccessToken.getUid());
        }
        else{
        	mSwipeRefreshLayout.setProgressViewOffset(false, s.dip2px(0), s.dip2px(50));
        	System.out.println(mTag);
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
					if(tmpUser != null){
						Status status = new Status();
						status.user = tmpUser;
						mStatusList.add(status);
						mStatusAdapter.notifyDataSetChanged();
						mLoadMore = true;
				        mStatusesAPI.friendsTimeline(0L, 0L, 50, 1, false, 0, false, mListener);
					}
				}
			});
        	
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
			}
    };

	protected void handleStatus(String response, boolean local) {
		// TODO Auto-generated method stub
		if (!TextUtils.isEmpty(response)) {
            if (response.startsWith("{\"statuses\"")) {
                StatusList statuses = StatusList.parse(response);
                if (statuses != null && statuses.total_number > 0) {
                	if(statuses.statusList != null){
                		if(statuses.statusList.size() == 1 && Long.parseLong(statuses.statusList.get(0).idstr) == mLatestTime){
                			//mStatusList.get(mStatusList.size()-1).text = "暂无更多";
                			//mStatusAdapter.notify();
                		}
                		getWbList(statuses.statusList);
                    	mSwipeRefreshLayout.setRefreshing(false);
                    	
                    	if(!local && mTag != USER){
                    		SaveStatus saveStatus = new SaveStatus();
                    		saveStatus.execute(response, mTag+mAccessToken.getUid());
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
                    		saveStatus.execute(response, mTag+mAccessToken.getUid());
                    	}
                	}
                }
            } else if (response.startsWith("{\"created_at\"")) {
            	
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

	
	
    private void getWbList(ArrayList<Status> statusList) {
    	try {
    		// TODO Auto-generated method stub
        	if(statusList != null){
        		System.out.println("statusList.size() "+statusList.size());
        		System.out.println(""+mLoadLocal);
        		if(mLoadMore || mLoadLocal){
        			mLoadLocal = false;
        			//if(mStatusList.size() > 1)
        			//	mStatusList.remove(mStatusList.size()-1);
        			for(int i = 1; i < statusList.size(); i++) {
        				mStatusList.add(statusList.get(i));
        			}
        			//mStatusList.add(mLoadMoreStatus);
        		}
        		if(mLoadNewest){
        			for(int i = statusList.size()-1; i >= 0; i--) {
        				mStatusList.add(1, statusList.get(i));
        			}
        		}
        	}
        	//System.out.println("statuses.statusList.size() "+statuses.statusList.size());
        	showWbList();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
    }

	private void showWbList() {
		try {
			System.out.println("statuslistfragment mstatuslist.size "+mStatusList.size());
			mStatusAdapter.notifyDataSetChanged();
			if(mStatusList.size() > 1){
				mNewestTime  = Long.parseLong(mStatusList.get(1).idstr);
				mLatestTime = Long.parseLong(mStatusList.get(mStatusList.size()-1).idstr);
				System.out.println(mNewestTime+" "+mLatestTime+""+mStatusList.size());
			}
			mLoadMore = mLoadNewest = false;
			mLoadMoreFinish = mLoadNewestFinished  = true;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
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
		switch (mTag) {
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
			break;
		default:
			break;
		}
	}
	
	private OnStatusFragmentListener mStatusListener;
	
	public void setStatusListener(OnStatusFragmentListener statusListener) {
		this.mStatusListener = statusListener;
	}
	
	public interface OnStatusFragmentListener{
		public void setUser(User user);
		public void setUserTitleBarVisable(int visibility);
	}
}  