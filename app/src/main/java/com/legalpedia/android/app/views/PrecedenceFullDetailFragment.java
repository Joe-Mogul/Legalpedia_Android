package com.legalpedia.android.app.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
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
import com.legalpedia.android.app.LawFullDetailViewActivity;
import com.legalpedia.android.app.PrecedenceFullDetailViewActivity;
import com.legalpedia.android.app.R;
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

public class PrecedenceFullDetailFragment extends Fragment {

    private String category;
    private TextView titleView;
    private TextView bodyView;
    private View rootView;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedpreferences = null;
    private Context ctx=null;
    private DaoSession daoSession;
    private PrecedenceDao precedenceDao;
    private Precedence precedence;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.lawfulldetail_fragment, container, false);
        ctx=rootView.getContext();
        PrecedenceFullDetailViewActivity activity = (PrecedenceFullDetailViewActivity)ctx;
        Intent intent=activity.getIntent();
        category=intent.getStringExtra("category");
        Log.d("Precedence",category);
        progressDialog = new ProgressDialog(ctx,R.style.customDialog);
        progressDialog.setTitle("Legalpedia");
        progressDialog.setMessage("Processing...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        bodyView = (TextView)rootView.findViewById(R.id.body);

        daoSession = ((App)  activity.getApplication()).getDaoSession();

        new UpdatePrecedence().execute();

        return rootView;
    }


    public SpannableStringBuilder getFullPrecedence(String category){
        String fulllaw="";

        PrecedenceDao precedenceDao =daoSession.getPrecedenceDao();
        QueryBuilder qb = precedenceDao.queryBuilder();
        List<Precedence> resp=qb.where(PrecedenceDao.Properties.Category.eq(category)).orderAsc(PrecedenceDao.Properties.Title).list();
        //List<Sections> resp=qb.where(SectionsDao.Properties.Lawid.eq(lawid)).orderAsc(SectionsDao.Properties.Weightindex).list();
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder ();

        for(Precedence precedence : resp){
            String titleText = "\n\t\t\t"+precedence.getTitle()+"\n\n";
            String bodyText = "\t\t\t"+precedence.getContent()+"\n";
            SpannableString titleString = new SpannableString(titleText);
            titleString.setSpan(new StyleSpan(Typeface.BOLD), 0, titleText.length(), 0);
            titleString.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
            titleString.setSpan(new RelativeSizeSpan(1.4f), 0, titleText.length(), 0);
            String replaceHtml = bodyText
                    .replaceAll("&lt;", "<")
                    .replaceAll("&gt;", ">")
                    .replaceAll("<br>", "\n")
                    .replaceAll("</p>", "\n\n");

            Spanned span = Html.fromHtml(replaceHtml);

            SpannableString bodyString = new SpannableString(span);
            bodyString.setSpan(new BackgroundColorSpan(0x000000), 0, span.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            //bodyString.setSpan(new RelativeSizeSpan(1f), 0, bodyText.length(), 0);
            stringBuilder.append(titleString);
            stringBuilder.append(bodyString);

            /**fulllaw = fulllaw+ section.getTitle();
            fulllaw = fulllaw + "\t\t\n";
            fulllaw = fulllaw + section.getBody()+"\n\n";
             */
        }

        return stringBuilder;
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


    class UpdatePrecedence  extends AsyncTask<String, String, SpannableStringBuilder> {

        private SpannableStringBuilder bodyString;

        @Override
        protected SpannableStringBuilder doInBackground(String... offset) {
            precedenceDao = daoSession.getPrecedenceDao();
            QueryBuilder qb = precedenceDao.queryBuilder();
            List<Precedence> resp=qb.where(PrecedenceDao.Properties.Category.eq(category)).list();
            precedence = resp.get(0);
            //titleView = (TextView)rootView.findViewById(R.id.title);
            //titleView.setText(law.getTitle());


            SpannableStringBuilder lawbody = getFullPrecedence(category);

            return lawbody;
        }
        @Override
        protected void onPostExecute(SpannableStringBuilder bodyString) {
            getActivity().setTitle(precedence.getTitle());
            bodyView.setText(bodyString);

            progressDialog.dismiss();
        }
    }


}

