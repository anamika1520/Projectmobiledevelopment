package com.project.fttracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collection;

public class RecyclerMatchAdapter extends RecyclerView.Adapter<RecyclerMatchAdapter.ViewHolder> implements Filterable {

    ArrayList<FixtureItems> list;
    ArrayList<FixtureItems> listAll;
    onClick listener;

    public RecyclerMatchAdapter(ArrayList<FixtureItems> list , onClick listener) {
        this.list = list;
        this.listAll = new ArrayList<>(list);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_main_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        FixtureItems currentItem = list.get(position);
        holder.matchStatus.setText(currentItem.getMatchStatus());
        holder.team1.setText(currentItem.getTeamHome());
        holder.team2.setText(currentItem.getTeamAway());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            ArrayList<FixtureItems> filteredList = new ArrayList<>();

            if(charSequence.toString().isEmpty()){
                filteredList.addAll(listAll);
            }else{
                for(FixtureItems item : listAll){
                    if(item.getTeamHome().toLowerCase().contains(charSequence.toString().toLowerCase())
                            || item.getTeamAway().toLowerCase().contains(charSequence.toString().toLowerCase())){

                        filteredList.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            list.clear();
            list.addAll((Collection<? extends FixtureItems>) filterResults.values);
            notifyDataSetChanged();

        }
    };

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView league;
        TextView matchStatus;
        TextView team1;
        TextView team2;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            matchStatus = itemView.findViewById(R.id.tvMatchStatus_RVM);
            team1 = itemView.findViewById(R.id.tvTeam1_RVM);
            team2 = itemView.findViewById(R.id.tvTeam2_RVM);
        }
        {
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onRecyclerItemClick(getAdapterPosition());
        }
    }
    interface onClick {
        void onRecyclerItemClick(int position);
    }
}
