package com.mulai_berkarya.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mulai_berkarya.admin.cimol.R;
import com.mulai_berkarya.admin.entitas.Detail;
import com.mulai_berkarya.admin.server.Config;

import java.util.ArrayList;

public class AdapterDetail extends ArrayAdapter<Detail> {
    Context mContext;
    ArrayList<Detail> list;

    int layout;
    String URL_GAMBAR = Config.URL_BASE + "/Gambar/";

    public AdapterDetail(Context context,
                         int textViewResourceId,
                         ArrayList<Detail> list_params) {
        super(context, textViewResourceId, list_params);
        this.mContext = context;
        this.layout = textViewResourceId;
        this.list = list_params;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(layout, null);

            holder = new ViewHolder();
/*
            holder.idtrans = (TextView) convertView.findViewById(R.id.textViewidtransaksi2);
            holder.tanggal = (TextView) convertView.findViewById(R.id.textviewtgl_trans2);
            holder.ongkir = (TextView) convertView.findViewById(R.id.textViewongkir2);
            holder.total = (TextView) convertView.findViewById(R.id.textViewttl2);
            holder.jumlah = (TextView) convertView.findViewById(R.id.textViewsubt2);
*/
            holder.resi = (TextView) convertView.findViewById(R.id.textviResi22);
            holder.alamat = (TextView) convertView.findViewById(R.id.textViewAlamat2);
            holder.kec = (TextView) convertView.findViewById(R.id.textviewkec2);
            holder.kab = (TextView) convertView.findViewById(R.id.textViewkab2);
            holder.prov = (TextView) convertView.findViewById(R.id.textViewprof2);
            holder.kode = (TextView) convertView.findViewById(R.id.textViewkodepos2);
            holder.telp = (TextView) convertView.findViewById(R.id.textViewtlp2);
            holder.nama = (TextView) convertView.findViewById(R.id.textatasnama2);
            holder.namarek = (TextView) convertView.findViewById(R.id.textViewnamarek);
            holder.norek = (TextView) convertView.findViewById(R.id.textViewnorek);
            holder.jmlrek = (TextView) convertView.findViewById(R.id.textViewjmlrek);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Detail m = list.get(position);
        holder.idtrans.setText("ID Transaksi " + m.getidtrans());
        holder.tanggal.setText("Tanggal " + m.gettanggal());
        holder.ongkir.setText("Ongkir " + m.getongkir());
        holder.total.setText("Total " + m.gettotal());
        holder.resi.setText("Resi " + m.getresi());
        holder.jumlah.setText("Jumlah " + m.getjumlah());
        holder.alamat.setText("Alamat " + m.getalamat());
        holder.kec.setText("Kec " + m.getkec());
        holder.kab.setText("Kab " + m.getkab());
        holder.prov.setText("Provinsi " + m.getprov());
        holder.kode.setText("Kode " + m.getkodepos());
        holder.telp.setText("Telp " + m.gettelp());
        holder.nama.setText("A.N. " + m.getAtas_nama());
        holder.namarek.setText("Nama " + m.getNamarek());
        holder.norek.setText("No. Rek. " + m.getNorek());
        holder.jmlrek.setText("Jumlah " + m.getJmlrek());


        /*Glide
                .with(mContext)
                .load(URL_GAMBAR + "/" + m.getgambar())
                .fitCenter()
                //.override(250, 250)
                .crossFade()
                .error(R.drawable.ic_bayar)
                .into(holder.gambar);*/
        return convertView;
    }

    private class ViewHolder {
        TextView idtrans, tanggal, ongkir, total, resi, jumlah, alamat, kec, kab, prov, kode, telp, nama, namarek, norek, jmlrek;
        //ImageView gambar;
    }
}
