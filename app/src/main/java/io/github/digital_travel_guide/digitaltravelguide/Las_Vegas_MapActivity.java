package io.github.digital_travel_guide.digitaltravelguide;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Las_Vegas_MapActivity extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    boolean lock_user = false;

    //This is for checking request for User's GPS location
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                mMap.setMyLocationEnabled(true);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_las__vegas__map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /** This part is needed to get the User's GPS location */
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //when location changes, we log and toast it for now
                //Log.i("Location", location.toString());
                //Toast.makeText(Las_Vegas_MapActivity.this, location.toString() , Toast.LENGTH_SHORT).show();
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                //mMap.clear();
                if(lock_user == true) {
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
                    //Ranges from 1 to 20, 20 being the most zoomed in.
                    //mMap.animateCamera(CameraUpdateFactory.zoomTo(18));

                    // Construct a CameraPosition focusing on User location and animate the camera to that position.
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(userLocation)                           // Sets the center of the map to Mountain View
                            .zoom(18)                                       // Sets the zoom
                            .bearing(location.getBearing())                 // Sets the orientation of the camera to user's
                            .tilt(0)                                        // Sets the tilt of the camera to 0 degrees
                            .build();                                       // Creates a CameraPosition from the builder
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        //Get permission if necessary
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //ask for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else{
            //we have permission, note minTime is in milli-seconds so 1 sec = 1000 milli-secs
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, locationListener);
            mMap.setMyLocationEnabled(true);
        }

        //At this point, we have GPS location of the User

        // Add a marker in Las Vegas and move the camera
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        LatLng LasVegas = new LatLng(36.115134, -115.172934);
        mMap.addMarker(new MarkerOptions().position(LasVegas).title("Las Vegas"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LasVegas));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));


        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        //Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
        //Toast.makeText(this, "Zooming to your location", Toast.LENGTH_SHORT).show();
        if(lock_user == false){
            lock_user = true;
            Toast.makeText(this, "Locking on to your location", Toast.LENGTH_SHORT).show();
        }
        else{
            lock_user = false;
            Toast.makeText(this, "Locking your location disabled", Toast.LENGTH_SHORT).show();
        }
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
    }

    @Override
    public boolean onMyLocationButtonClick() {
       Toast.makeText(this, "Going to your location", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }
}
