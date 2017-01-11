package io.brainmachine.adt.domain;

import io.brainmachine.adt.domain.entity.User;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * GitHub API using retrofit.
 * <p>
 * Created by falvojr on 1/5/17.
 */
public interface GitHubApi {

    String BASE_URL = "https://api.github.com/";

    Retrofit RETROFIT = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(GitHubApi.BASE_URL)
            .build();

    @GET("user")
    Call<User> basicAuth(@Header("Authorization") String authorization);
}
