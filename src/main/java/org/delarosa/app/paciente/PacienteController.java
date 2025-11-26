package org.delarosa.app.paciente;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.security.auth.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    @PostMapping("/auth/registrar")
    public ResponseEntity<AuthResponse> registrar(@RequestBody PacienteDTO request) {
        return ResponseEntity.ok(pacienteService.registrarPaciente(request));
    }

    @GetMapping("/paciente/ping")
    public ResponseEntity<String> pacientePing() {
        return ResponseEntity.ok("Pong");
    }

    @GetMapping("/doctor/ping")
    public ResponseEntity<String> doctorPing() {
        return ResponseEntity.ok("Pong");
    }
}
