package com.polinomyacademy.bookstore_admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CategoryActivity extends AppCompatActivity {
    private Button addCategoryButton;
    private RecyclerView categoryListView;
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        setToolbar("Category");
        addCategoryButton = findViewById(R.id.btn_addCategory);
        addCategoryButton.setOnClickListener(v -> handleOnClick());
        categoryListView = findViewById(R.id.categoryListView);
        initCategoryList();
    }

    private void handleOnClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.category_add_layout, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
        Button add = view.findViewById(R.id.add_button);
        Button cancel = view.findViewById(R.id.cancel_button);
        EditText name_edt = view.findViewById(R.id.edt_category_name);

        add.setOnClickListener(v -> {
            addToDatabase(name_edt.getText().toString());
            dialog.dismiss();
        });
        cancel.setOnClickListener(v -> dialog.dismiss());
    }

    private void addToDatabase(String name) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference categoryCollection = firestore.collection("Category");
        categoryCollection.add(new Category(name))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, name + " is successfully added", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initCategoryList() {
        Query query = FirebaseFirestore.getInstance()
                .collection("Category");

        FirestoreRecyclerOptions options = new FirestoreRecyclerOptions.Builder<Category>()
                .setQuery(query, Category.class)
                .build();

        adapter = new CategoryAdapter(options);
        categoryListView.setAdapter(adapter);
        adapter.startListening();
    }

    private void setToolbar(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        TextView tv_title = toolbar.findViewById(R.id.tv_titleToolbar);
        tv_title.setText(title);
    }

}