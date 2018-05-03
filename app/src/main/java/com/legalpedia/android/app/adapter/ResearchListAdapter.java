package com.legalpedia.android.app.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.legalpedia.android.app.App;
import com.legalpedia.android.app.NoteDetailActivity;
import com.legalpedia.android.app.R;
import com.legalpedia.android.app.ResearchDetailActivity;
import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.Researchrequest;
import com.legalpedia.android.app.models.ResearchrequestDao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 4/9/17.
 */

public class ResearchListAdapter extends RecyclerView.Adapter<ResearchListAdapter.ResearchListViewHolder>{
    private Context context;
    private DaoSession daoSession;
    private ResearchrequestDao researchrequestDao;
    List<Researchrequest> researchRequests=new ArrayList<Researchrequest>();
    boolean undoOn; // is undo on, you can turn it on from the toolbar menu
    public ResearchListAdapter(Context context) {
        this.researchRequests = new ArrayList<Researchrequest>();
        Activity activity =(Activity)context;
        this.context = context;
        daoSession = ((App)   ((Activity) context).getApplication()).getDaoSession();
        researchrequestDao = daoSession.getResearchrequestDao();

    }

    @Override
    public ResearchListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.research_list, parent, false);
        return new ResearchListAdapter.ResearchListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ResearchListViewHolder holder, int position) {
        final String noteid=String.valueOf(researchRequests.get(position).getId());

        holder.title.setText(researchRequests.get(position).getTitle());
        String formatteddate=new SimpleDateFormat("dd MMMM yyyy").format(researchRequests.get(position).getCreatedate());
        holder.date.setText(formatteddate);
        final Researchrequest note = researchRequests.get(position);
        holder.delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //notesDao.delete(note);
            }

        });
        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d("ResearchListViewHolder","Note Clicked "+note.getId());
                Intent intent = new Intent(context, ResearchDetailActivity.class);
                intent.putExtra("requestid",String.valueOf(note.getId()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return researchRequests.size();
    }

    public void setUndoOn(boolean undoOn) {
        this.undoOn = undoOn;
    }

    public boolean isUndoOn() {
        return undoOn;
    }

    public void pendingRemoval(int position) {
        Researchrequest item = researchRequests.get(position);
        if (!researchRequests.contains(item)) {
            researchRequests.add(item);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
           /** Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove(researchRequests.indexOf(item));
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(item, pendingRemovalRunnable);
            */
        }
    }

    public void remove(int position) {
        Researchrequest item = researchRequests.get(position);
        Log.d("Remove","Removing "+item.getTitle());
        researchrequestDao.delete(item);
        researchRequests.remove(position);
        //recycler.removeViewAt(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, researchRequests.size());


    }

    public boolean isPendingRemoval(int position) {
        Researchrequest item = researchRequests.get(position);
        return researchRequests.contains(item);
    }



    public void addResearchRequest(List<Researchrequest> l){
        this.researchRequests=l;
    }
    class ResearchListViewHolder extends RecyclerView.ViewHolder {


        TextView title;
        TextView date;
        ImageView delete;
        public ResearchListViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            date = (TextView) v.findViewById(R.id.date);
            delete = (ImageView) v.findViewById(R.id.delete);

        }
    }

}
