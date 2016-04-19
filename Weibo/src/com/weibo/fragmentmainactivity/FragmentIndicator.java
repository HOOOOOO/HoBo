package com.weibo.fragmentmainactivity;  
  
import com.example.weibo.R;
import com.example.weibo.R.color;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;  
  
/** 
 * @author yangyu 
 *  功能描述：自定义底部工具栏 
 */  
public class FragmentIndicator extends LinearLayout implements OnClickListener {  
  
    private int mDefaultIndicator = 0;  
  
    private static int mCurIndicator;  
  
    private static View[] mIndicators;  
  
    private OnIndicateListener mOnIndicateListener;  
  
    private static final String TAG_ICON_0 = "icon_tag_0";  
    private static final String TAG_ICON_1 = "icon_tag_1";  
    private static final String TAG_ICON_2 = "icon_tag_2";  
    private static final String TAG_ICON_3 = "icon_tag_3";  
  
    private static final String TAG_TEXT_0 = "text_tag_0";  
    private static final String TAG_TEXT_1 = "text_tag_1";  
    private static final String TAG_TEXT_2 = "text_tag_2";  
    private static final String TAG_TEXT_3 = "text_tag_3"; 
      
    private static final int COLOR_UNSELECT = 0xff797979;//Color.argb(100, 0xff, 0xff, 0xff);  
    private static final int COLOR_SELECT = 0xffff8200;//R.color.themeclor;  
  
    private FragmentIndicator(Context context) {  
        super(context);  
    }  
    
    public FragmentIndicator(Context context, AttributeSet attrs) {  
        super(context, attrs);  
  
        mCurIndicator = mDefaultIndicator;  
        setOrientation(LinearLayout.HORIZONTAL);  
        setGravity(Gravity.CENTER_VERTICAL);  
        init();  
    }  
  
    private void init() {  
        mIndicators = new View[4];  
        
        mIndicators[0] = createIndicator(R.drawable.shouye,  
                R.string.tab_home, COLOR_SELECT, TAG_ICON_0, TAG_TEXT_0); 
        //mIndicators[0].setBackgroundColor(Color.alpha(0));  
        //mIndicators[0].setBackgroundResource(R.drawable.indic_select);
        mIndicators[0].setBackgroundColor(Color.alpha(0));
        mIndicators[0].setTag(Integer.valueOf(0));  
        mIndicators[0].setOnClickListener(this);  
        addView(mIndicators[0]);  
        
        mIndicators[1] = createIndicator(R.drawable.hottopic,  
                R.string.tab_hottopic, COLOR_UNSELECT, TAG_ICON_1, TAG_TEXT_1);  
        mIndicators[1].setBackgroundColor(Color.alpha(0));  
        mIndicators[1].setTag(Integer.valueOf(1));  
        mIndicators[1].setOnClickListener(this);  
        addView(mIndicators[1]);  
       
        ImageView plusion = new ImageView(getContext());  
        //iconView.setTag(iconTag);  
        plusion.setLayoutParams(new LinearLayout.LayoutParams(  
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));  
        plusion.setImageResource(R.drawable.plusicon);  
        addView(plusion);
        
        mIndicators[2] = createIndicator(R.drawable.hotweibo,  
                R.string.tab_hotweibo, COLOR_UNSELECT, TAG_ICON_2, TAG_TEXT_2);  
        mIndicators[2].setBackgroundColor(Color.alpha(0));  
        mIndicators[2].setTag(Integer.valueOf(2));  
        mIndicators[2].setOnClickListener(this);  
        addView(mIndicators[2]);  
        
        mIndicators[3] = createIndicator(R.drawable.me,  
                R.string.tab_me, COLOR_UNSELECT, TAG_ICON_3, TAG_TEXT_3);  
        mIndicators[3].setBackgroundColor(Color.alpha(0));  
        mIndicators[3].setTag(Integer.valueOf(3));  
        mIndicators[3].setOnClickListener(this);  
        addView(mIndicators[3]);  
    }  
    
    private View createIndicator(int iconResID, int stringResID, int stringColor,   
            String iconTag, String textTag) {  
        
    	LinearLayout view = new LinearLayout(getContext());  
        
    	
    	view.setOrientation(LinearLayout.VERTICAL);  
        view.setLayoutParams(new LinearLayout.LayoutParams(  
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));  
        view.setGravity(Gravity.CENTER_HORIZONTAL);  
  
        ImageView iconView = new ImageView(getContext());  
        iconView.setTag(iconTag);  
        iconView.setLayoutParams(new LinearLayout.LayoutParams(  
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));  
        iconView.setImageResource(iconResID);  
  
        TextView textView = new TextView(getContext());  
        textView.setTag(textTag);  
        textView.setLayoutParams(new LinearLayout.LayoutParams(  
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));  
        textView.setTextColor(stringColor);  
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);  
        textView.setText(stringResID);  
        
