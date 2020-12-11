package com.itdais.basekit.crash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by 32918 on 2016/8/1.
 * 全局处理异常.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    /**
     * CrashHandler实例
     */
    private static CrashHandler instance;
    private Context mContext;

    public static CrashHandler getInstance(Context context) {
        if (instance == null) {
            synchronized (CrashHandler.class) {
                if (instance == null) {
                    instance = new CrashHandler(context);
                }
            }
        }
        return instance;
    }

    private CrashHandler(Context context) {
        this.mContext = context.getApplicationContext();
    }

    /**
     * 在运行环境为Build.debug时，可进行初始化
     *
     * @param context
     * @param isDebug
     */
    public static void init(Context context, boolean isDebug) {
        if (isDebug) {
            Thread.setDefaultUncaughtExceptionHandler(getInstance(context));
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();
        Intent intent = new Intent(mContext, CrashLogActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putSerializable("e", ex);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }
}
