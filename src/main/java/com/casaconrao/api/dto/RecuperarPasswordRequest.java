package com.casaconrao.api.dto;

public class RecuperarPasswordRequest {

    private String correo;
    private String telefono;
    private String nuevaPass;

    public RecuperarPasswordRequest() {
    }

    public String getCorreo() {
        return correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getNuevaPass() {
        return nuevaPass;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setNuevaPass(String nuevaPass) {
        this.nuevaPass = nuevaPass;
    }
}