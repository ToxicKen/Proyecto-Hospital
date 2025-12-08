package org.delarosa.app.modules.personal.controllers;


import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.clinico.dtos.BitacoraResumenDTO;
import org.delarosa.app.modules.clinico.dtos.CitaResponse;
import org.delarosa.app.modules.clinico.dtos.RecetaPDF;
import org.delarosa.app.modules.clinico.entities.Cita;
import org.delarosa.app.modules.clinico.repositories.BitacoraRepository;
import org.delarosa.app.modules.clinico.services.CitaService;
import org.delarosa.app.modules.paciente.dtos.HistorialMedicoCompleto;
import org.delarosa.app.modules.paciente.dtos.PacienteResponse;
import org.delarosa.app.modules.paciente.services.PacienteService;
import org.delarosa.app.modules.personal.dtos.DoctorDatosCompletosResponse;
import org.delarosa.app.modules.personal.dtos.RegistroDoctorRequest;
import org.delarosa.app.modules.personal.entities.Doctor;
import org.delarosa.app.modules.personal.services.DoctorService;
import org.delarosa.app.modules.security.dto.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final CitaService citaService;
    private final PacienteService pacienteService;
    private  final BitacoraRepository bitacoraRepo;

    @PostMapping("/registro")
    public ResponseEntity<AuthResponse> registrar(@RequestBody RegistroDoctorRequest request) {
        return ResponseEntity.ok(doctorService.registrarDoctor(request));
    }

    @GetMapping("/doctor/ping")
    public ResponseEntity<String> doctorPing() {
        return ResponseEntity.ok("Pong");
    }

    @GetMapping("/me")
    public ResponseEntity<DoctorDatosCompletosResponse> obtenerDatosPaciente(Authentication authentication) {
        String email = authentication.getName();
        DoctorDatosCompletosResponse doctor = doctorService.obtenerDatosDoctor(email);
        return ResponseEntity.ok(doctor);
    }

    @GetMapping("/citas")
    public ResponseEntity<List<CitaResponse>> obtenerCitas(Authentication authentication) {
        String email = authentication.getName();
        Integer idDoctor = doctorService.obtenerDoctorByCorreo(email).getIdDoctor();
        return ResponseEntity.ok(citaService.obtenerCitasDoctor(idDoctor));
    }


    @GetMapping("/citas/{folioCita}/historialPaciente")
    public ResponseEntity<HistorialMedicoCompleto> obtenerDatosPacientePorCita(
            @PathVariable Integer folioCita,
            Authentication authentication) {

        String email = authentication.getName();
        Integer idDoctor = doctorService.obtenerDoctorByCorreo(email).getIdDoctor();

        Cita cita = citaService.obtenerById(folioCita);

        if (!cita.getDoctor().getIdDoctor().equals(idDoctor)) {
            throw new RuntimeException("No tiene acceso a los datos de esta cita.");
        }

        PacienteResponse pacienteResponse = pacienteService
                .obtenerDatosPaciente(cita.getPaciente().getPersona().getUsuario().getCorreoElectronico());

        List<BitacoraResumenDTO> bitacora = bitacoraRepo
                .findResumenByPaciente(cita.getPaciente().getIdPaciente());
        System.out.println(bitacora);

        HistorialMedicoCompleto historialCompleto = new HistorialMedicoCompleto(
                pacienteResponse,
                bitacora
        );

        return ResponseEntity.ok(historialCompleto);
    }


    @GetMapping("/recetas/historial")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<RecetaPDF>> listarRecetasDeDoctor( Authentication authentication) {
        String email = authentication.getName();
        Doctor doctor = doctorService.obtenerDoctorByCorreo(email);
        List<RecetaPDF> historial = citaService.obtenerRecetasPorDoctor(doctor.getIdDoctor());
        System.out.println("historial" + historial);
        if (historial.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(historial);
    }

    @PutMapping("/cita/marcarAusencia/{folioCita}")
    public ResponseEntity<String> marcarAusencia(@PathVariable Integer folioCita) {
        try {
            String mensaje = citaService.marcarPacienteNoAcudio(folioCita);

            if (mensaje.startsWith("Error")) {
                return ResponseEntity.badRequest().body(mensaje);
            }

            return ResponseEntity.ok(mensaje);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
