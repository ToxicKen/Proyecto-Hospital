package org.delarosa.app.modules.personal.services;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.personal.dtos.RegistroEmpleadoRequest;
import org.delarosa.app.modules.personal.entities.Empleado;
import org.delarosa.app.modules.personal.repositories.EmpleadoRepository;
import org.delarosa.app.modules.security.entities.Usuario;
import org.delarosa.app.modules.security.services.UsuarioService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmpleadoServiceImp implements EmpleadoService {

    private final UsuarioService usuarioService;
    private final EmpleadoRepository empleadoRepo;

    @Override
    public Empleado crearEmpleado(RegistroEmpleadoRequest registroEmpleadoRequest) {
        Usuario usuario = usuarioService.crearUsuario(registroEmpleadoRequest.registroPersonaRequest(), registroEmpleadoRequest.registroUsuarioRequest());
        Empleado nvo = new Empleado();
        nvo.setPersona(usuario.getPersona());
        nvo.setSalario(registroEmpleadoRequest.salario());
        return empleadoRepo.save(nvo);
    }


}
