package com.example.weibo;

import com.example.view.CircleImageView;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.legacy.GroupAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.GroupList;
import com.sina.weibo.sdk.openapi.models.User;
import com.weibo.sql.DataHelper;
import com.weibo.tools.AccessTokenKeeper;
import com.weibo.tools.Constants;
import com.weibo.tools.GetUserImageByURL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FirstLoginActivity extends Activity{

	private Oauth2AccessToken mAccessToken;
	private SsoHandler mSsoHandler;
	private AuthInfo mAuthInfo;
	private UsersAPI mUsersAPI;
	private GroupAPI mGroupAPI;
	private Button button_login; 
	private User mUser;
	private TextView mTextView;
	private CircleImageView mUserIcon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*// 隐藏标题栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);*/
		// 隐藏状态栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_first_login);
		
		mTextView = (TextView) findViewById(R.id.user_name);
		mUserIcon = (CircleImageView) findViewById(R.id.ri_usericon);
		button_login = (Button) findViewById(R.id.button_login);
		getUserInfo();
	}

	protected void getUserInfo() {
       button_login.setOnClickListener(new OnClickListener() {
				
			@Override
			public void onClick(View v) {
				System.out.println("点击按钮");
				// TODO Auto-generated method stub
		        mAuthInfo = new AuthInfo(FirstLoginActivity.this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
		    	mSsoHandler = new SsoHandler(FirstLoginActivity.this, mAuthInfo);
		    	//all_in_one
		    	mSsoHandler.authorize(new AuthListener());	
				}
			});
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
            	button_login.setVisibility(View.INVISIBLE);
            	 // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(FirstLoginActivity.this, mAccessToken);
                //保存Token到数据库
                new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						saveUserInfo();
					}
				}).start();
                // 获取用户信息接口
                mUsersAPI = new UsersAPI(FirstLoginActivity.this, Constants.APP_KEY, mAccessToken);
                long uid = Long.parseLong(mAccessToken.getUid());
        		mUsersAPI.show(uid, mListener);
        		System.out.println("授权成功");
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
                Toast.makeText(FirstLoginActivity.this, message, Toast.LENGTH_LONG).show();
                System.out.print("授权失败");
            }
        }

        @Override
        public void onCancel() {
            Toast.makeText(FirstLoginActivity.this, 
                   R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(FirstLoginActivity.this, 
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
            System.out.print(""+e);
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
                    Toast.makeText(FirstLoginActivity.this, 
                            "获取User信息成功，用户昵称：" + mUser.screen_name, 
                            Toast.LENGTH_LONG).show();
                    mTextView.setText(mUser.screen_name);
                    button_login.setVisibility(View.GONE);
                    
                    mGroupAPI = new GroupAPI(FirstLoginActivity.this, Constants.APP_KEY, mAccessToken);
                    mGroupAPI.groups(mGroupListener);
                    new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							DataHelper dataHelper = new DataHelper(FirstLoginActivity.this);
		                    dataHelper.UpdateUserInfo(mUser);
		                    dataHelper.Close();
						}
					}).start();
                    
					String usl = mUser.avatar_hd;
					GetUserImageByURL getUserImageByURL = new GetUserImageByURL(){
						protected void onPostExecute(Bitmap result) {
							mUserIcon.setImageBitmap(result);
							Toast.makeText(getApplicationContext(), "获取用户头像成功", Toast.LENGTH_LONG).show();
							
						};
					};
					getUserImageByURL.execute(usl, mUser.id);
                } else {
                    Toast.makeText(FirstLoginActivity.this, response, Toast.LENGTH_LONG).show();
                }
            }
        }
        @Override
        public void onWeiboException(WeiboException e) {
            //LogUtil.e(TAG, e.getMessage());
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            Toast.makeText(FirstLoginActivity.this, info.toString(), Toast.LENGTH_LONG).show();
        }
    };
    
    private RequestListener mGroupListener = new RequestListener() {
		
		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub
			System.out.println(arg0);
		}
		
		@Override
		public void onComplete(String response) {
			// TODO Auto-generated method stub
			System.out.println(response);
			GroupList groups = GroupList.parse(response);
			if(groups.groupList != null){
				DataHelper dataHelper = new DataHelper(getApplicationContext());
				dataHelper.CreateGroupTable(mUser.idstr);
				dataHelper.SaveGroupInfo(mUser.idstr, groups.groupList);
				Toast.makeText(getApplicationContext(), "保存分组信息成功", Toast.LENGTH_LONG).show();
				Intent intent = new Intent();
				intent.setClass(FirstLoginActivity.this, MainActivity.class);
				startActivity(intent);
			}
		}
	};

}
