package io.brainmachine.adt.infraestructure.storage.manager;

import io.brainmachine.adt.domain.entity.Status;
import io.brainmachine.adt.domain.repository.GitHubStatusRepository;
import io.brainmachine.adt.infraestructure.storage.service.GitHubStatusService;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GitHubStatusManager implements GitHubStatusRepository {

    private final GitHubStatusService mGitHubStatusService;

    public GitHubStatusManager(GitHubStatusService gitHubStatusService) {
        mGitHubStatusService = gitHubStatusService;
    }

    @Override
    public Observable<Status> getLastStatus() {
        return mGitHubStatusService.getLastStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}