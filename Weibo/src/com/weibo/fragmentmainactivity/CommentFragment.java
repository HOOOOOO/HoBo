package com.weibo.fragmentmainactivity;

import java.util.ArrayList;

import javax.crypto.Mac;

import com.example.view.SwipeBackLayout;
import com.example.weibo.R;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.CommentList;
import com.weibo.adapter.CommentAdapter;
import com.weibo.tools.AccessTokenKeeper;
import com.weibo.tools.Constants;
import com.weibo.tools.MyApplication;
import com.weibo.tools.ScreenTools;

import android.Manifest.permission;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CommentFragment extends DialogFragment implements OnRefreshListener{
	
	private View mParent;
	private Oauth2AccessToken mAccessToken;
	private ListView mListView;
	private CommentsAPI mCommentsAPI ;
	private String mWeiboID;
	private CommentAdapter mCommentsAdapter;
	private ArrayList<Comment> mComments;
	private Button mBtnBack;
	private SwipeRefreshLayout mRefreshLayout;
	private long mNewestTime = 0L;
	private long mLatestTime = 0L;
	protected boolean mLoadMore = false;
	private int mCommentsCount = 0;
	private TextView loadmore;
	private int mPage = 1;
	protected boolean mLoadNewst = false;
	private EditText mSendComment;

	public static CommentFragment newInstance(String weiboID) {
		System.out.println("CommentFragment newInstance");
		CommentFragment commentFragment = new CommentFragment();
		Bundle bundle = new Bundle();
		bundle.putString("weiboID", weiboID);
		commentFragment.setArguments(bundle);
		return commentFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		System.out.println("CommentFragment oncreateview");
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = null;
		if(MyApplication.mIsNightMode)
			view = LayoutInflater.from(getActivity()).inflate(R.layout.comment_fragment_night, container);
		else
			view = LayoutInflater.from(getActivity()).inflate(R.layout.comment_fragment, container);
		
		return view;
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		System.out.println("CommentFragment onstart");
		SwipeBackLayout wBackLayout = (SwipeBackLayout) getView().findViewById(R.id.comment_swipeback);		
		LinearLayout layout = (LinearLayout) getView().findViewById(R.id.comment_ll_background);
		wBackLayout.setContentView(layout, this);
		
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处
		getDialog().getWindow().setLayout((int) (MyApplication.mWidthOfScreen), MyApplication.mHeightOfScreen);//这2行,和上面的一样,注意顺序就行;
		mListView = (ListView) getView().findViewById(R.id.lv_comments);
		//View view = LayoutInflater.from(getActivity()).inflate(R.layout.comment_list_foot, null);
        //loadmore = (TextView) view.findViewById(R.id.tv_loadmore);
        loadmore = new TextView(getActivity());
        loadmore.setText("正在加载...");
        loadmore.setTextSize(15);
        loadmore.setGravity(Gravity.CENTER);
        
        if(MyApplication.mIsNightMode){
        	loadmore.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rounded_rectangle_btn_night));
        	loadmore.setTextColor(getResources().getColor(R.color.colorNightWhite));
        }
        else{
        	loadmore.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rounded_rectangle_btn));
        	loadmore.setTextColor(getResources().getColor(R.color.colorWeiboText1));
        }
        ScreenTools s = ScreenTools.instance(getActivity());
        //android.widget.AbsListView.LayoutParams layoutParams = android.widget.AbsListView.LayoutParams(android.widget.AbsListView.LayoutParams.FILL_PARENT, s.dip2px(50));
        //loadmore.setLayoutParams(layoutParams);
        //loadmore.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, s.dip2px(50)));
        loadmore.setPadding(0, s.dip2px(16), 0, s.dip2px(16));
        
        mListView.addFooterView(loadmore);
        loadmore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(loadmore.getText() == "暂无更多")
					return;
				System.out.println("加载更多");
				loadmore.setText("正在加载...");
				mLoadMore  = true;
				mCommentsAPI.show(Long.parseLong(mWeiboID), 0, mLatestTime, 50, mPage, 0, mCommentListener);			
			}
		});
        
        mRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.srl_commentrefresh);
		mRefreshLayout.setColorSchemeColors(MyApplication.mThemeColor);
		mRefreshLayout.setOnRefreshListener(this);
		mBtnBack = (Button) getView().findViewById(R.id.btn_back);
		mBtnBack.setText("返回");
		mSendComment = (EditText) getView().findViewById(R.id.ed_sendcomment);
		
		
		mBtnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mBtnBack.getText() == "返回")
					dismiss();
				if(mBtnBack.getText() == "发送"){
					String string = mSendComment.getText().toString();
					mSendComment.clearFocus();
					mSendComment.setText("");
					if(TextUtils.isEmpty(string))
						return;
					mCommentsAPI.create(string, Long.parseLong(mWeiboID), false, new RequestListener() {
						
						@Override
						public void onWeiboException(WeiboException arg0) {
							// TODO Auto-generated method stub
							System.out.println(arg0);
							if(arg0.toString().contains("only follower can comment.")){
								Toast.makeText(getContext(), "关注才能评论", Toast.LENGTH_LONG).show();
							}
							else{
								Toast.makeText(getContext(), "评论失败", Toast.LENGTH_LONG).show();
								
							}
						}
						
						@Override
						public void onComplete(String arg0) {
							// TODO Auto-generated method stub
							//System.out.println(arg0);
							if(!TextUtils.isEmpty(arg0)){
								if(arg0.startsWith("{\"created_at\"")){
									Toast.makeText(getActivity(), "评论成功", Toast.LENGTH_LONG).show();
									//Comment tmpComment = new Comment();
									//if(tmpComments.commentList!= null){
									//	System.out.println(tmpComments.commentList.get(0).text);
									//}
								}
							}
						}
					});
				}
			}
		});
		
		
        mSendComment.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus){
					mBtnBack.setText("发送");
				}
				else{
					mBtnBack.setText("返回");
				}
			}
		});
        
		mComments = new ArrayList<Comment>();
		mCommentsAdapter = new CommentAdapter(mComments, getContext());
		mListView.setAdapter(mCommentsAdapter);
		mParent = getView();
		
		mWeiboID = getArguments().getString("weiboID");
		
		mAccessToken = AccessTokenKeeper.readAccessToken(getContext());
		mCommentsAPI = new CommentsAPI(getActivity(), Constants.APP_KEY, mAccessToken);
		mRefreshLayout.setRefreshing(true);
		
		mLoadNewst = true;
		mCommentsAPI.show(Long.parseLong(mWeiboID), 0, 0, 50, 1, 0, mCommentListener);
		loadmore.setText("正在加载...");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		System.out.println("CommentFragment onCreate");
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		System.out.println("CommentFragment onAttach");
		super.onAttach(activity);
	}
	
	@Override
	public void onActivityCreated(Bundle arg0) {
		// TODO Auto-generated method stub
		System.out.println("CommentFragment onActivityCreated");
		super.onActivityCreated(arg0);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		mLoadNewst = true;
		mCommentsAPI.show(Long.parseLong(mWeiboID), 0, 0, 50, 1, 0, mCommentListener);
	}
	
	private RequestListener mCommentListener = new RequestListener() {
		
		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub
			if(arg0.toString().contains("User requests out of rate limit!")){
				Toast.makeText(getActivity(), "User requests out of rate limit!", Toast.LENGTH_LONG).show();
			}
			loadmore.setText("加载更多");
			mLoadMore = mLoadNewst = false;
			mRefreshLayout.setRefreshing(false);
			System.out.println(""+arg0);
		}
		
		@Override
		public void onComplete(String arg0) {
			// TODO Auto-generated method stub
			mRefreshLayout.setRefreshing(false);
			if(!TextUtils.isEmpty(arg0)){
				//System.out.println(arg0);
				CommentList comments = CommentList.parse(arg0);
				if(comments.commentList != null & comments.total_number > 0){
					System.out.println("返回的评论数："+comments.commentList.size());
					
					if(mLoadMore){
						if(comments.commentList.size() == 1 && Long.parseLong(comments.commentList.get(0).idstr) == mLatestTime){
							System.out.println("这条微博的评论数："+comments.commentList.get(0).status.comments_count+" "+comments.commentList.get(0).status.text);
							mPage=mPage+1;
							System.out.println("mpage:"+mPage);
							mLoadMore = true;
							loadmore.setText("暂无更多");
							//mCommentsAPI.show(Long.parseLong(mWeiboID), 0, mLatestTime, 50, mPage, 0, mCommentListener);
							return;
						}
						for(int i = 1; i < comments.commentList.size(); i++){
							mComments.add(comments.commentList.get(i));
						}
					}
					if(mLoadNewst ){
						mComments.clear();
						for(int i = comments.commentList.size()-1; i >= 0; i--){
							mComments.add(0, comments.commentList.get(i));
						}
					}
					
					mCommentsAdapter.notifyDataSetChanged();
					loadmore.setText("加载更多");
					mCommentsCount = mComments.size();
					mLoadMore = mLoadNewst = false;
					System.out.println("CommentFragment mCommentsCount "+String.valueOf(mCommentsCount));
					if(mComments.size() > 0){
						mNewestTime = Long.parseLong(mComments.get(0).idstr);
						mLatestTime = Long.parseLong(mComments.get(mComments.size()-1).idstr);
					}
				}
			}
		}
	};
}
