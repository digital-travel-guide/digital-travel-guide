package io.github.digital_travel_guide.digitaltravelguide;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Owner on 3/23/2018.
 */

public class locationInfo {
    private String name;
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
}
