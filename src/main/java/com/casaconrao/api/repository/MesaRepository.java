package com.casaconrao.api.repository;

import com.casaconrao.api.model.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MesaRepository extends JpaRepository<Mesa, Integer> {

    List<Mesa> findByCapacidadGreaterThanEqual(Integer capacidad);

}