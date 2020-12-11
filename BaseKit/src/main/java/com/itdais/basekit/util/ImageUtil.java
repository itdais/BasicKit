package com.itdais.basekit.util;

import android.graphics.Bitmap;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Author:  ding.jw
 * Description:
 * 图片工具类
 */
public class ImageUtil {
    /**
     * 判断bitmap对象是否为空
     *
     * @param src 源图片
     * @return {@code true}: 是<br>{@code false}: 否
     */
    private static boolean isEmptyBitmap(Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

    /**
     * bitmap转file
     *
     * @param src 源图片
     * @param file 保存的图片
     * @param recycleBitmap 是否回收bitmap
     * @return
     */
    public static boolean bitmap2File(Bitmap src, File file, boolean recycleBitmap) {
        if (isEmptyBitmap(src) || file == null) {
            return false;
        }
        OutputStream os = null;
        boolean ret = false;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            ret = src.compress(Bitmap.CompressFormat.JPEG, 100, os);
            if (recycleBitmap && !src.isRecycled()) src.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
}
