package com.legalpedia.android.app.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.legalpedia.android.app.AddNoteActivity;
import com.legalpedia.android.app.App;
import com.legalpedia.android.app.R;
import com.legalpedia.android.app.adapter.RulesListAdapter;
import com.legalpedia.android.app.adapter.StateRulesAdapter;
import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.Rules;
import com.legalpedia.android.app.models.RulesDao;
import com.legalpedia.android.app.models.RulesStat;
import com.legalpedia.android.app.ui.PaginationScrollListener;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 11/19/16.
 */


public class StateRulesFragment extends Fragment {
    private Context ctx=null;
    private RecyclerView recyclerView=null;
    private TextView emptyView;
    private StateRulesAdapter adapter;
    private int offset=0;
    private int limit=50;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private DaoSession daoSession;
    public StateRulesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.list_rules, container, false);
        //progressDialog=new ProgressDialog();
        getActivity().setTitle("Rules of Court");
        ctx=rootView.getContext();
        daoSession = ((App)  getActivity().getApplication()).getDaoSession();
        emptyView = (TextView) rootView.findViewById(R.id.empty_view);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rules_list);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(layoutManager);
        SimpleDividerItemDecoration dividerItemDecoration = new SimpleDividerItemDecoration(recyclerView.getContext());
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter=new StateRulesAdapter(ctx);
        recyclerView.setAdapter(adapter);
        /**recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                offset += 1;
                int pager=(offset)*limit;

                new  FetchFromDatabase().execute(pager);
            }

            @Override
            public int getTotalPageCount() {
                return 10000;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });*/

        return rootView;
    }

/**
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.staterules, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);



        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //*** setOnQueryTextFocusChangeListener ***
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                //myAppAdapter.filter(searchQuery.toString().trim());
                //listView.invalidate();
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.add) {

            Intent intent=new Intent(ctx,AddNoteActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

*/

public void searchFragment(String searchtext){
    List<RulesStat> resp= new ArrayList<RulesStat>();
    String SQL_DISTINCT_ENAME = "SELECT DISTINCT "+RulesDao.Properties.Name.columnName+",count("+RulesDao.Properties.Name.columnName+") FROM "+RulesDao.TABLENAME+" WHERE "+RulesDao.Properties.Type.columnName+"='State' GROUP BY "+RulesDao.Properties.Name.columnName;

    Cursor c = daoSession.getDatabase().rawQuery(SQL_DISTINCT_ENAME, null);
    try{
        if (c.moveToFirst()) {
            do {
                RulesStat rulesStat = new RulesStat(c.getString(0),c.getString(0));
                resp.add(rulesStat);
            } while (c.moveToNext());
        }
    } finally {
        c.close();
    }

    adapter.setRulesStat(resp);
    adapter.notifyDataSetChanged();
}



    @Override
    public void setUserVisibleHint(boolean visible)
    {
        super.setUserVisibleHint(visible);
        if (visible && isResumed())
        {
            //Only manually call onResume if fragment is already visible
            //Otherwise allow natural fragment lifecycle to call onResume
            onResume();
        }
    }



    @Override
    public void onResume()
    {
        super.onResume();
        if (!getUserVisibleHint())
        {
            return;
        }

        //INSERT CUSTOM CODE HERE
        try {
            new  FetchFromDatabase().execute(offset);

           /* if(Utils.hasNetwork((Context)getActivity())) {
                //progressBar.setVisibility(View.VISIBLE);

            }
            else{

            }
            */
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }


    class FetchFromDatabase extends AsyncTask<Integer, String, List<RulesStat>> {

        private List<Rules> resp;
        private List<String> rulestype;
        private List<RulesStat> rulestat;
        private ProgressDialog progressDialog = new ProgressDialog(ctx,R.style.customDialog);

        @Override
        protected List<RulesStat> doInBackground(Integer ...offset) {
            //publishProgress("Sleeping..."); // Calls onProgressUpdate()

            try {
                rulestat= new ArrayList<RulesStat>();
                rulestype =  new ArrayList<String>();

                RulesDao rulesdao = daoSession.getRulesDao();
               String SQL_DISTINCT_ENAME = "SELECT DISTINCT "+RulesDao.Properties.Name.columnName+",count("+RulesDao.Properties.Name.columnName+") FROM "+RulesDao.TABLENAME+" WHERE "+RulesDao.Properties.Type.columnName+"='State' GROUP BY "+RulesDao.Properties.Name.columnName;

                Cursor c = daoSession.getDatabase().rawQuery(SQL_DISTINCT_ENAME, null);
                try{
                    if (c.moveToFirst()) {
                        do {
                            RulesStat rules = new RulesStat(c.getString(0),c.getString(1));
                            rulestat.add(rules);
                        } while (c.moveToNext());
                    }
                } finally {
                    c.close();
                }


            } catch (Exception e) {
                e.printStackTrace();

            }
            return rulestat;
        }


        @Override
        protected void onPostExecute(List<RulesStat> result) {
            // execution of result of Long time consuming operation
            if (result.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
            else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }
            progressDialog.dismiss();

            adapter.setRulesStat(result);
            adapter.notifyDataSetChanged();

        }


        @Override
        protected void onPreExecute() {
            progressDialog.setTitle("Legalpedia");
            progressDialog.setMessage("Processing...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }


        @Override
        protected void onProgressUpdate(String... text) {


        }
    }

}


