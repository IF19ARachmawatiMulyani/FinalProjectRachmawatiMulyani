package com.example.laundryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CurrentLaundryAdapter extends BaseAdapter {
    Context c;
    String[] nama = {};
    String[] tglAwal = {};
    String[] tglAkhir = {};

    public CurrentLaundryAdapter(Context ctx) {
        this.c = ctx;
    }

    @Override
    public int getCount() {
        return nama.length;
    }

    @Override
    public Object getItem(int i) {
        return nama[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.activity_current_laundry_adapter, null);

        TextView name = (TextView) convertView.findViewById(R.id.nama_pemesan);
        TextView tanggalMulai = (TextView) convertView.findViewById(R.id.tanggal_mulai);
        TextView tanggalSelesai = (TextView) convertView.findViewById(R.id.tanggal_selesai);

        name.setText(nama[i]);
        tanggalMulai.setText(tglAwal[i]);
        tanggalSelesai.setText(tglAkhir[i]);

        return convertView;
    }
}