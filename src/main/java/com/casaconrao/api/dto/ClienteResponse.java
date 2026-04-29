package com.casaconrao.api.dto;

import com.casaconrao.api.model.Cliente;

public class ClienteResponse {

    private Integer idCliente;
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;

    public ClienteResponse(Cliente cliente) {
        this.idCliente = cliente.getIdCliente();
        this.nombre = cliente.getNombre();
        this.apellido = cliente.getApellido();
        this.telefono = cliente.getTelefono();
        this.correo = cliente.getCorreo();
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }
}