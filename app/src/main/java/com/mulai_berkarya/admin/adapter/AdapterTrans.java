package com.mulai_berkarya.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mulai_berkarya.admin.cimol.R;
import com.mulai_berkarya.admin.entitas.Trans;

import java.util.ArrayList;


/**
 * Created by DELL on 03/11/2016.
 */

public class AdapterTrans extends ArrayAdapter<Trans> {
    Context mContext;
    ArrayList<Trans> list;

    int layout;
   /* String URL_GAMBAR = Config.URL_BASE + "/Gambar/";*/

    public AdapterTrans(Context context,
                        int textViewResourceId,
                        ArrayList<Trans> list_params) {
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
            holder.idtrans = (TextView) convertView.findViewById(R.id.textViewidtrans);
            holder.tanggal = (TextView) convertView.findViewById(R.id.textViewtgl);
            holder.status = (TextView) convertView.findViewById(R.id.textViewStatus);
            holder.total = (TextView) convertView.findViewById(R.id.textViewtotal);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Trans t = list.get(position);
        holder.idtrans.setText("ID Transaksi : " + t.getId_transaksi());
        holder.tanggal.setText("Tanggal : " + t.getTanggal());
        //   holder.status.setText("Status " + t.getStatus());
        holder.total.setText("Total : " + t.getTotal());


        if (t.getStatus().equals("1") && t.getKonf_bay().equals("1") && t.getNo_resi().equals("")) {
            holder.status.setText("Status : Order");
        } else if (t.getStatus().equals("1") && t.getKonf_bay().equals("1") && !t.getNo_resi().equals("")) {
            holder.status.setText("Status : Selesai");
        }

        return convertView;
    }

    private class ViewHolder {
        TextView idtrans, tanggal, total, status;
        //ImageView gambar;
    }

}
