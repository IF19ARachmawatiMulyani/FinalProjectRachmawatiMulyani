package com.example.laundryapp;

public class ClassPelanggan {
    String status, Nama,  catatankhusus,  parfum, berat, estimasi, totalHarga, ID, tanggalMasuk;

    public ClassPelanggan() {
    }

    public ClassPelanggan(String nama, String berat, String catatankhusus, String estimasi, String parfum, String totalHarga, String status, String id, String tglmasusk) {
        this.ID = id;
        this.Nama = nama;
        this.tanggalMasuk = tglmasusk;
        this.berat = berat;
        this.catatankhusus = catatankhusus;
        this.estimasi = estimasi;
        this.parfum = parfum;
        this.totalHarga = totalHarga;
        this.status = status;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getBerat() {
        return berat;
    }

    public void setBerat(String berat) {
        this.berat = berat;
    }

    public String getCatatankhusus() {
        return catatankhusus;
    }

    public void setCatatankhusus(String catatankhusus) {
        this.catatankhusus = catatankhusus;
    }

    public String getEstimasi() {
        return estimasi;
    }

    public void setEstimasi(String estimasi) {
        this.estimasi = estimasi;
    }

    public String getParfum() {
        return parfum;
    }

    public void setParfum(String parfum) {
        this.parfum = parfum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalHarga() {
        return totalHarga;
    }
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String gettanggalMasuk() {
        return tanggalMasuk;
    }

    public void settanggalMasuk(String tanggalMasusk) {
        this.tanggalMasuk = tanggalMasusk;
    }

    public void setTotalHarga(String totalHarga) {
        this.totalHarga = totalHarga;
    }
}