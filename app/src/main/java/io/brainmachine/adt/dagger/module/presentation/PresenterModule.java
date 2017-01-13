package io.brainmachine.adt.dagger.module.presentation;

import dagger.Module;
import dagger.Provides;
import io.brainmachine.adt.dagger.scope.PerActivity;
import io.brainmachine.adt.domain.repository.GitHubOAuthRepository;
import io.brainmachine.adt.domain.repository.GitHubRepository;
import io.brainmachine.adt.domain.repository.GitHubStatusRepository;
import io.brainmachine.adt.presentation.ui.auth.AuthContract;
import io.brainmachine.adt.presentation.ui.auth.AuthPresenter;

@Module
public class PresenterModule {

    @PerActivity
    @Provides
    AuthContract.Presenter provideMainPresenter(
            GitHubRepository gitHubRepository,
            GitHubStatusRepository gitHubStatusRepository,
            GitHubOAuthRepository gitHubOAuthRepository) {
        return new AuthPresenter(gitHubRepository, gitHubStatusRepository, gitHubOAuthRepository);
    }
}
