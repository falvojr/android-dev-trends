package io.brainmachine.adt.dagger.module.infraestruture;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.brainmachine.adt.domain.repository.GitHubOAuthRepository;
import io.brainmachine.adt.domain.repository.GitHubRepository;
import io.brainmachine.adt.domain.repository.GitHubStatusRepository;
import io.brainmachine.adt.infraestructure.storage.manager.GitHubManager;
import io.brainmachine.adt.infraestructure.storage.manager.GitHubOAuthManager;
import io.brainmachine.adt.infraestructure.storage.manager.GitHubStatusManager;
import io.brainmachine.adt.infraestructure.storage.service.GitHubOAuthService;
import io.brainmachine.adt.infraestructure.storage.service.GitHubService;
import io.brainmachine.adt.infraestructure.storage.service.GitHubStatusService;

@Module
public class ManagerModule {

    @Singleton
    @Provides
    GitHubRepository providesGitHubRepository(GitHubService gitHubService) {
        return new GitHubManager(gitHubService);
    }

    @Singleton
    @Provides
    GitHubStatusRepository providesGitHubStatusRepository(GitHubStatusService gitHubStatusService) {
        return new GitHubStatusManager(gitHubStatusService);
    }

    @Singleton
    @Provides
    GitHubOAuthRepository providesGitHubOAuthRepository(GitHubOAuthService gitHubOAuthService) {
        return new GitHubOAuthManager(gitHubOAuthService);
    }

}
