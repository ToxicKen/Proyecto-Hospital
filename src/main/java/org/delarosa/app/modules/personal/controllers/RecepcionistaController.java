package org.delarosa.app.modules.personal.controllers;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.personal.dtos.HorarioEmpleadoDTO;
import org.delarosa.app.modules.personal.services.DoctorService;
import org.delarosa.app.modules.personal.services.EmpleadoService;
import org.delarosa.app.modules.personal.services.RecepcionistaService;
import org.delarosa.app.modules.personal.dtos.RegistroEmpleadoRequest;
import org.delarosa.app.modules.security.dto.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recepcionista")
public class RecepcionistaController {

    private final RecepcionistaService recepcionistaService;
    private final EmpleadoService empleadoService;
    private final DoctorService doctorService;

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


    @DeleteMapping("/doctor/baja/{id}")
    @PreAuthorize("hasRole('RECEPCIONISTA')")
    public ResponseEntity<?> darDeBaja(@PathVariable Integer id) {
        try {
            doctorService.darDeBajaDoctor(id);
            return ResponseEntity.ok(Map.of("mensaje", "Doctor dado de baja exitosamente. (Rol eliminado, Usuario activo)"));

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error inesperado en el servidor."));
        }
    }





}
