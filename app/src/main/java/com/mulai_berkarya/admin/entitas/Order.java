package com.mulai_berkarya.admin.entitas;

/**
 * Created by DELL on 31/10/2016.
 */

public class Order {
    private String idbrg, namabrg, hargabrg, qty, ket, gambar;

    public Order() {
        super();
    }

    public Order(String idbrg, String namabrg, String hargabrg, String qty, String ket, String gambar) {
        super();
        this.idbrg = idbrg;
        this.namabrg = namabrg;
        this.hargabrg = hargabrg;
        this.qty = qty;
        this.ket = ket;
        this.gambar = gambar;

    }

    public String getIdbrg() {
        return idbrg;
    }

    public void setIdbrg(String idbrg) {
        this.idbrg = idbrg;
    }

    public String getNamabrg() {
        return namabrg;
    }

    public void setNamabrg(String namabrg) {
        this.namabrg = namabrg;
    }

    public String getHargabrg() {
        return hargabrg;
    }

    public void setHargabrg(String hargabrg) {
        this.hargabrg = hargabrg;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getKet() {
        return ket;
    }

    public void setKet(String ket) {
        this.ket = ket;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
}
