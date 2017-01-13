package io.brainmachine.adt.infraestructure.storage.manager;

import javax.inject.Inject;

import io.brainmachine.adt.domain.entity.Status;
import io.brainmachine.adt.domain.repository.GitHubStatusRepository;
import io.brainmachine.adt.infraestructure.storage.service.GitHubStatusService;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GitHubStatusManager implements GitHubStatusRepository {

    private final GitHubStatusService mGitHubStatusService;

    @Inject
    public GitHubStatusManager(GitHubStatusService gitHubStatusService) {
        mGitHubStatusService = gitHubStatusService;
    }

    @Override
    public Observable<Status> lastMessage() {
        return mGitHubStatusService.lastMessage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}