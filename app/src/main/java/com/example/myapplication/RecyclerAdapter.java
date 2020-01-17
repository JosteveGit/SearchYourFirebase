package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<Data> list;

    public RecyclerAdapter(List<Data> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context con = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(con);
        View view = layoutInflater.inflate(R.layout.adapter_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.something.setText(
                "\n\nName: "+list.get(position).getName()+
                "\nLocation: "+list.get(position).getLocation()+
                "\nProfession: "+list.get(position).getProfession()+
                "\nNumber: "+list.get(position).getPhone()+
                "\nAddress: "+list.get(position).getAddress()+
                "\nDescription: "+list.get(position).getDescription()
        );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView something;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            something = itemView.findViewById(R.id.something);
        }
    }

}
