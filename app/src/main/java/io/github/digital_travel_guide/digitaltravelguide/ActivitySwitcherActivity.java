package io.github.digital_travel_guide.digitaltravelguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Selection;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ActivitySwitcherActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switcher);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //***The following is code to create the List View called Panel_Selection***
        //Define the object ListView and connect it to the one in the XML file in activity_switcher.xml
        ListView Panel_Selection = (ListView) findViewById(R.id.Panel_Selection);
        //Create an array of strings to list on the activity_switcher.xml screen
        final ArrayList<String> Selections = new ArrayList<String>();
        //add content to the list (array)
        Selections.add("Map");
        Selections.add("Casinos");
        Selections.add("Venues");
        Selections.add("Events");
        //The following code makes it possible to adapt the list/array onto the ListView object
        ArrayAdapter Panel_array_adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Selections);
        Panel_Selection.setAdapter(Panel_array_adapter);
        //set up redirection to other activity pages based on item clicked on list
        Panel_Selection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s = Selections.get(i);
                Intent intent;
                switch(s) {
                    case "Map":
                        intent = new Intent(getApplicationContext(), Las_Vegas_MapActivity.class); break;
                    default:
                        intent = new Intent(getApplicationContext(), Las_Vegas_MapActivity.class); break;

                }
                startActivity(intent);
            }
        });
    }

}
