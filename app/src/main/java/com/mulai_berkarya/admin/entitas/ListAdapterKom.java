package com.mulai_berkarya.admin.entitas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mulai_berkarya.admin.cimol.R;

/**
 * Created by Mastah on 22/09/2016.
 */

public class ListAdapterKom extends ArrayAdapter<String> {
    private final Context context;
    private final String[] vnama;
    private final String[] vjudul;
    private final String[] visi;


    public ListAdapterKom(Context context, String[] vnama,
                          String[] vjudul, String[] visi) {
        super(context, R.layout.komentar_tunggal, vnama);
        this.context = context;
        this.vnama = vnama;
        this.vjudul = vjudul;
        this.visi = visi;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.komentar_tunggal, parent, false);
        TextView nama = (TextView) rowView.findViewById(R.id.usernameNya);
        TextView judul = (TextView) rowView.findViewById(R.id.judul);
        TextView isi = (TextView) rowView.findViewById(R.id.komentar);

        nama.setText(vnama[position]);
        judul.setText(vjudul[position]);
        isi.setText(visi[position]);

        return rowView;
    }

}

