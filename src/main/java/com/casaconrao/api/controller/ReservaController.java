package com.casaconrao.api.controller;

import com.casaconrao.api.dto.ReservaRequest;
import com.casaconrao.api.dto.ReservaResponse;
import com.casaconrao.api.model.Reserva;
import com.casaconrao.api.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {

	@Autowired
	private ReservaService reservaService;

	@GetMapping("/cliente/{idCliente}")
	public List<ReservaResponse> obtenerReservasCliente(@PathVariable Integer idCliente) {
		List<Reserva> reservas = reservaService.obtenerReservasCliente(idCliente);

		return reservas.stream().map(ReservaResponse::new).toList();
	}

	@PostMapping
	public ReservaResponse crearReserva(@RequestBody ReservaRequest request) {
		Reserva reserva = reservaService.crearReserva(request);
		return new ReservaResponse(reserva);
	}

	@DeleteMapping("/{idReserva}")
	public String cancelarReserva(@PathVariable Integer idReserva) {
		reservaService.cancelarReserva(idReserva);
		return "Reserva cancelada correctamente";
	}

	@GetMapping("/disponibilidad")
	public Integer buscarMesaDisponible(@RequestParam Integer numeroPersonas, @RequestParam String fecha) {
		return reservaService.buscarMesaDisponible(numeroPersonas, fecha).getIdMesa();
	}
}