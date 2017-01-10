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
        NONE(android.R.color.black),
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
