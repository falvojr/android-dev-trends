package io.brainmachine.adt;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import io.brainmachine.adt.domain.entity.Status;
import io.brainmachine.adt.domain.entity.User;
import io.brainmachine.adt.domain.repository.GitHubRepository;
import io.brainmachine.adt.domain.repository.GitHubStatusRepository;
import io.brainmachine.adt.retrofit.Callback;
import okhttp3.Credentials;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * My first activity.
 *
 * @author falvojr
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ImageView mImgStatusImage;
    private TextView mLblStatusText;
    private EditText mTxtUsername;
    private EditText mTxtPassword;
    private Button mBtnLogin;

    private GitHubStatusRepository mGitHubStatusApi;
    private GitHubRepository mGitHubApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImgStatusImage = (ImageView) findViewById(R.id.ivGitHubStatus);
        mLblStatusText = (TextView) findViewById(R.id.tvGitHubStatus);
        mTxtUsername = (EditText) findViewById(R.id.etUsername);
        mTxtPassword = (EditText) findViewById(R.id.etPassword);
        mBtnLogin = (Button) findViewById(R.id.button);
        mBtnLogin.setOnClickListener(this);

        final Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create());

        final Retrofit retrofitGitHubStatus = retrofitBuilder
                .baseUrl(GitHubStatusRepository.GITHUB_STATUS_API)
                .build();

        final Retrofit retrofitGitHub = retrofitBuilder
                .baseUrl(GitHubRepository.GITHUB_API)
                .build();

        mGitHubStatusApi = retrofitGitHubStatus.create(GitHubStatusRepository.class);
        mGitHubApi = retrofitGitHub.create(GitHubRepository.class);
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
    }

    @Override
    public void onClick(final View view) {
        final String authorization = Credentials.basic(mTxtUsername.getText().toString(), mTxtPassword.getText().toString());
        mGitHubApi.basicAuth(authorization).enqueue(new Callback<User>() {
            @Override
            protected void onSuccess(User user) {
                Snackbar.make(view, user.login, Snackbar.LENGTH_LONG).show();
            }

            @Override
            protected void onError(String message) {
                Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
