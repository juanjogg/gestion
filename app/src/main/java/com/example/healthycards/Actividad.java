package com.example.healthycards;



public class Actividad {
    private String uID;
    private String descripcion;
    private String nombre;
    private String dificultad;
    private int duracionMin;
    private String imgUri;

    public Actividad(String nombre, String descripcion, int duracionMin, String dificultad, String uID){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracionMin = duracionMin;
        this.dificultad = dificultad;
        this.uID = uID;
        this.imgUri = "";
    }

    public Actividad() {

    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public int getDuracionMin() {
        return duracionMin;
    }

    public void setDuracionMin(int duracionMin) {
        this.duracionMin = duracionMin;
    }

    public String getuID() {
        return uID;
    }
}

