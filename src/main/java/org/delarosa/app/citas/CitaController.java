package org.delarosa.app.citas;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.delarosa.app.paciente.Paciente;
import org.delarosa.app.paciente.PacienteService;
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
    public ResponseEntity<CitaResponseDTO> registrarCita(
            @Validated @RequestBody CitaCreateDTO citaDTO,
            Authentication authentication
    ) {
        String emailUsuario = authentication.getName();
        Paciente paciente = pacienteService.buscarPorCorreo(emailUsuario);
        CitaResponseDTO nuevaCita = citaService.crearCita(citaDTO, paciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCita);
    }
}

