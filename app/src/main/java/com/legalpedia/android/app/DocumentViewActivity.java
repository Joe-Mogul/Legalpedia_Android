package com.legalpedia.android.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import com.legalpedia.android.app.models.Articles;
import com.legalpedia.android.app.models.ArticlesDao;
import com.legalpedia.android.app.models.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import static android.R.attr.offset;

/**
 * Created by adebayoolabode on 11/23/16.
 */

public class DocumentViewActivity  extends AppCompatActivity {
    private WebView documentview;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_view);
        setupToolbar();
        Intent intent = getIntent();
        long _id = intent.getLongExtra("_id",0);
        documentview= (WebView)findViewById(R.id.document);

        DaoSession daoSession = ((App)  DocumentViewActivity.this.getApplication()).getDaoSession();


        ArticlesDao articles = daoSession.getArticlesDao();
        QueryBuilder qb = articles.queryBuilder();
        List<Articles> articlelist = qb.where(ArticlesDao.Properties.Id.eq(_id)).list();
        if(articlelist.size()>0) {
            Articles data = articlelist.get(0);
            documentview.loadData(data.getContent(), "text/html; charset=utf-8", "utf-8");
        }



    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }


    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.app_name));
        }
    }
}
