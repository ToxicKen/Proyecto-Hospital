package org.delarosa.app.modules.personal.services;

import org.delarosa.app.modules.personal.dtos.*;
import org.delarosa.app.modules.personal.entities.Recepcionista;
import org.delarosa.app.modules.security.dto.AuthResponse;

import java.util.List;

public interface RecepcionistaService {
    AuthResponse registrarRecepcionista(RegistroEmpleadoRequest registroEmpleadoRequest);

    Recepcionista crearRecepcionista(RegistroEmpleadoRequest registroEmpleadoRequest);

    ConsultorioResponse crearConsultorio(ConsultorioRequest dto);
    ConsultorioResponse obtenerConsultorio(Integer idConsultorio);
    ConsultorioResponse editarConsultorio(Integer idConsultorio,ConsultorioRequest dto);
    ConsultorioResponse eliminarConsultorio(Integer idConsultorio);
    List<ConsultorioResponse> listarConsultorios();

    List<EspecialidadResponse> listarEspecialidades();
    EspecialidadResponse crearEspecialidad(EspecialidadRequest dto);
    EspecialidadResponse editarEspecialidad(Integer idEspecialidad,EspecialidadRequest dto);
    EspecialidadResponse eliminarEspecialidad(Integer idEspecialidad);
    EspecialidadResponse obtenerEspecialidad(Integer idEspecialidad);




}
