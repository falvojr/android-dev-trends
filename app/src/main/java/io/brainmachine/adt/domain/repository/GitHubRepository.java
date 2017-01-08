package io.brainmachine.adt.domain.repository;

import io.brainmachine.adt.domain.entity.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * GitHub API using retrofit.
 *
 * Created by falvojr on 1/5/17.
 */
public interface GitHubRepository {

    String GITHUB_API = "https://api.github.com/";

    @GET("user")
    Call<User> basicAuth(@Header("Authorization") String authorization);
}
