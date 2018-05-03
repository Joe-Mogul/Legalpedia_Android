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
import com.legalpedia.android.app.RulesListActivity;
import com.legalpedia.android.app.models.Rules;
import com.legalpedia.android.app.models.RulesStat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 2/10/17.
 */

public class StateRulesAdapter extends RecyclerView.Adapter<StateRulesAdapter.RulesListViewHolder> {

    private List<String> rules;
    private List<RulesStat> rulesstatlist;
    private int rowLayout;
    private Context context;




    ProgressBar progress;


    public StateRulesAdapter(Context context) {
        this.rules = new ArrayList<String>();
        this.rulesstatlist = new ArrayList<RulesStat>();
        this.context = context;
    }


    public void setRules(List<String> ruleslist){
        for(String s: ruleslist) {
            if(!this.rules.contains(s)) {
                this.rules.add(s);
            }
        }
    }


    public void setRulesStat(List<RulesStat> rulesstatlist){
        this.rulesstatlist.clear();
        this.rulesstatlist =  rulesstatlist;
        /**for(RulesStat s: rulesstatlist) {
            if(!this.rulesstatlist.contains(s)) {
                this.rulesstatlist.add(s);
            }
        }*/
    }

    public void addRulesStat(List<RulesStat> rulesstatlist){
        for(RulesStat s: rulesstatlist) {
            if(!this.rulesstatlist.contains(s)) {
                this.rulesstatlist.add(s);
            }
        }


    }

    @Override
    public StateRulesAdapter.RulesListViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_article_basic, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_rules_item, parent, false);
        return new StateRulesAdapter.RulesListViewHolder(view);
    }


    @Override
    public void onBindViewHolder(StateRulesAdapter.RulesListViewHolder holder, final int position) {

        String rulesdecription ="Rules:"+rulesstatlist.get(position).getCount();
        rulesdecription="";
        holder.title.setText(rulesstatlist.get(position).getTitle());
        holder.description.setText(rulesdecription);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //long id=rules.get(position).getId();
                String name=rulesstatlist.get(position).getTitle();
                Intent intent = new Intent(context, RulesListActivity.class);

                intent.putExtra("name", name);

                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        //return rules.size();
        return rulesstatlist.size();
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

