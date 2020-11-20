package com.itdais.basekit.widget.extend;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 被包裹时候使用
 * Created by 32918 on 2015/12/14.
 */
public class GridViewExtend extends GridView {
    public GridViewExtend(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewExtend(Context context) {
        super(context);
    }

    public GridViewExtend(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
