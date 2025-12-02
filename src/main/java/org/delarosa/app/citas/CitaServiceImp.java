package org.delarosa.app.citas;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.DoctorNoTrabajaEseDia;
import org.delarosa.app.HorarioNoDisponibleException;
import org.delarosa.app.modules.personal.entities.HorarioEmpleado;
import org.delarosa.app.modules.personal.entities.Doctor;
import org.delarosa.app.modules.personal.services.DoctorService;
import org.delarosa.app.paciente.Paciente;
import org.delarosa.app.paciente.PacienteService;
import org.delarosa.app.modules.general.services.PersonaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CitaServiceImp implements CitaService {

    private final CitaRepository citaRepository;
    private final DoctorService doctorService;
    private final PacienteService pacienteService;
    private final PagoService pagoService;
    private final PersonaService personaService;


    @Override
    @Transactional
    public CitaResponseDTO crearCita(CitaCreateDTO citaCreateDTO,Paciente paciente) {
        if (!esFechaEnRangoPermitido(citaCreateDTO.fechaCita())) {
            throw new FechaFueraRangoException("La fecha debe ser con al menos 48 horas de anticipo y máximo 3 meses.");
        }

        Doctor doctor = doctorService.obtenerDoctorById(citaCreateDTO.idDoctor());

        if (existeCitaPendienteConElDoctor(paciente, doctor)) {
            throw new CitaPendienteException("El paciente ya tiene una cita pendiente con este doctor.");
        }
        validarDisponibilidadDoctor(doctor, citaCreateDTO.fechaCita());

        Cita nuevaCita = crearEntidadCita(citaCreateDTO, doctor, paciente);
        nuevaCita.asignarOrdenPago( pagoService.crearOrdenPago(nuevaCita,obtenerMontoDeCita(nuevaCita)));
        Cita citaGuardada = citaRepository.save(nuevaCita);

        return mapToResponseDTO(citaGuardada);
    }

    // --- MÉTODOS DE MAPEO (DTO <-> Entidad) ---

    private Cita crearEntidadCita(CitaCreateDTO dto, Doctor doctor, Paciente paciente) {
        Cita cita = new Cita();
        cita.setFechaSolicitud(LocalDateTime.now());
        cita.setDoctor(doctor);
        cita.setPaciente(paciente);
        cita.setFechaCita(dto.fechaCita());
        cita.setEstatus(EstatusCita.AGENDADA_PENDIENTE_DE_PAGO);
        return cita;
    }

    private CitaResponseDTO mapToResponseDTO(Cita cita) {
        return new CitaResponseDTO(
            cita.getFolioCita(),
                personaService.obtenerNombreCompletoPersona(cita.getPaciente().getPersona()),
                personaService.obtenerNombreCompletoPersona(cita.getDoctor().getEmpleado().getPersona()),
                cita.getDoctor().getEspecialidad().getNombre(),
                cita.getDoctor().getConsultorio().getNumero(),
                cita.getFechaCita(),
                cita.getEstatus().name()
                );
    }

    // --- VALIDACIONES DE NEGOCIO ---

    private void validarDisponibilidadDoctor(Doctor doctor, LocalDateTime date) {
        if (!doctorTrabajaEseDia(doctor, date)) {
            throw new DoctorNoTrabajaEseDia("El doctor no trabaja en el día seleccionado.");
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

    // --- LÓGICA DE HORARIOS ---

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
                .orElseThrow(() -> new DoctorNoTrabajaEseDia("Día fuera del horario del doctor"));

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

    @Override
    public Cita obtenerById(Integer id) {
        return citaRepository.findById(id).orElseThrow(()-> new CitaNoEncontradaException("Cita no encontrada"));
    }

    @Override
    public BigDecimal obtenerMontoDeCita(Cita cita) {
        return cita.getDoctor().getEspecialidad().getCosto();
    }
}
