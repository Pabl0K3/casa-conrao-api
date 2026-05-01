package com.casaconrao.api.service;

import com.casaconrao.api.dto.ClienteUpdateRequest;
import com.casaconrao.api.dto.RecuperarPasswordRequest;
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

        validarNombreApellido(request.getNombre(), request.getApellido());
        validarTelefono(request.getTelefono());

        cliente.setNombre(request.getNombre().trim());
        cliente.setApellido(request.getApellido().trim());
        cliente.setTelefono(request.getTelefono().trim());
        cliente.setCorreo(request.getCorreo().trim());

        if (request.getPass() != null && !request.getPass().isBlank()) {
            validarPassword(request.getPass());
            cliente.setPass(request.getPass());
        }

        return clienteRepository.save(cliente);
    }

    public void recuperarPassword(RecuperarPasswordRequest request) {
        validarTelefono(request.getTelefono());
        validarPassword(request.getNuevaPass());

        Cliente cliente = clienteRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new RuntimeException("Correo no encontrado"));

        if (cliente.getTelefono() == null ||
                !cliente.getTelefono().equals(request.getTelefono().trim())) {
            throw new RuntimeException("El teléfono no coincide");
        }

        cliente.setPass(request.getNuevaPass());

        clienteRepository.save(cliente);
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