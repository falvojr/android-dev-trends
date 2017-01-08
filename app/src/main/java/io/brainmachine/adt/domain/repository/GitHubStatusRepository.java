package io.brainmachine.adt.domain.repository;

import java.util.List;

import io.brainmachine.adt.domain.entity.Status;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * GitHub Status API using retrofit.
 *
 * Created by falvojr on 1/5/17.
 */
public interface GitHubStatusRepository {

    String BASE_URL = "https://status.github.com/api/";

    @GET("status.json")
    Call<Status> status();

    @GET("last-message.json")
    Call<Status> lastMessage();

    @GET("messages.json")
    Call<List<Status>> messages();
}
