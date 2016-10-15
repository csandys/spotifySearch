package com.spotifysearch.retrofit;

import com.github.aurae.retrofit2.LoganSquareConverterFactory;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.schedulers.Schedulers;

/**
 * Created by csandys on 10/13/16.
 */

public class RetrofitManager {

    public static final String API_BASE_URL = "https://api.spotify.com";

    private Retrofit mRetrofit;

    private RetrofitManager() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(LoganSquareConverterFactory.create())
                .build();
    }

    private static class SingletonHolder {
        public static final RetrofitManager INSTANCE = new RetrofitManager();
    }

    public static RetrofitManager getInstance() {
        RetrofitManager retrofitManager = SingletonHolder.INSTANCE;
        return retrofitManager;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }
}
