package com.beecoder.bookstore.fragmentsMain;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.beecoder.bookstore.AddBookActivity;
import com.beecoder.bookstore.Book;
import com.beecoder.bookstore.BookAdapter;
import com.beecoder.bookstore.R;
import com.beecoder.bookstore.cart.Carts;
import com.beecoder.bookstore.database.CartDatabase;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener, androidx.appcompat.widget.SearchView.OnQueryTextListener {
    private RecyclerView bookListView;
    private BookAdapter adapter;
    private CartDatabase cartDb = new CartDatabase();
    private androidx.appcompat.widget.SearchView search_bar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.home_layout, container, false);
        FloatingActionButton fab = layout.findViewById(R.id.fab);
        search_bar = layout.findViewById(R.id.search_bar);
        fab.setOnClickListener(v -> openAddBookActivity());
        search_bar.setOnQueryTextListener(this);

        bookListView = layout.findViewById(R.id.book_recyclerList);
        return layout;
    }

    private void initBookList() {
        Query query = FirebaseFirestore.getInstance().collection("Books")
                .whereEqualTo("approved", true)
                .whereEqualTo("sold", false);

        FirestoreRecyclerOptions options = new FirestoreRecyclerOptions.Builder<Book>()
                .setQuery(query, Book.class)
                .build();
        adapter = new BookAdapter(options, getActivity());
        bookListView.setAdapter(adapter);
        adapter.setOnAddToCartClickListener(this::addToCart);
        adapter.startListening();
        CartDatabase cartDatabase = new CartDatabase();
        cartDatabase.getBookIds().addSnapshotListener((value, error) -> {
            if (!value.isEmpty()) {
                Carts.clearAll();
                for (DocumentSnapshot snapshot : value.getDocuments())
                    Carts.addBookId(snapshot.getId());
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void addToCart(DocumentSnapshot snapshot) {
        cartDb.addToCart(snapshot.getId())
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Carts.addBookId(snapshot.getId());
                        Toast.makeText(getActivity(), "Failed to Add Book", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getActivity(), "Added in Cart", Toast.LENGTH_SHORT).show();
                });
        adapter.notifyDataSetChanged();
    }

    private FirebaseAuth.AuthStateListener authStateListener = firebaseAuth -> {
        if (firebaseAuth.getCurrentUser() != null)
            initBookList();
    };

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

    private void openAddBookActivity() {
        Intent intent = new Intent(getActivity(), AddBookActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        searchBook(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        searchBook(s);
        return false;
    }

    private void searchBook(String bookName) {
        Query query = FirebaseFirestore.getInstance().collection("Books").orderBy("title").startAt(bookName).endAt(bookName + "\uf8ff");


        FirestoreRecyclerOptions options = new FirestoreRecyclerOptions.Builder<Book>()
                .setQuery(query, Book.class)
                .build();

        adapter = new BookAdapter(options, getActivity());
        adapter.setOnAddToCartClickListener(this::addToCart);
        bookListView.setAdapter(adapter);
        adapter.startListening();
    }
}
