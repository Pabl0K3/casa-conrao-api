package com.casaconrao.api.service;

import com.casaconrao.api.dto.ReservaRequest;
import com.casaconrao.api.model.Cliente;
import com.casaconrao.api.model.Mesa;
import com.casaconrao.api.model.Reserva;
import com.casaconrao.api.repository.ClienteRepository;
import com.casaconrao.api.repository.MesaRepository;
import com.casaconrao.api.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MesaRepository mesaRepository;

    public List<Reserva> obtenerReservasCliente(Integer idCliente) {
        List<String> estadosActivos = List.of("Pendiente", "Confirmada");
        return reservaRepository.findByClienteIdClienteAndEstadoIn(idCliente, estadosActivos);
    }

    public Reserva crearReserva(ReservaRequest request) {
        Cliente cliente = clienteRepository.findById(request.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        LocalDateTime fecha = LocalDateTime.parse(request.getFecha());

        validarHorarioApertura(fecha);

        List<Mesa> mesasSeleccionadas = buscarMesasDisponibles(
                request.getNumeroPersonas(),
                request.getFecha()
        );

        Reserva reserva = new Reserva();
        reserva.setNumeroPersonas(request.getNumeroPersonas());
        reserva.setFecha(fecha);
        reserva.setEstado("Pendiente");
        reserva.setCliente(cliente);

        // Compatibilidad temporal: guardamos una mesa principal
        reserva.setMesa(mesasSeleccionadas.get(0));

        // Nuevo sistema: guardamos todas las mesas en ReservaMesa
        reserva.setMesas(mesasSeleccionadas);

        return reservaRepository.save(reserva);
    }

    public Mesa buscarMesaDisponible(Integer numeroPersonas, String fechaTexto) {
        LocalDateTime fecha = LocalDateTime.parse(fechaTexto);

        validarHorarioApertura(fecha);

        List<Mesa> mesas = mesaRepository.findByCapacidadGreaterThanEqual(numeroPersonas);

        for (Mesa mesa : mesas) {
            boolean ocupada = mesaOcupadaEnRango(mesa.getIdMesa(), fecha);

            if (!ocupada) {
                return mesa;
            }
        }

        throw new RuntimeException("No hay mesas disponibles");
    }

    public void cancelarReserva(Integer idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        reserva.setEstado("Cancelada");

        reservaRepository.save(reserva);
    }

    private void validarHorarioApertura(LocalDateTime fecha) {
        LocalTime hora = fecha.toLocalTime();

        LocalTime apertura = LocalTime.of(12, 0);
        LocalTime cierre = LocalTime.of(1, 0);

        boolean dentroHorario = !hora.isBefore(apertura) || !hora.isAfter(cierre);

        if (!dentroHorario) {
            throw new RuntimeException("Fuera del horario de apertura");
        }
    }

    private void validarMesaDisponible(Integer idMesa, LocalDateTime fecha) {
        if (mesaOcupadaEnRango(idMesa, fecha)) {
            throw new RuntimeException("Mesa no disponible en ese horario");
        }
    }

    private boolean mesaOcupadaEnRango(Integer idMesa, LocalDateTime fecha) {
        LocalDateTime inicio = fecha.minusHours(2);
        LocalDateTime fin = fecha.plusHours(2);

        return reservaRepository.existsByMesaIdMesaAndFechaBetweenAndEstadoNot(
                idMesa,
                inicio,
                fin,
                "Cancelada"
        );
    }
    
    public List<Mesa> buscarMesasDisponibles(Integer numeroPersonas, String fechaTexto) {
        LocalDateTime fecha = LocalDateTime.parse(fechaTexto);

        validarHorarioApertura(fecha);

        // Todas las mesas ordenadas de mayor a menor capacidad
        List<Mesa> mesas = mesaRepository.findAll()
                .stream()
                .sorted((a, b) -> b.getCapacidad() - a.getCapacidad())
                .toList();

        List<Mesa> seleccionadas = new ArrayList<>();
        int capacidadTotal = 0;

        for (Mesa mesa : mesas) {
            boolean ocupada = mesaOcupadaEnRango(mesa.getIdMesa(), fecha);

            if (!ocupada) {
                seleccionadas.add(mesa);
                capacidadTotal += mesa.getCapacidad();
            }

            if (capacidadTotal >= numeroPersonas) {
                return seleccionadas;
            }
        }

        throw new RuntimeException("No hay combinación de mesas disponible");
    }
}