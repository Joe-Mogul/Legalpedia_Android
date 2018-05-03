package com.legalpedia.android.app.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.legalpedia.android.app.R;
import com.legalpedia.android.app.models.Maxims;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 11/19/16.
 */

public class MaximsDetailAdapter extends RecyclerView.Adapter<MaximsDetailAdapter.MaximsDetailViewHolder> {

    private List<Maxims> maxims;
    private int rowLayout;
    private Context context;



    ProgressBar progress;
    class MaximsDetailViewHolder extends RecyclerView.ViewHolder {


        TextView title;
        TextView content;
        public MaximsDetailViewHolder(View v) {
            super(v);
            //moviesLayout = (LinearLayout) v.findViewById(R.id.m);
            title = (TextView) v.findViewById(R.id.title);
            content = (TextView) v.findViewById(R.id.content);

        }
    }

    public MaximsDetailAdapter(Context context) {
        this.maxims = new ArrayList<Maxims>();

        this.context = context;
    }


    public void setMaxims(List<Maxims> m){
        for(Maxims a: m) {
            this.maxims.add(a);
        }
    }
    @Override
    public MaximsDetailAdapter.MaximsDetailViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_maximsdetail_item, parent, false);
        return new MaximsDetailAdapter.MaximsDetailViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MaximsDetailAdapter.MaximsDetailViewHolder holder, final int position) {
        holder.title.setText(maxims.get(position).getTitle());
        Typeface face=Typeface.createFromAsset(context.getAssets(),
                "Verdana.ttf");

        holder.content.setTypeface(face);
        holder.content.setText(Html.fromHtml(maxims.get(position).getContent()));



    }

    @Override
    public int getItemCount() {
        return maxims.size();
    }
}

