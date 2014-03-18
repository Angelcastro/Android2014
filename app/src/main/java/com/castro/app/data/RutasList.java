package com.castro.app.data;

import java.util.ArrayList;

public class RutasList {

    private static ArrayList<Rutas> rutasList;

    public static ArrayList<Rutas> getRutasList() {
        return rutasList;
    }

    public static void setRutasList(ArrayList<Rutas> rutasList) {
        RutasList.rutasList = rutasList;
    }

}
