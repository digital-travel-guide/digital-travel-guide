package io.github.digital_travel_guide.digitaltravelguide.models;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ULLOA on 4/6/2018.
 */

public class PlaceInfo {

    private String name;
    private String address;
    private String phoneNumber;
    private String id;
    private Uri wesiteUri;
    private LatLng latlng;
    private float rating;
    private String attriutions;

    public PlaceInfo(String name, String address, String phoneNumber, String id, Uri wesiteUri,
                     LatLng latlng, float rating, String attriutions) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.id = id;
        this.wesiteUri = wesiteUri;
        this.latlng = latlng;
        this.rating = rating;
        this.attriutions = attriutions;
    }

    public PlaceInfo() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Uri getWesiteUri() {
        return wesiteUri;
    }

    public void setWesiteUri(Uri wesiteUri) {
        this.wesiteUri = wesiteUri;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getAttriutions() {
        return attriutions;
    }

    public void setAttriutions(String attriutions) {
        this.attriutions = attriutions;
    }

    @Override
    public String toString() {
        return "PlaceInfo{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", id='" + id + '\'' +
                ", wesiteUri=" + wesiteUri +
                ", latlng=" + latlng +
                ", rating=" + rating +
                ", attriutions='" + attriutions + '\'' +
                '}';
    }
}
