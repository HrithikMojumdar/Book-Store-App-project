package com.beecoder.bookstore;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.beecoder.bookstore.database.CartDatabase;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CategoryItemActivity extends AppCompatActivity {
    private RecyclerView bookRecyclerView;
    private BookAdapter adapter;
    private String category;
    private CartDatabase cartDb = new CartDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_item);
        bookRecyclerView = findViewById(R.id.book_recyclerView);
        category = getIntent().getStringExtra("category");
        setToolbar();
    }


    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.tv_title_toolbar);
        title.setText(category);
    }

    private void initCategoryList() {
        Query query = FirebaseFirestore.getInstance()
                .collection("Books")
                .whereEqualTo("category", category)
                .whereEqualTo("approved", true)
                .whereEqualTo("sold", false);

        FirestoreRecyclerOptions options = new FirestoreRecyclerOptions.Builder<Book>()
                .setQuery(query, Book.class)
                .build();

        adapter = new BookAdapter(options, this);

        bookRecyclerView.setAdapter(adapter);
        adapter.setOnAddToCartClickListener(this::onAddToCartButtonClick);
        adapter.startListening();
    }

    private void onAddToCartButtonClick(DocumentSnapshot snapshot) {
        cartDb.addToCart(snapshot.getId())
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful())
                        Toast.makeText(this, "Failed to Add Book", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(this, "Added in Cart", Toast.LENGTH_SHORT).show();
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null)
            FirebaseAuth.getInstance().addAuthStateListener(authStateListener);

        if (adapter != null)
            adapter.stopListening();
    }

    private FirebaseAuth.AuthStateListener authStateListener = firebaseAuth -> {
        if (firebaseAuth.getCurrentUser() != null) initCategoryList();
    };
}