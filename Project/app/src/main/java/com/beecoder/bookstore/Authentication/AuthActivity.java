package com.beecoder.bookstore.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.beecoder.bookstore.MainActivity;
import com.beecoder.bookstore.R;
import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;
import java.util.List;

public class AuthActivity extends AppCompatActivity {
    private static final int RC_AUTH = 101;
    private Button signIn_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        initObjects();
    }

    private void initObjects() {
        signIn_btn = findViewById(R.id.btn_signIn);
        signIn_btn.setOnClickListener(v -> handleAuth());
    }

    private void handleAuth() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build()
        );

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_AUTH
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_AUTH && resultCode == RESULT_OK) {
            openMainActivity();
            finish();
        }

    }

    private void openMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
