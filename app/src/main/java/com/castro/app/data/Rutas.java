package com.castro.app.data;

/**
 * Created by Castro on 12/03/14.
 */
public class Rutas {
    private int id;
    private String nombre = "";
    private String alias = "";
    private String kilometros = "";
    private String dificultad = "";
    private String latitud = "";
    private String longitud = "";
    private String localidad = "";
    private String provincia = "";
    private String pais = "";
    private String comentario = "";
    private String fotoruta = "";

    public Rutas() {
    }

    public Rutas(int id, String nombre, String alias, String kilometros, String dificultad, String latitud, String longitud, String localidad, String provincia, String pais, String comentario, String fotoruta) {
        this.id = id;
        this.nombre = nombre;
        this.alias = alias;
        this.kilometros = kilometros;
        this.dificultad = dificultad;
        this.latitud = latitud;
        this.longitud = longitud;
        this.localidad = localidad;
        this.provincia = provincia;
        this.pais = pais;
        this.comentario = comentario;
        this.fotoruta = fotoruta;
    }

    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the alia
     */
    public String getAlias() {
        return alias;
    }

    /**
     * @param alia the alia to set
     */
    public void setAlias(String alia) {
        this.alias = alia;
    }

    /**
     * @return the kilometros
     */
    public String getKilometros() {
        return kilometros;
    }

    /**
     * @param kilometros the kilometros to set
     */
    public void setKilometros(String kilometros) {
        this.kilometros = kilometros;
    }

    /**
     * @return the dificultad
     */
    public String getDificultad() {
        return dificultad;
    }

    /**
     * @param dificultad the dificultad to set
     */
    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    /**
     * @return the latitud
     */
    public String getLatitud() {
        return latitud;
    }

    /**
     * @param latitud the latitud to set
     */
    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    /**
     * @return the longitud
     */
    public String getLongitud() {
        return longitud;
    }

    /**
     * @param longitud the longitud to set
     */
    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    /**
     * @return the localidad
     */
    public String getLocalidad() {
        return localidad;
    }

    /**
     * @param localidad the localidad to set
     */
    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    /**
     * @return the provincia
     */
    public String getProvincia() {
        return provincia;
    }

    /**
     * @param provincia the provincia to set
     */
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    /**
     * @return the pais
     */
    public String getPais() {
        return pais;
    }

    /**
     * @param pais the pais to set
     */
    public void setPais(String pais) {
        this.pais = pais;
    }

    /**
     * @return the comentario
     */
    public String getComentario() {
        return comentario;
    }

    /**
     * @param comentario the comentario to set
     */
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    /**
     * @return the fotoruta
     */
    public String getFotoruta() {
        return fotoruta;
    }

    /**
     * @param fotoruta the fotoruta to set
     */
    public void setFotoruta(String fotoruta) {
        this.fotoruta = fotoruta;
    }

    @Override
    public String toString() {
        return nombre + " // " + alias;
    }

    @Override
    public boolean equals(Object o) {
        return this.id == ((Rutas)o).getId();
    }
}
