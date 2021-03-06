package cn.figo.mydemo.http;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import cn.figo.mydemo.app.AppService;
import cn.figo.mydemo.event.RecommentEvent;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * User: Ligx
 * Date: 2015-10-15
 * Time: 15:52
 * Copyright (c)Ligx All rights reserved.
 */
public class RetrofitClientManager {

    private static final String TAG = "RetrofitClientManager";
    private static RetrofitClientManager mInstance;
    public static Retrofit retrofit;
    public static String baseurl="http://app.bilibili.com/api/index/";
    private static  CachePolicy cachePolicy;
    public CachePolicy getCachePolicy() {
        return cachePolicy;
    }
    public void setCachePolicy(CachePolicy cachePolicy) {
        this.cachePolicy = cachePolicy;
    }


    public static Api api;
    static {
        api = RetrofitClientManager.getRetrofit().create(Api.class);
    }

    public RetrofitClientManager() {

    }

    public static RetrofitClientManager getInstance() {
        if (mInstance == null)
        {
            synchronized (RetrofitClientManager.class)
            {
                if (mInstance == null)
                {
                    mInstance = new RetrofitClientManager();
                }
            }
        }
        return mInstance;
    }

    public static OkHttpClient httpClient;
    public static Retrofit getRetrofit() {
        if (retrofit == null){
            OkHttpClient httpClient = new OkHttpClient();
            httpClient.setReadTimeout(15, TimeUnit.SECONDS);
            httpClient.setConnectTimeout(15, TimeUnit.SECONDS);
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseurl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(httpClient)
                    .build();
        }
        return retrofit;
    }

    public static <T> void getAsyn(Call<T> responseCall, MyRetrofitCallBack myRetrofitCallBack) {
        cachePolicy = myRetrofitCallBack.getCachePolicy();
        retrofit.client().interceptors().clear();
        retrofit.client().interceptors().add(new GetMeThodInterceptor(cachePolicy, myRetrofitCallBack,responseCall));
        responseCall.enqueue(myRetrofitCallBack);
    }

    public static <T> void getAsyn(Observable<T> responseCall, MyRetrofitCallBack myRetrofitCallBack) {
        cachePolicy = myRetrofitCallBack.getCachePolicy();
        retrofit.client().interceptors().clear();
//        retrofit.client().interceptors().add(new GetMeThodInterceptor(cachePolicy, myRetrofitCallBack,responseCall));
        responseCall.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<T>() {
                    @Override
                    public void call(T t) {

                    }
                })
                .subscribe(new Subscriber<T>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(T t) {
                        AppService.getBus().post(new RecommentEvent(t));
                    }
                });
    }
}
