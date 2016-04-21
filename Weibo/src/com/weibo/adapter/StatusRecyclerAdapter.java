package com.weibo.adapter;

import java.util.ArrayList;

import com.example.view.CustomImageView;
import com.example.view.Image;
import com.example.view.NineGridlayout;
import com.example.view.RoundImageView;
import com.example.view.WeiboTextView;
import com.example.weibo.R;
import com.sina.weibo.sdk.openapi.models.Status;
import com.squareup.picasso.Picasso;
import com.weibo.fragment.StatusRecyclerViewFragment;
import com.weibo.tools.GetImageWidthAndHeigth;
import com.weibo.tools.MyApplication;
import com.weibo.tools.ScreenTools;
import com.weibo.tools.Tools;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StatusRecyclerAdapter extends RecyclerView.Adapter{
	
	private static int USERHEAD = 0;
	private static int NORMALHEAD = 1;
	private static int FOOT = 2;
	private static int BODY = 3;
	
	public final static int MORECHOICE = 0;
	public final static int REPOST = 1;
	public final static int COMMENT = 2;
	public final static int ATTITUDE = 3;
	public final static int LOADMORE = 4;
	public static final int RETWEETCOMMENT = 5;
	
	private Context mContext;
	private ArrayList<Status> mStatuses;
	private OnItemViewClickListener mItemViewClickListener;
	private String mTag;
	
	public StatusRecyclerAdapter(Context context, ArrayList<Status> statuses, String tag) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mStatuses = statuses;
		this.mTag = tag;
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		if(mStatuses.size() < 3 && mTag != StatusRecyclerViewFragment.USER)
			return 0;
		return mStatuses.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		// TODO Auto-generated method stub
		try {
			//System.out.println("onBindViewHolder "+position);
			if(position == 0 && mTag == StatusRecyclerViewFragment.USER){
				if(mStatuses.get(position) != null){
					if(mStatuses.get(position).user != null){
						HeadViewHolder headViewHolder = (HeadViewHolder) viewHolder;
						Picasso.with(mContext).load(mStatuses.get(position).user.avatar_hd).into(headViewHolder.rimgUserIcon);
						headViewHolder.tvUserName.setText(mStatuses.get(position).user.screen_name);
						headViewHolder.tvFollowerCount.setText(String.valueOf(mStatuses.get(position).user.followers_count));
						headViewHolder.tvFriendsCount.setText(String.valueOf(mStatuses.get(position).user.friends_count));
						headViewHolder.tvStatusCount.setText(String.valueOf(mStatuses.get(position).user.statuses_count));
						System.out.println("mStatuses.get(position).user.description "+mStatuses.get(position).user.description);
						headViewHolder.wtvDescription.setText(mStatuses.get(position).user.description);
					}
				}
				return;
			}
			
			if(position == 0 && mTag != StatusRecyclerViewFragment.USER){
				FootViewHolder footViewHolder = (FootViewHolder) viewHolder;
				footViewHolder.btnFoot.setVisibility(View.INVISIBLE);
				return;
			}
			
			if(position == mStatuses.size()-1){
				FootViewHolder footViewHolder = (FootViewHolder) viewHolder;
				//footViewHolder.btnFoot.setText(mStatuses.get(position).text);;
				footViewHolder.btnFoot.setText("加载更多");
				return;
			}
			WeiboLayoutHolder weiboLayoutHolder = (WeiboLayoutHolder) viewHolder;
			Status status = mStatuses.get(position);
			if(status.user != null){
				if(status.user.screen_name != null){
					weiboLayoutHolder.wtvUserName.setText(status.user.screen_name);
					if(status.user.verified)
						weiboLayoutHolder.imgAvatarVip.setVisibility(View.VISIBLE);
					else
						weiboLayoutHolder.imgAvatarVip.setVisibility(View.GONE);
					if(status.source.length() != 0){
						String tmpS = Tools.getTagContent(status.source);
						weiboLayoutHolder.tvCreateTime.setText(Tools.getTimeFormat(status.created_at)+"  "+tmpS);	
					}
					//if(status.text.con)
					weiboLayoutHolder.wtvWeiboText.setText(status.text);
					Picasso.with(mContext).load(status.user.avatar_large).into(weiboLayoutHolder.rimgUserIcon);
					
					//设置转发评论点赞数
					if(status.reposts_count == 0)
						weiboLayoutHolder.tvRepostCount.setText("转发");
					else 
						weiboLayoutHolder.tvRepostCount.setText(String.valueOf(status.reposts_count));
					if(status.comments_count == 0)
						weiboLayoutHolder.tvCommentCount.setText("评论");
					else
						weiboLayoutHolder.tvCommentCount.setText(String.valueOf(status.comments_count));
					if(status.comments_count == 0)
						weiboLayoutHolder.tvAttitudeCount.setText("赞");
					else
						weiboLayoutHolder.tvAttitudeCount.setText(String.valueOf(status.attitudes_count));
					
					//是否为转发
					if(status.retweeted_status == null){
						//不是转发
						weiboLayoutHolder.vWeiboTextDivide.setVisibility(View.GONE);
						weiboLayoutHolder.llRetweetStatus.setVisibility(View.GONE);
						//是否有图
						if(status.pic_urls != null){
							weiboLayoutHolder.vWeiboTextandPicDivide.setVisibility(View.VISIBLE);
			                //开始处理图片
							ArrayList<Image> itemList = new ArrayList<Image>();
							
							for(int i = 0; i < status.pic_urls.size(); i++){
								Image image = new Image(null, 0, 0);
								String u = status.pic_urls.get(i).replace("thumbnail", "bmiddle");
								image.setUrl(u);
								itemList.add(image);
							}
							
							if (itemList.isEmpty() || itemList.isEmpty()) {
								weiboLayoutHolder.rlImgOne.setVisibility(View.GONE);
					            weiboLayoutHolder.nglMore.setVisibility(View.GONE);
					            weiboLayoutHolder.cimgOne.setVisibility(View.GONE);
					        } else{// if (itemList.size() == 1) {
					        	weiboLayoutHolder.rlImgOne.setVisibility(View.VISIBLE);
					        	if(status.pic_urls.size() > 1){
					        		weiboLayoutHolder.tvPicsCount.setVisibility(View.VISIBLE);
					        		weiboLayoutHolder.tvPicsCount.setText(String.valueOf(status.pic_urls.size()));
					        	}
					        	else
					        		weiboLayoutHolder.tvPicsCount.setVisibility(View.GONE);
					        	weiboLayoutHolder.nglMore.setVisibility(View.GONE);
					            weiboLayoutHolder.cimgOne.setVisibility(View.VISIBLE);
					            weiboLayoutHolder.cimgOne.setImageBitmap(null);
					            weiboLayoutHolder.cimgOne.setTag(String.valueOf(position)+"0");
			                    handlerOneImage(weiboLayoutHolder, itemList,false, position);
//					        } else {
//					            weiboLayoutHolder.nglMore.setVisibility(View.VISIBLE);
//					            weiboLayoutHolder.cimgOne.setVisibility(View.GONE);
//					            weiboLayoutHolder.nglMore.setImagesData(itemList, position);
					        }
						}
						else {
							 weiboLayoutHolder.rlImgOne.setVisibility(View.GONE);
							 weiboLayoutHolder.vWeiboTextandPicDivide.setVisibility(View.GONE);
							 weiboLayoutHolder.nglMore.setVisibility(View.GONE);
					         weiboLayoutHolder.cimgOne.setVisibility(View.GONE);
						}
					}else{
			            	//为转发微博
						weiboLayoutHolder.rlImgOne.setVisibility(View.GONE);
						weiboLayoutHolder.vWeiboTextandPicDivide.setVisibility(View.GONE);
						weiboLayoutHolder.vWeiboTextDivide.setVisibility(View.VISIBLE);
						weiboLayoutHolder.llRetweetStatus.setVisibility(View.VISIBLE);
						weiboLayoutHolder.nglMore.setVisibility(View.GONE);
			            weiboLayoutHolder.cimgOne.setVisibility(View.GONE);
			            weiboLayoutHolder.wtvRetweetWeiboText.setVisibility(View.VISIBLE);
			            
						//设置转发微博文本
			            if(status.retweeted_status.user != null){
			            	StringBuilder stringBuilder = new StringBuilder();
			            	stringBuilder.append("@");
			            	if(!TextUtils.isEmpty(status.retweeted_status.user.screen_name)){
			            		if(status.retweeted_status.user.verified)
			            			stringBuilder.append(status.retweeted_status.user.screen_name +""+ " : " + 
			            					status.retweeted_status.text);
			            		else
			            			stringBuilder.append(status.retweeted_status.user.screen_name + " : " + 
			            					status.retweeted_status.text);
			            	}
			            	weiboLayoutHolder.wtvRetweetWeiboText.setText(stringBuilder.toString());
			            	weiboLayoutHolder.tvRetweetRepostCount.setText("转发:"+status.retweeted_status.reposts_count);
			            	weiboLayoutHolder.tvRetweetCommentCount.setText("评论:"+status.retweeted_status.comments_count);
			            	weiboLayoutHolder.tvRetweetAttitudeCount.setText("赞:"+status.retweeted_status.attitudes_count+"  "
			            			+ Tools.getTagContent(status.retweeted_status.source));
			            	//转发的微博是否有图
			            	if(status.retweeted_status.pic_urls != null){		
			            		//开始处理图片
			            		ArrayList<Image> itemList = new ArrayList<Image>();
			            		
			            		for(int i = 0; i < status.retweeted_status.pic_urls.size(); i++){
			            			Image image = new Image(null, 0, 0);
			            			String usl = status.retweeted_status.pic_urls.get(i).replace("thumbnail", "bmiddle");
			            			image.setUrl(usl);
			            			itemList.add(image);
			            		}
			            		if (itemList.isEmpty() || itemList.isEmpty()) {
			            			weiboLayoutHolder.vRetweerWeiboTextAndPicDivide.setVisibility(View.GONE);
			            			weiboLayoutHolder.nglRetweetMore.setVisibility(View.GONE);
			            			weiboLayoutHolder.cimgRetweetOne.setVisibility(View.GONE);
			            			weiboLayoutHolder.rlRetweetImgOne.setVisibility(View.GONE);
			            		} else{// if (itemList.size() == 1) {
			            			weiboLayoutHolder.rlRetweetImgOne.setVisibility(View.VISIBLE);
			            			if(status.retweeted_status.pic_urls.size() > 1){
			            				weiboLayoutHolder.tvRetweetPicsCount.setVisibility(View.VISIBLE);
			            				weiboLayoutHolder.tvRetweetPicsCount.setText(String.valueOf(status.retweeted_status.pic_urls.size()));
			            			}else{
			            				weiboLayoutHolder.tvRetweetPicsCount.setVisibility(View.GONE);
			            			}
			            			weiboLayoutHolder.vRetweerWeiboTextAndPicDivide.setVisibility(View.VISIBLE);
			            			weiboLayoutHolder.nglRetweetMore.setVisibility(View.GONE);
			            			weiboLayoutHolder.cimgRetweetOne.setVisibility(View.VISIBLE);
			            			weiboLayoutHolder.cimgRetweetOne.setImageBitmap(null);
			            			weiboLayoutHolder.cimgRetweetOne.setTag(String.valueOf(position)+"0");
			            			handlerOneImage(weiboLayoutHolder, itemList,true, position);
//			            		} else {
//			            			weiboLayoutHolder.vRetweerWeiboTextAndPicDivide.setVisibility(View.GONE);
//			            			weiboLayoutHolder.nglRetweetMore.setVisibility(View.VISIBLE);
//			            			weiboLayoutHolder.cimgRetweetOne.setVisibility(View.GONE);
//			            			weiboLayoutHolder.nglRetweetMore.setImagesData(itemList, position);
			            		}
			            	}else{
			            		weiboLayoutHolder.rlRetweetImgOne.setVisibility(View.GONE);
			            		weiboLayoutHolder.vRetweerWeiboTextAndPicDivide.setVisibility(View.GONE);
			            		weiboLayoutHolder.nglRetweetMore.setVisibility(View.GONE);
			            		weiboLayoutHolder.cimgRetweetOne.setVisibility(View.GONE);
			            	}
			            }
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
		
	}
	
	private void handlerOneImage(final WeiboLayoutHolder viewHolder, final ArrayList<Image> images, final Boolean isRetweet, final int position) {
        int imageWidth;
        final int imageHeight;

		if(mStatuses.get(position).widthOfPicture > 0){
			setImageViewSize(viewHolder, mStatuses.get(position).heightOfPictur,
					mStatuses.get(position).widthOfPicture, isRetweet, images);

		}else {
			GetImageWidthAndHeigth getImageWidthAndHeigth = new GetImageWidthAndHeigth() {
				@Override
				protected void onPostExecute(int[] result) {
					int totalWidth = MyApplication.mWidthOfWeiboLayout;
					// TODO Auto-generated method stub
					if (result != null) {
						int totalHeight = (int) (totalWidth * ((float) result[2] / result[1]));
						if (totalHeight > totalWidth * MyApplication.mRate)
							totalHeight = (int) (totalWidth * MyApplication.mRate);
						mStatuses.get(position).widthOfPicture = totalWidth;
						mStatuses.get(position).heightOfPictur = totalHeight;
						setImageViewSize(viewHolder, totalHeight, totalWidth, isRetweet, images);
					}else{
						setImageViewSize(viewHolder, totalWidth, totalWidth, isRetweet, images);
					}
				}
			};
			getImageWidthAndHeigth.execute(images.get(0).getUrl(), "0");
		}
    }

	private void setImageViewSize(WeiboLayoutHolder viewHolder, int totalHeight, int totalWidth, boolean isRetweet, ArrayList<Image> images){
		RelativeLayout.LayoutParams layoutparams = (RelativeLayout.LayoutParams) viewHolder.cimgOne.getLayoutParams();
		layoutparams.height = totalHeight;
		layoutparams.width = totalWidth;
		if (!isRetweet) {
			viewHolder.cimgOne.setLayoutParams(layoutparams);
			viewHolder.cimgOne.setClickable(true);
			viewHolder.cimgOne.setImageBitmap(null);
			//List<Image> imageList = new ArrayList<Image>();
			//imageList.add(image);
			viewHolder.cimgOne.setImageUrl(images.get(0).getUrl(), images, 0);
		} else {
			viewHolder.cimgRetweetOne.setLayoutParams(layoutparams);
			viewHolder.cimgRetweetOne.setClickable(true);
			viewHolder.cimgRetweetOne.setImageBitmap(null);
			//List<Image> imageList = new ArrayList<Image>();
			//imageList.add(image);
			viewHolder.cimgRetweetOne.setImageUrl(images.get(0).getUrl(), images, 0);
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int viewType) {
		// TODO Auto-generated method stub
		//System.out.println("onCreateViewHolder "+viewType);
		
		if(viewType == USERHEAD){
			View headView = null;
			if(MyApplication.mIsNightMode)
				headView = LayoutInflater.from(mContext).inflate(R.layout.user_info_head_night, null);
			else
				headView = LayoutInflater.from(mContext).inflate(R.layout.user_info_head, null);
			
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
	        headView.setLayoutParams(lp);
	        return new HeadViewHolder(headView);
		}
		if(viewType == NORMALHEAD || viewType == FOOT){
			View footView = null;
			if(MyApplication.mIsNightMode)
				footView = LayoutInflater.from(mContext).inflate(R.layout.status_list_foot_night, null);
			else
				footView = LayoutInflater.from(mContext).inflate(R.layout.status_list_foot, null);
			footView.setTag("headView");
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
	        footView.setLayoutParams(lp);
	        return new FootViewHolder(footView);
		}
		View itemView = null;
		if(MyApplication.mIsNightMode)
			itemView = LayoutInflater.from(mContext).inflate(R.layout.weibo_layout_night, null);
		else
			itemView = LayoutInflater.from(mContext).inflate(R.layout.weibo_layout, null);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemView.setLayoutParams(lp);
		return new WeiboLayoutHolder(itemView);
	}
	
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if(position == 0 && mTag == StatusRecyclerViewFragment.USER)
			return USERHEAD;
		else if(position == 0 && mTag != StatusRecyclerViewFragment.USER)
			return NORMALHEAD;
		else if(position == mStatuses.size()-1)
			return FOOT;
		else
			return BODY;
	}
	
	public void setOnItemViewClickListener(OnItemViewClickListener itemViewClickListener) {
		this.mItemViewClickListener = itemViewClickListener;
	}
	
	public interface OnItemViewClickListener{
		public void click(int index, int positon);
	}
	
	class WeiboLayoutHolder extends RecyclerView.ViewHolder implements OnClickListener{
		
		public View vWeiboTextDivide, vWeiboTextandPicDivide, vRetweerWeiboTextAndPicDivide;
		
		public ImageView imgAttitude, imgAvatarVip, imgMoreChoice;
		public RoundImageView rimgUserIcon; 
		public CustomImageView cimgOne, cimgRetweetOne;
        
		public WeiboTextView wtvWeiboText, wtvRetweetWeiboText, wtvUserName;
        public TextView tvCreateTime, tvRepostCount, tvCommentCount, tvAttitudeCount;
        public TextView tvRetweetRepostCount, tvRetweetCommentCount, tvRetweetAttitudeCount,
        	tvPicsCount, tvRetweetPicsCount;
        
        public LinearLayout llRetweetStatus, llRepostLayout, llCommentLayout, llAttitudeLayout;
        public NineGridlayout nglMore , nglRetweetMore;
        public RelativeLayout rlImgOne, rlRetweetImgOne;
        
		public WeiboLayoutHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
			vWeiboTextDivide = (View) itemView.findViewById(R.id.v_weiboTextDivide);
			vWeiboTextandPicDivide = (View) itemView.findViewById(R.id.v_weiboTextandPicDivide);
			vRetweerWeiboTextAndPicDivide = (View) itemView.findViewById(R.id.v_retweetWeiboTextandPicDivide);
			
			rimgUserIcon = (RoundImageView) itemView.findViewById(R.id.rimg_userIcon);
			
			imgMoreChoice = (ImageView) itemView.findViewById(R.id.img_moreChoice);
			imgAttitude = (ImageView) itemView.findViewById(R.id.img_attitude);
			imgAvatarVip = (ImageView) itemView.findViewById(R.id.img_avatarVip);
			
			cimgOne = (CustomImageView) itemView.findViewById(R.id.cimgOne);
			cimgRetweetOne = (CustomImageView) itemView.findViewById(R.id.cimg_retweetOne);
			
			wtvUserName = (WeiboTextView) itemView.findViewById(R.id.wtv_userName);
			wtvWeiboText = (WeiboTextView) itemView.findViewById(R.id.wtv_weiboText);
			wtvRetweetWeiboText = (WeiboTextView) itemView.findViewById(R.id.wtv_retweetWeiboText);
			
			tvCreateTime = (TextView) itemView.findViewById(R.id.tv_createTime);
			tvRepostCount = (TextView) itemView.findViewById(R.id.tv_repostCount);
			tvCommentCount = (TextView) itemView.findViewById(R.id.tv_commentCount);
			tvAttitudeCount = (TextView) itemView.findViewById(R.id.tv_attitudeCount);
			
			tvRetweetRepostCount = (TextView) itemView.findViewById(R.id.tv_retweetRepostCount);
			tvRetweetCommentCount = (TextView) itemView.findViewById(R.id.tv_retweetCommentsCount);
			tvRetweetAttitudeCount = (TextView) itemView.findViewById(R.id.tv_retweetAttitudeCount);
			
			tvPicsCount = (TextView) itemView.findViewById(R.id.tv_picsCount);
			tvRetweetPicsCount = (TextView) itemView.findViewById(R.id.tv_retweetPicsCount);
			
			llRetweetStatus = (LinearLayout) itemView.findViewById(R.id.ll_retweetStatus);
			llRepostLayout = (LinearLayout) itemView.findViewById(R.id.ll_repostLayout);
			llCommentLayout = (LinearLayout) itemView.findViewById(R.id.ll_commentLayout);
			llAttitudeLayout = (LinearLayout) itemView.findViewById(R.id.ll_attitudeLayout);
			
			nglMore = (NineGridlayout) itemView.findViewById(R.id.ngl_more);
			nglRetweetMore = (NineGridlayout) itemView.findViewById(R.id.ngl_retweetMore);
			rlImgOne = (RelativeLayout) itemView.findViewById(R.id.rl_cimgOne);
			rlRetweetImgOne = (RelativeLayout) itemView.findViewById(R.id.rl_retweetCimgOne);
			
			imgMoreChoice.setOnClickListener(this);
			llRepostLayout.setOnClickListener(this);
			llCommentLayout.setOnClickListener(this);
			llAttitudeLayout.setOnClickListener(this);
			
			rimgUserIcon.setOnClickListener(this);
			wtvUserName.setOnClickListener(this);
			
			tvRetweetCommentCount.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.img_moreChoice:
				mItemViewClickListener.click(MORECHOICE, getPosition());
				break;
			case R.id.ll_repostLayout:
				mItemViewClickListener.click(REPOST, getPosition());
				break;
			case R.id.ll_commentLayout:
				mItemViewClickListener.click(COMMENT, getPosition());
				break;
			case R.id.ll_attitudeLayout:
				mItemViewClickListener.click(ATTITUDE, getPosition());
				break;
			case R.id.wtv_userName:
			case R.id.rimg_userIcon:
				String[] strings = new String[5];
				strings[0] = mStatuses.get(getPosition()).user.screen_name;
				strings[1] = mStatuses.get(getPosition()).user.avatar_hd;
				strings[2] = String.valueOf(mStatuses.get(getPosition()).user.followers_count);
				strings[3] = String.valueOf(mStatuses.get(getPosition()).user.friends_count);
				strings[4] = String.valueOf(mStatuses.get(getPosition()).user.statuses_count);
				Tools.goToUserPage(mContext, strings);
				break;
			case R.id.tv_retweetCommentsCount:
				mItemViewClickListener.click(RETWEETCOMMENT, getPosition());
				break;
			default:
				break;
			}
		}
	}
	
	class HeadViewHolder extends ViewHolder{

		public RoundImageView rimgUserIcon;
		public TextView tvUserName, tvFriendsCount, tvStatusCount, tvFollowerCount, tvStatus, tvFollower, tvFriend;
		public WeiboTextView wtvDescription;
		public HeadViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
			rimgUserIcon = (RoundImageView) itemView.findViewById(R.id.rimg_userIcon);
			tvUserName = (TextView) itemView.findViewById(R.id.tv_userName);
			tvFriendsCount = (TextView) itemView.findViewById(R.id.tv_friendsCount);
			tvFollowerCount = (TextView) itemView.findViewById(R.id.tv_followerCount);
			tvStatusCount = (TextView) itemView.findViewById(R.id.tv_statusCount);
			
			wtvDescription = (WeiboTextView) itemView.findViewById(R.id.wtv_description);
			
			tvFollower = (TextView) itemView.findViewById(R.id.tv_follower);
			tvFriend = (TextView) itemView.findViewById(R.id.tv_friends);
			tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
			
			tvUserName.setTextColor(MyApplication.mTitleColor);
			tvFollowerCount.setTextColor(MyApplication.mTitleColor);
			tvFriendsCount.setTextColor(MyApplication.mTitleColor);
			tvStatusCount.setTextColor(MyApplication.mTitleColor);
			
			tvFollower.setTextColor(MyApplication.mTitleColor);
			tvStatus.setTextColor(MyApplication.mTitleColor);
			tvFriend.setTextColor(MyApplication.mTitleColor);
			
			wtvDescription.setTextColor(MyApplication.mTitleColor);
		}
	}
	
	class FootViewHolder extends ViewHolder implements OnClickListener{

		private Button btnFoot;
		public FootViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
			btnFoot = (Button) itemView.findViewById(R.id.btn_foot);
			btnFoot.setOnClickListener(this);
			
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			btnFoot.setText("正在加载");
			mItemViewClickListener.click(LOADMORE, LOADMORE);
		}
		
	}
}
