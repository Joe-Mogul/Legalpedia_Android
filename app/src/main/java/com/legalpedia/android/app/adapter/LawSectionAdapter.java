package com.legalpedia.android.app.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.legalpedia.android.app.LawDetailViewActivity;
import com.legalpedia.android.app.R;
import com.legalpedia.android.app.models.Sections;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 4/9/17.
 */

public class LawSectionAdapter extends RecyclerView.Adapter<LawSectionAdapter.LawSectionViewHolder>{

    private Context context;
    List<Sections> sections=new ArrayList<Sections>();
    public LawSectionAdapter(Context context) {
        this.sections = new ArrayList<Sections>();

        this.context = context;
    }

    @Override
    public LawSectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single, parent, false);
        return new LawSectionAdapter.LawSectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LawSectionViewHolder holder, int position) {
        //final String sectionid=sections.get(position).getSid();
        final String sectionid = String.valueOf(sections.get(position).getId());
        holder.title.setText(sections.get(position).getTitle());
        //holder.description.setText(laws.get(position).getDescription());
        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LawDetailViewActivity.class);
                intent.putExtra("sectionid", sectionid);
                context.startActivity(intent);
            }
        });
    }

    public void addSections(List<Sections> s){

            sections=s;

    }
    @Override
    public int getItemCount() {
        return sections.size();
    }

    class LawSectionViewHolder extends RecyclerView.ViewHolder {


        TextView title;
        public LawSectionViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);


        }
    }
}
