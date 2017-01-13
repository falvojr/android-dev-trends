package io.brainmachine.adt.infraestructure.storage.manager;

import io.brainmachine.adt.domain.entity.User;
import io.brainmachine.adt.domain.repository.GitHubRepository;
import io.brainmachine.adt.infraestructure.storage.service.GitHubService;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GitHubManager implements GitHubRepository {

    private final GitHubService mGitHubService;

    public GitHubManager(GitHubService gitHubService) {
        mGitHubService = gitHubService;
    }

    @Override
    public Observable<User> getUser(String authorization) {
        return mGitHubService.getUser(authorization)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}