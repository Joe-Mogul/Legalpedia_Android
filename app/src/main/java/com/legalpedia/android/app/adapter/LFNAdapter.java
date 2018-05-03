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

import com.legalpedia.android.app.LawDetailViewActivity;
import com.legalpedia.android.app.R;
import com.legalpedia.android.app.SectionListActivity;
import com.legalpedia.android.app.models.Laws;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 11/19/16.
 */

public class LFNAdapter extends RecyclerView.Adapter<LFNAdapter.LFNViewHolder> {

    private List<Laws> laws;
    private int rowLayout;
    private Context context;



    ProgressBar progress;
    class LFNViewHolder extends RecyclerView.ViewHolder {


        TextView title;
        TextView year;
        public LFNViewHolder(View v) {
            super(v);
            //moviesLayout = (LinearLayout) v.findViewById(R.id.m);
            title = (TextView) v.findViewById(R.id.title);
            year = (TextView) v.findViewById(R.id.year);

        }
    }

    public LFNAdapter(Context context) {
        this.laws = new ArrayList<Laws>();

        this.context = context;
    }


    public boolean isEmpty(){
        boolean isempty = false;

        if(this.laws.size()<=0){
            isempty= true;
        }

        return isempty;
    }


    public void clear(){
        this.laws.clear();
    }

    public void setLaws(List<Laws> laws){
        this.laws = laws;
    }


    public void addLaws(List<Laws> laws){
        for(Laws l: laws) {
            if(!this.laws.contains(l)) {
                this.laws.add(l);
            }
        }
    }



    @Override
    public LFNAdapter.LFNViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_article_basic, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_lfn_item, parent, false);
        return new LFNAdapter.LFNViewHolder(view);
    }


    @Override
    public void onBindViewHolder(LFNAdapter.LFNViewHolder holder, final int position) {


        holder.title.setText(laws.get(position).getTitle());
        //String date = new SimpleDateFormat("dd MMMM yyyy").format(laws.get(position).getDate());
        String date ="";
        holder.year.setText(date);
        //description.setText(articles.get(position).getContent());

        //progress.setProgress(30);
        //progress.setVisibility(View.INVISIBLE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=String.valueOf(laws.get(position).getId());
                String sid=String.valueOf(laws.get(position).getSid());
                Intent intent = new Intent(context, SectionListActivity.class);

                intent.putExtra("lawid", id);
                intent.putExtra("sid", sid);

                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return laws.size();
    }
}

