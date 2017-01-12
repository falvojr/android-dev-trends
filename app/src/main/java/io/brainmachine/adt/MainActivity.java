package io.brainmachine.adt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.Lazy;
import io.brainmachine.adt.domain.GitHubOAuthService;
import io.brainmachine.adt.domain.GitHubService;
import io.brainmachine.adt.domain.GitHubStatusService;
import io.brainmachine.adt.domain.entity.AccessToken;
import io.brainmachine.adt.domain.entity.Status;
import io.brainmachine.adt.domain.entity.User;
import io.brainmachine.adt.util.AppUtil;
import okhttp3.Credentials;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * GitHub authentication activity.
 *
 * @author falvojr
 */
public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.ivGitHubStatus)
    ImageView mImgStatusImage;
    @BindView(R.id.tvGitHubStatus)
    TextView mLblStatusText;
    @BindView(R.id.tilUsername)
    TextInputLayout mWrapperTxtUsername;
    @BindView(R.id.tilPassword)
    TextInputLayout mWrapperTxtPassword;
    @BindView(R.id.btOAuth)
    Button mBtnOAuth;

    @Inject
    GitHubStatusService mGitHubStatusService;
    @Inject
    Lazy<GitHubOAuthService> mGitHubOAuthService;
    @Inject
    Lazy<GitHubService> mGitHubService;

    @Inject
    SharedPreferences mSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        super.getMyApplication().getDaggerDiComponent().inject(this);

        this.bindUsingRx();
    }

    private void bindUsingRx() {
        RxView.clicks(mBtnOAuth).subscribe(aVoid -> {
            final String baseUrl = GitHubOAuthService.BASE_URL + "authorize";
            final String clientId = getString(R.string.oauth_client_id);
            final String redirectUri = getOAuthRedirectUri();
            final Uri uri = Uri.parse(baseUrl + "?client_id=" + clientId + "&redirect_uri=" + redirectUri);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        if (mWrapperTxtUsername.getEditText() != null && mWrapperTxtPassword.getEditText() != null) {
            RxTextView.textChanges(mWrapperTxtUsername.getEditText())
                    .skip(1)
                    .subscribe(text -> {
                AppUtil.validateRequiredTextInputLayout(this, mWrapperTxtUsername);
            });
            RxTextView.textChanges(mWrapperTxtPassword.getEditText())
                    .skip(1)
                    .subscribe(text -> {
                AppUtil.validateRequiredTextInputLayout(this, mWrapperTxtPassword);
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Restore default GitHub fields status
        updateGitHubStatusFields(Status.Type.NONE);
        // Get last status from GitHub Status API
        mGitHubStatusService.lastMessage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Status>() {
                    @Override
                    public void onCompleted() { }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.getMessage(), e);
                        updateGitHubStatusFields(Status.Type.MAJOR);
                    }

                    @Override
                    public void onNext(Status status) {
                        updateGitHubStatusFields(status.type);
                    }
                });
        // Process (if necessary) OAUth redirect
        this.processOAuthRedirectUri();
    }

    private void updateGitHubStatusFields(Status.Type statusType) {
        int color = ContextCompat.getColor(MainActivity.this, statusType.getColorRes());
        DrawableCompat.setTint(mImgStatusImage.getDrawable(), color);
        mLblStatusText.setTextColor(color);
        mLblStatusText.setText(getString(statusType.getMessageRes()));
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
                mGitHubOAuthService.get().accessToken(clientId, clientSecret, code)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<AccessToken>() {
                                       @Override
                                       public void onCompleted() { }

                                       @Override
                                       public void onError(Throwable e) {
                                           Log.d(TAG, e.getMessage(), e);
                                           Snackbar.make(mBtnOAuth, e.getMessage(), Snackbar.LENGTH_LONG).show();
                                       }

                                       @Override
                                       public void onNext(AccessToken accessToken) {
                                           saveSharedPrefAuthCredential(accessToken.getAuthCredential());
                                           //TODO Redirect to next activity
                                           Snackbar.make(mBtnOAuth, "Sucesso OAuth!", Snackbar.LENGTH_LONG).show();
                                       }
                                   });
            } else if (uri.getQueryParameter("error") != null) {
                Snackbar.make(mBtnOAuth, uri.getQueryParameter("error"), Snackbar.LENGTH_LONG).show();
            }
            // Clear Intent Data preventing multiple calls
            getIntent().setData(null);
        }
    }

    @OnClick(R.id.btBasicAuth)
    public void onBasicAuthClick(final View view) {
        if (AppUtil.validateRequiredTextInputLayout(this, mWrapperTxtUsername, mWrapperTxtPassword)) {
            final String username = mWrapperTxtUsername.getEditText().getText().toString();
            final String password = mWrapperTxtPassword.getEditText().getText().toString();
            final String authCredential = Credentials.basic(username, password);
            mGitHubService.get().basicAuth(authCredential)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<User>() {
                        @Override
                        public void onCompleted() { }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, e.getMessage(), e);
                            Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG).show();
                        }

                        @Override
                        public void onNext(User user) {
                            saveSharedPrefAuthCredential(authCredential);
                            //TODO Redirect to next activity
                            Snackbar.make(view, "Sucesso Basic Auth!", Snackbar.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void saveSharedPrefAuthCredential(String authCredential) {
        final String authCredentialKey = getString(R.string.sp_auth_credential_key);
        mSharedPrefs.edit().putString(authCredentialKey, authCredential).apply();
    }

    @NonNull
    private String getOAuthRedirectUri() {
        return getString(R.string.oauth_schema) + "://" + getString(R.string.oauth_host);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.icCallHelpdesk:
                final String phone = "+55 16 997218281";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
