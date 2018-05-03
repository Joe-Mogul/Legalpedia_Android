package com.legalpedia.android.app.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
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
import com.legalpedia.android.app.adapter.OtherRulesAdapter;
import com.legalpedia.android.app.adapter.RulesListAdapter;
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


public class OtherRulesFragment extends Fragment {
    private Context ctx=null;
    private RecyclerView recyclerView=null;
    private TextView emptyView;
    private OtherRulesAdapter adapter;
    private int offset=0;
    private int limit=50;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private DaoSession daoSession;
    public OtherRulesFragment() {
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
        adapter=new OtherRulesAdapter(ctx);
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


    public void searchFragment(String searchtext){
        List<RulesStat> resp= new ArrayList<RulesStat>();
        String SQL_DISTINCT_ENAME = "SELECT DISTINCT "+RulesDao.Properties.Name.columnName+",count("+RulesDao.Properties.Name.columnName+") FROM "+RulesDao.TABLENAME+" WHERE "+RulesDao.Properties.Type.columnName+"='Other' GROUP BY "+RulesDao.Properties.Name.columnName;

        Cursor c = daoSession.getDatabase().rawQuery(SQL_DISTINCT_ENAME, null);
        try{
            if (c.moveToFirst()) {
                do {
                    RulesStat rulesStat = new RulesStat(c.getString(0),c.getString(1));
                    resp.add(rulesStat);
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }
        Log.d("Total records",String.valueOf(resp.size()));
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


        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }


    class FetchFromDatabase extends AsyncTask<Integer, String, List<RulesStat>> {

        private List<String> rulestype;
        private ProgressDialog progressDialog = new ProgressDialog(ctx,R.style.customDialog);
        private List<RulesStat> rulestat;
        @Override
        protected List<RulesStat> doInBackground(Integer ...offset) {
            //publishProgress("Sleeping..."); // Calls onProgressUpdate()

            try {
                rulestat= new ArrayList<RulesStat>();
                rulestype =  new ArrayList<String>();
                RulesDao rulesdao = daoSession.getRulesDao();
                String SQL_DISTINCT_ENAME = "SELECT DISTINCT "+RulesDao.Properties.Name.columnName+",count("+RulesDao.Properties.Name.columnName+") FROM "+RulesDao.TABLENAME+" WHERE "+RulesDao.Properties.Type.columnName+"='Other' GROUP BY "+RulesDao.Properties.Name.columnName;
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


