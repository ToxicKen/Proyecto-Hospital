package org.delarosa.app.modules.personal.services;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.personal.dtos.*;
import org.delarosa.app.modules.personal.entities.Consultorio;
import org.delarosa.app.modules.personal.entities.Empleado;
import org.delarosa.app.modules.personal.entities.Recepcionista;
import org.delarosa.app.modules.personal.repositories.ConsultorioRepository;
import org.delarosa.app.modules.personal.repositories.EspecialidadRepository;
import org.delarosa.app.modules.personal.repositories.RecepcionistaRepository;
import org.delarosa.app.modules.security.dto.AuthResponse;
import org.delarosa.app.modules.security.jwt.JwtService;
import org.delarosa.app.modules.security.entities.Usuario;
import org.delarosa.app.modules.security.services.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecepcionistaServiceImp implements RecepcionistaService {

    private final EmpleadoService empleadoService;
    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    private final RecepcionistaRepository recepcionistaRepo;
    private final ConsultorioRepository consultorioRepo;
    private final EspecialidadRepository especialidadRepo;


    // --- Registro de Recepcionista ---


    @Transactional
    @Override
    public AuthResponse registrarRecepcionista(RegistroEmpleadoRequest registroEmpleadoRequest) {
        Recepcionista recepcionista = crearRecepcionista(registroEmpleadoRequest);
        return crearAuthResponse(recepcionista);
    }

    // --- Creación de Recepcionista ---

    @Override
    public Recepcionista crearRecepcionista(RegistroEmpleadoRequest registroEmpleadoRequest) {
        Empleado empleado = empleadoService.crearEmpleado(registroEmpleadoRequest);
        asignarRolRecepcionista(empleado.getPersona().getUsuario());
        Recepcionista recepcionista = new Recepcionista();
        recepcionista.setEmpleado(empleado);
        return recepcionistaRepo.save(recepcionista);
    }

    @Override
    public ConsultorioResponse crearConsultorio(ConsultorioRequest dto) {
        Consultorio consultorio = consultorioRepo.findByNombre()
    }

    @Override
    public ConsultorioResponse obtenerConsultorio(Integer idConsultorio) {
        return null;
    }

    @Override
    public ConsultorioResponse editarConsultorio(Integer idConsultorio, ConsultorioRequest dto) {
        return null;
    }

    @Override
    public ConsultorioResponse eliminarConsultorio(Integer idConsultorio) {
        return null;
    }

    @Override
    public List<ConsultorioResponse> listarConsultorios() {
        return List.of();
    }

    @Override
    public List<EspecialidadResponse> listarEspecialidades(Integer idEspecialidad) {
        return List.of();
    }

    @Override
    public EspecialidadResponse crearEspecialidad(EspecialidadRequest dto) {
        return null;
    }

    @Override
    public EspecialidadResponse editarEspecialidad(Integer idEspecialidad, EspecialidadRequest dto) {
        return null;
    }

    @Override
    public EspecialidadResponse eliminarEspecialidad(Integer idEspecialidad) {
        return null;
    }

    @Override
    public EspecialidadResponse obtenerEspecialidad(Integer idEspecialidad) {
        return null;
    }


    // --- Metodos de apoyo ---

    private void asignarRolRecepcionista(Usuario usuarioCreado) {
        usuarioService.addRolRecepcioniste(usuarioCreado);
    }

    private AuthResponse crearAuthResponse(Recepcionista recepcionista) {
        Usuario usuario = recepcionista.getEmpleado().getPersona().getUsuario();
        String token = jwtService.getToken(usuario);
        return AuthResponse.builder().token(token).build();
    }




}

