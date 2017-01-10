package io.brainmachine.adt.domain;

import io.brainmachine.adt.domain.entity.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * GitHub API using retrofit.
 *
 * Created by falvojr on 1/5/17.
 */
public interface GitHubApi {

    String BASE_URL = "https://api.github.com/";

    @GET("user")
    Call<User> basicAuth(@Header("Authorization") String authorization);
}
