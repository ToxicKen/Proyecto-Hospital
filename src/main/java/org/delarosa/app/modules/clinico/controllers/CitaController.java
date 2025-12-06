package org.delarosa.app.modules.clinico.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.delarosa.app.modules.clinico.dtos.CrearCitaRequest;
import org.delarosa.app.modules.clinico.dtos.CitaResponse;
import org.delarosa.app.modules.clinico.dtos.EspecialidadDTO;
import org.delarosa.app.modules.clinico.services.CitaService;
import org.delarosa.app.modules.paciente.entities.Paciente;
import org.delarosa.app.modules.paciente.services.PacienteService;
import org.delarosa.app.modules.personal.dtos.DoctorDatosResponse;
import org.delarosa.app.modules.personal.services.DoctorService;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/cita")
@RequiredArgsConstructor
public class CitaController {
    private final CitaService citaService;
    private final PacienteService pacienteService;
    private final DoctorService doctorService;



    //Proceso para Registrar Citas

    //Mostrar todas las especialidades
    @GetMapping("/especialidades")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<List<EspecialidadDTO>> mostrarEspecialidades() {
        return ResponseEntity.ok(doctorService.obtenerEspecialidades());
    }

    //Mostrar Doctores para esa especialidad

    @GetMapping("/especialidades/{idEspecialidad}/doctores")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<List<DoctorDatosResponse>> mostrarDoctores(@PathVariable Integer idEspecialidad) {
        return ResponseEntity.ok(doctorService.obtenerDoctoresByEspecialidadId(idEspecialidad));
    }

    // Mostrar todos los días disponibles para una cita con ese doctor
    @GetMapping("/doctores/{idDoctor}/fechas-disponibles")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<List<LocalDate>> mostrarFechasDisponibles(@PathVariable Integer idDoctor) {
        return  ResponseEntity.ok(doctorService.obtenerFechasDisponiblesByDoctorId(idDoctor));
    }

    //Mostrar todos los horarios disponibles de un doctor

    @GetMapping("/doctores/{idDoctor}/horarios/disponibles")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<List<LocalTime>> obtenerHorasDisponibles(
            @PathVariable Integer idDoctor,
            @RequestParam String fecha) {
        LocalDate dia = LocalDate.parse(fecha);
        List<LocalTime> horasDisponibles = citaService.obtenerHorasDisponiblesByDoctorIdYFecha(idDoctor, dia);
        return ResponseEntity.ok(horasDisponibles);
    }

    //Registrar una cita
    
    @PostMapping("/registrar")
    public ResponseEntity<CitaResponse> registrarCita(
            @Validated @RequestBody CrearCitaRequest citaDTO,
            Authentication authentication
    ) {
        String emailUsuario = authentication.getName();
        Paciente paciente = pacienteService.obtenerPacienteByCorreo(emailUsuario);
        CitaResponse nuevaCita = citaService.crearCita(citaDTO, paciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCita);
    }
}

