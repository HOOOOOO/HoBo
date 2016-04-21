package com.weibo.adapter;

import java.util.ArrayList;
import java.util.List;


import com.example.view.CustomImageView;
import com.example.view.Image;
import com.example.view.NineGridlayout;
import com.example.view.RoundImageView;
import com.example.view.WeiboTextView;
import com.example.weibo.R;
import com.example.weibo.UserPageActivity;
import com.sina.weibo.sdk.openapi.models.Status;
import com.squareup.picasso.Picasso;
import com.weibo.tools.AsyncImageLoader;
import com.weibo.tools.GetArticleOfWeibo;
import com.weibo.tools.GetImageWidthAndHeigth;
import com.weibo.tools.MyApplication;
import com.weibo.tools.ScreenTools;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StatusListAdapter extends BaseAdapter {
	
	private static final String ARTICLE = "...全文:";
	private AsyncImageLoader asyncImageLoader;
	private List<Status> wblist;
	private Context context;
	private Status weiBoInfo;
	private DialogListen dialogListen;
	private ViewHolder holder;
	
    
	public StatusListAdapter(Context context, List<Status> wblist){
        this.wblist = wblist;
        this.context = context;
    }         
    
	public int getCount() {     
        // TODO Auto-generated method stub
        return wblist.size();        
        }            
    public Object getItem(int arg0) {         
        // TODO Auto-generated method stub          
        return null;          
        }          
    
    public long getItemId(int arg0) {   
        // TODO Auto-generated method stub       
        return 0;         
        }      
    
    //获取ListView每一行  
    public View getView(final int position, View convertView, ViewGroup parent) {    
        holder = new ViewHolder();;
        weiBoInfo = wblist.get(position);
        if (convertView == null) {
            //convertView = LayoutInflater.from(context).inflate(R.layout.weibo_layout_night, parent, false);
        	convertView = LayoutInflater.from(context).inflate(R.layout.weibo_layout, parent, false);
            
        	holder.usericon = (RoundImageView) convertView.findViewById(R.id.rimg_userIcon);    
            holder.username = (WeiboTextView) convertView.findViewById(R.id.wtv_userName);
            holder.avatar_vip = (ImageView) convertView.findViewById(R.id.img_avatarVip);
            holder.faboshijian = (TextView) convertView.findViewById(R.id.tv_createTime);
            
            holder.text = (WeiboTextView) convertView.findViewById(R.id.wtv_weiboText);
            holder.ivMore = (NineGridlayout) convertView.findViewById(R.id.ngl_more);
            holder.ivOne = (CustomImageView) convertView.findViewById(R.id.cimgOne);
            holder.wiboTextDivide = (View) convertView.findViewById(R.id.v_weiboTextDivide);
            holder.wiboTextandPicDivide = (View) convertView.findViewById(R.id.v_weiboTextandPicDivide);
            
            holder.retweetstatus = (LinearLayout) convertView.findViewById(R.id.ll_retweetStatus);
            holder.retweetweibotext = (WeiboTextView) convertView.findViewById(R.id.wtv_retweetWeiboText);
            holder.retweet_ivMore = (NineGridlayout) convertView.findViewById(R.id.ngl_retweetMore);
            holder.retweet_ivOne = (CustomImageView) convertView.findViewById(R.id.cimg_retweetOne);
            
            holder.zhanfashu = (TextView) convertView.findViewById(R.id.tv_repostCount);
            holder.pinglunshu = (TextView) convertView.findViewById(R.id.tv_commentCount);
            holder.dianzanshu = (TextView) convertView.findViewById(R.id.tv_attitudeCount);
        
            
            holder.repost_listen = (LinearLayout) convertView.findViewById(R.id.ll_repostLayout);
            holder.comment_listen = (LinearLayout) convertView.findViewById(R.id.ll_commentLayout);
            holder.attitude_listen = (LinearLayout) convertView.findViewById(R.id.ll_attitudeLayout);

            holder.dianzan = (ImageView) convertView.findViewById(R.id.img_attitude);
            holder.mMore = (ImageButton) convertView.findViewById(R.id.img_moreChoice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try{
        if(wblist.get(position).user == null){
        	holder.avatar_vip.setVisibility(View.GONE);
        	holder.text.setText("微博不存在");
        	holder.wiboTextandPicDivide.setVisibility(View.GONE);
        	holder.ivMore.setVisibility(View.GONE);
        	holder.ivOne.setVisibility(View.GONE);
        	return convertView;
        }
        //System.out.println(weiBoInfo.created_at+"  "+weiBoInfo.id+"  "+weiBoInfo.idstr);
        //System.out.println(weiBoInfo.mid+"  "+weiBoInfo.geo+"  "+weiBoInfo.favorited);
        holder.dianzan.setImageResource(R.drawable.fav);
        //博主昵称，发送时间
        //if(weiBoInfo.user.verified)
        //	holder.username.setText((String) wblist.get(position).user.screen_name+"[认证]");
       // else
        	holder.username.setText((String) wblist.get(position).user.screen_name);
        	holder.username.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					goToUserPage(position);
				}
			});
        String timeFormat = getTimeFormat((String) wblist.get(position).created_at);
        String tmpS = "";
        if(weiBoInfo.source.length() != 0){
			int indexA = 0, indexB = 0;
			for(int i = 0; i < weiBoInfo.source.length(); i++){
				if(weiBoInfo.source.charAt(i) == '>'){
					indexA = i;
					break;
				}
			}
			for(int i = 0; i < weiBoInfo.source.length(); i++){
				if(weiBoInfo.source.charAt(i) == '<' && i > indexA){
					indexB = i;
					break;
				}
			}
			//holder.sourse.setText("来源: "+ weiBoInfo.source.subSequence(indexA+1, indexB));
			tmpS = (String) weiBoInfo.source.subSequence(indexA+1, indexB);
			//CharSequence character = Html.fromHtml(str_sourse); //style='text-decoration:none;'
			//holder.sourse.setText(character);
			//holder.sourse.setText(str_sourse);
			//holder.sourse.setMovementMethod(LinkMovementMethod.getInstance());
		}
        
        if(wblist.get(position).user.verified)
        	holder.avatar_vip.setVisibility(View.VISIBLE);
        else 
        	holder.avatar_vip.setVisibility(View.GONE);
        
        holder.faboshijian.setText(timeFormat+tmpS);
        //设置头像
        holder.usericon.setTag("usericon"+String.valueOf(position));
        //holder.usericon.invalidate();
        holder.usericon.setImageBitmap(null);
        Picasso.with(context).load(weiBoInfo.user.avatar_large).into(holder.usericon);
       
		//设置微博文本
        if(weiBoInfo.text.contains(ARTICLE)){
        	GetArticleOfWeibo getArticleOfWeibo = new GetArticleOfWeibo();
        	getArticleOfWeibo.execute(weiBoInfo.text.substring(weiBoInfo.text.indexOf(ARTICLE)+ARTICLE.length(), weiBoInfo.text.length()));
        	//System.out.println(weiBoInfo.text.substring(weiBoInfo.text.indexOf(ARTICLE)+ARTICLE.length(), weiBoInfo.text.length()));
        }
		holder.text.setText(weiBoInfo.text);
		holder.text.setMovementMethod(LinkMovementMethod.getInstance());
		
		//设置转发评论点赞数
		if(weiBoInfo.reposts_count == 0)
			holder.zhanfashu.setText("转发");
		else 
			holder.zhanfashu.setText(String.valueOf(weiBoInfo.reposts_count));
		if(weiBoInfo.comments_count == 0)
			holder.pinglunshu.setText("评论");
		else
			holder.pinglunshu.setText(String.valueOf(weiBoInfo.comments_count));
		if(weiBoInfo.comments_count == 0)
			holder.dianzanshu.setText("赞");
		else
			holder.dianzanshu.setText(String.valueOf(weiBoInfo.attitudes_count));
		
		//if(weiBoInfo.favorited)
		//	holder.dianzan.setImageResource(R.drawable.fav_like);
		//else {
			holder.dianzan.setImageResource(R.drawable.fav);
		//}
		
		holder.usericon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goToUserPage(position);
				Log.e("position", ""+position);
				// TODO Auto-generated method stub
				Log.e("usericon_click", wblist.get(position).user.screen_name);
			}
		});
		
		holder.repost_listen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("转发", wblist.get(position).user.screen_name);
				dialogListen.repost(position);
			}
		});
		
		holder.comment_listen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogListen.comment(position);
				Log.e("评论", wblist.get(position).user.screen_name);
			}
		});
		
		holder.attitude_listen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("点赞", wblist.get(position).user.screen_name);
				//((ImageView) convertView.findViewById(R.id.dianzan)).setImageResource(R.drawable.fav_like);
			    dialogListen.dianzan(position);
			}
		});
		
		holder.mMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogListen.more(position);
			}
		});
		
        //是否为转发
		if(weiBoInfo.retweeted_status == null){
			//不是转发
			holder.wiboTextDivide.setVisibility(View.GONE);
			holder.retweet_ivOne.setVisibility(View.GONE);
			holder.retweet_ivMore.setVisibility(View.GONE);
			holder.retweetweibotext.setVisibility(View.GONE);
			//是否有图
			if( wblist.get(position).pic_urls != null){
				holder.wiboTextandPicDivide.setVisibility(View.VISIBLE);
				//显示图片数目	
				//holder.text.setText(stringBuilder2.toString());
                //开始处理图片
				ArrayList<Image> itemList = new ArrayList<Image>();
				
				for(int i = 0; i < weiBoInfo.pic_urls.size(); i++){
					Image image = new Image(null, 0, 0);
					String u = weiBoInfo.pic_urls.get(i).replace("thumbnail", "bmiddle");
					image.setUrl(u);
					//System.out.println(u);
					//stringBuilder2.append(usl);
					itemList.add(image);
				}
				
				if (itemList.isEmpty() || itemList.isEmpty()) {
		            holder.ivMore.setVisibility(View.GONE);
		            holder.ivOne.setVisibility(View.GONE);
		        } else if (itemList.size() == 1) {
		            holder.ivMore.setVisibility(View.GONE);
		            holder.ivOne.setVisibility(View.VISIBLE);
		            holder.ivOne.setImageBitmap(null);
		            holder.ivOne.setTag(String.valueOf(position)+"0");
                    handlerOneImage(holder, itemList.get(0),false, position);
		        } else {
		            holder.ivMore.setVisibility(View.VISIBLE);
		            holder.ivOne.setVisibility(View.GONE);
		            holder.ivMore.setImagesData(itemList, position);
		        }
			}
			else {
				 holder.wiboTextandPicDivide.setVisibility(View.GONE);
				 holder.ivMore.setVisibility(View.GONE);
		         holder.ivOne.setVisibility(View.GONE);
			}
		}else{
            	//为转发微博
			holder.wiboTextandPicDivide.setVisibility(View.GONE);
			holder.wiboTextDivide.setVisibility(View.VISIBLE);
			holder.retweetstatus.setVisibility(View.VISIBLE);
			holder.ivMore.setVisibility(View.GONE);
            holder.ivOne.setVisibility(View.GONE);
            holder.retweetweibotext.setVisibility(View.VISIBLE);
            
			//设置转发微博文本
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("@");
			if(!TextUtils.isEmpty(weiBoInfo.retweeted_status.user.screen_name)){
				if(weiBoInfo.retweeted_status.user.verified)
					stringBuilder.append(wblist.get(position).retweeted_status.user.screen_name +""+ " : " + 
		                       weiBoInfo.retweeted_status.text);
				else
					stringBuilder.append(wblist.get(position).retweeted_status.user.screen_name + " : " + 
                       weiBoInfo.retweeted_status.text);
			}
			holder.retweetweibotext.setText(stringBuilder.toString());
			holder.retweetweibotext.setMovementMethod(LinkMovementMethod.getInstance());
	        
			
			//转发的微博是否有图
			if(weiBoInfo.retweeted_status.pic_urls != null){		
				//开始处理图片
				ArrayList<Image> itemList = new ArrayList<Image>();
				
				for(int i = 0; i < weiBoInfo.retweeted_status.pic_urls.size(); i++){
					Image image = new Image(null, 0, 0);
					String usl = weiBoInfo.retweeted_status.pic_urls.get(i).replace("thumbnail", "bmiddle");
					image.setUrl(usl);
					itemList.add(image);
				}
				if (itemList.isEmpty() || itemList.isEmpty()) {
			           holder.retweet_ivMore.setVisibility(View.GONE);
			           holder.retweet_ivOne.setVisibility(View.GONE);
			       } else if (itemList.size() == 1) {
			           holder.retweet_ivMore.setVisibility(View.GONE);
			           holder.retweet_ivOne.setVisibility(View.VISIBLE);
			           holder.retweet_ivOne.setImageBitmap(null);
			           holder.retweet_ivOne.setTag(String.valueOf(position)+"0");
			           handlerOneImage(holder, itemList.get(0),true, position);
			       } else {
			           holder.retweet_ivMore.setVisibility(View.VISIBLE);
			           holder.retweet_ivOne.setVisibility(View.GONE);
			           holder.retweet_ivMore.setImagesData(itemList, position);
			       }
			   }else{
				   holder.retweet_ivMore.setVisibility(View.GONE);
		           holder.retweet_ivOne.setVisibility(View.GONE);
			   }
           }
        }catch (Exception e){
        	System.out.println(e.toString());
        	System.out.println(weiBoInfo.toString());
        	
        }
    return convertView; 
    }
    
    protected void goToUserPage(int position) {
		// TODO Auto-generated method stub
    	Intent intent = new Intent();
    	intent.setClass(context, UserPageActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("screen_name", wblist.get(position).user.screen_name);
		bundle.putString("avatar_large", wblist.get(position).user.avatar_hd);
		bundle.putInt("followers_count", wblist.get(position).user.followers_count);
		bundle.putInt("friends_count", wblist.get(position).user.friends_count);
		bundle.putInt("statuses_count", wblist.get(position).user.statuses_count);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}
    
	public static String getTimeFormat(String time) {
		// TODO Auto-generated method stub
		StringBuilder s = new StringBuilder();
		String aS = "";
		aS = time.substring(0, 3);
		//s.append(aS);
		if(aS.endsWith("Mon"))
			s.append("星期一 ");
		else if (aS.endsWith("The"))
			s.append("星期二 ");
		else if (aS.endsWith("Wed"))
			s.append("星期三 ");
		else if (aS.endsWith("Thu"))
			s.append("星期四 ");
		else if (aS.endsWith("Fri"))
			s.append("星期五 ");
		else if (aS.endsWith("Sat"))
			s.append("星期六 ");
		else
			s.append("星期日 ");
		
		aS = time.substring(11, 19);
		s.append(aS + ' ');
		
		//aS = time.substring(26, 30);
		//s.append(aS);
		//s.append('.');
		//aS = time.substring(4, 7);
		
		/*if(aS.endsWith("Jan"))
			s.append("1");
		else if (aS.endsWith("Feb"))
			s.append("2");
		else if (aS.endsWith("Mar"))
			s.append("3");
		else if (aS.endsWith("Apr"))
			s.append("4");
		else if (aS.endsWith("May"))
			s.append("5");
		else if (aS.endsWith("Jun"))
			s.append("6");
		else if (aS.endsWith("Jul"))
			s.append("7");
		else if (aS.endsWith("Aug"))
			s.append("8");
		else if (aS.endsWith("Sep"))
			s.append("9");
		else if (aS.endsWith("Oct"))
			s.append("10");
		else if (aS.endsWith("Nov"))
			s.append("11");
		else
			s.append("12");
		s.append('.');
        aS = time.substring(8, 10);
        s.append(aS);*/
		return s.toString();
	}

	private void handlerOneImage(final ViewHolder viewHolder, final Image image, final Boolean isRetweet, final int position) {
        int imageWidth;
        int imageHeight;
        ScreenTools screentools = ScreenTools.instance(context);
        final int totalWidth = screentools.getScreenWidth() - screentools.dip2px(16);
        GetImageWidthAndHeigth getImageWidthAndHeigth = new GetImageWidthAndHeigth(){
        	@Override
        	protected void onPostExecute(int[] result) {
        		// TODO Auto-generated method stub
        		if(result != null){
        		int totalHeight = (int) (totalWidth*((float)result[2]/result[1]));
        		if(totalHeight > totalWidth*MyApplication.mRate)
        			totalHeight = (int) (totalWidth*MyApplication.mRate);
        		ViewGroup.LayoutParams layoutparams = viewHolder.ivOne.getLayoutParams();
        		layoutparams.height = totalHeight;
                layoutparams.width = totalWidth;
                if(!isRetweet){
                	viewHolder.ivOne.setTag(String.valueOf(position)+"0");
                	viewHolder.ivOne.setLayoutParams(layoutparams);
                    viewHolder.ivOne.setClickable(true);
                    viewHolder.ivOne.setImageBitmap(null);
                    List<Image> imageList = new ArrayList<Image>();
                    imageList.add(image);
                    viewHolder.ivOne.setImageUrl(image.getUrl(), imageList, 0);
                }
                else{
                	viewHolder.retweet_ivOne.setTag(String.valueOf(position)+"0");
                	viewHolder.retweet_ivOne.setLayoutParams(layoutparams);
                    viewHolder.retweet_ivOne.setClickable(true);
                    viewHolder.retweet_ivOne.setImageBitmap(null);
                    List<Image> imageList = new ArrayList<Image>();
                    imageList.add(image);
                    viewHolder.retweet_ivOne.setImageUrl(image.getUrl(), imageList, 0);
                }
        	}
        	}
        };
        getImageWidthAndHeigth.execute(image.getUrl(), "0");
    }
	
	public interface DialogListen {
		public void repost(int position);
		public void more(int position);
		public void dianzan(int position);
		public void comment(int position);
	}
	
	public void setDialogListen(DialogListen dialogListen) {
		this.dialogListen = dialogListen;
	}
	
    class ViewHolder{          
        public RoundImageView usericon;     
        public WeiboTextView text, retweetweibotext, username;
        public TextView faboshijian;
        public View zhuanfa, pinglun, wiboTextDivide, wiboTextandPicDivide;
        public TextView zhanfashu, pinglunshu, dianzanshu;
        public ImageView dianzan, avatar_vip;
        public LinearLayout retweetstatus;
        public LinearLayout repost_listen, comment_listen, attitude_listen;
        public NineGridlayout ivMore , retweet_ivMore;
        public CustomImageView ivOne, retweet_ivOne;
        public ImageView mMore;
	}  

}
