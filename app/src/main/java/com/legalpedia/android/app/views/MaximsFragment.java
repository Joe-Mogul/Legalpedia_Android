package com.legalpedia.android.app.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.legalpedia.android.app.adapter.MaximsAdapter;
import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.Dictionary;
import com.legalpedia.android.app.models.DictionaryDao;
import com.legalpedia.android.app.models.Maxims;
import com.legalpedia.android.app.models.MaximsDao;
import com.legalpedia.android.app.ui.PaginationScrollListener;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 11/19/16.
 */

public class MaximsFragment extends Fragment {
    private Context ctx=null;
    private RecyclerView recyclerView=null;
    private TextView emptyView;
    private MaximsAdapter adapter;
    private int offset=0;
    private int limit=50;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private List<String> maximstitles=new ArrayList<String>();
    public MaximsFragment() {
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
        View rootView=inflater.inflate(R.layout.list_dictionary, container, false);
        //progressDialog=new ProgressDialog();
        ctx=rootView.getContext();
        getActivity().setTitle("Maxims");
        emptyView = (TextView) rootView.findViewById(R.id.empty_view);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.dictionary_list);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(layoutManager);
        SimpleDividerItemDecoration dividerItemDecoration = new SimpleDividerItemDecoration(recyclerView.getContext());
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter=new MaximsAdapter(ctx);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
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
        });



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

        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }


    class FetchFromDatabase extends AsyncTask<Integer, String, List<Maxims>> {

        private List<Maxims> resp;
        private ProgressDialog progressDialog = new ProgressDialog(ctx,R.style.customDialog);

        @Override
        protected List<Maxims> doInBackground(Integer... offset) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()

            try {
                resp= new ArrayList<Maxims>();
                DaoSession daoSession = ((App)  getActivity().getApplication()).getDaoSession();
                MaximsDao maxims = daoSession.getMaximsDao();
                List<Maxims> resp1=maxims.loadAll();
                Log.v("Maxims","Total Maxims "+String.valueOf(resp1.size()));
                QueryBuilder qb = maxims.queryBuilder();
                Log.v("ArticleFragmentAsync","Async running "+String.valueOf(offset[0]));
                List<Maxims> maximslist=(List<Maxims>)qb.orderAsc(MaximsDao.Properties.Title).list();
                for(Maxims c:maximslist) {
                    if (!maximstitles.contains(c.getTitle())) {
                        resp.add(c);
                        maximstitles.add(c.getTitle());
                    }

                }




            } catch (Exception e) {
                e.printStackTrace();

            }
            return resp;
        }


        @Override
        protected void onPostExecute(List<Maxims> result) {
            isLoading = false;
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

            adapter.setMaxims(result);
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


