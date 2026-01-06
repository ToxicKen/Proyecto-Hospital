package org.delarosa.app.modules.clinico.controllers;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.clinico.dtos.*;
import org.delarosa.app.modules.clinico.entities.Cita;
import org.delarosa.app.modules.clinico.entities.Receta;
import org.delarosa.app.modules.clinico.repositories.RecetaRepository;
import org.delarosa.app.modules.clinico.services.CitaService;
import org.delarosa.app.modules.clinico.services.PagoService;
import org.delarosa.app.modules.general.services.PersonaService;
import org.delarosa.app.modules.paciente.entities.Paciente;
import org.delarosa.app.modules.paciente.services.PacienteService;
import org.delarosa.app.modules.personal.dtos.DoctorDatosResponse;
import org.delarosa.app.modules.personal.services.DoctorService;
import org.delarosa.app.modules.personal.services.PdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cita")
@RequiredArgsConstructor
public class CitaController {
    private final CitaService citaService;
    private final PacienteService pacienteService;
    private final DoctorService doctorService;
    private final PagoService pagoService;
    private final RecetaRepository recetaRepository;
    private final PdfService pdfService;
    private final PersonaService personaService;


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
        System.out.println(dia);
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


    //Pagar una Cita
    @PostMapping("/paciente/orden/pagar")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<LineaPagoResponse> pagarOrden(@RequestBody LineaPagoRequest dto) {
        Cita cita = citaService.obtenerById(dto.folioCita());
        LineaPagoResponse response = pagoService.pagarLineaPago(cita.getOrdenPago().getIdOrdenPago(), dto.montoPago());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/doctor/receta")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Map<String, Object>> crearReceta(@RequestBody RecetaRequest dto) {
        Integer idReceta = citaService.CrearReceta(dto); // Ahora el servicio debe retornar el ID
        return ResponseEntity.ok(Map.of(
                "mensaje", "Receta generada y enviada correctamente.",
                "idReceta", idReceta
        ));
    }


    @GetMapping("/doctor/receta/{id}/pdf")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<byte[]> descargarReceta(@PathVariable Integer id) throws Exception {
        Receta receta = recetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada"));

        RecetaRequest recetaRequest = new RecetaRequest(
                receta.getCita().getFolioCita(),
                receta.getDiagnostico(),
                receta.getObservaciones(),
                receta.getMedicamentos().stream()
                        .map(m -> new MedicamentosRecetaRequest(m.getNombre(), m.getTratamiento(), m.getCantidad()))
                        .toList()
        );

        RecetaPDF recetaPdf = new RecetaPDF(receta.getFolioReceta(),
                personaService.obtenerNombreCompletoPersona(receta.getCita().getPaciente().getPersona()),
                personaService.obtenerNombreCompletoPersona(receta.getCita().getDoctor().getEmpleado().getPersona()),
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                ,recetaRequest
        );

        byte[] pdfBytes = pdfService.generarRecetaPdf(recetaPdf);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=receta_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    }




