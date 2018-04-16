package io.github.digital_travel_guide.digitaltravelguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class InformationTesting extends AppCompatActivity {

    private String currentLocationNameTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_testing);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        if (b != null) {
            currentLocationNameTest = (String) b.get("locationName");
            Toast.makeText(this, "Current location:\n" + currentLocationNameTest, Toast.LENGTH_LONG).show();
        }
    }

    /**
    String hotelName=currentLocationNameTest;
    TextView LocationHotel;
    LocationHotel=(TextView)findViewById(R.id.locationName);
    LocationHotel.setText(hotelName);
    */

    public void returnToMapView(View v)
    {
        onBackPressed();
    }
}
