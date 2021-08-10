package com.beecoder.bookstore.fragmentsMain;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.beecoder.bookstore.R;

public class ContactFragment extends Fragment {

    TextView tv_contact1,tv_contact2,tv_contact3;
    ImageButton btn1_makeCall,btn2_makeCall,btn3_makeCall;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_contact, container, false);
        tv_contact1=layout.findViewById(R.id.tv_contact1);
        tv_contact2=layout.findViewById(R.id.tv_contact2);
        tv_contact3=layout.findViewById(R.id.tv_contact3);

        btn1_makeCall=layout.findViewById(R.id.btn1_makeCall);
        btn2_makeCall=layout.findViewById(R.id.btn2_makeCall);
        btn3_makeCall=layout.findViewById(R.id.btn3_makeCall);

        btn1_makeCall.setOnClickListener(v-> makeCall(tv_contact1.getText().toString()));
        btn2_makeCall.setOnClickListener(v-> makeCall(tv_contact2.getText().toString()));
        btn3_makeCall.setOnClickListener(v-> makeCall(tv_contact3.getText().toString()));


        return layout;
    }
    private void makeCall(String number) {

        Uri uri = Uri.parse("tel:" + number);
        Intent intent = new Intent(Intent.ACTION_DIAL,uri);
        startActivity(intent);

    }
}