package com.polinomyacademy.bookstore_admin;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class BuyerRequestActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BuyerBookAdapter adapter;
    private ArrayList<Book> itemList = new ArrayList<>();
    private String buyerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_request);

        buyerId = getIntent().getStringExtra("buyerId");
        setToolbar("Requests");
        retrieveConfirmedBooks(buyerId);
        initRecyclerView();
    }

    private void setToolbar(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        TextView tv_title = toolbar.findViewById(R.id.tv_titleToolbar);
        tv_title.setText(title);
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new BuyerBookAdapter(this, itemList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemButtonClickListener(this::handleMarkSoldButtonClick);
    }

    private void handleMarkSoldButtonClick(int position) {
        String key = itemList.get(position).getId();
        FirebaseFirestore.getInstance().collection("Books")
                .document(key)
                .update("sold", true);
        FirebaseFirestore.getInstance().collection("bookedBook")
                .document(buyerId).update(key, FieldValue.delete());

        itemList.remove(position);
        adapter.notifyItemRemoved(position);
        Toast.makeText(this, "Marked as Sold", Toast.LENGTH_SHORT).show();

        if (itemList.size() == 0)
            FirebaseFirestore.getInstance().collection("bookedBook")
                    .document(buyerId).delete();

    }

    private void retrieveConfirmedBooks(String buyerId) {
        FirebaseFirestore.getInstance().collection("bookedBook")
                .document(buyerId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    itemList.clear();
                    Map<String, Object> map = snapshot.getData();

                    for (String key : map.keySet()) {
                        FirebaseFirestore.getInstance().collection("Books")
                                .document(key).get()
                                .addOnSuccessListener(snap -> {
                                    if (snap.exists()) {
                                        Book book = snap.toObject(Book.class);
                                        book.setId(key);
                                        itemList.add(book);
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                    }
                });
    }


}