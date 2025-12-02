package org.delarosa.app.modules.personal.services;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.personal.entities.Empleado;
import org.delarosa.app.modules.personal.dtos.RegistroEmpleadoRequest;
import org.delarosa.app.modules.personal.entities.Recepcionista;
import org.delarosa.app.modules.personal.repositories.RecepcionistaRepository;
import org.delarosa.app.modules.security.dto.AuthResponse;
import org.delarosa.app.modules.security.jwt.JwtService;
import org.delarosa.app.modules.security.entities.Usuario;
import org.delarosa.app.modules.security.services.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecepcionistaServiceImp implements RecepcionistaService {

    private final EmpleadoService empleadoService;
    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    private final RecepcionistaRepository recepcionistaRepo;

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

