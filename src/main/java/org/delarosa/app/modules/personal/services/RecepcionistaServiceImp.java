package org.delarosa.app.modules.personal.services;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.personal.dtos.*;
import org.delarosa.app.modules.personal.entities.Consultorio;
import org.delarosa.app.modules.personal.entities.Empleado;
import org.delarosa.app.modules.personal.entities.Especialidad;
import org.delarosa.app.modules.personal.entities.Recepcionista;
import org.delarosa.app.modules.personal.exceptions.ConsultorioAsignadoException;
import org.delarosa.app.modules.personal.exceptions.EspecialidadAsignadaException;
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
import java.util.Optional;

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
        if (consultorioRepo.findByNumero(dto.numeroConsultorio()).isPresent()) {
            throw new IllegalStateException("El consultorio ya existe");
        }

        Consultorio consultorio = new Consultorio();
        consultorio.setNumero(dto.numeroConsultorio());

        return mapearConsultorioAResponse(consultorioRepo.save(consultorio));
    }

    @Override
    public ConsultorioResponse obtenerConsultorio(Integer idConsultorio) {
        Consultorio consultorio = obtenerConsultorioPorId(idConsultorio);
        return mapearConsultorioAResponse(consultorio);
    }

    @Override
    public ConsultorioResponse editarConsultorio(Integer idConsultorio, ConsultorioRequest dto) {
        Consultorio consultorio = obtenerConsultorioPorId(idConsultorio);
        consultorio.setNumero(dto.numeroConsultorio());
        return mapearConsultorioAResponse(consultorioRepo.save(consultorio));
    }


    @Override
    @Transactional
    public ConsultorioResponse eliminarConsultorio(Integer idConsultorio) {

        // 1️⃣ Obtener consultorio (ya lo tienes)
        Consultorio consultorio = obtenerConsultorioPorId(idConsultorio);

        // 2️⃣ VALIDACIÓN DE NEGOCIO
        if (consultorio.getDoctores() != null && !consultorio.getDoctores().isEmpty()) {
            throw new ConsultorioAsignadoException(
                    "No se puede eliminar el consultorio porque tiene doctores asignados"
            );
        }

        // 3️⃣ Eliminar
        consultorioRepo.delete(consultorio);

        // 4️⃣ Respuesta (opcional, pero válido)
        return mapearConsultorioAResponse(consultorio);
    }

    @Override
    public List<ConsultorioResponse> listarConsultorios() {
        return consultorioRepo.findAll()
                .stream()
                .map(this::mapearConsultorioAResponse)
                .toList();
    }

    @Override
    public List<EspecialidadResponse> listarEspecialidades() {
        return especialidadRepo.findAll()
                .stream()
                .map(this::mapearEspecialidadAResponse)
                .toList();
    }

    @Override
    public EspecialidadResponse crearEspecialidad(EspecialidadRequest dto) {
        Especialidad especialidad = new Especialidad();
        especialidad.setNombre(dto.nombre());
        especialidad.setCosto(dto.costo());
        return mapearEspecialidadAResponse(especialidadRepo.save(especialidad));
    }

    @Override
    public EspecialidadResponse editarEspecialidad(Integer idEspecialidad, EspecialidadRequest dto) {
        Especialidad especialidad = obtenerEspecialidadPorId(idEspecialidad);
        especialidad.setNombre(dto.nombre());
        especialidad.setCosto(dto.costo());
        return mapearEspecialidadAResponse(especialidadRepo.save(especialidad));
    }
    @Override
    @Transactional
    public EspecialidadResponse eliminarEspecialidad(Integer idEspecialidad) {

        // 1️⃣ Obtener especialidad
        Especialidad especialidad = obtenerEspecialidadPorId(idEspecialidad);

        // 2️⃣ Validación de negocio
        if (especialidad.getDoctores() != null && !especialidad.getDoctores().isEmpty()) {
            throw new EspecialidadAsignadaException(
                    "No se puede eliminar la especialidad porque tiene doctores asignados"
            );
        }

        // 3️⃣ Eliminar
        especialidadRepo.delete(especialidad);

        // 4️⃣ Respuesta
        return mapearEspecialidadAResponse(especialidad);
    }


    @Override
    public EspecialidadResponse obtenerEspecialidad(Integer idEspecialidad) {
        return mapearEspecialidadAResponse(obtenerEspecialidadPorId(idEspecialidad));
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


    private Consultorio obtenerConsultorioPorId(Integer id) {
        return consultorioRepo.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException(
                        "Consultorio no encontrado"));
    }

    private Especialidad obtenerEspecialidadPorId(Integer id) {
        return especialidadRepo.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException(
                        "Especialidad no encontrada"));
    }

    private ConsultorioResponse mapearConsultorioAResponse(Consultorio consultorio) {
        return new ConsultorioResponse(consultorio.getIdConsultorio(),consultorio.getNumero());
    }

    private EspecialidadResponse mapearEspecialidadAResponse(Especialidad e) {
        return new EspecialidadResponse(e.getIdEspecialidad(), e.getNombre(),e.getCosto());
    }


}

