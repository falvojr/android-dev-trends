package io.brainmachine.adt.dagger.module;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.brainmachine.adt.domain.GitHubOAuthService;
import io.brainmachine.adt.domain.GitHubService;
import io.brainmachine.adt.domain.GitHubStatusService;
import retrofit2.Retrofit;

import static io.brainmachine.adt.dagger.module.NetworkModule.RETROFIT_GITHUB;
import static io.brainmachine.adt.dagger.module.NetworkModule.RETROFIT_GITHUB_OAUTH;
import static io.brainmachine.adt.dagger.module.NetworkModule.RETROFIT_GITHUB_STATUS;

/**
 * Created by falvojr on 1/12/17.
 */
@Module
public class ServiceModule {

    @Singleton
    @Provides
    GitHubService providesGitHubApi(
            @Named(RETROFIT_GITHUB) Retrofit retrofit) {
        return retrofit.create(GitHubService.class);
    }

    @Singleton
    @Provides
    GitHubStatusService providesGitHubStatusApi(
            @Named(RETROFIT_GITHUB_STATUS) Retrofit retrofit) {
        return retrofit.create(GitHubStatusService.class);
    }

    @Singleton
    @Provides
    GitHubOAuthService providesGitHubOAuthApi(
            @Named(RETROFIT_GITHUB_OAUTH) Retrofit retrofit) {
        return retrofit.create(GitHubOAuthService.class);
    }

}
