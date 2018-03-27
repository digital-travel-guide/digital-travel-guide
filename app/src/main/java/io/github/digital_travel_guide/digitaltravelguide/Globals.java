package io.github.digital_travel_guide.digitaltravelguide;

import android.app.Application;

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
