package org.delarosa.app.empleado;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.security.entities.Usuario;
import org.delarosa.app.usuario.UsuarioService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmpleadoServiceImp implements  EmpleadoService {
    private final UsuarioService usuarioService;
    private final EmpleadoRepository empleadoRepo;
    @Override
    public Empleado crearEmpleado(EmpleadoDTO empleadoDTO) {
        Usuario usuario = usuarioService.crearUsuario(empleadoDTO.personaDTO(),empleadoDTO.registroUsuarioRequest());
        Empleado nvo =  new Empleado();
        nvo.setPersona(usuario.getPersona());
        nvo.setSalario(empleadoDTO.salario());
        return empleadoRepo.save(nvo);
    }




}
