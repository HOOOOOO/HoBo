package com.weibo.adapter;

import java.util.ArrayList;

import com.example.weibo.R;
import com.sina.weibo.sdk.openapi.models.Group;
import com.weibo.tools.MyApplication;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class MyExpandableAdapter implements ExpandableListAdapter{

	private final static String TAG = "MyExpandableAdapter.";
	private ArrayList<Group> mGroup;
	private Context mContext;
	public final static String[] GROUPNAME = new String[]{
			"发微博", "分组查看", "我的收藏", "账号管理", "设置", "退出HOBO"
	};
	
	public MyExpandableAdapter(ArrayList<Group> group, Context context) {
		// TODO Auto-generated constructor stub
		this.mGroup = group;
		this.mContext = context;
	}
	
	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		System.out.println(TAG+" "+groupPosition+" "+childPosition);
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub
		//System.out.println(TAG+" getChildView"+" "+childPosition);
		TextView textView = null;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.group_list_item, parent, false);
			textView = (TextView) convertView.findViewById(R.id.tv_groupName);
			textView.setTextSize(14);
			convertView.setTag(textView);
		}
		else {
			textView = (TextView)convertView.getTag();
		}
		textView.setText(mGroup.get(childPosition).name);
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		if(groupPosition == 1){
			//System.out.println(TAG+" getChildrenCount "+mGroup.size()+" groupPosition "+groupPosition);
			return mGroup.size();
		}
		else
			return 0;
	}

	@Override
	public long getCombinedChildId(long groupId, long childId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getCombinedGroupId(long groupId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return GROUPNAME.length;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(MyApplication.mIsNightMode)
			convertView = LayoutInflater.from(mContext).inflate(R.layout.group_list_item_night, parent, false);
		else
			convertView = LayoutInflater.from(mContext).inflate(R.layout.group_list_item, parent, false);
		((TextView)convertView.findViewById(R.id.tv_groupName)).setText(GROUPNAME[groupPosition]);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		
	}

}
