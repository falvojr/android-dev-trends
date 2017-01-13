package io.brainmachine.adt.infraestructure.storage.manager;

import javax.inject.Inject;

import io.brainmachine.adt.domain.entity.AccessToken;
import io.brainmachine.adt.domain.repository.GitHubOAuthRepository;
import io.brainmachine.adt.infraestructure.storage.service.GitHubOAuthService;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GitHubOAuthManager implements GitHubOAuthRepository {

    private final GitHubOAuthService mGitHubOAuthService;

    @Inject
    public GitHubOAuthManager(GitHubOAuthService gitHubOAuthService) {
        mGitHubOAuthService = gitHubOAuthService;
    }

    @Override
    public Observable<AccessToken> accessToken(String clientId, String clientSecret, String code) {
        return mGitHubOAuthService.accessToken(clientId, clientSecret, code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}