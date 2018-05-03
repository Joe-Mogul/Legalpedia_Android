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
import com.legalpedia.android.app.adapter.DictionaryAdapter;
import com.legalpedia.android.app.models.Articles;
import com.legalpedia.android.app.models.ArticlesDao;
import com.legalpedia.android.app.models.Dictionary;
import com.legalpedia.android.app.models.DictionaryDao;
import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.ui.PaginationScrollListener;
import com.legalpedia.android.app.util.Utils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 11/19/16.
 */

public class DictionaryFragment extends Fragment {
    private Context ctx=null;
    private RecyclerView recyclerView=null;
    private TextView emptyView;
    private DictionaryAdapter adapter;
    private int offset=0;
    private int limit=50;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private List<String> dicttitles=new ArrayList<String>();
    public DictionaryFragment() {
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
        View rootView=inflater.inflate(R.layout.list_dictionary, container, false);
        //progressDialog=new ProgressDialog();
        ctx=rootView.getContext();
        getActivity().setTitle("Dictionary");
        emptyView = (TextView) rootView.findViewById(R.id.empty_view);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.dictionary_list);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(layoutManager);
        SimpleDividerItemDecoration dividerItemDecoration = new SimpleDividerItemDecoration(recyclerView.getContext());
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter=new DictionaryAdapter(ctx);
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


    class FetchFromDatabase extends AsyncTask<Integer, String, List<Dictionary>> {

        private List<Dictionary> resp;
        private ProgressDialog progressDialog = new ProgressDialog(ctx,R.style.customDialog);

        @Override
        protected List<Dictionary> doInBackground(Integer... offset) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()

            try {
                resp= new ArrayList<Dictionary>();
                DaoSession daoSession = ((App)  getActivity().getApplication()).getDaoSession();
                /**DictionaryDao dictionary = daoSession.getDictionaryDao();
                 List<Dictionary> resp1 = dictionary.loadAll();
                 Log.v("Dictionary","Total Dictionary "+String.valueOf(resp1.size()));
                 QueryBuilder qb = dictionary.queryBuilder();
                 qb.LOG_SQL=true;
                 Log.v("ArticleFragmentAsync","Async running "+String.valueOf(offset[0]));

                 resp=(List<Dictionary>)qb.where(
                 new WhereCondition.StringCondition("TITLE IN (SELECT DISTINCT TITLE FROM DICTIONARY)")
                 ).limit(limit).offset(offset[0]).orderAsc(DictionaryDao.Properties.Title).list();
                 */
                //resp=dictionary.loadAll();
                String SQL_DISTINCT_ENAME = "SELECT "+DictionaryDao.Properties.Title.columnName+","+DictionaryDao.Properties.Id.columnName+","+DictionaryDao.Properties.Content.columnName+" FROM "+DictionaryDao.TABLENAME+" ORDER BY "+DictionaryDao.Properties.Title.columnName;
                Cursor c = daoSession.getDatabase().rawQuery(SQL_DISTINCT_ENAME, null);
                try{
                    if (c.moveToFirst()) {
                        do {
                            Dictionary dc = new Dictionary(c.getLong(1),c.getString(0),c.getString(2));
                            if(!dicttitles.contains(c.getString(0))) {
                                resp.add(dc);
                                dicttitles.add(c.getString(0));
                            }
                        } while (c.moveToNext());
                    }
                } finally {
                    c.close();
                }




            } catch (Exception e) {
                e.printStackTrace();

            }
            return resp;
        }


        @Override
        protected void onPostExecute(List<Dictionary> result) {
            isLoading = false;
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

            adapter.setDictionary(result);
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


