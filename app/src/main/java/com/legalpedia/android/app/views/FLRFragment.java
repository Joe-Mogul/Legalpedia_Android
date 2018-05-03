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
import com.legalpedia.android.app.adapter.FLRAdapter;
import com.legalpedia.android.app.models.Articles;
import com.legalpedia.android.app.models.ArticlesDao;
import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.FLR;
import com.legalpedia.android.app.models.FLRDao;
import com.legalpedia.android.app.ui.PaginationScrollListener;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 11/19/16.
 */

public class FLRFragment extends Fragment {
    private Context ctx=null;
    private RecyclerView recyclerView=null;
    private TextView emptyView;
    private FLRAdapter adapter;
    private int offset=0;
    private int limit=50;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    public FLRFragment() {
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
        recyclerView = (RecyclerView) rootView.findViewById(R.id.movies_recycler_view);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(layoutManager);
        SimpleDividerItemDecoration dividerItemDecoration = new SimpleDividerItemDecoration(recyclerView.getContext());
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter=new FLRAdapter(ctx);
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


    class FetchFromDatabase extends AsyncTask<Integer, String, List<FLR>> {

        private List<FLR> resp;
        private ProgressDialog progressDialog = new ProgressDialog(ctx,R.style.customDialog);

        @Override
        protected List<FLR> doInBackground(Integer ...offset) {
            //publishProgress("Sleeping..."); // Calls onProgressUpdate()
            resp= new ArrayList<FLR>();
            try {

                DaoSession daoSession = ((App)  getActivity().getApplication()).getDaoSession();


                FLRDao flrdao = daoSession.getFLRDao();
                QueryBuilder qb = flrdao.queryBuilder();
                Log.v("FLRFragmentAsync","Async running "+String.valueOf(offset[0]));
                resp=(List<FLR>)qb.limit(limit).offset(offset[0]).list();
                //resp=(List<Articles>)articles.loadAll();
                System.out.println("Total items "+resp.size());

            } catch (Exception e) {
                e.printStackTrace();

            }
            return resp;
        }


        @Override
        protected void onPostExecute(List<FLR> result) {
            if (result.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
            else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }
            // execution of result of Long time consuming operation
            isLoading = false;
            progressDialog.dismiss();

            adapter.setResources(result);
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


