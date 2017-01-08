package io.brainmachine.adt.domain.entity;

/**
 * Created by falvojr on 1/8/17.
 */
public class AccessToken {
    public String access_token;
    public String token_type;

    public String getAuthCredential() {
        // OAuth requires uppercase Authorization HTTP header value for token type
        if (!Character.isUpperCase(token_type.charAt(0))) {
            token_type = Character.toString(token_type.charAt(0)).toUpperCase() + token_type.substring(1);
        }
        return token_type + " " + access_token;
    }
}
