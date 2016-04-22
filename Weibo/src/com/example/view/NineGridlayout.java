package com.example.view;

import java.util.List;

import com.weibo.tools.GetImageWidthAndHeigth;
import com.weibo.tools.ScreenTools;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * Created by Pan_ on 2015/2/2.
 */
public class NineGridlayout extends ViewGroup {

    /**
     * 图片之间的间隔
     */
    private int gap = 5;
    private int columns;//
    private int rows;//
    private List<Image> listData;
    private int totalWidth;
    private int mPositive;
	private int singleHeight;
	private int singleWidth;
	private int mPosition;
	private int childrenCount;

    public NineGridlayout(Context context) {
        super(context);
    }

    public NineGridlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        ScreenTools screenTools=ScreenTools.instance(getContext());
        totalWidth=screenTools.getScreenWidth()-screenTools.dip2px(16);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
    
    private void layoutChildrenView(){
        childrenCount = listData.size();

        //根据子view数量确定高度
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = singleHeight * rows + gap * (rows - 1);
        setLayoutParams(params);
        layout();
//        switch (childrenCount) {
//		case 2:
//			layout();
//			break;
//		case 3:
//			for(int i = 0; i < childrenCount; i++){
//				CustomImageView childrenView = (CustomImageView) getChildAt(i);
//				childrenView.setTag(String.valueOf(mPosition)+String.valueOf(i));
//				childrenView.setImageBitmap(null);
//				childrenView.setImageUrl(((Image) listData.get(i)).getUrl(), listData, i);
//				if(i == 0)
//					childrenView.layout(0, 0, totalWidth,(int) (((double)listData.get(i).getHeight())/(double)(listData.get(i).getWidth())*totalWidth));
//				if(i == 1)
//					childrenView.layout(0, 0, (totalWidth-gap)/2,(totalWidth-gap)/2);
//				if(i == 2)
//					childrenView.layout((totalWidth-gap)/2+gap, 0, totalWidth,(totalWidth-gap)/2);
//			}
//            break;
//		default:
//			layout();
			//break;
		//}
        
    }
    
    private void layout() {
    	for (int i = 0; i < childrenCount; i++) {
            CustomImageView childrenView = (CustomImageView) getChildAt(i);
            
            childrenView.setTag(String.valueOf(mPosition)+String.valueOf(i));
            childrenView.setImageBitmap(null);
            
            int[] position = findPosition(i);
            int left = (singleWidth + gap) * position[1];
            int top = (singleHeight + gap) * position[0];
            int right = left + singleWidth;
            int bottom = top + singleHeight;
            childrenView.layout(left, top, right, bottom);
            
            childrenView.setImageUrl(((Image) listData.get(i)).getUrl(), listData, i);
        }
	}
    


    private int[] findPosition(int childNum) {
        int[] position = new int[2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if ((i * columns + j) == childNum) {
                    position[0] = i;//行
                    position[1] = j;//列
                    break;
                }
            }
        }
        return position;
    }

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }


    public void setImagesData(List<Image> lists , int position) {
    	mPosition = position;
    	
        if (lists == null || lists.isEmpty()) {
            return;
        }
        //初始化布局
        generateChildrenLayout(lists.size());
        //这里做一个重用view的处理
        if (listData == null) {
            int i = 0;
            while (i < lists.size()) {
                CustomImageView iv = generateImageView();
                addView(iv);
                //addView(iv,generateDefaultLayoutParams());
                i++;
            }
        } else {
            int oldViewCount = listData.size();
            int newViewCount = lists.size();
            if (oldViewCount > newViewCount) {
                removeViews(newViewCount - 1, oldViewCount - newViewCount);
            } else if (oldViewCount < newViewCount) {
                for (int i = 0; i < newViewCount - oldViewCount; i++) {
                    CustomImageView iv = generateImageView();
                    //addView(iv,generateDefaultLayoutParams());
                    addView(iv);
                }
            }
        }
        ViewGroup.LayoutParams layoutParams = new LayoutParams(singleHeight, singleHeight);
        for(int i = 0; i < getChildCount(); i++){
        	((CustomImageView)getChildAt(i)).setLayoutParams(layoutParams);
        }
        listData = lists;
        
        
        /*for(int index = 0; index < listData.size(); index++){
        	GetImageWidthAndHeigth getImageWidthAndHeigth = new GetImageWidthAndHeigth(){
        		@Override
        		protected void onPostExecute(int[] result) {
        			// TODO Auto-generated method stub
        			listData.get(result[3]).setWidth(result[1]);
        			listData.get(result[3]).setHeight(result[2]);
        			if(result[3] == listData.size())
        				layoutChildrenView();
        		}
        	};
        	getImageWidthAndHeigth.execute(listData.get(index).getUrl(), String.valueOf(index));
        }*/
        layoutChildrenView();
    }


    /**
     * 根据图片个数确定行列数量
     * 对应关系如下
     * num	row	column
     * 1	   1	1
     * 2	   1	2
     * 3	   1	3
     * 4	   2	2
     * 5	   2	3
     * 6	   2	3
     * 7	   3	3
     * 8	   3	3
     * 9	   3	3
     *
     * @param length
     */
    private void generateChildrenLayout(int length) {
    	if (length == 2){
    		rows = 1;
    		columns = 2;
    		singleHeight = singleWidth = (int) ((totalWidth - gap * (2 - 1)) / 2.0+0.5);
    	}
    	else if (length == 3) {
            rows = 1;
            columns = 3;
            singleHeight = singleWidth = (totalWidth - gap * (3 - 1)) / 3;
        }
    	else if (length == 4) {
			rows = 2;
			columns = 2;
			singleHeight = singleWidth = (int) ((totalWidth - gap * (2 - 1)) / 2.0+0.5);
		}
    	else if (length <= 6) {
            rows = 2;
            columns = 3;
            singleHeight = singleWidth = (totalWidth - gap * (3 - 1)) / 3;
        } else {
            rows = 3;
            columns = 3;
            singleHeight = singleWidth = (totalWidth - gap * (3 - 1)) / 3;
        }
    }

    private CustomImageView generateImageView() {
        CustomImageView iv = new CustomImageView(getContext());
        //iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return iv;
    }


}
