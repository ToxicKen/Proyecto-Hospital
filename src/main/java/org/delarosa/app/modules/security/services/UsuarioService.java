package org.delarosa.app.modules.security.services;


import org.delarosa.app.modules.security.dto.LoginRequest;
import org.delarosa.app.modules.security.dto.RegistroUsuarioRequest;
import org.delarosa.app.modules.security.entities.Usuario;
import org.delarosa.app.modules.general.dtos.RegistroPersonaRequest;
import org.delarosa.app.modules.security.dto.AuthResponse;

public interface UsuarioService {

    Usuario crearUsuario(RegistroPersonaRequest persona, RegistroUsuarioRequest registroUsuarioRequest);

    RegistroUsuarioRequest mapearUsuario(Usuario usuario);

    AuthResponse loginUsuario(LoginRequest loginRequest);

    void addRolPaciente(Usuario usuario);

    void addRolRecepcioniste(Usuario usuario);

    void addRolDoctor(Usuario usuario);

}