       // TextView blankView = new TextView(getContext());  
        //blankView.setLayoutParams(new LinearLayout.LayoutParams(  
         //       LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));  
        //textView.setTextColor(stringColor);  
        //blankView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 1);  
        //blankView.setText(" ");  
       //View nothingView= new View(getContext());
       //nothingView.setMinimumHeight(TypedValue.COMPLEX_UNIT_SP);
        
        view.addView(iconView);  
        //view.addView(nothingView);
        //view.addView(blankView);
        //view.addView(textView);  
  
        return view;  
  
    } 
  
    public static void setIndicator(int which) {  
        // clear previous status.  
        mIndicators[mCurIndicator].setBackgroundColor(Color.alpha(0));  
        ImageView prevIcon;  
        TextView prevText;  
        switch(mCurIndicator) {  
        case 0:  
            prevIcon =(ImageView) mIndicators[mCurIndicator].findViewWithTag(TAG_ICON_0);  
            prevIcon.setImageResource(R.drawable.shouye);  
            //prevText = (TextView) mIndicators[mCurIndicator].findViewWithTag(TAG_TEXT_0);  
            //prevText.setTextColor(COLOR_UNSELECT);  
            break;  
        case 1:  
            prevIcon =(ImageView) mIndicators[mCurIndicator].findViewWithTag(TAG_ICON_1);  
            prevIcon.setImageResource(R.drawable.hottopic);  
            //prevText = (TextView) mIndicators[mCurIndicator].findViewWithTag(TAG_TEXT_1);  
            //prevText.setTextColor(COLOR_UNSELECT);  
            break;  
        case 2:  
            prevIcon =(ImageView) mIndicators[mCurIndicator].findViewWithTag(TAG_ICON_2);  
            prevIcon.setImageResource(R.drawable.hotweibo);  
            //prevText = (TextView) mIndicators[mCurIndicator].findViewWithTag(TAG_TEXT_2);  
            //prevText.setTextColor(COLOR_UNSELECT);  
            break;
        case 3:  
            prevIcon =(ImageView) mIndicators[mCurIndicator].findViewWithTag(TAG_ICON_3);  
            prevIcon.setImageResource(R.drawable.me);  
            //prevText = (TextView) mIndicators[mCurIndicator].findViewWithTag(TAG_TEXT_3);  
            //prevText.setTextColor(COLOR_UNSELECT);  
            break;  
        }  
          
        // update current status.  
        //mIndicators[which].setBackgroundResource(R.drawable.buttonbar);  
        ImageView currIcon;  
        TextView currText;  
        switch(which) {  
        case 0:  
            currIcon =(ImageView) mIndicators[which].findViewWithTag(TAG_ICON_0);  
            currIcon.setImageResource(R.drawable.shouyecolor);  
            //currText = (TextView) mIndicators[which].findViewWithTag(TAG_TEXT_0);  
            //currText.setTextColor(COLOR_SELECT);  
            break;  
        case 1:  
            currIcon =(ImageView) mIndicators[which].findViewWithTag(TAG_ICON_1);  
            currIcon.setImageResource(R.drawable.hottopiccolor);  
            //currText = (TextView) mIndicators[which].findViewWithTag(TAG_TEXT_1);  
            //currText.setTextColor(COLOR_SELECT);  
            break;  
        case 2:  
            currIcon =(ImageView) mIndicators[which].findViewWithTag(TAG_ICON_2);  
            currIcon.setImageResource(R.drawable.hotweibocolor);  
            //currText = (TextView) mIndicators[which].findViewWithTag(TAG_TEXT_2);  
            //currText.setTextColor(COLOR_SELECT); 
            break;
        case 3:  
            currIcon =(ImageView) mIndicators[which].findViewWithTag(TAG_ICON_3);  
            currIcon.setImageResource(R.drawable.mecolor);  
            //currText = (TextView) mIndicators[which].findViewWithTag(TAG_TEXT_3);  
            //currText.setTextColor(COLOR_SELECT); 
            break;  
        }  
          
        mCurIndicator = which;  
    }  
  
    public interface OnIndicateListener {  
        public void onIndicate(View v, int which);  
    }  
  
    public void setOnIndicateListener(OnIndicateListener listener) {  
        mOnIndicateListener = listener;  
    }  
  
    @Override  
    public void onClick(View v) {  
        if (mOnIndicateListener != null) {  
            int tag = (Integer) v.getTag();  
            switch (tag) {  
            case 0:  
                if (mCurIndicator != 0) {  
                    mOnIndicateListener.onIndicate(v, 0);  
                    setIndicator(0);  
                }  
                break;  
            case 1:  
                if (mCurIndicator != 1) {  
                    mOnIndicateListener.onIndicate(v, 1);  
                    setIndicator(1);  
                }  
                break;  
            case 2:  
                if (mCurIndicator != 2) {  
                    mOnIndicateListener.onIndicate(v, 2);  
                    setIndicator(2);  
                }  
                break;  
            case 3:  
                if (mCurIndicator != 3) {  
                    mOnIndicateListener.onIndicate(v, 3);  
                    setIndicator(3);  
                }  
                break;  
            default:  
                break;  
            }  
        }  
    }  
}  