package io.github.digital_travel_guide.digitaltravelguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


public class InformationTesting extends AppCompatActivity {

    private String currentLocationNameTest;
    private String phNumber, locationInformation, locInfo1, locInfo2;
    locationInfo currLoc;
    //ADD MORE VARIABLES

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_testing);
        Intent i = getIntent();
        Bundle b = i.getExtras();

        if (b != null) {
            currentLocationNameTest = (String) b.get("locationName"); // current name passed from map activity

            // get information from the json file about the current location
            currLoc= locationHandler.getLocation(currentLocationNameTest);

            // assign the info located in json file to variables
            phNumber = currLoc.getPhone();
            locInfo1 = currLoc.getAddress1();
            locInfo2 = currLoc.getAddress2();

            // combine the variables into a string we can display
            locationInformation = locInfo1+ " " + locInfo2 + "\n\n" + phNumber;

        }

        // update name of casino textview
        TextView LocationHotel=(TextView)this.findViewById(R.id.locationName);
        LocationHotel.setText(currentLocationNameTest);

        // update location info textview
        TextView LocationHotelInfo=(TextView)this.findViewById(R.id.hotelInfo);
        LocationHotelInfo.setText(locationInformation);

    }




    public void returnToMapView(View v)
    {
        onBackPressed();
    }
}
