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

import com.legalpedia.android.app.App;
import com.legalpedia.android.app.DatabaseDetailsActivity;
import com.legalpedia.android.app.MainActivity;
import com.legalpedia.android.app.R;
import com.legalpedia.android.app.adapter.ArticleAdapter;
import com.legalpedia.android.app.adapter.LFNAdapter;
import com.legalpedia.android.app.models.Articles;
import com.legalpedia.android.app.models.ArticlesDao;
import com.legalpedia.android.app.models.DaoMaster;
import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.Laws;
import com.legalpedia.android.app.models.LawsDao;
import com.legalpedia.android.app.models.Subjects;
import com.legalpedia.android.app.models.SubjectsDao;
import com.legalpedia.android.app.ui.PaginationScrollListener;

import org.greenrobot.greendao.query.QueryBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by adebayoolabode on 11/19/16.
 */

public class LawsFragment extends Fragment {
    private Context ctx=null;
    private RecyclerView recyclerView=null;
    private TextView emptyView;
    private LFNAdapter adapter;
    private int offset=0;
    private int limit=50;
    private int totalcount=625;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private DaoSession daoSession;
    public LawsFragment() {
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
        View rootView=inflater.inflate(R.layout.list_laws, container, false);
        //progressDialog=new ProgressDialog();
        getActivity().setTitle("Laws of the Federation");
        daoSession = ((App)  getActivity().getApplication()).getDaoSession();
        ctx=rootView.getContext();
        emptyView = (TextView) rootView.findViewById(R.id.empty_view);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.law_list);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(layoutManager);
        SimpleDividerItemDecoration dividerItemDecoration = new SimpleDividerItemDecoration(recyclerView.getContext());
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter=new LFNAdapter(ctx);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                offset += 1;
                int pager=(offset)*limit;

                new FetchLaws().execute(pager);
            }

            @Override
            public int getTotalPageCount() {
                return totalcount;
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

        //new  FetchLaws().execute(offset);
        MainActivity.selectedTab = 2;
        return rootView;
    }


    /**@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.lfn, menu);
        MenuItem searchItem = menu.findItem(R.id.law_search);



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
        if (id == R.id.database) {

            Intent intent=new Intent(ctx,DatabaseDetailsActivity.class);
            startActivity(intent);
        }
        if (id == R.id.add) {

            Intent intent=new Intent(ctx,DatabaseDetailsActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

*/


    public void filterFragment(int index){
        new FilterFromDatabase().execute(offset,index);

    }


    public void searchFragment(String searchtext){
        List<Laws> resp= new ArrayList<Laws>();
        String SQL_DISTINCT_ENAME = "SELECT "+ LawsDao.Properties.Id.columnName+","+LawsDao.Properties.Title.columnName+","+LawsDao.Properties.Date.columnName+"  FROM laws WHERE "+LawsDao.Properties.Title.columnName+" LIKE '%"+searchtext+"%' ORDER BY "+LawsDao.Properties.Title.columnName;

        Cursor c = daoSession.getDatabase().rawQuery(SQL_DISTINCT_ENAME, null);

        try{
            if (c.moveToFirst()) {
                do {
                    Laws laws = new Laws();
                    laws.setId(c.getLong(0));
                    laws.setSid(c.getString(0));
                    laws.setTitle(c.getString(1));
                     try {
                        laws.setDate(new SimpleDateFormat("MMMMM dd, yyyy").parse(c.getString(2)));
                    }
                    catch(Exception ex){
                        laws.setDate(new Date());
                    }
                    resp.add(laws);
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }
        Log.d("Total records",String.valueOf(resp.size()));
        isLoading = false;
        isLastPage = true;
        adapter.setLaws(resp);
        adapter.notifyDataSetChanged();
    }



    @Override
    public void setUserVisibleHint(boolean visible)
    {
        super.setUserVisibleHint(visible);
        if (visible && isResumed())
        {
            isLoading = false;
            isLastPage = false;
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
            offset = 0;
            isLoading = false;
            isLastPage = false;
            new  FetchLaws().execute(offset);

        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }


    class FetchLaws extends AsyncTask<Integer, String, List<Laws>> {

        private List<Laws> resp;
        private ProgressDialog progressDialog = new ProgressDialog(ctx,R.style.customDialog);

        @Override
        protected List<Laws> doInBackground(Integer ...offset) {
            //publishProgress("Sleeping..."); // Calls onProgressUpdate()

            try {
                resp= new ArrayList<Laws>();
                daoSession = ((App)  getActivity().getApplication()).getDaoSession();


                LawsDao lawlist = daoSession.getLawsDao();
                long count = lawlist.count();
                totalcount = Integer.parseInt(String.valueOf(lawlist.count()));
                System.out.println("Total Laws "+String.valueOf(count));
                QueryBuilder qb = lawlist.queryBuilder();
                Log.v("LawsFragmentAsync","Async running "+String.valueOf(offset[0]));
                if(offset[0]>=totalcount){
                    isLastPage=true;
                }
                resp=(List<Laws>)qb.orderAsc(LawsDao.Properties.Title).offset(offset[0]).limit(limit).list();
                //resp=(List<Articles>)articles.loadAll();
                System.out.println("Total items "+resp.size());

            } catch (Exception e) {
                e.printStackTrace();

            }
            return resp;
        }


        @Override
        protected void onPostExecute(List<Laws> result) {
            isLoading = false;
            if (adapter.isEmpty() && result.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
            else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }
            // execution of result of Long time consuming operation
            progressDialog.dismiss();

            adapter.addLaws(result);
            adapter.notifyDataSetChanged();

        }


        @Override
        protected void onPreExecute() {

            progressDialog.setTitle("Legalpedia");
            progressDialog.setMessage("Processing...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }


        @Override
        protected void onProgressUpdate(String... text) {


        }
    }



    class FilterFromDatabase extends AsyncTask<Integer, String, List<Laws>> {

        private List<Laws> resp;
        private ProgressDialog progressDialog = new ProgressDialog(ctx,R.style.customDialog);

        @Override
        protected List<Laws> doInBackground(Integer ...offset) {
            //publishProgress("Sleeping..."); // Calls onProgressUpdate()

            try {
                resp= new ArrayList<Laws>();
                daoSession = ((App)  getActivity().getApplication()).getDaoSession();


                LawsDao lawlist = daoSession.getLawsDao();
                QueryBuilder qb = lawlist.queryBuilder();
                Log.v("LawsFragmentAsync","Async running "+String.valueOf(offset[0]));
                limit = 50;
                int index= offset[1];
                if(index==0) {
                    resp = (List<Laws>) qb.orderAsc(LawsDao.Properties.Title).offset(offset[0]).limit(limit).list();
                }
                else{
                    resp = (List<Laws>) qb.orderAsc(LawsDao.Properties.Date).offset(offset[0]).limit(limit).list();
                }
                //resp=(List<Articles>)articles.loadAll();
                System.out.println("Total items "+resp.size());

            } catch (Exception e) {
                e.printStackTrace();

            }
            return resp;
        }


        @Override
        protected void onPostExecute(List<Laws> result) {
            // execution of result of Long time consuming operation
            isLoading = false;
            progressDialog.dismiss();

            adapter.addLaws(result);
            adapter.notifyDataSetChanged();

        }


        @Override
        protected void onPreExecute() {
            progressDialog.setTitle("Legalpedia");
            progressDialog.setMessage("Processing...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }


        @Override
        protected void onProgressUpdate(String... text) {


        }
    }


}


