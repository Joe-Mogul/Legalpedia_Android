package com.legalpedia.android.app.views;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.legalpedia.android.app.MainActivity;
import com.legalpedia.android.app.NewsJournalActivity;
import com.legalpedia.android.app.R;

/**
 * Created by adebayoolabode on 9/28/17.
 */

public class NewsArticleFragment extends Fragment {

    public NewsArticleFragment(){

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Context context;
    private ProgressDialog progressDialog;
    private NewsJournalActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.web_fragment, container, false);
        context = rootView.getContext();
        progressDialog = new ProgressDialog(context,R.style.customDialog);
        progressDialog.setTitle("Legalpedia");
        progressDialog.setMessage("Processing...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        activity = (NewsJournalActivity)context;
        Intent intent=activity.getIntent();
        String newsfeedurl = intent.getStringExtra("newsfeedurl");
        if(newsfeedurl==null){
            newsfeedurl="http://resources.legalpediaonline.com/newsfeed";
        }
        WebView browser=(WebView) rootView.findViewById(R.id.webview);
        WebSettings settings = browser.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        browser.requestFocusFromTouch();

        browser.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressDialog.dismiss();
                activity.invalidateOptionsMenu();
            }

            @Override
            @Deprecated
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.contains("legalpedia://")){
                    String txnref= url.replace("legalpedia://","");

                    Intent intent = new Intent(context,MainActivity.class);
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
                    Intent intent = new Intent(context,MainActivity.class);
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
                activity.invalidateOptionsMenu();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                progressDialog.dismiss();
                activity.invalidateOptionsMenu();
            }
        });
        browser.loadUrl(newsfeedurl);

        return rootView;
    }


    private class MyWebChromeClient extends WebChromeClient {
        Context context;

        public MyWebChromeClient(Context context) {
            super();
            this.context = context;
        }


    }
}
