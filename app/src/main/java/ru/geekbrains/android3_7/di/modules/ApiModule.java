package ru.geekbrains.android3_7.di.modules;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.geekbrains.android3_7.model.api.ApiService;


@Module
public class ApiModule
{
    @Provides
    @Named("GsonLCWU")
    public Gson gsonLcwu()
    {
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }

    @Provides
    @Named("Gson")
    public Gson gson()
    {
        return new GsonBuilder().create();
    }


    @Provides
    public GsonConverterFactory converterFactory(@Named("GsonLCWU") Gson gson)
    {
        return GsonConverterFactory.create(gson);
    }

    @Provides
    public CallAdapter.Factory callAdapterFactory()
    {
        return RxJava2CallAdapterFactory.create();
    }


    @Provides
    @Named("endpoint")
    public String endpoint(){
        return "https://api.github.com/";
    }

    @Provides
    public OkHttpClient client()
    {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    public Retrofit retrofit(@Named("endpoint") String endpoint, OkHttpClient client ,CallAdapter.Factory callAdapterFactory, GsonConverterFactory converterFactory)
    {
        return new Retrofit.Builder()
                .baseUrl(endpoint)
                .addCallAdapterFactory(callAdapterFactory)
                .client(client)
                .addConverterFactory(converterFactory)
                .build();
    }

    @Singleton
    @Provides
    public ApiService api(Retrofit retrofit)
    {
        return retrofit.create(ApiService.class);
    }

}
