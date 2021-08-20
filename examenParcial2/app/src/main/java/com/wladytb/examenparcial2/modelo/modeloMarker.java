package com.wladytb.examenparcial2.modelo;

public class modeloMarker {
    String facultad, decano, logo,lat,lng,correo;

    public modeloMarker() {
    }

    public modeloMarker(String facultad, String decano, String correo, String logo, String lat, String lng) {
        this.facultad = facultad;
        this.decano = decano;
        this.correo=correo;
        this.logo = logo;
        this.lat = lat;
        this.lng = lng;

    }

    public String getFacultad() {
        return facultad;
    }

    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }

    public String getDecano() {
        return decano;
    }

    public void setDecano(String decano) {
        this.decano = decano;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
