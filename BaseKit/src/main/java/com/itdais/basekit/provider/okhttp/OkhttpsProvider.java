package com.itdais.basekit.provider.okhttp;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;
import rxhttp.wrapper.ssl.HttpsUtils;

/**
 * Author:  ding.jw
 * Description:
 */
public final class OkhttpsProvider {
    /**
     * 获取不需要认证的okhttpclient
     */
    public static OkHttpClient getOkhttpsClient() {
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        return new OkHttpClient.Builder()
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        //忽略host认证
                        return true;
                    }
                })
                .build();
    }

}
