package io.brainmachine.adt.domain.repository;

import io.brainmachine.adt.domain.entity.Status;
import rx.Observable;

/**
 * GitHub Status repository.
 * <p>
 * Created by falvojr on 1/13/17.
 */
public interface GitHubStatusRepository {

    Observable<Status> lastMessage();
}
