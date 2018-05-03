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
import com.legalpedia.android.app.RulesDetailViewActivity;
import com.legalpedia.android.app.models.Rules;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 2/10/17.
 */

public class RulesListAdapter extends RecyclerView.Adapter<RulesListAdapter.RulesListViewHolder> {

    private List<Rules> rules;
    private int rowLayout;
    private Context context;

    private String name;


    ProgressBar progress;


    public RulesListAdapter(Context context,String name) {
        this.rules = new ArrayList<Rules>();
        this.name  = name;
        this.context = context;
    }


    public void setRules(List<Rules> ruleslist){
        for(Rules s: ruleslist) {
            if(!this.rules.contains(s)) {
                this.rules.add(s);
            }
        }
    }
    @Override
    public RulesListAdapter.RulesListViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_article_basic, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_rules_item, parent, false);
        return new RulesListAdapter.RulesListViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RulesListAdapter.RulesListViewHolder holder, final int position) {

        holder.title.setText(rules.get(position).getTitle());
        holder.description.setText(rules.get(position).getSection());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id=rules.get(position).getId();
                Intent intent = new Intent(context, RulesDetailViewActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("id",String.valueOf(id));

                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return rules.size();
    }

    class RulesListViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView description;

        public RulesListViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            description = (TextView) v.findViewById(R.id.description);


        }
    }
}

