package com.legalpedia.android.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.legalpedia.android.app.DocumentViewActivity;
import com.legalpedia.android.app.R;
import com.legalpedia.android.app.models.Summary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 2/10/17.
 */

public class CasesListAdapter  extends RecyclerView.Adapter<CasesListAdapter.CaseListViewHolder> {

    private List<Summary> summaries;
    private Context context;



    ProgressBar progress;
    class CaseListViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView year;


        public CaseListViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            year = (TextView) v.findViewById(R.id.year);

        }
    }

    public CasesListAdapter(Context context) {
        this.summaries = new ArrayList<Summary>();

        this.context = context;
    }


    public void setCases(List<Summary> summaries){
        for(Summary s: summaries) {
            this.summaries.add(s);
        }
    }
    @Override
    public CasesListAdapter.CaseListViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_article_basic, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_case_item, parent, false);
        return new CasesListAdapter.CaseListViewHolder(view);
    }


    @Override
    public void onBindViewHolder(CasesListAdapter.CaseListViewHolder holder, final int position) {
        holder.title.setText(summaries.get(position).getCase_title());
        holder.year.setText("2016");


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id=summaries.get(position).getId();
                Intent intent = new Intent(context, DocumentViewActivity.class);

                intent.putExtra("_id", id);

                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return summaries.size();
    }
}


