package com.legalpedia.android.app;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.legalpedia.android.app.models.ArticlesDao;
import com.legalpedia.android.app.models.CategoryDao;
import com.legalpedia.android.app.models.CourtsDao;
import com.legalpedia.android.app.models.DaoMaster;
import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.DictionaryDao;
import com.legalpedia.android.app.models.JudgementsDao;
import com.legalpedia.android.app.models.LawsDao;
import com.legalpedia.android.app.models.MaximsDao;
import com.legalpedia.android.app.models.PrecedenceDao;
import com.legalpedia.android.app.models.PrinciplesDao;
import com.legalpedia.android.app.models.Profile;
import com.legalpedia.android.app.models.ProfileDao;
import com.legalpedia.android.app.models.PublicationsDao;
import com.legalpedia.android.app.models.RatioDao;
import com.legalpedia.android.app.models.RulesDao;
import com.legalpedia.android.app.models.SectionsDao;
import com.legalpedia.android.app.models.SubcategoryDao;
import com.legalpedia.android.app.models.Subjects;
import com.legalpedia.android.app.models.SubjectsDao;
import com.legalpedia.android.app.models.Subscription;
import com.legalpedia.android.app.models.SubscriptionDao;
import com.legalpedia.android.app.models.SummaryDao;
import com.legalpedia.android.app.models.UpdateRequest;
import com.legalpedia.android.app.models.UpdateRequestDao;
import com.legalpedia.android.app.models.User;
import com.legalpedia.android.app.models.UserDao;
import com.legalpedia.android.app.services.IntegrityVerificationService;
import com.legalpedia.android.app.services.LicenseCheckReceiver;
import com.legalpedia.android.app.services.MessageService;
import com.legalpedia.android.app.services.UpdateService;
import com.legalpedia.android.app.util.ImageLoader;
import com.legalpedia.android.app.util.LGPClient;
import com.legalpedia.android.app.util.Utils;
import com.legalpedia.android.app.views.*;
import com.roughbike.bottombar.BottomBar;
import com.roughbike.bottombar.OnTabSelectListener;

