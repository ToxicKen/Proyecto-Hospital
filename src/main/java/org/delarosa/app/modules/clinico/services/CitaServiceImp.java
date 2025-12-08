package org.delarosa.app.modules.clinico.services;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.clinico.dtos.EspecialidadDTO;
import org.delarosa.app.modules.clinico.dtos.RecetaRequest;
import org.delarosa.app.modules.clinico.entities.MedicamentosReceta;
import org.delarosa.app.modules.clinico.entities.OrdenPago;
import org.delarosa.app.modules.clinico.entities.Receta;
import org.delarosa.app.modules.clinico.enums.EstatusCita;
import org.delarosa.app.modules.clinico.exceptions.CitaNoEncontradaException;
import org.delarosa.app.modules.clinico.exceptions.FechaFueraRangoException;
import org.delarosa.app.modules.clinico.dtos.CitaResponse;
import org.delarosa.app.modules.clinico.dtos.CrearCitaRequest;
import org.delarosa.app.modules.clinico.entities.Cita;
import org.delarosa.app.modules.clinico.exceptions.CitaPendienteException;
import org.delarosa.app.modules.clinico.repositories.CitaRepository;
import org.delarosa.app.modules.clinico.repositories.RecetaRepository;
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
    private final RecetaRepository recetaRepo;

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
        System.out.println("Fecha solicitada: " + dia);
        System.out.println("Horas base del doctor (tamaño): " + rangoHorasDoctor.size());
        System.out.println("Horas base del doctor (valores): " + rangoHorasDoctor);
        System.out.println("Horas ocupadas por citas: " + horasOcupadas);

        return rangoHorasDoctor.stream()
                .filter(h -> !horasOcupadas.contains(h))
                .toList();
    }

    //Filtros de Cita de un Paciente por distintos parámetros

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponse> obtenerCitasPaciente(Integer idPaciente) {
        List<Object[]> resultados = citaRepository.filtrarCitas(
                null, null, null, null, idPaciente, null
        );
        return resultados.stream().map(this::mapearCitaFiltrada).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponse> obtenerCitasPacientePorEstatus(Integer idPaciente, EstatusCita estatus) {
        List<Object[]> resultados = citaRepository.filtrarCitas(
                null, null, estatus.name(), null, idPaciente, null
        );
        return resultados.stream().map(this::mapearCitaFiltrada).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponse> obtenerCitasPacientePorFechas(Integer idPaciente, LocalDate fechaInicio, LocalDate fechaFin) {
        List<Object[]> resultados = citaRepository.filtrarCitas(
                fechaInicio, fechaFin, null, null, idPaciente, null
        );
        return resultados.stream().map(this::mapearCitaFiltrada).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponse> obtenerCitasPacientePorDoctor(Integer idPaciente, Integer idDoctor) {
        List<Object[]> resultados = citaRepository.filtrarCitas(
                null, null, null, null, idPaciente, idDoctor
        );
        return resultados.stream().map(this::mapearCitaFiltrada).toList();
    }

    // Obtener Citas del Doctor (Pendientes por atender y atendidas

    @Override
    @Transactional(readOnly = true)
        public List<CitaResponse> obtenerCitasDoctor(Integer idDoctor) {
        List<CitaResponse> pendientes = obtenerCitasPendientes(idDoctor);
        List<CitaResponse> atendidas = obtenerCitasAtendidas(idDoctor);

        List<CitaResponse> resultado = new ArrayList<>();
        resultado.addAll(pendientes);
        resultado.addAll(atendidas);

        return resultado;
    }

    // --- Crear receta ---

    @Override
    @Transactional
    public Integer CrearReceta(RecetaRequest dto) {
        Cita cita = obtenerById(dto.folioCita());
        if (cita.getEstatus() == EstatusCita.ATENDIDA) {
            throw new IllegalStateException("Error: Esta cita ya fue atendida y tiene una receta generada.");
        }

        if (cita.getEstatus() == EstatusCita.CANCELADA_DOCTOR ||  cita.getEstatus() == EstatusCita.CANCELADA_FALTA_DE_PAGO || cita.getEstatus() ==  EstatusCita.CANCELADA_PACIENTE ) {
            throw new IllegalStateException("Error: No se puede generar receta de una cita cancelada.");
        }

        LocalDateTime ahora = LocalDateTime.now();
        System.out.println("Ahora: " + ahora);
        LocalDateTime inicioCita = cita.getFechaCita();
        System.out.println(inicioCita);
        LocalDateTime finLimite = inicioCita.plusHours(2).plusMinutes(15);
        System.out.println(finLimite);
        if (ahora.isBefore(inicioCita)) {
            throw new IllegalArgumentException(
                    "Error: Aún no es la hora de la cita. Inicia a las: " + inicioCita.toLocalTime());
        }

        if (ahora.isAfter(finLimite)) {
            throw new IllegalArgumentException(
                    "Error: El tiempo para generar la receta ha expirado. El límite era: " + finLimite.toLocalTime());
        }

        Receta receta = Receta.builder()
                .cita(cita)
                .diagnostico(dto.diagnostico())
                .observaciones(dto.observaciones())
                .build();

        List<MedicamentosReceta> listaMedicamentos = dto.medicamentos().stream()
                .map(medDto -> MedicamentosReceta.builder()
                        .nombre(medDto.nombre())
                        .tratamiento(medDto.tratamiento())
                        .cantidad(medDto.cantidad())
                        .receta(receta)
                        .build()
                ).toList();
        cita.setEstatus(EstatusCita.ATENDIDA);
        citaRepository.save(cita);
        receta.setMedicamentos(listaMedicamentos);
        recetaRepo.save(receta);
        return receta.getFolioReceta();
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
                .anyMatch(h -> h.getDiaSemana().getDay().equals(fecha.getDayOfWeek()));
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
                .filter(h -> h.getDiaSemana().getDay().equals(diaSemana))
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
                (LocalDateTime) filas[1],
                (String) filas[2],
                (String) filas[3],
                (String) filas[4],
                (String) filas[5],
                (Integer) filas[6],
                (BigDecimal) filas[7]
        );
    }

    private List<CitaResponse> obtenerCitasPendientes(Integer idDoctor) {
        List<Object[]> datos = citaRepository.filtrarCitas(
                null, null,
                EstatusCita.PAGADA_PENDIENTE_POR_ATENDER.name(),
                null, null,
                idDoctor
        );

        return datos.stream()
                .map(this::mapearCitaFiltrada)
                .toList();
    }

    private List<CitaResponse> obtenerCitasAtendidas(Integer idDoctor) {
        List<Object[]> datos = citaRepository.filtrarCitas(
                null, null,
                EstatusCita.ATENDIDA.name(),
                null, null,
                idDoctor
        );

        return datos.stream()
                .map(this::mapearCitaFiltrada)
                .toList();
    }



}
