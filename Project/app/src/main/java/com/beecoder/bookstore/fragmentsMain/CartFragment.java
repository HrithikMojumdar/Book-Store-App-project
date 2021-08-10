package com.beecoder.bookstore.fragmentsMain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.beecoder.bookstore.Book;
import com.beecoder.bookstore.CartBookAdapter;
import com.beecoder.bookstore.R;
import com.beecoder.bookstore.cart.Carts;
import com.beecoder.bookstore.database.CartDatabase;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CartFragment extends Fragment {

    private RecyclerView bookListView;
    private CartBookAdapter adapter;
    private Button btn_confirmOrder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_cart, container, false);
        bookListView = layout.findViewById(R.id.book_recyclerList);
        btn_confirmOrder = layout.findViewById(R.id.btn_confirmOrder);
        btn_confirmOrder.setOnClickListener(v -> handleConfirm());
        return layout;
    }

    private void initBookList() {
        Query query = new CartDatabase().getBookIds();


        FirestoreRecyclerOptions options = new FirestoreRecyclerOptions.Builder<Book>()
                .setQuery(query, Book.class)
                .build();
        adapter = new CartBookAdapter(options, getActivity());
        adapter.setOnCancelClickListener(this::removeFromCart);
        bookListView.setAdapter(adapter);
        adapter.setOnDataSetChangeListener(this::onListChange);
        adapter.startListening();
    }

    private void removeFromCart(DocumentSnapshot snapshot, int position) {
        new CartDatabase().removeBook(snapshot.getId())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        Carts.removeBookId(snapshot.getId());
                    adapter.notifyItemRemoved(position);
                });
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

    private void handleConfirm() {
        Map<String, Timestamp> map = new HashMap<>();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            DocumentSnapshot snapshot = adapter.getSnapshots().getSnapshot(i);
            Book book = snapshot.toObject(Book.class);
            if (!book.isBooked()) {
                map.put(snapshot.getId(), new Timestamp(new Date()));
                FirebaseFirestore.getInstance().collection("Books")
                        .document(snapshot.getId()).update("booked", true);
            }
        }
        if (!map.isEmpty()) {
            FirebaseFirestore.getInstance().collection("bookedBook")
                    .document(FirebaseAuth.getInstance().getUid()).set(map, SetOptions.merge());
            Toast.makeText(getActivity(), "Request sent to Admin", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Already booked", Toast.LENGTH_SHORT).show();
        }


    }

    private void onListChange(int count) {
        btn_confirmOrder.setEnabled(count > 0);
    }
}