package io.github.digital_travel_guide.digitaltravelguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

public class websiteActivity extends AppCompatActivity {

    String website = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);
        Intent i = getIntent();
        Bundle b = i.getExtras();

        if (b != null) {
            website = (String) b.get("websiteName"); // current name passed from information activity
        }

        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl(website);
    }

    public void returnToInformationView(View v)
    {
        onBackPressed();
    }
}
