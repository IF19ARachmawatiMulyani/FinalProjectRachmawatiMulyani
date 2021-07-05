package com.example.laundryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Register_pegawai extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pegawai);
    }
    public void Register(View view) {
        Intent register = new Intent(this, logIn.class);
    }

    public void Login(View view) {
        Intent login = new Intent(this, logIn.class);
        startActivity(login);
    }
}