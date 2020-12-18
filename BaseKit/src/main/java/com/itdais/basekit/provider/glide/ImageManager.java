package com.itdais.basekit.provider.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.itdais.basekit.provider.glide.tf.BlurTransformation;
import com.itdais.basekit.provider.glide.tf.ColorFilterTransformation;
import com.itdais.basekit.provider.glide.tf.GlideCircleBorderTransform;
import com.itdais.basekit.provider.glide.tf.GrayscaleTransformation;
import com.itdais.basekit.provider.glide.tf.RoundedCornersTransformation;
import com.itdais.basekit.util.FileUtil;
import com.itdais.basekit.util.ImageUtil;
import com.itdais.basekit.util.ToastUtils;
import com.itdais.basekit.util.Utils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Author:  ding.jw
 * Description:
 * 基于Glide的图片加载管理器
 */
public class ImageManager {
    private static final int NORMAL = 0;
    /**
     * 圆形
     */
    private static final int CIRCLE = 1;
    /**
     * 圆角
     */
    private static final int ROUND = 2;
    /**
     * 圆形带边框
     */
    private static final int CIRCLE_BORDER = 3;

    public static Builder load(Context context, String loadUrl) {
        return new Builder(context, loadUrl);
    }

    public static Builder load(Context context, Uri loadUri) {
        return new Builder(context, loadUri);
    }

    public static Builder load(Context context, Bitmap bitmap) {
        return new Builder(context, bitmap);
    }

    public static Builder load(Context context, @DrawableRes int drawRes) {
        return new Builder(context, drawRes);
    }

    public static class Builder {
        private Context context;
        /**
         * 加载
         */
        private String loadUrl;
        private Uri loadUri;
        private Bitmap loadBitmap;
        @DrawableRes
        private int loadRes;
        @DrawableRes
        private int placeholder;
        @DrawableRes
        private int errorResId;
        private int shape;
        private int circleBorderWidth;
        @ColorInt
        private int circleBorderColor;
        private int roundRadio;
        //是否高斯模糊
        private boolean isNeedBlur;
        private int blurRadio;
        //是否过滤颜色
        private boolean isNeedFilterColor;
        @ColorInt
        private int filterColor;
        //是否黑白转换
        private boolean isNeedGray;

        private RoundedCornersTransformation.CornerType cornerType;

        public Builder(Context context, String loadUrl) {
            this.context = context;
            this.loadUrl = loadUrl;
        }

        public Builder(Context context, @DrawableRes int loadRes) {
            this.context = context;
            this.loadRes = loadRes;
        }

        public Builder(Context context, Uri loadUri) {
            this.context = context;
            this.loadUri = loadUri;
        }

        public Builder(Context context, Bitmap bitmap) {
            this.context = context;
            this.loadBitmap = bitmap;
        }

        public Builder placeholder(int placeholder) {
            this.placeholder = placeholder;
            return this;
        }

        public Builder error(int errorResId) {
            this.errorResId = errorResId;
            return this;
        }

        /**
         * 圆形转换
         */
        public Builder circle() {
            this.shape = CIRCLE;
            return this;
        }

        /**
         * 带边框圆形转换
         *
         * @param borderWidth
         * @param borderColor
         * @return
         */
        public Builder circleBorder(int borderWidth, @ColorInt int borderColor) {
            this.shape = CIRCLE_BORDER;
            this.circleBorderWidth = borderWidth;
            this.circleBorderColor = borderColor;
            return this;
        }

        public Builder round(int radio) {
            round(radio, RoundedCornersTransformation.CornerType.ALL);
            return this;
        }

        public Builder round(int radio, RoundedCornersTransformation.CornerType cornerType) {
            this.shape = ROUND;
            this.roundRadio = radio;
            this.cornerType = cornerType;
            return this;
        }

        /**
         * 效果转换 -> 高斯模糊
         *
         * @return
         */
        public Builder transformBlur(int radio) {
            this.isNeedBlur = true;
            this.blurRadio = radio;
            return this;
        }

        /**
         * 效果转换 -> 颜色过滤
         *
         * @return
         */
        public Builder transformColorFilter(@ColorInt int filterColor) {
            this.isNeedFilterColor = true;
            this.filterColor = filterColor;
            return this;
        }

        /**
         * 效果转换 -> 黑白色
         *
         * @return
         */
        public Builder transformGrey() {
            this.isNeedGray = true;
            return this;
        }

        /**
         * 统计需要转换的
         *
         * @return
         */
        private int statisticsCount() {
            int count = 0;
            if (shape == CIRCLE || shape == ROUND || shape == CIRCLE_BORDER) {
                count++;
            }
            if (isNeedBlur) {
                count++;
            }

            if (isNeedFilterColor) {
                count++;
            }

            if (isNeedGray) {
                count++;
            }
            return count;
        }

