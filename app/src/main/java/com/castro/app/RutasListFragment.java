package com.castro.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import com.castro.app.data.RutasDBManagerAndroid;
import com.castro.app.data.RutasDownloader;
import com.castro.app.data.RutasList;
import com.castro.app.data.Rutas;

/**
 * A list fragment representing a list of Items. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link RutasDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class RutasListFragment extends ListFragment {

    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private Callbacks mCallbacks = sDummyCallbacks;
    private int mActivatedPosition = ListView.INVALID_POSITION;

    //Direcciones desde las que se obtendrán los datos
    public static final String URL_DATA = "http://192.168.200.101/xmlFichero.xml";
    public static final String URL_IMAGES = "http://192.168.200.101/imagenes/";

    //Almacenará el contexto (Activity) en el que se encuentra este fragment, ya que será necesario para abrir la BD
    private Context context;

    public interface Callbacks {
        public void onItemSelected(String id);
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    public RutasListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Este bloque, con algunos cambios, se ha trasladado al método showPersonList
//        setListAdapter(new ArrayAdapter<DummyContent.DummyItem>(
//                getActivity(),
//                android.R.layout.simple_list_item_activated_1,
//                android.R.id.text1,
//                DummyContent.ITEMS));

        //Descargar los datos del documento XML
        RutasDownloader rutasDownloader = new RutasDownloader(context, this);
        rutasDownloader.execute(URL_DATA);
        // Mostrar la lista que haya de momento en la BD, hasta que finalice la descarga anterior
        showRutasList();
    }

    //Este método también es ejecutado desde el onPostExecute de la descarga del XML
    public void showRutasList() {
        //Conectar con la BD y obtener los datos necesarios para rellenar la lista
        RutasDBManagerAndroid rutasDBManagerAndroid = new RutasDBManagerAndroid(context);
        RutasList.setRutasList(rutasDBManagerAndroid.getRutasList());

        //Indicar en el primer parámetro el tipo de objetos y en el último parámetro el nombre de la lista obtenida antes
        setListAdapter(new ArrayAdapter<Rutas>(
                getActivity(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                RutasList.getRutasList()));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;

        //Guardar una referencia al contexto, ya que hará falta para crear la BD
        context = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

//        mCallbacks.onItemSelected(DummyContent.ITEMS.get(position).id);
        //Enviar al fragment de detalle la posición del elemento que se ha pulsado
        Log.d(RutasListFragment.class.getName(), "Se ha detectado clic en el registro con posición: "+position);
        mCallbacks.onItemSelected(String.valueOf(position));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }
}
