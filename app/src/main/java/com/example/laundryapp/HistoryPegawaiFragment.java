package com.example.laundryapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class HistoryPegawaiFragment extends Fragment {
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    String status = "Selesai";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_history_pegawai_fragment, container, false);
        recyclerView = view.findViewById(R.id.recycleViewhistory);
        progressBar = view.findViewById(R.id.progressBarhistory);
        firebaseFirestore = FirebaseFirestore.getInstance();
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        getData();

        return view;
    }

    private void getData() {
        Query query = firebaseFirestore.collection("Data Laundry").whereEqualTo("status", status);

        FirestoreRecyclerOptions<ClassPelanggan> response = new FirestoreRecyclerOptions.Builder
                <ClassPelanggan>().setQuery(query, ClassPelanggan.class).build();

        adapter = new FirestoreRecyclerAdapter<ClassPelanggan, Holder>(response) {
            @NonNull
            @Override
            public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history,
                        parent, false);
                return new Holder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull Holder holder, int position, @NonNull final ClassPelanggan model) {
                progressBar.setVisibility(View.GONE);
                holder.namaHistory.setText(model.getNama());
                holder.hargaHistory.setText(model.getTotalHarga());
                holder.tglhistory.setText(model.gettanggalMasuk());
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
        TextView namaHistory, tglhistory;
        TextView hargaHistory;
        ConstraintLayout constraintLayout;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tglhistory = itemView.findViewById(R.id.tanggalpelangganhistory);
            namaHistory = itemView.findViewById(R.id.tvNamahistory);
            hargaHistory = itemView.findViewById(R.id.tvHargahistory);
            constraintLayout = itemView.findViewById(R.id.constaintLayouthistory);
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