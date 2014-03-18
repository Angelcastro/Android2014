package com.castro.app.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import com.castro.app.RutasListFragment;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.castro.app.RutasListFragment;

public class RutasDownloader extends AsyncTask<String, Void, Void> {

    private final String NAMESPACE = null;
    private XmlPullParser parser = Xml.newPullParser();

    private static final String TAG_XML = "address_book";
    private static final String TAG_REGISTRO = "rutas";
    private ArrayList<Rutas> listaDatos;
    private Context context;
    private RutasListFragment RutasListFragment;

    public RutasDownloader(Context context, RutasListFragment senderoListFragment) {
        this.context = context;
        this.RutasListFragment = senderoListFragment;
    }

    private Rutas leerRegistro() {
        //Crear una variable para cada dato del objeto
        int id = -1;
        String nombre = "";
        String alias = "";
        String kilometros = "";
        String dificultad = "";
        String latitud = "";
        String longitud = "";
        String localidad = "";
        String provincia = "";
        String pais = "";
        String comentario = "";
        String fotoruta = "";
        try {
            parser.require(XmlPullParser.START_TAG, NAMESPACE, TAG_REGISTRO);
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String tagName = parser.getName();
                if (tagName.equals("id")) {
                    id = Integer.valueOf(readText(tagName));
                } else if (tagName.equals("nombre")) {
                    nombre = readText(tagName);
                } else if (tagName.equals("alias")) {
                    alias = readText(tagName);
                } else if (tagName.equals("kilometros")) {
                    kilometros = readText(tagName);
                } else if (tagName.equals("dificultad")) {
                    dificultad = readText(tagName);
                } else if (tagName.equals("latitud")) {
                    latitud = readText(tagName);
                } else if (tagName.equals("longitud")) {
                    longitud = readText(tagName);
                } else if (tagName.equals("localidad")) {
                    localidad = readText(tagName);
                } else if (tagName.equals("provincia")) {
                    provincia = readText(tagName);
                } else if (tagName.equals("pais")) {
                    pais = readText(tagName);
                } else if (tagName.equals("comentario")) {
                    comentario = readText(tagName);
                } else if (tagName.equals("fotoruta")) {
                    fotoruta = readText(tagName);
                } else {
                    skip();
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Rutas rutas = new Rutas(id, nombre, alias, kilometros, dificultad, latitud, longitud, localidad, provincia, pais, comentario, fotoruta);
        return rutas;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.d(RutasDownloader.class.getName(), "Descarga de datos finalizada. Iniciando procesamiento");
        Log.d(RutasDownloader.class.getName(), "Nº registros descargados: "+listaDatos.size());
        //Conectar con la BD y recorrer los elementos descargados desde el XML
        RutasDBManagerAndroid addressBookDBManagerAndroid = new RutasDBManagerAndroid(context);
        for (Rutas rutas : listaDatos) {
            //Comprobar si ya existe un elemento con la misma ID
            Log.d(RutasDownloader.class.getName(), "Comprobando si exite el id: "+rutas.getId());
            if (addressBookDBManagerAndroid.getRutasByID(rutas.getId()) == null) {
                Log.d(RutasDownloader.class.getName(), "Id no encontrado. Se insertará el registro");
                //Si no existe, se inserta
                addressBookDBManagerAndroid.insertRutas(rutas);
            } else {
                Log.d(RutasDownloader.class.getName(), "Id existente. Se actualizará el registro");
                //Ya existe un contacto con ese id, se actualiza con los datos descargados
                addressBookDBManagerAndroid.updateRutas(rutas);
            }
        }

        //Borrar los contactos del teléfono que no estén en el XML descargado
        ArrayList<Rutas> rutasListDB = addressBookDBManagerAndroid.getRutasList();
        //Se recorre cada persona de la BD local comprobando si existen en los datos del documento XML
        for (Rutas rutas : rutasListDB) {
            //Para comparar si dos personas son iguales se ha sobrecargado el método equals en la clase Person
            if(listaDatos.indexOf(rutas)==-1) {
                Log.d(RutasDownloader.class.getName(), "Rutas a eliminar: " +rutas.toString());
                //Si se observa que no está en el XML, se elimina de la BD
                addressBookDBManagerAndroid.deleteRutasById(rutas.getId());
            }
        }

        //Mostrar la lista una vez finalizada la descarga
        RutasListFragment.showRutasList();
    }

    @Override
    protected Void doInBackground(String... urls) {
        Log.d(RutasDownloader.class.getName(), "Iniciando descarga de datos en segundo plano");
        InputStream stream = null;
        try {
            Log.d(RutasDownloader.class.getName(), "Dirección de descarga: "+urls[0]);
            stream = downloadUrl(urls[0]);
            listaDatos = xmlToList(stream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        return conn.getInputStream();
    }


    private ArrayList xmlToList(InputStream stream) {
        Log.d(RutasDownloader.class.getName(), "Iniciando interpretación de datos XML");
        ArrayList list = new ArrayList();
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(stream, null);
            parser.nextTag();
            Log.d(RutasDownloader.class.getName(), "Primera etiqueta encontrada: "+parser.getName());
            parser.require(XmlPullParser.START_TAG, NAMESPACE, TAG_XML);
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                Log.d(RutasDownloader.class.getName(), "Etiqueta encontrada: "+parser.getName());
                if (name.equals(TAG_REGISTRO)) {
                    list.add(leerRegistro());
                } else {
                    skip();
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void skip() throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private String readText(String tag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, tag);
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, NAMESPACE, tag);
        return result;
    }


}
