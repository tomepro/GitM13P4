package com.example.spotify;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class SearchFragmentMTJ extends Fragment {

    private ListView listView;
    static List<CancionesMTJ> listaCanciones;
    private CancionesAdapterMTJ adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        listaCanciones = new ArrayList<>();
        cargarCancionesDesdeDispositivo();

        adapter = new CancionesAdapterMTJ(getContext(), android.R.layout.simple_list_item_1, listaCanciones);

        listView = view.findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mostrarDetalleCancion(position);
            }
        });

        return view;
    }

    //private void cargarCancionesDesdeDispositivo() {
     //   String[] projection = {
       //         MediaStore.Audio.Media.TITLE,
         //       MediaStore.Audio.Media.ARTIST,
           //     MediaStore.Audio.Media.DATA
      //  };

        //Cursor cursor = getActivity().getContentResolver().query(
          //      MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            //    projection,
              //  null,
                //null,
               // null
       // );

       // if (cursor != null && cursor.moveToFirst()) {
       //     int titleColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
       //     int artistColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
       //     int dataColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
//
        //    do {
       //         String title = (titleColumnIndex != -1) ? cursor.getString(titleColumnIndex) : "";
       //         String artist = (artistColumnIndex != -1) ? cursor.getString(artistColumnIndex) : "";
       //         String path = (dataColumnIndex != -1) ? cursor.getString(dataColumnIndex) : "";

        //        listaCanciones.add(new Canciones(title, artist, path));
        //    } while (cursor.moveToNext());

        //    cursor.close();
        //}
    //}

    private void cargarCancionesDesdeDispositivo() {
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA
        };

        // Ruta específica de la carpeta Downloads
        String selection = MediaStore.Audio.Media.DATA + " LIKE ?";
        String[] selectionArgs = new String[]{"%Download%"};

        Cursor cursor = getActivity().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int titleColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artistColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do {
                String title = (titleColumnIndex != -1) ? cursor.getString(titleColumnIndex) : "";
                String artist = (artistColumnIndex != -1) ? cursor.getString(artistColumnIndex) : "";
                String path = (dataColumnIndex != -1) ? cursor.getString(dataColumnIndex) : "";

                // Agrega solo archivos de audio encontrados en Downloads
                listaCanciones.add(new CancionesMTJ(title, artist, path));
            } while (cursor.moveToNext());

            cursor.close();
        }
    }
    ///AQUI ESTA E HOT FIXXX

    private void mostrarDetalleCancion(int position) {
        DetalleCancionFragmentMTJ detalleCancionFragment = new DetalleCancionFragmentMTJ();
        Bundle bundle = new Bundle();
        bundle.putString("nombreCancion", listaCanciones.get(position).getNombre());
        bundle.putString("artista", listaCanciones.get(position).getArtista());
        bundle.putString("rutaCancion", listaCanciones.get(position).getRutaCancion());
        detalleCancionFragment.setArguments(bundle);

        Log.d("SearchFragment", "Mostrando DetalleCancionFragment");

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, detalleCancionFragment)
                .addToBackStack(null)
                .commit();
    }
}
