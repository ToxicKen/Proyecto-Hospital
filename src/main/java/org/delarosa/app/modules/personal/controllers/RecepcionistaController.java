package org.delarosa.app.modules.personal.controllers;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.personal.dtos.HorarioEmpleadoDTO;
import org.delarosa.app.modules.personal.services.EmpleadoService;
import org.delarosa.app.modules.personal.services.RecepcionistaService;
import org.delarosa.app.modules.personal.dtos.RegistroEmpleadoRequest;
import org.delarosa.app.modules.security.dto.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recepcionista")
public class RecepcionistaController {

    private final RecepcionistaService recepcionistaService;
    private final EmpleadoService empleadoService;

    @PostMapping("/registro")
    public ResponseEntity<AuthResponse> registrarRecepcionista(@RequestBody RegistroEmpleadoRequest registroEmpleadoRequest) {
        return ResponseEntity.ok(recepcionistaService.registrarRecepcionista(registroEmpleadoRequest));
    }



    @PostMapping("/{idEmpleado}/horarios")
    @PreAuthorize("hasRole('RECEPCIONISTA')")
    public ResponseEntity<?> agregarHorario(
            @PathVariable Integer idEmpleado,
            @RequestBody HorarioEmpleadoDTO dto) {

        empleadoService.agregarHorario(idEmpleado, dto);
        return ResponseEntity.ok("Horario registrado correctamente");
    }


}
