package org.delarosa.app.usuario;

import org.delarosa.app.persona.Persona;
import org.delarosa.app.persona.PersonaDTO;
import org.delarosa.app.security.auth.AuthResponse;

public interface UsuarioService {

    Usuario crearUsuario(PersonaDTO persona, UsuarioDTO usuarioDTO);
    AuthResponse loginUsuario(Usuario usuario);
    void addRolPaciente(Usuario usuario);
    void addRolRecepcioniste(Usuario usuario);
    void addRolDoctor(Usuario usuario);

}
