package io.github.digital_travel_guide.digitaltravelguide;

import android.content.Intent;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Owner on 3/23/2018.
 */

public class locationInfo {
    private int id;
    private String name;
    private double lat;
    private double lng;
    private double parkingLat;
    private double parkingLng;
    private String googleID;

    public locationInfo(int id, String name, double lat, double lng, double parkingLat, double parkingLng, String googleID) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.parkingLat = parkingLat;
        this.parkingLng = parkingLng;
        this.googleID = googleID;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getGoogleID() {
        return googleID;
    }

    public LatLng getLatLng() {
        if (lat != 0 || lng != 0) {
            return new LatLng(lat, lng);
        } else {
            return null;
        }
    }

    public LatLng getParkingLatLng() {
        if (parkingLat != 0 || parkingLng != 0) {
            return new LatLng(parkingLat, parkingLng);
        } else if (parkingLat == 0.1 || parkingLng == 0.1) {
            /*
            UPDATE HERE
            TO OPEN THE INFORMATION ACTIVTTY
            */
            return null;
        } else {
            return null;
        }
    }

    /*
    private LatLng position;
    private Marker marker;

    public locationInfo(String name, LatLng position, Marker marker) {
        this.name = name;
        this.position = position;
        this.marker = marker;
    }

    public String getName() {
        return name;
    }

    public LatLng getPosition() {
        return position;
    }

    public Marker getMarker() {
        return marker;
    }
    */
}
