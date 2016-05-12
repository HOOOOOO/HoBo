package com.weibo.adapter;

import java.util.ArrayList;

import com.example.view.CircleImageView;
import com.example.view.WeiboTextView;
import com.example.weibo.R;
import com.example.weibo.UserPageActivity;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.squareup.picasso.Picasso;
import com.weibo.tools.MyApplication;
import com.weibo.tools.Tools;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter{
	
	private ArrayList<Comment> mComments;
	private Context mContext;
	
	public CommentAdapter(ArrayList<Comment> comments, Context context) {
		// TODO Auto-generated constructor stub
		this.mComments = comments;
		this.mContext = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mComments.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = new ViewHolder();
		if(convertView == null){
			if(MyApplication.mIsNightMode)
				convertView = LayoutInflater.from(mContext).inflate(R.layout.comment_layout_night, parent, false);
			else		
				convertView = LayoutInflater.from(mContext).inflate(R.layout.comment_layout, parent, false);
			viewHolder.mCommentText = (WeiboTextView) convertView.findViewById(R.id.comment_text);
			viewHolder.mUserIcon = (CircleImageView) convertView.findViewById(R.id.comment_usericon);
			viewHolder.mCreateTime = (TextView) convertView.findViewById(R.id.comment_createtime);
			convertView.setTag(viewHolder);
		}
		viewHolder = (ViewHolder) convertView.getTag();
		Picasso.with(mContext).load(mComments.get(position).user.avatar_large).into(viewHolder.mUserIcon);
		if(mComments.get(position).user.verified)
			viewHolder.mCommentText.setText("@"+mComments.get(position).user.screen_name+""+" : "+mComments.get(position).text);
		else
			viewHolder.mCommentText.setText("@"+mComments.get(position).user.screen_name+" : "+mComments.get(position).text);
		
		viewHolder.mCreateTime.setText(Tools.getTimeFormat((mComments.get(position).created_at)));
		viewHolder.mUserIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goToUserPage(position);
			}
		});
		return convertView;
	}
	
	protected void goToUserPage(int position) {
		// TODO Auto-generated method stub
    	Intent intent = new Intent();
    	intent.setClass(mContext, UserPageActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("screen_name", mComments.get(position).user.screen_name);
		bundle.putString("avatar_large", mComments.get(position).user.avatar_hd);
		bundle.putInt("followers_count", mComments.get(position).user.followers_count);
		bundle.putInt("friends_count", mComments.get(position).user.friends_count);
		bundle.putInt("statuses_count", mComments.get(position).user.statuses_count);
		intent.putExtras(bundle);
		mContext.startActivity(intent);
	}
	
	class ViewHolder {
		public CircleImageView mUserIcon;
		public WeiboTextView mCommentText;
		public TextView mCreateTime;
	}

}
