package io.brainmachine.adt.dagger;

import javax.inject.Singleton;

import dagger.Component;
import io.brainmachine.adt.MainActivity;
import io.brainmachine.adt.dagger.module.ApplicationModule;
import io.brainmachine.adt.dagger.module.PreferenceModule;
import io.brainmachine.adt.dagger.module.NetworkModule;
import io.brainmachine.adt.dagger.module.ServiceModule;

/**
 * Created by falvojr on 1/12/17.
 */
@Singleton
@Component(modules = {
        ApplicationModule.class,
        PreferenceModule.class,
        NetworkModule.class,
        ServiceModule.class
})
public interface DiComponent {
    void inject(MainActivity activity);
}
