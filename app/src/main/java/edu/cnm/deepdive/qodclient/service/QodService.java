package edu.cnm.deepdive.qodclient.service;

import edu.cnm.deepdive.qodclient.BuildConfig;
import edu.cnm.deepdive.qodclient.model.Quote;
import io.reactivex.Observable;
import io.reactivex.Single;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface QodService {

  @GET("quotes/random")
  Single<Quote> random(@Header("Authorization") String oauthHeader);

  @GET("quotes/search")
  Observable<List<Quote>> search(
      @Header("Authorization") String oauthHeader,@Query("q") String fragment);

  static QodService getInstance() {
    return InstanceHolder.INSTANCE;
  }

  class InstanceHolder {

    private static final QodService INSTANCE;

    static {
      // Following five lines should be removed/commented out for production release.
      HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
      interceptor.setLevel(Level.BODY);
      OkHttpClient client = new OkHttpClient.Builder()
          .addInterceptor(interceptor)
          .build();
      Retrofit retrofit = new Retrofit.Builder()
          .client(client) // This should be removed/commented out for production release.
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .addConverterFactory(GsonConverterFactory.create()) // TODO Check; maybe change?
          .baseUrl(BuildConfig.BASE_URL)
          .build();
      INSTANCE = retrofit.create(QodService.class);
    }

  }

}
