package com.casaconrao.api.dto;

import com.casaconrao.api.model.Mesa;
import com.casaconrao.api.model.Reserva;

import java.util.List;

public class ReservaResponse {

    private Integer idReserva;
    private Integer numeroPersonas;
    private String fecha;
    private String estado;

    // Compatibilidad antigua
    private Integer idMesa;
    private Integer numeroMesa;

    // Nuevo sistema
    private List<Integer> idsMesas;
    private List<Integer> numerosMesas;
    private Integer capacidadTotal;

    private Integer idCliente;

    public ReservaResponse(Reserva reserva) {
        this.idReserva = reserva.getIdReserva();
        this.numeroPersonas = reserva.getNumeroPersonas();
        this.fecha = reserva.getFecha().toString();
        this.estado = reserva.getEstado();

        if (reserva.getMesa() != null) {
            this.idMesa = reserva.getMesa().getIdMesa();
            this.numeroMesa = reserva.getMesa().getNumero();
        }

        if (reserva.getMesas() != null && !reserva.getMesas().isEmpty()) {
            this.idsMesas = reserva.getMesas()
                    .stream()
                    .map(Mesa::getIdMesa)
                    .toList();

            this.numerosMesas = reserva.getMesas()
                    .stream()
                    .map(Mesa::getNumero)
                    .toList();

            this.capacidadTotal = reserva.getMesas()
                    .stream()
                    .mapToInt(Mesa::getCapacidad)
                    .sum();
        }

        if (reserva.getCliente() != null) {
            this.idCliente = reserva.getCliente().getIdCliente();
        }
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

    public List<Integer> getIdsMesas() {
        return idsMesas;
    }

    public List<Integer> getNumerosMesas() {
        return numerosMesas;
    }

    public Integer getCapacidadTotal() {
        return capacidadTotal;
    }

    public Integer getIdCliente() {
        return idCliente;
    }
}