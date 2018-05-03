package com.legalpedia.android.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.legalpedia.android.app.views.ArticleFragment;
import com.legalpedia.android.app.views.FLRFragment;
import com.legalpedia.android.app.views.ForeignResourcesFragment;
import com.legalpedia.android.app.views.NewsArticleFragment;
import com.legalpedia.android.app.views.NewsFragment;
import com.legalpedia.android.app.views.PublicationsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 11/23/16.
 */

public class NewsJournalActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private EditText searchEditText=null;
    private boolean isonline = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsjournal);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        System.out.println(viewPager);
        System.out.println(tabLayout);
        setupViewPager(viewPager,tabLayout);
    }


    private void setupViewPager(ViewPager viewPager, TabLayout tabLayout) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //adapter.addFrag(new PublicationsFragment(), "NBA Publications");
        //adapter.addFrag(new NewsFragment(), "Legal News");
        adapter.addFrag(new ForeignResourcesFragment(), "Foreign Legal Resources");
        adapter.addFrag(new NewsArticleFragment(), "Legal Articles and Reviews");

        viewPager.setAdapter(adapter);
        tabLayout.setVisibility(View.VISIBLE);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);

        MenuItem searchItem = menu.findItem(R.id.search_menu);


        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        int id =  searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        try {
            searchEditText = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
            searchEditText.setHintTextColor(getResources().getColor(R.color.aluminum));
            searchEditText.setTextColor(getResources().getColor(R.color.white));

        }catch(Exception ex){
            ex.printStackTrace();
        }

        // Detect SearchView icon clicks
        searchView.setOnSearchClickListener(new View.OnClickListener() {
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

                searchEditText.setText("Searching Dictionary and Maxims....");
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {

                return false;
            }



        });



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.length()>3) {
                    Intent intent = new Intent(NewsJournalActivity.this, SearchActivity.class);
                    intent.putExtra("query",query);
                    int position = 6;
                    intent.putExtra("position", position);
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