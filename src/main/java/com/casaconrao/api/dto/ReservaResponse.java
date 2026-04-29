package com.casaconrao.api.dto;

import com.casaconrao.api.model.Reserva;

public class ReservaResponse {

    private Integer idReserva;
    private Integer numeroPersonas;
    private String fecha;
    private String estado;
    private Integer idMesa;
    private Integer numeroMesa;
    private Integer idCliente;

    public ReservaResponse(Reserva reserva) {
        this.idReserva = reserva.getIdReserva();
        this.numeroPersonas = reserva.getNumeroPersonas();
        this.fecha = reserva.getFecha().toString();
        this.estado = reserva.getEstado();
        this.idMesa = reserva.getMesa().getIdMesa();
        this.numeroMesa = reserva.getMesa().getNumero();
        this.idCliente = reserva.getCliente().getIdCliente();
    }

    public Integer getIdReserva() {
        return idReserva;
    }

    public Integer getNumeroPersonas() {
        return numeroPersonas;
    }

    public String getFecha() {
        return fecha;
    }

    public String getEstado() {
        return estado;
    }

    public Integer getIdMesa() {
        return idMesa;
    }

    public Integer getNumeroMesa() {
        return numeroMesa;
    }

    public Integer getIdCliente() {
        return idCliente;
    }
}