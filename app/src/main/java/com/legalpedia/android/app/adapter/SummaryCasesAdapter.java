package com.legalpedia.android.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.legalpedia.android.app.JudgementDetailActivity;
import com.legalpedia.android.app.JudgementFullDetailViewActivity;
import com.legalpedia.android.app.R;
import com.legalpedia.android.app.models.Summary;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 11/19/16.
 */

public class SummaryCasesAdapter extends RecyclerView.Adapter<SummaryCasesAdapter.SummaryCasesViewHolder> {

    private List<Summary> summaries;
    private int rowLayout;
    private Context context;



    ProgressBar progress;
    class SummaryCasesViewHolder extends RecyclerView.ViewHolder {


        TextView title;
        TextView year;
        public SummaryCasesViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            year = (TextView) v.findViewById(R.id.year);

        }
    }

    public SummaryCasesAdapter(Context context) {
        this.summaries = new ArrayList<Summary>();

        this.context = context;
    }


    public void setSummary(List<Summary> summaries){
        for(Summary a: summaries) {
            if(!this.summaries.contains(a)) {
                this.summaries.add(a);
            }
        }
    }
    @Override
    public SummaryCasesAdapter.SummaryCasesViewHolder onCreateViewHolder(ViewGroup parent,
                                                                         int viewType) {
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_article_basic, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_case_item, parent, false);
        return new SummaryCasesAdapter.SummaryCasesViewHolder(view);
    }


    @Override
    public void onBindViewHolder(SummaryCasesAdapter.SummaryCasesViewHolder holder, final int position) {
        holder.title.setText(summaries.get(position).getCase_title());
        //holder.year.setText(new Date(summaries.get(position).getDate()));
        String formatteddate=new SimpleDateFormat("dd MMMM yyyy").format(summaries.get(position).getDate());
        holder.year.setText(formatteddate);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id=summaries.get(position).getId();
                Intent intent = new Intent(context, JudgementFullDetailViewActivity.class);

                intent.putExtra("judgementid", String.valueOf(id));

                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return summaries.size();
    }
}

