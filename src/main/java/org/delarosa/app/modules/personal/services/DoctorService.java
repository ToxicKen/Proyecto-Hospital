package org.delarosa.app.modules.personal.services;

import org.delarosa.app.modules.clinico.dtos.EspecialidadDTO;
import org.delarosa.app.modules.personal.dtos.DoctorDatosCompletosResponse;
import org.delarosa.app.modules.personal.dtos.DoctorDatosResponse;
import org.delarosa.app.modules.personal.dtos.RegistroDoctorRequest;
import org.delarosa.app.modules.personal.entities.Doctor;
import org.delarosa.app.modules.security.dto.AuthResponse;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface DoctorService {
    AuthResponse registrarDoctor(RegistroDoctorRequest registroDoctorRequest);

    Doctor crearDoctor(RegistroDoctorRequest registroDoctorRequest);

    Doctor obtenerDoctorById(Integer id);

    List<EspecialidadDTO> obtenerEspecialidades();

    List<DoctorDatosResponse> obtenerDoctoresByEspecialidadId(Integer idEspecialidad);

    List<LocalDate> obtenerFechasDisponiblesByDoctorId(Integer idDoctor);

    List<LocalTime> obtenerHorasByDoctorYFecha(Integer idDoctor, LocalDate dia);

    DoctorDatosCompletosResponse obtenerDatosDoctor(String email);

    Doctor obtenerDoctorByCorreo(String correo);

    void darDeBajaDoctor(Integer idDoctor);

}
