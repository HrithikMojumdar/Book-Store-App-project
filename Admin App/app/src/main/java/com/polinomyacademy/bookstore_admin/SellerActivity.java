package com.polinomyacademy.bookstore_admin;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class SellerActivity extends AppCompatActivity {
    private RecyclerView bookListView;
    private BookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        setToolbar("Seller");
        initBookList();
    }

    private void setToolbar(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        TextView tv_title = toolbar.findViewById(R.id.tv_titleToolbar);
        tv_title.setText(title);
    }


    private void initBookList() {
        bookListView = findViewById(R.id.book_recyclerList);
        Query query = FirebaseFirestore.getInstance().collection("Books")
                .whereEqualTo("approved", false);

        FirestoreRecyclerOptions options = new FirestoreRecyclerOptions.Builder<Book>()
                .setQuery(query, Book.class)
                .build();
        adapter = new BookAdapter(options, this);
        bookListView.setAdapter(adapter);
        adapter.setOnApproveClickListener(this::approveBook);
        adapter.startListening();
    }

    private void approveBook(DocumentSnapshot snapshot) {
        Book book = snapshot.toObject(Book.class);
        book.setApproved(true);
        FirebaseFirestore.getInstance().collection("Books").document(snapshot.getId()).set(book);
        adapter.notifyDataSetChanged();
    }
}