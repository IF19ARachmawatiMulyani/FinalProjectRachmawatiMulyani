package com.example.laundryapp;

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

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;


public class HomePegawaiFragment extends Fragment {
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    String status = "Sedang diproses";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_home_pegawai_fragment, container, false);
        recyclerView = v.findViewById(R.id.recycleView);
        progressBar = v.findViewById(R.id.progressBar);
        firebaseFirestore = FirebaseFirestore.getInstance();
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        getData();

        FloatingActionButton fab = v.findViewById(R.id.add_laundry);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), Add_Laundry.class);
            startActivity(intent);
        });
        return v;
    }

    private void getData() {
        Query query = firebaseFirestore.collection("Data Laundry").whereEqualTo("status", status);

        FirestoreRecyclerOptions<ClassPelanggan> response = new FirestoreRecyclerOptions.Builder
                <ClassPelanggan>().setQuery(query, ClassPelanggan.class).build();

        adapter = new FirestoreRecyclerAdapter<ClassPelanggan, Holder>(response) {
            @NonNull
            @Override
            public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pelanggan,
                        parent, false);
                return new Holder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull Holder holder, int position, @NonNull final ClassPelanggan model) {
                progressBar.setVisibility(View.GONE);
                holder.tgl.setText(model.gettanggalMasusk());
                holder.ID.setText(model.getID());
                holder.nama.setText(model.getNama());
                holder.harga.setText(model.getTotalHarga());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(),
                                ActivityDetailPelanggan.class);
                        intent.putExtra("ID", model.getID());
                        startActivity(intent);
                    }
                });

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
        TextView harga, ID, tgl;
        ConstraintLayout constraintLayout;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tgl = itemView.findViewById(R.id.tanggalpelanggan);
            ID = itemView.findViewById(R.id.Id);
            nama = itemView.findViewById(R.id.tvNama);
            harga = itemView.findViewById(R.id.tvHarga);
            constraintLayout = itemView.findViewById(R.id.constaintLayout);
            LinearLayout linearLayout = itemView.findViewById(R.id.Detail);

            Button knfr;
            knfr = itemView.findViewById(R.id.konfirmasi_btn);
            knfr.setOnClickListener((View v) -> {
                String sts = "Selesai";
                String Id = ID.getText().toString();
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Konfirasmi Pesanan")
                        .setMessage("Apakah pesanan sudah selesai?")
                        .setIcon(R.mipmap.logo)
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                konfiramsi(Id, sts);
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            });
        }

        private void konfiramsi(String ID, String status) {
            firebaseFirestore.collection("Data Laundry").document(ID).update("status", status);
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