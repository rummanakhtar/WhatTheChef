package com.example.whatthechef;

import android.app.LauncherActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<ListItem> listItems;
    private LayoutInflater layoutInflater;
    MyAdapter(Context context,List<ListItem> listItems){
        this.layoutInflater=LayoutInflater.from(context);
        this.listItems=listItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= layoutInflater.inflate(R.layout.recycler_row_item, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvTitle.setText(listItems.get(position).getItemName());
        holder.tvDescription.setText(listItems.get(position).getItemDescription());
        Picasso.get().load(listItems.get(position).getItemImage()).into(holder.ivImage);


    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;
        TextView tvDescription;
        ImageView ivImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle= itemView.findViewById(R.id.tvTitle);
            tvDescription= itemView.findViewById(R.id.tvDescription);
            ivImage= itemView.findViewById(R.id.ivImage);


        }
    }
}
