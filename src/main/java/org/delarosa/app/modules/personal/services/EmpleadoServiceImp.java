package org.delarosa.app.modules.personal.services;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.general.enums.Dia;
import org.delarosa.app.modules.personal.dtos.RegistroEmpleadoRequest;
import org.delarosa.app.modules.personal.entities.Empleado;
import org.delarosa.app.modules.personal.entities.HorarioEmpleado;
import org.delarosa.app.modules.personal.repositories.EmpleadoRepository;
import org.delarosa.app.modules.personal.repositories.HorarioEmpleadoRepository;
import org.delarosa.app.modules.security.entities.Usuario;
import org.delarosa.app.modules.security.services.UsuarioService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpleadoServiceImp implements EmpleadoService {

    private final UsuarioService usuarioService;
    private final EmpleadoRepository empleadoRepo;
    private final HorarioEmpleadoRepository horarioEmpleadoRepo;

    //  Crear y guardar entidad empleado
    @Override
    public Empleado crearEmpleado(RegistroEmpleadoRequest registroEmpleadoRequest) {
        Usuario usuario = usuarioService.crearUsuario(registroEmpleadoRequest.registroPersonaRequest(), registroEmpleadoRequest.registroUsuarioRequest());
        Empleado nvo = new Empleado();
        nvo.setPersona(usuario.getPersona());
        nvo.setSalario(registroEmpleadoRequest.salario());
        return empleadoRepo.save(nvo);
    }
    //Obtener Horarios de Empleado
    @Override
    public List<HorarioEmpleado> obtenerHorariosDeEmpleadoByIdEmpleado(Integer idEmpleado) {
        return horarioEmpleadoRepo.findByEmpleadoIdEmpleado(idEmpleado);
    }

    //Obtener Días Laborales de Empleado
    @Override
    public List<Dia> obtenerDiasLaboralesByIdEmpleado(Integer idEmpleado) {
        return obtenerHorariosDeEmpleadoByIdEmpleado(idEmpleado).stream().map(HorarioEmpleado::getDia).toList();
    }


}
