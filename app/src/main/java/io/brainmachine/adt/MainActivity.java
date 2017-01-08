package io.brainmachine.adt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import io.brainmachine.adt.domain.entity.AccessToken;
import io.brainmachine.adt.domain.entity.Status;
import io.brainmachine.adt.domain.entity.User;
import io.brainmachine.adt.domain.repository.GitHubOAuthRepository;
import io.brainmachine.adt.domain.repository.GitHubRepository;
import io.brainmachine.adt.domain.repository.GitHubStatusRepository;
import io.brainmachine.adt.retrofit.Callback;
import okhttp3.Credentials;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * GitHub authentication activity.
 *
 * @author falvojr
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ImageView mImgStatusImage;
    private TextView mLblStatusText;
    private EditText mTxtUsername;
    private EditText mTxtPassword;
    private Button mBtnBasicAuth;
    private Button mBtnOAuth;

    private GitHubStatusRepository mGitHubStatusApi;
    private GitHubOAuthRepository mGitHubOAuthApi;
    private GitHubRepository mGitHubApi;

    private SharedPreferences mSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImgStatusImage = (ImageView) findViewById(R.id.ivGitHubStatus);
        mLblStatusText = (TextView) findViewById(R.id.tvGitHubStatus);
        mTxtUsername = (EditText) findViewById(R.id.etUsername);
        mTxtPassword = (EditText) findViewById(R.id.etPassword);
        mBtnBasicAuth = (Button) findViewById(R.id.btBasicAuth);
        mBtnOAuth = (Button) findViewById(R.id.btOAuth);

        mBtnBasicAuth.setOnClickListener(this);
        mBtnOAuth.setOnClickListener(this);

        final Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create());

        final Retrofit retrofitGitHubStatus = retrofitBuilder
                .baseUrl(GitHubStatusRepository.BASE_URL)
                .build();
        final Retrofit retrofitGitHubOAuth = retrofitBuilder
                .baseUrl(GitHubOAuthRepository.BASE_URL)
                .build();
        final Retrofit retrofitGitHub = retrofitBuilder
                .baseUrl(GitHubRepository.BASE_URL)
                .build();

        mGitHubStatusApi = retrofitGitHubStatus.create(GitHubStatusRepository.class);
        mGitHubOAuthApi = retrofitGitHubOAuth.create(GitHubOAuthRepository.class);
        mGitHubApi = retrofitGitHub.create(GitHubRepository.class);

        mSharedPrefs = this.getSharedPreferences(getString(R.string.sp_file_key), Context.MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGitHubStatusApi.lastMessage().enqueue(new Callback<Status>() {

            @Override
            protected void onSuccess(Status status) {
                updateGitHubStatusFields(status.body, status.type.getColor());
            }

            @Override
            protected void onError(String message) {
                updateGitHubStatusFields(message, Status.Type.MAJOR.getColor());
            }

            private void updateGitHubStatusFields(String message, int colorRes) {
                int color = ContextCompat.getColor(MainActivity.this, colorRes);
                DrawableCompat.setTint(mImgStatusImage.getDrawable(), color);
                mLblStatusText.setTextColor(color);
                mLblStatusText.setText(message);
            }
        });
        this.processOAuthRedirectUri();
    }

    private void processOAuthRedirectUri() {
        // the intent filter defined in AndroidManifest will handle the return from ACTION_VIEW intent
        final Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(this.getOAuthRedirectUri())) {
            // use the parameter your API exposes for the code (mostly it's "code")
            String code = uri.getQueryParameter("code");
            if (code != null) {
                // get access token
                final String clientId = getString(R.string.oauth_client_id);
                final String clientSecret = getString(R.string.oauth_client_secret);
                mGitHubOAuthApi.acessToken(clientId, clientSecret, code).enqueue(new Callback<AccessToken>() {
                    @Override
                    protected void onSuccess(AccessToken entity) {
                        saveSharedPrefAuthCredential(entity.getAuthCredential());
                        //TODO Redirect to next activity
                    }

                    @Override
                    protected void onError(String message) {
                        Snackbar.make(mBtnOAuth, message, Snackbar.LENGTH_LONG).show();
                    }
                });
            } else if (uri.getQueryParameter("error") != null) {
                Snackbar.make(mBtnOAuth, uri.getQueryParameter("error"), Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.btBasicAuth:
                //TODO Validate fields to login
                final String authCredential = Credentials.basic(mTxtUsername.getText().toString(), mTxtPassword.getText().toString());
                mGitHubApi.basicAuth(authCredential).enqueue(new Callback<User>() {
                    @Override
                    protected void onSuccess(User user) {
                        saveSharedPrefAuthCredential(authCredential);
                        //TODO Redirect to next activity
                    }

                    @Override
                    protected void onError(String message) {
                        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
                    }
                });
                break;
            case R.id.btOAuth:
                final String baseUrl = GitHubOAuthRepository.BASE_URL + "authorize";
                final String clientId = getString(R.string.oauth_client_id);
                final String redirectUri = getOAuthRedirectUri();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(baseUrl + "?client_id=" + clientId + "&redirect_uri=" + redirectUri));
                startActivity(intent);
                break;
        }
    }

    private void saveSharedPrefAuthCredential(String authCredential) {
        final String authCredentialKey = getString(R.string.sp_auth_credential_key);
        mSharedPrefs.edit()
                .putString(authCredentialKey, authCredential)
                .apply();
    }

    @NonNull
    private String getOAuthRedirectUri() {
        return getString(R.string.oauth_schema) + "://" + getString(R.string.oauth_host);
    }
}
