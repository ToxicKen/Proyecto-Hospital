package org.delarosa.app.modules.personal.services;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.clinico.dtos.EspecialidadDTO;
import org.delarosa.app.modules.clinico.entities.Cita;
import org.delarosa.app.modules.general.enums.Dia;
import org.delarosa.app.modules.general.services.PersonaService;
import org.delarosa.app.modules.personal.dtos.DoctorDatosCompletosResponse;
import org.delarosa.app.modules.personal.dtos.DoctorDatosResponse;
import org.delarosa.app.modules.personal.dtos.HorarioEmpleadoDTO;
import org.delarosa.app.modules.personal.entities.Especialidad;
import org.delarosa.app.modules.personal.entities.HorarioEmpleado;
import org.delarosa.app.modules.personal.exceptions.EspecialidadNoEncontradaException;
import org.delarosa.app.modules.personal.repositories.EspecialidadRepository;
import org.delarosa.app.modules.personal.dtos.RegistroDoctorRequest;
import org.delarosa.app.modules.personal.entities.Doctor;
import org.delarosa.app.modules.personal.entities.Empleado;
import org.delarosa.app.modules.personal.exceptions.ConsultorioNoEncontradoException;
import org.delarosa.app.modules.personal.exceptions.DoctorNoEncontradoException;
import org.delarosa.app.modules.personal.repositories.ConsultorioRepository;
import org.delarosa.app.modules.personal.repositories.DoctorRepository;
import org.delarosa.app.modules.personal.repositories.HorarioEmpleadoRepository;
import org.delarosa.app.modules.security.dto.AuthResponse;
import org.delarosa.app.modules.security.jwt.JwtService;
import org.delarosa.app.modules.security.entities.Usuario;
import org.delarosa.app.modules.security.services.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.Doc;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorServiceImp implements DoctorService {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    private final EmpleadoService empleadoService;
    private final DoctorRepository doctorRepo;
    private final EspecialidadRepository especialidadRepo;
    private final ConsultorioRepository consultorioRepo;
    private final PersonaService personaService;
    private final HorarioEmpleadoRepository horarioEmpleadoRepo;


    // --- Registro de Doctor ---

    @Override
    @Transactional
    public AuthResponse registrarDoctor(RegistroDoctorRequest registroDoctorRequest) {
        Doctor doctor = crearDoctor(registroDoctorRequest);
        return crearAuthResponse(doctor);
    }

    // --- Registro y Creación ---

    @Override
    public Doctor crearDoctor(RegistroDoctorRequest registroDoctorRequest) {

        Empleado empleado = empleadoService.crearEmpleado(registroDoctorRequest.registroEmpleadoRequest());
        asignarRolDoctor(empleado.getPersona().getUsuario());

        Doctor doctor = Doctor.builder()
                .empleado(empleado)
                .cedulaProfesional(registroDoctorRequest.cedulaProfesional())
                .especialidad(especialidadRepo.findById(registroDoctorRequest.idEspecialidad()).orElseThrow(() -> new EspecialidadNoEncontradaException("Especialidad no existente")))
                .consultorio(consultorioRepo.findById(registroDoctorRequest.idConsultorio()).orElseThrow(() -> new ConsultorioNoEncontradoException("Consultorio no existente")))
                .build();
        return doctorRepo.save(doctor);
    }

    // --- Obtener Doctor ---

    @Override
    public Doctor obtenerDoctorById(Integer id) {
        return doctorRepo.findById(id).orElseThrow(() -> new DoctorNoEncontradoException("Doctor no encontrado"));
    }

    // --- Obtener todas las especialidades ---

    @Override
    public List<EspecialidadDTO> obtenerEspecialidades() {
        return especialidadRepo.findAll().stream().map(this::mapearEspecialidad).toList();
    }

    // --- Obtener todos los doctores de una especialidad ---

    @Override
    public List<DoctorDatosResponse> obtenerDoctoresByEspecialidadId(Integer idEspecialidad) {
        return doctorRepo.findAllByEspecialidadIdEspecialidad(idEspecialidad).stream().map(this::mapearDoctor).toList();
    }

    // --- Obtener todos los días disponibles de un doctor
    // (incluyendo la restricción de mínimo 48 horas previas y maximo 3 messes después---

    @Override
    public List<LocalDate> obtenerFechasDisponiblesByDoctorId(Integer idDoctor) {
        List<DayOfWeek> diasLaborales = empleadoService.obtenerDiasLaboralesByIdEmpleado(idDoctor).stream().map(Dia::getDay).toList();
        LocalDate fechaInicial = LocalDateTime.now().plusHours(48).toLocalDate();
        LocalDate fechaMaxima = LocalDateTime.now().plusMonths(3).toLocalDate();
        LocalDate fechaActual = fechaInicial;

        List<LocalDate> diasDisponibles = new ArrayList<>();
        while (!fechaActual.isAfter(fechaMaxima)) {
            if (diasLaborales.contains(fechaActual.getDayOfWeek())) {
                diasDisponibles.add(fechaActual);
            }
            fechaActual = fechaActual.plusDays(1);
        }

        return diasDisponibles;
    }

    //Obtener Horas Laborales del doctor
    @Override
    public List<LocalTime> obtenerHorasByDoctorYFecha(Integer idDoctor,LocalDate dia) {
        HorarioEmpleado horarioDia = horarioEmpleadoRepo.findByEmpleadoIdEmpleadoAndDiaSemana(idDoctor,Dia.fromDayOfWeek(dia.getDayOfWeek())).orElseThrow(()->new RuntimeException("Horario no Encontrado"));
        return generarHorasDisponibles(horarioDia.getHrsInicio(),horarioDia.getHrsFin());
    }

    //Obtener Los datos de un Doctor

    @Override
    public DoctorDatosCompletosResponse obtenerDatosDoctor(String email) {
        Doctor doctor = obtenerDoctorByCorreo(email);
        return  mapearDoctorCompleto(doctor);
    }

    //Obtener doctor por Correo

    @Override
    public Doctor obtenerDoctorByCorreo(String correo) {
        return doctorRepo.buscarPorEmailDeUsuario(correo).orElseThrow(()->new DoctorNoEncontradoException("Doctor no encontrado"));
    }

    //Dar de baja un doctor
    @Override
    public void darDeBajaDoctor(Integer idDoctor) {
        try {
            // Llamamos al Stored Procedure
            doctorRepo.darDeBajaDoctor(idDoctor);

        } catch (Exception e) {
            String mensajeError = e.getMessage();

            if (e.getCause() != null) {
                mensajeError += " " + e.getCause().getMessage();
            }

            if (mensajeError.contains("El doctor tiene citas pendientes")) {
                throw new IllegalStateException("No se puede dar de baja: El doctor tiene citas pendientes o pagos sin procesar.");
            }

            throw new RuntimeException("Error interno al procesar la baja del doctor.");
        }
    }

    @Override
    public Doctor obtenerDoctorByNombre(String nombre) {
        return doctorRepo.findByEmpleadoPersonaNombre(nombre).orElseThrow(()-> new RuntimeException("Doctor no encontrado"));
    }

    // --- Metodos de apoyo ---

    private void asignarRolDoctor(Usuario usuarioCreado) {
        usuarioService.addRolDoctor(usuarioCreado);
    }

    private AuthResponse crearAuthResponse(Doctor doctor) {
        Usuario usuario = doctor.getEmpleado().getPersona().getUsuario();
        String token = jwtService.getToken(usuario);
        return AuthResponse.builder().token(token).build();
    }

    private EspecialidadDTO mapearEspecialidad(Especialidad especialidad){
        return new EspecialidadDTO(especialidad.getIdEspecialidad(),especialidad.getNombre());
    }

    private DoctorDatosResponse mapearDoctor(Doctor doctor) {
        return new  DoctorDatosResponse(doctor.getIdDoctor(),
                personaService.obtenerNombreCompletoPersona(doctor.getEmpleado().getPersona()),
                doctor.getEspecialidad().getNombre(),doctor.getConsultorio().getIdConsultorio());
    }

    private List<LocalTime> generarHorasDisponibles(LocalTime inicio, LocalTime fin) {
        List<LocalTime> horasDisponibles = new ArrayList<>();
        while (!inicio.isAfter(fin)) {
            horasDisponibles.add(inicio);
            inicio = inicio.plusHours(1);        }
        return horasDisponibles;
    }


    private DoctorDatosCompletosResponse mapearDoctorCompleto(Doctor doctor) {
        List<HorarioEmpleadoDTO> horarios = mapearHorarios(doctor.getEmpleado().getHorarios());
        return new DoctorDatosCompletosResponse(personaService.obtenerResponsePersona(doctor.getEmpleado().getPersona()),
                horarios,
                doctor.getEmpleado().getPersona().getUsuario().getCorreoElectronico(),
                doctor.getEspecialidad().getNombre(),
                doctor.getConsultorio().getNumero(),
                doctor.getCedulaProfesional()
        );
    }

    private List<HorarioEmpleadoDTO> mapearHorarios(List<HorarioEmpleado> horarios) {
        return horarios.stream().map(h-> new HorarioEmpleadoDTO(h.getDiaSemana().name(),h.getHrsInicio(),h.getHrsFin())).toList();
    }


}

