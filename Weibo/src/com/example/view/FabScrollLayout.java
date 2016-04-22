package com.example.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * Created by Administrator on 2016/4/21.
 */
public class FabScrollLayout extends RelativeLayout{

    private Scroller mScroller;
    public FabScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    public void hideFab(){
        mScroller.startScroll(0, getScrollY(), 0, -(getMeasuredHeight() - getScrollY()), 1000);
        invalidate();
    }

    public void showFab(){
        mScroller.startScroll(0, getScrollY(), 0, 0 - getScrollY(), 1000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()){
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }

    }
}
