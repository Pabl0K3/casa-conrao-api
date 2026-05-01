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

    public Cliente login(LoginRequest request) {
        Cliente cliente = clienteRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new RuntimeException("Correo no encontrado"));

        if (!cliente.getPass().equals(request.getPass())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return cliente;
    }

    public Cliente registro(RegistroRequest request) {
        validarNombreApellido(request.getNombre(), request.getApellido());
        validarTelefono(request.getTelefono());
        validarPassword(request.getPass());

        if (clienteRepository.existsByCorreo(request.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        if (request.getTelefono() != null &&
                clienteRepository.existsByTelefono(request.getTelefono())) {
            throw new RuntimeException("El teléfono ya está registrado");
        }

        Cliente cliente = new Cliente();
        cliente.setNombre(request.getNombre().trim());
        cliente.setApellido(request.getApellido().trim());
        cliente.setTelefono(request.getTelefono().trim());
        cliente.setCorreo(request.getCorreo().trim());
        cliente.setPass(request.getPass());

        return clienteRepository.save(cliente);
    }

    private void validarNombreApellido(String nombre, String apellido) {
        if (nombre == null || nombre.trim().length() < 2 ||
                !nombre.trim().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            throw new RuntimeException("El nombre solo puede contener letras");
        }

        if (apellido == null || apellido.trim().length() < 2 ||
                !apellido.trim().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            throw new RuntimeException("El apellido solo puede contener letras");
        }
    }

    private void validarTelefono(String telefono) {
        if (telefono == null || !telefono.trim().matches("^[0-9]{9}$")) {
            throw new RuntimeException("El teléfono debe tener 9 números");
        }
    }

    private void validarPassword(String pass) {
        if (pass == null || pass.length() < 6) {
            throw new RuntimeException("La contraseña debe tener al menos 6 caracteres");
        }
    }
}