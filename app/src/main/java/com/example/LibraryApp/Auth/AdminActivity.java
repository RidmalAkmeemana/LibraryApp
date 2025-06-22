package com.example.LibraryApp.Auth;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

import LibraryApp.R;

public class AdminActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_layout);

        // Find the WebView by its ID
        webView = findViewById(R.id.webView);

        // Enable JavaScript (if required)
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Set a WebViewClient to handle page navigation in the WebView
        webView.setWebViewClient(new WebViewClient());

        //inside the WebView
        webView.loadUrl("https://www.facebook.com/");
    }

    // Handle back button press to navigate back in the WebView (optional)
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}
