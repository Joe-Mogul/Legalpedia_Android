package com.legalpedia.android.app.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
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
import com.legalpedia.android.app.LawDetailViewActivity;
import com.legalpedia.android.app.PrecedenceViewActivity;
import com.legalpedia.android.app.R;
import com.legalpedia.android.app.adapter.NoteListAdapter;
import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.Precedence;
import com.legalpedia.android.app.models.PrecedenceDao;
import com.legalpedia.android.app.models.Sections;
import com.legalpedia.android.app.models.SectionsDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by adebayoolabode on 4/9/17.
 */

public class PrecedenceDetailFragment extends Fragment {

    private NoteListAdapter adapter;
    private String sectionid;
    private TextView titleView;
    private TextView bodyView;
    private View rootView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedpreferences = null;
    private Context ctx=null;
    private int offset=0;
    private int limit=20;
    private int page=0;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    public String precedenceid;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.precedencedetail_fragment, container, false);
        ctx=rootView.getContext();
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setTitle("Legalpedia");
        progressDialog.setMessage("Processing...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        PrecedenceViewActivity activity = (PrecedenceViewActivity)ctx;
        Intent intent=activity.getIntent();
        precedenceid=intent.getStringExtra("precedenceid");
         Log.d("Law ID ",precedenceid);
         DaoSession daoSession = ((App)  activity.getApplication()).getDaoSession();

         PrecedenceDao precedenceDao =daoSession.getPrecedenceDao();
         QueryBuilder qb = precedenceDao.queryBuilder();
         List resp=qb.where(PrecedenceDao.Properties.Id.eq(precedenceid)).list();
         Precedence precedence=  (Precedence)resp.get(0);
        getActivity().setTitle(precedence.getTitle());
         //titleView = (TextView)rootView.findViewById(R.id.title);
        //titleView.setText(section.getTitle());
         bodyView = (TextView)rootView.findViewById(R.id.body);
        String html = "\n\n\n"+precedence.getContent();
        String replaceHtml = html
                .replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">");

        Spanned span = Html.fromHtml(replaceHtml);
        SpannableString bodytext = new SpannableString(span);
         bodyView.setText(bodytext);

        progressDialog.dismiss();


        return rootView;
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.notelist, menu);
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
        if (id == R.id.edit) {

            Intent intent=new Intent(ctx,AddNoteActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }





}

