package com.casaconrao.api.controller;

import com.casaconrao.api.dto.ClienteResponse;
import com.casaconrao.api.dto.ClienteUpdateRequest;
import com.casaconrao.api.model.Cliente;
import com.casaconrao.api.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.casaconrao.api.dto.RecuperarPasswordRequest;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PutMapping("/{idCliente}")
    public ClienteResponse actualizarCliente(
            @PathVariable Integer idCliente,
            @RequestBody ClienteUpdateRequest request
    ) {
        Cliente cliente = clienteService.actualizarCliente(idCliente, request);
        return new ClienteResponse(cliente);
    }
    
    @PutMapping("/recuperar-password")
    public String recuperarPassword(@RequestBody RecuperarPasswordRequest request) {
        clienteService.recuperarPassword(request);
        return "Contraseña actualizada correctamente";
    }
}