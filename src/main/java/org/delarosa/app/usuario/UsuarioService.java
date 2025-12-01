package org.delarosa.app.usuario;

import org.delarosa.app.modules.security.entity.Usuario;
import org.delarosa.app.persona.PersonaDTO;
import org.delarosa.app.modules.security.dto.AuthResponse;

public interface UsuarioService {

    Usuario crearUsuario(PersonaDTO persona, UsuarioDTO usuarioDTO);
    AuthResponse loginUsuario(LoginDTO loginDTO);
    void addRolPaciente(Usuario usuario);
    void addRolRecepcioniste(Usuario usuario);
    void addRolDoctor(Usuario usuario);
    UsuarioDTO mapearUsuario(Usuario usuario);



}
