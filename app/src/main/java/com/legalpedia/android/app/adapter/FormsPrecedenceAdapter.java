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
import com.legalpedia.android.app.PrecedenceListActivity;
import com.legalpedia.android.app.R;
import com.legalpedia.android.app.models.Precedence;
import com.legalpedia.android.app.models.PrecedenceStat;
import com.legalpedia.android.app.models.Rules;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 2/10/17.
 */

public class FormsPrecedenceAdapter extends RecyclerView.Adapter<FormsPrecedenceAdapter.FormsPrecedenceViewHolder> {

    private List<Precedence> precedencelist;
    private List<String> categorylist;
    private List<PrecedenceStat> precedencestatlist;
    private int rowLayout;
    private Context context;




    ProgressBar progress;


    public FormsPrecedenceAdapter(Context context) {
        this.precedencelist = new ArrayList<Precedence>();
        this.categorylist =  new ArrayList<String>();
        this.precedencestatlist = new ArrayList<PrecedenceStat>();
        this.context = context;
    }


    public void setPreced(List<Precedence> plist){
        this.precedencelist = plist;
    }

    public void setCategory(List<String> clist){
        for(String s: clist) {
            if(!this.categorylist.contains(s)) {
                this.categorylist.add(s);
            }
        }
    }


    public void addPrecedence(List<PrecedenceStat> plist){
        for(PrecedenceStat s: plist) {
            if(!this.precedencestatlist.contains(s)) {
                this.precedencestatlist.add(s);
            }
        }
    }

    public void setPrecedence(List<PrecedenceStat> plist){
        this.precedencestatlist = plist;
    }


    @Override
    public FormsPrecedenceAdapter.FormsPrecedenceViewHolder onCreateViewHolder(ViewGroup parent,
                                                                               int viewType) {
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_article_basic, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_precedence_item, parent, false);
        return new FormsPrecedenceAdapter.FormsPrecedenceViewHolder(view);
    }


    @Override
    public void onBindViewHolder(FormsPrecedenceAdapter.FormsPrecedenceViewHolder holder, final int position) {
        String counttext = "Forms: "+precedencestatlist.get(position).getCount();
        counttext="";
        holder.title.setText(precedencestatlist.get(position).getTitle().replace("_"," ").toUpperCase());
        holder.description.setText(counttext);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //long id=precedencelist.get(position).getId();
                String category = precedencestatlist.get(position).getTitle();
                Intent intent = new Intent(context, PrecedenceListActivity.class);

                intent.putExtra("category", category);

                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        //return categorylist.size();
        return precedencestatlist.size();
        //return precedencelist.size();
    }

    class FormsPrecedenceViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView description;

        public FormsPrecedenceViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            description = (TextView) v.findViewById(R.id.description);

        }
    }
}

