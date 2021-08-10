package com.polinomyacademy.bookstore_admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class BuyerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BuyerAdapter adapter;
    private ArrayList<User> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);

        setToolbar("Buyer");

        retrieveBuyersFromDb();
        initRecyclerView();
    }

    private void retrieveBuyersFromDb() {
        itemList.clear();
        FirebaseFirestore.getInstance().collection("bookedBook")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    itemList.clear();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        FirebaseFirestore.getInstance().collection("Users")
                                .document(snapshot.getId()).get()
                                .addOnSuccessListener(snap -> {
                                    if (snap.exists()) {
                                        User user = snap.toObject(User.class);
                                        user.setId(snap.getId());
                                        itemList.add(user);
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                    }
                });
    }

    private void setToolbar(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        TextView tv_title = toolbar.findViewById(R.id.tv_titleToolbar);
        tv_title.setText(title);
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new BuyerAdapter(this, itemList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this::openRequestOfBuyer);
    }

    private void openRequestOfBuyer(int position) {
        Intent intent = new Intent(this, BuyerRequestActivity.class);
        intent.putExtra("buyerId", itemList.get(position).getId());
        startActivity(intent);
    }

    @Override
    protected void onResume() {

        super.onResume();
        retrieveBuyersFromDb();
    }
}