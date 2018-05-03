package com.legalpedia.android.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.legalpedia.android.app.JudgementDetailActivity;
import com.legalpedia.android.app.JudgementFullDetailViewActivity;
import com.legalpedia.android.app.LawDetailViewActivity;
import com.legalpedia.android.app.PrecedenceViewActivity;
import com.legalpedia.android.app.R;
import com.legalpedia.android.app.RulesDetailViewActivity;
import com.legalpedia.android.app.models.SearchResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 7/2/17.
 */

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder>  {
    private List<SearchResult> results;
    private int rowLayout;
    private Context context;


    public SearchResultAdapter(Context context) {
        this.results = new ArrayList<SearchResult>();

        this.context = context;
    }


    public void setResults(List<SearchResult> resultslist,boolean ispaged){

            for (SearchResult s : resultslist) {
                /**if (!this.results.contains(s)) {

                    this.results.add(s);
                }*/
                this.results.add(s);
            }
           // Log.d("SearchAdapter","Finished the search");
    }


    public void clearResults(){
        this.results.clear();

    }


    @Override
    public SearchResultAdapter.SearchResultViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_article_basic, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_searchresult_item, parent, false);
        return new SearchResultAdapter.SearchResultViewHolder(view);
    }

    private SpannableString highlightString(String content,String selected) {
        //Get the text from text view and create a spannable string
        SpannableString spannableString = new SpannableString(content);

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
        return spannableString;
    }


    @Override
    public void onBindViewHolder(SearchResultAdapter.SearchResultViewHolder holder,final int position) {
        String searchtext=results.get(position).getQuery();
        holder.title.setText(results.get(position).getTitle());
        holder.resource.setText(results.get(position).getResource());
        holder.year.setText(results.get(position).getYear());
        if(Build.VERSION.SDK_INT>=24) {
            String content=Html.fromHtml(results.get(position).getDescription(), Html.FROM_HTML_MODE_LEGACY).toString();
            holder.description.setText(highlightString(content,searchtext));
        }
        else{
            String content=Html.fromHtml(results.get(position).getDescription()).toString();
            holder.description.setText(highlightString(content,searchtext));
        }
        String ratioheader="";
        if(results.get(position).getRatioheader().length()>0){
            ratioheader = results.get(position).getRatioheader();
        }
        holder.ratio.setText(ratioheader);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Search","Clicked");
                String id=results.get(position).getResourceid();
                String resource = results.get(position).getResource();
                Log.d("Resource",resource);
                Log.d("ID ",String.valueOf(id));
                Log.d("searchtext ",results.get(position).getQuery());
                Intent intent = null;
                if(resource.equals("Judgement")) {
                    intent = new Intent(context, JudgementFullDetailViewActivity.class);
                    intent.putExtra("judgementid", String.valueOf(id));
                    intent.putExtra("searchtext", results.get(position).getQuery());
                    String[] tokenlist = new String[results.get(position).getTokens().size()];
                    int i = 0;
                    for (String s : results.get(position).getTokens()) {
                        tokenlist[i] = s;
                        i++;
                    }
                    intent.putExtra("tokenlist", tokenlist);
                    context.startActivity(intent);
                }
                if(resource.equals("Subject Matter Index")) {
                    intent = new Intent(context, JudgementFullDetailViewActivity.class);
                    intent.putExtra("judgementid", String.valueOf(id));
                    intent.putExtra("searchtext", results.get(position).getQuery());
                    String[] tokenlist = new String[results.get(position).getTokens().size()];
                    int i = 0;
                    for (String s : results.get(position).getTokens()) {
                        tokenlist[i] = s;
                        i++;
                    }
                    intent.putExtra("tokenlist", tokenlist);
                    context.startActivity(intent);
                }
                if(resource.equals("Laws of the Federation")) {
                    intent = new Intent(context, LawDetailViewActivity.class);
                    intent.putExtra("sectionid", String.valueOf(id));
                    intent.putExtra("searchtext", results.get(position).getQuery());
                    context.startActivity(intent);

                }
                if(resource.equals("Rules")) {
                    intent = new Intent(context, RulesDetailViewActivity.class);
                    intent.putExtra("id", String.valueOf(id));
                    intent.putExtra("name", results.get(position).getQuery());
                    intent.putExtra("searchtext", results.get(position).getQuery());
                    context.startActivity(intent);

                }
                if(resource.equals("Precedence")) {
                    intent = new Intent(context, PrecedenceViewActivity.class);
                    intent.putExtra("precedenceid", String.valueOf(id));
                    intent.putExtra("searchtext", results.get(position).getQuery());
                    context.startActivity(intent);

                }
                if(resource.equals("Annotation")) {
                    intent = new Intent(context, JudgementFullDetailViewActivity.class);
                    intent.putExtra("judgementid", String.valueOf(id));
                    intent.putExtra("searchtext", results.get(position).getQuery());
                    String[] tokenlist = new String[results.get(position).getTokens().size()];
                    int i = 0;
                    for (String s : results.get(position).getTokens()) {
                        tokenlist[i] = s;
                        i++;
                    }
                    intent.putExtra("tokenlist", tokenlist);
                    context.startActivity(intent);
                }





            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    class SearchResultViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView ratio;
        private TextView description;
        private TextView resource;
        private TextView year;

        public SearchResultViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            ratio = (TextView) v.findViewById(R.id.ratio);
            description = (TextView) v.findViewById(R.id.description);
            resource = (TextView) v.findViewById(R.id.resource);
            year = (TextView) v.findViewById(R.id.year);

        }
    }
}
