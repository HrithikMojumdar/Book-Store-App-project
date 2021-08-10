package com.polinomyacademy.bookstore_admin;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BuyerAdapter extends RecyclerView.Adapter<BuyerAdapter.BuyerViewHolder> {
    private Context context;
    private ArrayList<User> buyer;

    public BuyerAdapter(Context context, ArrayList<User> buyer) {
        this.context = context;
        this.buyer = buyer;
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onClick(int i);
    }

    void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    static class BuyerViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_number;

        BuyerViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_number = itemView.findViewById(R.id.tv_number);

            itemView.setOnClickListener(v -> onItemClickListener.onClick(getAdapterPosition()));
        }
    }

    @NonNull
    @Override
    public BuyerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row, parent, false);
        return new BuyerViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyerViewHolder holder, int position) {
        holder.tv_name.setText(buyer.get(position).getName());
        holder.tv_number.setText(buyer.get(position).getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return buyer.size();
    }
}
