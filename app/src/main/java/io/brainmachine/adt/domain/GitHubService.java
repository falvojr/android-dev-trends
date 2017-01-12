package io.brainmachine.adt.domain;

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
    Observable<User> basicAuth(@Header("Authorization") String authorization);
}
