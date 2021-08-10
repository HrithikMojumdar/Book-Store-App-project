package com.beecoder.bookstore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.beecoder.bookstore.Authentication.AuthActivity;
import com.beecoder.bookstore.fragmentsMain.CartFragment;
import com.beecoder.bookstore.fragmentsMain.CatalogueFragment;
import com.beecoder.bookstore.fragmentsMain.ContactFragment;
import com.beecoder.bookstore.fragmentsMain.HomeFragment;
import com.beecoder.bookstore.fragmentsMain.ProfileFragment;
import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseUser user;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    public ImageView dp_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            openAuthActivity();
            finish();
        } else {
            setupToolbar();
            setUpNavigationDrawer(savedInstanceState);
            setUserValues();
        }
    }

    private void setUserValues() {
        Log.i("1234567890", "setUserValues:1 " + CurrentUser.getCurrentUser().toString());
        CurrentUser.getCurrentUser().setName(user.getDisplayName());
        CurrentUser.getCurrentUser().setEmail(user.getEmail());
        CurrentUser.getUserCollection()
                .document(user.getUid())
                .get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                User currentUser = snapshot.toObject(User.class);
                CurrentUser.getCurrentUser().setPhoneNumber(currentUser.getPhoneNumber());
                CurrentUser.getCurrentUser().setPhotoUrl(currentUser.getPhotoUrl());
                CurrentUser.getCurrentUser().setId(user.getUid());
                Glide.with(this).load(CurrentUser.getCurrentUser().getPhotoUrl())
                        .circleCrop()
                        .into(dp_iv);
            }

            Log.i("1234567890", "setUserValues:2 " + CurrentUser.getCurrentUser().toString());
        });
    }

    private void openAuthActivity() {
        startActivity(new Intent(this, AuthActivity.class));
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setUpNavigationDrawer(Bundle saveInstanceState) {
        drawerLayout = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawerContent, R.string.closeDrawerContent);
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(toggle);
        navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        toggle.syncState();

        if (saveInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.home_item);
        }

        View headerView = navigationView.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.username);
        TextView email = headerView.findViewById(R.id.email);
        dp_iv = headerView.findViewById(R.id.dp);
        username.setText(user.getDisplayName());
        email.setText(user.getEmail());
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (!(fragment instanceof HomeFragment))
            setFragment(new HomeFragment());
        else super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        navigationView.setCheckedItem(item.getItemId());
        switch (item.getItemId()) {
            case R.id.home_item:
                setFragment(new HomeFragment());
                break;
            case R.id.catalogue_item:
                setFragment(new CatalogueFragment());
                break;
            case R.id.profile_item:
                setFragment(new ProfileFragment());
                break;
            case R.id.cart_item:
                setFragment(new CartFragment());
                break;
            case R.id.contact_item:
                setFragment(new ContactFragment());
                break;
            case R.id.logOut_item:
                CurrentUser.clearInfo();
                Log.i("1234567890", "onNavigationItemSelected: " + CurrentUser.getCurrentUser().toString());
                AuthUI.getInstance().signOut(this);
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, AuthActivity.class));
                finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }
}
