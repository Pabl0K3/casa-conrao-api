package com.casaconrao.api.dto;

public class LoginRequest {

    private String correo;
    private String pass;

    public LoginRequest() {
    }

    public String getCorreo() {
        return correo;
    }

    public String getPass() {
        return pass;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}