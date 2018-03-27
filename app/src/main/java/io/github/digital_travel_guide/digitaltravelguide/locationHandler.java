package io.github.digital_travel_guide.digitaltravelguide;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Joel on 3/25/2018.
 */

public class locationHandler {
    private static ArrayList<locationInfo> locationInfoArr = new ArrayList<locationInfo>();
    private static Context context;

    public locationHandler(Context context) {
        this.context = context;
    }

    public void loadLocationInfo() {
        //initialize locationInfo array

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String jsonAsset = readJSONFromAsset("locations.json");
        Log.d("gson",jsonAsset);
        locationInfoArr = gson.fromJson(jsonAsset,new TypeToken<ArrayList<locationInfo>>(){}.getType());

        //test json output
        String jsonOutput = gson.toJson(locationInfoArr);
        Log.d("gson",jsonOutput);
    }

    private String readJSONFromAsset(String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
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

    public static locationInfo getLocation(String name) {
        for (locationInfo loc : locationInfoArr) {
            if (loc.getName().equals(name)) {
                return loc;
            }
        }
        return null;
    }

    public static locationInfo getLocation(int id) {
        for (locationInfo loc : locationInfoArr) {
            if (loc.getId() == id) {
                return loc;
            }
        }
        return null;
    }
}
