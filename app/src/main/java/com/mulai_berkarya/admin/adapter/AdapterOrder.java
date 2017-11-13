package com.mulai_berkarya.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mulai_berkarya.admin.cimol.R;
import com.mulai_berkarya.admin.entitas.Order;
import com.mulai_berkarya.admin.server.Config;

import java.util.ArrayList;

/**
 * Created by DELL on 01/11/2016.
 */

public class AdapterOrder extends ArrayAdapter<Order> {
    Context mContext;
    ArrayList<Order> list;

    int layout;
    String URL_GAMBAR = Config.URL_BASE + "/Gambar";

    public AdapterOrder(Context context, int textViewResourceId, ArrayList<Order> list_params) {
        super(context, textViewResourceId, list_params);
        this.mContext = context;
        this.layout = textViewResourceId;
        this.list = list_params;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        return super.getView(position, convertView, parent)
// ;
        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(layout, null);

            holder = new ViewHolder();
            holder.nama = (TextView) convertView.findViewById(R.id.namabrgnya2);
            holder.qty = (TextView) convertView.findViewById(R.id.qty2);
            holder.jumlah = (TextView) convertView.findViewById(R.id.harga2);
            holder.ket = (TextView) convertView.findViewById(R.id.ket2);
            holder.gambar = (ImageView) convertView.findViewById(R.id.gambar_barang2);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Order m = list.get(position);
        holder.nama.setText("Nama : " + m.getNamabrg());
        holder.qty.setText("QTY : " + m.getQty());
        holder.jumlah.setText("Jumlah : Rp. " + m.getHargabrg());
        holder.ket.setText("Ket : " + m.getKet());


        Glide
                .with(mContext)
                .load(URL_GAMBAR + "/" + m.getGambar())
                .fitCenter()
                //.override(250, 250)
                .crossFade()
                .error(R.drawable.ic_bayar)
                .into(holder.gambar);
        return convertView;
    }

    private class ViewHolder {
        TextView nama, qty, jumlah, ket;
        ImageView gambar;
    }
}

