package com.example.laundryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FormLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);
    }

    public void Login(View view) {
        Intent login = new Intent(FormLogin.this, MainPegawai.class);
        startActivity(login);
    }

    public void Register(View view) {
        Intent register = new Intent(this, Register_pegawai.class);
        startActivity(register);
    }
}
