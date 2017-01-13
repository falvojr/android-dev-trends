package io.brainmachine.adt.dagger;

import dagger.Subcomponent;
import io.brainmachine.adt.dagger.module.presentation.PresenterModule;
import io.brainmachine.adt.dagger.scope.PerActivity;
import io.brainmachine.adt.presentation.ui.auth.AuthActivity;

@PerActivity
@Subcomponent(modules = {PresenterModule.class})
public interface UiComponent {
    void inject(AuthActivity activity);
}
