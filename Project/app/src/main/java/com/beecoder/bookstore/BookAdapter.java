package com.beecoder.bookstore;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beecoder.bookstore.cart.Carts;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;


public class BookAdapter extends FirestoreRecyclerAdapter<Book, BookAdapter.bookHolder> {

    private Context context;
    public OnAddToCartClickListener onAddToCartClickListener;
    private ImageView bookCover;
    private TextView title, authorName, edition, price, category;

    public interface OnAddToCartClickListener {
        void onClick(DocumentSnapshot snapshot);
    }

    public void setOnAddToCartClickListener(OnAddToCartClickListener onAddToCartClickListener) {
        this.onAddToCartClickListener = onAddToCartClickListener;
    }

    public BookAdapter(@NonNull FirestoreRecyclerOptions<Book> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull bookHolder holder, int position, @NonNull Book book) {
        holder.bookName.setText(book.getTitle());
        holder.authorName.setText(book.getAuthorName());
        holder.edition.setText(book.getEdition());
        holder.price.setText(book.getPrice());
        holder.category.setText(book.getCategory());
        if (Carts.hasAddedToCart(getSnapshots().getSnapshot(position).getId())) {
            holder.btn_addCart.setEnabled(false);
            holder.btn_addCart.setText("Added");
        } else {
            holder.btn_addCart.setEnabled(true);
            holder.btn_addCart.setText("Add to Cart");
        }
        holder.btn_addCart.setOnClickListener(v ->
                onAddToCartClickListener.onClick(getSnapshots().getSnapshot(position)));
        Glide.with(context)
                .load(book.getImageUrl())
                .centerCrop()
                .into(holder.bookCover);
        holder.bookCover.setOnClickListener(view -> showBookInfoDialog(view, book));
    }

    private void showBookInfoDialog(View v, Book book) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
        View bookInfoDialog = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.book_info_dialog, null);
        setViews(bookInfoDialog);
        title.setText(book.getTitle());
        authorName.setText(book.getAuthorName());
        price.setText(book.getPrice());
        edition.setText(book.getEdition());
        Glide.with(context)
                .load(book.getImageUrl())
                .centerCrop()
                .into(bookCover);
        category.setText(book.getCategory());
        builder.setView(bookInfoDialog);
        builder.setCancelable(true);
        builder.show();

    }

    private void setViews(View dialog) {
        bookCover = dialog.findViewById(R.id.bookCover);
        title = dialog.findViewById(R.id.txt_bookName);
        authorName = dialog.findViewById(R.id.txt_authorName);
        edition = dialog.findViewById(R.id.txt_eiditipn);
        price = dialog.findViewById(R.id.txt_price);
        category = dialog.findViewById(R.id.txt_category);
    }

    @NonNull
    @Override
    public bookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new bookHolder(view);
    }

    class bookHolder extends RecyclerView.ViewHolder {

        TextView bookName, authorName, edition, price, category;
        ImageView bookCover;
        Button btn_addCart;

        public bookHolder(@NonNull View itemView) {
            super(itemView);
            bookName = itemView.findViewById(R.id.txt_bookName);
            authorName = itemView.findViewById(R.id.txt_authorName);
            edition = itemView.findViewById(R.id.txt_eiditipn);
            price = itemView.findViewById(R.id.txt_price);
            category = itemView.findViewById(R.id.txt_category);
            bookCover = itemView.findViewById(R.id.bookCover);
            btn_addCart = itemView.findViewById(R.id.btn_addCart);
        }
    }
}
