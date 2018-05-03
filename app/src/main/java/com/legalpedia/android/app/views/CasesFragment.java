package com.legalpedia.android.app.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.legalpedia.android.app.App;
import com.legalpedia.android.app.R;
import com.legalpedia.android.app.adapter.CasesListAdapter;
import com.legalpedia.android.app.models.Cases;
import com.legalpedia.android.app.models.CasesDao;
import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.Summary;
import com.legalpedia.android.app.models.SummaryDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 11/19/16.
 */


public class CasesFragment extends Fragment {
    private Context ctx=null;
    private RecyclerView recyclerView=null;
    private TextView emptyView;
    private CasesListAdapter adapter;
    private int offset=0;
    private int limit=20;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    ProgressDialog progressDialog;
    public CasesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.base_fragment, container, false);
        //progressDialog=new ProgressDialog();
        getActivity().setTitle("Legalpedia: Cases");
        ctx=rootView.getContext();
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setTitle("Legalpedia");
        progressDialog.setMessage("Processing...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        emptyView = (TextView) rootView.findViewById(R.id.empty_view);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.movies_recycler_view);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(layoutManager);
        SimpleDividerItemDecoration dividerItemDecoration = new SimpleDividerItemDecoration(recyclerView.getContext());
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter=new CasesListAdapter(ctx);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
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
                            new  FetchCases().execute(pager);
                        }
                    }
                }
            }
        });


        return rootView;
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
            new  FetchCases().execute(offset);

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


    class FetchCases extends AsyncTask<Integer, String, List<Summary>> {

        private List<Summary> resp;


        @Override
        protected List<Summary> doInBackground(Integer ...offset) {
            //publishProgress("Sleeping..."); // Calls onProgressUpdate()

            try {
                resp= new ArrayList<Summary>();
                DaoSession daoSession = ((App)  getActivity().getApplication()).getDaoSession();


                SummaryDao casesdao = daoSession.getSummaryDao();
                QueryBuilder qb = casesdao.queryBuilder();
                Log.v("CasesFragmentAsync","Async running "+String.valueOf(offset[0]));
                resp=(List<Summary>)qb.limit(limit).offset(offset[0]).list();
                //resp=(List<Articles>)articles.loadAll();
                System.out.println("Total items "+resp.size());

            } catch (Exception e) {
                e.printStackTrace();

            }
            return resp;
        }


        @Override
        protected void onPostExecute(List<Summary> result) {
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

            adapter.setCases(result);
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


