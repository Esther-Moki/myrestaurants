
package com.moringaschool.myrestaurants.models;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.moringaschool.myrestaurants.models.Center;

import org.parceler.Parcel;

@Generated("jsonschema2pojo")

@Parcel
public class Region {

    @SerializedName("center")
    @Expose
    private Center center;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Region() {
    }

    /**
     * 
     * @param center
     */
    public Region(Center center) {
        super();
        this.center = center;
    }

    public Center getCenter() {
        return center;
    }

    public void setCenter(Center center) {
        this.center = center;
    }

}
