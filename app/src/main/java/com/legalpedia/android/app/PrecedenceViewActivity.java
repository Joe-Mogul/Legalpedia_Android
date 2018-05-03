package com.legalpedia.android.app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.legalpedia.android.app.views.MyNotesFragment;
import com.legalpedia.android.app.views.PrecedenceDetailFragment;
import com.legalpedia.android.app.views.PublicNotesFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 5/12/17.
 */

public class PrecedenceViewActivity extends AppCompatActivity {
    private String sectionid;
    private TextView titleView;
    private TextView bodyView;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private PrecedenceDetailFragment precedencedetail;
    private MyNotesFragment mynotes;
    private PublicNotesFragment pubnote;
    private TabLayout tabLayout;
    private String precedenceid;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawdetail_main);
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
        precedenceid = intent.getStringExtra("precedenceid");
        category = intent.getStringExtra("category");
        Log.d("Law ID ",precedenceid);
        //SpannableString str = new SpannableString("Where the commissioner.");
        //str.setSpan(new BackgroundColorSpan(Color.YELLOW), 0, 11, 0);
        //bodyView.setText(str);
        //bodyView.setOnTouchListener(new CustomTouchListener());

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home) {
            // click on 'up' button in the action bar, handle it here
            Intent intent= new Intent(this,PrecedenceListActivity.class);
            intent.putExtra("category",category);
            startActivity(intent);

            return true;


        }


        return super.onOptionsItemSelected(item);
    }


    private void setupViewPager(ViewPager viewPager) {
        precedencedetail=new PrecedenceDetailFragment();
        mynotes=new MyNotesFragment();
        pubnote=new PublicNotesFragment();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(precedencedetail, "Precedence");
        adapter.addFrag(mynotes, "My Notes");
        adapter.addFrag(pubnote, "Public Notes");
        viewPager.setAdapter(adapter);
    }



    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        Layout layout = ((TextView) v).getLayout();
        int x = (int)event.getX();
        int y = (int)event.getY();
        if (layout!=null){
            int line = layout.getLineForVertical(y);
            int characterOffset = layout.getOffsetForHorizontal(line, x);
            Log.i("index", ""+characterOffset);
        }
        return true;


    }

    public class CustomTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    ((TextView) view).setTextColor(0xFFFFFFFF); // white
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    ((TextView) view).setTextColor(Color.parseColor("#4a4a4a")); // lightblack
                    break;
            }
            return false;
        }
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
