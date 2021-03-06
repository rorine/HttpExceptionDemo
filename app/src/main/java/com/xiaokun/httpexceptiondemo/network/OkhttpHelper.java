package com.xiaokun.httpexceptiondemo.network;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.xiaokun.httpexceptiondemo.BuildConfig;
import com.xiaokun.httpexceptiondemo.Constants;
import com.xiaokun.httpexceptiondemo.network.interceptors.AppCacheInterceptor;
import com.xiaokun.httpexceptiondemo.network.interceptors.CookieInterceptor;
import com.xiaokun.httpexceptiondemo.network.interceptors.DownloadInterceptor;
import com.xiaokun.httpexceptiondemo.network.interceptors.HeaderInterceptor;
import com.xiaokun.httpexceptiondemo.network.interceptors.TokenInterceptor;
import com.xiaokun.httpexceptiondemo.rx.download.DownloadEntity;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/20
 *     描述   : okhttp配置
 *     版本   : 1.0
 * </pre>
 */
public class OkhttpHelper
{
    private static int CONNECT_TIME = 10;
    private static int READ_TIME = 20;
    private static int WRITE_TIME = 20;
    private static OkHttpClient.Builder builder;
    private static OkHttpClient client;

    private static OkHttpClient.Builder getDefaultBuilder(boolean isCache)
    {
        builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG)
        {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(BODY);
            //打印拦截器
            builder.addInterceptor(loggingInterceptor);
            //调试拦截器
            builder.addInterceptor(new StethoInterceptor());
        }
        File cacheFile = new File(Constants.PATH_CACHE);
        //最大50M，缓存太大领导有意见！为何你App占这么多内存？
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        if (isCache)
        {
            builder.addInterceptor(new AppCacheInterceptor())
                    .cache(cache);
        }
        //下面3个超时,不设置默认就是10s
        builder.connectTimeout(CONNECT_TIME, TimeUnit.SECONDS)
                .readTimeout(READ_TIME, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME, TimeUnit.SECONDS)
                //失败重试
                .retryOnConnectionFailure(true);
        return builder;
    }

    public static OkHttpClient getOkhttpClient(boolean isCache, Interceptor... interceptors)
    {
        if (builder == null)
        {
            builder = getDefaultBuilder(isCache);
        }
        for (Interceptor interceptor : interceptors)
        {
            builder.addInterceptor(interceptor);
        }
        return builder.build();
    }

    public static OkHttpClient getDefaultClient(boolean isCache)
    {
        OkHttpClient.Builder builder = getDefaultBuilder(isCache);
        builder.addInterceptor(new CookieInterceptor());
        if (client == null)
        {
            client = builder.build();
        }
        return client;
    }

    /**
     * 模拟token刷新
     *
     * @return
     */
    public static OkHttpClient initOkHttp2()
    {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG)
        {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(BODY);
            //打印拦截器
            builder.addInterceptor(loggingInterceptor);
            //调试拦截器
            builder.addInterceptor(new StethoInterceptor());
        }

        File cacheFile = new File(Constants.PATH_CACHE);
        //最大50M，缓存太大领导有意见！为何你App占这么多内存？
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        //这里用到okhttp的拦截器知识
        builder.addInterceptor(new HeaderInterceptor())
                .addInterceptor(new AppCacheInterceptor())
//                .addNetworkInterceptor(netCacheInterceptor)
                //token刷新拦截器
                .addInterceptor(new TokenInterceptor())
                .cache(cache)
                //下面3个超时,不设置默认就是10s
                .connectTimeout(CONNECT_TIME, TimeUnit.SECONDS)
                .readTimeout(READ_TIME, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME, TimeUnit.SECONDS)
                //失败重试
                .retryOnConnectionFailure(true)
                .build();
        return builder.build();
    }

    public static OkHttpClient initDownloadClient(DownloadEntity entity)
    {
        DownloadInterceptor downloadInterceptor = new DownloadInterceptor(entity);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG)
        {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(BODY);
            //打印拦截器
            builder.addInterceptor(loggingInterceptor);
            //调试拦截器
            builder.addInterceptor(new StethoInterceptor());
        }
        builder.addInterceptor(downloadInterceptor)
                //下面3个超时,不设置默认就是10s
                .connectTimeout(CONNECT_TIME, TimeUnit.SECONDS)
                .readTimeout(READ_TIME, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME, TimeUnit.SECONDS)
                //失败重试
                .retryOnConnectionFailure(true);
        return builder.build();
    }
}
