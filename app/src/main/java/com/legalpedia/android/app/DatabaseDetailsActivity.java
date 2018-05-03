package com.legalpedia.android.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.legalpedia.android.app.models.ArticlesDao;
import com.legalpedia.android.app.models.CategoryDao;
import com.legalpedia.android.app.models.CourtsDao;
import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.DictionaryDao;
import com.legalpedia.android.app.models.JudgementsDao;
import com.legalpedia.android.app.models.LawsDao;
import com.legalpedia.android.app.models.MaximsDao;
import com.legalpedia.android.app.models.PrecedenceDao;
import com.legalpedia.android.app.models.PrinciplesDao;
import com.legalpedia.android.app.models.PublicationsDao;
import com.legalpedia.android.app.models.RatioDao;
import com.legalpedia.android.app.models.RulesDao;
import com.legalpedia.android.app.models.SectionsDao;
import com.legalpedia.android.app.models.SubcategoryDao;
import com.legalpedia.android.app.models.SubjectsDao;
import com.legalpedia.android.app.models.SummaryDao;
import com.legalpedia.android.app.models.UpdateRequestDao;
import com.legalpedia.android.app.models.UserDao;

/**
 * Created by adebayoolabode on 6/17/17.
 */

public class DatabaseDetailsActivity  extends AppCompatActivity {
    private DaoSession daoSession;
    private ArticlesDao articlesDao;
    private PublicationsDao publicationsDao;
    private JudgementsDao judgementsDao;
    private SummaryDao summaryDao;
    private CategoryDao categoryDao;
    private SubcategoryDao subcategoryDao;
    private DictionaryDao dictionaryDao;
    private MaximsDao maximsDao;
    private RulesDao rulesDao;
    private SubjectsDao subjectsDao;
    private PrinciplesDao principlesDao;
    private PrecedenceDao precedenceDao;
    private LawsDao lawsDao;
    private CourtsDao courtsDao;
    private RatioDao ratioDao;
    private UserDao userdao;
    private SectionsDao sectionsDao;
    private UpdateRequestDao updateRequestDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        daoSession = ((App) getApplication()).getDaoSession();
        articlesDao=daoSession.getArticlesDao();
        publicationsDao=daoSession.getPublicationsDao();
        judgementsDao=daoSession.getJudgementsDao();
        summaryDao=daoSession.getSummaryDao();
        categoryDao = daoSession.getCategoryDao();
        subcategoryDao = daoSession.getSubcategoryDao();
        dictionaryDao = daoSession.getDictionaryDao();
        maximsDao = daoSession.getMaximsDao();
        rulesDao = daoSession.getRulesDao();
        subjectsDao = daoSession.getSubjectsDao();
        principlesDao = daoSession.getPrinciplesDao();
        precedenceDao = daoSession.getPrecedenceDao();
        lawsDao = daoSession.getLawsDao();
        courtsDao = daoSession.getCourtsDao();
        ratioDao = daoSession.getRatioDao();
        sectionsDao = daoSession.getSectionsDao();
        updateRequestDao = daoSession.getUpdateRequestDao();
        userdao = daoSession.getUserDao();

        Log.d("DatabaseDetailsActivity","articlesDao "+String.valueOf(articlesDao.count()));
        Log.d("DatabaseDetailsActivity","publicationsDao "+String.valueOf(publicationsDao.count()));
        Log.d("DatabaseDetailsActivity","judgementsDao "+String.valueOf(judgementsDao.count()));
        Log.d("DatabaseDetailsActivity","summaryDao "+String.valueOf(summaryDao.count()));
        Log.d("DatabaseDetailsActivity","categoryDao "+String.valueOf(categoryDao.count()));
        Log.d("DatabaseDetailsActivity","subcategoryDao "+String.valueOf(subcategoryDao.count()));
        Log.d("DatabaseDetailsActivity","dictionaryDao "+String.valueOf(dictionaryDao.count()));
        Log.d("DatabaseDetailsActivity","maximsDao "+String.valueOf(maximsDao.count()));
        Log.d("DatabaseDetailsActivity","rulesDao "+String.valueOf(rulesDao.count()));
        Log.d("DatabaseDetailsActivity","subjectsDao "+String.valueOf(subjectsDao.count()));
        Log.d("DatabaseDetailsActivity","principlesDao "+String.valueOf(principlesDao.count()));
        Log.d("DatabaseDetailsActivity","precedenceDao "+String.valueOf(precedenceDao.count()));
        Log.d("DatabaseDetailsActivity","lawsDao "+String.valueOf(lawsDao.count()));
        Log.d("DatabaseDetailsActivity","ratioDao "+String.valueOf(ratioDao.count()));
        Log.d("DatabaseDetailsActivity","sectionsDao "+String.valueOf(sectionsDao.count()));
        Log.d("DatabaseDetailsActivity","updateRequestDao "+String.valueOf(updateRequestDao.count()));
        Log.d("DatabaseDetailsActivity","userdao "+String.valueOf(userdao.count()));
    }
}

