package com.example.laundryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class HomeCustomer extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView = findViewById(R.id.recycleView);
        progressBar = findViewById(R.id.progressBar);
        firebaseFirestore = FirebaseFirestore.getInstance();
        linearLayoutManager = new LinearLayoutManager(HomeCustomer.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        getData();
    }
    private void getData() {
        Query query = firebaseFirestore.collection("Data Laundry");

        FirestoreRecyclerOptions<ClassPelanggan> response = new FirestoreRecyclerOptions.Builder
                <ClassPelanggan>().setQuery(query, ClassPelanggan.class).build();

        adapter = new FirestoreRecyclerAdapter<ClassPelanggan, Holder>(response) {
            @NonNull
            @Override
            public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewcustomer,
                        parent, false);
                return new Holder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull Holder holder, int position, @NonNull final ClassPelanggan model) {
                progressBar.setVisibility(View.GONE);
                holder.ID.setText(model.getID());
                holder.nama.setText(model.getNama());
                holder.harga.setText(model.getTotalHarga());
                holder.status.setText(model.getStatus());
            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
                Log.e("Ditemukan Error: ", e.getMessage());
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView nama;
        TextView harga, ID, status;
        ConstraintLayout constraintLayout;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ID = itemView.findViewById(R.id.Id);
            nama = itemView.findViewById(R.id.tvNama);
            status = itemView.findViewById(R.id.tvstatus);
            harga = itemView.findViewById(R.id.tvHarga);
            constraintLayout = itemView.findViewById(R.id.constaintLayout);
            LinearLayout linearLayout = itemView.findViewById(R.id.Detail);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}