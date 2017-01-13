package io.brainmachine.adt.dagger.module.presentation;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import io.brainmachine.adt.presentation.helper.AppHelper;

/**
 * Created by falvojr on 1/13/17.
 */
@Module
public class HelperModule {
    @Provides
    @Reusable
    AppHelper provideTextHelper(Context context) {
        return new AppHelper(context);
    }
}
