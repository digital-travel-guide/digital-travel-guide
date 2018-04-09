package io.github.digital_travel_guide.digitaltravelguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;


public class informationActivity extends AppCompatActivity {

    private String currentLocationName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        if (b != null) {
            currentLocationName = (String) b.get("locationName");
            Toast.makeText(this, "Current location:\n" + currentLocationName, Toast.LENGTH_LONG).show();
        }
    }
}
