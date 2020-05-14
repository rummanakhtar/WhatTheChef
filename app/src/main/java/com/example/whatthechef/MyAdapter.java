package com.example.whatthechef;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private String string="null";
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
        holder.cardFlag.setText(listItems.get(position).getCardFlag());
        Picasso.get().load(listItems.get(position).getItemImage()).into(holder.ivImage);
        Picasso.get().load(listItems.get(position).getColorStrip()).into(holder.colorStrip);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;
        TextView tvDescription;
        ImageView ivImage;
        TextView cardFlag;
        ImageView colorStrip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //set onClickListener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=getAdapterPosition();
                    if(listItems.get(pos).getCardFlag()==null){
                        listItems.get(pos).setCardFlag("0");
                    }
                    if(listItems.get(pos).getCardFlag().equals("0")){
                        Intent i=new Intent(v.getContext(),SecondRecipe.class);
                        jsonSetter(i);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        v.getContext().startActivity(i);
                    }
                    else{
                        Intent i=new Intent(v.getContext(),FinalRecipe.class);
                        jsonSetter(i);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        v.getContext().startActivity(i);
                    }
                }

                private void jsonSetter(Intent i) {
                    string=listItems.get(getAdapterPosition()).getItemName();
                    string=string.toLowerCase().replaceAll("\\s","");
                    i.putExtra("jsonURL", "https://rummanakhtar.github.io/dataforwtc/"+string+".json");
                }
            });
            cardFlag=itemView.findViewById(R.id.cardflag);
            tvTitle= itemView.findViewById(R.id.tvTitle);
            tvDescription= itemView.findViewById(R.id.tvDescription);
            ivImage= itemView.findViewById(R.id.ivImage);
            colorStrip=itemView.findViewById(R.id.colorstrip);
        }
    }
}
