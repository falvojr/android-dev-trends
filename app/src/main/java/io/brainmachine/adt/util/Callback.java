package io.brainmachine.adt.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Custom callback for app needs (GitHub and GitHub Status APIs).
 *
 * Created by falvojr on 1/8/17.
 */
public abstract class Callback<T> implements retrofit2.Callback<T> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            onSuccess(response.body());
        } else {
            String errorBody = null;
            try {
                errorBody = response.errorBody().string();
                final JSONObject json = new JSONObject(errorBody);
                onError(json.getString("message"));
            } catch (IOException e) {
                onError(e.getMessage());
            } catch (JSONException e) {
                onError(errorBody);
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onError(t.getMessage());
    }

    protected abstract void onSuccess(T entity);
    protected abstract void onError(String message);

    class ErrorBody {
        private String message;
    }
}
