package org.delarosa.app.modules.personal.services;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.clinico.dtos.EspecialidadDTO;
import org.delarosa.app.modules.general.services.PersonaService;
import org.delarosa.app.modules.personal.dtos.DoctorDatosResponse;
import org.delarosa.app.modules.personal.entities.Especialidad;
import org.delarosa.app.modules.personal.exceptions.EspecialidadNoEncontradaException;
import org.delarosa.app.modules.personal.repositories.EspecialidadRepository;
import org.delarosa.app.modules.personal.dtos.RegistroDoctorRequest;
import org.delarosa.app.modules.personal.entities.Doctor;
import org.delarosa.app.modules.personal.entities.Empleado;
import org.delarosa.app.modules.personal.exceptions.ConsultorioNoEncontradoException;
import org.delarosa.app.modules.personal.exceptions.DoctorNoEncontradoException;
import org.delarosa.app.modules.personal.repositories.ConsultorioRepository;
import org.delarosa.app.modules.personal.repositories.DoctorRepository;
import org.delarosa.app.modules.security.dto.AuthResponse;
import org.delarosa.app.modules.security.jwt.JwtService;
import org.delarosa.app.modules.security.entities.Usuario;
import org.delarosa.app.modules.security.services.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


}

