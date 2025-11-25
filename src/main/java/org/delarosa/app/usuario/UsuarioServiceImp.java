package org.delarosa.app.usuario;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.persona.Persona;
import org.delarosa.app.persona.PersonaDTO;
import org.delarosa.app.persona.PersonaService;
import org.delarosa.app.security.auth.AuthResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImp implements UsuarioService {
    private final PersonaService personaService;
    private final UsuarioRepository usuarioRepo;

    @Override
    public Usuario crearUsuario(PersonaDTO persona, UsuarioDTO usuarioDTO){
        Persona personaCreada = personaService.crearPersona(persona);
        Usuario nvoUsuario = crearEntidadUsuario(personaCreada, usuarioDTO);
       vincularUsuarioPersona(nvoUsuario,personaCreada);
       return usuarioRepo.save(nvoUsuario);
    }
    private Usuario crearEntidadUsuario(Persona persona,UsuarioDTO dto){
        return Usuario.builder()
                .correoElectronico(dto.correoElectronico())
                .contrasenia(dto.contrasenia())
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
}
