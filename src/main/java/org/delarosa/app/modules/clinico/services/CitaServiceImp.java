package org.delarosa.app.modules.clinico.services;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.clinico.dtos.EspecialidadDTO;
import org.delarosa.app.modules.clinico.enums.EstatusCita;
import org.delarosa.app.modules.clinico.exceptions.CitaNoEncontradaException;
import org.delarosa.app.modules.clinico.exceptions.FechaFueraRangoException;
import org.delarosa.app.modules.clinico.dtos.CitaResponse;
import org.delarosa.app.modules.clinico.dtos.CrearCitaRequest;
import org.delarosa.app.modules.clinico.entities.Cita;
import org.delarosa.app.modules.clinico.exceptions.CitaPendienteException;
import org.delarosa.app.modules.clinico.repositories.CitaRepository;
import org.delarosa.app.modules.paciente.entities.Paciente;
import org.delarosa.app.modules.paciente.services.PacienteService;
import org.delarosa.app.modules.personal.exceptions.DoctorNoTrabajaEseDiaException;
import org.delarosa.app.modules.personal.exceptions.HorarioNoDisponibleException;
import org.delarosa.app.modules.personal.entities.HorarioEmpleado;
import org.delarosa.app.modules.personal.entities.Doctor;
import org.delarosa.app.modules.personal.services.DoctorService;
import org.delarosa.app.modules.general.services.PersonaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CitaServiceImp implements CitaService {

    private final DoctorService doctorService;
    private final PacienteService pacienteService;
    private final PagoService pagoService;
    private final PersonaService personaService;
    private final CitaRepository citaRepository;

    // --- Crear Cita ---

    @Override
    @Transactional
    public CitaResponse crearCita(CrearCitaRequest crearCitaRequest, Paciente paciente) {
        if (!esFechaEnRangoPermitido(crearCitaRequest.fechaCita())) {
            throw new FechaFueraRangoException("La fecha debe ser con al menos 48 horas de anticipo y máximo 3 meses.");
        }

        Doctor doctor = doctorService.obtenerDoctorById(crearCitaRequest.idDoctor());

        if (existeCitaPendienteConElDoctor(paciente, doctor)) {
            throw new CitaPendienteException("El paciente ya tiene una cita pendiente con este doctor.");
        }
        validarDisponibilidadDoctor(doctor, crearCitaRequest.fechaCita());

        Cita nuevaCita = crearEntidadCita(crearCitaRequest, doctor, paciente);
        nuevaCita.asignarOrdenPago( pagoService.crearOrdenPago(nuevaCita,obtenerMontoDeCita(nuevaCita)));
        Cita citaGuardada = citaRepository.save(nuevaCita);

        return mapToResponseDTO(citaGuardada);
    }

    // --- Obtener cita por Id ---

    @Override
    public Cita obtenerById(Integer id) {
        return citaRepository.findById(id).orElseThrow(()-> new CitaNoEncontradaException("Cita no encontrada"));
    }

    // --- Obtener horas disponibles para un doctor ---

    @Override
    public List<LocalTime> obtenerHorasDisponiblesByDoctorIdYFecha(Integer idDoctor, LocalDate dia) {
        LocalDateTime inicio = dia.atStartOfDay();
        LocalDateTime fin = dia.atTime(23, 59);
        List<Cita> citas = citaRepository.findAllByDoctorIdDoctorAndFechaCitaBetween(idDoctor, inicio, fin);
        List<LocalTime> rangoHorasDoctor = doctorService.obtenerHorasByDoctorYFecha(idDoctor, dia);

        List<LocalTime> horasOcupadas = citas.stream()
                .map(c -> c.getFechaCita().toLocalTime())
                .toList();
        
        return rangoHorasDoctor.stream()
                .filter(h -> !horasOcupadas.contains(h))
                .toList();
    }

    //Filtros de Cita de un Paciente por distintos parámetros

    @Override
    public List<CitaResponse> obtenerCitasPaciente(Integer idPaciente) {
        List<Object[]> resultados = citaRepository.filtrarCitas(
                null, null, null, null, idPaciente, null
        );
        return resultados.stream().map(this::mapearCitaFiltrada).toList();
    }

    @Override
    public List<CitaResponse> obtenerCitasPacientePorEstatus(Integer idPaciente, EstatusCita estatus) {
        List<Object[]> resultados = citaRepository.filtrarCitas(
                null, null, estatus.name(), null, idPaciente, null
        );
        return resultados.stream().map(this::mapearCitaFiltrada).toList();
    }

    @Override
    public List<CitaResponse> obtenerCitasPacientePorFechas(Integer idPaciente, LocalDate fechaInicio, LocalDate fechaFin) {
        List<Object[]> resultados = citaRepository.filtrarCitas(
                fechaInicio, fechaFin, null, null, idPaciente, null
        );
        return resultados.stream().map(this::mapearCitaFiltrada).toList();
    }

    public List<CitaResponse> obtenerCitasPacientePorDoctor(Integer idPaciente, Integer idDoctor) {
        List<Object[]> resultados = citaRepository.filtrarCitas(
                null, null, null, null, idPaciente, idDoctor
        );
        return resultados.stream().map(this::mapearCitaFiltrada).toList();
    }


    // --- Metodos de apoyo ---

    private Cita crearEntidadCita(CrearCitaRequest dto, Doctor doctor, Paciente paciente) {
        Cita cita = new Cita();
        cita.setFechaSolicitud(LocalDateTime.now());
        cita.setDoctor(doctor);
        cita.setPaciente(paciente);
        cita.setFechaCita(dto.fechaCita());
        cita.setEstatus(EstatusCita.AGENDADA_PENDIENTE_DE_PAGO);
        return cita;
    }

    private CitaResponse mapToResponseDTO(Cita cita) {
        return new CitaResponse(
                cita.getFolioCita(),
                cita.getFechaCita(),
                cita.getEstatus().name(),
                personaService.obtenerNombreCompletoPersona(cita.getPaciente().getPersona()),
                personaService.obtenerNombreCompletoPersona(cita.getDoctor().getEmpleado().getPersona()),
                cita.getDoctor().getEspecialidad().getNombre(),
                cita.getDoctor().getConsultorio().getNumero(),
                cita.getDoctor().getEspecialidad().getCosto()
        );
    }

    private void validarDisponibilidadDoctor(Doctor doctor, LocalDateTime date) {
        if (!doctorTrabajaEseDia(doctor, date)) {
            throw new DoctorNoTrabajaEseDiaException("El doctor no trabaja en el día seleccionado.");
        }
        if (!horaDisponibleDoctor(doctor, date)) {
            throw new HorarioNoDisponibleException("El horario seleccionado no está disponible.");
        }
    }

    private boolean esFechaEnRangoPermitido(LocalDateTime fecha) {
        LocalDateTime ahora = LocalDateTime.now();
        return fecha.isAfter(ahora.plusHours(48)) && fecha.isBefore(ahora.plusMonths(3));
    }

    private boolean existeCitaPendienteConElDoctor(Paciente paciente, Doctor doctor) {
        return citaRepository.obtenerCitasPendientesDeDoctorPaciente(
                paciente.getIdPaciente(),
                doctor.getIdDoctor()
        ).isPresent();
    }


    private boolean doctorTrabajaEseDia(Doctor doctor, LocalDateTime fecha) {
        return doctor.getEmpleado().getHorarios().stream()
                .anyMatch(h -> h.getDia().getDay().equals(fecha.getDayOfWeek()));
    }

    private boolean horaDisponibleDoctor(Doctor doctor, LocalDateTime fecha) {
        return horasDisponiblesDoctorPorDia(doctor, fecha.toLocalDate())
                .stream()
                .anyMatch(h -> h.equals(fecha.toLocalTime()));
    }

    private List<LocalTime> horasDisponiblesDoctorPorDia(Doctor doctor, LocalDate fecha) {
        List<LocalTime> horasLaborables = obtenerHorasLaborablesPorDia(doctor, fecha.getDayOfWeek());

        return horasLaborables.stream()
                .filter(hora -> {
                    LocalDateTime fechaHoraCita = LocalDateTime.of(fecha, hora);
                    return !existeCitaProgramadaEnEseHorarioParaDoctor(doctor, fechaHoraCita);
                })
                .toList();
    }

    private List<LocalTime> obtenerHorasLaborablesPorDia(Doctor doctor, DayOfWeek diaSemana) {
        HorarioEmpleado horario = doctor.getEmpleado()
                .getHorarios()
                .stream()
                .filter(h -> h.getDia().getDay().equals(diaSemana))
                .findFirst()
                .orElseThrow(() -> new DoctorNoTrabajaEseDiaException("Día fuera del horario del doctor"));

        List<LocalTime> slots = new ArrayList<>();
        LocalTime actual = horario.getHrsInicio();
        while (actual.isBefore(horario.getHrsFin())) {
            slots.add(actual);
            actual = actual.plusHours(1);
        }
        return slots;
    }

    private boolean existeCitaProgramadaEnEseHorarioParaDoctor(Doctor doctor, LocalDateTime fecha) {
        return citaRepository.findByDoctorAndFechaSolicitud(doctor, fecha).isPresent();
    }

    private BigDecimal obtenerMontoDeCita(Cita cita) {
        return cita.getDoctor().getEspecialidad().getCosto();
    }

    //Método para mapear Citas filtradas a Cita Response

    private CitaResponse mapearCitaFiltrada(Object[] filas) {
        return new CitaResponse(
                (Integer) filas[0],
                ((Timestamp) filas[1]).toLocalDateTime(),
                (String) filas[2],
                (String) filas[3],
                (String) filas[4],
                (String) filas[5],
                (Integer) filas[6],
                (BigDecimal) filas[7]
        );
    }

}
