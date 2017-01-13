package io.brainmachine.adt.infraestructure.storage.service;

import io.brainmachine.adt.domain.entity.Status;
import retrofit2.http.GET;
import rx.Observable;

/**
 * GitHub Status API using retrofit.
 * <p>
 * Created by falvojr on 1/5/17.
 */
public interface GitHubStatusService {

    String BASE_URL = "https://status.github.com/api/";

    @GET("last-message.json")
    Observable<Status> lastMessage();
}
