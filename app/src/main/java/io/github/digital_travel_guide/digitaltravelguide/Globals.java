package io.github.digital_travel_guide.digitaltravelguide;

import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Joel on 3/23/2018.
 */

public class Globals extends Application {
    private ArrayList<locationInfo> locationInfoArr = new ArrayList<locationInfo>();

    public void loadJSON() {
        locationInfoArr.add(new locationInfo (00001, "Mandalay Bay", 36.091799, -115.176143, "ChIJYUKC-8_FyIAR7920xSWK9pc"));
        locationInfoArr.add(new locationInfo (00002, "Tropicana", 36.091799, -115.176143, "ChIJx9KzNzPEyIARcKoYGU1i1g8"));


        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(locationInfoArr);
        Log.d("gsonTest",jsonOutput);

        String jsonAsset = readJSONFromAsset();
        Log.d("gsonTest",jsonAsset);
        locationInfoArr = gson.fromJson(jsonAsset,new TypeToken<ArrayList<locationInfo>>(){}.getType());

        jsonOutput = gson.toJson(locationInfoArr);
        Log.d("gsonTest",jsonOutput);
    }

    public String readJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("locations.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
