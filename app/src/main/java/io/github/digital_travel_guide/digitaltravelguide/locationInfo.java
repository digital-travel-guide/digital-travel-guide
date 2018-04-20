package io.github.digital_travel_guide.digitaltravelguide;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Owner on 3/23/2018.
 */

public class locationInfo {
    private int id;
    private String name;

    private String phone;
    private String address1;
    private String address2;
    private String webaddress;

    private double lat;
    private double lng;

    private double parkingLat;
    private double parkingLng;
    private String googleID;
    private Marker marker = null;

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public locationInfo(int id, String name, String phone, String address1, String address2, String webaddress, double lat, double lng, double parkingLat, double parkingLng, String googleID) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address1 = address1;
        this.address2 = address2;
        this.webaddress = webaddress;
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

    public String getPhone() {
        return phone;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getWebaddress() {
        return webaddress;
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
}