        public void into(View view) {
            RequestOptions options = new RequestOptions();
            if (placeholder != 0) {
                options.placeholder(placeholder);
            }
            if (errorResId != 0) {
                options.error(errorResId);
            }
            Transformation[] transformation = new Transformation[statisticsCount()];
            int count = 0;
            if (transformation.length > 0) {
                if (isNeedBlur) {
                    transformation[count] = new BlurTransformation(blurRadio);
                    count++;
                }
                if (isNeedFilterColor) {
                    transformation[count] = new ColorFilterTransformation(filterColor);
                    count++;
                }
                if (isNeedGray) {
                    transformation[count] = new GrayscaleTransformation();
                    count++;
                }
                switch (shape) {
                    case CIRCLE:
                        transformation[count] = new CircleCrop();
                        break;
                    case ROUND:
                        transformation[count] = new RoundedCornersTransformation(roundRadio, 0, cornerType);
                        break;
                    case CIRCLE_BORDER:
                        transformation[count] = new GlideCircleBorderTransform(circleBorderWidth, circleBorderColor);
                        break;
                    case NORMAL:
                    default:
                        break;
                }
                options.transform(transformation);
            }
            RequestBuilder<Drawable> requestBuilder = null;
            if (!TextUtils.isEmpty(loadUrl)) {
                requestBuilder = Glide.with(context).load(loadUrl);
            } else if (null != loadUri) {
                requestBuilder = Glide.with(context).load(loadUri);
            } else if (null != loadBitmap) {
                requestBuilder = Glide.with(context).load(loadBitmap);
            } else if (loadRes != 0) {
                requestBuilder = Glide.with(context).load(loadRes);
            }
            if (requestBuilder != null && view instanceof ImageView) {
                requestBuilder.apply(options).into((ImageView) view);
            }
        }
    }

    /**
     * @param imgUrl
     */
    public static GlideDownloadTask saveImage(String imgUrl) {
        GlideDownloadTask task = new GlideDownloadTask();
        task.execute(imgUrl);
        return task;
    }

    /**
     * 通过glide下载文件
     * 使用submit()得到FutureTarget
     * FutureTarget是一个可获取结果的线程
     * 需要运行在异步中
     *
     * @param url
     * @return
     */
    public static Bitmap getFileBitmap(String url) {
        try {
            return Glide.with(Utils.getContext()).asBitmap().load(url).submit().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 下载文件task
     */
    private static class GlideDownloadTask extends AsyncTask<String, Object, File> {
        @Override
        protected File doInBackground(String... strings) {
            if (strings != null) {
                Bitmap bitmap = getFileBitmap(strings[0]);
                if (bitmap != null) {
                    String filePath = Utils.getContext().getFilesDir() + "/" + FileUtil.generateFileName() + ".jpg";
                    File file = new File(filePath);
                    boolean isSuccess = ImageUtil.bitmap2File(bitmap, file, true);
                    if (isSuccess) {
                        return file;
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(File file) {
            if (null == file) {
                ToastUtils.showShortToast("图片下载失败，请稍后重试！");
                return;
            }
            try {
                //把文件插入到系统图库
                MediaStore.Images.Media.insertImage(Utils.getContext().getContentResolver(),
                        file.getAbsolutePath(),
                        file.getName(),
                        null);
                // 通知图库更新
                //android10之后，则需要把文件复制到DCIM目录下
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
//                }
                //android9之前，保存图片使用MediaScannerConnection
                MediaScannerConnection.scanFile(Utils.getContext(), new String[]{file.getAbsolutePath()}, new String[]{"image/jpeg"},
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                            }
                        });
                //不适用MediaScannerConnection.scanFile就可以使用ACTION_MEDIA_SCANNER_SCAN_FILE方法
//                Utils.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
//                        Uri.fromFile(file)));
                ToastUtils.showShortToast("图片保存成功！");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 如需要页面onPause时暂停加载
     */
    public static void onPause() {
        Glide.with(Utils.getContext()).pauseRequests();
    }

    /**
     * 如需要页面onResume时恢复加载
     */
    public static void onResume() {
        Glide.with(Utils.getContext()).resumeRequests();
    }

    /**
     * 清除硬盘和内存缓存
     *
     * @param view
     */
    public static void clearMomoryCache(View view) {
        Glide.with(Utils.getContext()).clear(view);
    }

    /**
     * 清除缓存
     */
    public static void clearMomory() {
        Glide.get(Utils.getContext()).clearMemory();
    }

    /**
     * 清除硬盘缓存
     */
    public static void clearDiskCache() {
        Glide.get(Utils.getContext()).clearDiskCache();
    }

    /**
     * 4.0以后 系统会根据不同的内存状态来回调
     */
    public static void onTrimMemory(int level) {
        Glide.get(Utils.getContext()).trimMemory(level);
    }

    /**
     * 内存不足时使用
     */
    public static void onLowMemory() {
        Glide.get(Utils.getContext()).onLowMemory();
    }
}
