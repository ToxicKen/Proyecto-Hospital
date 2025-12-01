package org.delarosa.app.empleado.recepcionista;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.empleado.Empleado;
import org.delarosa.app.empleado.EmpleadoDTO;
import org.delarosa.app.empleado.EmpleadoService;
import org.delarosa.app.modules.security.dto.AuthResponse;
import org.delarosa.app.modules.security.jwt.JwtService;
import org.delarosa.app.modules.security.entities.Usuario;
import org.delarosa.app.usuario.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecepcionistaServiceImp implements RecepcionistaService {
    private final EmpleadoService empleadoService;
    private final RecepcionistaRepository recepcionistaRepo;
    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    @Transactional
    @Override
    public AuthResponse registrarRecepcionista(EmpleadoDTO empleadoDTO) {
        Recepcionista recepcionista = crearRecepcionista(empleadoDTO);
        return crearAuthResponse(recepcionista);
    }

    @Override
    public Recepcionista crearRecepcionista(EmpleadoDTO empleadoDTO) {
        Empleado empleado = empleadoService.crearEmpleado(empleadoDTO);
        asignarRolRecepcionista(empleado.getPersona().getUsuario());
        Recepcionista recepcionista = new Recepcionista();
        recepcionista.setEmpleado(empleado);
        return recepcionistaRepo.save(recepcionista);
    }


    private void asignarRolRecepcionista(Usuario usuarioCreado) {
        usuarioService.addRolRecepcioniste(usuarioCreado);
    }

    private AuthResponse crearAuthResponse(Recepcionista recepcionista) {
        Usuario usuario = recepcionista.getEmpleado().getPersona().getUsuario();
        String token = jwtService.getToken(usuario);
        return AuthResponse.builder().token(jwtService.getToken(usuario)).build();
    }



}

