package com.polinomyacademy.bookstore_admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    private Button btn_seeBuyerRequest;
    private Button btn_seeSellerRequest;
    private Button btn_handleCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setToolbar("Admin");
    }

    private void setToolbar(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        TextView tv_title = toolbar.findViewById(R.id.tv_titleToolbar);
        tv_title.setText(title);
    }

    private void initViews() {
        btn_handleCategory = findViewById(R.id.btn_handleCategory);
        btn_seeSellerRequest = findViewById(R.id.btn_sellerRequest);
        btn_seeBuyerRequest = findViewById(R.id.btn_buyerRequest);

        btn_handleCategory.setOnClickListener(v -> openActivity(CategoryActivity.class));
        btn_seeSellerRequest.setOnClickListener(v -> openActivity(SellerActivity.class));
        btn_seeBuyerRequest.setOnClickListener(v -> openActivity(BuyerActivity.class));

    }

    private void openActivity(Class clazz) {
        startActivity(new Intent(this, clazz));
    }


}