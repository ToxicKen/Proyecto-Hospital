package org.delarosa.app.modules.personal.services;

import org.delarosa.app.modules.personal.dtos.RegistroDoctorRequest;
import org.delarosa.app.modules.personal.entities.Doctor;
import org.delarosa.app.modules.security.dto.AuthResponse;

public interface DoctorService {
    AuthResponse registrarDoctor(RegistroDoctorRequest registroDoctorRequest);

    Doctor crearDoctor(RegistroDoctorRequest registroDoctorRequest);

    Doctor obtenerDoctorById(Integer id);

}
