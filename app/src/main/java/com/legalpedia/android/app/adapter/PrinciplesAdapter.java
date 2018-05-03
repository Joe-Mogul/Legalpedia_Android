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
import com.legalpedia.android.app.R;
import com.legalpedia.android.app.SummaryCasesViewActivity;
import com.legalpedia.android.app.models.Principles;
import com.legalpedia.android.app.models.PrinciplesStat;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 11/19/16.
 */

public class PrinciplesAdapter extends RecyclerView.Adapter<PrinciplesAdapter.PrinciplesViewHolder> {

    private List<PrinciplesStat> principles;
    private int rowLayout;
    private Context context;



    ProgressBar progress;
    class PrinciplesViewHolder extends RecyclerView.ViewHolder {


        TextView title;
        TextView judgements;
        public PrinciplesViewHolder(View v) {
            super(v);
            //moviesLayout = (LinearLayout) v.findViewById(R.id.m);
            title = (TextView) v.findViewById(R.id.title);
            judgements = (TextView) v.findViewById(R.id.judgements);

        }
    }

    public PrinciplesAdapter(Context context) {
        this.principles = new ArrayList<PrinciplesStat>();

        this.context = context;
    }


    public void addPrinciplesStat(List<PrinciplesStat> listprinciples){
        for(PrinciplesStat p: listprinciples) {
            this.principles.add(p);
        }
    }



    /**
    public void addPrinciples(List<Principles> principles){
        for(Principles p: principles) {
            this.principles.add(p);
        }
    }*/



    @Override
    public PrinciplesAdapter.PrinciplesViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_principles_item, parent, false);
        return new PrinciplesAdapter.PrinciplesViewHolder(view);
    }


    @Override
    public void onBindViewHolder(PrinciplesAdapter.PrinciplesViewHolder holder, final int position) {
        String judgementtext = "Judgements: "+principles.get(position).getCount();
        judgementtext="";
        holder.title.setText(principles.get(position).getTitle());
        holder.judgements.setText(judgementtext);
        //description.setText(articles.get(position).getContent());

        //progress.setProgress(30);
        //progress.setVisibility(View.INVISIBLE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id=principles.get(position).getId();
                String title  = principles.get(position).getTitle();
                Intent intent = new Intent(context, SummaryCasesViewActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("principleid", String.valueOf(id));

                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return principles.size();
    }
}

