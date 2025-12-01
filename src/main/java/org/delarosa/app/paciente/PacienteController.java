package org.delarosa.app.paciente;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.security.dto.AuthResponse;
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

    @GetMapping("/paciente/me")
    public PacienteDatosDTO obtenerDatosPaciente(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        System.out.println(pacienteService.obtenerPacienteDesdeToken(token).getIdPaciente());
        return pacienteService.obtenerDatosPaciente(token);
    }

}
