package com.polinomyacademy.bookstore_admin;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BuyerBookAdapter extends RecyclerView.Adapter<BuyerBookAdapter.BuyerBookViewHolder> {
    private Context context;
    private ArrayList<Book> books;

    public BuyerBookAdapter(Context context, ArrayList<Book> books) {
        this.context = context;
        this.books = books;
    }

    private OnItemButtonClickListener onItemButtonClickListener;

    public interface OnItemButtonClickListener {
        void onClick(int i);
    }

    public void setOnItemButtonClickListener(OnItemButtonClickListener onItemButtonClickListener) {
        this.onItemButtonClickListener = onItemButtonClickListener;
    }

    static class BuyerBookViewHolder extends RecyclerView.ViewHolder {
        TextView bookName, authorName, edition, price, category;
        ImageView bookCover;
        Button btn_markAsSold;

        BuyerBookViewHolder(@NonNull View itemView, OnItemButtonClickListener onItemButtonClickListener) {
            super(itemView);
            bookName = itemView.findViewById(R.id.txt_bookName);
            authorName = itemView.findViewById(R.id.txt_authorName);
            edition = itemView.findViewById(R.id.txt_eiditipn);
            price = itemView.findViewById(R.id.txt_price);
            category = itemView.findViewById(R.id.txt_category);
            bookCover = itemView.findViewById(R.id.bookCover);
            btn_markAsSold = itemView.findViewById(R.id.btn_approve);
            itemView.setOnClickListener(v -> onItemButtonClickListener.onClick(getAdapterPosition()));
        }
    }

    @NonNull
    @Override
    public BuyerBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new BuyerBookViewHolder(itemView, onItemButtonClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyerBookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.bookName.setText(book.getTitle());
        holder.authorName.setText(book.getAuthorName());
        holder.edition.setText(book.getEdition());
        holder.price.setText(book.getPrice());
        holder.category.setText(book.getCategory());
        holder.btn_markAsSold.setOnClickListener(v ->
                onItemButtonClickListener.onClick(position));
        Glide.with(context)
                .load(book.getImageUrl())
                .centerCrop()
                .into(holder.bookCover);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}
