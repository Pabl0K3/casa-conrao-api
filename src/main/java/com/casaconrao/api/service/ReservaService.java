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
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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
        reserva.setMesas(mesasSeleccionadas);

        return reservaRepository.save(reserva);
    }

    public void cancelarReserva(Integer idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        reserva.setEstado("Cancelada");

        reservaRepository.save(reserva);
    }

    public List<Mesa> buscarMesasDisponibles(Integer numeroPersonas, String fechaTexto) {
        LocalDateTime fecha = LocalDateTime.parse(fechaTexto);

        validarHorarioApertura(fecha);

        List<Mesa> mesasDisponibles = mesaRepository.findAll()
                .stream()
                .filter(mesa -> !mesaOcupadaEnRango(mesa.getIdMesa(), fecha))
                .toList();

        List<Mesa> mejorCombinacion = new ArrayList<>();

        buscarMejorCombinacion(
                mesasDisponibles,
                numeroPersonas,
                0,
                new ArrayList<>(),
                mejorCombinacion
        );

        if (mejorCombinacion.isEmpty()) {
            throw new RuntimeException("No hay combinación de mesas disponible");
        }

        return mejorCombinacion;
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

    private boolean mesaOcupadaEnRango(Integer idMesa, LocalDateTime fecha) {
        LocalDateTime inicio = fecha.minusHours(2);
        LocalDateTime fin = fecha.plusHours(2);

        List<Reserva> reservas = reservaRepository.findAll();

        for (Reserva reserva : reservas) {
            if ("Cancelada".equals(reserva.getEstado())) {
                continue;
            }

            boolean dentroDelRango =
                    !reserva.getFecha().isBefore(inicio)
                            && !reserva.getFecha().isAfter(fin);

            if (dentroDelRango) {
                for (Mesa mesa : reserva.getMesas()) {
                    if (mesa.getIdMesa().equals(idMesa)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private void buscarMejorCombinacion(
            List<Mesa> mesas,
            int numeroPersonas,
            int indice,
            List<Mesa> actual,
            List<Mesa> mejor
    ) {
        int capacidadActual = actual.stream()
                .mapToInt(Mesa::getCapacidad)
                .sum();

        int capacidadMejor = mejor.stream()
                .mapToInt(Mesa::getCapacidad)
                .sum();

        if (capacidadActual >= numeroPersonas) {
            if (mejor.isEmpty()
                    || capacidadActual < capacidadMejor
                    || (capacidadActual == capacidadMejor && actual.size() < mejor.size())) {
                mejor.clear();
                mejor.addAll(actual);
            }
            return;
        }

        if (indice >= mesas.size()) {
            return;
        }

        actual.add(mesas.get(indice));
        buscarMejorCombinacion(mesas, numeroPersonas, indice + 1, actual, mejor);

        actual.remove(actual.size() - 1);
        buscarMejorCombinacion(mesas, numeroPersonas, indice + 1, actual, mejor);
    }
}