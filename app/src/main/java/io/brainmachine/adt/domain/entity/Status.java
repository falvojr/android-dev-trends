package io.brainmachine.adt.domain.entity;

import com.google.gson.annotations.SerializedName;

import io.brainmachine.adt.R;

/**
 * Entity for GitHub Status API.
 *
 * Created by falvojr on 1/5/17.
 */
public class Status {

    @SerializedName("status")
    public Type type;
    public String body;
    public String created_on;
    public String last_updated;

    public enum Type {
        @SerializedName("good")
        GOOD(R.color.materialGreen500),
        @SerializedName("minor")
        MINOR(R.color.materialOrange500),
        @SerializedName("major")
        MAJOR(R.color.materialRed500);

        private int color;

        Type(int color) {
            this.color = color;
        }

        public int getColor() {
            return color;
        }
    }
}
