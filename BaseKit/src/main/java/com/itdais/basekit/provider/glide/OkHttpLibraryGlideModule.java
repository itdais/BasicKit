package com.itdais.basekit.provider.glide;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.LibraryGlideModule;
import com.itdais.basekit.provider.okhttp.OkhttpsProvider;

import java.io.InputStream;

/**
 * Author:  ding.jw
 * Description:
 * 在基础依赖包中，glide使用okhttp
 */
@GlideModule
public final class OkHttpLibraryGlideModule extends LibraryGlideModule {

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(OkhttpsProvider.getOkhttpsClient()));
    }
}
