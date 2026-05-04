package com.casaconrao.api.repository;

import com.casaconrao.api.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    List<Reserva> findByClienteIdCliente(Integer idCliente);

    List<Reserva> findByClienteIdClienteAndEstadoIn(
            Integer idCliente,
            List<String> estados
    );
}