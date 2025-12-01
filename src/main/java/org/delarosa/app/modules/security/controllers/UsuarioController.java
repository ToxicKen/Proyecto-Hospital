package org.delarosa.app.usuario;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.security.dto.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDTO request) {
        return ResponseEntity.ok(usuarioService.loginUsuario(request));
    }
}
