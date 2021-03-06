package io.brainmachine.adt.infraestructure.storage.service;

import io.brainmachine.adt.domain.entity.AccessToken;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * GitHub OAUth API using retrofit.
 * <p>
 * Created by falvojr on 1/5/17.
 */
public interface GitHubOAuthService {

    String BASE_URL = "https://github.com/login/oauth/";

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("access_token")
    Observable<AccessToken> accessToken(
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("code") String code);
}
