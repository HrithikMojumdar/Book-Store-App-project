package com.beecoder.bookstore;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class CategoryAdapter extends FirestoreRecyclerAdapter<Category, CategoryAdapter.CategoryViewHolder> {
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onClick(DocumentSnapshot snapshot);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public CategoryAdapter(FirestoreRecyclerOptions<Category> options) {
        super(options);

    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        //private Button button;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_category_title);
           /* button=itemView.findViewById(R.id.btn_addCart);
            button.setOnClickListener(view -> AddCart());*/
            itemView.setOnClickListener(v -> onItemClickListener.onClick(getSnapshots().getSnapshot(getAdapterPosition())));
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
