package com.casaconrao.api.service;

import com.casaconrao.api.dto.ClienteUpdateRequest;
import com.casaconrao.api.model.Cliente;
import com.casaconrao.api.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.casaconrao.api.dto.RecuperarPasswordRequest;

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
    
    public void recuperarPassword(RecuperarPasswordRequest request) {
        Cliente cliente = clienteRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new RuntimeException("Correo no encontrado"));

        if (cliente.getTelefono() == null ||
                !cliente.getTelefono().equals(request.getTelefono())) {
            throw new RuntimeException("El teléfono no coincide");
        }

        cliente.setPass(request.getNuevaPass());

        clienteRepository.save(cliente);
    }
}