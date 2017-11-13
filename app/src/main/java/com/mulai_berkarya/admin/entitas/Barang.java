package com.mulai_berkarya.admin.entitas;

public class Barang {
    private String id_barang, nama_barang, harga_barang, berat, stok, deskripsi, gambar;
    private String diskonPersen, hargaDiskon, terjual;

    public Barang() {
        super();
    }

    public Barang(String id_barang, String nama_barang, String harga_barang, String berat, String stok, String deskripsi, String terjual) {
        this.id_barang = id_barang;
        this.nama_barang = nama_barang;
        this.harga_barang = harga_barang;
        this.berat = berat;
        this.stok = stok;
        this.deskripsi = deskripsi;
        this.terjual = terjual;
    }

    public String getid_barang() {
        return id_barang;
    }

    public void setid_barang(String id_barang) {
        this.id_barang = id_barang;
    }

    public String getnama_barang() {
        return nama_barang;
    }

    public void setnama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public String getharga() {
        return harga_barang;
    }

    public void setharga(String harga) {
        this.harga_barang = harga;
    }

    public String getberat() {
        return berat;
    }

    public void setberat(String berat) {
        this.berat = berat;
    }

    public String getStok() {
        return stok;
    }

    public void setStok(String stok) {
        this.stok = stok;
    }

    public String getdeskripsi() {
        return deskripsi;
    }

    public void setdeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getgambar() {
        return gambar;
    }

    public void setgambar(String gambar) {
        this.gambar = gambar;
    }

    public String getHargaDiskon() {
        return hargaDiskon;
    }

    public void setHargaDiskon(String hargaDiskon) {
        this.hargaDiskon = hargaDiskon;
    }

    public String getDiskonPersen() {
        return diskonPersen;
    }

    public void setDiskonPersen(String diskonPersen) {
        this.diskonPersen = diskonPersen;
    }

    public String getTerjual() {
        return terjual;
    }

    public void setTerjual(String terjual) {
        this.terjual = terjual;
    }

}
