package com.beecoder.bookstore.fragmentsMain;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.beecoder.bookstore.CurrentUser;
import com.beecoder.bookstore.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    private final int IMG_REQUEST_ID = 10;

    private TextView username, phoneNumber_tv, email;
    private ImageView editPhone_iv, photoEdit_iv, profile_iv;
    FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.profile_layout, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        setViews(layout);
        return layout;
    }

    private void setViews(View layout) {
        email = layout.findViewById(R.id.email);
        phoneNumber_tv = layout.findViewById(R.id.phone_number);
        username = layout.findViewById(R.id.username);
        editPhone_iv = layout.findViewById(R.id.edit_phone_number);
        profile_iv = layout.findViewById(R.id.img_userProfile);
        photoEdit_iv = layout.findViewById(R.id.imgBtn_photoEdit);
        editPhone_iv.setOnClickListener(view -> showEditPhoneDialogue());

        if (CurrentUser.getCurrentUser() != null) {
            Toast.makeText(getActivity(), "asi eikhane", Toast.LENGTH_SHORT).show();
            Log.i("1234567890", "setViews: " + CurrentUser.getCurrentUser().toString());
            email.setText(CurrentUser.getCurrentUser().getEmail());
            username.setText(CurrentUser.getCurrentUser().getName());
            phoneNumber_tv.setText(CurrentUser.getCurrentUser().getPhoneNumber());
            setProfilePhoto(CurrentUser.getCurrentUser().getPhotoUrl());
        }
        photoEdit_iv.setOnClickListener(v -> openGallery());
    }

    private void openGallery() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent, "Selected Image"), IMG_REQUEST_ID);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMG_REQUEST_ID && resultCode == RESULT_OK && data != null && data.getData() != null)
            showUploadDialog(data.getData());
    }

    private void showUploadDialog(Uri uri) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.upload_image_dialog, null);
        ImageView photo_iv = view.findViewById(R.id.pic_dialog_iv);
        Button cancel_btn = view.findViewById(R.id.cancel_btn);
        Button upload_btn = view.findViewById(R.id.upload_btn);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        Glide.with(getActivity()).load(uri).into(photo_iv);

        cancel_btn.setOnClickListener(v -> dialog.dismiss());
        upload_btn.setOnClickListener(v -> {
            uploadImage(uri);
            dialog.dismiss();
        });
    }

    private void uploadImage(Uri uri) {
        Toast.makeText(getActivity(), "Uploading...", Toast.LENGTH_SHORT).show();
        CurrentUser.uploadPhoto(uri)
                .addOnSuccessListener(s -> {
                    CurrentUser.getPhotoRef().getDownloadUrl()
                            .addOnSuccessListener(url -> {
                                Toast.makeText(getActivity(), "Photo Uploaded", Toast.LENGTH_SHORT).show();
                                CurrentUser.updatePhotoUrl(url.toString())
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                setProfilePhoto(url.toString());
                                            }
                                        });
                            });
                });
    }

    private void setProfilePhoto(String url) {
        Glide.with(getActivity())
                .load(url)
                .circleCrop()
                .into(profile_iv);
    }

    private void showEditPhoneDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        EditText editText = new EditText(getActivity());
        editText.setPadding(16, 16, 16, 16);
        if (phoneNumber_tv.getText() != null) {
            editText.setText(phoneNumber_tv.getText());
        }

        builder.setTitle("Set Phone")
                .setView(editText)
                .setPositiveButton("Save", (a, b) -> {
                    CurrentUser.updatePhone(editText.getText().toString())
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Successful", Toast.LENGTH_SHORT).show();
                                    phoneNumber_tv.setText(CurrentUser.getCurrentUser().getPhoneNumber());
                                } else {
                                    Toast.makeText(getActivity(), "failed :" + task.getResult(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }).setNeutralButton("Cancel", null).create().show();
    }
}

