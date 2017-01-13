package io.brainmachine.adt.domain.repository;

import io.brainmachine.adt.domain.entity.User;
import rx.Observable;

/**
 * GitHub API repository.
 * <p>
 * Created by falvojr on 1/5/17.
 */
public interface GitHubRepository {

    Observable<User> getUser(String authorization);
}
