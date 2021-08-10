package com.beecoder.bookstore;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AddBookActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final int IMG_REQUEST_ID = 10;
    private static final String TAG = "AddBooks";
    private EditText title_edt, author_edt, edition_edt, price_edt;
    private Button btn;
    private Spinner spinner;
    private ProgressBar progressBar;
    private ImageButton imageButton;

    private Uri uri;
    private String filePath;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private FirebaseStorage storage;
    private StorageReference reference;

    private Book book = new Book();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_books);
        title_edt = findViewById(R.id.edit_txt_title);
        author_edt = findViewById(R.id.edit_txt_author);
        edition_edt = findViewById(R.id.edit_txt_edition);
        price_edt = findViewById(R.id.edit_txt_price);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        btn = findViewById(R.id.btn_add);
        imageButton = findViewById(R.id.btn_upload_img);

        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();

        spinner = findViewById(R.id.spin_category);
        setupCategorySpinner();
        setToolbar();
    }

    private void setupCategorySpinner() {
        firestore.collection("Category").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<String> category = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots)
                        category.add(document.toObject(Category.class).getName());
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, category);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    spinner.setOnItemSelectedListener(this);
                });
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.tv_title_toolbar);
        title.setText("Add Book");
    }

    public void addBook(View view) {
        String title = title_edt.getText().toString();
        String authorName = author_edt.getText().toString();
        String edition = edition_edt.getText().toString();
        String price = price_edt.getText().toString();
        String categoryName = spinner.getSelectedItem().toString();

        book.setTitle(title);
        book.setSellerId(FirebaseAuth.getInstance().getUid());
        book.setAuthorName(authorName);
        book.setEdition(edition);
        book.setPrice(price);
        book.setCategory(categoryName);

        saveImage();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void uploadImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent, "Selected Image"), IMG_REQUEST_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMG_REQUEST_ID && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            Glide.with(this)
                    .load(uri)
                    .centerCrop()
                    .into(imageButton);
        }
    }


    public void saveImage() {
        progressBar.setVisibility(View.VISIBLE);
        btn.setEnabled(false);
        reference = storage.getReference().child("News").child(uri.hashCode() + "");
        reference.putFile(uri)
                .addOnSuccessListener(s -> {
                    reference.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                filePath = uri.toString();
                                book.setImageUrl(filePath);


                                firestore.collection("Books").add(book)
                                        .addOnSuccessListener(documentReference -> {
                                            btn.setEnabled(true);
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(this, "request sent to admin...", Toast.LENGTH_SHORT).show();
                                            finish();
                                        })
                                        .addOnFailureListener(e -> {
                                            btn.setEnabled(true);
                                            progressBar.setVisibility(View.GONE);
                                            Log.d(TAG, "Failed");
                                        });
                            });
                });


    }
}

