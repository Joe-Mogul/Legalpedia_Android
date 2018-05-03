package com.legalpedia.android.app;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.legalpedia.android.app.adapter.MyAccountAdapter;
import com.legalpedia.android.app.models.MenuItem;
import com.legalpedia.android.app.views.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class MyAccountActivity extends AppCompatActivity {
    private RecyclerView recyclerView=null;
    private MyAccountAdapter adapter;
    private List<MenuItem> menuItemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("My Account");
        recyclerView = (RecyclerView)findViewById(R.id.account_list);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        SimpleDividerItemDecoration dividerItemDecoration = new SimpleDividerItemDecoration(recyclerView.getContext());
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter=new MyAccountAdapter(this);
        menuItemList = getMenuList();
        Log.d("MyAccount",String.valueOf(menuItemList.size()));
        adapter.setMenu(menuItemList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();




    }

    public List<MenuItem> getMenuList(){
        List menuItemList = new ArrayList<MenuItem>();
        menuItemList.add(new MenuItem(0,R.drawable.ic_nav_profile,"Profile"));
        menuItemList.add(new MenuItem(1,R.drawable.ic_nav_password,"Change Password"));
        menuItemList.add(new MenuItem(2,R.drawable.icon_reset,"Reset my account"));
        menuItemList.add(new MenuItem(3,R.drawable.ic_nav_logout,"Logout"));

        return menuItemList;
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

    }
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home) {
            onBackPressed();

        }

        return true;
    }

}
