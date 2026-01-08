package org.delarosa.app.modules.personal.controllers;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.personal.dtos.*;
import org.delarosa.app.modules.personal.services.DoctorService;
import org.delarosa.app.modules.personal.services.EmpleadoService;
import org.delarosa.app.modules.personal.services.RecepcionistaService;
import org.delarosa.app.modules.security.dto.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recepcionista")
@PreAuthorize("hasRole('RECEPCIONISTA')")
public class RecepcionistaController {

    private final RecepcionistaService recepcionistaService;
    private final EmpleadoService empleadoService;
    private final DoctorService doctorService;

    // ================= REGISTRO =================

    @PostMapping("/registro")
    @PreAuthorize("permitAll()")
    public ResponseEntity<AuthResponse> registrarRecepcionista(
            @RequestBody RegistroEmpleadoRequest request) {

        AuthResponse response = recepcionistaService.registrarRecepcionista(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ================= HORARIOS =================

    @PostMapping("/empleados/{idEmpleado}/horarios")
    public ResponseEntity<Map<String, String>> agregarHorario(
            @PathVariable Integer idEmpleado,
            @RequestBody HorarioEmpleadoDTO dto) {

        empleadoService.agregarHorario(idEmpleado, dto);
        return ResponseEntity.ok(
                Map.of("mensaje", "Horario registrado correctamente")
        );
    }

    // ================= DOCTORES =================

    @DeleteMapping("/doctores/{id}/baja")
    public ResponseEntity<Map<String, String>> darDeBajaDoctor(@PathVariable Integer id) {
        doctorService.darDeBajaDoctor(id);
        return ResponseEntity.ok(
                Map.of("mensaje",
                        "Doctor dado de baja exitosamente. (Rol eliminado, usuario activo)")
        );
    }

    // ================= CONSULTORIOS =================

    @PostMapping("/consultorio/crear")
    public ResponseEntity<ConsultorioResponse> crearConsultorio(
            @RequestBody ConsultorioRequest dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(recepcionistaService.crearConsultorio(dto));
    }

    @GetMapping("/consultorios")
    public ResponseEntity<List<ConsultorioResponse>> listarConsultorios() {
        return ResponseEntity.ok(recepcionistaService.listarConsultorios());
    }

    @GetMapping("/consultorios/{id}")
    public ResponseEntity<ConsultorioResponse> obtenerConsultorio(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                recepcionistaService.obtenerConsultorio(id)
        );
    }

    @PutMapping("/consultorios/{id}")
    public ResponseEntity<ConsultorioResponse> editarConsultorio(
            @PathVariable Integer id,
            @RequestBody ConsultorioRequest dto) {

        return ResponseEntity.ok(
                recepcionistaService.editarConsultorio(id, dto)
        );
    }

    @DeleteMapping("/consultorios/{id}")

    public ResponseEntity<ConsultorioResponse> eliminarConsultorio(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                recepcionistaService.eliminarConsultorio(id)
        );
    }

    // ================= ESPECIALIDADES =================

    @PostMapping("/especialidades")
    public ResponseEntity<EspecialidadResponse> crearEspecialidad(
            @RequestBody EspecialidadRequest dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(recepcionistaService.crearEspecialidad(dto));
    }

    @GetMapping("/especialidades")
    public ResponseEntity<List<EspecialidadResponse>> listarEspecialidades() {
        return ResponseEntity.ok(
                recepcionistaService.listarEspecialidades()
        );
    }

    @GetMapping("/especialidades/{id}")
    public ResponseEntity<EspecialidadResponse> obtenerEspecialidad(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                recepcionistaService.obtenerEspecialidad(id)
        );
    }

    @PutMapping("/especialidades/{id}")
    public ResponseEntity<EspecialidadResponse> editarEspecialidad(
            @PathVariable Integer id,
            @RequestBody EspecialidadRequest dto) {

        return ResponseEntity.ok(
                recepcionistaService.editarEspecialidad(id, dto)
        );
    }

    @DeleteMapping("/especialidades/{id}")
    public ResponseEntity<EspecialidadResponse> eliminarEspecialidad(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                recepcionistaService.eliminarEspecialidad(id)
        );
    }


    @GetMapping("/doctores")
    public ResponseEntity<List<DoctorDatosResponse>> consultarDoctores() {
        return ResponseEntity.ok(
                doctorService.consultarDoctores()
        );
    }



}
