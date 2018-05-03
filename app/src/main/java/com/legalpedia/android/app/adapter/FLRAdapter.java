package com.legalpedia.android.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.legalpedia.android.app.DocumentViewActivity;
import com.legalpedia.android.app.R;
import com.legalpedia.android.app.models.Articles;
import com.legalpedia.android.app.models.FLR;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 11/19/16.
 */

public class FLRAdapter extends RecyclerView.Adapter<FLRAdapter.FLRViewHolder> {

    private List<FLR> flrList;
    private int rowLayout;
    private Context context;



    ProgressBar progress;
    class FLRViewHolder extends RecyclerView.ViewHolder {


        TextView title;
        TextView year;
        public FLRViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            year = (TextView) v.findViewById(R.id.year);

        }
    }

    public FLRAdapter(Context context) {
        this.flrList = new ArrayList<FLR>();

        this.context = context;
    }


    public void setResources(List<FLR> flrlist){
        for(FLR a: flrlist) {
            this.flrList.add(a);
        }
    }
    @Override
    public FLRAdapter.FLRViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_case_item, parent, false);
        return new FLRAdapter.FLRViewHolder(view);
    }


    @Override
    public void onBindViewHolder(FLRAdapter.FLRViewHolder holder, final int position) {
        holder.title.setText(flrList.get(position).getTitle());
        holder.year.setText("2016");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id=flrList.get(position).getId();
                Intent intent = new Intent(context, DocumentViewActivity.class);

                intent.putExtra("_id", id);

                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return flrList.size();
    }
}

