package org.delarosa.app.modules.clinico.controllers;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.clinico.dtos.CrearCitaRequest;
import org.delarosa.app.modules.clinico.dtos.CitaResponse;
import org.delarosa.app.modules.clinico.services.CitaService;
import org.delarosa.app.modules.paciente.entities.Paciente;
import org.delarosa.app.modules.paciente.services.PacienteService;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {
    private final CitaService citaService;
    private final PacienteService pacienteService;

    @PostMapping
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<CitaResponse> registrarCita(
            @Validated @RequestBody CrearCitaRequest citaDTO,
            Authentication authentication
    ) {
        String emailUsuario = authentication.getName();
        Paciente paciente = pacienteService.buscarPorCorreo(emailUsuario);
        CitaResponse nuevaCita = citaService.crearCita(citaDTO, paciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCita);
    }
}

