package io.github.digital_travel_guide.digitaltravelguide;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.github.digital_travel_guide.digitaltravelguide.models.PlaceInfo;

public class Las_Vegas_MapActivity extends AppCompatActivity  implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, OnMapReadyCallback, SensorEventListener, GoogleMap.OnCameraIdleListener, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    boolean lock_user = false;
    private float bearing = 0;
    private SensorManager mSensorManager;
    private Sensor mRotVectSensor;
    private HashMap mMarkers = new HashMap<Marker, locationInfo>();
    private Marker current_parking = null;
    private Marker current_building = null;

    private GroundOverlay imageOverlay = null;
    private RelativeLayout relativeLayout;
    private Button cancel_button;

    private AutoCompleteTextView mSearchText;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;

    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(35.96391231596752, -115.83624217356936), new LatLng(36.44179992718371, -114.73434514331382));

    private static final int PLACE_PICKER_REQUEST = 1;


    private PlaceInfo mPlace;

    private Marker mMarker;

    private ImageView mInfo, mPlacePicker;

    private static ArrayList<GroundOverlay> groundArray = new ArrayList<GroundOverlay>();



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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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

        mSearchText = (AutoCompleteTextView) findViewById(R.id.input_search);
       // mInfo = (ImageView) findViewById(R.id.place_info);
        mPlacePicker = (ImageView) findViewById(R.id.place_picker);

        Toolbar mapToolbar = (Toolbar) findViewById(R.id.map_toolbar);
        setSupportActionBar(mapToolbar);
        //mapToolbar.setTitle(mapToolbar.getTitle());
        //mapToolbar.setTitleTextColor(0x42ffffff);

        relativeLayout = (RelativeLayout) findViewById(R.id.relLayout1);
        relativeLayout.setVisibility(View.GONE);

        cancel_button = (Button) findViewById(R.id.cancel_button);
        cancel_button.setVisibility(View.GONE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.cameraActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(intent);
            }
        });


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) != null) {
            mRotVectSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        } else {
            mRotVectSensor = null;
        }

        init();

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

    private void init() {

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this,this)
                .build();

        mSearchText.setOnItemClickListener(mAutocompleteClickListener);

        AutocompleteFilter filter = new AutocompleteFilter.Builder().setCountry("US").build();

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, LAT_LNG_BOUNDS, filter);

        mSearchText.setAdapter(mPlaceAutocompleteAdapter);

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {

                    //execute our method for searching
                    geoLocate();
                }
                return false;
            }
        });

        mPlacePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                /*
                builder.setLatLngBounds(new LatLngBounds(
                        new LatLng(36.063977, -115.216821),         //SW point
                        new LatLng(36.180729, -115.088419)));       //NE point
                */

                try {
                    startActivityForResult(builder.build(Las_Vegas_MapActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.getMessage();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.getMessage();
                }
            }
        });

        hideSoftKeyboard();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PLACE_PICKER_REQUEST) {
            if(resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this,data);

                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, place.getId());
                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            }
        }
    }

    private void geoLocate(){
        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(Las_Vegas_MapActivity.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch(IOException e) {
            e.getMessage();
        }

        if(list.size() > 0) {
            Address address = list.get(0);
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), 15f,address.getAddressLine(0));
        }
    }

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

        //Call function that will set initial markers
        setMarkers();
        //Add the ground maps
        addGroundMaps();

        //These are necessary to enable special phone features like GPS and detecting phone movement
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.setOnCameraIdleListener(this);
        mMap.setOnMarkerClickListener(
                new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        locationInfo curLoc = (locationInfo) mMarkers.get(marker);
                        //Check to see if this is a parking marker, if yes, do nothing, else continue
                        if(marker.getTitle().endsWith("Parking")){
                            //Do nothing, this is a parking marker
                            current_building = marker;
                        }
                        else {
                            if (current_parking != null) {
                                current_parking.remove();
                                current_parking = null;
                            }
                            //The marker has been clicked, and now we have the building info for that marker
                            //Toast.makeText(getApplicationContext(), "Marker has been clicked.", Toast.LENGTH_SHORT).show();
                            current_building = marker;

                        }
                        // Return false to indicate that we have not consumed the event and that we wish
                        // for the default behavior to occur (which is for the camera to move such that the
                        // marker is centered and for the marker's info window to open, if it has one).
                        return false;
                    }
                }


                //<<<<<<<<<<<<<<<<<<<<<<<<<
                //<<<<<<<<<<<<<<<<<<<<<<<<<
                //<<<<<<<<<<<<<<<<<<<<<<<<<
        );
        mMap.setOnInfoWindowClickListener(
                new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        locationInfo curLoc = (locationInfo) mMarkers.get(marker);
                        //The marker has been clicked, and now we have the building info for that marker
                        //Toast.makeText(getApplicationContext(), "Info Window has been clicked.", Toast.LENGTH_SHORT).show();
                        //curLoc.getName()
                        //Send user to information activity

                        /* THIS IS FOR THE ORIGINAL INFORMATIONACTIVITY
                        Intent i = new Intent(getApplicationContext(), informationActivity.class);
                        try {
                            i.putExtra("locationName", curLoc.getName());
                        } catch (NullPointerException e) {
                            e.getMessage();
                        }
                        startActivity(i);
                        */
                        View view = null;
                        hideSearch(view);
                        Intent activityTest = new Intent(getApplicationContext(), InformationTesting.class);
                        activityTest.putExtra("locationName", curLoc.getName());
                        startActivity(activityTest);
                    }
                }
        );

        //Toggle indoor mapping; True to show, False for not
        mMap.setIndoorEnabled(false);




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
        //Set all the initial markers
        //Names are taken from the locations.json
        putMarker("Mandalay Bay");
        putMarker("Tropicana");
        putMarker("Luxor");
        putMarker("MGM Grand");
        putMarker("Excalibur");
        putMarker("Planet Hollywood");
        putMarker("New York New York");
        putMarker("Paris Las Vegas");
        putMarker("Monte Carlo");
        putMarker("Bally's");
        putMarker("Aria");
        putMarker("Flamingo");
        putMarker("The Cosmopolitan");
        putMarker("The Linq");
        putMarker("Vdara");
        putMarker("Harrah's");
        putMarker("Bellagio");
        putMarker("The Venetian");
        putMarker("Caesar's Palace");
        putMarker("The Palazzo");
        putMarker("The Mirage");
        putMarker("Wynn Las Vegas");
        putMarker("Treasure Island");
        putMarker("Encore");
        putMarker("Circus Circus");
        putMarker("Welcome to Las Vegas");
        putMarker("UNLV Thomas and Mack Center");

        //putMarker("Information Button");
    }

    private void addGroundMaps(){
        GroundOverlay imageOverlay;
        LatLng buildingCenter;
        LatLng buildingSW;
        LatLng buildingNE;
        LatLngBounds buildingBounds;
        GroundOverlayOptions buildingMap;

        //Bellagio ground overlay test
        //information here: https://developers.google.com/maps/documentation/android-api/groundoverlay

        buildingCenter = new LatLng(36.113406, -115.176031);
        buildingSW = new LatLng(36.110001, -115.179299);
        buildingNE = new LatLng(36.115057, -115.1727991);

        buildingBounds = new LatLngBounds(
                buildingSW,       // South west corner
                buildingNE);      // North east corner

        buildingMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.groundmap_bellagio))
                .positionFromBounds(buildingBounds)
                .transparency(0.1f)
                .visible(false);

        // Add an overlay to the map, retaining a handle to the GroundOverlay object.
        imageOverlay = mMap.addGroundOverlay(buildingMap);
        imageOverlay.setVisible(false);
        groundArray.add(imageOverlay);

        //Caesars Palace
        //36.114853, -115.178580 SW
        //36.118964, -115.172915 NE
        //Caesars Palace ground overlay test
        //information here: https://developers.google.com/maps/documentation/android-api/groundoverlay

        buildingSW = new LatLng(36.114853, -115.178580);
        buildingNE = new LatLng(36.118964, -115.172915);

        buildingBounds = new LatLngBounds(
                buildingSW,       // South west corner
                buildingNE);      // North east corner

        buildingMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.groundmap_caesars_palace))
                .positionFromBounds(buildingBounds)
                .transparency(0.1f)
                .visible(false);

        // Add an overlay to the map, retaining a handle to the GroundOverlay object.
        imageOverlay = mMap.addGroundOverlay(buildingMap);
        imageOverlay.setVisible(false);
        groundArray.add(imageOverlay);
    }

    private void putMarker(String name) {
        //get location info from global list
        locationInfo curLoc = locationHandler.getLocation(name);

        Marker marker;

        if (curLoc != null) {
            if (curLoc.getLatLng() != null) {
                marker = mMap.addMarker(new MarkerOptions()
                        .position(curLoc.getLatLng())
                        .title(curLoc.getName())
                        .visible(true)
                        .snippet("Click this box for more information")
                );
                mMarkers.put(marker, curLoc);
                curLoc.setMarker(marker);
            }
        }
    }

    private LatLng getParking(String name) {
        //returns parking LatLng of requested location
        locationInfo curLoc = locationHandler.getLocation(name);

        if (curLoc != null) {
            return curLoc.getParkingLatLng();
        } else {
            return null;
        }
    }

    public void showAllMarkers(boolean showM){

    }

    public void nearCamLocation(){
        LatLng camLocation = mMap.getCameraPosition().target;

        LatLngBounds currentBounds;
        LatLng currentSW;
        LatLng currentNE;

        for (GroundOverlay current_ground : groundArray) {
            //Get current ground overlay bounds from the Ground Overlay
            currentBounds = current_ground.getBounds();
            currentSW = currentBounds.southwest;
            currentNE = currentBounds.northeast;


            if(mMap.getCameraPosition().zoom >= 15 ) {
                if (camLocation.latitude <= currentNE.latitude && camLocation.latitude >= currentSW.latitude) {
                    if (camLocation.longitude <= currentNE.longitude && camLocation.longitude >= currentSW.longitude) {
                        current_ground.setVisible(true);
                    } else {
                        current_ground.setVisible(false);
                    }
                } else {
                    current_ground.setVisible(false);
                }
            }
            else{
                current_ground.setVisible(false);
            }
        }
    }

    private void parkingButton(Marker marker){
        if (marker == null){
            //Do nothing, no building or parking is selected
            Toast.makeText(this, "Select a building to view its parking", Toast.LENGTH_SHORT).show();
        }else{
            if(marker.getTitle().endsWith("Parking")) {
                //Parking marker selected
                //Move camera to parking marker
                // Construct a CameraPosition focusing on User location and animate the camera to that position.
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(marker.getPosition())           // Sets the center of the map to Mountain View
                        .zoom(17)                                       // Sets the zoom
                        .bearing(0)                                     // Sets the orientation of the camera to user's , location.getBearing()
                        .tilt(0)                                        // Sets the tilt of the camera to 0 degrees
                        .build();                                       // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }else{
                //Building marker selected
                locationInfo curLoc = (locationInfo) mMarkers.get(marker);
                //The marker has been clicked, and now we have the building info for that marker
                //Create a parking marker if applicable
                if(current_parking == null && getParking(curLoc.getName()) != null) {
                    current_parking = mMap.addMarker(new MarkerOptions()
                            .position(getParking(curLoc.getName()))
                            .title(curLoc.getName() + " Parking")
                            .visible(true)
                            //.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.parkingmarker))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    );
                    current_parking.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.parkingmarkerv2));
                    // Construct a CameraPosition focusing on User location and animate the camera to that position.
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(current_parking.getPosition())           // Sets the center of the map to Mountain View
                            .zoom(17)                                       // Sets the zoom
                            .bearing(0)                                     // Sets the orientation of the camera to user's , location.getBearing()
                            .tilt(0)                                        // Sets the tilt of the camera to 0 degrees
                            .build();                                       // Creates a CameraPosition from the builder
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
                else{
                    if(getParking(curLoc.getName()) == null) {
                        Toast.makeText(getApplicationContext(), "No parking at the moment.", Toast.LENGTH_SHORT).show();
                    }else{
                        if(current_parking != null){
                            // Construct a CameraPosition focusing on User location and animate the camera to that position.
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(current_parking.getPosition())           // Sets the center of the map to Mountain View
                                    .zoom(17)                                       // Sets the zoom
                                    .bearing(0)                                     // Sets the orientation of the camera to user's , location.getBearing()
                                    .tilt(0)                                        // Sets the tilt of the camera to 0 degrees
                                    .build();                                       // Creates a CameraPosition from the builder
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        }
                    }
                }
            }
        }
    }


    @Override
    public void onCameraIdle() {
        //Toast.makeText(this, "The camera has stopped moving.", Toast.LENGTH_SHORT).show();
        //TODO: Using this function, we will find the position of the camera and find anything that is nearby and update map
        nearCamLocation();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                // User chose the "Home" item
                centerOnVegas();
                return true;

            case R.id.navigation_dashboard:

                // User chose the "searching" action
                relativeLayout.setVisibility(View.VISIBLE);
                cancel_button.setVisibility(View.VISIBLE);
                return true;

            case R.id.navigation_notifications:
                // User chose the "parking" action
                parkingButton(current_building);

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void moveCamera(LatLng latlng, float zoom, PlaceInfo placeInfo) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,zoom));

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(Las_Vegas_MapActivity.this));

        if(placeInfo != null) {
            try {
                String snippet = "Address: " + placeInfo.getAddress() + "\n" +
                        "Phone Number: " + placeInfo.getPhoneNumber() + "\n" +
                        "Website: " + placeInfo.getWesiteUri() + "\n" +
                        "Rating: " + placeInfo.getRating() + "\n";

                MarkerOptions options = new MarkerOptions()
                        .position(latlng)
                        .title(placeInfo.getName())
                        .snippet(snippet);

                mMarker = mMap.addMarker(options);

            } catch (NullPointerException e) {
                e.getMessage();
            }
        } else {
            mMap.addMarker(new MarkerOptions().position(latlng));
        }

        hideSoftKeyboard();

    }

    private void moveCamera(LatLng latlng, float zoom, String title) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,zoom));

        MarkerOptions options = new MarkerOptions()
                .position(latlng)
                .title(title);
        mMap.addMarker(options);

        hideSoftKeyboard();
    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow((findViewById(R.id.input_search)).getWindowToken(), 00);
        relativeLayout.setVisibility(View.GONE);
        AutoCompleteTextView search_box = (AutoCompleteTextView) findViewById(R.id.input_search);
        search_box.setText("");
    }

    /*
    **************************** Google Places API Autocomplete Suggestions **************************
     */

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //hideSoftKeyboard();

            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(i);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            LatLng selectedLocation;
            if(!places.getStatus().isSuccess()) {
                places.getStatus().toString();
                places.release();
                return;
            }
            final Place place = places.get(0);

            selectedLocation = place.getLatLng();
            if(selectedLocation.latitude < 35.96391231596752 || selectedLocation.latitude > 36.44179992718371 &&
                    selectedLocation.longitude > -114.73434514331382 || selectedLocation.longitude < -115.83624217356936) {
                Toast.makeText(getApplicationContext(), "Invalid Search. Location must be within the confines of Las Vegas", Toast.LENGTH_SHORT).show();
                hideSoftKeyboard();
            } else {

                /*try{
                    mPlace = new PlaceInfo();
                    mPlace.setName(place.getName().toString());
                    mPlace.setAddress(place.getAddress().toString());
                    //mPlace.setAttriutions(place.getAttributions().toString());
                    mPlace.setId(place.getId());
                    mPlace.setLatlng(place.getLatLng());
                    mPlace.setRating(place.getRating());
                    mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                    mPlace.setWesiteUri(place.getWebsiteUri());

                    mPlace.toString();
                }catch (NullPointerException e) {
                    e.getMessage();
                }

                moveCamera(new LatLng(place.getViewport().getCenter().latitude, place.getViewport().getCenter().longitude), 15f, mPlace);
                */
                locationInfo curloc = locationHandler.getClosestLocation(place.getLatLng());
                Toast.makeText(getApplicationContext(), "Parking is based on nearest casino", Toast.LENGTH_LONG).show();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 17));      //curloc.getLatLng()
                curloc.getMarker().showInfoWindow();
                if (current_parking != null) {
                    current_parking.remove();
                    current_parking = null;
                }
                current_building = curloc.getMarker();
                hideSoftKeyboard();
                places.release();
            }

        }
    };

    public void hideSearch(View view){
        hideSoftKeyboard();
        cancel_button.setVisibility(View.GONE);

    }

}
