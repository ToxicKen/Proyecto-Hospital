package org.delarosa.app.modules.paciente.controllers;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.clinico.dtos.CitaResponse;
import org.delarosa.app.modules.clinico.enums.EstatusCita;
import org.delarosa.app.modules.clinico.services.CitaService;
import org.delarosa.app.modules.general.dtos.ActualizarPacienteRequest;
import org.delarosa.app.modules.general.entities.Persona;
import org.delarosa.app.modules.general.entities.Telefono;
import org.delarosa.app.modules.paciente.dtos.AlergiaExistenteDTO;
import org.delarosa.app.modules.paciente.dtos.PacienteResponse;
import org.delarosa.app.modules.paciente.dtos.PadecimientoExistenteDTO;
import org.delarosa.app.modules.paciente.dtos.RegistroPacienteRequest;
import org.delarosa.app.modules.paciente.entities.Alergia;
import org.delarosa.app.modules.paciente.entities.HistorialMedico;
import org.delarosa.app.modules.paciente.entities.Paciente;
import org.delarosa.app.modules.paciente.entities.Padecimiento;
import org.delarosa.app.modules.paciente.services.PacienteService;
import org.delarosa.app.modules.personal.services.DoctorService;
import org.delarosa.app.modules.security.dto.AuthResponse;
import org.delarosa.app.modules.security.entities.Usuario;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(("/api/paciente"))
public class PacienteController {

    private final PacienteService pacienteService;
    private final CitaService citaService;


    @PostMapping("/registrar")
    public ResponseEntity<AuthResponse> registrarPaciente(@RequestBody RegistroPacienteRequest request) {
        return ResponseEntity.ok(pacienteService.registrarPaciente(request));
    }

    @GetMapping("/paciente/ping")
    public ResponseEntity<String> pacientePing() {
        return ResponseEntity.ok("Pong");
    }

    @GetMapping("/me")
    public ResponseEntity<PacienteResponse> obtenerDatosPaciente(Authentication authentication) {
        String email = authentication.getName();
        PacienteResponse paciente = pacienteService.obtenerDatosPaciente(email);
        return ResponseEntity.ok(paciente);
    }

    @GetMapping("/registrar/padecimientos")
    public ResponseEntity<List<PadecimientoExistenteDTO>> obtenerPadecimientos() {
        return ResponseEntity.ok(pacienteService.obtenerPadecimientosExistentes());
    }

    @GetMapping("/registrar/alergias")
    public ResponseEntity<List<AlergiaExistenteDTO>> obtenerAlergias() {
        return ResponseEntity.ok(pacienteService.obtenerAlergiasExistentes());
    }

    @GetMapping("/citas")
    public ResponseEntity<List<CitaResponse>> obtenerCitas(Authentication authentication) {
        String email = authentication.getName();
        Integer idPaciente = pacienteService.obtenerPacienteByCorreo(email).getIdPaciente();
        return ResponseEntity.ok(citaService.obtenerCitasPaciente(idPaciente));
    }

    @GetMapping("/citas/estatus")
    public ResponseEntity<List<CitaResponse>> obtenerCitasPorEstatus(
            @RequestParam EstatusCita estatus,
            Authentication authentication) {

        String email = authentication.getName();
        Integer idPaciente = pacienteService.obtenerPacienteByCorreo(email).getIdPaciente();

        return ResponseEntity.ok(citaService.obtenerCitasPacientePorEstatus(idPaciente, estatus));
    }

    @GetMapping("/citas/fechas")
    public ResponseEntity<List<CitaResponse>> obtenerCitasPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            Authentication authentication) {

        String email = authentication.getName();
        Integer idPaciente = pacienteService.obtenerPacienteByCorreo(email).getIdPaciente();

        return ResponseEntity.ok(citaService.obtenerCitasPacientePorFechas(idPaciente, fechaInicio, fechaFin));
    }

    @GetMapping("/citas/doctor")
    public ResponseEntity<List<CitaResponse>> obtenerCitasPorDoctor(
            @RequestParam Integer idDoctor,
            Authentication authentication) {

        String email = authentication.getName();
        Integer idPaciente = pacienteService.obtenerPacienteByCorreo(email).getIdPaciente();

        return ResponseEntity.ok(citaService.obtenerCitasPacientePorDoctor(idPaciente, idDoctor));
    }


    @PutMapping("/cancelar/{idCita}")
    public ResponseEntity<Integer> cancelarCita(@PathVariable Integer idCita,
                                                Authentication authentication) {
        try {
            String email = authentication.getName();
            Integer idPaciente = pacienteService.obtenerPacienteByCorreo(email).getIdPaciente();
            Integer dineroDevuelto = citaService.cancelarCitaPorPaciente(idCita, idPaciente);
            return ResponseEntity.ok(dineroDevuelto);

        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(-1);
        }
    }

    // En PacienteController.java - Añadir este endpoint
    @PutMapping("/actualizar")
    public ResponseEntity<PacienteResponse> actualizarPaciente(
            @RequestBody ActualizarPacienteRequest request,
            Authentication authentication) {

        String email = authentication.getName();
        PacienteResponse pacienteActualizado = pacienteService.actualizarPaciente(email, request);
        return ResponseEntity.ok(pacienteActualizado);
    }
}
