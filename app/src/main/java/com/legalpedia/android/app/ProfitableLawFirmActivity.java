package com.legalpedia.android.app;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * Created by adebayoolabode on 9/13/17.
 */

public class ProfitableLawFirmActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private String videourl;
    private ProgressBar progressBar;
    private String LOGINPREFERENCES="login_data";
    private SharedPreferences sharedpreferences = null;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plfs);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Profitable Law Firm Series");
        sharedpreferences=getSharedPreferences(LOGINPREFERENCES, Context.MODE_PRIVATE);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        videourl=sharedpreferences.getString("videourl","http://resources.legalpediaonline.com/feeds");
        WebView browser=(WebView) findViewById(R.id.webview);

        browser=(WebView)findViewById(R.id.webview);
        WebSettings settings = browser.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        browser.requestFocusFromTouch();

        browser.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                invalidateOptionsMenu();
                progressDialog = new ProgressDialog(ProfitableLawFirmActivity.this,R.style.customDialog);
                progressDialog.setTitle("Legalpedia");
                progressDialog.setMessage("Processing...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
            }

            @Override
            @Deprecated
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.contains("legalpedia://")){
                    String txnref= url.replace("legalpedia://","");

                    Intent intent = new Intent(ProfitableLawFirmActivity.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    view.loadUrl(url); // Stay within this webview and load url

                }
                return true;
            }

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if(request.getUrl().toString().contains("legalpedia://")){
                    String txnref= request.getUrl().toString().replace("legalpedia://","");
                    Intent intent = new Intent(ProfitableLawFirmActivity.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    view.loadUrl(request.getUrl().toString()); // Stay within this webview and load url

                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                invalidateOptionsMenu();
                progressDialog.hide();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                invalidateOptionsMenu();
                progressDialog.hide();
            }
        });
        browser.loadUrl(videourl);

    }
}
