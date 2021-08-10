package com.polinomyacademy.bookstore_admin;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class CategoryAdapter extends FirestoreRecyclerAdapter<Category, CategoryAdapter.CategoryViewHolder> {

    public CategoryAdapter(FirestoreRecyclerOptions<Category> options) {
        super(options);
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        private TextView title;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.category_name_tv);
        }
    }

    @Override
    protected void onBindViewHolder(CategoryViewHolder holder, int position, Category category) {
        holder.title.setText(category.getName());
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(itemView);
    }
}
