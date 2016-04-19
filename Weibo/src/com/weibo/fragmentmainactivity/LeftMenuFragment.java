package com.weibo.fragmentmainactivity;

import java.util.ArrayList;

import com.example.view.RoundImageView;
import com.example.view.RoundView;
import com.example.weibo.ChangeUserActivity;
import com.example.weibo.FavoriteActivity;
import com.example.weibo.MainActivity;
import com.example.weibo.R;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.GroupAPI;
import com.sina.weibo.sdk.openapi.models.Group;
import com.sina.weibo.sdk.openapi.models.GroupList;
import com.sina.weibo.sdk.openapi.models.User;
import com.weibo.adapter.MyExpandableAdapter;
import com.weibo.sql.DataHelper;
import com.weibo.tools.AccessTokenKeeper;
import com.weibo.tools.Constants;
import com.weibo.tools.GetUserImageByURL;
import com.weibo.tools.MyApplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LeftMenuFragment extends Fragment implements OnClickListener{
	
	//private Button mSend, mGroup, mCollect, mUserManager, mSetting;
	private RoundImageView mUserIcon;
	private ImageView mUserBackground;
	private TextView mUSerName, mCountOfFans, mCountOfWeibo, mCountOfFollowing;
	private LinearLayout mFans, mFollowing, mStatus;
	private ExpandableListView mGroupListView;
	private MyExpandableAdapter mGroupAdapter;
	private ArrayList<Group> mGroupList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = null;
		if(MyApplication.mIsNightMode)
			view = inflater.inflate(R.layout.left_drawer_night, container, false);
		else
			view = inflater.inflate(R.layout.left_drawer, container, false);
//		mSend = (Button) view.findViewById(R.id.btn_send);
//		mGroup = (Button) view.findViewById(R.id.btn_group);
//		mCollect = (Button) view.findViewById(R.id.btn_collection);
//		mUserManager = (Button) view.findViewById(R.id.btn_usersmanager);
//		mSetting = (Button) view.findViewById(R.id.btn_setting);
		mUserIcon = (RoundImageView) view.findViewById(R.id.left_drawer_user_image);
		mUserBackground = (ImageView) view.findViewById(R.id.left_drawer_user_backgruand);
		mUSerName = (TextView) view.findViewById(R.id.left_drawer_user_name);
		mCountOfFans = (TextView) view.findViewById(R.id.count_of_fans);
		mCountOfFollowing = (TextView) view.findViewById(R.id.count_of_following);
		mCountOfWeibo = (TextView) view.findViewById(R.id.count_of_status);
		mStatus = (LinearLayout) view.findViewById(R.id.btn_status);
		mFans = (LinearLayout) view.findViewById(R.id.btn_fans);
		mFollowing = (LinearLayout) view.findViewById(R.id.btn_following);
		
		mGroupListView = (ExpandableListView) view.findViewById(R.id.explv_groupList);
		mGroupList = new ArrayList<Group>();
		for(int i = 0; i < 10; i++){
			Group group = new Group();
			group.name = "Group"+String.valueOf(i);
			mGroupList.add(group);
		}
		mGroupAdapter = new MyExpandableAdapter(mGroupList, getContext());
		mGroupListView.setAdapter(mGroupAdapter);
		mGroupListView.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				// TODO Auto-generated method stub
				Intent intent = null;
				System.out.println("OnGroupClickListener "+MyExpandableAdapter.GROUPNAME[groupPosition]);;
				switch (groupPosition) {
				case 0:
					
					break;
				case 1:
					
					break;
				case 2:
					intent = new Intent();
					intent.setClass(getActivity(), FavoriteActivity.class);
					((MainActivity)getActivity()).startActivity(intent, false);
					break;
					
				case 3:
					intent = new Intent();
					intent.setClass(getActivity(), ChangeUserActivity.class);
					((MainActivity)getActivity()).startActivity(intent, false);
					break;
					
				case 4:
					break;
				case 5:
					break;
				default:
					break;
				}
				return false;
			}
		});
		
		mGroupListView.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				if(groupPosition == 1){
					Toast.makeText(getContext(), mGroupList.get(childPosition).name, Toast.LENGTH_LONG).show();
					return true;
				}
				return false;
			}
		});
		
		mUserIcon.setOnClickListener(this);
		mFans.setOnClickListener(this);
		mFollowing.setOnClickListener(this);
		mStatus.setOnClickListener(this);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		GetUserImageByURL getUserImageByURL = new GetUserImageByURL(){
			protected void onPostExecute(android.graphics.Bitmap result) {
				mUserIcon.setImageBitmap(result);
				//mUserBackground.setImageBitmap(result);
			};
		};
		
		Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(getActivity());
		if(accessToken.isSessionValid()){
			DataHelper dataHelper = new DataHelper(getActivity());
			User u = dataHelper.getUserByUid(accessToken.getUid());
			mCountOfFans.setText(String.valueOf(u.followers_count));
			mCountOfFollowing.setText(String.valueOf(u.friends_count));
			mCountOfWeibo.setText(String.valueOf(u.statuses_count));
			mUSerName.setText(u.screen_name);
			getUserImageByURL.execute(u.avatar_hd, accessToken.getUid());
			
			//ArrayList<Group> groups = dataHelper.GetGroupInfo(accessToken.getUid());
			//Toast.makeText(getContext(), "groups.size() "+groups.size(), Toast.LENGTH_LONG).show();
			//System.out.println(groups.get(0).name);
		}
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		case R.id.btn_fans:
			
			break;
			
		case R.id.btn_following:
			
			break;
			
		case R.id.btn_status:
			
			
	
			
			break;
		default:
			break;
		}
	}
}
