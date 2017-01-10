package io.brainmachine.adt.util;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

import io.brainmachine.adt.R;

/**
 * Useful class for App.
 *
 * Created by falvojr on 1/10/17.
 */
public final class AppUtil {

    private AppUtil() {
        super();
    }

    public static boolean validateRequiredTextInputLayout(Context context, TextInputLayout... fields) {
        boolean isValid = true;
        for (TextInputLayout field : fields) {
            final EditText editText = field.getEditText();
            if (editText != null) {
                if (editText.getText().toString().isEmpty()) {
                    field.setError(context.getString(R.string.msg_required_field));
                    field.setErrorEnabled(true);
                    isValid = false;
                } else {
                    field.setError(null);
                    field.setErrorEnabled(false);
                }
            } else {
                throw new RuntimeException("All TextInputLayout must have a EditText inside.");
            }
        }
        return isValid;
    }
}
