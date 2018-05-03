package com.legalpedia.android.app;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import org.apache.http.util.EncodingUtils;

import java.util.Date;

/**
 * Created by adebayoolabode on 11/23/16.
 */

public class PaymentViewActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_viewactivity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        progressDialog = new ProgressDialog(this,R.style.customDialog);
        progressDialog.setTitle("Legalpedia");
        progressDialog.setMessage("Processing...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        Intent intent=getIntent();
        String paymenturl = intent.getStringExtra("url");
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
                progressDialog.dismiss();
                invalidateOptionsMenu();
            }

            @Override
            @Deprecated
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.contains("legalpedia://")){
                    String txnref= url.replace("legalpedia://","");

                    Intent intent = new Intent(PaymentViewActivity.this,LoginActivity.class);
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
                    Intent intent = new Intent(PaymentViewActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else{
                    view.loadUrl(request.getUrl().toString()); // Stay within this webview and load url

                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressDialog.dismiss();
                invalidateOptionsMenu();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                progressDialog.dismiss();
                invalidateOptionsMenu();
            }
        });
        browser.loadUrl(paymenturl);
    }

    private class MyWebChromeClient extends WebChromeClient {
        Context context;

        public MyWebChromeClient(Context context) {
            super();
            this.context = context;
        }


    }
}
