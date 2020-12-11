package com.itdais.basekit.widget.state;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.itdais.basekit.R;

/**
 * Author:  ding.jw
 * Description:
 * 状态也没
 */
public class StateView extends FrameLayout {

    // 内容 View
    public static final int VIEW_CONTENT = 0;
    // 加载 View
    public static final int VIEW_LOADING = -1;
    // 错误页面 View
    public static final int VIEW_ERROR = -2;
    // 网络异常 View
    public static final int VIEW_ERROR_NET = -3;
    // 数据为空 View
    public static final int VIEW_EMPTY = -4;
    // 当前显示的 ViewTag
    private int mCurrentState;
    private int loadingResId;
    private int emptyResId;
    private int errorResId;
    private int errorNetResId;
    private boolean viewRetry;

    public StateView(@NonNull Context context) {
        this(context, null);
    }

    public StateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StateView);
        mCurrentState = typedArray.getInt(R.styleable.StateView_sv_viewState, VIEW_CONTENT);
        loadingResId = typedArray.getResourceId(R.styleable.StateView_sv_loadingView, NO_ID);
        emptyResId = typedArray.getResourceId(R.styleable.StateView_sv_emptyView, NO_ID);
        errorResId = typedArray.getResourceId(R.styleable.StateView_sv_errorView, NO_ID);
        errorNetResId = typedArray.getResourceId(R.styleable.StateView_sv_errorNetView, NO_ID);
        viewRetry = typedArray.getBoolean(R.styleable.StateView_sv_retry, false);
        showView(mCurrentState);
    }

    public void showView(@IntRange(from = VIEW_EMPTY, to = VIEW_CONTENT) int state) {

    }
}
