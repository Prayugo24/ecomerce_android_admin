package com.mulai_berkarya.admin.entitas;

/**
 * Created by DELL on 11/11/2016.
 */

public class Trans {
    private String id_transaksi, tanggal, total, status, konf_bay, no_resi;

    public Trans() {
        super();
    }

    public Trans(String id_transaksi, String tanggal, String total, String status,
                 String konf_bay, String no_resi) {
        super();
        this.id_transaksi = id_transaksi;
        this.tanggal = tanggal;
        this.total = total;
        this.status = status;
        this.konf_bay = konf_bay;
        this.no_resi = no_resi;

    }

    public String getId_transaksi() {
        return id_transaksi;
    }

    public void setId_transaksi(String id_transaksi) {
        this.id_transaksi = id_transaksi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKonf_bay() {
        return konf_bay;
    }

    public void setKonf_bay(String konf_bay) {
        this.konf_bay = konf_bay;
    }

    public String getNo_resi() {
        return no_resi;
    }

    public void setNo_resi(String no_resi) {
        this.no_resi = no_resi;
    }
}

