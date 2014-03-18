package com.castro.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.castro.app.data.Rutas;
import com.castro.app.data.RutasList;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link RutasListActivity}
 * in two-pane mode (on tablets) or a {@link RutasDetailActivity}
 * on handsets.
 */
public class RutasDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    private ImageView imageViewPhoto;

    //    private DummyContent.DummyItem mItem;
    private Rutas rutas;

    public RutasDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
//            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            //Obtener la persona que se encuentre en la posición que se ha seleccionado dentro de la lista
            int rutasIndex = Integer.valueOf(getArguments().getString(ARG_ITEM_ID));
            rutas = RutasList.getRutasList().get(rutasIndex);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);

//        if (mItem != null) {
//            ((TextView) rootView.findViewById(R.id.person_detail)).setText(mItem.content);
//        }
        if(rutas != null) {
            ((EditText) rootView.findViewById(R.id.editTextNombre)).setText(rutas.getNombre());
            ((EditText) rootView.findViewById(R.id.editTextAlias)).setText(rutas.getAlias());
            ((EditText) rootView.findViewById(R.id.editTextKilometros)).setText(rutas.getKilometros());
            ((EditText) rootView.findViewById(R.id.editTextDificultad)).setText(rutas.getDificultad());
            ((EditText) rootView.findViewById(R.id.editTextLatitud)).setText(rutas.getLatitud());
            ((EditText) rootView.findViewById(R.id.editTextLongitud)).setText(rutas.getLongitud());
            ((EditText) rootView.findViewById(R.id.editTextLocalidad)).setText(rutas.getLocalidad());
            ((EditText) rootView.findViewById(R.id.editTextProvincia)).setText(rutas.getProvincia());
            ((EditText) rootView.findViewById(R.id.editTextPais)).setText(rutas.getPais());
            ((EditText) rootView.findViewById(R.id.editTextComentario)).setText(rutas.getComentario());
            //Dar formato a la fecha para mostrarla

            //Descargar foto y mostrarla al finalizar la descarga, sólo si hay un nombre de archivo para la imagen
            if(!rutas.getFotoruta().trim().isEmpty() || rutas.getFotoruta()!=null) {
                imageViewPhoto = ((ImageView)rootView.findViewById(R.id.imageViewPhoto));
                ImageDownloader imageDownloader = new ImageDownloader();
                imageDownloader.execute(RutasListFragment.URL_IMAGES + rutas.getFotoruta());
            }
        }

        return rootView;
    }



    private class ImageDownloader extends AsyncTask<String, Void, Void> {

        private Bitmap bitmap;

        @Override
        protected Void doInBackground(String... strings) {
            String urlImage = strings[0];
            bitmap = getImageBitmap(urlImage);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            imageViewPhoto.setImageBitmap(bitmap);
        }

        private Bitmap getImageBitmap(String url) {
            Bitmap bm = null;
            try {
                URL aURL = new URL(url);
                URLConnection conn = aURL.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
                is.close();
            } catch (IOException e) {
                Log.e(ImageDownloader.class.getName(), "Error getting bitmap", e);
            }
            return bm;
        }
    }

}
