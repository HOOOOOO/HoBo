package com.example.weibo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import com.android.style.AndroidTitleBar;
import com.example.view.CustomDialogFragment;
import com.example.view.CustomDialogFragment.DialogClickListener;
import com.example.view.RoundImageView;
import com.example.view.SwipeBackLayout;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.User;
import com.squareup.picasso.Picasso;
import com.weibo.sql.DataHelper;
import com.weibo.tools.AccessTokenKeeper;
import com.weibo.tools.Constants;
import com.weibo.tools.GetUserImageByURL;
import com.weibo.tools.GetUserImgByPath;
import com.weibo.tools.MyApplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChangeUserActivity extends FragmentActivity {
    
	private ListView mListVIew;
	private UsersListAdapter adapter;
	private ArrayList<User> usersL;
	private Button btn_adduser;
	
	private User mUser;
	private Oauth2AccessToken mAccessToken;
	private SsoHandler mSsoHandler;
	private AuthInfo mAuthInfo;
	private UsersAPI mUsersAPI;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(MyApplication.mIsNightMode)
			setContentView(R.layout.activity_change_user_night);
		else
			setContentView(R.layout.activity_change_user);
		
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.ll_activity_back);
		layout.setBackgroundColor(MyApplication.mThemeColor);
		SwipeBackLayout swipeBackLayout = (SwipeBackLayout) findViewById(R.id.swpl_changUser);
		swipeBackLayout.setContentView(layout, this);
		//View view = findViewById(R.id.v_status_background);
		//view.setBackgroundColor(MyApplication.mThemeColor);
		TextView textView = (TextView) findViewById(R.id.title_text);
	    textView.setBackgroundColor(MyApplication.mThemeColor);
	    textView.setTextColor(MyApplication.mTitleColor);
        textView.setText("账号管理");
		mUser = new User();
		mListVIew = (ListView) findViewById(R.id.users_list);
		
		final DataHelper dataHelper = new DataHelper(getApplicationContext());
		usersL = new ArrayList<User>();
		usersL = (ArrayList<User>) dataHelper.GetUserList(false);
		//dataHelper.Close();
		//Toast.makeText(getApplicationContext(), ""+users.get(0).screen_name, Toast.LENGTH_LONG).show();
		if(!usersL.isEmpty()){
		   adapter = new UsersListAdapter(usersL);
		   mListVIew.setAdapter(adapter);
		}
		else {
			Toast.makeText(getApplicationContext(), "users.isEmpty()", Toast.LENGTH_LONG).show();
		}
		
		
		mListVIew.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Log.e("onItemClick", "onItemClick");
				Log.e("usersL.get("+""+position+").id", usersL.get(position).id);
				Oauth2AccessToken tmpAccessToken = new Oauth2AccessToken();
				tmpAccessToken =  dataHelper.getAccessToken(usersL.get(position).id);
				//if(!tmpAccessToken.equals(null)) {
				Log.e("ChangeUserActivity.class", tmpAccessToken.getUid());
				AccessTokenKeeper.clear(getApplicationContext());
				AccessTokenKeeper.writeAccessToken(ChangeUserActivity.this, tmpAccessToken);
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), MainActivity.class);
				startActivity(intent);
				finish();//}
			}
		});
		
		mListVIew.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
				// TODO Auto-generated method stub
				//Toast.makeText(getApplicationContext(), ""+position, Toast.LENGTH_LONG).show();
				CustomDialogFragment customDialogFragment = CustomDialogFragment.newInstance("删除账号", "重新授权");
				customDialogFragment.setListener(new DialogClickListener() {
					
					@Override
					public void choose(int p) {
						// TODO Auto-generated method stub
						switch (p) {
						case 1:
							DataHelper dataHelper = new DataHelper(getApplicationContext());
							String uid = usersL.get(position).idstr;
							if(mAccessToken.getUid() == usersL.get(position).idstr){
								AccessTokenKeeper.clear(getApplicationContext());
								if(usersL.size() > 1){
									Oauth2AccessToken tmpAccessToken = new Oauth2AccessToken();
									tmpAccessToken =  dataHelper.getAccessToken(usersL.get(usersL.size() - position).id);
									AccessTokenKeeper.writeAccessToken(ChangeUserActivity.this, tmpAccessToken);
								}
							}
							usersL.remove(position);
							adapter.notifyDataSetChanged();
							dataHelper.DelTokenInfo(uid);
							dataHelper.DelUserInfo(uid);
							break;
						case 2:
							mAuthInfo = new AuthInfo(ChangeUserActivity.this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
				    		mSsoHandler = new SsoHandler(ChangeUserActivity.this, mAuthInfo);
				    		//all_in_one
				    		mSsoHandler.authorize(new AuthListener());
							break;
						default:
							break;
						}
					}
				});
				customDialogFragment.show(getSupportFragmentManager(), "change");
				return true;
			}
			
		});
		
		btn_adduser = (Button) findViewById(R.id.btn_adduser);
		btn_adduser.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mAuthInfo = new AuthInfo(ChangeUserActivity.this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
	    		mSsoHandler = new SsoHandler(ChangeUserActivity.this, mAuthInfo);
	    		//all_in_one
	    		mSsoHandler.authorize(new AuthListener());
				}
		});
		
	}
	
	public class UsersListAdapter extends BaseAdapter{
		
		private ArrayList<User> users;
		
		public UsersListAdapter(ArrayList<User> userList) {
			// TODO Auto-generated constructor stub
			this.users = userList;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			Log.e("users.size()", ""+users.size());
			//Toast.makeText(getApplicationContext(), ""+users.size(), Toast.LENGTH_LONG).show();
			return users.size();
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

		@SuppressWarnings("null")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			UsersListItemHolder usersListItemHolder = null;
			
			if(convertView == null) {
				usersListItemHolder = new UsersListItemHolder();
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.change_user_item, parent, false);
	            usersListItemHolder.user_icon = (RoundImageView) convertView.findViewById(R.id.change_user_icon);
	            usersListItemHolder.user_name = (TextView) convertView.findViewById(R.id.change_user_name);
	            convertView.setTag(usersListItemHolder);
			}
			else {
				usersListItemHolder = (UsersListItemHolder) convertView.getTag();
			}
			//System.out.println(users.get(position).idstr);
			System.out.println(users.get(position).id);
			//GetUserImgByPath getUserImgByPath = new GetUserImgByPath(users.get(position).id);
			//getUserImgByPath.execute(usersListItemHolder.user_icon);
			File file = new File(getApplicationContext().getFilesDir().getAbsolutePath()+MyApplication.PATH_OF_USERICON+"/"+users.get(position).id+".jpg");
			Picasso.with(getApplicationContext()).load(file).into(usersListItemHolder.user_icon);
			//new GetUserImageByURL().execute(mUser.avatar_hd, mUser.idstr, usersListItemHolder.user_icon);
			usersListItemHolder.user_name.setText(users.get(position).screen_name);
			return convertView;
		}
	}
	
	class UsersListItemHolder {
		public RoundImageView user_icon;
		public TextView user_name; 
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        //super.onActivityResult(requestCode, resultCode, data);
        if (mSsoHandler != null) {
        mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     *    该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
	class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
        	 // 从 Bundle 中解析 Token
        	mAccessToken = Oauth2AccessToken.parseAccessToken(values);
        	mAccessToken.getPhoneNum();
            if (mAccessToken.isSessionValid()) {
            	 // 保存 Token 到 SharedPreferences
               // AccessTokenKeeper.writeAccessToken(ChangeUserActivity.this, mAccessToken);
                //保存Token到数据库
                saveUserInfo();
                
             // 获取用户信息接口
                mUsersAPI = new UsersAPI(ChangeUserActivity.this, Constants.APP_KEY, mAccessToken);
                long uid = Long.parseLong(mAccessToken.getUid());
        		mUsersAPI.show(uid, mListener);
            } else {
            	// 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");
                String message = getString(R.string.weibosdk_demo_toast_auth_failed);
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                Toast.makeText(ChangeUserActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancel() {
            Toast.makeText(ChangeUserActivity.this, 
                   R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(ChangeUserActivity.this, 
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
	
	private void saveUserInfo(){
    	DataHelper helper=new DataHelper(this);
    	helper.SaveUserInfo(mAccessToken);
    	Log.e("UserInfo", "add");
    }
	
	 /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {

		@Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                //LogUtil.i(TAG, response);
                // 调用 User#parse 将JSON串解析成User对象
                mUser = User.parse(response);
                if (mUser != null) {
                    Toast.makeText(ChangeUserActivity.this, 
                            "获取User信息成功，用户昵称：" + mUser.screen_name, 
                            Toast.LENGTH_LONG).show();
                    DataHelper dataHelper = new DataHelper(ChangeUserActivity.this);
                    dataHelper.UpdateUserInfo(mUser);
                    dataHelper.Close();
                    //mTextView.setText(user.screen_name);
                    String usl = mUser.avatar_hd;
					GetUserImageByURL getUserImageByURL = new GetUserImageByURL(){
						protected void onPostExecute(Bitmap result) {
							Toast.makeText(getApplicationContext(), "获取用户头像成功", Toast.LENGTH_LONG).show();
							for(int i = 0; i < usersL.size(); i++){
								if(usersL.get(i).idstr == mUser.idstr){
									usersL.get(i).screen_name = mUser.screen_name;
									adapter.notifyDataSetChanged();
									return;
								}
							}
							usersL.add(mUser);
							adapter.notifyDataSetChanged();
						};
					};
					getUserImageByURL.execute(usl, mUser.id);
                } else {
                    Toast.makeText(ChangeUserActivity.this, response, Toast.LENGTH_LONG).show();
                }
            }
        }
        @Override
        public void onWeiboException(WeiboException e) {
            //LogUtil.e(TAG, e.getMessage());
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            Toast.makeText(ChangeUserActivity.this, info.toString(), Toast.LENGTH_LONG).show();
        }
    };
    
    public void startActivity(Intent intent) {
    	super.startActivity(intent);
		overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
    };
}
