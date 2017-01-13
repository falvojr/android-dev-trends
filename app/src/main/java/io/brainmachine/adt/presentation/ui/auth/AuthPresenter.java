package io.brainmachine.adt.presentation.ui.auth;

import javax.inject.Inject;

import io.brainmachine.adt.domain.entity.Status;
import io.brainmachine.adt.domain.repository.GitHubOAuthRepository;
import io.brainmachine.adt.domain.repository.GitHubRepository;
import io.brainmachine.adt.domain.repository.GitHubStatusRepository;

/**
 * Created by falvojr on 1/13/17.
 */
public class AuthPresenter implements AuthContract.Presenter {

    private AuthContract.View mView;
    private GitHubRepository mGitHubRepository;
    private GitHubStatusRepository mGitHubStatusRepository;
    private GitHubOAuthRepository mGitHubOAuthRepository;

    @Inject
    public AuthPresenter(GitHubRepository gitHubRepository,
                         GitHubStatusRepository gitHubStatusRepository,
                         GitHubOAuthRepository gitHubOAuthRepository) {
        mGitHubRepository = gitHubRepository;
        mGitHubStatusRepository = gitHubStatusRepository;
        mGitHubOAuthRepository = gitHubOAuthRepository;
    }

    @Override
    public void setView(AuthContract.View view) {
        mView = view;
    }

    @Override
    public void loadStatus() {
        mGitHubStatusRepository.lastMessage()
                .subscribe(entity -> {
                    mView.onLoadStatusFinished(entity.type);
                }, error -> {
                    mView.onLoadStatusFinished(Status.Type.MAJOR);
                });
    }

    @Override
    public void callGetUser(String authorization) {
        mGitHubRepository.getUser(authorization)
                .subscribe(entity -> {
                    mView.onAuthSuccess(authorization, entity);
                }, error -> {
                    mView.showError(error.getMessage());
                });
    }

    @Override
    public void callAccessToken(String clientId, String clientSecret, String code) {
        mGitHubOAuthRepository.accessToken(clientId, clientSecret, code)
                .subscribe(entity -> {
                    callGetUser(entity.getAuthCredential());
                }, error -> {
                    mView.showError(error.getMessage());
                });
    }
}
