package com.example.laundryapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.firestore.FirebaseFirestore;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Calendar;

import static java.lang.Integer.getInteger;
import static java.lang.Integer.parseInt;

//import android.support.v7.app.AppCompatActivity;

public class Add_Laundry extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;

    String status = "Sedang diproses";
    String itemSelimut;
    String itemSepatu;
    String itemTas;
    int hargaPerKilo = 3000;
    int beratCucianInt = 0;
    int hargaSepatu = 10000;
    int hargaSelimut = 15000;
    int hargatas = 10000;
    int totalHarga = 0;
    boolean[] hargaTambahanArr = {false, false, false};
    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__laundry);

        final EditText nama = findViewById(R.id.nama_pemesan);
        final EditText parfum = findViewById(R.id.parfum);
        final EditText waktu = findViewById(R.id.lamaPencucian);
        final EditText catatan = findViewById(R.id.catatan_khusus);
        final EditText beratCucian = findViewById(R.id.berat_cucian);
        final TextView hargaKilo = findViewById(R.id.harga_kilo);
        final TextView totalHargaView = findViewById(R.id.total_harga);
        final TextView hargaTambahan = findViewById(R.id.harga_tambahan);
        final Button tambahLaundry = findViewById(R.id.tambah_laundry_btn);
        final CheckBox selimut = findViewById(R.id.selimut_cb);
        final CheckBox tas = findViewById(R.id.tas_cb);
        final CheckBox sepatu = findViewById(R.id.sepatu_cb);

        firebaseFirestore = FirebaseFirestore.getInstance();


        hargaKilo.setText(beratCucianInt + " kg x " + hargaPerKilo + " = Rp." + (beratCucianInt * hargaPerKilo));

        beratCucian.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                hargaKilo.setText(beratCucianInt + " kg x " + hargaPerKilo + " = Rp." + (beratCucianInt * hargaPerKilo));
                totalHarga = beratCucianInt * hargaPerKilo;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (beratCucian.getText().toString().length() <= 0) beratCucianInt = 0;
                else beratCucianInt = parseInt(beratCucian.getText().toString());
                hargaKilo.setText(beratCucianInt + " kg x " + hargaPerKilo + " = Rp." + (beratCucianInt * hargaPerKilo));
                totalHarga = beratCucianInt * hargaPerKilo;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (beratCucian.getText().toString().length() <= 0) beratCucianInt = 0;
                else beratCucianInt = parseInt(beratCucian.getText().toString());
                hargaKilo.setText(beratCucianInt + " kg x " + hargaPerKilo + " = Rp." + (beratCucianInt * hargaPerKilo));
                totalHarga = beratCucianInt * hargaPerKilo;

                updateView(hargaTambahan, totalHargaView);
            }
        });

        sepatu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    hargaTambahanArr[0] = true;
                    itemSepatu = "Iya";
                    updateView(hargaTambahan, totalHargaView);
                } else if (!b) {
                    hargaTambahanArr[0] = false;
                    itemSepatu = "Tidak";
                    updateView(hargaTambahan, totalHargaView);
                }
            }
        });

        selimut.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    hargaTambahanArr[1] = true;
                    itemSelimut = "Iya";
                    updateView(hargaTambahan, totalHargaView);
                } else if (!b) {
                    hargaTambahanArr[1] = false;
                    itemSelimut = "Tidak";
                    updateView(hargaTambahan, totalHargaView);
                }
            }
        });

        tas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    hargaTambahanArr[2] = true;
                    itemTas = "Iya";
                    updateView(hargaTambahan, totalHargaView);
                } else if (!b) {
                    hargaTambahanArr[2] = false;
                    itemTas = "Tidak";
                    updateView(hargaTambahan, totalHargaView);
                }
            }
        });
        Random random = new Random();
        int auto = random.nextInt(100);

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        tambahLaundry.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "Data ditambahkan", Toast.LENGTH_LONG).show();
            SimpanData(nama.getText().toString(), parfum.getText().toString(), beratCucian.getText().toString(),
                    waktu.getText().toString(), catatan.getText().toString(), String.valueOf(totalHarga),
                    status, itemSepatu, itemSelimut, itemTas, String.valueOf(auto), simpleDateFormat.format(date));
        });

    }

    private void SimpanData(String nama, String parfum, String beratCucian, String waktu,
                            String catatan, String totalHarga, String status,
                            String sepatu, String selimut, String tas, String ID, String tglMasuk) {

        Map<String, Object> LaundryData = new HashMap<>();
        LaundryData.put("ID", ID);
        LaundryData.put("Nama", nama);
        LaundryData.put("tanggalMasuk", tglMasuk);
        LaundryData.put("parfum", parfum);
        LaundryData.put("estimasi", waktu);
        LaundryData.put("berat", beratCucian);
        LaundryData.put("sepatu", sepatu);
        LaundryData.put("selimut", selimut);
        LaundryData.put("tas", tas);
        LaundryData.put("catatanKhusus", catatan);
        LaundryData.put("totalHarga", totalHarga);
        LaundryData.put("status", status);


        firebaseFirestore.collection("Data Laundry").document(ID).set(LaundryData).isSuccessful();
    }

    public void updateView(TextView hargaTambahan, TextView totalHargaView) {
        int totalHargaTambahan = 0;
        hargaTambahan.setText("");

        if (hargaTambahanArr[0]) {
            hargaTambahan.setText(hargaTambahan.getText() + "+ Sepatu Rp." + hargaSepatu + "\n");
            totalHargaTambahan += hargaSepatu;
        }
        if (hargaTambahanArr[1]) {
            hargaTambahan.setText(hargaTambahan.getText() + "+ Selimut Rp." + hargaSelimut + "\n");
            totalHargaTambahan += hargaSelimut;
        }
        if (hargaTambahanArr[2]) {
            hargaTambahan.setText(hargaTambahan.getText() + "+ Tas Rp." + hargatas + "\n");
            totalHargaTambahan += hargatas;
        }
        totalHarga = (totalHargaTambahan + totalHarga);
        totalHargaView.setText("TOTAL : Rp." + totalHarga);
    }

}