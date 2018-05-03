package com.legalpedia.android.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.legalpedia.android.app.DocumentViewActivity;
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

public class InvestmentSecAdapter extends RecyclerView.Adapter<InvestmentSecAdapter.SupremeCourtViewHolder> {

    private List<Summary> summaries;
    private int rowLayout;
    private Context context;



    ProgressBar progress;
    class SupremeCourtViewHolder extends RecyclerView.ViewHolder {


        TextView title;
        TextView year;
        public SupremeCourtViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            year = (TextView) v.findViewById(R.id.year);

        }
    }

    public InvestmentSecAdapter(Context context) {
        this.summaries = new ArrayList<Summary>();

        this.context = context;
    }


    public void setSummary(List<Summary> summaries){
        this.summaries = summaries;
    }


    public void addSummary(List<Summary> summaries){
        for(Summary a: summaries) {
            if(!this.summaries.contains(a)) {
                this.summaries.add(a);
            }
        }
    }



    @Override
    public InvestmentSecAdapter.SupremeCourtViewHolder onCreateViewHolder(ViewGroup parent,
                                                                          int viewType) {
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_article_basic, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_case_item, parent, false);
        return new InvestmentSecAdapter.SupremeCourtViewHolder(view);
    }


    @Override
    public void onBindViewHolder(InvestmentSecAdapter.SupremeCourtViewHolder holder, final int position) {
        holder.title.setText(summaries.get(position).getCase_title());
        if (summaries.get(position).getJudgement_date() != null){
            String formatteddate = new SimpleDateFormat("dd MMMM yyyy").format(summaries.get(position).getDate());
            holder.year.setText(formatteddate);
        }else if(summaries.get(position).getDate()!= null){
            String formatteddate = new SimpleDateFormat("dd MMMM yyyy").format(summaries.get(position).getDate());
            holder.year.setText(formatteddate);
        }else{
            holder.year.setText("Not Available");
        }


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

