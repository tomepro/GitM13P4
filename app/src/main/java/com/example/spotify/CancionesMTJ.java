package com.example.spotify;

import android.graphics.Bitmap;

public class CancionesMTJ {
    private String nombre;
    private String artista;
    private String rutaCancion;
    private Bitmap imagen;

    public CancionesMTJ(String nombre, String artista, String rutaCancion) {
        this.nombre = nombre;
        this.artista = artista;
        this.rutaCancion = rutaCancion;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public String getArtista() {
        return artista;
    }

    public String getRutaCancion() {  // Cambiado el nombre de getRecursoAudio a getRutaCancion
        return rutaCancion;
    }

public Bitmap getImagen() {
        return imagen;
    }
}
