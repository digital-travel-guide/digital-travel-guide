package io.github.digital_travel_guide.digitaltravelguide;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class splashActivity extends AppCompatActivity {
    boolean initialStart = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //final ProgressBar loadingBar = (ProgressBar) findViewById(R.id.loadingBar);
        final int maxTime = 3000;
        //loadingBar.setMax(maxTime);
        //loadingBar.setProgress(0, true);

        //initialize location info array (might want to do this asynchronously)
        ((Globals)getApplication()).initLocationInfo();

        //A count down timer that is done in milliseconds. So recall that 1000 milliseconds = 1 second
        //This countdown is counting down from 3 seconds at intervals of 1 second.
        new CountDownTimer(maxTime, 1000) {

            public void onTick(long millisUntilFinished) {
                //loadingBar.setProgress((int) (maxTime - millisUntilFinished), true);
            }

            public void onFinish() {
                //loadingBar.setProgress(maxTime, true);
                Intent i = new Intent(getApplicationContext(), Las_Vegas_MapActivity.class);
                startActivity(i);
                finish();
                initialStart = false;
            }
        }.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (initialStart == false){
            //A count down timer that is done in milliseconds. So recall that 1000 milliseconds = 1 second
            //This countdown is counting down from 1 seconds at intervals of 1 second.
            //This is needed if user gets back to this screen, so we will push the user back
            new CountDownTimer(1000, 1000) {

                public void onTick(long millisUntilFinished) {
                    //Do nothing when counting down
                }

                public void onFinish() {
                    //Send user back to map activity
                    Intent i = new Intent(getApplicationContext(), Las_Vegas_MapActivity.class);
                    startActivity(i);
                    initialStart = false;
                }
            }.start();
        }
    }
}
