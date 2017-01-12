package io.brainmachine.adt.dagger.module;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.brainmachine.adt.R;

/**
 * Created by falvojr on 1/12/17.
 */
@Module
public class PreferenceModule {

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Context context) {
        final String fileName = context.getString(R.string.sp_file_key);
        return context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }
}