package io.brainmachine.adt.infraestructure.storage.service;

import io.brainmachine.adt.domain.entity.User;
import retrofit2.http.GET;
import retrofit2.http.Header;
import rx.Observable;

/**
 * GitHub API using retrofit.
 * <p>
 * Created by falvojr on 1/5/17.
 */
public interface GitHubService {

    String BASE_URL = "https://api.github.com/";

    @GET("user")
    Observable<User> getUser(@Header("Authorization") String authorization);
}
