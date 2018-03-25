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
    private locationHandler mainHandler;

    /*
    public Globals() {
        initLocationInfo();
    }
    */

    public void initLocationInfo() {
        mainHandler = new locationHandler(getBaseContext());
        mainHandler.loadLocationInfo();
    }
}
