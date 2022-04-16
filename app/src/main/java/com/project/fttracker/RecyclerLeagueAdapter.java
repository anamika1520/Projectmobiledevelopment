package com.project.fttracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import java.util.ArrayList;

public class RecyclerLeagueAdapter extends RecyclerView.Adapter<RecyclerLeagueAdapter.ViewHolderr> {

    ArrayList<LeagueItems> list;
    Context context;
    ArrayList<LeagueItems> listAll;

    public RecyclerLeagueAdapter(ArrayList<LeagueItems> list, Context context) {
        this.list = list;
        this.context = context;
        this.listAll = new ArrayList<>(list);
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

    class ViewHolderr extends RecyclerView.ViewHolder {

        TextView name;
        ShapeableImageView flag;

        public ViewHolderr(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvLeague);
            flag = itemView.findViewById(R.id.ivLeagueFlag);
        }
    }
}

