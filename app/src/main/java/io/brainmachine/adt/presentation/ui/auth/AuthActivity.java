package io.brainmachine.adt.presentation.ui.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
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
import io.brainmachine.adt.R;
import io.brainmachine.adt.domain.entity.Status;
import io.brainmachine.adt.domain.entity.User;
import io.brainmachine.adt.infraestructure.storage.service.GitHubOAuthService;
import io.brainmachine.adt.presentation.base.BaseActivity;
import io.brainmachine.adt.presentation.helper.AppHelper;
import okhttp3.Credentials;

/**
 * GitHub authentication activity.
 *
 * @author falvojr
 */
public class AuthActivity extends BaseActivity implements AuthContract.View {

    @BindView(R.id.ivGitHubStatus) ImageView mImgStatusImage;
    @BindView(R.id.tvGitHubStatus) TextView mLblStatusText;
    @BindView(R.id.tilUsername) TextInputLayout mWrapperTxtUsername;
    @BindView(R.id.tilPassword) TextInputLayout mWrapperTxtPassword;
    @BindView(R.id.btOAuth) Button mBtnOAuth;

    @Inject SharedPreferences mSharedPrefs;
    @Inject AppHelper mAppHelper;
    @Inject AuthContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        ButterKnife.bind(this);
        super.getDaggerUiComponent().inject(this);

        mPresenter.setView(this);

        this.bindUsingRx();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Restore default GitHub fields status
        onLoadStatusType(Status.Type.NONE);
        // Get last status from GitHub Status API
        mPresenter.loadStatus();
        // Process (if necessary) OAUth redirect
        this.processOAuthRedirectUri();
    }

    @Override
    public void onLoadStatusType(Status.Type statusType) {
        int color = ContextCompat.getColor(AuthActivity.this, statusType.getColorRes());
        DrawableCompat.setTint(mImgStatusImage.getDrawable(), color);
        mLblStatusText.setTextColor(color);
        mLblStatusText.setText(getString(statusType.getMessageRes()));
    }

    @Override
    public void onAuthSuccess(String credential, User entity) {
        final String authCredentialKey = getString(R.string.sp_auth_credential_key);
        mSharedPrefs.edit().putString(authCredentialKey, credential).apply();
        //TODO Redirect to next activity
        Snackbar.make(mImgStatusImage, credential, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showError(String message) {
        Snackbar.make(mImgStatusImage, message, Snackbar.LENGTH_LONG).show();
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
                        mAppHelper.validateRequiredTextInputLayout(mWrapperTxtUsername);
                    });
            RxTextView.textChanges(mWrapperTxtPassword.getEditText())
                    .skip(1)
                    .subscribe(text -> {
                        mAppHelper.validateRequiredTextInputLayout(mWrapperTxtPassword);
                    });
        }
    }

    @OnClick(R.id.btBasicAuth)
    public void onBasicAuthClick(final View view) {
        if (mAppHelper.validateRequiredTextInputLayout(mWrapperTxtUsername, mWrapperTxtPassword)) {
            final String username = mWrapperTxtUsername.getEditText().getText().toString();
            final String password = mWrapperTxtPassword.getEditText().getText().toString();
            final String authCredential = Credentials.basic(username, password);
            mPresenter.callGetUser(authCredential);
        }
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
                mPresenter.callAccessToken(clientId, clientSecret, code);
            } else if (uri.getQueryParameter("error") != null) {
                showError(uri.getQueryParameter("error"));
            }
            // Clear Intent Data preventing multiple calls
            getIntent().setData(null);
        }
    }

    @NonNull
    private String getOAuthRedirectUri() {
        return getString(R.string.oauth_schema) + "://" + getString(R.string.oauth_host);
    }

    @Override
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
