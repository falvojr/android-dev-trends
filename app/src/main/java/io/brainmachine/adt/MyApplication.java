package io.brainmachine.adt;

import android.app.Application;

import io.brainmachine.adt.dagger.DaggerDiComponent;
import io.brainmachine.adt.dagger.DiComponent;
import io.brainmachine.adt.dagger.UiComponent;
import io.brainmachine.adt.dagger.module.ApplicationModule;

/**
 * Created by falvojr on 1/12/17.
 */
public class MyApplication extends Application {

    private DiComponent mDiComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mDiComponent = DaggerDiComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public UiComponent getDaggerUiComponent() {
        return mDiComponent.uiComponent();
    }
}
