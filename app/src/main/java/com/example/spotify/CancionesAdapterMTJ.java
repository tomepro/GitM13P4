package com.example.spotify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class CancionesAdapterMTJ extends ArrayAdapter<CancionesMTJ> {

    public CancionesAdapterMTJ(Context context, int resource, List<CancionesMTJ> canciones) {
        super(context, resource, canciones);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtén el elemento de la lista en la posición actual
        CancionesMTJ cancion = getItem(position);

        // Reutiliza la vista si ya existe, o infla una nueva si es necesario
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        // Configura el texto y el color en el TextView
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(cancion.getNombre());
        textView.setTextColor(getContext().getResources().getColor(android.R.color.white)); // Color blanco

        return convertView;
    }
}
