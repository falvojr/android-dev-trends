package io.brainmachine.adt.domain;

import com.google.gson.GsonBuilder;

import java.util.List;

import io.brainmachine.adt.domain.entity.Status;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * GitHub Status API using retrofit.
 * <p>
 * Created by falvojr on 1/5/17.
 */
public interface GitHubStatusApi {

    String BASE_URL = "https://status.github.com/api/";

    /**
     * @see <a href="http://stackoverflow.com/a/6875295/3072570">Gson Date Format</a>
     */
    Retrofit RETROFIT = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(
                    new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create()))
            .baseUrl(GitHubStatusApi.BASE_URL)
            .build();

    @GET("status.json")
    Call<Status> status();

    @GET("last-message.json")
    Call<Status> lastMessage();

    @GET("messages.json")
    Call<List<Status>> messages();
}
