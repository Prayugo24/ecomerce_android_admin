package com.mulai_berkarya.admin.entitas;

public class Detail {
    public String atas_nama;
    private String id_transaksi, tanggal, ongkir, total, resi, jumlah, nama, alamat, kec, kab, prov, kodepos, telp, namarek, norek, jmlrek;


    public Detail() {
        super();
    }

    public Detail(String id_transaksi, String tanggal, String ongkir, String total, String resi, String jumlah,
                  String alamat, String kec, String kab, String prov, String kodepos, String telp, String nama,
                  String namarek, String norek, String jmlrek) {
        super();
        this.id_transaksi = id_transaksi;
        this.tanggal = tanggal;
        this.ongkir = ongkir;
        this.total = total;
        this.resi = resi;
        this.jumlah = jumlah;
        this.alamat = alamat;
        this.kec = kec;
        this.kab = kab;
        this.prov = prov;
        this.kodepos = kodepos;
        this.telp = telp;
        this.nama = nama;
        this.namarek = namarek;
        this.norek = norek;
        this.jmlrek = jmlrek;
    }


    public String getnama() {
        return nama;
    }

    public void setnama(String nama) {
        this.nama = nama;
    }

    public String gettelp() {
        return telp;
    }

    public void settelp(String telp) {
        this.telp = telp;
    }

    public String getkodepos() {
        return kodepos;
    }

    public void setkodepos(String kodepos) {
        this.kodepos = kodepos;
    }

    public String getprov() {
        return prov;
    }

    public void setprov(String prov) {
        this.prov = prov;
    }

    public String getkab() {
        return kab;
    }

    public void setkab(String kab) {
        this.kab = kab;
    }

    public String getkec() {
        return kec;
    }

    public void setkec(String kec) {
        this.kec = kec;
    }

    public String getalamat() {
        return alamat;
    }

    public void setalamat(String alamat) {
        this.alamat = alamat;
    }

    public String getidtrans() {
        return id_transaksi;
    }

    public void setidtrans(String id_transaksi) {
        this.id_transaksi = id_transaksi;
    }

    public String getjumlah() {
        return jumlah;
    }

    public void setjumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String gettanggal() {
        return tanggal;
    }

    public void settanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getongkir() {
        return ongkir;
    }

    public void setongkir(String ongkir) {
        this.ongkir = ongkir;
    }

    public String gettotal() {
        return total;
    }

    public void settotal(String total) {
        this.total = total;
    }

    public String getresi() {
        return resi;
    }

    public void setresi(String resi) {
        this.resi = resi;
    }

    public String getNamarek() {
        return namarek;
    }

    public void setNamarek(String namarek) {
        this.namarek = namarek;
    }

    public String getNorek() {
        return norek;
    }

    public void setNorek(String norek) {
        this.norek = norek;
    }

    public String getJmlrek() {
        return jmlrek;
    }

    public void setJmlrek(String jmlrek) {
        this.jmlrek = jmlrek;
    }

    public String getAtas_nama() {
        return atas_nama;
    }

    public void setAtas_nama(String atas_nama) {
        this.atas_nama = atas_nama;
    }


}

