package io.brainmachine.adt.presentation.base;

import android.support.v7.app.AppCompatActivity;

import io.brainmachine.adt.MyApplication;
import io.brainmachine.adt.dagger.UiComponent;

public class BaseActivity extends AppCompatActivity {

    protected MyApplication getMyApplication() {
        return (MyApplication) getApplication();
    }

    protected UiComponent getDaggerUiComponent() {
        return getMyApplication().getDaggerUiComponent();
    }
}
