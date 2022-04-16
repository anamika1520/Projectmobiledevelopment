package com.project.fttracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.Collection;

public class RecyclerLeagueAdapter extends RecyclerView.Adapter<RecyclerLeagueAdapter.ViewHolderr> implements Filterable {

    ArrayList<LeagueItems> list;
    Context context;
    ArrayList<LeagueItems> listAll;
    OnLeagueClick listener;

    public RecyclerLeagueAdapter(ArrayList<LeagueItems> list, Context context, OnLeagueClick listener) {
        this.list = list;
        this.context = context;
        this.listAll = new ArrayList<>(list);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolderr onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_league_items, parent, false);
        return new ViewHolderr(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderr holder, int position) {
        LeagueItems currentItem = list.get(position);
        holder.name.setText(currentItem.getName());
        Glide.with(context).load(currentItem.getFlag()).into(holder.flag);
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

            ArrayList<LeagueItems> filteredList = new ArrayList<>();

            if (charSequence.toString().isEmpty()) {
                filteredList.addAll(listAll);
            } else {
                for (LeagueItems item : listAll) {
                    if (item.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {

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
            list.addAll((Collection<? extends LeagueItems>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    class ViewHolderr extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        ShapeableImageView flag;

        public ViewHolderr(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvLeague);
            flag = itemView.findViewById(R.id.ivLeagueFlag);
        }

        {
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onLeagueItemClick(getAdapterPosition());
        }
    }

    interface OnLeagueClick {
        void onLeagueItemClick(int position);
    }
}

