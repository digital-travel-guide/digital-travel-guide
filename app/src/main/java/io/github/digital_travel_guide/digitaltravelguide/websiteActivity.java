package io.github.digital_travel_guide.digitaltravelguide;

import android.content.Intent;
import android.net.Uri;
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
        //myWebView.loadUrl(website);
        shouldOverrideUrlLoading(myWebView, website);


        finish();
    }

    public void returnToInformationView(View v)
    {
        onBackPressed();
    }


    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        /*
        if (Uri.parse(url).getHost().equals("www.example.com")) {
            // This is my web site, so do not override; let my WebView load the page
            return false;
        }
        */
        // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
        return true;
    }
}

