package org.delarosa.app.modules.security.services;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.security.dto.LoginRequest;
import org.delarosa.app.modules.security.dto.RegistroUsuarioRequest;
import org.delarosa.app.modules.security.entities.Rol;
import org.delarosa.app.modules.security.entities.Usuario;
import org.delarosa.app.modules.security.enums.NombreRol;
import org.delarosa.app.modules.security.repositories.RolRepository;
import org.delarosa.app.modules.security.repositories.UsuarioRepository;
import org.delarosa.app.modules.general.entities.Persona;
import org.delarosa.app.modules.general.dtos.RegistroPersonaRequest;
import org.delarosa.app.modules.general.services.PersonaService;
import org.delarosa.app.modules.security.dto.AuthResponse;
import org.delarosa.app.modules.security.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UsuarioServiceImp implements UsuarioService {

    private final PersonaService personaService;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepo;
    private final RolRepository rolRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    // --- Autenticación (Login) ---

    @Override
    public AuthResponse loginUsuario(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.correoElectronico(), loginRequest.contrasenia()));
        UserDetails user = usuarioRepo.findByCorreoElectronico(loginRequest.correoElectronico()).orElseThrow();
        String token = jwtService.getToken(user);
        System.out.println("Token: " + token);
        return AuthResponse.builder().token(token).build();
    }

    // --- Registro y Creación ---

    @Override
    public Usuario crearUsuario(RegistroPersonaRequest persona, RegistroUsuarioRequest registroUsuarioRequest) {
        System.out.println(registroUsuarioRequest.correoElectronico());
        verificarCorreoDuplicado(registroUsuarioRequest.correoElectronico());
    
        Persona personaCreada = personaService.crearPersona(persona);
        Usuario nvoUsuario = crearEntidadUsuario(personaCreada, registroUsuarioRequest);
        vincularUsuarioPersona(nvoUsuario, personaCreada);
        return usuarioRepo.save(nvoUsuario);
    }

    // --- Gestion de Roles ---

    @Override
    public void addRolDoctor(Usuario usuario) {
        Rol rolDoctor = buscarOCrearRol(NombreRol.ROLE_DOCTOR);
        usuario.addRol(rolDoctor);
        usuarioRepo.save(usuario);
    }

    @Override
    public void addRolPaciente(Usuario usuario) {
        Rol rolDoctor = buscarOCrearRol(NombreRol.ROLE_PACIENTE);
        usuario.addRol(rolDoctor);
        usuarioRepo.save(usuario);
    }

    @Override
    public void addRolRecepcioniste(Usuario usuario) {
        Rol rolDoctor = buscarOCrearRol(NombreRol.ROLE_RECEPCIONISTA);
        usuario.addRol(rolDoctor);
        usuarioRepo.save(usuario);
    }

    // --- Métodos de ayuda xd ---

    private void verificarCorreoDuplicado(String correoElectronico) {
            if(usuarioRepo.findByCorreoElectronico(correoElectronico).isPresent()) {
                throw  new IllegalStateException("Ya existe un usuario con el correo: " + correoElectronico);
            }
    }


    private Usuario crearEntidadUsuario(Persona persona, RegistroUsuarioRequest dto) {
        return Usuario.builder()
                .correoElectronico(dto.correoElectronico())
                .contrasenia(passwordEncoder.encode(dto.contrasenia()))
                .persona(persona)
                .build();
    }

    private void vincularUsuarioPersona(Usuario usuario, Persona persona) {
        persona.setUsuario(usuario);
    }


    private Rol buscarOCrearRol(NombreRol enumRol) {
        return rolRepo.findByNombre(enumRol)
                .orElseGet(() -> {
                    Rol nuevoRol = Rol.builder()
                            .nombre(enumRol)
                            .build();
                    return rolRepo.save(nuevoRol);
                });
    }

    public RegistroUsuarioRequest mapearUsuario(Usuario usuario) {
        return new RegistroUsuarioRequest(usuario.getUsername(), "");
    }

}