import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager judgementPager;
    private ViewPager rulesPager;
    private ViewPagerAdapter judgementadapter;
    private ViewPagerAdapter rulesadapter;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private FragmentTransaction ft;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtEmail,logView;
    private SearchView searchview;
    private BottomBar bottomBar;
    private String[] activityTitles;
    public static int navItemIndex = 0;
    private Handler updatehandler = new Handler();
    private Handler searchhandler = new Handler();
    private Handler uiHandler = new Handler();
    private UpdateService updateSrv;
    private boolean updatebound;
    private Intent updateIntent=null;
    private LinearLayout progressView=null;
    private final DashboardFragment dashf=new DashboardFragment();
    private final SMIFragment smi=new SMIFragment();
    private final  LawsFragment lf= new LawsFragment();
    private final FormsPrecedenceFragment fp = new FormsPrecedenceFragment();
    private SupremeCourtFragment  scf = new SupremeCourtFragment();
    private CourtOfAppealFragment  caf = new CourtOfAppealFragment();
    private FederalHighCourtFragment  fhcf = new FederalHighCourtFragment();
    private InvestmentSecFragment  isf = new InvestmentSecFragment();
    private ShariaCourtFragment  shcf = new ShariaCourtFragment();
    private NationalIndustrialCourtFragment  nicf = new NationalIndustrialCourtFragment();
    private ElectionPetitionCourtFragment  epcf = new ElectionPetitionCourtFragment();
    private StateRulesFragment srf = new StateRulesFragment();
    private OtherRulesFragment orf = new OtherRulesFragment();
    private Profile profile;
    private User user;
    private DaoSession daoSession;
    private SubjectsDao subjectsDao;
    private UpdateRequestDao updateRequestDao;
    private UpdateRequest updateRequest;
    private String LOGINPREFERENCES="login_data";
    private SharedPreferences sharedpreferences = null;
    private SharedPreferences.Editor editor = null;
    public static int selectedTab=0;
    public static String SEARCHTEXT="";
    private EditText searchEditText=null;
    private boolean isonline = false;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpreferences=getSharedPreferences(LOGINPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        isonline =  sharedpreferences.getBoolean("social",false);
        daoSession = ((App) getApplication()).getDaoSession();
        subjectsDao = daoSession.getSubjectsDao();
        if(isonline) {
            setContentView(R.layout.activity_main_social);
            initSocialViews();
        }else {
            setContentView(R.layout.activity_main);
            initNormalViews();

        }






        if(UpdateService.isrunning){

            try {
                //if(!isServiceRunning(UpdateService.class)) {
                updateProgress();
                // }
            }
            catch(Exception ex){
                ex.printStackTrace();
            }

            uiHandler.postDelayed(new Runnable() {
                public void run() {

                    UpdateService.isrunning = false;
                    progressView.setVisibility(View.GONE);

                }
            }, 3600000L);
        }else{
            progressView.setVisibility(View.GONE);
        }


        /**updateRequestDao = daoSession.getUpdateRequestDao();
         List<UpdateRequest> updateRequests = updateRequestDao.loadAll();

         Log.d("Updates",String.valueOf(updateRequests.size()));

         for(UpdateRequest ur : updateRequests){
         Log.d("Filename",String.valueOf(ur.getFilename()));
         Log.d("Filesize",String.valueOf(ur.getFilesize()));
         Log.d("Resourceid",String.valueOf(ur.getResourceid()));

         }
         */
        String username = sharedpreferences.getString("username","");
        String password = sharedpreferences.getString("password","");

        new StartServices().execute();
        Intent intent =getIntent();
        if(intent.getBooleanExtra("requestcheck",false)){
            Snackbar integrityRequestBar = Snackbar.make(findViewById(R.id.coordinatorLayout),
                    R.string.request_integrity,Snackbar.LENGTH_LONG).setDuration(30000);
            if(Build.VERSION.SDK_INT>=23){
                integrityRequestBar.setActionTextColor(getResources().getColor(R.color.black));
                integrityRequestBar.getView().setBackgroundColor(getColor(R.color.primary_darker));
                TextView tv =(TextView) integrityRequestBar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextColor(getColor(R.color.white));
            }else {
                integrityRequestBar.setActionTextColor(getResources().getColor(R.color.black));
                integrityRequestBar.getView().setBackgroundColor(getResources().getColor(R.color.primary_darker));
                TextView tv =(TextView) integrityRequestBar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextColor(getResources().getColor(R.color.white));
            }

            integrityRequestBar.setAction("Fix Issues", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    runIntegrityVerificationFix();
                }
            });
            integrityRequestBar.show();
        }
        try {
            if (intent.getBooleanExtra("datacomplete", false)) {
                Snackbar integrityRequestBar = Snackbar.make(findViewById(R.id.coordinatorLayout),
                        R.string.data_completed, Snackbar.LENGTH_LONG).setDuration(120000);
                if (Build.VERSION.SDK_INT >= 23) {
                    integrityRequestBar.setActionTextColor(getResources().getColor(R.color.white));
                    integrityRequestBar.getView().setBackgroundColor(getColor(R.color.green));
                    TextView tv = (TextView) integrityRequestBar.getView().findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(getColor(R.color.white));
                } else {
                    integrityRequestBar.setActionTextColor(getResources().getColor(R.color.white));
                    integrityRequestBar.getView().setBackgroundColor(getResources().getColor(R.color.green));
                    TextView tv = (TextView) integrityRequestBar.getView().findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(getResources().getColor(R.color.white));
                }

                integrityRequestBar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                integrityRequestBar.show();
            }

        }
        catch(Exception ex){
            ex.printStackTrace();
        }




    }


    public void initNormalViews(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        judgementPager = (ViewPager) findViewById(R.id.viewpager);
        rulesPager = (ViewPager) findViewById(R.id.viewpager2);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        searchview = (SearchView) findViewById(R.id.search);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);


        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.nametext);
        txtEmail = (TextView) navHeader.findViewById(R.id.emailtext);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);
        navigationView.setNavigationItemSelectedListener(this);
        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.selectTabAtPosition(selectedTab);
        progressView = (LinearLayout) findViewById(R.id.progressview);
        logView = (TextView) findViewById(R.id.logview);


        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        //setUpNavigationView();

        initNavBar();
        initSearchView();
    }

    public void initSocialViews(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        judgementPager = (ViewPager) findViewById(R.id.viewpager);
        rulesPager = (ViewPager) findViewById(R.id.viewpager2);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        searchview = (SearchView) findViewById(R.id.search);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);


        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.nametext);
        txtEmail = (TextView) navHeader.findViewById(R.id.emailtext);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);
        navigationView.setNavigationItemSelectedListener(this);
        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.selectTabAtPosition(selectedTab);
        progressView = (LinearLayout) findViewById(R.id.progressview);
        logView = (TextView) findViewById(R.id.logview);


        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        //setUpNavigationView();

        initSocialNavBar();
        initSearchView();
    }

    public void initSearchView(){
        if(Build.VERSION.SDK_INT>=11) {
            searchview.setActivated(false);
        }


        searchview.onActionViewExpanded();
        searchview.setFocusable(false);
        searchview.setIconified(false);
        searchview.clearFocus();

        LinearLayout ll = (LinearLayout)searchview.getChildAt(0);
        LinearLayout ll2 = (LinearLayout)ll.getChildAt(2);
        LinearLayout ll3 = (LinearLayout)ll2.getChildAt(1);
        SearchView.SearchAutoComplete autoComplete = (SearchView.SearchAutoComplete)ll3.getChildAt(0);
// set the hint text color
        autoComplete.setHintTextColor(getResources().getColor(R.color.primary_darker));
// set the text color
        autoComplete.setTextColor(getResources().getColor(R.color.primary_dark));
        searchview.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String query = searchview.getQuery().toString();
                    //get the current bottom tab
                    int selectedtab = bottomBar.getCurrentTabPosition();
                    String position="SMI";
                    handleSearch(selectedtab,query);
                }
            }
        });

        ImageView searchButton = (ImageView) searchview.findViewById(android.support.v7.appcompat.R.id.search_button);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            searchButton.setImageDrawable(getResources().getDrawable(R.drawable.search_red_25,null));
            searchButton.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }

        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                final int  selectedtab = bottomBar.getCurrentTabPosition();
                handleSearch(selectedtab,query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final String query = newText;
                final int  selectedtab = bottomBar.getCurrentTabPosition();
                //searchhandler.removeCallbacksAndMessages(null);
                //if(query.length()>0) {
                SEARCHTEXT = query;
                handleSearch(selectedtab, query);
                /**searchhandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                //Put your call to the server here (with mQueryString)
                handleSearch(selectedtab, query);
                }
                }, 2500);*/

                //}



                return false;
            }
        });
    }


    public void searchHint(){
        int pos = bottomBar.getCurrentTabPosition();
        switch(pos){
            case 0:
                searchview.setQueryHint("Search for subject matter here");
                break;
            case 1:
                searchview.setQueryHint("Search for cases by title here");
                break;
            case 2:
                searchview.setQueryHint("Search for laws here");
                break;
            case 3:
                searchview.setQueryHint("Search for rules here");
                break;
            case 4:
                searchview.setQueryHint("Search for forms here");
                break;
            default:
                searchview.setQueryHint("Search resources with keywords");
                break;
        }

    }
    private void setItemsVisibility(Menu menu, MenuItem exception, boolean visible) {
        for (int i=0; i<menu.size(); ++i) {
            MenuItem item = menu.getItem(i);
            if (item != exception) item.setVisible(visible);
        }
    }

    public void handleSearch(int selectedtab,String query){
        String position="SMI";
        switch(selectedtab){
            case 0:
                position="SMI";
                smi.searchFragment(query);
                break;
            case 1:
                position="Judgement";
                int currentjudgement = judgementPager.getCurrentItem();
                if(currentjudgement==0){
                    scf.searchFragment(query);
                }
                if(currentjudgement==1){
                    caf.searchFragment(query);
                }
                if(currentjudgement==2){
                    fhcf.searchFragment(query);
                }
                if(currentjudgement==3){
                    isf.searchFragment(query);
                }
                if(currentjudgement==4){
                    shcf.searchFragment(query);
                }
                if(currentjudgement==5){
                    nicf.searchFragment(query);
                }
                if(currentjudgement==6){
                    epcf.searchFragment(query);
                }
                break;

            case 2:
                position="Laws of the Federation";
                lf.searchFragment(query);
                break;
            case 3:
                position="Rules";
                int currentitem = rulesPager.getCurrentItem();
                if(currentitem==0){
                    srf.searchFragment(query);
                }
                else{
                    orf.searchFragment(query);
                }
                break;
            case 4:
                position="Forms and Precedence";
                fp.searchFragment(query);
                break;
        }
    }



    public void handleFilter(int selectedtab,int filterindex){
        String position="SMI";
        switch(selectedtab){
            case 0:
                position="SMI";
                smi.filterFragment(filterindex);
                break;
            case 1:
                position="Judgement";
                int currentjudgement = judgementPager.getCurrentItem();
                if(currentjudgement==0){
                    scf.filterFragment(filterindex);
                }
                if(currentjudgement==1){
                    caf.filterFragment(filterindex);
                }
                if(currentjudgement==2){
                    fhcf.filterFragment(filterindex);
                }
                if(currentjudgement==3){
                    isf.filterFragment(filterindex);
                }
                if(currentjudgement==4){
                    shcf.filterFragment(filterindex);
                }
                if(currentjudgement==5){
                    nicf.filterFragment(filterindex);
                }
                if(currentjudgement==6){
                    epcf.filterFragment(filterindex);
                }
                break;

            case 2:
                position="Laws of the Federation";
                lf.filterFragment(filterindex);
                break;
            /**case 3:
             position="Rules";
             int currentitem = rulesPager.getCurrentItem();
             if(currentitem==0){
             srf.searchFragment(query);
             }
             else{
             orf.searchFragment(query);
             }
             break;
             case 4:
             position="Forms and Precedence";
             fp.searchFragment(query);
             break;
             */
        }
    }





    public void initNavBar(){

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {








                if (tabId == R.id.tab_smi) {
                    Log.d("MainActivity","SMI Clicked");
                    setTitle("Subject Matter Index");
                    bottomBar.setActiveTabColor(getResources().getColor(R.color.primary_dark));
                    Log.d("MainActivity","Favourites Clicked");
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container,smi);
                    ft.commit();
                    judgementPager.setVisibility(View.GONE);
                    rulesPager.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);
                    if(searchview!=null){
                        searchview.setQueryHint("Search for subject matter here");
                    }

                    //viewPager.setVisibility(View.VISIBLE);
                    //setupViewPager(viewPager);
                    //tabLayout.setVisibility(View.VISIBLE);
                    //tabLayout.setupWithViewPager(viewPager);


                }

                if (tabId == R.id.tab_cases) {
                    setTitle("Judgements");
                    Log.d("MainActivity","Judgements Clicked");
                    bottomBar.setActiveTabColor(getResources().getColor(R.color.primary_dark));

                    ft = getSupportFragmentManager().beginTransaction();
                    ft.remove(fp);
                    ft.remove(lf);
                    ft.remove(smi);
                    ft.commit();

                    rulesPager.setVisibility(View.GONE);
                    setupViewPager(judgementPager,tabLayout);

                    if(searchview!=null){
                        searchview.setQueryHint("Search for cases by title here");
                    }


                }

                if (tabId == R.id.tab_lfn) {
                    setTitle("Laws of the Federation");
                    bottomBar.setActiveTabColor(getResources().getColor(R.color.primary_dark));
                    Log.d("MainActivity","LFN Clicked");

                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container,lf);
                    ft.commit();
                    judgementPager.setVisibility(View.GONE);
                    rulesPager.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);
                    if(searchview!=null){
                        searchview.setQueryHint("Search for laws here");
                    }

                }

                if (tabId == R.id.tab_rules) {
                    setTitle("Rules");
                    Log.d("MainActivity","Rules Clicked");
                    bottomBar.setActiveTabColor(getResources().getColor(R.color.primary_dark));
                    //bottomBar.selectTabAtPosition(1);
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.remove(fp);
                    ft.remove(lf);
                    ft.remove(smi);
                    ft.commit();

                    judgementPager.setVisibility(View.GONE);
                    setupRulesViewPager(rulesPager,tabLayout);
                    if(searchview!=null){
                        searchview.setQueryHint("Search for rules here");
                    }

                }

                if (tabId == R.id.tab_precedence) {
                    setTitle("Forms and Precedence");
                    bottomBar.setActiveTabColor(getResources().getColor(R.color.primary_dark));
                    Log.d("MainActivity","Forms Clicked");


                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container,fp);
                    ft.commit();
                    judgementPager.setVisibility(View.GONE);
                    rulesPager.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);
                    if(searchview!=null){
                        searchview.setQueryHint("Search for forms here");
                    }

                }





            }
        });

    }




    public void initSocialNavBar(){

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {



                if (tabId == R.id.tab_dashboard) {
                    setTitle("Dashboard");
                    bottomBar.setActiveTabColor(getResources().getColor(R.color.primary_dark));
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container,dashf);
                    ft.commit();
                    tabLayout.setVisibility(View.GONE);
                    searchview.setVisibility(View.GONE);
                    judgementPager.setVisibility(View.GONE);
                    rulesPager.setVisibility(View.GONE);



                }




                if (tabId == R.id.tab_smi) {
                    setTitle("Subject Matter Index");
                    Log.d("MainActivity","SMI Clicked");
                    bottomBar.setActiveTabColor(getResources().getColor(R.color.primary_dark));
                    Log.d("MainActivity","Favourites Clicked");
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container,smi);
                    ft.remove(dashf);
                    ft.remove(lf);
                    ft.commit();
                    searchview.setVisibility(View.VISIBLE);
                    judgementPager.setVisibility(View.GONE);
                    rulesPager.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);

                    //viewPager.setVisibility(View.VISIBLE);
                    //setupViewPager(viewPager);
                    //tabLayout.setVisibility(View.VISIBLE);
                    //tabLayout.setupWithViewPager(viewPager);


                }

                if (tabId == R.id.tab_cases) {
                    setTitle("Judgements");
                    Log.d("MainActivity","Judgements Clicked");
                    bottomBar.setActiveTabColor(getResources().getColor(R.color.primary_dark));

                    ft = getSupportFragmentManager().beginTransaction();
                    ft.remove(dashf);
                    ft.remove(lf);
                    ft.remove(smi);
                    ft.commit();
                    searchview.setVisibility(View.VISIBLE);
                    rulesPager.setVisibility(View.GONE);
                    setupViewPager(judgementPager,tabLayout);




                }

                if (tabId == R.id.tab_lfn) {
                    setTitle("Laws of the Federation");
                    bottomBar.setActiveTabColor(getResources().getColor(R.color.primary_dark));
                    Log.d("MainActivity","LFN Clicked");

                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container,lf);
                    ft.commit();
                    searchview.setVisibility(View.VISIBLE);
                    judgementPager.setVisibility(View.GONE);
                    rulesPager.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);


                }

                if (tabId == R.id.tab_rules) {
                    setTitle("Rules");
                    Log.d("MainActivity","Rules Clicked");
                    bottomBar.setActiveTabColor(getResources().getColor(R.color.primary_dark));
                    //bottomBar.selectTabAtPosition(1);
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.remove(dashf);
                    ft.remove(lf);
                    ft.remove(smi);
                    ft.commit();
                    searchview.setVisibility(View.VISIBLE);
                    judgementPager.setVisibility(View.GONE);
                    setupRulesViewPager(rulesPager,tabLayout);


                }







            }
        });


    }

    //connect to the service
    private ServiceConnection updateConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("ContentAdapter","Service started");
            UpdateService.UpdateServiceBinder binder = (UpdateService.UpdateServiceBinder)service;
            //get service
            updateSrv = binder.getService();

            updatebound = true;


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            updatebound = false;

        }
    };


    @Override
    protected void onStop() {
        // your onStop code
        try {
            if (updateConnection != null) {
                if (!Utils.isServiceRunning(MainActivity.this, "com.legalpedia.android.zte.app.services.UpdateService")) {
                    unbindService(updateConnection);
                }

            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (updateConnection != null) {
                if (!Utils.isServiceRunning(MainActivity.this, "com.legalpedia.android.zte.app.services.UpdateService")) {
                    unbindService(updateConnection);
                }

            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

    };

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void updateProgress(){
        // Start the lengthy operation in a background thread
        try {
            if (updateIntent == null) {
                updateIntent = new Intent(this, UpdateService.class);
                bindService(updateIntent, updateConnection, Context.BIND_AUTO_CREATE);
            }

        }catch(Exception ex)
        {
            UpdateService.isrunning = false;
            progressView.setVisibility(View.GONE);
            ex.printStackTrace();

        }
        System.out.println(updateIntent);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RunUpdateUI();
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }

            }
        }).start();
        // Start the operation
        // new UpdateProgressAsync().execute();
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */


    public void RunUpdateUI(){
        while(UpdateService.isrunning){
            // Try to sleep the thread for 20 milliseconds


            try{
                Thread.sleep(2000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }


            //runOnUiThread(new Runnable(){
            //public void run(){
            // Update the progress bar
            updatehandler.post(new Runnable() {
                @Override
                public void run() {
                    String logtext = updateSrv.getProgressText();
                    logView.setText(logtext+" (Please wait)");

                    if(!UpdateService.isrunning){

                        progressView.setVisibility(View.GONE);
                    }

                }
            });
        }
    }
    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        // name, website

        daoSession = ((App) getApplication()).getDaoSession();
        String username = sharedpreferences.getString("username", "");
        try{
            UserDao users = daoSession.getUserDao();
            List<User> userlist = users.queryBuilder()
                    .where(UserDao.Properties.Username.eq(username))

                    .list();
            System.out.println("Total size " + userlist.size());
            if (userlist.size() > 0) {
                user = (User) userlist.get(0);


                ProfileDao pf = daoSession.getProfileDao();
                try {
                    List<Profile> profilelist = pf.queryBuilder()
                            .where(ProfileDao.Properties.Uid.eq(user.getId()))

                            .list();
                    profile = (Profile) profilelist.get(0);
                } catch (Exception ex) {
                    profile = pf.load(1L);
                }

                try {
                    String profilepics=profile.getProfilepics();
                    if((profilepics!="") && (profilepics!=null)) {
                        try {

                            new ImageLoader(MainActivity.this).DisplayImage(profilepics, imgProfile);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                // name, website
                txtName.setText(user.getUsername());
                txtEmail.setText(profile.getEmail());

            } else {
                txtName.setText(getString(R.string.app_name));
                txtEmail.setText("Not logged in");
            }

        }
        catch(Exception ex){
            ex.printStackTrace();
        }

    }



    private void setupViewPager(ViewPager viewPager,TabLayout tabLayout) {

        judgementadapter = new ViewPagerAdapter(getSupportFragmentManager());
        judgementadapter.addFrag(scf, "Supreme Court");
        judgementadapter.addFrag(caf, "Court of Appeal");
        judgementadapter.addFrag(fhcf, "Federal High Court");
        judgementadapter.addFrag(isf, "Investment/Securities Tribunal");
        judgementadapter.addFrag(shcf, "Sharia Court");
        judgementadapter.addFrag(nicf, "National Industrial Court");
        judgementadapter.addFrag(epcf, "Election Petition");
        viewPager.setAdapter(judgementadapter);
        viewPager.setVisibility(View.VISIBLE);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setVisibility(View.VISIBLE);
        tabLayout.setupWithViewPager(viewPager);

        MainActivity.selectedTab = 1;

    }



    private void setupRulesViewPager(ViewPager viewPager,TabLayout tabLayout) {

        rulesadapter = new ViewPagerAdapter(getSupportFragmentManager());
        rulesadapter.addFrag(srf,"State Rules");
        rulesadapter.addFrag(orf, "Other Rules");
        viewPager.setAdapter(rulesadapter);
        viewPager.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.VISIBLE);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
        MainActivity.selectedTab =3;

    }



    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Intent intent=null;
                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.action_forms:
                        drawer.closeDrawers();
                        intent = new Intent(MainActivity.this, FormsPrecedenceActivity.class);
                        startActivity(intent);

                        return false;

                    case R.id.action_dictionary:
                        drawer.closeDrawers();
                        intent = new Intent(MainActivity.this, DictionaryMaximsActivity.class);
                        startActivity(intent);
                        return false;

                    case R.id.action_journals:
                        drawer.closeDrawers();
                        intent = new Intent(MainActivity.this, NewsJournalActivity.class);
                        startActivity(intent);
                        return false;

                    case R.id.action_news:
                        drawer.closeDrawers();
                        intent = new Intent(MainActivity.this, ProfitableLawFirmActivity.class);
                        startActivity(intent);
                        return false;


                    case R.id.action_genie:
                        drawer.closeDrawers();
                        intent = new Intent(MainActivity.this, ResearchGenieActivity.class);
                        startActivity(intent);
                        return false;

                    case R.id.action_bugs:
                        drawer.closeDrawers();
                        intent = new Intent(MainActivity.this, ReportBugActivity.class);
                        startActivity(intent);
                        return false;


                    case R.id.action_notes:
                        drawer.closeDrawers();
                        intent = new Intent(MainActivity.this, NotesListActivity.class);
                        startActivity(intent);
                        return false;


                    case R.id.navItemAccount:
                        drawer.closeDrawers();
                        intent = new Intent(MainActivity.this, MyAccountActivity.class);
                        startActivity(intent);
                        return false;


                    /**case R.id.navItemProfile:
                     drawer.closeDrawers();
                     intent = new Intent(MainActivity.this, ProfileViewActivity.class);
                     startActivity(intent);
                     return false;
                     case R.id.navItemLogout:
                     drawer.closeDrawers();
                     doLogout();
                     return false;
                     */

                    case R.id.navItemSetting:
                        drawer.closeDrawers();
                        intent = new Intent(MainActivity.this, SettingsViewActivity.class);
                        startActivity(intent);
                        return false;


                    case R.id.navItemAbout:
                        drawer.closeDrawers();
                        intent = new Intent(MainActivity.this, AboutUsActivity.class);
                        startActivity(intent);
                        return false;




                    case R.id.nav_privacy_policy:
                        // launch new intent instead of loading fragment
                        //startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
                        drawer.closeDrawers();
                        return false;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);



                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }



        //super.onBackPressed();
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_forms) {
            Intent intent = new Intent(MainActivity.this, FormsPrecedenceActivity.class);
            startActivity(intent);

        }

        if (id == R.id.action_dictionary) {
            Intent intent = new Intent(MainActivity.this, DictionaryMaximsActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_journals) {
            Intent intent = new Intent(MainActivity.this, NewsJournalActivity.class);
            startActivity(intent);
        }


        if (id == R.id.action_news) {
            Intent intent = new Intent(MainActivity.this, ProfitableLawFirmActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_genie) {
            Intent intent = new Intent(MainActivity.this, ResearchGenieActivity.class);
            startActivity(intent);
        }


        if (id == R.id.action_bugs) {
            Intent intent = new Intent(MainActivity.this, ReportBugActivity.class);
            startActivity(intent);
        }


        if (id == R.id.action_notes) {
            Intent intent = new Intent(MainActivity.this, NotesListActivity.class);
            startActivity(intent);
        }

        else if (id == R.id.navItemAccount) {
            Log.d("MainActivity","My Account Link Clicked");
            Intent intent = new Intent(MainActivity.this, MyAccountActivity.class);
            startActivity(intent);
        }


        else if (id == R.id.navItemSetting) {
            Log.d("MainActivity","Settings Link Clicked");
            Intent intent = new Intent(MainActivity.this, SettingsViewActivity.class);
            //Intent intent = new Intent(MainActivity.this, DatabaseDetailsActivity.class);
            startActivity(intent);

        }
        else if (id == R.id.navItemVideos) {
            Log.d("MainActivity","Settings Link Clicked");
            //Intent intent = new Intent(MainActivity.this, SettingsViewActivity.class);
            Intent intent = new Intent(MainActivity.this, VideoListActivity.class);
            startActivity(intent);

        }

/**
 else if (id == R.id.navItemProfile) {
 Log.d("MainActivity","Profile Link Clicked");
 Intent intent = new Intent(MainActivity.this, ProfileViewActivity.class);
 startActivity(intent);
 }
 else if (id == R.id.navChangePassword) {
 Log.d("MainActivity","Change Password Clicked");
 Intent intent = new Intent(MainActivity.this, ChangePasswordActivity.class);
 startActivity(intent);
 }
 else if (id == R.id.navItemReset) {
 Log.d("MainActivity","Reset Account");
 showReset();

 }

 else if (id == R.id.navItemLogout) {
 doLogout();


 }*/
        else if (id == R.id.navItemAbout) {
            Log.d("MainActivity","Settings Link Clicked");
            Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
            startActivity(intent);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.search_menu);
        MenuItem spinnerItem = menu.findItem(R.id.spinner);

        final Menu currentmenu = menu;
        final MenuItem currentSearchItem = searchItem;
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(spinnerItem);

        /**String[] myResArray = getResources().getStringArray(R.array.filter_list);
         List<String> myResArrayList = Arrays.asList(myResArray);
         List<String> list = new ArrayList<String>(myResArrayList);

         ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.filter_layout,list){
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        // this part is needed for hiding the original view
        View view = super.getView(position, convertView, parent);
        view.setVisibility(View.GONE);

        return view;
        }
        };
         */
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.filter_list, R.layout.filter_layout);



        adapter.setDropDownViewResource(R.layout.filter_layout);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                final int  selectedtab = bottomBar.getCurrentTabPosition();
                int index=0;
                if(item.equals("Alphabetical(A-Z)")){
                    index=0;
                }
                /**else if(item.equals("Alphabetical(Z-A)")){
                 index=1;
                 }
                 else if(item.equals("Date (1900-Now)")){
                 index=2;
                 }*/
                else{
                    index = 3;
                }
                handleFilter(selectedtab,index);
                //Toast.makeText(parent.getContext(), "Android Simple Spinner Example Output..." + item, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        int id =  searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        try {
            searchEditText = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
            searchEditText.setHintTextColor(getResources().getColor(R.color.aluminum));
            searchEditText.setTextColor(getResources().getColor(R.color.white));

        }catch(Exception ex){
            ex.printStackTrace();
        }
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    setItemsVisibility(currentmenu, currentSearchItem, false);
                }else{
                    setItemsVisibility(currentmenu, currentSearchItem, true);
                }
            }
        });
        // Detect SearchView icon clicks
        /** */searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SearchView","Opened It");

            }
        });


        // Detect SearchView close
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.d("SearchView","Closed It");

                return false;
            }
        });



        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                //Toast.makeText(MainActivity.this, "onMenuItemActionExpand called", Toast.LENGTH_SHORT).show();
                if(isonline) {
                    if (bottomBar.getCurrentTabPosition() == 1) {
                        searchEditText.setText("Searching Subject Matter Index....");
                    }
                    if (bottomBar.getCurrentTabPosition() == 2) {
                        searchEditText.setText("Searching Judgements....");
                    }
                    if (bottomBar.getCurrentTabPosition() == 3) {
                        searchEditText.setText("Searching Laws of the Federation....");
                    }
                    if (bottomBar.getCurrentTabPosition() == 4) {
                        searchEditText.setText("Searching Rules....");
                    }

                }
                else{
                    if (bottomBar.getCurrentTabPosition() == 0) {
                        searchEditText.setText("Searching Subject Matter Index....");
                    }
                    if (bottomBar.getCurrentTabPosition() == 1) {
                        searchEditText.setText("Searching Judgements....");
                    }
                    if (bottomBar.getCurrentTabPosition() == 2) {
                        searchEditText.setText("Searching Laws of the Federation....");
                    }
                    if (bottomBar.getCurrentTabPosition() == 3) {
                        searchEditText.setText("Searching Rules....");
                    }
                    if (bottomBar.getCurrentTabPosition() == 4) {
                        searchEditText.setText("Searching Forms and Precedence....");
                    }
                }
                setItemsVisibility(currentmenu, currentSearchItem, false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                //Toast.makeText(MainActivity.this, "onMenutItemActionCollapse called", Toast.LENGTH_SHORT).show();
                setItemsVisibility(currentmenu, currentSearchItem, true);
                return true;
            }
        });



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.length()>3) {
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    intent.putExtra("query",query);
                    int position = bottomBar.getCurrentTabPosition();
                    if(isonline) {
                        position = position+1;
                        intent.putExtra("position", position);
                    }else{

                        intent.putExtra("position", position);
                    }
                    startActivity(intent);

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                //myAppAdapter.filter(searchQuery.toString().trim());
                //listView.invalidate();

                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        });



        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.search_menu) {


        }


        return super.onOptionsItemSelected(item);
    }


    public void showReset(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Reset Account");
        builder.setMessage("You are about to reset your account on this device.You will be able to login and use another device to access Legalpedia Plus or reload the Legalpedia database on this device.Only the Legalpedia database on this device would be reset you account and subscriptions will remain intact.Do you wish to continue?");
        builder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doReset();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }


    public void doReset(){
        new ResetAccount().execute();
    }


    class UpdateProgressAsync extends AsyncTask<String,Void,Boolean> {
        private boolean status = false;

        @Override
        protected Boolean doInBackground(String... params) {
            RunUpdateUI();
            return status;
        }
        @Override
        protected void onPostExecute(Boolean status){

        }

    }

    class ResetAccount extends AsyncTask<String,Void,JSONObject> {
        private JSONObject jobj;
        private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,R.style.customDialog);
        @Override
        protected JSONObject doInBackground(String... params) {
            jobj = LGPClient.ResetAccount();

            return jobj;
        }
        @Override
        protected void onPostExecute(JSONObject jobj){
            String message="Unable to reset account at the moment.Please try again later";
            try{
                if(jobj.getString("status").equals("ok")){
                    try {

                        File sharedPreferenceFile = new File("/data/data/"+ getPackageName()+ "/shared_prefs/");
                        File[] listFiles = sharedPreferenceFile.listFiles();
                        for (File file : listFiles) {
                            file.delete();
                        }
                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                    }
                    try {
                        new DeleteDatabase().execute();

                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                    }

                }else{
                    message=jobj.getString("message");
                    try{
                        showMessage(message);
                    }
                    catch(Exception ex){
                        showMessage(message);
                    }
                }
            }
            catch(Exception ex){
                showMessage(message);
            }


            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setTitle("Legalpedia");
            progressDialog.setMessage("Processing...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
            FragmentTransaction ft = manager.beginTransaction();
            try {

                for (Fragment f : mFragmentList) {
                    Log.d("Removing",f.toString());
                    ft.remove(f).commit();
                }
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            ft.commit();

        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        public void removeAllFragment(){
            try {

                for (Fragment f : mFragmentList) {
                    mFragmentList.remove(f);
                }
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }



    public void doLogout(){
        Log.d("Logout","Login out");
        SharedPreferences preferences = getSharedPreferences(LOGINPREFERENCES, 0);
        preferences.edit().remove("username").commit();
        preferences.edit().remove("password").commit();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }


    public void startLicenseChecker(){
        //need to correct for users whose clock is wrong possibly by sending number millisecond
        if (!Utils.isServiceRunning(MainActivity.this, "com.legalpedia.android.zte.app.services.LicenseCheckReceiver")) {

            try {
                SubscriptionDao subscriptionDao = daoSession.getSubscriptionDao();
                Subscription subscription = subscriptionDao.loadAll().get(0);
                Calendar c = Calendar.getInstance();
                try {
                    c.setTime(subscription.getExpirydate());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //c.add(Calendar.DATE, 40);
                long setTimeInMilli = c.getTimeInMillis();
                long startTimeInMilli = setTimeInMilli - System.currentTimeMillis();
                System.out.println("It will logout in " + String.valueOf(startTimeInMilli / 1000));
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, LicenseCheckReceiver.class);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 234324243/*some random id*/, intent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                        + setTimeInMilli, pendingIntent);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }




    }


    public void checkServices(){



        try{
            startLicenseChecker();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        try {
            if (!Utils.isServiceRunning(MainActivity.this, "com.legalpedia.android.app.services.UpdateService")) {
                String username = sharedpreferences.getString("username", "");
                String password = sharedpreferences.getString("password", "");
                Log.d("Service status", "Service not running,starting it");
                Intent updateIntent = new Intent(MainActivity.this, UpdateService.class);
                updateIntent.putExtra("username", username);
                updateIntent.putExtra("password", password);
                bindService(updateIntent, updateConnection, Context.BIND_AUTO_CREATE);
                startService(updateIntent);


            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

        try{
            if (!Utils.isServiceRunning(MainActivity.this, "com.legalpedia.android.app.services.MessageService")) {
                String username = sharedpreferences.getString("username","");
                String password = sharedpreferences.getString("password","");
                Log.d("Service status", "Service not running,starting it");
                Intent messageIntent = new Intent(MainActivity.this, MessageService.class);
                messageIntent.putExtra("username", username);
                messageIntent.putExtra("password", password);
                startService(messageIntent);



            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }


        try{

            if (!Utils.isServiceRunning(MainActivity.this, "com.legalpedia.android.app.services.IntegrityVerificationService")) {
                String username = sharedpreferences.getString("username","");
                String password = sharedpreferences.getString("password","");
                Log.d("Service status", "IntegrityVerificationService: Service not running,starting it");
                final Intent messageIntent = new Intent(MainActivity.this, IntegrityVerificationService.class);
                messageIntent.putExtra("username", username);
                messageIntent.putExtra("password", password);

                if(UpdateService.isrunning){

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                while(UpdateService.isrunning) {
                                    Thread.sleep(60000);
                                    if (!UpdateService.isrunning) {
                                        updatehandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                startService(messageIntent);
                                            }
                                        });
                                    }
                                }
                            }
                            catch(Exception ex){
                                ex.printStackTrace();
                            }

                        }
                    }).start();


                    uiHandler.postDelayed(new Runnable() {
                        public void run() {

                            checkServices();


                        }
                    }, 3600000L);
                }else{
                    startService(messageIntent);
                }


            }

        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void showMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setMessage(message);
        final int checkedvalue=0;
        //list of items


        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic


                    }


                });



        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    class StartServices extends AsyncTask<String,Void,Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {

            try{
                checkServices();
            }
            catch(Exception ex){

            }
            return true;
        }
    }


    class DeleteDatabase extends AsyncTask<String,Void,Boolean> {
        private boolean status;
        private DaoSession daoSession;
        private DaoMaster daoMaster;
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

        public DeleteDatabase(){
            try{
                daoSession = ((App) getApplication()).getDaoSession();
                daoMaster = ((App) getApplication()).getDaoMaster();
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
            }
            catch(Exception ex){
                ex.printStackTrace();
            }

        }


        @Override
        protected Boolean doInBackground(String... params) {




            Log.d("Deleting","Article");
            articlesDao.deleteAll();
            Log.d("Deleting","articlesDao");
            publicationsDao.deleteAll();
            Log.d("Deleting","publicationsDao");
            judgementsDao.deleteAll();
            Log.d("Deleting","judgementsDao");
            summaryDao.deleteAll();
            Log.d("Deleting","summaryDao");
            categoryDao.deleteAll();
            Log.d("Deleting","categoryDao");
            subcategoryDao.deleteAll();
            Log.d("Deleting","subcategoryDao");
            dictionaryDao.deleteAll();
            Log.d("Deleting","dictionaryDao");
            maximsDao.deleteAll();
            Log.d("Deleting","maximsDao");
            rulesDao.deleteAll();
            subjectsDao.deleteAll();
            principlesDao.deleteAll();
            precedenceDao.deleteAll();
            lawsDao.deleteAll();
            courtsDao.deleteAll();
            ratioDao.deleteAll();
            sectionsDao.deleteAll();
            updateRequestDao.deleteAll();
            userdao.deleteAll();
            Log.d("Deleting","userdao");

            daoMaster.createAllTables(daoSession.getDatabase(), true);

            return status;
        }
        @Override
        protected void onPostExecute(Boolean status) {
            try {
                try {
                    getApplicationContext().getSharedPreferences(LOGINPREFERENCES, 0).edit().clear().commit();
                    editor.clear();
                    editor.commit();
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.clear();
                    editor.commit();
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
                Utils.deleteDir(new File(Utils.getROOTDIR()));
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }

    }


    public void runIntegrityVerificationFix(){
        Log.d("IntegrityVeirification","Starting data integrity fix");
        IntegrityVerificationService.ispermittedupdate=true;
        progressDialog = new ProgressDialog(MainActivity.this,R.style.customDialog);
        progressDialog.setTitle("Legalpedia");
        progressDialog.setMessage("Downloading resources.This might take a while.");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        progressDialog.setCancelable(false);
        uiHandler.postDelayed(new Runnable() {
            public void run() {

                progressDialog.hide();


            }
        }, 1800000L);
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    //public native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    /** static {
     System.loadLibrary("native-lib");
     }
     */
}
