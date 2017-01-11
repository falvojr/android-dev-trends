package io.brainmachine.adt.domain.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.brainmachine.adt.R;

/**
 * Entity for GitHub Status API.
 *
 * Created by falvojr on 1/5/17.
 */
public class Status {

    @SerializedName("status")
    public Type type;
    @SerializedName("body")
    public String message;
    public Date created_on;
    public Date last_updated;

    public enum Type {
        NONE(android.R.color.black, R.string.txt_loading),
        @SerializedName("good")
        GOOD(R.color.materialGreen500, R.string.txt_good),
        @SerializedName("minor")
        MINOR(R.color.materialOrange500, R.string.txt_minor),
        @SerializedName("major")
        MAJOR(R.color.materialRed500, R.string.txt_major);

        private final int messageRes;
        private final int colorRes;

        Type(int colorRes, int messageRes) {
            this.colorRes = colorRes;
            this.messageRes = messageRes;
        }

        public int getColorRes() {
            return colorRes;
        }
        public int getMessageRes() {
            return messageRes;
        }
    }
}
