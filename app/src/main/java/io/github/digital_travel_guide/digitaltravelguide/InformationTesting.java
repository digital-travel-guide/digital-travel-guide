package io.github.digital_travel_guide.digitaltravelguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;


public class InformationTesting extends AppCompatActivity {

    private String currentLocationNameTest;
    private String phNumber;
    //ADD MORE VARIABLES

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_testing);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        if (b != null) {
            currentLocationNameTest = (String) b.get("locationName");
            phNumber=(String) b.get("phone");
            //UPDATE ADDED VARIABLES HERE
            Toast.makeText(this, "Current location:\n" + currentLocationNameTest, Toast.LENGTH_LONG).show();
        }

        TextView LocationHotel=(TextView)this.findViewById(R.id.locationName);
        LocationHotel.setText(currentLocationNameTest);
    }




    public void returnToMapView(View v)
    {
        onBackPressed();
    }
}
