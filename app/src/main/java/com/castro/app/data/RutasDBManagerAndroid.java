package com.castro.app.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


public class RutasDBManagerAndroid {

    private static final String NOMBRE_BD = "db_Address_book";
    private static final int VERSION_BD = 1;
    private SQLiteDatabase dbAddressBook;
    private Context mContext;

    public RutasDBManagerAndroid(Context context) {
        mContext = context;
        AddressBookDBOpenHelper addressBookDBOpenHelper = new AddressBookDBOpenHelper(context, NOMBRE_BD, null, VERSION_BD);
        dbAddressBook = addressBookDBOpenHelper.getWritableDatabase();
    }

    public void insertRutas(Rutas rutas) {
        //Formato para campos de tipo fecha
        /*SimpleDateFormat dateFormat = new SimpleDateFormat("''yyyy-MM-dd''");
        Date birthDate = person.getBirthDate();
        String birthDateSql = null;
        if(birthDate!=null) {
            birthDateSql = dateFormat.format(birthDate);
        }*/

        String sql = "INSERT INTO rutas "
                //No se incluye el ID, ya que es autonumérico
                + "(id, nombre, alias, kilometros, dificultad, latitud, longitud, "
                + "localidad, provincia, pais, comentario, fotoruta) "
                + "VALUES ("
                + "'" + rutas.getId()+ "', "
                + "'" + rutas.getNombre()+ "', "
                + "'" + rutas.getAlias() + "', "
                + "'" + rutas.getKilometros() + "', "
                + "'" + rutas.getDificultad() + "', "
                + "'" + rutas.getLatitud() + "', "
                + "'" + rutas.getLongitud()+ "', "
                + "'" + rutas.getLocalidad() + "', "
                + "'" + rutas.getProvincia() + "', "
                + "'" + rutas.getPais() + "', "
                + "'" + rutas.getComentario()+ "', "
                + "'" + rutas.getFotoruta()+ "')";
        Log.d(RutasDBManagerAndroid.class.getName(), "Executing SQL statement: " + sql);
        dbAddressBook.execSQL(sql);
    }
    
    public ArrayList<Rutas> getRutasList() {
        ArrayList<Rutas> rutasList = new ArrayList();
        String sql = "SELECT * FROM rutas";
        Log.d(RutasDBManagerAndroid.class.getName(), "Executing SQL statement: " + sql);
        Cursor rs = dbAddressBook.rawQuery(sql, null);
        while(rs.moveToNext()) {
            int id = rs.getInt(0);
            String nombre = rs.getString(1);
            String alias = rs.getString(2);
            String kilometros = rs.getString(3);
            String dificultad = rs.getString(4);
            String latitud = rs.getString(5);
            String longitud = rs.getString(6);
            String localidad = rs.getString(7);
            String provincia = rs.getString(8);
            String pais = rs.getString(9);
            String comentario = rs.getString(10);
            String fotoruta = rs.getString(11);
            //En SQLite no hay datos de tipo DATE
            /*Date birthDate = null;
            String strBirthDate = rs.getString(12);
            if(strBirthDate!=null) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    birthDate = dateFormat.parse(strBirthDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }*/

            Rutas rutas = new Rutas(id, nombre, alias, kilometros, dificultad, latitud, longitud, localidad, provincia, pais, comentario, fotoruta);
            rutasList.add(rutas);
        }
        return rutasList;
    }
    
    public Rutas getRutasByID(int rutasId) {
        Rutas rutas = null;

        String sql = "SELECT * FROM rutas WHERE id="+rutasId;
        Log.d(RutasDBManagerAndroid.class.getName(), "Executing SQL statement: " + sql);
        Cursor rs = dbAddressBook.rawQuery(sql, null);

        if(rs.moveToNext()) {
            int id = rs.getInt(0);
            String nombre = rs.getString(1);
            String alias = rs.getString(2);
            String kilometros = rs.getString(3);
            String dificultad = rs.getString(4);
            String latitud = rs.getString(5);
            String longitud = rs.getString(6);
            String localidad = rs.getString(7);
            String provincia = rs.getString(8);
            String pais = rs.getString(9);
            String comentario = rs.getString(10);
            String fotoruta = rs.getString(11);
            //En SQLite no hay datos de tipo DATE
            /*Date birthDate = null;
            String strBirthDate = rs.getString(12);
            if(strBirthDate!=null) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    birthDate = dateFormat.parse(strBirthDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }*/
            rutas = new Rutas(id, nombre, alias, kilometros, dificultad, latitud, longitud, localidad, provincia, pais, comentario, fotoruta);
        }
        return rutas;
    }

    public void updateRutas(Rutas rutas) {
        //Formato para campos de tipo fecha
        /*SimpleDateFormat dateFormat = new SimpleDateFormat("''yyyy-MM-dd''");
        Date birthDate = person.getBirthDate();
        String birthDateSql = null;
        if(birthDate!=null) {
            birthDateSql = dateFormat.format(birthDate);
        }*/

        String sql = "UPDATE rutas SET "
                + "nombre='" + rutas.getNombre()+ "', "
                + "alias='" + rutas.getAlias()+ "', "
                + "kilometros='" + rutas.getKilometros()+ "', "
                + "dificultad='" + rutas.getDificultad()+ "', "
                + "latitud='" + rutas.getLatitud()+ "', "
                + "longitud='" + rutas.getLongitud()+ "', "
                + "localidad='" + rutas.getLocalidad()+ "', "
                + "provincia='" + rutas.getProvincia()+ "', "
                + "pais='" + rutas.getPais()+ "', "
                + "comentario='" + rutas.getComentario()+ "', "
                + "fotoruta='" + rutas.getFotoruta() +"' "
                + "WHERE id=" + rutas.getId();

        Log.d(RutasDBManagerAndroid.class.getName(), "Executing SQL statement: " + sql);
        dbAddressBook.execSQL(sql);
    }
    
    public void deleteRutasById(int id) {
        String sql = "DELETE FROM rutas WHERE id="+id;
        Log.d(RutasDBManagerAndroid.class.getName(), "Executing SQL statement: " + sql);
        dbAddressBook.execSQL(sql);
    }

    public class AddressBookDBOpenHelper extends SQLiteOpenHelper {

        public AddressBookDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = "CREATE TABLE IF NOT EXISTS rutas ("
                    + "id INT PRIMARY KEY, "
                    + "nombre VARCHAR(50), "
                    + "alias VARCHAR(50), "
                    + "kilometros VARCHAR(50), "
                    + "dificultad VARCHAR(50), "
                    + "latitud VARCHAR(50), "
                    + "longitud VARCHAR(255), "
                    + "localidad VARCHAR(10), "
                    + "provincia VARCHAR(50), "
                    + "pais VARCHAR(50), "
                    + "comentario VARCHAR(50), "
                    + "fotoruta VARCHAR(50))";

            Log.d(RutasDBManagerAndroid.class.getName(), "Executing SQL statement: " + sql);
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }


    }
    
}
