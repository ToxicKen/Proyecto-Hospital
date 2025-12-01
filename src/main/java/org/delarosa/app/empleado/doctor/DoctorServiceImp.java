package org.delarosa.app.empleado.doctor;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.empleado.Empleado;
import org.delarosa.app.empleado.EmpleadoService;
import org.delarosa.app.modules.security.dto.AuthResponse;
import org.delarosa.app.modules.security.jwt.JwtService;
import org.delarosa.app.modules.security.entities.Usuario;
import org.delarosa.app.usuario.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DoctorServiceImp implements DoctorService {
    private final DoctorRepository doctorRepo;
    private final EmpleadoService empleadoService;
    private final EspecialidadRepository especialidadRepo;
    private final ConsultorioRepository consultorioRepo;
    private final UsuarioService usuarioService ;
    private final JwtService jwtService;



    @Override
    public Doctor crearDoctor(DoctorDTO doctorDTO) {

        Empleado empleado = empleadoService.crearEmpleado(doctorDTO.empleadoDTO());
        asignarRolDoctor(empleado.getPersona().getUsuario());

        Doctor doctor = Doctor.builder()
                .empleado(empleado)
                .cedulaProfesional(doctorDTO.cedulaProfesional())
                .especialidad(especialidadRepo.findById(doctorDTO.idEspecialidad()).orElseThrow(()-> new EspecialidadNoExistenteExecption("Especialidad no existente")))
                .consultorio(consultorioRepo.findById(doctorDTO.idConsultorio()).orElseThrow(()-> new ConsultorioNoExistenteExecption("Consultorio no existente")))
                .build();
        return doctorRepo.save(doctor);
    }


    private void asignarRolDoctor(Usuario usuarioCreado) {
        usuarioService.addRolDoctor(usuarioCreado);
    }

    private AuthResponse crearAuthResponse(Doctor doctor) {
        Usuario usuario = doctor.getEmpleado().getPersona().getUsuario();
        String token = jwtService.getToken(usuario);
        return AuthResponse.builder().token(jwtService.getToken(usuario)).build();
    }

    @Override
    @Transactional
    public AuthResponse registrarDoctor(DoctorDTO doctorDTO) {
        Doctor doctor =  crearDoctor(doctorDTO);
        return crearAuthResponse(doctor);
    }

    @Override
    public Doctor obtenerDoctorById(Integer id) {
        return doctorRepo.findById(id).orElseThrow(() -> new DoctorNoEncontradoException("Doctor no encontrado"));
    }
    

}

