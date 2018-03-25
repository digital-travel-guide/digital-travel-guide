package io.github.digital_travel_guide.digitaltravelguide;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.WeakHashMap;

public class Las_Vegas_MapActivity extends AppCompatActivity  implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, OnMapReadyCallback, SensorEventListener, GoogleMap.OnCameraIdleListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    boolean lock_user = false;
    private float bearing = 0;
    private SensorManager mSensorManager;
    private Sensor mRotVectSensor;
    private WeakHashMap mMarkers = new WeakHashMap<Marker, locationInfo>();

    //This is for checking request for User's GPS location
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, locationListener);
                mMap.setMyLocationEnabled(true);
            }
        }
    }

    //Get the menu options bar based on the navigation.xml file under the menu directory
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_las__vegas__map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar mapToolbar = (Toolbar) findViewById(R.id.map_toolbar);
        setSupportActionBar(mapToolbar);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) != null) {
            mRotVectSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        } else {
            mRotVectSensor = null;
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Las Vegas, NV, United States.
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
                if (lock_user == true) {
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
                    //Ranges from 1 to 20, 20 being the most zoomed in.
                    //mMap.animateCamera(CameraUpdateFactory.zoomTo(18));

                    // Construct a CameraPosition focusing on User location and animate the camera to that position.
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(userLocation)                           // Sets the center of the map to Mountain View
                            .zoom(18)                                       // Sets the zoom
                            .bearing(bearing)                               // Sets the orientation of the camera to user's , location.getBearing()
                            .tilt(45)                                        // Sets the tilt of the camera to 0 degrees
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //ask for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            //we have permission, note minTime is in milli-seconds so 1 sec = 1000 milli-secs
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, locationListener);
            mMap.setMyLocationEnabled(true);
        }

        //At this point, we have GPS location of the User

        // Move camera to Las Vegas
        //This is also the default view when user opens the app
        //Set map style to be hybrid (satellite) with indoor map on by default
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        LatLng LasVegas = new LatLng(36.115134, -115.172934);
        //Marker LasVegasMarker = mMap.addMarker(new MarkerOptions().position(LasVegas).title("Las Vegas"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LasVegas));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

        //These are necessary to enable special phone features like GPS and detecting phone movement
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.setOnCameraIdleListener(this);

        //Bellagio ground overlay test
        //information here: https://developers.google.com/maps/documentation/android-api/groundoverlay

        LatLng bellagioCenter = new LatLng(36.113406, -115.176031);
        LatLng bellagioSW = new LatLng(36.110001, -115.179299);
        LatLng bellagioNE = new LatLng(36.115057, -115.1727991);

        LatLngBounds bellagioBounds = new LatLngBounds(
                bellagioSW,       // South west corner
                bellagioNE);      // North east corner

        GroundOverlayOptions bellagioMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.groundmap_bellagio))
                .positionFromBounds(bellagioBounds)
                .transparency(0.1f);

        // Add an overlay to the map, retaining a handle to the GroundOverlay object.
        GroundOverlay imageOverlay = mMap.addGroundOverlay(bellagioMap);



        //Call function that will set initial markers
        setMarkers();

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {


        if (lock_user == true) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                //bearing = (float) Math.toDegrees(sensorEvent.values[0]);
                float[] mRotationMatrix = new float[16];
                SensorManager.getRotationMatrixFromVector(mRotationMatrix, sensorEvent.values);
                float[] orientation = new float[3];
                SensorManager.getOrientation(mRotationMatrix, orientation);
                bearing = (float) Math.toDegrees(orientation[0]);
                //CameraPosition oldPos = mMap.getCameraPosition();
                //CameraPosition pos = CameraPosition.builder(oldPos).bearing(bearing).build();
                //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(pos));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRotVectSensor != null) {
            mSensorManager.registerListener(this,
                    mRotVectSensor,
                    SensorManager.SENSOR_STATUS_ACCURACY_LOW);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        //Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
        //Toast.makeText(this, "Zooming to your location", Toast.LENGTH_SHORT).show();
        if (lock_user == false) {
            lock_user = true;
            Toast.makeText(this, "Locking on to your location", Toast.LENGTH_SHORT).show();
        } else {
            lock_user = false;
            Toast.makeText(this, "Locking your location disabled", Toast.LENGTH_SHORT).show();
        }
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        // Construct a CameraPosition focusing on User location and animate the camera to that position.
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(userLocation)                           // Sets the center of the map to Mountain View
                .zoom(17)                                       // Sets the zoom
                .bearing(0)                               // Sets the orientation of the camera to user's , location.getBearing()
                .tilt(0)                                        // Sets the tilt of the camera to 0 degrees
                .build();                                       // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Going to your location", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    public void centerOnVegas() {
        if (lock_user == true) {
            lock_user = false;
            Toast.makeText(this, "Locking your location disabled", Toast.LENGTH_SHORT).show();
        }
        LatLng LasVegas = new LatLng(36.115134, -115.172934);
        // Construct a CameraPosition focusing on User location and animate the camera to that position.
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(LasVegas)                           // Sets the center of the map to Mountain View
                .zoom(15)                                       // Sets the zoom //Ranges from 1 to 20, 20 being the most zoomed in.
                .bearing(0)                               // Sets the orientation of the camera to user's , location.getBearing()
                .tilt(0)                                        // Sets the tilt of the camera to 0 degrees
                .build();                                       // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void setMarkers(){

        /*
        //Ballagio
        initMarker(36.112642, -115.176355, "Bellagio");

        //Luxor
        initMarker(36.095480, -115.175788, "Luxor");

        //Mandalay Bay
        initMarker(36.091799, -115.176143, "Mandalay Bay");
        */

        putMarker("Bellagio");

    }

    private void putMarker(String name) {
        //get info from global list
    }

    private void initMarker(double lat, double lng, String name){
        Marker marker;
        locationInfo loc;

        LatLng building = new LatLng(lat, lng);
        marker = mMap.addMarker(new MarkerOptions()
                .position(building)
                .title(name)
                .visible(true)
        );
        //loc = new locationInfo(name, building, marker);
        //mMarkers.put(marker, loc);
    }

    public void showAllMarkers(boolean showM){

    }

    public void nearCamLocation(){
        LatLng camLocation = mMap.getCameraPosition().target;

    }

    @Override
    public void onCameraIdle() {
        Toast.makeText(this, "The camera has stopped moving.", Toast.LENGTH_SHORT).show();
        //TODO: Using this function, we will find the position of the camera and find anything that is nearby and update map
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                // User chose the "Home" item
                centerOnVegas();
                return true;

            case R.id.navigation_dashboard:
                // User chose the "Dashboard" action
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}
