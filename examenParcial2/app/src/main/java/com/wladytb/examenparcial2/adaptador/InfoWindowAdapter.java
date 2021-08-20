package com.wladytb.examenparcial2.adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;
import com.wladytb.examenparcial2.R;

public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    LayoutInflater inflater = null;

    public InfoWindowAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View informacion = inflater.inflate(R.layout.makerpersonalizado, null);
        TextView txtPais = (TextView) informacion.findViewById(R.id.txtPais);
        TextView txtCapital = (TextView) informacion.findViewById(R.id.txtCapital);
        TextView txtCoordenadas = (TextView) informacion.findViewById(R.id.txtCoordenadas);
        TextView txtCodigoPais = (TextView) informacion.findViewById(R.id.txtCodigoPais);
        TextView txtPrefijo = (TextView) informacion.findViewById(R.id.txtPrefijo);
        ImageView img = (ImageView) informacion.findViewById(R.id.imgPais);
        txtPais.setText(marker.getTitle());
        txtCapital.setText(marker.getSnippet().split(";")[0].trim());
        txtCodigoPais.setText(marker.getSnippet().split(";")[1].trim());
        Picasso.get().load(marker.getSnippet().split(";")[5].toString().trim()).into(img);
        txtCoordenadas.setText(marker.getSnippet().split(";")[3].trim());
        txtPrefijo.setText(marker.getSnippet().split(";")[4].trim());
        return informacion;
    }
}
