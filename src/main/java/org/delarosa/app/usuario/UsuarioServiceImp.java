package org.delarosa.app.usuario;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.paciente.Paciente;
import org.delarosa.app.persona.Persona;
import org.delarosa.app.persona.PersonaDTO;
import org.delarosa.app.persona.PersonaService;
import org.delarosa.app.security.auth.AuthResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImp implements UsuarioService {
    private final PersonaService personaService;
    private final UsuarioRepository usuarioRepo;
    private final RolRepository rolRepo;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Usuario crearUsuario(PersonaDTO persona, UsuarioDTO usuarioDTO){
        Optional<Usuario> existe = usuarioRepo.findByCorreoElectronico(usuarioDTO.correoElectronico());
        if(existe.isPresent()){
            throw new IllegalStateException("Ya existe una persona con ese correo: " + usuarioDTO.correoElectronico());
        }
        Persona personaCreada = personaService.crearPersona(persona);
        Usuario nvoUsuario = crearEntidadUsuario(personaCreada, usuarioDTO);
       vincularUsuarioPersona(nvoUsuario,personaCreada);
       return usuarioRepo.save(nvoUsuario);
    }

    private Usuario crearEntidadUsuario(Persona persona,UsuarioDTO dto){
        return Usuario.builder()
                .correoElectronico(dto.correoElectronico())
                .contrasenia(passwordEncoder.encode(dto.contrasenia()))
                .persona(persona)
                .build();
    }

    private void vincularUsuarioPersona(Usuario usuario, Persona persona) {
        persona.setUsuario(usuario);
    }

    @Override
    public AuthResponse loginUsuario(Usuario usuario) {
        return null;
    }

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


    private Rol buscarOCrearRol(NombreRol enumRol) {
        return rolRepo.findByNombre(enumRol)
                .orElseGet(() -> {
                    Rol nuevoRol = Rol.builder()
                            .nombre(enumRol)
                            .build();
                    return rolRepo.save(nuevoRol);
                });
    }

}
