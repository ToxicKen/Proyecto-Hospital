package org.delarosa.app.modules.personal.services;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.general.enums.Dia;
import org.delarosa.app.modules.personal.dtos.HorarioEmpleadoDTO;
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
import java.util.Optional;

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
        return obtenerHorariosDeEmpleadoByIdEmpleado(idEmpleado).stream().map(HorarioEmpleado::getDiaSemana).toList();
    }

    //Registrar un nuevo Horario
    @Override
    public void agregarHorario(Integer idEmpleado, HorarioEmpleadoDTO dto) {

        Empleado empleado = empleadoRepo.findById(idEmpleado)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        List<HorarioEmpleado> existentes =
                horarioEmpleadoRepo.findByEmpleadoIdEmpleado(idEmpleado);

        // 1. Obtener el día del nuevo horario
        Dia nuevoDia = Dia.valueOf(dto.dia());

        // 2. Buscar si ya existe un horario en ese mismo día
        Optional<HorarioEmpleado> horarioMismoDia = existentes.stream()
                .filter(h -> h.getDiaSemana() == nuevoDia)
                .findFirst();

        // 3. Si existe un horario ese día, revisar traslape
        if (horarioMismoDia.isPresent()) {
            HorarioEmpleado h = horarioMismoDia.get();

            boolean traslape = horasTraslapan(
                    h.getHrsInicio(),
                    h.getHrsFin(),
                    dto.hraEntrada(),
                    dto.hraSalida()
            );

            if (traslape) {
                throw new RuntimeException("El horario se traslapa con uno existente ese mismo día");
            } else {
                throw new RuntimeException("Ese día ya tiene un horario registrado");
            }
        }

        // 4. Si NO existe horario ese día, simplemente guardamos
        HorarioEmpleado nuevo = new HorarioEmpleado();
        nuevo.setEmpleado(empleado);
        nuevo.setDiaSemana(nuevoDia);
        nuevo.setHrsInicio(dto.hraEntrada());
        nuevo.setHrsFin(dto.hraSalida());

        horarioEmpleadoRepo.save(nuevo);
    }

    private boolean horasTraslapan(
            LocalTime inicio1, LocalTime fin1,
            LocalTime inicio2, LocalTime fin2
    ) {
        return !(fin1.isBefore(inicio2) || fin2.isBefore(inicio1));
    }

}


