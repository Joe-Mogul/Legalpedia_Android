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
import com.legalpedia.android.app.adapter.ArticleAdapter;
import com.legalpedia.android.app.adapter.PublicationsAdapter;
import com.legalpedia.android.app.models.Articles;
import com.legalpedia.android.app.models.ArticlesDao;
import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.Publications;
import com.legalpedia.android.app.models.PublicationsDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 11/19/16.
 */

public class PublicationsFragment extends Fragment {
    private Context ctx=null;
    private TextView emptyView;
    private RecyclerView recyclerView=null;
    private PublicationsAdapter adapter;
    private int offset=0;
    private int limit=20;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    ProgressDialog progressDialog;
    public PublicationsFragment() {
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

        ctx=rootView.getContext();
        emptyView = (TextView) rootView.findViewById(R.id.empty_view);
        progressDialog = new ProgressDialog(ctx,R.style.customDialog);
        progressDialog.setTitle("Legalpedia");
        progressDialog.setMessage("Processing...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.movies_recycler_view);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(layoutManager);
        SimpleDividerItemDecoration dividerItemDecoration = new SimpleDividerItemDecoration(recyclerView.getContext());
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter=new PublicationsAdapter(ctx);
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
                    Log.v("ArticleFragment", "Paging....");
                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            Log.v("ArticleFragment", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
                            int pager=(offset+1)*10;
                            new  FetchPublications().execute(pager);
                        }
                    }
                }
            }
        });

        new  FetchPublications().execute(offset);
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
            new  FetchPublications().execute(offset);

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


    class FetchPublications extends AsyncTask<Integer, String, List<Publications>> {

        private List<Publications> resp;


        @Override
        protected List<Publications> doInBackground(Integer ...offset) {
            //publishProgress("Sleeping..."); // Calls onProgressUpdate()

            try {
                resp= new ArrayList<Publications>();
                DaoSession daoSession = ((App)  getActivity().getApplication()).getDaoSession();


                PublicationsDao publicationsDao = daoSession.getPublicationsDao();
                QueryBuilder qb = publicationsDao.queryBuilder();
                Log.v("PublicationsFragment","Async running "+String.valueOf(offset[0]));
                resp=(List<Publications>)qb.limit(limit).offset(offset[0]).list();
                //resp=(List<Articles>)articles.loadAll();
                System.out.println("Total items "+resp.size());

            } catch (Exception e) {
                e.printStackTrace();

            }
            return resp;
        }


        @Override
        protected void onPostExecute(List<Publications> result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();
            if (result.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
            else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }
            adapter.setPublications(result);
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


