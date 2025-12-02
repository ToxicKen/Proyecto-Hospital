package org.delarosa.app.modules.security.controllers;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.security.dto.AuthResponse;
import org.delarosa.app.modules.security.dto.LoginRequest;
import org.delarosa.app.modules.security.services.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(usuarioService.loginUsuario(request));
    }
}
