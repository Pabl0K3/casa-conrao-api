package com.casaconrao.api.service;

import com.casaconrao.api.dto.LoginRequest;
import com.casaconrao.api.dto.RegistroRequest;
import com.casaconrao.api.model.Cliente;
import com.casaconrao.api.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private ClienteRepository clienteRepository;

    // 🔐 LOGIN
    public Cliente login(LoginRequest request) {

        Cliente cliente = clienteRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new RuntimeException("Correo no encontrado"));

        if (!cliente.getPass().equals(request.getPass())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return cliente;
    }

    // 📝 REGISTRO
    public Cliente registro(RegistroRequest request) {

        if (clienteRepository.existsByCorreo(request.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        if (request.getTelefono() != null &&
            clienteRepository.existsByTelefono(request.getTelefono())) {
            throw new RuntimeException("El teléfono ya está registrado");
        }

        Cliente cliente = new Cliente();
        cliente.setNombre(request.getNombre());
        cliente.setApellido(request.getApellido());
        cliente.setTelefono(request.getTelefono());
        cliente.setCorreo(request.getCorreo());
        cliente.setPass(request.getPass());

        return clienteRepository.save(cliente);
    }
}