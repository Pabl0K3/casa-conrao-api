package com.casaconrao.api.service;

import com.casaconrao.api.dto.ClienteUpdateRequest;
import com.casaconrao.api.model.Cliente;
import com.casaconrao.api.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente actualizarCliente(Integer idCliente, ClienteUpdateRequest request) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        cliente.setNombre(request.getNombre());
        cliente.setApellido(request.getApellido());
        cliente.setTelefono(request.getTelefono());
        cliente.setCorreo(request.getCorreo());

        if (request.getPass() != null && !request.getPass().isBlank()) {
            cliente.setPass(request.getPass());
        }

        return clienteRepository.save(cliente);
    }
}