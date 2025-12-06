package org.delarosa.app.modules.paciente.controllers;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.paciente.dtos.PacienteResponse;
import org.delarosa.app.modules.paciente.dtos.RegistroPacienteRequest;
import org.delarosa.app.modules.paciente.services.PacienteService;
import org.delarosa.app.modules.security.dto.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(("/api/paciente"))
public class PacienteController {

    private final PacienteService pacienteService;

    @PostMapping("/registrar")
    public ResponseEntity<AuthResponse> registrarPaciente(@RequestBody RegistroPacienteRequest request) {
        return ResponseEntity.ok(pacienteService.registrarPaciente(request));
    }

    @GetMapping("/paciente/ping")
    public ResponseEntity<String> pacientePing() {
        return ResponseEntity.ok("Pong");
    }

    @GetMapping("/paciente/me")
    public ResponseEntity<PacienteResponse> obtenerDatosPaciente(Authentication authentication) {
        String email = authentication.getName();
        PacienteResponse paciente = pacienteService.obtenerDatosPacienteByCorreo(email);
        return ResponseEntity.ok(paciente);
    }

}
