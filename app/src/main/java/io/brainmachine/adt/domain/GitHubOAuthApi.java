package io.brainmachine.adt.domain;

import io.brainmachine.adt.domain.entity.AccessToken;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * GitHub OAUth API using retrofit.
 * <p>
 * Created by falvojr on 1/5/17.
 */
public interface GitHubOAuthApi {

    String BASE_URL = "https://github.com/login/oauth/";

    Retrofit RETROFIT = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(GitHubOAuthApi.BASE_URL)
            .build();

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("access_token")
    Call<AccessToken> accessToken(
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("code") String code);
}
