package com.legalpedia.android.app.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.v4.app.Fragment;
import android.support.v4.print.PrintHelper;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.legalpedia.android.app.App;
import com.legalpedia.android.app.JudgementFullDetailViewActivity;
import com.legalpedia.android.app.R;
import com.legalpedia.android.app.models.Annotations;
import com.legalpedia.android.app.models.AnnotationsDao;
import com.legalpedia.android.app.models.Courts;
import com.legalpedia.android.app.models.CourtsDao;
import com.legalpedia.android.app.models.DaoMaster;
import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.Judgements;
import com.legalpedia.android.app.models.JudgementsDao;
import com.legalpedia.android.app.models.Ratio;
import com.legalpedia.android.app.models.RatioDao;
import com.legalpedia.android.app.models.Summary;
import com.legalpedia.android.app.models.SummaryDao;
import com.legalpedia.android.app.util.LGPClient;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



/**
 * Created by adebayoolabode on 4/9/17.
 */

public class JudgementFullDetailFragment extends Fragment {

    private String judgementid;
    private TextView titleView;
    private TextView bodyView;
    private View rootView;
    private String  title = "";
    private Context ctx=null;
    private Database db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private JudgementsDao judgementsDao;
    private SummaryDao summaryDao;
    private CourtsDao courtsDao;
    private RatioDao ratioDao;
    private AnnotationsDao annotationsDao;
    private List<Ratio> ratios;
    private Courts court;
    private Summary summary;
    private ProgressDialog progressDialog;
    private static final int TRANSLATE = 1;
    private String LOGINPREFERENCES="login_data";
    private SharedPreferences sharedpreferences = null;
    private int resource=0;
    private int uid=0;
    private String searchtext="";
    private String[] searchtokens=null;
    private float lastY=0;
    private float TIME_DELAY=2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.judgementfulldetail_fragment, container, false);
        ctx=rootView.getContext();
        JudgementFullDetailViewActivity activity = (JudgementFullDetailViewActivity)ctx;
        setHasOptionsMenu(false);
        daoSession = ((App)getActivity().getApplication()).getDaoSession();
        daoMaster = ((App)getActivity().getApplication()).getDaoMaster();
        db = daoSession.getDatabase();
        judgementsDao = daoSession.getJudgementsDao();
        summaryDao =daoSession.getSummaryDao();
        courtsDao = daoSession.getCourtsDao();
        ratioDao = daoSession.getRatioDao();
        annotationsDao = daoSession.getAnnotationsDao();
        sharedpreferences=ctx.getSharedPreferences(LOGINPREFERENCES, Context.MODE_PRIVATE);
        uid = Integer.parseInt(sharedpreferences.getString("uid","0"));
        Intent intent=activity.getIntent();
        judgementid=intent.getStringExtra("judgementid");


        try {
        if(intent.getStringExtra("searchtext")!=null){
            searchtext =  intent.getStringExtra("searchtext");
        }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

        try {
            if (intent.getStringArrayExtra("tokenlist") != null) {
                searchtokens = intent.getStringArrayExtra("tokenlist");
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        Log.d("Judgement ID ",judgementid);
        resource =2;
        bodyView = (TextView)rootView.findViewById(R.id.body);
        /**if(Build.VERSION.SDK_INT>=23) {
            bodyView.setOnTouchListener(new OnTouchListener(){

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            v.setBackgroundColor(Color.YELLOW);
                            lastY = event.getY();
                            break;
                        case MotionEvent.ACTION_UP:
                            v.setBackgroundColor(Color.WHITE);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            float abs = Math.abs(lastY - event.getY());

                            if (abs > TIME_DELAY) // TIME_DELAY=2
                                v.setBackgroundColor(Color.WHITE);
                            break;
                    }
                    return true;
                }
            });
        }
         */



         if(Build.VERSION.SDK_INT>=11){
             Log.d("JudgementFragment","Enabled annotation");
            bodyView.setTextIsSelectable(true);
            bodyView.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    menu.add(0, TRANSLATE, 0, "Translate").setIcon(R.drawable.ic_discuss); //choose any icon
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
                            //Toast.makeText(getActivity().getApplicationContext(), selectedText, Toast.LENGTH_SHORT).show();
                            //ctx.getSystemService(ctx.CLIPBOARD_SERVICE).setText(selectedText);
                            //Here put your code for translation
                            String annotationtext= selectedText.toString();
                            String comment="";
                            String titleshare="Share using";
                            highlightString(annotationtext);
                            String sharedtext=title+"\n\n"+annotationtext+"\n\n Sent from Legalpedia Android https://www.legalpediaonline.com/";
                            shareToSocial(sharedtext,titleshare);
                            saveToDB(title,annotationtext,comment);

                            mode.finish();
                    }
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });


        }
        Typeface face=Typeface.createFromAsset(getActivity().getAssets(),
                "Verdana.ttf");

        bodyView.setTypeface(face);

        new UpdateJudgement().execute();
        //registerForContextMenu(bodyView);
        return rootView;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select The Action");
        menu.add(0, v.getId(), 0, "Call");//groupId, itemId, order, title
        menu.add(0, v.getId(), 0, "SMS");
    }


    public void shareToSocial(String message,String title){
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_SUBJECT, "Legalpedia");
        share.putExtra(Intent.EXTRA_TEXT, message);

        startActivity(Intent.createChooser(share, title));
    }


    private void doPhotoPrint() {
        if(Build.VERSION.SDK_INT>=19) {
            PrintHelper photoPrinter = new PrintHelper(getActivity());
            photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.logo);
            photoPrinter.printBitmap("droids.jpg - test print", bitmap);
        }
    }

    private void createWebPrintJob(WebView webView) {
        List mPrintJobs = new ArrayList();
        if(Build.VERSION.SDK_INT>=19) {
            // Get a PrintManager instance
            PrintManager printManager = (PrintManager) getActivity()
                    .getSystemService(Context.PRINT_SERVICE);

            // Get a print adapter instance
            PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();

            // Create a print job with name and adapter instance
            String jobName = getString(R.string.app_name) + " Document";
            PrintJob printJob = printManager.print(jobName, printAdapter,
                    new PrintAttributes.Builder().build());

            // Save the job object for later status checking
            mPrintJobs.add(printJob);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        if(item.getTitle()=="Call"){
            Toast.makeText(ctx,"calling code",Toast.LENGTH_LONG).show();
        }
        else if(item.getTitle()=="SMS"){
            Toast.makeText(ctx,"sending sms code",Toast.LENGTH_LONG).show();
        }else{
            return false;
        }
        return true;
    }

    public void saveToDB(String title,String content,String comment){
        Annotations annotations = new Annotations();
        annotations.setTitle(title);
        annotations.setContent(content);
        annotations.setResource(resource);
        annotations.setResourceid(Integer.parseInt(judgementid));
        annotations.setUid(uid);
        annotations.setComment(comment);
        annotationsDao.save(annotations);
        try {
            new SaveAnnotation().execute(String.valueOf(uid),String.valueOf(resource),judgementid,title,content,"", comment);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }



    private void highlightString(String selected) {
        //Get the text from text view and create a spannable string
        SpannableString spannableString = new SpannableString(bodyView.getText());

        //Get the previous spans and remove them
        BackgroundColorSpan[] backgroundSpans = spannableString.getSpans(0, spannableString.length(), BackgroundColorSpan.class);

        for (BackgroundColorSpan span: backgroundSpans) {
            spannableString.removeSpan(span);
        }

        //Search for all occurrences of the keyword in the string
        int indexOfKeyword = spannableString.toString().indexOf(selected);

        while (indexOfKeyword > 0) {
            //Create a background color span on the keyword
            spannableString.setSpan(new BackgroundColorSpan(Color.YELLOW), indexOfKeyword, indexOfKeyword + selected.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            //Get the next index of the keyword
            indexOfKeyword = spannableString.toString().indexOf(selected, indexOfKeyword + selected.length());
        }

        //Set the final text on TextView
        bodyView.setText(spannableString);
    }



    private void highlightStringAll(List<String> selectedlist) {
        //Get the text from text view and create a spannable string
        SpannableString spannableString = new SpannableString(bodyView.getText());

        //Get the previous spans and remove them
        BackgroundColorSpan[] backgroundSpans = spannableString.getSpans(0, spannableString.length(), BackgroundColorSpan.class);

        for (BackgroundColorSpan span: backgroundSpans) {
            spannableString.removeSpan(span);
        }

        for(String selected : selectedlist) {
            //Search for all occurrences of the keyword in the string
            int indexOfKeyword = spannableString.toString().indexOf(selected);

            while (indexOfKeyword > 0) {
                //Create a background color span on the keyword
                spannableString.setSpan(new BackgroundColorSpan(Color.YELLOW), indexOfKeyword, indexOfKeyword + selected.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                //Get the next index of the keyword
                indexOfKeyword = spannableString.toString().indexOf(selected, indexOfKeyword + selected.length());
            }

        }
        //Set the final text on TextView
        bodyView.setText(spannableString);
    }


    public SpannableStringBuilder getBody(Summary summary){

        String title = "\n\n\n"+summary.getCase_title()+"\n\n";
        String citation = ""+summary.getL_citation()+"\n\n";
        String courttxt = court.getTitle()+"\n\n";
        String sittingat = summary.getSitting_at()+"\n\n";
        String formatteddate = new SimpleDateFormat("EEE,dd MMMM yyyy").format(summary.getDate());
        String date = formatteddate+"\n\n";
        String suitnumber = "Suit Number:"+summary.getSuit_number()+"\n\n";
        String coram = "Coram:"+Html.fromHtml(summary.getCoram())+"\n\n";
        String partya = Html.fromHtml(summary.getParty_a_type())+"\n\n"+Html.fromHtml(summary.getParty_a_names())+"\n\n";
        String partyb = Html.fromHtml(summary.getParty_b_type())+"\n\n"+Html.fromHtml(summary.getParty_b_names())+"\n\n";
        String summaryoffacts = "Summary:\n\n"+summary.getSummary_of_facts()+"\n\n";
        String held = "Held:\n\n"+summary.getHeld()+"\n\n";
        String issue = "Issue:\n\n"+summary.getIssue()+"\n\n";

        String case_cited ="";
        if(summary.getCases_cited().equals("") || summary.getCases_cited().length()<=3){
            case_cited = "Case Cited:\n\nNot Available\n\n";
        }else {
            case_cited = "Case Cited:\n\n" + Html.fromHtml(summary.getCases_cited()) + "\n\n";
        }
        String statues_cited="";
        if(summary.getStatutes_cited().equals("<p>\"</p>") || summary.getStatutes_cited().length()<=3){
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
        int i =0;
        Log.d("Total Ratio",String.valueOf(ratios.size()));
        for(Ratio ratio:ratios) {
            String ratioS="";
            if(i>0) {
                 ratioS =  ratio.getTitle() + "\n" + ratio.getContent() + "\n\n";
            }else{
                ratioS = "Ratio:\n\n" + ratio.getTitle() + "\n" + ratio.getContent() + "\n\n";
            }
            SpannableString ratioString = new SpannableString(ratioS);
            ratioString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, ratioString.length(), 0);
            ratioString.setSpan(new StyleSpan(Typeface.BOLD), 0, ratioString.length(), 0);
            stringBuilder.append(ratioString);
            i++;
        }
        stringBuilder.append(casesCitedString);
        stringBuilder.append(statutesCitedString);
        stringBuilder.append(judgementsString);




        return stringBuilder;
    }


    public SpannableStringBuilder getBody(Summary summary,Judgements judgement) {
        int defaultfont = (int) getResources().getDimension(R.dimen.judgement__defaultfont);
        String title = "\n\n\n" + summary.getCase_title() + "\n\n";
        String citation = "" + summary.getL_citation() + "\n\n";
        String courttxt = court.getTitle() + "\n\n";
        String sittingat = summary.getSitting_at() + "\n\n";
        String date = "Not Available\n\n";
        if (summary.getDate() != null) {
            String formatteddate = new SimpleDateFormat("E,dd MMMM yyyy").format(summary.getDate());
            date = formatteddate + "\n\n";
        }
        if (summary.getJudgement_date() != null) {
            String formatteddate = new SimpleDateFormat("E,dd MMMM yyyy").format(summary.getJudgement_date());
            date = formatteddate + "\n\n";
        }
        String suitnumberheader = "Suit Number\n\n";
        String suitnumber = summary.getSuit_number() + "\n\n";
        String coramheader = "Coram\n\n";
        String coram = Html.fromHtml(summary.getCoram()) + "\n\n";
        String partyaheader = Html.fromHtml(summary.getParty_a_type()).toString();
        String partya = Html.fromHtml(summary.getParty_a_names()).toString();
        String partybheader = Html.fromHtml(summary.getParty_b_type()).toString();
        String partyb = Html.fromHtml(summary.getParty_b_names()).toString();
        String areaoflawheader = "Area of Law\n\n";

        String summaryheader = "Summary\n\n";

        String heldheader = "Held\n\n";

        String issueheader = "Issue\n\n";
        String issue = "";
        if (summary.getIssue() == null || summary.getIssue().equals("<p>\"</p>") || summary.getIssue().equals("") || summary.getIssue().length() <= 3) {
            issue = "Not Available\n\n";
        } else {
            issue = Html.fromHtml(summary.getIssue()) + "\n\n";
        }

        String case_citedheader = "Case Cited\n\n";

        String areaoflaw = "";
        if (summary.getArea_of_law().equals("<p>\"</p>") || summary.getArea_of_law().equals("") || summary.getArea_of_law().length() <= 3) {
            areaoflaw = "Not Available\n\n";
        } else {
            areaoflaw = Html.fromHtml(summary.getArea_of_law()) + "\n\n";
        }
        String summaryoffacts = "";
        if (summary.getSummary_of_facts().equals("<p>\"</p>") || summary.getSummary_of_facts().equals("") || summary.getSummary_of_facts().length() <= 3) {
            summaryoffacts = "Not Available\n\n";
        } else {
            summaryoffacts = Html.fromHtml(summary.getSummary_of_facts()) + "\n\n";
        }
        String held = "";
        if (summary.getHeld().equals("<p>\"</p>") || summary.getHeld().equals("") || summary.getHeld().length() <= 3) {
            held = "Not Available\n\n";
        } else {
            held = Html.fromHtml(summary.getHeld()) + "\n\n";
        }

        String case_cited = "";
        if (summary.getCases_cited().equals("<p>\"</p>") || summary.getCases_cited().equals("") || summary.getCases_cited().length() <= 0) {
            case_cited = "Not Available\n\n";
        } else {
            case_cited = Html.fromHtml(summary.getCases_cited()) + "\n\n";
        }
        String statutes_citedheader = "Statutes Cited\n\n";
        String statues_cited = "";
        if (summary.getStatutes_cited().equals("<p>\"</p>") || summary.getStatutes_cited().length() <= 0) {
            statues_cited = "Not Available\n\n";
        } else {
            statues_cited = Html.fromHtml(summary.getStatutes_cited()) + "\n\n";
        }
        String counselheader = "Counsel\n\n";
        String counsel = "";
        if (judgement.getCounsel().equals("<p>\"</p>") || judgement.getCounsel().length() <= 0) {
            counsel = "Not Available\n\n";
        } else {
            counsel = Html.fromHtml(judgement.getCounsel()) + "\n\n";
        }
        //String appearances = "\t\t\t\tAppearances:\n\n"+summary.get()+"\n\n";
        String judgementsheader = "Judgements\n\n";
        String judgements = Html.fromHtml(judgement.getJudgement()) + "\n\n";
        //String counsel = "\t\t\t\tCounsel:\n\n"+summary.getC()+"\n\n";
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();

        SpannableString titleString = new SpannableString(title);
        titleString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, title.length(), 0);
        titleString.setSpan(new RelativeSizeSpan(1.5f), 0, title.length(), 0);
        titleString.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(), 0);
        titleString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString citationString = new SpannableString(citation);
        citationString.setSpan(new ForegroundColorSpan(Color.parseColor("#006621")), 0, citationString.length(), 0);
        citationString.setSpan(new StyleSpan(Typeface.BOLD), 0, citationString.length(), 0);
        citationString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, citationString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString courtString = new SpannableString(courttxt);
        courtString.setSpan(new ForegroundColorSpan(Color.RED), 0, courtString.length(), 0);
        courtString.setSpan(new StyleSpan(Typeface.BOLD), 0, courtString.length(), 0);
        courtString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, courtString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString sittingString = new SpannableString(sittingat);
        sittingString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, sittingString.length(), 0);
        sittingString.setSpan(new StyleSpan(Typeface.NORMAL), 0, sittingString.length(), 0);
        sittingString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, sittingString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString dateString = new SpannableString(date);
        dateString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, dateString.length(), 0);
        dateString.setSpan(new StyleSpan(Typeface.BOLD), 0, dateString.length(), 0);
        dateString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, dateString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString suitnumberHeaderString = new SpannableString(suitnumberheader);
        suitnumberHeaderString.setSpan(new ForegroundColorSpan(Color.parseColor("#006621")), 0, suitnumberHeaderString.length(), 0);
        suitnumberHeaderString.setSpan(new StyleSpan(Typeface.BOLD), 0, suitnumberHeaderString.length(), 0);
        suitnumberHeaderString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, suitnumberHeaderString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString suitnumberString = new SpannableString(suitnumber);
        suitnumberString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, suitnumberString.length(), 0);
        suitnumberString.setSpan(new AbsoluteSizeSpan(defaultfont), 0, suitnumberString.length(), 0);
        suitnumberString.setSpan(new StyleSpan(Typeface.NORMAL), 0, suitnumberString.length(), 0);
        suitnumberString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, suitnumberString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString coramHeaderString = new SpannableString(coramheader);
        coramHeaderString.setSpan(new ForegroundColorSpan(Color.parseColor("#006621")), 0, coramHeaderString.length(), 0);
        coramHeaderString.setSpan(new StyleSpan(Typeface.BOLD), 0, coramHeaderString.length(), 0);
        coramHeaderString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, coramHeaderString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString coramString = new SpannableString(coram);
        coramString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, coramString.length(), 0);
        coramString.setSpan(new AbsoluteSizeSpan(defaultfont), 0, coramString.length(), 0);
        coramString.setSpan(new StyleSpan(Typeface.NORMAL), 0, coramString.length(), 0);
        coramString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, coramString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString partyAHeaderString = new SpannableString(partyaheader);
        partyAHeaderString.setSpan(new ForegroundColorSpan(Color.parseColor("#006621")), 0, partyAHeaderString.length(), 0);
        partyAHeaderString.setSpan(new StyleSpan(Typeface.BOLD), 0, partyAHeaderString.length(), 0);
        partyAHeaderString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, partyAHeaderString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString partyAString = new SpannableString(partya);
        partyAString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, partyAString.length(), 0);
        partyAString.setSpan(new AbsoluteSizeSpan(defaultfont), 0, partyAString.length(), 0);
        partyAString.setSpan(new StyleSpan(Typeface.NORMAL), 0, partyAString.length(), 0);
        partyAString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, partyAString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString partyBHeaderString = new SpannableString(partybheader);
        partyBHeaderString.setSpan(new ForegroundColorSpan(Color.parseColor("#006621")), 0, partyBHeaderString.length(), 0);
        partyBHeaderString.setSpan(new StyleSpan(Typeface.BOLD), 0, partyBHeaderString.length(), 0);
        partyBHeaderString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, partyBHeaderString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString partyBString = new SpannableString(partyb);
        partyBString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, partyBString.length(), 0);
        partyBString.setSpan(new AbsoluteSizeSpan(defaultfont), 0, partyBString.length(), 0);
        partyBString.setSpan(new StyleSpan(Typeface.NORMAL), 0, partyBString.length(), 0);
        partyBString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, partyBString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString areaoflawHeaderString = new SpannableString(areaoflawheader);
        areaoflawHeaderString.setSpan(new ForegroundColorSpan(Color.parseColor("#006621")), 0, areaoflawHeaderString.length(), 0);
        areaoflawHeaderString.setSpan(new StyleSpan(Typeface.BOLD), 0, areaoflawHeaderString.length(), 0);


        SpannableString areaoflawString = new SpannableString(areaoflaw);
        areaoflawString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, areaoflawString.length(), 0);
        areaoflawString.setSpan(new AbsoluteSizeSpan(defaultfont), 0, areaoflawString.length(), 0);
        areaoflawString.setSpan(new StyleSpan(Typeface.NORMAL), 0, areaoflawString.length(), 0);


        SpannableString summaryOfFactsHeaderString = new SpannableString(summaryheader);
        summaryOfFactsHeaderString.setSpan(new ForegroundColorSpan(Color.parseColor("#006621")), 0, summaryOfFactsHeaderString.length(), 0);
        summaryOfFactsHeaderString.setSpan(new StyleSpan(Typeface.BOLD), 0, summaryOfFactsHeaderString.length(), 0);


        SpannableString summaryOfFactsString = new SpannableString(summaryoffacts);
        summaryOfFactsString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, summaryOfFactsString.length(), 0);
        summaryOfFactsString.setSpan(new AbsoluteSizeSpan(defaultfont), 0, summaryOfFactsString.length(), 0);
        summaryOfFactsString.setSpan(new StyleSpan(Typeface.NORMAL), 0, summaryOfFactsString.length(), 0);


        SpannableString heldHeaderString = new SpannableString(heldheader);
        heldHeaderString.setSpan(new ForegroundColorSpan(Color.parseColor("#006621")), 0, heldHeaderString.length(), 0);
        heldHeaderString.setSpan(new StyleSpan(Typeface.BOLD), 0, heldHeaderString.length(), 0);


        SpannableString heldString = new SpannableString(held);
        heldString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, heldString.length(), 0);
        heldString.setSpan(new AbsoluteSizeSpan(defaultfont), 0, heldString.length(), 0);
        heldString.setSpan(new StyleSpan(Typeface.NORMAL), 0, heldString.length(), 0);


        SpannableString issueHeaderString = new SpannableString(issueheader);
        issueHeaderString.setSpan(new ForegroundColorSpan(Color.parseColor("#006621")), 0, issueHeaderString.length(), 0);
        issueHeaderString.setSpan(new StyleSpan(Typeface.BOLD), 0, issueHeaderString.length(), 0);


        SpannableString issueString = new SpannableString(issue);
        issueString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, issueString.length(), 0);
        issueString.setSpan(new AbsoluteSizeSpan(defaultfont), 0, issueString.length(), 0);
        issueString.setSpan(new StyleSpan(Typeface.NORMAL), 0, issueString.length(), 0);


        SpannableString casesCitedHeaderString = new SpannableString(case_citedheader);
        casesCitedHeaderString.setSpan(new ForegroundColorSpan(Color.parseColor("#006621")), 0, casesCitedHeaderString.length(), 0);
        casesCitedHeaderString.setSpan(new StyleSpan(Typeface.BOLD), 0, casesCitedHeaderString.length(), 0);


        SpannableString casesCitedString = new SpannableString(case_cited);
        casesCitedString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, casesCitedString.length(), 0);
        casesCitedString.setSpan(new AbsoluteSizeSpan(defaultfont), 0, casesCitedString.length(), 0);
        casesCitedString.setSpan(new StyleSpan(Typeface.NORMAL), 0, casesCitedString.length(), 0);


        SpannableString statutesCitedHeaderString = new SpannableString(statutes_citedheader);
        statutesCitedHeaderString.setSpan(new ForegroundColorSpan(Color.parseColor("#006621")), 0, statutesCitedHeaderString.length(), 0);
        statutesCitedHeaderString.setSpan(new StyleSpan(Typeface.BOLD), 0, statutesCitedHeaderString.length(), 0);


        SpannableString statutesCitedString = new SpannableString(statues_cited);
        statutesCitedString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, statutesCitedString.length(), 0);
        statutesCitedString.setSpan(new AbsoluteSizeSpan(defaultfont), 0, statutesCitedString.length(), 0);
        statutesCitedString.setSpan(new StyleSpan(Typeface.NORMAL), 0, statutesCitedString.length(), 0);


        SpannableString judgementsHeaderString = new SpannableString(judgementsheader);
        judgementsHeaderString.setSpan(new ForegroundColorSpan(Color.parseColor("#006621")), 0, judgementsHeaderString.length(), 0);
        judgementsHeaderString.setSpan(new StyleSpan(Typeface.BOLD), 0, judgementsHeaderString.length(), 0);


        SpannableString judgementsString = new SpannableString(judgements);
        judgementsString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, judgementsString.length(), 0);
        judgementsString.setSpan(new AbsoluteSizeSpan(defaultfont), 0, judgementsString.length(), 0);
        judgementsString.setSpan(new StyleSpan(Typeface.NORMAL), 0, judgementsString.length(), 0);

        SpannableString counselHeaderString = new SpannableString(counselheader);
        counselHeaderString.setSpan(new ForegroundColorSpan(Color.parseColor("#006621")), 0, counselHeaderString.length(), 0);
        counselHeaderString.setSpan(new StyleSpan(Typeface.BOLD), 0, counselHeaderString.length(), 0);


        SpannableString counselString = new SpannableString(counsel);
        counselString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, counselString.length(), 0);
        counselString.setSpan(new AbsoluteSizeSpan(defaultfont), 0, counselString.length(), 0);
        counselString.setSpan(new StyleSpan(Typeface.NORMAL), 0, counselString.length(), 0);


        stringBuilder.append(titleString);
        stringBuilder.append(citationString);
        stringBuilder.append(courtString);
        stringBuilder.append(sittingString);
        stringBuilder.append(dateString);
        stringBuilder.append(suitnumberHeaderString);
        stringBuilder.append(suitnumberString);
        stringBuilder.append(coramHeaderString);
        stringBuilder.append(coramString);
        stringBuilder.append(partyAHeaderString);
        stringBuilder.append(partyAString);
        stringBuilder.append(partyBHeaderString);
        stringBuilder.append(partyBString);
        stringBuilder.append(areaoflawHeaderString);
        stringBuilder.append(areaoflawString);
        stringBuilder.append(summaryOfFactsHeaderString);
        stringBuilder.append(summaryOfFactsString);
        stringBuilder.append(heldHeaderString);
        stringBuilder.append(heldString);
        stringBuilder.append(issueHeaderString);
        stringBuilder.append(issueString);
        String ratioheader = "Ratio\n\n";
        SpannableString ratioHeaderString = new SpannableString(ratioheader);
        ratioHeaderString.setSpan(new ForegroundColorSpan(Color.parseColor("#006621")), 0, ratioHeaderString.length(), 0);
        ratioHeaderString.setSpan(new StyleSpan(Typeface.BOLD), 0, ratioHeaderString.length(), 0);
        stringBuilder.append(ratioHeaderString);
        Log.d("Total Ratio",String.valueOf(ratios.size()));
        for(Ratio ratio:ratios){


        String ratioS = "";
        if (ratio.getContent() == null || ratio.getContent().equals("<p>\"</p>") || ratio.getContent().equals("") || ratio.getContent().length() <= 3) {
            ratioS = "Not Available\n\n";
        } else {
            ratioS = Html.fromHtml(ratio.getContent()) + "\n\n";
        }
        String ratiotitle = "";
        if (ratio.getTitle() == null || ratio.getTitle().equals("<p>\"</p>") || ratio.getTitle().equals("") || ratio.getTitle().length() <= 3) {
            ratiotitle = "Not Available\n\n";
        } else {
            ratiotitle = Html.fromHtml(ratio.getTitle()) + "\n\n";
        }



        SpannableString ratioTitleString = new SpannableString(ratiotitle);
        ratioTitleString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, ratioTitleString.length(), 0);
        ratioTitleString.setSpan(new AbsoluteSizeSpan(defaultfont), 0, ratioTitleString.length(), 0);
        ratioTitleString.setSpan(new StyleSpan(Typeface.BOLD), 0, ratioTitleString.length(), 0);


        SpannableString ratioString = new SpannableString(ratioS);
        ratioString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, ratioString.length(), 0);
        ratioString.setSpan(new AbsoluteSizeSpan(defaultfont), 0, ratioString.length(), 0);
        ratioString.setSpan(new StyleSpan(Typeface.NORMAL), 0, ratioString.length(), 0);

        stringBuilder.append(ratioTitleString);
        stringBuilder.append(ratioString);


    }
        stringBuilder.append(casesCitedHeaderString);
        stringBuilder.append(casesCitedString);
        stringBuilder.append(statutesCitedHeaderString);
        stringBuilder.append(statutesCitedString);
        stringBuilder.append(judgementsHeaderString);
        stringBuilder.append(judgementsString);
        stringBuilder.append(counselHeaderString);
        stringBuilder.append(counselString);




        return stringBuilder;
    }


    class UpdateJudgement  extends AsyncTask<String, String, SpannableStringBuilder> {

        private SpannableStringBuilder bodyString;
        private ProgressDialog progressDialog = new ProgressDialog(ctx,R.style.customDialog);
        @Override
        protected SpannableStringBuilder doInBackground(String ...offset) {
            Log.d("JudgementDetails","Got here 2");
            ratios = new ArrayList<Ratio>();
            QueryBuilder qb = summaryDao.queryBuilder();
            List resp=qb.where(SummaryDao.Properties.Id.eq(judgementid)).list();
            summary=  (Summary)resp.get(0);
            /**try {
                Log.d("Summary Number", String.valueOf(summary.getId()));
                Log.d("Case Title", String.valueOf(summary.getCase_title()));
                Log.d("Suit Number", summary.getSuit_number());
            }catch(Exception ex){
                ex.printStackTrace();
            }*/
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

            Log.d("Ratios",summary.getRatio());
            List<String> ids= Arrays.asList(summary.getRatio().split("]"));
            for(String id : ids) {
                id= id.replace("[","").replace("]","");
                if(id.length()>0) {

                    Log.d("Ratio ID ",id);
                    QueryBuilder qb3 = ratioDao.queryBuilder();
                    List resp3 = qb3.where(RatioDao.Properties.Id.eq(id)).list();
                    Log.d("TotalRatio",String.valueOf(resp3.size()));
                    if (resp3.size() > 0) {
                        Ratio ratio = (Ratio) resp3.get(0);
                        ratios.add(ratio);
                    }
                }

            }




            if(judge.size()==0) {



                bodyString = getBody(summary);


            }else{
                try {
                    judgements = (Judgements) judge.get(0);
                }catch(Exception ex){
                    ex.printStackTrace();
                }

                bodyString = getBody(summary,judgements);





            }


            return bodyString;
        }

        @Override
        protected void onPostExecute(SpannableStringBuilder bodyString) {
            Log.d("JudgementDetails","Got here 3");

            getActivity().setTitle(summary.getCase_title());
            bodyView.setText(bodyString);
            try {

                List<Annotations> annotationList = annotationsDao.queryBuilder().whereOr(AnnotationsDao.Properties.Resourceid.eq(judgementid), AnnotationsDao.Properties.Resource.eq(resource)).list();
                List<String> selectedlist = new ArrayList<String>();
                if (annotationList.size() > 0) {
                   // List<String> selectedlist = new ArrayList<String>();
                    for (Annotations annotations : annotationList) {
                        selectedlist.add(annotations.getContent());
                    }
                    //highlightStringAll(selectedlist);

                }
                try {
                if(searchtext.length()>0){
                    //List<String> selectedlist = new ArrayList<String>();
                    selectedlist.add(searchtext);

                    //highlightStringAll(selectedlist);
                }
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
                try {
                    if (searchtokens.length > 0) {
                        //List<String> selectedlist = new ArrayList<String>();
                        for (String s : searchtokens) {
                            selectedlist.add(s);
                        }

                        //highlightStringAll(selectedlist);
                    }
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
                highlightStringAll(selectedlist);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }

            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setTitle("Legalpedia");
            progressDialog.setMessage("Processing...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }



    }


    //String uid,String resource,String resourceid,String title,String content,String position,String comment
    class SaveAnnotation extends AsyncTask<String,Void,JSONObject> {


        private JSONObject jsonobj;
        private ProgressDialog progressDialog = new ProgressDialog(ctx,R.style.customDialog);
        @Override
        protected JSONObject doInBackground(String... params) {
            jsonobj = LGPClient.addAnnotation(params[0],params[1],params[2],params[3],params[4],params[5],params[6]);
            return jsonobj;
        }

        @Override
        protected void onPostExecute(JSONObject jsonobj) {
            try {
                progressDialog.dismiss();
                Log.d("SaveAnnotation",jsonobj.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setTitle("Legalpedia");
            progressDialog.setMessage("Processing...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }
    }



}

