package com.example.weibo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.view.SwipeBackLayout;
import com.weibo.tools.MyApplication;

public class PostActivity extends AppCompatActivity {

    private SwipeBackLayout mSwipeBackLayout;
    private LinearLayout mLinearLayout;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_post);
        mSwipeBackLayout = (SwipeBackLayout) findViewById(R.id.swpb_search);
        mLinearLayout = (LinearLayout) findViewById(R.id.ll_postBackground);
        mLinearLayout.setBackgroundColor(MyApplication.mThemeColor);
        mSwipeBackLayout.setContentView(mLinearLayout, this);
        mTitle = (TextView) findViewById(R.id.title_text);
        mTitle.setTextColor(MyApplication.mTitleColor);
        mTitle.setText("新建微博");
    }

}
