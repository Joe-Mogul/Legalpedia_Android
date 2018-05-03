package com.legalpedia.android.app.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.legalpedia.android.app.AddNoteActivity;
import com.legalpedia.android.app.App;
import com.legalpedia.android.app.R;
import com.legalpedia.android.app.adapter.NoteListAdapter;
import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.Notes;
import com.legalpedia.android.app.models.NotesDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 4/9/17.
 */

public class PublicNotesFragment extends Fragment {

    private NoteListAdapter adapter;
    private RecyclerView recyclerView=null;
    private TextView emptyView;
    private View rootView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar progressBar;
    private SharedPreferences sharedpreferences = null;
    private Context ctx=null;
    private int offset=0;
    private int limit=20;
    private int page=0;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    public String resource="0";
    public String resourceid="0";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.notes_fragment, container, false);
        setHasOptionsMenu(false);
        emptyView = (TextView) rootView.findViewById(R.id.empty_view);
        ctx=rootView.getContext();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.download_list_view);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);


        final LinearLayoutManager layoutManager=new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(layoutManager);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_view);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


            }
        });

        adapter=new NoteListAdapter(ctx);
        recyclerView.setAdapter(adapter);
        SimpleDividerItemDecoration dividerItemDecoration = new SimpleDividerItemDecoration(recyclerView.getContext());
        recyclerView.addItemDecoration(dividerItemDecoration);


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
                    Log.v("NotesFragment", "Paging....");
                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            Log.v("LawChapterFragment", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
                            int pager=(page+1)*10;
                            new FetchFromDatabase().execute(pager);
                        }
                    }
                }
            }
        });

        new FetchFromDatabase().execute(page);
        return rootView;
    }








    class FetchFromDatabase extends AsyncTask<Integer, String, List<Notes>> {

        private List<Notes> resp;


        @Override
        protected List<Notes> doInBackground(Integer ...offset) {
            //publishProgress("Sleeping..."); // Calls onProgressUpdate()

            try {
                Log.d("Resource",resource);
                Log.d("Resourceid",resourceid);
                resp= new ArrayList<Notes>();
                DaoSession daoSession = ((App)  getActivity().getApplication()).getDaoSession();

                NotesDao notesDao =daoSession.getNotesDao();

                resp=notesDao.queryBuilder().where(NotesDao.Properties.Resource.eq(resource),NotesDao.Properties.Resourceid.eq(resourceid)).list();



            } catch (Exception e) {
                e.printStackTrace();

            }
            return resp;
        }


        @Override
        protected void onPostExecute(List<Notes> result) {
            // execution of result of Long time consuming operation
            //progressDialog.dismiss();
            if (result.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setText("There are no public notes for this resource.");
                emptyView.setVisibility(View.VISIBLE);
            }
            else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }
            progressBar.setVisibility(View.INVISIBLE);
            adapter.addNotes(result);
            adapter.notifyDataSetChanged();

        }


    }

}
