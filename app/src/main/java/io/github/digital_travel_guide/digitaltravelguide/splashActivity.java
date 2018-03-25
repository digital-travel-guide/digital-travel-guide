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

        loadLocationList();

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

    private void loadLocationList() {
        Globals g = (Globals)getApplication();
        g.loadJSON();

        //String jsonString = loadLocationJSON();



        //load location info from json file
        //store list/array as a global
        /*
        try {
            JSONObject obj = new JSONObject(jsonString);
            JSONArray m_jArry = obj.getJSONArray("formules");
            ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> m_li;

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                Log.d("Details-->", jo_inside.getString("formule"));
                String formula_value = jo_inside.getString("formule");
                String url_value = jo_inside.getString("url");

                //Add your values in your `ArrayList` as below:
                m_li = new HashMap<String, String>();
                m_li.put("formule", formula_value);
                m_li.put("url", url_value);

                formList.add(m_li);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        */
    }

    private String loadLocationJSON() {
        return loadJSONFromAsset("locations.json");
    }

    public String loadJSONFromAsset(String fileStr) {
        String json = null;
        /*
        try {
            InputStream is = getActivity().getAssets().open(fileStr);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        */
        return json;
    }
}
