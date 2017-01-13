package io.brainmachine.adt.dagger;

import javax.inject.Singleton;

import dagger.Component;
import io.brainmachine.adt.dagger.module.ApplicationModule;
import io.brainmachine.adt.dagger.module.PreferenceModule;
import io.brainmachine.adt.dagger.module.infraestruture.ManagerModule;
import io.brainmachine.adt.dagger.module.infraestruture.NetworkModule;
import io.brainmachine.adt.dagger.module.infraestruture.ServiceModule;
import io.brainmachine.adt.dagger.module.presentation.HelperModule;

/**
 * Created by falvojr on 1/12/17.
 */
@Singleton
@Component(modules = {
        ApplicationModule.class,
        HelperModule.class,
        PreferenceModule.class,
        NetworkModule.class,
        ServiceModule.class,
        ManagerModule.class
})
public interface DiComponent {
    UiComponent uiComponent();
}
