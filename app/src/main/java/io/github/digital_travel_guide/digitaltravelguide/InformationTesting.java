package io.github.digital_travel_guide.digitaltravelguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import android.widget.ImageView;


public class InformationTesting extends AppCompatActivity {

    private String currentLocationNameTest;
    private String phNumber, locationInformation, locInfo1, locInfo2, webAddress;
    //private int locationID;
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
            webAddress = currLoc.getWebaddress();
            //locationID = currLoc.getId();

            // combine the variables into a string we can display
            locationInformation = locInfo1+ "\n" + locInfo2;

            // change images
            int locationID = currLoc.getId();
            ImageView Image=(ImageView)this.findViewById(R.id.LocationPhoto);
            switch(locationID) {
                case 1 :
                    Image.setImageDrawable(getDrawable(R.drawable.locationmandalaybay));
                    break;
                case 2 :
                    Image.setImageDrawable(getDrawable(R.drawable.locationtropicana));
                    break;
                case 3 :
                    Image.setImageDrawable(getDrawable(R.drawable.locationluxor));
                    break;
                case 4 :
                    Image.setImageDrawable(getDrawable(R.drawable.locationmgmgrand));
                    break;
                case 5 :
                    Image.setImageDrawable(getDrawable(R.drawable.locationexcalibur));
                    break;
                case 6 :
                    Image.setImageDrawable(getDrawable(R.drawable.locationplanetholllywood));
                    break;
                case 7 :
                    Image.setImageDrawable(getDrawable(R.drawable.locationnewyorknewyork));
                    break;
                case 8 :
                    Image.setImageDrawable(getDrawable(R.drawable.locationparis));
                    break;
                case 9 :
                    Image.setImageDrawable(getDrawable(R.drawable.locationmontecarlo));
                    break;
                case 10 :
                    Image.setImageDrawable(getDrawable(R.drawable.locationballys));
                    break;
                case 11 :
                    Image.setImageDrawable(getDrawable(R.drawable.locationaria));
                    break;
                case 12 :
                    Image.setImageDrawable(getDrawable(R.drawable.locationflamingo));
                    break;
                case 13 :
                    Image.setImageDrawable(getDrawable(R.drawable.locationthecosmopolitan));
                    break;
                case 14 :
                    Image.setImageDrawable(getDrawable(R.drawable.locationthelinq));
                    break;
                case 15 :
                    Image.setImageDrawable(getDrawable(R.drawable.locationvdara));
                    break;
                case 16 :
                    Image.setImageDrawable(getDrawable(R.drawable.locationharrahs));
                    break;
                case 17 :
                    Image.setImageDrawable(getDrawable(R.drawable.locationbellagio));
                    break;
                case 18 :
                    Image.setImageDrawable(getDrawable(R.drawable.locationthevenetian));
                    break;
                case 19 :
                    Image.setImageDrawable(getDrawable(R.drawable.locationcaesarspalace));
                    break;
                case 20 :
                    Image.setImageDrawable(getDrawable(R.drawable.locationthepalazzo));
                    break;
                case 21 :
                    Image.setImageDrawable(getDrawable(R.drawable.locationthemirage));
                    break;
                case 22 :
                    Image.setImageDrawable(getDrawable(R.drawable.locationwynn));
                    break;
                case 23 :
                    Image.setImageDrawable(getDrawable(R.drawable.locationtreasureisland));
                    break;
                case 24 :
                    Image.setImageDrawable(getDrawable(R.drawable.locationencore));
                    break;
                case 25 :
                    Image.setImageDrawable(getDrawable(R.drawable.locationcircuscircus));
                    break;
                case 26 :
                    Image.setImageDrawable(getDrawable(R.drawable.locationvegassign));
                    break;
                case 27 :
                    Image.setImageDrawable(getDrawable(R.drawable.locationthomasandmack));
                    break;
                default:
                    Image.setImageDrawable(getDrawable(R.drawable.locationplaceholder));
                    break;
            }

/*
            if(currLoc.getId() == 17) {
                ImageView Image=(ImageView)this.findViewById(R.id.LocationPhoto);
                Image.setImageDrawable(getDrawable(R.drawable.locationbellagio));
            }
            if(currLoc.getName().equals("Caesar's Palace")) {
                ImageView Image=(ImageView)this.findViewById(R.id.LocationPhoto);
                Image.setImageDrawable(getDrawable(R.drawable.locationcaesarspalace));
            }
            if(currLoc.getName().equals("Welcome to Las Vegas")) {
                ImageView Image=(ImageView)this.findViewById(R.id.LocationPhoto);
                Image.setImageDrawable(getDrawable(R.drawable.locationvegassign));
            }
*/
        }

        // update name of casino textview
        TextView LocationHotel=(TextView)this.findViewById(R.id.locationName);
        LocationHotel.setText(currentLocationNameTest);

        // update hotel info textview
        TextView LocationHotelInfoAddress=(TextView)this.findViewById(R.id.hotelInfoAddress);
        LocationHotelInfoAddress.setText(locationInformation);

        // update phone info textview
        TextView LocationHotelInfoPhone=(TextView)this.findViewById(R.id.hotelInfoPhone);
        LocationHotelInfoPhone.setText(phNumber);

        // update web info textview
        TextView LocationHotelInfoWeb=(TextView)this.findViewById(R.id.hotelInfoWeb);
        LocationHotelInfoWeb.setText(webAddress);

        /*

        if(locationID=="00019") {
        ImageView LocationImageCaesars=(ImageView)this.findViewById(R.id.LocationPhoto);
        LocationImageCaesars.setImageDrawable(@drawable/caesarspalace);
        }

        if(locationID=="00026") {
            ImageView LocationImageVegasSign=(ImageView)this.findViewById(R.id.LocationPhoto);
            LocationImageVegasSign.setImageDrawable(@drawable/locationvegassign);
        }
         */

    }


    public void openWebsite(View view){
        //Open in-app browser
        int id = view.getId();

        switch (id) {
            case R.id.buttonWebsite:
                Toast.makeText(getApplicationContext(), "Website Button clicked.", Toast.LENGTH_SHORT).show();
                startBrowser(webAddress);
                break;

            case R.id.buttonVenue:
                Toast.makeText(getApplicationContext(), "Venue Button clicked.", Toast.LENGTH_SHORT).show();
                startBrowser(webAddress);
                break;

            case R.id.buttonDining:
                Toast.makeText(getApplicationContext(), "Dining Button clicked.", Toast.LENGTH_SHORT).show();
                startBrowser(webAddress);
                break;

            default:
                Toast.makeText(getApplicationContext(), "Button clicked.", Toast.LENGTH_SHORT).show(); break;

        }

    }

    private void startBrowser(String website){
        Intent activityTest = new Intent(getApplicationContext(), websiteActivity.class);
        activityTest.putExtra("websiteName", website);
        startActivity(activityTest);
    }



    public void returnToMapView(View v)
    {
        onBackPressed();
    }
}
