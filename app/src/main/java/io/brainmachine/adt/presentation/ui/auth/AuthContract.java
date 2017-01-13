package io.brainmachine.adt.presentation.ui.auth;

import io.brainmachine.adt.domain.entity.Status;
import io.brainmachine.adt.domain.entity.User;

public interface AuthContract {

    interface View {
        void onLoadStatusType(Status.Type statusType);

        void onAuthSuccess(String credential, User entity);

        void showError(String message);
    }

    interface Presenter {
        void setView(AuthContract.View view);

        void loadStatus();

        void callGetUser(String authorization);

        void callAccessToken(String clientId, String clientSecret, String code);
    }
}
