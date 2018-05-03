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
import com.legalpedia.android.app.MainActivity;
import com.legalpedia.android.app.R;
import com.legalpedia.android.app.adapter.FormsPrecedenceAdapter;
import com.legalpedia.android.app.adapter.RulesListAdapter;
import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.Precedence;
import com.legalpedia.android.app.models.PrecedenceDao;
import com.legalpedia.android.app.models.PrecedenceStat;
import com.legalpedia.android.app.models.Rules;
import com.legalpedia.android.app.models.RulesDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 11/19/16.
 */


public class FormsPrecedenceFragment extends Fragment {
    private Context ctx=null;
    private RecyclerView recyclerView=null;
    private TextView emptyView;
    private FormsPrecedenceAdapter adapter;
    private  DaoSession daoSession;
    private int offset=0;
    private int limit=20;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    ProgressDialog progressDialog;
    public FormsPrecedenceFragment() {
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
        View rootView=inflater.inflate(R.layout.list_precedence, container, false);
        //progressDialog=new ProgressDialog();
        getActivity().setTitle("Forms and Precedence");
        ctx=rootView.getContext();
        progressDialog = new ProgressDialog(ctx,R.style.customDialog);
        progressDialog.setTitle("Legalpedia");
        progressDialog.setMessage("Processing...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        emptyView = (TextView) rootView.findViewById(R.id.empty_view);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.precedence_list);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(layoutManager);
        SimpleDividerItemDecoration dividerItemDecoration = new SimpleDividerItemDecoration(recyclerView.getContext());
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter=new FormsPrecedenceAdapter(ctx);
        recyclerView.setAdapter(adapter);
        /**recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                    Log.v("CasesFragment", "Paging....");
                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            Log.v("CasesFragment", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
                            int pager=(offset+1)*10;
                            new  FetchForms().execute(pager);
                        }
                    }
                }
            }
        });*/

        new  FetchForms().execute(offset);
        MainActivity.selectedTab = 4;
        return rootView;
    }

/**
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.formsandprecedence, menu);
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
    List<PrecedenceStat> resp= new ArrayList<PrecedenceStat>();
    String SQL_DISTINCT_ENAME="";
    if(searchtext.length()>0) {
         SQL_DISTINCT_ENAME = "SELECT DISTINCT " + PrecedenceDao.Properties.Category.columnName + ",count(" + PrecedenceDao.Properties.Category.columnName + ")   FROM precedence WHERE " + PrecedenceDao.Properties.Category.columnName + " LIKE '" + searchtext + "%' GROUP BY "+PrecedenceDao.Properties.Category.columnName;

    }
    else{
         SQL_DISTINCT_ENAME = "SELECT DISTINCT "+PrecedenceDao.Properties.Category.columnName+",count("+PrecedenceDao.Properties.Category.columnName+") FROM "+PrecedenceDao.TABLENAME+" GROUP BY "+PrecedenceDao.Properties.Category.columnName;

    }
    Cursor c = daoSession.getDatabase().rawQuery(SQL_DISTINCT_ENAME, null);
    try{
        if (c.moveToFirst()) {
            do {
                PrecedenceStat precedence = new PrecedenceStat(c.getString(0),c.getString(1));

                resp.add(precedence);
            } while (c.moveToNext());
        }
    } finally {
        c.close();
    }
    Log.d("Total records",String.valueOf(resp.size()));
    adapter.setPrecedence(resp);
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
            new  FetchForms().execute(offset);

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


    class FetchForms extends AsyncTask<Integer, String, List<PrecedenceStat>> {

        private List<Precedence> resp;
        private List<String> precedencelist;
        private List<PrecedenceStat> precedencetype;


        @Override
        protected List<PrecedenceStat> doInBackground(Integer ...offset) {
            //publishProgress("Sleeping..."); // Calls onProgressUpdate()
            precedencelist = new ArrayList<String>();
            try {
                resp= new ArrayList<Precedence>();
                precedencetype = new ArrayList<PrecedenceStat>();
                daoSession = ((App)  getActivity().getApplication()).getDaoSession();


                PrecedenceDao precedenceDao = daoSession.getPrecedenceDao();
                QueryBuilder qb = precedenceDao.queryBuilder();
                Log.v("RulesFragmentAsync","Async running "+String.valueOf(offset[0]));
                limit=50;

                String SQL_DISTINCT_ENAME = "SELECT DISTINCT "+PrecedenceDao.Properties.Category.columnName+",count("+PrecedenceDao.Properties.Category.columnName+") FROM "+PrecedenceDao.TABLENAME+" GROUP BY "+PrecedenceDao.Properties.Category.columnName;

                Cursor c = daoSession.getDatabase().rawQuery(SQL_DISTINCT_ENAME, null);
                try{
                    if (c.moveToFirst()) {
                        do {
                            if(!precedencelist.contains(c.getString(0))) {
                                PrecedenceStat p = new PrecedenceStat(c.getString(0), c.getString(1));
                                Log.d("Precendence",c.getString(0));
                                precedencetype.add(p);
                                precedencelist.add(c.getString(0));
                            }
                        } while (c.moveToNext());
                    }
                } finally {
                    c.close();
                }

                //resp=(List<Precedence>)qb.orderAsc(PrecedenceDao.Properties.Title).limit(limit).offset(offset[0]).list();
                //resp=(List<Articles>)articles.loadAll();
                System.out.println("Total items "+resp.size());

            } catch (Exception e) {
                e.printStackTrace();

            }
            return precedencetype;
        }


        @Override
        protected void onPostExecute(List<PrecedenceStat> result) {
            if (result.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
            else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }
            // execution of result of Long time consuming operation
            progressDialog.dismiss();

            //adapter.setPrecedence(result);
            adapter.setPrecedence(result);
            adapter.notifyDataSetChanged();

        }


        @Override
        protected void onPreExecute() {

        }


        @Override
        protected void onProgressUpdate(String... text) {


        }
    }

}


