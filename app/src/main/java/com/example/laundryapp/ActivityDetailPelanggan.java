package com.example.laundryapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.Objects;

import static java.lang.Integer.parseInt;

public class ActivityDetailPelanggan extends AppCompatActivity {
    private String ID, slmt, spt, ts, itemSelimut, itemSepatu, itemTas;;
    private TextView statuspelanggan, hargaKilo, hargaTambahan;
    private FirebaseFirestore firebaseFirestore;
    CheckBox selimut, sepatu, tas;
    TextView totalHargaView;
    ProgressBar progressBar;
    EditText nama, parfum, waktu, catatan, beratCucian;
    int hargaPerKilo = 3000;
    int beratCucianInt = 0;
    int hargaSepatu = 10000;
    int hargaSelimut = 15000;
    int hargatas = 10000;
    int totalHarga = 0;
    boolean[] hargaTambahanArr = {false, false, false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pelanggan);
        nama = findViewById(R.id.detail_nama_pemesan);
        parfum = findViewById(R.id.detail_parfum);
        waktu = findViewById(R.id.detail_lamaPencucian);
        catatan = findViewById(R.id.detail_catatan_khusus);
        beratCucian = findViewById(R.id.detail_berat_cucian);
        totalHargaView = findViewById(R.id.total_harga);
        selimut = findViewById(R.id.selimut_cb);
        tas = findViewById(R.id.tas_cb);
        sepatu = findViewById(R.id.sepatu_cb);
        statuspelanggan = findViewById(R.id.statuspelanggan);
        hargaKilo = findViewById(R.id.harga_kilo);
        hargaTambahan = findViewById(R.id.harga_tambahan);
        firebaseFirestore = FirebaseFirestore.getInstance();

        ID = getIntent().getExtras().getString("ID");
        readData();

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

        Button edit = findViewById(R.id.edit_laundry_btn);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatedata(nama.getText().toString(), parfum.getText().toString(), beratCucian.getText().toString(),
                        waktu.getText().toString(), catatan.getText().toString(), String.valueOf(totalHarga),
                        itemSepatu, itemSelimut, itemTas, ID);
            }
        });

        Button hapus = findViewById(R.id.hapus_laundry_btn);
        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hapusData();
            }
        });
    }

    private void readData() {
        firebaseFirestore.collection("Data Laundry").whereEqualTo("ID", ID)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@Nullable Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        statuspelanggan.setText(document.getString("status"));
                        nama.setText(document.getString("Nama"));
                        parfum.setText(document.getString("parfum"));
                        waktu.setText(document.getString("estimasi"));
                        catatan.setText(document.getString("catatanKhusus"));
                        beratCucian.setText(document.getString("berat"));
                        slmt = document.getString("selimut");
                        spt = document.getString("sepatu");
                        ts = document.getString("tas");
                        String totalHarga = document.getString("totalHarga");
                        totalHargaView.setText(totalHarga);

                        try {
                            if (slmt.equals("Iya")) {
                                selimut.setChecked(true);
                            } else {
                                selimut.setChecked(false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (spt.equals("Iya")) {
                                sepatu.setChecked(true);
                            } else {
                                sepatu.setChecked(false);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (ts.equals("Iya")) {
                                tas.setChecked(true);
                            } else {
                                tas.setChecked(false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(ActivityDetailPelanggan.this, "Error getting documents",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updatedata(String nama, String parfum, String beratCucian, String waktu,
                            String catatan, String totalHarga,
                            String sepatu, String selimut, String tas, String ID)  {
        DocumentReference documentReference = firebaseFirestore.collection("Data Laundry").document(ID);
        documentReference.update("Nama", nama);
        documentReference.update("parfum", parfum);
        documentReference.update("berat", beratCucian);
        documentReference.update("estimasi", waktu);
        documentReference.update("berat", beratCucian);
        documentReference.update("sepatu", sepatu);
        documentReference.update("selimut", selimut);
        documentReference.update("tas", tas);
        documentReference.update("catatanKhusus", catatan);
        documentReference.update("totalHarga", totalHarga)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ActivityDetailPelanggan.this, "Data telah diubah", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void hapusData() {
        firebaseFirestore.collection("Data Laundry").document(ID).delete();

        Toast.makeText(this, "Data telah dihapus", Toast.LENGTH_SHORT).show();
        finish();
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