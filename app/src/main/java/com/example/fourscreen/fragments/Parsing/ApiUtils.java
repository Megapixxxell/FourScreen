package com.example.fourscreen.fragments.Parsing;

import com.example.fourscreen.BuildConfig;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtils {
    private static OkHttpClient sHttpClient;
    private static Retrofit sRetrofit;
    private static Gson sGson;
    private static ZennexApi sApi;

    public static OkHttpClient getBasicClient() {
        if (sHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
            if (!BuildConfig.BUILD_TYPE.contains("release")) {
                builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
            }
            sHttpClient = builder.build();
        }
        return sHttpClient;
    }

    private static Retrofit getRetrofit() {
        if (sGson == null) {
            sGson = new Gson();
        }
        if (sRetrofit == null) {
            sRetrofit = new Retrofit.Builder()
                    .baseUrl("http://quotes.zennex.ru/api/v3/bash/")
                    // need for interceptors
                    .client(getBasicClient())
                    .addConverterFactory(GsonConverterFactory.create(sGson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return sRetrofit;
    }

    public static ZennexApi getApiService() {
        if (sApi == null) {
            sApi = getRetrofit().create(ZennexApi.class);
        }
        return sApi;
    }
}