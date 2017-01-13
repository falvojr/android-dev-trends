package io.brainmachine.adt.domain.repository;

import io.brainmachine.adt.domain.entity.AccessToken;
import rx.Observable;

/**
 * GitHub OAUth repository.
 * <p>
 * Created by falvojr on 1/13/17.
 */
public interface GitHubOAuthRepository {

    Observable<AccessToken> getAccessToken(String clientId, String clientSecret, String code);
}
