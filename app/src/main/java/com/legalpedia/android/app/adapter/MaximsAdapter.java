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
import com.legalpedia.android.app.MaximsActivity;
import com.legalpedia.android.app.R;
import com.legalpedia.android.app.models.Articles;
import com.legalpedia.android.app.models.Maxims;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 11/19/16.
 */

public class MaximsAdapter extends RecyclerView.Adapter<MaximsAdapter.MaximsHolder> {

    private List<Maxims> maxims;
    private int rowLayout;
    private Context context;



    ProgressBar progress;
    class MaximsHolder extends RecyclerView.ViewHolder {


        TextView title;
        public MaximsHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);

        }
    }

    public MaximsAdapter(Context context) {
        this.maxims = new ArrayList<Maxims>();

        this.context = context;
    }


    public void setMaxims(List<Maxims> m){
        for(Maxims a: m) {
            this.maxims.add(a);
        }
    }
    @Override
    public MaximsAdapter.MaximsHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_article_basic, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_maxims_item, parent, false);
        return new MaximsAdapter.MaximsHolder(view);
    }


    @Override
    public void onBindViewHolder(MaximsAdapter.MaximsHolder holder, final int position) {
        holder.title.setText(maxims.get(position).getTitle());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id=maxims.get(position).getId();
                Intent intent = new Intent(context, MaximsActivity.class);

                intent.putExtra("maximid", String.valueOf(id));

                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return maxims.size();
    }
}

