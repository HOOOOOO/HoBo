package com.example.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.weibo.R;

/**
 * Created by Administrator on 2016/4/21.
 */
public class CustomFloatingButton extends FrameLayout{

    private ImageView ivBackground;
    private ImageView ivIcon;

    public CustomFloatingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.view_custom_floating_button, this, false);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ivBackground = (ImageView) findViewById(R.id.iv_fab_background);
        ivIcon = (ImageView) findViewById(R.id.iv_fab_icon);
    }

    public void setBackgroundColor(int color){
        ivBackground.setColorFilter(color);
    }

    public void setIconColor(int color){
        ivIcon.setColorFilter(color);
    }
}
