package org.delarosa.app.paciente;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.security.auth.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    @PostMapping("/auth/registrar")
    public ResponseEntity<AuthResponse> registrar(@RequestBody PacienteDTO request) {
        return ResponseEntity.ok(pacienteService.registrarPaciente(request));
    }

}
