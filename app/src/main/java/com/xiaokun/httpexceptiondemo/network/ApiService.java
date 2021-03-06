package com.xiaokun.httpexceptiondemo.network;

import com.xiaokun.httpexceptiondemo.network.wanAndroid.WanBaseResponseEntity;
import com.xiaokun.httpexceptiondemo.network.wanAndroid.WanLoginEntityRes;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/19
 *     描述   :
 *     版本   : 1.0
 * </pre>
 */
public interface ApiService
{
    String baseUrl = "http://www.wanandroid.com/";

    String baseUrl2 = "http://180.101.250.185:19030/";

    //测试服务端返回成功
    @GET("tools/mockapi/440/yx0419")
    Observable<BaseResponse<ResEntity1.DataBean>> getHttpData1();

    //测试服务端返回错误码
    @GET("tools/mockapi/440/yx04191")
    Observable<BaseResponse<ResEntity1.DataBean>> getHttpData2();

    //测试未登录
    @GET("tools/mockapi/440/yx04192")
    Observable<BaseResponse<ResEntity1.DataBean>> getHttpData3();

    //获取token过期的http
    @GET("tools/mockapi/440/token_expired")
    Observable<BaseResponse<ResEntity1.DataBean>> getExpiredHttp();

    //获取新token
    @GET("tools/mockapi/440/yx04193")
    Call<BaseResponse<ResEntity1.DataBean>> getNewToken();

    //下载文件
    //如果文件非常大，必须要使用Streaming注解。否则retrofit会默认将整个文件读取到内存中
    //造成OOM
    @Streaming
    @GET
    Observable<ResponseBody> downLoadFile(@Url String fileUrl);

    @Streaming
    @GET("tools/test.apk")
    Observable<ResponseBody> downLoadFile();

    @GET("tools/mockapi/440/register")
    Observable<BaseResponse<RegisterEntity.DataBean>> register();

    @GET("tools/mockapi/440/login")
    Observable<BaseResponse<LoginEntity.DataBean>> login();

    @POST("/user/login")
    @FormUrlEncoded
    Observable<WanBaseResponseEntity<WanLoginEntityRes.DataBean>> login(@Field("username") String username,
                                                                        @Field("password") String password);
}

