package com.casaconrao.api.controller;

import com.casaconrao.api.dto.ClienteResponse;
import com.casaconrao.api.dto.LoginRequest;
import com.casaconrao.api.dto.RegistroRequest;
import com.casaconrao.api.model.Cliente;
import com.casaconrao.api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ClienteResponse login(@RequestBody LoginRequest request) {
        Cliente cliente = authService.login(request);
        return new ClienteResponse(cliente);
    }

    @PostMapping("/registro")
    public ClienteResponse registro(@RequestBody RegistroRequest request) {
        Cliente cliente = authService.registro(request);
        return new ClienteResponse(cliente);
    }
}