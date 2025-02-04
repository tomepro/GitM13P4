package com.example.spotify;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import static com.example.spotify.SearchFragmentMTJ.listaCanciones;

import java.io.IOException;

public class DetalleCancionFragmentMTJ extends Fragment {

    private ImageView imagenCancionImageView;
    private TextView nombreCancionTextView;
    private TextView artistaTextView;
    private String rutaCancion;
    private SeekBar seekBar;

    static MediaPlayer mediaPlayer = new MediaPlayer();
    private int currentSongIndex;

    public ImageButton btnPlay, btnNext, btnPrevious;

    private Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detalle_cancion, container, false);

        imagenCancionImageView = view.findViewById(R.id.imageView3);
        nombreCancionTextView = view.findViewById(R.id.nombreCancionTextView);
        artistaTextView = view.findViewById(R.id.artistaTextView);
        seekBar = view.findViewById(R.id.seekBar);
        btnPlay = view.findViewById(R.id.btnPlay);
        btnNext = view.findViewById(R.id.btnSiguiente);
        btnPrevious = view.findViewById(R.id.btnAnterior);

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }

        handler = new Handler();



        // Add SeekBar functionality
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Handle tracking start if needed
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Handle tracking stop if needed
            }
        });

        btnPlay.setOnClickListener(v -> toggleReproduccion());
        btnNext.setOnClickListener(v -> reproducirSiguienteCancion());
        btnPrevious.setOnClickListener(v -> reproducirCancionAnterior());

        Bundle bundle = getArguments();
        if (bundle != null) {
            String nombreCancion = bundle.getString("nombreCancion", "");
            String artista = bundle.getString("artista", "");
            rutaCancion = bundle.getString("rutaCancion", "");

            // Configurar textos
            nombreCancionTextView.setText(nombreCancion);
            artistaTextView.setText(artista);

            // Obtener la imagen de la canción desde los metadatos
            Bitmap imagenCancion = obtenerImagenCancionDesdeMetadata(rutaCancion);
            if (imagenCancion != null) {
                imagenCancionImageView.setImageBitmap(imagenCancion);
            } else {
                // Si no hay imagen, puedes mostrar una imagen predeterminada o dejarla vacía
                imagenCancionImageView.setImageResource(R.drawable.ruc);
            }
        }

        // Configurar la reproducción y la barra de progreso
        configurarReproduccion();

        return view;
    }

    private void configurarReproduccion() {
        try {
            mediaPlayer.setDataSource(rutaCancion);
            mediaPlayer.prepare();
            mediaPlayer.start();

            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            actualizarSeekBar();

            mediaPlayer.setOnCompletionListener(mp -> reproducirSiguienteCancion());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void actualizarSeekBar() {
        getActivity().runOnUiThread(() -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                seekBar.setProgress(mCurrentPosition);
            }
            handler.postDelayed(this::actualizarSeekBar, 1000);
        });
    }

    private Bitmap obtenerImagenCancionDesdeMetadata(String rutaCancion) {
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(rutaCancion);
            byte[] imagenBytes = retriever.getEmbeddedPicture();
            if (imagenBytes != null) {
                return BitmapFactory.decodeByteArray(imagenBytes, 0, imagenBytes.length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void toggleReproduccion() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btnPlay.setImageResource(R.drawable.baseline_play_arrow_24);
        } else {
            mediaPlayer.start();
            btnPlay.setImageResource(R.drawable.baseline_pause_24);
        }
    }

    private void reproducirSiguienteCancion() {
        currentSongIndex = (currentSongIndex + 1) % listaCanciones.size();
        cambiarCancion(currentSongIndex);
    }

    private void reproducirCancionAnterior() {
        currentSongIndex = (currentSongIndex - 1 + listaCanciones.size()) % listaCanciones.size();
        cambiarCancion(currentSongIndex);
    }

    private void cambiarCancion(int index) {
        detenerReproduccion();
        rutaCancion = listaCanciones.get(index).getRutaCancion();
        configurarReproduccion();

        // Actualizar la información de la canción en la UI
        String nombreCancion = listaCanciones.get(index).getNombre();
        String artista = listaCanciones.get(index).getArtista();

        // Configurar textos
        nombreCancionTextView.setText(nombreCancion);
        artistaTextView.setText(artista);

        // Obtener la imagen de la canción desde los metadatos
        Bitmap imagenCancion = obtenerImagenCancionDesdeMetadata(rutaCancion);
        if (imagenCancion != null) {
            imagenCancionImageView.setImageBitmap(imagenCancion);
        } else {
            // Si no hay imagen, puedes mostrar una imagen predeterminada o dejarla vacía
            imagenCancionImageView.setImageResource(R.drawable.ruc);
        }
    }

    private void detenerReproduccion() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        detenerReproduccion();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        handler.removeCallbacksAndMessages(null);
    }
}
