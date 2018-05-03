package com.legalpedia.android.app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.Sections;
import com.legalpedia.android.app.models.SectionsDao;
import com.legalpedia.android.app.util.ResourceUtil;
import com.legalpedia.android.app.views.JudgementFullDetailFragment;
import com.legalpedia.android.app.views.LawFullDetailFragment;
import com.legalpedia.android.app.views.MyNotesFragment;
import com.legalpedia.android.app.views.PublicNotesFragment;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 5/12/17.
 */

public class JudgementFullDetailViewActivity extends AppCompatActivity  {
    private String lawid;
    private TextView titleView;
    private TextView bodyView;
    private Toolbar toolbar;
    private DaoSession daoSession;
    private ViewPager viewPager;
    private JudgementFullDetailFragment fulljudgement;
    private MyNotesFragment mynotes;
    private PublicNotesFragment pubnote;
    private TabLayout tabLayout;
    private String judgementid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judgement_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        Intent intent = getIntent();
        judgementid=intent.getStringExtra("judgementid");
        System.out.println("Judgement ID "+judgementid);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }


    private void setupViewPager(ViewPager viewPager) {
        fulljudgement=new JudgementFullDetailFragment();
        mynotes=new MyNotesFragment();
        pubnote=new PublicNotesFragment();
        mynotes.resource="2";
        mynotes.resourceid=judgementid;
        Log.d("Fetching Note",mynotes.resource +" "+mynotes.resourceid);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(fulljudgement, "Judgement");
        adapter.addFrag(mynotes, "My Notes");
        adapter.addFrag(pubnote, "Public Notes");
        viewPager.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //    finish();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();

        }

        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.edit) {

            Intent intent=new Intent(JudgementFullDetailViewActivity.this,AddNoteActivity.class);
            startActivity(intent);
        }
        return true;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
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

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
