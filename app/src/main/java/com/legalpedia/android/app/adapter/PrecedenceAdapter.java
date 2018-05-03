package com.legalpedia.android.app.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.legalpedia.android.app.LawDetailViewActivity;
import com.legalpedia.android.app.PrecedenceViewActivity;
import com.legalpedia.android.app.R;
import com.legalpedia.android.app.models.Precedence;
import com.legalpedia.android.app.models.Sections;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 4/9/17.
 */

public class PrecedenceAdapter extends RecyclerView.Adapter<PrecedenceAdapter.PrecedenceViewHolder>{

    private Context context;
    List<Precedence> precedencelist=new ArrayList<Precedence>();
    private String category;
    public PrecedenceAdapter(Context context,String category) {
        this.precedencelist = new ArrayList<Precedence>();
        this.category = category;
        this.context = context;
    }

    @Override
    public PrecedenceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single, parent, false);
        return new PrecedenceAdapter.PrecedenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PrecedenceViewHolder holder, int position) {
        //final String sectionid=sections.get(position).getSid();
        final String precedenceid = String.valueOf(precedencelist.get(position).getId());
        holder.title.setText(precedencelist.get(position).getTitle());
        //holder.description.setText(laws.get(position).getDescription());
        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PrecedenceViewActivity.class);
                intent.putExtra("precedenceid", precedenceid);
                intent.putExtra("category", category);
                context.startActivity(intent);
            }
        });
    }

    public void addPrecedence(List<Precedence> s){

        precedencelist=s;

    }
    @Override
    public int getItemCount() {
        return precedencelist.size();
    }

    class PrecedenceViewHolder extends RecyclerView.ViewHolder {


        TextView title;
        public PrecedenceViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);


        }
    }
}
