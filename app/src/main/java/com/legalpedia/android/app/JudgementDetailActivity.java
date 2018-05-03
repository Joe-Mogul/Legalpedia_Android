package com.legalpedia.android.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.legalpedia.android.app.models.Courts;
import com.legalpedia.android.app.models.CourtsDao;
import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.Judgements;
import com.legalpedia.android.app.models.JudgementsDao;
import com.legalpedia.android.app.models.Ratio;
import com.legalpedia.android.app.models.RatioDao;
import com.legalpedia.android.app.models.Subjects;
import com.legalpedia.android.app.models.Summary;
import com.legalpedia.android.app.models.SummaryDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by adebayoolabode on 11/23/16.
 */

public class JudgementDetailActivity extends AppCompatActivity {
    private String sectionid;
    private TextView titleView;
    private TextView bodyView;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String judgementid;
    private DaoSession daoSession;
    private JudgementsDao judgementsDao;
    private SummaryDao summaryDao;
    private CourtsDao courtsDao;
    private RatioDao ratioDao;
    private Ratio ratio;
    private Courts court;
    private Summary summary;
    private ProgressDialog progressDialog;
    private static final int TRANSLATE = 1;
    private static final int SHARE = 2;
    private static final int COPY = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judgementdetail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        bodyView = (TextView) findViewById(R.id.body);
        if(Build.VERSION.SDK_INT>=11){
        bodyView.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                menu.add(0, TRANSLATE, 0, "Annotate").setIcon(R.drawable.ic_discuss); //choose any icon
                //menu.add(0, SHARE, 0, "Share").setIcon);
                // Remove the other options
                //menu.removeItem(android.R.id.selectAll);
                //menu.removeItem(android.R.id.cut);
                //menu.removeItem(android.R.id.copy);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                Log.d("Menu",String.valueOf(item.getItemId()));
                switch (item.getItemId()) {
                    case TRANSLATE:
                        int min = 0;
                        int max = bodyView.getText().length();
                        if (bodyView.isFocused()) {
                            final int selStart = bodyView.getSelectionStart();
                            final int selEnd = bodyView.getSelectionEnd();

                            min = Math.max(0, Math.min(selStart, selEnd));
                            max = Math.max(0, Math.max(selStart, selEnd));
                        }

                        final CharSequence selectedText = bodyView.getText().subSequence(min, max); //this is your desired string
                        Toast.makeText(getApplicationContext(), selectedText, Toast.LENGTH_SHORT).show();

                        //Here put your code for translation

                        mode.finish();
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });


    }
        Typeface face=Typeface.createFromAsset(getAssets(),
                "Verdana.ttf");

        bodyView.setTypeface(face);
        Intent intent=getIntent();
        judgementid=intent.getStringExtra("judgementid");
        System.out.println("Judgement ID "+judgementid);
        daoSession = ((App)getApplication()).getDaoSession();
        judgementsDao = daoSession.getJudgementsDao();
        summaryDao =daoSession.getSummaryDao();
        courtsDao = daoSession.getCourtsDao();
        ratioDao = daoSession.getRatioDao();
        progressDialog = new ProgressDialog(JudgementDetailActivity.this,R.style.customDialog);
        progressDialog.setTitle("Legalpedia");
        progressDialog.setMessage("Processing...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        new UpdateJudgement().execute();
    }


    public SpannableStringBuilder getBody(Summary summary){

        String title = "\n\n\n"+summary.getCase_title()+"\n\n";
        String citation = ""+summary.getL_citation()+"\n\n";
        String courttxt = court.getTitle()+"\n\n";
        String sittingat = summary.getSitting_at()+"\n\n";
        String formatteddate = new SimpleDateFormat("E,M dd,yyyy").format(summary.getDate());
        String date = formatteddate+"\n\n";
        String suitnumber = "Suit Number:"+summary.getSuit_number()+"\n\n";
        String coram = "Coram:"+Html.fromHtml(summary.getCoram())+"\n\n";
        String partya = Html.fromHtml(summary.getParty_a_type())+"\n\n"+Html.fromHtml(summary.getParty_a_names())+"\n\n";
        String partyb = Html.fromHtml(summary.getParty_b_type())+"\n\n"+Html.fromHtml(summary.getParty_b_names())+"\n\n";
        String summaryoffacts = "Summary:\n\n"+summary.getSummary_of_facts()+"\n\n";
        String held = "Held:\n\n"+summary.getHeld()+"\n\n";
        String issue = "Issue:\n\n"+summary.getIssue()+"\n\n";
        String ratioS = "Ratio:\n\n"+ratio.getContent()+"\n\n";
        String case_cited ="";
        if(summary.getCases_cited().equals("") || summary.getCases_cited().length()<=0){
           case_cited = "Case Cited:\n\nNot Available\n\n";
        }else {
            case_cited = "Case Cited:\n\n" + summary.getCases_cited() + "\n\n";
        }
        String statues_cited="";
        if(summary.getStatutes_cited().equals("<p>\"</p>") || summary.getStatutes_cited().length()<=0){
           statues_cited = "Statutes Cited:\n\nNot Available\n\n";
        }else {
           statues_cited = "Statutes Cited:\n\n" + Html.fromHtml(summary.getStatutes_cited()) + "\n\n";
        }
        //String appearances = "\t\t\t\tAppearances:\n\n"+summary.get()+"\n\n";
        String judgements = "Judgements:\n\n"+summary.getHeld()+"\n\n";
        //String counsel = "\t\t\t\tCounsel:\n\n"+summary.getC()+"\n\n";
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder ();

        SpannableString titleString = new SpannableString(title);
        titleString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, title.length(), 0);
        titleString.setSpan(new RelativeSizeSpan(1.5f), 0, title.length(), 0);
        titleString.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(), 0);
        titleString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString citationString = new SpannableString(citation);
        citationString.setSpan(new ForegroundColorSpan(Color.GREEN), 0, citationString.length(), 0);
        citationString.setSpan(new StyleSpan(Typeface.BOLD), 0, citationString.length(), 0);
        citationString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, citationString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString courtString = new SpannableString(courttxt);
        courtString.setSpan(new ForegroundColorSpan(Color.RED), 0, courtString.length(), 0);
        courtString.setSpan(new StyleSpan(Typeface.BOLD), 0, courtString.length(), 0);
        courtString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, courtString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString sittingString = new SpannableString(sittingat);
        sittingString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, sittingString.length(), 0);
        sittingString.setSpan(new StyleSpan(Typeface.BOLD), 0, sittingString.length(), 0);
        sittingString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, sittingString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);



        SpannableString dateString = new SpannableString(date);
        dateString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, dateString.length(), 0);
        dateString.setSpan(new StyleSpan(Typeface.BOLD), 0, dateString.length(), 0);
        dateString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, dateString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString suitnumberString = new SpannableString(suitnumber);
        suitnumberString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, suitnumberString.length(), 0);
        suitnumberString.setSpan(new StyleSpan(Typeface.BOLD), 0, suitnumberString.length(), 0);
        suitnumberString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, suitnumberString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString coramString = new SpannableString(coram);
        coramString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, coramString.length(), 0);
        coramString.setSpan(new StyleSpan(Typeface.BOLD), 0, coramString.length(), 0);
        coramString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, coramString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);



        SpannableString partyAString = new SpannableString(partya);
        partyAString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, partyAString.length(), 0);
        partyAString.setSpan(new StyleSpan(Typeface.BOLD), 0, partyAString.length(), 0);
        partyAString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, partyAString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString partyBString = new SpannableString(partyb);
        partyBString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, partyBString.length(), 0);
        partyBString.setSpan(new StyleSpan(Typeface.BOLD), 0, partyBString.length(), 0);
        partyBString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, partyBString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString summaryOfFactsString = new SpannableString(summaryoffacts);
        summaryOfFactsString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, summaryOfFactsString.length(), 0);
        summaryOfFactsString.setSpan(new StyleSpan(Typeface.BOLD), 0, summaryOfFactsString.length(), 0);

        SpannableString heldString = new SpannableString(held);
        heldString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, heldString.length(), 0);
        heldString.setSpan(new StyleSpan(Typeface.BOLD), 0, heldString.length(), 0);


        SpannableString issueString = new SpannableString(issue);
        issueString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, issueString.length(), 0);
        issueString.setSpan(new StyleSpan(Typeface.BOLD), 0, issueString.length(), 0);


        SpannableString ratioString = new SpannableString(ratioS);
        ratioString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, ratioString.length(), 0);
        ratioString.setSpan(new StyleSpan(Typeface.BOLD), 0, ratioString.length(), 0);

        SpannableString casesCitedString = new SpannableString(case_cited);
        casesCitedString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, casesCitedString.length(), 0);
        casesCitedString.setSpan(new StyleSpan(Typeface.BOLD), 0, casesCitedString.length(), 0);


        SpannableString statutesCitedString = new SpannableString(statues_cited);
        statutesCitedString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, statutesCitedString.length(), 0);
        statutesCitedString.setSpan(new StyleSpan(Typeface.BOLD), 0, statutesCitedString.length(), 0);


        SpannableString judgementsString = new SpannableString(judgements);
        judgementsString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, judgementsString.length(), 0);
        judgementsString.setSpan(new StyleSpan(Typeface.BOLD), 0, judgementsString.length(), 0);


        stringBuilder.append(titleString);
        stringBuilder.append(citationString);
        stringBuilder.append(courtString);
        stringBuilder.append(sittingString);
        stringBuilder.append(dateString);
        stringBuilder.append(suitnumberString);
        stringBuilder.append(coramString);
        stringBuilder.append(partyAString);
        stringBuilder.append(partyBString);
        stringBuilder.append(summaryOfFactsString);
        stringBuilder.append(heldString);
        stringBuilder.append(issueString);
        stringBuilder.append(ratioString);
        stringBuilder.append(casesCitedString);
        stringBuilder.append(statutesCitedString);
        stringBuilder.append(judgementsString);




        return stringBuilder;
    }


    public SpannableStringBuilder getBody(Summary summary,Judgements judgement){

        String title = "\n\n\n"+summary.getCase_title()+"\n\n";
        String citation = ""+summary.getL_citation()+"\n\n";
        String courttxt = court.getTitle()+"\n\n";
        String sittingat = summary.getSitting_at()+"\n\n";
        String formatteddate = new SimpleDateFormat("E,M dd,yyyy").format(summary.getDate());
        String date = formatteddate+"\n\n";
        String suitnumber = "Suit Number:"+summary.getSuit_number()+"\n\n";
        String coram = "Coram:"+Html.fromHtml(summary.getCoram())+"\n\n";
        String partya = Html.fromHtml(summary.getParty_a_type())+"\n\n"+Html.fromHtml(summary.getParty_a_names())+"\n\n";
        String partyb = Html.fromHtml(summary.getParty_b_type())+"\n\n"+Html.fromHtml(summary.getParty_b_names())+"\n\n";
        String summaryoffacts = "Summary:\n\n"+summary.getSummary_of_facts()+"\n\n";
        String held = "Held:\n\n"+summary.getHeld()+"\n\n";
        String issue = "Issue:\n\n"+summary.getIssue()+"\n\n";
        String ratioS = "Ratio:\n\n"+ratio.getContent()+"\n\n";
        String case_cited ="";
        if(summary.getCases_cited().equals("") || summary.getCases_cited().length()<=0){
            case_cited = "Case Cited:\n\nNot Available\n\n";
        }else {
            case_cited = "Case Cited:\n\n" + summary.getCases_cited() + "\n\n";
        }
        String statues_cited="";
        if(summary.getStatutes_cited().equals("<p>\"</p>") || summary.getStatutes_cited().length()<=0){
            statues_cited = "Statutes Cited:\n\nNot Available\n\n";
        }else {
            statues_cited = "Statutes Cited:\n\n" + Html.fromHtml(summary.getStatutes_cited()) + "\n\n";
        }
        //String appearances = "\t\t\t\tAppearances:\n\n"+summary.get()+"\n\n";
        String judgements = "Judgements:\n\n"+Html.fromHtml(judgement.getJudgement())+"\n\n";
        //String counsel = "\t\t\t\tCounsel:\n\n"+summary.getC()+"\n\n";
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder ();

        SpannableString titleString = new SpannableString(title);
        titleString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, title.length(), 0);
        titleString.setSpan(new RelativeSizeSpan(1.5f), 0, title.length(), 0);
        titleString.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(), 0);
        titleString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString citationString = new SpannableString(citation);
        citationString.setSpan(new ForegroundColorSpan(Color.GREEN), 0, citationString.length(), 0);
        citationString.setSpan(new StyleSpan(Typeface.BOLD), 0, citationString.length(), 0);
        citationString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, citationString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString courtString = new SpannableString(courttxt);
        courtString.setSpan(new ForegroundColorSpan(Color.RED), 0, courtString.length(), 0);
        courtString.setSpan(new StyleSpan(Typeface.BOLD), 0, courtString.length(), 0);
        courtString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, courtString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString sittingString = new SpannableString(sittingat);
        sittingString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, sittingString.length(), 0);
        sittingString.setSpan(new StyleSpan(Typeface.BOLD), 0, sittingString.length(), 0);
        sittingString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, sittingString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);



        SpannableString dateString = new SpannableString(date);
        dateString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, dateString.length(), 0);
        dateString.setSpan(new StyleSpan(Typeface.BOLD), 0, dateString.length(), 0);
        dateString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, dateString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString suitnumberString = new SpannableString(suitnumber);
        suitnumberString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, suitnumberString.length(), 0);
        suitnumberString.setSpan(new StyleSpan(Typeface.BOLD), 0, suitnumberString.length(), 0);
        suitnumberString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, suitnumberString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString coramString = new SpannableString(coram);
        coramString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, coramString.length(), 0);
        coramString.setSpan(new StyleSpan(Typeface.BOLD), 0, coramString.length(), 0);
        coramString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, coramString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);



        SpannableString partyAString = new SpannableString(partya);
        partyAString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, partyAString.length(), 0);
        partyAString.setSpan(new StyleSpan(Typeface.BOLD), 0, partyAString.length(), 0);
        partyAString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, partyAString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString partyBString = new SpannableString(partyb);
        partyBString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, partyBString.length(), 0);
        partyBString.setSpan(new StyleSpan(Typeface.BOLD), 0, partyBString.length(), 0);
        partyBString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, partyBString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString summaryOfFactsString = new SpannableString(summaryoffacts);
        summaryOfFactsString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, summaryOfFactsString.length(), 0);
        summaryOfFactsString.setSpan(new StyleSpan(Typeface.BOLD), 0, summaryOfFactsString.length(), 0);

        SpannableString heldString = new SpannableString(held);
        heldString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, heldString.length(), 0);
        heldString.setSpan(new StyleSpan(Typeface.BOLD), 0, heldString.length(), 0);



        SpannableString issueString = new SpannableString(issue);
        issueString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, issueString.length(), 0);
        issueString.setSpan(new StyleSpan(Typeface.BOLD), 0, issueString.length(), 0);


        SpannableString ratioString = new SpannableString(ratioS);
        ratioString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, ratioString.length(), 0);
        ratioString.setSpan(new StyleSpan(Typeface.BOLD), 0, ratioString.length(), 0);

        SpannableString casesCitedString = new SpannableString(case_cited);
        casesCitedString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, casesCitedString.length(), 0);
        casesCitedString.setSpan(new StyleSpan(Typeface.BOLD), 0, casesCitedString.length(), 0);


        SpannableString statutesCitedString = new SpannableString(statues_cited);
        statutesCitedString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, statutesCitedString.length(), 0);
        statutesCitedString.setSpan(new StyleSpan(Typeface.BOLD), 0, statutesCitedString.length(), 0);


        SpannableString judgementsString = new SpannableString(judgements);
        judgementsString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, judgementsString.length(), 0);
        judgementsString.setSpan(new StyleSpan(Typeface.BOLD), 0, judgementsString.length(), 0);


        stringBuilder.append(titleString);
        stringBuilder.append(citationString);
        stringBuilder.append(courtString);
        stringBuilder.append(sittingString);
        stringBuilder.append(dateString);
        stringBuilder.append(suitnumberString);
        stringBuilder.append(coramString);
        stringBuilder.append(partyAString);
        stringBuilder.append(partyBString);
        stringBuilder.append(summaryOfFactsString);
        stringBuilder.append(heldString);
        stringBuilder.append(issueString);
        stringBuilder.append(ratioString);
        stringBuilder.append(casesCitedString);
        stringBuilder.append(statutesCitedString);
        stringBuilder.append(judgementsString);




        return stringBuilder;
    }


    class UpdateJudgement  extends AsyncTask<String, String, SpannableStringBuilder> {

        private SpannableStringBuilder bodyString;
        @Override
        protected SpannableStringBuilder doInBackground(String ...offset) {
            Log.d("JudgementDetails","Got here 2");


            QueryBuilder qb = summaryDao.queryBuilder();
            List resp=qb.where(SummaryDao.Properties.Id.eq(judgementid)).list();

            summary=  (Summary)resp.get(0);
            //titleView = (TextView)rootView.findViewById(R.id.title);
            //titleView.setText(section.getTitle());
            String summaryid = String.valueOf(summary.getId());
            QueryBuilder qb1 = judgementsDao.queryBuilder();
            List judge=qb1.where(JudgementsDao.Properties.Summaryid.eq(summaryid)).list();
            Judgements judgements = new Judgements();
            QueryBuilder qb2 = courtsDao.queryBuilder();
            List resp2=qb2.where(CourtsDao.Properties.Id.eq(summary.getCourtid())).list();
            if(resp2.size()>0) {
                court = (Courts) resp2.get(0);
            }
            else{
                court = new Courts();
            }
            QueryBuilder qb3 = ratioDao.queryBuilder();
            List resp3=qb3.where(RatioDao.Properties.Id.eq(summary.getRatio())).list();
            if(resp3.size()>0) {
                ratio = (Ratio) resp3.get(0);
            }else{
                ratio = new Ratio();
            }
            if(judge.size()==0) {



                bodyString = getBody(summary);

                Log.d("JudgementDetails",bodyString.toString());
            }else{
                try {
                    judgements = (Judgements) judge.get(0);
                }catch(Exception ex){
                    ex.printStackTrace();
                }

                bodyString = getBody(summary,judgements);
                Log.d("JudgementDetails",bodyString.toString());



            }


            return bodyString;
        }

        @Override
        protected void onPostExecute(SpannableStringBuilder bodyString) {
            Log.d("JudgementDetails","Got here 3");
            setTitle(summary.getCase_title());
            bodyView.setText(bodyString);
            progressDialog.dismiss();
        }



    }

}
