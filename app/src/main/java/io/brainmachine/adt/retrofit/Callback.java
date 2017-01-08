package io.brainmachine.adt.retrofit;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Custom callback for app needs.
 *
 * Created by falvojr on 1/8/17.
 */
public abstract class Callback<T> implements retrofit2.Callback<T> {

    private static final String TAG = Callback.class.getSimpleName();

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
                Log.e(TAG, "Error on response.errorBody().string()", e);
                onError(e.getMessage());
            } catch (JSONException e) {
                onError(errorBody);
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.e(TAG, "The onFailure method is called", t);
        onError(t.getMessage());
    }

    protected abstract void onSuccess(T entity);
    protected abstract void onError(String message);

    class ErrorBody {
        private String message;
    }
}
